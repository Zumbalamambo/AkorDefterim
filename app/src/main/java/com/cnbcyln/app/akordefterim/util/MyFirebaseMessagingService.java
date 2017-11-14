package com.cnbcyln.app.akordefterim.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.SplashEkran;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    AkorDefterimSys AkorDefterimSys;
    Context context;
    Intent mIntent;
    SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        AkorDefterimSys = new AkorDefterimSys(context);

        sharedPref = getApplicationContext().getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

        if(sharedPref.getString("prefEkranAdi", "AnaEkran").equals("AnaEkran")) mIntent = new Intent("com.cnbcyln.app.akordefterim.AnaEkran");
        else if(sharedPref.getString("prefEkranAdi", "AnaEkran").equals("GirisEkran")) mIntent = new Intent("com.cnbcyln.app.akordefterim.GirisEkran");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /**
         * Önemli not!!: uygulama kapalı olsa bile firebase'den bildirim geliyordu ama Broadcast yapamıyordu.
         * Bunun nedeni, uygulama kapalı olduğu için uygulamadan broadcast yapamaz.
         * Bu nedenle sadece bildirim tipi işlemleri notification şeklinde bu class içinde bir void tanımladık
         * Ve bu class içinden nitification çağırdık. Bu sayede artık uygulama kapalı olsa bile notification alabildik ;)
         */
        try {
            JSONObject JSONGelenVeri = new JSONObject(new JSONObject(remoteMessage.getData()).getString("JSONData"));
            //JSONObject JSONGelenVeri = new JSONObject(remoteMessage.getNotification().getBody());

            Intent myIntent;

            switch (JSONGelenVeri.getString("Islem")) {
                case "Bildirim":
                    //mIntent.putExtra("JSONData", "{\"Islem\":\"Firebase_Bildirim\", \"MesajIcerik\":\"" + JSONGelenVeri.getString("MesajIcerik") + "\"}");
                    //this.sendBroadcast(mIntent);

                    myIntent = new Intent(context, SplashEkran.class);
                    NotifyGoster(myIntent, getString(R.string.uygulama_adi), JSONGelenVeri.getString("MesajIcerik"), "", -1, JSONGelenVeri.getString("MesajIcerik"), getString(R.string.uygulama_adi), JSONGelenVeri.getString("MesajIcerik"), "", true);

                    break;
                case "YeniSarkiBildirim":
                    //mIntent.putExtra("JSONData", "{\"Islem\":\"Firebase_YeniSarkiBildirim\", \"SarkiID\":" + JSONGelenVeri.getInt("SarkiID") + ", \"SanatciAdi\":\"" + JSONGelenVeri.getString("SanatciAdi") + "\", \"SarkiAdi\":\"" + JSONGelenVeri.getString("SarkiAdi") + "\"}");
                    //this.sendBroadcast(mIntent);

                    String MesajIcerik = getString(R.string.yeni_sarki_ekleme_notify_mesaji, JSONGelenVeri.getString("SanatciAdi"), JSONGelenVeri.getString("SarkiAdi"), getString(R.string.uygulama_adi));
                    myIntent = new Intent(context, SplashEkran.class);
                    NotifyGoster(myIntent, getString(R.string.uygulama_adi), MesajIcerik, "", -1, MesajIcerik, getString(R.string.uygulama_adi), MesajIcerik, "", true);

                    break;
                case "Guncelleme":
                    //mIntent.putExtra("JSONData", "{\"Islem\":\"Firebase_Guncelleme\"}");
                    //this.sendBroadcast(mIntent);

                    myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
                    NotifyGoster(myIntent, getString(R.string.uygulama_adi), getString(R.string.yeni_guncelleme_icerik2), "", -1, getString(R.string.yeni_guncelleme_icerik2), getString(R.string.uygulama_adi), getString(R.string.yeni_guncelleme_icerik2), "", true);

                    break;
                case "DuyuruReklam":
                    mIntent.putExtra("JSONData", "{\"Islem\":\"Firebase_Duyuru_Reklam\"}");
                    this.sendBroadcast(mIntent);

                    break;
                case "PushHesapCikis":
                    String FirebaseToken = FirebaseInstanceId.getInstance().getToken();

                    if(JSONGelenVeri.getString("FirebaseToken").equals(FirebaseToken)) { // Sisteme yeni giriş yapan FirebaseToken, cihazda giriş yapmış olan FirebaseToken'a eşit ise, cihazda giriş yapmış olan FirebaseToken'a çıkış yaptır
                        mIntent.putExtra("JSONData", "{\"Islem\":\"Firebase_HesapCikis\"}");
                        this.sendBroadcast(mIntent);
                    }

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void NotifyGoster(Intent intent, String NotifyBaslik, String NotifyIcerik, String SubIcerik, int Number, String TickerIcerik, String BigNotifyBaslik, String BigNotifyIcerik, String BigSubIcerik, Boolean Titresim) {
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
        notificationManager.notify(1, notification);
    }
}