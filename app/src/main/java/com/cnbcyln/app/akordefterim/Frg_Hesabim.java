package com.cnbcyln.app.akordefterim;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.CircleImageView;

import com.theartofdev.edmodo.cropper.CropImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

@SuppressWarnings({"deprecation", "resource", "ConstantConditions", "ResultOfMethodCallIgnored"})
@SuppressLint({ "HandlerLeak", "DefaultLocale", "InflateParams" })
public class Frg_Hesabim extends Fragment implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	InputMethodManager imm;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	Random rnd;
	AlertDialog ADDialog, ADDialog_InternetBaglantisi;
	LayoutInflater inflater;
	View ViewDialogContent;
	Resources RES;
	AnimationDrawable frameAnimation;
	ProgressDialog PDDialog;

	Button btnKaydet;
	EditText EtxtAdSoyad, EtxtDogumTarih, EtxtKullaniciAdi, EtxtCepTelefon, EtxtEmail, Dialog_txtOnayKodu;
	ImageView ImgBuyukProfilResim, ImgKameraIcon, ImgCepTelefonWarningIcon, ImgEmailWarningIcon;
	CircleImageView CImgKucukProfilResim;
	TextView lblHesapBilgileriBaslik, lblAdSoyad, lblDogumTarihi, lblKullaniciAdi, lblCepTelefon, lblEmail, Dialog_lblOnayKoduBilgilendirme;

	private static final int RESIMSEC = 1;
	private static final int SMSALMAIZIN = 2;
	private static final int DOSYAOKUMAYAZMA_IZIN = 3;

	String GeciciAdSoyadBilgi, GeciciKullaniciAdiBilgi, GeciciEmailBilgi, GeciciCepTelefonBilgi;
	Bitmap ProfilResimBitmap;
	Boolean DogumTarihiAyarlandiMi = true;
	int OnayKodu;
	File SecilenProfilResim;

	Handler CepOnayKoduHandler = new Handler();
	Timer CepOnayKoduSayac;
	TimerTask CepOnayKoduDialogSayac;
	int CepOnayKoduKalanSure = 0;

	Handler EmailOnayKoduHandler = new Handler();
	Timer EmailOnayKoduSayac;
	TimerTask EmailOnayKoduDialogSayac;
	int EPostaOnayKoduKalanSure = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_frg_hesabim, container, false);
	}

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = new AkorDefterimSys(activity);
		RES = activity.getResources();
		rnd = new Random();
		FragmentDataConn = (Interface_FragmentDataConn) activity;
		CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
		EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;

		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		inflater = activity.getLayoutInflater();

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.putString("prefSMSOnayKoduSayfaAdi", "Frg_Hesabim");
		sharedPrefEditor.apply();

		activity.registerReceiver(OnayKoduAlici, new IntentFilter("com.cnbcyln.app.akordefterim.Frg_Hesabim"));

		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		ImgBuyukProfilResim = (ImageView) activity.findViewById(R.id.ImgBuyukProfilResim);

		CImgKucukProfilResim = (CircleImageView) activity.findViewById(R.id.CImgKucukProfilResim);
		CImgKucukProfilResim.setOnClickListener(this);
		registerForContextMenu(CImgKucukProfilResim);

		ImgKameraIcon = (ImageView) activity.findViewById(R.id.ImgKameraIcon);
		ImgKameraIcon.setOnClickListener(this);
		registerForContextMenu(ImgKameraIcon);

		lblHesapBilgileriBaslik = (TextView) activity.findViewById(R.id.lblHesapBilgileriBaslik);
		lblHesapBilgileriBaslik.setTypeface(YaziFontu);

		lblAdSoyad = (TextView) activity.findViewById(R.id.lblAdSoyad);
		lblAdSoyad.setTypeface(YaziFontu);

		EtxtAdSoyad = (EditText) activity.findViewById(R.id.EtxtAdSoyad);
		EtxtAdSoyad.setTypeface(YaziFontu);

		lblDogumTarihi = (TextView) activity.findViewById(R.id.lblDogumTarihi);
		lblDogumTarihi.setTypeface(YaziFontu);

		EtxtDogumTarih = (EditText) activity.findViewById(R.id.EtxtDogumTarih);
		EtxtDogumTarih.setTypeface(YaziFontu);
		EtxtDogumTarih.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int year, month, day;
				Calendar mcurrentTime = Calendar.getInstance();

				imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);

				if (DogumTarihiAyarlandiMi) {
					String[] Tarih = EtxtDogumTarih.getText().toString().split("[.]");

					day = Integer.parseInt(Tarih[0]);
					month = Integer.parseInt(Tarih[1]) - 1;
					year = Integer.parseInt(Tarih[2]);
				} else {
					year = mcurrentTime.get(Calendar.YEAR); // Güncel Yılı alıyoruz
					month = mcurrentTime.get(Calendar.MONTH); // Güncel Ayı alıyoruz
					day = mcurrentTime.get(Calendar.DAY_OF_MONTH); // Güncel Günü alıyoruz
				}

				DatePickerDialog datePicker; // Datepicker objemiz
				datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// Ayarla butonu tıklandığında textview'a yazdırıyoruz
						String Gun, Ay;

						if (String.valueOf(dayOfMonth).length() > 1) Gun = String.valueOf(dayOfMonth);
						else Gun = "0" + String.valueOf(dayOfMonth);

						if (String.valueOf(monthOfYear).length() > 1) Ay = String.valueOf(monthOfYear + 1);
						else Ay = "0" + String.valueOf(monthOfYear + 1);

						EtxtDogumTarih.setText(Gun + "." + Ay + "." + year);

						DogumTarihiAyarlandiMi = true;
					}
				}, year, month, day); // Başlarken set edilecek değerlerimizi atıyoruz

				datePicker.setTitle(getString(R.string.tarih_secin));
				datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ayarla), datePicker);
				datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.iptal), datePicker);
                /*datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
					}
				});*/
				datePicker.show();
			}
		});

		EtxtDogumTarih.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					int year, month, day;
					Calendar mcurrentTime = Calendar.getInstance();

					imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);

					if (DogumTarihiAyarlandiMi) {
						String[] Tarih = EtxtDogumTarih.getText().toString().split("[.]");

						day = Integer.parseInt(Tarih[0]);
						month = Integer.parseInt(Tarih[1]) - 1;
						year = Integer.parseInt(Tarih[2]);
					} else {
						year = mcurrentTime.get(Calendar.YEAR); // Güncel Yılı alıyoruz
						month = mcurrentTime.get(Calendar.MONTH); // Güncel Ayı alıyoruz
						day = mcurrentTime.get(Calendar.DAY_OF_MONTH); // Güncel Günü alıyoruz
					}

					DatePickerDialog datePicker; // Datepicker objemiz
					datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							// Ayarla butonu tıklandığında textview'a yazdırıyoruz
							String Gun, Ay;

							if (String.valueOf(dayOfMonth).length() > 1) Gun = String.valueOf(dayOfMonth);
							else Gun = "0" + String.valueOf(dayOfMonth);

							if (String.valueOf(monthOfYear).length() > 1) Ay = String.valueOf(monthOfYear + 1);
							else Ay = "0" + String.valueOf(monthOfYear + 1);

							EtxtDogumTarih.setText(Gun + "." + Ay + "." + year);

							DogumTarihiAyarlandiMi = true;
						}
					}, year, month, day); // Başlarken set edilecek değerlerimizi atıyoruz

					datePicker.setTitle(getString(R.string.tarih_secin));
					datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ayarla), datePicker);
					datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.iptal), datePicker);
					datePicker.show();
				}
			}
		});

		lblKullaniciAdi = (TextView) activity.findViewById(R.id.lblKullaniciAdi);
		lblKullaniciAdi.setTypeface(YaziFontu);

		EtxtKullaniciAdi = (EditText) activity.findViewById(R.id.EtxtKullaniciAdi);
		EtxtKullaniciAdi.setTypeface(YaziFontu);

		lblCepTelefon = (TextView) activity.findViewById(R.id.lblCepTelefon);
		lblCepTelefon.setTypeface(YaziFontu);

		EtxtCepTelefon = (EditText) activity.findViewById(R.id.EtxtCepTelefon);
		EtxtCepTelefon.setTypeface(YaziFontu);
		EtxtCepTelefon.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				EtxtCepTelefon.removeTextChangedListener(this);
				EtxtCepTelefon.setText(s.toString().replaceAll("[^\\d]", ""));
				Selection.setSelection(EtxtCepTelefon.getText(), EtxtCepTelefon.getText().toString().length());
				EtxtCepTelefon.addTextChangedListener(this);

				if(!EtxtCepTelefon.getText().toString().isEmpty() && !EtxtCepTelefon.getText().toString().substring(0,1).equals("5")){
					EtxtCepTelefon.removeTextChangedListener(this);
					EtxtCepTelefon.setText("");
					EtxtCepTelefon.addTextChangedListener(this);
					AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata3), Toast.LENGTH_SHORT);

				} else if(EtxtCepTelefon.getText().toString().length() > 10) {
					EtxtCepTelefon.removeTextChangedListener(this);
					EtxtCepTelefon.setText(EtxtCepTelefon.getText().toString().substring(0,10));
					Selection.setSelection(EtxtCepTelefon.getText(), EtxtCepTelefon.getText().toString().length());
					EtxtCepTelefon.addTextChangedListener(this);
				}
			}
		});

		ImgCepTelefonWarningIcon = (ImageView) activity.findViewById(R.id.ImgCepTelefonWarningIcon);
		ImgCepTelefonWarningIcon.setBackgroundResource(R.drawable.sari_unlem_blink);
		ImgCepTelefonWarningIcon.setOnClickListener(this);
		ImgCepTelefonWarningIcon.setVisibility(View.GONE);

		lblEmail = (TextView) activity.findViewById(R.id.lblEmail);
		lblEmail.setTypeface(YaziFontu);

		EtxtEmail = (EditText) activity.findViewById(R.id.EtxtEmail);
		EtxtEmail.setTypeface(YaziFontu);
		EtxtEmail.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					btnKaydet.performClick();
				}

				return false;
			}
		});

		ImgEmailWarningIcon = (ImageView) activity.findViewById(R.id.ImgEmailWarningIcon);
		ImgEmailWarningIcon.setBackgroundResource(R.drawable.sari_unlem_blink);
		ImgEmailWarningIcon.setOnClickListener(this);
		ImgEmailWarningIcon.setVisibility(View.GONE);

		btnKaydet = (Button) activity.findViewById(R.id.btnKaydet);
		btnKaydet.setTypeface(YaziFontu);
		btnKaydet.setOnClickListener(this);

		imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

		new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case RESIMSEC:
					GaleridenFotoAl(data);
					break;
				case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
					CropImage.ActivityResult result = CropImage.getActivityResult(data);

					SecilenProfilResim = new File(result.getUri().toString().substring(7));

					if (SecilenProfilResim.exists()) { //Cihazda bu dosya var mı kontrol ediyoruz..
						double SecilenProfilResimBoyutuMB = ((double) SecilenProfilResim.length()/1024)/1024;

						if(SecilenProfilResimBoyutuMB <= AkorDefterimSys.ProfilResmiResimBoyutuMB) {
							new ProfilResimYukle().execute();
						} else {
							SecilenProfilResim.delete();
							AkorDefterimSys.ToastMsj(activity, getString(R.string.buyuk_profil_resim_hatasi, String.valueOf(AkorDefterimSys.ProfilResmiResimBoyutuMB)), Toast.LENGTH_SHORT);
						}
					} else AkorDefterimSys.ToastMsj(activity, getString(R.string.dosya_bulunamadi), Toast.LENGTH_SHORT);

					break;
			}
		}
	}

	private void GaleridenFotoAl(Intent data) {
		Uri selectedImage = data.getData(); //Intent'ten gelen data

		Intent intent = CropImage.activity(selectedImage)
				.setFixAspectRatio(true)
				.getIntent(activity.getBaseContext());
		startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	private class HesapBilgiGetir extends AsyncTask<String, String, String> {
		String EmailKullaniciAdi, SifreSHA1;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.bilgileriniz_yukleniyor));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			EmailKullaniciAdi = String.valueOf(parametre[0]);
			SifreSHA1 = String.valueOf(parametre[1]);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
			nameValuePairs.add(new BasicNameValuePair("sifresha1", SifreSHA1));
			String sonuc = null;

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPHesapBilgiGetir);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				sonuc = sb.toString();

				JSONObject JSONGelenVeri = new JSONObject(new JSONArray(sonuc).getString(0));

				if(JSONGelenVeri.getInt("sonuc") == 1) {
					URL url = new URL(AkorDefterimSys.ProfilResimleriKlasoruURL + JSONGelenVeri.getString("id") + ".jpg");
					URLConnection urlConnection = url.openConnection();
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					StringBuilder sb2 = new StringBuilder();

					while ((line = bufferedReader.readLine()) != null) {
						sb2.append(line);
					}

					bufferedReader.close();

					if(sb2.toString().equals("You are not allowed to call this page directly.")) {
						ProfilResimBitmap = BitmapFactory.decodeStream((InputStream)new URL(AkorDefterimSys.ProfilResimleriKlasoruURL + "0.jpg").getContent());
					} else {
						HttpURLConnection.setFollowRedirects(false);
						// note : you may also need
						// HttpURLConnection.setInstanceFollowRedirects(false)
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setRequestMethod("HEAD");

						if((con.getResponseCode() == HttpURLConnection.HTTP_OK))
							ProfilResimBitmap = BitmapFactory.decodeStream((InputStream)new URL(AkorDefterimSys.ProfilResimleriKlasoruURL + JSONGelenVeri.getString("id") + ".jpg").getContent());
						else
							ProfilResimBitmap = BitmapFactory.decodeStream((InputStream)new URL(AkorDefterimSys.ProfilResimleriKlasoruURL + "0.jpg").getContent());
					}
				}
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}

			return sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				PDDialog.dismiss();

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						if (JSONGelenVeri.getString("HesapDurum").equals("Ban")) {
							FragmentDataConn.HesapCikisYap(new JSONObject("{\"Islem\":\"HesapCikis\"}"));
							AkorDefterimSys.ToastMsj(activity, getString(R.string.hesap_banlandi, JSONGelenVeri.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)), Toast.LENGTH_SHORT);
						} else if (JSONGelenVeri.getString("HesapDurum").equals("Aktif")) {
							String ID = String.valueOf(JSONGelenVeri.getInt("id"));
							String FirebaseToken = JSONGelenVeri.getString("FirebaseToken");
							String AdSoyad = JSONGelenVeri.getString("AdSoyad");
							String Email = JSONGelenVeri.getString("Email");
							String DogumTarih = JSONGelenVeri.getString("DogumTarih");
							String KullaniciAdi = JSONGelenVeri.getString("KullaniciAdi");
							String CepTelefon = JSONGelenVeri.getString("CepTelefon");
							String CepTelefonOnay = JSONGelenVeri.getString("CepTelefonOnay");
							String EmailOnay = JSONGelenVeri.getString("EmailOnay");

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefFirebaseToken", FirebaseToken);
							sharedPrefEditor.putString("prefHesapID", ID);
							sharedPrefEditor.putString("prefHesapProfilResimUrl", AkorDefterimSys.CBCAPP_HttpsAdres + "/akordefterim/profil_img/" + ID + ".jpg");
							sharedPrefEditor.putString("prefHesapAdSoyad", AdSoyad);
							sharedPrefEditor.putString("prefHesapEmailKullaniciAdi", Email);
							sharedPrefEditor.putString("prefHesapSifreSha1", SifreSHA1);
							sharedPrefEditor.putString("prefOturumTipi", "Normal");
							sharedPrefEditor.apply();

							ImgBuyukProfilResim.setImageBitmap(ProfilResimBitmap);
							CImgKucukProfilResim.setImageBitmap(ProfilResimBitmap);

							EtxtAdSoyad.setText(AdSoyad);
							GeciciAdSoyadBilgi = AdSoyad;

							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
								Date inputDate = format.parse(DogumTarih);
								format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
								EtxtDogumTarih.setText(format.format(inputDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}

							if(KullaniciAdi.equals("")) {
								EtxtKullaniciAdi.setText("");
								GeciciKullaniciAdiBilgi = "";
							} else {
								EtxtKullaniciAdi.setText(KullaniciAdi);
								GeciciKullaniciAdiBilgi = KullaniciAdi;
							}

							if(CepTelefon.equals("")) {
								EtxtCepTelefon.setText("");
								GeciciCepTelefonBilgi = "";

								ImgCepTelefonWarningIcon.setVisibility(View.GONE);
							} else if(CepTelefonOnay.equals("0")) {
								EtxtCepTelefon.setText(CepTelefon);
								GeciciCepTelefonBilgi = CepTelefon;

								frameAnimation = (AnimationDrawable) ImgCepTelefonWarningIcon.getBackground();
								frameAnimation.start();

								ImgCepTelefonWarningIcon.setVisibility(View.VISIBLE);
							} else {
								EtxtCepTelefon.setText(CepTelefon);
								GeciciCepTelefonBilgi = CepTelefon;

								ImgCepTelefonWarningIcon.setVisibility(View.GONE);
							}

							if(EmailOnay.equals("0")) {
								frameAnimation = (AnimationDrawable) ImgEmailWarningIcon.getBackground();
								frameAnimation.start();

								ImgEmailWarningIcon.setVisibility(View.VISIBLE);
							} else ImgEmailWarningIcon.setVisibility(View.GONE);

							EtxtEmail.setText(Email);
							GeciciEmailBilgi = Email;
						}

						break;
					case 0:
						AkorDefterimSys.HesapPrefSifirla();
						AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.CImgKucukProfilResim:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
						requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOSYAOKUMAYAZMA_IZIN);
					else {
						Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						PickPictureIntent.setType("image/*");
						startActivityForResult(Intent.createChooser(PickPictureIntent, getString(R.string.bir_fotograf_secin)), RESIMSEC);
					}
				} else {
					Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					PickPictureIntent.setType("image/*");
					startActivityForResult(Intent.createChooser(PickPictureIntent, getString(R.string.bir_fotograf_secin)), RESIMSEC);
				}

				break;
			case R.id.ImgKameraIcon:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
						requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOSYAOKUMAYAZMA_IZIN);
					else {
						Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						PickPictureIntent.setType("image/*");
						startActivityForResult(Intent.createChooser(PickPictureIntent, getString(R.string.bir_fotograf_secin)), RESIMSEC);
					}
				} else {
					Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					PickPictureIntent.setType("image/*");
					startActivityForResult(Intent.createChooser(PickPictureIntent, getString(R.string.bir_fotograf_secin)), RESIMSEC);
				}

				break;
			case R.id.btnKaydet:
				EtxtAdSoyad.setText(EtxtAdSoyad.getText().toString().trim());
				EtxtDogumTarih.setText(EtxtDogumTarih.getText().toString().trim());
				EtxtKullaniciAdi.setText(EtxtKullaniciAdi.getText().toString().trim());
				EtxtCepTelefon.setText(EtxtCepTelefon.getText().toString().trim());
				EtxtEmail.setText(EtxtEmail.getText().toString().trim());

				String AdSoyad = EtxtAdSoyad.getText().toString();
				String DogumTarih = EtxtDogumTarih.getText().toString();
				String KullaniciAdi = EtxtKullaniciAdi.getText().toString();
				String CepTelefon = EtxtCepTelefon.getText().toString();
				String Email = EtxtEmail.getText().toString();

				if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
					if(TextUtils.isEmpty(AdSoyad)) {
						EtxtAdSoyad.requestFocus();
						EtxtAdSoyad.setSelection(EtxtAdSoyad.getText().length());
						imm.showSoftInput(EtxtAdSoyad, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtadsoyad_hata1), Toast.LENGTH_SHORT);

					} else if(!AkorDefterimSys.isValid(AdSoyad, "SadeceKucukHarfBuyukHarfBosluklu")) {
						EtxtAdSoyad.requestFocus();
						EtxtAdSoyad.setSelection(AdSoyad.length());
						imm.showSoftInput(EtxtAdSoyad, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtadsoyad_hata2), Toast.LENGTH_SHORT);

					} else if(AkorDefterimSys.EditTextKarakterKontrol(AdSoyad, RES.getInteger(R.integer.AdSoyadKarakterSayisi_MIN), RES.getInteger(R.integer.AdSoyadKarakterSayisi_MAX))) {
						EtxtAdSoyad.requestFocus();
						EtxtAdSoyad.setSelection(AdSoyad.length());
						imm.showSoftInput(EtxtAdSoyad, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtadsoyad_hata3, String.valueOf(RES.getInteger(R.integer.AdSoyadKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.AdSoyadKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

					} else if(TextUtils.isEmpty(DogumTarih)) {
						EtxtDogumTarih.requestFocus();
						EtxtDogumTarih.setSelection(DogumTarih.length());
						imm.showSoftInput(EtxtDogumTarih, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtdogumtarih_hata1), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(GeciciKullaniciAdiBilgi) && TextUtils.isEmpty(KullaniciAdi)) {
						EtxtKullaniciAdi.requestFocus();
						EtxtKullaniciAdi.setSelection(KullaniciAdi.length());
						imm.showSoftInput(EtxtKullaniciAdi, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata1), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(KullaniciAdi) && !AkorDefterimSys.isValid(KullaniciAdi, "SadeceSayiKucukHarfTurkceKaraktersiz")){
						EtxtKullaniciAdi.requestFocus();
						EtxtKullaniciAdi.setSelection(KullaniciAdi.length());
						imm.showSoftInput(EtxtKullaniciAdi, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata2), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(KullaniciAdi) && AkorDefterimSys.EditTextKarakterKontrol(KullaniciAdi, RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN), RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MAX))) {
						EtxtKullaniciAdi.requestFocus();
						EtxtKullaniciAdi.setSelection(KullaniciAdi.length());
						imm.showSoftInput(EtxtKullaniciAdi, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata3, String.valueOf(RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

					} else if (!TextUtils.isEmpty(GeciciCepTelefonBilgi) && TextUtils.isEmpty(CepTelefon)) {
						EtxtCepTelefon.requestFocus();
						EtxtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(EtxtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata1), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(CepTelefon) && !AkorDefterimSys.isValid(CepTelefon, "SadeceSayi")) {
						EtxtCepTelefon.requestFocus();
						EtxtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(EtxtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata2), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(CepTelefon) && CepTelefon.length() != 10) {
						EtxtCepTelefon.requestFocus();
						EtxtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(EtxtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata3), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(CepTelefon) && !CepTelefon.subSequence(0, 1).equals("5")) {
						EtxtCepTelefon.requestFocus();
						EtxtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(EtxtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata3), Toast.LENGTH_SHORT);

					} else if(TextUtils.isEmpty(Email)) {
						EtxtEmail.requestFocus();
						EtxtEmail.setSelection(Email.length());
						imm.showSoftInput(EtxtEmail, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtemail_hata1), Toast.LENGTH_SHORT);

					} else if(!AkorDefterimSys.isValid(Email, "Email") /*|| EtxtEmail.getText().subSequence(EtxtEmail.length() - 10, EtxtEmail.length()).toString().equals("@mvrht.com")*/) {
						EtxtEmail.requestFocus();
						EtxtEmail.setSelection(Email.length());
						imm.showSoftInput(EtxtEmail, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtemail_hata2), Toast.LENGTH_SHORT);

					} else {
						new HesapBilgiGuncelle().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""), AdSoyad, DogumTarih, KullaniciAdi, CepTelefon, Email);

						imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);
					}
				} else {
					imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

					ADDialog_InternetBaglantisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.internet_baglantisi),
							getString(R.string.internet_baglantisi_saglanamadi),
							activity.getString(R.string.tamam));
					ADDialog_InternetBaglantisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog_InternetBaglantisi.show();

					ADDialog_InternetBaglantisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog_InternetBaglantisi.cancel();
						}
					});
				}

				break;
			case R.id.ImgCepTelefonWarningIcon:
				if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
					if(!GeciciCepTelefonBilgi.equals("")) EtxtCepTelefon.setText(GeciciCepTelefonBilgi);

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						if (activity.checkSelfPermission(android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
							requestPermissions(new String[]{android.Manifest.permission.RECEIVE_SMS}, SMSALMAIZIN);
						else {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
									getString(R.string.ceptelefon_onayi_baslik),
									getString(R.string.ceptelefon_onayi_icerik, EtxtCepTelefon.getText()),
									getString(R.string.devam),
									getString(R.string.vazgec));
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();

							ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									new DogrulamaOnayKoduYolla().execute(GeciciAdSoyadBilgi, GeciciCepTelefonBilgi, "TelefonYontemi");
									ADDialog.cancel();
								}
							});

							ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog.cancel();
								}
							});
						}
					} else {
						ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								getString(R.string.ceptelefon_onayi_baslik),
								getString(R.string.ceptelefon_onayi_icerik, EtxtCepTelefon.getText()),
								getString(R.string.devam),
								getString(R.string.vazgec));
						ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog.show();

						ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								new DogrulamaOnayKoduYolla().execute(GeciciAdSoyadBilgi, GeciciCepTelefonBilgi, "TelefonYontemi");
								ADDialog.cancel();
							}
						});

						ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog.cancel();
							}
						});
					}
				} else {
					imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

					ADDialog_InternetBaglantisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.internet_baglantisi),
							getString(R.string.internet_baglantisi_saglanamadi),
							activity.getString(R.string.tamam));
					ADDialog_InternetBaglantisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog_InternetBaglantisi.show();

					ADDialog_InternetBaglantisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog_InternetBaglantisi.cancel();
						}
					});
				}

				break;
			case R.id.ImgEmailWarningIcon:
				if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
					if(!GeciciEmailBilgi.equals("")) EtxtEmail.setText(GeciciEmailBilgi);

					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.email_onayi_baslik),
							getString(R.string.email_onayi_icerik3, EtxtEmail.getText()),
							getString(R.string.devam),
							getString(R.string.vazgec));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new DogrulamaOnayKoduYolla().execute(GeciciAdSoyadBilgi, GeciciEmailBilgi, "EmailYontemi");
							ADDialog.cancel();
						}
					});

					ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();
						}
					});
				} else {
					imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

					ADDialog_InternetBaglantisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.internet_baglantisi),
							getString(R.string.internet_baglantisi_saglanamadi),
							activity.getString(R.string.tamam));
					ADDialog_InternetBaglantisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog_InternetBaglantisi.show();

					ADDialog_InternetBaglantisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog_InternetBaglantisi.cancel();
						}
					});
				}

				break;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case SMSALMAIZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.ceptelefon_onayi_baslik),
							getString(R.string.ceptelefon_onayi_icerik, EtxtCepTelefon.getText()),
							getString(R.string.devam),
							getString(R.string.vazgec));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new DogrulamaOnayKoduYolla().execute(GeciciAdSoyadBilgi, GeciciCepTelefonBilgi, "TelefonYontemi");
							ADDialog.cancel();
						}
					});

					ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();
						}
					});
				} else {
					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.uygulama_izni),
							getString(R.string.uygulama_izni_sms_alma_hata),
							activity.getString(R.string.tamam));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();
						}
					});
				}

				break;
			case DOSYAOKUMAYAZMA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					PickPictureIntent.setType("image/*");
					startActivityForResult(Intent.createChooser(PickPictureIntent, getString(R.string.bir_fotograf_secin)), RESIMSEC);
				} else {
					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.uygulama_izni),
							getString(R.string.uygulama_izni_dosya_yazma_hata),
							activity.getString(R.string.tamam));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();
						}
					});
				}

				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private class HesapBilgiGuncelle extends AsyncTask<String, String, String> {
		String EmailKullaniciAdi, SifreSHA1, AdSoyad, DogumTarih, KullaniciAdi, CepTelefon, Email;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.bilgileriniz_guncelleniyor));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... parametre) {
			EmailKullaniciAdi = String.valueOf(parametre[0]);
			SifreSHA1 = String.valueOf(parametre[1]);
			AdSoyad = String.valueOf(parametre[2]);
			DogumTarih = String.valueOf(parametre[3]);
			KullaniciAdi = String.valueOf(parametre[4]);
			CepTelefon = String.valueOf(parametre[5]);
			Email = String.valueOf(parametre[6]);

			try {
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				Date inputDate = format.parse(DogumTarih + " 00:00:00");
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				DogumTarih = format.format(inputDate);
			} catch (ParseException e) {
				e.printStackTrace();
				return getString(R.string.hata) + ": " + e.toString();
			}

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = null;

			List<String> Islemler = new ArrayList<>();
			Boolean KontroldeSonucBulunduMu = false;
			String sonuc = "";

			if(!GeciciKullaniciAdiBilgi.equals(KullaniciAdi)) { //Kullanıcı adı değiştirildiyse, kullanıcı adı kontrolü yapıyoruz
				Islemler.add("KullaniciAdiSorgula");
			}

			if(!GeciciCepTelefonBilgi.equals(CepTelefon)) { //CepTelefonu değiştirildiyse, cep telefonu kontrolü yapıyoruz..
				Islemler.add("CepTelefonSorgula");
			}

			if(!GeciciEmailBilgi.equals(Email)) { //Email değiştirildiyse, email kontrolü yapıyoruz..
				Islemler.add("EmailSorgula");
			}

			try{
				for (int i = 0; i < Islemler.size(); i++) {
					if(Islemler.get(i).equals("KullaniciAdiSorgula")) {
						nameValuePairs.add(new BasicNameValuePair("kullaniciadi", KullaniciAdi));
						httpPost = new HttpPost(AkorDefterimSys.PHPKullaniciAdiKontrol);
					} else if(Islemler.get(i).equals("EmailSorgula")) {
						nameValuePairs.add(new BasicNameValuePair("email", Email));
						httpPost = new HttpPost(AkorDefterimSys.PHPEmailKontrol);
					} else if(Islemler.get(i).equals("CepTelefonSorgula")) {
						nameValuePairs.add(new BasicNameValuePair("ceptelefon", CepTelefon));
						httpPost = new HttpPost(AkorDefterimSys.PHPCepTelefonKontrol);
					}

					assert httpPost != null;
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
					StringBuilder sb = new StringBuilder();

					String line;

					while ((line = reader.readLine()) != null)
					{
						sb.append(line).append("\n");
					}

					sonuc = sb.toString();

					JSONObject JSONGelenVeri = new JSONObject(new JSONArray(sonuc).getString(0));

					if (JSONGelenVeri.getInt("sonuc") == 0) {
						KontroldeSonucBulunduMu = true;
						break;
					}
				}

				if(!KontroldeSonucBulunduMu) {
					nameValuePairs = new ArrayList<>();
					nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
					nameValuePairs.add(new BasicNameValuePair("sifresha1", SifreSHA1));
					nameValuePairs.add(new BasicNameValuePair("adsoyad", AdSoyad));
					nameValuePairs.add(new BasicNameValuePair("dogumtarih", DogumTarih));

					if(!GeciciKullaniciAdiBilgi.equals(KullaniciAdi)) nameValuePairs.add(new BasicNameValuePair("kullaniciadi", KullaniciAdi));

					if(!GeciciCepTelefonBilgi.equals(CepTelefon)) {
						nameValuePairs.add(new BasicNameValuePair("ceptelefon", CepTelefon));
						nameValuePairs.add(new BasicNameValuePair("ceptelefononay", "0"));
					}

					if (!GeciciEmailBilgi.equals(Email)) {
						nameValuePairs.add(new BasicNameValuePair("email", Email));
						nameValuePairs.add(new BasicNameValuePair("emailonay", "0"));
					}

					httpClient = new DefaultHttpClient();
					httpPost = new HttpPost(AkorDefterimSys.PHPHesapGuncelle);
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					HttpResponse response = httpClient.execute(httpPost);
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
					StringBuilder sb = new StringBuilder();

					String line;

					while ((line = reader.readLine()) != null) {
						sb.append(line).append("\n");
					}

					sonuc = sb.toString();
				}
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}

			return sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				PDDialog.dismiss();

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						if(!GeciciAdSoyadBilgi.equals(AdSoyad)) {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefHesapAdSoyad", AdSoyad);
							sharedPrefEditor.apply();
						}

						if(!GeciciEmailBilgi.equals(Email) && sharedPref.getString("prefHesapEmailKullaniciAdi", "").contains("@")) {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefHesapEmailKullaniciAdi", Email);
							sharedPrefEditor.apply();
						} else if(!GeciciKullaniciAdiBilgi.equals(KullaniciAdi) && !sharedPref.getString("prefHesapEmailKullaniciAdi", "").contains("@")) {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefHesapEmailKullaniciAdi", KullaniciAdi);
							sharedPrefEditor.apply();
						}

						new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""));

						imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

						AkorDefterimSys.ToastMsj(activity, getString(R.string.hesap_bilgileri_guncellendi), Toast.LENGTH_SHORT);

						break;
					case 0:
						if(JSONGelenVeri.getString("aciklama").equals("kullaniciadi bulundu")) {
							EtxtKullaniciAdi.requestFocus();
							EtxtKullaniciAdi.setSelection(EtxtKullaniciAdi.getText().length());
							imm.showSoftInput(EtxtKullaniciAdi, 0);
							AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata4), Toast.LENGTH_SHORT);

						} else if(JSONGelenVeri.getString("aciklama").equals("ceptelefon bulundu")) {
							EtxtCepTelefon.requestFocus();
							EtxtCepTelefon.setSelection(EtxtCepTelefon.getText().length());
							imm.showSoftInput(EtxtCepTelefon, 0);
							AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata4), Toast.LENGTH_SHORT);

						} else if(JSONGelenVeri.getString("aciklama").equals("email bulundu")) {
							EtxtEmail.requestFocus();
							EtxtEmail.setSelection(EtxtEmail.getText().length());
							imm.showSoftInput(EtxtEmail, 0);
							AkorDefterimSys.ToastMsj(activity, getString(R.string.hata_format_eposta), Toast.LENGTH_SHORT);

						} else if(JSONGelenVeri.getString("aciklama").equals("güncelleme işlemi başarısız")) {
							imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

							AkorDefterimSys.ToastMsj(activity, getString(R.string.hesap_bilgileri_guncellenemedi), Toast.LENGTH_SHORT);

						} else if(JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
							imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						}

						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@SuppressLint("InflateParams")
	private class ProfilResimYukle extends AsyncTask<Void, Integer, String> {
		long totalSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.profil_resmi_yukleniyor, "0%"));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(Void... parametre) {
			/*EmailKullaniciAdi = String.valueOf(parametre[0]);
			SifreSHA1 = String.valueOf(parametre[1]);
			SecilenProfilResimDizin = String.valueOf(parametre[2]);

			String sonuc = "";

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
			nameValuePairs.add(new BasicNameValuePair("sifresha1", SifreSHA1));

			Bitmap SecilenResimBitmap = BitmapFactory.decodeFile(SecilenProfilResimDizin);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			SecilenResimBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();

			nameValuePairs.add(new BasicNameValuePair("secilenresim_base64", Base64.encodeToString(byteArray, Base64.DEFAULT)));

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPProfilResimYukle);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null)
				{
					sb.append(line).append("\n");
				}

				sonuc = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return sonuc;*/
			return UploadFile();
		}

		private String UploadFile() {
			String Sonuc;

			try {
				Bitmap SecilenResimBitmap = BitmapFactory.decodeFile(SecilenProfilResim.getAbsolutePath());
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
				entity.addPart("dosya", new FileBody(SecilenProfilResim));
				entity.addPart("dizin", new StringBody(AkorDefterimSys.ProfilResimleriKlasoruDizin));
				entity.addPart("hesapid", new StringBody(sharedPref.getString("prefHesapID", "")));

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
							ImgBuyukProfilResim.setImageBitmap(BitmapFactory.decodeFile(SecilenProfilResim.getAbsolutePath()));
							CImgKucukProfilResim.setImageBitmap(BitmapFactory.decodeFile(SecilenProfilResim.getAbsolutePath()));

							if(SecilenProfilResim != null && SecilenProfilResim.exists()) SecilenProfilResim.delete();

							AkorDefterimSys.ToastMsj(activity, getString(R.string.profil_resmi_guncellendi), Toast.LENGTH_SHORT);
						}
					});
					break;
				case "FileNotFoundException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						}
					});
					break;
				case "MalformedURLException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.url_hatasi), Toast.LENGTH_SHORT);
						}
					});
					break;
				case "IOException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.dosya_yazma_okuma_hatasi), Toast.LENGTH_SHORT);
						}
					});
					break;
				default:
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.beklenmedik_hata_meydana_geldi), Toast.LENGTH_SHORT);
						}
					});
					break;
			}

			PDDialog.dismiss();
		}

		@Override
		protected void onProgressUpdate(final Integer... Deger) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					PDDialog.setMessage(getString(R.string.profil_resmi_yukleniyor, String.valueOf(Deger[0]) + "%"));
				}
			});
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	public void CepOnayKoduDialogSayac_Ayarla() {
		CepOnayKoduDialogSayac = new TimerTask() {
			@Override
			public void run() {
				CepOnayKoduHandler.post(new Runnable() {
					public void run() {
						if (CepOnayKoduKalanSure == 0) {
							ADDialog.cancel();

							if (CepOnayKoduSayac != null) {
								CepOnayKoduSayac.cancel();
								CepOnayKoduSayac = null;
								CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
							}

							AkorDefterimSys.ToastMsj(activity, getString(R.string.onay_kodu_sure_bitti), Toast.LENGTH_SHORT);
						} else {
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_bilgilendirme_cep, AkorDefterimSys.ZamanFormatMMSS(CepOnayKoduKalanSure)));
							CepOnayKoduKalanSure--;
						}
					}
				});
			}
		};
	}

	public void EmailOnayKoduDialogSayac_Ayarla() {
		EmailOnayKoduDialogSayac = new TimerTask() {
			@Override
			public void run() {
				EmailOnayKoduHandler.post(new Runnable() {
					public void run() {
						if (EPostaOnayKoduKalanSure == 0) {
							ADDialog.cancel();

							if (EmailOnayKoduSayac != null) {
								EmailOnayKoduSayac.cancel();
								EmailOnayKoduSayac = null;
								EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
							}

							AkorDefterimSys.ToastMsj(activity, getString(R.string.onay_kodu_sure_bitti), Toast.LENGTH_SHORT);
						} else {
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_bilgilendirme_email, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
							EPostaOnayKoduKalanSure--;
						}
					}
				});
			}
		};
	}

	@SuppressLint("InflateParams")
	private class DogrulamaOnayKoduYolla extends AsyncTask<String, String, String> {
		String AdSoyad, EmailTelefon, Yontem;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.islem_yapiliyor));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			OnayKodu = (100000 + rnd.nextInt(900000));
			AdSoyad = String.valueOf(parametre[0]);
			EmailTelefon = String.valueOf(parametre[1]);
			Yontem = String.valueOf(parametre[2]);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			String sonuc = null;

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost();

				switch (Yontem) {
					case "TelefonYontemi":
						nameValuePairs.add(new BasicNameValuePair("cepno", EmailTelefon));
						nameValuePairs.add(new BasicNameValuePair("mesaj", getString(R.string.sms_onay_kodu_icerik1, AdSoyad, String.valueOf(OnayKodu))));

						httpPost = new HttpPost(AkorDefterimSys.PHPSMSYolla);

						break;
					case "EmailYontemi":
						nameValuePairs.add(new BasicNameValuePair("adsoyad", AdSoyad));
						nameValuePairs.add(new BasicNameValuePair("email", EmailTelefon));
						nameValuePairs.add(new BasicNameValuePair("baslik", getString(R.string.uygulama_adi) + " - " + getString(R.string.mail_onayi)));
						nameValuePairs.add(new BasicNameValuePair("icerik", getString(R.string.mail_onayi_icerik, AdSoyad, String.valueOf(OnayKodu))));

						httpPost = new HttpPost(AkorDefterimSys.PHPEMailYolla);

						break;
				}

				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}

				sonuc = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				PDDialog.dismiss();

				switch (Yontem) {
					case "TelefonYontemi":
						if (JSONGelenVeri.getInt("sonuc") == 1 && JSONGelenVeri.getString("aciklama").equals("mesaj gönderildi")) {
							ViewDialogContent = inflater.inflate(R.layout.dialog_onaykodu, null);
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.dogrulama_kodu), ViewDialogContent, getString(R.string.dogrula), getString(R.string.iptal), getString(R.string.yeniden_gonder));
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

							Dialog_lblOnayKoduBilgilendirme = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblOnayKoduBilgilendirme);
							Dialog_lblOnayKoduBilgilendirme.setTypeface(YaziFontu);
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_bilgilendirme_cep, String.valueOf(CepOnayKoduKalanSure)));

							Dialog_txtOnayKodu = (EditText) ViewDialogContent.findViewById(R.id.Dialog_txtOnayKodu);
							Dialog_txtOnayKodu.setTypeface(YaziFontu);

							ADDialog.show();

							Dialog_txtOnayKodu.requestFocus();
							imm.showSoftInput(Dialog_txtOnayKodu, 0);

							ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Dialog_txtOnayKodu.setText(Dialog_txtOnayKodu.getText().toString().trim());

									if (Dialog_txtOnayKodu.getText().toString().equals("")) {
										AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata1), Toast.LENGTH_SHORT);
									} else if (!Dialog_txtOnayKodu.getText().toString().equals(String.valueOf(OnayKodu))) {
										AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata2), Toast.LENGTH_SHORT);
									} else {
										ADDialog.cancel();

										if (CepOnayKoduSayac != null) {
											CepOnayKoduSayac.cancel();
											CepOnayKoduSayac = null;
											CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
										}

										new OnayVer().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""), "CepTelefon");
									}
								}
							});

							ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog.cancel();

									if (CepOnayKoduSayac != null) {
										CepOnayKoduSayac.cancel();
										CepOnayKoduSayac = null;
										CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
									}
								}
							});

							ADDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(CepOnayKoduKalanSure <= 0){
										if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
											ADDialog.cancel();

											if (CepOnayKoduSayac != null) {
												CepOnayKoduSayac.cancel();
												CepOnayKoduSayac = null;
												CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
											}

											EtxtCepTelefon.setText(GeciciCepTelefonBilgi);

											new DogrulamaOnayKoduYolla().execute(GeciciAdSoyadBilgi, GeciciCepTelefonBilgi, "TelefonYontemi");
										} else {
											imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
											imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
											imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
											imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
											imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

											ADDialog_InternetBaglantisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
													getString(R.string.internet_baglantisi),
													getString(R.string.internet_baglantisi_saglanamadi),
													activity.getString(R.string.tamam));
											ADDialog_InternetBaglantisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
											ADDialog_InternetBaglantisi.show();

											ADDialog_InternetBaglantisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													ADDialog_InternetBaglantisi.cancel();
												}
											});
										}
									} else AkorDefterimSys.ToastMsj(activity, getString(R.string.sure_bitmeden_yeni_onay_kodu_telebinde_bulunamazsiniz), Toast.LENGTH_SHORT);
								}
							});

							CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
							CepOnayKoduSayac = new Timer();
							CepOnayKoduDialogSayac_Ayarla();
							CepOnayKoduSayac.schedule(CepOnayKoduDialogSayac, 10, 1000);
						} else if (JSONGelenVeri.getInt("sonuc") == 0) {
							if (JSONGelenVeri.getString("aciklama").equals("cepno alanı boş")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("mesaj alanı boş")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı mesaj metni")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı api ip hatası")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı gönderici adı")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı sorgu parametresi")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
							}
						}

						break;
					case "EmailYontemi":
						if (JSONGelenVeri.getInt("sonuc") == 1 && JSONGelenVeri.getString("aciklama").equals("email gönderildi")) {
							ViewDialogContent = inflater.inflate(R.layout.dialog_onaykodu, null);
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.dogrulama_kodu), ViewDialogContent, getString(R.string.dogrula), getString(R.string.iptal), getString(R.string.yeniden_gonder));
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

							Dialog_lblOnayKoduBilgilendirme = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblOnayKoduBilgilendirme);
							Dialog_lblOnayKoduBilgilendirme.setTypeface(YaziFontu);
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_bilgilendirme_email, String.valueOf(EPostaOnayKoduKalanSure)));

							Dialog_txtOnayKodu = (EditText) ViewDialogContent.findViewById(R.id.Dialog_txtOnayKodu);
							Dialog_txtOnayKodu.setTypeface(YaziFontu);

							ADDialog.show();

							Dialog_txtOnayKodu.requestFocus();
							imm.showSoftInput(Dialog_txtOnayKodu, 0);

							ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Dialog_txtOnayKodu.setText(Dialog_txtOnayKodu.getText().toString().trim());

									if (Dialog_txtOnayKodu.getText().toString().equals("")) {
										AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata1), Toast.LENGTH_SHORT);
									} else if (!Dialog_txtOnayKodu.getText().toString().equals(String.valueOf(OnayKodu))) {
										AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata2), Toast.LENGTH_SHORT);
									} else {
										ADDialog.cancel();

										if (EmailOnayKoduSayac != null) {
											EmailOnayKoduSayac.cancel();
											EmailOnayKoduSayac = null;
											EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
										}

										new OnayVer().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""), "Email");
									}
								}
							});

							ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog.cancel();

									if (EmailOnayKoduSayac != null) {
										EmailOnayKoduSayac.cancel();
										EmailOnayKoduSayac = null;
										EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
									}
								}
							});

							ADDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog.cancel();

									if (EmailOnayKoduSayac != null) {
										EmailOnayKoduSayac.cancel();
										EmailOnayKoduSayac = null;
										EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
									}

									if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
										EtxtEmail.setText(GeciciEmailBilgi);

										new DogrulamaOnayKoduYolla().execute(GeciciAdSoyadBilgi, GeciciEmailBilgi, "EmailYontemi");
									} else {
										imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
										imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
										imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
										imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
										imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);

										ADDialog_InternetBaglantisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
												getString(R.string.internet_baglantisi),
												getString(R.string.internet_baglantisi_saglanamadi),
												activity.getString(R.string.tamam));
										ADDialog_InternetBaglantisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
										ADDialog_InternetBaglantisi.show();

										ADDialog_InternetBaglantisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												ADDialog_InternetBaglantisi.cancel();
											}
										});
									}
								}
							});

							EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaOnayKoduKalanSure;
							EmailOnayKoduSayac = new Timer();
							EmailOnayKoduDialogSayac_Ayarla();
							EmailOnayKoduSayac.schedule(EmailOnayKoduDialogSayac, 10, 1000);
						} else if (JSONGelenVeri.getInt("sonuc") == 0) {
							if (JSONGelenVeri.getString("aciklama").equals("adsoyad alanı boş")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("email alanı boş")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("baslik alanı boş")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("icerik alanı boş")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("email gönderilemedi")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
								AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
							}
						}

						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	private class OnayVer extends AsyncTask<String, String, String> {
		String EmailKullaniciAdi = "", SifreSHA1 = "", OnaylanacakBilgi = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.bilgileriniz_guncelleniyor));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... parametre) {
			EmailKullaniciAdi = String.valueOf(parametre[0]);
			SifreSHA1 = String.valueOf(parametre[1]);
			OnaylanacakBilgi = String.valueOf(parametre[2]);

			String sonuc = "";

			try{
				List<NameValuePair> nameValuePairs = new ArrayList<>();
				nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
				nameValuePairs.add(new BasicNameValuePair("sifresha1", SifreSHA1));
				nameValuePairs.add(new BasicNameValuePair("onaylanacakbilgi", OnaylanacakBilgi));

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPHesapOnayIslemleri);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null)
				{
					sb.append(line).append("\n");
				}

				sonuc = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				PDDialog.dismiss();

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						if(JSONGelenVeri.getString("aciklama").equals("cep telefon onayi verildi")) {
							new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""));

							AkorDefterimSys.ToastMsj(activity, getString(R.string.ceptelefon_onay_verildi), Toast.LENGTH_SHORT);
						} else if(JSONGelenVeri.getString("aciklama").equals("email onayi verildi")) {
							new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""));

							AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_onay_verildi), Toast.LENGTH_SHORT);
						}

						break;
					case 0:
						if(JSONGelenVeri.getString("aciklama").equals("emailkullaniciadi alanı boş")) AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						else if(JSONGelenVeri.getString("aciklama").equals("sifresha1 alanı boş")) AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						else if(JSONGelenVeri.getString("aciklama").equals("onay işlemi başarısız")) AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						else if(JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

						break;
				}

				imm.hideSoftInputFromWindow(EtxtAdSoyad.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(EtxtEmail.getWindowToken(), 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	private BroadcastReceiver OnayKoduAlici = new BroadcastReceiver() {
		@SuppressWarnings("unused")
		@SuppressLint("InflateParams")
		@Override
		public void onReceive(Context context, Intent intent) {
			String Kimden = intent.getStringExtra("Kimden");
			String OnayKodu = intent.getStringExtra("OnayKodu");

			if(ADDialog.isShowing()) {
				Dialog_txtOnayKodu.setText(OnayKodu);
				Dialog_txtOnayKodu.requestFocus();
				Dialog_txtOnayKodu.setSelection(Dialog_txtOnayKodu.getText().length());
				imm.hideSoftInputFromWindow(Dialog_txtOnayKodu.getWindowToken(), 0);

				if (Dialog_txtOnayKodu.getText().toString().equals("")) {
					AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata1), Toast.LENGTH_SHORT);
				} else if (!Dialog_txtOnayKodu.getText().toString().equals(String.valueOf(OnayKodu))) {
					AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata2), Toast.LENGTH_SHORT);
				} else {
					ADDialog.cancel();

					if (CepOnayKoduSayac != null) {
						CepOnayKoduSayac.cancel();
						CepOnayKoduSayac = null;
						CepOnayKoduKalanSure = AkorDefterimSys.CepOnayKoduKalanSure;
					}

					new OnayVer().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""), "CepTelefon");
				}
			}
		}
	};
}