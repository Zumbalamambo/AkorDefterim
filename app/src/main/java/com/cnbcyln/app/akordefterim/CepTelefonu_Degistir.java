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
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class CepTelefonu_Degistir extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog_HesapDurumu, ADDialog_CepTelefonKalanSure, ADDialog_Hata;
	ProgressDialog PDCepTelefonKontrolIslem;
	InputMethodManager imm;
	Random rnd;
	Timer TCepTelefonOnayKoduSayac;
	TimerTask TTCepTelefonOnayKoduSayac;
	Handler CepTelefonOnayKoduHandler = new Handler();

	CoordinatorLayout coordinatorLayout;
	ImageButton btnIptal, btnKaydet;
	LinearLayout LLTelKodu;
	TextInputLayout txtILCepTelefon;
	EditText txtCepTelefon;
	TextView lblBaslik, lblTelKodu, lblCepTelefonBasvuruKalanSure;

	String OnayKodu = "";
	int SMSGondermeKalanSure = 0;
	private static final int SMSOKUMA_IZIN = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ceptelefon_degistir);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
		rnd = new Random();

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

		LLTelKodu = findViewById(R.id.LLTelKodu);
		LLTelKodu.setOnClickListener(this);

		lblTelKodu = findViewById(R.id.lblTelKodu);
		lblTelKodu.setTypeface(YaziFontu, Typeface.BOLD);

		txtILCepTelefon = findViewById(R.id.txtILCepTelefon);
		txtILCepTelefon.setTypeface(YaziFontu);

		txtCepTelefon = findViewById(R.id.txtCepTelefon);
		txtCepTelefon.setTypeface(YaziFontu, Typeface.NORMAL);
		//txtEPosta.setText(sharedPref.getString("prefEPosta",""));
		txtCepTelefon.addTextChangedListener(new TextWatcher() {
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
		});
		txtCepTelefon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Kaydet();
				}

				return false;
			}
		});

		lblCepTelefonBasvuruKalanSure = findViewById(R.id.lblCepTelefonBasvuruKalanSure);
		lblCepTelefonBasvuruKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(!AkorDefterimSys.GirisYapildiMi()) AkorDefterimSys.CikisYap();
		else {
			if(sharedPref.getString("prefAction", "").equals("IslemTamamlandi")) onBackPressed();
			else {
				if(!AkorDefterimSys.ProgressDialogisShowing(PDCepTelefonKontrolIslem)) { // Eğer progress dialog açık değilse
					PDCepTelefonKontrolIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDCepTelefonKontrolIslem_Timeout");
					PDCepTelefonKontrolIslem.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID", ""), "", "", "HesapBilgiGetir_CepTelefon_Bilgisi_Al");

				if(sharedPref.getInt("prefSMSGondermeKalanSure", 0) > 0) {
					if (TCepTelefonOnayKoduSayac != null) {
						TCepTelefonOnayKoduSayac.cancel();
						TCepTelefonOnayKoduSayac = null;
						TTCepTelefonOnayKoduSayac.cancel();
						TTCepTelefonOnayKoduSayac = null;
					}

					TCepTelefonOnayKoduSayac = new Timer();
					CepTelefonOnayKoduDialogSayac_Ayarla();
					SMSGondermeKalanSure = sharedPref.getInt("prefSMSGondermeKalanSure", 0);
					lblCepTelefonBasvuruKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
					AkorDefterimSys.setTextViewHTML(lblCepTelefonBasvuruKalanSure);
					lblCepTelefonBasvuruKalanSure.setVisibility(View.VISIBLE);

					TCepTelefonOnayKoduSayac.schedule(TTCepTelefonOnayKoduSayac, 10, 1000);
				} else lblCepTelefonBasvuruKalanSure.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_CepTelefonKalanSure);
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_CepTelefonKalanSure);
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

		super.onDestroy();
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
				sharedPrefEditor = sharedPref.edit();
				sharedPrefEditor.remove("prefAction");
				sharedPrefEditor.apply();

				onBackPressed();

				break;
			case R.id.btnKaydet:
				Kaydet();

				break;
			case R.id.LLTelKodu:


				break;
			case R.id.lblTelKodu:


				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case SMSOKUMA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 6 haneli onay kodu oluşturuldu
					OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

					// Onay kodu belirtilen eposta adresine gönderiliyor
					AkorDefterimSys.SMSGonder(lblTelKodu.getText().toString().substring(1,lblTelKodu.getText().length()), txtCepTelefon.getText().toString().trim(), getString(R.string.sms_dosrulama_kodu_icerik, OnayKodu, getString(R.string.uygulama_adi)));
				} else {
					AkorDefterimSys.DismissProgressDialog(PDCepTelefonKontrolIslem);

					if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
						ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.uygulama_izinleri),
								getString(R.string.uygulama_izni_sms_alma_hata),
								getString(R.string.tamam), "ADDialog_Hata_Kapat");
						ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_Hata.show();
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
				case "HesapBilgiGetir_CepTelefon_Bilgisi_Al":
					if(JSONSonuc.getBoolean("Sonuc")) {
						AkorDefterimSys.DismissProgressDialog(PDCepTelefonKontrolIslem);

						if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
								ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.hesap_durumu),
										getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
										activity.getString(R.string.tamam),
										"ADDialog_HesapDurumu_Kapat");
								ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog_HesapDurumu.show();
							}
						} else {
							lblTelKodu.setText(String.format("%s%s", "+", JSONSonuc.getString("TelKodu")));
							txtCepTelefon.setText(JSONSonuc.getString("CepTelefon"));
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
							ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hesap_durumu),
									getString(R.string.hesap_bilgileri_bulunamadi),
									activity.getString(R.string.tamam),
									"ADDialog_HesapDurumu_Kapat_GeriGit");
							ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_HesapDurumu.show();
						}
					}

					break;
				case "HesapBilgiGetir_CepTelefon_Kontrol":
					btnKaydet.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDCepTelefonKontrolIslem);

					if(JSONSonuc.getBoolean("Sonuc")) { // Eğer yazılan e-posta adresi kayıtlı ise
						// Bizim ID, kayıtlı olan cep telefonu sahibinin ID'si ile aynı DEĞİL ise
						if(!sharedPref.getString("prefHesapID","").equals(JSONSonuc.getString("HesapID")))
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.ceptelefonu_kayitli));
						else { // Aynı ise
							if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
								if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
									ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
											getString(R.string.hesap_durumu),
											getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
											activity.getString(R.string.tamam),
											"ADDialog_HesapDurumu_Kapat");
									ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
									ADDialog_HesapDurumu.show();
								}
							} else onBackPressed(); // Bu durumda aynı ceptelefonu_kayitli olduğu için güncel demek oluyor. Hiçbir işlem yapmıyoruz..
						}
					} else { // Yazılan ceptelefonu_kayitli kayıtlı değil ise

						if(SMSGondermeKalanSure > 0 && SMSGondermeKalanSure < AkorDefterimSys.SMSGondermeToplamSure) { // Eğer kalan süre hala bitmemişse
							if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_CepTelefonKalanSure)) {
								ADDialog_CepTelefonKalanSure = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.dogrulama_kodu),
										getString(R.string.yeni_onay_kodu_talebi_hata),
										activity.getString(R.string.tamam),
										"ADDialog_CepTelefonKalanSure_Kapat");
								ADDialog_CepTelefonKalanSure.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog_CepTelefonKalanSure.show();
							}
						} else {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								if (activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
                                    activity.requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, SMSOKUMA_IZIN);
                                else {
									// 6 haneli onay kodu oluşturuldu
									OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

									// Onay kodu belirtilen eposta adresine gönderiliyor
									AkorDefterimSys.SMSGonder(lblTelKodu.getText().toString().substring(1,lblTelKodu.getText().length()), txtCepTelefon.getText().toString().trim(), getString(R.string.sms_dosrulama_kodu_icerik, OnayKodu, getString(R.string.uygulama_adi)));
                                }
							}
						}
					}

					break;
				case "ADDialog_HesapDurumu_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

					AkorDefterimSys.CikisYap();
					break;
				case "ADDialog_HesapDurumu_Kapat_GeriGit":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

					onBackPressed();
					break;
				case "PDCepTelefonKontrolIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDCepTelefonKontrolIslem);

					onBackPressed();
					break;
				case "SMSGonder":
					btnKaydet.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDCepTelefonKontrolIslem);

					// Eğer SMS gönderildiyse sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
						Intent mIntent = new Intent(activity, Dogrulama_Kodu.class);
						mIntent.putExtra("Islem", "CepTelefonuDegisikligi");
						mIntent.putExtra("TelKodu", lblTelKodu.getText().toString().substring(1,lblTelKodu.getText().length()));
						mIntent.putExtra("CepTelefonu", txtCepTelefon.getText().toString());
						mIntent.putExtra("OnayKodu", String.valueOf(OnayKodu));

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

					break;
				case "ADDialog_CepTelefonKalanSure_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_CepTelefonKalanSure);
					break;
				case "ADDialog_Hata_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void Kaydet() {
		if(AkorDefterimSys.InternetErisimKontrolu()) {
			btnKaydet.setEnabled(false);
			AkorDefterimSys.KlavyeKapat();

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
				AkorDefterimSys.UnFocusEditText(txtCepTelefon);

				if(!AkorDefterimSys.ProgressDialogisShowing(PDCepTelefonKontrolIslem)) { // Eğer progress dialog açık değilse
					PDCepTelefonKontrolIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDCepTelefonKontrolIslem_Timeout");
					PDCepTelefonKontrolIslem.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, "", lblTelKodu.getText().toString().substring(1,lblTelKodu.getText().length()), CepTelefon, "HesapBilgiGetir_CepTelefon_Kontrol");
			} else btnKaydet.setEnabled(true);
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}

	public void CepTelefonOnayKoduDialogSayac_Ayarla() {
		TTCepTelefonOnayKoduSayac = new TimerTask() {
			@Override
			public void run() {
				CepTelefonOnayKoduHandler.post(new Runnable() {
					public void run() {
						if (SMSGondermeKalanSure == 0) {
							if (TCepTelefonOnayKoduSayac != null) {
								TCepTelefonOnayKoduSayac.cancel();
								TCepTelefonOnayKoduSayac = null;
								TTCepTelefonOnayKoduSayac.cancel();
								TTCepTelefonOnayKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGondermeKalanSure");
							sharedPrefEditor.apply();

							lblCepTelefonBasvuruKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblCepTelefonBasvuruKalanSure);
							lblCepTelefonBasvuruKalanSure.setVisibility(View.GONE);

							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitti_yeniden_basvuru_yapilabilir));
						} else {
							lblCepTelefonBasvuruKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							AkorDefterimSys.setTextViewHTML(lblCepTelefonBasvuruKalanSure);
							SMSGondermeKalanSure--;
						}
					}
				});
			}
		};
	}
}