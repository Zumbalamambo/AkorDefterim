package com.cnbcyln.app.akordefterim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Strings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

@SuppressWarnings({"deprecation", "ConstantConditions"})
public class SifremiUnuttum extends Activity implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	InputMethodManager imm;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	Random rnd;
	AlertDialog ADDialog, ADDialog_InternetBaglantisi;
	ProgressDialog PDDialog;
	LayoutInflater inflater;
	View ViewDialogContent;
	Resources RES;
	Timer CepOnayKoduSayac, EmailOnayKoduSayac;
	TimerTask CepOnayKoduDialogSayac, EmailOnayKoduDialogSayac;
	Handler CepOnayKoduHandler = new Handler(), EmailOnayKoduHandler = new Handler();

	EditText EtxtEmailKullaniciAdi, Dialog_txtOnayKodu, Dialog_EtxtYeniSifre, Dialog_EtxtYeniSifreTekrar;
	Button btnAra;
	TextView lblVersiyonNo, lblSifremiUnuttumBaslik, lblSifremiUnuttumBilgi, Dialog_lblSifreSifirlamaYontemBilgi, Dialog_lblOnayKoduBilgilendirme, Dialog_lblSifreSifirlaBilgi;
	RadioButton Dialog_RBTelefon, Dialog_RBEmail;

	int CepOnayKoduKalanSure = 0, EPostaOnayKoduKalanSure;
	int OnayKodu = 0;
	int BulunanHesapID = 0;
	String Versiyon, AdSoyad, Telefon, Email;
	//private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sifremi_unuttum);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		RES = activity.getResources();
		rnd = new Random();
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
		CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Ekranın yalnızca portrait (Dikey) olarak çalışmasını ayarlıyoruz

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, MODE_PRIVATE);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.putString("prefSMSOnayKoduSayfaAdi", "SifremiUnuttum");
		sharedPrefEditor.apply();

		inflater = getLayoutInflater();

		registerReceiver(OnayKoduAlici, new IntentFilter("com.cnbcyln.app.akordefterim.SifremiUnuttum"));

		// Ekran ışığını eğer prefEkranIsigiAydinligi değeri ayarlanmamışsa en parlak olan 255'e ayarlıyoruz. Aksi halde ayar ne ise o ayarlanıyor..
		WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
		//Set the brightness of this window
		layoutpars.screenBrightness = sharedPref.getInt("prefEkranIsigiAydinligi", 255) / (float)255;
		//Apply attribute changes to this window
		getWindow().setAttributes(layoutpars);

		ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.labeltire), getString(R.string.labeltire), getString(R.string.labeltire));

		try {
			Versiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		lblVersiyonNo = findViewById(R.id.lblVersiyonNo);
		lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
		lblVersiyonNo.setText(String.valueOf("v").concat(Versiyon));

		lblSifremiUnuttumBaslik = findViewById(R.id.lblSifremiUnuttumBaslik);
		lblSifremiUnuttumBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblSifremiUnuttumBilgi = findViewById(R.id.lblSifremiUnuttumBilgi);
		lblSifremiUnuttumBilgi.setTypeface(YaziFontu);

		EtxtEmailKullaniciAdi = findViewById(R.id.EtxtEmailKullaniciAdi);
		EtxtEmailKullaniciAdi.setTypeface(YaziFontu);
		EtxtEmailKullaniciAdi.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					KayitAra();
				}

				return false;
			}
		});

		btnAra = findViewById(R.id.btnAra);
		btnAra.setTypeface(YaziFontu);
		btnAra.setOnClickListener(this);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		//client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(OnayKoduAlici);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnAra:
				btnAra.setEnabled(false);
				KayitAra();

				break;
		}
	}

	private void KayitAra() {
		btnAra.setText(btnAra.getText().toString().trim());

		if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
			EtxtEmailKullaniciAdi.setText(EtxtEmailKullaniciAdi.getText().toString().trim());

			if (EtxtEmailKullaniciAdi.getText().toString().equals("")) {
				btnAra.setEnabled(true);
				EtxtEmailKullaniciAdi.requestFocus();
				EtxtEmailKullaniciAdi.setSelection(EtxtEmailKullaniciAdi.getText().length());
				AkorDefterimSys.ToastMsj(activity, getString(R.string.txtemail_kullaniciadi_telefon_hata1), Toast.LENGTH_SHORT);
			} else {
				new KayitKontrol().execute(EtxtEmailKullaniciAdi.getText().toString());

				imm.hideSoftInputFromWindow(EtxtEmailKullaniciAdi.getWindowToken(), 0);
			}
		} else {
			btnAra.setEnabled(true);
			imm.hideSoftInputFromWindow(EtxtEmailKullaniciAdi.getWindowToken(), 0);

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

	@SuppressLint("InflateParams")
	private class KayitKontrol extends AsyncTask<String, String, String> {
		String EmailKullaniciAdi = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.kontrol_ediliyor));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			EmailKullaniciAdi = String.valueOf(parametre[0]);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
			String sonuc = null;

			try {
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
				final JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				PDDialog.dismiss();

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						if(!ADDialog.isShowing()) {
							BulunanHesapID = JSONGelenVeri.getInt("id");
							AdSoyad = JSONGelenVeri.getString("AdSoyad");
							Telefon = JSONGelenVeri.getString("CepTelefon");
							Email = JSONGelenVeri.getString("Email");

							ViewDialogContent = inflater.inflate(R.layout.dialog_sifre_sifirlama_yontemi, null);
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.yontem_secin), ViewDialogContent, getString(R.string.devam), getString(R.string.iptal), false);
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

							Dialog_lblSifreSifirlamaYontemBilgi = ViewDialogContent.findViewById(R.id.Dialog_lblSifreSifirlamaYontemBilgi);
							Dialog_lblSifreSifirlamaYontemBilgi.setTypeface(YaziFontu);
							Dialog_lblSifreSifirlamaYontemBilgi.setText(getString(R.string.sifre_sifirlama_yontem_bilgi, AdSoyad));

							Dialog_RBTelefon = ViewDialogContent.findViewById(R.id.Dialog_RBTelefon);
							Dialog_RBTelefon.setTypeface(YaziFontu);
							Dialog_RBTelefon.setText(Html.fromHtml(getString(R.string.sonu_xx_ile_biten_telefon, Telefon.substring(Telefon.length() - 2))), TextView.BufferType.SPANNABLE);

							Dialog_RBEmail = ViewDialogContent.findViewById(R.id.Dialog_RBEmail);
							Dialog_RBEmail.setTypeface(YaziFontu);
							Dialog_RBEmail.setText(Html.fromHtml(getString(R.string.email_adresine_onay_kodu_gonder, Email.subSequence(0, 2) + "*********" + Email.substring(Email.indexOf("@"), Email.length()))), TextView.BufferType.SPANNABLE);

							ADDialog.show();

							ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
								@TargetApi(Build.VERSION_CODES.M)
								@Override
								public void onClick(View v) {
									if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
										if (Dialog_RBTelefon.isChecked()) {
											ADDialog.cancel();
											new SifreSifirlamaOnayKoduYolla().execute(AdSoyad, Telefon, "TelefonYontemi");
										} else if (Dialog_RBEmail.isChecked()) {
											ADDialog.cancel();
											new SifreSifirlamaOnayKoduYolla().execute(AdSoyad, Email, "EmailYontemi");
										} else {
											AkorDefterimSys.ToastMsj(activity, getString(R.string.sifre_sifirlama_yontemi_secin), Toast.LENGTH_SHORT);
										}
									} else {
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

							ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog.cancel();
								}
							});
						}

						btnAra.setEnabled(true);

						break;
					case 0:
						if (JSONGelenVeri.getString("aciklama").equals("hesap bulunamadı")) {
							AkorDefterimSys.HesapPrefSifirla();
							AkorDefterimSys.ToastMsj(activity, getString(R.string.hesap_bilgileri_bulunamadi), Toast.LENGTH_SHORT);
						} else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
							AkorDefterimSys.HesapPrefSifirla();
							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						}

						btnAra.setEnabled(true);

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

	@SuppressLint("InflateParams")
	private class SifreSifirlamaOnayKoduYolla extends AsyncTask<String, String, String> {
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
						nameValuePairs.add(new BasicNameValuePair("baslik", getString(R.string.uygulama_adi) + " - " + getString(R.string.eposta_sifremiunuttum)));
						nameValuePairs.add(new BasicNameValuePair("icerik", getString(R.string.eposta_sifremiunuttum_icerik, AdSoyad, String.valueOf(OnayKodu))));

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

							Dialog_lblOnayKoduBilgilendirme = ViewDialogContent.findViewById(R.id.Dialog_lblOnayKoduBilgilendirme);
							Dialog_lblOnayKoduBilgilendirme.setTypeface(YaziFontu);
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_cep, String.valueOf(CepOnayKoduKalanSure)));

							Dialog_txtOnayKodu = ViewDialogContent.findViewById(R.id.Dialog_txtOnayKodu);
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
											CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
										}

										ViewDialogContent = inflater.inflate(R.layout.dialog_sifre_sifirla, null);
										ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.sifre_sifirla), ViewDialogContent, getString(R.string.sifirla), getString(R.string.iptal), false);
										ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

										TextView Dialog_lblSifreSifirlaBilgi = ViewDialogContent.findViewById(R.id.Dialog_lblSifreSifirlaBilgi);
										Dialog_lblSifreSifirlaBilgi.setTypeface(YaziFontu);

										final EditText Dialog_EtxtYeniSifre = ViewDialogContent.findViewById(R.id.EtxtYeniSifre);
										Dialog_EtxtYeniSifre.setTypeface(YaziFontu);

										final EditText Dialog_EtxtYeniSifreTekrar = ViewDialogContent.findViewById(R.id.EtxtYeniSifreTekrar);
										Dialog_EtxtYeniSifreTekrar.setTypeface(YaziFontu);

										ADDialog.show();

										ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												Dialog_EtxtYeniSifre.setText(Dialog_EtxtYeniSifre.getText().toString().trim());
												Dialog_EtxtYeniSifreTekrar.setText(Dialog_EtxtYeniSifreTekrar.getText().toString().trim());

												String YeniSifre = Dialog_EtxtYeniSifre.getText().toString();
												String YeniSifreTekrar = Dialog_EtxtYeniSifreTekrar.getText().toString();

												if (TextUtils.isEmpty(YeniSifre)) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata1), Toast.LENGTH_SHORT);

												} else if (AkorDefterimSys.EditTextKarakterKontrol(YeniSifre, RES.getInteger(R.integer.ParolaKarakterSayisi_MIN), RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata2, String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

												} else if (!AkorDefterimSys.isValid(YeniSifre, "SadeceSayiKucukHarfBuyukHarf")) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata3), Toast.LENGTH_SHORT);

												} else if (!AkorDefterimSys.isValid(YeniSifre, "Mutlaka_EnAzBir_Sayi_KucukHarf_Icermeli")) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata4), Toast.LENGTH_SHORT);

												} else if (!AkorDefterimSys.EditTextisMatching(Dialog_EtxtYeniSifre, Dialog_EtxtYeniSifreTekrar)) {
													Dialog_EtxtYeniSifreTekrar.requestFocus();
													Dialog_EtxtYeniSifreTekrar.setSelection(YeniSifreTekrar.length());
													imm.showSoftInput(Dialog_EtxtYeniSifreTekrar, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.yenisifre_yenisifretekrar_uyusmuyor), Toast.LENGTH_SHORT);

												} else {
													ADDialog.dismiss();

													imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifre.getWindowToken(), 0);
													imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifreTekrar.getWindowToken(), 0);

													new SifreDegistir().execute(YeniSifre);
												}
											}
										});

										ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												ADDialog.dismiss();
											}
										});
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
										CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
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
                                                CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
                                            }

                                            new SifreSifirlamaOnayKoduYolla().execute(AdSoyad, Email, "TelefonYontemi");
                                        } else {
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

							CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
							CepOnayKoduSayac = new Timer();
							CepOnayKoduDialogSayac_Ayarla();
							CepOnayKoduSayac.schedule(CepOnayKoduDialogSayac, 10, 1000);
						} else if (JSONGelenVeri.getInt("sonuc") == 0) {
							if (JSONGelenVeri.getString("aciklama").equals("cepno alanı boş")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("mesaj alanı boş")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı mesaj metni")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı api ip hatası")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı gönderici adı")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı sorgu parametresi")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.sms_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
							}
						}

						break;
					case "EmailYontemi":
						if (JSONGelenVeri.getInt("sonuc") == 1 && JSONGelenVeri.getString("aciklama").equals("email gönderildi")) {
							ViewDialogContent = inflater.inflate(R.layout.dialog_onaykodu, null);
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.dogrulama_kodu), ViewDialogContent, getString(R.string.dogrula), getString(R.string.iptal), getString(R.string.yeniden_gonder));
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

							Dialog_lblOnayKoduBilgilendirme = ViewDialogContent.findViewById(R.id.Dialog_lblOnayKoduBilgilendirme);
							Dialog_lblOnayKoduBilgilendirme.setTypeface(YaziFontu);
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_email, String.valueOf(EPostaOnayKoduKalanSure)));

							Dialog_txtOnayKodu = ViewDialogContent.findViewById(R.id.Dialog_txtOnayKodu);
							Dialog_txtOnayKodu.setTypeface(YaziFontu);

							ADDialog.show();

							Dialog_txtOnayKodu.requestFocus();
							imm.showSoftInput(Dialog_txtOnayKodu, 0);

							ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if (Dialog_txtOnayKodu.getText().toString().equals("")) {
										AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata1), Toast.LENGTH_SHORT);
									} else if (!Dialog_txtOnayKodu.getText().toString().equals(String.valueOf(OnayKodu))) {
										AkorDefterimSys.ToastMsj(activity, getString(R.string.girilen_onay_kodu_hata2), Toast.LENGTH_SHORT);
									} else {
										ADDialog.cancel();

										if (EmailOnayKoduSayac != null) {
											EmailOnayKoduSayac.cancel();
											EmailOnayKoduSayac = null;
											EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
										}

										ViewDialogContent = inflater.inflate(R.layout.dialog_sifre_sifirla, null);
										ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.sifre_sifirla), ViewDialogContent, getString(R.string.sifirla), getString(R.string.iptal), false);
										ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

										Dialog_lblSifreSifirlaBilgi = ViewDialogContent.findViewById(R.id.Dialog_lblSifreSifirlaBilgi);
										Dialog_lblSifreSifirlaBilgi.setTypeface(YaziFontu);

										Dialog_EtxtYeniSifre = ViewDialogContent.findViewById(R.id.EtxtYeniSifre);
										Dialog_EtxtYeniSifre.setTypeface(YaziFontu);

										Dialog_EtxtYeniSifreTekrar = ViewDialogContent.findViewById(R.id.EtxtYeniSifreTekrar);
										Dialog_EtxtYeniSifreTekrar.setTypeface(YaziFontu);

										ADDialog.show();

										Dialog_EtxtYeniSifre.requestFocus();
										imm.showSoftInput(Dialog_EtxtYeniSifre, 0);

										ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												Dialog_EtxtYeniSifre.setText(Dialog_EtxtYeniSifre.getText().toString().trim());
												Dialog_EtxtYeniSifreTekrar.setText(Dialog_EtxtYeniSifreTekrar.getText().toString().trim());

												String YeniSifre = Dialog_EtxtYeniSifre.getText().toString();
												String YeniSifreTekrar = Dialog_EtxtYeniSifreTekrar.getText().toString();

												if (TextUtils.isEmpty(YeniSifre)) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata1), Toast.LENGTH_SHORT);

												} else if (AkorDefterimSys.EditTextKarakterKontrol(YeniSifre, RES.getInteger(R.integer.ParolaKarakterSayisi_MIN), RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata2, String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

												} else if (!AkorDefterimSys.isValid(YeniSifre, "SadeceSayiKucukHarfBuyukHarf")) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata3), Toast.LENGTH_SHORT);

												} else if (!AkorDefterimSys.isValid(YeniSifre, "Mutlaka_EnAzBir_Sayi_KucukHarf_Icermeli")) {
													Dialog_EtxtYeniSifre.requestFocus();
													Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
													imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata4), Toast.LENGTH_SHORT);

												} else if (!AkorDefterimSys.EditTextisMatching(Dialog_EtxtYeniSifre, Dialog_EtxtYeniSifreTekrar)) {
													Dialog_EtxtYeniSifreTekrar.requestFocus();
													Dialog_EtxtYeniSifreTekrar.setSelection(YeniSifreTekrar.length());
													imm.showSoftInput(Dialog_EtxtYeniSifreTekrar, 0);
													AkorDefterimSys.ToastMsj(activity, getString(R.string.yenisifre_yenisifretekrar_uyusmuyor), Toast.LENGTH_SHORT);

												} else {
													ADDialog.dismiss();

													imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifre.getWindowToken(), 0);
													imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifreTekrar.getWindowToken(), 0);

													new SifreDegistir().execute(YeniSifre);
												}
											}
										});

										ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												ADDialog.dismiss();
											}
										});
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
										EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
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
										EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
									}

									new SifreSifirlamaOnayKoduYolla().execute(AdSoyad, Email, "EmailYontemi");
								}
							});

							EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
							EmailOnayKoduSayac = new Timer();
							EmailOnayKoduDialogSayac_Ayarla();
							EmailOnayKoduSayac.schedule(EmailOnayKoduDialogSayac, 10, 1000);
						} else if (JSONGelenVeri.getInt("sonuc") == 0) {
							if (JSONGelenVeri.getString("aciklama").equals("adsoyad alanı boş")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("email alanı boş")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("baslik alanı boş")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("icerik alanı boş")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("email gönderilemedi")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.mail_gonderim_hatasi), Toast.LENGTH_SHORT);
							} else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
								//AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
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

	private class SifreDegistir extends AsyncTask<String, String, String> {
		String YeniSifre, YeniSifreSHA1;

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
			YeniSifre = String.valueOf(parametre[0]);
			YeniSifreSHA1 = Strings.getSHA1(YeniSifre);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(BulunanHesapID)));
			nameValuePairs.add(new BasicNameValuePair("yenisifre", YeniSifre));
			nameValuePairs.add(new BasicNameValuePair("yenisifresha1", YeniSifreSHA1));
			String sonuc = null;

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPHesapYeniSifre);
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

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						ADDialog.dismiss();
						AkorDefterimSys.ToastMsj(activity, getString(R.string.parola_degistirildi), Toast.LENGTH_SHORT);
						finish();

						break;
					case 0:
						imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifre.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifreTekrar.getWindowToken(), 0);

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
								CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
							}

							AkorDefterimSys.ToastMsj(activity, getString(R.string.onay_kodu_sure_bitti), Toast.LENGTH_SHORT);
						} else {
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_cep, AkorDefterimSys.ZamanFormatMMSS(CepOnayKoduKalanSure)));
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
								EPostaOnayKoduKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;
							}

							AkorDefterimSys.ToastMsj(activity, getString(R.string.onay_kodu_sure_bitti), Toast.LENGTH_SHORT);
						} else {
							Dialog_lblOnayKoduBilgilendirme.setText(getString(R.string.onay_kodu_email, AkorDefterimSys.ZamanFormatMMSS(EPostaOnayKoduKalanSure)));
							EPostaOnayKoduKalanSure--;
						}
					}
				});
			}
		};
	}

    @SuppressWarnings("unused")
    @SuppressLint("InflateParams")
	private BroadcastReceiver OnayKoduAlici = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String Kimden = intent.getStringExtra("Kimden");
			String OnayKodu = intent.getStringExtra("OnayKodu");
			
			/*int SMSID = 0;
			Cursor cursor = activity.getContentResolver().query(Uri.parse("content://sms/"), null, "address='"+ Kimden + "'", null, null);

			if (cursor.moveToFirst()) {
				SMSID = Integer.parseInt(cursor.getString(0));
			}*/

            if(ADDialog.isShowing()) {
                Dialog_txtOnayKodu.setText(OnayKodu);
                Dialog_txtOnayKodu.requestFocus();
                Dialog_txtOnayKodu.setSelection(Dialog_txtOnayKodu.getText().length());
                imm.hideSoftInputFromWindow(Dialog_txtOnayKodu.getWindowToken(), 0);

                ADDialog.cancel();

                if (CepOnayKoduSayac != null) {
                    CepOnayKoduSayac.cancel();
                    CepOnayKoduSayac = null;
                    CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;
                }

                ViewDialogContent = inflater.inflate(R.layout.dialog_sifre_sifirla, null);
                ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.sifre_sifirla), ViewDialogContent, getString(R.string.sifirla), getString(R.string.iptal), false);
                ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                Dialog_lblSifreSifirlaBilgi = ViewDialogContent.findViewById(R.id.Dialog_lblSifreSifirlaBilgi);
                Dialog_lblSifreSifirlaBilgi.setTypeface(YaziFontu);

                Dialog_EtxtYeniSifre = ViewDialogContent.findViewById(R.id.EtxtYeniSifre);
                Dialog_EtxtYeniSifre.setTypeface(YaziFontu);

                Dialog_EtxtYeniSifreTekrar = ViewDialogContent.findViewById(R.id.EtxtYeniSifreTekrar);
                Dialog_EtxtYeniSifreTekrar.setTypeface(YaziFontu);

                ADDialog.show();

                ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog_EtxtYeniSifre.setText(Dialog_EtxtYeniSifre.getText().toString().trim());
                        Dialog_EtxtYeniSifreTekrar.setText(Dialog_EtxtYeniSifreTekrar.getText().toString().trim());

                        String YeniSifre = Dialog_EtxtYeniSifre.getText().toString();
                        String YeniSifreTekrar = Dialog_EtxtYeniSifreTekrar.getText().toString();

                        if (TextUtils.isEmpty(YeniSifre)) {
                            Dialog_EtxtYeniSifre.requestFocus();
                            Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
                            imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
                            AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata1), Toast.LENGTH_SHORT);

                        } else if (AkorDefterimSys.EditTextKarakterKontrol(YeniSifre, RES.getInteger(R.integer.ParolaKarakterSayisi_MIN), RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
                            Dialog_EtxtYeniSifre.requestFocus();
                            Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
                            imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
                            AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata2, String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

                        } else if (!AkorDefterimSys.isValid(YeniSifre, "SadeceSayiKucukHarfBuyukHarf")) {
                            Dialog_EtxtYeniSifre.requestFocus();
                            Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
                            imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
                            AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata3), Toast.LENGTH_SHORT);

                        } else if (!AkorDefterimSys.isValid(YeniSifre, "Mutlaka_EnAzBir_Sayi_KucukHarf_Icermeli")) {
                            Dialog_EtxtYeniSifre.requestFocus();
                            Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
                            imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
                            AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata4), Toast.LENGTH_SHORT);

                        } else if (!AkorDefterimSys.EditTextisMatching(Dialog_EtxtYeniSifre, Dialog_EtxtYeniSifreTekrar)) {
                            Dialog_EtxtYeniSifreTekrar.requestFocus();
                            Dialog_EtxtYeniSifreTekrar.setSelection(YeniSifreTekrar.length());
                            imm.showSoftInput(Dialog_EtxtYeniSifreTekrar, 0);
                            AkorDefterimSys.ToastMsj(activity, getString(R.string.yenisifre_yenisifretekrar_uyusmuyor), Toast.LENGTH_SHORT);

                        } else {
                            ADDialog.dismiss();

                            imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifre.getWindowToken(), 0);
                            imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifreTekrar.getWindowToken(), 0);

                            new SifreDegistir().execute(Dialog_EtxtYeniSifre.getText().toString());
                        }
                    }
                });

                ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ADDialog.dismiss();
                    }
                });
            }

			//AkorDefterimSys.SmsSil(activity, SMSID);
		}
	};
}