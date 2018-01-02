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

@SuppressWarnings("ALL")
public class Hakkinda_Open_Source_Lib extends AppCompatActivity implements OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	SharedPreferences.OnSharedPreferenceChangeListener sharedPrefChanged;
	Typeface YaziFontu;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnKapat;
	TextView lblBaslik, lblAcikKaynakKutuphaneleri, lblAcikKaynakKutuphaneleriAciklama, lblUcuncuPartiYazilimListesi, lblUcuncuPartiYazilimLisansi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hakkinda_open_source_lib);

        activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        btnKapat = findViewById(R.id.btnKapat);
        btnKapat.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblAcikKaynakKutuphaneleri = findViewById(R.id.lblAcikKaynakKutuphaneleri);
		lblAcikKaynakKutuphaneleri.setTypeface(YaziFontu, Typeface.BOLD);

		lblAcikKaynakKutuphaneleriAciklama = findViewById(R.id.lblAcikKaynakKutuphaneleriAciklama);
		lblAcikKaynakKutuphaneleriAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblAcikKaynakKutuphaneleriAciklama.setText(getString(R.string.acik_kaynak_lib_aciklama, getString(R.string.uygulama_adi)));
		AkorDefterimSys.setTextViewHTML(lblAcikKaynakKutuphaneleriAciklama);

		lblUcuncuPartiYazilimListesi = findViewById(R.id.lblUcuncuPartiYazilimListesi);
		lblUcuncuPartiYazilimListesi.setTypeface(YaziFontu, Typeface.NORMAL);
		lblUcuncuPartiYazilimListesi.setText(getString(R.string.acik_kaynak_lib_listesi));
		AkorDefterimSys.setTextViewHTML(lblUcuncuPartiYazilimListesi);

		lblUcuncuPartiYazilimLisansi = findViewById(R.id.lblUcuncuPartiYazilimLisansi);
		lblUcuncuPartiYazilimLisansi.setTypeface(YaziFontu, Typeface.NORMAL);
		lblUcuncuPartiYazilimLisansi.setText(getString(R.string.acik_kaynak_lib_lisansi));
		AkorDefterimSys.setTextViewHTML(lblUcuncuPartiYazilimLisansi);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnKapat:
				onBackPressed();
				break;
		}
	}
}