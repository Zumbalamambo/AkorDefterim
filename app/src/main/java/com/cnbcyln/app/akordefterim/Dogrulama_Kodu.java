package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mhk.android.passcodeview.PasscodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Dogrulama_Kodu extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	Timer EPostaOnayKoduSayac, SMSOnayKoduSayac;
	TimerTask EPostaOnayKoduDialogSayac, SMSOnayKoduDialogSayac;
	Handler EPostaOnayKoduHandler = new Handler(), SMSOnayKoduHandler = new Handler();
	Random rnd;
	AlertDialog ADDialog_Hata, ADDialog_Guncelleme;
	ProgressDialog PDBilgilerGuncelleniyor;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnKaydet;
	Button btnDogrula;
	TextView lblVazgec, lblBaslik, lblOnayKoduAciklama, lblOnayKoduKalanSure, lblYenidenGonder;
	PasscodeView txtOnayKodu;

	String Islem = "", EPosta = "", TelKodu = "", CepTelefonu = "", OnayKodu = "";
	int EPostaGondermeKalanSure = 0;
	int SMSGondermeKalanSure = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dogrulama_kodu);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
		rnd = new Random();

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.putString("prefSMSOnayKoduSayfaAdi", "Dogrulama_Kodu");
		sharedPrefEditor.apply();

		registerReceiver(OnayKoduAlici, new IntentFilter("com.cnbcyln.app.akordefterim.Dogrulama_Kodu"));

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setTypeface(YaziFontu, Typeface.BOLD);
		lblVazgec.setOnClickListener(this);

		btnKaydet = findViewById(R.id.btnKaydet);
		btnKaydet.setOnClickListener(this);

		lblOnayKoduAciklama = findViewById(R.id.lblOnayKoduAciklama);
		lblOnayKoduAciklama.setTypeface(YaziFontu, Typeface.NORMAL);

		txtOnayKodu = findViewById(R.id.txtOnayKodu);
		txtOnayKodu.setPasscodeEntryListener(new PasscodeView.PasscodeEntryListener() {
			@Override
			public void onPasscodeEntered(String GirilenOnayKodu) {
				DevamEt();
			}
		});

		lblOnayKoduKalanSure = findViewById(R.id.lblOnayKoduKalanSure);

		lblYenidenGonder = findViewById(R.id.lblYenidenGonder);
		lblYenidenGonder.setTypeface(YaziFontu, Typeface.BOLD);
		lblYenidenGonder.setOnClickListener(this);

		btnDogrula = findViewById(R.id.btnDogrula);
		btnDogrula.setTypeface(YaziFontu, Typeface.NORMAL);
		btnDogrula.setOnClickListener(this);

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");

		switch (Islem) {
			case "Kayit":
				EPosta = mBundle.getString("EPosta");
				OnayKodu = mBundle.getString("OnayKodu");

				EPostaOnayKoduSayac = new Timer();
				EPostaOnayKoduDialogSayac_Ayarla();

				if(sharedPref.getInt("prefEPostaGondermeKalanSure", 0) > 0)
					EPostaGondermeKalanSure = sharedPref.getInt("prefEPostaGondermeKalanSure", 0);
				else
					EPostaGondermeKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

				lblVazgec.setVisibility(View.VISIBLE);

				btnKaydet.setVisibility(View.GONE);

				lblOnayKoduAciklama.setText(getString(R.string.dogrulama_kodu_eposta, EPosta));
				AkorDefterimSys.setTextViewHTML(lblOnayKoduAciklama);

				lblOnayKoduKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
				lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaGondermeKalanSure)));

				btnDogrula.setVisibility(View.VISIBLE);

				EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);
				break;
			case "EPostaDegisikligi":
				EPosta = mBundle.getString("EPosta");
				OnayKodu = mBundle.getString("OnayKodu");

				EPostaOnayKoduSayac = new Timer();
				EPostaOnayKoduDialogSayac_Ayarla();

				if(sharedPref.getInt("prefEPostaGondermeKalanSure", 0) > 0)
					EPostaGondermeKalanSure = sharedPref.getInt("prefEPostaGondermeKalanSure", 0);
				else
					EPostaGondermeKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

				lblVazgec.setVisibility(View.GONE);

				btnKaydet.setVisibility(View.VISIBLE);

				lblOnayKoduAciklama.setText(getString(R.string.dogrulama_kodu_eposta, EPosta));
				AkorDefterimSys.setTextViewHTML(lblOnayKoduAciklama);

				lblOnayKoduKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
				lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaGondermeKalanSure)));

				btnDogrula.setVisibility(View.GONE);

				EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);
				break;
			case "CepTelefonuDegisikligi":
				SMSOnayKoduSayac = new Timer();
				SMSOnayKoduDialogSayac_Ayarla();

				if(sharedPref.getInt("prefSMSGondermeKalanSure", 0) > 0)
					SMSGondermeKalanSure = sharedPref.getInt("prefSMSGondermeKalanSure", 0);
				else
					SMSGondermeKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

				TelKodu = mBundle.getString("TelKodu");
				CepTelefonu = mBundle.getString("CepTelefonu");
				OnayKodu = mBundle.getString("OnayKodu");

				lblVazgec.setVisibility(View.GONE);

				btnKaydet.setVisibility(View.VISIBLE);

				lblOnayKoduAciklama.setText(getString(R.string.dogrulama_kodu_cep, "+" + TelKodu + CepTelefonu));
				AkorDefterimSys.setTextViewHTML(lblOnayKoduAciklama);

				lblOnayKoduKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
				lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));

				btnDogrula.setVisibility(View.GONE);

				SMSOnayKoduSayac.schedule(SMSOnayKoduDialogSayac, 10, 1000);
				break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(sharedPref.getString("prefAction", "").equals("Vazgec")) onBackPressed();
	}

	@Override
	protected void onDestroy() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);
		AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.remove("prefSMSOnayKoduSayfaAdi");
		sharedPrefEditor.apply();

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);
		AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);

		if(OnayKoduAlici != null) unregisterReceiver(OnayKoduAlici);

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
				sharedPrefEditor = sharedPref.edit();
				sharedPrefEditor.putString("prefAction", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
				break;
			case R.id.btnKaydet:
				DevamEt();
				break;
			case R.id.lblYenidenGonder:
				lblYenidenGonder.setEnabled(false);
				AkorDefterimSys.KlavyeKapat();

				if(AkorDefterimSys.InternetErisimKontrolu()) {
					switch (Islem) {
						case "Kayit":
							if(EPostaGondermeKalanSure > 0 && EPostaGondermeKalanSure < AkorDefterimSys.EPostaGondermeToplamSure) { // Eğer kalan süre hala bitmemişse
								lblYenidenGonder.setEnabled(true);
								AkorDefterimSys.KlavyeKapat();
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
							} else {
								// 6 haneli onay kodu oluşturuldu
								OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, OnayKodu, getString(R.string.uygulama_adi)));
							}
							break;
						case "EPostaDegisikligi":
							if(EPostaGondermeKalanSure > 0 && EPostaGondermeKalanSure < AkorDefterimSys.EPostaGondermeToplamSure) { // Eğer kalan süre hala bitmemişse
								lblYenidenGonder.setEnabled(true);
								AkorDefterimSys.KlavyeKapat();
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
							} else {
								// 6 haneli onay kodu oluşturuldu
								OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, OnayKodu, getString(R.string.uygulama_adi)));
							}
							break;
						case "CepTelefonuDegisikligi":
							if(SMSGondermeKalanSure > 0 && SMSGondermeKalanSure < AkorDefterimSys.SMSGondermeToplamSure) { // Eğer kalan süre hala bitmemişse
								lblYenidenGonder.setEnabled(true);
								AkorDefterimSys.KlavyeKapat();
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
							} else {
								// 6 haneli onay kodu oluşturuldu
								OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

								// Onay kodu belirtilen cep telefonuna sms olarak gönderiliyor
								AkorDefterimSys.SMSGonder(TelKodu, CepTelefonu, getString(R.string.sms_dosrulama_kodu_icerik, OnayKodu, getString(R.string.uygulama_adi)));
							}
							break;
					}
				} else {
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
				}
				break;
			case R.id.btnIleri:
				DevamEt();
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

						EPostaGondermeKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putInt("prefEPostaGondermeKalanSure", EPostaGondermeKalanSure);
						sharedPrefEditor.apply();

						EPostaOnayKoduSayac = new Timer();
						EPostaOnayKoduDialogSayac_Ayarla();
						EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);
					} else {
						if (EPostaOnayKoduSayac != null) {
							EPostaOnayKoduSayac.cancel();
							EPostaOnayKoduSayac = null;
							EPostaGondermeKalanSure = 0;
							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaGondermeKalanSure)));
						}

						AkorDefterimSys.KlavyeKapat();

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
							ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.onay_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Hata_Kapat");
							ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Hata.show();
						}
					}

					break;
				case "SMSGonder":
					if(JSONSonuc.getBoolean("Sonuc")) {
						lblYenidenGonder.setEnabled(true);
						txtOnayKodu.clearText();
						AkorDefterimSys.KlavyeKapat();

						SMSGondermeKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
						sharedPrefEditor.apply();

						SMSOnayKoduSayac = new Timer();
						SMSOnayKoduDialogSayac_Ayarla();
						SMSOnayKoduSayac.schedule(SMSOnayKoduDialogSayac, 10, 1000);
					} else {
						if (SMSOnayKoduSayac != null) {
							SMSOnayKoduSayac.cancel();
							SMSOnayKoduSayac = null;
							SMSGondermeKalanSure = 0;
							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
						}

						AkorDefterimSys.KlavyeKapat();

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
							ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.onay_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Hata_Kapat");
							ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Hata.show();
						}
					}

					break;
				case "ADDialog_Hata_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

					onBackPressed();
					break;
				case "HesapBilgiGuncelle":
					btnKaydet.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);

					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						if(Islem.equals("EPostaDegisikligi")) sharedPrefEditor.putString("prefEPosta", EPosta);
						sharedPrefEditor.putString("prefAction", "IslemTamamlandi");
						sharedPrefEditor.apply();

						onBackPressed();
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Guncelleme)) {
							ADDialog_Guncelleme = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.hesap_bilgileri_guncellenemedi),
									activity.getString(R.string.tamam),
									"ADDialog_Guncelleme_Kapat_GeriGit");
							ADDialog_Guncelleme.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Guncelleme.show();
						}
					}
					break;
				case "ADDialog_Guncelleme_Kapat_GeriGit":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);

					onBackPressed();
					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void DevamEt() {
		btnDogrula.setEnabled(false);
		btnKaydet.setEnabled(false);
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			switch (Islem) {
				case "Kayit":
					if(txtOnayKodu.getText().toString().equals(OnayKodu)) {
						Intent mIntent = new Intent(activity, KayitEkran_Parola_Belirle.class);
						mIntent.putExtra("Islem", Islem);
						mIntent.putExtra("EPosta", EPosta);

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
						btnDogrula.setEnabled(true);
					} else {
						btnDogrula.setEnabled(true);
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
					}
					break;
				case "EPostaDegisikligi":
					if(txtOnayKodu.getText().toString().equals(OnayKodu)) {
						String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
						@SuppressLint("HardwareIds")
						String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
						String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
						String UygulamaVersiyon = "";

						try {
							UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}

						if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
							PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_guncelleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
							PDBilgilerGuncelleniyor.show();
						}

						AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), FirebaseToken, OSID, OSVersiyon, "", "", EPosta, "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");
					} else {
						btnKaydet.setEnabled(true);
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
					}
					break;
				case "CepTelefonuDegisikligi":
					if(txtOnayKodu.getText().toString().equals(OnayKodu)) {
						String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
						@SuppressLint("HardwareIds")
						String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
						String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
						String UygulamaVersiyon = "";

						try {
							UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}

						if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
							PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_guncelleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
							PDBilgilerGuncelleniyor.show();
						}

						AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), FirebaseToken, OSID, OSVersiyon, "", "", "", "", "", "", TelKodu, CepTelefonu, UygulamaVersiyon, "HesapBilgiGuncelle");
					} else {
						btnKaydet.setEnabled(true);
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
					}
					break;
			}
		} else {
			btnDogrula.setEnabled(true);
			AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	public void EPostaOnayKoduDialogSayac_Ayarla() {
		EPostaOnayKoduDialogSayac = new TimerTask() {
			@Override
			public void run() {
				EPostaOnayKoduHandler.post(new Runnable() {
					public void run() {
						if (EPostaGondermeKalanSure == 0) {
							if (EPostaOnayKoduSayac != null) {
								EPostaOnayKoduSayac.cancel();
								EPostaOnayKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefEPostaGondermeKalanSure");
							sharedPrefEditor.apply();

							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaGondermeKalanSure)));
							AkorDefterimSys.KlavyeKapat();
						} else {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putInt("prefEPostaGondermeKalanSure", EPostaGondermeKalanSure);
							sharedPrefEditor.apply();

							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaGondermeKalanSure)));
							EPostaGondermeKalanSure--;
						}
					}
				});
			}
		};
	}

	public void SMSOnayKoduDialogSayac_Ayarla() {
		SMSOnayKoduDialogSayac = new TimerTask() {
			@Override
			public void run() {
				SMSOnayKoduHandler.post(new Runnable() {
					public void run() {
						if (SMSGondermeKalanSure == 0) {
							if (SMSOnayKoduSayac != null) {
								SMSOnayKoduSayac.cancel();
								SMSOnayKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGondermeKalanSure");
							sharedPrefEditor.apply();

							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							AkorDefterimSys.KlavyeKapat();
						} else {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
							sharedPrefEditor.apply();

							lblOnayKoduKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							SMSGondermeKalanSure--;
						}
					}
				});
			}
		};
	}

	@SuppressWarnings("unused")
	@SuppressLint("InflateParams")
	private BroadcastReceiver OnayKoduAlici = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String Kimden = intent.getStringExtra("Kimden");
			String OnayKodu = intent.getStringExtra("OnayKodu");

			/*int SMSID = 0;
			Cursor cursor = activity.getContentResolver().query(Uri.parse("content://sms/"), null, "address='"+ Kimden + "'", null, null);

			if (cursor.moveToFirst()) {
				SMSID = Integer.parseInt(cursor.getString(0));
			}*/

			txtOnayKodu.setText(OnayKodu);

			//AkorDefterimSys.SmsSil(activity, SMSID);
		}
	};
}