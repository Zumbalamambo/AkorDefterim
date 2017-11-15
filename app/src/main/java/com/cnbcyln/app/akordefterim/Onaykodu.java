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
public class Onaykodu extends AppCompatActivity implements Interface_AsyncResponse {

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
	Button btnGeri, btnIleri;
	TextView lblVazgec, lblBaslik, lblOnayKoduAciklama, lblOnayKoduKalanSureAciklama, lblYenidenGonder;
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
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		EPosta = mBundle.getString("EPosta");
		OnayKodu = mBundle.getString("OnayKodu");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AkorDefterimSys.KlavyeKapat();
			}
		});

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
				sharedPrefEditor.putString("Action", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
			}
		});

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblOnayKoduAciklama = findViewById(R.id.lblOnayKoduAciklama);
		lblOnayKoduAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblOnayKoduAciklama);

		txtOnayKodu = findViewById(R.id.txtOnayKodu);
		txtOnayKodu.setPasscodeEntryListener(new PasscodeView.PasscodeEntryListener() {
			@Override
			public void onPasscodeEntered(String GirilenOnayKodu) {
				IleriIslem();
			}
		});

		lblOnayKoduKalanSureAciklama = findViewById(R.id.lblOnayKoduKalanSureAciklama);
		lblOnayKoduKalanSureAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblOnayKoduKalanSureAciklama.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));

		lblYenidenGonder = findViewById(R.id.lblYenidenGonder);
		lblYenidenGonder.setTypeface(YaziFontu, Typeface.BOLD);
		lblYenidenGonder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AkorDefterimSys.KlavyeKapat();

				if(AkorDefterimSys.InternetErisimKontrolu()) {
					if(EPostaOnayKoduKalanSure > 0) { // Eğer kalan süre hala bitmemişse
						AkorDefterimSys.KlavyeKapat();
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
					} else {
						// 6 haneli onay kodu oluşturuldu
						OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

						// Onay kodu belirtilen eposta adresine gönderiliyor
						AkorDefterimSys.EPostaGonder(EPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.mail_onayi_icerik2, getString(R.string.uygulama_adi), OnayKodu));
					}
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
			}
		});

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IleriIslem();
			}
		});

		EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
		EPostaOnayKoduSayac = new Timer();
		EmailOnayKoduDialogSayac_Ayarla();
		EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.PrefAyarlar().getString("Action", "").equals("Vazgec") || AkorDefterimSys.PrefAyarlar().getString("GelinenEkran", "").equals("Parola_Belirle")) {
			sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
			sharedPrefEditor.remove("GelinenEkran");
			sharedPrefEditor.apply();

			onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		AkorDefterimSys.DismissAlertDialog(ADDialog_Sonuc);
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		AkorDefterimSys.DismissAlertDialog(ADDialog_Sonuc);

		super.onBackPressed();
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

							lblOnayKoduKalanSureAciklama.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
							AkorDefterimSys.KlavyeKapat();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.onay_kodu_sure_bitti));
						} else {
							lblOnayKoduKalanSureAciklama.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
							EPostaOnayKoduKalanSure--;
						}
					}
				});
			}
		};
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "EPostaGonder":
					if(JSONSonuc.getBoolean("Sonuc")) {
						txtOnayKodu.clearText();
						AkorDefterimSys.KlavyeKapat();

						EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
						EPostaOnayKoduSayac = new Timer();
						EmailOnayKoduDialogSayac_Ayarla();
						EPostaOnayKoduSayac.schedule(EPostaOnayKoduDialogSayac, 10, 1000);

						ADDialog_Sonuc = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								getString(R.string.onay_kodu),
								getString(R.string.onay_kodu_bilgilendirme_email, EPosta),
								activity.getString(R.string.tamam));
						ADDialog_Sonuc.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_Sonuc.show();

						ADDialog_Sonuc.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_Sonuc.dismiss();
							}
						});
					} else {
						if (EPostaOnayKoduSayac != null) {
							EPostaOnayKoduSayac.cancel();
							EPostaOnayKoduSayac = null;
							EPostaOnayKoduKalanSure = 0;
							lblOnayKoduKalanSureAciklama.setText(getString(R.string.kalan_sure, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
						}

						AkorDefterimSys.KlavyeKapat();

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

					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			if(EPostaOnayKoduKalanSure > 0) { // Eğer kalan süre hala bitmemişse
				if(txtOnayKodu.getText().toString().equals(OnayKodu)) {
					Intent mIntent = new Intent(activity, Parola_Belirle.class);
					mIntent.putExtra("Islem", Islem);
					mIntent.putExtra("EPosta", EPosta);

					AkorDefterimSys.EkranGetir(mIntent, "Slide");
				}
				else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.girilen_onay_kodu_hata2));
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.onay_kodu_sure_bitti));
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}