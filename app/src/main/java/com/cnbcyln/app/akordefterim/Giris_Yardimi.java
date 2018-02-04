package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Giris_Yardimi extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;

	ImageButton btnGeri;
	Button btnKullaniciAdiVeyaEPostaKullan, btnSmsGonder, btnAkorDefterimYardimMerkezi;
	TextView lblBaslik, lblHesabinaEris, lblYardimMerkezi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_girisyardimi);

		activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblHesabinaEris = findViewById(R.id.lblHesabinaEris);
		lblHesabinaEris.setTypeface(YaziFontu, Typeface.BOLD);
		lblHesabinaEris.setText(lblHesabinaEris.getText().toString().toUpperCase());

		btnKullaniciAdiVeyaEPostaKullan = findViewById(R.id.btnKullaniciAdiVeyaEPostaKullan);
		btnKullaniciAdiVeyaEPostaKullan.setTypeface(YaziFontu, Typeface.BOLD);
		btnKullaniciAdiVeyaEPostaKullan.setOnClickListener(this);

		btnSmsGonder = findViewById(R.id.btnSmsGonder);
		btnSmsGonder.setTypeface(YaziFontu, Typeface.BOLD);
		btnSmsGonder.setOnClickListener(this);

		lblYardimMerkezi = findViewById(R.id.lblYardimMerkezi);
		lblYardimMerkezi.setTypeface(YaziFontu, Typeface.BOLD);
		lblYardimMerkezi.setText(lblYardimMerkezi.getText().toString().toUpperCase());

		btnAkorDefterimYardimMerkezi = findViewById(R.id.btnAkorDefterimYardimMerkezi);
		btnAkorDefterimYardimMerkezi.setTypeface(YaziFontu, Typeface.BOLD);
		btnAkorDefterimYardimMerkezi.setText(getString(R.string.s_yardim_merkezi, getString(R.string.uygulama_adi)));
		btnAkorDefterimYardimMerkezi.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnKullaniciAdiVeyaEPostaKullan:
				AkorDefterimSys.KlavyeKapat();
				AkorDefterimSys.EkranGetir(new Intent(activity, Hesabina_Eris_Hesabini_Bul.class), "Slide");
				break;
			case R.id.btnSmsGonder:
				AkorDefterimSys.KlavyeKapat();
				AkorDefterimSys.EkranGetir(new Intent(activity, Hesabina_Eris_Sms_Gonder.class), "Slide");
				break;
			case R.id.btnAkorDefterimYardimMerkezi:

				break;
		}
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
				case "":

					break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}