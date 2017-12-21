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
	Timer TDogrulamaKoduSayac;
	TimerTask TTDogrulamaKoduSayac;
	Handler DogrulamaKoduHandler = new Handler();
	Random rnd;
	AlertDialog ADDialog_Hata;
	ProgressDialog PDIslem, PDBilgilerGuncelleniyor;
	Bundle mBundle;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnDogrula;
	TextView lblBaslik, lblDogrulamaKoduAciklama, lblKalanSure, lblYenidenGonder;
	PasscodeView txtDogrulamaKodu;

	String Islem = "", EPosta = "", TelKodu = "", CepTelefonu = "", DogrulamaKodu = "";
	int KalanSure = 0;

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
		sharedPrefEditor.putString("prefSMSDogrulamaKoduSayfaAdi", "Dogrulama_Kodu");
		sharedPrefEditor.apply();

		registerReceiver(DogrulamaKoduAlici, new IntentFilter("com.cnbcyln.app.akordefterim.Dogrulama_Kodu"));

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		btnDogrula = findViewById(R.id.btnDogrula);
		btnDogrula.setOnClickListener(this);

		lblDogrulamaKoduAciklama = findViewById(R.id.lblDogrulamaKoduAciklama);
		lblDogrulamaKoduAciklama.setTypeface(YaziFontu, Typeface.NORMAL);

		txtDogrulamaKodu = findViewById(R.id.txtDogrulamaKodu);
		txtDogrulamaKodu.setPasscodeEntryListener(new PasscodeView.PasscodeEntryListener() {
			@Override
			public void onPasscodeEntered(String GirilenDogrulamaKodu) {
				DevamEt();
			}
		});

		lblKalanSure = findViewById(R.id.lblKalanSure);
		lblKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
		lblKalanSure.setVisibility(View.GONE);

		lblYenidenGonder = findViewById(R.id.lblYenidenGonder);
		lblYenidenGonder.setTypeface(YaziFontu, Typeface.BOLD);
		lblYenidenGonder.setOnClickListener(this);

		mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(sharedPref.getString("prefAction", "").equals("Vazgec")) finish();
		else {
			switch (Islem) {
				case "Kayit":
					EPosta = mBundle.getString("EPosta");
					DogrulamaKodu = mBundle.getString("DogrulamaKodu");

					lblDogrulamaKoduAciklama.setText(getString(R.string.dogrulama_kodu_eposta, EPosta));
					AkorDefterimSys.setTextViewHTML(lblDogrulamaKoduAciklama);

					// prefEPostaGonderiTarihi isimli prefkey var mı?
					if(sharedPref.contains("prefEPostaGonderiTarihi")) { // Varsa
						btnGeri.setEnabled(false);
						btnDogrula.setEnabled(false);
						lblYenidenGonder.setEnabled(false);

						if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
							PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
							PDIslem.show();
						}

						AkorDefterimSys.TarihSaatGetir("TarihSaatOgren_GeriSayimaBasla");
					}
					break;
				case "EPostaDegisikligi":
					EPosta = mBundle.getString("EPosta");
					DogrulamaKodu = mBundle.getString("DogrulamaKodu");

					lblDogrulamaKoduAciklama.setText(getString(R.string.dogrulama_kodu_eposta, EPosta));
					AkorDefterimSys.setTextViewHTML(lblDogrulamaKoduAciklama);

					// prefEPostaGonderiTarihi isimli prefkey var mı?
					if(sharedPref.contains("prefEPostaGonderiTarihi")) { // Varsa
						btnGeri.setEnabled(false);
						btnDogrula.setEnabled(false);
						lblYenidenGonder.setEnabled(false);

						if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
							PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
							PDIslem.show();
						}

						AkorDefterimSys.TarihSaatGetir("TarihSaatOgren_GeriSayimaBasla");
					}
					break;
				case "CepTelefonuDegisikligi":
					TelKodu = mBundle.getString("TelKodu");
					CepTelefonu = mBundle.getString("CepTelefonu");
					DogrulamaKodu = mBundle.getString("DogrulamaKodu");

					lblDogrulamaKoduAciklama.setText(getString(R.string.dogrulama_kodu_cep, "+" + TelKodu + CepTelefonu));
					AkorDefterimSys.setTextViewHTML(lblDogrulamaKoduAciklama);

					// prefSMSGonderiTarihi isimli prefkey var mı?
					if(sharedPref.contains("prefSMSGonderiTarihi")) { // Varsa
						btnGeri.setEnabled(false);
						btnDogrula.setEnabled(false);
						lblYenidenGonder.setEnabled(false);

						if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
							PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
							PDIslem.show();
						}

						AkorDefterimSys.TarihSaatGetir("TarihSaatOgren_GeriSayimaBasla");
					}
					break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.remove("prefSMSDogrulamaKoduSayfaAdi");
		sharedPrefEditor.apply();

		if(DogrulamaKoduAlici != null) unregisterReceiver(DogrulamaKoduAlici);

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

		if(DogrulamaKoduAlici != null) unregisterReceiver(DogrulamaKoduAlici);

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				AkorDefterimSys.KlavyeKapat();
				break;
			case R.id.btnGeri:
				finish();
				break;
			case R.id.btnDogrula:
				DevamEt();
				break;
			case R.id.lblYenidenGonder:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					switch (Islem) {
						case "Kayit":
							if(KalanSure > 0 && KalanSure < AkorDefterimSys.EPostaGondermeToplamSure)
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
							else {
								btnGeri.setEnabled(false);
								btnDogrula.setEnabled(false);
								lblYenidenGonder.setEnabled(false);
								txtDogrulamaKodu.clearText();
								AkorDefterimSys.KlavyeKapat();

								if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
									PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
									PDIslem.show();
								}

								// 6 haneli onay kodu oluşturuldu
								DogrulamaKodu = String.valueOf((100000 + rnd.nextInt(900000)));

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
							break;
						case "EPostaDegisikligi":
							if(KalanSure > 0 && KalanSure < AkorDefterimSys.EPostaGondermeToplamSure)
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
							else {
								btnGeri.setEnabled(false);
								btnDogrula.setEnabled(false);
								lblYenidenGonder.setEnabled(false);
								txtDogrulamaKodu.clearText();
								AkorDefterimSys.KlavyeKapat();

								if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
									PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
									PDIslem.show();
								}

								// 6 haneli onay kodu oluşturuldu
								DogrulamaKodu = String.valueOf((100000 + rnd.nextInt(900000)));

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
							break;
						case "CepTelefonuDegisikligi":
							if(KalanSure > 0 && KalanSure < AkorDefterimSys.SMSGondermeToplamSure)
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
							else {
								btnGeri.setEnabled(false);
								btnDogrula.setEnabled(false);
								lblYenidenGonder.setEnabled(false);
								txtDogrulamaKodu.clearText();
								AkorDefterimSys.KlavyeKapat();

								if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
									PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
									PDIslem.show();
								}

								// 6 haneli onay kodu oluşturuldu
								DogrulamaKodu = String.valueOf((100000 + rnd.nextInt(900000)));

								// Onay kodu belirtilen cep telefonuna sms olarak gönderiliyor
								AkorDefterimSys.SMSGonder(TelKodu, CepTelefonu, getString(R.string.sms_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
							break;
					}
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
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
				case "TarihSaatOgren_GeriSayimaBasla":
					btnGeri.setEnabled(true);
					btnDogrula.setEnabled(true);
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						switch (Islem) {
							case "Kayit":
								KalanSure = AkorDefterimSys.EPostaGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefEPostaGonderiTarihi", ""),"Saniye");

								if(KalanSure > 0 && KalanSure < AkorDefterimSys.EPostaGondermeToplamSure) {
									if (TDogrulamaKoduSayac != null) {
										TDogrulamaKoduSayac.cancel();
										TDogrulamaKoduSayac = null;
										TTDogrulamaKoduSayac.cancel();
										TTDogrulamaKoduSayac = null;
									}

									TDogrulamaKoduSayac = new Timer();
									DogrulamaKoduSayac_Ayarla();

									lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
									AkorDefterimSys.setTextViewHTML(lblKalanSure);
									lblKalanSure.setVisibility(View.VISIBLE);

									TDogrulamaKoduSayac.schedule(TTDogrulamaKoduSayac, 10, 1000);
								} else {
									KalanSure = 0;

									sharedPrefEditor = sharedPref.edit();
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									sharedPrefEditor.apply();

									lblKalanSure.setVisibility(View.GONE);
								}
								break;
							case "EPostaDegisikligi":
								KalanSure = AkorDefterimSys.EPostaGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefEPostaGonderiTarihi", ""),"Saniye");

								if(KalanSure > 0 && KalanSure < AkorDefterimSys.EPostaGondermeToplamSure) {
									if (TDogrulamaKoduSayac != null) {
										TDogrulamaKoduSayac.cancel();
										TDogrulamaKoduSayac = null;
										TTDogrulamaKoduSayac.cancel();
										TTDogrulamaKoduSayac = null;
									}

									TDogrulamaKoduSayac = new Timer();
									DogrulamaKoduSayac_Ayarla();

									lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
									AkorDefterimSys.setTextViewHTML(lblKalanSure);
									lblKalanSure.setVisibility(View.VISIBLE);

									TDogrulamaKoduSayac.schedule(TTDogrulamaKoduSayac, 10, 1000);
								} else {
									KalanSure = 0;

									sharedPrefEditor = sharedPref.edit();
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									sharedPrefEditor.apply();

									lblKalanSure.setVisibility(View.GONE);
								}
								break;
							case "CepTelefonuDegisikligi":
								KalanSure = AkorDefterimSys.SMSGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefSMSGonderiTarihi", ""),"Saniye");

								if(KalanSure > 0 && KalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
									if (TDogrulamaKoduSayac != null) {
										TDogrulamaKoduSayac.cancel();
										TDogrulamaKoduSayac = null;
										TTDogrulamaKoduSayac.cancel();
										TTDogrulamaKoduSayac = null;
									}

									TDogrulamaKoduSayac = new Timer();
									DogrulamaKoduSayac_Ayarla();

									lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
									AkorDefterimSys.setTextViewHTML(lblKalanSure);
									lblKalanSure.setVisibility(View.VISIBLE);

									TDogrulamaKoduSayac.schedule(TTDogrulamaKoduSayac, 10, 1000);
								} else {
									KalanSure = 0;

									sharedPrefEditor = sharedPref.edit();
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									sharedPrefEditor.apply();

									lblKalanSure.setVisibility(View.GONE);
								}
								break;
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
							ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Hata_Kapat");
							ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Hata.show();
						}
					}
					break;
				case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					finish();
					break;
				case "ADDialog_Hata_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

					finish();
					break;
				case "EPostaGonder":
					if(JSONSonuc.getBoolean("Sonuc"))
						AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_YenidenGonder");
					else {
						btnGeri.setEnabled(true);
						btnDogrula.setEnabled(true);
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
							ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.dogrulama_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Hata_Kapat");
							ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Hata.show();
						}
					}

					break;
				case "SMSGonder":
					if(JSONSonuc.getBoolean("Sonuc"))
						AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_YenidenGonder");
					else {
						btnGeri.setEnabled(true);
						btnDogrula.setEnabled(true);
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
							ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.dogrulama_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Hata_Kapat");
							ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Hata.show();
						}
					}

					break;
				case "TarihSaatGetir_YenidenGonder":
					btnGeri.setEnabled(true);
					btnDogrula.setEnabled(true);
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();

						switch (Islem) {
							case "Kayit":
								sharedPrefEditor.putString("prefEPostaGonderiTarihi", JSONSonuc.getString("TarihSaat"));
								break;
							case "EPostaDegisikligi":
								sharedPrefEditor.putString("prefEPostaGonderiTarihi", JSONSonuc.getString("TarihSaat"));
								break;
							case "SMSDegisikligi":
								sharedPrefEditor.putString("prefSMSGonderiTarihi", JSONSonuc.getString("TarihSaat"));
								break;
						}

						sharedPrefEditor.apply();

						KalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

						if (TDogrulamaKoduSayac != null) {
							TDogrulamaKoduSayac.cancel();
							TDogrulamaKoduSayac = null;
							TTDogrulamaKoduSayac.cancel();
							TTDogrulamaKoduSayac = null;
						}

						TDogrulamaKoduSayac = new Timer();
						DogrulamaKoduSayac_Ayarla();

						lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
						AkorDefterimSys.setTextViewHTML(lblKalanSure);
						lblKalanSure.setVisibility(View.VISIBLE);

						TDogrulamaKoduSayac.schedule(TTDogrulamaKoduSayac, 10, 1000);
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

					break;
				case "HesapBilgiGuncelle":
					btnGeri.setEnabled(true);
					btnDogrula.setEnabled(true);
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);

					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();

						if(Islem.equals("EPostaDegisikligi")) sharedPrefEditor.putString("prefEPosta", EPosta);

						sharedPrefEditor.putString("prefAction", "IslemTamamlandi");
						sharedPrefEditor.apply();

						finish();
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
							ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.hesap_bilgileri_guncellenemedi),
									activity.getString(R.string.tamam),
									"ADDialog_Hata_Kapat");
							ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Hata.show();
						}
					}
					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void DevamEt() {
		if(AkorDefterimSys.InternetErisimKontrolu()) {
			btnGeri.setEnabled(false);
			btnDogrula.setEnabled(false);
			lblYenidenGonder.setEnabled(false);
			AkorDefterimSys.KlavyeKapat();

			switch (Islem) {
				case "Kayit":
					if(txtDogrulamaKodu.getText().toString().equals(DogrulamaKodu)) {
						Intent mIntent = new Intent(activity, KayitEkran_Parola_Belirle.class);
						mIntent.putExtra("Islem", Islem);
						mIntent.putExtra("EPosta", EPosta);

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));

					btnGeri.setEnabled(true);
					btnDogrula.setEnabled(true);
					lblYenidenGonder.setEnabled(true);
					break;
				case "EPostaDegisikligi":
					if(txtDogrulamaKodu.getText().toString().equals(DogrulamaKodu)) {
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
						btnGeri.setEnabled(true);
						btnDogrula.setEnabled(true);
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
					}
					break;
				case "CepTelefonuDegisikligi":
					if(txtDogrulamaKodu.getText().toString().equals(DogrulamaKodu)) {
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
						btnGeri.setEnabled(true);
						btnDogrula.setEnabled(true);
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
					}
					break;
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}

	public void DogrulamaKoduSayac_Ayarla() {
		TTDogrulamaKoduSayac = new TimerTask() {
			@Override
			public void run() {
				DogrulamaKoduHandler.post(new Runnable() {
					public void run() {
						if (KalanSure == 0) {
							if (TDogrulamaKoduSayac != null) {
								TDogrulamaKoduSayac.cancel();
								TDogrulamaKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();

							switch (Islem) {
								case "Kayit":
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									break;
								case "EPostaDegisikligi":
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									break;
								case "CepTelefonuDegisikligi":
									sharedPrefEditor.remove("prefSMSGonderiTarihi");
									break;
							}

							sharedPrefEditor.apply();

							lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							lblKalanSure.setVisibility(View.GONE);
							AkorDefterimSys.KlavyeKapat();

							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitti_yeniden_basvuru_yapilabilir));
						} else {
							lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							KalanSure--;
						}
					}
				});
			}
		};
	}

	@SuppressWarnings("unused")
	@SuppressLint("InflateParams")
	private BroadcastReceiver DogrulamaKoduAlici = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String Kimden = intent.getStringExtra("Kimden");
			String DogrulamaKodu = intent.getStringExtra("DogrulamaKodu");

			/*int SMSID = 0;
			Cursor cursor = activity.getContentResolver().query(Uri.parse("content://sms/"), null, "address='"+ Kimden + "'", null, null);

			if (cursor.moveToFirst()) {
				SMSID = Integer.parseInt(cursor.getString(0));
			}*/

			txtDogrulamaKodu.setText(DogrulamaKodu);

			//AkorDefterimSys.SmsSil(activity, SMSID);
		}
	};
}