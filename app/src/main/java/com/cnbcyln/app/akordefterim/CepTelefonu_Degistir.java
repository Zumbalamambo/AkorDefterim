package com.cnbcyln.app.akordefterim;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.hbb20.CountryCodePicker;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class CepTelefonu_Degistir extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnIptal, btnKaydet;
	CountryCodePicker CCPTelKodu;
	TextInputLayout txtILCepTelefon;
	EditText txtCepTelefon;
	TextView lblBaslik, lblKalanSure;

	String DogrulamaKodu = "", YazilanCepTelefonu = "";
	int SMSKalanSure = 0;
	private static final int SMSOKUMA_IZIN = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ceptelefon_degistir);

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
		coordinatorLayout.setOnClickListener(this);

		btnIptal = findViewById(R.id.btnIptal);
		btnIptal.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		btnKaydet = findViewById(R.id.btnKaydet);
		btnKaydet.setOnClickListener(this);

		CCPTelKodu = findViewById(R.id.CCPTelKodu);
		CCPTelKodu.setCountryPreference(Locale.getDefault().getCountry());
		CCPTelKodu.setTypeFace(YaziFontu, Typeface.NORMAL);

		txtILCepTelefon = findViewById(R.id.txtILCepTelefon);
		txtILCepTelefon.setTypeface(YaziFontu);

		txtCepTelefon = findViewById(R.id.txtCepTelefon);
		txtCepTelefon.setTypeface(YaziFontu, Typeface.NORMAL);
		//txtEPosta.setText(sharedPref.getString("prefEPosta",""));
		/*txtCepTelefon.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILCepTelefon.setError(null);
			}
		});*/
		txtCepTelefon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Kaydet();
				}

				return false;
			}
		});

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

		lblKalanSure = findViewById(R.id.lblKalanSure);
		lblKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
		lblKalanSure.setVisibility(View.GONE);
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;

		if(AkorDefterimSys.GirisYapildiMi()) {
			if(AkorDefterimSys.prefAction.equals("IslemTamamlandi")) finish();
			else {
				// prefSMSGonderiTarihi isimli prefkey var mı?
				if(sharedPref.contains("prefSMSGonderiTarihi")) { // Varsa
					btnKaydet.setEnabled(false);

					if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
						PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
						PDIslem.show();
					}

					AkorDefterimSys.TarihSaatGetir(activity, "TarihSaatOgren_GeriSayimaBasla");
				} else {
					if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
						PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
						PDIslem.show();
					}

					AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID", ""), "", "", "", "", "HesapBilgiGetir_CepTelefon_Bilgisi_Al");
				}
			}
		} else AkorDefterimSys.CikisYap();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		super.onBackPressed();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				txtILCepTelefon.setError(null);
				AkorDefterimSys.UnFocusEditText(txtCepTelefon);
				break;
			case R.id.btnIptal:
				AkorDefterimSys.prefAction = "";
				AkorDefterimSys.EkranKapat();
				break;
			case R.id.btnKaydet:
				Kaydet();
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case SMSOKUMA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 6 haneli onay kodu oluşturuldu
					DogrulamaKodu = AkorDefterimSys.KodUret(6, true, false, false, false);

					// Onay kodu belirtilen eposta adresine gönderiliyor
					AkorDefterimSys.SMSGonder(CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu, getString(R.string.sms_dogrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
				} else {
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
						ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.uygulama_izinleri),
								getString(R.string.uygulama_izni_sms_alma_hata),
								getString(R.string.tamam), "ADDialog_Kapat");
						ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog.show();
					}
				}

				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "TarihSaatOgren_GeriSayimaBasla":
					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefSMSGonderiTarihi", ""),"Saniye");

						if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
							AkorDefterimSys.ZamanlayiciBaslat(activity, "Countdown", 1000, SMSKalanSure * 1000, "ZamanlayiciBaslat_SMSKalanSure");
							lblKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							lblKalanSure.setVisibility(View.VISIBLE);
						} else {
							SMSKalanSure = 0;

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGonderiTarihi");
							sharedPrefEditor.apply();

							lblKalanSure.setVisibility(View.GONE);
						}

						AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID", ""), "", "", "", "", "HesapBilgiGetir_CepTelefon_Bilgisi_Al");
					} else {
						btnIptal.setEnabled(true);
						btnKaydet.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

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
				case "HesapBilgiGetir_CepTelefon_Bilgisi_Al":
					btnIptal.setEnabled(true);
					btnKaydet.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					if(JSONSonuc.getBoolean("Sonuc")) {
						if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
								ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.hesap_durumu),
										getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
										activity.getString(R.string.tamam),
										"ADDialog_Kapat");
								ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog.show();
							}
						} else {
							if(!JSONSonuc.getString("TelKodu").equals("")) CCPTelKodu.setCountryForPhoneCode(Integer.parseInt(JSONSonuc.getString("TelKodu")));
							YazilanCepTelefonu = JSONSonuc.getString("CepTelefon");
							txtCepTelefon.setText(YazilanCepTelefonu);
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hesap_durumu),
									getString(R.string.hesap_bilgileri_bulunamadi),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_GeriGit");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "HesapBilgiGetir_CepTelefon_Kontrol":
					if(JSONSonuc.getBoolean("Sonuc")) { // Eğer yazılan e-posta adresi kayıtlı ise
						// Bizim ID, kayıtlı olan cep telefonu sahibinin ID'si ile aynı DEĞİL ise
						btnIptal.setEnabled(true);
						btnKaydet.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);
						
						if(!sharedPref.getString("prefHesapID","").equals(JSONSonuc.getString("HesapID")))
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.ceptelefonu_kayitli));
						else { // Aynı ise
							if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
								if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
									ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
											getString(R.string.hesap_durumu),
											getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
											activity.getString(R.string.tamam),
											"ADDialog_Kapat");
									ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
									ADDialog.show();
								}
							} else finish(); // Bu durumda aynı ceptelefonu_kayitli olduğu için güncel demek oluyor. Hiçbir işlem yapmıyoruz..
						}
					} else { // Yazılan ceptelefonu_kayitli kayıtlı değil ise
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
							if (activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
								btnIptal.setEnabled(true);
								btnKaydet.setEnabled(true);
								AkorDefterimSys.DismissProgressDialog(PDIslem);

								activity.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, SMSOKUMA_IZIN);
							} else {
								// 6 haneli onay kodu oluşturuldu
								DogrulamaKodu = AkorDefterimSys.KodUret(6, true, false, false, false);

								// Onay kodu belirtilen eposta adresine gönderiliyor
								AkorDefterimSys.SMSGonder(CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu, getString(R.string.sms_dogrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
							}
						} else {
							btnIptal.setEnabled(true);
							btnKaydet.setEnabled(true);
							AkorDefterimSys.DismissProgressDialog(PDIslem);
						}
					}

					break;
				case "SMSGonder":
					// Eğer SMS gönderildiyse sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir(activity, "TarihSaatGetir_DevamEt"); // Öncelikle sistem saatini öğreniyoruz. Daha sonra SMS gönderdikten sonra sistem tarihini kullanıcıya kaydediyoruz..
					else {
						btnIptal.setEnabled(true);
						btnKaydet.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);
						
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					}

					break;
				case "TarihSaatGetir_DevamEt":
					btnIptal.setEnabled(true);
					btnKaydet.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefSMSGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
						Intent mIntent = new Intent(activity, Dogrulama_Kodu.class);
						mIntent.putExtra("Islem", "CepTelefonuDegisikligi");
						mIntent.putExtra("TelKodu", CCPTelKodu.getSelectedCountryCode());
						mIntent.putExtra("CepTelefonu", YazilanCepTelefonu);
						mIntent.putExtra("DogrulamaKodu", String.valueOf(DogrulamaKodu));

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					break;
				case "ZamanlayiciBaslat_SMSKalanSure":
					if(JSONSonuc.getString("Method").equals("Tick")) {
						SMSKalanSure--;
						lblKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
						AkorDefterimSys.setTextViewHTML(lblKalanSure);
					} else if(JSONSonuc.getString("Method").equals("Finish")) {
						SMSKalanSure = 0;

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.remove("prefSMSGonderiTarihi");
						sharedPrefEditor.apply();

						lblKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
						AkorDefterimSys.setTextViewHTML(lblKalanSure);
						lblKalanSure.setVisibility(View.GONE);

						//AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitti_yeniden_basvuru_yapilabilir));
					}
					break;
				case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);
					AkorDefterimSys.EkranKapat();
					break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					break;
				case "ADDialog_Kapat_GeriGit":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					AkorDefterimSys.EkranKapat();
					break;
				case "ADDialog_Kapat_CikisYap":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					AkorDefterimSys.CikisYap();
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void Kaydet() {
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
					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
				else {
					btnIptal.setEnabled(false);
					btnKaydet.setEnabled(false);
					AkorDefterimSys.UnFocusEditText(txtCepTelefon);

					if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
						PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
						PDIslem.show();
					}

					AkorDefterimSys.HesapBilgiGetir(null, "", CCPTelKodu.getSelectedCountryCode(), YazilanCepTelefonu, "", "", "HesapBilgiGetir_CepTelefon_Kontrol");
				}
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}