package com.cnbcyln.app.akordefterim.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class AppRunningControl extends Service {
    Context context;
    AkorDefterimSys AkorDefterimSys;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //AkorDefterimSys = new AkorDefterimSys(context);
        AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.context = context;

        sharedPref = getApplicationContext().getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

        /*sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putBoolean("prefAppRunning", true);
        sharedPrefEditor.apply();*/

        Log.i("AppRunningControl", "Servis Çalışıyor..");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putBoolean("prefAppRunning", false);
        sharedPrefEditor.apply();

        Intent mIntentMqttService = new Intent(MqttService.class.getName());
        mIntentMqttService.putExtra("JSONData", "{\"Islem\":\"OnlineDurum\"}");
        this.sendBroadcast(mIntentMqttService);

        Log.i("AppRunningControl", "Servis kapandı..");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }
}