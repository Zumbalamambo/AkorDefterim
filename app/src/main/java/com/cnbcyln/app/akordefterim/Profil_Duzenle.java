package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.CircleImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Profil_Duzenle extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	CallbackManager mFacebookCallbackManager;
	private GoogleApiClient mGoogleLoginApiClient;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDBilgilerAliniyor, PDBilgilerGuncelleniyor;
	InputMethodManager imm;
	File SecilenProfilResmiFile;

	CoordinatorLayout coordinatorLayout;
	ConstraintLayout ConstraintLayout1, ConstraintLayout2;
	ImageButton btnGeri, btnKaydet;
	TextInputLayout txtILAdSoyad, txtILDogumTarih, txtILKullaniciAdi;
	EditText txtAdSoyad, txtDogumTarih, txtKullaniciAdi;
	TextView lblBaslik, lblFotografiDegistir, lblFotografiDegistir2, lblGenelBilgiler, lblGizliBilgiler, lblEPostaAdresi, lblEPostaAdresi2, lblTelefonNumarasi, lblTelefonNumarasi2;
	ImageView ImgBuyukProfilResim;
	CircleImageView CImgProfilResim;
	LinearLayout LLEPostaAdresi, LLTelefonNumarasi;

	String FirebaseToken = "", OSID = "", OSVersiyon = "", UygulamaVersiyon = "", KayitliKullaniciAdi = "", FacebookID = "", GoogleID = "";
	int ProfilResimYuklemeBoyutu = 0, KullaniciAdiKarakterSayisiMIN = 0, KullaniciAdiKarakterSayisiMAX = 0, KullaniciAdiBastakiHarfKarakterSayisi = 0;
	Boolean DogumTarihAlaninaGirildiMi = false;
	Boolean IlkYuklemeYapildiMi = false;
	private static final int DOSYAOKUMAYAZMA_IZIN = 1;
	private static final int KAMERA_IZIN = 2;
	private static final int RESIMSEC = 3;
	private static final int FOTOGRAFCEK = 4;
	private static final int GoogleBilgiAlma = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		mFacebookCallbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.activity_profil_duzenle);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		KullaniciAdiKarakterSayisiMIN = getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN);
		KullaniciAdiKarakterSayisiMAX = getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MAX);
		KullaniciAdiBastakiHarfKarakterSayisi = getResources().getInteger(R.integer.KullaniciAdiBastakiHarfKarakterSayisi);

		ProfilResimYuklemeBoyutu = getResources().getInteger(R.integer.ProfilResimYuklemeBoyutu);

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		ConstraintLayout1 = activity.findViewById(R.id.ConstraintLayout1);
		ConstraintLayout1.setOnClickListener(this);

		ConstraintLayout2 = activity.findViewById(R.id.ConstraintLayout2);
		ConstraintLayout2.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		btnKaydet = findViewById(R.id.btnKaydet);
		btnKaydet.setOnClickListener(this);

		ImgBuyukProfilResim = findViewById(R.id.ImgBuyukProfilResim);

		CImgProfilResim = findViewById(R.id.CImgProfilResim);

		lblFotografiDegistir = findViewById(R.id.lblFotografiDegistir);
		lblFotografiDegistir.setTypeface(YaziFontu, Typeface.BOLD);
		lblFotografiDegistir.setOnClickListener(this);
		registerForContextMenu(lblFotografiDegistir);

		lblFotografiDegistir2 = findViewById(R.id.lblFotografiDegistir2);
		lblFotografiDegistir2.setTypeface(YaziFontu, Typeface.BOLD);
		lblFotografiDegistir2.setOnClickListener(this);
		registerForContextMenu(lblFotografiDegistir2);

		lblGenelBilgiler = findViewById(R.id.lblGenelBilgiler);
		lblGenelBilgiler.setTypeface(YaziFontu, Typeface.BOLD);

		txtILAdSoyad = findViewById(R.id.txtILAdSoyad);
		txtILAdSoyad.setTypeface(YaziFontu);

		txtAdSoyad = findViewById(R.id.txtAdSoyad);
		txtAdSoyad.setTypeface(YaziFontu, Typeface.BOLD);
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
		txtDogumTarih.setTypeface(YaziFontu, Typeface.BOLD);
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
					AkorDefterimSys.KlavyeKapat();
				} else DogumTarihAlaninaGirildiMi = false;
			}
		});
		txtDogumTarih.setOnClickListener(this);

		txtILKullaniciAdi = findViewById(R.id.txtILKullaniciAdi);
		txtILKullaniciAdi.setTypeface(YaziFontu);

		txtKullaniciAdi = findViewById(R.id.txtKullaniciAdi);
		txtKullaniciAdi.setTypeface(YaziFontu, Typeface.BOLD);
		txtKullaniciAdi.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILKullaniciAdi.setError(null);

				if(AkorDefterimSys.InternetErisimKontrolu()) {
					// btnKaydet'in etkin olup olmadığı durumunu kontrol ediyoruz. Çünkü KlavyeKapat işlemi olduğu için kayıt sırasında yine change method'u aktif oluyor. Bunu engelliyoruz..
					if(btnKaydet.isEnabled()) AkorDefterimSys.HesapBilgiGetir(null, "", "", txtKullaniciAdi.getText().toString(), "KullaniciAdiKontrol");
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
			}
		});
		txtKullaniciAdi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					txtILKullaniciAdi.setError(null);
					AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
				}

				return false;
			}
		});

		lblGizliBilgiler = findViewById(R.id.lblGizliBilgiler);
		lblGizliBilgiler.setTypeface(YaziFontu, Typeface.BOLD);

		LLEPostaAdresi = findViewById(R.id.LLEPostaAdresi);
		LLEPostaAdresi.setOnClickListener(this);

		lblEPostaAdresi = findViewById(R.id.lblEPostaAdresi);
		lblEPostaAdresi.setTypeface(YaziFontu, Typeface.NORMAL);

		lblEPostaAdresi2 = findViewById(R.id.lblEPostaAdresi2);
		lblEPostaAdresi2.setTypeface(YaziFontu, Typeface.BOLD);

		LLTelefonNumarasi = findViewById(R.id.LLTelefonNumarasi);
		LLTelefonNumarasi.setOnClickListener(this);

		lblTelefonNumarasi = findViewById(R.id.lblTelefonNumarasi);
		lblTelefonNumarasi.setTypeface(YaziFontu, Typeface.NORMAL);

		lblTelefonNumarasi2 = findViewById(R.id.lblTelefonNumarasi2);
		lblTelefonNumarasi2.setTypeface(YaziFontu, Typeface.BOLD);
	}

	@SuppressLint("HardwareIds")
	@Override
	protected void onStart() {
		super.onStart();

		if(sharedPref.getString("prefAction", "").equals("IslemTamamlandi")) {
			sharedPrefEditor = sharedPref.edit();
			sharedPrefEditor.remove("prefAction");
			sharedPrefEditor.apply();

			IlkYuklemeYapildiMi = false;
		}

		FirebaseToken = FirebaseInstanceId.getInstance().getToken();
		OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);

		try {
			UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		if(!IlkYuklemeYapildiMi) {
			if(AkorDefterimSys.GirisYapildiMi()) {
				if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerAliniyor)) {
					PDBilgilerAliniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_yukleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerAliniyor_Timeout");
					PDBilgilerAliniyor.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID",""), "", "", "HesapBilgiGetir");
			} else AkorDefterimSys.CikisYap();
		}

		FacebookLoginInit();
		GoogleAPIInit();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle(getString(R.string.bir_profil_fotografi_belirle));

		String[] ResimDuzenleContextMenu = getResources().getStringArray(R.array.ProfilFotografiBelirleContextMenu);

		for (String aResimDuzenleContextMenu : ResimDuzenleContextMenu) {
			switch (aResimDuzenleContextMenu.subSequence(0, aResimDuzenleContextMenu.indexOf("_")).toString()) {
				case "fotografyukle":
					menu.add(0, 0, 0, aResimDuzenleContextMenu.subSequence(aResimDuzenleContextMenu.indexOf("_") + 1, aResimDuzenleContextMenu.length()));
					break;
				case "fotografcek":
					menu.add(0, 1, 0, aResimDuzenleContextMenu.subSequence(aResimDuzenleContextMenu.indexOf("_") + 1, aResimDuzenleContextMenu.length()));
					break;
				case "facebooktanaktar":
					if(!FacebookID.equals("-")) menu.add(0, 2, 0, aResimDuzenleContextMenu.subSequence(aResimDuzenleContextMenu.indexOf("_") + 1, aResimDuzenleContextMenu.length()));
					break;
				case "googledanaktar":
					if(!GoogleID.equals("-")) menu.add(0, 3, 0, aResimDuzenleContextMenu.subSequence(aResimDuzenleContextMenu.indexOf("_") + 1, aResimDuzenleContextMenu.length()));
					break;
				case "fotografikaldir":
					menu.add(0, 4, 0, aResimDuzenleContextMenu.subSequence(aResimDuzenleContextMenu.indexOf("_") + 1, aResimDuzenleContextMenu.length()));
					break;
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case 1:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.IntentGetir(new String[]{"FotografCek", String.valueOf(KAMERA_IZIN), String.valueOf(FOTOGRAFCEK)});
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case 2:
				LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));

				break;
			case 3:
				if (mGoogleLoginApiClient != null) {
					mGoogleLoginApiClient.connect();

					Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleLoginApiClient);
					startActivityForResult(signInIntent, GoogleBilgiAlma);
				} else {
					GoogleAPIInit();

					Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleLoginApiClient);
					startActivityForResult(signInIntent, GoogleBilgiAlma);
				}

				break;
			case 4:
				if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
					PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_guncelleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
					PDBilgilerGuncelleniyor.show();
				}

				AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", "-","", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle_Profil_Resim2");
				break;
			default:
				return false;
		}

		return true;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				txtILAdSoyad.setError(null);
				AkorDefterimSys.UnFocusEditText(txtAdSoyad);
				txtILDogumTarih.setError(null);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
				txtILKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
				break;
			case R.id.ConstraintLayout1:
				txtILAdSoyad.setError(null);
				AkorDefterimSys.UnFocusEditText(txtAdSoyad);
				txtILDogumTarih.setError(null);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
				txtILKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
				break;
			case R.id.ConstraintLayout2:
				txtILAdSoyad.setError(null);
				AkorDefterimSys.UnFocusEditText(txtAdSoyad);
				txtILDogumTarih.setError(null);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
				txtILKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
				break;
			case R.id.btnGeri:
				onBackPressed();

				break;
			case R.id.btnKaydet:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					Kaydet();
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case R.id.lblFotografiDegistir:
				activity.openContextMenu(lblFotografiDegistir);

				break;
			case R.id.lblFotografiDegistir2:
				activity.openContextMenu(lblFotografiDegistir2);

				break;
			case R.id.txtDogumTarih:
				txtDogumTarih.setText(AkorDefterimSys.TarihSeciciDialog(txtDogumTarih));
				AkorDefterimSys.KlavyeKapat();
				break;
			case R.id.LLEPostaAdresi:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.EkranGetir(new Intent(activity, EPosta_Degistir.class), "Slide");
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case R.id.LLTelefonNumarasi:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.EkranGetir(new Intent(activity, CepTelefonu_Degistir.class), "Slide");
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

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
					if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
						ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.uygulama_izinleri),
								getString(R.string.uygulama_izni_dosya_yazma_hata),
								getString(R.string.tamam), "ADDialog_Kapat");
						ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog.show();
					}
				}

				break;
			case KAMERA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
					AkorDefterimSys.IntentGetir(new String[]{"FotografCek", String.valueOf(KAMERA_IZIN), String.valueOf(FOTOGRAFCEK)});
				else {
					if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
						ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.uygulama_izinleri),
								getString(R.string.uygulama_izni_kamera_hata),
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case RESIMSEC:
					AkorDefterimSys.IntentGetir(new String[]{"ResimKirp", data.getData().toString(), String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)});
					break;
				case FOTOGRAFCEK:
					File FAnaDizin = new File(AkorDefterimSys.AnaKlasorDizini);

					if(!FAnaDizin.exists()) FAnaDizin.mkdir();

					File Fotograf = new File(String.format("%s%s%s", AkorDefterimSys.AnaKlasorDizini, sharedPref.getString("prefHesapID", ""), ".jpg"));

					if(Fotograf.exists())
						AkorDefterimSys.IntentGetir(new String[]{"ResimKirp", "file://" + Fotograf.getAbsolutePath(), String.valueOf(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)});
					break;
				case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
					SecilenProfilResmiFile = AkorDefterimSys.ResimIsleme(coordinatorLayout, SecilenProfilResmiFile, CImgProfilResim, CropImage.getActivityResult(data).getUri());

					File EskiFotograf = new File(String.format("%s%s%s", AkorDefterimSys.AnaKlasorDizini, sharedPref.getString("prefHesapID", ""), ".jpg"));

					if(EskiFotograf.exists()) EskiFotograf.delete();

					new ProfilResimYukle().execute();
					break;
				case GoogleBilgiAlma:
					GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

					if (result.isSuccess()) {
						GoogleSignInAccount acct = result.getSignInAccount();

						String ResimURL_Google = (acct.getPhotoUrl() == null ? "":acct.getPhotoUrl().toString());

						if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
							PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_guncelleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
							PDBilgilerGuncelleniyor.show();
						}

						AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", ResimURL_Google,"", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle_Profil_Resim2");
					}
					break;
			}
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			AkorDefterimSys.DismissProgressDialog(PDBilgilerAliniyor);
			AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);

			switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					if(JSONSonuc.getBoolean("Sonuc")) {
						if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
								ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.hesap_durumu),
										getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
										activity.getString(R.string.tamam),
										"ADDialog_Kapat_CikisYap");
								ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog.show();
							}
						} else {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefEPosta", JSONSonuc.getString("EPosta"));
							sharedPrefEditor.putString("prefParolaSHA1", JSONSonuc.getString("ParolaSHA1"));
							sharedPrefEditor.apply();

							FacebookID = JSONSonuc.getString("FacebookID");
							GoogleID = JSONSonuc.getString("GoogleID");

							if(JSONSonuc.getString("ResimURL").equals("-")) {
								ImgBuyukProfilResim.setImageResource(R.drawable.bos_profil);
								CImgProfilResim.setImageResource(R.drawable.bos_profil);
							} else {
								if (JSONSonuc.getString("ResimURL").startsWith("http://") || JSONSonuc.getString("ResimURL").startsWith("https://")) {
									new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(JSONSonuc.getString("ResimURL"));
									new AkorDefterimSys.NettenResimYukle(CImgProfilResim).execute(JSONSonuc.getString("ResimURL"));
								} else {
									new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + JSONSonuc.getString("ResimURL"));
									new AkorDefterimSys.NettenResimYukle(CImgProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + JSONSonuc.getString("ResimURL"));
								}
							}

							txtAdSoyad.setText(JSONSonuc.getString("AdSoyad"));

							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
								Date inputDate = format.parse(JSONSonuc.getString("DogumTarih"));
								format = new SimpleDateFormat("dd" + AkorDefterimSys.TarihGunAyYilAyiracREGEX + "MM" + AkorDefterimSys.TarihGunAyYilAyiracREGEX + "yyyy", Locale.getDefault());
								txtDogumTarih.setText(format.format(inputDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}

							txtKullaniciAdi.setText(JSONSonuc.getString("KullaniciAdi"));
							KayitliKullaniciAdi = JSONSonuc.getString("KullaniciAdi");

							if(JSONSonuc.getString("EPosta").equals("")) lblEPostaAdresi2.setText(String.format("%s%s%s", "(", getString(R.string.belirtilmemis), ")"));
							else lblEPostaAdresi2.setText(JSONSonuc.getString("EPosta"));

							if(JSONSonuc.getString("CepTelefon").equals("")) lblTelefonNumarasi2.setText(String.format("%s%s%s", "(", getString(R.string.belirtilmemis), ")"));
							else lblTelefonNumarasi2.setText(String.format("%s%s%s", "+", JSONSonuc.getString("TelKodu"), JSONSonuc.getString("CepTelefon")));
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hesap_durumu),
									getString(R.string.hesap_bilgileri_bulunamadi),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_CikisYap");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					IlkYuklemeYapildiMi = true;

					break;
				case "KullaniciAdiKontrol":
					if(JSONSonuc.getBoolean("Sonuc"))
						if(!KayitliKullaniciAdi.equals(JSONSonuc.getString("KullaniciAdi")))
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kullaniciadi_kayitli));

					break;
				case "HesapBilgiGuncelle_Profil":
					btnKaydet.setEnabled(true);

					if(JSONSonuc.getBoolean("Sonuc")) {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.profili_duzenle),
									getString(R.string.hesap_bilgileri_guncellendi),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_GeriGit");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					} else {
						switch (JSONSonuc.getString("Aciklama")) {
							case "Kullanıcı Adı başka birisine kayıtlı!":
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kullaniciadi_kayitli));
								break;
							default:
								if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
									ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
											getString(R.string.hata),
											getString(R.string.hesap_bilgileri_guncellenemedi),
											activity.getString(R.string.tamam),
											"ADDialog_Kapat_GeriGit");
									ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
									ADDialog.show();
								}
								break;
						}
					}
					break;
				case "HesapBilgiGuncelle_Profil_Resim":
					if(JSONSonuc.getBoolean("Sonuc")) {
						Bitmap mBitmap = BitmapFactory.decodeFile(SecilenProfilResmiFile.getAbsolutePath());

						ImgBuyukProfilResim.setImageBitmap(mBitmap);
						CImgProfilResim.setImageBitmap(mBitmap);

						if(SecilenProfilResmiFile != null && SecilenProfilResmiFile.exists()) SecilenProfilResmiFile.delete();
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.profil_resmi_guncellenemedi),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_GeriGit");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "HesapBilgiGuncelle_Profil_Resim2":
					if(JSONSonuc.getBoolean("Sonuc")) {
						AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID",""), "", "", "HesapBilgiGetir");
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.profil_resmi_guncellenemedi),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_GeriGit");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					break;
				case "ADDialog_Kapat_GeriGit":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					onBackPressed();
					break;
				case "ADDialog_Kapat_CikisYap":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					AkorDefterimSys.CikisYap();
					break;
				case "PDBilgilerAliniyor_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDBilgilerAliniyor);
					onBackPressed();
					break;
				case "PDBilgilerGuncelleniyor_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);
					onBackPressed();
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@SuppressLint({"InflateParams", "StaticFieldLeak"})
	private class ProfilResimYukle extends AsyncTask<Void, Integer, String> {
		long totalSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
				PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.profil_resmi_yukleniyor, "0%"), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
				PDBilgilerGuncelleniyor.show();
			}
		}

		@Override
		protected String doInBackground(Void... parametre) {
			return UploadFile();
		}

		private String UploadFile() {
			String Sonuc;

			try {
				Bitmap SecilenResimBitmap = BitmapFactory.decodeFile(SecilenProfilResmiFile.getAbsolutePath());
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				SecilenResimBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
					@Override
					public void transferred(long num) {
						publishProgress((int) ((num / (float) totalSize) * 100));
					}
				});

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AkorDefterimSys.PHPResimYukle);

				// Adding file data to http body
				entity.addPart("Dosya", new FileBody(SecilenProfilResmiFile));
				entity.addPart("Dizin", new StringBody(AkorDefterimSys.PHPProfilResimleriDizini));
				entity.addPart("DosyaAdi",new StringBody(sharedPref.getString("prefHesapID","")));

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				//HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					Sonuc = "OK";
				} else {
					Sonuc = "Error occurred! Http Status Code: " + statusCode;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Sonuc = "FileNotFoundException";
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Sonuc = "MalformedURLException";
			} catch (IOException e) {
				e.printStackTrace();
				Sonuc = "IOException";
			}

			return Sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			switch (Sonuc) {
				case "OK":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
							@SuppressLint("HardwareIds")
							String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
							String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
							String UygulamaVersiyon = "";

							try {
								UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
							} catch (PackageManager.NameNotFoundException e) {
								e.printStackTrace();
							}

							String ResimURL = AkorDefterimSys.ProfilResimleriDizini + sharedPref.getString("prefHesapID", "") + ".jpg";

							PDBilgilerGuncelleniyor.setMessage(getString(R.string.bilgileriniz_guncelleniyor));

							AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", ResimURL,"", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle_Profil_Resim");
						}
					});
					break;
				case "FileNotFoundException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
						}
					});
					break;
				case "MalformedURLException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.url_hatasi));
						}
					});
					break;
				case "IOException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.dosya_yazma_okuma_hatasi));
						}
					});
					break;
				default:
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.beklenmedik_hata_meydana_geldi));
						}
					});
					break;
			}
		}

		@Override
		protected void onProgressUpdate(final Integer... Deger) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					PDBilgilerGuncelleniyor.setMessage(getString(R.string.profil_resmi_yukleniyor, String.valueOf(Deger[0]) + "%"));
				}
			});
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			AkorDefterimSys.DismissProgressDialog(PDBilgilerGuncelleniyor);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void Kaydet() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			txtAdSoyad.setText(txtAdSoyad.getText().toString().trim());
			String AdSoyad = txtAdSoyad.getText().toString();

			txtDogumTarih.setText(txtDogumTarih.getText().toString().trim());
			String DogumTarih = txtDogumTarih.getText().toString().trim();

			txtKullaniciAdi.setText(txtKullaniciAdi.getText().toString().trim());
			String KullaniciAdi = txtKullaniciAdi.getText().toString().trim();

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

			if (TextUtils.isEmpty(KullaniciAdi)) {
				txtILKullaniciAdi.setError(getString(R.string.hata_bos_alan));
				txtKullaniciAdi.requestFocus();
				txtKullaniciAdi.setSelection(txtKullaniciAdi.length());
				imm.showSoftInput(txtKullaniciAdi, 0);
			} else if (KullaniciAdi.length() < KullaniciAdiKarakterSayisiMIN) {
				txtILKullaniciAdi.setError(getString(R.string.hata_en_az_karakter, String.valueOf(KullaniciAdiKarakterSayisiMIN)));
				txtKullaniciAdi.requestFocus();
				txtKullaniciAdi.setSelection(txtKullaniciAdi.length());
				imm.showSoftInput(txtKullaniciAdi, 0);
			} else if (KullaniciAdi.length() > KullaniciAdiKarakterSayisiMAX) {
				txtILKullaniciAdi.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(KullaniciAdiKarakterSayisiMAX)));
				txtKullaniciAdi.requestFocus();
				txtKullaniciAdi.setSelection(txtKullaniciAdi.length());
				imm.showSoftInput(txtKullaniciAdi, 0);
			} else if(!AkorDefterimSys.isValid(KullaniciAdi, "KullaniciAdi")) {
				txtILKullaniciAdi.setError(getString(R.string.hata_format_sadece_sayi_kucukharf));
				txtKullaniciAdi.requestFocus();
				txtKullaniciAdi.setSelection(txtKullaniciAdi.length());
				imm.showSoftInput(txtKullaniciAdi, 0);
			} else if(!StringUtils.isAlpha(KullaniciAdi.substring(0, KullaniciAdiBastakiHarfKarakterSayisi))) {
				txtILKullaniciAdi.setError(getString(R.string.hata_format_bastaki_karakterler_harf_olmak_zorunda, String.valueOf(KullaniciAdiBastakiHarfKarakterSayisi)));
				txtKullaniciAdi.requestFocus();
				txtKullaniciAdi.setSelection(txtKullaniciAdi.length());
				imm.showSoftInput(txtKullaniciAdi, 0);
			} else txtILKullaniciAdi.setError(null);

			if(txtILAdSoyad.getError() == null && txtILDogumTarih.getError() == null && txtILKullaniciAdi.getError() == null) {
				btnKaydet.setEnabled(false);
				AkorDefterimSys.UnFocusEditText(txtAdSoyad);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);

				String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
				@SuppressLint("HardwareIds")
				String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
				String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
				String UygulamaVersiyon = "";

				try {
					UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}

				try {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					Date inputDate = format.parse(DogumTarih + " 00:00:00");
					format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					DogumTarih = format.format(inputDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
					PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_guncelleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
					PDBilgilerGuncelleniyor.show();
				}

				AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, AdSoyad, DogumTarih, "","", "", "", KullaniciAdi, "", "", UygulamaVersiyon, "HesapBilgiGuncelle_Profil");
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}

	private void GoogleAPIInit() {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleLoginApiClient = new GoogleApiClient.Builder(this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
	}

	@SuppressLint("HardwareIds")
	private void FacebookLoginInit() {
		LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject JSONFacebookGelenVeri, GraphResponse response) {
						if (response.getError() != null) {
							Log.d("Hata", "Facebook girişi yapılırken hata oluştu..");
						} else {
							try {
								String ResimURL_Facebook = "https://graph.facebook.com/" + JSONFacebookGelenVeri.getString("id") + "/picture?width=300&height=300";

								if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerGuncelleniyor)) {
									PDBilgilerGuncelleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_guncelleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerGuncelleniyor_Timeout");
									PDBilgilerGuncelleniyor.show();
								}

								AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", ResimURL_Facebook,"", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle_Profil_Resim2");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});

				Bundle parameters = new Bundle();
				parameters.putString("fields", "id,name,email");
				request.setParameters(parameters);
				request.executeAsync();
			}

			@Override
			public void onCancel() {
				Log.d("","");
			}

			@Override
			public void onError(FacebookException error) {
				Log.d("","");
			}
		});
	}
}