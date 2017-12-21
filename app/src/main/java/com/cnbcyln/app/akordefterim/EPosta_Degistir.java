package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class EPosta_Degistir extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog_HesapDurumu, ADDialog_Hata;
	ProgressDialog PDIslem;
	InputMethodManager imm;
	Random rnd;
	Timer TDogrulamaKoduSayac;
	TimerTask TTDogrulamaKoduSayac;
	Handler DogrulamaKoduHandler = new Handler();

	CoordinatorLayout coordinatorLayout;
	ImageButton btnIptal, btnKaydet;
	TextInputLayout txtILEPosta;
	EditText txtEPosta;
	TextView lblBaslik, lblKalanSure;

	String DogrulamaKodu = "";
	int KalanSure = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eposta_degistir);

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

		txtILEPosta = findViewById(R.id.txtILEPosta);
		txtILEPosta.setTypeface(YaziFontu);

		txtEPosta = findViewById(R.id.txtEPosta);
		txtEPosta.setTypeface(YaziFontu, Typeface.NORMAL);
		txtEPosta.setText(sharedPref.getString("prefEPosta",""));
		txtEPosta.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILEPosta.setError(null);
			}
		});
		txtEPosta.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Kaydet();
				}

				return false;
			}
		});

		lblKalanSure = findViewById(R.id.lblKalanSure);
		lblKalanSure.setTypeface(YaziFontu, Typeface.NORMAL);
		lblKalanSure.setVisibility(View.GONE);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(!AkorDefterimSys.GirisYapildiMi()) AkorDefterimSys.CikisYap();
		else {
			if(sharedPref.getString("prefAction", "").equals("IslemTamamlandi")) onBackPressed();
			else {
				// prefEPostaGonderiTarihi isimli prefkey var mı?
				if(sharedPref.contains("prefEPostaGonderiTarihi")) { // Varsa
					btnIptal.setEnabled(false);
					btnKaydet.setEnabled(false);

					if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
						PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
						PDIslem.show();
					}

					AkorDefterimSys.TarihSaatGetir("TarihSaatOgren_GeriSayimaBasla");
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				txtILEPosta.setError(null);
				AkorDefterimSys.UnFocusEditText(txtEPosta);
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
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "TarihSaatOgren_GeriSayimaBasla":
					btnIptal.setEnabled(true);
					btnKaydet.setEnabled(true);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
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

							lblKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
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

					onBackPressed();
					break;
				case "ADDialog_Hata_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

					onBackPressed();
					break;
				case "HesapBilgiGetir":
					if(JSONSonuc.getBoolean("Sonuc")) { // Eğer yazılan e-posta adresi kayıtlı ise
						btnIptal.setEnabled(true);
						btnKaydet.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						// Bizim ID, kayıtlı olan e-posta adresi sahibinin ID'si ile aynı DEĞİL ise
						if(!sharedPref.getString("prefHesapID","").equals(JSONSonuc.getString("HesapID")))
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.eposta_kayitli));
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
							} else onBackPressed(); // Bu durumda aynı e-posta adresi olduğu için güncel demek oluyor. Hiçbir işlem yapmıyoruz..
						}
					} else { // Yazılan e-posta adresi kayıtlı değil ise
						if(KalanSure > 0 && KalanSure < AkorDefterimSys.EPostaGondermeToplamSure) { // Eğer kalan süre hala bitmemişse
							btnIptal.setEnabled(true);
							btnKaydet.setEnabled(true);
							AkorDefterimSys.DismissProgressDialog(PDIslem);

							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yeni_onay_kodu_talebi_hata));
						} else {
							// 6 haneli onay kodu oluşturuldu
							DogrulamaKodu = String.valueOf((100000 + rnd.nextInt(900000)));

							// Onay kodu belirtilen eposta adresine gönderiliyor
							AkorDefterimSys.EPostaGonder(txtEPosta.getText().toString().trim(), "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dosrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
						}
					}

					break;
				case "ADDialog_HesapDurumu_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

					AkorDefterimSys.CikisYap();
					break;
				case "EPostaGonder":
					// Eğer EPosta gönderildiyse sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_DevamEt"); // Öncelikle sistem saatini öğreniyoruz. Daha sonra E-Posta gönderdikten sonra sistem tarihini kullanıcıya kaydediyoruz..
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
						sharedPrefEditor.putString("prefEPostaGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
						Intent mIntent = new Intent(activity, Dogrulama_Kodu.class);
						mIntent.putExtra("Islem", "EPostaDegisikligi");
						mIntent.putExtra("EPosta", txtEPosta.getText().toString().trim());
						mIntent.putExtra("DogrulamaKodu", String.valueOf(DogrulamaKodu));

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void Kaydet() {
		if(AkorDefterimSys.InternetErisimKontrolu()) {
			btnIptal.setEnabled(false);
			btnKaydet.setEnabled(false);
			AkorDefterimSys.KlavyeKapat();

			txtEPosta.setText(txtEPosta.getText().toString().trim());
			String EPosta = txtEPosta.getText().toString();

			if(TextUtils.isEmpty(EPosta))
				txtILEPosta.setError(getString(R.string.hata_bos_alan));
			else if(!AkorDefterimSys.isValid(EPosta, "EPosta"))
				txtILEPosta.setError(getString(R.string.hata_format_eposta));
			else if(AkorDefterimSys.isValid(EPosta, "FakeEPosta")) {
				txtILEPosta.setError(getString(R.string.hata_format_eposta));
			} else txtILEPosta.setError(null);

			if(txtILEPosta.getError() == null) {
				AkorDefterimSys.UnFocusEditText(txtEPosta);

				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
					PDIslem.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, "", "", EPosta, "HesapBilgiGetir");
			} else {
				btnIptal.setEnabled(true);
				btnKaydet.setEnabled(true);
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
							sharedPrefEditor.remove("prefEPostaGonderiTarihi");
							sharedPrefEditor.apply();

							lblKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							lblKalanSure.setVisibility(View.GONE);

							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitti_yeniden_basvuru_yapilabilir));
						} else {
							lblKalanSure.setText(getString(R.string.yeni_basvuru_kalan_sure, AkorDefterimSys.ZamanFormatMMSS(KalanSure)));
							AkorDefterimSys.setTextViewHTML(lblKalanSure);
							KalanSure--;
						}
					}
				});
			}
		};
	}
}