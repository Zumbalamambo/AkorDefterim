package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.github.ybq.android.spinkit.SpinKitView;
import com.hbb20.CountryCodePicker;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabina_Eris_Sms_Gonder extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
	Timer TDogrulamaKoduSayac;
	TimerTask TTDogrulamaKoduSayac;
	Handler DogrulamaKoduHandler = new Handler();
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	SpinKitView SKVLoader;
	TextInputLayout txtILCepTelefon;
	EditText txtCepTelefon;
	TextView lblBaslik, lblSmsGonder, lblHesabiniBulSmsGonderAciklama;
	CountryCodePicker CCPTelKodu;

	int KalanSure = 0;

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

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			// prefSMSGonderiTarihi isimli prefkey var mı?
			if(sharedPref.contains("prefSMSGonderiTarihi")) { // Varsa
				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
					PDIslem.show();
				}

				AkorDefterimSys.TarihSaatGetir("TarihSaatOgren_GeriSayimaBasla");
			}
		} else {
			if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
				ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
						getString(R.string.hata),
						getString(R.string.internet_baglantisi_saglanamadi),
						activity.getString(R.string.tamam),
						"ADDialog_Kapat_GeriGit");
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog.show();
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

							lblSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
							AkorDefterimSys.setTextViewHTML(lblSmsGonder);

							TDogrulamaKoduSayac.schedule(TTDogrulamaKoduSayac, 10, 1000);
						} else {
							KalanSure = 0;

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
							SKVLoader.setVisibility(View.GONE);
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
                        } else {
							if(KalanSure > 0 && KalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_sms_telebinde_bulunamazsiniz));

								btnIleri.setEnabled(true);
								SKVLoader.setVisibility(View.GONE);
								AkorDefterimSys.DismissProgressDialog(PDIslem);
							} else AkorDefterimSys.SMSGonder(CCPTelKodu.getSelectedCountryCode(), txtCepTelefon.getText().toString().trim(), getString(R.string.sms_parola_gonderildi_icerik, JSONSonuc.getString("AdSoyad"), JSONSonuc.getString("Parola"), getString(R.string.uygulama_adi)));
                        }
					} else {
						btnIleri.setEnabled(true);
						SKVLoader.setVisibility(View.GONE);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));
					}

                    break;
				case "SMSGonder":
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_DevamEt"); // Öncelikle sistem saatini öğreniyoruz. Daha sonra SMS gönderdikten sonra sistem tarihini kullanıcıya kaydediyoruz..
					else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.sms_gonderilemedi),
									getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(CCPTelKodu.getSelectedCountryCode(), txtCepTelefon.getText().toString().trim())),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "TarihSaatGetir_DevamEt":
					btnIleri.setEnabled(true);
					SKVLoader.setVisibility(View.GONE);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefSMSGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.sms_gonderildi),
									getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(CCPTelKodu.getSelectedCountryCode(), txtCepTelefon.getText().toString().trim())),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}

						if (TDogrulamaKoduSayac != null) {
							TDogrulamaKoduSayac.cancel();
							TDogrulamaKoduSayac = null;
							TTDogrulamaKoduSayac.cancel();
							TTDogrulamaKoduSayac = null;
						}

						TDogrulamaKoduSayac = new Timer();
						DogrulamaKoduSayac_Ayarla();
						KalanSure = AkorDefterimSys.SMSGondermeToplamSure;

						lblSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
						AkorDefterimSys.setTextViewHTML(lblSmsGonder);

						TDogrulamaKoduSayac.schedule(TTDogrulamaKoduSayac, 10, 1000);
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
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
			txtCepTelefon.setText(txtCepTelefon.getText().toString().trim());
			String CepTelefon = txtCepTelefon.getText().toString();

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
				btnIleri.setEnabled(false);
				SKVLoader.setVisibility(View.VISIBLE);
				AkorDefterimSys.UnFocusEditText(txtCepTelefon);

				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
					PDIslem.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, "", CCPTelKodu.getSelectedCountryCode(), CepTelefon, "HesapBilgiGetir");
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
								TTDogrulamaKoduSayac.cancel();
								TTDogrulamaKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGonderiTarihi");
							sharedPrefEditor.apply();

							lblSmsGonder.setText(getString(R.string.sms_gonder));
						} else {
							lblSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
							AkorDefterimSys.setTextViewHTML(lblSmsGonder);
							KalanSure--;
						}
					}
				});
			}
		};
	}
}