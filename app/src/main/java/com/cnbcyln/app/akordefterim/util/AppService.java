package com.cnbcyln.app.akordefterim.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class AppService extends Service {
    AkorDefterimSys AkorDefterimSys;
    Context context;
    Veritabani veritabani;
    Intent mIntent;
    SharedPreferences sharedPref;
    Timer mTimer;
    TimerTask timerTask;

    int AppServiceCalismaAraligi;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        AkorDefterimSys = new AkorDefterimSys(context);
        veritabani = new Veritabani(context);

        registerReceiver(broadcastreceiver, new IntentFilter("com.cnbcyln.app.akordefterim.util.AppService"));

        FirebaseMessaging.getInstance().subscribeToTopic("akordefterim");

        sharedPref = getApplicationContext().getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

        mIntent = new Intent("com.cnbcyln.app.akordefterim." + sharedPref.getString("prefBulunulanEkran", "AnaEkran"));

        AppServiceCalismaAraligi = 10000;

        TimerCalistir();
    }

    @Override
    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();

            unregisterReceiver(broadcastreceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }

    private void TimerCalistir() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("AppService", "Servis başladı..");

                /*mIntent.putExtra("JSONData", "{\"Islem\":\"YeniGuncellemeKontrol\", \"BildirimTipi\":\"Notify\"}");
                context.sendBroadcast(mIntent);

                mIntent.putExtra("JSONData", "{\"Islem\":\"YeniGuncellemeKontrol\", \"BildirimTipi\":\"Dialog\"}");
                context.sendBroadcast(mIntent);*/

                //AkorDefterimSys.YeniGuncellemeKontrol();

                //if(sharedPref.getBoolean("prefYedekleSenkronizeEt", false) && AkorDefterimSys.InternetErisimKontrolu()) AkorDefterimSys.VeritabaniSenkronizeEt(veritabani.DBAdi, sharedPref.getString("prefHesapID", "") + ".db");

                //mIntent.putExtra("JSONData", "{\"Islem\":\"ToastMesaj\", \"Mesaj\":\"Timer Çalıştı\"}");
                //context.sendBroadcast(mIntent);
            }
        }, 1000, AppServiceCalismaAraligi);
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String JSON = intent.getStringExtra("JSONData");
                JSONObject JSONGelenVeri = new JSONObject(JSON);

                switch (JSONGelenVeri.getString("Islem")) {
                    case "PushHesapCikis":

                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}