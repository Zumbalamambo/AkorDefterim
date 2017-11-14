package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.Strings;
import com.google.firebase.iid.FirebaseInstanceId;

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
public class KayitEkran_KullaniciAdi extends AppCompatActivity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
	File SecilenProfilResmiFile;
	ProgressDialog PDKayitIslem;
	AlertDialog ADHesapKayitHata;

	CoordinatorLayout coordinatorLayout;
	Button btnGeri, btnTamamla;
	TextInputLayout txtILKullaniciAdi;
	TextView lblVazgec, lblBaslik, lblKullaniciAdiAciklama, lblKullanimKosullariAciklama;
	EditText txtKullaniciAdi;

	String EPosta, Parola, AdSoyad, DogumTarih, EklenenHesapID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kayit_ekran_kullaniciadi);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		EPosta = mBundle.getString("EPosta");
		Parola = mBundle.getString("Parola");
		AdSoyad = mBundle.getString("AdSoyad");
		DogumTarih = mBundle.getString("DogumTarih");
		SecilenProfilResmiFile = (File) mBundle.get("SecilenProfilResmiFile");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
			}
		});

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
				sharedPrefEditor.putString("Action", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
			}
		});

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblKullaniciAdiAciklama = findViewById(R.id.lblKullaniciAdiAciklama);
		lblKullaniciAdiAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblKullaniciAdiAciklama);

		txtILKullaniciAdi = findViewById(R.id.txtILEPosta);
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
		AkorDefterimSys.setTextViewHTML(lblKullanimKosullariAciklama);

		btnTamamla = findViewById(R.id.btnTamamla);
		btnTamamla.setTypeface(YaziFontu, Typeface.NORMAL);
		btnTamamla.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Tamamla();
			}
		});
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
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
						// PDKayitIslem Progress Dialog'u kapattık
						AkorDefterimSys.DismissProgressDialog(PDKayitIslem);

						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.txtkullaniciadi_hata5));
					}

					break;
				case "HesapEkle":
					if(JSONSonuc.getBoolean("Sonuc")) { // Kayıt yapıldıysa
						EklenenHesapID = JSONSonuc.getString("EklenenHesapID");

						sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
						sharedPrefEditor.putString("prefHesapID", EklenenHesapID);
						sharedPrefEditor.putString("prefEPostaKullaniciAdi", EPosta);
						sharedPrefEditor.putString("prefParolaSHA1", Strings.getSHA1(Parola));
						sharedPrefEditor.putString("prefOturumTipi", "Normal");
						sharedPrefEditor.apply();

						// Profil resmi seçilmediyse
						if(SecilenProfilResmiFile == null) SonrakiEkran();
						else new ProfilResimYukle().execute();
					} else { // Kayıt yapılmadıysa
						// PDKayitIslem Progress Dialog'u kapattık
						switch (JSONSonuc.getString("Aciklama")) {
							case "Kayıtlı hesap bulundu!":
								ADHesapKayitHata = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
										getString(R.string.hesap_durumu),
										getString(R.string.hata_hesap_durum2, EPosta),
										getString(R.string.tamam));
								ADHesapKayitHata.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADHesapKayitHata.show();

								ADHesapKayitHata.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										ADHesapKayitHata.dismiss();

										sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
										sharedPrefEditor.putString("Action", "Vazgec");
										sharedPrefEditor.apply();

										onBackPressed();
									}
								});
								break;
							default:
								AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
								break;
						}


					}

					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void Tamamla() {
		AkorDefterimSys.KlavyeKapat();

		txtKullaniciAdi.setText(txtKullaniciAdi.getText().toString().trim());
		String KullaniciAdi = txtKullaniciAdi.getText().toString().trim();

		if (TextUtils.isEmpty(KullaniciAdi))
			txtILKullaniciAdi.setError(getString(R.string.hata_bos_alan));
		else if (KullaniciAdi.length() < getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))
			txtILKullaniciAdi.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN))));
		else if(!AkorDefterimSys.isValid(KullaniciAdi, "KullaniciAdi"))
			txtILKullaniciAdi.setError(getString(R.string.hata_format_sadece_sayi_kucukharf));
		else txtILKullaniciAdi.setError(null);

		AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);

		if(txtILKullaniciAdi.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				PDKayitIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.profiliniz_olusturuluyor_lutfen_bekleyiniz), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
				PDKayitIslem.show();

				AkorDefterimSys.HesapBilgiGetir(null, txtKullaniciAdi.getText().toString().trim());
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@SuppressLint("InflateParams")
	private class ProfilResimYukle extends AsyncTask<Void, Integer, String> {
		long totalSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PDKayitIslem.setMessage(getString(R.string.profil_resmi_yukleniyor, "0%"));
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
				entity.addPart("Dizin", new StringBody(AkorDefterimSys.ProfilResimleriKlasoruDizin));
				entity.addPart("HesapID", new StringBody(EklenenHesapID));

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

			// PDKayitIslem Progress Dialog'u kapattık
			AkorDefterimSys.DismissProgressDialog(PDKayitIslem);
		}

		@Override
		protected void onProgressUpdate(final Integer... Deger) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					PDKayitIslem.setMessage(getString(R.string.profil_resmi_yukleniyor, String.valueOf(Deger[0]) + "%"));
				}
			});
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			// PDKayitIslem Progress Dialog'u kapattık
			AkorDefterimSys.DismissProgressDialog(PDKayitIslem);
		}
	}

	private void SonrakiEkran() {
		// PDKayitIslem Progress Dialog'u kapattık
		AkorDefterimSys.DismissProgressDialog(PDKayitIslem);

		Intent mIntent = new Intent(activity, Ana.class);
		mIntent.putExtra("Islem", "");

		AkorDefterimSys.EkranGetir(mIntent, "Normal");

		finish();
	}
}