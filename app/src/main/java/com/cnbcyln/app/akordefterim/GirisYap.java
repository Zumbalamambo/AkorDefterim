package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.CustomItemClickListener;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Strings;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class GirisYap extends AppCompatActivity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
	ProgressDialog PDGirisYap;
	AlertDialog ADDialog_HesapDurumu, ADDialog_GirisHata;

	CoordinatorLayout coordinatorLayout;
	Button btnGeri, btnGirisYap;
	TextView lblBaslik, lblGirisYardimi;
	TextInputLayout txtILEPostaKullaniciAdi, txtILParola;
	EditText txtEPostaKullaniciAdi, txtParola;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_girisyap);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.
		AkorDefterimSys.EkranAnimasyon("Explode");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILEPostaKullaniciAdi.setError(null);
                AkorDefterimSys.UnFocusEditText(txtEPostaKullaniciAdi);
				txtILParola.setError(null);
				AkorDefterimSys.UnFocusEditText(txtParola);
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

		txtILEPostaKullaniciAdi = findViewById(R.id.txtILEPostaKullaniciAdi);
		txtILEPostaKullaniciAdi.setTypeface(YaziFontu);

		txtEPostaKullaniciAdi = findViewById(R.id.txtEPostaKullaniciAdi);
		txtEPostaKullaniciAdi.setTypeface(YaziFontu, Typeface.NORMAL);
		txtEPostaKullaniciAdi.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILEPostaKullaniciAdi.setError(null);
			}
		});

		txtILParola = findViewById(R.id.txtILParola);
		txtILParola.setTypeface(YaziFontu);

		txtParola = findViewById(R.id.txtParola);
		txtParola.setTypeface(YaziFontu, Typeface.NORMAL);
		txtParola.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILParola.setError(null);
			}
		});
		txtParola.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Giris();
				}

				return false;
			}
		});

		lblGirisYardimi = findViewById(R.id.lblGirisYardimi);
		lblGirisYardimi.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewOnClick(lblGirisYardimi, "GirisYardimi");

		btnGirisYap = findViewById(R.id.btnGirisYap);
		btnGirisYap.setTypeface(YaziFontu, Typeface.NORMAL);
		btnGirisYap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Giris();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		AkorDefterimSys.DismissProgressDialog(PDGirisYap);
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_GirisHata);
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		AkorDefterimSys.DismissProgressDialog(PDGirisYap);
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_GirisHata);

		super.onBackPressed();
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
				case "GirisYardimi":
					AkorDefterimSys.KlavyeKapat();
					AkorDefterimSys.EkranGetir(new Intent(activity, ParolamiUnuttum.class), "Slide");
					break;
				case "HesapGirisYap":
					// PDGirisYap Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDGirisYap);

					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
						sharedPrefEditor.putString("prefHesapID", JSONSonuc.getString("HesapID"));
						sharedPrefEditor.putString("prefEPosta", JSONSonuc.getString("HesapEPosta"));
						sharedPrefEditor.putString("prefParolaSHA1", JSONSonuc.getString("HesapParolaSHA1"));
						sharedPrefEditor.apply();

						Intent mIntent = new Intent(activity, Ana.class);
						mIntent.putExtra("Islem", "");

						AkorDefterimSys.EkranGetir(mIntent, "Normal");

						finish();
					} else {
						AkorDefterimSys.HesapPrefSifirla();

						switch (JSONSonuc.getString("HesapDurum")) {
							case "Ban":
								ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
										getString(R.string.hesap_durumu),
										getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
										activity.getString(R.string.tamam));
								ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog_HesapDurumu.show();

								ADDialog_HesapDurumu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										ADDialog_HesapDurumu.dismiss();
									}
								});

								break;
							default:
								ADDialog_GirisHata = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
										getString(R.string.giris_yapilamadi),
										getString(R.string.giris_yapilamadi_bilgilerinizi_kontrol_edin),
										activity.getString(R.string.tamam));
								ADDialog_GirisHata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog_GirisHata.show();

								ADDialog_GirisHata.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										ADDialog_GirisHata.dismiss();
									}
								});
								break;
						}
					}

					break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Giris() {
		AkorDefterimSys.KlavyeKapat();

		txtEPostaKullaniciAdi.setText(txtEPostaKullaniciAdi.getText().toString().trim());
		String EPostaKullaniciAdi = txtEPostaKullaniciAdi.getText().toString();
		if(TextUtils.isEmpty(EPostaKullaniciAdi))
			txtILEPostaKullaniciAdi.setError(getString(R.string.hata_bos_alan));
		else {
			if(AkorDefterimSys.isValid(EPostaKullaniciAdi, "EPosta")) {
				if(AkorDefterimSys.isValid(EPostaKullaniciAdi, "FakeEPosta"))
					txtILEPostaKullaniciAdi.setError(getString(R.string.hata_format_eposta));
				else
					txtILEPostaKullaniciAdi.setError(null);
			} else {
				if(EPostaKullaniciAdi.length() < getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))
					txtILEPostaKullaniciAdi.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))));
				else if(!AkorDefterimSys.isValid(EPostaKullaniciAdi, "KullaniciAdi"))
					txtILEPostaKullaniciAdi.setError(getString(R.string.hata_format_sadece_sayi_kucukharf));
				else
					txtILEPostaKullaniciAdi.setError(null);
			}
		}

		txtParola.setText(txtParola.getText().toString().trim());
		String Parola = txtParola.getText().toString();
		if(TextUtils.isEmpty(Parola))
			txtILParola.setError(getString(R.string.hata_bos_alan));
		else if(AkorDefterimSys.EditTextKarakterKontrolMIN(Parola, getResources().getInteger(R.integer.SifreKarakterSayisi_MIN)))
			txtILParola.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.SifreKarakterSayisi_MIN))));
		else if(AkorDefterimSys.EditTextKarakterKontrolMAX(Parola, getResources().getInteger(R.integer.SifreKarakterSayisi_MAX)))
			txtILParola.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(getResources().getInteger(R.integer.SifreKarakterSayisi_MAX))));
		else txtILParola.setError(null);

		AkorDefterimSys.UnFocusEditText(txtEPostaKullaniciAdi);
		AkorDefterimSys.UnFocusEditText(txtParola);

		if(txtILEPostaKullaniciAdi.getError() == null && txtILParola.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				PDGirisYap = AkorDefterimSys.CustomProgressDialog(getString(R.string.giris_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
				PDGirisYap.show();

				String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
				@SuppressLint("HardwareIds")
				String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
				String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
				String ParolaSHA1 = Strings.getSHA1(Parola);
				String UygulamaVersiyon = "";

				try {
					UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}

				AkorDefterimSys.HesapGirisYap("Normal", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, EPostaKullaniciAdi, ParolaSHA1,"", "");
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}
}