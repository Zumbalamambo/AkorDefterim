package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabina_Eris_Hesabini_Bul extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	SpinKitView SKVLoader;
	TextInputLayout txtILEPostaKullaniciAdi;
	EditText txtEPostaKullaniciAdi;
	TextView lblBaslik, lblHesabiniBul, lblHesabiniBulAciklama;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabini_bul);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILEPostaKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtEPostaKullaniciAdi);
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

		lblHesabiniBul = findViewById(R.id.lblHesabiniBul);
		lblHesabiniBul.setTypeface(YaziFontu, Typeface.BOLD);
		lblHesabiniBul.setText(lblHesabiniBul.getText().toString().toUpperCase());

		txtILEPostaKullaniciAdi = findViewById(R.id.txtILEPostaKullaniciAdi);
		txtILEPostaKullaniciAdi.setTypeface(YaziFontu);

		txtEPostaKullaniciAdi = findViewById(R.id.txtEPostaKullaniciAdi);
		txtEPostaKullaniciAdi.setTypeface(YaziFontu, Typeface.NORMAL);
		txtEPostaKullaniciAdi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					IleriIslem();
				}

				return false;
			}
		});
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

		lblHesabiniBulAciklama = findViewById(R.id.lblHesabiniBulAciklama);
		lblHesabiniBulAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblHesabiniBulAciklama.setText(getString(R.string.hesabini_bul_aciklama, getString(R.string.uygulama_adi)));
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

			btnIleri.setEnabled(true);
			SKVLoader.setVisibility(View.GONE);
			AkorDefterimSys.DismissProgressDialog(PDIslem);

            switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
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
							Intent mIntent = new Intent(activity, Hesabina_Eris_Hesabini_Bul2.class);
							mIntent.putExtra("HesapID", JSONSonuc.getString("HesapID"));
							mIntent.putExtra("FacebookID", JSONSonuc.getString("FacebookID"));
							mIntent.putExtra("GoogleID", JSONSonuc.getString("GoogleID"));
							mIntent.putExtra("AdSoyad", JSONSonuc.getString("AdSoyad"));
							mIntent.putExtra("ResimURL", JSONSonuc.getString("ResimURL"));
							mIntent.putExtra("EPosta", JSONSonuc.getString("EPosta"));
							mIntent.putExtra("Parola", JSONSonuc.getString("Parola"));
							mIntent.putExtra("TelKodu", JSONSonuc.getString("TelKodu"));
							mIntent.putExtra("CepTelefon", JSONSonuc.getString("CepTelefon"));

							AkorDefterimSys.KlavyeKapat();
							AkorDefterimSys.EkranGetir(mIntent, "Slide");
                        }
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));

                    break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					break;
				case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);
					break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			txtEPostaKullaniciAdi.setText(txtEPostaKullaniciAdi.getText().toString().trim());
			String EPostaKullaniciAdi = txtEPostaKullaniciAdi.getText().toString();

			if(TextUtils.isEmpty(EPostaKullaniciAdi)) {
				txtILEPostaKullaniciAdi.setError(getString(R.string.hata_bos_alan));
				txtEPostaKullaniciAdi.requestFocus();
				txtEPostaKullaniciAdi.setSelection(txtEPostaKullaniciAdi.length());
				imm.showSoftInput(txtEPostaKullaniciAdi, 0);
			} else {
				if(AkorDefterimSys.isValid(EPostaKullaniciAdi, "EPosta")) {
					if(AkorDefterimSys.isValid(EPostaKullaniciAdi, "FakeEPosta")) {
						txtILEPostaKullaniciAdi.setError(getString(R.string.hata_format_eposta));
						txtEPostaKullaniciAdi.requestFocus();
						txtEPostaKullaniciAdi.setSelection(txtEPostaKullaniciAdi.length());
						imm.showSoftInput(txtEPostaKullaniciAdi, 0);
					} else
						txtILEPostaKullaniciAdi.setError(null);
				} else {
					if(EPostaKullaniciAdi.length() < getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN)) {
						txtILEPostaKullaniciAdi.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))));
						txtEPostaKullaniciAdi.requestFocus();
						txtEPostaKullaniciAdi.setSelection(txtEPostaKullaniciAdi.length());
						imm.showSoftInput(txtEPostaKullaniciAdi, 0);
					} else if(!AkorDefterimSys.isValid(EPostaKullaniciAdi, "KullaniciAdi")) {
						txtILEPostaKullaniciAdi.setError(getString(R.string.hata_format_sadece_sayi_kucukharf));
						txtEPostaKullaniciAdi.requestFocus();
						txtEPostaKullaniciAdi.setSelection(txtEPostaKullaniciAdi.length());
						imm.showSoftInput(txtEPostaKullaniciAdi, 0);
					} else
						txtILEPostaKullaniciAdi.setError(null);
				}
			}

			if(txtILEPostaKullaniciAdi.getError() == null) {
				btnIleri.setEnabled(false);
				SKVLoader.setVisibility(View.VISIBLE);
				AkorDefterimSys.UnFocusEditText(txtEPostaKullaniciAdi);

				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
					PDIslem.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, "", "", EPostaKullaniciAdi, "HesapBilgiGetir");
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}