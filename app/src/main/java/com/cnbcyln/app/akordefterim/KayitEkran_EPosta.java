package com.cnbcyln.app.akordefterim;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class KayitEkran_EPosta extends AppCompatActivity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
    Random rnd;
	ProgressDialog PDEPosta;
	AlertDialog ADHesapKontrol;

	CoordinatorLayout coordinatorLayout;
	Button btnGeri, btnIleri;
	TextView lblBaslik, lblEPostaAciklama;
	TextInputLayout txtILEPosta;
	EditText txtEPosta;

	String OnayKodu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kayit_ekran_eposta);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
        rnd = new Random();

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.
		AkorDefterimSys.EkranAnimasyon("Explode");

		coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILEPosta.setError(null);
                AkorDefterimSys.UnFocusEditText(txtEPosta);
			}
		});

		btnGeri = (Button) findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		lblBaslik = (TextView) findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		txtILEPosta = (TextInputLayout) findViewById(R.id.txtILEPosta);
		txtILEPosta.setTypeface(YaziFontu);

		txtEPosta = (EditText) findViewById(R.id.txtEPosta);
		txtEPosta.setTypeface(YaziFontu, Typeface.NORMAL);
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
					IleriIslem();
				}

				return false;
			}
		});

		lblEPostaAciklama = (TextView) findViewById(R.id.lblEPostaAciklama);
		lblEPostaAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblEPostaAciklama, getString(R.string.eposta_adresini_kimseye_gormeyecek));

		btnIleri = (Button) findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IleriIslem();
			}
		});
	}

    @Override
    protected void onStart() {
        super.onStart();

        if(AkorDefterimSys.PrefAyarlar().getString("Action", "").equals("Vazgec")) {
            sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
			sharedPrefEditor.remove("Action");
            sharedPrefEditor.apply();

            onBackPressed();
        }
    }

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					// Eğer hesap kontrol'de hesap bulunamadıysa sonuç false döner. Böylelikle kayıt işlemlerine devam edebiliriz.
					if(!JSONSonuc.getBoolean("Sonuc")) {
						// 6 haneli onay kodu oluşturuldu
						OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

						// Onay kodu belirtilen eposta adresine gönderiliyor
						AkorDefterimSys.EPostaGonder(txtEPosta.getText().toString().trim(), "", getString(R.string.dogrulama_kodu), getString(R.string.mail_onayi_icerik2, getString(R.string.uygulama_adi), OnayKodu));
					} else {
						// PDEPosta Progress Dialog'u kapattık
						AkorDefterimSys.DismissProgressDialog(PDEPosta);

						String BulunanHesaplar = new JSONObject(JSONSonuc.getString("BulunanHesaplar")).getString("Hesaplar");

						// String'in eğer başında "," var ise siliyoruz..
						BulunanHesaplar = (BulunanHesaplar.startsWith(",") ? BulunanHesaplar.substring(1, BulunanHesaplar.length()):BulunanHesaplar);

						// String'i dizi yapıyoruz
						String[] HesapListesi = BulunanHesaplar.split(",");

						ADHesapKontrol = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								getString(R.string.hesap_durumu),
								getString(R.string.hata_hesap_durum, txtEPosta.getText().toString().trim(), (HesapListesi.length > 1?"(lar)<br>(" + BulunanHesaplar + ")":"<br>(" + BulunanHesaplar + ")")),
								getString(R.string.giris_yap),
								getString(R.string.tamam));
						ADHesapKontrol.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADHesapKontrol.show();

						ADHesapKontrol.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADHesapKontrol.dismiss();

								sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
								sharedPrefEditor.putString("Action", "GirisYap");
								sharedPrefEditor.apply();

								onBackPressed();
							}
						});

						ADHesapKontrol.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADHesapKontrol.dismiss();
							}
						});
					}

					break;
                case "EPostaGonder":
					// PDEPosta Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDEPosta);

                	// Eğer EPosta gönderildiyse sonuç true döner..
                    if(JSONSonuc.getBoolean("Sonuc")) SonrakiEkran();
					else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		String EPosta = txtEPosta.getText().toString().trim();

		if(TextUtils.isEmpty(EPosta))
			txtILEPosta.setError(getString(R.string.hata_bos_alan));
		else if(!AkorDefterimSys.isValid(EPosta, "EPosta"))
			txtILEPosta.setError(getString(R.string.hata_format_eposta));
		else txtILEPosta.setError(null);

		if(txtILEPosta.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				PDEPosta = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
				PDEPosta.show();

				AkorDefterimSys.HesapBilgiGetir(null, txtEPosta.getText().toString().trim());
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	private void SonrakiEkran() {
		// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
		Intent mIntent = new Intent(activity, Onaykodu.class);
		mIntent.putExtra("Islem", "Kayit");
		mIntent.putExtra("EPosta", txtEPosta.getText().toString().trim());
		mIntent.putExtra("OnayKodu", String.valueOf(OnayKodu));

		AkorDefterimSys.EkranGetir(mIntent, "Slide");
	}
}