package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.CircleImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class KayitEkran_AdSoyad_DTarih_Resim extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
	File SecilenProfilResmiFile;
	AlertDialog ADDialog_Hata, ADDialog_Profil_Resim;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	TextInputLayout txtILAdSoyad, txtILDogumTarih;
	TextView lblVazgec, lblBaslik, lblAdSoyadResimAciklama;
	EditText txtAdSoyad, txtDogumTarih;
    CircleImageView CImgProfilResim;
    ImageView ImgSilIcon, ImgKameraIcon;

	String EPosta, Parola;
	int ProfilResimYuklemeBoyutu;
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
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		ProfilResimYuklemeBoyutu = getResources().getInteger(R.integer.ProfilResimYuklemeBoyutu);

		Bundle mBundle = getIntent().getExtras();
		EPosta = mBundle.getString("EPosta");
		Parola = mBundle.getString("Parola");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setTypeface(YaziFontu, Typeface.BOLD);
		lblVazgec.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblAdSoyadResimAciklama = findViewById(R.id.lblAdSoyadResimAciklama);
		lblAdSoyadResimAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblAdSoyadResimAciklama.setText(getString(R.string.adsoyad_dogumtarih_resim_ekran_aciklama, String.valueOf(ProfilResimYuklemeBoyutu)));
		AkorDefterimSys.setTextViewHTML(lblAdSoyadResimAciklama);

		txtILAdSoyad = findViewById(R.id.txtILAdSoyad);
		txtILAdSoyad.setTypeface(YaziFontu);

		txtAdSoyad = findViewById(R.id.txtAdSoyad);
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

		txtILDogumTarih = findViewById(R.id.txtILDogumTarih);
		txtILDogumTarih.setTypeface(YaziFontu);

		txtDogumTarih = findViewById(R.id.txtDogumTarih);
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
		txtDogumTarih.setOnClickListener(this);

        CImgProfilResim = findViewById(R.id.CImgProfilResim);
		CImgProfilResim.setOnClickListener(this);

		ImgSilIcon = findViewById(R.id.ImgSilIcon);
		ImgSilIcon.setVisibility(View.GONE);
		ImgSilIcon.setOnClickListener(this);

        ImgKameraIcon = findViewById(R.id.ImgKameraIcon);
		ImgKameraIcon.setOnClickListener(this);

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.PrefAyarlar().getString("prefAction", "").equals("Vazgec")) onBackPressed();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog_Hata);

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				txtILAdSoyad.setError(null);
				AkorDefterimSys.UnFocusEditText(txtAdSoyad);
				txtILDogumTarih.setError(null);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
				break;
			case R.id.btnGeri:
				onBackPressed();
				break;
			case R.id.lblVazgec:
				sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
				sharedPrefEditor.putString("prefAction", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
				break;
			case R.id.btnIleri:
				IleriIslem();
				break;
			case R.id.txtDogumTarih:
				txtDogumTarih.setText(AkorDefterimSys.TarihSeciciDialog(txtDogumTarih));
				break;
			case R.id.CImgProfilResim:
				AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
				break;
			case R.id.ImgSilIcon:
				SecilenProfilResmiFile = null;
				CImgProfilResim.setImageDrawable(getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_square));
				ImgSilIcon.setVisibility(View.GONE);
				break;
			case R.id.ImgKameraIcon:
				AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case DOSYAOKUMAYAZMA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
					AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
				else {
					if(AkorDefterimSys.AlertDialogisShowing(ADDialog_Hata)) {
						ADDialog_Hata = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								getString(R.string.uygulama_izinleri),
								getString(R.string.uygulama_izni_dosya_yazma_hata),
								getString(R.string.tamam));
						ADDialog_Hata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_Hata.show();

						ADDialog_Hata.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_Hata.cancel();
							}
						});
					}
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

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "ADDialog_Profil_Resim_Hayir":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Profil_Resim);
					break;
				case "ADDialog_Profil_Resim_Evet":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Profil_Resim);
					SonrakiEkran();
					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void IleriIslem() {
		btnIleri.setEnabled(false);
		AkorDefterimSys.KlavyeKapat();

		txtAdSoyad.setText(txtAdSoyad.getText().toString().trim());
		String AdSoyad = txtAdSoyad.getText().toString();

		txtDogumTarih.setText(txtDogumTarih.getText().toString().trim());
		String DogumTarih = txtDogumTarih.getText().toString().trim();

		if (TextUtils.isEmpty(AdSoyad)) {
			txtILAdSoyad.setError(getString(R.string.hata_bos_alan));
			txtAdSoyad.requestFocus();
			txtAdSoyad.setSelection(txtAdSoyad.length());
			imm.showSoftInput(txtAdSoyad, 0);
		} else if (!AkorDefterimSys.isValid(AdSoyad, "SadeceKucukHarfBuyukHarfBosluklu")) {
			txtILAdSoyad.setError(getString(R.string.hata_format_sadece_sayi_kucukharf_buyukharf_bosluklu));
			txtAdSoyad.requestFocus();
			txtAdSoyad.setSelection(txtAdSoyad.length());
			imm.showSoftInput(txtAdSoyad, 0);
		} else if(AdSoyad.length() < getResources().getInteger(R.integer.AdSoyadKarakterSayisi_MIN)) {
			txtILAdSoyad.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))));
			txtAdSoyad.requestFocus();
			txtAdSoyad.setSelection(txtAdSoyad.length());
			imm.showSoftInput(txtAdSoyad, 0);
		} else txtILAdSoyad.setError(null);

		if(TextUtils.isEmpty(DogumTarih))
			txtILDogumTarih.setError(getString(R.string.hata_bos_alan));
		else
			txtILDogumTarih.setError(null);

		if(txtILAdSoyad.getError() == null && txtILDogumTarih.getError() == null) {
			AkorDefterimSys.UnFocusEditText(txtAdSoyad);
			AkorDefterimSys.UnFocusEditText(txtDogumTarih);

			if(AkorDefterimSys.InternetErisimKontrolu()) {
				if(SecilenProfilResmiFile == null) {
					if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Profil_Resim)) {
						ADDialog_Profil_Resim = AkorDefterimSys.HButtonCustomAlertDialog(activity,
								getString(R.string.profil_resmi),
								getString(R.string.profil_resmi_secmemissin_devam_etmek_istiyormusun),
								activity.getString(R.string.hayir),
								"ADDialog_Profil_Resim_Hayir",
								activity.getString(R.string.evet),
								"ADDialog_Profil_Resim_Evet");
						ADDialog_Profil_Resim.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_Profil_Resim.show();
					}

					btnIleri.setEnabled(true);
				} else SonrakiEkran();
			} else {
				btnIleri.setEnabled(true);
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
			}
		} else btnIleri.setEnabled(true);
	}

	private void SonrakiEkran(){
		Intent mIntent = new Intent(activity, KayitEkran_KullaniciAdi.class);
		mIntent.putExtra("EPosta", EPosta);
		mIntent.putExtra("Parola", Parola);
		mIntent.putExtra("AdSoyad", txtAdSoyad.getText().toString().trim());
		mIntent.putExtra("DogumTarih", txtDogumTarih.getText().toString().trim());
		mIntent.putExtra("SecilenProfilResmiFile", SecilenProfilResmiFile);

		AkorDefterimSys.EkranGetir(mIntent, "Slide");

		btnIleri.setEnabled(true);
	}
}