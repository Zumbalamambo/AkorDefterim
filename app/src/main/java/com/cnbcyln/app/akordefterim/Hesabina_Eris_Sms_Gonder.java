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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpUlkeKodlari;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfUlkeKodlari;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.github.ybq.android.spinkit.SpinKitView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabina_Eris_Sms_Gonder extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog_HesapDurumu, ADDialog_SMS_Gonder;
	Timer TSMSGondermeSayac;
	TimerTask TTSMSGondermeSayac;
	Handler SMSGondermeHandler = new Handler();
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	SpinKitView SKVLoader;
	TextInputLayout txtILCepTelefon;
	EditText txtCepTelefon;
	TextView lblBaslik, lblSmsGonder, lblHesabiniBulSmsGonderAciklama;
	Spinner spnUlkeKodlari;

	int SMSGondermeKalanSure = 0;
	String TelKodu = "", CepTelefon = "";
	List<SnfUlkeKodlari> snfUlkeKodlari;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabini_bul_sms_gonder);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

        sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

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

		SKVLoader = findViewById(R.id.SKVLoader);

		lblSmsGonder = findViewById(R.id.lblSmsGonder);
		lblSmsGonder.setTypeface(YaziFontu, Typeface.BOLD);
		lblSmsGonder.setText(lblSmsGonder.getText().toString().toUpperCase());

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
	protected void onStart() {
		super.onStart();

		if (TSMSGondermeSayac != null) {
			TSMSGondermeSayac.cancel();
			TSMSGondermeSayac = null;
			TTSMSGondermeSayac.cancel();
			TTSMSGondermeSayac = null;
		}

		TSMSGondermeSayac = new Timer();
		TSMSGondermeSayac_Ayarla();

		if(sharedPref.getInt("prefSMSGondermeKalanSure", 0) > 0) {
			SMSGondermeKalanSure = sharedPref.getInt("prefSMSGondermeKalanSure", 0);
			TSMSGondermeSayac.schedule(TTSMSGondermeSayac, 10, 1000);
		} else
			SMSGondermeKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
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

							btnIleri.setEnabled(true);
							SKVLoader.setVisibility(View.GONE);
                        } else {
							if(SMSGondermeKalanSure > 0 && SMSGondermeKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_sms_telebinde_bulunamazsiniz));

								btnIleri.setEnabled(true);
								SKVLoader.setVisibility(View.GONE);
							} else AkorDefterimSys.SMSGonder(TelKodu, CepTelefon, getString(R.string.sms_parola_gonderildi_icerik, JSONSonuc.getString("AdSoyad"), JSONSonuc.getString("Parola"), getString(R.string.uygulama_adi)));
                        }
					} else {
						btnIleri.setEnabled(true);
						SKVLoader.setVisibility(View.GONE);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));
					}

                    break;
				case "ADDialog_HesapDurumu_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
					break;
				case "SMSGonder":
					btnIleri.setEnabled(true);
					SKVLoader.setVisibility(View.GONE);

					if(JSONSonuc.getBoolean("Sonuc")) {
						ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.sms_gonderildi),
								getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
								activity.getString(R.string.tamam),
								"ADDialog_SMS_Gonder_Kapat");
						ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SMS_Gonder.show();

						TSMSGondermeSayac = new Timer();
						TSMSGondermeSayac_Ayarla();
						SMSGondermeKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

						sharedPrefEditor = sharedPref.edit();
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
		if(AkorDefterimSys.InternetErisimKontrolu()) {
			btnIleri.setEnabled(false);
			SKVLoader.setVisibility(View.VISIBLE);
			AkorDefterimSys.KlavyeKapat();

			txtCepTelefon.setText(txtCepTelefon.getText().toString().trim());
			CepTelefon = txtCepTelefon.getText().toString();

			TelKodu = snfUlkeKodlari.get(spnUlkeKodlari.getSelectedItemPosition()).getUlkeKodu();

			if(TextUtils.isEmpty(CepTelefon)) {
				txtILCepTelefon.setError(getString(R.string.hata_bos_alan));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else if(!StringUtils.isNumeric(CepTelefon)) {
				txtILCepTelefon.setError(getString(R.string.hata_format_sadece_sayi));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else if(CepTelefon.length() != 10) {
				txtILCepTelefon.setError(getString(R.string.hata_basinda_sifir_olmadan_10_hane));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else if(!CepTelefon.subSequence(0, 1).equals("5")) {
				txtILCepTelefon.setError(getString(R.string.hata_basinda_sifir_olmadan_10_hane));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else txtILCepTelefon.setError(null);

			if(txtILCepTelefon.getError() == null) {
				AkorDefterimSys.UnFocusEditText(txtCepTelefon);

				AkorDefterimSys.HesapBilgiGetir(null, "", TelKodu, CepTelefon, "HesapBilgiGetir");
			} else {
				btnIleri.setEnabled(true);
				SKVLoader.setVisibility(View.GONE);
			}
		} else {
			btnIleri.setEnabled(true);
			SKVLoader.setVisibility(View.GONE);
			AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
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
								TTSMSGondermeSayac.cancel();
								TTSMSGondermeSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGondermeKalanSure");
							sharedPrefEditor.apply();

							lblSmsGonder.setText(getString(R.string.telefon_numarasi));
						} else {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
							sharedPrefEditor.apply();

							lblSmsGonder.setText(getString(R.string.telefon_numarasi2, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							SMSGondermeKalanSure--;
						}
					}
				});
			}
		};
	}
}