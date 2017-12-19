package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ConstantConditions")
public class SplashEkran extends Activity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	Animation anim;
	ProgressDialog PDSistemKontrol, PDGuncellemeKontrol, PDGirisYap;
	AlertDialog ADDialog_PlayGoogleServisi, ADDialog_SistemDurum, ADDialog_Guncelleme, ADDialog_InternetErisimSorunu, ADDialog_HesapDurumu;
	Intent mIntent;

	//private GoogleApiClient mGoogleLoginApiClient;

	//CallbackManager mFacebookCallbackManager;

	RelativeLayout RLBG;
	TextView lblVersiyonNo;

	String FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon;

	@SuppressLint("HardwareIds")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//FacebookSdk.sdkInitialize(getApplicationContext());
		//mFacebookCallbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.activity_splash_ekran);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		FirebaseToken = FirebaseInstanceId.getInstance().getToken();
		OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);

		try {
			UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		lblVersiyonNo = findViewById(R.id.lblVersiyonNo);
		lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
		lblVersiyonNo.setText(String.valueOf("v").concat(UygulamaVersiyon));

		anim = AnimationUtils.loadAnimation(this, R.anim.anim_giris_bg_alpha);
		anim.reset();

		RLBG = findViewById(R.id.RLBG);
		RLBG.clearAnimation();
		RLBG.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
		anim.reset();

        //FacebookLoginInit();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (AkorDefterimSys.checkPlayServices(activity)) {
			//GoogleAPIInit();

			if (AkorDefterimSys.InternetErisimKontrolu()) { // İnternet bağlantısı var ise
				if(!AkorDefterimSys.ProgressDialogisShowing(PDSistemKontrol)) {
					PDSistemKontrol = AkorDefterimSys.CustomProgressDialog(getString(R.string.lutfen_bekleyiniz), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDSistemKontrol_Timeout");
					PDSistemKontrol.show();
				}

				AkorDefterimSys.SistemDurumKontrol();
			} else { // İnternet bağlantısı yok ise
				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_InternetErisimSorunu)) {
					ADDialog_InternetErisimSorunu = AkorDefterimSys.VButtonCustomAlertDialog(activity,
							getString(R.string.internet_baglantisi),
							getString(R.string.internet_baglantisi_saglanamadi),
							activity.getString(R.string.cevrimdisi_devam_et),
							"ADDialog_InternetErisimSorunu_Cevrimdisi_Giris_Yap",
							activity.getString(R.string.uygulamadan_cik),
							"ADDialog_InternetErisimSorunu_Uygulamadan_Cik");
					ADDialog_InternetErisimSorunu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog_InternetErisimSorunu.show();
				}
			}
		} else {
			if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_PlayGoogleServisi)) {
				ADDialog_PlayGoogleServisi = AkorDefterimSys.CustomAlertDialog(activity,
						getString(R.string.google_play_servis_baslik),
						getString(R.string.google_play_servis_hata1),
						activity.getString(R.string.tamam),
						"ADDialog_PlayGoogleServisi_Tamam");
				ADDialog_PlayGoogleServisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog_PlayGoogleServisi.show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		AkorDefterimSys.DismissProgressDialog(PDSistemKontrol);
		AkorDefterimSys.DismissProgressDialog(PDGuncellemeKontrol);
		AkorDefterimSys.DismissProgressDialog(PDGirisYap);
		AkorDefterimSys.DismissAlertDialog(ADDialog_PlayGoogleServisi);
		AkorDefterimSys.DismissAlertDialog(ADDialog_SistemDurum);
		AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);
		AkorDefterimSys.DismissAlertDialog(ADDialog_InternetErisimSorunu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
	}

	@Override
	public void AsyncTaskReturnValue(String Sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(Sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "SistemDurumKontrol":
					// PDSistemKontrol Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDSistemKontrol);

					if(JSONSonuc.getBoolean("Durum")) { // Sistem çalışıyor ise
						if(!AkorDefterimSys.ProgressDialogisShowing(PDGuncellemeKontrol)) {
							PDGuncellemeKontrol = AkorDefterimSys.CustomProgressDialog(getString(R.string.guncellemeler_denetleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDGuncellemeKontrol_Timeout");
							PDGuncellemeKontrol.show();
						}

						AkorDefterimSys.YeniGuncellemeKontrol(); // Yeni güncelleme kontrolü yap
					} else { // Sistem kapalı ise
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_SistemDurum)) {
							ADDialog_SistemDurum = AkorDefterimSys.CustomAlertDialog(activity,
									JSONSonuc.getString("Baslik"),
									JSONSonuc.getString("Icerik"),
									activity.getString(R.string.tamam),
									"ADDialog_SistemDurum_Tamam");
							ADDialog_SistemDurum.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_SistemDurum.show();
						}
					}

					break;
				case "Guncelleme":
					// PDGuncellemeKontrol Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDGuncellemeKontrol);

					// Durum => false (Eğer güncelleme yok ise false dönüyor)
					if(JSONSonuc.getBoolean("Durum")) { // Güncelleme var ise
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_SistemDurum)) {
							ADDialog_Guncelleme = AkorDefterimSys.VButtonCustomAlertDialog(activity,
									activity.getString(R.string.yeni_guncelleme),
									activity.getString(R.string.yeni_guncelleme_icerik, JSONSonuc.getString("GecerliVersiyonAdi"), JSONSonuc.getString("YeniVersiyonAdi")),
									activity.getString(R.string.cevrimdisi_devam_et),
									"ADDialog_Guncelleme_Cevrimdisi_Devam_Et",
									activity.getString(R.string.guncelle),
									"ADDialog_Guncelleme_Guncelle",
									activity.getString(R.string.uygulamadan_cik),
									"ADDialog_Guncelleme_Uygulamadan_Cik");
							ADDialog_Guncelleme.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_Guncelleme.show();
						}
					} else { // Güncelleme yok ise
						if (AkorDefterimSys.GirisYapildiMi()) {
							if(!AkorDefterimSys.ProgressDialogisShowing(PDGirisYap)) {
								PDGirisYap = AkorDefterimSys.CustomProgressDialog(getString(R.string.giris_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDGirisYap_Timeout");
								PDGirisYap.show();
							}

							AkorDefterimSys.HesapGirisYap("Normal", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, sharedPref.getString("prefEPosta", ""), sharedPref.getString("prefParolaSHA1", ""),"", "");
						} else {
							AkorDefterimSys.HesapPrefSifirla();

							AkorDefterimSys.EkranGetir(new Intent(activity, Giris.class), "Normal");
							finishAffinity();
						}
					}

					break;
				case "HesapGirisYap":
					// PDGirisYap Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDGirisYap);

					if(JSONSonuc.getBoolean("Sonuc")) {
						AkorDefterimSys.HesapPrefSifirla();

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefHesapID", JSONSonuc.getString("HesapID"));
						sharedPrefEditor.putString("prefEPosta", JSONSonuc.getString("HesapEPosta"));
						sharedPrefEditor.putString("prefParolaSHA1", JSONSonuc.getString("HesapParolaSHA1"));
						sharedPrefEditor.putString("prefOturumTipi", "Cevrimici");
                        sharedPrefEditor.apply();

						Intent mIntent = new Intent(activity, AnaEkran.class);
						mIntent.putExtra("Islem", "");

						AkorDefterimSys.EkranGetir(mIntent, "Normal");

						finishAffinity();
					} else {
						AkorDefterimSys.HesapPrefSifirla();

						switch (JSONSonuc.getString("HesapDurum")) {
							case "Ban":
                                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
									ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
											getString(R.string.hesap_durumu),
											getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
											activity.getString(R.string.tamam),
											"ADDialog_HesapDurumu_Tamam");
									ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
									ADDialog_HesapDurumu.show();
								}

								break;
							default:
								AkorDefterimSys.EkranGetir(new Intent(activity, Giris.class), "Normal");
								finishAffinity();
								break;
						}
					}

					break;
				case "ADDialog_InternetErisimSorunu_Cevrimdisi_Giris_Yap":
					AkorDefterimSys.DismissAlertDialog(ADDialog_InternetErisimSorunu);

					AkorDefterimSys.HesapPrefSifirla();

					sharedPrefEditor = sharedPref.edit();
					sharedPrefEditor.putString("prefOturumTipi", "Cevrimdisi");
					sharedPrefEditor.apply();

					mIntent = new Intent(activity, AnaEkran.class);
					mIntent.putExtra("Islem", "");

					AkorDefterimSys.EkranGetir(mIntent, "Normal");

					finishAffinity();
					break;
				case "ADDialog_InternetErisimSorunu_Uygulamadan_Cik":
					AkorDefterimSys.DismissAlertDialog(ADDialog_InternetErisimSorunu);
					Process.killProcess(Process.myPid());
					break;
				case "ADDialog_PlayGoogleServisi_Tamam":
					AkorDefterimSys.DismissAlertDialog(ADDialog_PlayGoogleServisi);
					Process.killProcess(Process.myPid());
					break;
				case "ADDialog_SistemDurum_Tamam":
					AkorDefterimSys.DismissAlertDialog(ADDialog_SistemDurum);
					Process.killProcess(Process.myPid());
					break;
				case "ADDialog_Guncelleme_Cevrimdisi_Devam_Et":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);

					AkorDefterimSys.HesapPrefSifirla();

					sharedPrefEditor = sharedPref.edit();
					sharedPrefEditor.putString("prefOturumTipi", "Cevrimdisi");
					sharedPrefEditor.apply();

					mIntent = new Intent(activity, AnaEkran.class);
					mIntent.putExtra("Islem", "");

					AkorDefterimSys.EkranGetir(mIntent, "Normal");

					finishAffinity();
					break;
				case "ADDialog_Guncelleme_Guncelle":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);

					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
					Process.killProcess(Process.myPid());
					break;
				case "ADDialog_Guncelleme_Uygulamadan_Cik":
					AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);

					Process.killProcess(Process.myPid());
					break;
				case "ADDialog_HesapDurumu_Tamam":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

					AkorDefterimSys.EkranGetir(new Intent(activity, Giris.class), "Normal");
					finishAffinity();
					break;
				case "PDSistemKontrol_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDSistemKontrol);

					AkorDefterimSys.EkranGetir(new Intent(activity, Giris.class), "Normal");
					finishAffinity();
					break;
				case "PDGuncellemeKontrol_Timeout":
					AkorDefterimSys.DismissAlertDialog(PDGuncellemeKontrol);

					AkorDefterimSys.EkranGetir(new Intent(activity, Giris.class), "Normal");
					finishAffinity();
					break;
				case "PDGirisYap_Timeout":
					AkorDefterimSys.DismissAlertDialog(PDGirisYap);

					AkorDefterimSys.EkranGetir(new Intent(activity, Giris.class), "Normal");
					finishAffinity();
					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}