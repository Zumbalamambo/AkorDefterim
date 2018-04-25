package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

@SuppressWarnings("ALL")
public class YoutubeVideoPlayer extends YouTubeBaseActivity  implements YouTubePlayer.OnInitializedListener, OnClickListener {

    private Activity activity;
    private AkorDefterimSys AkorDefterimSys;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    Typeface YaziFontu;

    YouTubePlayerView YTVideoPlayer;
    Button btnKapat;

    String VideoID = "";

    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);

        activity = this;
        AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
        YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

        sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

        AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
        //AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
        //AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        YTVideoPlayer = findViewById(R.id.YTVideoPlayer);
        YTVideoPlayer.initialize(getString(R.string.google_api_key), this);

        btnKapat = findViewById(R.id.btnKapat);
        btnKapat.setOnClickListener(this);

        Bundle mBundle = getIntent().getExtras();
        VideoID = mBundle.getString("VideoID");
    }

    @Override
    protected void onStart() {
        super.onStart();
        AkorDefterimSys.activity = activity;
        AkorDefterimSys.SharePrefAyarlarınıUygula();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo(VideoID); // https://www.youtube.com/watch?v=<VideoID>
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(R.string.google_api_key), this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return YTVideoPlayer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnKapat:
                finish();
                break;
        }
    }
}