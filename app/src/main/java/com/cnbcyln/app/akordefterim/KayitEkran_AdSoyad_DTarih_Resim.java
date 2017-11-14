package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.CircleImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class KayitEkran_AdSoyad_DTarih_Resim extends AppCompatActivity {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
	File SecilenProfilResmiFile;
	AlertDialog ADHata;

	CoordinatorLayout coordinatorLayout;
	Button btnGeri, btnIleri;
	TextInputLayout txtILAdSoyad, txtILDogumTarih;
	TextView lblVazgec, lblBaslik, lblAdSoyadResimAciklama;
	EditText txtAdSoyad, txtDogumTarih;
    CircleImageView CImgProfilResim;
    ImageView ImgSilIcon, ImgKameraIcon;

	String EPosta, Parola;
	Boolean DogumTarihAlaninaGirildiMi = false;
    private static final int DOSYAOKUMAYAZMA_IZIN = 1;
	private static final int RESIMSEC = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kayit_ekran_adsoyad_dtarih_profilresmi);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		EPosta = mBundle.getString("EPosta");
		Parola = mBundle.getString("Parola");

		coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILAdSoyad.setError(null);
				AkorDefterimSys.UnFocusEditText(txtAdSoyad);
				txtILDogumTarih.setError(null);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
			}
		});

		btnGeri = (Button) findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		lblVazgec = (TextView) findViewById(R.id.lblVazgec);
		lblVazgec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
				sharedPrefEditor.putString("Action", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
			}
		});

		lblBaslik = (TextView) findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblAdSoyadResimAciklama = (TextView) findViewById(R.id.lblAdSoyadResimAciklama);
		lblAdSoyadResimAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblAdSoyadResimAciklama, getString(R.string.adsoyad_dogumtarih_resim_ekran_aciklama, String.valueOf(AkorDefterimSys.ProfilResmiResimBoyutuMB)));

		txtILAdSoyad = (TextInputLayout) findViewById(R.id.txtILAdSoyad);
		txtILAdSoyad.setTypeface(YaziFontu);

		txtAdSoyad = (EditText) findViewById(R.id.txtAdSoyad);
		txtAdSoyad.setTypeface(YaziFontu, Typeface.NORMAL);
		txtAdSoyad.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILAdSoyad.setError(null);
			}
		});

		txtILDogumTarih = (TextInputLayout) findViewById(R.id.txtILDogumTarih);
		txtILDogumTarih.setTypeface(YaziFontu);

		txtDogumTarih = (EditText) findViewById(R.id.txtDogumTarih);
		txtDogumTarih.setInputType(InputType.TYPE_NULL);
		txtDogumTarih.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILDogumTarih.setError(null);
			}
		});
		txtDogumTarih.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!DogumTarihAlaninaGirildiMi) { // Bu kontrol sayesinde edittext alanına tıklandığında klavyenin kapatılmasını sağladık..
					DogumTarihAlaninaGirildiMi = true;
					txtDogumTarih.setText(AkorDefterimSys.TarihSeciciDialog(txtDogumTarih));
				} else DogumTarihAlaninaGirildiMi = false;
			}
		});
		txtDogumTarih.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtDogumTarih.setText(AkorDefterimSys.TarihSeciciDialog(txtDogumTarih));
			}
		});

        CImgProfilResim = (CircleImageView) findViewById(R.id.CImgProfilResim);
		CImgProfilResim.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
			}
		});

		ImgSilIcon = (ImageView) findViewById(R.id.ImgSilIcon);
		ImgSilIcon.setVisibility(View.GONE);
		ImgSilIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SecilenProfilResmiFile = null;
				CImgProfilResim.setImageDrawable(getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_square));
				ImgSilIcon.setVisibility(View.GONE);
			}
		});

        ImgKameraIcon = (ImageView) findViewById(R.id.ImgKameraIcon);
		ImgKameraIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
			}
		});

		btnIleri = (Button) findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IleriEkran();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.PrefAyarlar().getString("Action", "").equals("Vazgec")) onBackPressed();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case DOSYAOKUMAYAZMA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
					AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
				else {
					ADHata = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.uygulama_izinleri),
							getString(R.string.uygulama_izni_dosya_yazma_hata),
							getString(R.string.tamam));
					ADHata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADHata.show();

					ADHata.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADHata.cancel();
						}
					});
				}

				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESIMSEC:
					AkorDefterimSys.IntentGetir(new String[]{"ResimKirp", data.getData().toString(), String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)});
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
					SecilenProfilResmiFile = AkorDefterimSys.ResimIsleme(coordinatorLayout, SecilenProfilResmiFile, CImgProfilResim, CropImage.getActivityResult(data).getUri());
					ImgSilIcon.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

	private void IleriEkran() {
		AkorDefterimSys.KlavyeKapat();

		txtAdSoyad.setText(txtAdSoyad.getText().toString().trim());
		String AdSoyad = txtAdSoyad.getText().toString().trim();
		String DogumTarih = txtDogumTarih.getText().toString().trim();

		if(TextUtils.isEmpty(AdSoyad))
			txtILAdSoyad.setError(getString(R.string.hata_bos_alan));
		else if(!AkorDefterimSys.isValid(AdSoyad, "SadeceKucukHarfBuyukHarfBosluklu"))
			txtILAdSoyad.setError(getString(R.string.hata_format_sadece_sayi_kucukharf_buyukharf_bosluklu));
		else if(AdSoyad.length() < getResources().getInteger(R.integer.AdSoyadKarakterSayisi_MIN))
			txtILAdSoyad.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))));
		else txtILAdSoyad.setError(null);

		if(TextUtils.isEmpty(DogumTarih))
			txtILDogumTarih.setError(getString(R.string.hata_bos_alan));
		else txtILDogumTarih.setError(null);

		AkorDefterimSys.UnFocusEditText(txtAdSoyad);
		AkorDefterimSys.UnFocusEditText(txtDogumTarih);

		if(txtILAdSoyad.getError() == null && txtILDogumTarih.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) SonrakiEkran();
			else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	private void SonrakiEkran() {
		Intent mIntent = new Intent(activity, KayitEkran_KullaniciAdi.class);
		mIntent.putExtra("EPosta", EPosta);
		mIntent.putExtra("Parola", Parola);
		mIntent.putExtra("AdSoyad", txtAdSoyad.getText().toString().trim());
		mIntent.putExtra("DogumTarih", txtDogumTarih.getText().toString().trim());
		mIntent.putExtra("SecilenProfilResmiFile", SecilenProfilResmiFile);

		AkorDefterimSys.EkranGetir(mIntent, "Slide");
	}
}