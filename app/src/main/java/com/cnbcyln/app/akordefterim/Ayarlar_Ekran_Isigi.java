package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.xw.repo.BubbleSeekBar;

@SuppressWarnings("ALL")
public class Ayarlar_Ekran_Isigi extends AppCompatActivity implements OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	SharedPreferences.OnSharedPreferenceChangeListener sharedPrefChanged;
	Typeface YaziFontu;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	BubbleSeekBar BSBEkranIsigi;
	TextView lblBaslik, lblEkranIsigiAciklama;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ayarlar_ekran_isigi);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        btnGeri = findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblEkranIsigiAciklama = findViewById(R.id.lblEkranIsigiAciklama);
		lblEkranIsigiAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblEkranIsigiAciklama.setText(getString(R.string.ekran_isigi_aciklama));
		AkorDefterimSys.setTextViewHTML(lblEkranIsigiAciklama);

        BSBEkranIsigi = findViewById(R.id.BSBEkranIsigi);
		BSBEkranIsigi.setProgress(sharedPref.getInt("prefEkranIsigiAydinligi", 255));
		BSBEkranIsigi.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
			@Override
			public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
				sharedPrefEditor = sharedPref.edit();
				sharedPrefEditor.putInt("prefEkranIsigiAydinligi", progress);
				sharedPrefEditor.apply();
			}

			@Override
			public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

			}

			@Override
			public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				AkorDefterimSys.EkranKapat();
				break;
		}
	}
}