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
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.Strings;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.util.Date;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class KayitEkran_KullaniciAdi extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	File SecilenProfilResmiFile;
	ProgressDialog PDIslem;
	AlertDialog ADDialog;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnTamamla;
	TextInputLayout txtILKullaniciAdi;
	TextView lblVazgec, lblBaslik, lblKullaniciAdiAciklama, lblKullanimKosullariAciklama;
	EditText txtKullaniciAdi;

	String EPosta = "", Parola = "", AdSoyad = "", DogumTarih = "", EklenenHesapID = "";
	int KullaniciAdiKarakterSayisiMIN = 0, KullaniciAdiKarakterSayisiMAX = 0, KullaniciAdiBastakiHarfKarakterSayisi = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kayit_ekran_kullaniciadi);

		activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		KullaniciAdiKarakterSayisiMIN = getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN);
		KullaniciAdiKarakterSayisiMAX = getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MAX);
		KullaniciAdiBastakiHarfKarakterSayisi = getResources().getInteger(R.integer.KullaniciAdiBastakiHarfKarakterSayisi);

		Bundle mBundle = getIntent().getExtras();
		EPosta = mBundle.getString("EPosta");
		Parola = mBundle.getString("Parola");
		AdSoyad = mBundle.getString("AdSoyad");
		DogumTarih = mBundle.getString("DogumTarih");
		SecilenProfilResmiFile = (File) mBundle.get("SecilenProfilResmiFile");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setTypeface(YaziFontu, Typeface.BOLD);
		lblVazgec.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblKullaniciAdiAciklama = findViewById(R.id.lblKullaniciAdiAciklama);
		lblKullaniciAdiAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblKullaniciAdiAciklama.setText(getString(R.string.kullaniciadi_ekran_belirle, String.valueOf(KullaniciAdiKarakterSayisiMIN), String.valueOf(KullaniciAdiKarakterSayisiMAX)));
		AkorDefterimSys.setTextViewHTML(lblKullaniciAdiAciklama);

		txtILKullaniciAdi = findViewById(R.id.txtILKullaniciAdi);
		txtILKullaniciAdi.setTypeface(YaziFontu);

		txtKullaniciAdi = findViewById(R.id.txtKullaniciAdi);
		txtKullaniciAdi.setTypeface(YaziFontu, Typeface.NORMAL);
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
            }
        });
		txtKullaniciAdi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Tamamla();
				}

				return false;
			}
		});

		lblKullanimKosullariAciklama = findViewById(R.id.lblKullanimKosullariAciklama);
		lblKullanimKosullariAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblKullanimKosullariAciklama.setText(getString(R.string.hizmet_kosullari_aciklama, getString(R.string.uygulama_adi), getString(R.string.hizmet_kosullari_link), getString(R.string.gizlilik_sartlari_link)));
		AkorDefterimSys.setTextViewHTML(lblKullanimKosullariAciklama);

		btnTamamla = findViewById(R.id.btnTamamla);
		btnTamamla.setTypeface(YaziFontu, Typeface.NORMAL);
		btnTamamla.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				txtILKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
				break;
			case R.id.btnGeri:
				onBackPressed();
				break;
			case R.id.lblVazgec:
				AkorDefterimSys.prefAction = "Vazgec";
				onBackPressed();
				break;
			case R.id.btnTamamla:
				Tamamla();
				break;
		}
	}

	@SuppressLint("SimpleDateFormat, HardwareIds")
	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					if(!JSONSonuc.getBoolean("Sonuc")) {
						String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
						String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
						String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
						String ParolaSHA1 = Strings.getSHA1(Parola);
						String KullaniciAdi = txtKullaniciAdi.getText().toString().trim();
						String UygulamaVersiyon = "";

						try {
							UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}

						try {
							SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
							Date inputDate = format.parse(DogumTarih);
							format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							DogumTarih = format.format(inputDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}

						AkorDefterimSys.HesapEkle(FirebaseToken, OSID, OSVersiyon, AdSoyad, DogumTarih, SecilenProfilResmiFile != null, EPosta, Parola, ParolaSHA1, KullaniciAdi, UygulamaVersiyon);
					} else {
						btnTamamla.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kullaniciadi_kayitli));
					}

					break;
				case "HesapEkle":
					if(JSONSonuc.getBoolean("Sonuc")) { // Kayıt yapıldıysa
						EklenenHesapID = JSONSonuc.getString("EklenenHesapID");

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefHesapID", EklenenHesapID);
						sharedPrefEditor.putString("prefEPosta", EPosta);
						sharedPrefEditor.putString("prefParolaSHA1", Strings.getSHA1(Parola));
						sharedPrefEditor.putString("prefOturumTipi", "Cevrimici");
						sharedPrefEditor.apply();

						// Profil resmi seçilmediyse
						if(SecilenProfilResmiFile == null) SonrakiEkran();
						else new ProfilResimYukle().execute();
					} else { // Kayıt yapılmadıysa
						btnTamamla.setEnabled(true);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					}

					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void Tamamla() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			txtKullaniciAdi.setText(txtKullaniciAdi.getText().toString().trim());
			String KullaniciAdi = txtKullaniciAdi.getText().toString().trim();

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

			if(txtILKullaniciAdi.getError() == null) {
				btnTamamla.setEnabled(false);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);

				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.profiliniz_olusturuluyor_lutfen_bekleyiniz), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "");
					PDIslem.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, "", "", KullaniciAdi, "HesapBilgiGetir");
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@SuppressLint({"InflateParams", "StaticFieldLeak"})
	private class ProfilResimYukle extends AsyncTask<Void, Integer, String> {
		long totalSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PDIslem.setMessage(getString(R.string.profil_resmi_yukleniyor, "0%"));
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
				entity.addPart("DosyaAdi", new StringBody(EklenenHesapID));

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
							if(SecilenProfilResmiFile != null && SecilenProfilResmiFile.exists()) SecilenProfilResmiFile.delete();
							SonrakiEkran();
						}
					});
					break;
				case "FileNotFoundException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
						}
					});
					break;
				case "MalformedURLException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.url_hatasi));
						}
					});
					break;
				case "IOException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.dosya_yazma_okuma_hatasi));
						}
					});
					break;
				default:
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.beklenmedik_hata_meydana_geldi));
						}
					});
					break;
			}

			// PDIslem Progress Dialog'u kapattık
			AkorDefterimSys.DismissProgressDialog(PDIslem);
		}

		@Override
		protected void onProgressUpdate(final Integer... Deger) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					PDIslem.setMessage(getString(R.string.profil_resmi_yukleniyor, String.valueOf(Deger[0]) + "%"));
				}
			});
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			// PDIslem Progress Dialog'u kapattık
			AkorDefterimSys.DismissProgressDialog(PDIslem);
		}
	}

	private void SonrakiEkran() {
		btnTamamla.setEnabled(true);
		AkorDefterimSys.DismissProgressDialog(PDIslem);

		Intent mIntent = new Intent(activity, AnaEkran.class);
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.putExtra("Islem", "");

		AkorDefterimSys.EkranGetir(mIntent, "Normal");

		finishAffinity(); // Bu komut altta açık olan tüm etkinlikleri sonlandırmaya yarar..
	}
}