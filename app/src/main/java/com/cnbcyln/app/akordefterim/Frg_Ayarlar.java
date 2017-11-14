package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ConstantConditions")
@SuppressLint({ "HandlerLeak", "DefaultLocale", "CommitPrefEdits" })
public class Frg_Ayarlar extends Fragment {
	
	Activity activity;
	AkorDefterimSys AkorDefterimSys;
	Veritabani Veritabani;
	InputMethodManager imm;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	AlertDialog ADDialog, ADDialog_GucTasarrufu, ADDialog_InternetBaglantisi;
	ProgressDialog PDDialog;
	LayoutInflater inflater;
	View ViewDialogContent;

	LLTransparanPanel transparanPanel3;
	LinearLayout LLWebYonetimSayfasi_PortNo,
			LLWebYonetimSayfasi_BaslangictaCalistir,
			LLEkranIsigi,
			LLIstekAyarlari_KonumBilgisi,
			LLIstekAyarlari_CanliIstek,
			LLIstekAyarlari_ClientIstekSuresi,
			LLYerelRepertuvarIslemleri_RepertuvarYedekle,
			LLYerelRepertuvarIslemleri_RepertuvarGeriYukle,
			LLYerelRepertuvarIslemleri_RepertuvarSifirla,
			LLHakkinda_SurumHakkinda;
	TextView lblWebYonetimSayfasiBaslik,
			lblWebYonetimSayfasi_WifiBilgi,
			lblAndroidIPConnAdres,
			lblWifiAdi,
			lblWebYonetimSayfasi_PortNo,
			lblWebYonetimSayfasi_PortNo_Aciklama,
			lblWebYonetimSayfasi_BaslangictaCalistir,
			lblWebYonetimSayfasi_BaslangictaCalistir_Aciklama,
			lblEkranIsigiAyarlariBaslik,
			lblEkranIsigi_Aydinligi,
			lblIstekAyarlari_Baslik,
			lblIstekAyarlari_Aciklama,
			lblIstekAyarlari_KonumBilgisi,
			lblIstekAyarlari_KonumBilgisi_Aciklama,
			lblIstekAyarlari_CanliIstek,
			lblIstekAyarlari_CanliIstek_Aciklama,
			lblIstekAyarlari_ClientIstekSuresi,
			lblIstekAyarlari_ClientIstekSuresi_Aciklama,
			lblYerelRepertuvarIslemleriBaslik,
			lblYerelRepertuvarIslemleri_RepertuvarYedekle,
			lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama,
			lblYerelRepertuvarIslemleri_RepertuvarGeriYukle,
			lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama,
			lblYerelRepertuvarIslemleri_RepertuvarSifirla,
			lblYerelRepertuvarIslemleri_RepertuvarSifirla_Aciklama,
			lblHakkindaBaslik,
			lblHakkinda_SurumHakkinda,
			lblHakkinda_SurumHakkinda_Aciklama;
	CheckBox chkWebYonetimSayfasi_BaslangictaCalistir;
	SeekBar SBEkranIsigi;
	Switch SwitchWebYonetim, SwitchKonum, SwitchCanliIstek;

	String Versiyon;

	private static final int KONUM_IZIN = 1;
	private static final int DOSYAOKUMAYAZMA_IZIN = 2;
	private Boolean KonumIzniRedMi = false;
	private Boolean DosyaYazmaIzniRedMi = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_frg_ayarlar, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		activity = getActivity();
		AkorDefterimSys = new AkorDefterimSys(activity);
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Interface_FragmentDataConn) activity;
		inflater = activity.getLayoutInflater();

		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı
		
		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);
		
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		try {
			Versiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		// ##### WEB YÖNETİM SAYFASI #####
		lblWebYonetimSayfasiBaslik = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasiBaslik);
		lblWebYonetimSayfasiBaslik.setTypeface(YaziFontu);

		lblWebYonetimSayfasiBaslik = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasiBaslik);
		lblWebYonetimSayfasiBaslik.setTypeface(YaziFontu);

		lblWebYonetimSayfasi_WifiBilgi = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasi_WifiBilgi);
		lblWebYonetimSayfasi_WifiBilgi.setTypeface(YaziFontu);
		lblWebYonetimSayfasi_WifiBilgi.setText(R.string.wifi_bilgilendirme_yazisi);

		lblAndroidIPConnAdres = (TextView) activity.findViewById(R.id.lblAndroidIPConnAdres);
		lblAndroidIPConnAdres.setTypeface(YaziFontu);

		lblWifiAdi = (TextView) activity.findViewById(R.id.lblWifiAdi);
		lblWifiAdi.setTypeface(YaziFontu);

		SwitchWebYonetim = (Switch) activity.findViewById(R.id.SwitchWebYonetim);
		//SwitchWebYonetim.setTypeface(YaziFontu);
		SwitchWebYonetim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ADDialog_GucTasarrufu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.guc_tasarrufu),
						getString(R.string.guc_tasarruf_uyarisi_icerik),
						getString(R.string.tamam));
				ADDialog_GucTasarrufu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

				if (AkorDefterimSys.GucTasarrufKontrolu()) { // Güç tasarrufu açık ise
					if(ADDialog_GucTasarrufu != null && !ADDialog_GucTasarrufu.isShowing()) {
						ADDialog_GucTasarrufu.show();

						ADDialog_GucTasarrufu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_GucTasarrufu.cancel();
							}
						});
					}

					FragmentDataConn.WebYonetimServisiDurumGuncelle(false);
					SwitchWebYonetim.setChecked(false);
				} else if(!AkorDefterimSys.WifiErisimKontrolu()) { // Wifi erişimi yok ise
					FragmentDataConn.YayinAraci_KonumBilgileriThread(false);
					SwitchKonum.setChecked(false);
				} else {
					FragmentDataConn.WebYonetimServisiDurumGuncelle(isChecked);
					SwitchWebYonetim.setChecked(isChecked);
				}
			}
		});

		// Port No
		LLWebYonetimSayfasi_PortNo = (LinearLayout) activity.findViewById(R.id.LLWebYonetimSayfasi_PortNo);
		LLWebYonetimSayfasi_PortNo.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InflateParams")
			@Override
			public void onClick(View v) {
				ViewDialogContent = inflater.inflate(R.layout.dialog_integer_sayi, null);
				ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_yonetim_servisi_portno), ViewDialogContent, getString(R.string.uygula), getString(R.string.vazgec), false);
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

				TextView Dialog_lblWebYonetimPortNo = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblIntegerSayi_Icerik);
				Dialog_lblWebYonetimPortNo.setTypeface(YaziFontu);
				Dialog_lblWebYonetimPortNo.setText(getString(R.string.web_yonetim_servisi_portno_aciklama));

				final EditText Dialog_EtxtPortNo = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtIntegerSayi);
				Dialog_EtxtPortNo.setTypeface(YaziFontu);
				Dialog_EtxtPortNo.setText(sharedPref.getString("prefWebYonetimPortNo", "9658"));
				Dialog_EtxtPortNo.requestFocus();
				Dialog_EtxtPortNo.setSelection(Dialog_EtxtPortNo.length());
				imm.showSoftInput(Dialog_EtxtPortNo, 0);

				ADDialog.show();

				ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Dialog_EtxtPortNo.setText(Dialog_EtxtPortNo.getText().toString().trim());

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefWebYonetimPortNo", Dialog_EtxtPortNo.getText().toString());
						sharedPrefEditor.apply();

						FragmentDataConn.WebYonetimServisiYenidenBaslat();

						ADDialog.dismiss();
					}
				});

				ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog.dismiss();
					}
				});
			}
		});

		lblWebYonetimSayfasi_PortNo = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasi_PortNo);
		lblWebYonetimSayfasi_PortNo.setTypeface(YaziFontu);

		lblWebYonetimSayfasi_PortNo_Aciklama = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasi_PortNo_Aciklama);
		lblWebYonetimSayfasi_PortNo_Aciklama.setTypeface(YaziFontu);

		/** Başlangıçta Çalıştır **/
		LLWebYonetimSayfasi_BaslangictaCalistir = (LinearLayout) activity.findViewById(R.id.LLWebYonetimSayfasi_BaslangictaCalistir);
		LLWebYonetimSayfasi_BaslangictaCalistir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chkWebYonetimSayfasi_BaslangictaCalistir.performClick();
			}
		});

		lblWebYonetimSayfasi_BaslangictaCalistir = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasi_BaslangictaCalistir);
		lblWebYonetimSayfasi_BaslangictaCalistir.setTypeface(YaziFontu);

		lblWebYonetimSayfasi_BaslangictaCalistir_Aciklama = (TextView) activity.findViewById(R.id.lblWebYonetimSayfasi_BaslangictaCalistir_Aciklama);
		lblWebYonetimSayfasi_BaslangictaCalistir_Aciklama.setTypeface(YaziFontu);

		chkWebYonetimSayfasi_BaslangictaCalistir = (CheckBox) activity.findViewById(R.id.chkWebYonetimSayfasi_BaslangictaCalistir);
		chkWebYonetimSayfasi_BaslangictaCalistir.setChecked(sharedPref.getBoolean("prefWebYonetimBaslangictaCalistir", true));
		chkWebYonetimSayfasi_BaslangictaCalistir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				sharedPrefEditor = sharedPref.edit();
				sharedPrefEditor.putBoolean("prefWebYonetimBaslangictaCalistir", isChecked);
				sharedPrefEditor.apply();
			}
		});

		/** ##### EKRAN IŞIĞI AYARLARI ##### **/
		lblEkranIsigiAyarlariBaslik = (TextView) activity.findViewById(R.id.lblEkranIsigiAyarlariBaslik);
		lblEkranIsigiAyarlariBaslik.setTypeface(YaziFontu);

		/** Aydınlık **/
		LLEkranIsigi = (LinearLayout) activity.findViewById(R.id.LLEkranIsigi);

		lblEkranIsigi_Aydinligi = (TextView) activity.findViewById(R.id.lblEkranIsigi_Aydinligi);
		lblEkranIsigi_Aydinligi.setTypeface(YaziFontu);

		SBEkranIsigi = (SeekBar) activity.findViewById(R.id.SBEkranIsigi);
		SBEkranIsigi.setMax(255);
		SBEkranIsigi.setProgress(sharedPref.getInt("prefEkranIsigiAydinligi", 255));
		SBEkranIsigi.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				sharedPrefEditor = sharedPref.edit();
				sharedPrefEditor.putInt("prefEkranIsigiAydinligi", progress);
				sharedPrefEditor.apply();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		/** ##### YAYIN AYARLARI ##### **/
		transparanPanel3 = (LLTransparanPanel) activity.findViewById(R.id.transparanPanel3);
		transparanPanel3.setVisibility(View.VISIBLE);

		lblIstekAyarlari_Baslik = (TextView) activity.findViewById(R.id.lblIstekAyarlari_Baslik);
		lblIstekAyarlari_Baslik.setTypeface(YaziFontu);

		lblIstekAyarlari_Aciklama = (TextView) activity.findViewById(R.id.lblIstekAyarlari_Aciklama);
		lblIstekAyarlari_Aciklama.setTypeface(YaziFontu);
		lblIstekAyarlari_Aciklama.setText(getString(R.string.istek_ayarlari_aciklama, AkorDefterimSys.IstekWebSitesi, AkorDefterimSys.IstekWebSitesi));

		/** Konum Bilgisi **/
		LLIstekAyarlari_KonumBilgisi = (LinearLayout) activity.findViewById(R.id.LLIstekAyarlari_KonumBilgisi);

		lblIstekAyarlari_KonumBilgisi = (TextView) activity.findViewById(R.id.lblIstekAyarlari_KonumBilgisi);
		lblIstekAyarlari_KonumBilgisi.setTypeface(YaziFontu);

		lblIstekAyarlari_KonumBilgisi_Aciklama = (TextView) activity.findViewById(R.id.lblIstekAyarlari_KonumBilgisi_Aciklama);
		lblIstekAyarlari_KonumBilgisi_Aciklama.setTypeface(YaziFontu);

		SwitchKonum = (Switch) activity.findViewById(R.id.SwitchKonum);
		SwitchKonum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
				if (AkorDefterimSys.GucTasarrufKontrolu()) { // Güç tasarrufu açık ise
					ADDialog_GucTasarrufu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.guc_tasarrufu),
							getString(R.string.guc_tasarruf_uyarisi_icerik),
							getString(R.string.tamam));
					ADDialog_GucTasarrufu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog_GucTasarrufu.show();

					ADDialog_GucTasarrufu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							FragmentDataConn.YayinAraci_KonumBilgileriThread(false);
							SwitchKonum.setChecked(false);

							ADDialog_GucTasarrufu.cancel();
						}
					});
				} else if(!AkorDefterimSys.InternetErisimKontrolu()) { // İnternet erişimi yok ise
					FragmentDataConn.YayinAraci_KonumBilgileriThread(false);
					SwitchKonum.setChecked(false);

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
				} else {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						if(!KonumIzniRedMi) {
							if (activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
								requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, KONUM_IZIN);
							else {
								FragmentDataConn.YayinAraci_KonumBilgileriThread(isChecked);

								if(!isChecked && SwitchCanliIstek.isChecked()) {
									FragmentDataConn.YayinAraci_CanliIstek(isChecked);
									SwitchCanliIstek.setChecked(isChecked);
								}
							}
						}

						KonumIzniRedMi = false;
					} else {
						FragmentDataConn.YayinAraci_KonumBilgileriThread(isChecked);

						if(!isChecked && SwitchCanliIstek.isChecked()) {
							FragmentDataConn.YayinAraci_CanliIstek(isChecked);
							SwitchCanliIstek.setChecked(isChecked);
						}
					}
				}
			}
		});

		/** Canlı İstek **/
		LLIstekAyarlari_CanliIstek = (LinearLayout) activity.findViewById(R.id.LLIstekAyarlari_CanliIstek);

		lblIstekAyarlari_CanliIstek = (TextView) activity.findViewById(R.id.lblIstekAyarlari_CanliIstek);
		lblIstekAyarlari_CanliIstek.setTypeface(YaziFontu);

		lblIstekAyarlari_CanliIstek_Aciklama = (TextView) activity.findViewById(R.id.lblIstekAyarlari_CanliIstek_Aciklama);
		lblIstekAyarlari_CanliIstek_Aciklama.setTypeface(YaziFontu);

		SwitchCanliIstek = (Switch) activity.findViewById(R.id.SwitchCanliIstek);
		SwitchCanliIstek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (AkorDefterimSys.GucTasarrufKontrolu()) { // Güç tasarrufu açık ise
					 ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.guc_tasarrufu),
							getString(R.string.guc_tasarruf_uyarisi_icerik),
							getString(R.string.tamam));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							FragmentDataConn.YayinAraci_CanliIstek(false);
							SwitchCanliIstek.setChecked(false);

							ADDialog.cancel();
						}
					});
				} else if(!AkorDefterimSys.InternetErisimKontrolu()) { // İnternet erişimi yok ise
					FragmentDataConn.YayinAraci_CanliIstek(false);
					SwitchCanliIstek.setChecked(false);

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
				} else {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						if(!DosyaYazmaIzniRedMi) {
							if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
								requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOSYAOKUMAYAZMA_IZIN);
							else {
								if(SwitchKonum.isChecked() && isChecked)
									new RepertuvarOnlineListeGuncelle().execute();
								else {
									FragmentDataConn.YayinAraci_CanliIstek(false);
									SwitchCanliIstek.setChecked(false);
								}
							}
						}

						DosyaYazmaIzniRedMi = false;
					} else {
						if(SwitchKonum.isChecked() && isChecked)
							new RepertuvarOnlineListeGuncelle().execute();
						else {
							FragmentDataConn.YayinAraci_CanliIstek(false);
							SwitchCanliIstek.setChecked(false);
						}
					}
				}
			}
		});

		/** Client İstek Aralığı Süresi **/
		LLIstekAyarlari_ClientIstekSuresi = (LinearLayout) activity.findViewById(R.id.LLIstekAyarlari_ClientIstekSuresi);
		LLIstekAyarlari_ClientIstekSuresi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewDialogContent = inflater.inflate(R.layout.dialog_integer_sayi, null);
				ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.istek_ayarlari_client_istek_suresi),
						ViewDialogContent,
						getString(R.string.uygula),
						getString(R.string.vazgec),
						false);
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

				TextView Dialog_lblIntegerSayi_Icerik = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblIntegerSayi_Icerik);
				Dialog_lblIntegerSayi_Icerik.setTypeface(YaziFontu);
				Dialog_lblIntegerSayi_Icerik.setText(getString(R.string.istek_ayarlari_client_istek_suresi_aciklama));

				final EditText Dialog_EtxtIntegerSayi = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtIntegerSayi);
				Dialog_EtxtIntegerSayi.setTypeface(YaziFontu);
				Dialog_EtxtIntegerSayi.setText(String.valueOf(sharedPref.getInt("prefIstekAyarlariClientIstekSuresi", 15)));
				Dialog_EtxtIntegerSayi.requestFocus();
				Dialog_EtxtIntegerSayi.setSelection(Dialog_EtxtIntegerSayi.length());
				imm.showSoftInput(Dialog_EtxtIntegerSayi, 0);

				ADDialog.show();

				ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Dialog_EtxtIntegerSayi.setText(Dialog_EtxtIntegerSayi.getText().toString().trim());

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putInt("prefIstekAyarlariClientIstekSuresi", Integer.parseInt(Dialog_EtxtIntegerSayi.getText().toString()));
						sharedPrefEditor.apply();

						ADDialog.dismiss();
					}
				});

				ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog.dismiss();
					}
				});
			}
		});

		lblIstekAyarlari_ClientIstekSuresi = (TextView) activity.findViewById(R.id.lblIstekAyarlari_ClientIstekSuresi);
		lblIstekAyarlari_ClientIstekSuresi.setTypeface(YaziFontu);

		lblIstekAyarlari_ClientIstekSuresi_Aciklama = (TextView) activity.findViewById(R.id.lblIstekAyarlari_ClientIstekSuresi_Aciklama);
		lblIstekAyarlari_ClientIstekSuresi_Aciklama.setTypeface(YaziFontu);

		/** ##### YEREL REPERTUVAR İŞLEMLERİ ##### **/
		lblYerelRepertuvarIslemleriBaslik = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleriBaslik);
		lblYerelRepertuvarIslemleriBaslik.setTypeface(YaziFontu);

		/** Repertuvar Yedekle **/
		LLYerelRepertuvarIslemleri_RepertuvarYedekle = (LinearLayout) activity.findViewById(R.id.LLYerelRepertuvarIslemleri_RepertuvarYedekle);
		LLYerelRepertuvarIslemleri_RepertuvarYedekle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AkorDefterimSys.InternetErisimKontrolu()) { // İnternet erişimi var ise
					new RepertuvarYedekle().execute();
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

		lblYerelRepertuvarIslemleri_RepertuvarYedekle = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleri_RepertuvarYedekle);
		lblYerelRepertuvarIslemleri_RepertuvarYedekle.setTypeface(YaziFontu);

		lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama);
		lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setTypeface(YaziFontu);

		/** Repertuvar Geri Yükle **/
		LLYerelRepertuvarIslemleri_RepertuvarGeriYukle = (LinearLayout) activity.findViewById(R.id.LLYerelRepertuvarIslemleri_RepertuvarGeriYukle);
		LLYerelRepertuvarIslemleri_RepertuvarGeriYukle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.geri_yukleme),
							getString(R.string.geri_yukleme_mesaji),
							getString(R.string.evet),
							getString(R.string.vazgec));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();
							new RepertuvarGeriYukle().execute();
						}
					});

					ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();
						}
					});
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

		lblYerelRepertuvarIslemleri_RepertuvarGeriYukle = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleri_RepertuvarGeriYukle);
		lblYerelRepertuvarIslemleri_RepertuvarGeriYukle.setTypeface(YaziFontu);

		lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama);
		lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama.setTypeface(YaziFontu);

		/** Repertuvar Sıfırla **/
		LLYerelRepertuvarIslemleri_RepertuvarSifirla = (LinearLayout) activity.findViewById(R.id.LLYerelRepertuvarIslemleri_RepertuvarSifirla);
		LLYerelRepertuvarIslemleri_RepertuvarSifirla.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.repertuvar_sifirlama),
						getString(R.string.repertuvar_sifirlama_mesaji),
						getString(R.string.evet),
						getString(R.string.vazgec));
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog.show();

				ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog.cancel();
						Veritabani.VeritabaniSifirla();

						Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
						Frg_TabRepKontrol.spnListeGetir();

						AkorDefterimSys.ToastMsj(activity, getString(R.string.veritabani_basariyla_sifirlandi), Toast.LENGTH_SHORT);
					}
				});

				ADDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog.cancel();
					}
				});
			}
		});

		lblYerelRepertuvarIslemleri_RepertuvarSifirla = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleri_RepertuvarSifirla);
		lblYerelRepertuvarIslemleri_RepertuvarSifirla.setTypeface(YaziFontu);

		lblYerelRepertuvarIslemleri_RepertuvarSifirla_Aciklama = (TextView) activity.findViewById(R.id.lblYerelRepertuvarIslemleri_RepertuvarSifirla_Aciklama);
		lblYerelRepertuvarIslemleri_RepertuvarSifirla_Aciklama.setTypeface(YaziFontu);

		if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi")) { // Çevrimdışı olarak giriş yapıldıysa..
			LLYerelRepertuvarIslemleri_RepertuvarYedekle.setEnabled(false);
			LLYerelRepertuvarIslemleri_RepertuvarGeriYukle.setEnabled(false);

			lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setText(getString(R.string.bu_ozelligi_cevrimdisi_kullanamazsiniz));
			lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setTextColor(ContextCompat.getColor(activity, R.color.Kirmizi));
			lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama.setText(getString(R.string.bu_ozelligi_cevrimdisi_kullanamazsiniz));
			lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama.setTextColor(ContextCompat.getColor(activity, R.color.Kirmizi));

			/*ViewTreeObserver vto = transparanPanel4.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					transparanPanel4.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					int width  = transparanPanel4.getMeasuredWidth();
					int height = transparanPanel4.getMeasuredHeight();

					// *** dp değerini pixel'e çevirmek için gerekli kod.. ***
					// int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, <HEIGHT>, getResources().getDisplayMetrics());

					ViewGroup.LayoutParams transparanPanel4_Kapat_Params = transparanPanel4.getLayoutParams();

					transparanPanel4_Kapat_Params.height = height;
					transparanPanel4_Kapat.setLayoutParams(transparanPanel4_Kapat_Params);
				}
			});*/
		} else {
			LLYerelRepertuvarIslemleri_RepertuvarYedekle.setEnabled(true);
			LLYerelRepertuvarIslemleri_RepertuvarGeriYukle.setEnabled(true);

			lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setText(getString(R.string.repertuvar_yedekle_aciklama, sharedPref.getString("prefRepertuvarSonYedekTarih", getString(R.string.yok))));
			lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama.setText(getString(R.string.repertuvar_geri_yukle_aciklama, sharedPref.getString("prefRepertuvarSonGeriYuklemeTarih", getString(R.string.yok))));

			if(AkorDefterimSys.InternetErisimKontrolu()) new RepertuvarYedekLastModified().execute();
		}

		/** ##### HAKKINDA ##### **/
		lblHakkindaBaslik = (TextView) activity.findViewById(R.id.lblHakkindaBaslik);
		lblHakkindaBaslik.setTypeface(YaziFontu);

		/** Sürüm Hakkında **/
		lblHakkindaBaslik = (TextView) activity.findViewById(R.id.lblHakkindaBaslik);
		lblHakkindaBaslik.setTypeface(YaziFontu);

		LLHakkinda_SurumHakkinda = (LinearLayout) activity.findViewById(R.id.LLHakkinda_SurumHakkinda);
		LLHakkinda_SurumHakkinda.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.uygulama_hakkinda),
						getString(R.string.uygulama_copyright, getString(R.string.uygulama_adi)) + "\n\n" + getString(R.string.uygulama_telif_hakki_yazisi),
						getString(R.string.kapat));
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog.show();

				ADDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog.cancel();
					}
				});
			}
		});

		lblHakkinda_SurumHakkinda = (TextView) activity.findViewById(R.id.lblHakkinda_SurumHakkinda);
		lblHakkinda_SurumHakkinda.setTypeface(YaziFontu);
		lblHakkinda_SurumHakkinda.setText(getString(R.string.surum_hakkinda, Versiyon));

		lblHakkinda_SurumHakkinda_Aciklama = (TextView) activity.findViewById(R.id.lblHakkinda_SurumHakkinda_Aciklama);
		lblHakkinda_SurumHakkinda_Aciklama.setTypeface(YaziFontu);

		FragmentDataConn.YayinAraciDurumBilgisiAl();
	}

	public Handler WebYonetimDurum1 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			lblAndroidIPConnAdres.setText("http://" + AkorDefterimSys.getIpAddr(activity) + ":" + sharedPref.getString("prefWebYonetimPortNo", "9658"));
			lblWifiAdi.setText(AkorDefterimSys.getWifiAdi(activity));
			SwitchWebYonetim.setChecked(true);
		}
	};

	public Handler WebYonetimDurum2 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			lblAndroidIPConnAdres.setText("http://" + AkorDefterimSys.getIpAddr(activity) + ":" + sharedPref.getString("prefWebYonetimPortNo", "9658") + "\n(" + getString(R.string.web_yonetim_servis_kapali) + ")");
			lblWifiAdi.setText(AkorDefterimSys.getWifiAdi(activity));
			SwitchWebYonetim.setChecked(false);
		}
	};

	public Handler WebYonetimDurum3 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			lblAndroidIPConnAdres.setText(getString(R.string.wifi_baglantisi_bulunamadi));
			lblWifiAdi.setText("---");
			SwitchWebYonetim.setChecked(false);
		}
	};

	public Handler WebYonetimDurum4 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			lblAndroidIPConnAdres.setText(getString(R.string.guc_tasarrufu_aktif));
			lblWifiAdi.setText("---");
			SwitchWebYonetim.setChecked(false);
		}
	};

	private class RepertuvarYedekLastModified extends AsyncTask<String, String, String> {
		long date;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.lutfen_bekleyiniz));
			PDDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			String Sonuc;
			try {
				URL url = new URL(AkorDefterimSys.YedeklemeKlasoruURL + sharedPref.getString("prefHesapID", "0") + ".txt");
				HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
				httpCon.setUseCaches(false);
				httpCon.setDefaultUseCaches(false);

				date = httpCon.getLastModified();

				if (date == 0) Sonuc = "FileNotFoundException";
				else Sonuc = "OK";
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
					final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault());

					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setText(getString(R.string.repertuvar_yedekle_aciklama, format.format(new Date(date))));

							SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefRepertuvarSonYedekTarih", format.format(new Date(date)));
							sharedPrefEditor.commit();
						}
					});

					break;
				case "FileNotFoundException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setText(getString(R.string.repertuvar_yedekle_aciklama, getString(R.string.yok)));
							//AkorDefterimSys.ToastMsj(activity, getString(R.string.geri_yukleme_dosyasi_bulunamadi), Toast.LENGTH_SHORT);
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
				case "Tarih bilgisi bulunamadı":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.tarih_bilgisi_bulunamadi), Toast.LENGTH_SHORT);
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
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	@SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation"})
	private class RepertuvarYedekle extends AsyncTask<Void, Integer, String> {
		long totalSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.yedekleniyor_listeler, 0 + "%"));
			PDDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(Void... parametre) {
			return UploadFile();
		}

		private String UploadFile() {
			JSONObject JSONObYedek = new JSONObject();
			String Sonuc = null;

			try {
				final List<SnfListeler> snfListeler = Veritabani.RepYedekleme_lst_ListeGetir();
				final List<SnfKategoriler> snfKategoriler = Veritabani.RepYedekleme_lst_KategoriGetir();
				final List<SnfTarzlar> snfTarzlar = Veritabani.RepYedekleme_lst_TarzGetir();
				final List<SnfSarkilar> snfSarkilar = Veritabani.RepYedekleme_lst_SarkiGetir();

				if(snfListeler.size() > 0) {
					JSONArray JSONArrListeler = new JSONArray();
					JSONObject JSONObListeler;

					for (int i = 0; i <= snfListeler.size() - 1; i++) {
						JSONObListeler = new JSONObject();
						JSONObListeler.put("id", snfListeler.get(i).getId());
						JSONObListeler.put("ListeAdi", snfListeler.get(i).getListeAdi());
						JSONArrListeler.put(JSONObListeler);

						final int Sira = i;

						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								PDDialog.setMessage(getString(R.string.yedekleniyor_listeler, Math.round((Sira + 1) * 100 / snfListeler.size()) + "%"));
							}
						});
					}

					JSONObYedek.put("Listeler", JSONArrListeler);
				} else {
					JSONArray JSONArrListeler = new JSONArray();
					JSONObject JSONObListeler = new JSONObject();
					JSONArrListeler.put(JSONObListeler);

					JSONObYedek.put("Listeler", JSONArrListeler);
				}

				if(snfKategoriler.size() > 0) {
					JSONArray JSONArrKategoriler = new JSONArray();
					JSONObject JSONObKategoriler;

					for (int i = 0; i <= snfKategoriler.size() - 1; i++) {
						JSONObKategoriler = new JSONObject();
						JSONObKategoriler.put("id", snfKategoriler.get(i).getId());
						JSONObKategoriler.put("KategoriAdi", snfKategoriler.get(i).getKategoriAdi());
						JSONArrKategoriler.put(JSONObKategoriler);

						final int Sira = i;

						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								PDDialog.setMessage(getString(R.string.yedekleniyor_kategoriler, Math.round((Sira + 1) * 100 / snfKategoriler.size()) + "%"));
							}
						});
					}

					JSONObYedek.put("Kategoriler", JSONArrKategoriler);
				} else {
					JSONArray JSONArrKategoriler = new JSONArray();
					JSONObject JSONObKategoriler = new JSONObject();
					JSONArrKategoriler.put(JSONObKategoriler);

					JSONObYedek.put("Kategoriler", JSONArrKategoriler);
				}

				if(snfTarzlar.size() > 0) {
					JSONArray JSONArrTarzlar = new JSONArray();
					JSONObject JSONObTarzlar;

					for (int i = 0; i <= snfTarzlar.size() - 1; i++) {
						JSONObTarzlar = new JSONObject();
						JSONObTarzlar.put("id", snfTarzlar.get(i).getId());
						JSONObTarzlar.put("TarzAdi", snfTarzlar.get(i).getTarzAdi());
						JSONArrTarzlar.put(JSONObTarzlar);

						final int Sira = i;

						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								PDDialog.setMessage(getString(R.string.yedekleniyor_tarzlar, Math.round((Sira + 1) * 100 / snfTarzlar.size()) + "%"));
							}
						});
					}

					JSONObYedek.put("Tarzlar", JSONArrTarzlar);
				} else {
					JSONArray JSONArrTarzlar = new JSONArray();
					JSONObject JSONObTarzlar = new JSONObject();
					JSONArrTarzlar.put(JSONObTarzlar);

					JSONObYedek.put("Tarzlar", JSONArrTarzlar);
				}

				if(snfSarkilar.size() > 0) {
					JSONArray JSONArrSarkilar = new JSONArray();
					JSONObject JSONObSarkilar;

					for (int i = 0; i <= snfSarkilar.size() - 1; i++) {
						JSONObSarkilar = new JSONObject();
						JSONObSarkilar.put("id", snfSarkilar.get(i).getId());
						JSONObSarkilar.put("ListeID", snfSarkilar.get(i).getListeID());
						JSONObSarkilar.put("KategoriID", snfSarkilar.get(i).getKategoriID());
						JSONObSarkilar.put("TarzID", snfSarkilar.get(i).getTarzID());
						JSONObSarkilar.put("SanatciAdi", snfSarkilar.get(i).getSanatciAdi());
						JSONObSarkilar.put("SarkiAdi", snfSarkilar.get(i).getSarkiAdi());
						JSONObSarkilar.put("Icerik", snfSarkilar.get(i).getIcerik());
						JSONObSarkilar.put("EklenmeTarihi", snfSarkilar.get(i).getEklenmeTarihi());
						JSONObSarkilar.put("DuzenlenmeTarihi", snfSarkilar.get(i).getDuzenlenmeTarihi());
						JSONArrSarkilar.put(JSONObSarkilar);

						final int Sira = i;

						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								PDDialog.setMessage(getString(R.string.yedekleniyor_sarkilar, Math.round((Sira + 1) * 100 / snfSarkilar.size()) + "%"));
							}
						});
					}

					JSONObYedek.put("Sarkilar", JSONArrSarkilar);
				} else {
					JSONArray JSONArrSarkilar = new JSONArray();
					JSONObject JSONObSarkilar = new JSONObject();
					JSONArrSarkilar.put(JSONObSarkilar);

					JSONObYedek.put("Sarkilar", JSONArrSarkilar);
				}

				//Calendar c = Calendar.getInstance();
				//SimpleDateFormat format2 = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());

				File YedekDosyasi = new File(AkorDefterimSys.AnaKlasorDizini, sharedPref.getString("prefHesapID", "0") + ".txt");

				if(new File(AkorDefterimSys.AnaKlasorDizini).exists()) { // Cihazda AnaKlasorDizini varmı?
					new File(AkorDefterimSys.AnaKlasorDizini).delete(); // Var ise klasörü siliyoruz
					new File(AkorDefterimSys.AnaKlasorDizini).mkdirs(); // Yeni klasör oluşturuyoruz
				} else new File(AkorDefterimSys.AnaKlasorDizini).mkdirs(); // Yok ise klasörü oluşturuyoruz

				if(YedekDosyasi.exists()) YedekDosyasi.delete();

				AkorDefterimSys.DosyayaYaz(JSONObYedek.toString(), YedekDosyasi);

				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
					@Override
					public void transferred(long num) {
						publishProgress((int) ((num / (float) totalSize) * 100));
					}
				});

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AkorDefterimSys.PHPDosyaYukle);

				// Adding file data to http body
				entity.addPart("dosya", new FileBody(YedekDosyasi));
				entity.addPart("dizin", new StringBody(AkorDefterimSys.YedeklemeKlasoruDizin));

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

				/*HttpURLConnection connection;
				DataOutputStream dataOutputStream;
				String lineEnd = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";

				int bytesRead,bytesAvailable,bufferSize;
				byte[] buffer;
				int maxBufferSize = 100 * 1024 * 1024; //Sunucuya max ne kadarlık boyutta dosya göndereceğimizi belirliyoruz..

				FileInputStream fileInputStream = new FileInputStream(YedekDosyasi);
				URL url = new URL(AkorDefterimSys.PHPDosyaYukle);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);//Allow Inputs
				connection.setDoOutput(true);//Allow Outputs
				connection.setUseCaches(false);//Don't use a cached Copy
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("ENCTYPE", "multipart/form-data");
				connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				connection.setRequestProperty("uploaded_file", YedekDosyasi.toString());

				//creating new dataoutputstream
				dataOutputStream = new DataOutputStream(connection.getOutputStream());

				//writing bytes to data outputstream
				dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
				dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + YedekDosyasi.toString() + "\"" + lineEnd);

				dataOutputStream.writeBytes(lineEnd);

				//returns no. of bytes present in fileInputStream
				bytesAvailable = fileInputStream.available();
				//selecting the buffer size as minimum of available bytes or 1 MB
				bufferSize = Math.min(bytesAvailable,maxBufferSize);
				//setting the buffer as byte array of size of bufferSize
				buffer = new byte[bufferSize];

				//reads bytes from FileInputStream(from 0th index of buffer to buffersize)
				bytesRead = fileInputStream.read(buffer,0,bufferSize);

				//loop repeats till bytesRead = -1, i.e., no bytes are left to read
				while (bytesRead > 0){
					//write the bytes read from inputstream
					dataOutputStream.write(buffer,0,bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable,maxBufferSize);
					bytesRead = fileInputStream.read(buffer,0,bufferSize);
				}

				dataOutputStream.writeBytes(lineEnd);
				dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				//serverResponseCode = connection.getResponseCode();
				Sonuc = connection.getResponseMessage();

				//closing the input and output streams
				fileInputStream.close();
				dataOutputStream.flush();
				dataOutputStream.close();*/

				if(YedekDosyasi.exists()) { //İşlemlerin ardından cihazdaki yedek dosyasını siliyoruz..
					YedekDosyasi.delete();
				}
			} catch (JSONException e) {
				e.printStackTrace();
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
							Calendar c = Calendar.getInstance();
							SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault());

							SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefRepertuvarSonYedekTarih", format.format(c.getTime()));
							sharedPrefEditor.commit();

							//lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setText(getString(R.string.repertuvar_yedekle_aciklama, sharedPref.getString("prefRepertuvarSonYedekTarih", getString(R.string.yok))));
							lblYerelRepertuvarIslemleri_RepertuvarYedekle_Aciklama.setText(getString(R.string.repertuvar_yedekle_aciklama, format.format(c.getTime())));

							AkorDefterimSys.ToastMsj(activity, getString(R.string.repertuvar_yedeklendi), Toast.LENGTH_SHORT);
						}
					});
					break;
				case "FileNotFoundException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.geri_yukleme_dosyasi_bulunamadi), Toast.LENGTH_SHORT);
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
					PDDialog.setMessage(getString(R.string.yedek_dosyasi_yukleniyor, String.valueOf(Deger[0]) + "%"));
				}
			});
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private class RepertuvarGeriYukle extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.yedek_dosyasi_indiriliyor));
			PDDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			int count;
			String Sonuc = null;

			try {
				File YedekDosyasi = new File(AkorDefterimSys.AnaKlasorDizini, sharedPref.getString("prefHesapID", "0") + ".txt");

				if(new File(AkorDefterimSys.AnaKlasorDizini).exists()) {
					new File(AkorDefterimSys.AnaKlasorDizini).delete();
					new File(AkorDefterimSys.AnaKlasorDizini).mkdirs();
				} else new File(AkorDefterimSys.AnaKlasorDizini).mkdirs();

				if(YedekDosyasi.exists()) YedekDosyasi.delete();

				URL url = new URL(AkorDefterimSys.YedeklemeKlasoruURL + sharedPref.getString("prefHesapID", "0") + ".txt");
				URLConnection conn = url.openConnection();
				conn.connect();
				// getting file length
				int lenghtOfFile = conn.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(), 8192);

				// Output stream to write file
				OutputStream output = new FileOutputStream(YedekDosyasi);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress(""+(int)((total*100)/lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

				StringBuilder OkunanVeri = new StringBuilder();

				BufferedReader br = new BufferedReader(new FileReader(YedekDosyasi));
				String line;

				while ((line = br.readLine()) != null) {
					OkunanVeri.append(line);
				}

				br.close();

				if(!OkunanVeri.toString().equals("")) {
					JSONObject JSONObYedek = new JSONObject(OkunanVeri.toString());
					final JSONArray JSONArrListeler = JSONObYedek.getJSONArray("Listeler");
					final JSONArray JSONArrKategoriler = JSONObYedek.getJSONArray("Kategoriler");
					final JSONArray JSONArrTarzlar = JSONObYedek.getJSONArray("Tarzlar");
					final JSONArray JSONArrSarkilar = JSONObYedek.getJSONArray("Sarkilar");

					Veritabani.VeritabaniSifirla();

					if(JSONArrListeler.length() > 0) {
						if(!JSONArrListeler.getJSONObject(0).toString().equals("{}")) {
							for (int i = 0; i <= JSONArrListeler.length() - 1; i++) {
								Veritabani.RepGeriYukleme_ListeEkle(JSONArrListeler.getJSONObject(i).getInt("id"), JSONArrListeler.getJSONObject(i).getString("ListeAdi"));

								final int Sira = i;

								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										PDDialog.setMessage(getString(R.string.geri_yukleniyor_listeler, Math.round((Sira + 1) * 100 / JSONArrListeler.length()) + "%"));
									}
								});
							}
						}
					}

					if(JSONArrKategoriler.length() > 0) {
						if(!JSONArrKategoriler.getJSONObject(0).toString().equals("{}")) {
							for (int i = 0; i <= JSONArrKategoriler.length() - 1; i++) {
								Veritabani.RepGeriYukleme_KategoriEkle(JSONArrKategoriler.getJSONObject(i).getInt("id"), JSONArrKategoriler.getJSONObject(i).getString("KategoriAdi"));

								final int Sira = i;

								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										PDDialog.setMessage(getString(R.string.geri_yukleniyor_kategoriler, Math.round((Sira + 1) * 100 / JSONArrKategoriler.length()) + "%"));
									}
								});
							}
						}
					}

					if(JSONArrTarzlar.length() > 0) {
						if(!JSONArrTarzlar.getJSONObject(0).toString().equals("{}")) {
							for (int i = 0; i <= JSONArrTarzlar.length() - 1; i++) {
								Veritabani.RepGeriYukleme_TarzEkle(JSONArrTarzlar.getJSONObject(i).getInt("id"), JSONArrTarzlar.getJSONObject(i).getString("TarzAdi"));

								final int Sira = i;

								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										PDDialog.setMessage(getString(R.string.geri_yukleniyor_tarzlar, Math.round((Sira + 1) * 100 / JSONArrTarzlar.length()) + "%"));
									}
								});
							}
						}
					}

					if(JSONArrSarkilar.length() > 0) {
						if(!JSONArrSarkilar.getJSONObject(0).toString().equals("{}")) {
							for (int i = 0; i <= JSONArrSarkilar.length() - 1; i++) {
								Veritabani.SarkiEkle(JSONArrSarkilar.getJSONObject(i).getInt("id"),
										JSONArrSarkilar.getJSONObject(i).getInt("ListeID"),
										JSONArrSarkilar.getJSONObject(i).getInt("KategoriID"),
										JSONArrSarkilar.getJSONObject(i).getInt("TarzID"),
										JSONArrSarkilar.getJSONObject(i).getString("SarkiAdi"),
										JSONArrSarkilar.getJSONObject(i).getString("SanatciAdi"),
										JSONArrSarkilar.getJSONObject(i).getString("Icerik"),
										JSONArrSarkilar.getJSONObject(i).getString("EklenmeTarihi"),
										JSONArrSarkilar.getJSONObject(i).getString("DuzenlenmeTarihi"));

								final int Sira = i;

								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										PDDialog.setMessage(getString(R.string.geri_yukleniyor_sarkilar, Math.round((Sira + 1) * 100 / JSONArrSarkilar.length()) + "%"));
									}
								});
							}
						}
					}

					if(YedekDosyasi.exists()) { //İşlemlerin ardından cihazdaki yedek dosyasını siliyoruz..
						YedekDosyasi.delete();
					}

					Sonuc = "OK";
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Sonuc = "JSONException";
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
							Calendar c = Calendar.getInstance();
							SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault());

							SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefRepertuvarSonGeriYuklemeTarih", format.format(c.getTime()));
							sharedPrefEditor.commit();

							Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
							Frg_TabRepKontrol.spnListeGetir();

							lblYerelRepertuvarIslemleri_RepertuvarGeriYukle_Aciklama.setText(getString(R.string.repertuvar_geri_yukle_aciklama, sharedPref.getString("prefRepertuvarSonGeriYuklemeTarih", getString(R.string.yok))));
							AkorDefterimSys.ToastMsj(activity, getString(R.string.repertuvar_geri_yuklendi), Toast.LENGTH_SHORT);
						}
					});
					break;
				case "FileNotFoundException":
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							AkorDefterimSys.ToastMsj(activity, getString(R.string.geri_yukleme_dosyasi_bulunamadi), Toast.LENGTH_SHORT);
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
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	@SuppressWarnings({"ResultOfMethodCallIgnored", "deprecation"})
	private class RepertuvarOnlineListeGuncelle extends AsyncTask<Void, Integer, String> {
		long totalSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.repertuvar_listesi_guncelleniyor, 0 + "%"));
			PDDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(Void... parametre) {
			return UploadFile();
		}

		private String UploadFile() {
			String Sonuc = null;

			try {
				JSONObject JSONObYedek = new JSONObject();

				final List<SnfSarkilar> snfSarkilar = Veritabani.RepYedekleme_lst_SarkiGetir();

				if(snfSarkilar.size() > 0) {
					JSONArray JSONArrSarkilar = new JSONArray();
					JSONObject JSONObSarkilar;

					for (int i = 0; i <= snfSarkilar.size() - 1; i++) {
						/*if(!JSONArrSarkilar.toString().contains("\"SarkiAdi\":\"" + snfSarkilar.get(i).getSanatciAdi() + "\"")) {
							JSONObSarkilar = new JSONObject();
							JSONObSarkilar.put("id", snfSarkilar.get(i).getId());
							JSONObSarkilar.put("SanatciAdi", snfSarkilar.get(i).getSanatciAdi());
							JSONObSarkilar.put("SarkiAdi", snfSarkilar.get(i).getSarkiAdi());
							JSONArrSarkilar.put(JSONObSarkilar);
						}*/

						JSONObSarkilar = new JSONObject();
						JSONObSarkilar.put("id", snfSarkilar.get(i).getId());
						JSONObSarkilar.put("SanatciAdi", snfSarkilar.get(i).getSanatciAdi());
						JSONObSarkilar.put("SarkiAdi", snfSarkilar.get(i).getSarkiAdi());
						JSONArrSarkilar.put(JSONObSarkilar);
					}

					JSONObYedek.put("Sarkilar", JSONArrSarkilar);
				} else {
					JSONArray JSONArrSarkilar = new JSONArray();

					JSONObYedek.put("Sarkilar", JSONArrSarkilar);
				}

				if(new File(AkorDefterimSys.AnaKlasorDizini).exists()) { // Cihazda AnaKlasorDizini varmı?
					new File(AkorDefterimSys.AnaKlasorDizini).delete(); // Var ise klasörü siliyoruz
					new File(AkorDefterimSys.AnaKlasorDizini).mkdirs(); // Yeni klasör oluşturuyoruz
				} else new File(AkorDefterimSys.AnaKlasorDizini).mkdirs(); // Yok ise klasörü oluşturuyoruz

				File YedekDosyasi = new File(AkorDefterimSys.AnaKlasorDizini, sharedPref.getString("prefHesapID", "0") + ".txt");

				AkorDefterimSys.DosyayaYaz(JSONObYedek.toString(), YedekDosyasi);

				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
					@Override
					public void transferred(long num) {
						publishProgress((int) ((num / (float) totalSize) * 100));
					}
				});

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(AkorDefterimSys.PHPDosyaYukle);

				// Adding file data to http body
				entity.addPart("dosya", new FileBody(YedekDosyasi));
				entity.addPart("dizin", new StringBody(AkorDefterimSys.OnlineRepertuvarListesiKlasoruDizin));

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

				if(YedekDosyasi.exists()) YedekDosyasi.delete(); //İşlemlerin ardından cihazdaki yedek dosyasını siliyoruz..
			} catch (JSONException e) {
				e.printStackTrace();
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
							Veritabani.IstekTablosunuBosalt();
							FragmentDataConn.YayinAraci_CanliIstek(true);

							Frg_TabIstekler Frg_TabIstekler = (Frg_TabIstekler) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_istekler));
							if(Frg_TabIstekler != null) Frg_TabIstekler.IstekListesiGetir();
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
					PDDialog.setMessage(getString(R.string.repertuvar_listesi_guncelleniyor, String.valueOf(Deger[0]) + "%"));
				}
			});
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			PDDialog.dismiss();
		}
	}

	public void YayinAyarlari_KonumBilgisiSwitchDurum(Boolean Durum) {
		SwitchKonum.setChecked(Durum);
	}

	public void YayinAyarlari_CanliIstekSwitchDurum(Boolean Durum) {
		SwitchCanliIstek.setChecked(Durum);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case KONUM_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					FragmentDataConn.YayinAraci_KonumBilgileriThread(SwitchKonum.isChecked());

					if(!SwitchKonum.isChecked() && SwitchCanliIstek.isChecked()) {
						FragmentDataConn.YayinAraci_CanliIstek(SwitchKonum.isChecked());
						SwitchCanliIstek.setChecked(SwitchKonum.isChecked());
					}
				} else {
					ADDialog = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
							getString(R.string.uygulama_izni),
							getString(R.string.uygulama_izni_konum_hata),
							activity.getString(R.string.tamam));
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							ADDialog.cancel();

							KonumIzniRedMi = true;

							SwitchKonum.setChecked(false);
							SwitchCanliIstek.setChecked(false);

							FragmentDataConn.YayinAraci_KonumBilgileriThread(false);
							FragmentDataConn.YayinAraci_CanliIstek(false);
						}
					});
				}

				break;
			case DOSYAOKUMAYAZMA_IZIN:
				if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if(SwitchKonum.isChecked() && SwitchCanliIstek.isChecked())
						new RepertuvarOnlineListeGuncelle().execute();
					else {
						FragmentDataConn.YayinAraci_CanliIstek(false);
						SwitchCanliIstek.setChecked(false);
					}
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

							DosyaYazmaIzniRedMi = true;
							SwitchCanliIstek.setChecked(false);
							FragmentDataConn.YayinAraci_CanliIstek(false);
						}
					});
				}
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}