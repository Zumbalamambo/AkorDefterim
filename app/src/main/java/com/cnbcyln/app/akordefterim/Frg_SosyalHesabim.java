package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpUlkeKodlari;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.Siniflar.SnfUlkeKodlari;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.CircleImageView;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Frg_SosyalHesabim extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnClickListener {
	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	AlertDialog ADDialog, ADDialog_HesapDurumu;
	Random rnd;
	Resources RES;
	InputMethodManager imm;
	LayoutInflater inflater;
	View ViewDialogContent;
	ProgressDialog PDDialog, PDBilgiGetir;
	CoordinatorLayout coordinatorLayout;
	SwipeRefreshLayout SRLSosyalHesabim;

	Button btnKaydet;
	EditText txtDogumTarih, txtKullaniciAdi, txtCepTelefon, Dialog_txtOnayKodu;
	ImageView ImgBuyukProfilResim;
	CircleImageView CImgKucukProfilResim, CImgSosyalAgIcon;
	TextView lblHesapBilgileriBaslik, lblAdSoyad, lblAdSoyad2, lblUlkeKodlari, lblEPosta, lblEPosta2, Dialog_lblOnayKoduBilgilendirme;
	TextInputLayout txtILDogumTarih, txtILKullaniciAdi, txtILCepTelefon;
	Spinner spnUlkeKodlari;

	String GeciciKullaniciAdiBilgi = "", GeciciCepTelefonBilgi = "";
	Boolean DogumTarihAlaninaGirildiMi = false;
	int OnayKodu;

	Handler CepOnayKoduHandler = new Handler();
	Timer CepOnayKoduSayac;
	TimerTask CepOnayKoduDialogSayac;
	int CepOnayKoduKalanSure = 0;

	private static final int SMSALMAIZIN = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frg_sosyalhesabim, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = new AkorDefterimSys(activity);
		FragmentDataConn = (Interface_FragmentDataConn) activity;
		RES = activity.getResources();
		rnd = new Random();

		CepOnayKoduKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		inflater = activity.getLayoutInflater();

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.putString("prefSMSOnayKoduSayfaAdi", "Frg_SosyalHesabim");
		sharedPrefEditor.apply();

		activity.registerReceiver(OnayKoduAlici, new IntentFilter("com.cnbcyln.app.akordefterim.Frg_SosyalHesabim"));

		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILDogumTarih.setError(null);
				AkorDefterimSys.UnFocusEditText(txtDogumTarih);
				txtILKullaniciAdi.setError(null);
				AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
				txtILCepTelefon.setError(null);
				AkorDefterimSys.UnFocusEditText(txtCepTelefon);
			}
		});

		SRLSosyalHesabim = activity.findViewById(R.id.SRLSosyalHesabim);
		SRLSosyalHesabim.setOnRefreshListener(this);
		SRLSosyalHesabim.post(new Runnable() {
			@Override
			public void run() {
				BilgiGetir();
			}
		});

		ImgBuyukProfilResim = activity.findViewById(R.id.ImgBuyukProfilResim);
		CImgKucukProfilResim = activity.findViewById(R.id.CImgKucukProfilResim);

		CImgSosyalAgIcon = activity.findViewById(R.id.CImgSosyalAgIcon);
		if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Google"))
			CImgSosyalAgIcon.setImageResource(R.drawable.ic_google_icon);
		else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Facebook"))
			CImgSosyalAgIcon.setImageResource(R.drawable.ic_facebook_icon);

		lblHesapBilgileriBaslik = activity.findViewById(R.id.lblHesapBilgileriBaslik);
		lblHesapBilgileriBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblAdSoyad = activity.findViewById(R.id.lblAdSoyad);
		lblAdSoyad.setTypeface(YaziFontu, Typeface.NORMAL);

		lblAdSoyad2 = activity.findViewById(R.id.lblAdSoyad2);
		lblAdSoyad2.setTypeface(YaziFontu, Typeface.NORMAL);

		txtILDogumTarih = activity.findViewById(R.id.txtILDogumTarih);
		txtILDogumTarih.setTypeface(YaziFontu);

		txtDogumTarih = activity.findViewById(R.id.txtDogumTarih);
		txtDogumTarih.setInputType(InputType.TYPE_NULL);
		txtDogumTarih.setTypeface(YaziFontu, Typeface.NORMAL);
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
		txtDogumTarih.setTypeface(YaziFontu);

		txtILKullaniciAdi = activity.findViewById(R.id.txtILKullaniciAdi);
		txtILKullaniciAdi.setTypeface(YaziFontu);

		txtKullaniciAdi = activity.findViewById(R.id.txtKullaniciAdi);
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

		lblUlkeKodlari = activity.findViewById(R.id.lblUlkeKodlari);
		lblUlkeKodlari.setTypeface(YaziFontu, Typeface.NORMAL);

		spnUlkeKodlari = activity.findViewById(R.id.spnUlkeKodlari);

		txtILCepTelefon = activity.findViewById(R.id.txtILCepTelefon);
		txtILCepTelefon.setTypeface(YaziFontu);

		txtCepTelefon = activity.findViewById(R.id.txtCepTelefon);
		txtCepTelefon.setTypeface(YaziFontu, Typeface.NORMAL);

		lblEPosta = activity.findViewById(R.id.lblEPosta);
		lblEPosta.setTypeface(YaziFontu, Typeface.NORMAL);

		lblEPosta2 = activity.findViewById(R.id.lblEPosta2);
		lblEPosta2.setTypeface(YaziFontu, Typeface.NORMAL);

		btnKaydet = activity.findViewById(R.id.btnKaydet);
		btnKaydet.setTypeface(YaziFontu, Typeface.NORMAL);
		btnKaydet.setOnClickListener(this);

		txtILDogumTarih.setError(null);
		AkorDefterimSys.UnFocusEditText(txtDogumTarih);
		txtILKullaniciAdi.setError(null);
		AkorDefterimSys.UnFocusEditText(txtKullaniciAdi);
		txtILCepTelefon.setError(null);
		AkorDefterimSys.UnFocusEditText(txtCepTelefon);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnKaydet:
				AkorDefterimSys.KlavyeKapat();

				txtDogumTarih.setText(txtDogumTarih.getText().toString().trim());
				txtKullaniciAdi.setText(txtKullaniciAdi.getText().toString().trim());

				String DogumTarih = txtDogumTarih.getText().toString().trim();
				String KullaniciAdi = txtKullaniciAdi.getText().toString().trim();
				String CepTelefon = txtCepTelefon.getText().toString().trim();

				if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
					if(TextUtils.isEmpty(DogumTarih)) {
						txtDogumTarih.requestFocus();
						txtDogumTarih.setSelection(DogumTarih.length());
						imm.showSoftInput(txtDogumTarih, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtdogumtarih_hata1), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(GeciciKullaniciAdiBilgi) && TextUtils.isEmpty(KullaniciAdi)) {
						txtKullaniciAdi.requestFocus();
						txtKullaniciAdi.setSelection(KullaniciAdi.length());
						imm.showSoftInput(txtKullaniciAdi, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata1), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(KullaniciAdi) && !AkorDefterimSys.isValid(KullaniciAdi, "SadeceSayiKucukHarfTurkceKaraktersiz")){
						txtKullaniciAdi.requestFocus();
						txtKullaniciAdi.setSelection(KullaniciAdi.length());
						imm.showSoftInput(txtKullaniciAdi, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata2), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(KullaniciAdi) && AkorDefterimSys.EditTextKarakterKontrol(KullaniciAdi, RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN), RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MAX))) {
						txtKullaniciAdi.requestFocus();
						txtKullaniciAdi.setSelection(KullaniciAdi.length());
						imm.showSoftInput(txtKullaniciAdi, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtkullaniciadi_hata3, String.valueOf(RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.KullaniciAdiKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

					} else if (!TextUtils.isEmpty(GeciciCepTelefonBilgi) && TextUtils.isEmpty(CepTelefon)) {
						txtCepTelefon.requestFocus();
						txtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(txtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata1), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(CepTelefon) && !AkorDefterimSys.isValid(CepTelefon, "SadeceSayi")) {
						txtCepTelefon.requestFocus();
						txtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(txtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata2), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(CepTelefon) && CepTelefon.length() != 10) {
						txtCepTelefon.requestFocus();
						txtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(txtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata3), Toast.LENGTH_SHORT);

					} else if(!TextUtils.isEmpty(CepTelefon) && !CepTelefon.subSequence(0, 1).equals("5")) {
						txtCepTelefon.requestFocus();
						txtCepTelefon.setSelection(CepTelefon.length());
						imm.showSoftInput(txtCepTelefon, 0);
						AkorDefterimSys.ToastMsj(activity, getString(R.string.txtceptelefon_hata3), Toast.LENGTH_SHORT);

					} else {
						/*if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Google"))
							new HesapBilgiGuncelle().execute(sharedPref.getString("prefHesapEmail", ""), DogumTarih, KullaniciAdi, CepTelefon, "Google");
						else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Facebook"))
							new HesapBilgiGuncelle().execute(sharedPref.getString("prefHesapEmail", ""), DogumTarih, KullaniciAdi, CepTelefon, "Facebook");*/

						imm.hideSoftInputFromWindow(txtDogumTarih.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(txtKullaniciAdi.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(txtCepTelefon.getWindowToken(), 0);
					}
				} else {
					imm.hideSoftInputFromWindow(txtDogumTarih.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(txtKullaniciAdi.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(txtCepTelefon.getWindowToken(), 0);

					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
				}

				break;
			/*case R.id.ImgCepTelefonWarningIcon:
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
									new DogrulamaOnayKoduYolla().execute(lblAdSoyad2.getText().toString(), GeciciCepTelefonBilgi);
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
								new DogrulamaOnayKoduYolla().execute(lblAdSoyad2.getText().toString(), GeciciCepTelefonBilgi);
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
					imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);

					AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
				}

				break;*/
		}
	}

	@Override
	public void onRefresh() {
		BilgiGetir();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case SMSALMAIZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.ceptelefon_onayi_baslik),
							getString(R.string.ceptelefon_onayi_icerik, txtCepTelefon.getText()),
							getString(R.string.devam),
							getString(R.string.vazgec));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new DogrulamaOnayKoduYolla().execute(lblAdSoyad2.getText().toString(), GeciciCepTelefonBilgi);
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
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public void AsyncTaskReturnValue(JSONObject JSONSonuc) {
		try {
			switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					SRLSosyalHesabim.setRefreshing(false);

					if(JSONSonuc.getBoolean("Sonuc") ) { // Eğer hesap bulundu ise
						if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							// PDParolamiUnuttum Progress Dialog'u kapattık
							AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

							ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
									getString(R.string.hesap_durumu),
									getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
									activity.getString(R.string.tamam));
							ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_HesapDurumu.show();

							ADDialog_HesapDurumu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog_HesapDurumu.dismiss();

									try {
										FragmentDataConn.HesapCikisYap(new JSONObject("{\"Islem\":\"GoogleHesapCikis\"}"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
						} else {
							String AdSoyad = JSONSonuc.getString("AdSoyad");
							String ResimURL = JSONSonuc.getString("ResimURL");
							String DogumTarih = JSONSonuc.getString("DogumTarih");
							String EPosta = JSONSonuc.getString("EPosta");
							String KullaniciAdi = JSONSonuc.getString("KullaniciAdi");
							String TelKodu = JSONSonuc.getString("TelKodu");
							String CepTelefon = JSONSonuc.getString("CepTelefon");
							String CepTelefonOnay = JSONSonuc.getString("CepTelefonOnay");

							if(ResimURL.equals("")) {
								ImgBuyukProfilResim.setImageResource(R.drawable.bos_profil);
								CImgKucukProfilResim.setImageResource(R.drawable.bos_profil);
							} else {
								new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(ResimURL);
								new AkorDefterimSys.NettenResimYukle(CImgKucukProfilResim).execute(ResimURL);
							}

							lblAdSoyad2.setText(AdSoyad);

							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
								Date inputDate = format.parse(DogumTarih);
								format = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
								txtDogumTarih.setText(format.format(inputDate));
							} catch (ParseException e) {
								e.printStackTrace();
							}

							if(KullaniciAdi.equals("")) {
								txtKullaniciAdi.setText("");
								GeciciKullaniciAdiBilgi = "";
							} else {
								txtKullaniciAdi.setText(KullaniciAdi);
								GeciciKullaniciAdiBilgi = KullaniciAdi;
							}

							List<SnfUlkeKodlari> snfUlkeKodlari = AkorDefterimSys.UlkeKodlariniGetir();

							for(SnfUlkeKodlari tel : snfUlkeKodlari){
								if(tel.getUlkeKodu().equals(TelKodu)) {
									spnUlkeKodlari.setSelection(snfUlkeKodlari.indexOf(tel));
									break;
								}
							}

							if(CepTelefon.equals("")) {
								txtCepTelefon.setText("");
								GeciciCepTelefonBilgi = "";
							} else if(CepTelefonOnay.equals("0")) {
								txtCepTelefon.setText(CepTelefon);
								GeciciCepTelefonBilgi = CepTelefon;
							} else {
								txtCepTelefon.setText(CepTelefon);
								GeciciCepTelefonBilgi = CepTelefon;
							}

							lblEPosta2.setText(EPosta);
						}
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.hesap_bilgileri_bulunamadi));

					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void BilgiGetir() {
		if(AkorDefterimSys.InternetErisimKontrolu()) {
			AdpUlkeKodlari adpUlkeKodlari = new AdpUlkeKodlari(activity, AkorDefterimSys.UlkeKodlariniGetir());
			spnUlkeKodlari.setAdapter(adpUlkeKodlari);

			SRLSosyalHesabim.setRefreshing(true);

			AkorDefterimSys.HesapBilgiGetir(activity.getFragmentManager().findFragmentByTag("Frg_SosyalHesabim"), "", sharedPref.getString("prefEPostaKullaniciAdiTelefon", ""));
		} else {
			SRLSosyalHesabim.setRefreshing(false);

			Snackbar snackbar = AkorDefterimSys.SnackBar(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi),
					R.color.Beyaz,
					R.color.TuruncuYazi,
					R.integer.SnackBar_GurunumSuresi_10);

			snackbar.setAction(getString(R.string.yeniden_dene), new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					SRLSosyalHesabim.setRefreshing(true);
					BilgiGetir();
				}
			});

			snackbar.show();
		}
	}


	/*@SuppressWarnings("deprecation")
	private class HesapBilgiGuncelle extends AsyncTask<String, String, String> {
		String Email, DogumTarih, KullaniciAdi, CepTelefon, SosyalAg;

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
			Email = String.valueOf(parametre[0]);
			DogumTarih = String.valueOf(parametre[1]);
			KullaniciAdi = String.valueOf(parametre[2]);
			CepTelefon = String.valueOf(parametre[3]);
			SosyalAg = String.valueOf(parametre[4]);

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

			try{
				for (int i = 0; i < Islemler.size(); i++) {
					if(Islemler.get(i).equals("KullaniciAdiSorgula")) {
						nameValuePairs.add(new BasicNameValuePair("kullaniciadi", KullaniciAdi));
						httpPost = new HttpPost(AkorDefterimSys.PHPKullaniciAdiKontrol);
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
					nameValuePairs.add(new BasicNameValuePair("email", Email));
					nameValuePairs.add(new BasicNameValuePair("dogumtarih", DogumTarih));

					if(!GeciciKullaniciAdiBilgi.equals(KullaniciAdi)) nameValuePairs.add(new BasicNameValuePair("kullaniciadi", KullaniciAdi));

					if(!GeciciCepTelefonBilgi.equals(CepTelefon)) {
						nameValuePairs.add(new BasicNameValuePair("ceptelefon", CepTelefon));
						nameValuePairs.add(new BasicNameValuePair("ceptelefononay", "0"));
					}

					httpClient = new DefaultHttpClient();

					switch (SosyalAg) {
						case "Google":
							httpPost = new HttpPost(AkorDefterimSys.PHPGoogleHesapGuncelle);
							break;
						case "Facebook":
							httpPost = new HttpPost(AkorDefterimSys.PHPFacebookHesapGuncelle);
							break;
					}

					assert httpPost != null;
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
						new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmail", ""), SosyalAg);

						imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);

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

						} else if(JSONGelenVeri.getString("aciklama").equals("güncelleme işlemi başarısız")) {
							imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);

							AkorDefterimSys.ToastMsj(activity, getString(R.string.hesap_bilgileri_guncellenemedi), Toast.LENGTH_SHORT);

						} else if(JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
							imm.hideSoftInputFromWindow(EtxtDogumTarih.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtKullaniciAdi.getWindowToken(), 0);
							imm.hideSoftInputFromWindow(EtxtCepTelefon.getWindowToken(), 0);

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
	}*/

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

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private class DogrulamaOnayKoduYolla extends AsyncTask<String, String, String> {
		String AdSoyad, Telefon;

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
			Telefon = String.valueOf(parametre[1]);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			String sonuc = null;

			try {
				nameValuePairs.add(new BasicNameValuePair("cepno", Telefon));
				nameValuePairs.add(new BasicNameValuePair("mesaj", getString(R.string.sms_onay_kodu_icerik1, AdSoyad, String.valueOf(OnayKodu))));

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPSMSYolla);

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

								if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Google"))
									new OnayVer().execute(sharedPref.getString("prefHesapEmail", ""), "Google");
								else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Facebook"))
									new OnayVer().execute(sharedPref.getString("prefHesapEmail", ""), "Facebook");
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

									txtCepTelefon.setText(GeciciCepTelefonBilgi);

									new DogrulamaOnayKoduYolla().execute(lblAdSoyad2.getText().toString());
								} else {
									imm.hideSoftInputFromWindow(txtDogumTarih.getWindowToken(), 0);
									imm.hideSoftInputFromWindow(txtKullaniciAdi.getWindowToken(), 0);
									imm.hideSoftInputFromWindow(txtCepTelefon.getWindowToken(), 0);

									AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
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

	@SuppressWarnings("deprecation")
	private class OnayVer extends AsyncTask<String, String, String> {
		String Email, SosyalAg;

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
			Email = String.valueOf(parametre[0]);
			SosyalAg = String.valueOf(parametre[1]);

			String sonuc = "";

			try{
				List<NameValuePair> nameValuePairs = new ArrayList<>();
				nameValuePairs.add(new BasicNameValuePair("email", Email));

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = null;

				switch (SosyalAg) {
					case "Google":
						httpPost = new HttpPost(AkorDefterimSys.PHPGoogleCepTelOnayIslemi);
						break;
					case "Facebook":
						httpPost = new HttpPost(AkorDefterimSys.PHPFacebookCepTelOnayIslemi);
						break;
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
						switch (SosyalAg) {
							case "Google":
								//new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmail", ""), "Google");
								break;
							case "Facebook":
								//new HesapBilgiGetir().execute(sharedPref.getString("prefHesapEmail", ""), "Facebook");
								break;
						}

						AkorDefterimSys.ToastMsj(activity, getString(R.string.ceptelefon_onay_verildi), Toast.LENGTH_SHORT);

						break;
					case 0:
						AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

						break;
				}

				imm.hideSoftInputFromWindow(txtDogumTarih.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(txtKullaniciAdi.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(txtCepTelefon.getWindowToken(), 0);
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

					if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Google"))
						new OnayVer().execute(sharedPref.getString("prefHesapEmail", ""), "Google");
					else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Facebook"))
						new OnayVer().execute(sharedPref.getString("prefHesapEmail", ""), "Facebook");
				}
			}

			//AkorDefterimSys.SmsSil(activity, SMSID);
		}
	};
}