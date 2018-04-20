package com.cnbcyln.app.akordefterim.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Retrofit.Interface.RetrofitInterface;
import com.cnbcyln.app.akordefterim.Retrofit.Network.RetrofitServiceGenerator;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfTarihSaat;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppService extends Service {
    AkorDefterimSys AkorDefterimSys;
    Context context;
    //Intent mIntent;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    Timer mGuncellemeKontrolTimer, mOturumGuncellemeTimer;
    Intent mGuncellemeGooglePlayIntent;

    static int GooglePlayGuncellemeID = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        //AkorDefterimSys = new AkorDefterimSys(context);
        AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.context = context;

        GooglePlayGuncellemeID = getResources().getInteger(R.integer.GooglePlayGuncellemeID);

        sharedPref = getApplicationContext().getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

        //mIntent = new Intent("com.cnbcyln.app.akordefterim." + sharedPref.getString("prefBulunulanEkran", "AnaEkran"));

        GuncellemeKontrolTimerCalistir(getResources().getInteger(R.integer.AppService_GuncellemeKontrolAraligi) * 1000);

        //OturumGuncellemeTimerCalistir(getResources().getInteger(R.integer.AppService_OturumAraligi) * 1000);
    }

    @Override
    public void onDestroy() {
        try {
            mGuncellemeKontrolTimer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    // UYGULAMA GÜNCELLEME KONTROL SİSTEMİ
    private void GuncellemeKontrolTimerCalistir(int AppService_GuncellemeKontrolAraligi) {
        mGuncellemeKontrolTimer = new Timer();
        mGuncellemeKontrolTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("AppService", "Güncelleme kontrol ediliyor..");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (AkorDefterimSys.InternetErisimKontrolu()) new YeniGuncellemeKontrol().execute();
                        //else Toast.makeText(context, "İnternet bağlantısı yok", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 0, AppService_GuncellemeKontrolAraligi);
    }

    @SuppressLint("StaticFieldLeak")
    private class YeniGuncellemeKontrol extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... parametre) {
            Document doc = null;
            String JSONYeniVersiyon = "";
            String VersiyonCode = "";
            String VersiyonAdi = "";

            try {
                doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())
                        .timeout(60*1000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();

                VersiyonCode = doc.select("span.htlgb").get(3).text().trim().replace(".", "").replace(" ", "");
                VersiyonAdi = doc.select("span.htlgb").get(3).text().trim().replace(" ", "");

                if(StringUtils.isNumeric(VersiyonCode)) {
                    JSONYeniVersiyon = "{\"VersiyonCode\":" + Integer.parseInt(VersiyonCode) + ", \"VersiyonAdi\":\"" + VersiyonAdi + "\"}";
                } else {
                    VersiyonCode = doc.select("span.htlgb").get(7).text().trim().replace(".", "").replace(" ", "");
                    VersiyonAdi = doc.select("span.htlgb").get(7).text().trim().replace(" ", "");

                    if(StringUtils.isNumeric(VersiyonCode)) {
                        JSONYeniVersiyon = "{\"VersiyonCode\":" + Integer.parseInt(VersiyonCode) + ", \"VersiyonAdi\":\"" + VersiyonAdi + "\"}";
                    }
                }

                if(VersiyonCode.equals("")) {
                    VersiyonCode = doc.select("div[itemprop=softwareVersion]").text().trim().replace(".", "").replace(" ", "");
                    VersiyonAdi = doc.select("div[itemprop=softwareVersion]").text().trim().replace(".", "").replace(" ", "");

                    JSONYeniVersiyon = "{\"VersiyonCode\":" + Integer.parseInt(VersiyonCode) + ", \"VersiyonAdi\":\"" + VersiyonAdi + "\"}";
                }
            } catch (Exception e) {
                //e.printStackTrace();
                Log.e("YeniGuncelleme Hatası", e.getMessage());

                VersiyonCode = doc.select("div[itemprop=softwareVersion]").text().trim().replace(".", "").replace(" ", "");
                VersiyonAdi = doc.select("div[itemprop=softwareVersion]").text().trim().replace(".", "").replace(" ", "");

                JSONYeniVersiyon = "{\"VersiyonCode\":" + Integer.parseInt(VersiyonCode) + ", \"VersiyonAdi\":\"" + VersiyonAdi + "\"}";
            }

            return JSONYeniVersiyon;
        }

        @Override
        protected void onPostExecute(String Sonuc) {
            try {
                int YeniVersiyonCode = 0, GecerliVersiyonCode = 0;
                String YeniVersiyonAdi = "", GecerliVersiyonAdi = null;
                JSONObject JSONYeniVersiyon;

                if(!Sonuc.equals("")) {
                    JSONYeniVersiyon = new JSONObject(Sonuc);

                    YeniVersiyonCode = JSONYeniVersiyon.getInt("VersiyonCode");
                    YeniVersiyonAdi = JSONYeniVersiyon.getString("VersiyonAdi");

                    try {
                        GecerliVersiyonCode = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode + 100;
                        GecerliVersiyonAdi = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (YeniVersiyonCode > GecerliVersiyonCode) {
                        final String finalGecerliVersiyonAdi = GecerliVersiyonAdi;
                        final String finalYeniVersiyonAdi = YeniVersiyonAdi;

                        RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(context, RetrofitInterface.class);

                        Call<SnfTarihSaat> snfTarihSaatCall = retrofitInterface.TarihSaatGetir();
                        snfTarihSaatCall.enqueue(new Callback<SnfTarihSaat>() {
                            @Override
                            public void onResponse(Call<SnfTarihSaat> call, Response<SnfTarihSaat> response) {
                                if (response.isSuccessful()) {
                                    SnfTarihSaat snfTarihSaat = response.body();
                                    String GuncelTarihSaat = snfTarihSaat.getTarihSaat();
                                    long GuncellemeTarihFarkiGunCinsi = AkorDefterimSys.IkiTarihArasiFark(GuncelTarihSaat, sharedPref.getString("prefGuncellemeNotifySonGosterimTarih", "1990-01-01 00:00:00"), "Gun");
                                    mGuncellemeGooglePlayIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));

                                    // Güncelleme için gösterilen notification bildirimin son görüntülenme tarihi 1 gün yada aşmış ise bildirim gösteriyoruz.
                                    // Eğer güncelleme var ise günde sadece 1 kere bildirim gösteriyoruz..
                                    if (GuncellemeTarihFarkiGunCinsi >= 1) {
                                        NotifyGoster(mGuncellemeGooglePlayIntent,
                                                GooglePlayGuncellemeID,
                                                getString(R.string.uygulama_adi),
                                                getString(R.string.yeni_guncelleme_icerik, finalGecerliVersiyonAdi, finalYeniVersiyonAdi),
                                                "",
                                                -1,
                                                getString(R.string.yeni_guncelleme_icerik, finalGecerliVersiyonAdi, finalYeniVersiyonAdi),
                                                getString(R.string.uygulama_adi),
                                                getString(R.string.yeni_guncelleme_icerik, finalGecerliVersiyonAdi, finalYeniVersiyonAdi),
                                                "",
                                                true);

                                        sharedPrefEditor = sharedPref.edit();
                                        sharedPrefEditor.putString("prefGuncellemeNotifySonGosterimTarih", GuncelTarihSaat);
                                        sharedPrefEditor.apply();
                                    }// else Toast.makeText(context, "Güncelleme bildirimi bugün gösterilmiş..", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SnfTarihSaat> call, Throwable t) {

                            }
                        });
                    }// else Toast.makeText(context, "Uygulama güncel.. YeniVersiyonAdi: " + YeniVersiyonAdi + " / GecerliVersiyonAdi: " + GecerliVersiyonAdi, Toast.LENGTH_SHORT).show();
                }
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


    // OTURUM GÜNCELLEME SİSTEMİ
    /*private void OturumGuncellemeTimerCalistir(int AppService_OturumAraligi) {
        mOturumGuncellemeTimer = new Timer();
        mOturumGuncellemeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("AppService", "Oturum tarihi güncelleniyor..");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isAppIsInBackground(context)) { // Servis arka planda çalışıyor ise
                            Toast.makeText(context, "Servis arka planda çaışıyor..", Toast.LENGTH_SHORT).show();
                        } else { // Servis uygulama da çalışıyor ise
                            Toast.makeText(context, "Servis uyguluma içinde çalışıyor..", Toast.LENGTH_SHORT).show();
                        }

                        //if (AkorDefterimSys.InternetErisimKontrolu()) new OturumTarihiGuncelle().execute();
                    }
                });
            }
        }, 0, AppService_OturumAraligi);
    }*/

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
}