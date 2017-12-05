package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.mhk.android.passcodeview.PasscodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Onaykodu extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
	SharedPreferences.Editor sharedPrefEditor;
	Timer EPostaOnayKoduSayac;
	TimerTask EPostaOnayKoduDialogSayac;
	Handler EPostaOnayKoduHandler = new Handler();
	Random rnd;
	AlertDialog ADDialog_Sonuc;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	TextView lblVazgec, lblBaslik, lblOnayKoduAciklama, lblOnayKoduKalanSure, lblYenidenGonder;
	PasscodeView txtOnayKodu;

	String Islem, EPosta, OnayKodu;
	int EPostaOnayKoduKalanSure = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onaykodu);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
		rnd = new Random();

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		EPosta = mBundle.getString("EPosta");
		OnayKodu = mBundle.getString("OnayKodu");

		EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
		EPostaOnayKoduSayac = new Timer();
		EmailOnayKoduDialogSayac_Ayarla();
		EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setTypeface(YaziFontu, Typeface.BOLD);
		lblVazgec.setOnClickListener(this);

		lblOnayKoduAciklama = findViewById(R.id.lblOnayKoduAciklama);
		lblOnayKoduAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblOnayKoduAciklama.setText(getString(R.string.onay_kodu_email, EPosta));
		AkorDefterimSys.setTextViewHTML(lblOnayKoduAciklama);

		txtOnayKodu = findViewById(R.id.txtOnayKodu);
		txtOnayKodu.setPasscodeEntryListener(new PasscodeView.PasscodeEntryListener() {
			@Override
			public void onPasscodeEntered(String GirilenOnayKodu) {
				IleriIslem();
			}
		});

		lblOnayKoduKalanSure = findViewById(R.id.lblOnayKoduKalanSure);
		lblOnayKoduKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
		lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));

		lblYenidenGonder = findViewById(R.id.lblYenidenGonder);
		lblYenidenGonder.setTypeface(YaziFontu, Typeface.BOLD);
		lblYenidenGonder.setOnClickListener(this);

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.PrefAyarlar().getString("prefAction", "").equals("Vazgec")) onBackPressed();
	}

	@Override
	protected void onDestroy() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Sonuc);

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Sonuc);

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				AkorDefterimSys.KlavyeKapat();
				break;
			case R.id.btnGeri:
				onBackPressed();
				break;
			case R.id.lblVazgec:
				sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
				sharedPrefEditor.putString("prefAction", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
				break;
			case R.id.lblYenidenGonder:
				lblYenidenGonder.setEnabled(false);
				AkorDefterimSys.KlavyeKapat();

				if(AkorDefterimSys.InternetErisimKontrolu()) {
					if(EPostaOnayKoduKalanSure > 0) { // Eğer kalan süre hala bitmemişse
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.KlavyeKapat();
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
					} else {
						// 6 haneli onay kodu oluşturuldu
						OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

						// Onay kodu belirtilen eposta adresine gönderiliyor
						AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_onayi_icerik2, getString(R.string.uygulama_adi), OnayKodu));
					}
				} else {
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
				}
				break;
			case R.id.btnIleri:
				IleriIslem();
				break;
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "EPostaGonder":
					if(JSONSonuc.getBoolean("Sonuc")) {
						lblYenidenGonder.setEnabled(true);
						txtOnayKodu.clearText();
						AkorDefterimSys.KlavyeKapat();

						EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
						EPostaOnayKoduSayac = new Timer();
						EmailOnayKoduDialogSayac_Ayarla();
						EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);

						if(AkorDefterimSys.AlertDialogisShowing(ADDialog_Sonuc)) {
							ADDialog_Sonuc = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
									getString(R.string.onay_kodu),
									getString(R.string.onay_kodu_email, EPosta),
									activity.getString(R.string.tamam));
							ADDialog_Sonuc.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Sonuc.show();

							ADDialog_Sonuc.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog_Sonuc.dismiss();
								}
							});
						}
					} else {
						if (EPostaOnayKoduSayac != null) {
							EPostaOnayKoduSayac.cancel();
							EPostaOnayKoduSayac = null;
							EPostaOnayKoduKalanSure = 0;
							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
						}

						AkorDefterimSys.KlavyeKapat();

						if(AkorDefterimSys.AlertDialogisShowing(ADDialog_Sonuc)) {
							ADDialog_Sonuc = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
									getString(R.string.onay_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam));
							ADDialog_Sonuc.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Sonuc.show();

							ADDialog_Sonuc.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog_Sonuc.dismiss();
								}
							});
						}
					}

					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void IleriIslem() {
		btnIleri.setEnabled(false);
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			if(EPostaOnayKoduKalanSure > 0) { // Eğer kalan süre hala bitmemişse
				if(txtOnayKodu.getText().toString().equals(OnayKodu)) {
					Intent mIntent = new Intent(activity, KayitEkran_Parola_Belirle.class);
					mIntent.putExtra("Islem", Islem);
					mIntent.putExtra("EPosta", EPosta);

					AkorDefterimSys.EkranGetir(mIntent, "Slide");
					btnIleri.setEnabled(true);
				} else {
					btnIleri.setEnabled(true);
					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
				}
			} else {
				btnIleri.setEnabled(true);
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.onay_kodu_sure_bitti));
			}
		} else {
			btnIleri.setEnabled(true);
			AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	public void EmailOnayKoduDialogSayac_Ayarla() {
		EPostaOnayKoduDialogSayac = new TimerTask() {
			@Override
			public void run() {
				EPostaOnayKoduHandler.post(new Runnable() {
					public void run() {
						if (EPostaOnayKoduKalanSure == 0) {
							if (EPostaOnayKoduSayac != null) {
								EPostaOnayKoduSayac.cancel();
								EPostaOnayKoduSayac = null;
							}

							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
							AkorDefterimSys.KlavyeKapat();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.onay_kodu_sure_bitti));
						} else {
							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
							EPostaOnayKoduKalanSure--;
						}
					}
				});
			}
		};
	}
}