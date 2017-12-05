package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpUlkeKodlari;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfUlkeKodlari;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabina_Eris_Sms_Gonder extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
	SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
	AlertDialog ADDialog_HesapDurumu, ADDialog_SMS_Gonder;
	Timer TSMSGondermeSayac;
	TimerTask TTSMSGondermeSayac;
	Handler SMSGondermeHandler = new Handler();

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnIleri;
	SpinKitView SKVIleri;
	TextInputLayout txtILCepTelefon;
	EditText txtCepTelefon;
	TextView lblBaslik, lblCepTelefon, lblHesabiniBulSmsGonderAciklama;
	Spinner spnUlkeKodlari;

	int SMSGondermeKalanSure = 0;
	String TelKodu, CepTelefon;
	List<SnfUlkeKodlari> snfUlkeKodlari;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabini_bul_sms_gonder);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		TSMSGondermeSayac = new Timer();
		TSMSGondermeSayac_Ayarla();

		if(sharedPref.getInt("prefSMSGondermeKalanSure", 0) > 0) {
			SMSGondermeKalanSure = sharedPref.getInt("prefSMSGondermeKalanSure", 0);
			TSMSGondermeSayac.schedule(TTSMSGondermeSayac, 10, 1000);
		} else
			SMSGondermeKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILCepTelefon.setError(null);
				AkorDefterimSys.UnFocusEditText(txtCepTelefon);
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

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setOnClickListener(this);

		SKVIleri = findViewById(R.id.SKVIleri);

		lblCepTelefon = findViewById(R.id.lblCepTelefon);
		lblCepTelefon.setTypeface(YaziFontu, Typeface.BOLD);
		lblCepTelefon.setText(lblCepTelefon.getText().toString().toUpperCase());

		spnUlkeKodlari = findViewById(R.id.spnUlkeKodlari);
		snfUlkeKodlari = AkorDefterimSys.UlkeKodlariniGetir();
		AdpUlkeKodlari adpUlkeKodlari = new AdpUlkeKodlari(activity, snfUlkeKodlari);
		spnUlkeKodlari.setAdapter(adpUlkeKodlari);

		txtILCepTelefon = findViewById(R.id.txtILCepTelefon);
		txtILCepTelefon.setTypeface(YaziFontu);

		txtCepTelefon = findViewById(R.id.txtCepTelefon);
		txtCepTelefon.setTypeface(YaziFontu, Typeface.NORMAL);
		txtCepTelefon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					IleriIslem();
				}

				return false;
			}
		});
		txtCepTelefon.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtCepTelefon.setError(null);
			}
		});

		lblHesabiniBulSmsGonderAciklama = findViewById(R.id.lblHesabiniBulSmsGonderAciklama);
		lblHesabiniBulSmsGonderAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblHesabiniBulSmsGonderAciklama.setText(getString(R.string.hesabini_bul_sms_gonder_aciklama));
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
				case "HesapBilgiGetir":
					if(JSONSonuc.getBoolean("Sonuc")) {
                        if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hesap_durumu),
									getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
									activity.getString(R.string.tamam),
									"ADDialog_HesapDurumu_Kapat");
							ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_HesapDurumu.show();

							SKVIleri.setVisibility(View.GONE);
							btnIleri.setVisibility(View.VISIBLE);
                        } else {
							if (JSONSonuc.getString("CepTelefonOnay").equals("1")) {
								if(SMSGondermeKalanSure > 0 && SMSGondermeKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
									SKVIleri.setVisibility(View.GONE);
									btnIleri.setVisibility(View.VISIBLE);

									AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_sms_telebinde_bulunamazsiniz));
								} else AkorDefterimSys.SMSGonder(TelKodu, CepTelefon, getString(R.string.sms_parola_gonderildi_icerik, JSONSonuc.getString("AdSoyad"), JSONSonuc.getString("Parola"), getString(R.string.uygulama_adi)));
							} else {
								ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.hata),
										getString(R.string.hesabini_bul_ceptelefon_onay_hata),
										activity.getString(R.string.tamam),
										"ADDialog_SMS_Gonder_Kapat");
								ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog_SMS_Gonder.show();

								SKVIleri.setVisibility(View.GONE);
								btnIleri.setVisibility(View.VISIBLE);
							}
                        }
					} else {
						SKVIleri.setVisibility(View.GONE);
						btnIleri.setVisibility(View.VISIBLE);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));
					}

                    break;
				case "ADDialog_HesapDurumu_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
					break;
				case "SMSGonder":
					SKVIleri.setVisibility(View.GONE);
					btnIleri.setVisibility(View.VISIBLE);

					if(JSONSonuc.getBoolean("Sonuc")) {
						ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.sms_gonderildi),
								getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
								activity.getString(R.string.tamam),
								"ADDialog_SMS_Gonder_Kapat");
						ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SMS_Gonder.show();

						sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
						sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
						sharedPrefEditor.apply();

						TSMSGondermeSayac.schedule(TTSMSGondermeSayac, 10, 1000);
					} else {
						ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.sms_gonderilemedi),
								getString(R.string.sms_gonderilemedi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
								activity.getString(R.string.tamam),
								"ADDialog_SMS_Gonder_Kapat");
						ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SMS_Gonder.show();
					}

					break;
				case "ADDialog_SMS_Gonder_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_SMS_Gonder);
					break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		txtCepTelefon.setText(txtCepTelefon.getText().toString().trim());
		CepTelefon = txtCepTelefon.getText().toString();

		TelKodu = snfUlkeKodlari.get(spnUlkeKodlari.getSelectedItemPosition()).getUlkeKodu();

		if(TextUtils.isEmpty(CepTelefon))
			txtILCepTelefon.setError(getString(R.string.hata_bos_alan));
		else if(!TextUtils.isEmpty(CepTelefon) && !AkorDefterimSys.isValid(CepTelefon, "SadeceSayi")) {
			txtILCepTelefon.setError(getString(R.string.txtceptelefon_hata2));
		} else if(!TextUtils.isEmpty(CepTelefon) && CepTelefon.length() != 10) {
			txtILCepTelefon.setError(getString(R.string.txtceptelefon_hata3));
		} else if(!TextUtils.isEmpty(CepTelefon) && !CepTelefon.subSequence(0, 1).equals("5")) {
			txtILCepTelefon.setError(getString(R.string.txtceptelefon_hata3));
		} else {
			txtILCepTelefon.setError(null);
		}

		AkorDefterimSys.UnFocusEditText(txtCepTelefon);

		if(txtILCepTelefon.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				SKVIleri.setVisibility(View.VISIBLE);
				btnIleri.setVisibility(View.GONE);

				AkorDefterimSys.HesapBilgiGetir(null, TelKodu, CepTelefon);
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	public void TSMSGondermeSayac_Ayarla() {
		TTSMSGondermeSayac = new TimerTask() {
			@Override
			public void run() {
				SMSGondermeHandler.post(new Runnable() {
					public void run() {
						if (SMSGondermeKalanSure == 0) {
							if (TSMSGondermeSayac != null) {
								TSMSGondermeSayac.cancel();
								TSMSGondermeSayac = null;
							}

							sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
							sharedPrefEditor.remove("prefSMSGondermeKalanSure");
							sharedPrefEditor.apply();

							lblCepTelefon.setText(getString(R.string.telefon_numarasi));
						} else {
							sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
							sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
							sharedPrefEditor.apply();

							lblCepTelefon.setText(getString(R.string.telefon_numarasi2, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							SMSGondermeKalanSure--;
						}
					}
				});
			}
		};
	}
}