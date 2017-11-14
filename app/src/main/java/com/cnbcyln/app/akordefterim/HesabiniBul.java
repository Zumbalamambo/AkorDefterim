package com.cnbcyln.app.akordefterim;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class HesabiniBul extends AppCompatActivity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
    Random rnd;
	ProgressDialog PDParolamiUnuttum;
	AlertDialog ADDialog_HesapDurumu;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnIleri;
	TextInputLayout txtILEPostaKullaniciAdi;
	EditText txtEPostaKullaniciAdi;
	TextView lblBaslik, lblHesabiniBul, lblHesabiniBulAciklama;

    String OnayKodu, BulunanEPosta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabini_bul);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
        rnd = new Random();

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
		btnIleri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

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
					//IleriIslem();
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
	}

	@Override
	protected void onStart() {
		super.onStart();

		/*if(AkorDefterimSys.PrefAyarlar().getString("Action", "").equals("Vazgec")) {
			sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
			sharedPrefEditor.remove("Action");
			sharedPrefEditor.apply();

			onBackPressed();
		}*/
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		//AkorDefterimSys.DismissProgressDialog(PDParolamiUnuttum);
		//AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		//AkorDefterimSys.DismissProgressDialog(PDParolamiUnuttum);
		//AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

		super.onBackPressed();
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					if(JSONSonuc.getBoolean("Sonuc") && !TextUtils.isEmpty(JSONSonuc.getString("ParolaSHA1"))) { // Eğer hesap bulundu ve ParolaSHA1 var ise. Yani normal hesap açıldıysa..
                        if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							// PDParolamiUnuttum Progress Dialog'u kapattık
							AkorDefterimSys.DismissProgressDialog(PDParolamiUnuttum);

                            ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
                                    getString(R.string.hesap_durumu),
                                    getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
                                    activity.getString(R.string.tamam));
                            ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog_HesapDurumu.show();

                            ADDialog_HesapDurumu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ADDialog_HesapDurumu.dismiss();
                                }
                            });
                        } else {
                            BulunanEPosta = JSONSonuc.getString("EPosta");

                            // 6 haneli onay kodu oluşturuldu
                            OnayKodu = String.valueOf((100000 + rnd.nextInt(900000)));

                            // Onay kodu belirtilen eposta adresine gönderiliyor
                            AkorDefterimSys.EPostaGonder(BulunanEPosta, "", getString(R.string.dogrulama_kodu), getString(R.string.mail_onayi_icerik2, getString(R.string.uygulama_adi), OnayKodu));
                        }
					} else {
						// PDParolamiUnuttum Progress Dialog'u kapattık
						AkorDefterimSys.DismissProgressDialog(PDParolamiUnuttum);

						//AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));
					}

					break;
                case "EPostaGonder":
                    // PDParolamiUnuttum Progress Dialog'u kapattık
                    AkorDefterimSys.DismissProgressDialog(PDParolamiUnuttum);

                    if(JSONSonuc.getBoolean("Sonuc")) {
						// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
						Intent mIntent = new Intent(activity, Onaykodu.class);
						mIntent.putExtra("Islem", "GirisYardimi");
						mIntent.putExtra("EPosta", BulunanEPosta);
						mIntent.putExtra("OnayKodu", String.valueOf(OnayKodu));

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
					}
                    else //AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*private void IleriIslem() {
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

		AkorDefterimSys.UnFocusEditText(txtEPostaKullaniciAdi);

		if(txtILEPostaKullaniciAdi.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				PDParolamiUnuttum = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
				PDParolamiUnuttum.show();

				AkorDefterimSys.HesapBilgiGetir(null, EPostaKullaniciAdi);
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}*/
}