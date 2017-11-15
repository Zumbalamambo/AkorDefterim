package com.cnbcyln.app.akordefterim.util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cnbcyln.app.akordefterim.AnaEkran;
import com.cnbcyln.app.akordefterim.Giris;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfIstekler;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"WrongConstant", "deprecation", "MismatchedQueryAndUpdateOfStringBuilder"})
@SuppressLint("HardwareIds")
public class MqttService extends Service implements MqttCallback {
    AkorDefterimSys AkorDefterimSys;
    Context context;
    Veritabani Veritabani;
    Intent Intent_AnaEkran;
    MqttClient mqttClient;
    SharedPreferences sharedPref;
    Timer mTimer;
    TimerTask timerTask;

    String MqttBrokerAdres, MqttBrokerKullaniciAdi, MqttBrokerSifre;
    int qos;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        AkorDefterimSys = new AkorDefterimSys(context);
        Veritabani = new Veritabani(this);
        Intent_AnaEkran = new Intent("com.cnbcyln.app.akordefterim.AnaEkran");
        registerReceiver(broadcastreceiver, new IntentFilter("com.cnbcyln.app.akordefterim.util.MqttService"));

        sharedPref = getApplicationContext().getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

        if(AkorDefterimSys.InternetErisimKontrolu(context)) {
            new MqttBaglantiBilgisiAl().execute();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();

            if(mqttClient.isConnected()) {
                mqttClient.disconnect();
                mqttClient.close();
            }

            unregisterReceiver(broadcastreceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MqttBaglantiBilgisiAl extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... parametre) {
            String sonuc = null;

            try {
                URL url = new URL("http://akordefterim.cbcapp.net/araclar/mqtt_broker_bilgileri.txt");

                URLConnection urlConnection = url.openConnection();
                urlConnection.setUseCaches(false);

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                reader.close();

                sonuc = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sonuc;
        }

        @Override
        protected void onPostExecute(String Sonuc) {
            try {
                if(Sonuc != null) {
                    JSONObject JSONGelenVeri = new JSONObject(Sonuc);

                    MqttBrokerAdres = JSONGelenVeri.getString("MqttBrokerAdres");
                    MqttBrokerKullaniciAdi = JSONGelenVeri.getString("MqttBrokerKullaniciAdi");
                    MqttBrokerSifre = JSONGelenVeri.getString("MqttBrokerSifre");
                    qos = JSONGelenVeri.getInt("qos");

                    MqttBaglantisiYap();
                } else new MqttBaglantiBilgisiAl().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... Deger) {
            super.onProgressUpdate(Deger);
        }

        @Override
        protected void onCancelled(String Sonuc) {
            super.onCancelled(Sonuc);
        }
    }

    private void MqttBaglantisiYap() {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            //mqttClient = new MqttClient("tcp://46.197.112.15:1883", MqttClient.generateClientId(), persistence);
            mqttClient = new MqttClient(MqttBrokerAdres, sharedPref.getString("prefHesapEmailKullaniciAdi", ""), persistence);
            mqttClient.setCallback(this);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setConnectionTimeout(10);
            //connOpts.setAutomaticReconnect(true);
            connOpts.setUserName(MqttBrokerKullaniciAdi);
            connOpts.setPassword(MqttBrokerSifre.toCharArray());

            mqttClient.connect(connOpts);

            //mqttClient.subscribe(getApplicationContext().getPackageName() + "/akordefterim", qos); // Genel dinleme kanalı
            mqttClient.subscribe(getApplicationContext().getPackageName() + "/akordefterim/hesapid/" + sharedPref.getString("prefHesapID", ""), qos); // Sadece kendi kanalı

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

            mTimer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.e("Log", "MqttPushNotifyService çalışıyor..");

                    if(!mqttClient.isConnected()) {
                        Log.e("Log", "Mqtt bağlantısı koptu. İnternet kontrol ediliyor..");

                        if (AkorDefterimSys.InternetErisimKontrolu(context)) { //İnternet kontrolü yap
                            Log.e("Log", "İnternet bağlantısı sağlandı. Mqtt bağlantı bilgisi alınıyor..");

                            mTimer.cancel();
                            timerTask.cancel();

                            new MqttBaglantiBilgisiAl().execute();
                        }
                    }
                }
            };

            mTimer.schedule(timerTask, 2000, 5 * 1000);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        if(!mqttClient.isConnected()) {
            if (AkorDefterimSys.InternetErisimKontrolu(context)) { //İnternet kontrolü yap
                mTimer.cancel();
                timerTask.cancel();

                new MqttBaglantiBilgisiAl().execute();
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage gelenmesaj) throws Exception {
        Intent myIntent;
        Context context = getApplicationContext();

        JSONObject JSONGelenVeri = new JSONObject(new JSONArray(new String(gelenmesaj.getPayload())).getString(0));

        switch (JSONGelenVeri.getString("Islem")) {
            case "Bildirim":
                if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Normal")) {
                    if (sharedPref.getString("prefHesapEmailKullaniciAdi", "").equals("") && sharedPref.getString("prefHesapSifreSha1", "").equals("")) {
                        myIntent = new Intent(context, Giris.class);
                    } else {
                        myIntent = new Intent(context, AnaEkran.class);
                    }
                } else myIntent = new Intent(context, Giris.class);

                AkorDefterimSys.NotifyGoster(myIntent, JSONGelenVeri.getString("Baslik"), JSONGelenVeri.getString("Icerik"), "", -1, JSONGelenVeri.getString("Icerik"), JSONGelenVeri.getString("Baslik"), JSONGelenVeri.getString("Icerik"), "", true);

                break;
            case "Guncelleme":
                myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));

                AkorDefterimSys.NotifyGoster(myIntent, JSONGelenVeri.getString("Baslik"), JSONGelenVeri.getString("Icerik"), "", -1, JSONGelenVeri.getString("Icerik"), JSONGelenVeri.getString("Baslik"), JSONGelenVeri.getString("Icerik"), "", true);
                break;
            case "PushHesapCikis":
                String AndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                if(!JSONGelenVeri.getString("AndroidID").equals(AndroidID)) { // Sisteme yeni giriş yapan AndroidID, eski AndroidID'e eşit değilse, eski cihaza çıkış yap
                    Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"PushHesapCikis\"}");
                    this.sendBroadcast(Intent_AnaEkran);

                    myIntent = new Intent(context, AnaEkran.class);
                    AkorDefterimSys.NotifyGoster(myIntent, getString(R.string.uygulama_adi), getString(R.string.farkli_bir_cihazdan_oturum_acildi), "", -1, getString(R.string.farkli_bir_cihazdan_oturum_acildi), getString(R.string.uygulama_adi), getString(R.string.farkli_bir_cihazdan_oturum_acildi), "", true);
                }

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

                break;
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
                    case "PushHesapCikis":
                        if(mqttClient != null) {
                            String MqttJSONDATA = "[{\"Islem\":\"" + JSONGelenVeri.getString("Islem") + "\",\"AndroidID\":\"" + JSONGelenVeri.getString("AndroidID") + "\"}]";

                            MqttMessage message = new MqttMessage(MqttJSONDATA.getBytes());
                            message.setQos(qos);

                            mqttClient.publish(getApplicationContext().getPackageName() + "/akordefterim/hesapid/" + JSONGelenVeri.getString("HesapID"), message);
                        }

                        break;
                    case "IstekBudur_LokasyonBilgisi":
                        if(mqttClient != null) {
                            String MqttJSONDATA = "{\"Islem\":\"" + JSONGelenVeri.getString("Islem") + "\",\"HesapID\":\"" + JSONGelenVeri.getString("HesapID") + "\", \"HesapAdSoyad\":\"" + JSONGelenVeri.getString("HesapAdSoyad") + "\", \"Koordinat\":\"" + JSONGelenVeri.getString("Koordinat") + "\", \"ProfilResimUrl\":\"" + JSONGelenVeri.getString("ProfilResimUrl") + "\", \"CanliIstek\":\"" + JSONGelenVeri.getString("CanliIstek") + "\"}";

                            MqttMessage message = new MqttMessage(MqttJSONDATA.getBytes());
                            message.setQos(qos);

                            mqttClient.publish(getApplicationContext().getPackageName() + "/istekbudur", message);
                        }

                        break;
                }
            } catch (MqttException | JSONException e) {
                e.printStackTrace();
            }
        }
    };
}