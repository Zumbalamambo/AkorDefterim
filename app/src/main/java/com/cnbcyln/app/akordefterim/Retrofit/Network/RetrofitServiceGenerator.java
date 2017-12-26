package com.cnbcyln.app.akordefterim.Retrofit.Network;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("StaticFieldLeak")
public class RetrofitServiceGenerator {
    private static AkorDefterimSys AkorDefterimSys;
    private static Retrofit retrofit;
    private static Retrofit.Builder RB;

    private static OkHttpClient.Builder HttpClient = new OkHttpClient.Builder();
    public static <S> S createService(Activity activity, Class<S> serviceClass) {
        AkorDefterimSys = new AkorDefterimSys(activity);
        Gson gson = new GsonBuilder().setLenient().create();
        RB = new Retrofit.Builder().baseUrl(AkorDefterimSys.CBCAPP_HttpsAdres + File.separator + AkorDefterimSys.AkorDefterimKlasorAdi + File.separator).addConverterFactory(GsonConverterFactory.create(gson));
        RB.client(HttpClient.build());
        retrofit = RB.build();
        return retrofit.create(serviceClass);
    }
}
