package com.cnbcyln.app.akordefterim.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Retrofit.Interface.RetrofitInterface;
import com.cnbcyln.app.akordefterim.Retrofit.Network.RetrofitServiceGenerator;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfMqttBroker;
import com.cnbcyln.app.akordefterim.SplashEkran;
import com.google.firebase.iid.FirebaseInstanceId;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"WrongConstant", "deprecation", "MismatchedQueryAndUpdateOfStringBuilder"})
@SuppressLint("HardwareIds")
public class MqttService extends Service implements MqttCallback {
    AkorDefterimSys AkorDefterimSys;
    Context context;
    //Veritabani Veritabani;
    Intent mIntent;
    MqttClient mMqttClient;
    MqttCallback mMqttCallback;
    SharedPreferences sharedPref;
    Timer mMqttBrokerTimer;

    String MqttBrokerAdres, MqttBrokerKullaniciAdi;
    char[] MqttBrokerSifre;
    int MqttBrokerQos;

    static int StandartNotifyID = 0;
    static int GooglePlayGuncellemeID = 0;
    static int YeniSarkiNotifyID = 0;
    static int MqttBroker_BaglantiKontrolAraligi = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        mMqttCallback = this;
        AkorDefterimSys = new AkorDefterimSys(context);
        //Veritabani = new Veritabani(this);

        StandartNotifyID = getResources().getInteger(R.integer.StandartNotifyID);
        GooglePlayGuncellemeID = getResources().getInteger(R.integer.GooglePlayGuncellemeID);
        YeniSarkiNotifyID = getResources().getInteger(R.integer.YeniSarkiNotifyID);

        sharedPref = getApplicationContext().getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

        registerReceiver(broadcastreceiver, new IntentFilter("com.cnbcyln.app.akordefterim.util.MqttService"));

        MqttBroker_BaglantiKontrolAraligi = getResources().getInteger(R.integer.MqttBroker_BaglantiKontrolAraligi) * 1000;

        MqttBaglantisiYap();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            if(mMqttBrokerTimer != null) {
                mMqttBrokerTimer.cancel();
                mMqttBrokerTimer = null;
            }

            if(mMqttClient.isConnected()) {
                mMqttClient.disconnect();
                mMqttClient.close();
                mMqttClient = null;
            }

            unregisterReceiver(broadcastreceiver);

            Log.e("MqttService", "Mqtt servisi kapatıldı..");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean InternetErisimKontrolu() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo MobileInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifiInfo != null && MobileInfo != null) {
            if (wifiInfo.isConnected()) {
                return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
            } else if (MobileInfo.isConnected()) {
                return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Boolean GirisYapildiMi() {
        return sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimici") &&
                !sharedPref.getString("prefHesapID", "").equals("") &&
                !sharedPref.getString("prefEPosta", "").equals("") &&
                !sharedPref.getString("prefParolaSHA1", "").equals("");
    }

    private void MqttBaglantisiYap() {
        if(InternetErisimKontrolu()) {
            Log.i("MqttService", "İnternet bağlantısı sağlandı. Mqtt bağlantısı yapılıyor..");

            RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(context, RetrofitInterface.class);

            Call<SnfMqttBroker> snfMqttBrokerCall = retrofitInterface.MqttBrokerBilgisiGetir();
            snfMqttBrokerCall.enqueue(new Callback<SnfMqttBroker>() {
                @Override
                public void onResponse(Call<SnfMqttBroker> call, Response<SnfMqttBroker> response) {
                    if(response.isSuccessful()) {
                        SnfMqttBroker snfMqttBroker = response.body();

                        MqttBrokerAdres = snfMqttBroker.getAdres();
                        MqttBrokerKullaniciAdi = snfMqttBroker.getKullaniciAdi();
                        MqttBrokerSifre = snfMqttBroker.getSifre().toCharArray();
                        MqttBrokerQos = snfMqttBroker.getQos();

                        // getHata'nin false olması durumu hata yok demektir..
                        if(!snfMqttBroker.getHata()) {
                            try {
                                MemoryPersistence persistence = new MemoryPersistence();
                                //mqttClient = new MqttClient("tcp://46.197.112.15:1883", MqttClient.generateClientId(), persistence);
                                mMqttClient = new MqttClient(MqttBrokerAdres, MqttClient.generateClientId(), persistence);
                                mMqttClient.setCallback(mMqttCallback);

                                MqttConnectOptions connOpts = new MqttConnectOptions();
                                connOpts.setCleanSession(true);
                                connOpts.setConnectionTimeout(10);
                                //connOpts.setAutomaticReconnect(true);
                                connOpts.setUserName(MqttBrokerKullaniciAdi);
                                connOpts.setPassword(MqttBrokerSifre);

                                mMqttClient.connect(connOpts);

                                Log.i("MqttService", "Mqtt bağlantısı yapıldı..");

                                mMqttClient.subscribe(getApplicationContext().getPackageName(), MqttBrokerQos); // Genel dinleme kanalı

                                /*if(!sharedPref.getString("prefHesapID", "").equals("")) {
                                    mMqttClient.subscribe(getApplicationContext().getPackageName() + "/" + sharedPref.getString("prefHesapID", ""), MqttBrokerQos); // Sadece kendi kanalı
                                }

                                Log.i("MqttService", "prefHesapID: " + sharedPref.getString("prefHesapID", ""));*/


                                //MqttMessage message = new MqttMessage("A single message from my computer fff".getBytes());
                                //message.setQos(AkorDefterimSys.qos);
                                //mqttClient.publish(getApplicationContext().getPackageName(), message);

                                //Kaynak: http://www.hivemq.com/blog/mqtt-client-library-enyclopedia-paho-android-service
                                //Kaynak: http://stackoverflow.com/questions/22715682/subscribe-and-read-mqtt-message-using-paho

                                /*ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
                                MyThread worker = new MyThread("Thread 1", 3);
                                pool.schedule(worker, 5, TimeUnit.SECONDS);
                                Thread.sleep(20000);
                                pool.shutdown();*/

                                /*mMqttBrokerTimer = new Timer();
                                mMqttBrokerTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Log.d("MqttService", "Mqtt Broker bağlantısı kontrol ediliyor..");

                                        Handler handler = new Handler(Looper.getMainLooper());
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(!mMqttClient.isConnected()) {
                                                    Log.e("Log", "Mqtt bağlantısı koptu. İnternet kontrol ediliyor..");

                                                    if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
                                                        Log.e("Log", "İnternet bağlantısı sağlandı. Mqtt bağlantısı yapılıyor..");

                                                        if(mMqttBrokerTimer != null) {
                                                            mMqttBrokerTimer.cancel();
                                                            mMqttBrokerTimer = null;
                                                        }

                                                        MqttBaglantisiYap();
                                                    }
                                                } else Log.e("MqttService", "Mqtt Broker bağlantısı bağlı ve çalışıyor..");
                                            }
                                        });
                                    }
                                }, 0, MqttBroker_BaglantiKontrolAraligi);*/
                            } catch (MqttException e) {
                                e.printStackTrace();

                                try {
                                    Log.e("MqttService", "Mqtt bağlantısı sağlanamadı. İnternet kontrol ediliyor..");

                                    if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
                                        Log.i("MqttService", "İnternet bağlantısı sağlandı. Yeniden deneniyor..");
                                        Thread.sleep(MqttBroker_BaglantiKontrolAraligi);
                                        MqttBaglantisiYap();
                                    } else {
                                        Log.e("MqttService", "İnternet bağlantısı sağlanamadı. Yeniden deneniyor.. 2");
                                        Thread.sleep(MqttBroker_BaglantiKontrolAraligi);
                                        MqttBaglantisiYap();
                                    }
                                } catch (InterruptedException a) {
                                    a.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                Log.e("MqttService", "Mqtt Broker bilgileri alınırken hata oluştu. Yeniden deneniyor..");
                                Thread.sleep(MqttBroker_BaglantiKontrolAraligi);
                                MqttBaglantisiYap();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SnfMqttBroker> call, Throwable t) {
                    try {
                        Log.e("MqttService", "Mqtt Broker bilgileri alınamadı. Yeniden deneniyor..");
                        Thread.sleep(MqttBroker_BaglantiKontrolAraligi);
                        MqttBaglantisiYap();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            try {
                Log.e("MqttService", "İnternet bağlantısı sağlanamadı. Yeniden deneniyor.. 1");
                Thread.sleep(MqttBroker_BaglantiKontrolAraligi);
                MqttBaglantisiYap();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isServiceRunning(Class Servis) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(Servis.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    private void NotifyGoster(Intent intent, int NotificationID, String NotifyBaslik, String NotifyIcerik, String SubIcerik, int Number, String TickerIcerik, String BigNotifyBaslik, String BigNotifyIcerik, String BigSubIcerik, Boolean Titresim) {
        Uri defaultSoundUri;
        PendingIntent pendingIntent;
        Notification.Builder NotifyBuilder;

        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		/*RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_view);
		remoteViews.setImageViewResource(R.id.NotifyImage, R.mipmap.ic_launcher);

		remoteViews.setTextViewText(R.id.NotifyBaslik, NotifyBaslik);
		remoteViews.setTextViewText(R.id.NotifyIcerik, NotifyIcerik);*/

        NotifyBuilder = new Notification.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(NotifyBaslik)
                .setContentText(NotifyIcerik)
                .setAutoCancel(true)
                .setDefaults(Titresim ? Notification.DEFAULT_ALL : Notification.DEFAULT_LIGHTS)
                .setSound(defaultSoundUri)
                .setLights(Color.WHITE, 1000, 300)
                //.setContent(remoteViews) // Farklı bir layout göstermek istersek..
                .setContentIntent(pendingIntent);


        if(!SubIcerik.equals("")) NotifyBuilder.setSubText(SubIcerik); // Icerigin alt kısmına bir yazı daha ekler..
        if(!TickerIcerik.equals("")) NotifyBuilder.setTicker(TickerIcerik); // Bildirim çubuğuna yazı ekler..
        if(Number != -1) NotifyBuilder.setNumber(Number); // Sol kısıma numara ekler..

        Notification notification = new Notification.BigTextStyle(NotifyBuilder)
                .setBigContentTitle(BigNotifyBaslik)
                .bigText(BigNotifyIcerik)
                .setSummaryText(BigSubIcerik) // Icerigin alt kısmına bir yazı daha ekler..
                .build();

        /** Small Icon'u kaldırmaya yarayan fonksiyon **/
        int smallIconId = context.getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
        if (smallIconId != 0) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                notification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
                notification.bigContentView.setViewVisibility(smallIconId, View.INVISIBLE);
            }
        }
        /** Small Icon'u kaldırmaya yarayan fonksiyon **/

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationID, notification);
    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            if(!mMqttClient.isConnected()) {
                Log.e("MqttService", "Mqtt bağlantısı koptu. İnternet kontrol ediliyor..");

                if (InternetErisimKontrolu()) { //İnternet kontrolü yap
                    Log.i("MqttService", "İnternet bağlantısı sağlandı. Mqtt bağlantısı yapılıyor..");

                    Thread.sleep(MqttBroker_BaglantiKontrolAraligi);

                    MqttBaglantisiYap();
                } else {
                    Log.e("MqttService", "İnternet bağlantısı sağlanamadı. Yeniden deneniyor.. 3");

                    Thread.sleep(MqttBroker_BaglantiKontrolAraligi);

                    MqttBaglantisiYap();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage gelenmesaj) {
        Intent myIntent;

        try {
            final JSONObject JSONGelenVeri = new JSONObject(new String(gelenmesaj.getPayload()));

            switch (JSONGelenVeri.getString("Islem")) {
                case "OnlineDurum": // "com.cnbcyln.app.akordefterim" kanalından gelen mesaj
                    if(mMqttClient != null && mMqttClient.isConnected() && GirisYapildiMi() && sharedPref.getBoolean("prefAppRunning", false) && isServiceRunning(AppRunningControl.class)) {
                        String MqttJSONData = "{\"Islem\":\"OnlineDurum\", \"HesapID\":\"" + sharedPref.getString("prefHesapID", "") + "\", \"SonYapilanIslem_CumleKodu\":\"" + sharedPref.getString("prefSonYapilanIslem_CumleKodu", "") + "\", \"SonYapilanIslem_Param\":" + sharedPref.getString("prefSonYapilanIslem_Param", "") + ", \"Durum\":true}";

                        MqttMessage GidenMesaj = new MqttMessage(MqttJSONData.getBytes());
                        GidenMesaj.setQos(MqttBrokerQos);

                        mMqttClient.publish(getApplicationContext().getPackageName() + "/yonetim/kullanici/onlinedurum", GidenMesaj);
                    }

                    /*Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Mqtt Server'dan Bilgi geldi.", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                    break;
                case "CikisYap": // "com.cnbcyln.app.akordefterim/kullanici/{hesap_id}" kanalından gelen mesaj
                    String FirebaseToken = FirebaseInstanceId.getInstance().getToken();

                    if(JSONGelenVeri.getString("HesapFirebaseToken").equals(FirebaseToken)) { // Sisteme yeni giriş yapan FirebaseToken, cihazda giriş yapmış olan FirebaseToken'a eşit ise, cihazda giriş yapmış olan FirebaseToken'a çıkış yaptır
                        mIntent = new Intent("com.cnbcyln.app.akordefterim." + sharedPref.getString("prefBulunulanEkran", "AnaEkran"));
                        mIntent.putExtra("JSONData", "{\"Islem\":\"CikisYap\"}");
                        this.sendBroadcast(mIntent);
                    }

                    break;
                case "HesapSilindi_CikisYap": // "com.cnbcyln.app.akordefterim/kullanici/{hesap_id}" kanalından gelen mesaj
                    mIntent = new Intent("com.cnbcyln.app.akordefterim." + sharedPref.getString("prefBulunulanEkran", "AnaEkran"));
                    mIntent.putExtra("JSONData", "{\"Islem\":\"HesapSilindi_CikisYap\", \"Tarih\":\"" + JSONGelenVeri.getString("Tarih") + "\"}");
                    this.sendBroadcast(mIntent);

                    break;
                case "HesapBanlandi_CikisYap": // "com.cnbcyln.app.akordefterim/kullanici/{hesap_id}" kanalından gelen mesaj
                    mIntent = new Intent("com.cnbcyln.app.akordefterim." + sharedPref.getString("prefBulunulanEkran", "AnaEkran"));
                    mIntent.putExtra("JSONData", "{\"Islem\":\"HesapBanlandi_CikisYap\", \"Tarih\":\"" + JSONGelenVeri.getString("Tarih") + "\"}");
                    this.sendBroadcast(mIntent);

                    break;
                case "Bildirim": // "com.cnbcyln.app.akordefterim" kanalından gelen mesaj
                    String MesajIcerik = JSONGelenVeri.getString("MesajIcerik");

                    myIntent = new Intent(context, SplashEkran.class);
                    NotifyGoster(myIntent,
                            StandartNotifyID,
                            getString(R.string.uygulama_adi),
                            MesajIcerik,
                            "",
                            -1,
                            MesajIcerik,
                            getString(R.string.uygulama_adi),
                            MesajIcerik,
                            "",
                            true);

                    break;
            /*case "Guncelleme":
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));

                AkorDefterimSys.NotifyGoster(myIntent, 1, JSONGelenVeri.getString("Baslik"), JSONGelenVeri.getString("Icerik"), "", -1, JSONGelenVeri.getString("Icerik"), JSONGelenVeri.getString("Baslik"), JSONGelenVeri.getString("Icerik"), "", true);
                break;
            case "IstekBudur_SarkiIstegi":
                JSONObject JSONSonuc = new JSONObject(Veritabani.ClientIstekKontrol(JSONGelenVeri.getString("ClientID"), JSONGelenVeri.getString("IstekTarihi"), sharedPref.getInt("prefIstekAyarlariClientIstekSuresi", 15)));

                if(JSONSonuc.getInt("Sonuc") == 0 || JSONSonuc.getInt("Sonuc") == 1) {
                    if(Veritabani.IstekEkle(JSONGelenVeri.getInt("SarkiID"), JSONGelenVeri.getString("ClientID"), JSONGelenVeri.getString("ClientAdSoyad"), JSONGelenVeri.getString("ClientIP"), JSONGelenVeri.getString("ClientNot"), JSONGelenVeri.getString("IstekTarihi"))) {
                        if(mqttClient != null) {
                            String MqttJSONDATA = "{\"Islem\":\"IstekBudur_Mesaj\", \"MesajBaslik\":\"İstek yap\", \"Mesaj\":\"İsteğiniz iletildi. Yeni istek için kalan süre: " + AkorDefterimSys.ZamanFormatMMSS(JSONSonuc.getInt("KalanSureSaniye")) + "\", \"MesajTipi\":\"TYPE_SUCCESS\"}";

                            MqttMessage gidenmesaj = new MqttMessage(MqttJSONDATA.getBytes());
                            gidenmesaj.setQos(qos);

                            mqttClient.publish(getApplicationContext().getPackageName() + "/istekbudur/clientid/" + JSONGelenVeri.getString("ClientID"), gidenmesaj);
                        }

                        Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabIstekler_Guncelle\"}");
                        this.sendBroadcast(Intent_AnaEkran);

                        //myIntent = new Intent(context, AnaEkran.class);
                        List<SnfIstekler> snfIstekler = Veritabani.lst_IstekGetir();
                        StringBuilder BigNotifyIcerik = new StringBuilder();

                        for(int i = 0; i <= snfIstekler.size() - 1; i++) {
                            if(i == 3) break;
                            BigNotifyIcerik.append(i + 1).append(". ").append(snfIstekler.get(i).getSanatciAdi()).append(" - ").append(snfIstekler.get(i).getSarkiAdi()).append("\n");
                        }

                        BigNotifyIcerik.delete(BigNotifyIcerik.length() - 1, BigNotifyIcerik.length());

                        if(snfIstekler.size() > 0) {
                            Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"NotifyMesaj\"," +
                                    "\"NotifyBaslik\":\"" + snfIstekler.size() + " yeni şarkı isteği\"," +
                                    "\"NotifyIcerik\":\"" + getString(R.string.uygulama_adi) +"\"," +
                                    "\"SubIcerik\":\"\"," +
                                    "\"Number\":-1," +
                                    "\"TickerIcerik\":\"" + snfIstekler.size() + " yeni şarkı isteği\"," +
                                    "\"BigNotifyBaslik\":\"" + snfIstekler.size() + " yeni şarkı isteği\"," +
                                    "\"BigNotifyIcerik\":\"" + BigNotifyIcerik.toString() + "\"," +
                                    "\"BigSubIcerik\":\"" + ((snfIstekler.size() > 3) ? "+" + String.valueOf(snfIstekler.size() - 3) + " daha" : "") + "\"," +
                                    "\"Titresim\":true}");
                            this.sendBroadcast(Intent_AnaEkran);
                        }


                        //Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"SnackBarMesaj\", \"Mesaj\":\"İstek geldi..\"}");
                        //this.sendBroadcast(Intent_AnaEkran);

                        //Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        //long[] pattern = {0, 200, 100, 200, 100, 200};
                        //vibrator.vibrate(pattern, -1);
                    }
                } else if(JSONSonuc.getInt("Sonuc") == 2) {
                    Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"SnackBarMesaj\", \"Mesaj\":\"Client tarafından istek geldi ancak spam filtresine takıldı..\"}");
                    this.sendBroadcast(Intent_AnaEkran);

                    if(mqttClient != null) {
                        String MqttJSONDATA = "{\"Islem\":\"IstekBudur_Mesaj\", \"MesajBaslik\":\"İstek yap\", \"Mesaj\":\"Daha önce istek yapmışsınız. Yeni istek için kalan süre: " + AkorDefterimSys.ZamanFormatMMSS(JSONSonuc.getInt("KalanSureSaniye")) + "\", \"MesajTipi\":\"TYPE_DANGER\"}";

                        MqttMessage gidenmesaj = new MqttMessage(MqttJSONDATA.getBytes());
                        gidenmesaj.setQos(qos);

                        mqttClient.publish(getApplicationContext().getPackageName() + "/istekbudur/clientid/" + JSONGelenVeri.getString("ClientID"), gidenmesaj);
                    }
                }

                break;*/
            }
        } catch (JSONException | MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String JSON = intent.getStringExtra("JSONData");
                JSONObject JSONGelenVeri = new JSONObject(JSON);

                switch (JSONGelenVeri.getString("Islem")) {
                    case "CikisYap":
                        if(mMqttClient != null && mMqttClient.isConnected()) {
                            String MqttJSONData = "{\"Islem\":\"CikisYap\", \"HesapFirebaseToken\":\"" + JSONGelenVeri.getString("HesapFirebaseToken") + "\"}";

                            MqttMessage message = new MqttMessage(MqttJSONData.getBytes());
                            message.setQos(MqttBrokerQos);

                            mMqttClient.publish(getApplicationContext().getPackageName() + "/kullanici/" + JSONGelenVeri.getString("HesapID"), message);
                        }

                        break;
                    case "Subscribe_HesapKanal":
                        if(mMqttClient != null && mMqttClient.isConnected()) {
                            mMqttClient.subscribe(getApplicationContext().getPackageName() + "/kullanici/" + JSONGelenVeri.getString("HesapID"), MqttBrokerQos); // Sadece kendi kanalına giriş yap
                        }

                        break;
                    case "UnSubscribe_HesapKanal":
                        if(mMqttClient != null && mMqttClient.isConnected()) {
                            mMqttClient.unsubscribe(getApplicationContext().getPackageName() + "/kullanici/" + JSONGelenVeri.getString("HesapID")); // Sadece kendi kanalından çıkış yap
                        }

                        break;
                    case "OnlineDurum": // "com.cnbcyln.app.akordefterim" kanalından gelen mesaj
                        if(mMqttClient != null && mMqttClient.isConnected() && !sharedPref.getBoolean("prefAppRunning", false)) {
                            String MqttJSONData = "{\"Islem\":\"OnlineDurum\", \"HesapID\":\"" + sharedPref.getString("prefHesapID", "") + "\", \"Durum\":false}";

                            MqttMessage GidenMesaj = new MqttMessage(MqttJSONData.getBytes());
                            GidenMesaj.setQos(MqttBrokerQos);

                            mMqttClient.publish(getApplicationContext().getPackageName() + "/yonetim/kullanici/onlinedurum", GidenMesaj);
                        }

                    /*Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Mqtt Server'dan Bilgi geldi.", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                        break;
                    /*case "IstekBudur_LokasyonBilgisi":
                        if(mqttClient != null) {
                            String MqttJSONDATA = "{\"Islem\":\"" + JSONGelenVeri.getString("Islem") + "\",\"HesapID\":\"" + JSONGelenVeri.getString("HesapID") + "\", \"HesapAdSoyad\":\"" + JSONGelenVeri.getString("HesapAdSoyad") + "\", \"Koordinat\":\"" + JSONGelenVeri.getString("Koordinat") + "\", \"ProfilResimUrl\":\"" + JSONGelenVeri.getString("ProfilResimUrl") + "\", \"CanliIstek\":\"" + JSONGelenVeri.getString("CanliIstek") + "\"}";

                            MqttMessage message = new MqttMessage(MqttJSONDATA.getBytes());
                            message.setQos(qos);

                            mqttClient.publish(getApplicationContext().getPackageName() + "/istekbudur", message);
                        }

                        break;*/
                }
            } catch (MqttException | JSONException e) {
                e.printStackTrace();
            }
        }
    };
}