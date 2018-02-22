package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.github.ybq.android.spinkit.SpinKitView;
import com.hbb20.CountryCodePicker;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabina_Eris_Sms_Gonder extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	TextInputLayout txtILCepTelefon;
	EditText txtCepTelefon;
	TextView lblBaslik, lblSmsGonder, lblHesabiniBulSmsGonderAciklama;
	CountryCodePicker CCPTelKodu;

	String YazilanCepTelefonu = "";
	int SMSKalanSure = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabini_bul_sms_gonder);

		activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
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
				AkorDefterimSys.EkranKapat();
			}
		});

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setOnClickListener(this);

		lblSmsGonder = findViewById(R.id.lblSmsGonder);
		lblSmsGonder.setTypeface(YaziFontu, Typeface.BOLD);
		lblSmsGonder.setText(lblSmsGonder.getText().toString().toUpperCase());

		CCPTelKodu = findViewById(R.id.CCPTelKodu);
		CCPTelKodu.setCountryPreference(Locale.getDefault().getCountry());
		CCPTelKodu.setTypeFace(YaziFontu, Typeface.NORMAL);

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
		/*txtCepTelefon.addTextChangedListener(new TextWatcher() {
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
		});*/

		final MaskedTextChangedListener txtCepTelefonListener = new MaskedTextChangedListener(
				"([000]) [000] [00] [00]",
				true,
				txtCepTelefon,
				null,
				new MaskedTextChangedListener.ValueListener() {
					@Override
					public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {
						YazilanCepTelefonu = extractedValue;
						//Log.d(CepTelefonu_Degistir.class.getSimpleName(), String.valueOf(maskFilled));
					}
				}
		);

		txtCepTelefon.addTextChangedListener(txtCepTelefonListener);
		txtCepTelefon.setOnFocusChangeListener(txtCepTelefonListener);
		txtCepTelefon.setHint(txtCepTelefonListener.placeholder());

		lblHesabiniBulSmsGonderAciklama = findViewById(R.id.lblHesabiniBulSmsGonderAciklama);
		lblHesabiniBulSmsGonderAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblHesabiniBulSmsGonderAciklama.setText(getString(R.string.hesabini_bul_sms_gonder_aciklama));
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			// prefSMSGonderiTarihi isimli prefkey var mı?
			if(sharedPref.contains("prefSMSGonderiTarihi")) { // Varsa
				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
					PDIslem.show();
				}

				AkorDefterimSys.TarihSaatGetir(activity, "TarihSaatOgren_GeriSayimaBasla");
			}
		}
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		super.onDestroy();
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
				case "TarihSaatOgren_GeriSayimaBasla":
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefSMSGonderiTarihi", ""),"Saniye");

						if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
							AkorDefterimSys.ZamanlayiciBaslat(activity, "Countdown", 1000, SMSKalanSure * 1000, "ZamanlayiciBaslat_SMSKalanSure");
							lblSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
						} else {
							SMSKalanSure = 0;

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGonderiTarihi");
							sharedPrefEditor.apply();

							lblSmsGonder.setText(getString(R.string.sms_gonder));
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_GeriGit");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "HesapBilgiGetir":
					if(JSONSonuc.getBoolean("Sonuc")) {
                        if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							btnIleri.setEnabled(true);
							AkorDefterimSys.DismissProgressDialog(PDIslem);

							if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
								ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.hesap_durumu),
										getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
										activity.getString(R.string.tamam),
										"ADDialog_Kapat_GeriGit");
								ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog.show();
							}
                        } else AkorDefterimSys.SMSGonder(CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu, getString(R.string.sms_parola_gonderildi_icerik, JSONSonuc.getString("AdSoyad"), JSONSonuc.getString("Parola"), getString(R.string.uygulama_adi)));
					} else {
						btnIleri.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));
					}

                    break;
				case "SMSGonder":
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir(activity, "TarihSaatGetir_DevamEt"); // Öncelikle sistem saatini öğreniyoruz. Daha sonra SMS gönderdikten sonra sistem tarihini kullanıcıya kaydediyoruz..
					else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.sms_gonderilemedi),
									getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu)),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "TarihSaatGetir_DevamEt":
					btnIleri.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefSMSGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

						AkorDefterimSys.ZamanlayiciBaslat(activity, "Countdown", 1000, SMSKalanSure * 1000, "ZamanlayiciBaslat_SMSKalanSure");
						lblSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.sms_gonderildi),
									getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu)),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					break;
				case "ZamanlayiciBaslat_SMSKalanSure":
					if(JSONSonuc.getString("Method").equals("Tick")) {
						SMSKalanSure--;
						lblSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
					} else if(JSONSonuc.getString("Method").equals("Finish")) {
						SMSKalanSure = 0;

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.remove("prefSMSGonderiTarihi");
						sharedPrefEditor.apply();

						lblSmsGonder.setText(getString(R.string.sms_gonder));
					}
					break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					break;
				case "ADDialog_Kapat_GeriGit":
					AkorDefterimSys.DismissAlertDialog(ADDialog);

					finish();
					break;
				case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					finish();
					break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			if(TextUtils.isEmpty(YazilanCepTelefonu)) {
				txtILCepTelefon.setError(getString(R.string.hata_bos_alan));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else if(!AkorDefterimSys.isValid(YazilanCepTelefonu, "CepTelefonu")) {
				txtILCepTelefon.setError(getString(R.string.hata_cep_telefonu));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else if(YazilanCepTelefonu.length() != 10) {
				txtILCepTelefon.setError(getString(R.string.hata_basinda_sifir_olmadan_10_hane));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else if(!YazilanCepTelefonu.subSequence(0, 1).equals("5")) {
				txtILCepTelefon.setError(getString(R.string.hata_basinda_sifir_olmadan_10_hane));
				txtCepTelefon.requestFocus();
				txtCepTelefon.setSelection(txtCepTelefon.length());
				imm.showSoftInput(txtCepTelefon, 0);
			} else txtILCepTelefon.setError(null);

			if(txtILCepTelefon.getError() == null) {
				if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure)
					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_sms_telebinde_bulunamazsiniz));
				else {
					btnIleri.setEnabled(false);
					AkorDefterimSys.UnFocusEditText(txtCepTelefon);

					if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
						PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
						PDIslem.show();
					}

					AkorDefterimSys.HesapBilgiGetir(null, "", CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu, "HesapBilgiGetir");
				}
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}