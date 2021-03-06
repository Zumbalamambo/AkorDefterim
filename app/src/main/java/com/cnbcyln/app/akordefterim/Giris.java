package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.MqttService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@SuppressWarnings("ALL")
public class Giris extends AppCompatActivity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	CallbackManager mFacebookCallbackManager;
	GoogleSignInClient mGoogleSignInClient;
	VideoView VideoArkaplan;
	ViewPager VPGirisEkranPager;
	AlertDialog ADDialog_PlayGoogleServisi, ADDialog_InternetErisimSorunu, ADDialog_HesapDurumu;
	Intent mIntentMqttService;

	CoordinatorLayout coordinatorLayout;
	RelativeLayout RLLoader;
	CarouselView carouselView;

	String FirebaseToken = "", OSID = "", OSVersiyon = "", UygulamaVersiyon = "";
	boolean CikisIcinCiftTiklandiMi = false;
	int SwipeIndexer = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		mFacebookCallbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.activity_giris_bgekran);

		activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		mIntentMqttService = new Intent(MqttService.class.getName());

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		coordinatorLayout = findViewById(R.id.coordinatorLayout);

		VideoArkaplan = findViewById(R.id.VideoArkaplan);
		VideoArkaplan.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video));
		VideoArkaplan.start();
		VideoArkaplan.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});

		VPGirisEkranPager = findViewById(R.id.VPGirisEkranPager);
		ViewPagerAdapter ViewPagerAdapter = new ViewPagerAdapter();
		VPGirisEkranPager.setAdapter(ViewPagerAdapter);

		RLLoader = findViewById(R.id.RLLoader);
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;

		FirebaseToken = FirebaseInstanceId.getInstance().getToken();
		OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);

		try {
			UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		FacebookLoginInit();
		GoogleAPIInit(false);

		AkorDefterimSys.YeniSurumYeniliklerDialog();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		AkorDefterimSys.DismissAlertDialog(ADDialog_PlayGoogleServisi);
		AkorDefterimSys.DismissAlertDialog(ADDialog_InternetErisimSorunu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
	}

	@Override
	protected void onResume() {
		super.onResume();

		/*if(AkorDefterimSys.prefAction.equals("Giris_Yap")) {
			VPGirisEkranPager.setCurrentItem(1);
			AkorDefterimSys.prefAction = "";
		}*/

		VideoArkaplan.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video));
		VideoArkaplan.start();
		VideoArkaplan.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// Cihazdan fiziksel geri tuşuna basıldığında eğer ana ekranda değilsek ana ekranı getiriyoruz.
		// AnaEkran ekrandaysak uygulamayı kapatıyoruz..
		if(VPGirisEkranPager.getCurrentItem() > 0) {
			VPGirisEkranPager.setCurrentItem(0);
		} else {
			// Çift tıklandığında uygulamadan çıkılması için gerekli fonksiyonu yazdık.
			if (CikisIcinCiftTiklandiMi) {
				Process.killProcess(Process.myPid());
			}

			CikisIcinCiftTiklandiMi = true;
			AkorDefterimSys.ToastMsj(activity, getString(R.string.uygulama_cikis_mesaji), Toast.LENGTH_SHORT);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					CikisIcinCiftTiklandiMi = false;
				}
			}, 2000);
		}

		//super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
			case Activity.RESULT_OK: //Second activity normal olarak sonlanirsa RESULT_OK degeri döner.
				if (requestCode == AkorDefterimSys.RC_GOOGLE_LOGIN) {
					try {
						Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
						GoogleSignInAccount acct = task.getResult(ApiException.class);

						RLLoader.setVisibility(View.GONE);

						if (acct != null) {
							String AdSoyad = acct.getDisplayName();
							//String personGivenName = acct.getGivenName();
							//String personFamilyName = acct.getFamilyName();
							String EPosta = acct.getEmail();
							String GoogleID = acct.getId();
							//Uri personPhoto = acct.getPhotoUrl();
							//String ResimURL = (acct.getPhotoUrl() == null ? "":acct.getPhotoUrl().toString());

							AkorDefterimSys.HesapGirisYap("Google", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, EPosta, "", GoogleID, AdSoyad);
						}
					} catch (ApiException e) {
						e.printStackTrace();
					}
				}

				break;
			case Activity.RESULT_CANCELED: //Second activity beklendmedik sekilde kapanirsa(Mesela cihazdaki back buttonuna tikalnirsa) RESULT_CANCELED degeri doner.
				//Toast.makeText(activity, "Beklenmedik sekilde second activity sonlandi", Toast.LENGTH_SHORT).show();

				RLLoader.setVisibility(View.GONE);

				break;
			default:

				break;
		}
	}

	private class ViewPagerAdapter extends android.support.v4.view.PagerAdapter {
		private LayoutInflater layoutInflater;
		private int[] sayfalar = new int[]{
				R.layout.activity_giris_anaekran,
				R.layout.activity_giris_girisekran};

		private ViewPagerAdapter() {

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(sayfalar[position], container, false);

			switch (position) {
				case 0:
					YaziFontu = AkorDefterimSys.FontGetir(activity, "android");

					TextView lblUygulamaAdi = (TextView) view.findViewById(R.id.lblUygulamaAdi);
					lblUygulamaAdi.setTypeface(YaziFontu, Typeface.BOLD);

					YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

					TextView lblVersiyon = (TextView) view.findViewById(R.id.lblVersiyon);
					lblVersiyon.setTypeface(YaziFontu, Typeface.NORMAL);
					lblVersiyon.setText(AkorDefterimSys.UygulamaVersiyonuGetir());

					carouselView = (CarouselView) view.findViewById(R.id.carouselView);

					if(AkorDefterimSys.InternetErisimKontrolu()) AkorDefterimSys.TanitimMesajlariniGetir();
					else CarouselViewListenerInit(getResources().getStringArray(R.array.OfflineTanitimMesajlari));

					Button btnGirisYap = (Button) view.findViewById(R.id.btnGirisYap);
					btnGirisYap.setTypeface(YaziFontu, Typeface.BOLD);
					btnGirisYap.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							VPGirisEkranPager.setCurrentItem(1);
						}
					});

					Button btnKaydol = (Button) view.findViewById(R.id.btnKaydol);
					btnKaydol.setTypeface(YaziFontu, Typeface.BOLD);
					btnKaydol.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AkorDefterimSys.EkranGetir(new Intent(activity, KayitEkran_EPosta.class), "Normal");
						}
					});

					break;
				case 1:
					YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

					ImageButton btnGeri = (ImageButton) view.findViewById(R.id.btnGeri);
					btnGeri.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							VPGirisEkranPager.setCurrentItem(0);
						}
					});

					TextView lblBaslik = (TextView) view.findViewById(R.id.lblBaslik);
					lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

					TextView lblAciklama = (TextView) view.findViewById(R.id.lblAciklama);
					lblAciklama.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnFacebookLogin = (Button) view.findViewById(R.id.btnFacebookLogin);
					btnFacebookLogin.setTypeface(YaziFontu, Typeface.BOLD);
					btnFacebookLogin.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(AkorDefterimSys.InternetErisimKontrolu()) {
								RLLoader.setVisibility(View.VISIBLE);
								LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
							} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
						}
					});

					Button btnGoogleLogin = (Button) view.findViewById(R.id.btnGoogleLogin);
					btnGoogleLogin.setTypeface(YaziFontu, Typeface.BOLD);
					btnGoogleLogin.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(AkorDefterimSys.InternetErisimKontrolu()) {
								RLLoader.setVisibility(View.VISIBLE);
								Intent signInIntent = mGoogleSignInClient.getSignInIntent();
								startActivityForResult(signInIntent, AkorDefterimSys.RC_GOOGLE_LOGIN);
							} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
						}
					});

					TextView lblVeya = (TextView) view.findViewById(R.id.lblVeya);
					lblVeya.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnNormalLogin = (Button) view.findViewById(R.id.btnNormalLogin);
					btnNormalLogin.setTypeface(YaziFontu, Typeface.BOLD);
					btnNormalLogin.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
                            AkorDefterimSys.EkranGetir(new Intent(activity, Giris_Yap.class), "Normal");
						}
					});

					TextView lblInternetYoksa = (TextView) view.findViewById(R.id.lblInternetYoksa);
					lblInternetYoksa.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnCevrimdisiLogin = (Button) view.findViewById(R.id.btnCevrimdisiLogin);
					btnCevrimdisiLogin.setTypeface(YaziFontu, Typeface.BOLD);
					btnCevrimdisiLogin.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AkorDefterimSys.HesapPrefSifirla();

							Intent mIntent = new Intent(activity, AnaEkran.class);
							mIntent.putExtra("Islem", "");

							AkorDefterimSys.EkranGetir(mIntent, "Normal");

							finishAffinity();
						}
					});

					break;
			}

			container.addView(view);

			return view;
		}

		@Override
		public int getCount() {
			return sayfalar.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}
	}

	public void CarouselViewListenerInit(final String[] TanitimMesajlari) {
		ViewListener CarouselViewListener = new ViewListener() {
			@Override
			public View setViewForPosition(int position) {
				View ViewDialogCustom = getLayoutInflater().inflate(R.layout.tanitim_mesajlari_carousel_layout, null);

				TextView lblBaslik = ViewDialogCustom.findViewById(R.id.lblBaslik);
				lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

				TextView lblIcerik = ViewDialogCustom.findViewById(R.id.lblIcerik);
				lblIcerik.setTypeface(YaziFontu, Typeface.NORMAL);

				try {
					JSONObject JSONGelenVeri = new JSONObject(TanitimMesajlari[position]);

					lblBaslik.setText(JSONGelenVeri.getString("Baslik"));
					lblIcerik.setText(JSONGelenVeri.getString("Icerik"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return ViewDialogCustom;
			}
		};

		carouselView.setViewListener(CarouselViewListener);
		carouselView.setPageCount(TanitimMesajlari.length);
	}

	/**private class KaydolViewPagerAdapter extends android.support.v4.view.PagerAdapter {
		private LayoutInflater layoutInflater;
		private int[] sayfalar = new int[]{
				R.layout.activity_giris_anaekran,
				R.layout.activity_giris_kayitekran};

		private KaydolViewPagerAdapter() {

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(sayfalar[position], container, false);

			switch (position) {
				case 0: // ANA EKRAN
					YaziFontu = AkorDefterimSys.FontGetir(activity, "android");

					TextView lblUygulamaAdi = (TextView) view.findViewById(R.id.lblUygulamaAdi);
					lblUygulamaAdi.setTypeface(YaziFontu, Typeface.BOLD);

					YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

					TextView lblVersiyon = (TextView) view.findViewById(R.id.lblVersiyon);
					lblVersiyon.setTypeface(YaziFontu, Typeface.NORMAL);
					lblVersiyon.setText(AkorDefterimSys.UygulamaVersiyonuGetir());

					Button btnKaydol = (Button) view.findViewById(R.id.btnKaydol);
					btnKaydol.setTypeface(YaziFontu, Typeface.BOLD);
					btnKaydol.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							VPGirisEkranPager.setCurrentItem(1);
						}
					});

					TextView lblHesabimVar = (TextView) view.findViewById(R.id.lblHesabimVar);
					lblHesabimVar.setTypeface(YaziFontu, Typeface.BOLD);
					lblHesabimVar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							GirisViewPagerAdapter GirisViewPagerAdapter = new GirisViewPagerAdapter();
							VPGirisEkranPager.setAdapter(GirisViewPagerAdapter);

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									VPGirisEkranPager.setCurrentItem(1);
								}
							}, 100);
						}
					});

					break;
				case 1: // KAYIT EKRAN
					YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

					Button btnGeri = (Button) view.findViewById(R.id.btnGeri);
					btnGeri.setTypeface(YaziFontu, Typeface.BOLD);
					btnGeri.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							VPGirisEkranPager.setCurrentItem(0);
						}
					});

					TextView lblKayitBaslik = (TextView) view.findViewById(R.id.lblKayitBaslik);
					lblKayitBaslik.setTypeface(YaziFontu, Typeface.BOLD);

					TextView lblKayitAciklama = (TextView) view.findViewById(R.id.lblKayitAciklama);
					lblKayitAciklama.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnFacebookKayit = (Button) view.findViewById(R.id.btnFacebookKayit);
					btnFacebookKayit.setTypeface(YaziFontu, Typeface.BOLD);
					btnFacebookKayit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							FacebookLoginEkran = "Kayit";
							LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
						}
					});

					Button btnGoogleKayit = (Button) view.findViewById(R.id.btnGoogleKayit);
					btnGoogleKayit.setTypeface(YaziFontu, Typeface.BOLD);
					btnGoogleKayit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					});

					Button btnTwitterKayit = (Button) view.findViewById(R.id.btnTwitterKayit);
					btnTwitterKayit.setTypeface(YaziFontu, Typeface.BOLD);

					TextView lblVeya = (TextView) view.findViewById(R.id.lblVeya);
					lblVeya.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnNormalKayit = (Button) view.findViewById(R.id.btnNormalKayit);
					btnNormalKayit.setTypeface(YaziFontu, Typeface.BOLD);
                    btnNormalKayit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
							Intent mIntent = new Intent(activity, KayitEkran_EPosta.class);
							mIntent.putExtra("KayitTipi"), "Normal");

							AkorDefterimSys.EkranGetir(mIntent, "Explode");
                        }
                    });

					break;
			}

			container.addView(view);

			return view;
		}

		@Override
		public int getCount() {
			return sayfalar.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}
	}

	private class GirisViewPagerAdapter extends android.support.v4.view.PagerAdapter {
		private LayoutInflater layoutInflater;
		private int[] sayfalar = new int[]{
				R.layout.activity_giris_anaekran,
				R.layout.activity_giris_girisekran};

		private GirisViewPagerAdapter() {

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(sayfalar[position], container, false);

			switch (position) {
				case 0:
					YaziFontu = AkorDefterimSys.FontGetir(activity, "android");

					TextView lblUygulamaAdi = (TextView) view.findViewById(R.id.lblUygulamaAdi);
					lblUygulamaAdi.setTypeface(YaziFontu, Typeface.BOLD);

					YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

					TextView lblVersiyon = (TextView) view.findViewById(R.id.lblVersiyon);
					lblVersiyon.setTypeface(YaziFontu, Typeface.NORMAL);
					lblVersiyon.setText(AkorDefterimSys.UygulamaVersiyonuGetir());

					Button btnKaydol = (Button) view.findViewById(R.id.btnKaydol);
					btnKaydol.setTypeface(YaziFontu, Typeface.BOLD);
					btnKaydol.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							KaydolViewPagerAdapter KaydolViewPagerAdapter = new KaydolViewPagerAdapter();
							VPGirisEkranPager.setAdapter(KaydolViewPagerAdapter);

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									VPGirisEkranPager.setCurrentItem(1);
								}
							}, 100);
						}
					});

					TextView lblHesabimVar = (TextView) view.findViewById(R.id.lblHesabimVar);
					lblHesabimVar.setTypeface(YaziFontu, Typeface.BOLD);
					lblHesabimVar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							VPGirisEkranPager.setCurrentItem(1);
						}
					});

					break;
				case 1:
					YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

					Button btnGeri = (Button) view.findViewById(R.id.btnGeri);
					btnGeri.setTypeface(YaziFontu, Typeface.BOLD);
					btnGeri.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							VPGirisEkranPager.setCurrentItem(0);
						}
					});

					TextView lblGirisBaslik = (TextView) view.findViewById(R.id.lblGirisBaslik);
					lblGirisBaslik.setTypeface(YaziFontu, Typeface.BOLD);

					TextView lblGirisAciklama = (TextView) view.findViewById(R.id.lblGirisAciklama);
					lblGirisAciklama.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnFacebookLogin = (Button) view.findViewById(R.id.btnFacebookLogin);
					btnFacebookLogin.setTypeface(YaziFontu, Typeface.BOLD);

					Button btnGoogleLogin = (Button) view.findViewById(R.id.btnGoogleLogin);
					btnGoogleLogin.setTypeface(YaziFontu, Typeface.BOLD);

					Button btnTwitterLogin = (Button) view.findViewById(R.id.btnTwitterLogin);
					btnTwitterLogin.setTypeface(YaziFontu, Typeface.BOLD);

					TextView lblVeya = (TextView) view.findViewById(R.id.lblVeya);
					lblVeya.setTypeface(YaziFontu, Typeface.NORMAL);

					Button btnNormalLogin = (Button) view.findViewById(R.id.btnNormalLogin);
					btnNormalLogin.setTypeface(YaziFontu, Typeface.BOLD);

					break;
			}

			container.addView(view);

			return view;
		}

		@Override
		public int getCount() {
			return sayfalar.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}
	}*/

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "HesapGirisYap":
					AkorDefterimSys.HesapPrefSifirla();

					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefHesapID", JSONSonuc.getString("HesapID"));
						sharedPrefEditor.putString("prefEPosta", JSONSonuc.getString("HesapEPosta"));
						sharedPrefEditor.putString("prefParolaSHA1", JSONSonuc.getString("HesapParolaSHA1"));
						sharedPrefEditor.putString("prefOturumTipi", "Cevrimici");
						sharedPrefEditor.apply();

						Intent mIntent = new Intent(activity, AnaEkran.class);
						mIntent.putExtra("Islem", "");

						AkorDefterimSys.EkranGetir(mIntent, "Normal");

						mIntentMqttService.putExtra("JSONData", "{\"Islem\":\"Subscribe_HesapKanal\", \"HesapID\":\"" + JSONSonuc.getString("HesapID") + "\"}");
						this.sendBroadcast(mIntentMqttService);

						if(!JSONSonuc.getString("HesapFirebaseToken").equals(FirebaseToken)) {
							mIntentMqttService.putExtra("JSONData", "{\"Islem\":\"CikisYap\", \"HesapID\":\"" + JSONSonuc.getString("HesapID") + "\", \"HesapFirebaseToken\":\"" + JSONSonuc.getString("HesapFirebaseToken") + "\"}");
							this.sendBroadcast(mIntentMqttService);
						}

						//AkorDefterimSys.FirebaseMesajGonder(activity, JSONSonuc.getString("HesapFirebaseToken"), "{\"Islem\":\"CikisYap\", \"HesapFirebaseToken\":\"" + JSONSonuc.getString("HesapFirebaseToken") + "\"}", "FirebaseMesajGonder_CikisYap");

						finishAffinity();
					} else {
						switch (JSONSonuc.getString("HesapDurum")) {
							case "Ban":
								if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
									@SuppressLint("SimpleDateFormat")
									SimpleDateFormat SDF1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									@SuppressLint("SimpleDateFormat")
									SimpleDateFormat SDF2 = new SimpleDateFormat("dd MMMM yyyy - HH:mm:ss");

									ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
											getString(R.string.hesap_durumu),
											getString(R.string.hesap_banlandi, SDF2.format(SDF1.parse(JSONSonuc.getString("HesapDurumTarihSaat"))), JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_eposta)),
											activity.getString(R.string.tamam),
											"ADDialog_HesapDurumu_Tamam");
									ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
									ADDialog_HesapDurumu.show();
								}

								break;
						}
					}

					break;
				case "TanitimMesajlariniGetir":
					if(JSONSonuc.getBoolean("Sonuc")) {
						JSONArray JSONGelenVeriArr = new JSONArray(JSONSonuc.getString("MesajListesi"));
						String[] TanitimMesajlari = new String[JSONGelenVeriArr.length()];

						for (int i = 0; i < JSONGelenVeriArr.length(); i++)
							TanitimMesajlari[i] = JSONGelenVeriArr.getString(i);

						CarouselViewListenerInit(TanitimMesajlari);
					} else CarouselViewListenerInit(getResources().getStringArray(R.array.OfflineTanitimMesajlari));

					break;
				case "ADDialog_HesapDurumu_Tamam":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);
					break;
				case "FirebaseMesajGonder_CikisYap":

					break;
			}

		} catch (JSONException | ParseException e) {
			e.printStackTrace();
		}
	}

	private void GoogleAPIInit(Boolean GirisYapisinMi) {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		//mGoogleLoginApiClient = new GoogleApiClient.Builder(this)
		//.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
		//.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
							RLLoader.setVisibility(View.GONE);
							Log.d("Hata", "Facebook girişi yapılırken hata oluştu..");
						} else {
							RLLoader.setVisibility(View.GONE);

							try {
								String HesapID = JSONFacebookGelenVeri.getString("id");
								String AdSoyad = JSONFacebookGelenVeri.getString("name");
								//final String ResimURL = new URL("https://graph.facebook.com/" + HesapID + "/picture?width=300&height=300").toString();
								String EPosta = JSONFacebookGelenVeri.getString("email");

								AkorDefterimSys.HesapGirisYap("Facebook", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, EPosta, "", HesapID, AdSoyad);
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
				RLLoader.setVisibility(View.GONE);
				Log.d("","");
			}

			@Override
			public void onError(FacebookException error) {
				RLLoader.setVisibility(View.GONE);
				Log.d("","");
			}
		});
	}
}