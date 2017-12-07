package com.cnbcyln.app.akordefterim.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpAkorlar;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTonlar;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Retrofit.Interface.RetrofitInterface;
import com.cnbcyln.app.akordefterim.Retrofit.Network.RetrofitServiceGenerator;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapBilgiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapEkle;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapGirisYap;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfIslemSonuc;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfSistemDurum;
import com.cnbcyln.app.akordefterim.Siniflar.SnfAkorlar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTonlar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfUlkeKodlari;
import com.cnbcyln.app.akordefterim.util.SlideNav.SlideGravity;
import com.cnbcyln.app.akordefterim.util.SlideNav.SlidingRootNav;
import com.cnbcyln.app.akordefterim.util.SlideNav.SlidingRootNavBuilder;
import com.cnbcyln.app.akordefterim.util.SlideNav.callback.DragListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.transition.Explode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("deprecation")
public class AkorDefterimSys {
	private Activity activity;
	Context context;
	private Interface_FragmentDataConn FragmentDataConn;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedPrefEditor;
	private SharedPreferences.OnSharedPreferenceChangeListener sharedPrefChanged;
	private AlertDialog ADDialog, ADDialog_SistemDurum;

	// PHP AYARLARI (MAİL VE SMS GÖNDERİMİ)
	public String CBCAPP_HttpsAdres = "https://www.cbcapp.net";
	private String AkorDefterimHttpAdres = "http://akordefterim.cbcapp.net/";
	List<String> SMSGondericiAdi = Arrays.asList("+908503042567", "C.B.CEYLAN");

	public String AnaKlasorDizini = Environment.getExternalStorageDirectory() + File.separator + "Akor Defterim" + File.separator;
	public int WebBaglantiIstegiToplamSure = 30;
	public int SMSGondermeToplamSure = 180;
	public int EPostaGondermeToplamSure = 180;
	public int ProgressBarTimeoutSuresi = 30 * 1000;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	public int RC_GOOGLE_LOGIN = 9001;
	public int RC_TWITTER_LOGIN = 9002;
	public String GOOGLE_API_TAG = "Google API Giriş";
	public long FirebaseRemoteConfigCacheExpiration = 3600;
	public String PrefAdi = "AkorDefterim";
	public String IstekWebSitesi = "IstekBudur.Com";

	/** Google API Lokasyon İşlem Değişkenleri **/
	public int UPDATE_INTERVAL = 4000; // 4 saniye
	public int FATEST_INTERVAL = 3000; // 3 saniye
	public int DISPLACEMENT = 5; // 5 metre

	// **** URL ADRESLERİ
	public String SanatciResimleriKlasoruURL = CBCAPP_HttpsAdres + "/akordefterim/sanatci_img/";
	public String ProfilResimleriKlasoruURL = CBCAPP_HttpsAdres + "/akordefterim/profil_img/";
	public String ProfilResimleriKlasoruDizin = "../../profil_img/";
	public String YedeklemeKlasoruURL = AkorDefterimHttpAdres + "/rep_yedekleri/";
	public String YedeklemeKlasoruDizin = "../../rep_yedekleri/";
	public String OnlineRepertuvarListesiKlasoruURL = AkorDefterimHttpAdres + "/rep_listeleri/";
	public String OnlineRepertuvarListesiKlasoruDizin = "../../../httpdocs/akordefterim/rep_listeleri/";

	// **** Genel İşlemler
	public String PHPSMSYolla = AkorDefterimHttpAdres + "/araclar/sms/index.php";
	public String PHPEMailYolla = "/akordefterim/phpscriptleri/mail/index.php";
	public String PHPDosyaYukle = AkorDefterimHttpAdres + "/araclar/genel/dosyayukle.php";
	public String PHPResimYukle = CBCAPP_HttpsAdres + "/akordefterim/phpscriptleri/genel/resimyukle.php";
	public String AnasayfaURL = CBCAPP_HttpsAdres + "/akordefterim/phpscriptleri/genel/anasayfa.php";
	private String ReklamDuyuru = AkorDefterimHttpAdres + "/araclar/genel/reklamduyuru.php";
	private String SistemDurum = AkorDefterimHttpAdres + "/araclar/genel/sistemdurum.php";

	// **** Hesap İşlemleri - Normal
	public String PHPHesapGirisYap = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesapgirisyap.php";
	public String PHPHesapKontrol = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesapkontrol.php";
	public String PHPHesapBilgiGetir = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesapbilgigetir.php";
	public String PHPHesapOnayIslemleri = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesaponayislemleri.php";
	public String PHPHesapEkle = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesapekle.php";
	public String PHPHesapYeniSifre = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesapyenisifre.php";
	public String PHPHesapGuncelle = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/hesapguncelle.php";
	public String PHPEmailKontrol = AkorDefterimHttpAdres + "/araclar/hesapislemleri/normal/emailkontrol.php";

	// **** Hesap İşlemleri - Google
	public String PHPGoogleHesapEkle_GirisYap = AkorDefterimHttpAdres + "/araclar/hesapislemleri/google/hesapekle_girisyap.php";
	public String PHPGoogleHesapBilgiGetir = AkorDefterimHttpAdres + "/araclar/hesapislemleri/google/hesapbilgigetir.php";
	public String PHPGoogleCepTelOnayIslemi = AkorDefterimHttpAdres + "/araclar/hesapislemleri/google/ceptelonay.php";
	public String PHPGoogleHesapGuncelle = AkorDefterimHttpAdres + "/araclar/hesapislemleri/google/hesapguncelle.php";

	// **** Hesap İşlemleri - Facebook
	public String PHPFacebookHesapEkle_GirisYap = AkorDefterimHttpAdres + "/araclar/hesapislemleri/facebook/hesapekle_girisyap.php";
	public String PHPFacebookHesapBilgiGetir = AkorDefterimHttpAdres + "/araclar/hesapislemleri/facebook/hesapbilgigetir.php";
	public String PHPFacebookCepTelOnayIslemi = AkorDefterimHttpAdres + "/araclar/hesapislemleri/facebook/ceptelonay.php";
	public String PHPFacebookHesapGuncelle = AkorDefterimHttpAdres + "/araclar/hesapislemleri/facebook/hesapguncelle.php";

	// **** Genel Hesap İşlemleri
	public String PHPKullaniciAdiKontrol = AkorDefterimHttpAdres + "/araclar/hesapislemleri/kullaniciadikontrol.php";
	public String PHPCepTelefonKontrol = AkorDefterimHttpAdres + "/araclar/hesapislemleri/ceptelefonkontrol.php";

	// **** Repertuvar İşlemleri
	public String PHPSarkiListesiGetir = AkorDefterimHttpAdres + "/araclar/repertuvarislemleri/sarkilistesigetir.php";
	public String PHPSarkiGetir = AkorDefterimHttpAdres + "/araclar/repertuvarislemleri/sarkigetir.php";
	public String PHPKategoriListesiGetir = AkorDefterimHttpAdres + "/araclar/repertuvarislemleri/kategorilistesigetir.php";
	public String PHPTarzListesiGetir = AkorDefterimHttpAdres + "/araclar/repertuvarislemleri/tarzlistesigetir.php";
	public String PHPRepertuvarIcerikGonder = AkorDefterimHttpAdres + "/araclar/repertuvarislemleri/rep_icerik_gonder.php";

	// **** Firebase İşlemleri
	private String PHPFirebaseNotify = AkorDefterimHttpAdres + "/araclar/firebase/mesajgonder.php";

	public AkorDefterimSys() {}

	public AkorDefterimSys(Activity activity) {
		this.activity = activity;
	}

	public AkorDefterimSys(Context context) {
		this.context = context;
	}

	public void GenelAyarlar() {
		final WindowManager.LayoutParams layoutpars = activity.getWindow().getAttributes();

		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Klavyeyi gizleyen method
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Ekranın yalnızca portrait (Dikey) olarak çalışmasını ayarlıyoruz

		sharedPref = activity.getSharedPreferences(PrefAdi, Context.MODE_PRIVATE); // Genel Ayarlar objesi

		sharedPrefChanged = new SharedPreferences.OnSharedPreferenceChangeListener() { // Uygulama içi ayar değişikliği olduğunda tetiklenen method
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				switch (key) {
					case "prefEkranIsigiAydinligi":
						// Ekran ışığını eğer prefEkranIsigiAydinligi değeri ayarlanmamışsa en parlak olan 255'e ayarlıyoruz. Aksi halde ayar ne ise o ayarlanıyor..
						layoutpars.screenBrightness = sharedPref.getInt("prefEkranIsigiAydinligi", 255) / (float)255;
						activity.getWindow().setAttributes(layoutpars); // Yeni değerler ekrana uygulanıyor

						break;
				}
			}
		};
		sharedPref.registerOnSharedPreferenceChangeListener(sharedPrefChanged);

		// Ekran ışığını eğer prefEkranIsigiAydinligi değeri ayarlanmamışsa en parlak olan 255'e ayarlıyoruz. Aksi halde ayar ne ise o ayarlanıyor..
		layoutpars.screenBrightness = sharedPref.getInt("prefEkranIsigiAydinligi", 255) / (float)255;
		activity.getWindow().setAttributes(layoutpars); // Yeni değerler ekrana uygulanıyor
	}

	public SharedPreferences PrefAyarlar() {
		return sharedPref;
	}

	public void NotifyIkonParlakligi() {
		// Notification Bar'daki simgelerin parlaklığını aldık
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
			view.setSystemUiVisibility(view.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		}
	}

	public String UygulamaVersiyonuGetir() {
		String Versiyon = null;

		try {
			Versiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return Versiyon;
	}

	public void EkranAnimasyon(String AnimasyonTipi) {
		switch (AnimasyonTipi) {
			case "Explode":
				// Eğer cihazın android sürümü LOLLIPOP ya da daha üstüyse explode animasyonunu çalıştırır.
				// Aksi halde soldan sağa slide animasyonu çalışır..
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					// Ekranın "Explode" animasyonu ile açılıp (setEnterTransition) kapanmasını (setExitTransition) sağladık. Animasyon hızı: 500ms
					Explode explode = new Explode();
					explode.setDuration(500);
					activity.getWindow().setEnterTransition(explode);
					activity.getWindow().setExitTransition(explode);
				}

				break;
			default:
				// 1.Parametre: Açılan ekran animasyonu / 2.Parametre: Kapanan ekran animasyonu
				activity.overridePendingTransition(R.anim.anim_slide_acilan_ekran_sol, R.anim.anim_slide_kapanan_ekran_sol);
				break;
		}
	}

	public void EkranGetir(Intent mIntent, String AnimasyonTipi) {
		switch (AnimasyonTipi) {
			case "Explode":
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					// Ekranın "Explode" animasyonu ile açılıp (setEnterTransition) kapanmasını (setExitTransition) sağladık. Animasyon hızı: 500ms
					Explode explode = new Explode();
					explode.setDuration(500);
					activity.getWindow().setEnterTransition(explode);
					activity.getWindow().setExitTransition(explode);

					@SuppressWarnings("unchecked")
					ActivityOptionsCompat AOC = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
					activity.startActivity(mIntent, AOC.toBundle());
				} else {
					activity.startActivity(mIntent);
					// 1.Parametre: Açılan ekran animasyonu / 2.Parametre: Kapanan ekran animasyonu
					activity.overridePendingTransition(R.anim.anim_slide_acilan_ekran_sol, R.anim.anim_slide_kapanan_ekran_sol);
				}

				break;
			case "Slide":
				activity.startActivity(mIntent);

				// 1.Parametre: Açılan ekran animasyonu / 2.Parametre: Kapanan ekran animasyonu
				activity.overridePendingTransition(R.anim.anim_slide_acilan_ekran_sol, R.anim.anim_slide_kapanan_ekran_sol);
				break;
			case "Normal":
				activity.startActivity(mIntent);
				break;
			default:
				activity.startActivity(mIntent);
				break;
		}
	}

	private float[] EkranDPGetir() {
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float density  = activity.getResources().getDisplayMetrics().density;
		float dpWidth  = outMetrics.widthPixels / density;
		float dpHeight = outMetrics.heightPixels / density;

		return new float[]{dpWidth, dpHeight};
	}

	public SlidingRootNav SetupSlidingMenu(Bundle savedInstanceState, View ViewSlidingContainer, final boolean Alpha, final boolean Scale) {
        final ConstraintLayout CLContainer = ViewSlidingContainer.findViewById(R.id.CLMenu);
        if(Alpha) CLContainer.setAlpha(0.0f);
        if(Scale) {
            CLContainer.setScaleX(1.1f);
            CLContainer.setScaleY(1.1f);
        }

        return new SlidingRootNavBuilder(activity)
                .withSavedState(savedInstanceState)
                .withMenuView(ViewSlidingContainer)
                .withDragDistance(Math.round((EkranDPGetir()[0] / 100) * 65)) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(8) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .withGravity(ViewSlidingContainer.getTag() == "R.layout.menu_sol" ? SlideGravity.LEFT : SlideGravity.RIGHT)
                .addDragListener(new DragListener() {
                    @Override
                    public void onDrag(float progress) {
                        if(Alpha) CLContainer.setAlpha(progress);
                        if(Scale) {
                            CLContainer.setScaleX(1.1f - (progress / 10));
                            CLContainer.setScaleY(1.1f - (progress / 10));
                        }
                    }
                })
                .inject();
	}

	public void KlavyeKapat() {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		View view = activity.getCurrentFocus();

		// Klavyeyi kapatıyoruz.
		if (view != null) if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public int ParolaGuvenligi(String Parola) {
		int Skor = 0;
		System.out.println("Girilen Parola : " + Parola);

		int length = 0;
		int uppercase = 0;
		int lowercase = 0;
		int digits = 0;
		int symbols = 0;
		int bonus = 0;
		int requirements = 0;
		int lettersonly = 0, numbersonly = 0, cuc = 0, clc = 0;

		length = Parola.length();

		for (int i = 0; i < Parola.length(); i++) {
			if (Character.isUpperCase(Parola.charAt(i))) uppercase++;
			else if (Character.isLowerCase(Parola.charAt(i))) lowercase++;
			else if (Character.isDigit(Parola.charAt(i))) digits++;

			symbols = length - uppercase - lowercase - digits;
		}

		for (int j = 1; j < Parola.length() - 1; j++) {
			if (Character.isDigit(Parola.charAt(j))) bonus++;
		}

		for (int k = 0; k < Parola.length(); k++) {
			if (Character.isUpperCase(Parola.charAt(k))) {
				k++;

				if (k < Parola.length()) {
					if (Character.isUpperCase(Parola.charAt(k))) {
						cuc++;
						k--;
					}
				}
			}
		}

		for (int l = 0; l < Parola.length(); l++) {
			if (Character.isLowerCase(Parola.charAt(l))) {
				l++;

				if (l < Parola.length()) {
					if (Character.isLowerCase(Parola.charAt(l))) {
						clc++;
						l--;
					}
				}
			}
		}

		System.out.println("length" + length);
		System.out.println("uppercase" + uppercase);
		System.out.println("lowercase" + lowercase);
		System.out.println("digits" + digits);
		System.out.println("symbols" + symbols);
		System.out.println("bonus" + bonus);
		System.out.println("cuc" + cuc);
		System.out.println("clc" + clc);

		if (length >= 6) requirements++;
		if (uppercase > 0) requirements++;
		if (lowercase > 0) requirements++;
		if (digits > 0) requirements++;
		if (symbols > 0) requirements++;
		if (bonus > 0) requirements++;
		if (digits == 0 && symbols == 0) lettersonly = 1;
		if (lowercase == 0 && uppercase == 0 && symbols == 0) numbersonly = 1;

		int Total = (length * 4) + ((length - uppercase) * 2)
				+ ((length - lowercase) * 2) + (digits * 4) + (symbols * 6)
				+ (bonus * 2) + (requirements * 2) - (lettersonly * length*2)
				- (numbersonly * length*3) - (cuc * 2) - (clc * 2);

		System.out.println("Total" + Total);

		if(Total<30) Skor = Total-15;
		else if (Total>=40 && Total <50) Skor = Total-20;
		else if (Total>=56 && Total <70) Skor = Total-25;
		else if (Total>=76) Skor = Total-30;
		else Skor = Total-20;

		return Total;
	}

	public float getRating(String password) throws IllegalArgumentException {
		if (password == null) {throw new IllegalArgumentException();}

		int passwordStrength = 0;

		if (password.length() > 5) passwordStrength++; // Parola min 6 karakter ise
		if (password.toLowerCase().equals(password)) passwordStrength++; // lower and upper case
		if (password.length() > 8) {passwordStrength++;} // good pw length of 9+
		int numDigits= getNumberDigits(password);
		if (numDigits > 0 && numDigits != password.length()) {passwordStrength++;} // contains digits and non-digits

		return (float)passwordStrength;
	}

	private static int getNumberDigits(String inString){
		if (TextUtils.isEmpty(inString)) {
			return 0;
		}
		int numDigits= 0;
		int length= inString.length();
		for (int i = 0; i < length; i++) {
			if (Character.isDigit(inString.charAt(i))) {
				numDigits++;
			}
		}
		return numDigits;
	}

	public void FullPrefSifirla() {
		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.clear();
		sharedPrefEditor.apply();
	}

	public void HesapPrefSifirla() {
		sharedPrefEditor = sharedPref.edit();
		sharedPrefEditor.remove("prefHesapID");
		sharedPrefEditor.remove("prefEPostaKullaniciAdiTelefon");
		sharedPrefEditor.remove("prefParolaSHA1");
		sharedPrefEditor.remove("prefOturumTipi");
		sharedPrefEditor.apply();
	}

	public double LokasyonlarArasiMetreHesaplayici(double LokasyonLat1, double LokasyonLng1, double LokasyonLat2, double LokasyonLng2) {
		int DunyaninYariCapiOrtalama = 6378137;
		double dLat = (LokasyonLat2 - LokasyonLat1) * Math.PI / 180;
		double dLong = (LokasyonLng2 - LokasyonLng1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(LokasyonLat1 * Math.PI / 180) * Math.cos(LokasyonLat2 * Math.PI / 180) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return DunyaninYariCapiOrtalama * c;
	}

	public boolean WifiErisimKontrolu() {
		ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();
	}

	public boolean InternetErisimKontrolu() {
		if(activity != null) {
			ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService (Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo MobileInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifiInfo.isConnected()) {
				return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
			} else if (MobileInfo.isConnected()) {
				return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
			} else {
				return false;
			}
		} else if(context != null) {
			ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo MobileInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifiInfo.isConnected()) {
				return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
			} else if (MobileInfo.isConnected()) {
				return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
			} else {
				return false;
			}
		} else return false;
	}

	public boolean InternetErisimKontrolu(Context context) {
		if(context != null) {
			ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo MobileInfo = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (wifiInfo.isConnected()) {
				return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
			} else if (MobileInfo.isConnected()) {
				return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected();
			} else {
				return false;
			}
		} else return false;
	}

	public boolean GucTasarrufKontrolu() {
		PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);

		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && powerManager.isPowerSaveMode();
	}

	public boolean checkPlayServices(Activity activity) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				//Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
				//finish();
			}
			return false;
		}
		return true;
	}

	public String ZamanFormatHHMMSS(long secondsCount) {
		//Calculate the seconds to display:
		int seconds = (int) (secondsCount % 60);
		secondsCount -= seconds;
		//Calculate the minutes:
		long minutesCount = secondsCount / 60;
		long minutes = minutesCount % 60;
		minutesCount -= minutes;
		//Calculate the hours:
		long hoursCount = minutesCount / 60;

		//Build the String
		String Saniye = String.valueOf(seconds);
		String Dakika = String.valueOf(minutes);
		String Saat = String.valueOf(hoursCount);

		if (Saniye.length() < 2) {
			Saniye = "0" + Saniye;
		}

		if (Dakika.length() < 2) {
			Dakika = "0" + Dakika;
		}

		if (Saat.length() < 2) {
			Saat = "0" + Saat;
		}

		return Saat + ":" + Dakika + ":" + Saniye;
	}

	public String ZamanFormatMMSS(long secondsCount) {
		//Calculate the seconds to display:
		int seconds = (int) (secondsCount % 60);
		secondsCount -= seconds;

		//Calculate the minutes:
		long minutesCount = secondsCount / 60;
		long minutes = minutesCount % 60;
		minutesCount -= minutes;

		//Build the String
		String Saniye = String.valueOf(seconds);
		String Dakika = String.valueOf(minutes);

		if (Saniye.length() < 2) {
			Saniye = "0" + Saniye;
		}

		if (Dakika.length() < 2) {
			Dakika = "0" + Dakika;
		}

		return Dakika + ":" + Saniye;
	}

	public Typeface FontGetir(Context context, String FontAdi) {
		Typeface Font = null;

		switch (FontAdi) {
			case "android":
				Font = Typeface.createFromAsset(context.getAssets(), "fonts/android.ttf");
				break;
			case "android_italic":
				Font = Typeface.createFromAsset(context.getAssets(), "fonts/android.ttf");
				break;
			case "android_scratch":
				Font = Typeface.createFromAsset(context.getAssets(), "fonts/android_scratch.ttf");
				break;
			case "angelina":
				Font = Typeface.createFromAsset(context.getAssets(), "fonts/angelina.ttf");
				break;
			case "anivers_regular":
				Font = Typeface.createFromAsset(context.getAssets(), "fonts/anivers_regular.otf");
				break;
		}

		return Font;
	}

	public String TarihSeciciDialog(final EditText txtTarih) {
		int Yil, Ay, Gun;

		KlavyeKapat();

		if(!TextUtils.isEmpty(txtTarih.getText().toString())) {
			String Tarih = txtTarih.getText().toString();

			if(Tarih.split("/")[0].substring(0,1).equals("0"))
				Gun = Integer.parseInt(Tarih.split("/")[0].substring(1, Tarih.split("/")[0].length()));
			else
				Gun = Integer.parseInt(Tarih.split("/")[0]);

			if(Tarih.split("/")[1].substring(0,1).equals("0"))
				Ay = Integer.parseInt(Tarih.split("/")[1].substring(1, Tarih.split("/")[1].length())) - 1;
			else
				Ay = Integer.parseInt(Tarih.split("/")[1]) - 1;

			Yil = Integer.parseInt(Tarih.split("/")[2]);
		} else {
			Calendar mcurrentTime = Calendar.getInstance();

			Gun = mcurrentTime.get(Calendar.DAY_OF_MONTH); // Güncel günü alıyoruz
			Ay = mcurrentTime.get(Calendar.MONTH); // Güncel ayı alıyoruz
			Yil = mcurrentTime.get(Calendar.YEAR); // Güncel yılı alıyoruz
		}

		DatePickerDialog datePicker; // Datepicker objemiz

		datePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				String Gun, Ay;

				if(String.valueOf(dayOfMonth).length() == 1) Gun = "0" + String.valueOf(dayOfMonth);
				else Gun = String.valueOf(dayOfMonth);

				if(String.valueOf(monthOfYear+1).length() == 1) Ay = "0" + String.valueOf(monthOfYear+1);
				else Ay = String.valueOf(monthOfYear+1);

				txtTarih.setText(Gun + "/" + Ay + "/" + year); // Ayarla butonu tıklandığında textview'a yazdırıyoruz

                UnFocusEditText(txtTarih);
			}
		}, Yil, Ay, Gun); // Başlarken set edilcek değerlerimizi atıyoruz

		datePicker.setTitle(activity.getString(R.string.tarih_secin));
		datePicker.setButton(DialogInterface.BUTTON_POSITIVE, activity.getString(R.string.ayarla), datePicker);
		datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.iptal), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_NEGATIVE) {
                    UnFocusEditText(txtTarih);
					dialog.dismiss();
				}
			}
		});

		datePicker.show();

		return txtTarih.getText().toString();
	}

	private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
		int start = strBuilder.getSpanStart(span);
		int end = strBuilder.getSpanEnd(span);
		int flags = strBuilder.getSpanFlags(span);

		ClickableSpan clickable = new ClickableSpan() {
			public void onClick(View view) {
				String url = span.getURL();

				if (!url.startsWith("http://") && !url.startsWith("https://"))
					url = "http://" + url;

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				activity.startActivity(browserIntent);
			}
		};

		strBuilder.setSpan(clickable, start, end, flags);
		strBuilder.removeSpan(span);
	}

	public void setTextViewHTML(TextView text) {
		CharSequence sequence = Html.fromHtml(text.getText().toString());
		SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
		URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);

		for(URLSpan span : urls) {
			makeLinkClickable(strBuilder, span);
		}

		text.setText(strBuilder);
		text.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public void setTextViewOnClick(TextView textView, final String Islem) {
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;
		String Icerik = textView.getText().toString().trim();
		int KarakterBaslangicNo = Icerik.indexOf("[");
        int KarakterBitisNo = Icerik.indexOf("]") - 1;

		SpannableString ss = new SpannableString(Icerik.replace("[", "").replace("]", ""));

		StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
		ss.setSpan(bss, KarakterBaslangicNo, KarakterBitisNo, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		//StyleSpan iss = new StyleSpan(android.graphics.Typeface.ITALIC);
		//ss.setSpan(iss, 4, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Islem + "\"}");
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setUnderlineText(false);
			}
		};
		ss.setSpan(clickableSpan, KarakterBaslangicNo, KarakterBitisNo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		textView.setText(ss);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		textView.setHighlightColor(Color.TRANSPARENT);
	}

	@SuppressLint("InflateParams")
	public void ToastMsj(Activity activity, String Mesaj, int Sure) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewCustomToast = inflater.inflate(R.layout.toast_custom, null);
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		TextView txtToastMesaj = ViewCustomToast.findViewById(R.id.txtToastMesaj);
		txtToastMesaj.setText(Mesaj);
		txtToastMesaj.setTypeface(YaziFontu, Typeface.NORMAL);

		Toast toast = new Toast(activity);
		toast.setGravity(Gravity.BOTTOM, 0, 20);
		toast.setDuration(Sure);
		toast.setView(ViewCustomToast);
		toast.show();
	}

	public void StandartSnackBarMsj(CoordinatorLayout mCoordinatorLayout, String Mesaj) {
		final Snackbar snackbar = SnackBar(mCoordinatorLayout,
				Mesaj,
				R.color.Beyaz,
				R.color.TuruncuYazi,
				R.integer.SnackBar_GurunumSuresi_5);

		snackbar.setAction(activity.getString(R.string.tamam), new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				snackbar.dismiss();
			}
		});

		snackbar.show();
	}

	public Snackbar SnackBar(final CoordinatorLayout coordinatorLayout, String Mesaj) {
		Snackbar snackbar = Snackbar.make(coordinatorLayout, Mesaj, Snackbar.LENGTH_LONG);
		snackbar.show();

		return snackbar;
	}

	public Snackbar SnackBar(final CoordinatorLayout coordinatorLayout, String Mesaj, int Sure) {
		Snackbar snackbar = Snackbar.make(coordinatorLayout, Mesaj, Snackbar.LENGTH_LONG);
		snackbar.setDuration(activity.getResources().getInteger(Sure));
		snackbar.show();

		return snackbar;
	}

	public Snackbar SnackBar(final CoordinatorLayout coordinatorLayout, String Mesaj, int MesajRengi, int Sure) {
		Typeface YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
		Snackbar snackbar = Snackbar.make(coordinatorLayout, Mesaj, Snackbar.LENGTH_LONG);
		snackbar.setDuration(activity.getResources().getInteger(Sure));

		View sbView = snackbar.getView();

		sbView.setBackgroundColor(activity.getResources().getColor(R.color.SnackBarBG)); // Snack bar arkaplan rengi ayarlandı
		//sbView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_fadein)); // Snack bar açılış animasyonu ayarlandı

		TextView MesajIcerik = sbView.findViewById(android.support.design.R.id.snackbar_text);
		MesajIcerik.setTextColor(activity.getResources().getColor(MesajRengi));
		MesajIcerik.setTypeface(YaziFontu, Typeface.NORMAL);

		return snackbar;
	}

	public Snackbar SnackBar(final CoordinatorLayout coordinatorLayout, String Mesaj, int MesajRengi, int ButtonYaziRengi, int Sure) {
		Typeface YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
		Snackbar snackbar = Snackbar.make(coordinatorLayout, Mesaj, Snackbar.LENGTH_LONG);
		snackbar.setDuration(activity.getResources().getInteger(Sure));
		snackbar.setActionTextColor(activity.getResources().getColor(ButtonYaziRengi)); // Button rengi ayarlandı

		View sbView = snackbar.getView();

		sbView.setBackgroundColor(activity.getResources().getColor(R.color.SnackBarBG)); // Snack bar arkaplan rengi ayarlandı
		sbView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_fade_up)); // Snack bar açılış animasyonu ayarlandı

		TextView MesajIcerik = sbView.findViewById(android.support.design.R.id.snackbar_text);
		MesajIcerik.setTextColor(activity.getResources().getColor(MesajRengi));
		MesajIcerik.setTypeface(YaziFontu, Typeface.NORMAL);
		//MesajIcerik.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.yazi_boyutu16));

		return snackbar;
	}

	public void CustomPopupWindow(final Activity activity, View view, int MenuRes) {
		FragmentDataConn = (Interface_FragmentDataConn) activity;
		PopupMenu popup = new PopupMenu(activity, view);

		try {
			Field[] fields = popup.getClass().getDeclaredFields();
			for (Field field : fields) {
				if ("mPopup".equals(field.getName())) {
					field.setAccessible(true);
					Object menuPopupHelper = field.get(popup);
					Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
					Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
					setForceIcons.invoke(menuPopupHelper, true);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		popup.getMenuInflater().inflate(MenuRes, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				//FragmentDataConn.PopupMenuIslemleri(item);
				return false;
			}
		});

		popup.show();
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, String Baslik, String Mesaj, String ButtonMsjText, final String ButtonIslem) {
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogCustom;
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogCustom = inflater.inflate(R.layout.dialog_custom_1tus, null);

		TextView lblDialogBaslik = ViewDialogCustom.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		TextView lblDialogIcerik = ViewDialogCustom.findViewById(R.id.lblDialogIcerik);
		lblDialogIcerik.setTypeface(YaziFontu, Typeface.NORMAL);
		lblDialogIcerik.setText(new SpannableStringBuilder(Html.fromHtml(Mesaj)));

		Button btnDialogButton = ViewDialogCustom.findViewById(R.id.btnDialogButton);
		btnDialogButton.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton.setText(ButtonMsjText);
		btnDialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + ButtonIslem + "\"}");
			}
		});

		return new AlertDialog.Builder(activity)
				.setView(ViewDialogCustom)
				.setCancelable(false)
				.create();
	}

	@SuppressLint("InflateParams")
	public AlertDialog HButtonCustomAlertDialog(Activity activity, String Baslik, String Mesaj, String Button1MsjText, final String Button1Islem, String Button2MsjText, final String Button2Islem) {
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogCustom;
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogCustom = inflater.inflate(R.layout.dialog_custom_yanyana_2tus, null);

		TextView lblDialogBaslik = ViewDialogCustom.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		TextView lblDialogIcerik = ViewDialogCustom.findViewById(R.id.lblDialogIcerik);
		lblDialogIcerik.setTypeface(YaziFontu, Typeface.NORMAL);
		lblDialogIcerik.setText(new SpannableStringBuilder(Html.fromHtml(Mesaj)));

		Button btnDialogButton1 = ViewDialogCustom.findViewById(R.id.btnDialogButton1);
		btnDialogButton1.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton1.setText(Button1MsjText);
		btnDialogButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button1Islem + "\"}");
			}
		});

		Button btnDialogButton2 = ViewDialogCustom.findViewById(R.id.btnDialogButton2);
		btnDialogButton2.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton2.setText(Button2MsjText);
		btnDialogButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button2Islem + "\"}");
			}
		});

		return new AlertDialog.Builder(activity)
				.setView(ViewDialogCustom)
				.setCancelable(false)
				.create();
	}

	@SuppressLint("InflateParams")
	public AlertDialog VButtonCustomAlertDialog(Activity activity, String Baslik, String Mesaj, String Button1MsjText, final String Button1Islem, String Button2MsjText, final String Button2Islem) {
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogCustom;
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogCustom = inflater.inflate(R.layout.dialog_custom_altalta_2tus, null);

		TextView lblDialogBaslik = ViewDialogCustom.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		TextView lblDialogIcerik = ViewDialogCustom.findViewById(R.id.lblDialogIcerik);
		lblDialogIcerik.setTypeface(YaziFontu, Typeface.NORMAL);
		lblDialogIcerik.setText(new SpannableStringBuilder(Html.fromHtml(Mesaj)));

		Button btnDialogButton1 = ViewDialogCustom.findViewById(R.id.btnDialogButton1);
		btnDialogButton1.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton1.setText(Button1MsjText);
		btnDialogButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button1Islem + "\"}");
			}
		});

		Button btnDialogButton2 = ViewDialogCustom.findViewById(R.id.btnDialogButton2);
		btnDialogButton2.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton2.setText(Button2MsjText);
		btnDialogButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button2Islem + "\"}");
			}
		});

		return new AlertDialog.Builder(activity)
				.setView(ViewDialogCustom)
				.setCancelable(false)
				.create();
	}

	@SuppressLint("InflateParams")
	public AlertDialog VButtonCustomAlertDialog(Activity activity, String Baslik, String Mesaj, String Button1MsjText, final String Button1Islem, String Button2MsjText, final String Button2Islem, String Button3MsjText, final String Button3Islem) {
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogCustom;
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogCustom = inflater.inflate(R.layout.dialog_custom_altalta_2tus, null);

		TextView lblDialogBaslik = ViewDialogCustom.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		TextView lblDialogIcerik = ViewDialogCustom.findViewById(R.id.lblDialogIcerik);
		lblDialogIcerik.setTypeface(YaziFontu, Typeface.NORMAL);
		lblDialogIcerik.setText(new SpannableStringBuilder(Html.fromHtml(Mesaj)));

		Button btnDialogButton1 = ViewDialogCustom.findViewById(R.id.btnDialogButton1);
		btnDialogButton1.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton1.setText(Button1MsjText);
		btnDialogButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button1Islem + "\"}");
			}
		});

		Button btnDialogButton2 = ViewDialogCustom.findViewById(R.id.btnDialogButton2);
		btnDialogButton2.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton2.setText(Button2MsjText);
		btnDialogButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button2Islem + "\"}");
			}
		});

		Button btnDialogButton3 = ViewDialogCustom.findViewById(R.id.btnDialogButton3);
		btnDialogButton3.setTypeface(YaziFontu, Typeface.BOLD);
		btnDialogButton3.setText(Button3MsjText);
		btnDialogButton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"" + Button3Islem + "\"}");
			}
		});

		return new AlertDialog.Builder(activity)
				.setView(ViewDialogCustom)
				.setCancelable(false)
				.create();
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, View DialogLayoutContent) {
		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setView(DialogLayoutContent)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, View DialogLayoutContent, String NormalButtonMsjText) {
		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setView(DialogLayoutContent)
				.setNeutralButton(NormalButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, View DialogLayoutContent, String OnayButtonMsjText, String IptalButtonMsjText) {
		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setView(DialogLayoutContent)
				.setPositiveButton(OnayButtonMsjText, null)
				.setNegativeButton(IptalButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, View DialogLayoutContent, String OnayButtonMsjText, String IptalButtonMsjText, String NormalButtonMsjText) {
		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setView(DialogLayoutContent)
				.setPositiveButton(OnayButtonMsjText, null)
				.setNegativeButton(IptalButtonMsjText, null)
				.setNeutralButton(NormalButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, int Icon, String Baslik, View DialogLayoutContent, String ButtonMsjText) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogBaslik;
		Resources res = activity.getResources();
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogBaslik = inflater.inflate(R.layout.dialog_custom_baslik, null);

		ImageView ImgDialogIcon = ViewDialogBaslik.findViewById(R.id.ImgDialogIcon);
		ImgDialogIcon.setImageDrawable(res.getDrawable(Icon));

		TextView lblDialogBaslik = ViewDialogBaslik.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setCustomTitle(ViewDialogBaslik)
				.setView(DialogLayoutContent)
				.setNeutralButton(ButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}


	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, int Icon, String Baslik, View DialogLayoutContent, String OnayButtonMsjText, String IptalButtonMsjText, Boolean KapanabilirMi) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogBaslik;
		Resources res = activity.getResources();
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogBaslik = inflater.inflate(R.layout.dialog_custom_baslik, null);

		ImageView ImgDialogIcon = ViewDialogBaslik.findViewById(R.id.ImgDialogIcon);
		ImgDialogIcon.setImageDrawable(res.getDrawable(Icon));

		TextView lblDialogBaslik = ViewDialogBaslik.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setCustomTitle(ViewDialogBaslik)
				.setView(DialogLayoutContent)
				.setPositiveButton(OnayButtonMsjText, null)
				.setNegativeButton(IptalButtonMsjText, null)
				.setCancelable(KapanabilirMi)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, int Icon, String Baslik, View DialogLayoutContent, String OnayButtonMsjText, String IptalButtonMsjText, String NormalButtonMsjText) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogBaslik;
		Resources res = activity.getResources();
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogBaslik = inflater.inflate(R.layout.dialog_custom_baslik, null);

		ImageView ImgDialogIcon = ViewDialogBaslik.findViewById(R.id.ImgDialogIcon);
		ImgDialogIcon.setImageDrawable(res.getDrawable(Icon));

		TextView lblDialogBaslik = ViewDialogBaslik.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setCustomTitle(ViewDialogBaslik)
				.setView(DialogLayoutContent)
				.setPositiveButton(OnayButtonMsjText, null)
				.setNegativeButton(IptalButtonMsjText, null)
				.setNeutralButton(NormalButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, int Icon, String Baslik, String Mesaj, String OnayButtonMsjText, String IptalButtonMsjText, String NormalButtonMsjText) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogBaslik;
		Resources res = activity.getResources();
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogBaslik = inflater.inflate(R.layout.dialog_custom_baslik, null);

		ImageView ImgDialogIcon = ViewDialogBaslik.findViewById(R.id.ImgDialogIcon);
		ImgDialogIcon.setImageDrawable(res.getDrawable(Icon));

		TextView lblDialogBaslik = ViewDialogBaslik.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setCustomTitle(ViewDialogBaslik)
				.setMessage(new SpannableStringBuilder(Html.fromHtml(Mesaj)))
				.setPositiveButton(OnayButtonMsjText, null)
				.setNegativeButton(IptalButtonMsjText, null)
				.setNeutralButton(NormalButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, int Icon, String Baslik, String Mesaj, String OnayButtonMsjText, String IptalButtonMsjText) {
		LayoutInflater inflater = activity.getLayoutInflater();
		Resources res = activity.getResources();
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		View ViewDialogBaslik = inflater.inflate(R.layout.dialog_custom_baslik, null);

		ImageView ImgDialogIcon = ViewDialogBaslik.findViewById(R.id.ImgDialogIcon);
		ImgDialogIcon.setImageDrawable(res.getDrawable(Icon));

		TextView lblDialogBaslik = ViewDialogBaslik.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setCustomTitle(ViewDialogBaslik)
				.setMessage(new SpannableStringBuilder(Html.fromHtml(Mesaj)))
				.setPositiveButton(OnayButtonMsjText, null)
				.setNegativeButton(IptalButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	@SuppressLint("InflateParams")
	public AlertDialog CustomAlertDialog(Activity activity, int Icon, String Baslik, String Mesaj, String OnayButtonMsjText) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View ViewDialogBaslik;
		Resources res = activity.getResources();
		Typeface YaziFontu = FontGetir(activity, "anivers_regular");

		ViewDialogBaslik = inflater.inflate(R.layout.dialog_custom_baslik, null);

		ImageView ImgDialogIcon = ViewDialogBaslik.findViewById(R.id.ImgDialogIcon);
		ImgDialogIcon.setImageDrawable(res.getDrawable(Icon));

		TextView lblDialogBaslik = ViewDialogBaslik.findViewById(R.id.lblDialogBaslik);
		lblDialogBaslik.setTypeface(YaziFontu, Typeface.BOLD);
		lblDialogBaslik.setText(new SpannableStringBuilder(Html.fromHtml(Baslik)));

		AlertDialog Dialog = new AlertDialog.Builder(activity)
				.setCustomTitle(ViewDialogBaslik)
				.setMessage(new SpannableStringBuilder(Html.fromHtml(Mesaj)))
				.setPositiveButton(OnayButtonMsjText, null)
				.setCancelable(false)
				.create();

		return Dialog;
	}

	public void NotifyGoster(Intent ıntent, String NotifyBaslik, String NotifyIcerik, String SubIcerik, int Number, String TickerIcerik, String BigNotifyBaslik, String BigNotifyIcerik, String BigSubIcerik, Boolean Titresim) {
		Uri defaultSoundUri;
		PendingIntent pendingIntent;
		Notification.Builder NotifyBuilder;

		pendingIntent = PendingIntent.getActivity(activity, 0, ıntent, PendingIntent.FLAG_UPDATE_CURRENT);
		defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		/*RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_view);
		remoteViews.setImageViewResource(R.id.NotifyImage, R.mipmap.ic_launcher);

		remoteViews.setTextViewText(R.id.NotifyBaslik, NotifyBaslik);
		remoteViews.setTextViewText(R.id.NotifyIcerik, NotifyIcerik);*/

		NotifyBuilder = new Notification.Builder(activity)
				.setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher))
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(NotifyBaslik)
				.setContentText(NotifyIcerik)
				.setAutoCancel(true)
				.setDefaults(Titresim ? Notification.DEFAULT_ALL : Notification.DEFAULT_LIGHTS)
				.setSound(defaultSoundUri)
				.setLights(Color.WHITE, 1000, 300)
				//.setContent(remoteViews) // Farklı bir layout göstermek istersek..
				.setContentIntent(pendingIntent);


		if(!SubIcerik.equals("")) NotifyBuilder.setSubText(SubIcerik); // Icerigin alt kısmına bir yazı daha ekler..
		if(!TickerIcerik.equals("")) NotifyBuilder.setTicker(TickerIcerik); // Bildirim çubuğuna yazı ekler..
		if(Number != -1) NotifyBuilder.setNumber(Number); // Sol kısıma numara ekler..

		Notification notification = new Notification.BigTextStyle(NotifyBuilder)
				.setBigContentTitle(BigNotifyBaslik)
				.bigText(BigNotifyIcerik)
				.setSummaryText(BigSubIcerik) // Icerigin alt kısmına bir yazı daha ekler..
				.build();

		/** Small Icon'u kaldırmaya yarayan fonksiyon **/
		int smallIconId = activity.getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
		if (smallIconId != 0) {
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
				notification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
				notification.bigContentView.setViewVisibility(smallIconId, View.INVISIBLE);
			}
		}
		/** Small Icon'u kaldırmaya yarayan fonksiyon **/

		NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification);
	}

	public ProgressDialog CustomProgressDialog(String Mesaj, Boolean KapanabilirMi, int Timeout) {
		final ProgressDialog mProgressDialog = new ProgressDialog(activity);
		mProgressDialog.setMessage(Mesaj);
		mProgressDialog.setCancelable(KapanabilirMi);

		if(Timeout != 0) {
			final Handler h = new Handler() { // Progress Dialog'a 10 saniye timeout süresi verdik..
				@Override
				public void handleMessage(Message message) {
					// Progress Dialog'u kapattık
					DismissProgressDialog(mProgressDialog);
				}
			};
			h.sendMessageDelayed(new Message(), Timeout);
		}

		return mProgressDialog;
	}

	public int ResimBoyutHesapla(Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public int ListeGenislikHesapla(int width, int yuzde) {
		return ((width / 2) * yuzde) / 100;
	}

	public String getIpAddress() {
		String ip = "";

		try {
			Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();

			while (enumNetworkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
				Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

				while (enumInetAddress.hasMoreElements()) {
					InetAddress inetAddress = enumInetAddress.nextElement();

					if (inetAddress.isSiteLocalAddress()) {
						ip += inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			ip += "Something Wrong! " + e.toString() + "\n";
		}

		return ip;
	}

	public static class NettenResimYukle extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public NettenResimYukle(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	public void UnFocusEditText(EditText mEditText) {
        if (mEditText.isFocused()) {
            mEditText.clearFocus();
            KlavyeKapat();
        }
    }

    public void IntentGetir(String[] Data) {
        switch (Data[0]) {
            case "GaleridenResimGetir":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        activity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Integer.parseInt(Data[1]));
                    else {
                        Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        PickPictureIntent.setType("image/*");
                        activity.startActivityForResult(Intent.createChooser(PickPictureIntent, activity.getString(R.string.bir_fotograf_secin)), Integer.parseInt(Data[2]));
                    }
                } else {
                    Intent PickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    PickPictureIntent.setType("image/*");
                    activity.startActivityForResult(Intent.createChooser(PickPictureIntent, activity.getString(R.string.bir_fotograf_secin)), Integer.parseInt(Data[2]));
                }

                break;
            case "ResimKirp":
                Intent intent = CropImage.activity(Uri.parse(Data[1]))
                        .setFixAspectRatio(true)
                        .getIntent(activity.getBaseContext());
                activity.startActivityForResult(intent, Integer.parseInt(Data[2]));

                break;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File ResimIsleme(CoordinatorLayout mCoordinatorLayout, File SecilenProfilResmiFile, ImageView CImgResim, Uri SecilenResimUri) {
        // Eğer daha önce resim seçildiyse ve dosya var ise, secilen eski resmi siliyoruz.
        if(SecilenProfilResmiFile != null && SecilenProfilResmiFile.exists()) SecilenProfilResmiFile.delete();

        SecilenProfilResmiFile = new File(SecilenResimUri.toString().substring(7));

        if (SecilenProfilResmiFile.exists()) { //Cihazda bu dosya var mı kontrol ediyoruz..
            double SecilenProfilResimBoyutuMB = ((double) SecilenProfilResmiFile.length()/1024)/1024;
            int ProfilResimYuklemeBoyutu = activity.getResources().getInteger(R.integer.ProfilResimYuklemeBoyutu);

            if(SecilenProfilResimBoyutuMB > ProfilResimYuklemeBoyutu) {
                SecilenProfilResmiFile.delete();
                SecilenProfilResmiFile = null;

                CImgResim.setImageDrawable(activity.getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_square));

                StandartSnackBarMsj(mCoordinatorLayout, activity.getString(R.string.buyuk_profil_resim_hatasi, String.valueOf(ProfilResimYuklemeBoyutu)));
            } else CImgResim.setImageURI(SecilenResimUri);
        } else StandartSnackBarMsj(mCoordinatorLayout, activity.getString(R.string.dosya_bulunamadi));

        return SecilenProfilResmiFile;
    }

    public List<SnfUlkeKodlari> UlkeKodlariniGetir(){
		List<SnfUlkeKodlari> UlkeKodlari = new ArrayList<>();
		SnfUlkeKodlari snfUlkeKodlari;

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Afghanistan");
		snfUlkeKodlari.setUlkeKodu("93");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Åland Islands");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Albania");
		snfUlkeKodlari.setUlkeKodu("355");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Algeria");
		snfUlkeKodlari.setUlkeKodu("213");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("American Samoa");
		snfUlkeKodlari.setUlkeKodu("1684");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Andorra");
		snfUlkeKodlari.setUlkeKodu("376");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Angola");
		snfUlkeKodlari.setUlkeKodu("244");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Anguilla");
		snfUlkeKodlari.setUlkeKodu("1264");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Antarctica");
		snfUlkeKodlari.setUlkeKodu("672");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Antigua and Barbuda");
		snfUlkeKodlari.setUlkeKodu("1268");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Argentina");
		snfUlkeKodlari.setUlkeKodu("54");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Armenia");
		snfUlkeKodlari.setUlkeKodu("374");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Aruba");
		snfUlkeKodlari.setUlkeKodu("297");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Australia");
		snfUlkeKodlari.setUlkeKodu("61");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Austria");
		snfUlkeKodlari.setUlkeKodu("43");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Azerbaijan");
		snfUlkeKodlari.setUlkeKodu("994");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bahamas");
		snfUlkeKodlari.setUlkeKodu("1242");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bahrain");
		snfUlkeKodlari.setUlkeKodu("973");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bangladesh");
		snfUlkeKodlari.setUlkeKodu("880");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Barbados");
		snfUlkeKodlari.setUlkeKodu("1246");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Belarus");
		snfUlkeKodlari.setUlkeKodu("375");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Belgium");
		snfUlkeKodlari.setUlkeKodu("32");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Belize");
		snfUlkeKodlari.setUlkeKodu("501");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Benin");
		snfUlkeKodlari.setUlkeKodu("229");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bermuda");
		snfUlkeKodlari.setUlkeKodu("1441");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bhutan");
		snfUlkeKodlari.setUlkeKodu("975");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bolivia");
		snfUlkeKodlari.setUlkeKodu("591");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bonaire, Sint Eustatius and Saba");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bosnia and Herzegovina");
		snfUlkeKodlari.setUlkeKodu("387");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Botswana");
		snfUlkeKodlari.setUlkeKodu("267");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bouvet Island");
		snfUlkeKodlari.setUlkeKodu("44");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Brazil");
		snfUlkeKodlari.setUlkeKodu("55");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("British Indian Ocean Territory");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Brunei Darussalam");
		snfUlkeKodlari.setUlkeKodu("673");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Bulgaria");
		snfUlkeKodlari.setUlkeKodu("359");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Burkina Faso");
		snfUlkeKodlari.setUlkeKodu("226");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Burundi");
		snfUlkeKodlari.setUlkeKodu("257");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cambodia");
		snfUlkeKodlari.setUlkeKodu("855");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cameroon");
		snfUlkeKodlari.setUlkeKodu("237");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Canada");
		snfUlkeKodlari.setUlkeKodu("1");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cape Verde");
		snfUlkeKodlari.setUlkeKodu("238");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cayman Islands");
		snfUlkeKodlari.setUlkeKodu("1345");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Central African Republic");
		snfUlkeKodlari.setUlkeKodu("236");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Chad");
		snfUlkeKodlari.setUlkeKodu("235");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Chile");
		snfUlkeKodlari.setUlkeKodu("56");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("China");
		snfUlkeKodlari.setUlkeKodu("86");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Christmas Island");
		snfUlkeKodlari.setUlkeKodu("61");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cocos (Keeling) Islands");
		snfUlkeKodlari.setUlkeKodu("61");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Colombia");
		snfUlkeKodlari.setUlkeKodu("57");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Comoros");
		snfUlkeKodlari.setUlkeKodu("269");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Congo");
		snfUlkeKodlari.setUlkeKodu("242");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Congo (Democratic Republic of the)");
		snfUlkeKodlari.setUlkeKodu("243");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cook Islands");
		snfUlkeKodlari.setUlkeKodu("682");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Costa Rica");
		snfUlkeKodlari.setUlkeKodu("506");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Croatia (Hrvatska)");
		snfUlkeKodlari.setUlkeKodu("385");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cuba");
		snfUlkeKodlari.setUlkeKodu("53");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Curaçao");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Cyprus");
		snfUlkeKodlari.setUlkeKodu("357");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Czech Republic");
		snfUlkeKodlari.setUlkeKodu("420");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Denmark");
		snfUlkeKodlari.setUlkeKodu("45");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Djibouti");
		snfUlkeKodlari.setUlkeKodu("253");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Dominica");
		snfUlkeKodlari.setUlkeKodu("1767");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Dominican Republic");
		snfUlkeKodlari.setUlkeKodu("1809");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Ecuador");
		snfUlkeKodlari.setUlkeKodu("593");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Egypt");
		snfUlkeKodlari.setUlkeKodu("20");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("El Salvador");
		snfUlkeKodlari.setUlkeKodu("503");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Equatorial Guinea");
		snfUlkeKodlari.setUlkeKodu("240");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Eritrea");
		snfUlkeKodlari.setUlkeKodu("291");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Estonia");
		snfUlkeKodlari.setUlkeKodu("372");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Ethiopia");
		snfUlkeKodlari.setUlkeKodu("251");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Falkland Islands (Malvinas)");
		snfUlkeKodlari.setUlkeKodu("500");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Faroe Islands");
		snfUlkeKodlari.setUlkeKodu("298");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Fiji");
		snfUlkeKodlari.setUlkeKodu("679");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Finland");
		snfUlkeKodlari.setUlkeKodu("358");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("France");
		snfUlkeKodlari.setUlkeKodu("33");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("French Guiana");
		snfUlkeKodlari.setUlkeKodu("594");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("French Polynesia");
		snfUlkeKodlari.setUlkeKodu("689");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("French Southern Territories");
		snfUlkeKodlari.setUlkeKodu("44");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Gabon");
		snfUlkeKodlari.setUlkeKodu("241");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Gambia");
		snfUlkeKodlari.setUlkeKodu("220");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Georgia");
		snfUlkeKodlari.setUlkeKodu("995");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Germany");
		snfUlkeKodlari.setUlkeKodu("49");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Ghana");
		snfUlkeKodlari.setUlkeKodu("233");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Gibraltar");
		snfUlkeKodlari.setUlkeKodu("350");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Greece");
		snfUlkeKodlari.setUlkeKodu("30");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Greenland");
		snfUlkeKodlari.setUlkeKodu("299");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Grenada");
		snfUlkeKodlari.setUlkeKodu("1473");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guadeloupe");
		snfUlkeKodlari.setUlkeKodu("590");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guam");
		snfUlkeKodlari.setUlkeKodu("1671");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guatemala");
		snfUlkeKodlari.setUlkeKodu("502");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guernsey");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guinea");
		snfUlkeKodlari.setUlkeKodu("224");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guinea-Bissau");
		snfUlkeKodlari.setUlkeKodu("245");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Guyana");
		snfUlkeKodlari.setUlkeKodu("592");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Haiti");
		snfUlkeKodlari.setUlkeKodu("509");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Heard Island and McDonald Islands");
		snfUlkeKodlari.setUlkeKodu("44");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Honduras");
		snfUlkeKodlari.setUlkeKodu("504");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Hong Kong");
		snfUlkeKodlari.setUlkeKodu("852");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Hungary");
		snfUlkeKodlari.setUlkeKodu("36");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Iceland");
		snfUlkeKodlari.setUlkeKodu("354");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("India");
		snfUlkeKodlari.setUlkeKodu("91");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Indonesia");
		snfUlkeKodlari.setUlkeKodu("62");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Iran (Islamic Republic of)");
		snfUlkeKodlari.setUlkeKodu("98");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Iraq");
		snfUlkeKodlari.setUlkeKodu("964");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Ireland");
		snfUlkeKodlari.setUlkeKodu("353");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Isle of Man");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Israel");
		snfUlkeKodlari.setUlkeKodu("972");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Italy");
		snfUlkeKodlari.setUlkeKodu("39");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Ivory Coast");
		snfUlkeKodlari.setUlkeKodu("225");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Jamaica");
		snfUlkeKodlari.setUlkeKodu("1876");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Japan");
		snfUlkeKodlari.setUlkeKodu("81");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Jersey");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Jordan");
		snfUlkeKodlari.setUlkeKodu("962");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Kazakhstan");
		snfUlkeKodlari.setUlkeKodu("7");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Kenya");
		snfUlkeKodlari.setUlkeKodu("254");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Kiribati");
		snfUlkeKodlari.setUlkeKodu("686");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Korea (Democratic People's Republic of)");
		snfUlkeKodlari.setUlkeKodu("850");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Korea (Republic of)");
		snfUlkeKodlari.setUlkeKodu("82");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Kuwait");
		snfUlkeKodlari.setUlkeKodu("965");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Kyrgyzstan");
		snfUlkeKodlari.setUlkeKodu("996");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Lao People's Democratic Republic");
		snfUlkeKodlari.setUlkeKodu("856");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Latvia");
		snfUlkeKodlari.setUlkeKodu("371");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Lebanon");
		snfUlkeKodlari.setUlkeKodu("961");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Lesotho");
		snfUlkeKodlari.setUlkeKodu("266");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Liberia");
		snfUlkeKodlari.setUlkeKodu("231");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Libya");
		snfUlkeKodlari.setUlkeKodu("218");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Liechtenstein");
		snfUlkeKodlari.setUlkeKodu("423");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Lithuania");
		snfUlkeKodlari.setUlkeKodu("370");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Luxembourg");
		snfUlkeKodlari.setUlkeKodu("352");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Macau");
		snfUlkeKodlari.setUlkeKodu("853");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Macedonia");
		snfUlkeKodlari.setUlkeKodu("389");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Madagascar");
		snfUlkeKodlari.setUlkeKodu("261");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Malawi");
		snfUlkeKodlari.setUlkeKodu("265");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Malaysia");
		snfUlkeKodlari.setUlkeKodu("60");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Maldives");
		snfUlkeKodlari.setUlkeKodu("960");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mali");
		snfUlkeKodlari.setUlkeKodu("223");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Malta");
		snfUlkeKodlari.setUlkeKodu("356");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Marshall Islands");
		snfUlkeKodlari.setUlkeKodu("692");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Martinique");
		snfUlkeKodlari.setUlkeKodu("596");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mauritania");
		snfUlkeKodlari.setUlkeKodu("222");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mauritius");
		snfUlkeKodlari.setUlkeKodu("230");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mayotte");
		snfUlkeKodlari.setUlkeKodu("262");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mexico");
		snfUlkeKodlari.setUlkeKodu("52");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Micronesia (Federated States of)");
		snfUlkeKodlari.setUlkeKodu("691");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Moldova (Republic of)");
		snfUlkeKodlari.setUlkeKodu("373");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Monaco");
		snfUlkeKodlari.setUlkeKodu("377");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mongolia");
		snfUlkeKodlari.setUlkeKodu("976");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Montenegro");
		snfUlkeKodlari.setUlkeKodu("382");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Montserrat");
		snfUlkeKodlari.setUlkeKodu("1664");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Morocco");
		snfUlkeKodlari.setUlkeKodu("212");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Mozambique");
		snfUlkeKodlari.setUlkeKodu("258");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Myanmar");
		snfUlkeKodlari.setUlkeKodu("95");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Namibia");
		snfUlkeKodlari.setUlkeKodu("264");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Nauru");
		snfUlkeKodlari.setUlkeKodu("674");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Nepal");
		snfUlkeKodlari.setUlkeKodu("977");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Netherlands");
		snfUlkeKodlari.setUlkeKodu("31");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("New Caledonia");
		snfUlkeKodlari.setUlkeKodu("687");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("New Zealand");
		snfUlkeKodlari.setUlkeKodu("64");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Nicaragua");
		snfUlkeKodlari.setUlkeKodu("505");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Niger");
		snfUlkeKodlari.setUlkeKodu("227");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Nigeria");
		snfUlkeKodlari.setUlkeKodu("234");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Niue");
		snfUlkeKodlari.setUlkeKodu("683");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Norfolk Island");
		snfUlkeKodlari.setUlkeKodu("672");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Northern Mariana Islands");
		snfUlkeKodlari.setUlkeKodu("1670");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Norway");
		snfUlkeKodlari.setUlkeKodu("47");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Oman");
		snfUlkeKodlari.setUlkeKodu("968");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Pakistan");
		snfUlkeKodlari.setUlkeKodu("92");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Palau");
		snfUlkeKodlari.setUlkeKodu("680");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Palestine, State of");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Panama");
		snfUlkeKodlari.setUlkeKodu("507");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Papua New Guinea");
		snfUlkeKodlari.setUlkeKodu("675");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Paraguay");
		snfUlkeKodlari.setUlkeKodu("595");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Peru");
		snfUlkeKodlari.setUlkeKodu("51");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Philippines");
		snfUlkeKodlari.setUlkeKodu("63");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Pitcairn");
		snfUlkeKodlari.setUlkeKodu("870");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Poland");
		snfUlkeKodlari.setUlkeKodu("48");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Portugal");
		snfUlkeKodlari.setUlkeKodu("351");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Puerto Rico");
		snfUlkeKodlari.setUlkeKodu("1");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Qatar");
		snfUlkeKodlari.setUlkeKodu("974");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Reunion");
		snfUlkeKodlari.setUlkeKodu("262");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Romania");
		snfUlkeKodlari.setUlkeKodu("40");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Russian Federation");
		snfUlkeKodlari.setUlkeKodu("7");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Rwanda");
		snfUlkeKodlari.setUlkeKodu("250");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Barthélemy");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Helena, Ascension and Tristan da Cunha");
		snfUlkeKodlari.setUlkeKodu("290");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Kitts and Nevis");
		snfUlkeKodlari.setUlkeKodu("1869");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Lucia");
		snfUlkeKodlari.setUlkeKodu("1758");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Martin (French part)");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Pierre and Miquelon");
		snfUlkeKodlari.setUlkeKodu("508");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saint Vincent and the Grenadines");
		snfUlkeKodlari.setUlkeKodu("1784");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Samoa");
		snfUlkeKodlari.setUlkeKodu("685");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("San Marino");
		snfUlkeKodlari.setUlkeKodu("378");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Sao Tome and Principe");
		snfUlkeKodlari.setUlkeKodu("239");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Saudi Arabia");
		snfUlkeKodlari.setUlkeKodu("966");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Senegal");
		snfUlkeKodlari.setUlkeKodu("221");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Serbia");
		snfUlkeKodlari.setUlkeKodu("381");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Seychelles");
		snfUlkeKodlari.setUlkeKodu("248");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Sierra Leone");
		snfUlkeKodlari.setUlkeKodu("232");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Singapore");
		snfUlkeKodlari.setUlkeKodu("65");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Sint Maarten (Dutch part)");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Slovakia");
		snfUlkeKodlari.setUlkeKodu("421");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Slovenia");
		snfUlkeKodlari.setUlkeKodu("386");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Solomon Islands");
		snfUlkeKodlari.setUlkeKodu("677");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Somalia");
		snfUlkeKodlari.setUlkeKodu("252");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("South Africa");
		snfUlkeKodlari.setUlkeKodu("27");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("South Georgia and the South Sandwich Islands");
		snfUlkeKodlari.setUlkeKodu("44");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("South Sudan");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Spain");
		snfUlkeKodlari.setUlkeKodu("34");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Sri Lanka");
		snfUlkeKodlari.setUlkeKodu("94");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Sudan");
		snfUlkeKodlari.setUlkeKodu("249");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Suriname");
		snfUlkeKodlari.setUlkeKodu("597");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Svalbard and Jan Mayen");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Swaziland");
		snfUlkeKodlari.setUlkeKodu("268");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Sweden");
		snfUlkeKodlari.setUlkeKodu("46");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Switzerland");
		snfUlkeKodlari.setUlkeKodu("41");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Syrian Arab Republic");
		snfUlkeKodlari.setUlkeKodu("963");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Taiwan");
		snfUlkeKodlari.setUlkeKodu("886");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Tajikistan");
		snfUlkeKodlari.setUlkeKodu("992");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Tanzania, United Republic of");
		snfUlkeKodlari.setUlkeKodu("255");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Thailand");
		snfUlkeKodlari.setUlkeKodu("66");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Timor-Leste");
		snfUlkeKodlari.setUlkeKodu("670");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Togo");
		snfUlkeKodlari.setUlkeKodu("228");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Tokelau");
		snfUlkeKodlari.setUlkeKodu("690");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Tonga");
		snfUlkeKodlari.setUlkeKodu("676");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Trinidad and Tobago");
		snfUlkeKodlari.setUlkeKodu("1868");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Tunisia");
		snfUlkeKodlari.setUlkeKodu("216");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Turkey");
		snfUlkeKodlari.setUlkeKodu("90");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Turkmenistan");
		snfUlkeKodlari.setUlkeKodu("993");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Turks and Caicos Islands");
		snfUlkeKodlari.setUlkeKodu("1649");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Tuvalu");
		snfUlkeKodlari.setUlkeKodu("688");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Uganda");
		snfUlkeKodlari.setUlkeKodu("256");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Ukraine");
		snfUlkeKodlari.setUlkeKodu("380");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("United Arab Emirates");
		snfUlkeKodlari.setUlkeKodu("971");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("United Kingdom");
		snfUlkeKodlari.setUlkeKodu("44");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("United States Minor Outlying Islands");
		snfUlkeKodlari.setUlkeKodu("44");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("United States of America");
		snfUlkeKodlari.setUlkeKodu("1");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Uruguay");
		snfUlkeKodlari.setUlkeKodu("598");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Uzbekistan");
		snfUlkeKodlari.setUlkeKodu("998");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Vanuatu");
		snfUlkeKodlari.setUlkeKodu("678");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Vatican City State");
		snfUlkeKodlari.setUlkeKodu("39");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Venezuela");
		snfUlkeKodlari.setUlkeKodu("58");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Viet Nam");
		snfUlkeKodlari.setUlkeKodu("84");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Virgin Islands (British)");
		snfUlkeKodlari.setUlkeKodu("1284");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Virgin Islands (U.S.)");
		snfUlkeKodlari.setUlkeKodu("1340");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Wallis and Futuna");
		snfUlkeKodlari.setUlkeKodu("681");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Western Sahara");
		snfUlkeKodlari.setUlkeKodu("0");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Yemen");
		snfUlkeKodlari.setUlkeKodu("967");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Zambia");
		snfUlkeKodlari.setUlkeKodu("260");
		UlkeKodlari.add(snfUlkeKodlari);

		snfUlkeKodlari = new SnfUlkeKodlari();
		snfUlkeKodlari.setUlkeAdi("Zimbabwe");
		snfUlkeKodlari.setUlkeKodu("263");
		UlkeKodlari.add(snfUlkeKodlari);

		return UlkeKodlari;
	}

	public String EPostaSifrele(String EPosta) {
    	return EPosta.substring(0,1) + "*******" + EPosta.substring(EPosta.indexOf("@") - 1, EPosta.length());
	}

	public String CepTelefonSifrele(String TelKodu, String CepTelefon) {
		return TelKodu + " " +  CepTelefon.substring(0,3) + " *** ** " + CepTelefon.substring(CepTelefon.length() - 2, CepTelefon.length());
	}

	public String ParolaUret(int Uzunluk, Boolean Sayi, Boolean Kucukharf, Boolean Buyukharf, Boolean Semboller) {
    	String Karakterler = "";

    	if(Sayi) Karakterler = Karakterler.concat("1234567890");
		if(Kucukharf) Karakterler = Karakterler.concat("abcdefghijklmnopqrstuvwxyz");
		if(Buyukharf) Karakterler = Karakterler.concat("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		if(Semboller) Karakterler = Karakterler.concat("/*-+!%&()=_#$½{[]}");

		return RandomStringUtils.random(Uzunluk, Karakterler);
	}

	// PHP İŞLEMLERİ - Retrofit 2

	public void SistemDurumKontrol() {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfSistemDurum> snfSistemDurumCall = retrofitInterface.SistemDurumKontrol();
		snfSistemDurumCall.enqueue(new Callback<SnfSistemDurum>() {
			@Override
			public void onResponse(Call<SnfSistemDurum> call, Response<SnfSistemDurum> response) {
				if(response.isSuccessful()) {
					SnfSistemDurum snfSistemDurum = response.body();

					if(!snfSistemDurum.getHata()) { // getHata'nin false olması durumu hata yok demektir..
						if(snfSistemDurum.getDurum()) AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SistemDurumKontrol\", \"Durum\":true}");
						else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SistemDurumKontrol\", \"Durum\":false, \"Baslik\":\"" + snfSistemDurum.getBaslik() + "\", \"Icerik\":\"" + snfSistemDurum.getIcerik() + "\"}");
					} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SistemDurumKontrol\", \"Durum\":false, \"Baslik\":\"" + activity.getString(R.string.hata) + "\", \"Icerik\":\"" + activity.getString(R.string.islem_yapilirken_bir_hata_olustu) + "\"}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SistemDurumKontrol\", \"Durum\":false, \"Baslik\":\"" + activity.getString(R.string.hata) + "\", \"Icerik\":\"" + response.code() + " " + response.message() + "\"}");
			}

			@Override
			public void onFailure(Call<SnfSistemDurum> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SistemDurumKontrol\", \"Durum\":false, \"Baslik\":\"" + activity.getString(R.string.hata) + "\", \"Icerik\":\"" + activity.getString(R.string.islem_yapilirken_bir_hata_olustu) + "\"}");
			}
		});
	}

	public void EPostaGonder(String mEPosta, String mAdSoyad, String mBaslik, String mIcerik) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfIslemSonuc> snfIslemSonucCall = retrofitInterface.EPostaGonder(mEPosta, mAdSoyad, mBaslik, mIcerik);
        snfIslemSonucCall.enqueue(new Callback<SnfIslemSonuc>() {
			@Override
			public void onResponse(Call<SnfIslemSonuc> call, Response<SnfIslemSonuc> response) {
				if(response.isSuccessful()) {
					SnfIslemSonuc snfIslemSonuc = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfIslemSonuc.getHata()) AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"EPostaGonder\", \"Sonuc\":" + snfIslemSonuc.getSonuc() + "}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"EPostaGonder\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"EPostaGonder\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfIslemSonuc> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"EPostaGonder\", \"Sonuc\":false}");
			}
		});
	}

	public void SMSGonder(String mTelKodu, String mCepTelefon, String mMesaj) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfIslemSonuc> snfIslemSonucCall = retrofitInterface.SMSGonder(mTelKodu, mCepTelefon, mMesaj);
		snfIslemSonucCall.enqueue(new Callback<SnfIslemSonuc>() {
			@Override
			public void onResponse(Call<SnfIslemSonuc> call, Response<SnfIslemSonuc> response) {
				if(response.isSuccessful()) {
					SnfIslemSonuc snfIslemSonuc = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfIslemSonuc.getHata()) AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SMSGonder\", \"Sonuc\":" + snfIslemSonuc.getSonuc() + "}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SMSGonder\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SMSGonder\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfIslemSonuc> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SMSGonder\", \"Sonuc\":false}");
			}
		});
	}

	public void HesapEkle(String mFirebaseToken, String mOSID, String mOSVersiyon, String mAdSoyad, String mDogumTarih, Boolean mProfilResmiVarmi, String mEPosta, String mParola, String mParolaSHA1, String mKullaniciAdi, String mUygulamaVersiyon) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfHesapEkle> snfHesapEkleCall = retrofitInterface.HesapEkle(mFirebaseToken, mOSID, mOSVersiyon, mAdSoyad, mDogumTarih, mProfilResmiVarmi, mEPosta, mParola, mParolaSHA1, mKullaniciAdi, mUygulamaVersiyon);
		snfHesapEkleCall.enqueue(new Callback<SnfHesapEkle>() {
			@Override
			public void onResponse(Call<SnfHesapEkle> call, Response<SnfHesapEkle> response) {
				if(response.isSuccessful()) {
					SnfHesapEkle snfHesapEkle = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfHesapEkle.getHata()) AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapEkle\", \"Sonuc\":" + snfHesapEkle.getSonuc() + ", \"Aciklama\":\"" + snfHesapEkle.getAciklama() + "\", \"EklenenHesapID\":\"" + snfHesapEkle.getEklenenHesapID() + "\"}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapEkle\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapEkle\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfHesapEkle> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapEkle\", \"Sonuc\":false}");
			}
		});
	}

	public void HesapGirisYap(final String mGirisTipi, String mFirebaseToken, String mOSID, String mOSVersiyon, String mUygulamaVersiyon, String mEPostaVeyaKullaniciAdi, String mParolaSHA1, String mSosyalHesapID, String mAdSoyad) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfHesapGirisYap> snfHesapGirisYapCall = retrofitInterface.HesapGirisYap(mGirisTipi, mFirebaseToken, mOSID, mOSVersiyon, mUygulamaVersiyon, mEPostaVeyaKullaniciAdi, mParolaSHA1, mSosyalHesapID, mAdSoyad);
		snfHesapGirisYapCall.enqueue(new Callback<SnfHesapGirisYap>() {
			@Override
			public void onResponse(Call<SnfHesapGirisYap> call, Response<SnfHesapGirisYap> response) {
				if(response.isSuccessful()) {
					SnfHesapGirisYap snfHesapGirisYap = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfHesapGirisYap.getHata()) AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapGirisYap\", \"Sonuc\":" + snfHesapGirisYap.getSonuc() + ", \"HesapID\":\"" + snfHesapGirisYap.getHesapID() + "\", \"HesapEPosta\":\"" + snfHesapGirisYap.getHesapEPosta() + "\", \"HesapDurum\":\"" + snfHesapGirisYap.getHesapDurum() + "\", \"HesapParolaSHA1\":\"" + snfHesapGirisYap.getHesapParolaSHA1() + "\", \"HesapDurumBilgi\":\"" + snfHesapGirisYap.getHesapDurumBilgi() + "\"}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapGirisYap\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapGirisYap\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfHesapGirisYap> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"Giris_Yap\", \"Sonuc\":false}");
			}
		});
	}

	public void HesapBilgiGetir(final Fragment fragment, String mTelKodu, String mEPostaKullaniciAdiTelefon) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfHesapBilgiGetir> snfHesapBilgiGetirCall = retrofitInterface.HesapBilgiGetir(mTelKodu, mEPostaKullaniciAdiTelefon);
		snfHesapBilgiGetirCall.enqueue(new Callback<SnfHesapBilgiGetir>() {
			@Override
			public void onResponse(Call<SnfHesapBilgiGetir> call, Response<SnfHesapBilgiGetir> response) {
				if(response.isSuccessful()) {
					SnfHesapBilgiGetir snfHesapBilgiGetir = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfHesapBilgiGetir.getHata())
						AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGetir\", " +
								((fragment != null) ? "\"Fragment\":\"" + fragment.getTag() + "\", ":"\"Fragment\":\"\", ") +
								"\"Sonuc\":" + snfHesapBilgiGetir.getSonuc() + ", " +
								"\"HesapID\":\"" + snfHesapBilgiGetir.getHesapID() + "\", " +
								"\"FacebookID\":\"" + snfHesapBilgiGetir.getFacebookID() + "\", " +
								"\"GoogleID\":\"" + snfHesapBilgiGetir.getGoogleID() + "\", " +
								"\"AdSoyad\":\"" + snfHesapBilgiGetir.getAdSoyad() + "\", " +
								"\"DogumTarih\":\"" + snfHesapBilgiGetir.getDogumTarih() + "\", " +
								"\"ResimURL\":\"" + snfHesapBilgiGetir.getResimURL() + "\", " +
								"\"EPosta\":\"" + snfHesapBilgiGetir.getEPosta() + "\", " +
								"\"EPostaOnay\":\"" + snfHesapBilgiGetir.getEPostaOnay() + "\", " +
								"\"Parola\":\"" + snfHesapBilgiGetir.getParola() + "\", " +
								"\"ParolaSHA1\":\"" + snfHesapBilgiGetir.getParolaSHA1() + "\", " +
								"\"KullaniciAdi\":\"" + snfHesapBilgiGetir.getKullaniciAdi() + "\", " +
								"\"TelKodu\":\"" + snfHesapBilgiGetir.getTelKodu() + "\", " +
								"\"CepTelefon\":\"" + snfHesapBilgiGetir.getCepTelefon() + "\", " +
								"\"CepTelefonOnay\":\"" + snfHesapBilgiGetir.getCepTelefonOnay() + "\", " +
								"\"KayitTarih\":\"" + snfHesapBilgiGetir.getKayitTarih() + "\", " +
								"\"SonOturumTarih\":\"" + snfHesapBilgiGetir.getSonOturumTarih() + "\", " +
								"\"HesapDurum\":\"" + snfHesapBilgiGetir.getHesapDurum() + "\", " +
								"\"HesapDurumBilgi\":\"" + snfHesapBilgiGetir.getHesapDurumBilgi() + "\"}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGetir\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGetir\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfHesapBilgiGetir> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGetir\", \"Sonuc\":false}");
			}
		});
	}

	public void HesapBilgiGuncelle(String mFirebaseToken, String mOSID, String mOSVersiyon, String mAdSoyad, String mDogumTarih, String mResimURL, String mEPosta, String mParola, String mParolaSHA1, String mKullaniciAdi, String mTelKodu, String mCepTelefon, String mUygulamaVersiyon) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfIslemSonuc> snfIslemSonucCall = retrofitInterface.HesapBilgiGuncelle(mFirebaseToken, mOSID, mOSVersiyon, mAdSoyad, mDogumTarih, mResimURL, mEPosta, mParola, mParolaSHA1, mKullaniciAdi, mTelKodu, mCepTelefon, mUygulamaVersiyon);
		snfIslemSonucCall.enqueue(new Callback<SnfIslemSonuc>() {
			@Override
			public void onResponse(Call<SnfIslemSonuc> call, Response<SnfIslemSonuc> response) {
				if(response.isSuccessful()) {
					SnfIslemSonuc snfIslemSonuc = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfIslemSonuc.getHata())
						AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGuncelle\", \"Sonuc\":" + snfIslemSonuc.getSonuc() + "}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGuncelle\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGuncelle\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfIslemSonuc> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapBilgiGuncelle\", \"Sonuc\":false}");
			}
		});
	}

	/*public void HesapParolaDegistir(String mEPosta, String mParola, String mParolaSHA1) {
		RetrofitInterface retrofitInterface = RetrofitServiceGenerator.createService(activity, RetrofitInterface.class);
		final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		Call<SnfIslemSonuc> snfIslemSonucCall = retrofitInterface.HesapParolaDegistir(mEPosta, mParola, mParolaSHA1);
		snfIslemSonucCall.enqueue(new Callback<SnfIslemSonuc>() {
			@Override
			public void onResponse(Call<SnfIslemSonuc> call, Response<SnfIslemSonuc> response) {
				if(response.isSuccessful()) {
					SnfIslemSonuc snfIslemSonuc = response.body();

					// getHata'nin false olması durumu hata yok demektir..
					if(!snfIslemSonuc.getHata())
						AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapParolaDegistir\", \"Sonuc\":" + snfIslemSonuc.getSonuc() + "}");
					else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapParolaDegistir\", \"Sonuc\":false}");
				} else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapParolaDegistir\", \"Sonuc\":false}");
			}

			@Override
			public void onFailure(Call<SnfIslemSonuc> call, Throwable t) {
				AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"HesapParolaDegistir\", \"Sonuc\":false}");
			}
		});
	}*/



















	public void FirebaseMesajGonder(String FirebaseToken, String Icerik) {
		new FirebaseMesajGonder().execute(FirebaseToken, Icerik);
	}

	private class FirebaseMesajGonder extends AsyncTask<String, String, String> {
		String FirebaseToken, Icerik;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... parametre) {
			FirebaseToken = String.valueOf(parametre[0]);
			Icerik = String.valueOf(parametre[1]);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("firebasetoken", FirebaseToken));
			nameValuePairs.add(new BasicNameValuePair("icerik", Icerik));
			String sonuc = null;

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(PHPFirebaseNotify);
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

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						Log.i("Firebase", "Mesaj gönderildi..");

						break;
					case 0:
						Log.i("Firebase", "Mesaj gönderilemedi..");

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
		}
	}

	public void YeniGuncellemeKontrol() {
		if (InternetErisimKontrolu()) new YeniGuncellemeKontrol().execute();
	}

	private class YeniGuncellemeKontrol extends AsyncTask<String, String, String> {
		Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... parametre) {
			String YeniVersiyon = "";

			try {
				YeniVersiyon = Jsoup.connect("https://play.google.com/store/apps/details?id=" + activity.getPackageName())
						.timeout(30000)
						.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
						.referrer("http://www.google.com")
						.get()
						.select("div[itemprop=softwareVersion]")
						.first()
						.ownText();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return YeniVersiyon;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			int YeniVersiyonCode, GecerliVersiyonCode = 0;
			String YeniVersiyonAdi, GecerliVersiyonAdi = null;

			if(Sonuc != null && !Sonuc.equals("")) {
				YeniVersiyonCode = Integer.parseInt(Sonuc.replace(".", ""));
				YeniVersiyonAdi = Sonuc;

				try {
					GecerliVersiyonCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode + 100;
					GecerliVersiyonAdi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}

				if(YeniVersiyonCode > GecerliVersiyonCode){
					AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"Guncelleme\", \"Durum\":true, \"YeniVersiyonAdi\":\"" + YeniVersiyonAdi + "\", \"GecerliVersiyonAdi\":\"" + GecerliVersiyonAdi + "\"}");
				} else
					AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"Guncelleme\", \"Durum\":false}");
			}
		}

		@Override
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
		}
	}

	public void YeniSurumYeniliklerDialog(int GecerliVersiyonCode, String GecerliVersiyonAdi) {
		sharedPref = activity.getSharedPreferences(PrefAdi, MODE_PRIVATE);

		if (GecerliVersiyonCode > sharedPref.getInt("prefEskiVersiyonCode", 1)) {
			Typeface YaziFontu = FontGetir(activity, "anivers_regular");
			LayoutInflater inflater = activity.getLayoutInflater();
			View ViewDialogContent = inflater.inflate(R.layout.dialog_uygulama_yenilikler, null);

			ADDialog = CustomAlertDialog(activity, R.mipmap.ic_launcher, activity.getString(R.string.yenilikler), ViewDialogContent, activity.getString(R.string.tamam));
			ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

			TextView Dialog_lblVersiyonNo = ViewDialogContent.findViewById(R.id.Dialog_lblVersiyonNo);
			Dialog_lblVersiyonNo.setTypeface(YaziFontu);
			Dialog_lblVersiyonNo.setText(String.valueOf("v").concat(GecerliVersiyonAdi));

			TextView Dialog_lblYenilikler = ViewDialogContent.findViewById(R.id.Dialog_lblYenilikler);
			Dialog_lblYenilikler.setTypeface(YaziFontu);

			TextView Dialog_lblYenilikler_Icerik = ViewDialogContent.findViewById(R.id.Dialog_lblYenilikler_Icerik);
			Dialog_lblYenilikler_Icerik.setTypeface(YaziFontu);
			Dialog_lblYenilikler_Icerik.setText(activity.getString(R.string.yenilikler_icerik));
			Dialog_lblYenilikler_Icerik.setMovementMethod(ScrollingMovementMethod.getInstance());

			ADDialog.show();

			ADDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ADDialog.cancel();
				}
			});

			sharedPrefEditor = sharedPref.edit();
			sharedPrefEditor.putInt("prefEskiVersiyonCode", GecerliVersiyonCode);
			sharedPrefEditor.apply();
		}
	}

	public void Duyuru_Reklam_Goster() {
		if(InternetErisimKontrolu()) new Duyuru_Reklam_Goster().execute();
	}

	private class Duyuru_Reklam_Goster extends AsyncTask<String, String, String> {
		Bitmap ResimBitmap = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... parametre) {
			String sonuc = null;

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(ReklamDuyuru);
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

				if(JSONGelenVeri.getString("Tip").equals("Resim")) {
					URL url = new URL(JSONGelenVeri.getString("Icerik"));
					HttpURLConnection.setFollowRedirects(false);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("HEAD");

					if((con.getResponseCode() == HttpURLConnection.HTTP_OK)){
						ResimBitmap = BitmapFactory.decodeStream((InputStream)new URL(JSONGelenVeri.getString("Icerik")).getContent());
					} else ResimBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.banner_trans);
				}
			} catch (IOException  | JSONException e) {
				e.printStackTrace();
			}

			return sonuc;
		}

		@SuppressLint("InflateParams")
		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				final JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				if(JSONGelenVeri.getBoolean("Durum")) {
					DismissAlertDialog(ADDialog);

					sharedPref = activity.getSharedPreferences(PrefAdi, MODE_PRIVATE);

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
					Date ReklamDuyuruTarih1 = format.parse(sharedPref.getString("ReklamDuyuruTarih", "1990-01-01 00:00:00"));
					Date ReklamDuyuruTarih2 = format.parse(JSONGelenVeri.getString("Tarih"));

					if (ReklamDuyuruTarih2.after(ReklamDuyuruTarih1)) {
						Typeface YaziFontu = FontGetir(activity, "anivers_regular");
						LayoutInflater inflater = activity.getLayoutInflater();
						View ViewDialogContent = null;
						final Boolean[] BirDahaGostermeSeciliMi = {false};
						CheckBox Dialog_chkBirDahaGosterme;

						switch (JSONGelenVeri.getString("Tip")) {
							case "Yazi":
								ViewDialogContent = inflater.inflate(R.layout.dialog_reklam_duyuru_yazi, null);

								TextView Dialog_lblVersiyonNo = ViewDialogContent.findViewById(R.id.Dialog_lblVersiyonNo);
								Dialog_lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
								Dialog_lblVersiyonNo.setText(String.valueOf("v").concat(activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName));

								TextView Dialog_lblDuyuru_Reklam_Icerik = ViewDialogContent.findViewById(R.id.Dialog_lblDuyuru_Reklam_Icerik);
								Dialog_lblDuyuru_Reklam_Icerik.setTypeface(YaziFontu);
								Dialog_lblDuyuru_Reklam_Icerik.setText(new SpannableString(Html.fromHtml(JSONGelenVeri.getString("Icerik"))));
								Dialog_lblDuyuru_Reklam_Icerik.setMovementMethod(ScrollingMovementMethod.getInstance());

								/*final WebView Dialog_WebDuyuru_Reklam_Icerik  = (WebView) ViewDialogContent.findViewById(R.id.Dialog_WebDuyuru_Reklam_Icerik);
								Dialog_WebDuyuru_Reklam_Icerik.setBackgroundColor(Color.TRANSPARENT);
								Dialog_WebDuyuru_Reklam_Icerik.getSettings().setJavaScriptEnabled(true);
								Dialog_WebDuyuru_Reklam_Icerik.getSettings().setBuiltInZoomControls(false);
								Dialog_WebDuyuru_Reklam_Icerik.getSettings().setDisplayZoomControls(false);

								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {
											Dialog_WebDuyuru_Reklam_Icerik.loadDataWithBaseURL(null, "<!DOCTYPE html><body>" + JSONGelenVeri.getString("Icerik") + "</body></html>", "text/html", "utf-8", null);
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});*/

								Dialog_chkBirDahaGosterme = ViewDialogContent.findViewById(R.id.Dialog_chkBirDahaGosterme);
								Dialog_chkBirDahaGosterme.setTypeface(YaziFontu);
								Dialog_chkBirDahaGosterme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
										BirDahaGostermeSeciliMi[0] = isChecked;
									}
								});

								break;
							case "Resim":
								ViewDialogContent = inflater.inflate(R.layout.dialog_reklam_duyuru_resim, null);

								ImageView Dialog_ImgResim = ViewDialogContent.findViewById(R.id.Dialog_ImgResim);
								Dialog_ImgResim.setImageBitmap(ResimBitmap);

								Dialog_chkBirDahaGosterme = ViewDialogContent.findViewById(R.id.Dialog_chkBirDahaGosterme);
								Dialog_chkBirDahaGosterme.setTypeface(YaziFontu);
								Dialog_chkBirDahaGosterme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
										BirDahaGostermeSeciliMi[0] = isChecked;
									}
								});
								break;
						}

						ADDialog = CustomAlertDialog(activity, R.mipmap.ic_launcher, JSONGelenVeri.getString("Baslik"), ViewDialogContent, activity.getString(R.string.tamam));
						ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog.show();

						ADDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								try {
									if (BirDahaGostermeSeciliMi[0]) {
										sharedPrefEditor = sharedPref.edit();
										sharedPrefEditor.putString("ReklamDuyuruTarih", JSONGelenVeri.getString("Tarih"));
										sharedPrefEditor.apply();
									}

									ADDialog.cancel();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			} catch (PackageManager.NameNotFoundException | ParseException | JSONException e) {
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
		}
	}

	@SuppressWarnings("static-access")
	public String getIpAddr(Activity activity) {
		WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(activity.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();

		return String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
	}

	@SuppressWarnings("static-access")
	public String getWifiAdi(Activity activity) {
		WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(activity.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		return wifiInfo.getSSID();
	}

	public Boolean TabletMiMobilMi(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public void DismissProgressDialog(ProgressDialog mPDDialog) {
		try {
			if (mPDDialog != null && mPDDialog.isShowing()) {
				mPDDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DismissAlertDialog(AlertDialog mADDialog) {
		try {
			if (mADDialog != null && mADDialog.isShowing()) {
				mADDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean ProgressDialogisShowing(ProgressDialog mProgressDialog) {
		return mProgressDialog != null && mProgressDialog.isShowing();
	}

	public boolean AlertDialogisShowing(AlertDialog mAlertDialog) {
		return mAlertDialog != null && mAlertDialog.isShowing();
	}

	@SuppressLint("DefaultLocale")
	public String DosyaUzantiBul(String DosyaAdi) {
		String Uzanti = null;
		int i = DosyaAdi.lastIndexOf('.');

		if (i > 0 && i < DosyaAdi.length() - 1) {
			Uzanti = DosyaAdi.substring(i + 1).toLowerCase();
		}
		return Uzanti;
	}

	public void DosyaKopyala(String SecilenDosya, String HedefDizin) throws IOException {
		InputStream in = new FileInputStream(new File(SecilenDosya));
		OutputStream out = new FileOutputStream(new File(HedefDizin));

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		in.close();
		out.close();
	}

	public void DosyayaYaz(String data, File Dosya) {
		BufferedWriter out;

		try {
			FileWriter fileWriter = new FileWriter(Dosya);
			out = new BufferedWriter(fileWriter);
			out.write(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean copyFile(File source, File dest) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(dest, false));

			byte[] buf = new byte[1024];
			bis.read(buf);

			do {
				bos.write(buf);
			} while(bis.read(buf) != -1);
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}

	public static boolean moveFile(File source, File dest) {
		return copyFile(source, dest) && source.delete();
	}

	public static boolean isSDMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public String openHTMLString(Context context, int id, Boolean ContentSayfaMi) {
		InputStream is = context.getResources().openRawResource(id);

		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String line;
		StringBuilder html = new StringBuilder();

		try {
			while ((line = reader.readLine()) != null) {
				html.append(line);
				if (!ContentSayfaMi) html.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//String Duzenlenmis_HTML = html.toString().replace("\t","").replace("\t\t","").replace("\t\t\t","").replace("\t\t\t\t","").replace("\t\t\t\t\t","").replace("\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t\t\t\t\t\t","").replace("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t","");

		return html.toString();
	}

	public String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;

				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return writer.toString();
		} else {
			return "";
		}
	}

	public String ImageToString(Context context, int ResimID, String Tip) { //Bu fonksiyon belirtilen resmi BASE64 String'e dönüştürür..
		Options options = new Options();
		options.inScaled = false; // Bu ayarı belirtilen resmin boyutunu koruması için yazdık.

		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), ResimID, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (Tip.equals("jpg")) bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		else if (Tip.equals("png")) bm.compress(Bitmap.CompressFormat.PNG,  100, baos);
		else if (Tip.equals("ico")) bm.compress(Bitmap.CompressFormat.PNG,  100, baos);

		byte[] b = baos.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

		return encodedImage;
	}

	public Bitmap StringToImage(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);

			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

			return bitmap;
		} catch(Exception e){
			e.getMessage();
			return null;
		}
	}

	public boolean EditTextisMatching(EditText txt1, EditText txt2) {
		return txt1.getText().toString().equals(txt2.getText().toString());
	}

	public boolean EditTextKarakterKontrolMIN(String txt, int MinKarakterSayisi) {
		return txt.length() < MinKarakterSayisi;
	}

	public boolean EditTextKarakterKontrolMAX(String txt, int MaxKarakterSayisi) {
		return txt.length() > MaxKarakterSayisi;
	}

	public boolean EditTextKarakterKontrol(String txt, int MinKarakterSayisi, int MaxKarakterSayisi) {
		return txt.length() < MinKarakterSayisi || txt.length() > MaxKarakterSayisi;
	}

	public boolean isValid(String txt, String ValidTipi) {
		/* Orjinal: ((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})
			(					# Start of group
		 		(?=.*\d)		# must contains one digit from 0-9
  				(?=.*[a-z])		# must contains one lowercase characters
				(?=.*[A-Z])		# must contains one uppercase characters
				(?=.*[@#$%])	# must contains one special symbols in the list "@#$%"
				.				# match anything with previous condition checking
				{6,20}			# length at least 6 characters and maximum of 20	
			)					# End of group
		*/

		switch (ValidTipi) {
			case "Mutlaka_EnAzBir_Sayi_Icermeli":
				return txt.matches("^(?=.*[0-9]).{1,}$");
			case "Mutlaka_EnAzBir_KucukHarf_Icermeli":
				return txt.matches("^(?=.*[a-zığüşöç]).{1,}$");
			case "Mutlaka_EnAzBir_BuyukHarf_Icermeli":
				return txt.matches("^(?=.*[A-ZĞÜŞIÖÇ]).{1,}$");
			case "Mutlaka_EnAzBir_KucukHarf_BuyukHarf_Icermeli":
				return txt.matches("^(?=.*[a-zığüşöç])(?=.*[A-Z]).{1,}$");
			case "Mutlaka_EnAzBir_Sayi_KucukHarf_BuyukHarf_Icermeli":
				return txt.matches("^(?=.*[0-9])(?=.*[a-zığüşöç])(?=.*[A-ZĞÜŞIÖÇ]).{1,}$");
			case "Mutlaka_EnAzBir_Sayi_KucukHarf_Icermeli":
				return txt.matches("^(?=.*[0-9])(?=.*[a-zığüşöç]).{1,}$");
			case "Mutlaka_EnAzBir_Sayi_BuyukHarf_Icermeli":
				return txt.matches("^(?=.*[0-9])(?=.*[A-ZĞÜŞIÖÇ]).{1,}$");

			case "SadeceSayi":
				return txt.matches("^[0-9]*$");
			case "SadeceKucukHarf":
				return txt.matches("^[a-zığüşöç]*$");
			case "SadeceBuyukHarf":
				return txt.matches("^[A-ZĞÜŞIÖÇ]*$");
			case "SadeceSayiKucukHarf":
				return txt.matches("^[0-9a-zığüşöç]*$");
			case "SadeceSayiBuyukHarf":
				return txt.matches("^[0-9A-ZĞÜŞIÖÇ]*$");
			case "SadeceSayiKucukHarfBuyukHarf":
				return txt.matches("^[0-9a-zığüşöçA-ZĞÜŞIÖÇ]*$");
			case "SadeceKucukHarfBuyukHarf":
				return txt.matches("^[a-zığüşöçA-ZĞÜŞIÖÇ]*$");


			case "SadeceSayiBosluklu":
				return txt.matches("^[0-9 ]*$");
			case "SadeceKucukHarfBosluklu":
				return txt.matches("^[a-zığüşöç ]*$");
			case "SadeceBuyukHarfBosluklu":
				return txt.matches("^[A-ZĞÜŞIÖÇ ]*$");
			case "SadeceSayiKucukHarfBosluklu":
				return txt.matches("^[0-9a-zığüşöç ]*$");
			case "SadeceSayiBuyukHarfBosluklu":
				return txt.matches("^[0-9A-ZĞÜŞIÖÇ ]*$");
			case "SadeceSayiKucukHarfBuyukHarfBosluklu":
				return txt.matches("^[0-9a-zığüşöçA-ZĞÜŞIÖÇ ]*$");
			case "SadeceKucukHarfBuyukHarfBosluklu":
				return txt.matches("^[a-zığüşöçA-ZĞÜŞIÖÇ ]*$");

			case "SadeceSayiKucukHarfTurkceKaraktersiz":
				return txt.matches("^[0-9a-z]*$");
			case "SadeceSayiBuyukHarfTurkceKaraktersiz":
				return txt.matches("^[0-9A-Z]*$");
			case "SadeceSayiKucukHarfBuyukHarfTurkceKaraktersiz":
				return txt.matches("^[0-9a-zA-Z]*$");

			case "Sifre":
				return txt.matches("^(?=.*[0-9])(?=.*[a-zığüşöç])(?=.*[A-ZĞÜŞIÖÇ]).{1,}$");
			case "EPosta":
				return android.util.Patterns.EMAIL_ADDRESS.matcher(txt).matches();
			case "FakeEPosta":
				return txt.trim().contains("@mvrht.com");
			case "KullaniciAdi":
				return txt.matches("^[0-9a-z]*$"); // Kullanıcı adı kontrolü
			case "EmailKullaniciAdiTelefon":
				if (android.util.Patterns.EMAIL_ADDRESS.matcher(txt).matches()) return true; // Email kontrolü
				else return txt.matches("^[0-9a-z+]*$"); // Kullanıcı adı ya da telefon kontrolü
		}

		return false;
	}

	public boolean isNumeric(String string) {
		return string.matches("^\\d+$");
	}

	public Spannable TiklanabilirAkorEtiketleyici(String Icerik, String AkorTag1, String AkorTag2, int YaziRengi) {
		Spannable wordtoSpan = new SpannableString(Html.fromHtml(Icerik));

		Icerik = Icerik.replace("&nbsp;", " ");
		Icerik = Icerik.replace("<br />", "\n");
		Icerik = Icerik.replace("&uuml;", "ü");
		Icerik = Icerik.replace("&ouml;", "ö");
		Icerik = Icerik.replace("&ccedil;", "ç");
		Icerik = Icerik.replace("&Uuml;", "Ü");
		Icerik = Icerik.replace("&Ouml;", "Ö");
		Icerik = Icerik.replace("&Ccedil;", "Ç");
		Icerik = Icerik.replace("<strong>", "");
		Icerik = Icerik.replace("</strong>", "");

		int tag1_koor, tag2_koor, x, y;

		while (Icerik.contains(AkorTag1)) {
			tag1_koor = Icerik.indexOf(AkorTag1);
			tag2_koor = Icerik.indexOf(AkorTag2);

			int AkorLen = tag2_koor - (tag1_koor + AkorTag1.length());

			Icerik = Icerik.substring(0, tag1_koor) + Icerik.substring(tag1_koor + AkorTag1.length(), Icerik.length());
			Icerik = Icerik.substring(0, Icerik.indexOf(AkorTag2)) + Icerik.substring(Icerik.indexOf(AkorTag2) + AkorTag2.length(), Icerik.length());

			x = tag1_koor;
			y = x + AkorLen;

			wordtoSpan.setSpan(new TiklanabilirAkorEtiketleyici(activity, Icerik, YaziRengi, x, y), x, y, Spanned.SPAN_POINT_MARK);

			if(!Icerik.contains(AkorTag1)) break;
		}

		return wordtoSpan;
	}

	public SpannableStringBuilder Transpoze(String TranspozeIslem, String Icerik) {
		SpannableStringBuilder SBTranspozeEdilenIcerik = new SpannableStringBuilder(Icerik);
		SpannableStringBuilder PartIcerik1;
		SpannableStringBuilder PartIcerik2;
		
		String TagBaslangic = "<span style=\"color: #ea494d;\" onclick=\"AkorGosterici.performClick('";
		String TagOrta = "');\"><strong>";
		String TagBitis = "</strong></span>";
		
		int BaslangicNo = 0;
		int Deger = 0;
		ArrayList<Integer> NotaKordinatlari = new ArrayList<>();
		String[] Notalar = {
				TagBaslangic + "C" + TagOrta + "C" + TagBitis, TagBaslangic + "Cmaj7" + TagOrta + "Cmaj7" + TagBitis, TagBaslangic + "Cmaj9" + TagOrta + "Cmaj9" + TagBitis, TagBaslangic + "Cmaj11" + TagOrta + "Cmaj11" + TagBitis, TagBaslangic + "Cmaj13" + TagOrta + "Cmaj13" + TagBitis, TagBaslangic + "Cmaj9#11" + TagOrta + "Cmaj9#11" + TagBitis, TagBaslangic + "Cmaj13#11" + TagOrta + "Cmaj13#11" + TagBitis, TagBaslangic + "C6" + TagOrta + "C6" + TagBitis, TagBaslangic + "Cadd9" + TagOrta + "Cadd9" + TagBitis, TagBaslangic + "C6add9" + TagOrta + "C6add9" + TagBitis, TagBaslangic + "Cmaj7b5" + TagOrta + "Cmaj7b5" + TagBitis, TagBaslangic + "Cmaj7" + TagOrta + "Cmaj7#5" + TagBitis, TagBaslangic + "Cm" + TagOrta + "Cm" + TagBitis, TagBaslangic + "Cm7" + TagOrta + "Cm7" + TagBitis, TagBaslangic + "Cm9" + TagOrta + "Cm9" + TagBitis, TagBaslangic + "Cm11" + TagOrta + "Cm11" + TagBitis, TagBaslangic + "Cm13" + TagOrta + "Cm13" + TagBitis, TagBaslangic + "Cm6" + TagOrta + "Cm6" + TagBitis, TagBaslangic + "Cmadd9" + TagOrta + "Cmadd9" + TagBitis, TagBaslangic + "Cm6add9" + TagOrta + "Cm6add9" + TagBitis, TagBaslangic + "Cmmaj7" + TagOrta + "Cmmaj7" + TagBitis, TagBaslangic + "Cmmaj9" + TagOrta + "Cmmaj9" + TagBitis, TagBaslangic + "Cm7b5" + TagOrta + "Cm7b5" + TagBitis, TagBaslangic + "Cm7#5" + TagOrta + "Cm7#5" + TagBitis, TagBaslangic + "C7" + TagOrta + "C7" + TagBitis, TagBaslangic + "C9" + TagOrta + "C9" + TagBitis, TagBaslangic + "C11" + TagOrta + "C11" + TagBitis, TagBaslangic + "C13" + TagOrta + "C13" + TagBitis, TagBaslangic + "C7sus4" + TagOrta + "C7sus4" + TagBitis, TagBaslangic + "C7b5" + TagOrta + "C7b5" + TagBitis, TagBaslangic + "C7#5" + TagOrta + "C7#5" + TagBitis, TagBaslangic + "C7b9" + TagOrta + "C7b9" + TagBitis, TagBaslangic + "C7#9" + TagOrta + "C7#9" + TagBitis, TagBaslangic + "C7(b5,b9)" + TagOrta + "C7(b5,b9)" + TagBitis, TagBaslangic + "C7(b5,#9" + TagOrta + "C7(b5,#9)" + TagBitis, TagBaslangic + "C7(#5,b9)" + TagOrta + "C7(#5,b9)" + TagBitis, TagBaslangic + "C7(#5,#9)" + TagOrta + "C7(#5,#9)" + TagBitis, TagBaslangic + "C9b5" + TagOrta + "C9b5" + TagBitis, TagBaslangic + "C9#5" + TagOrta + "C9#5" + TagBitis, TagBaslangic + "C13#11" + TagOrta + "C13#11" + TagBitis, TagBaslangic + "C13b9" + TagOrta + "C13b9" + TagBitis, TagBaslangic + "C11b9" + TagOrta + "C11b9" + TagBitis, TagBaslangic + "Caug" + TagOrta + "Caug" + TagBitis, TagBaslangic + "Cdim" + TagOrta + "Cdim" + TagBitis, TagBaslangic + "Cdim7" + TagOrta + "Cdim7" + TagBitis, TagBaslangic + "C5" + TagOrta + "C5" + TagBitis, TagBaslangic + "Csus4" + TagOrta + "Csus4" + TagBitis, TagBaslangic + "Csus2" + TagOrta + "Csus2" + TagBitis, TagBaslangic + "Csus2sus4" + TagOrta + "Csus2sus4" + TagBitis, TagBaslangic + "C-5" + TagOrta + "C-5" + TagBitis,
				TagBaslangic + "C#" + TagOrta + "C#" + TagBitis, TagBaslangic + "C#maj7" + TagOrta + "C#maj7" + TagBitis, TagBaslangic + "C#maj9" + TagOrta + "C#maj9" + TagBitis, TagBaslangic + "C#maj11" + TagOrta + "C#maj11" + TagBitis, TagBaslangic + "C#maj13" + TagOrta + "C#maj13" + TagBitis, TagBaslangic + "C#maj9#11" + TagOrta + "C#maj9#11" + TagBitis, TagBaslangic + "C#maj13#11" + TagOrta + "C#maj13#11" + TagBitis, TagBaslangic + "C#6" + TagOrta + "C#6" + TagBitis, TagBaslangic + "C#add9" + TagOrta + "C#add9" + TagBitis, TagBaslangic + "C#6add9" + TagOrta + "C#6add9" + TagBitis, TagBaslangic + "C#maj7b5" + TagOrta + "C#maj7b5" + TagBitis, TagBaslangic + "C#maj7" + TagOrta + "C#maj7#5" + TagBitis, TagBaslangic + "C#m" + TagOrta + "C#m" + TagBitis, TagBaslangic + "C#m7" + TagOrta + "C#m7" + TagBitis, TagBaslangic + "C#m9" + TagOrta + "C#m9" + TagBitis, TagBaslangic + "C#m11" + TagOrta + "C#m11" + TagBitis, TagBaslangic + "C#m13" + TagOrta + "C#m13" + TagBitis, TagBaslangic + "C#m6" + TagOrta + "C#m6" + TagBitis, TagBaslangic + "C#madd9" + TagOrta + "C#madd9" + TagBitis, TagBaslangic + "C#m6add9" + TagOrta + "C#m6add9" + TagBitis, TagBaslangic + "C#mmaj7" + TagOrta + "C#mmaj7" + TagBitis, TagBaslangic + "C#mmaj9" + TagOrta + "C#mmaj9" + TagBitis, TagBaslangic + "C#m7b5" + TagOrta + "C#m7b5" + TagBitis, TagBaslangic + "C#m7#5" + TagOrta + "C#m7#5" + TagBitis, TagBaslangic + "C#7" + TagOrta + "C#7" + TagBitis, TagBaslangic + "C#9" + TagOrta + "C#9" + TagBitis, TagBaslangic + "C#11" + TagOrta + "C#11" + TagBitis, TagBaslangic + "C#13" + TagOrta + "C#13" + TagBitis, TagBaslangic + "C#7sus4" + TagOrta + "C#7sus4" + TagBitis, TagBaslangic + "C#7b5" + TagOrta + "C#7b5" + TagBitis, TagBaslangic + "C#7#5" + TagOrta + "C#7#5" + TagBitis, TagBaslangic + "C#7b9" + TagOrta + "C#7b9" + TagBitis, TagBaslangic + "C#7#9" + TagOrta + "C#7#9" + TagBitis, TagBaslangic + "C#7(b5,b9)" + TagOrta + "C#7(b5,b9)" + TagBitis, TagBaslangic + "C#7(b5,#9" + TagOrta + "C#7(b5,#9)" + TagBitis, TagBaslangic + "C#7(#5,b9)" + TagOrta + "C#7(#5,b9)" + TagBitis, TagBaslangic + "C#7(#5,#9)" + TagOrta + "C#7(#5,#9)" + TagBitis, TagBaslangic + "C#9b5" + TagOrta + "C#9b5" + TagBitis, TagBaslangic + "C#9#5" + TagOrta + "C#9#5" + TagBitis, TagBaslangic + "C#13#11" + TagOrta + "C#13#11" + TagBitis, TagBaslangic + "C#13b9" + TagOrta + "C#13b9" + TagBitis, TagBaslangic + "C#11b9" + TagOrta + "C#11b9" + TagBitis, TagBaslangic + "C#aug" + TagOrta + "C#aug" + TagBitis, TagBaslangic + "C#dim" + TagOrta + "C#dim" + TagBitis, TagBaslangic + "C#dim7" + TagOrta + "C#dim7" + TagBitis, TagBaslangic + "C#5" + TagOrta + "C#5" + TagBitis, TagBaslangic + "C#sus4" + TagOrta + "C#sus4" + TagBitis, TagBaslangic + "C#sus2" + TagOrta + "C#sus2" + TagBitis, TagBaslangic + "C#sus2sus4" + TagOrta + "C#sus2sus4" + TagBitis, TagBaslangic + "C#-5" + TagOrta + "C#-5" + TagBitis,
				TagBaslangic + "D" + TagOrta + "D" + TagBitis, TagBaslangic + "Dmaj7" + TagOrta + "Dmaj7" + TagBitis, TagBaslangic + "Dmaj9" + TagOrta + "Dmaj9" + TagBitis, TagBaslangic + "Dmaj11" + TagOrta + "Dmaj11" + TagBitis, TagBaslangic + "Dmaj13" + TagOrta + "Dmaj13" + TagBitis, TagBaslangic + "Dmaj9#11" + TagOrta + "Dmaj9#11" + TagBitis, TagBaslangic + "Dmaj13#11" + TagOrta + "Dmaj13#11" + TagBitis, TagBaslangic + "D6" + TagOrta + "D6" + TagBitis, TagBaslangic + "Dadd9" + TagOrta + "Dadd9" + TagBitis, TagBaslangic + "D6add9" + TagOrta + "D6add9" + TagBitis, TagBaslangic + "Dmaj7b5" + TagOrta + "Dmaj7b5" + TagBitis, TagBaslangic + "Dmaj7" + TagOrta + "Dmaj7#5" + TagBitis, TagBaslangic + "Dm" + TagOrta + "Dm" + TagBitis, TagBaslangic + "Dm7" + TagOrta + "Dm7" + TagBitis, TagBaslangic + "Dm9" + TagOrta + "Dm9" + TagBitis, TagBaslangic + "Dm11" + TagOrta + "Dm11" + TagBitis, TagBaslangic + "Dm13" + TagOrta + "Dm13" + TagBitis, TagBaslangic + "Dm6" + TagOrta + "Dm6" + TagBitis, TagBaslangic + "Dmadd9" + TagOrta + "Dmadd9" + TagBitis, TagBaslangic + "Dm6add9" + TagOrta + "Dm6add9" + TagBitis, TagBaslangic + "Dmmaj7" + TagOrta + "Dmmaj7" + TagBitis, TagBaslangic + "Dmmaj9" + TagOrta + "Dmmaj9" + TagBitis, TagBaslangic + "Dm7b5" + TagOrta + "Dm7b5" + TagBitis, TagBaslangic + "Dm7#5" + TagOrta + "Dm7#5" + TagBitis, TagBaslangic + "D7" + TagOrta + "D7" + TagBitis, TagBaslangic + "D9" + TagOrta + "D9" + TagBitis, TagBaslangic + "D11" + TagOrta + "D11" + TagBitis, TagBaslangic + "D13" + TagOrta + "D13" + TagBitis, TagBaslangic + "D7sus4" + TagOrta + "D7sus4" + TagBitis, TagBaslangic + "D7b5" + TagOrta + "D7b5" + TagBitis, TagBaslangic + "D7#5" + TagOrta + "D7#5" + TagBitis, TagBaslangic + "D7b9" + TagOrta + "D7b9" + TagBitis, TagBaslangic + "D7#9" + TagOrta + "D7#9" + TagBitis, TagBaslangic + "D7(b5,b9)" + TagOrta + "D7(b5,b9)" + TagBitis, TagBaslangic + "D7(b5,#9" + TagOrta + "D7(b5,#9)" + TagBitis, TagBaslangic + "D7(#5,b9)" + TagOrta + "D7(#5,b9)" + TagBitis, TagBaslangic + "D7(#5,#9)" + TagOrta + "D7(#5,#9)" + TagBitis, TagBaslangic + "D9b5" + TagOrta + "D9b5" + TagBitis, TagBaslangic + "D9#5" + TagOrta + "D9#5" + TagBitis, TagBaslangic + "D13#11" + TagOrta + "D13#11" + TagBitis, TagBaslangic + "D13b9" + TagOrta + "D13b9" + TagBitis, TagBaslangic + "D11b9" + TagOrta + "D11b9" + TagBitis, TagBaslangic + "Daug" + TagOrta + "Daug" + TagBitis, TagBaslangic + "Ddim" + TagOrta + "Ddim" + TagBitis, TagBaslangic + "Ddim7" + TagOrta + "Ddim7" + TagBitis, TagBaslangic + "D5" + TagOrta + "D5" + TagBitis, TagBaslangic + "Dsus4" + TagOrta + "Dsus4" + TagBitis, TagBaslangic + "Dsus2" + TagOrta + "Dsus2" + TagBitis, TagBaslangic + "Dsus2sus4" + TagOrta + "Dsus2sus4" + TagBitis, TagBaslangic + "D-5" + TagOrta + "D-5" + TagBitis,
				TagBaslangic + "D#" + TagOrta + "D#" + TagBitis, TagBaslangic + "D#maj7" + TagOrta + "D#maj7" + TagBitis, TagBaslangic + "D#maj9" + TagOrta + "D#maj9" + TagBitis, TagBaslangic + "D#maj11" + TagOrta + "D#maj11" + TagBitis, TagBaslangic + "D#maj13" + TagOrta + "D#maj13" + TagBitis, TagBaslangic + "D#maj9#11" + TagOrta + "D#maj9#11" + TagBitis, TagBaslangic + "D#maj13#11" + TagOrta + "D#maj13#11" + TagBitis, TagBaslangic + "D#6" + TagOrta + "D#6" + TagBitis, TagBaslangic + "D#add9" + TagOrta + "D#add9" + TagBitis, TagBaslangic + "D#6add9" + TagOrta + "D#6add9" + TagBitis, TagBaslangic + "D#maj7b5" + TagOrta + "D#maj7b5" + TagBitis, TagBaslangic + "D#maj7" + TagOrta + "D#maj7#5" + TagBitis, TagBaslangic + "D#m" + TagOrta + "D#m" + TagBitis, TagBaslangic + "D#m7" + TagOrta + "D#m7" + TagBitis, TagBaslangic + "D#m9" + TagOrta + "D#m9" + TagBitis, TagBaslangic + "D#m11" + TagOrta + "D#m11" + TagBitis, TagBaslangic + "D#m13" + TagOrta + "D#m13" + TagBitis, TagBaslangic + "D#m6" + TagOrta + "D#m6" + TagBitis, TagBaslangic + "D#madd9" + TagOrta + "D#madd9" + TagBitis, TagBaslangic + "D#m6add9" + TagOrta + "D#m6add9" + TagBitis, TagBaslangic + "D#mmaj7" + TagOrta + "D#mmaj7" + TagBitis, TagBaslangic + "D#mmaj9" + TagOrta + "D#mmaj9" + TagBitis, TagBaslangic + "D#m7b5" + TagOrta + "D#m7b5" + TagBitis, TagBaslangic + "D#m7#5" + TagOrta + "D#m7#5" + TagBitis, TagBaslangic + "D#7" + TagOrta + "D#7" + TagBitis, TagBaslangic + "D#9" + TagOrta + "D#9" + TagBitis, TagBaslangic + "D#11" + TagOrta + "D#11" + TagBitis, TagBaslangic + "D#13" + TagOrta + "D#13" + TagBitis, TagBaslangic + "D#7sus4" + TagOrta + "D#7sus4" + TagBitis, TagBaslangic + "D#7b5" + TagOrta + "D#7b5" + TagBitis, TagBaslangic + "D#7#5" + TagOrta + "D#7#5" + TagBitis, TagBaslangic + "D#7b9" + TagOrta + "D#7b9" + TagBitis, TagBaslangic + "D#7#9" + TagOrta + "D#7#9" + TagBitis, TagBaslangic + "D#7(b5,b9)" + TagOrta + "D#7(b5,b9)" + TagBitis, TagBaslangic + "D#7(b5,#9" + TagOrta + "D#7(b5,#9)" + TagBitis, TagBaslangic + "D#7(#5,b9)" + TagOrta + "D#7(#5,b9)" + TagBitis, TagBaslangic + "D#7(#5,#9)" + TagOrta + "D#7(#5,#9)" + TagBitis, TagBaslangic + "D#9b5" + TagOrta + "D#9b5" + TagBitis, TagBaslangic + "D#9#5" + TagOrta + "D#9#5" + TagBitis, TagBaslangic + "D#13#11" + TagOrta + "D#13#11" + TagBitis, TagBaslangic + "D#13b9" + TagOrta + "D#13b9" + TagBitis, TagBaslangic + "D#11b9" + TagOrta + "D#11b9" + TagBitis, TagBaslangic + "D#aug" + TagOrta + "D#aug" + TagBitis, TagBaslangic + "D#dim" + TagOrta + "D#dim" + TagBitis, TagBaslangic + "D#dim7" + TagOrta + "D#dim7" + TagBitis, TagBaslangic + "D#5" + TagOrta + "D#5" + TagBitis, TagBaslangic + "D#sus4" + TagOrta + "D#sus4" + TagBitis, TagBaslangic + "D#sus2" + TagOrta + "D#sus2" + TagBitis, TagBaslangic + "D#sus2sus4" + TagOrta + "D#sus2sus4" + TagBitis, TagBaslangic + "D#-5" + TagOrta + "D#-5" + TagBitis,
				TagBaslangic + "E" + TagOrta + "E" + TagBitis, TagBaslangic + "Emaj7" + TagOrta + "Emaj7" + TagBitis, TagBaslangic + "Emaj9" + TagOrta + "Emaj9" + TagBitis, TagBaslangic + "Emaj11" + TagOrta + "Emaj11" + TagBitis, TagBaslangic + "Emaj13" + TagOrta + "Emaj13" + TagBitis, TagBaslangic + "Emaj9#11" + TagOrta + "Emaj9#11" + TagBitis, TagBaslangic + "Emaj13#11" + TagOrta + "Emaj13#11" + TagBitis, TagBaslangic + "E6" + TagOrta + "E6" + TagBitis, TagBaslangic + "Eadd9" + TagOrta + "Eadd9" + TagBitis, TagBaslangic + "E6add9" + TagOrta + "E6add9" + TagBitis, TagBaslangic + "Emaj7b5" + TagOrta + "Emaj7b5" + TagBitis, TagBaslangic + "Emaj7" + TagOrta + "Emaj7#5" + TagBitis, TagBaslangic + "Em" + TagOrta + "Em" + TagBitis, TagBaslangic + "Em7" + TagOrta + "Em7" + TagBitis, TagBaslangic + "Em9" + TagOrta + "Em9" + TagBitis, TagBaslangic + "Em11" + TagOrta + "Em11" + TagBitis, TagBaslangic + "Em13" + TagOrta + "Em13" + TagBitis, TagBaslangic + "Em6" + TagOrta + "Em6" + TagBitis, TagBaslangic + "Emadd9" + TagOrta + "Emadd9" + TagBitis, TagBaslangic + "Em6add9" + TagOrta + "Em6add9" + TagBitis, TagBaslangic + "Emmaj7" + TagOrta + "Emmaj7" + TagBitis, TagBaslangic + "Emmaj9" + TagOrta + "Emmaj9" + TagBitis, TagBaslangic + "Em7b5" + TagOrta + "Em7b5" + TagBitis, TagBaslangic + "Em7#5" + TagOrta + "Em7#5" + TagBitis, TagBaslangic + "E7" + TagOrta + "E7" + TagBitis, TagBaslangic + "E9" + TagOrta + "E9" + TagBitis, TagBaslangic + "E11" + TagOrta + "E11" + TagBitis, TagBaslangic + "E13" + TagOrta + "E13" + TagBitis, TagBaslangic + "E7sus4" + TagOrta + "E7sus4" + TagBitis, TagBaslangic + "E7b5" + TagOrta + "E7b5" + TagBitis, TagBaslangic + "E7#5" + TagOrta + "E7#5" + TagBitis, TagBaslangic + "E7b9" + TagOrta + "E7b9" + TagBitis, TagBaslangic + "E7#9" + TagOrta + "E7#9" + TagBitis, TagBaslangic + "E7(b5,b9)" + TagOrta + "E7(b5,b9)" + TagBitis, TagBaslangic + "E7(b5,#9" + TagOrta + "E7(b5,#9)" + TagBitis, TagBaslangic + "E7(#5,b9)" + TagOrta + "E7(#5,b9)" + TagBitis, TagBaslangic + "E7(#5,#9)" + TagOrta + "E7(#5,#9)" + TagBitis, TagBaslangic + "E9b5" + TagOrta + "E9b5" + TagBitis, TagBaslangic + "E9#5" + TagOrta + "E9#5" + TagBitis, TagBaslangic + "E13#11" + TagOrta + "E13#11" + TagBitis, TagBaslangic + "E13b9" + TagOrta + "E13b9" + TagBitis, TagBaslangic + "E11b9" + TagOrta + "E11b9" + TagBitis, TagBaslangic + "Eaug" + TagOrta + "Eaug" + TagBitis, TagBaslangic + "Edim" + TagOrta + "Edim" + TagBitis, TagBaslangic + "Edim7" + TagOrta + "Edim7" + TagBitis, TagBaslangic + "E5" + TagOrta + "E5" + TagBitis, TagBaslangic + "Esus4" + TagOrta + "Esus4" + TagBitis, TagBaslangic + "Esus2" + TagOrta + "Esus2" + TagBitis, TagBaslangic + "Esus2sus4" + TagOrta + "Esus2sus4" + TagBitis, TagBaslangic + "E-5" + TagOrta + "E-5" + TagBitis,
				TagBaslangic + "F" + TagOrta + "F" + TagBitis, TagBaslangic + "Fmaj7" + TagOrta + "Fmaj7" + TagBitis, TagBaslangic + "Fmaj9" + TagOrta + "Fmaj9" + TagBitis, TagBaslangic + "Fmaj11" + TagOrta + "Fmaj11" + TagBitis, TagBaslangic + "Fmaj13" + TagOrta + "Fmaj13" + TagBitis, TagBaslangic + "Fmaj9#11" + TagOrta + "Fmaj9#11" + TagBitis, TagBaslangic + "Fmaj13#11" + TagOrta + "Fmaj13#11" + TagBitis, TagBaslangic + "F6" + TagOrta + "F6" + TagBitis, TagBaslangic + "Fadd9" + TagOrta + "Fadd9" + TagBitis, TagBaslangic + "F6add9" + TagOrta + "F6add9" + TagBitis, TagBaslangic + "Fmaj7b5" + TagOrta + "Fmaj7b5" + TagBitis, TagBaslangic + "Fmaj7" + TagOrta + "Fmaj7#5" + TagBitis, TagBaslangic + "Fm" + TagOrta + "Fm" + TagBitis, TagBaslangic + "Fm7" + TagOrta + "Fm7" + TagBitis, TagBaslangic + "Fm9" + TagOrta + "Fm9" + TagBitis, TagBaslangic + "Fm11" + TagOrta + "Fm11" + TagBitis, TagBaslangic + "Fm13" + TagOrta + "Fm13" + TagBitis, TagBaslangic + "Fm6" + TagOrta + "Fm6" + TagBitis, TagBaslangic + "Fmadd9" + TagOrta + "Fmadd9" + TagBitis, TagBaslangic + "Fm6add9" + TagOrta + "Fm6add9" + TagBitis, TagBaslangic + "Fmmaj7" + TagOrta + "Fmmaj7" + TagBitis, TagBaslangic + "Fmmaj9" + TagOrta + "Fmmaj9" + TagBitis, TagBaslangic + "Fm7b5" + TagOrta + "Fm7b5" + TagBitis, TagBaslangic + "Fm7#5" + TagOrta + "Fm7#5" + TagBitis, TagBaslangic + "F7" + TagOrta + "F7" + TagBitis, TagBaslangic + "F9" + TagOrta + "F9" + TagBitis, TagBaslangic + "F11" + TagOrta + "F11" + TagBitis, TagBaslangic + "F13" + TagOrta + "F13" + TagBitis, TagBaslangic + "F7sus4" + TagOrta + "F7sus4" + TagBitis, TagBaslangic + "F7b5" + TagOrta + "F7b5" + TagBitis, TagBaslangic + "F7#5" + TagOrta + "F7#5" + TagBitis, TagBaslangic + "F7b9" + TagOrta + "F7b9" + TagBitis, TagBaslangic + "F7#9" + TagOrta + "F7#9" + TagBitis, TagBaslangic + "F7(b5,b9)" + TagOrta + "F7(b5,b9)" + TagBitis, TagBaslangic + "F7(b5,#9" + TagOrta + "F7(b5,#9)" + TagBitis, TagBaslangic + "F7(#5,b9)" + TagOrta + "F7(#5,b9)" + TagBitis, TagBaslangic + "F7(#5,#9)" + TagOrta + "F7(#5,#9)" + TagBitis, TagBaslangic + "F9b5" + TagOrta + "F9b5" + TagBitis, TagBaslangic + "F9#5" + TagOrta + "F9#5" + TagBitis, TagBaslangic + "F13#11" + TagOrta + "F13#11" + TagBitis, TagBaslangic + "F13b9" + TagOrta + "F13b9" + TagBitis, TagBaslangic + "F11b9" + TagOrta + "F11b9" + TagBitis, TagBaslangic + "Faug" + TagOrta + "Faug" + TagBitis, TagBaslangic + "Fdim" + TagOrta + "Fdim" + TagBitis, TagBaslangic + "Fdim7" + TagOrta + "Fdim7" + TagBitis, TagBaslangic + "F5" + TagOrta + "F5" + TagBitis, TagBaslangic + "Fsus4" + TagOrta + "Fsus4" + TagBitis, TagBaslangic + "Fsus2" + TagOrta + "Fsus2" + TagBitis, TagBaslangic + "Fsus2sus4" + TagOrta + "Fsus2sus4" + TagBitis, TagBaslangic + "F-5" + TagOrta + "F-5" + TagBitis,
				TagBaslangic + "F#" + TagOrta + "F#" + TagBitis, TagBaslangic + "F#maj7" + TagOrta + "F#maj7" + TagBitis, TagBaslangic + "F#maj9" + TagOrta + "F#maj9" + TagBitis, TagBaslangic + "F#maj11" + TagOrta + "F#maj11" + TagBitis, TagBaslangic + "F#maj13" + TagOrta + "F#maj13" + TagBitis, TagBaslangic + "F#maj9#11" + TagOrta + "F#maj9#11" + TagBitis, TagBaslangic + "F#maj13#11" + TagOrta + "F#maj13#11" + TagBitis, TagBaslangic + "F#6" + TagOrta + "F#6" + TagBitis, TagBaslangic + "F#add9" + TagOrta + "F#add9" + TagBitis, TagBaslangic + "F#6add9" + TagOrta + "F#6add9" + TagBitis, TagBaslangic + "F#maj7b5" + TagOrta + "F#maj7b5" + TagBitis, TagBaslangic + "F#maj7" + TagOrta + "F#maj7#5" + TagBitis, TagBaslangic + "F#m" + TagOrta + "F#m" + TagBitis, TagBaslangic + "F#m7" + TagOrta + "F#m7" + TagBitis, TagBaslangic + "F#m9" + TagOrta + "F#m9" + TagBitis, TagBaslangic + "F#m11" + TagOrta + "F#m11" + TagBitis, TagBaslangic + "F#m13" + TagOrta + "F#m13" + TagBitis, TagBaslangic + "F#m6" + TagOrta + "F#m6" + TagBitis, TagBaslangic + "F#madd9" + TagOrta + "F#madd9" + TagBitis, TagBaslangic + "F#m6add9" + TagOrta + "F#m6add9" + TagBitis, TagBaslangic + "F#mmaj7" + TagOrta + "F#mmaj7" + TagBitis, TagBaslangic + "F#mmaj9" + TagOrta + "F#mmaj9" + TagBitis, TagBaslangic + "F#m7b5" + TagOrta + "F#m7b5" + TagBitis, TagBaslangic + "F#m7#5" + TagOrta + "F#m7#5" + TagBitis, TagBaslangic + "F#7" + TagOrta + "F#7" + TagBitis, TagBaslangic + "F#9" + TagOrta + "F#9" + TagBitis, TagBaslangic + "F#11" + TagOrta + "F#11" + TagBitis, TagBaslangic + "F#13" + TagOrta + "F#13" + TagBitis, TagBaslangic + "F#7sus4" + TagOrta + "F#7sus4" + TagBitis, TagBaslangic + "F#7b5" + TagOrta + "F#7b5" + TagBitis, TagBaslangic + "F#7#5" + TagOrta + "F#7#5" + TagBitis, TagBaslangic + "F#7b9" + TagOrta + "F#7b9" + TagBitis, TagBaslangic + "F#7#9" + TagOrta + "F#7#9" + TagBitis, TagBaslangic + "F#7(b5,b9)" + TagOrta + "F#7(b5,b9)" + TagBitis, TagBaslangic + "F#7(b5,#9" + TagOrta + "F#7(b5,#9)" + TagBitis, TagBaslangic + "F#7(#5,b9)" + TagOrta + "F#7(#5,b9)" + TagBitis, TagBaslangic + "F#7(#5,#9)" + TagOrta + "F#7(#5,#9)" + TagBitis, TagBaslangic + "F#9b5" + TagOrta + "F#9b5" + TagBitis, TagBaslangic + "F#9#5" + TagOrta + "F#9#5" + TagBitis, TagBaslangic + "F#13#11" + TagOrta + "F#13#11" + TagBitis, TagBaslangic + "F#13b9" + TagOrta + "F#13b9" + TagBitis, TagBaslangic + "F#11b9" + TagOrta + "F#11b9" + TagBitis, TagBaslangic + "F#aug" + TagOrta + "F#aug" + TagBitis, TagBaslangic + "F#dim" + TagOrta + "F#dim" + TagBitis, TagBaslangic + "F#dim7" + TagOrta + "F#dim7" + TagBitis, TagBaslangic + "F#5" + TagOrta + "F#5" + TagBitis, TagBaslangic + "F#sus4" + TagOrta + "F#sus4" + TagBitis, TagBaslangic + "F#sus2" + TagOrta + "F#sus2" + TagBitis, TagBaslangic + "F#sus2sus4" + TagOrta + "F#sus2sus4" + TagBitis, TagBaslangic + "F#-5" + TagOrta + "F#-5" + TagBitis,
				TagBaslangic + "G" + TagOrta + "G" + TagBitis, TagBaslangic + "Gmaj7" + TagOrta + "Gmaj7" + TagBitis, TagBaslangic + "Gmaj9" + TagOrta + "Gmaj9" + TagBitis, TagBaslangic + "Gmaj11" + TagOrta + "Gmaj11" + TagBitis, TagBaslangic + "Gmaj13" + TagOrta + "Gmaj13" + TagBitis, TagBaslangic + "Gmaj9#11" + TagOrta + "Gmaj9#11" + TagBitis, TagBaslangic + "Gmaj13#11" + TagOrta + "Gmaj13#11" + TagBitis, TagBaslangic + "G6" + TagOrta + "G6" + TagBitis, TagBaslangic + "Gadd9" + TagOrta + "Gadd9" + TagBitis, TagBaslangic + "G6add9" + TagOrta + "G6add9" + TagBitis, TagBaslangic + "Gmaj7b5" + TagOrta + "Gmaj7b5" + TagBitis, TagBaslangic + "Gmaj7" + TagOrta + "Gmaj7#5" + TagBitis, TagBaslangic + "Gm" + TagOrta + "Gm" + TagBitis, TagBaslangic + "Gm7" + TagOrta + "Gm7" + TagBitis, TagBaslangic + "Gm9" + TagOrta + "Gm9" + TagBitis, TagBaslangic + "Gm11" + TagOrta + "Gm11" + TagBitis, TagBaslangic + "Gm13" + TagOrta + "Gm13" + TagBitis, TagBaslangic + "Gm6" + TagOrta + "Gm6" + TagBitis, TagBaslangic + "Gmadd9" + TagOrta + "Gmadd9" + TagBitis, TagBaslangic + "Gm6add9" + TagOrta + "Gm6add9" + TagBitis, TagBaslangic + "Gmmaj7" + TagOrta + "Gmmaj7" + TagBitis, TagBaslangic + "Gmmaj9" + TagOrta + "Gmmaj9" + TagBitis, TagBaslangic + "Gm7b5" + TagOrta + "Gm7b5" + TagBitis, TagBaslangic + "Gm7#5" + TagOrta + "Gm7#5" + TagBitis, TagBaslangic + "G7" + TagOrta + "G7" + TagBitis, TagBaslangic + "G9" + TagOrta + "G9" + TagBitis, TagBaslangic + "G11" + TagOrta + "G11" + TagBitis, TagBaslangic + "G13" + TagOrta + "G13" + TagBitis, TagBaslangic + "G7sus4" + TagOrta + "G7sus4" + TagBitis, TagBaslangic + "G7b5" + TagOrta + "G7b5" + TagBitis, TagBaslangic + "G7#5" + TagOrta + "G7#5" + TagBitis, TagBaslangic + "G7b9" + TagOrta + "G7b9" + TagBitis, TagBaslangic + "G7#9" + TagOrta + "G7#9" + TagBitis, TagBaslangic + "G7(b5,b9)" + TagOrta + "G7(b5,b9)" + TagBitis, TagBaslangic + "G7(b5,#9" + TagOrta + "G7(b5,#9)" + TagBitis, TagBaslangic + "G7(#5,b9)" + TagOrta + "G7(#5,b9)" + TagBitis, TagBaslangic + "G7(#5,#9)" + TagOrta + "G7(#5,#9)" + TagBitis, TagBaslangic + "G9b5" + TagOrta + "G9b5" + TagBitis, TagBaslangic + "G9#5" + TagOrta + "G9#5" + TagBitis, TagBaslangic + "G13#11" + TagOrta + "G13#11" + TagBitis, TagBaslangic + "G13b9" + TagOrta + "G13b9" + TagBitis, TagBaslangic + "G11b9" + TagOrta + "G11b9" + TagBitis, TagBaslangic + "Gaug" + TagOrta + "Gaug" + TagBitis, TagBaslangic + "Gdim" + TagOrta + "Gdim" + TagBitis, TagBaslangic + "Gdim7" + TagOrta + "Gdim7" + TagBitis, TagBaslangic + "G5" + TagOrta + "G5" + TagBitis, TagBaslangic + "Gsus4" + TagOrta + "Gsus4" + TagBitis, TagBaslangic + "Gsus2" + TagOrta + "Gsus2" + TagBitis, TagBaslangic + "Gsus2sus4" + TagOrta + "Gsus2sus4" + TagBitis, TagBaslangic + "G-5" + TagOrta + "G-5" + TagBitis,
				TagBaslangic + "G#" + TagOrta + "G#" + TagBitis, TagBaslangic + "G#maj7" + TagOrta + "G#maj7" + TagBitis, TagBaslangic + "G#maj9" + TagOrta + "G#maj9" + TagBitis, TagBaslangic + "G#maj11" + TagOrta + "G#maj11" + TagBitis, TagBaslangic + "G#maj13" + TagOrta + "G#maj13" + TagBitis, TagBaslangic + "G#maj9#11" + TagOrta + "G#maj9#11" + TagBitis, TagBaslangic + "G#maj13#11" + TagOrta + "G#maj13#11" + TagBitis, TagBaslangic + "G#6" + TagOrta + "G#6" + TagBitis, TagBaslangic + "G#add9" + TagOrta + "G#add9" + TagBitis, TagBaslangic + "G#6add9" + TagOrta + "G#6add9" + TagBitis, TagBaslangic + "G#maj7b5" + TagOrta + "G#maj7b5" + TagBitis, TagBaslangic + "G#maj7" + TagOrta + "G#maj7#5" + TagBitis, TagBaslangic + "G#m" + TagOrta + "G#m" + TagBitis, TagBaslangic + "G#m7" + TagOrta + "G#m7" + TagBitis, TagBaslangic + "G#m9" + TagOrta + "G#m9" + TagBitis, TagBaslangic + "G#m11" + TagOrta + "G#m11" + TagBitis, TagBaslangic + "G#m13" + TagOrta + "G#m13" + TagBitis, TagBaslangic + "G#m6" + TagOrta + "G#m6" + TagBitis, TagBaslangic + "G#madd9" + TagOrta + "G#madd9" + TagBitis, TagBaslangic + "G#m6add9" + TagOrta + "G#m6add9" + TagBitis, TagBaslangic + "G#mmaj7" + TagOrta + "G#mmaj7" + TagBitis, TagBaslangic + "G#mmaj9" + TagOrta + "G#mmaj9" + TagBitis, TagBaslangic + "G#m7b5" + TagOrta + "G#m7b5" + TagBitis, TagBaslangic + "G#m7#5" + TagOrta + "G#m7#5" + TagBitis, TagBaslangic + "G#7" + TagOrta + "G#7" + TagBitis, TagBaslangic + "G#9" + TagOrta + "G#9" + TagBitis, TagBaslangic + "G#11" + TagOrta + "G#11" + TagBitis, TagBaslangic + "G#13" + TagOrta + "G#13" + TagBitis, TagBaslangic + "G#7sus4" + TagOrta + "G#7sus4" + TagBitis, TagBaslangic + "G#7b5" + TagOrta + "G#7b5" + TagBitis, TagBaslangic + "G#7#5" + TagOrta + "G#7#5" + TagBitis, TagBaslangic + "G#7b9" + TagOrta + "G#7b9" + TagBitis, TagBaslangic + "G#7#9" + TagOrta + "G#7#9" + TagBitis, TagBaslangic + "G#7(b5,b9)" + TagOrta + "G#7(b5,b9)" + TagBitis, TagBaslangic + "G#7(b5,#9" + TagOrta + "G#7(b5,#9)" + TagBitis, TagBaslangic + "G#7(#5,b9)" + TagOrta + "G#7(#5,b9)" + TagBitis, TagBaslangic + "G#7(#5,#9)" + TagOrta + "G#7(#5,#9)" + TagBitis, TagBaslangic + "G#9b5" + TagOrta + "G#9b5" + TagBitis, TagBaslangic + "G#9#5" + TagOrta + "G#9#5" + TagBitis, TagBaslangic + "G#13#11" + TagOrta + "G#13#11" + TagBitis, TagBaslangic + "G#13b9" + TagOrta + "G#13b9" + TagBitis, TagBaslangic + "G#11b9" + TagOrta + "G#11b9" + TagBitis, TagBaslangic + "G#aug" + TagOrta + "G#aug" + TagBitis, TagBaslangic + "G#dim" + TagOrta + "G#dim" + TagBitis, TagBaslangic + "G#dim7" + TagOrta + "G#dim7" + TagBitis, TagBaslangic + "G#5" + TagOrta + "G#5" + TagBitis, TagBaslangic + "G#sus4" + TagOrta + "G#sus4" + TagBitis, TagBaslangic + "G#sus2" + TagOrta + "G#sus2" + TagBitis, TagBaslangic + "G#sus2sus4" + TagOrta + "G#sus2sus4" + TagBitis, TagBaslangic + "G#-5" + TagOrta + "G#-5" + TagBitis,
				TagBaslangic + "A" + TagOrta + "A" + TagBitis, TagBaslangic + "Amaj7" + TagOrta + "Amaj7" + TagBitis, TagBaslangic + "Amaj9" + TagOrta + "Amaj9" + TagBitis, TagBaslangic + "Amaj11" + TagOrta + "Amaj11" + TagBitis, TagBaslangic + "Amaj13" + TagOrta + "Amaj13" + TagBitis, TagBaslangic + "Amaj9#11" + TagOrta + "Amaj9#11" + TagBitis, TagBaslangic + "Amaj13#11" + TagOrta + "Amaj13#11" + TagBitis, TagBaslangic + "A6" + TagOrta + "A6" + TagBitis, TagBaslangic + "Aadd9" + TagOrta + "Aadd9" + TagBitis, TagBaslangic + "A6add9" + TagOrta + "A6add9" + TagBitis, TagBaslangic + "Amaj7b5" + TagOrta + "Amaj7b5" + TagBitis, TagBaslangic + "Amaj7" + TagOrta + "Amaj7#5" + TagBitis, TagBaslangic + "Am" + TagOrta + "Am" + TagBitis, TagBaslangic + "Am7" + TagOrta + "Am7" + TagBitis, TagBaslangic + "Am9" + TagOrta + "Am9" + TagBitis, TagBaslangic + "Am11" + TagOrta + "Am11" + TagBitis, TagBaslangic + "Am13" + TagOrta + "Am13" + TagBitis, TagBaslangic + "Am6" + TagOrta + "Am6" + TagBitis, TagBaslangic + "Amadd9" + TagOrta + "Amadd9" + TagBitis, TagBaslangic + "Am6add9" + TagOrta + "Am6add9" + TagBitis, TagBaslangic + "Ammaj7" + TagOrta + "Ammaj7" + TagBitis, TagBaslangic + "Ammaj9" + TagOrta + "Ammaj9" + TagBitis, TagBaslangic + "Am7b5" + TagOrta + "Am7b5" + TagBitis, TagBaslangic + "Am7#5" + TagOrta + "Am7#5" + TagBitis, TagBaslangic + "A7" + TagOrta + "A7" + TagBitis, TagBaslangic + "A9" + TagOrta + "A9" + TagBitis, TagBaslangic + "A11" + TagOrta + "A11" + TagBitis, TagBaslangic + "A13" + TagOrta + "A13" + TagBitis, TagBaslangic + "A7sus4" + TagOrta + "A7sus4" + TagBitis, TagBaslangic + "A7b5" + TagOrta + "A7b5" + TagBitis, TagBaslangic + "A7#5" + TagOrta + "A7#5" + TagBitis, TagBaslangic + "A7b9" + TagOrta + "A7b9" + TagBitis, TagBaslangic + "A7#9" + TagOrta + "A7#9" + TagBitis, TagBaslangic + "A7(b5,b9)" + TagOrta + "A7(b5,b9)" + TagBitis, TagBaslangic + "A7(b5,#9" + TagOrta + "A7(b5,#9)" + TagBitis, TagBaslangic + "A7(#5,b9)" + TagOrta + "A7(#5,b9)" + TagBitis, TagBaslangic + "A7(#5,#9)" + TagOrta + "A7(#5,#9)" + TagBitis, TagBaslangic + "A9b5" + TagOrta + "A9b5" + TagBitis, TagBaslangic + "A9#5" + TagOrta + "A9#5" + TagBitis, TagBaslangic + "A13#11" + TagOrta + "A13#11" + TagBitis, TagBaslangic + "A13b9" + TagOrta + "A13b9" + TagBitis, TagBaslangic + "A11b9" + TagOrta + "A11b9" + TagBitis, TagBaslangic + "Aaug" + TagOrta + "Aaug" + TagBitis, TagBaslangic + "Adim" + TagOrta + "Adim" + TagBitis, TagBaslangic + "Adim7" + TagOrta + "Adim7" + TagBitis, TagBaslangic + "A5" + TagOrta + "A5" + TagBitis, TagBaslangic + "Asus4" + TagOrta + "Asus4" + TagBitis, TagBaslangic + "Asus2" + TagOrta + "Asus2" + TagBitis, TagBaslangic + "Asus2sus4" + TagOrta + "Asus2sus4" + TagBitis, TagBaslangic + "A-5" + TagOrta + "A-5" + TagBitis,
				TagBaslangic + "A#" + TagOrta + "A#" + TagBitis, TagBaslangic + "A#maj7" + TagOrta + "A#maj7" + TagBitis, TagBaslangic + "A#maj9" + TagOrta + "A#maj9" + TagBitis, TagBaslangic + "A#maj11" + TagOrta + "A#maj11" + TagBitis, TagBaslangic + "A#maj13" + TagOrta + "A#maj13" + TagBitis, TagBaslangic + "A#maj9#11" + TagOrta + "A#maj9#11" + TagBitis, TagBaslangic + "A#maj13#11" + TagOrta + "A#maj13#11" + TagBitis, TagBaslangic + "A#6" + TagOrta + "A#6" + TagBitis, TagBaslangic + "A#add9" + TagOrta + "A#add9" + TagBitis, TagBaslangic + "A#6add9" + TagOrta + "A#6add9" + TagBitis, TagBaslangic + "A#maj7b5" + TagOrta + "A#maj7b5" + TagBitis, TagBaslangic + "A#maj7" + TagOrta + "A#maj7#5" + TagBitis, TagBaslangic + "A#m" + TagOrta + "A#m" + TagBitis, TagBaslangic + "A#m7" + TagOrta + "A#m7" + TagBitis, TagBaslangic + "A#m9" + TagOrta + "A#m9" + TagBitis, TagBaslangic + "A#m11" + TagOrta + "A#m11" + TagBitis, TagBaslangic + "A#m13" + TagOrta + "A#m13" + TagBitis, TagBaslangic + "A#m6" + TagOrta + "A#m6" + TagBitis, TagBaslangic + "A#madd9" + TagOrta + "A#madd9" + TagBitis, TagBaslangic + "A#m6add9" + TagOrta + "A#m6add9" + TagBitis, TagBaslangic + "A#mmaj7" + TagOrta + "A#mmaj7" + TagBitis, TagBaslangic + "A#mmaj9" + TagOrta + "A#mmaj9" + TagBitis, TagBaslangic + "A#m7b5" + TagOrta + "A#m7b5" + TagBitis, TagBaslangic + "A#m7#5" + TagOrta + "A#m7#5" + TagBitis, TagBaslangic + "A#7" + TagOrta + "A#7" + TagBitis, TagBaslangic + "A#9" + TagOrta + "A#9" + TagBitis, TagBaslangic + "A#11" + TagOrta + "A#11" + TagBitis, TagBaslangic + "A#13" + TagOrta + "A#13" + TagBitis, TagBaslangic + "A#7sus4" + TagOrta + "A#7sus4" + TagBitis, TagBaslangic + "A#7b5" + TagOrta + "A#7b5" + TagBitis, TagBaslangic + "A#7#5" + TagOrta + "A#7#5" + TagBitis, TagBaslangic + "A#7b9" + TagOrta + "A#7b9" + TagBitis, TagBaslangic + "A#7#9" + TagOrta + "A#7#9" + TagBitis, TagBaslangic + "A#7(b5,b9)" + TagOrta + "A#7(b5,b9)" + TagBitis, TagBaslangic + "A#7(b5,#9" + TagOrta + "A#7(b5,#9)" + TagBitis, TagBaslangic + "A#7(#5,b9)" + TagOrta + "A#7(#5,b9)" + TagBitis, TagBaslangic + "A#7(#5,#9)" + TagOrta + "A#7(#5,#9)" + TagBitis, TagBaslangic + "A#9b5" + TagOrta + "A#9b5" + TagBitis, TagBaslangic + "A#9#5" + TagOrta + "A#9#5" + TagBitis, TagBaslangic + "A#13#11" + TagOrta + "A#13#11" + TagBitis, TagBaslangic + "A#13b9" + TagOrta + "A#13b9" + TagBitis, TagBaslangic + "A#11b9" + TagOrta + "A#11b9" + TagBitis, TagBaslangic + "A#aug" + TagOrta + "A#aug" + TagBitis, TagBaslangic + "A#dim" + TagOrta + "A#dim" + TagBitis, TagBaslangic + "A#dim7" + TagOrta + "A#dim7" + TagBitis, TagBaslangic + "A#5" + TagOrta + "A#5" + TagBitis, TagBaslangic + "A#sus4" + TagOrta + "A#sus4" + TagBitis, TagBaslangic + "A#sus2" + TagOrta + "A#sus2" + TagBitis, TagBaslangic + "A#sus2sus4" + TagOrta + "A#sus2sus4" + TagBitis, TagBaslangic + "A#-5" + TagOrta + "A#-5" + TagBitis,
				TagBaslangic + "Bb" + TagOrta + "Bb" + TagBitis, TagBaslangic + "Bbmaj7" + TagOrta + "Bbmaj7" + TagBitis, TagBaslangic + "Bbmaj9" + TagOrta + "Bbmaj9" + TagBitis, TagBaslangic + "Bbmaj11" + TagOrta + "Bbmaj11" + TagBitis, TagBaslangic + "Bbmaj13" + TagOrta + "Bbmaj13" + TagBitis, TagBaslangic + "Bbmaj9#11" + TagOrta + "Bbmaj9#11" + TagBitis, TagBaslangic + "Bbmaj13#11" + TagOrta + "Bbmaj13#11" + TagBitis, TagBaslangic + "Bb6" + TagOrta + "Bb6" + TagBitis, TagBaslangic + "Bbadd9" + TagOrta + "Bbadd9" + TagBitis, TagBaslangic + "Bb6add9" + TagOrta + "Bb6add9" + TagBitis, TagBaslangic + "Bbmaj7b5" + TagOrta + "Bbmaj7b5" + TagBitis, TagBaslangic + "Bbmaj7" + TagOrta + "Bbmaj7#5" + TagBitis, TagBaslangic + "Bbm" + TagOrta + "Bbm" + TagBitis, TagBaslangic + "Bbm7" + TagOrta + "Bbm7" + TagBitis, TagBaslangic + "Bbm9" + TagOrta + "Bbm9" + TagBitis, TagBaslangic + "Bbm11" + TagOrta + "Bbm11" + TagBitis, TagBaslangic + "Bbm13" + TagOrta + "Bbm13" + TagBitis, TagBaslangic + "Bbm6" + TagOrta + "Bbm6" + TagBitis, TagBaslangic + "Bbmadd9" + TagOrta + "Bbmadd9" + TagBitis, TagBaslangic + "Bbm6add9" + TagOrta + "Bbm6add9" + TagBitis, TagBaslangic + "Bbmmaj7" + TagOrta + "Bbmmaj7" + TagBitis, TagBaslangic + "Bbmmaj9" + TagOrta + "Bbmmaj9" + TagBitis, TagBaslangic + "Bbm7b5" + TagOrta + "Bbm7b5" + TagBitis, TagBaslangic + "Bbm7#5" + TagOrta + "Bbm7#5" + TagBitis, TagBaslangic + "Bb7" + TagOrta + "Bb7" + TagBitis, TagBaslangic + "Bb9" + TagOrta + "Bb9" + TagBitis, TagBaslangic + "Bb11" + TagOrta + "Bb11" + TagBitis, TagBaslangic + "Bb13" + TagOrta + "Bb13" + TagBitis, TagBaslangic + "Bb7sus4" + TagOrta + "Bb7sus4" + TagBitis, TagBaslangic + "Bb7b5" + TagOrta + "Bb7b5" + TagBitis, TagBaslangic + "Bb7#5" + TagOrta + "Bb7#5" + TagBitis, TagBaslangic + "Bb7b9" + TagOrta + "Bb7b9" + TagBitis, TagBaslangic + "Bb7#9" + TagOrta + "Bb7#9" + TagBitis, TagBaslangic + "Bb7(b5,b9)" + TagOrta + "Bb7(b5,b9)" + TagBitis, TagBaslangic + "Bb7(b5,#9" + TagOrta + "Bb7(b5,#9)" + TagBitis, TagBaslangic + "Bb7(#5,b9)" + TagOrta + "Bb7(#5,b9)" + TagBitis, TagBaslangic + "Bb7(#5,#9)" + TagOrta + "Bb7(#5,#9)" + TagBitis, TagBaslangic + "Bb9b5" + TagOrta + "Bb9b5" + TagBitis, TagBaslangic + "Bb9#5" + TagOrta + "Bb9#5" + TagBitis, TagBaslangic + "Bb13#11" + TagOrta + "Bb13#11" + TagBitis, TagBaslangic + "Bb13b9" + TagOrta + "Bb13b9" + TagBitis, TagBaslangic + "Bb11b9" + TagOrta + "Bb11b9" + TagBitis, TagBaslangic + "Bbaug" + TagOrta + "Bbaug" + TagBitis, TagBaslangic + "Bbdim" + TagOrta + "Bbdim" + TagBitis, TagBaslangic + "Bbdim7" + TagOrta + "Bbdim7" + TagBitis, TagBaslangic + "Bb5" + TagOrta + "Bb5" + TagBitis, TagBaslangic + "Bbsus4" + TagOrta + "Bbsus4" + TagBitis, TagBaslangic + "Bbsus2" + TagOrta + "Bbsus2" + TagBitis, TagBaslangic + "Bbsus2sus4" + TagOrta + "Bbsus2sus4" + TagBitis, TagBaslangic + "Bb-5" + TagOrta + "Bb-5" + TagBitis,
				TagBaslangic + "B" + TagOrta + "B" + TagBitis, TagBaslangic + "Bmaj7" + TagOrta + "Bmaj7" + TagBitis, TagBaslangic + "Bmaj9" + TagOrta + "Bmaj9" + TagBitis, TagBaslangic + "Bmaj11" + TagOrta + "Bmaj11" + TagBitis, TagBaslangic + "Bmaj13" + TagOrta + "Bmaj13" + TagBitis, TagBaslangic + "Bmaj9#11" + TagOrta + "Bmaj9#11" + TagBitis, TagBaslangic + "Bmaj13#11" + TagOrta + "Bmaj13#11" + TagBitis, TagBaslangic + "B6" + TagOrta + "B6" + TagBitis, TagBaslangic + "Badd9" + TagOrta + "Badd9" + TagBitis, TagBaslangic + "B6add9" + TagOrta + "B6add9" + TagBitis, TagBaslangic + "Bmaj7b5" + TagOrta + "Bmaj7b5" + TagBitis, TagBaslangic + "Bmaj7" + TagOrta + "Bmaj7#5" + TagBitis, TagBaslangic + "Bm" + TagOrta + "Bm" + TagBitis, TagBaslangic + "Bm7" + TagOrta + "Bm7" + TagBitis, TagBaslangic + "Bm9" + TagOrta + "Bm9" + TagBitis, TagBaslangic + "Bm11" + TagOrta + "Bm11" + TagBitis, TagBaslangic + "Bm13" + TagOrta + "Bm13" + TagBitis, TagBaslangic + "Bm6" + TagOrta + "Bm6" + TagBitis, TagBaslangic + "Bmadd9" + TagOrta + "Bmadd9" + TagBitis, TagBaslangic + "Bm6add9" + TagOrta + "Bm6add9" + TagBitis, TagBaslangic + "Bmmaj7" + TagOrta + "Bmmaj7" + TagBitis, TagBaslangic + "Bmmaj9" + TagOrta + "Bmmaj9" + TagBitis, TagBaslangic + "Bm7b5" + TagOrta + "Bm7b5" + TagBitis, TagBaslangic + "Bm7#5" + TagOrta + "Bm7#5" + TagBitis, TagBaslangic + "B7" + TagOrta + "B7" + TagBitis, TagBaslangic + "B9" + TagOrta + "B9" + TagBitis, TagBaslangic + "B11" + TagOrta + "B11" + TagBitis, TagBaslangic + "B13" + TagOrta + "B13" + TagBitis, TagBaslangic + "B7sus4" + TagOrta + "B7sus4" + TagBitis, TagBaslangic + "B7b5" + TagOrta + "B7b5" + TagBitis, TagBaslangic + "B7#5" + TagOrta + "B7#5" + TagBitis, TagBaslangic + "B7b9" + TagOrta + "B7b9" + TagBitis, TagBaslangic + "B7#9" + TagOrta + "B7#9" + TagBitis, TagBaslangic + "B7(b5,b9)" + TagOrta + "B7(b5,b9)" + TagBitis, TagBaslangic + "B7(b5,#9" + TagOrta + "B7(b5,#9)" + TagBitis, TagBaslangic + "B7(#5,b9)" + TagOrta + "B7(#5,b9)" + TagBitis, TagBaslangic + "B7(#5,#9)" + TagOrta + "B7(#5,#9)" + TagBitis, TagBaslangic + "B9b5" + TagOrta + "B9b5" + TagBitis, TagBaslangic + "B9#5" + TagOrta + "B9#5" + TagBitis, TagBaslangic + "B13#11" + TagOrta + "B13#11" + TagBitis, TagBaslangic + "B13b9" + TagOrta + "B13b9" + TagBitis, TagBaslangic + "B11b9" + TagOrta + "B11b9" + TagBitis, TagBaslangic + "Baug" + TagOrta + "Baug" + TagBitis, TagBaslangic + "Bdim" + TagOrta + "Bdim" + TagBitis, TagBaslangic + "Bdim7" + TagOrta + "Bdim7" + TagBitis, TagBaslangic + "B5" + TagOrta + "B5" + TagBitis, TagBaslangic + "Bsus4" + TagOrta + "Bsus4" + TagBitis, TagBaslangic + "Bsus2" + TagOrta + "Bsus2" + TagBitis, TagBaslangic + "Bsus2sus4" + TagOrta + "Bsus2sus4" + TagBitis, TagBaslangic + "B-5" + TagOrta + "B-5" + TagBitis
		};

		for (int i = 0; i <= Notalar.length - 1; i++) {
			while ((BaslangicNo <= Icerik.length()) && (Deger > -1)) {
				Deger = Icerik.indexOf(Notalar[i], BaslangicNo);

				if (Deger == -1) break;
				else NotaKordinatlari.add(Deger);

				BaslangicNo = Deger + 1;
			}

			BaslangicNo = 0;
			Deger = 0;
		}

		Collections.sort(NotaKordinatlari);

		String BulunanTon;

		for (int kere = 0; kere < Integer.parseInt(TranspozeIslem.substring(1, TranspozeIslem.length())); kere++) {
			if (TranspozeIslem.substring(0, 1).equals("+")) {
				for (int i = 0; i <= NotaKordinatlari.size() - 1; i++) {
					SpannableStringBuilder Akor = (SpannableStringBuilder) SBTranspozeEdilenIcerik.subSequence(NotaKordinatlari.get(i), SBTranspozeEdilenIcerik.toString().indexOf(TagBitis, NotaKordinatlari.get(i)) + TagBitis.length());
					BulunanTon = SBTranspozeEdilenIcerik.toString().substring(SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length(), SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length() + 2);

					PartIcerik1 = (SpannableStringBuilder) SBTranspozeEdilenIcerik.subSequence(0, NotaKordinatlari.get(i));
					PartIcerik2 = (SpannableStringBuilder) SBTranspozeEdilenIcerik.subSequence(SBTranspozeEdilenIcerik.toString().indexOf(TagBitis, NotaKordinatlari.get(i)) + TagBitis.length(), SBTranspozeEdilenIcerik.length());

					if (BulunanTon.contains("#")) { // Ton içinde diyez var ise
						switch (BulunanTon) {
							case "C#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "D") + PartIcerik2);
								break;
							case "D#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "E") + PartIcerik2);
								break;
							case "F#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "G") + PartIcerik2);
								break;
							case "G#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "A") + PartIcerik2);
								break;
							case "A#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "B") + PartIcerik2);
								break;
						}

						for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
							NotaKordinatlari.set(x, NotaKordinatlari.get(x) - 2);
						}
					} else if (BulunanTon.contains("b")) {
						if (BulunanTon.equals("Bb")) {
							SBTranspozeEdilenIcerik.clear();
							SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "B") + PartIcerik2);
						}

						for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
							NotaKordinatlari.set(x, NotaKordinatlari.get(x) - 2);
						}
					} else {
						BulunanTon = SBTranspozeEdilenIcerik.toString().substring(SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length(), SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length() + 1);

						switch (BulunanTon) {
							case "C":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "C#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "D":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "D#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "E":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "F") + PartIcerik2);
								break;
							case "F":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "F#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "G":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "G#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "A":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "A#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "B":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "C") + PartIcerik2);
								break;
						}

					}
				}
			} else if (TranspozeIslem.substring(0,1).equals("-")) {
				for (int i = 0; i <= NotaKordinatlari.size() - 1; i++) {
					SpannableStringBuilder Akor = (SpannableStringBuilder) SBTranspozeEdilenIcerik.subSequence(NotaKordinatlari.get(i), SBTranspozeEdilenIcerik.toString().indexOf(TagBitis, NotaKordinatlari.get(i)) + TagBitis.length());
					BulunanTon = SBTranspozeEdilenIcerik.toString().substring(SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length(), SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length() + 2);

					PartIcerik1 = (SpannableStringBuilder) SBTranspozeEdilenIcerik.subSequence(0, NotaKordinatlari.get(i));
					PartIcerik2 = (SpannableStringBuilder) SBTranspozeEdilenIcerik.subSequence(SBTranspozeEdilenIcerik.toString().indexOf(TagBitis, NotaKordinatlari.get(i)) + TagBitis.length(), SBTranspozeEdilenIcerik.length());

					if (BulunanTon.contains("#")) {
						switch (BulunanTon) {
							case "C#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "C") + PartIcerik2);
								break;
							case "D#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "D") + PartIcerik2);
								break;
							case "F#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "F") + PartIcerik2);
								break;
							case "G#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "G") + PartIcerik2);
								break;
							case "A#":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "A") + PartIcerik2);
								break;
						}

						for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
							NotaKordinatlari.set(x, NotaKordinatlari.get(x) - 2);
						}
					} else if (BulunanTon.contains("b")) {
						if (BulunanTon.equals("Bb")) {
							SBTranspozeEdilenIcerik.clear();
							SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "A") + PartIcerik2);
						}

						for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
							NotaKordinatlari.set(x, NotaKordinatlari.get(x) - 2);
						}
					} else {
						BulunanTon = SBTranspozeEdilenIcerik.toString().substring(SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length(), SBTranspozeEdilenIcerik.toString().indexOf(TagOrta, NotaKordinatlari.get(i)) + TagOrta.length() + 1);

						switch (BulunanTon) {
							case "C":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "B") + PartIcerik2);
								break;
							case "D":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "C#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "E":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "D#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "F":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "E") + PartIcerik2);
								break;
							case "G":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "F#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "A":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "G#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
							case "B":
								SBTranspozeEdilenIcerik.clear();
								SBTranspozeEdilenIcerik.append(PartIcerik1 + TagBaslangic + Akor.subSequence(TagBaslangic.length(), Akor.toString().length()).toString().replace(BulunanTon, "A#") + PartIcerik2);

								for (int x = 0; x <= NotaKordinatlari.size() - 1; x++) {
									NotaKordinatlari.set(x, NotaKordinatlari.get(x) + 2);
								}
								break;
						}
					}
				}
			}
		}

		NotaKordinatlari.clear();

		return SBTranspozeEdilenIcerik;
	}

	public void AkorCetveli(final Activity activity, String SecilenAkor) {
		final Typeface YaziFontu = FontGetir(activity, "anivers_regular");
        LayoutInflater inflater = activity.getLayoutInflater();
        View ViewDialogContent = inflater.inflate(R.layout.dialog_akor_gosterici, null);

        final AlertDialog ADDialogAkorGosterici = CustomAlertDialog(activity, R.mipmap.ic_launcher, activity.getString(R.string.akor_cetveli, SecilenAkor.equals("") ? "" : " - " + SecilenAkor + " " + activity.getString(R.string.akoru)), ViewDialogContent, activity.getString(R.string.kapat));
        ADDialogAkorGosterici.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

		TextView Dialog_txtTon = ViewDialogContent.findViewById(R.id.Dialog_txtTon);
		Dialog_txtTon.setTypeface(YaziFontu, Typeface.BOLD);

		TextView Dialog_txtAkor = ViewDialogContent.findViewById(R.id.Dialog_txtAkor);
		Dialog_txtAkor.setTypeface(YaziFontu, Typeface.BOLD);

        final ListView Dialog_lstTonlar = ViewDialogContent.findViewById(R.id.Dialog_lstTonlar);
        final ListView Dialog_lstAkorlar = ViewDialogContent.findViewById(R.id.Dialog_lstAkorlar);
        final ImageView Dialog_ImgGitarKlavye = ViewDialogContent.findViewById(R.id.Dialog_ImgGitarKlavye);
		
		String SecilenTon;
		final String[] SecilenAkorDizisi = {"0,0,0,0,0,0"};
		Boolean AkorBulunduMu = false;
		final List<SnfAkorlar> snfAkorlar = new ArrayList<>();
		AdpTonlar AdpTonlar = null;
		AdpAkorlar AdpAkorlar = null;
		
		if(!SecilenAkor.equals("")) {
			if(SecilenAkor.length() < 2) SecilenTon = SecilenAkor;
			else SecilenTon = SecilenAkor.substring(0,2).contains("#") ? SecilenAkor.substring(0,2) : SecilenAkor.substring(0,1);
		} else {
			SecilenTon = "";
		}

		try {
			JSONArray JSONArrTonlar;
			List<SnfTonlar> snfTonlar = new ArrayList<>();
			SnfTonlar Tonlar;

			for(int x = 0; x < 12; x++) {
				JSONArrTonlar = new JSONArray(new JSONObject(new JSONObject(JSONTonAkorGetir(x)).getString("AkorCetveli")).getString("Tonlar"));

				for(int i = 0; i < JSONArrTonlar.length(); i++) {
					Tonlar = new SnfTonlar();
					Tonlar.setTonAdi(new JSONObject(JSONArrTonlar.getString(i)).getString("TonAdi"));

					if(SecilenTon.equals(new JSONObject(JSONArrTonlar.getString(i)).getString("TonAdi"))) Tonlar.setSecim(true);
					else Tonlar.setSecim(false);

					snfTonlar.add(Tonlar);
				}
			}

			AdpTonlar = new AdpTonlar(activity, snfTonlar);
			Dialog_lstTonlar.setAdapter(AdpTonlar);
		} catch (JSONException e) {
			e.printStackTrace();
		}

        try {
            snfAkorlar.clear();
            SnfAkorlar Akorlar;
			Boolean IlkAkorSecildiMi = false;

			JSONArray JSONArrTonlar;
			JSONArray JSONArrAkorlar = null;

			for(int x = 0; x < 12; x++) {
				JSONArrTonlar = new JSONArray(new JSONObject(new JSONObject(JSONTonAkorGetir(x)).getString("AkorCetveli")).getString("Tonlar"));

				for(int i = 0; i < JSONArrTonlar.length(); i++) {
					if(new JSONObject(JSONArrTonlar.getString(i)).getString("TonAdi").equals(SecilenTon)) {
						JSONArrAkorlar = new JSONArray(new JSONObject(JSONArrTonlar.getString(i)).getString("Akorlari"));
						AkorBulunduMu = true;
					}
				}
			}

            for(int i = 0; i < JSONArrAkorlar.length(); i++) {
                Akorlar = new SnfAkorlar();
                Akorlar.setAkorAdi(new JSONObject(JSONArrAkorlar.getString(i)).getString("AkorAdi"));
                Akorlar.setDizi(new JSONObject(JSONArrAkorlar.getString(i)).getString("Dizi"));

				if(!SecilenAkor.equals("") && AkorBulunduMu) Akorlar.setSecimBG(true);
				else Akorlar.setSecimBG(false);

				if(SecilenAkor.equals(new JSONObject(JSONArrAkorlar.getString(i)).getString("AkorAdi"))) {
					if(!IlkAkorSecildiMi) {
						Akorlar.setSecimYazi(true);
						IlkAkorSecildiMi = true;

						SecilenAkorDizisi[0] = new JSONObject(JSONArrAkorlar.getString(i)).getString("Dizi");
					} else Akorlar.setSecimYazi(false);
				} else Akorlar.setSecimYazi(false);

                snfAkorlar.add(Akorlar);
            }

            AdpAkorlar = new AdpAkorlar(activity, snfAkorlar);
			Dialog_lstAkorlar.setAdapter(AdpAkorlar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

		Dialog_ImgGitarKlavye.setImageBitmap(GitarUzerindeAkorGetir(activity, ADDialogAkorGosterici, YaziFontu, SecilenAkorDizisi[0]));
        Dialog_ImgGitarKlavye.setScaleType(ImageView.ScaleType.FIT_XY);

		final AdpTonlar AdpTonlar2 = AdpTonlar;
		final AdpAkorlar[] AdpAkorlar2 = {AdpAkorlar};
		Dialog_lstTonlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
				AdpTonlar2.setSelectable(position);

				try {
					snfAkorlar.clear();
					SnfAkorlar Akorlar;
					Boolean IlkAkorSecildiMi = false;

					JSONArray JSONArrAkorlar = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(JSONTonAkorGetir(position)).getString("AkorCetveli")).getString("Tonlar")).getString(0)).getString("Akorlari"));

					for(int i = 0; i < JSONArrAkorlar.length(); i++) {
						Akorlar = new SnfAkorlar();
						Akorlar.setAkorAdi(new JSONObject(JSONArrAkorlar.getString(i)).getString("AkorAdi"));
						Akorlar.setDizi(new JSONObject(JSONArrAkorlar.getString(i)).getString("Dizi"));
						Akorlar.setSecimBG(true);

						if(!IlkAkorSecildiMi) {
							Akorlar.setSecimYazi(true);
							IlkAkorSecildiMi = true;

							SecilenAkorDizisi[0] = new JSONObject(JSONArrAkorlar.getString(i)).getString("Dizi");
						} else Akorlar.setSecimYazi(false);

						snfAkorlar.add(Akorlar);
					}

					AdpAkorlar2[0] = new AdpAkorlar(activity, snfAkorlar);
					Dialog_lstAkorlar.setAdapter(AdpAkorlar2[0]);

					Dialog_ImgGitarKlavye.setImageBitmap(GitarUzerindeAkorGetir(activity, ADDialogAkorGosterici, YaziFontu, SecilenAkorDizisi[0]));
					Dialog_ImgGitarKlavye.setScaleType(ImageView.ScaleType.FIT_XY);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		Dialog_lstAkorlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
				AdpAkorlar2[0].setSelectableYazi(position);

				String AkorDizi = snfAkorlar.get(position).getDizi();

				//ToastMsj(activity, AkorDizi, Toast.LENGTH_SHORT);

                Dialog_ImgGitarKlavye.setImageBitmap(GitarUzerindeAkorGetir(activity, ADDialogAkorGosterici, YaziFontu, AkorDizi));
                Dialog_ImgGitarKlavye.setScaleType(ImageView.ScaleType.FIT_XY);
			}
		});

		ADDialogAkorGosterici.show();

		ADDialogAkorGosterici.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ADDialogAkorGosterici.dismiss();
			}
		});

		if(!AkorBulunduMu) ToastMsj(activity, activity.getString(R.string.akor_bulunamadi), Toast.LENGTH_LONG);
	}

	private String JSONTonAkorGetir(int i) {
		String JSONTonAkorData = null;

		switch (i){
			case 0:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"A\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"A\", \"Dizi\": \"x,0,2,2,2,0\"},\n" +
						"{\"AkorAdi\": \"A\", \"Dizi\": \"x,0,2,2,2,5\"},\n" +
						"{\"AkorAdi\": \"A\", \"Dizi\": \"5,7,7,6,5,5\"},\n" +
						"{\"AkorAdi\": \"A\", \"Dizi\": \"x,7,7,9,10,9\"},\n" +
						"{\"AkorAdi\": \"Am\", \"Dizi\": \"x,0,2,2,1,0\"},\n" +
						"{\"AkorAdi\": \"Am\", \"Dizi\": \"x,0,2,5,5,5\"},\n" +
						"{\"AkorAdi\": \"Am\", \"Dizi\": \"5,7,7,5,5,5\"},\n" +
						"{\"AkorAdi\": \"Am\", \"Dizi\": \"x,7,7,9,10,8\"},\n" +
						"{\"AkorAdi\": \"Adim\", \"Dizi\": \"x,0,1,2,1,2\"},\n" +
						"{\"AkorAdi\": \"Adim\", \"Dizi\": \"x,0,4,5,4,5\"},\n" +
						"{\"AkorAdi\": \"Adim\", \"Dizi\": \"5,6,7,5,7,5\"},\n" +
						"{\"AkorAdi\": \"Adim\", \"Dizi\": \"8,9,10,8,10,8\"},\n" +
						"{\"AkorAdi\": \"Asus4\", \"Dizi\": \"x,0,2,2,3,0\"},\n" +
						"{\"AkorAdi\": \"Asus4\", \"Dizi\": \"x,0,2,2,3,5\"},\n" +
						"{\"AkorAdi\": \"Asus4\", \"Dizi\": \"5,5,7,7,5,5\"},\n" +
						"{\"AkorAdi\": \"Asus4\", \"Dizi\": \"x,7,7,9,10,10\"},\n" +
						"{\"AkorAdi\": \"A7sus4\", \"Dizi\": \"x,0,2,0,3,0\"},\n" +
						"{\"AkorAdi\": \"A7sus4\", \"Dizi\": \"x,0,2,2,3,3\"},\n" +
						"{\"AkorAdi\": \"A7sus4\", \"Dizi\": \"5,7,5,7,5,5\"},\n" +
						"{\"AkorAdi\": \"A7sus4\", \"Dizi\": \"x,12,12,12,10,10\"},\n" +
						"{\"AkorAdi\": \"A-5\", \"Dizi\": \"x,0,1,2,2,x\"},\n" +
						"{\"AkorAdi\": \"A-5\", \"Dizi\": \"x,6,7,6,4,x\"},\n" +
						"{\"AkorAdi\": \"A-5\", \"Dizi\": \"x,x,11,8,10,9\"},\n" +
						"{\"AkorAdi\": \"A-5\", \"Dizi\": \"11,12,11,14,[14],x\"},\n" +
						"{\"AkorAdi\": \"A+5\", \"Dizi\": \"x,0,3,2,2,1\"},\n" +
						"{\"AkorAdi\": \"A+5\", \"Dizi\": \"x,4,3,2,2,x\"},\n" +
						"{\"AkorAdi\": \"A+5\", \"Dizi\": \"x,8,7,6,6,x\"},\n" +
						"{\"AkorAdi\": \"A+5\", \"Dizi\": \"x,x,11,10,10,9\"},\n" +
						"{\"AkorAdi\": \"A6\", \"Dizi\": \"x,0,2,2,2,2\"},\n" +
						"{\"AkorAdi\": \"A6\", \"Dizi\": \"2,4,2,2,5,2\"},\n" +
						"{\"AkorAdi\": \"A6\", \"Dizi\": \"x,7,7,6,7,x\"},\n" +
						"{\"AkorAdi\": \"A6\", \"Dizi\": \"9,9,11,9,10,9\"},\n" +
						"{\"AkorAdi\": \"A69\", \"Dizi\": \"5,4,4,4,5,5\"},\n" +
						"{\"AkorAdi\": \"A69\", \"Dizi\": \"x,7,7,6,7,7\"},\n" +
						"{\"AkorAdi\": \"A69\", \"Dizi\": \"x,12,11,11,12,12\"},\n" +
						"{\"AkorAdi\": \"A7\", \"Dizi\": \"x,0,2,0,2,0\"},\n" +
						"{\"AkorAdi\": \"A7\", \"Dizi\": \"x,0,2,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A7\", \"Dizi\": \"5,7,5,6,5,5\"},\n" +
						"{\"AkorAdi\": \"A7\", \"Dizi\": \"x,7,7,9,8,9\"},\n" +
						"{\"AkorAdi\": \"A7-5\", \"Dizi\": \"x,0,1,0,2,3\"},\n" +
						"{\"AkorAdi\": \"A7-5\", \"Dizi\": \"x,4,5,6,4,5\"},\n" +
						"{\"AkorAdi\": \"A7-5\", \"Dizi\": \"x,x,7,8,8,9\"},\n" +
						"{\"AkorAdi\": \"A7-5\", \"Dizi\": \"x,10,11,12,10,11\"},\n" +
						"{\"AkorAdi\": \"A7+5\", \"Dizi\": \"x,0,3,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A7+5\", \"Dizi\": \"x,0,5,6,6,5\"},\n" +
						"{\"AkorAdi\": \"A7+5\", \"Dizi\": \"x,x,7,10,8,9\"},\n" +
						"{\"AkorAdi\": \"A7+5\", \"Dizi\": \"x,x,11,12,10,13\"},\n" +
						"{\"AkorAdi\": \"A9\", \"Dizi\": \"x,0,2,4,2,3\"},\n" +
						"{\"AkorAdi\": \"A9\", \"Dizi\": \"x,2,2,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A9\", \"Dizi\": \"5,7,5,6,5,7\"},\n" +
						"{\"AkorAdi\": \"A9\", \"Dizi\": \"7,7,7,9,8,9\"},\n" +
						"{\"AkorAdi\": \"A9-5\", \"Dizi\": \"x,2,1,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A9-5\", \"Dizi\": \"5,4,5,4,4,5\"},\n" +
						"{\"AkorAdi\": \"A9-5\", \"Dizi\": \"x,6,7,6,8,7\"},\n" +
						"{\"AkorAdi\": \"A9-5\", \"Dizi\": \"x,0,9,8,8,9\"},\n" +
						"{\"AkorAdi\": \"A9+5\", \"Dizi\": \"x,2,3,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A9+5\", \"Dizi\": \"x,4,5,4,6,5\"},\n" +
						"{\"AkorAdi\": \"A9+5\", \"Dizi\": \"9,10,9,10,10,9\"},\n" +
						"{\"AkorAdi\": \"A9+5\", \"Dizi\": \"x,12,11,12,12,13\"},\n" +
						"{\"AkorAdi\": \"A7-9\", \"Dizi\": \"x,1,2,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A7-9\", \"Dizi\": \"5,7,5,6,5,6\"},\n" +
						"{\"AkorAdi\": \"A7-9\", \"Dizi\": \"6,7,7,6,8,6\"},\n" +
						"{\"AkorAdi\": \"A7-9\", \"Dizi\": \"x,12,11,12,11,12\"},\n" +
						"{\"AkorAdi\": \"A7+9\", \"Dizi\": \"5,7,5,6,8,8\"},\n" +
						"{\"AkorAdi\": \"A7+9\", \"Dizi\": \"x,7,7,6,8,8\"},\n" +
						"{\"AkorAdi\": \"A7+9\", \"Dizi\": \"8,10,11,9,8,8\"},\n" +
						"{\"AkorAdi\": \"A7+9\", \"Dizi\": \"x,12,11,12,13,x\"},\n" +
						"{\"AkorAdi\": \"A11\", \"Dizi\": \"x,0,0,0,2,0\"},\n" +
						"{\"AkorAdi\": \"A11\", \"Dizi\": \"x,4,2,2,3,3\"},\n" +
						"{\"AkorAdi\": \"A11\", \"Dizi\": \"5,5,5,5,6,7\"},\n" +
						"{\"AkorAdi\": \"A11\", \"Dizi\": \"x,12,11,12,10,10\"},\n" +
						"{\"AkorAdi\": \"A+11\", \"Dizi\": \"x,2,1,2,2,3\"},\n" +
						"{\"AkorAdi\": \"A+11\", \"Dizi\": \"5,4,5,4,4,5\"},\n" +
						"{\"AkorAdi\": \"A+11\", \"Dizi\": \"7,7,7,8,8,9\"},\n" +
						"{\"AkorAdi\": \"A+11\", \"Dizi\": \"x,12,11,12,12,11\"},\n" +
						"{\"AkorAdi\": \"A13\", \"Dizi\": \"x,0,2,0,2,2\"},\n" +
						"{\"AkorAdi\": \"A13\", \"Dizi\": \"5,7,5,6,7,5\"},\n" +
						"{\"AkorAdi\": \"A13\", \"Dizi\": \"5,5,5,6,7,7\"},\n" +
						"{\"AkorAdi\": \"A13\", \"Dizi\": \"x,10,11,11,10,x\"},\n" +
						"{\"AkorAdi\": \"AM7\", \"Dizi\": \"x,0,2,1,2,0\"},\n" +
						"{\"AkorAdi\": \"AM7\", \"Dizi\": \"x,0,2,2,2,4\"},\n" +
						"{\"AkorAdi\": \"AM7\", \"Dizi\": \"5,7,6,6,5,5\"},\n" +
						"{\"AkorAdi\": \"AM7\", \"Dizi\": \"x,7,7,9,9,9\"},\n" +
						"{\"AkorAdi\": \"AM7-5\", \"Dizi\": \"x,0,1,1,2,4\"},\n" +
						"{\"AkorAdi\": \"AM7-5\", \"Dizi\": \"x,0,1,2,2,4\"},\n" +
						"{\"AkorAdi\": \"AM7-5\", \"Dizi\": \"5,6,6,6,x,x\"},\n" +
						"{\"AkorAdi\": \"AM7-5\", \"Dizi\": \"x,x,7,8,9,9\"},\n" +
						"{\"AkorAdi\": \"AM7+5\", \"Dizi\": \"x,0,3,1,2,1\"},\n" +
						"{\"AkorAdi\": \"AM7+5\", \"Dizi\": \"x,0,3,2,2,4\"},\n" +
						"{\"AkorAdi\": \"AM7+5\", \"Dizi\": \"x,8,7,6,9,9\"},\n" +
						"{\"AkorAdi\": \"AM7+5\", \"Dizi\": \"9,12,11,10,9,9\"},\n" +
						"{\"AkorAdi\": \"AM9\", \"Dizi\": \"x,2,2,2,2,4\"},\n" +
						"{\"AkorAdi\": \"AM9\", \"Dizi\": \"x,4,6,4,5,5\"},\n" +
						"{\"AkorAdi\": \"AM9\", \"Dizi\": \"x,0,6,6,5,7\"},\n" +
						"{\"AkorAdi\": \"AM9\", \"Dizi\": \"12,12,11,13,12,x\"},\n" +
						"{\"AkorAdi\": \"AM11\", \"Dizi\": \"x,0,0,1,0,0\"},\n" +
						"{\"AkorAdi\": \"AM11\", \"Dizi\": \"x,2,2,4,3,4\"},\n" +
						"{\"AkorAdi\": \"AM11\", \"Dizi\": \"5,5,6,6,5,7\"},\n" +
						"{\"AkorAdi\": \"AM11\", \"Dizi\": \"x,x,11,13,12,10\"},\n" +
						"{\"AkorAdi\": \"AM13\", \"Dizi\": \"x,0,2,1,2,2\"},\n" +
						"{\"AkorAdi\": \"AM13\", \"Dizi\": \"5,4,4,4,5,4\"},\n" +
						"{\"AkorAdi\": \"AM13\", \"Dizi\": \"x,9,7,7,9,9\"},\n" +
						"{\"AkorAdi\": \"AM13\", \"Dizi\": \"x,12,11,11,9,9\"},\n" +
						"{\"AkorAdi\": \"Am6\", \"Dizi\": \"x,0,2,2,1,2\"},\n" +
						"{\"AkorAdi\": \"Am6\", \"Dizi\": \"x,0,4,5,5,5\"},\n" +
						"{\"AkorAdi\": \"Am6\", \"Dizi\": \"x,7,7,5,7,5\"},\n" +
						"{\"AkorAdi\": \"Am6\", \"Dizi\": \"x,7,7,9,7,8\"},\n" +
						"{\"AkorAdi\": \"Am69\", \"Dizi\": \"x,3,4,4,5,5\"},\n" +
						"{\"AkorAdi\": \"Am69\", \"Dizi\": \"x,7,7,5,7,7\"},\n" +
						"{\"AkorAdi\": \"Am69\", \"Dizi\": \"8,7,7,9,7,7\"},\n" +
						"{\"AkorAdi\": \"Am69\", \"Dizi\": \"x,12,10,11,12,12\"},\n" +
						"{\"AkorAdi\": \"Am7\", \"Dizi\": \"x,0,2,2,1,3\"},\n" +
						"{\"AkorAdi\": \"Am7\", \"Dizi\": \"0,0,2,0,1,0\"},\n" +
						"{\"AkorAdi\": \"Am7\", \"Dizi\": \"5,7,5,5,5,5\"},\n" +
						"{\"AkorAdi\": \"Am7\", \"Dizi\": \"x,7,7,9,8,8\"},\n" +
						"{\"AkorAdi\": \"Am7-5\", \"Dizi\": \"x,0,1,0,1,x\"},\n" +
						"{\"AkorAdi\": \"Am7-5\", \"Dizi\": \"x,0,1,2,1,3\"},\n" +
						"{\"AkorAdi\": \"Am7-5\", \"Dizi\": \"3,3,5,5,4,5\"},\n" +
						"{\"AkorAdi\": \"Am7-5\", \"Dizi\": \"x,x,7,8,8,8\"},\n" +
						"{\"AkorAdi\": \"Am9\", \"Dizi\": \"x,2,2,2,1,3\"},\n" +
						"{\"AkorAdi\": \"Am9\", \"Dizi\": \"5,7,5,5,5,7\"},\n" +
						"{\"AkorAdi\": \"Am9\", \"Dizi\": \"8,10,9,9,10,8\"},\n" +
						"{\"AkorAdi\": \"Am9\", \"Dizi\": \"x,12,10,12,12,12\"},\n" +
						"{\"AkorAdi\": \"Am11\", \"Dizi\": \"5,3,5,4,3,3\"},\n" +
						"{\"AkorAdi\": \"Am11\", \"Dizi\": \"5,5,5,5,5,7\"},\n" +
						"{\"AkorAdi\": \"Am11\", \"Dizi\": \"8,7,7,7,8,7\"},\n" +
						"{\"AkorAdi\": \"Am11\", \"Dizi\": \"x,12,10,12,12,10\"},\n" +
						"{\"AkorAdi\": \"AmM7\", \"Dizi\": \"x,0,2,1,1,0\"},\n" +
						"{\"AkorAdi\": \"AmM7\", \"Dizi\": \"5,7,6,5,5,5\"},\n" +
						"{\"AkorAdi\": \"AmM7\", \"Dizi\": \"x,7,7,9,9,8\"},\n" +
						"{\"AkorAdi\": \"AmM7\", \"Dizi\": \"x,11,10,9,10,x\"},\n" +
						"{\"AkorAdi\": \"AmM7-5\", \"Dizi\": \"x,0,1,1,1,4\"},\n" +
						"{\"AkorAdi\": \"AmM7-5\", \"Dizi\": \"5,6,6,5,x,5\"},\n" +
						"{\"AkorAdi\": \"AmM7-5\", \"Dizi\": \"x,x,7,8,9,8\"},\n" +
						"{\"AkorAdi\": \"AmM7-5\", \"Dizi\": \"11,12,13,13,13,x\"},\n" +
						"{\"AkorAdi\": \"AmM9\", \"Dizi\": \"x,3,2,1,0,0\"},\n" +
						"{\"AkorAdi\": \"AmM9\", \"Dizi\": \"x,3,x,4,5,4\"},\n" +
						"{\"AkorAdi\": \"AmM9\", \"Dizi\": \"5,7,6,5,5,7\"},\n" +
						"{\"AkorAdi\": \"AmM9\", \"Dizi\": \"7,7,7,9,9,8\"},\n" +
						"{\"AkorAdi\": \"AmM11\", \"Dizi\": \"x,2,0,1,1,0\"},\n" +
						"{\"AkorAdi\": \"AmM11\", \"Dizi\": \"5,5,6,5,5,7\"},\n" +
						"{\"AkorAdi\": \"AmM11\", \"Dizi\": \"7,7,7,7,9,8\"},\n" +
						"{\"AkorAdi\": \"AmM11\", \"Dizi\": \"x,12,10,13,12,10\"},\n" +
						"{\"AkorAdi\": \"Aadd9\", \"Dizi\": \"x,0,2,2,0,0\"},\n" +
						"{\"AkorAdi\": \"Aadd9\", \"Dizi\": \"x,x,7,6,5,7\"},\n" +
						"{\"AkorAdi\": \"Aadd9\", \"Dizi\": \"x,12,11,9,12,9\"},\n" +
						"{\"AkorAdi\": \"Amadd9\", \"Dizi\": \"x,3,2,2,0,x\"},\n" +
						"{\"AkorAdi\": \"Amadd9\", \"Dizi\": \"x,x,7,5,5,7\"},\n" +
						"{\"AkorAdi\": \"Amadd9\", \"Dizi\": \"x,x,10,9,10,7\"},\n" +
						"{\"AkorAdi\": \"Amadd9\", \"Dizi\": \"x,12,10,9,12,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 1:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"A#\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"A#\", \"Dizi\": \"x,1,3,3,3,1\"},\n" +
						"{\"AkorAdi\": \"A#\", \"Dizi\": \"x,x,3,3,3,6\"},\n" +
						"{\"AkorAdi\": \"A#\", \"Dizi\": \"6,8,8,7,6,6\"},\n" +
						"{\"AkorAdi\": \"A#\", \"Dizi\": \"x,8,8,10,11,10\"},\n" +
						"{\"AkorAdi\": \"A#m\", \"Dizi\": \"x,1,3,3,2,1\"},\n" +
						"{\"AkorAdi\": \"A#m\", \"Dizi\": \"x,x,3,6,6,6\"},\n" +
						"{\"AkorAdi\": \"A#m\", \"Dizi\": \"6,8,8,6,6,6\"},\n" +
						"{\"AkorAdi\": \"A#m\", \"Dizi\": \"x,x,11,10,11,9\"},\n" +
						"{\"AkorAdi\": \"A#dim\", \"Dizi\": \"x,1,2,0,2,0\"},\n" +
						"{\"AkorAdi\": \"A#dim\", \"Dizi\": \"x,x,2,3,2,3\"},\n" +
						"{\"AkorAdi\": \"A#dim\", \"Dizi\": \"6,7,8,6,8,6\"},\n" +
						"{\"AkorAdi\": \"A#dim\", \"Dizi\": \"9,10,11,9,11,9\"},\n" +
						"{\"AkorAdi\": \"A#sus4\", \"Dizi\": \"x,1,3,3,4,1\"},\n" +
						"{\"AkorAdi\": \"A#sus4\", \"Dizi\": \"x,x,3,3,4,6\"},\n" +
						"{\"AkorAdi\": \"A#sus4\", \"Dizi\": \"6,6,8,8,6,6\"},\n" +
						"{\"AkorAdi\": \"A#sus4\", \"Dizi\": \"x,8,8,10,11,11\"},\n" +
						"{\"AkorAdi\": \"A#7sus4\", \"Dizi\": \"x,1,3,1,4,1\"},\n" +
						"{\"AkorAdi\": \"A#7sus4\", \"Dizi\": \"x,x,3,3,4,4\"},\n" +
						"{\"AkorAdi\": \"A#7sus4\", \"Dizi\": \"6,8,6,8,6,6\"},\n" +
						"{\"AkorAdi\": \"A#7sus4\", \"Dizi\": \"x,13,13,13,11,11\"},\n" +
						"{\"AkorAdi\": \"A#-5\", \"Dizi\": \"x,1,2,3,3,0\"},\n" +
						"{\"AkorAdi\": \"A#-5\", \"Dizi\": \"x,5,2,3,5,x\"},\n" +
						"{\"AkorAdi\": \"A#-5\", \"Dizi\": \"x,7,8,7,5,x\"},\n" +
						"{\"AkorAdi\": \"A#-5\", \"Dizi\": \"x,x,12,9,11,10\"},\n" +
						"{\"AkorAdi\": \"A#+5\", \"Dizi\": \"x,x,4,3,3,2\"},\n" +
						"{\"AkorAdi\": \"A#+5\", \"Dizi\": \"x,5,4,3,3,x\"},\n" +
						"{\"AkorAdi\": \"A#+5\", \"Dizi\": \"x,x,8,7,7,6\"},\n" +
						"{\"AkorAdi\": \"A#+5\", \"Dizi\": \"x,x,8,7,7,10\"},\n" +
						"{\"AkorAdi\": \"A#6\", \"Dizi\": \"x,1,3,3,3,3\"},\n" +
						"{\"AkorAdi\": \"A#6\", \"Dizi\": \"3,5,3,3,6,3\"},\n" +
						"{\"AkorAdi\": \"A#6\", \"Dizi\": \"x,8,8,7,8,x\"},\n" +
						"{\"AkorAdi\": \"A#6\", \"Dizi\": \"10,10,12,10,11,10\"},\n" +
						"{\"AkorAdi\": \"A#69\", \"Dizi\": \"6,5,5,5,6,6\"},\n" +
						"{\"AkorAdi\": \"A#69\", \"Dizi\": \"x,8,8,7,8,8\"},\n" +
						"{\"AkorAdi\": \"A#69\", \"Dizi\": \"x,13,12,12,13,13\"},\n" +
						"{\"AkorAdi\": \"A#7\", \"Dizi\": \"x,1,3,1,3,1\"},\n" +
						"{\"AkorAdi\": \"A#7\", \"Dizi\": \"x,[5],3,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#7\", \"Dizi\": \"6,8,6,7,6,6\"},\n" +
						"{\"AkorAdi\": \"A#7\", \"Dizi\": \"x,8,8,10,9,10\"},\n" +
						"{\"AkorAdi\": \"A#7-5\", \"Dizi\": \"x,1,2,1,3,4\"},\n" +
						"{\"AkorAdi\": \"A#7-5\", \"Dizi\": \"x,5,6,7,5,6\"},\n" +
						"{\"AkorAdi\": \"A#7-5\", \"Dizi\": \"x,x,8,9,9,10\"},\n" +
						"{\"AkorAdi\": \"A#7-5\", \"Dizi\": \"x,11,12,13,11,12\"},\n" +
						"{\"AkorAdi\": \"A#7+5\", \"Dizi\": \"x,x,4,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#7+5\", \"Dizi\": \"x,x,6,7,7,6\"},\n" +
						"{\"AkorAdi\": \"A#7+5\", \"Dizi\": \"x,x,8,11,9,10\"},\n" +
						"{\"AkorAdi\": \"A#7+5\", \"Dizi\": \"x,x,12,13,11,14\"},\n" +
						"{\"AkorAdi\": \"A#9\", \"Dizi\": \"x,1,0,1,1,1\"},\n" +
						"{\"AkorAdi\": \"A#9\", \"Dizi\": \"x,3,3,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#9\", \"Dizi\": \"6,8,6,7,6,8\"},\n" +
						"{\"AkorAdi\": \"A#9\", \"Dizi\": \"8,8,8,10,9,10\"},\n" +
						"{\"AkorAdi\": \"A#9-5\", \"Dizi\": \"x,1,0,1,1,0\"},\n" +
						"{\"AkorAdi\": \"A#9-5\", \"Dizi\": \"x,3,2,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#9-5\", \"Dizi\": \"6,5,6,5,5,6\"},\n" +
						"{\"AkorAdi\": \"A#9-5\", \"Dizi\": \"x,7,8,7,9,8\"},\n" +
						"{\"AkorAdi\": \"A#9+5\", \"Dizi\": \"x,1,0,1,1,2\"},\n" +
						"{\"AkorAdi\": \"A#9+5\", \"Dizi\": \"4,3,4,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#9+5\", \"Dizi\": \"x,5,6,5,7,6\"},\n" +
						"{\"AkorAdi\": \"A#9+5\", \"Dizi\": \"[10],11,10,11,11,10\"},\n" +
						"{\"AkorAdi\": \"A#7-9\", \"Dizi\": \"x,1,0,1,0,1\"},\n" +
						"{\"AkorAdi\": \"A#7-9\", \"Dizi\": \"[1],2,3,1,3,1\"},\n" +
						"{\"AkorAdi\": \"A#7-9\", \"Dizi\": \"x,2,3,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#7-9\", \"Dizi\": \"6,8,6,7,6,7\"},\n" +
						"{\"AkorAdi\": \"A#7+9\", \"Dizi\": \"x,1,0,1,2,x\"},\n" +
						"{\"AkorAdi\": \"A#7+9\", \"Dizi\": \"6,8,6,7,9,9\"},\n" +
						"{\"AkorAdi\": \"A#7+9\", \"Dizi\": \"x,8,8,7,9,9\"},\n" +
						"{\"AkorAdi\": \"A#7+9\", \"Dizi\": \"9,11,12,10,9,9\"},\n" +
						"{\"AkorAdi\": \"A#11\", \"Dizi\": \"x,1,1,1,3,1\"},\n" +
						"{\"AkorAdi\": \"A#11\", \"Dizi\": \"x,5,3,3,4,4\"},\n" +
						"{\"AkorAdi\": \"A#11\", \"Dizi\": \"[8],[8],8,8,9,10\"},\n" +
						"{\"AkorAdi\": \"A#11\", \"Dizi\": \"x,13,12,13,11,11\"},\n" +
						"{\"AkorAdi\": \"A#+11\", \"Dizi\": \"x,3,2,3,3,4\"},\n" +
						"{\"AkorAdi\": \"A#+11\", \"Dizi\": \"6,5,6,5,5,6\"},\n" +
						"{\"AkorAdi\": \"A#+11\", \"Dizi\": \"8,8,8,9,9,10\"},\n" +
						"{\"AkorAdi\": \"A#+11\", \"Dizi\": \"x,13,12,13,13,12\"},\n" +
						"{\"AkorAdi\": \"A#13\", \"Dizi\": \"x,1,3,1,3,3\"},\n" +
						"{\"AkorAdi\": \"A#13\", \"Dizi\": \"6,8,6,7,8,6\"},\n" +
						"{\"AkorAdi\": \"A#13\", \"Dizi\": \"6,6,6,7,8,8\"},\n" +
						"{\"AkorAdi\": \"A#13\", \"Dizi\": \"x,11,12,12,11,x\"},\n" +
						"{\"AkorAdi\": \"A#M7\", \"Dizi\": \"x,1,3,2,3,1\"},\n" +
						"{\"AkorAdi\": \"A#M7\", \"Dizi\": \"x,x,3,3,3,5\"},\n" +
						"{\"AkorAdi\": \"A#M7\", \"Dizi\": \"6,8,7,7,6,6\"},\n" +
						"{\"AkorAdi\": \"A#M7\", \"Dizi\": \"x,[8],8,10,10,10\"},\n" +
						"{\"AkorAdi\": \"A#M7-5\", \"Dizi\": \"x,1,2,2,3,x\"},\n" +
						"{\"AkorAdi\": \"A#M7-5\", \"Dizi\": \"x,x,2,3,3,5\"},\n" +
						"{\"AkorAdi\": \"A#M7-5\", \"Dizi\": \"6,7,7,7,x,x\"},\n" +
						"{\"AkorAdi\": \"A#M7-5\", \"Dizi\": \"x,x,8,9,10,10\"},\n" +
						"{\"AkorAdi\": \"A#M7+5\", \"Dizi\": \"x,1,0,2,3,2\"},\n" +
						"{\"AkorAdi\": \"A#M7+5\", \"Dizi\": \"x,x,4,3,3,5\"},\n" +
						"{\"AkorAdi\": \"A#M7+5\", \"Dizi\": \"x,9,8,7,10,10\"},\n" +
						"{\"AkorAdi\": \"A#M7+5\", \"Dizi\": \"[10],13,12,11,10,10\"},\n" +
						"{\"AkorAdi\": \"A#M9\", \"Dizi\": \"[1],1,0,2,1,x\"},\n" +
						"{\"AkorAdi\": \"A#M9\", \"Dizi\": \"x,3,3,3,3,5\"},\n" +
						"{\"AkorAdi\": \"A#M9\", \"Dizi\": \"x,5,7,5,6,6\"},\n" +
						"{\"AkorAdi\": \"A#M9\", \"Dizi\": \"x,x,7,7,6,8\"},\n" +
						"{\"AkorAdi\": \"A#M11\", \"Dizi\": \"x,1,1,2,1,1\"},\n" +
						"{\"AkorAdi\": \"A#M11\", \"Dizi\": \"x,3,3,5,4,5\"},\n" +
						"{\"AkorAdi\": \"A#M11\", \"Dizi\": \"6,6,7,7,6,8\"},\n" +
						"{\"AkorAdi\": \"A#M11\", \"Dizi\": \"x,x,12,14,13,11\"},\n" +
						"{\"AkorAdi\": \"A#M13\", \"Dizi\": \"x,1,[1],2,3,3\"},\n" +
						"{\"AkorAdi\": \"A#M13\", \"Dizi\": \"6,5,5,5,6,5\"},\n" +
						"{\"AkorAdi\": \"A#M13\", \"Dizi\": \"x,10,8,8,10,10\"},\n" +
						"{\"AkorAdi\": \"A#M13\", \"Dizi\": \"x,13,12,12,10,[10]\"},\n" +
						"{\"AkorAdi\": \"A#m6\", \"Dizi\": \"x,x,3,3,2,3\"},\n" +
						"{\"AkorAdi\": \"A#m6\", \"Dizi\": \"x,4,5,3,6,3\"},\n" +
						"{\"AkorAdi\": \"A#m6\", \"Dizi\": \"x,x,5,6,6,6\"},\n" +
						"{\"AkorAdi\": \"A#m6\", \"Dizi\": \"x,8,8,6,8,6\"},\n" +
						"{\"AkorAdi\": \"A#m69\", \"Dizi\": \"x,4,5,5,6,6\"},\n" +
						"{\"AkorAdi\": \"A#m69\", \"Dizi\": \"x,8,8,6,8,8\"},\n" +
						"{\"AkorAdi\": \"A#m69\", \"Dizi\": \"9,8,8,10,8,8\"},\n" +
						"{\"AkorAdi\": \"A#m69\", \"Dizi\": \"x,13,11,12,13,13\"},\n" +
						"{\"AkorAdi\": \"A#m7\", \"Dizi\": \"x,1,3,1,2,1\"},\n" +
						"{\"AkorAdi\": \"A#m7\", \"Dizi\": \"x,x,3,3,2,4\"},\n" +
						"{\"AkorAdi\": \"A#m7\", \"Dizi\": \"6,8,6,6,6,6\"},\n" +
						"{\"AkorAdi\": \"A#m7\", \"Dizi\": \"x,8,8,10,9,9\"},\n" +
						"{\"AkorAdi\": \"A#m7-5\", \"Dizi\": \"x,1,2,1,2,x\"},\n" +
						"{\"AkorAdi\": \"A#m7-5\", \"Dizi\": \"x,x,2,3,2,4\"},\n" +
						"{\"AkorAdi\": \"A#m7-5\", \"Dizi\": \"4,4,6,6,5,6\"},\n" +
						"{\"AkorAdi\": \"A#m7-5\", \"Dizi\": \"x,x,8,9,9,9\"},\n" +
						"{\"AkorAdi\": \"A#m9\", \"Dizi\": \"x,3,3,3,2,4\"},\n" +
						"{\"AkorAdi\": \"A#m9\", \"Dizi\": \"6,8,6,6,6,8\"},\n" +
						"{\"AkorAdi\": \"A#m9\", \"Dizi\": \"[9],11,10,10,11,9\"},\n" +
						"{\"AkorAdi\": \"A#m9\", \"Dizi\": \"x,13,11,13,13,13\"},\n" +
						"{\"AkorAdi\": \"A#m11\", \"Dizi\": \"6,4,6,5,4,4\"},\n" +
						"{\"AkorAdi\": \"A#m11\", \"Dizi\": \"6,6,6,6,6,8\"},\n" +
						"{\"AkorAdi\": \"A#m11\", \"Dizi\": \"9,8,8,8,9,8\"},\n" +
						"{\"AkorAdi\": \"A#m11\", \"Dizi\": \"x,13,11,13,13,11\"},\n" +
						"{\"AkorAdi\": \"A#mM7\", \"Dizi\": \"x,1,3,2,2,1\"},\n" +
						"{\"AkorAdi\": \"A#mM7\", \"Dizi\": \"6,8,7,6,6,6\"},\n" +
						"{\"AkorAdi\": \"A#mM7\", \"Dizi\": \"x,8,8,10,10,9\"},\n" +
						"{\"AkorAdi\": \"A#mM7\", \"Dizi\": \"x,12,11,10,11,x\"},\n" +
						"{\"AkorAdi\": \"A#mM7-5\", \"Dizi\": \"x,1,2,2,2,0\"},\n" +
						"{\"AkorAdi\": \"A#mM7-5\", \"Dizi\": \"6,7,x,6,5,5\"},\n" +
						"{\"AkorAdi\": \"A#mM7-5\", \"Dizi\": \"6,7,7,6,x,6\"},\n" +
						"{\"AkorAdi\": \"A#mM7-5\", \"Dizi\": \"x,x,8,9,10,9\"},\n" +
						"{\"AkorAdi\": \"A#mM9\", \"Dizi\": \"x,4,3,2,1,1\"},\n" +
						"{\"AkorAdi\": \"A#mM9\", \"Dizi\": \"x,4,x,5,6,5\"},\n" +
						"{\"AkorAdi\": \"A#mM9\", \"Dizi\": \"6,8,7,6,6,8\"},\n" +
						"{\"AkorAdi\": \"A#mM9\", \"Dizi\": \"8,8,8,10,10,9\"},\n" +
						"{\"AkorAdi\": \"A#mM11\", \"Dizi\": \"x,3,1,2,2,1\"},\n" +
						"{\"AkorAdi\": \"A#mM11\", \"Dizi\": \"6,6,7,6,6,8\"},\n" +
						"{\"AkorAdi\": \"A#mM11\", \"Dizi\": \"8,8,8,8,10,9\"},\n" +
						"{\"AkorAdi\": \"A#mM11\", \"Dizi\": \"x,13,11,14,13,11\"},\n" +
						"{\"AkorAdi\": \"A#add9\", \"Dizi\": \"x,1,3,3,1,1\"},\n" +
						"{\"AkorAdi\": \"A#add9\", \"Dizi\": \"x,x,8,7,6,8\"},\n" +
						"{\"AkorAdi\": \"A#add9\", \"Dizi\": \"x,13,12,10,13,10\"},\n" +
						"{\"AkorAdi\": \"A#madd9\", \"Dizi\": \"x,4,3,3,1,x\"},\n" +
						"{\"AkorAdi\": \"A#madd9\", \"Dizi\": \"x,x,8,6,6,8\"},\n" +
						"{\"AkorAdi\": \"A#madd9\", \"Dizi\": \"x,x,11,10,11,8\"},\n" +
						"{\"AkorAdi\": \"A#madd9\", \"Dizi\": \"x,13,11,10,13,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 2:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"B\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"B\", \"Dizi\": \"[2],2,4,4,4,2\"},\n" +
						"{\"AkorAdi\": \"B\", \"Dizi\": \"x,x,4,4,4,7\"},\n" +
						"{\"AkorAdi\": \"B\", \"Dizi\": \"7,9,9,8,7,7\"},\n" +
						"{\"AkorAdi\": \"B\", \"Dizi\": \"x,9,9,11,12,11\"},\n" +
						"{\"AkorAdi\": \"Bm\", \"Dizi\": \"[2],2,4,4,3,2\"},\n" +
						"{\"AkorAdi\": \"Bm\", \"Dizi\": \"x,x,4,7,7,7\"},\n" +
						"{\"AkorAdi\": \"Bm\", \"Dizi\": \"7,9,9,7,7,7\"},\n" +
						"{\"AkorAdi\": \"Bm\", \"Dizi\": \"x,x,12,11,12,10\"},\n" +
						"{\"AkorAdi\": \"Bdim\", \"Dizi\": \"x,x,0,1,0,1\"},\n" +
						"{\"AkorAdi\": \"Bdim\", \"Dizi\": \"x,x,3,4,3,4\"},\n" +
						"{\"AkorAdi\": \"Bdim\", \"Dizi\": \"4,5,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"Bdim\", \"Dizi\": \"7,8,9,7,9,7\"},\n" +
						"{\"AkorAdi\": \"Bsus4\", \"Dizi\": \"[2],2,4,4,5,2\"},\n" +
						"{\"AkorAdi\": \"Bsus4\", \"Dizi\": \"x,x,4,4,5,7\"},\n" +
						"{\"AkorAdi\": \"Bsus4\", \"Dizi\": \"7,7,9,9,7,7\"},\n" +
						"{\"AkorAdi\": \"Bsus4\", \"Dizi\": \"x,9,9,11,12,12\"},\n" +
						"{\"AkorAdi\": \"B7sus4\", \"Dizi\": \"x,2,2,2,0,2\"},\n" +
						"{\"AkorAdi\": \"B7sus4\", \"Dizi\": \"[2],2,4,2,5,2\"},\n" +
						"{\"AkorAdi\": \"B7sus4\", \"Dizi\": \"x,x,4,4,5,5\"},\n" +
						"{\"AkorAdi\": \"B7sus4\", \"Dizi\": \"7,9,7,9,7,7\"},\n" +
						"{\"AkorAdi\": \"B-5\", \"Dizi\": \"x,x,3,4,4,1\"},\n" +
						"{\"AkorAdi\": \"B-5\", \"Dizi\": \"x,2,3,4,4,x\"},\n" +
						"{\"AkorAdi\": \"B-5\", \"Dizi\": \"x,8,9,8,6,x\"},\n" +
						"{\"AkorAdi\": \"B-5\", \"Dizi\": \"x,x,13,10,12,11\"},\n" +
						"{\"AkorAdi\": \"B+5\", \"Dizi\": \"x,2,1,0,0,3\"},\n" +
						"{\"AkorAdi\": \"B+5\", \"Dizi\": \"x,x,5,4,4,3\"},\n" +
						"{\"AkorAdi\": \"B+5\", \"Dizi\": \"x,10,9,8,8,x\"},\n" +
						"{\"AkorAdi\": \"B+5\", \"Dizi\": \"x,x,9,8,8,11\"},\n" +
						"{\"AkorAdi\": \"B6\", \"Dizi\": \"x,2,1,1,0,2\"},\n" +
						"{\"AkorAdi\": \"B6\", \"Dizi\": \"4,6,4,4,7,4\"},\n" +
						"{\"AkorAdi\": \"B6\", \"Dizi\": \"x,9,9,8,9,x\"},\n" +
						"{\"AkorAdi\": \"B6\", \"Dizi\": \"11,11,13,11,12,11\"},\n" +
						"{\"AkorAdi\": \"B69\", \"Dizi\": \"x,2,1,1,2,2\"},\n" +
						"{\"AkorAdi\": \"B69\", \"Dizi\": \"7,6,6,6,7,7\"},\n" +
						"{\"AkorAdi\": \"B69\", \"Dizi\": \"x,9,9,8,9,9\"},\n" +
						"{\"AkorAdi\": \"B7\", \"Dizi\": \"x,2,1,2,0,2\"},\n" +
						"{\"AkorAdi\": \"B7\", \"Dizi\": \"[2],2,4,2,4,2\"},\n" +
						"{\"AkorAdi\": \"B7\", \"Dizi\": \"x,[6],4,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B7\", \"Dizi\": \"7,9,7,8,7,7\"},\n" +
						"{\"AkorAdi\": \"B7-5\", \"Dizi\": \"x,x,1,2,0,1\"},\n" +
						"{\"AkorAdi\": \"B7-5\", \"Dizi\": \"x,2,3,2,4,5\"},\n" +
						"{\"AkorAdi\": \"B7-5\", \"Dizi\": \"x,6,7,8,6,7\"},\n" +
						"{\"AkorAdi\": \"B7-5\", \"Dizi\": \"x,x,9,10,10,11\"},\n" +
						"{\"AkorAdi\": \"B7+5\", \"Dizi\": \"x,2,1,2,0,3\"},\n" +
						"{\"AkorAdi\": \"B7+5\", \"Dizi\": \"x,x,5,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B7+5\", \"Dizi\": \"x,x,7,8,8,7\"},\n" +
						"{\"AkorAdi\": \"B7+5\", \"Dizi\": \"x,x,9,12,10,11\"},\n" +
						"{\"AkorAdi\": \"B9\", \"Dizi\": \"x,2,1,2,2,2\"},\n" +
						"{\"AkorAdi\": \"B9\", \"Dizi\": \"x,4,4,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B9\", \"Dizi\": \"7,9,7,8,7,9\"},\n" +
						"{\"AkorAdi\": \"B9\", \"Dizi\": \"9,9,9,11,10,11\"},\n" +
						"{\"AkorAdi\": \"B9-5\", \"Dizi\": \"x,2,1,2,2,1\"},\n" +
						"{\"AkorAdi\": \"B9-5\", \"Dizi\": \"x,4,3,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B9-5\", \"Dizi\": \"7,6,7,6,6,7\"},\n" +
						"{\"AkorAdi\": \"B9-5\", \"Dizi\": \"x,8,9,8,10,9\"},\n" +
						"{\"AkorAdi\": \"B9+5\", \"Dizi\": \"x,2,1,2,2,3\"},\n" +
						"{\"AkorAdi\": \"B9+5\", \"Dizi\": \"5,4,5,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B9+5\", \"Dizi\": \"x,6,7,6,8,7\"},\n" +
						"{\"AkorAdi\": \"B9+5\", \"Dizi\": \"[11],12,11,12,12,11\"},\n" +
						"{\"AkorAdi\": \"B7-9\", \"Dizi\": \"x,2,1,2,1,2\"},\n" +
						"{\"AkorAdi\": \"B7-9\", \"Dizi\": \"[2],3,4,2,4,2\"},\n" +
						"{\"AkorAdi\": \"B7-9\", \"Dizi\": \"x,3,4,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B7-9\", \"Dizi\": \"7,9,7,8,7,8\"},\n" +
						"{\"AkorAdi\": \"B7+9\", \"Dizi\": \"x,2,1,2,3,x\"},\n" +
						"{\"AkorAdi\": \"B7+9\", \"Dizi\": \"7,9,7,8,10,10\"},\n" +
						"{\"AkorAdi\": \"B7+9\", \"Dizi\": \"x,9,9,8,10,10\"},\n" +
						"{\"AkorAdi\": \"B7+9\", \"Dizi\": \"10,12,13,11,10,10\"},\n" +
						"{\"AkorAdi\": \"B11\", \"Dizi\": \"x,2,1,2,0,0\"},\n" +
						"{\"AkorAdi\": \"B11\", \"Dizi\": \"[2],2,2,2,4,2\"},\n" +
						"{\"AkorAdi\": \"B11\", \"Dizi\": \"x,6,4,4,5,5\"},\n" +
						"{\"AkorAdi\": \"B11\", \"Dizi\": \"[9],[9],9,9,10,11\"},\n" +
						"{\"AkorAdi\": \"B+11\", \"Dizi\": \"x,2,1,2,2,1\"},\n" +
						"{\"AkorAdi\": \"B+11\", \"Dizi\": \"x,4,3,4,4,5\"},\n" +
						"{\"AkorAdi\": \"B+11\", \"Dizi\": \"7,6,7,6,6,7\"},\n" +
						"{\"AkorAdi\": \"B+11\", \"Dizi\": \"9,9,9,10,10,11\"},\n" +
						"{\"AkorAdi\": \"B13\", \"Dizi\": \"x,2,1,2,2,4\"},\n" +
						"{\"AkorAdi\": \"B13\", \"Dizi\": \"[2],2,4,2,4,4\"},\n" +
						"{\"AkorAdi\": \"B13\", \"Dizi\": \"7,9,7,8,9,7\"},\n" +
						"{\"AkorAdi\": \"B13\", \"Dizi\": \"7,7,7,8,9,9\"},\n" +
						"{\"AkorAdi\": \"BM7\", \"Dizi\": \"[2],2,4,3,4,2\"},\n" +
						"{\"AkorAdi\": \"BM7\", \"Dizi\": \"x,x,4,4,4,6\"},\n" +
						"{\"AkorAdi\": \"BM7\", \"Dizi\": \"7,9,8,8,7,7\"},\n" +
						"{\"AkorAdi\": \"BM7\", \"Dizi\": \"x,[9],9,11,11,11\"},\n" +
						"{\"AkorAdi\": \"BM7-5\", \"Dizi\": \"x,2,3,3,4,x\"},\n" +
						"{\"AkorAdi\": \"BM7-5\", \"Dizi\": \"x,x,3,4,4,6\"},\n" +
						"{\"AkorAdi\": \"BM7-5\", \"Dizi\": \"7,8,8,8,x,x\"},\n" +
						"{\"AkorAdi\": \"BM7-5\", \"Dizi\": \"x,x,9,10,11,11\"},\n" +
						"{\"AkorAdi\": \"BM7+5\", \"Dizi\": \"x,2,1,3,0,3\"},\n" +
						"{\"AkorAdi\": \"BM7+5\", \"Dizi\": \"x,x,5,4,4,6\"},\n" +
						"{\"AkorAdi\": \"BM7+5\", \"Dizi\": \"x,10,9,8,11,11\"},\n" +
						"{\"AkorAdi\": \"BM7+5\", \"Dizi\": \"[11],14,13,12,11,11\"},\n" +
						"{\"AkorAdi\": \"BM9\", \"Dizi\": \"[2],2,1,3,2,x\"},\n" +
						"{\"AkorAdi\": \"BM9\", \"Dizi\": \"x,4,4,4,4,6\"},\n" +
						"{\"AkorAdi\": \"BM9\", \"Dizi\": \"x,6,8,6,7,7\"},\n" +
						"{\"AkorAdi\": \"BM9\", \"Dizi\": \"x,x,8,8,7,9\"},\n" +
						"{\"AkorAdi\": \"BM11\", \"Dizi\": \"x,x,1,3,2,0\"},\n" +
						"{\"AkorAdi\": \"BM11\", \"Dizi\": \"[2],2,2,3,2,2\"},\n" +
						"{\"AkorAdi\": \"BM11\", \"Dizi\": \"x,4,4,6,5,6\"},\n" +
						"{\"AkorAdi\": \"BM11\", \"Dizi\": \"7,7,8,8,7,9\"},\n" +
						"{\"AkorAdi\": \"BM13\", \"Dizi\": \"x,2,[2],3,4,4\"},\n" +
						"{\"AkorAdi\": \"BM13\", \"Dizi\": \"7,6,6,6,7,6\"},\n" +
						"{\"AkorAdi\": \"BM13\", \"Dizi\": \"x,11,9,9,11,11\"},\n" +
						"{\"AkorAdi\": \"BM13\", \"Dizi\": \"x,14,13,13,11,[11]\"},\n" +
						"{\"AkorAdi\": \"Bm6\", \"Dizi\": \"[2],2,0,1,0,2\"},\n" +
						"{\"AkorAdi\": \"Bm6\", \"Dizi\": \"x,x,4,4,3,4\"},\n" +
						"{\"AkorAdi\": \"Bm6\", \"Dizi\": \"x,5,6,4,7,4\"},\n" +
						"{\"AkorAdi\": \"Bm6\", \"Dizi\": \"x,x,6,7,7,7\"},\n" +
						"{\"AkorAdi\": \"Bm69\", \"Dizi\": \"x,2,0,1,2,2\"},\n" +
						"{\"AkorAdi\": \"Bm69\", \"Dizi\": \"x,5,6,6,7,7\"},\n" +
						"{\"AkorAdi\": \"Bm69\", \"Dizi\": \"x,9,9,7,9,9\"},\n" +
						"{\"AkorAdi\": \"Bm69\", \"Dizi\": \"10,9,9,11,9,9\"},\n" +
						"{\"AkorAdi\": \"Bm7\", \"Dizi\": \"[2],2,4,2,3,2\"},\n" +
						"{\"AkorAdi\": \"Bm7\", \"Dizi\": \"x,x,4,4,3,5\"},\n" +
						"{\"AkorAdi\": \"Bm7\", \"Dizi\": \"7,9,7,7,7,7\"},\n" +
						"{\"AkorAdi\": \"Bm7\", \"Dizi\": \"x,9,9,11,10,10\"},\n" +
						"{\"AkorAdi\": \"Bm7-5\", \"Dizi\": \"x,2,3,2,3,x\"},\n" +
						"{\"AkorAdi\": \"Bm7-5\", \"Dizi\": \"x,x,3,4,3,5\"},\n" +
						"{\"AkorAdi\": \"Bm7-5\", \"Dizi\": \"5,5,7,7,6,7\"},\n" +
						"{\"AkorAdi\": \"Bm7-5\", \"Dizi\": \"x,x,9,10,10,10\"},\n" +
						"{\"AkorAdi\": \"Bm9\", \"Dizi\": \"x,2,0,2,2,2\"},\n" +
						"{\"AkorAdi\": \"Bm9\", \"Dizi\": \"x,4,4,4,3,5\"},\n" +
						"{\"AkorAdi\": \"Bm9\", \"Dizi\": \"7,9,7,7,7,9\"},\n" +
						"{\"AkorAdi\": \"Bm9\", \"Dizi\": \"[10],12,11,11,12,10\"},\n" +
						"{\"AkorAdi\": \"Bm11\", \"Dizi\": \"x,2,0,2,2,0\"},\n" +
						"{\"AkorAdi\": \"Bm11\", \"Dizi\": \"7,5,7,6,5,5\"},\n" +
						"{\"AkorAdi\": \"Bm11\", \"Dizi\": \"7,7,7,7,7,9\"},\n" +
						"{\"AkorAdi\": \"Bm11\", \"Dizi\": \"10,9,9,9,10,9\"},\n" +
						"{\"AkorAdi\": \"BmM7\", \"Dizi\": \"x,2,0,3,0,2\"},\n" +
						"{\"AkorAdi\": \"BmM7\", \"Dizi\": \"2,2,4,3,3,2\"},\n" +
						"{\"AkorAdi\": \"BmM7\", \"Dizi\": \"7,9,8,7,7,7\"},\n" +
						"{\"AkorAdi\": \"BmM7\", \"Dizi\": \"x,9,9,11,11,10\"},\n" +
						"{\"AkorAdi\": \"BmM7-5\", \"Dizi\": \"x,2,3,3,3,x\"},\n" +
						"{\"AkorAdi\": \"BmM7-5\", \"Dizi\": \"7,8,x,7,6,6\"},\n" +
						"{\"AkorAdi\": \"BmM7-5\", \"Dizi\": \"7,8,8,7,x,7\"},\n" +
						"{\"AkorAdi\": \"BmM7-5\", \"Dizi\": \"x,x,9,10,11,10\"},\n" +
						"{\"AkorAdi\": \"BmM9\", \"Dizi\": \"[2],5,4,3,2,2\"},\n" +
						"{\"AkorAdi\": \"BmM9\", \"Dizi\": \"x,5,x,6,7,6\"},\n" +
						"{\"AkorAdi\": \"BmM9\", \"Dizi\": \"7,9,8,7,7,9\"},\n" +
						"{\"AkorAdi\": \"BmM9\", \"Dizi\": \"9,9,9,11,11,10\"},\n" +
						"{\"AkorAdi\": \"BmM11\", \"Dizi\": \"x,2,0,3,2,0\"},\n" +
						"{\"AkorAdi\": \"BmM11\", \"Dizi\": \"2,4,2,3,3,2\"},\n" +
						"{\"AkorAdi\": \"BmM11\", \"Dizi\": \"7,7,8,7,7,9\"},\n" +
						"{\"AkorAdi\": \"BmM11\", \"Dizi\": \"9,9,9,9,11,10\"},\n" +
						"{\"AkorAdi\": \"Badd9\", \"Dizi\": \"[2],2,4,4,2,2\"},\n" +
						"{\"AkorAdi\": \"Badd9\", \"Dizi\": \"x,x,9,8,7,9\"},\n" +
						"{\"AkorAdi\": \"Badd9\", \"Dizi\": \"x,14,13,11,14,11\"},\n" +
						"{\"AkorAdi\": \"Bmadd9\", \"Dizi\": \"x,5,4,4,2,x\"},\n" +
						"{\"AkorAdi\": \"Bmadd9\", \"Dizi\": \"x,x,9,7,7,9\"},\n" +
						"{\"AkorAdi\": \"Bmadd9\", \"Dizi\": \"x,x,12,11,12,9\"},\n" +
						"{\"AkorAdi\": \"Bmadd9\", \"Dizi\": \"x,14,12,11,14,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 3:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"C\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"C\", \"Dizi\": \"[3],3,2,0,1,0\"},\n" +
						"{\"AkorAdi\": \"C\", \"Dizi\": \"[3],3,5,5,5,3\"},\n" +
						"{\"AkorAdi\": \"C\", \"Dizi\": \"x,[7],5,5,5,8\"},\n" +
						"{\"AkorAdi\": \"C\", \"Dizi\": \"8,10,10,9,8,8\"},\n" +
						"{\"AkorAdi\": \"Cm\", \"Dizi\": \"x,x,1,0,1,3\"},\n" +
						"{\"AkorAdi\": \"Cm\", \"Dizi\": \"[3],3,5,5,4,3\"},\n" +
						"{\"AkorAdi\": \"Cm\", \"Dizi\": \"x,[6],5,8,8,8,\"},\n" +
						"{\"AkorAdi\": \"Cm\", \"Dizi\": \"8,10,10,8,8,8\"},\n" +
						"{\"AkorAdi\": \"Cdim\", \"Dizi\": \"x,x,1,2,1,2\"},\n" +
						"{\"AkorAdi\": \"Cdim\", \"Dizi\": \"x,3,4,2,4,2\"},\n" +
						"{\"AkorAdi\": \"Cdim\", \"Dizi\": \"8,x,7,8,7,x\"},\n" +
						"{\"AkorAdi\": \"Cdim\", \"Dizi\": \"x,x,10,11,10,11\"},\n" +
						"{\"AkorAdi\": \"Csus4\", \"Dizi\": \"x,3,3,0,1,1\"},\n" +
						"{\"AkorAdi\": \"Csus4\", \"Dizi\": \"[3],3,5,5,6,3\"},\n" +
						"{\"AkorAdi\": \"Csus4\", \"Dizi\": \"x,x,5,5,6,8\"},\n" +
						"{\"AkorAdi\": \"Csus4\", \"Dizi\": \"8,8,10,10,8,8\"},\n" +
						"{\"AkorAdi\": \"C7sus4\", \"Dizi\": \"x,3,3,3,1,1\"},\n" +
						"{\"AkorAdi\": \"C7sus4\", \"Dizi\": \"[3],3,5,3,6,3\"},\n" +
						"{\"AkorAdi\": \"C7sus4\", \"Dizi\": \"x,x,5,5,6,6\"},\n" +
						"{\"AkorAdi\": \"C7sus4\", \"Dizi\": \"8,10,8,10,8,8\"},\n" +
						"{\"AkorAdi\": \"C-5\", \"Dizi\": \"x,x,4,5,5,2\"},\n" +
						"{\"AkorAdi\": \"C-5\", \"Dizi\": \"2,3,2,5,[5],x\"},\n" +
						"{\"AkorAdi\": \"C-5\", \"Dizi\": \"x,3,4,5,5,x\"},\n" +
						"{\"AkorAdi\": \"C-5\", \"Dizi\": \"x,x,10,9,7,8\"},\n" +
						"{\"AkorAdi\": \"C+5\", \"Dizi\": \"x,3,2,1,1,x\"},\n" +
						"{\"AkorAdi\": \"C+5\", \"Dizi\": \"x,x,6,5,5,4\"},\n" +
						"{\"AkorAdi\": \"C+5\", \"Dizi\": \"x,7,6,5,5,x\"},\n" +
						"{\"AkorAdi\": \"C+5\", \"Dizi\": \"x,x,10,9,9,8\"},\n" +
						"{\"AkorAdi\": \"C6\", \"Dizi\": \"x,0,2,0,1,0\"},\n" +
						"{\"AkorAdi\": \"C6\", \"Dizi\": \"x,3,2,2,x,3\"},\n" +
						"{\"AkorAdi\": \"C6\", \"Dizi\": \"5,7,5,5,8,5\"},\n" +
						"{\"AkorAdi\": \"C6\", \"Dizi\": \"x,10,10,9,10,x\"},\n" +
						"{\"AkorAdi\": \"C69\", \"Dizi\": \"x,3,2,2,3,3\"},\n" +
						"{\"AkorAdi\": \"C69\", \"Dizi\": \"8,7,7,7,8,8\"},\n" +
						"{\"AkorAdi\": \"C69\", \"Dizi\": \"x,10,10,9,10,10\"},\n" +
						"{\"AkorAdi\": \"C7\", \"Dizi\": \"x,3,2,3,1,0\"},\n" +
						"{\"AkorAdi\": \"C7\", \"Dizi\": \"[3],3,5,3,5,3\"},\n" +
						"{\"AkorAdi\": \"C7\", \"Dizi\": \"x,[7],5,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C7\", \"Dizi\": \"8,10,8,9,8,8\"},\n" +
						"{\"AkorAdi\": \"C7-5\", \"Dizi\": \"x,x,2,3,1,2\"},\n" +
						"{\"AkorAdi\": \"C7-5\", \"Dizi\": \"x,3,4,3,5,6\"},\n" +
						"{\"AkorAdi\": \"C7-5\", \"Dizi\": \"x,x,4,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C7-5\", \"Dizi\": \"x,7,8,9,7,8\"},\n" +
						"{\"AkorAdi\": \"C7+5\", \"Dizi\": \"x,x,2,3,1,4\"},\n" +
						"{\"AkorAdi\": \"C7+5\", \"Dizi\": \"x,3,6,3,5,4\"},\n" +
						"{\"AkorAdi\": \"C7+5\", \"Dizi\": \"x,x,6,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C7+5\", \"Dizi\": \"8,[11],8,9,9,8\"},\n" +
						"{\"AkorAdi\": \"C9\", \"Dizi\": \"[3],3,2,3,3,3\"},\n" +
						"{\"AkorAdi\": \"C9\", \"Dizi\": \"x,5,5,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C9\", \"Dizi\": \"8,10,8,9,8,10\"},\n" +
						"{\"AkorAdi\": \"C9\", \"Dizi\": \"10,10,10,12,11,12\"},\n" +
						"{\"AkorAdi\": \"C9-5\", \"Dizi\": \"x,3,2,3,3,2\"},\n" +
						"{\"AkorAdi\": \"C9-5\", \"Dizi\": \"x,5,4,3,5,x\"},\n" +
						"{\"AkorAdi\": \"C9-5\", \"Dizi\": \"x,7,8,7,7,10\"},\n" +
						"{\"AkorAdi\": \"C9-5\", \"Dizi\": \"x,x,12,11,11,12\"},\n" +
						"{\"AkorAdi\": \"C9+5\", \"Dizi\": \"x,1,0,1,1,0\"},\n" +
						"{\"AkorAdi\": \"C9+5\", \"Dizi\": \"x,3,2,3,3,4\"},\n" +
						"{\"AkorAdi\": \"C9+5\", \"Dizi\": \"6,5,6,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C9+5\", \"Dizi\": \"x,7,8,7,9,8\"},\n" +
						"{\"AkorAdi\": \"C7-9\", \"Dizi\": \"x,3,2,3,2,3\"},\n" +
						"{\"AkorAdi\": \"C7-9\", \"Dizi\": \"[3],4,5,3,5,3\"},\n" +
						"{\"AkorAdi\": \"C7-9\", \"Dizi\": \"6,7,8,6,8,6\"},\n" +
						"{\"AkorAdi\": \"C7-9\", \"Dizi\": \"x,x,8,9,8,9\"},\n" +
						"{\"AkorAdi\": \"C7+9\", \"Dizi\": \"x,3,2,3,4,x\"},\n" +
						"{\"AkorAdi\": \"C7+9\", \"Dizi\": \"x,6,5,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C7+9\", \"Dizi\": \"x,7,8,8,8,8\"},\n" +
						"{\"AkorAdi\": \"C7+9\", \"Dizi\": \"8,10,8,9,8,11\"},\n" +
						"{\"AkorAdi\": \"C11\", \"Dizi\": \"x,3,2,3,1,1\"},\n" +
						"{\"AkorAdi\": \"C11\", \"Dizi\": \"x,3,3,3,5,3\"},\n" +
						"{\"AkorAdi\": \"C11\", \"Dizi\": \"x,7,5,5,6,6\"},\n" +
						"{\"AkorAdi\": \"C11\", \"Dizi\": \"10,10,10,10,11,12\"},\n" +
						"{\"AkorAdi\": \"C+11\", \"Dizi\": \"x,3,2,3,3,2\"},\n" +
						"{\"AkorAdi\": \"C+11\", \"Dizi\": \"x,5,4,5,5,6\"},\n" +
						"{\"AkorAdi\": \"C+11\", \"Dizi\": \"8,7,8,7,7,8\"},\n" +
						"{\"AkorAdi\": \"C+11\", \"Dizi\": \"10,10,10,11,11,12\"},\n" +
						"{\"AkorAdi\": \"C13\", \"Dizi\": \"x,1,2,2,1,x\"},\n" +
						"{\"AkorAdi\": \"C13\", \"Dizi\": \"[3],3,5,3,5,5\"},\n" +
						"{\"AkorAdi\": \"C13\", \"Dizi\": \"8,10,8,9,10,8\"},\n" +
						"{\"AkorAdi\": \"C13\", \"Dizi\": \"8,8,8,9,10,10\"},\n" +
						"{\"AkorAdi\": \"CM7\", \"Dizi\": \"[3],3,2,0,0,0\"},\n" +
						"{\"AkorAdi\": \"CM7\", \"Dizi\": \"[3],3,5,4,5,3\"},\n" +
						"{\"AkorAdi\": \"CM7\", \"Dizi\": \"x,x,5,5,5,7\"},\n" +
						"{\"AkorAdi\": \"CM7\", \"Dizi\": \"x,10,10,12,12,12\"},\n" +
						"{\"AkorAdi\": \"CM7-5\", \"Dizi\": \"x,3,2,4,0,2\"},\n" +
						"{\"AkorAdi\": \"CM7-5\", \"Dizi\": \"x,3,4,4,5,x\"},\n" +
						"{\"AkorAdi\": \"CM7-5\", \"Dizi\": \"x,x,4,5,5,7\"},\n" +
						"{\"AkorAdi\": \"CM7-5\", \"Dizi\": \"x,x,10,11,12,12\"},\n" +
						"{\"AkorAdi\": \"CM7+5\", \"Dizi\": \"x,3,2,1,0,0\"},\n" +
						"{\"AkorAdi\": \"CM7+5\", \"Dizi\": \"x,3,6,4,5,x\"},\n" +
						"{\"AkorAdi\": \"CM7+5\", \"Dizi\": \"x,x,6,5,5,7\"},\n" +
						"{\"AkorAdi\": \"CM7+5\", \"Dizi\": \"x,11,10,x,12,12\"},\n" +
						"{\"AkorAdi\": \"CM9\", \"Dizi\": \"x,3,0,0,0,0\"},\n" +
						"{\"AkorAdi\": \"CM9\", \"Dizi\": \"[3],3,2,4,3,x\"},\n" +
						"{\"AkorAdi\": \"CM9\", \"Dizi\": \"x,5,5,7,5,7\"},\n" +
						"{\"AkorAdi\": \"CM9\", \"Dizi\": \"x,x,9,9,8,10\"},\n" +
						"{\"AkorAdi\": \"CM11\", \"Dizi\": \"x,x,2,4,3,1\"},\n" +
						"{\"AkorAdi\": \"CM11\", \"Dizi\": \"[3],3,3,4,3,3\"},\n" +
						"{\"AkorAdi\": \"CM11\", \"Dizi\": \"x,5,5,7,6,7\"},\n" +
						"{\"AkorAdi\": \"CM11\", \"Dizi\": \"8,8,9,10,8,10\"},\n" +
						"{\"AkorAdi\": \"CM13\", \"Dizi\": \"3,3,0,2,0,0\"},\n" +
						"{\"AkorAdi\": \"CM13\", \"Dizi\": \"x,3,[3],4,5,5\"},\n" +
						"{\"AkorAdi\": \"CM13\", \"Dizi\": \"8,7,7,7,8,7\"},\n" +
						"{\"AkorAdi\": \"CM13\", \"Dizi\": \"x,12,10,10,12,12\"},\n" +
						"{\"AkorAdi\": \"Cm6\", \"Dizi\": \"x,3,1,2,1,3\"},\n" +
						"{\"AkorAdi\": \"Cm6\", \"Dizi\": \"x,x,5,5,4,5\"},\n" +
						"{\"AkorAdi\": \"Cm6\", \"Dizi\": \"x,x,7,8,8,8\"},\n" +
						"{\"AkorAdi\": \"Cm6\", \"Dizi\": \"8,10,10,8,10,8\"},\n" +
						"{\"AkorAdi\": \"Cm69\", \"Dizi\": \"x,3,1,2,3,3\"},\n" +
						"{\"AkorAdi\": \"Cm69\", \"Dizi\": \"x,6,7,7,8,8\"},\n" +
						"{\"AkorAdi\": \"Cm69\", \"Dizi\": \"x,10,10,8,10,10\"},\n" +
						"{\"AkorAdi\": \"Cm69\", \"Dizi\": \"11,10,10,12,10,10\"},\n" +
						"{\"AkorAdi\": \"Cm7\", \"Dizi\": \"[3],3,5,3,4,3\"},\n" +
						"{\"AkorAdi\": \"Cm7\", \"Dizi\": \"x,x,5,5,4,6\"},\n" +
						"{\"AkorAdi\": \"Cm7\", \"Dizi\": \"8,10,8,8,8,8\"},\n" +
						"{\"AkorAdi\": \"Cm7\", \"Dizi\": \"x,10,10,12,11,11\"},\n" +
						"{\"AkorAdi\": \"Cm7-5\", \"Dizi\": \"x,3,4,3,4,x\"},\n" +
						"{\"AkorAdi\": \"Cm7-5\", \"Dizi\": \"x,x,4,5,4,6\"},\n" +
						"{\"AkorAdi\": \"Cm7-5\", \"Dizi\": \"6,6,8,8,7,8\"},\n" +
						"{\"AkorAdi\": \"Cm7-5\", \"Dizi\": \"x,x,10,11,11,11\"},\n" +
						"{\"AkorAdi\": \"Cm9\", \"Dizi\": \"x,3,1,3,3,[3]\"},\n" +
						"{\"AkorAdi\": \"Cm9\", \"Dizi\": \"x,5,5,5,4,6\"},\n" +
						"{\"AkorAdi\": \"Cm9\", \"Dizi\": \"x,6,8,7,8,8\"},\n" +
						"{\"AkorAdi\": \"Cm9\", \"Dizi\": \"8,10,8,8,8,10\"},\n" +
						"{\"AkorAdi\": \"Cm11\", \"Dizi\": \"x,3,1,3,3,1\"},\n" +
						"{\"AkorAdi\": \"Cm11\", \"Dizi\": \"8,6,8,7,6,6\"},\n" +
						"{\"AkorAdi\": \"Cm11\", \"Dizi\": \"8,8,8,8,8,10\"},\n" +
						"{\"AkorAdi\": \"Cm11\", \"Dizi\": \"11,10,10,10,11,10\"},\n" +
						"{\"AkorAdi\": \"CmM7\", \"Dizi\": \"x,3,1,0,0,3\"},\n" +
						"{\"AkorAdi\": \"CmM7\", \"Dizi\": \"x,3,5,4,4,3\"},\n" +
						"{\"AkorAdi\": \"CmM7\", \"Dizi\": \"8,10,9,8,8,8\"},\n" +
						"{\"AkorAdi\": \"CmM7\", \"Dizi\": \"x,10,10,12,12,11\"},\n" +
						"{\"AkorAdi\": \"CmM7-5\", \"Dizi\": \"2,3,4,4,4,x\"},\n" +
						"{\"AkorAdi\": \"CmM7-5\", \"Dizi\": \"x,3,4,4,4,x\"},\n" +
						"{\"AkorAdi\": \"CmM7-5\", \"Dizi\": \"8,9,9,8,x,8\"},\n" +
						"{\"AkorAdi\": \"CmM7-5\", \"Dizi\": \"x,x,10,11,12,11\"},\n" +
						"{\"AkorAdi\": \"CmM9\", \"Dizi\": \"[3],6,5,4,3,3\"},\n" +
						"{\"AkorAdi\": \"CmM9\", \"Dizi\": \"x,6,x,7,8,7\"},\n" +
						"{\"AkorAdi\": \"CmM9\", \"Dizi\": \"8,10,9,8,8,10\"},\n" +
						"{\"AkorAdi\": \"CmM9\", \"Dizi\": \"10,10,10,12,12,11\"},\n" +
						"{\"AkorAdi\": \"CmM11\", \"Dizi\": \"x,3,1,4,3,1\"},\n" +
						"{\"AkorAdi\": \"CmM11\", \"Dizi\": \"3,5,3,4,4,3\"},\n" +
						"{\"AkorAdi\": \"CmM11\", \"Dizi\": \"8,8,9,8,8,10\"},\n" +
						"{\"AkorAdi\": \"CmM11\", \"Dizi\": \"10,10,10,10,12,11\"},\n" +
						"{\"AkorAdi\": \"Cadd9\", \"Dizi\": \"x,3,2,0,3,0\"},\n" +
						"{\"AkorAdi\": \"Cadd9\", \"Dizi\": \"x,3,5,5,3,3\"},\n" +
						"{\"AkorAdi\": \"Cadd9\", \"Dizi\": \"x,x,10,9,8,10\"},\n" +
						"{\"AkorAdi\": \"Cmadd9\", \"Dizi\": \"x,3,1,0,3,3\"},\n" +
						"{\"AkorAdi\": \"Cmadd9\", \"Dizi\": \"x,6,5,5,3,x\"},\n" +
						"{\"AkorAdi\": \"Cmadd9\", \"Dizi\": \"x,x,10,8,8,10\"},\n" +
						"{\"AkorAdi\": \"Cmadd9\", \"Dizi\": \"x,x,13,12,13,10\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 4:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"C#\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"C#\", \"Dizi\": \"[1],4,3,1,2,1\"},\n" +
						"{\"AkorAdi\": \"C#\", \"Dizi\": \"[4],4,6,6,6,4\"},\n" +
						"{\"AkorAdi\": \"C#\", \"Dizi\": \"x,8,6,6,6,9\"},\n" +
						"{\"AkorAdi\": \"C#\", \"Dizi\": \"9,11,11,10,9,9\"},\n" +
						"{\"AkorAdi\": \"C#m\", \"Dizi\": \"x,x,2,1,2,0\"},\n" +
						"{\"AkorAdi\": \"C#m\", \"Dizi\": \"[4],4,6,6,5,4\"},\n" +
						"{\"AkorAdi\": \"C#m\", \"Dizi\": \"x,[7],6,9,9,9\"},\n" +
						"{\"AkorAdi\": \"C#m\", \"Dizi\": \"9,11,11,9,9,9\"},\n" +
						"{\"AkorAdi\": \"C#dim\", \"Dizi\": \"x,x,2,3,2,3\"},\n" +
						"{\"AkorAdi\": \"C#dim\", \"Dizi\": \"x,4,5,3,5,3\"},\n" +
						"{\"AkorAdi\": \"C#dim\", \"Dizi\": \"9,x,8,9,8,x\"},\n" +
						"{\"AkorAdi\": \"C#dim\", \"Dizi\": \"x,x,11,12,11,12\"},\n" +
						"{\"AkorAdi\": \"C#sus4\", \"Dizi\": \"x,x,x,1,2,2\"},\n" +
						"{\"AkorAdi\": \"C#sus4\", \"Dizi\": \"[4],4,6,6,7,4\"},\n" +
						"{\"AkorAdi\": \"C#sus4\", \"Dizi\": \"x,x,6,6,7,9\"},\n" +
						"{\"AkorAdi\": \"C#sus4\", \"Dizi\": \"9,9,11,11,9,9\"},\n" +
						"{\"AkorAdi\": \"C#7sus4\", \"Dizi\": \"x,4,4,4,2,2\"},\n" +
						"{\"AkorAdi\": \"C#7sus4\", \"Dizi\": \"[4],4,6,4,7,4\"},\n" +
						"{\"AkorAdi\": \"C#7sus4\", \"Dizi\": \"x,x,6,6,7,7\"},\n" +
						"{\"AkorAdi\": \"C#7sus4\", \"Dizi\": \"9,11,9,11,9,9\"},\n" +
						"{\"AkorAdi\": \"C#-5\", \"Dizi\": \"x,x,3,0,2,1\"},\n" +
						"{\"AkorAdi\": \"C#-5\", \"Dizi\": \"x,x,5,6,6,3\"},\n" +
						"{\"AkorAdi\": \"C#-5\", \"Dizi\": \"3,4,3,6,[6],x\"},\n" +
						"{\"AkorAdi\": \"C#-5\", \"Dizi\": \"x,4,5,6,6,x\"},\n" +
						"{\"AkorAdi\": \"C#+5\", \"Dizi\": \"x,x,3,2,2,1\"},\n" +
						"{\"AkorAdi\": \"C#+5\", \"Dizi\": \"x,4,3,2,2,x\"},\n" +
						"{\"AkorAdi\": \"C#+5\", \"Dizi\": \"x,x,7,6,6,5\"},\n" +
						"{\"AkorAdi\": \"C#+5\", \"Dizi\": \"x,8,7,6,6,x\"},\n" +
						"{\"AkorAdi\": \"C#6\", \"Dizi\": \"1,1,3,1,2,1\"},\n" +
						"{\"AkorAdi\": \"C#6\", \"Dizi\": \"x,4,3,3,x,4\"},\n" +
						"{\"AkorAdi\": \"C#6\", \"Dizi\": \"6,8,6,6,9,6\"},\n" +
						"{\"AkorAdi\": \"C#6\", \"Dizi\": \"x,11,11,10,11,x\"},\n" +
						"{\"AkorAdi\": \"C#69\", \"Dizi\": \"x,4,3,3,4,4\"},\n" +
						"{\"AkorAdi\": \"C#69\", \"Dizi\": \"9,8,8,8,9,9\"},\n" +
						"{\"AkorAdi\": \"C#69\", \"Dizi\": \"x,11,11,10,11,11\"},\n" +
						"{\"AkorAdi\": \"C#7\", \"Dizi\": \"x,4,3,4,2,x\"},\n" +
						"{\"AkorAdi\": \"C#7\", \"Dizi\": \"[4],4,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"C#7\", \"Dizi\": \"x,[8],6,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#7\", \"Dizi\": \"9,11,9,10,9,9\"},\n" +
						"{\"AkorAdi\": \"C#7-5\", \"Dizi\": \"x,x,3,4,2,3\"},\n" +
						"{\"AkorAdi\": \"C#7-5\", \"Dizi\": \"x,4,5,4,6,7\"},\n" +
						"{\"AkorAdi\": \"C#7-5\", \"Dizi\": \"x,x,5,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#7-5\", \"Dizi\": \"x,8,9,10,8,9\"},\n" +
						"{\"AkorAdi\": \"C#7+5\", \"Dizi\": \"x,x,3,4,2,5\"},\n" +
						"{\"AkorAdi\": \"C#7+5\", \"Dizi\": \"x,4,7,4,6,5\"},\n" +
						"{\"AkorAdi\": \"C#7+5\", \"Dizi\": \"x,x,7,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#7+5\", \"Dizi\": \"9,[12],9,10,10,9\"},\n" +
						"{\"AkorAdi\": \"C#9\", \"Dizi\": \"[4],4,3,4,4,4\"},\n" +
						"{\"AkorAdi\": \"C#9\", \"Dizi\": \"x,6,6,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#9\", \"Dizi\": \"9,11,9,10,9,11\"},\n" +
						"{\"AkorAdi\": \"C#9\", \"Dizi\": \"11,11,11,13,12,13\"},\n" +
						"{\"AkorAdi\": \"C#9-5\", \"Dizi\": \"x,x,1,0,0,1\"},\n" +
						"{\"AkorAdi\": \"C#9-5\", \"Dizi\": \"x,4,3,4,4,3\"},\n" +
						"{\"AkorAdi\": \"C#9-5\", \"Dizi\": \"x,6,5,4,6,x\"},\n" +
						"{\"AkorAdi\": \"C#9-5\", \"Dizi\": \"x,8,9,8,8,11\"},\n" +
						"{\"AkorAdi\": \"C#9+5\", \"Dizi\": \"[1],2,1,2,2,1\"},\n" +
						"{\"AkorAdi\": \"C#9+5\", \"Dizi\": \"x,4,3,4,4,5\"},\n" +
						"{\"AkorAdi\": \"C#9+5\", \"Dizi\": \"7,6,7,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#9+5\", \"Dizi\": \"x,8,9,8,10,9\"},\n" +
						"{\"AkorAdi\": \"C#7-9\", \"Dizi\": \"x,4,3,4,3,4\"},\n" +
						"{\"AkorAdi\": \"C#7-9\", \"Dizi\": \"[4],5,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"C#7-9\", \"Dizi\": \"7,8,9,7,9,7\"},\n" +
						"{\"AkorAdi\": \"C#7-9\", \"Dizi\": \"x,x,9,10,9,10\"},\n" +
						"{\"AkorAdi\": \"C#7+9\", \"Dizi\": \"x,4,3,4,5,x\"},\n" +
						"{\"AkorAdi\": \"C#7+9\", \"Dizi\": \"x,7,6,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#7+9\", \"Dizi\": \"x,8,9,9,9,9\"},\n" +
						"{\"AkorAdi\": \"C#7+9\", \"Dizi\": \"9,11,9,10,9,12\"},\n" +
						"{\"AkorAdi\": \"C#11\", \"Dizi\": \"x,4,3,4,2,2\"},\n" +
						"{\"AkorAdi\": \"C#11\", \"Dizi\": \"x,4,4,4,6,4\"},\n" +
						"{\"AkorAdi\": \"C#11\", \"Dizi\": \"x,8,6,6,7,7\"},\n" +
						"{\"AkorAdi\": \"C#11\", \"Dizi\": \"11,11,11,11,12,13\"},\n" +
						"{\"AkorAdi\": \"C#+11\", \"Dizi\": \"x,4,3,4,4,3\"},\n" +
						"{\"AkorAdi\": \"C#+11\", \"Dizi\": \"x,6,5,6,6,7\"},\n" +
						"{\"AkorAdi\": \"C#+11\", \"Dizi\": \"9,8,9,8,8,9\"},\n" +
						"{\"AkorAdi\": \"C#+11\", \"Dizi\": \"11,11,11,12,12,13\"},\n" +
						"{\"AkorAdi\": \"C#13\", \"Dizi\": \"x,2,3,3,2,x\"},\n" +
						"{\"AkorAdi\": \"C#13\", \"Dizi\": \"[4],4,6,4,6,6\"},\n" +
						"{\"AkorAdi\": \"C#13\", \"Dizi\": \"9,11,9,10,11,9\"},\n" +
						"{\"AkorAdi\": \"C#13\", \"Dizi\": \"9,9,9,10,11,11\"},\n" +
						"{\"AkorAdi\": \"C#M7\", \"Dizi\": \"1,3,3,1,2,1\"},\n" +
						"{\"AkorAdi\": \"C#M7\", \"Dizi\": \"[4],4,6,5,6,4\"},\n" +
						"{\"AkorAdi\": \"C#M7\", \"Dizi\": \"x,x,6,6,6,8\"},\n" +
						"{\"AkorAdi\": \"C#M7\", \"Dizi\": \"x,11,11,13,13,13\"},\n" +
						"{\"AkorAdi\": \"C#M7-5\", \"Dizi\": \"x,3,3,0,2,3\"},\n" +
						"{\"AkorAdi\": \"C#M7-5\", \"Dizi\": \"x,4,5,5,6,x\"},\n" +
						"{\"AkorAdi\": \"C#M7-5\", \"Dizi\": \"x,x,5,6,6,8\"},\n" +
						"{\"AkorAdi\": \"C#M7-5\", \"Dizi\": \"x,x,11,12,13,13\"},\n" +
						"{\"AkorAdi\": \"C#M7+5\", \"Dizi\": \"[1],4,3,2,1,1\"},\n" +
						"{\"AkorAdi\": \"C#M7+5\", \"Dizi\": \"x,4,7,5,6,x\"},\n" +
						"{\"AkorAdi\": \"C#M7+5\", \"Dizi\": \"x,x,7,6,6,8\"},\n" +
						"{\"AkorAdi\": \"C#M7+5\", \"Dizi\": \"x,12,11,x,13,13\"},\n" +
						"{\"AkorAdi\": \"C#M9\", \"Dizi\": \"x,4,1,1,1,1\"},\n" +
						"{\"AkorAdi\": \"C#M9\", \"Dizi\": \"[4],4,3,5,4,x\"},\n" +
						"{\"AkorAdi\": \"C#M9\", \"Dizi\": \"x,6,6,8,6,8\"},\n" +
						"{\"AkorAdi\": \"C#M9\", \"Dizi\": \"x,x,10,10,9,11\"},\n" +
						"{\"AkorAdi\": \"C#M11\", \"Dizi\": \"x,x,3,5,4,2\"},\n" +
						"{\"AkorAdi\": \"C#M11\", \"Dizi\": \"[4],4,4,5,4,4\"},\n" +
						"{\"AkorAdi\": \"C#M11\", \"Dizi\": \"x,6,6,8,7,8\"},\n" +
						"{\"AkorAdi\": \"C#M11\", \"Dizi\": \"9,9,10,11,9,11\"},\n" +
						"{\"AkorAdi\": \"C#M13\", \"Dizi\": \"x,4,1,3,1,1\"},\n" +
						"{\"AkorAdi\": \"C#M13\", \"Dizi\": \"x,4,[4],5,6,6\"},\n" +
						"{\"AkorAdi\": \"C#M13\", \"Dizi\": \"9,8,8,8,9,8\"},\n" +
						"{\"AkorAdi\": \"C#M13\", \"Dizi\": \"x,13,11,11,13,13\"},\n" +
						"{\"AkorAdi\": \"C#m6\", \"Dizi\": \"x,4,2,3,2,4\"},\n" +
						"{\"AkorAdi\": \"C#m6\", \"Dizi\": \"x,x,6,6,5,6\"},\n" +
						"{\"AkorAdi\": \"C#m6\", \"Dizi\": \"x,x,8,9,9,9\"},\n" +
						"{\"AkorAdi\": \"C#m6\", \"Dizi\": \"9,11,11,9,11,9\"},\n" +
						"{\"AkorAdi\": \"C#m69\", \"Dizi\": \"x,4,2,3,4,4\"},\n" +
						"{\"AkorAdi\": \"C#m69\", \"Dizi\": \"x,7,8,8,9,9\"},\n" +
						"{\"AkorAdi\": \"C#m69\", \"Dizi\": \"x,11,11,9,11,11\"},\n" +
						"{\"AkorAdi\": \"C#m69\", \"Dizi\": \"12,11,11,13,11,11\"},\n" +
						"{\"AkorAdi\": \"C#m7\", \"Dizi\": \"[4],4,6,4,5,4\"},\n" +
						"{\"AkorAdi\": \"C#m7\", \"Dizi\": \"x,x,6,6,5,7\"},\n" +
						"{\"AkorAdi\": \"C#m7\", \"Dizi\": \"9,11,9,9,9,9\"},\n" +
						"{\"AkorAdi\": \"C#m7\", \"Dizi\": \"x,11,11,13,12,12\"},\n" +
						"{\"AkorAdi\": \"C#m7-5\", \"Dizi\": \"x,4,5,4,5,x\"},\n" +
						"{\"AkorAdi\": \"C#m7-5\", \"Dizi\": \"x,x,5,6,5,7\"},\n" +
						"{\"AkorAdi\": \"C#m7-5\", \"Dizi\": \"7,7,9,9,8,9\"},\n" +
						"{\"AkorAdi\": \"C#m7-5\", \"Dizi\": \"x,x,11,12,12,12\"},\n" +
						"{\"AkorAdi\": \"C#m9\", \"Dizi\": \"x,4,2,4,4,4\"},\n" +
						"{\"AkorAdi\": \"C#m9\", \"Dizi\": \"x,6,6,6,5,7\"},\n" +
						"{\"AkorAdi\": \"C#m9\", \"Dizi\": \"x,7,9,8,9,9\"},\n" +
						"{\"AkorAdi\": \"C#m9\", \"Dizi\": \"9,11,9,9,9,11\"},\n" +
						"{\"AkorAdi\": \"C#m11\", \"Dizi\": \"x,4,2,4,4,2\"},\n" +
						"{\"AkorAdi\": \"C#m11\", \"Dizi\": \"9,7,9,8,7,7\"},\n" +
						"{\"AkorAdi\": \"C#m11\", \"Dizi\": \"9,9,9,9,9,11\"},\n" +
						"{\"AkorAdi\": \"C#m11\", \"Dizi\": \"12,11,11,11,12,11\"},\n" +
						"{\"AkorAdi\": \"C#mM7\", \"Dizi\": \"0,3,2,1,2,0\"},\n" +
						"{\"AkorAdi\": \"C#mM7\", \"Dizi\": \"4,4,6,5,5,4\"},\n" +
						"{\"AkorAdi\": \"C#mM7\", \"Dizi\": \"9,11,10,9,9,9\"},\n" +
						"{\"AkorAdi\": \"C#mM7\", \"Dizi\": \"x,11,11,13,13,12\"},\n" +
						"{\"AkorAdi\": \"C#mM7-5\", \"Dizi\": \"3,4,5,5,5,x\"},\n" +
						"{\"AkorAdi\": \"C#mM7-5\", \"Dizi\": \"x,4,5,5,5,x\"},\n" +
						"{\"AkorAdi\": \"C#mM7-5\", \"Dizi\": \"9,10,10,9,x,9\"},\n" +
						"{\"AkorAdi\": \"C#mM7-5\", \"Dizi\": \"x,x,11,12,13,12\"},\n" +
						"{\"AkorAdi\": \"C#mM9\", \"Dizi\": \"4,7,6,5,4,4\"},\n" +
						"{\"AkorAdi\": \"C#mM9\", \"Dizi\": \"x,7,x,8,9,8\"},\n" +
						"{\"AkorAdi\": \"C#mM9\", \"Dizi\": \"9,11,10,9,9,11\"},\n" +
						"{\"AkorAdi\": \"C#mM9\", \"Dizi\": \"11,11,11,13,13,12\"},\n" +
						"{\"AkorAdi\": \"C#mM11\", \"Dizi\": \"x,4,2,5,4,2\"},\n" +
						"{\"AkorAdi\": \"C#mM11\", \"Dizi\": \"4,6,4,5,5,4\"},\n" +
						"{\"AkorAdi\": \"C#mM11\", \"Dizi\": \"9,9,10,9,9,11\"},\n" +
						"{\"AkorAdi\": \"C#mM11\", \"Dizi\": \"11,11,11,11,13,12\"},\n" +
						"{\"AkorAdi\": \"C#add9\", \"Dizi\": \"x,4,3,1,4,1\"},\n" +
						"{\"AkorAdi\": \"C#add9\", \"Dizi\": \"x,4,6,6,4,4\"},\n" +
						"{\"AkorAdi\": \"C#add9\", \"Dizi\": \"x,x,11,10,9,11\"},\n" +
						"{\"AkorAdi\": \"C#madd9\", \"Dizi\": \"x,4,2,1,4,x\"},\n" +
						"{\"AkorAdi\": \"C#madd9\", \"Dizi\": \"x,7,6,6,4,x\"},\n" +
						"{\"AkorAdi\": \"C#madd9\", \"Dizi\": \"x,x,11,9,9,11\"},\n" +
						"{\"AkorAdi\": \"C#madd9\", \"Dizi\": \"x,x,14,13,14,11\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 5:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"D\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"D\", \"Dizi\": \"x,x,0,2,3,2\"},\n" +
						"{\"AkorAdi\": \"D\", \"Dizi\": \"[2],5,4,2,3,2\"},\n" +
						"{\"AkorAdi\": \"D\", \"Dizi\": \"[5],5,7,7,7,5\"},\n" +
						"{\"AkorAdi\": \"D\", \"Dizi\": \"10,12,12,11,10,10\"},\n" +
						"{\"AkorAdi\": \"Dm\", \"Dizi\": \"x,x,0,2,3,1\"},\n" +
						"{\"AkorAdi\": \"Dm\", \"Dizi\": \"[5],5,7,7,6,5\"},\n" +
						"{\"AkorAdi\": \"Dm\", \"Dizi\": \"x,8,7,7,6,x\"},\n" +
						"{\"AkorAdi\": \"Dm\", \"Dizi\": \"10,12,12,10,10,10\"},\n" +
						"{\"AkorAdi\": \"Ddim\", \"Dizi\": \"x,x,0,1,0,1\"},\n" +
						"{\"AkorAdi\": \"Ddim\", \"Dizi\": \"4,5,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"Ddim\", \"Dizi\": \"7,8,9,7,9,7\"},\n" +
						"{\"AkorAdi\": \"Ddim\", \"Dizi\": \"10,x,9,10,9,x\"},\n" +
						"{\"AkorAdi\": \"Dsus4\", \"Dizi\": \"x,0,0,2,3,3\"},\n" +
						"{\"AkorAdi\": \"Dsus4\", \"Dizi\": \"[5],5,7,7,8,5\"},\n" +
						"{\"AkorAdi\": \"Dsus4\", \"Dizi\": \"x,x,7,7,8,10\"},\n" +
						"{\"AkorAdi\": \"Dsus4\", \"Dizi\": \"10,10,12,12,10,10\"},\n" +
						"{\"AkorAdi\": \"D7sus4\", \"Dizi\": \"x,5,5,5,3,3\"},\n" +
						"{\"AkorAdi\": \"D7sus4\", \"Dizi\": \"[5],5,7,5,8,5\"},\n" +
						"{\"AkorAdi\": \"D7sus4\", \"Dizi\": \"x,x,7,7,8,8\"},\n" +
						"{\"AkorAdi\": \"D7sus4\", \"Dizi\": \"10,12,10,12,10,10\"},\n" +
						"{\"AkorAdi\": \"D-5\", \"Dizi\": \"x,x,0,1,3,2\"},\n" +
						"{\"AkorAdi\": \"D-5\", \"Dizi\": \"x,x,6,7,7,4\"},\n" +
						"{\"AkorAdi\": \"D-5\", \"Dizi\": \"4,5,4,7,[7],x\"},\n" +
						"{\"AkorAdi\": \"D-5\", \"Dizi\": \"x,5,6,7,7,x\"},\n" +
						"{\"AkorAdi\": \"D+5\", \"Dizi\": \"x,x,4,3,3,2\"},\n" +
						"{\"AkorAdi\": \"D+5\", \"Dizi\": \"x,5,4,3,3,x\"},\n" +
						"{\"AkorAdi\": \"D+5\", \"Dizi\": \"x,x,8,7,7,6\"},\n" +
						"{\"AkorAdi\": \"D+5\", \"Dizi\": \"x,9,8,7,7,x\"},\n" +
						"{\"AkorAdi\": \"D6\", \"Dizi\": \"x,0,0,2,0,2\"},\n" +
						"{\"AkorAdi\": \"D6\", \"Dizi\": \"2,2,4,2,3,2\"},\n" +
						"{\"AkorAdi\": \"D6\", \"Dizi\": \"x,5,4,4,x,5\"},\n" +
						"{\"AkorAdi\": \"D6\", \"Dizi\": \"7,9,7,7,10,7\"},\n" +
						"{\"AkorAdi\": \"D69\", \"Dizi\": \"x,5,4,4,5,5\"},\n" +
						"{\"AkorAdi\": \"D69\", \"Dizi\": \"10,9,9,9,10,10\"},\n" +
						"{\"AkorAdi\": \"D69\", \"Dizi\": \"x,12,12,11,12,12\"},\n" +
						"{\"AkorAdi\": \"D7\", \"Dizi\": \"x,0,0,2,1,2\"},\n" +
						"{\"AkorAdi\": \"D7\", \"Dizi\": \"x,5,4,5,3,x\"},\n" +
						"{\"AkorAdi\": \"D7\", \"Dizi\": \"[5],5,7,5,7,5\"},\n" +
						"{\"AkorAdi\": \"D7\", \"Dizi\": \"x,[9],7,7,7,8\"},\n" +
						"{\"AkorAdi\": \"D7-5\", \"Dizi\": \"x,x,0,1,1,2\"},\n" +
						"{\"AkorAdi\": \"D7-5\", \"Dizi\": \"x,x,4,5,3,4\"},\n" +
						"{\"AkorAdi\": \"D7-5\", \"Dizi\": \"x,5,6,5,7,8\"},\n" +
						"{\"AkorAdi\": \"D7-5\", \"Dizi\": \"x,9,10,11,9,10\"},\n" +
						"{\"AkorAdi\": \"D7+5\", \"Dizi\": \"x,x,0,3,1,2\"},\n" +
						"{\"AkorAdi\": \"D7+5\", \"Dizi\": \"x,x,4,5,3,6\"},\n" +
						"{\"AkorAdi\": \"D7+5\", \"Dizi\": \"x,5,8,5,7,6\"},\n" +
						"{\"AkorAdi\": \"D7+5\", \"Dizi\": \"x,x,8,7,7,8\"},\n" +
						"{\"AkorAdi\": \"D9\", \"Dizi\": \"2,0,0,2,1,0\"},\n" +
						"{\"AkorAdi\": \"D9\", \"Dizi\": \"[5],5,4,5,5,5\"},\n" +
						"{\"AkorAdi\": \"D9\", \"Dizi\": \"x,7,7,7,7,8\"},\n" +
						"{\"AkorAdi\": \"D9\", \"Dizi\": \"10,12,10,11,10,12\"},\n" +
						"{\"AkorAdi\": \"D9-5\", \"Dizi\": \"x,x,2,1,1,2\"},\n" +
						"{\"AkorAdi\": \"D9-5\", \"Dizi\": \"x,5,4,5,5,4\"},\n" +
						"{\"AkorAdi\": \"D9-5\", \"Dizi\": \"x,7,6,5,7,x\"},\n" +
						"{\"AkorAdi\": \"D9-5\", \"Dizi\": \"x,9,10,9,9,12\"},\n" +
						"{\"AkorAdi\": \"D9+5\", \"Dizi\": \"[2],3,2,3,3,2\"},\n" +
						"{\"AkorAdi\": \"D9+5\", \"Dizi\": \"x,5,4,5,5,6\"},\n" +
						"{\"AkorAdi\": \"D9+5\", \"Dizi\": \"8,7,8,7,7,8\"},\n" +
						"{\"AkorAdi\": \"D9+5\", \"Dizi\": \"x,9,10,9,11,10\"},\n" +
						"{\"AkorAdi\": \"D7-9\", \"Dizi\": \"x,0,1,2,1,2\"},\n" +
						"{\"AkorAdi\": \"D7-9\", \"Dizi\": \"x,5,4,5,4,5\"},\n" +
						"{\"AkorAdi\": \"D7-9\", \"Dizi\": \"[5],6,7,5,7,5\"},\n" +
						"{\"AkorAdi\": \"D7-9\", \"Dizi\": \"8,9,10,8,10,8\"},\n" +
						"{\"AkorAdi\": \"D7+9\", \"Dizi\": \"2,0,0,2,1,1\"},\n" +
						"{\"AkorAdi\": \"D7+9\", \"Dizi\": \"x,5,4,5,6,x\"},\n" +
						"{\"AkorAdi\": \"D7+9\", \"Dizi\": \"x,8,7,7,7,8\"},\n" +
						"{\"AkorAdi\": \"D7+9\", \"Dizi\": \"x,9,10,10,10,10\"},\n" +
						"{\"AkorAdi\": \"D11\", \"Dizi\": \"0,0,0,0,1,2\"},\n" +
						"{\"AkorAdi\": \"D11\", \"Dizi\": \"x,5,4,5,3,3\"},\n" +
						"{\"AkorAdi\": \"D11\", \"Dizi\": \"x,5,5,5,7,5\"},\n" +
						"{\"AkorAdi\": \"D11\", \"Dizi\": \"x,9,7,7,8,8\"},\n" +
						"{\"AkorAdi\": \"D+11\", \"Dizi\": \"0,0,0,1,1,2\"},\n" +
						"{\"AkorAdi\": \"D+11\", \"Dizi\": \"x,5,4,5,5,4\"},\n" +
						"{\"AkorAdi\": \"D+11\", \"Dizi\": \"x,7,6,7,7,8\"},\n" +
						"{\"AkorAdi\": \"D+11\", \"Dizi\": \"10,9,10,9,9,10\"},\n" +
						"{\"AkorAdi\": \"D13\", \"Dizi\": \"x,3,4,4,3,x\"},\n" +
						"{\"AkorAdi\": \"D13\", \"Dizi\": \"[5],5,7,5,7,7\"},\n" +
						"{\"AkorAdi\": \"D13\", \"Dizi\": \"10,12,10,11,12,10\"},\n" +
						"{\"AkorAdi\": \"D13\", \"Dizi\": \"10,10,10,11,12,12\"},\n" +
						"{\"AkorAdi\": \"DM7\", \"Dizi\": \"x,x,0,2,2,2\"},\n" +
						"{\"AkorAdi\": \"DM7\", \"Dizi\": \"2,4,4,2,3,2\"},\n" +
						"{\"AkorAdi\": \"DM7\", \"Dizi\": \"[5],5,7,6,7,5\"},\n" +
						"{\"AkorAdi\": \"DM7\", \"Dizi\": \"x,x,7,7,7,9\"},\n" +
						"{\"AkorAdi\": \"DM7-5\", \"Dizi\": \"x,x,0,1,2,2\"},\n" +
						"{\"AkorAdi\": \"DM7-5\", \"Dizi\": \"x,5,6,6,7,x\"},\n" +
						"{\"AkorAdi\": \"DM7-5\", \"Dizi\": \"x,x,6,7,7,9\"},\n" +
						"{\"AkorAdi\": \"DM7-5\", \"Dizi\": \"10,11,11,11,x,x\"},\n" +
						"{\"AkorAdi\": \"DM7+5\", \"Dizi\": \"x,1,0,3,2,2\"},\n" +
						"{\"AkorAdi\": \"DM7+5\", \"Dizi\": \"[2],5,4,3,2,2\"},\n" +
						"{\"AkorAdi\": \"DM7+5\", \"Dizi\": \"x,5,8,6,7,x\"},\n" +
						"{\"AkorAdi\": \"DM7+5\", \"Dizi\": \"x,x,8,7,7,9\"},\n" +
						"{\"AkorAdi\": \"DM9\", \"Dizi\": \"x,5,2,2,2,2\"},\n" +
						"{\"AkorAdi\": \"DM9\", \"Dizi\": \"[5],5,4,6,5,x\"},\n" +
						"{\"AkorAdi\": \"DM9\", \"Dizi\": \"x,7,7,9,7,9\"},\n" +
						"{\"AkorAdi\": \"DM9\", \"Dizi\": \"x,x,11,11,10,12\"},\n" +
						"{\"AkorAdi\": \"DM11\", \"Dizi\": \"x,x,4,6,5,3\"},\n" +
						"{\"AkorAdi\": \"DM11\", \"Dizi\": \"[5],5,5,6,5,5\"},\n" +
						"{\"AkorAdi\": \"DM11\", \"Dizi\": \"x,7,7,9,8,9\"},\n" +
						"{\"AkorAdi\": \"DM11\", \"Dizi\": \"10,10,11,12,10,12\"},\n" +
						"{\"AkorAdi\": \"DM13\", \"Dizi\": \"x,2,0,2,2,2\"},\n" +
						"{\"AkorAdi\": \"DM13\", \"Dizi\": \"x,5,4,4,2,0\"},\n" +
						"{\"AkorAdi\": \"DM13\", \"Dizi\": \"x,5,[5],6,7,7\"},\n" +
						"{\"AkorAdi\": \"DM13\", \"Dizi\": \"10,9,9,9,10,9\"},\n" +
						"{\"AkorAdi\": \"Dm6\", \"Dizi\": \"x,x,0,2,0,1\"},\n" +
						"{\"AkorAdi\": \"Dm6\", \"Dizi\": \"x,5,3,4,3,5\"},\n" +
						"{\"AkorAdi\": \"Dm6\", \"Dizi\": \"x,x,7,7,6,7\"},\n" +
						"{\"AkorAdi\": \"Dm6\", \"Dizi\": \"x,x,9,10,10,10\"},\n" +
						"{\"AkorAdi\": \"Dm69\", \"Dizi\": \"1,0,0,2,0,0\"},\n" +
						"{\"AkorAdi\": \"Dm69\", \"Dizi\": \"x,5,3,4,5,5\"},\n" +
						"{\"AkorAdi\": \"Dm69\", \"Dizi\": \"x,8,9,9,10,10\"},\n" +
						"{\"AkorAdi\": \"Dm69\", \"Dizi\": \"x,12,12,10,12,12\"},\n" +
						"{\"AkorAdi\": \"Dm7\", \"Dizi\": \"x,0,0,2,1,1\"},\n" +
						"{\"AkorAdi\": \"Dm7\", \"Dizi\": \"[5],5,7,5,6,5\"},\n" +
						"{\"AkorAdi\": \"Dm7\", \"Dizi\": \"x,x,7,7,6,8\"},\n" +
						"{\"AkorAdi\": \"Dm7\", \"Dizi\": \"10,12,10,10,10,10\"},\n" +
						"{\"AkorAdi\": \"Dm7-5\", \"Dizi\": \"x,x,0,1,1,1\"},\n" +
						"{\"AkorAdi\": \"Dm7-5\", \"Dizi\": \"x,5,6,5,6,x\"},\n" +
						"{\"AkorAdi\": \"Dm7-5\", \"Dizi\": \"x,x,6,7,6,8\"},\n" +
						"{\"AkorAdi\": \"Dm7-5\", \"Dizi\": \"8,8,10,10,9,10\"},\n" +
						"{\"AkorAdi\": \"Dm9\", \"Dizi\": \"x,3,2,2,3,1\"},\n" +
						"{\"AkorAdi\": \"Dm9\", \"Dizi\": \"x,5,3,5,5,5\"},\n" +
						"{\"AkorAdi\": \"Dm9\", \"Dizi\": \"x,7,7,7,6,8\"},\n" +
						"{\"AkorAdi\": \"Dm9\", \"Dizi\": \"x,8,10,9,10,10\"},\n" +
						"{\"AkorAdi\": \"Dm11\", \"Dizi\": \"1,0,0,0,1,0\"},\n" +
						"{\"AkorAdi\": \"Dm11\", \"Dizi\": \"x,5,3,5,5,3\"},\n" +
						"{\"AkorAdi\": \"Dm11\", \"Dizi\": \"10,8,10,9,8,8\"},\n" +
						"{\"AkorAdi\": \"Dm11\", \"Dizi\": \"10,10,10,10,10,12\"},\n" +
						"{\"AkorAdi\": \"DmM7\", \"Dizi\": \"x,x,0,2,2,1\"},\n" +
						"{\"AkorAdi\": \"DmM7\", \"Dizi\": \"x,4,3,2,3,x\"},\n" +
						"{\"AkorAdi\": \"DmM7\", \"Dizi\": \"5,5,7,6,6,5\"},\n" +
						"{\"AkorAdi\": \"DmM7\", \"Dizi\": \"10,12,11,10,10,10\"},\n" +
						"{\"AkorAdi\": \"DmM7-5\", \"Dizi\": \"x,x,0,1,2,1\"},\n" +
						"{\"AkorAdi\": \"DmM7-5\", \"Dizi\": \"[4],5,6,6,6,x\"},\n" +
						"{\"AkorAdi\": \"DmM7-5\", \"Dizi\": \"x,5,6,6,6,x\"},\n" +
						"{\"AkorAdi\": \"DmM7-5\", \"Dizi\": \"10,11,11,10,x,10\"},\n" +
						"{\"AkorAdi\": \"DmM9\", \"Dizi\": \"0,0,0,2,2,1\"},\n" +
						"{\"AkorAdi\": \"DmM9\", \"Dizi\": \"5,8,7,6,5,5\"},\n" +
						"{\"AkorAdi\": \"DmM9\", \"Dizi\": \"x,8,x,9,10,9\"},\n" +
						"{\"AkorAdi\": \"DmM9\", \"Dizi\": \"10,12,11,10,10,12\"},\n" +
						"{\"AkorAdi\": \"DmM11\", \"Dizi\": \"0,0,0,0,2,1\"},\n" +
						"{\"AkorAdi\": \"DmM11\", \"Dizi\": \"x,5,3,6,5,3\"},\n" +
						"{\"AkorAdi\": \"DmM11\", \"Dizi\": \"5,7,5,6,6,5\"},\n" +
						"{\"AkorAdi\": \"DmM11\", \"Dizi\": \"10,10,11,10,10,12\"},\n" +
						"{\"AkorAdi\": \"Dadd9\", \"Dizi\": \"x,5,4,2,5,2\"},\n" +
						"{\"AkorAdi\": \"Dadd9\", \"Dizi\": \"x,5,7,7,5,5\"},\n" +
						"{\"AkorAdi\": \"Dadd9\", \"Dizi\": \"x,x,12,11,10,12\"},\n" +
						"{\"AkorAdi\": \"Dmadd9\", \"Dizi\": \"x,x,3,2,3,0\"},\n" +
						"{\"AkorAdi\": \"Dmadd9\", \"Dizi\": \"x,5,3,2,5,x\"},\n" +
						"{\"AkorAdi\": \"Dmadd9\", \"Dizi\": \"x,8,7,7,5,x\"},\n" +
						"{\"AkorAdi\": \"Dmadd9\", \"Dizi\": \"x,x,12,10,10,12\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 6:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"D#\", \"Akorlari\": [\n" +
						"{\"AkorAdi\":\"D#\", \"Dizi\":\"x,x,1,3,4,3\"},\n" +
						"{\"AkorAdi\":\"D#\", \"Dizi\":\"[3],6,5,3,4,3\"},\n" +
						"{\"AkorAdi\":\"D#\", \"Dizi\":\"[6],6,8,8,8,6\"},\n" +
						"{\"AkorAdi\":\"D#\", \"Dizi\":\"x,x,8,8,8,11\"},\n" +
						"{\"AkorAdi\":\"D#m\", \"Dizi\":\"x,1,1,3,4,2\"},\n" +
						"{\"AkorAdi\":\"D#m\", \"Dizi\":\"x,x,4,3,4,2\"},\n" +
						"{\"AkorAdi\":\"D#m\", \"Dizi\":\"[6],6,8,8,7,6\"},\n" +
						"{\"AkorAdi\":\"D#m\", \"Dizi\":\"11,13,13,11,11,11\"},\n" +
						"{\"AkorAdi\":\"D#dim\", \"Dizi\":\"x,x,1,2,1,2\"},\n" +
						"{\"AkorAdi\":\"D#dim\", \"Dizi\":\"5,6,7,5,7,5\"},\n" +
						"{\"AkorAdi\":\"D#dim\", \"Dizi\":\"x,x,7,8,7,8\"},\n" +
						"{\"AkorAdi\":\"D#dim\", \"Dizi\":\"11,x,10,11,10,x\"},\n" +
						"{\"AkorAdi\":\"D#sus4\", \"Dizi\":\"x,1,1,3,4,4\"},\n" +
						"{\"AkorAdi\":\"D#sus4\", \"Dizi\":\"[6],6,8,8,9,6\"},\n" +
						"{\"AkorAdi\":\"D#sus4\", \"Dizi\":\"x,x,8,8,9,11\"},\n" +
						"{\"AkorAdi\":\"D#sus4\", \"Dizi\":\"11,11,13,13,11,11\"},\n" +
						"{\"AkorAdi\":\"D#7sus4\", \"Dizi\":\"x,6,6,6,4,4\"},\n" +
						"{\"AkorAdi\":\"D#7sus4\", \"Dizi\":\"[6],6,8,6,9,6\"},\n" +
						"{\"AkorAdi\":\"D#7sus4\", \"Dizi\":\"x,x,8,8,9,9\"},\n" +
						"{\"AkorAdi\":\"D#7sus4\", \"Dizi\":\"11,13,11,13,11,11\"},\n" +
						"{\"AkorAdi\":\"D#-5\", \"Dizi\":\"x,x,1,2,4,3\"},\n" +
						"{\"AkorAdi\":\"D#-5\", \"Dizi\":\"x,x,5,2,4,3\"},\n" +
						"{\"AkorAdi\":\"D#-5\", \"Dizi\":\"5,6,5,8,[8],x\"},\n" +
						"{\"AkorAdi\":\"D#-5\", \"Dizi\":\"x,6,7,8,8,x\"},\n" +
						"{\"AkorAdi\":\"D#+5\", \"Dizi\":\"x,x,5,4,4,3\"},\n" +
						"{\"AkorAdi\":\"D#+5\", \"Dizi\":\"x,6,5,4,4,x\"},\n" +
						"{\"AkorAdi\":\"D#+5\", \"Dizi\":\"x,x,9,8,8,7\"},\n" +
						"{\"AkorAdi\":\"D#+5\", \"Dizi\":\"x,10,9,8,8,x\"},\n" +
						"{\"AkorAdi\":\"D#6\", \"Dizi\":\"x,1,1,3,1,3\"},\n" +
						"{\"AkorAdi\":\"D#6\", \"Dizi\":\"x,3,5,3,4,3\"},\n" +
						"{\"AkorAdi\":\"D#6\", \"Dizi\":\"x,6,5,5,x,6\"},\n" +
						"{\"AkorAdi\":\"D#6\", \"Dizi\":\"8,10,8,8,11,8\"},\n" +
						"{\"AkorAdi\":\"D#69\", \"Dizi\":\"x,1,1,0,1,1\"},\n" +
						"{\"AkorAdi\":\"D#69\", \"Dizi\":\"x,6,5,5,6,6\"},\n" +
						"{\"AkorAdi\":\"D#69\", \"Dizi\":\"11,10,10,10,11,11\"},\n" +
						"{\"AkorAdi\":\"D#7\", \"Dizi\":\"x,1,1,3,2,3\"},\n" +
						"{\"AkorAdi\":\"D#7\", \"Dizi\":\"[6],6,8,6,8,6\"},\n" +
						"{\"AkorAdi\":\"D#7\", \"Dizi\":\"x,[10],8,8,8,9\"},\n" +
						"{\"AkorAdi\":\"D#7\", \"Dizi\":\"11,13,11,12,11,11\"},\n" +
						"{\"AkorAdi\":\"D#7-5\", \"Dizi\":\"x,0,1,2,2,3\"},\n" +
						"{\"AkorAdi\":\"D#7-5\", \"Dizi\":\"x,x,5,6,4,5\"},\n" +
						"{\"AkorAdi\":\"D#7-5\", \"Dizi\":\"x,6,7,6,8,9\"},\n" +
						"{\"AkorAdi\":\"D#7-5\", \"Dizi\":\"x,10,11,12,10,11\"},\n" +
						"{\"AkorAdi\":\"D#7+5\", \"Dizi\":\"x,2,1,0,2,3\"},\n" +
						"{\"AkorAdi\":\"D#7+5\", \"Dizi\":\"x,x,5,6,4,7\"},\n" +
						"{\"AkorAdi\":\"D#7+5\", \"Dizi\":\"x,6,9,6,8,7\"},\n" +
						"{\"AkorAdi\":\"D#7+5\", \"Dizi\":\"x,x,9,8,8,9\"},\n" +
						"{\"AkorAdi\":\"D#9\", \"Dizi\":\"1,1,1,3,2,3\"},\n" +
						"{\"AkorAdi\":\"D#9\", \"Dizi\":\"[6],6,5,6,6,6\"},\n" +
						"{\"AkorAdi\":\"D#9\", \"Dizi\":\"x,8,8,8,8,9\"},\n" +
						"{\"AkorAdi\":\"D#9\", \"Dizi\":\"11,13,11,12,11,13\"},\n" +
						"{\"AkorAdi\":\"D#9-5\", \"Dizi\":\"x,x,3,2,2,3\"},\n" +
						"{\"AkorAdi\":\"D#9-5\", \"Dizi\":\"x,6,5,6,6,5\"},\n" +
						"{\"AkorAdi\":\"D#9-5\", \"Dizi\":\"x,8,7,6,8,x\"},\n" +
						"{\"AkorAdi\":\"D#9-5\", \"Dizi\":\"x,10,11,10,10,13\"},\n" +
						"{\"AkorAdi\":\"D#9+5\", \"Dizi\":\"[3],4,3,4,4,3\"},\n" +
						"{\"AkorAdi\":\"D#9+5\", \"Dizi\":\"x,6,5,6,6,7\"},\n" +
						"{\"AkorAdi\":\"D#9+5\", \"Dizi\":\"9,8,9,8,8,9\"},\n" +
						"{\"AkorAdi\":\"D#9+5\", \"Dizi\":\"x,10,11,10,12,11\"},\n" +
						"{\"AkorAdi\":\"D#7-9\", \"Dizi\":\"0,1,1,0,2,0\"},\n" +
						"{\"AkorAdi\":\"D#7-9\", \"Dizi\":\"x,6,5,6,5,6\"},\n" +
						"{\"AkorAdi\":\"D#7-9\", \"Dizi\":\"[6],7,8,6,8,6\"},\n" +
						"{\"AkorAdi\":\"D#7-9\", \"Dizi\":\"9,10,11,9,11,9\"},\n" +
						"{\"AkorAdi\":\"D#7+9\", \"Dizi\":\"x,1,1,0,2,2\"},\n" +
						"{\"AkorAdi\":\"D#7+9\", \"Dizi\":\"2,4,5,3,2,2\"},\n" +
						"{\"AkorAdi\":\"D#7+9\", \"Dizi\":\"x,6,5,6,7,x\"},\n" +
						"{\"AkorAdi\":\"D#7+9\", \"Dizi\":\"x,9,8,8,8,9\"},\n" +
						"{\"AkorAdi\":\"D#11\", \"Dizi\":\"[1],[1],1,1,2,3\"},\n" +
						"{\"AkorAdi\":\"D#11\", \"Dizi\":\"x,6,5,6,4,4\"},\n" +
						"{\"AkorAdi\":\"D#11\", \"Dizi\":\"x,6,6,6,8,6\"},\n" +
						"{\"AkorAdi\":\"D#11\", \"Dizi\":\"x,10,8,8,9,9\"},\n" +
						"{\"AkorAdi\":\"D#+11\", \"Dizi\":\"1,1,1,2,2,3\"},\n" +
						"{\"AkorAdi\":\"D#+11\", \"Dizi\":\"x,6,5,6,6,5\"},\n" +
						"{\"AkorAdi\":\"D#+11\", \"Dizi\":\"x,8,7,8,8,9\"},\n" +
						"{\"AkorAdi\":\"D#+11\", \"Dizi\":\"11,10,11,10,10,11\"},\n" +
						"{\"AkorAdi\":\"D#13\", \"Dizi\":\"x,4,5,5,4,x\"},\n" +
						"{\"AkorAdi\":\"D#13\", \"Dizi\":\"[6],6,8,6,8,8\"},\n" +
						"{\"AkorAdi\":\"D#13\", \"Dizi\":\"11,13,11,12,13,11\"},\n" +
						"{\"AkorAdi\":\"D#13\", \"Dizi\":\"11,11,11,12,13,13\"},\n" +
						"{\"AkorAdi\":\"D#M7\", \"Dizi\":\"x,[1],1,3,3,3\"},\n" +
						"{\"AkorAdi\":\"D#M7\", \"Dizi\":\"3,5,5,3,4,3\"},\n" +
						"{\"AkorAdi\":\"D#M7\", \"Dizi\":\"[6],6,8,7,8,6\"},\n" +
						"{\"AkorAdi\":\"D#M7\", \"Dizi\":\"x,x,8,8,8,10\"},\n" +
						"{\"AkorAdi\":\"D#M7-5\", \"Dizi\":\"x,x,1,2,3,3\"},\n" +
						"{\"AkorAdi\":\"D#M7-5\", \"Dizi\":\"x,6,7,7,8,x\"},\n" +
						"{\"AkorAdi\":\"D#M7-5\", \"Dizi\":\"x,x,7,8,8,10\"},\n" +
						"{\"AkorAdi\":\"D#M7-5\", \"Dizi\":\"11,12,12,12,x,x\"},\n" +
						"{\"AkorAdi\":\"D#M7+5\", \"Dizi\":\"x,2,1,0,3,3\"},\n" +
						"{\"AkorAdi\":\"D#M7+5\", \"Dizi\":\"[3],6,5,4,3,3\"},\n" +
						"{\"AkorAdi\":\"D#M7+5\", \"Dizi\":\"x,6,9,7,8,x\"},\n" +
						"{\"AkorAdi\":\"D#M7+5\", \"Dizi\":\"x,x,9,8,8,10\"},\n" +
						"{\"AkorAdi\":\"D#M9\", \"Dizi\":\"x,6,3,3,3,3\"},\n" +
						"{\"AkorAdi\":\"D#M9\", \"Dizi\":\"[6],6,5,7,6,x\"},\n" +
						"{\"AkorAdi\":\"D#M9\", \"Dizi\":\"x,8,8,10,8,10\"},\n" +
						"{\"AkorAdi\":\"D#M9\", \"Dizi\":\"x,x,12,12,11,13\"},\n" +
						"{\"AkorAdi\":\"D#M11\", \"Dizi\":\"x,x,5,7,6,4\"},\n" +
						"{\"AkorAdi\":\"D#M11\", \"Dizi\":\"[6],6,6,7,6,6\"},\n" +
						"{\"AkorAdi\":\"D#M11\", \"Dizi\":\"x,8,8,10,9,10\"},\n" +
						"{\"AkorAdi\":\"D#M11\", \"Dizi\":\"11,11,12,13,11,13\"},\n" +
						"{\"AkorAdi\":\"D#M13\", \"Dizi\":\"x,3,1,0,3,x\"},\n" +
						"{\"AkorAdi\":\"D#M13\", \"Dizi\":\"x,6,5,5,3,[3]\"},\n" +
						"{\"AkorAdi\":\"D#M13\", \"Dizi\":\"x,6,[6],7,8,8\"},\n" +
						"{\"AkorAdi\":\"D#M13\", \"Dizi\":\"11,10,10,10,11,10\"},\n" +
						"{\"AkorAdi\":\"D#m6\", \"Dizi\":\"x,[1],1,3,1,2\"},\n" +
						"{\"AkorAdi\":\"D#m6\", \"Dizi\":\"x,6,4,5,4,6\"},\n" +
						"{\"AkorAdi\":\"D#m6\", \"Dizi\":\"x,x,8,8,7,8\"},\n" +
						"{\"AkorAdi\":\"D#m6\", \"Dizi\":\"x,x,10,11,11,11\"},\n" +
						"{\"AkorAdi\":\"D#m69\", \"Dizi\":\"2,1,1,3,1,1\"},\n" +
						"{\"AkorAdi\":\"D#m69\", \"Dizi\":\"x,6,4,5,6,6\"},\n" +
						"{\"AkorAdi\":\"D#m69\", \"Dizi\":\"x,9,10,10,11,11\"},\n" +
						"{\"AkorAdi\":\"D#m69\", \"Dizi\":\"x,13,13,11,13,13\"},\n" +
						"{\"AkorAdi\":\"D#m7\", \"Dizi\":\"x,1,1,3,2,2\"},\n" +
						"{\"AkorAdi\":\"D#m7\", \"Dizi\":\"[6],6,8,6,7,6\"},\n" +
						"{\"AkorAdi\":\"D#m7\", \"Dizi\":\"x,x,8,8,7,9\"},\n" +
						"{\"AkorAdi\":\"D#m7\", \"Dizi\":\"11,13,11,11,11,11\"},\n" +
						"{\"AkorAdi\":\"D#m7-5\", \"Dizi\":\"x,x,1,2,2,2\"},\n" +
						"{\"AkorAdi\":\"D#m7-5\", \"Dizi\":\"x,6,7,6,7,x\"},\n" +
						"{\"AkorAdi\":\"D#m7-5\", \"Dizi\":\"x,x,7,8,7,9\"},\n" +
						"{\"AkorAdi\":\"D#m7-5\", \"Dizi\":\"9,9,11,11,10,11\"},\n" +
						"{\"AkorAdi\":\"D#m9\", \"Dizi\":\"x,4,3,3,4,2\"},\n" +
						"{\"AkorAdi\":\"D#m9\", \"Dizi\":\"x,6,4,6,6,6\"},\n" +
						"{\"AkorAdi\":\"D#m9\", \"Dizi\":\"x,8,8,8,7,9\"},\n" +
						"{\"AkorAdi\":\"D#m9\", \"Dizi\":\"x,9,11,10,11,11\"},\n" +
						"{\"AkorAdi\":\"D#m11\", \"Dizi\":\"2,1,1,1,2,1\"},\n" +
						"{\"AkorAdi\":\"D#m11\", \"Dizi\":\"x,6,4,6,6,4\"},\n" +
						"{\"AkorAdi\":\"D#m11\", \"Dizi\":\"11,9,11,10,9,9\"},\n" +
						"{\"AkorAdi\":\"D#m11\", \"Dizi\":\"11,11,11,11,11,13\"},\n" +
						"{\"AkorAdi\":\"D#mM7\", \"Dizi\":\"x,1,1,3,3,2\"},\n" +
						"{\"AkorAdi\":\"D#mM7\", \"Dizi\":\"x,5,4,3,4,x\"},\n" +
						"{\"AkorAdi\":\"D#mM7\", \"Dizi\":\"6,6,8,7,7,6\"},\n" +
						"{\"AkorAdi\":\"D#mM7\", \"Dizi\":\"11,13,12,11,11,11\"},\n" +
						"{\"AkorAdi\":\"D#mM7-5\", \"Dizi\":\"x,x,1,2,3,2\"},\n" +
						"{\"AkorAdi\":\"D#mM7-5\", \"Dizi\":\"[5],6,7,7,7,x\"},\n" +
						"{\"AkorAdi\":\"D#mM7-5\", \"Dizi\":\"x,6,7,7,7,x\"},\n" +
						"{\"AkorAdi\":\"D#mM7-5\", \"Dizi\":\"11,12,12,11,x,11\"},\n" +
						"{\"AkorAdi\":\"D#mM9\", \"Dizi\":\"1,1,1,3,3,2\"},\n" +
						"{\"AkorAdi\":\"D#mM9\", \"Dizi\":\"6,9,8,7,6,6\"},\n" +
						"{\"AkorAdi\":\"D#mM9\", \"Dizi\":\"x,9,x,10,11,10\"},\n" +
						"{\"AkorAdi\":\"D#mM9\", \"Dizi\":\"11,13,12,11,11,13\"},\n" +
						"{\"AkorAdi\":\"D#mM11\", \"Dizi\":\"1,1,1,1,3,2\"},\n" +
						"{\"AkorAdi\":\"D#mM11\", \"Dizi\":\"x,6,4,7,6,4\"},\n" +
						"{\"AkorAdi\":\"D#mM11\", \"Dizi\":\"6,8,6,7,7,6\"},\n" +
						"{\"AkorAdi\":\"D#mM11\", \"Dizi\":\"11,11,12,11,11,13\"},\n" +
						"{\"AkorAdi\":\"D#add9\", \"Dizi\":\"x,6,5,3,6,3\"},\n" +
						"{\"AkorAdi\":\"D#add9\", \"Dizi\":\"x,6,8,8,6,6\"},\n" +
						"{\"AkorAdi\":\"D#add9\", \"Dizi\":\"x,x,13,12,11,13\"},\n" +
						"{\"AkorAdi\":\"D#madd9\", \"Dizi\":\"x,x,4,3,4,1\"},\n" +
						"{\"AkorAdi\":\"D#madd9\", \"Dizi\":\"x,6,4,3,6,x\"},\n" +
						"{\"AkorAdi\":\"D#madd9\", \"Dizi\":\"x,9,8,8,6,x\"},\n" +
						"{\"AkorAdi\":\"D#madd9\", \"Dizi\":\"x,x,13,11,11,13\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 7:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"E\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"E\", \"Dizi\": \"0,2,2,1,0,0\"},\n" +
						"{\"AkorAdi\": \"E\", \"Dizi\": \"0,2,2,4,5,4\"},\n" +
						"{\"AkorAdi\": \"E\", \"Dizi\": \"[4],7,6,4,5,4\"},\n" +
						"{\"AkorAdi\": \"E\", \"Dizi\": \"[7],7,9,9,9,7\"},\n" +
						"{\"AkorAdi\": \"Em\", \"Dizi\": \"0,2,2,0,0,0\"},\n" +
						"{\"AkorAdi\": \"Em\", \"Dizi\": \"0,2,2,4,5,3\"},\n" +
						"{\"AkorAdi\": \"Em\", \"Dizi\": \"[7],7,9,9,8,7\"},\n" +
						"{\"AkorAdi\": \"Em\", \"Dizi\": \"x,x,9,12,12,12\"},\n" +
						"{\"AkorAdi\": \"Edim\", \"Dizi\": \"0,1,2,0,2,0\"},\n" +
						"{\"AkorAdi\": \"Edim\", \"Dizi\": \"x,x,2,3,2,3\"},\n" +
						"{\"AkorAdi\": \"Edim\", \"Dizi\": \"6,7,8,6,8,6\"},\n" +
						"{\"AkorAdi\": \"Edim\", \"Dizi\": \"9,10,11,9,11,9\"},\n" +
						"{\"AkorAdi\": \"Esus4\", \"Dizi\": \"0,2,2,2,0,0\"},\n" +
						"{\"AkorAdi\": \"Esus4\", \"Dizi\": \"0,2,2,4,5,5\"},\n" +
						"{\"AkorAdi\": \"Esus4\", \"Dizi\": \"[7],7,9,9,10,7\"},\n" +
						"{\"AkorAdi\": \"Esus4\", \"Dizi\": \"x,x,9,9,10,12\"},\n" +
						"{\"AkorAdi\": \"E7sus4\", \"Dizi\": \"0,2,0,2,0,0\"},\n" +
						"{\"AkorAdi\": \"E7sus4\", \"Dizi\": \"x,7,7,7,5,5\"},\n" +
						"{\"AkorAdi\": \"E7sus4\", \"Dizi\": \"[7],7,9,7,10,7\"},\n" +
						"{\"AkorAdi\": \"E7sus4\", \"Dizi\": \"x,x,9,9,10,10\"},\n" +
						"{\"AkorAdi\": \"E-5\", \"Dizi\": \"0,1,2,1,x,x\"},\n" +
						"{\"AkorAdi\": \"E-5\", \"Dizi\": \"x,x,2,3,5,4\"},\n" +
						"{\"AkorAdi\": \"E-5\", \"Dizi\": \"6,7,6,9,[9],x\"},\n" +
						"{\"AkorAdi\": \"E-5\", \"Dizi\": \"x,7,8,9,9,x\"},\n" +
						"{\"AkorAdi\": \"E+5\", \"Dizi\": \"0,3,2,1,1,0\"},\n" +
						"{\"AkorAdi\": \"E+5\", \"Dizi\": \"x,x,6,5,5,4\"},\n" +
						"{\"AkorAdi\": \"E+5\", \"Dizi\": \"x,7,6,5,5,x\"},\n" +
						"{\"AkorAdi\": \"E+5\", \"Dizi\": \"x,x,10,9,9,8\"},\n" +
						"{\"AkorAdi\": \"E6\", \"Dizi\": \"0,2,2,1,2,0\"},\n" +
						"{\"AkorAdi\": \"E6\", \"Dizi\": \"0,2,2,4,2,4\"},\n" +
						"{\"AkorAdi\": \"E6\", \"Dizi\": \"4,4,6,4,5,4\"},\n" +
						"{\"AkorAdi\": \"E6\", \"Dizi\": \"x,7,6,6,x,7\"},\n" +
						"{\"AkorAdi\": \"E69\", \"Dizi\": \"x,2,2,1,2,2\"},\n" +
						"{\"AkorAdi\": \"E69\", \"Dizi\": \"x,7,6,6,7,0\"},\n" +
						"{\"AkorAdi\": \"E69\", \"Dizi\": \"12,11,11,11,12,12\"},\n" +
						"{\"AkorAdi\": \"E7\", \"Dizi\": \"0,2,2,1,3,0\"},\n" +
						"{\"AkorAdi\": \"E7\", \"Dizi\": \"x,7,6,7,5,x\"},\n" +
						"{\"AkorAdi\": \"E7\", \"Dizi\": \"[7],7,9,7,9,7\"},\n" +
						"{\"AkorAdi\": \"E7\", \"Dizi\": \"x,[11],9,9,9,10\"},\n" +
						"{\"AkorAdi\": \"E7-5\", \"Dizi\": \"0,1,0,1,3,0\"},\n" +
						"{\"AkorAdi\": \"E7-5\", \"Dizi\": \"x,x,2,3,3,4\"},\n" +
						"{\"AkorAdi\": \"E7-5\", \"Dizi\": \"x,x,6,7,5,6\"},\n" +
						"{\"AkorAdi\": \"E7-5\", \"Dizi\": \"x,7,8,7,9,10\"},\n" +
						"{\"AkorAdi\": \"E7+5\", \"Dizi\": \"0,[3],0,1,1,0\"},\n" +
						"{\"AkorAdi\": \"E7+5\", \"Dizi\": \"x,x,2,5,3,4\"},\n" +
						"{\"AkorAdi\": \"E7+5\", \"Dizi\": \"x,x,6,7,5,8\"},\n" +
						"{\"AkorAdi\": \"E7+5\", \"Dizi\": \"x,7,10,7,9,8\"},\n" +
						"{\"AkorAdi\": \"E9\", \"Dizi\": \"0,2,0,1,0,2\"},\n" +
						"{\"AkorAdi\": \"E9\", \"Dizi\": \"2,2,2,4,3,4\"},\n" +
						"{\"AkorAdi\": \"E9\", \"Dizi\": \"[7],7,6,7,7,0\"},\n" +
						"{\"AkorAdi\": \"E9\", \"Dizi\": \"x,9,9,9,9,10\"},\n" +
						"{\"AkorAdi\": \"E9-5\", \"Dizi\": \"x,1,2,1,3,2\"},\n" +
						"{\"AkorAdi\": \"E9-5\", \"Dizi\": \"x,x,4,3,3,4\"},\n" +
						"{\"AkorAdi\": \"E9-5\", \"Dizi\": \"x,7,6,7,7,6\"},\n" +
						"{\"AkorAdi\": \"E9-5\", \"Dizi\": \"x,9,8,7,9,x\"},\n" +
						"{\"AkorAdi\": \"E9+5\", \"Dizi\": \"0,3,0,1,3,2\"},\n" +
						"{\"AkorAdi\": \"E9+5\", \"Dizi\": \"[4],5,4,5,5,4\"},\n" +
						"{\"AkorAdi\": \"E9+5\", \"Dizi\": \"x,7,6,7,7,8\"},\n" +
						"{\"AkorAdi\": \"E9+5\", \"Dizi\": \"10,9,10,9,9,10\"},\n" +
						"{\"AkorAdi\": \"E7-9\", \"Dizi\": \"1,2,2,1,3,1\"},\n" +
						"{\"AkorAdi\": \"E7-9\", \"Dizi\": \"x,7,6,7,6,7\"},\n" +
						"{\"AkorAdi\": \"E7-9\", \"Dizi\": \"[7],8,9,7,9,7\"},\n" +
						"{\"AkorAdi\": \"E7-9\", \"Dizi\": \"10,11,12,10,12,10\"},\n" +
						"{\"AkorAdi\": \"E7+9\", \"Dizi\": \"0,2,2,1,3,3\"},\n" +
						"{\"AkorAdi\": \"E7+9\", \"Dizi\": \"3,5,6,4,3,3\"},\n" +
						"{\"AkorAdi\": \"E7+9\", \"Dizi\": \"x,7,6,7,8,x\"},\n" +
						"{\"AkorAdi\": \"E7+9\", \"Dizi\": \"x,10,9,9,9,10\"},\n" +
						"{\"AkorAdi\": \"E11\", \"Dizi\": \"0,2,2,2,3,4\"},\n" +
						"{\"AkorAdi\": \"E11\", \"Dizi\": \"x,7,6,7,5,5\"},\n" +
						"{\"AkorAdi\": \"E11\", \"Dizi\": \"x,7,7,7,9,7\"},\n" +
						"{\"AkorAdi\": \"E11\", \"Dizi\": \"x,11,9,9,10,10\"},\n" +
						"{\"AkorAdi\": \"E+11\", \"Dizi\": \"2,2,2,3,3,4\"},\n" +
						"{\"AkorAdi\": \"E+11\", \"Dizi\": \"x,7,6,7,7,6\"},\n" +
						"{\"AkorAdi\": \"E+11\", \"Dizi\": \"x,9,8,9,9,10\"},\n" +
						"{\"AkorAdi\": \"E+11\", \"Dizi\": \"12,11,12,11,11,12\"},\n" +
						"{\"AkorAdi\": \"E13\", \"Dizi\": \"0,2,0,1,2,0\"},\n" +
						"{\"AkorAdi\": \"E13\", \"Dizi\": \"0,0,0,1,2,2\"},\n" +
						"{\"AkorAdi\": \"E13\", \"Dizi\": \"x,5,6,6,5,x\"},\n" +
						"{\"AkorAdi\": \"E13\", \"Dizi\": \"[7],7,9,7,9,9\"},\n" +
						"{\"AkorAdi\": \"EM7\", \"Dizi\": \"0,2,1,1,0,0\"},\n" +
						"{\"AkorAdi\": \"EM7\", \"Dizi\": \"0,2,2,4,4,4\"},\n" +
						"{\"AkorAdi\": \"EM7\", \"Dizi\": \"4,6,6,4,5,4\"},\n" +
						"{\"AkorAdi\": \"EM7\", \"Dizi\": \"[7],7,9,8,9,7\"},\n" +
						"{\"AkorAdi\": \"EM7-5\", \"Dizi\": \"0,1,1,1,x,x\"},\n" +
						"{\"AkorAdi\": \"EM7-5\", \"Dizi\": \"x,x,2,3,4,4\"},\n" +
						"{\"AkorAdi\": \"EM7-5\", \"Dizi\": \"x,7,8,8,9,x\"},\n" +
						"{\"AkorAdi\": \"EM7-5\", \"Dizi\": \"x,x,8,9,9,11\"},\n" +
						"{\"AkorAdi\": \"EM7+5\", \"Dizi\": \"0,3,2,1,4,4\"},\n" +
						"{\"AkorAdi\": \"EM7+5\", \"Dizi\": \"[4],7,6,5,4,4\"},\n" +
						"{\"AkorAdi\": \"EM7+5\", \"Dizi\": \"x,7,10,8,9,x\"},\n" +
						"{\"AkorAdi\": \"EM7+5\", \"Dizi\": \"x,x,10,9,9,11\"},\n" +
						"{\"AkorAdi\": \"EM9\", \"Dizi\": \"0,2,1,1,0,2\"},\n" +
						"{\"AkorAdi\": \"EM9\", \"Dizi\": \"4,2,2,4,4,2\"},\n" +
						"{\"AkorAdi\": \"EM9\", \"Dizi\": \"[7],7,6,8,7,x\"},\n" +
						"{\"AkorAdi\": \"EM9\", \"Dizi\": \"x,9,9,11,9,11\"},\n" +
						"{\"AkorAdi\": \"EM11\", \"Dizi\": \"0,0,1,1,0,2\"},\n" +
						"{\"AkorAdi\": \"EM11\", \"Dizi\": \"x,x,6,8,7,5\"},\n" +
						"{\"AkorAdi\": \"EM11\", \"Dizi\": \"[7],7,7,8,7,7\"},\n" +
						"{\"AkorAdi\": \"EM11\", \"Dizi\": \"x,9,9,11,10,11\"},\n" +
						"{\"AkorAdi\": \"EM13\", \"Dizi\": \"0,2,1,1,2,2\"},\n" +
						"{\"AkorAdi\": \"EM13\", \"Dizi\": \"x,7,6,6,4,[4]\"},\n" +
						"{\"AkorAdi\": \"EM13\", \"Dizi\": \"x,7,[7],8,9,9\"},\n" +
						"{\"AkorAdi\": \"EM13\", \"Dizi\": \"12,11,11,11,12,11\"},\n" +
						"{\"AkorAdi\": \"Em6\", \"Dizi\": \"0,2,2,0,2,0\"},\n" +
						"{\"AkorAdi\": \"Em6\", \"Dizi\": \"0,2,2,4,2,3\"},\n" +
						"{\"AkorAdi\": \"Em6\", \"Dizi\": \"x,7,5,6,5,7\"},\n" +
						"{\"AkorAdi\": \"Em6\", \"Dizi\": \"x,x,9,9,8,9\"},\n" +
						"{\"AkorAdi\": \"Em69\", \"Dizi\": \"0,2,2,0,2,2\"},\n" +
						"{\"AkorAdi\": \"Em69\", \"Dizi\": \"3,2,2,4,2,2\"},\n" +
						"{\"AkorAdi\": \"Em69\", \"Dizi\": \"x,7,5,6,7,7\"},\n" +
						"{\"AkorAdi\": \"Em69\", \"Dizi\": \"x,10,11,11,12,12\"},\n" +
						"{\"AkorAdi\": \"Em7\", \"Dizi\": \"0,2,0,0,0,0\"},\n" +
						"{\"AkorAdi\": \"Em7\", \"Dizi\": \"0,2,2,4,3,3\"},\n" +
						"{\"AkorAdi\": \"Em7\", \"Dizi\": \"[7],7,9,7,8,7\"},\n" +
						"{\"AkorAdi\": \"Em7\", \"Dizi\": \"x,x,9,9,8,10\"},\n" +
						"{\"AkorAdi\": \"Em7-5\", \"Dizi\": \"0,1,2,3,3,3\"},\n" +
						"{\"AkorAdi\": \"Em7-5\", \"Dizi\": \"x,7,8,7,8,x\"},\n" +
						"{\"AkorAdi\": \"Em7-5\", \"Dizi\": \"x,x,8,9,8,10\"},\n" +
						"{\"AkorAdi\": \"Em7-5\", \"Dizi\": \"10,10,12,12,11,12\"},\n" +
						"{\"AkorAdi\": \"Em9\", \"Dizi\": \"0,2,0,0,0,2\"},\n" +
						"{\"AkorAdi\": \"Em9\", \"Dizi\": \"2,2,0,0,0,0\"},\n" +
						"{\"AkorAdi\": \"Em9\", \"Dizi\": \"x,7,5,7,7,7\"},\n" +
						"{\"AkorAdi\": \"Em9\", \"Dizi\": \"0,10,12,11,12,10\"},\n" +
						"{\"AkorAdi\": \"Em11\", \"Dizi\": \"0,0,0,0,0,2\"},\n" +
						"{\"AkorAdi\": \"Em11\", \"Dizi\": \"3,2,2,2,3,2\"},\n" +
						"{\"AkorAdi\": \"Em11\", \"Dizi\": \"x,7,5,7,7,5\"},\n" +
						"{\"AkorAdi\": \"Em11\", \"Dizi\": \"12,10,12,11,10,10\"},\n" +
						"{\"AkorAdi\": \"EmM7\", \"Dizi\": \"0,2,1,0,0,0\"},\n" +
						"{\"AkorAdi\": \"EmM7\", \"Dizi\": \"0,2,2,4,4,3\"},\n" +
						"{\"AkorAdi\": \"EmM7\", \"Dizi\": \"x,6,5,4,5,x\"},\n" +
						"{\"AkorAdi\": \"EmM7\", \"Dizi\": \"7,7,9,8,8,7\"},\n" +
						"{\"AkorAdi\": \"EmM7-5\", \"Dizi\": \"0,1,1,0,x,0\"},\n" +
						"{\"AkorAdi\": \"EmM7-5\", \"Dizi\": \"x,x,2,3,4,3\"},\n" +
						"{\"AkorAdi\": \"EmM7-5\", \"Dizi\": \"[6],7,8,8,8,x\"},\n" +
						"{\"AkorAdi\": \"EmM7-5\", \"Dizi\": \"x,7,8,8,8,x\"},\n" +
						"{\"AkorAdi\": \"EmM9\", \"Dizi\": \"0,2,1,0,0,2\"},\n" +
						"{\"AkorAdi\": \"EmM9\", \"Dizi\": \"2,2,2,4,4,3\"},\n" +
						"{\"AkorAdi\": \"EmM9\", \"Dizi\": \"7,10,9,8,7,7\"},\n" +
						"{\"AkorAdi\": \"EmM9\", \"Dizi\": \"x,10,x,11,12,11\"},\n" +
						"{\"AkorAdi\": \"EmM11\", \"Dizi\": \"0,0,1,0,0,2\"},\n" +
						"{\"AkorAdi\": \"EmM11\", \"Dizi\": \"2,2,2,2,4,3\"},\n" +
						"{\"AkorAdi\": \"EmM11\", \"Dizi\": \"x,7,5,8,7,5\"},\n" +
						"{\"AkorAdi\": \"EmM11\", \"Dizi\": \"7,9,7,8,8,7\"},\n" +
						"{\"AkorAdi\": \"Eadd9\", \"Dizi\": \"0,2,2,1,0,2\"},\n" +
						"{\"AkorAdi\": \"Eadd9\", \"Dizi\": \"x,7,6,4,7,4\"},\n" +
						"{\"AkorAdi\": \"Eadd9\", \"Dizi\": \"x,7,9,9,7,7\"},\n" +
						"{\"AkorAdi\": \"Emadd9\", \"Dizi\": \"0,2,2,0,0,2\"},\n" +
						"{\"AkorAdi\": \"Emadd9\", \"Dizi\": \"x,x,5,4,5,2\"},\n" +
						"{\"AkorAdi\": \"Emadd9\", \"Dizi\": \"x,7,5,4,7,x\"},\n" +
						"{\"AkorAdi\": \"Emadd9\", \"Dizi\": \"x,10,9,9,7,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 8:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"F\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"F\", \"Dizi\": \"1,3,3,2,1,1\"},\n" +
						"{\"AkorAdi\": \"F\", \"Dizi\": \"x,3,3,5,6,5\"},\n" +
						"{\"AkorAdi\": \"F\", \"Dizi\": \"[5],8,7,5,6,5\"},\n" +
						"{\"AkorAdi\": \"F\", \"Dizi\": \"[8],8,10,10,10,8\"},\n" +
						"{\"AkorAdi\": \"Fm\", \"Dizi\": \"1,3,3,1,1,1\"},\n" +
						"{\"AkorAdi\": \"Fm\", \"Dizi\": \"x,x,6,5,6,4\"},\n" +
						"{\"AkorAdi\": \"Fm\", \"Dizi\": \"[8],8,10,10,9,8\"},\n" +
						"{\"AkorAdi\": \"Fm\", \"Dizi\": \"x,[11],10,13,13,13\"},\n" +
						"{\"AkorAdi\": \"Fdim\", \"Dizi\": \"x,x,0,1,0,1\"},\n" +
						"{\"AkorAdi\": \"Fdim\", \"Dizi\": \"x,x,3,4,3,4\"},\n" +
						"{\"AkorAdi\": \"Fdim\", \"Dizi\": \"4,5,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"Fdim\", \"Dizi\": \"7,8,9,7,9,7\"},\n" +
						"{\"AkorAdi\": \"Fsus4\", \"Dizi\": \"1,1,3,3,1,1\"},\n" +
						"{\"AkorAdi\": \"Fsus4\", \"Dizi\": \"x,3,3,5,6,6\"},\n" +
						"{\"AkorAdi\": \"Fsus4\", \"Dizi\": \"[8],8,10,10,11,8\"},\n" +
						"{\"AkorAdi\": \"Fsus4\", \"Dizi\": \"x,x,10,10,11,13\"},\n" +
						"{\"AkorAdi\": \"F7sus4\", \"Dizi\": \"1,3,1,3,1,1\"},\n" +
						"{\"AkorAdi\": \"F7sus4\", \"Dizi\": \"x,8,8,8,6,6\"},\n" +
						"{\"AkorAdi\": \"F7sus4\", \"Dizi\": \"[8],8,10,8,11,8\"},\n" +
						"{\"AkorAdi\": \"F7sus4\", \"Dizi\": \"x,x,10,10,11,11\"},\n" +
						"{\"AkorAdi\": \"F-5\", \"Dizi\": \"1,2,3,2,0,x\"},\n" +
						"{\"AkorAdi\": \"F-5\", \"Dizi\": \"x,x,3,4,6,5\"},\n" +
						"{\"AkorAdi\": \"F-5\", \"Dizi\": \"7,8,7,10,[10],x\"},\n" +
						"{\"AkorAdi\": \"F-5\", \"Dizi\": \"x,8,9,10,10,x\"},\n" +
						"{\"AkorAdi\": \"F+5\", \"Dizi\": \"x,4,3,2,2,x\"},\n" +
						"{\"AkorAdi\": \"F+5\", \"Dizi\": \"x,x,7,6,6,5\"},\n" +
						"{\"AkorAdi\": \"F+5\", \"Dizi\": \"x,8,7,6,6,x\"},\n" +
						"{\"AkorAdi\": \"F+5\", \"Dizi\": \"x,x,11,10,10,9\"},\n" +
						"{\"AkorAdi\": \"F6\", \"Dizi\": \"x,3,3,2,3,x\"},\n" +
						"{\"AkorAdi\": \"F6\", \"Dizi\": \"x,3,3,5,3,5\"},\n" +
						"{\"AkorAdi\": \"F6\", \"Dizi\": \"5,5,7,5,6,5\"},\n" +
						"{\"AkorAdi\": \"F6\", \"Dizi\": \"x,8,7,7,x,8\"},\n" +
						"{\"AkorAdi\": \"F69\", \"Dizi\": \"1,0,0,0,1,1\"},\n" +
						"{\"AkorAdi\": \"F69\", \"Dizi\": \"x,3,3,2,3,3\"},\n" +
						"{\"AkorAdi\": \"F69\", \"Dizi\": \"x,8,7,7,8,8\"},\n" +
						"{\"AkorAdi\": \"F7\", \"Dizi\": \"1,3,1,2,1,1\"},\n" +
						"{\"AkorAdi\": \"F7\", \"Dizi\": \"1,3,3,5,4,5\"},\n" +
						"{\"AkorAdi\": \"F7\", \"Dizi\": \"[8],8,10,8,10,8\"},\n" +
						"{\"AkorAdi\": \"F7\", \"Dizi\": \"x,[12],10,10,10,11\"},\n" +
						"{\"AkorAdi\": \"F7-5\", \"Dizi\": \"1,0,1,2,0,1\"},\n" +
						"{\"AkorAdi\": \"F7-5\", \"Dizi\": \"x,x,3,4,4,5\"},\n" +
						"{\"AkorAdi\": \"F7-5\", \"Dizi\": \"x,6,7,8,6,7\"},\n" +
						"{\"AkorAdi\": \"F7-5\", \"Dizi\": \"x,8,9,8,10,11\"},\n" +
						"{\"AkorAdi\": \"F7+5\", \"Dizi\": \"1,[4],1,2,2,1\"},\n" +
						"{\"AkorAdi\": \"F7+5\", \"Dizi\": \"x,x,3,6,4,5\"},\n" +
						"{\"AkorAdi\": \"F7+5\", \"Dizi\": \"x,x,7,8,6,9\"},\n" +
						"{\"AkorAdi\": \"F7+5\", \"Dizi\": \"x,8,11,8,10,9\"},\n" +
						"{\"AkorAdi\": \"F9\", \"Dizi\": \"1,3,1,2,1,3\"},\n" +
						"{\"AkorAdi\": \"F9\", \"Dizi\": \"3,3,3,5,4,5\"},\n" +
						"{\"AkorAdi\": \"F9\", \"Dizi\": \"[8],8,7,8,8,8\"},\n" +
						"{\"AkorAdi\": \"F9\", \"Dizi\": \"x,10,10,10,10,11\"},\n" +
						"{\"AkorAdi\": \"F9-5\", \"Dizi\": \"x,0,1,0,0,3\"},\n" +
						"{\"AkorAdi\": \"F9-5\", \"Dizi\": \"x,2,3,2,4,3\"},\n" +
						"{\"AkorAdi\": \"F9-5\", \"Dizi\": \"x,x,5,4,4,5\"},\n" +
						"{\"AkorAdi\": \"F9-5\", \"Dizi\": \"x,8,7,8,8,7\"},\n" +
						"{\"AkorAdi\": \"F9+5\", \"Dizi\": \"x,0,1,0,2,1\"},\n" +
						"{\"AkorAdi\": \"F9+5\", \"Dizi\": \"[5],6,5,6,6,5\"},\n" +
						"{\"AkorAdi\": \"F9+5\", \"Dizi\": \"x,8,7,8,8,9\"},\n" +
						"{\"AkorAdi\": \"F9+5\", \"Dizi\": \"11,10,11,10,10,11\"},\n" +
						"{\"AkorAdi\": \"F7-9\", \"Dizi\": \"1,3,1,2,1,2\"},\n" +
						"{\"AkorAdi\": \"F7-9\", \"Dizi\": \"2,3,3,2,4,2\"},\n" +
						"{\"AkorAdi\": \"F7-9\", \"Dizi\": \"x,8,7,8,7,8\"},\n" +
						"{\"AkorAdi\": \"F7-9\", \"Dizi\": \"[8],9,10,8,10,8\"},\n" +
						"{\"AkorAdi\": \"F7+9\", \"Dizi\": \"x,3,3,2,4,4\"},\n" +
						"{\"AkorAdi\": \"F7+9\", \"Dizi\": \"4,6,7,5,4,4\"},\n" +
						"{\"AkorAdi\": \"F7+9\", \"Dizi\": \"x,8,7,8,9,x\"},\n" +
						"{\"AkorAdi\": \"F7+9\", \"Dizi\": \"x,11,10,10,10,11\"},\n" +
						"{\"AkorAdi\": \"F11\", \"Dizi\": \"[3],[3],3,3,4,5\"},\n" +
						"{\"AkorAdi\": \"F11\", \"Dizi\": \"x,8,7,8,6,6\"},\n" +
						"{\"AkorAdi\": \"F11\", \"Dizi\": \"x,8,8,8,10,8\"},\n" +
						"{\"AkorAdi\": \"F11\", \"Dizi\": \"x,12,10,10,11,11\"},\n" +
						"{\"AkorAdi\": \"F+11\", \"Dizi\": \"1,0,1,0,0,1\"},\n" +
						"{\"AkorAdi\": \"F+11\", \"Dizi\": \"3,3,3,4,4,5\"},\n" +
						"{\"AkorAdi\": \"F+11\", \"Dizi\": \"x,8,7,8,8,7\"},\n" +
						"{\"AkorAdi\": \"F+11\", \"Dizi\": \"x,10,9,10,10,11\"},\n" +
						"{\"AkorAdi\": \"F13\", \"Dizi\": \"1,3,1,2,3,1\"},\n" +
						"{\"AkorAdi\": \"F13\", \"Dizi\": \"1,1,1,2,3,3\"},\n" +
						"{\"AkorAdi\": \"F13\", \"Dizi\": \"x,6,7,7,6,x\"},\n" +
						"{\"AkorAdi\": \"F13\", \"Dizi\": \"[8],8,10,8,10,10\"},\n" +
						"{\"AkorAdi\": \"FM7\", \"Dizi\": \"1,3,2,2,1,1\"},\n" +
						"{\"AkorAdi\": \"FM7\", \"Dizi\": \"x,[3],3,5,5,5\"},\n" +
						"{\"AkorAdi\": \"FM7\", \"Dizi\": \"5,7,7,5,6,5\"},\n" +
						"{\"AkorAdi\": \"FM7\", \"Dizi\": \"[8],8,10,9,10,8\"},\n" +
						"{\"AkorAdi\": \"FM7-5\", \"Dizi\": \"1,2,2,2,x,x\"},\n" +
						"{\"AkorAdi\": \"FM7-5\", \"Dizi\": \"x,x,3,4,5,5\"},\n" +
						"{\"AkorAdi\": \"FM7-5\", \"Dizi\": \"x,8,9,9,10,x\"},\n" +
						"{\"AkorAdi\": \"FM7-5\", \"Dizi\": \"x,x,9,10,10,12\"},\n" +
						"{\"AkorAdi\": \"FM7+5\", \"Dizi\": \"x,4,3,2,5,5\"},\n" +
						"{\"AkorAdi\": \"FM7+5\", \"Dizi\": \"[5],8,7,6,5,5\"},\n" +
						"{\"AkorAdi\": \"FM7+5\", \"Dizi\": \"x,8,11,9,10,x\"},\n" +
						"{\"AkorAdi\": \"FM7+5\", \"Dizi\": \"x,x,11,10,10,12\"},\n" +
						"{\"AkorAdi\": \"FM9\", \"Dizi\": \"0,0,2,0,1,1\"},\n" +
						"{\"AkorAdi\": \"FM9\", \"Dizi\": \"x,x,2,2,1,3\"},\n" +
						"{\"AkorAdi\": \"FM9\", \"Dizi\": \"[8],8,7,9,8,x\"},\n" +
						"{\"AkorAdi\": \"FM9\", \"Dizi\": \"x,10,10,12,10,12\"},\n" +
						"{\"AkorAdi\": \"FM11\", \"Dizi\": \"1,1,2,2,1,3\"},\n" +
						"{\"AkorAdi\": \"FM11\", \"Dizi\": \"x,x,7,9,8,6\"},\n" +
						"{\"AkorAdi\": \"FM11\", \"Dizi\": \"[8],8,8,9,8,8\"},\n" +
						"{\"AkorAdi\": \"FM11\", \"Dizi\": \"x,10,10,12,11,12\"},\n" +
						"{\"AkorAdi\": \"FM13\", \"Dizi\": \"1,0,0,0,1,0\"},\n" +
						"{\"AkorAdi\": \"FM13\", \"Dizi\": \"x,5,3,3,5,5\"},\n" +
						"{\"AkorAdi\": \"FM13\", \"Dizi\": \"x,8,7,7,5,[5]\"},\n" +
						"{\"AkorAdi\": \"FM13\", \"Dizi\": \"x,8,[8],9,10,10\"},\n" +
						"{\"AkorAdi\": \"Fm6\", \"Dizi\": \"1,x,0,1,1,1\"},\n" +
						"{\"AkorAdi\": \"Fm6\", \"Dizi\": \"x,[3],3,5,3,4\"},\n" +
						"{\"AkorAdi\": \"Fm6\", \"Dizi\": \"x,8,6,7,6,8\"},\n" +
						"{\"AkorAdi\": \"Fm6\", \"Dizi\": \"x,x,10,10,9,10\"},\n" +
						"{\"AkorAdi\": \"Fm69\", \"Dizi\": \"1,3,3,1,3,3\"},\n" +
						"{\"AkorAdi\": \"Fm69\", \"Dizi\": \"4,3,3,5,3,3\"},\n" +
						"{\"AkorAdi\": \"Fm69\", \"Dizi\": \"x,8,6,7,8,8\"},\n" +
						"{\"AkorAdi\": \"Fm69\", \"Dizi\": \"x,11,12,12,13,13\"},\n" +
						"{\"AkorAdi\": \"Fm7\", \"Dizi\": \"1,3,1,1,1,1\"},\n" +
						"{\"AkorAdi\": \"Fm7\", \"Dizi\": \"x,3,3,5,4,4\"},\n" +
						"{\"AkorAdi\": \"Fm7\", \"Dizi\": \"[8],8,10,8,9,8\"},\n" +
						"{\"AkorAdi\": \"Fm7\", \"Dizi\": \"x,x,10,10,9,11\"},\n" +
						"{\"AkorAdi\": \"Fm7-5\", \"Dizi\": \"x,x,3,4,4,4\"},\n" +
						"{\"AkorAdi\": \"Fm7-5\", \"Dizi\": \"x,8,9,8,9,x\"},\n" +
						"{\"AkorAdi\": \"Fm7-5\", \"Dizi\": \"x,x,9,10,9,11\"},\n" +
						"{\"AkorAdi\": \"Fm7-5\", \"Dizi\": \"11,11,13,13,12,13\"},\n" +
						"{\"AkorAdi\": \"Fm9\", \"Dizi\": \"1,3,1,1,1,3\"},\n" +
						"{\"AkorAdi\": \"Fm9\", \"Dizi\": \"3,3,1,1,1,1\"},\n" +
						"{\"AkorAdi\": \"Fm9\", \"Dizi\": \"x,8,6,8,8,8\"},\n" +
						"{\"AkorAdi\": \"Fm9\", \"Dizi\": \"x,10,10,10,9,11\"},\n" +
						"{\"AkorAdi\": \"Fm11\", \"Dizi\": \"1,1,1,1,1,3\"},\n" +
						"{\"AkorAdi\": \"Fm11\", \"Dizi\": \"4,3,3,3,4,3\"},\n" +
						"{\"AkorAdi\": \"Fm11\", \"Dizi\": \"x,8,6,8,8,6\"},\n" +
						"{\"AkorAdi\": \"Fm11\", \"Dizi\": \"13,11,13,12,11,11\"},\n" +
						"{\"AkorAdi\": \"FmM7\", \"Dizi\": \"1,3,2,1,1,1\"},\n" +
						"{\"AkorAdi\": \"FmM7\", \"Dizi\": \"x,3,3,5,5,4\"},\n" +
						"{\"AkorAdi\": \"FmM7\", \"Dizi\": \"x,7,6,5,6,x\"},\n" +
						"{\"AkorAdi\": \"FmM7\", \"Dizi\": \"8,8,10,9,9,8\"},\n" +
						"{\"AkorAdi\": \"FmM7-5\", \"Dizi\": \"1,2,2,1,0,0\"},\n" +
						"{\"AkorAdi\": \"FmM7-5\", \"Dizi\": \"x,x,3,4,5,4\"},\n" +
						"{\"AkorAdi\": \"FmM7-5\", \"Dizi\": \"[7],8,9,9,9,x\"},\n" +
						"{\"AkorAdi\": \"FmM7-5\", \"Dizi\": \"x,8,9,9,9,x\"},\n" +
						"{\"AkorAdi\": \"FmM9\", \"Dizi\": \"1,3,2,1,1,3\"},\n" +
						"{\"AkorAdi\": \"FmM9\", \"Dizi\": \"3,3,3,5,5,4\"},\n" +
						"{\"AkorAdi\": \"FmM9\", \"Dizi\": \"8,11,10,9,8,8\"},\n" +
						"{\"AkorAdi\": \"FmM9\", \"Dizi\": \"x,11,x,12,13,12\"},\n" +
						"{\"AkorAdi\": \"FmM11\", \"Dizi\": \"1,1,2,1,1,3\"},\n" +
						"{\"AkorAdi\": \"FmM11\", \"Dizi\": \"3,3,3,3,5,4\"},\n" +
						"{\"AkorAdi\": \"FmM11\", \"Dizi\": \"x,8,6,9,8,6\"},\n" +
						"{\"AkorAdi\": \"FmM11\", \"Dizi\": \"8,10,8,9,9,8\"},\n" +
						"{\"AkorAdi\": \"Fadd9\", \"Dizi\": \"x,x,3,2,1,3\"},\n" +
						"{\"AkorAdi\": \"Fadd9\", \"Dizi\": \"x,8,7,5,8,5\"},\n" +
						"{\"AkorAdi\": \"Fadd9\", \"Dizi\": \"x,8,10,10,8,8\"},\n" +
						"{\"AkorAdi\": \"Fmadd9\", \"Dizi\": \"x,x,3,1,1,3\"},\n" +
						"{\"AkorAdi\": \"Fmadd9\", \"Dizi\": \"x,x,6,5,6,3\"},\n" +
						"{\"AkorAdi\": \"Fmadd9\", \"Dizi\": \"x,8,6,5,8,x\"},\n" +
						"{\"AkorAdi\": \"Fmadd9\", \"Dizi\": \"x,11,10,10,8,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 9:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"F#\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"F#\", \"Dizi\": \"2,4,4,3,2,2\"},\n" +
						"{\"AkorAdi\": \"F#\", \"Dizi\": \"x,4,4,6,7,6\"},\n" +
						"{\"AkorAdi\": \"F#\", \"Dizi\": \"[6],9,8,6,7,6\"},\n" +
						"{\"AkorAdi\": \"F#\", \"Dizi\": \"[9],9,11,11,11,9\"},\n" +
						"{\"AkorAdi\": \"F#m\", \"Dizi\": \"2,4,4,2,2,2\"},\n" +
						"{\"AkorAdi\": \"F#m\", \"Dizi\": \"x,4,4,6,7,5\"},\n" +
						"{\"AkorAdi\": \"F#m\", \"Dizi\": \"x,x,7,6,7,5\"},\n" +
						"{\"AkorAdi\": \"F#m\", \"Dizi\": \"[9],9,11,11,10,9\"},\n" +
						"{\"AkorAdi\": \"F#dim\", \"Dizi\": \"x,x,1,2,1,2\"},\n" +
						"{\"AkorAdi\": \"F#dim\", \"Dizi\": \"2,3,4,2,4,2\"},\n" +
						"{\"AkorAdi\": \"F#dim\", \"Dizi\": \"x,x,4,5,4,5\"},\n" +
						"{\"AkorAdi\": \"F#dim\", \"Dizi\": \"8,9,10,8,10,8\"},\n" +
						"{\"AkorAdi\": \"F#sus4\", \"Dizi\": \"2,2,4,4,2,2\"},\n" +
						"{\"AkorAdi\": \"F#sus4\", \"Dizi\": \"x,4,4,6,7,7\"},\n" +
						"{\"AkorAdi\": \"F#sus4\", \"Dizi\": \"[9],9,11,11,12,9\"},\n" +
						"{\"AkorAdi\": \"F#sus4\", \"Dizi\": \"x,x,11,11,12,14\"},\n" +
						"{\"AkorAdi\": \"F#7sus4\", \"Dizi\": \"2,4,2,4,2,2\"},\n" +
						"{\"AkorAdi\": \"F#7sus4\", \"Dizi\": \"x,9,9,9,7,7\"},\n" +
						"{\"AkorAdi\": \"F#7sus4\", \"Dizi\": \"[9],9,11,9,12,9\"},\n" +
						"{\"AkorAdi\": \"F#7sus4\", \"Dizi\": \"x,x,11,11,12,12\"},\n" +
						"{\"AkorAdi\": \"F#-5\", \"Dizi\": \"x,x,4,3,1,2\"},\n" +
						"{\"AkorAdi\": \"F#-5\", \"Dizi\": \"x,x,8,5,7,6\"},\n" +
						"{\"AkorAdi\": \"F#-5\", \"Dizi\": \"8,9,8,11,[11],x\"},\n" +
						"{\"AkorAdi\": \"F#-5\", \"Dizi\": \"x,9,10,11,11,x\"},\n" +
						"{\"AkorAdi\": \"F#+5\", \"Dizi\": \"x,x,4,3,3,2\"},\n" +
						"{\"AkorAdi\": \"F#+5\", \"Dizi\": \"x,5,4,3,3,x\"},\n" +
						"{\"AkorAdi\": \"F#+5\", \"Dizi\": \"x,x,8,7,7,6\"},\n" +
						"{\"AkorAdi\": \"F#+5\", \"Dizi\": \"x,9,8,7,7,x\"},\n" +
						"{\"AkorAdi\": \"F#6\", \"Dizi\": \"x,4,4,3,4,x\"},\n" +
						"{\"AkorAdi\": \"F#6\", \"Dizi\": \"x,4,4,6,4,6\"},\n" +
						"{\"AkorAdi\": \"F#6\", \"Dizi\": \"6,6,8,6,7,6\"},\n" +
						"{\"AkorAdi\": \"F#6\", \"Dizi\": \"x,9,8,8,x,9\"},\n" +
						"{\"AkorAdi\": \"F#69\", \"Dizi\": \"2,1,1,1,2,2\"},\n" +
						"{\"AkorAdi\": \"F#69\", \"Dizi\": \"x,4,4,3,4,4\"},\n" +
						"{\"AkorAdi\": \"F#69\", \"Dizi\": \"x,9,8,8,9,9\"},\n" +
						"{\"AkorAdi\": \"F#7\", \"Dizi\": \"2,4,2,3,2,2\"},\n" +
						"{\"AkorAdi\": \"F#7\", \"Dizi\": \"x,4,4,6,5,6\"},\n" +
						"{\"AkorAdi\": \"F#7\", \"Dizi\": \"[9],9,11,9,11,9\"},\n" +
						"{\"AkorAdi\": \"F#7\", \"Dizi\": \"x,13,11,11,11,12\"},\n" +
						"{\"AkorAdi\": \"F#7-5\", \"Dizi\": \"0,1,2,3,1,2\"},\n" +
						"{\"AkorAdi\": \"F#7-5\", \"Dizi\": \"x,x,4,5,5,6\"},\n" +
						"{\"AkorAdi\": \"F#7-5\", \"Dizi\": \"x,7,8,9,7,8\"},\n" +
						"{\"AkorAdi\": \"F#7-5\", \"Dizi\": \"x,9,10,9,11,12\"},\n" +
						"{\"AkorAdi\": \"F#7+5\", \"Dizi\": \"x,x,2,3,3,2\"},\n" +
						"{\"AkorAdi\": \"F#7+5\", \"Dizi\": \"x,x,4,7,5,6\"},\n" +
						"{\"AkorAdi\": \"F#7+5\", \"Dizi\": \"x,x,8,9,7,10\"},\n" +
						"{\"AkorAdi\": \"F#7+5\", \"Dizi\": \"x,9,12,9,11,10\"},\n" +
						"{\"AkorAdi\": \"F#9\", \"Dizi\": \"2,4,2,3,2,4\"},\n" +
						"{\"AkorAdi\": \"F#9\", \"Dizi\": \"4,4,4,6,5,6\"},\n" +
						"{\"AkorAdi\": \"F#9\", \"Dizi\": \"[9],9,8,9,9,9\"},\n" +
						"{\"AkorAdi\": \"F#9\", \"Dizi\": \"x,11,11,11,11,12\"},\n" +
						"{\"AkorAdi\": \"F#9-5\", \"Dizi\": \"2,1,2,1,1,2\"},\n" +
						"{\"AkorAdi\": \"F#9-5\", \"Dizi\": \"x,3,4,3,5,4\"},\n" +
						"{\"AkorAdi\": \"F#9-5\", \"Dizi\": \"x,x,6,5,5,6\"},\n" +
						"{\"AkorAdi\": \"F#9-5\", \"Dizi\": \"x,9,8,9,9,8\"},\n" +
						"{\"AkorAdi\": \"F#9+5\", \"Dizi\": \"x,1,2,1,3,2\"},\n" +
						"{\"AkorAdi\": \"F#9+5\", \"Dizi\": \"[6],7,6,7,7,6\"},\n" +
						"{\"AkorAdi\": \"F#9+5\", \"Dizi\": \"x,9,8,9,9,10\"},\n" +
						"{\"AkorAdi\": \"F#9+5\", \"Dizi\": \"12,11,12,11,11,12\"},\n" +
						"{\"AkorAdi\": \"F#7-9\", \"Dizi\": \"2,4,2,3,2,3\"},\n" +
						"{\"AkorAdi\": \"F#7-9\", \"Dizi\": \"3,4,4,3,5,3\"},\n" +
						"{\"AkorAdi\": \"F#7-9\", \"Dizi\": \"x,9,8,9,8,9\"},\n" +
						"{\"AkorAdi\": \"F#7-9\", \"Dizi\": \"[9],10,11,9,11,9\"},\n" +
						"{\"AkorAdi\": \"F#7+9\", \"Dizi\": \"x,4,4,3,5,5\"},\n" +
						"{\"AkorAdi\": \"F#7+9\", \"Dizi\": \"5,7,8,6,5,5\"},\n" +
						"{\"AkorAdi\": \"F#7+9\", \"Dizi\": \"x,9,8,9,10,x\"},\n" +
						"{\"AkorAdi\": \"F#7+9\", \"Dizi\": \"x,12,11,11,11,12\"},\n" +
						"{\"AkorAdi\": \"F#11\", \"Dizi\": \"[4],[4],4,4,5,6\"},\n" +
						"{\"AkorAdi\": \"F#11\", \"Dizi\": \"x,9,8,9,7,7\"},\n" +
						"{\"AkorAdi\": \"F#11\", \"Dizi\": \"x,9,9,9,11,9\"},\n" +
						"{\"AkorAdi\": \"F#11\", \"Dizi\": \"x,13,11,11,12,12\"},\n" +
						"{\"AkorAdi\": \"F#+11\", \"Dizi\": \"2,1,2,1,1,2\"},\n" +
						"{\"AkorAdi\": \"F#+11\", \"Dizi\": \"4,4,4,5,5,6\"},\n" +
						"{\"AkorAdi\": \"F#+11\", \"Dizi\": \"x,9,8,9,9,8\"},\n" +
						"{\"AkorAdi\": \"F#+11\", \"Dizi\": \"x,11,10,11,11,12\"},\n" +
						"{\"AkorAdi\": \"F#13\", \"Dizi\": \"2,4,2,3,4,2\"},\n" +
						"{\"AkorAdi\": \"F#13\", \"Dizi\": \"2,2,2,3,4,4\"},\n" +
						"{\"AkorAdi\": \"F#13\", \"Dizi\": \"x,7,8,8,7,x\"},\n" +
						"{\"AkorAdi\": \"F#13\", \"Dizi\": \"[9],9,11,9,11,11\"},\n" +
						"{\"AkorAdi\": \"F#M7\", \"Dizi\": \"2,4,3,3,2,2\"},\n" +
						"{\"AkorAdi\": \"F#M7\", \"Dizi\": \"x,[4],4,6,6,6\"},\n" +
						"{\"AkorAdi\": \"F#M7\", \"Dizi\": \"6,8,8,6,7,6\"},\n" +
						"{\"AkorAdi\": \"F#M7\", \"Dizi\": \"[9],9,11,10,11,9\"},\n" +
						"{\"AkorAdi\": \"F#M7-5\", \"Dizi\": \"2,3,3,3,x,x\"},\n" +
						"{\"AkorAdi\": \"F#M7-5\", \"Dizi\": \"x,x,4,5,6,6\"},\n" +
						"{\"AkorAdi\": \"F#M7-5\", \"Dizi\": \"x,9,10,10,11,x\"},\n" +
						"{\"AkorAdi\": \"F#M7-5\", \"Dizi\": \"x,x,10,11,11,13\"},\n" +
						"{\"AkorAdi\": \"F#M7+5\", \"Dizi\": \"x,5,4,3,6,6\"},\n" +
						"{\"AkorAdi\": \"F#M7+5\", \"Dizi\": \"[6],9,8,7,6,6\"},\n" +
						"{\"AkorAdi\": \"F#M7+5\", \"Dizi\": \"x,9,12,10,11,x\"},\n" +
						"{\"AkorAdi\": \"F#M7+5\", \"Dizi\": \"x,x,12,11,11,13\"},\n" +
						"{\"AkorAdi\": \"F#M9\", \"Dizi\": \"[1],1,3,1,2,2\"},\n" +
						"{\"AkorAdi\": \"F#M9\", \"Dizi\": \"x,x,3,3,2,4\"},\n" +
						"{\"AkorAdi\": \"F#M9\", \"Dizi\": \"[9],9,8,10,9,x\"},\n" +
						"{\"AkorAdi\": \"F#M9\", \"Dizi\": \"x,11,11,13,11,13\"},\n" +
						"{\"AkorAdi\": \"F#M11\", \"Dizi\": \"2,2,3,3,2,4\"},\n" +
						"{\"AkorAdi\": \"F#M11\", \"Dizi\": \"x,x,8,10,9,7\"},\n" +
						"{\"AkorAdi\": \"F#M11\", \"Dizi\": \"[9],9,9,10,9,9\"},\n" +
						"{\"AkorAdi\": \"F#M11\", \"Dizi\": \"x,11,11,13,12,13\"},\n" +
						"{\"AkorAdi\": \"F#M13\", \"Dizi\": \"2,1,1,1,2,1\"},\n" +
						"{\"AkorAdi\": \"F#M13\", \"Dizi\": \"x,6,4,4,6,6\"},\n" +
						"{\"AkorAdi\": \"F#M13\", \"Dizi\": \"x,9,8,8,6,[6]\"},\n" +
						"{\"AkorAdi\": \"F#M13\", \"Dizi\": \"x,9,[9],10,11,11\"},\n" +
						"{\"AkorAdi\": \"F#m6\", \"Dizi\": \"x,x,1,2,2,2\"},\n" +
						"{\"AkorAdi\": \"F#m6\", \"Dizi\": \"x,[4],4,6,4,5\"},\n" +
						"{\"AkorAdi\": \"F#m6\", \"Dizi\": \"x,9,7,8,7,9\"},\n" +
						"{\"AkorAdi\": \"F#m6\", \"Dizi\": \"x,x,11,11,10,11\"},\n" +
						"{\"AkorAdi\": \"F#m69\", \"Dizi\": \"x,0,1,1,2,2\"},\n" +
						"{\"AkorAdi\": \"F#m69\", \"Dizi\": \"x,4,4,2,4,4\"},\n" +
						"{\"AkorAdi\": \"F#m69\", \"Dizi\": \"5,4,4,6,4,4\"},\n" +
						"{\"AkorAdi\": \"F#m69\", \"Dizi\": \"x,9,7,8,9,9\"},\n" +
						"{\"AkorAdi\": \"F#m7\", \"Dizi\": \"2,4,2,2,2,2\"},\n" +
						"{\"AkorAdi\": \"F#m7\", \"Dizi\": \"x,4,4,6,5,5\"},\n" +
						"{\"AkorAdi\": \"F#m7\", \"Dizi\": \"[9],9,11,9,10,9\"},\n" +
						"{\"AkorAdi\": \"F#m7\", \"Dizi\": \"x,x,11,11,10,12\"},\n" +
						"{\"AkorAdi\": \"F#m7-5\", \"Dizi\": \"2,0,2,2,1,0\"},\n" +
						"{\"AkorAdi\": \"F#m7-5\", \"Dizi\": \"x,x,4,5,5,5\"},\n" +
						"{\"AkorAdi\": \"F#m7-5\", \"Dizi\": \"x,9,10,9,10,x\"},\n" +
						"{\"AkorAdi\": \"F#m7-5\", \"Dizi\": \"x,x,10,11,10,12\"},\n" +
						"{\"AkorAdi\": \"F#m9\", \"Dizi\": \"2,4,2,2,2,4\"},\n" +
						"{\"AkorAdi\": \"F#m9\", \"Dizi\": \"x,7,6,6,7,5\"},\n" +
						"{\"AkorAdi\": \"F#m9\", \"Dizi\": \"x,9,7,9,9,9\"},\n" +
						"{\"AkorAdi\": \"F#m9\", \"Dizi\": \"x,11,11,11,10,12\"},\n" +
						"{\"AkorAdi\": \"F#m11\", \"Dizi\": \"2,0,2,1,0,0\"},\n" +
						"{\"AkorAdi\": \"F#m11\", \"Dizi\": \"2,2,2,2,2,4\"},\n" +
						"{\"AkorAdi\": \"F#m11\", \"Dizi\": \"5,4,4,4,5,4\"},\n" +
						"{\"AkorAdi\": \"F#m11\", \"Dizi\": \"x,9,7,9,9,7\"},\n" +
						"{\"AkorAdi\": \"F#mM7\", \"Dizi\": \"2,4,3,2,2,2\"},\n" +
						"{\"AkorAdi\": \"F#mM7\", \"Dizi\": \"x,4,4,6,6,5\"},\n" +
						"{\"AkorAdi\": \"F#mM7\", \"Dizi\": \"x,8,7,6,7,x\"},\n" +
						"{\"AkorAdi\": \"F#mM7\", \"Dizi\": \"9,9,11,10,10,9\"},\n" +
						"{\"AkorAdi\": \"F#mM7-5\", \"Dizi\": \"2,3,3,2,x,2\"},\n" +
						"{\"AkorAdi\": \"F#mM7-5\", \"Dizi\": \"x,x,4,5,6,5\"},\n" +
						"{\"AkorAdi\": \"F#mM7-5\", \"Dizi\": \"[8],9,10,10,10,x\"},\n" +
						"{\"AkorAdi\": \"F#mM7-5\", \"Dizi\": \"x,9,10,10,10,x\"},\n" +
						"{\"AkorAdi\": \"F#mM9\", \"Dizi\": \"x,0,x,1,2,1\"},\n" +
						"{\"AkorAdi\": \"F#mM9\", \"Dizi\": \"2,4,3,2,2,4\"},\n" +
						"{\"AkorAdi\": \"F#mM9\", \"Dizi\": \"4,4,4,6,6,5\"},\n" +
						"{\"AkorAdi\": \"F#mM9\", \"Dizi\": \"9,12,11,10,9,9\"},\n" +
						"{\"AkorAdi\": \"F#mM11\", \"Dizi\": \"2,2,3,2,2,4\"},\n" +
						"{\"AkorAdi\": \"F#mM11\", \"Dizi\": \"4,4,4,4,6,5\"},\n" +
						"{\"AkorAdi\": \"F#mM11\", \"Dizi\": \"x,9,7,10,9,7\"},\n" +
						"{\"AkorAdi\": \"F#mM11\", \"Dizi\": \"9,11,9,10,10,9\"},\n" +
						"{\"AkorAdi\": \"F#add9\", \"Dizi\": \"x,x,4,3,2,4\"},\n" +
						"{\"AkorAdi\": \"F#add9\", \"Dizi\": \"x,9,8,6,9,6\"},\n" +
						"{\"AkorAdi\": \"F#add9\", \"Dizi\": \"x,9,11,11,9,9\"},\n" +
						"{\"AkorAdi\": \"F#madd9\", \"Dizi\": \"x,x,4,2,2,4\"},\n" +
						"{\"AkorAdi\": \"F#madd9\", \"Dizi\": \"x,x,7,6,7,4\"},\n" +
						"{\"AkorAdi\": \"F#madd9\", \"Dizi\": \"x,9,7,6,9,x\"},\n" +
						"{\"AkorAdi\": \"F#madd9\", \"Dizi\": \"x,12,11,11,9,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 10:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"G\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"G\", \"Dizi\": \"3,2,0,0,0,3\"},\n" +
						"{\"AkorAdi\": \"G\", \"Dizi\": \"3,5,5,4,3,3\"},\n" +
						"{\"AkorAdi\": \"G\", \"Dizi\": \"x,5,5,7,8,7\"},\n" +
						"{\"AkorAdi\": \"G\", \"Dizi\": \"[7],10,9,7,8,7\"},\n" +
						"{\"AkorAdi\": \"Gm\", \"Dizi\": \"3,x,0,3,3,3\"},\n" +
						"{\"AkorAdi\": \"Gm\", \"Dizi\": \"3,5,5,3,3,3\"},\n" +
						"{\"AkorAdi\": \"Gm\", \"Dizi\": \"x,x,8,7,8,6\"},\n" +
						"{\"AkorAdi\": \"Gm\", \"Dizi\": \"[10],10,12,12,11,10\"},\n" +
						"{\"AkorAdi\": \"Gdim\", \"Dizi\": \"x,1,2,0,2,x\"},\n" +
						"{\"AkorAdi\": \"Gdim\", \"Dizi\": \"x,x,2,3,2,3\"},\n" +
						"{\"AkorAdi\": \"Gdim\", \"Dizi\": \"3,4,5,3,5,3\"},\n" +
						"{\"AkorAdi\": \"Gdim\", \"Dizi\": \"6,7,8,6,8,6\"},\n" +
						"{\"AkorAdi\": \"Gsus4\", \"Dizi\": \"x,x,0,0,1,3\"},\n" +
						"{\"AkorAdi\": \"Gsus4\", \"Dizi\": \"3,3,5,5,3,3\"},\n" +
						"{\"AkorAdi\": \"Gsus4\", \"Dizi\": \"x,5,5,7,8,8\"},\n" +
						"{\"AkorAdi\": \"Gsus4\", \"Dizi\": \"[10],10,12,12,13,10\"},\n" +
						"{\"AkorAdi\": \"G7sus4\", \"Dizi\": \"x,3,0,0,3,1\"},\n" +
						"{\"AkorAdi\": \"G7sus4\", \"Dizi\": \"3,5,3,5,3,3\"},\n" +
						"{\"AkorAdi\": \"G7sus4\", \"Dizi\": \"x,10,10,10,8,8\"},\n" +
						"{\"AkorAdi\": \"G7sus4\", \"Dizi\": \"[10],10,12,10,13,10\"},\n" +
						"{\"AkorAdi\": \"G-5\", \"Dizi\": \"x,4,5,4,2,x\"},\n" +
						"{\"AkorAdi\": \"G-5\", \"Dizi\": \"x,x,9,6,8,7\"},\n" +
						"{\"AkorAdi\": \"G-5\", \"Dizi\": \"9,10,9,12,[12],x\"},\n" +
						"{\"AkorAdi\": \"G-5\", \"Dizi\": \"x,10,11,12,12,x\"},\n" +
						"{\"AkorAdi\": \"G+5\", \"Dizi\": \"3,2,1,0,0,3\"},\n" +
						"{\"AkorAdi\": \"G+5\", \"Dizi\": \"x,x,5,4,4,3\"},\n" +
						"{\"AkorAdi\": \"G+5\", \"Dizi\": \"x,6,5,4,4,x\"},\n" +
						"{\"AkorAdi\": \"G+5\", \"Dizi\": \"x,x,9,8,8,7\"},\n" +
						"{\"AkorAdi\": \"G6\", \"Dizi\": \"3,2,0,0,0,0\"},\n" +
						"{\"AkorAdi\": \"G6\", \"Dizi\": \"x,5,5,4,5,x\"},\n" +
						"{\"AkorAdi\": \"G6\", \"Dizi\": \"x,5,5,7,5,7\"},\n" +
						"{\"AkorAdi\": \"G6\", \"Dizi\": \"7,7,9,7,8,7\"},\n" +
						"{\"AkorAdi\": \"G69\", \"Dizi\": \"3,2,2,2,3,3\"},\n" +
						"{\"AkorAdi\": \"G69\", \"Dizi\": \"x,5,5,4,5,5\"},\n" +
						"{\"AkorAdi\": \"G69\", \"Dizi\": \"x,10,9,9,10,10\"},\n" +
						"{\"AkorAdi\": \"G7\", \"Dizi\": \"3,2,0,0,0,1\"},\n" +
						"{\"AkorAdi\": \"G7\", \"Dizi\": \"3,5,3,4,3,3\"},\n" +
						"{\"AkorAdi\": \"G7\", \"Dizi\": \"x,5,5,7,6,7\"},\n" +
						"{\"AkorAdi\": \"G7\", \"Dizi\": \"[10],10,12,10,12,10\"},\n" +
						"{\"AkorAdi\": \"G7-5\", \"Dizi\": \"x,2,3,4,2,3\"},\n" +
						"{\"AkorAdi\": \"G7-5\", \"Dizi\": \"x,x,5,6,6,7\"},\n" +
						"{\"AkorAdi\": \"G7-5\", \"Dizi\": \"x,8,9,10,8,9\"},\n" +
						"{\"AkorAdi\": \"G7-5\", \"Dizi\": \"x,10,11,10,12,13\"},\n" +
						"{\"AkorAdi\": \"G7+5\", \"Dizi\": \"x,x,1,0,0,1\"},\n" +
						"{\"AkorAdi\": \"G7+5\", \"Dizi\": \"x,x,3,4,4,3\"},\n" +
						"{\"AkorAdi\": \"G7+5\", \"Dizi\": \"x,x,5,8,6,7\"},\n" +
						"{\"AkorAdi\": \"G7+5\", \"Dizi\": \"x,x,9,10,8,11\"},\n" +
						"{\"AkorAdi\": \"G9\", \"Dizi\": \"x,0,0,0,0,1\"},\n" +
						"{\"AkorAdi\": \"G9\", \"Dizi\": \"3,5,3,4,3,5\"},\n" +
						"{\"AkorAdi\": \"G9\", \"Dizi\": \"5,5,5,7,6,7\"},\n" +
						"{\"AkorAdi\": \"G9\", \"Dizi\": \"[10],10,9,10,10,10\"},\n" +
						"{\"AkorAdi\": \"G9-5\", \"Dizi\": \"3,2,3,2,2,3\"},\n" +
						"{\"AkorAdi\": \"G9-5\", \"Dizi\": \"x,4,5,4,6,5\"},\n" +
						"{\"AkorAdi\": \"G9-5\", \"Dizi\": \"x,x,7,6,6,7\"},\n" +
						"{\"AkorAdi\": \"G9-5\", \"Dizi\": \"x,10,9,10,10,9\"},\n" +
						"{\"AkorAdi\": \"G9+5\", \"Dizi\": \"1,0,1,0,0,1\"},\n" +
						"{\"AkorAdi\": \"G9+5\", \"Dizi\": \"x,2,3,2,4,3\"},\n" +
						"{\"AkorAdi\": \"G9+5\", \"Dizi\": \"[7],8,7,8,8,7\"},\n" +
						"{\"AkorAdi\": \"G9+5\", \"Dizi\": \"x,10,9,10,10,11\"},\n" +
						"{\"AkorAdi\": \"G7-9\", \"Dizi\": \"3,2,0,1,3,1\"},\n" +
						"{\"AkorAdi\": \"G7-9\", \"Dizi\": \"3,5,3,4,3,4\"},\n" +
						"{\"AkorAdi\": \"G7-9\", \"Dizi\": \"4,5,5,4,6,4\"},\n" +
						"{\"AkorAdi\": \"G7-9\", \"Dizi\": \"x,10,9,10,9,10\"},\n" +
						"{\"AkorAdi\": \"G7+9\", \"Dizi\": \"3,2,0,3,0,1\"},\n" +
						"{\"AkorAdi\": \"G7+9\", \"Dizi\": \"3,5,3,4,6,6\"},\n" +
						"{\"AkorAdi\": \"G7+9\", \"Dizi\": \"x,5,5,4,6,6\"},\n" +
						"{\"AkorAdi\": \"G7+9\", \"Dizi\": \"6,8,9,7,6,6\"},\n" +
						"{\"AkorAdi\": \"G11\", \"Dizi\": \"3,2,0,0,1,1\"},\n" +
						"{\"AkorAdi\": \"G11\", \"Dizi\": \"[5],[5],5,5,6,7\"},\n" +
						"{\"AkorAdi\": \"G11\", \"Dizi\": \"x,10,9,10,8,8\"},\n" +
						"{\"AkorAdi\": \"G11\", \"Dizi\": \"x,10,10,10,12,10\"},\n" +
						"{\"AkorAdi\": \"G+11\", \"Dizi\": \"3,2,3,2,2,3\"},\n" +
						"{\"AkorAdi\": \"G+11\", \"Dizi\": \"5,5,5,6,6,7\"},\n" +
						"{\"AkorAdi\": \"G+11\", \"Dizi\": \"x,10,9,10,10,9\"},\n" +
						"{\"AkorAdi\": \"G+11\", \"Dizi\": \"x,12,11,12,12,13\"},\n" +
						"{\"AkorAdi\": \"G13\", \"Dizi\": \"3,0,3,2,0,0\"},\n" +
						"{\"AkorAdi\": \"G13\", \"Dizi\": \"3,5,3,4,5,3\"},\n" +
						"{\"AkorAdi\": \"G13\", \"Dizi\": \"3,3,3,4,5,5\"},\n" +
						"{\"AkorAdi\": \"G13\", \"Dizi\": \"x,8,9,9,8,x\"},\n" +
						"{\"AkorAdi\": \"GM7\", \"Dizi\": \"3,2,0,0,0,2\"},\n" +
						"{\"AkorAdi\": \"GM7\", \"Dizi\": \"3,5,4,4,3,3\"},\n" +
						"{\"AkorAdi\": \"GM7\", \"Dizi\": \"x,[5],5,7,7,7\"},\n" +
						"{\"AkorAdi\": \"GM7\", \"Dizi\": \"7,9,9,7,8,7\"},\n" +
						"{\"AkorAdi\": \"GM7-5\", \"Dizi\": \"3,4,4,4,x,x\"},\n" +
						"{\"AkorAdi\": \"GM7-5\", \"Dizi\": \"x,x,5,6,7,7\"},\n" +
						"{\"AkorAdi\": \"GM7-5\", \"Dizi\": \"x,10,11,11,12,x\"},\n" +
						"{\"AkorAdi\": \"GM7-5\", \"Dizi\": \"x,x,11,12,12,14\"},\n" +
						"{\"AkorAdi\": \"GM7+5\", \"Dizi\": \"x,x,1,0,0,2\"},\n" +
						"{\"AkorAdi\": \"GM7+5\", \"Dizi\": \"x,6,5,4,7,7\"},\n" +
						"{\"AkorAdi\": \"GM7+5\", \"Dizi\": \"[7],10,9,8,7,7\"},\n" +
						"{\"AkorAdi\": \"GM7+5\", \"Dizi\": \"x,10,13,11,12,x\"},\n" +
						"{\"AkorAdi\": \"GM9\", \"Dizi\": \"3,0,0,0,0,2\"},\n" +
						"{\"AkorAdi\": \"GM9\", \"Dizi\": \"x,2,4,2,3,3\"},\n" +
						"{\"AkorAdi\": \"GM9\", \"Dizi\": \"x,x,4,4,3,5\"},\n" +
						"{\"AkorAdi\": \"GM9\", \"Dizi\": \"x,10,7,7,7,7\"},\n" +
						"{\"AkorAdi\": \"GM11\", \"Dizi\": \"x,0,0,2,1,2\"},\n" +
						"{\"AkorAdi\": \"GM11\", \"Dizi\": \"3,3,4,4,3,5\"},\n" +
						"{\"AkorAdi\": \"GM11\", \"Dizi\": \"x,x,9,11,10,8\"},\n" +
						"{\"AkorAdi\": \"GM11\", \"Dizi\": \"[10],10,10,11,10,10\"},\n" +
						"{\"AkorAdi\": \"GM13\", \"Dizi\": \"3,2,2,2,3,2\"},\n" +
						"{\"AkorAdi\": \"GM13\", \"Dizi\": \"x,7,5,5,7,7\"},\n" +
						"{\"AkorAdi\": \"GM13\", \"Dizi\": \"x,10,9,9,7,7\"},\n" +
						"{\"AkorAdi\": \"GM13\", \"Dizi\": \"x,10,[10],11,12,12\"},\n" +
						"{\"AkorAdi\": \"Gm6\", \"Dizi\": \"x,x,2,3,3,3\"},\n" +
						"{\"AkorAdi\": \"Gm6\", \"Dizi\": \"x,[5],5,7,5,6\"},\n" +
						"{\"AkorAdi\": \"Gm6\", \"Dizi\": \"x,10,8,9,8,10\"},\n" +
						"{\"AkorAdi\": \"Gm6\", \"Dizi\": \"x,x,12,12,11,12\"},\n" +
						"{\"AkorAdi\": \"Gm69\", \"Dizi\": \"x,1,2,2,3,3\"},\n" +
						"{\"AkorAdi\": \"Gm69\", \"Dizi\": \"x,5,5,3,5,5\"},\n" +
						"{\"AkorAdi\": \"Gm69\", \"Dizi\": \"6,5,5,7,5,5\"},\n" +
						"{\"AkorAdi\": \"Gm69\", \"Dizi\": \"x,10,8,9,10,10\"},\n" +
						"{\"AkorAdi\": \"Gm7\", \"Dizi\": \"3,5,3,3,3,3\"},\n" +
						"{\"AkorAdi\": \"Gm7\", \"Dizi\": \"x,5,5,7,6,6\"},\n" +
						"{\"AkorAdi\": \"Gm7\", \"Dizi\": \"[10],10,12,10,11,10\"},\n" +
						"{\"AkorAdi\": \"Gm7\", \"Dizi\": \"x,x,12,12,11,13\"},\n" +
						"{\"AkorAdi\": \"Gm7-5\", \"Dizi\": \"1,1,3,3,2,3\"},\n" +
						"{\"AkorAdi\": \"Gm7-5\", \"Dizi\": \"x,x,5,6,6,6\"},\n" +
						"{\"AkorAdi\": \"Gm7-5\", \"Dizi\": \"x,10,11,10,11,x\"},\n" +
						"{\"AkorAdi\": \"Gm7-5\", \"Dizi\": \"x,x,11,12,11,13\"},\n" +
						"{\"AkorAdi\": \"Gm9\", \"Dizi\": \"x,0,3,3,3,3\"},\n" +
						"{\"AkorAdi\": \"Gm9\", \"Dizi\": \"3,5,3,3,3,5\"},\n" +
						"{\"AkorAdi\": \"Gm9\", \"Dizi\": \"x,8,7,7,8,6\"},\n" +
						"{\"AkorAdi\": \"Gm9\", \"Dizi\": \"x,10,8,10,10,10\"},\n" +
						"{\"AkorAdi\": \"Gm11\", \"Dizi\": \"3,1,3,2,1,1\"},\n" +
						"{\"AkorAdi\": \"Gm11\", \"Dizi\": \"3,3,3,3,3,5\"},\n" +
						"{\"AkorAdi\": \"Gm11\", \"Dizi\": \"6,5,5,5,6,5\"},\n" +
						"{\"AkorAdi\": \"Gm11\", \"Dizi\": \"x,10,8,10,10,8\"},\n" +
						"{\"AkorAdi\": \"GmM7\", \"Dizi\": \"x,1,0,0,3,2\"},\n" +
						"{\"AkorAdi\": \"GmM7\", \"Dizi\": \"3,5,4,3,3,3\"},\n" +
						"{\"AkorAdi\": \"GmM7\", \"Dizi\": \"3,5,5,7,7,6\"},\n" +
						"{\"AkorAdi\": \"GmM7\", \"Dizi\": \"x,9,8,7,8,x\"},\n" +
						"{\"AkorAdi\": \"GmM7-5\", \"Dizi\": \"3,4,4,3,x,3\"},\n" +
						"{\"AkorAdi\": \"GmM7-5\", \"Dizi\": \"x,x,5,6,7,6\"},\n" +
						"{\"AkorAdi\": \"GmM7-5\", \"Dizi\": \"9,10,11,11,11,x\"},\n" +
						"{\"AkorAdi\": \"GmM7-5\", \"Dizi\": \"x,10,11,11,11,x\"},\n" +
						"{\"AkorAdi\": \"GmM9\", \"Dizi\": \"x,1,0,2,3,2\"},\n" +
						"{\"AkorAdi\": \"GmM9\", \"Dizi\": \"3,5,4,3,3,5\"},\n" +
						"{\"AkorAdi\": \"GmM9\", \"Dizi\": \"5,5,5,7,7,6\"},\n" +
						"{\"AkorAdi\": \"GmM9\", \"Dizi\": \"[10],13,12,11,10,10\"},\n" +
						"{\"AkorAdi\": \"GmM11\", \"Dizi\": \"3,3,4,3,3,5\"},\n" +
						"{\"AkorAdi\": \"GmM11\", \"Dizi\": \"5,5,5,5,7,6\"},\n" +
						"{\"AkorAdi\": \"GmM11\", \"Dizi\": \"x,10,8,11,10,8\"},\n" +
						"{\"AkorAdi\": \"GmM11\", \"Dizi\": \"10,12,10,11,11,10\"},\n" +
						"{\"AkorAdi\": \"Gadd9\", \"Dizi\": \"x,x,5,4,3,5\"},\n" +
						"{\"AkorAdi\": \"Gadd9\", \"Dizi\": \"x,10,9,7,10,7\"},\n" +
						"{\"AkorAdi\": \"Gadd9\", \"Dizi\": \"x,10,12,12,10,10\"},\n" +
						"{\"AkorAdi\": \"Gmadd9\", \"Dizi\": \"x,x,5,3,3,5\"},\n" +
						"{\"AkorAdi\": \"Gmadd9\", \"Dizi\": \"x,x,8,7,8,5\"},\n" +
						"{\"AkorAdi\": \"Gmadd9\", \"Dizi\": \"x,10,8,7,10,x\"},\n" +
						"{\"AkorAdi\": \"Gmadd9\", \"Dizi\": \"x,13,12,12,10,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
			case 11:
				JSONTonAkorData = "{\n" +
						"\"AkorCetveli\": {\n" +
						"\"Tonlar\": [\n" +
						"{\"TonAdi\":\"G#\", \"Akorlari\": [\n" +
						"{\"AkorAdi\": \"G#\", \"Dizi\": \"4,3,1,1,1,4\"},\n" +
						"{\"AkorAdi\": \"G#\", \"Dizi\": \"4,6,6,5,4,4\"},\n" +
						"{\"AkorAdi\": \"G#\", \"Dizi\": \"x,6,6,8,9,8\"},\n" +
						"{\"AkorAdi\": \"G#\", \"Dizi\": \"[8],11,10,8,9,8\"},\n" +
						"{\"AkorAdi\": \"G#m\", \"Dizi\": \"x,x,1,4,4,4\"},\n" +
						"{\"AkorAdi\": \"G#m\", \"Dizi\": \"4,6,6,4,4,4\"},\n" +
						"{\"AkorAdi\": \"G#m\", \"Dizi\": \"x,x,9,8,9,7\"},\n" +
						"{\"AkorAdi\": \"G#m\", \"Dizi\": \"[11],11,13,13,12,10\"},\n" +
						"{\"AkorAdi\": \"G#dim\", \"Dizi\": \"x,x,0,1,0,1\"},\n" +
						"{\"AkorAdi\": \"G#dim\", \"Dizi\": \"1,2,3,1,3,1\"},\n" +
						"{\"AkorAdi\": \"G#dim\", \"Dizi\": \"x,x,3,4,3,4\"},\n" +
						"{\"AkorAdi\": \"G#dim\", \"Dizi\": \"4,5,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"G#sus4\", \"Dizi\": \"x,x,1,1,2,4\"},\n" +
						"{\"AkorAdi\": \"G#sus4\", \"Dizi\": \"4,4,6,6,4,4\"},\n" +
						"{\"AkorAdi\": \"G#sus4\", \"Dizi\": \"x,6,6,8,9,9\"},\n" +
						"{\"AkorAdi\": \"G#sus4\", \"Dizi\": \"[11],11,13,13,14,11\"},\n" +
						"{\"AkorAdi\": \"G#7sus4\", \"Dizi\": \"x,x,1,1,2,2\"},\n" +
						"{\"AkorAdi\": \"G#7sus4\", \"Dizi\": \"4,6,4,6,4,4\"},\n" +
						"{\"AkorAdi\": \"G#7sus4\", \"Dizi\": \"x,11,11,11,9,9\"},\n" +
						"{\"AkorAdi\": \"G#7sus4\", \"Dizi\": \"[11],11,13,11,14,11\"},\n" +
						"{\"AkorAdi\": \"G#-5\", \"Dizi\": \"x,5,6,5,3,x\"},\n" +
						"{\"AkorAdi\": \"G#-5\", \"Dizi\": \"x,x,10,7,9,8\"},\n" +
						"{\"AkorAdi\": \"G#-5\", \"Dizi\": \"10,11,10,13,[13],x\"},\n" +
						"{\"AkorAdi\": \"G#-5\", \"Dizi\": \"x,11,12,13,13,x\"},\n" +
						"{\"AkorAdi\": \"G#+5\", \"Dizi\": \"x,x,2,1,1,0\"},\n" +
						"{\"AkorAdi\": \"G#+5\", \"Dizi\": \"x,x,2,1,1,4\"},\n" +
						"{\"AkorAdi\": \"G#+5\", \"Dizi\": \"x,7,6,5,5,x\"},\n" +
						"{\"AkorAdi\": \"G#+5\", \"Dizi\": \"x,x,10,9,9,8\"},\n" +
						"{\"AkorAdi\": \"G#6\", \"Dizi\": \"x,x,1,1,1,1\"},\n" +
						"{\"AkorAdi\": \"G#6\", \"Dizi\": \"x,6,6,5,6,x\"},\n" +
						"{\"AkorAdi\": \"G#6\", \"Dizi\": \"x,6,6,8,6,8\"},\n" +
						"{\"AkorAdi\": \"G#6\", \"Dizi\": \"[8],8,10,8,9,8\"},\n" +
						"{\"AkorAdi\": \"G#69\", \"Dizi\": \"4,3,3,3,4,4\"},\n" +
						"{\"AkorAdi\": \"G#69\", \"Dizi\": \"x,6,6,5,6,6\"},\n" +
						"{\"AkorAdi\": \"G#69\", \"Dizi\": \"x,11,10,10,11,11\"},\n" +
						"{\"AkorAdi\": \"G#7\", \"Dizi\": \"x,[3],1,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#7\", \"Dizi\": \"4,6,4,5,4,4\"},\n" +
						"{\"AkorAdi\": \"G#7\", \"Dizi\": \"x,6,6,8,7,8\"},\n" +
						"{\"AkorAdi\": \"G#7\", \"Dizi\": \"[11],11,13,11,13,11\"},\n" +
						"{\"AkorAdi\": \"G#7-5\", \"Dizi\": \"x,3,4,5,3,4\"},\n" +
						"{\"AkorAdi\": \"G#7-5\", \"Dizi\": \"x,x,6,7,7,8\"},\n" +
						"{\"AkorAdi\": \"G#7-5\", \"Dizi\": \"x,9,10,11,9,10\"},\n" +
						"{\"AkorAdi\": \"G#7-5\", \"Dizi\": \"x,11,12,11,13,14\"},\n" +
						"{\"AkorAdi\": \"G#7+5\", \"Dizi\": \"x,x,2,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#7+5\", \"Dizi\": \"x,x,4,5,5,4\"},\n" +
						"{\"AkorAdi\": \"G#7+5\", \"Dizi\": \"x,x,6,9,7,8\"},\n" +
						"{\"AkorAdi\": \"G#7+5\", \"Dizi\": \"x,x,10,11,9,12\"},\n" +
						"{\"AkorAdi\": \"G#9\", \"Dizi\": \"x,1,1,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#9\", \"Dizi\": \"4,6,4,5,4,6\"},\n" +
						"{\"AkorAdi\": \"G#9\", \"Dizi\": \"6,6,6,8,7,8\"},\n" +
						"{\"AkorAdi\": \"G#9\", \"Dizi\": \"[11],11,10,11,11,11\"},\n" +
						"{\"AkorAdi\": \"G#9-5\", \"Dizi\": \"x,1,0,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#9-5\", \"Dizi\": \"4,3,4,3,3,4\"},\n" +
						"{\"AkorAdi\": \"G#9-5\", \"Dizi\": \"x,5,6,5,7,6\"},\n" +
						"{\"AkorAdi\": \"G#9-5\", \"Dizi\": \"x,x,8,7,7,8\"},\n" +
						"{\"AkorAdi\": \"G#9+5\", \"Dizi\": \"2,1,2,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#9+5\", \"Dizi\": \"x,3,4,3,5,4\"},\n" +
						"{\"AkorAdi\": \"G#9+5\", \"Dizi\": \"[8],9,8,9,9,8\"},\n" +
						"{\"AkorAdi\": \"G#9+5\", \"Dizi\": \"x,11,10,11,11,12\"},\n" +
						"{\"AkorAdi\": \"G#7-9\", \"Dizi\": \"x,0,1,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#7-9\", \"Dizi\": \"4,6,4,5,4,5\"},\n" +
						"{\"AkorAdi\": \"G#7-9\", \"Dizi\": \"5,6,6,5,7,5\"},\n" +
						"{\"AkorAdi\": \"G#7-9\", \"Dizi\": \"x,11,10,11,10,11\"},\n" +
						"{\"AkorAdi\": \"G#7+9\", \"Dizi\": \"4,6,4,5,7,7\"},\n" +
						"{\"AkorAdi\": \"G#7+9\", \"Dizi\": \"x,6,6,5,7,7\"},\n" +
						"{\"AkorAdi\": \"G#7+9\", \"Dizi\": \"7,9,10,8,7,7\"},\n" +
						"{\"AkorAdi\": \"G#7+9\", \"Dizi\": \"x,11,10,11,12,x\"},\n" +
						"{\"AkorAdi\": \"G#11\", \"Dizi\": \"x,3,1,1,2,2\"},\n" +
						"{\"AkorAdi\": \"G#11\", \"Dizi\": \"[6],[6],6,6,7,8\"},\n" +
						"{\"AkorAdi\": \"G#11\", \"Dizi\": \"x,11,10,11,9,9\"},\n" +
						"{\"AkorAdi\": \"G#11\", \"Dizi\": \"x,11,11,11,13,11\"},\n" +
						"{\"AkorAdi\": \"G#+11\", \"Dizi\": \"x,1,0,1,1,2\"},\n" +
						"{\"AkorAdi\": \"G#+11\", \"Dizi\": \"4,3,4,3,3,4\"},\n" +
						"{\"AkorAdi\": \"G#+11\", \"Dizi\": \"6,6,6,7,7,8\"},\n" +
						"{\"AkorAdi\": \"G#+11\", \"Dizi\": \"x,11,10,11,11,10\"},\n" +
						"{\"AkorAdi\": \"G#13\", \"Dizi\": \"2,1,1,1,1,1\"},\n" +
						"{\"AkorAdi\": \"G#13\", \"Dizi\": \"4,6,4,5,6,4\"},\n" +
						"{\"AkorAdi\": \"G#13\", \"Dizi\": \"4,4,4,5,6,6\"},\n" +
						"{\"AkorAdi\": \"G#13\", \"Dizi\": \"x,9,10,10,9,x\"},\n" +
						"{\"AkorAdi\": \"G#M7\", \"Dizi\": \"4,6,5,5,4,4\"},\n" +
						"{\"AkorAdi\": \"G#M7\", \"Dizi\": \"x,[6],6,8,8,8\"},\n" +
						"{\"AkorAdi\": \"G#M7\", \"Dizi\": \"8,10,10,8,9,8\"},\n" +
						"{\"AkorAdi\": \"G#M7\", \"Dizi\": \"[11],11,13,12,13,11\"},\n" +
						"{\"AkorAdi\": \"G#M7-5\", \"Dizi\": \"x,x,0,1,1,3\"},\n" +
						"{\"AkorAdi\": \"G#M7-5\", \"Dizi\": \"4,5,5,5,x,x\"},\n" +
						"{\"AkorAdi\": \"G#M7-5\", \"Dizi\": \"x,x,6,7,8,8\"},\n" +
						"{\"AkorAdi\": \"G#M7-5\", \"Dizi\": \"x,11,12,12,13,0\"},\n" +
						"{\"AkorAdi\": \"G#M7+5\", \"Dizi\": \"x,x,2,1,1,3\"},\n" +
						"{\"AkorAdi\": \"G#M7+5\", \"Dizi\": \"x,7,6,5,8,8\"},\n" +
						"{\"AkorAdi\": \"G#M7+5\", \"Dizi\": \"[8],11,10,9,8,8\"},\n" +
						"{\"AkorAdi\": \"G#M7+5\", \"Dizi\": \"x,11,14,12,13,x\"},\n" +
						"{\"AkorAdi\": \"G#M9\", \"Dizi\": \"x,1,1,1,1,3\"},\n" +
						"{\"AkorAdi\": \"G#M9\", \"Dizi\": \"x,3,5,3,4,4\"},\n" +
						"{\"AkorAdi\": \"G#M9\", \"Dizi\": \"x,x,5,5,4,6\"},\n" +
						"{\"AkorAdi\": \"G#M9\", \"Dizi\": \"[11],11,10,12,11,x\"},\n" +
						"{\"AkorAdi\": \"G#M11\", \"Dizi\": \"x,1,1,3,2,3\"},\n" +
						"{\"AkorAdi\": \"G#M11\", \"Dizi\": \"4,4,5,5,4,6\"},\n" +
						"{\"AkorAdi\": \"G#M11\", \"Dizi\": \"x,x,10,12,11,9\"},\n" +
						"{\"AkorAdi\": \"G#M11\", \"Dizi\": \"[11],11,11,12,11,11\"},\n" +
						"{\"AkorAdi\": \"G#M13\", \"Dizi\": \"4,3,3,3,4,3\"},\n" +
						"{\"AkorAdi\": \"G#M13\", \"Dizi\": \"x,8,6,6,8,8\"},\n" +
						"{\"AkorAdi\": \"G#M13\", \"Dizi\": \"x,11,10,10,8,[8]\"},\n" +
						"{\"AkorAdi\": \"G#M13\", \"Dizi\": \"x,11,[11],12,13,13\"},\n" +
						"{\"AkorAdi\": \"G#m6\", \"Dizi\": \"x,x,1,1,0,1\"},\n" +
						"{\"AkorAdi\": \"G#m6\", \"Dizi\": \"x,x,3,4,4,4\"},\n" +
						"{\"AkorAdi\": \"G#m6\", \"Dizi\": \"x,6,6,4,6,4\"},\n" +
						"{\"AkorAdi\": \"G#m6\", \"Dizi\": \"x,[6],6,8,6,7\"},\n" +
						"{\"AkorAdi\": \"G#m69\", \"Dizi\": \"x,2,3,3,4,4\"},\n" +
						"{\"AkorAdi\": \"G#m69\", \"Dizi\": \"x,6,6,4,6,6\"},\n" +
						"{\"AkorAdi\": \"G#m69\", \"Dizi\": \"7,6,6,8,6,6\"},\n" +
						"{\"AkorAdi\": \"G#m69\", \"Dizi\": \"x,11,9,10,11,11\"},\n" +
						"{\"AkorAdi\": \"G#m7\", \"Dizi\": \"x,x,1,1,0,2\"},\n" +
						"{\"AkorAdi\": \"G#m7\", \"Dizi\": \"4,6,4,4,4,4\"},\n" +
						"{\"AkorAdi\": \"G#m7\", \"Dizi\": \"x,6,6,8,7,7\"},\n" +
						"{\"AkorAdi\": \"G#m7\", \"Dizi\": \"[11],11,13,11,12,11\"},\n" +
						"{\"AkorAdi\": \"G#m7-5\", \"Dizi\": \"x,x,0,1,0,2\"},\n" +
						"{\"AkorAdi\": \"G#m7-5\", \"Dizi\": \"2,2,4,4,3,4\"},\n" +
						"{\"AkorAdi\": \"G#m7-5\", \"Dizi\": \"x,x,6,7,7,7\"},\n" +
						"{\"AkorAdi\": \"G#m7-5\", \"Dizi\": \"x,11,12,11,12,x\"},\n" +
						"{\"AkorAdi\": \"G#m9\", \"Dizi\": \"x,1,1,1,0,2\"},\n" +
						"{\"AkorAdi\": \"G#m9\", \"Dizi\": \"4,6,4,4,4,6\"},\n" +
						"{\"AkorAdi\": \"G#m9\", \"Dizi\": \"[7],9,8,8,9,7\"},\n" +
						"{\"AkorAdi\": \"G#m9\", \"Dizi\": \"x,11,9,11,11,11\"},\n" +
						"{\"AkorAdi\": \"G#m11\", \"Dizi\": \"4,2,4,3,2,2\"},\n" +
						"{\"AkorAdi\": \"G#m11\", \"Dizi\": \"4,4,4,4,4,6\"},\n" +
						"{\"AkorAdi\": \"G#m11\", \"Dizi\": \"7,6,6,6,7,6\"},\n" +
						"{\"AkorAdi\": \"G#m11\", \"Dizi\": \"x,11,9,11,11,9\"},\n" +
						"{\"AkorAdi\": \"G#mM7\", \"Dizi\": \"x,2,1,1,4,3\"},\n" +
						"{\"AkorAdi\": \"G#mM7\", \"Dizi\": \"4,6,5,4,4,4\"},\n" +
						"{\"AkorAdi\": \"G#mM7\", \"Dizi\": \"x,6,6,8,8,7\"},\n" +
						"{\"AkorAdi\": \"G#mM7\", \"Dizi\": \"x,10,9,8,9,x\"},\n" +
						"{\"AkorAdi\": \"G#mM7-5\", \"Dizi\": \"4,5,5,4,x,4\"},\n" +
						"{\"AkorAdi\": \"G#mM7-5\", \"Dizi\": \"x,x,6,7,8,7\"},\n" +
						"{\"AkorAdi\": \"G#mM7-5\", \"Dizi\": \"[10],11,12,12,12,x\"},\n" +
						"{\"AkorAdi\": \"G#mM7-5\", \"Dizi\": \"x,11,12,12,12,x\"},\n" +
						"{\"AkorAdi\": \"G#mM9\", \"Dizi\": \"x,2,x,3,4,3\"},\n" +
						"{\"AkorAdi\": \"G#mM9\", \"Dizi\": \"4,6,5,4,4,6\"},\n" +
						"{\"AkorAdi\": \"G#mM9\", \"Dizi\": \"6,6,6,8,8,7\"},\n" +
						"{\"AkorAdi\": \"G#mM9\", \"Dizi\": \"11,14,13,12,11,11\"},\n" +
						"{\"AkorAdi\": \"G#mM11\", \"Dizi\": \"4,4,5,4,4,6\"},\n" +
						"{\"AkorAdi\": \"G#mM11\", \"Dizi\": \"6,6,6,6,8,7\"},\n" +
						"{\"AkorAdi\": \"G#mM11\", \"Dizi\": \"x,11,9,12,11,9\"},\n" +
						"{\"AkorAdi\": \"G#mM11\", \"Dizi\": \"11,13,11,12,12,11\"},\n" +
						"{\"AkorAdi\": \"G#add9\", \"Dizi\": \"x,x,6,5,4,6\"},\n" +
						"{\"AkorAdi\": \"G#add9\", \"Dizi\": \"x,11,10,8,11,8\"},\n" +
						"{\"AkorAdi\": \"G#add9\", \"Dizi\": \"x,11,13,13,11,11\"},\n" +
						"{\"AkorAdi\": \"G#madd9\", \"Dizi\": \"x,x,6,4,4,6\"},\n" +
						"{\"AkorAdi\": \"G#madd9\", \"Dizi\": \"x,x,9,8,9,6\"},\n" +
						"{\"AkorAdi\": \"G#madd9\", \"Dizi\": \"x,11,9,8,11,x\"},\n" +
						"{\"AkorAdi\": \"G#madd9\", \"Dizi\": \"x,14,13,13,11,x\"}\n" +
						"]}\n" +
						"]\n" +
						"}\n" +
						"}";
				break;
		}

		return JSONTonAkorData;
	}

	private Bitmap GitarUzerindeAkorGetir(Activity activity, AlertDialog ADDialogAkorGosterici, Typeface YaziFontu, String AkorDizi) {
		/** Gitar Klavye Grafik Değişkenleri **/
		int EkranWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
		//int EkranHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
		int DialogWidth = ADDialogAkorGosterici.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		int CetvelPadding = 100;
		int BaslangicX, BaslangicY, BitisX, BitisY;

		int BaslangicPerdesiKalinligi = 40;

		int ToplamFredSayisi = 20;
		int FredlerArasiMesafe = 175;
		int FredKalinligi = 20;

		int ToplamTelSayisi = 6;
		int TellerArasiMesafe;
		int[] TelKalinligi = {25,23,20,16,14,12};

		int VidaBoyutu = 35;
		int NotaBoyutu = 50;
		int NotaBorderKalinligi = 10;
		int[] VidaKoordinatlari = new int[18];
		int[] NotaKoordinatlari = new int[12];

		String[] DiziParcasi = AkorDizi.split(",");

		Bitmap bitmap = Bitmap.createBitmap(EkranWidth, ToplamFredSayisi * FredlerArasiMesafe, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		Paint PaintBaslangicPerdesi = new Paint();
		PaintBaslangicPerdesi.setColor(activity.getResources().getColor(R.color.GitarKlavyeBaslangicPerdesiRengi));
		PaintBaslangicPerdesi.setStyle(Paint.Style.FILL);
		PaintBaslangicPerdesi.setStrokeWidth(BaslangicPerdesiKalinligi);

		Paint PaintPerde = new Paint();
		PaintPerde.setColor(activity.getResources().getColor(R.color.GitarKlavyePerdeRengi));
		PaintPerde.setStyle(Paint.Style.FILL);

		Paint PaintFred = new Paint();
		PaintFred.setColor(activity.getResources().getColor(R.color.GitarKlavyeFredRengi));
		PaintFred.setStyle(Paint.Style.FILL);
		PaintFred.setStrokeWidth(FredKalinligi);

		Paint PaintVida = new Paint();
		PaintVida.setColor(activity.getResources().getColor(R.color.GitarKlavyeVidaRengi));
		PaintVida.setStyle(Paint.Style.FILL);

		Paint PaintTel = new Paint();
		PaintTel.setColor(activity.getResources().getColor(R.color.GitarKlavyeTelRengi));
		PaintTel.setStyle(Paint.Style.FILL);

		Paint PaintNota = new Paint();
		PaintNota.setColor(activity.getResources().getColor(R.color.GitarKlavyeNotaRengi));
		PaintNota.setStyle(Paint.Style.FILL);

		Paint PaintNotaBorder = new Paint();
		PaintNotaBorder.setColor(activity.getResources().getColor(R.color.GitarKlavyeNotaBorderRengi));
		PaintNotaBorder.setStyle(Paint.Style.STROKE);
		PaintNotaBorder.setStrokeWidth(NotaBorderKalinligi);

		Paint PaintXNota = new Paint();
		PaintXNota.setColor(activity.getResources().getColor(R.color.GitarKlavyeXNotaRengi));
		PaintXNota.setTextSize(150);
		PaintXNota.setTypeface(Typeface.create(YaziFontu, Typeface.BOLD));

		BaslangicX = CetvelPadding;
		BaslangicY = CetvelPadding;
		BitisX = DialogWidth - BaslangicX;
		BitisY = CetvelPadding;

		for(int i = 0; i < ToplamFredSayisi; i++) {
			/** Perdeleri çiziyoruz. Y parameresi */
			if(i == 0) {
				canvas.drawLine(BaslangicX - 60, BaslangicY, BitisX + 60, BitisY, PaintBaslangicPerdesi);
				canvas.drawRect(BaslangicX - 60, BaslangicY + (BaslangicPerdesiKalinligi / 2), BitisX + 60, BaslangicY + (FredlerArasiMesafe - (FredKalinligi / 2)), PaintPerde);
			} else {
				canvas.drawLine(BaslangicX - 60, BaslangicY, BitisX + 60, BitisY, PaintFred);
				canvas.drawRect(BaslangicX - 60, BaslangicY + (FredKalinligi / 2), BitisX + 60, BaslangicY + (FredlerArasiMesafe - (FredKalinligi / 2)), PaintPerde);
			}

			/** Vidaları çiziyoruz. Y parameresi */
			if(i == 2) VidaKoordinatlari[1] = BaslangicY + (FredlerArasiMesafe / 2);
			if(i == 4) VidaKoordinatlari[3] = BaslangicY + (FredlerArasiMesafe / 2);
			if(i == 6) VidaKoordinatlari[5] = BaslangicY + (FredlerArasiMesafe / 2);
			if(i == 8) VidaKoordinatlari[7] = BaslangicY + (FredlerArasiMesafe / 2);
			if(i == 11) {
				VidaKoordinatlari[9] = BaslangicY + (FredlerArasiMesafe / 2);
				VidaKoordinatlari[11] = BaslangicY + (FredlerArasiMesafe / 2);
			}
			if(i == 14) VidaKoordinatlari[13] = BaslangicY + (FredlerArasiMesafe / 2);
			if(i == 16) VidaKoordinatlari[15] = BaslangicY + (FredlerArasiMesafe / 2);
			if(i == 18) VidaKoordinatlari[17] = BaslangicY + (FredlerArasiMesafe / 2);

			/** Akordaki notaları yerlerine çiziyoruz. Y parameresi */
			if(!AkorDizi.equals("0,0,0,0,0,0")) {
				//if(DiziParcasi[0].equals(String.valueOf(i + 1))) NotaKoordinatlari[1] = BaslangicY + (FredlerArasiMesafe / 2);

				if(isNumeric(DiziParcasi[0]) && DiziParcasi[0].equals(String.valueOf(i + 1))) NotaKoordinatlari[1] = BaslangicY + (FredlerArasiMesafe / 2);
				else if(DiziParcasi[0].contains("[") && DiziParcasi[0].contains("]")) {
					if(Integer.parseInt(DiziParcasi[0].substring(1, DiziParcasi[0].length() - 1)) == i + 1) NotaKoordinatlari[1] = BaslangicY + (FredlerArasiMesafe / 2);
				}

				if(isNumeric(DiziParcasi[1]) && DiziParcasi[1].equals(String.valueOf(i + 1))) NotaKoordinatlari[3] = BaslangicY + (FredlerArasiMesafe / 2);
				else if(DiziParcasi[1].contains("[") && DiziParcasi[1].contains("]")) {
					if(Integer.parseInt(DiziParcasi[1].substring(1, DiziParcasi[1].length() - 1)) == i + 1) NotaKoordinatlari[3] = BaslangicY + (FredlerArasiMesafe / 2);
				}

				if(isNumeric(DiziParcasi[2]) && DiziParcasi[2].equals(String.valueOf(i + 1))) NotaKoordinatlari[5] = BaslangicY + (FredlerArasiMesafe / 2);
				else if(DiziParcasi[2].contains("[") && DiziParcasi[2].contains("]")) {
					if(Integer.parseInt(DiziParcasi[2].substring(1, DiziParcasi[2].length() - 1)) == i + 1) NotaKoordinatlari[5] = BaslangicY + (FredlerArasiMesafe / 2);
				}

				if(isNumeric(DiziParcasi[3]) && DiziParcasi[3].equals(String.valueOf(i + 1))) NotaKoordinatlari[7] = BaslangicY + (FredlerArasiMesafe / 2);
				else if(DiziParcasi[3].contains("[") && DiziParcasi[3].contains("]")) {
					if(Integer.parseInt(DiziParcasi[3].substring(1, DiziParcasi[3].length() - 1)) == i + 1) NotaKoordinatlari[7] = BaslangicY + (FredlerArasiMesafe / 2);
				}

				if(isNumeric(DiziParcasi[4]) && DiziParcasi[4].equals(String.valueOf(i + 1))) NotaKoordinatlari[9] = BaslangicY + (FredlerArasiMesafe / 2);
				else if(DiziParcasi[4].contains("[") && DiziParcasi[4].contains("]")) {
					if(Integer.parseInt(DiziParcasi[4].substring(1, DiziParcasi[4].length() - 1)) == i + 1) NotaKoordinatlari[9] = BaslangicY + (FredlerArasiMesafe / 2);
				}

				if(isNumeric(DiziParcasi[5]) && DiziParcasi[5].equals(String.valueOf(i + 1))) NotaKoordinatlari[11] = BaslangicY + (FredlerArasiMesafe / 2);
				else if(DiziParcasi[5].contains("[") && DiziParcasi[5].contains("]")) {
					if(Integer.parseInt(DiziParcasi[5].substring(1, DiziParcasi[5].length() - 1)) == i + 1) NotaKoordinatlari[11] = BaslangicY + (FredlerArasiMesafe / 2);
				}
			}

			BaslangicY = BaslangicY + FredlerArasiMesafe;
			BitisY = BaslangicY;
		}

		BaslangicX = CetvelPadding;
		BaslangicY = CetvelPadding;
		BitisX = CetvelPadding;
		BitisY = FredlerArasiMesafe * ToplamFredSayisi;
		TellerArasiMesafe = ((DialogWidth - (BaslangicX * 2)) / (ToplamTelSayisi - 1));

		for(int i = 0; i < ToplamTelSayisi; i++) {
			PaintTel.setStrokeWidth(TelKalinligi[i]);
			canvas.drawLine(BaslangicX, BaslangicY, BitisX, BitisY, PaintTel);

			NotaKoordinatlari[(i * 2)] = BaslangicX;

			/*if(DiziParcasi[0].equals("0")) NotaKoordinatlari[1] = BaslangicY / 2;
			else if(DiziParcasi[0].equals("x")) NotaKoordinatlari[1] = BaslangicY;*/

			if(DiziParcasi[i].equals("x")) NotaKoordinatlari[(i * 2) + 1] = BaslangicY;

			if(i == 1) VidaKoordinatlari[8] = BaslangicX + (TellerArasiMesafe / 2);

			if(i == 2) {
				VidaKoordinatlari[0] = BaslangicX + (TellerArasiMesafe / 2);
				VidaKoordinatlari[2] = BaslangicX + (TellerArasiMesafe / 2);
				VidaKoordinatlari[4] = BaslangicX + (TellerArasiMesafe / 2);
				VidaKoordinatlari[6] = BaslangicX + (TellerArasiMesafe / 2);
				VidaKoordinatlari[12] = BaslangicX + (TellerArasiMesafe / 2);
				VidaKoordinatlari[14] = BaslangicX + (TellerArasiMesafe / 2);
				VidaKoordinatlari[16] = BaslangicX + (TellerArasiMesafe / 2);
			}

			if(i == 3) VidaKoordinatlari[10] = BaslangicX + (TellerArasiMesafe / 2);

			BaslangicX = BaslangicX + TellerArasiMesafe;
			BitisX = BaslangicX;
		}

		for(int i = 0; i < VidaKoordinatlari.length / 2; i++) {
			canvas.drawCircle(VidaKoordinatlari[i*2], VidaKoordinatlari[(i*2) + 1], VidaBoyutu, PaintVida);
		}

		for(int i = 0; i < NotaKoordinatlari.length / 2; i++) {
			if(NotaKoordinatlari[i*2] != 0 && NotaKoordinatlari[(i*2) + 1] != 0) {
				if(DiziParcasi[i].equals("x"))
					canvas.drawText("x", NotaKoordinatlari[i*2] - 40, NotaKoordinatlari[(i*2) + 1] + 45, PaintXNota);
				else {
					if(DiziParcasi[i].contains("[") && DiziParcasi[i].contains("]")) {
						PaintNota.setColor(activity.getResources().getColor(R.color.GitarKlavyeAlternatifNotaRengi));
						PaintNotaBorder.setColor(activity.getResources().getColor(R.color.GitarKlavyeAlternatifNotaBorderRengi));
					} else {
						PaintNota.setColor(activity.getResources().getColor(R.color.GitarKlavyeNotaRengi));
						PaintNotaBorder.setColor(activity.getResources().getColor(R.color.GitarKlavyeNotaBorderRengi));
					}

					canvas.drawCircle(NotaKoordinatlari[i*2], NotaKoordinatlari[(i*2) + 1], NotaBoyutu, PaintNota);
					canvas.drawCircle(NotaKoordinatlari[i*2], NotaKoordinatlari[(i*2) + 1], NotaBoyutu, PaintNotaBorder);

					/*BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					Bitmap background = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_anasayfa_icon, options);
					canvas.drawBitmap(background, NotaKoordinatlari[i*2] - 50, NotaKoordinatlari[(i*2) + 1] - 50, null);*/
				}
			}
		}

		return bitmap;
	}

	public String AndroidSurumBilgisi(int APILevel) {
		String AndroidVersiyon;

		switch (APILevel) {
			case 16:
				AndroidVersiyon = "Android 4.1 (Jelly Bean)";
				break;
			case 17:
				AndroidVersiyon = "Android 4.2 (Jelly Bean)";
				break;
			case 18:
				AndroidVersiyon = "Android 4.3 (Jelly Bean)";
				break;
			case 19:
				AndroidVersiyon = "Android 4.4 (KitKat)";
				break;
			case 20:
				AndroidVersiyon = "Android 4.4 (KitKat Wear)";
				break;
			case 21:
				AndroidVersiyon = "Android 5.0 (Lollipop)";
				break;
			case 22:
				AndroidVersiyon = "Android 5.1 (Lollipop)";
				break;
			case 23:
				AndroidVersiyon = "Android 6.0 (Marshmallow)";
				break;
			case 24:
				AndroidVersiyon = "Android 7.0 (Nougat)";
				break;
			case 25:
				AndroidVersiyon = "Android 7.1 (Nougat)";
				break;
			default:
				AndroidVersiyon = String.valueOf(Build.VERSION.SDK_INT);
				break;
		}

		return AndroidVersiyon;
	}

	public HttpClient getHttpsClient(HttpClient client) {
		try{
			X509TrustManager x509TrustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
			SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager clientConnectionManager = client.getConnectionManager();
			SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
			schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
			return new DefaultHttpClient(clientConnectionManager, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}

	public void TransparanNotifyBar() {
		if (Build.VERSION.SDK_INT >= 21) {
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
	}

	public int dpToPx(int dp) {
		Resources r = activity.getResources();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
	}
}