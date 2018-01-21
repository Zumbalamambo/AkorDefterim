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
	Timer TEPostaDogrulamaKoduSayac, TSMSDogrulamaKoduSayac;
	TimerTask TTEPostaDogrulamaKoduSayac, TTSMSDogrulamaKoduSayac;
	Handler EPostaDogrulamaKoduHandler = new Handler(), SMSDogrulamaKoduHandler = new Handler();
	AlertDialog ADDialog;
	ProgressDialog PDIslem, PDBilgilerGuncelleniyor;
	Bundle mBundle;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnDogrula;
	TextView lblBaslik, lblDogrulamaKoduAciklama, lblKalanSure, lblYenidenGonder;
	PasscodeView txtDogrulamaKodu;

	String Islem = "", EPosta = "", TelKodu = "", CepTelefonu = "", DogrulamaKodu = "";
	int EPostaKalanSure = 0, SMSKalanSure = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dogrulama_kodu);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

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
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.remove("prefSMSDogrulamaKoduSayfaAdi");
		sharedPrefEditor.apply();

		if(DogrulamaKoduAlici != null) unregisterReceiver(DogrulamaKoduAlici);

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.remove("prefSMSDogrulamaKoduSayfaAdi");
		sharedPrefEditor.apply();

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
							if(EPostaKalanSure > 0 && EPostaKalanSure < AkorDefterimSys.EPostaGondermeToplamSure)
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
								DogrulamaKodu = AkorDefterimSys.KodUret(6, true, false, false, false);

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
							break;
						case "EPostaDegisikligi":
							if(EPostaKalanSure > 0 && EPostaKalanSure < AkorDefterimSys.EPostaGondermeToplamSure)
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
								DogrulamaKodu = AkorDefterimSys.KodUret(6, true, false, false, false);

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
							break;
						case "CepTelefonuDegisikligi":
							if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure)
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
								DogrulamaKodu = AkorDefterimSys.KodUret(6, true, false, false, false);

								// Onay kodu belirtilen cep telefonuna sms olarak gönderiliyor
								AkorDefterimSys.SMSGonder(TelKodu, CepTelefonu, getString(R.string.sms_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
							break;
					}
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
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
								EPostaKalanSure = AkorDefterimSys.EPostaGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefEPostaGonderiTarihi", ""),"Saniye");

								if(EPostaKalanSure > 0 && EPostaKalanSure < AkorDefterimSys.EPostaGondermeToplamSure) {
									if (TEPostaDogrulamaKoduSayac != null) {
										TEPostaDogrulamaKoduSayac.cancel();
										TEPostaDogrulamaKoduSayac = null;
										TTEPostaDogrulamaKoduSayac.cancel();
										TTEPostaDogrulamaKoduSayac = null;
									}

									TEPostaDogrulamaKoduSayac = new Timer();
									EPostaDogrulamaKoduSayac_Ayarla();

									lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));
									AkorDefterimSys.setTextViewHTML(lblKalanSure);
									lblKalanSure.setVisibility(View.VISIBLE);

									TEPostaDogrulamaKoduSayac.schedule(TTEPostaDogrulamaKoduSayac, 10, 1000);
								} else {
									EPostaKalanSure = 0;

									sharedPrefEditor = sharedPref.edit();
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									sharedPrefEditor.apply();

									lblKalanSure.setVisibility(View.GONE);
								}
								break;
							case "EPostaDegisikligi":
								EPostaKalanSure = AkorDefterimSys.EPostaGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefEPostaGonderiTarihi", ""),"Saniye");

								if(EPostaKalanSure > 0 && EPostaKalanSure < AkorDefterimSys.EPostaGondermeToplamSure) {
									if (TEPostaDogrulamaKoduSayac != null) {
										TEPostaDogrulamaKoduSayac.cancel();
										TEPostaDogrulamaKoduSayac = null;
										TTEPostaDogrulamaKoduSayac.cancel();
										TTEPostaDogrulamaKoduSayac = null;
									}

									TEPostaDogrulamaKoduSayac = new Timer();
									EPostaDogrulamaKoduSayac_Ayarla();

									lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));
									AkorDefterimSys.setTextViewHTML(lblKalanSure);
									lblKalanSure.setVisibility(View.VISIBLE);

									TEPostaDogrulamaKoduSayac.schedule(TTEPostaDogrulamaKoduSayac, 10, 1000);
								} else {
									EPostaKalanSure = 0;

									sharedPrefEditor = sharedPref.edit();
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									sharedPrefEditor.apply();

									lblKalanSure.setVisibility(View.GONE);
								}
								break;
							case "CepTelefonuDegisikligi":
								SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefSMSGonderiTarihi", ""),"Saniye");

								if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
									if (TSMSDogrulamaKoduSayac != null) {
										TSMSDogrulamaKoduSayac.cancel();
										TSMSDogrulamaKoduSayac = null;
										TTSMSDogrulamaKoduSayac.cancel();
										TTSMSDogrulamaKoduSayac = null;
									}

									TSMSDogrulamaKoduSayac = new Timer();
									SMSDogrulamaKoduSayac_Ayarla();

									lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
									AkorDefterimSys.setTextViewHTML(lblKalanSure);
									lblKalanSure.setVisibility(View.VISIBLE);

									TSMSDogrulamaKoduSayac.schedule(TTSMSDogrulamaKoduSayac, 10, 1000);
								} else {
									SMSKalanSure = 0;

									sharedPrefEditor = sharedPref.edit();
									sharedPrefEditor.remove("prefEPostaGonderiTarihi");
									sharedPrefEditor.apply();

									lblKalanSure.setVisibility(View.GONE);
								}
								break;
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "EPostaGonder":
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_YenidenGonder_Eposta");
					else {
						btnGeri.setEnabled(true);
						btnDogrula.setEnabled(true);
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.dogrulama_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "SMSGonder":
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_YenidenGonder_SMS");
					else {
						btnGeri.setEnabled(true);
						btnDogrula.setEnabled(true);
						lblYenidenGonder.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.dogrulama_kodu),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "TarihSaatGetir_YenidenGonder_Eposta":
					btnGeri.setEnabled(true);
					btnDogrula.setEnabled(true);
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefEPostaGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						if (TEPostaDogrulamaKoduSayac != null) {
							TEPostaDogrulamaKoduSayac.cancel();
							TEPostaDogrulamaKoduSayac = null;
							TTEPostaDogrulamaKoduSayac.cancel();
							TTEPostaDogrulamaKoduSayac = null;
						}

						TEPostaDogrulamaKoduSayac = new Timer();
						EPostaDogrulamaKoduSayac_Ayarla();

						EPostaKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

						lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));
						AkorDefterimSys.setTextViewHTML(lblKalanSure);
						lblKalanSure.setVisibility(View.VISIBLE);

						TEPostaDogrulamaKoduSayac.schedule(TTEPostaDogrulamaKoduSayac, 10, 1000);
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

					break;
				case "TarihSaatGetir_YenidenGonder_SMS":
					btnGeri.setEnabled(true);
					btnDogrula.setEnabled(true);
					lblYenidenGonder.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefSMSGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						if (TSMSDogrulamaKoduSayac != null) {
							TSMSDogrulamaKoduSayac.cancel();
							TSMSDogrulamaKoduSayac = null;
							TTSMSDogrulamaKoduSayac.cancel();
							TTSMSDogrulamaKoduSayac = null;
						}

						TSMSDogrulamaKoduSayac = new Timer();
						SMSDogrulamaKoduSayac_Ayarla();

						SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

						lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
						AkorDefterimSys.setTextViewHTML(lblKalanSure);
						lblKalanSure.setVisibility(View.VISIBLE);

						TSMSDogrulamaKoduSayac.schedule(TTSMSDogrulamaKoduSayac, 10, 1000);
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
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.hesap_bilgileri_guncellenemedi),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					finish();
					break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);

					finish();
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

						AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", "", EPosta, "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");
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

						AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", "","", "", "", "", TelKodu, CepTelefonu, UygulamaVersiyon, "HesapBilgiGuncelle");
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

	public void EPostaDogrulamaKoduSayac_Ayarla() {
		TTEPostaDogrulamaKoduSayac = new TimerTask() {
			@Override
			public void run() {
				EPostaDogrulamaKoduHandler.post(new Runnable() {
					public void run() {
						if (EPostaKalanSure == 0) {
							if (TEPostaDogrulamaKoduSayac != null) {
								TEPostaDogrulamaKoduSayac.cancel();
								TEPostaDogrulamaKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefEPostaGonderiTarihi");
							sharedPrefEditor.apply();

							lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							lblKalanSure.setVisibility(View.GONE);
							AkorDefterimSys.KlavyeKapat();

							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitti_yeniden_basvuru_yapilabilir));
						} else {
							lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							EPostaKalanSure--;
						}
					}
				});
			}
		};
	}

	public void SMSDogrulamaKoduSayac_Ayarla() {
		TTSMSDogrulamaKoduSayac = new TimerTask() {
			@Override
			public void run() {
				SMSDogrulamaKoduHandler.post(new Runnable() {
					public void run() {
						if (SMSKalanSure == 0) {
							if (TSMSDogrulamaKoduSayac != null) {
								TSMSDogrulamaKoduSayac.cancel();
								TSMSDogrulamaKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGonderiTarihi");
							sharedPrefEditor.apply();

							lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							lblKalanSure.setVisibility(View.GONE);
							AkorDefterimSys.KlavyeKapat();

							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitti_yeniden_basvuru_yapilabilir));
						} else {
							lblKalanSure.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							SMSKalanSure--;
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