package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelerSPN;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@SuppressWarnings("ALL")
public class Frg_Sarki extends Fragment implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	Int_DataConn_AnaEkran FragmentDataConn;
	Typeface YaziFontu;
	InputMethodManager imm;
	ProgressDialog PDIslem;
	public AlertDialog ADDialog;
	SharedPreferences sharedPref;
	Intent mIntent;

	List<SnfListeler> SnfListeler;
	List<SnfKategoriler> SnfKategoriler;
	List<SnfTarzlar> SnfTarzlar;

	AdpListelerSPN AdpListelerSPN;
	AdpKategori AdpKategoriler;
	AdpTarz AdpTarzlar;

	CoordinatorLayout coordinatorLayout_FrgSarki;
	LinearLayout LLSayfa;
	FloatingActionMenu FABMenu;
	FloatingActionButton FABSarkiEkleCikar, FABSarkiDuzenle, FABTranspozeArti, FABTranspozeEksi, FABPaylas, FABAkorCetveli, FABVideoKlip, FABSorunBildir, FABAkorGizleGoster;
	WebView webview;

	int firstVisibleItem, mPreviousVisibleItem, SecilenSarkiID, SecilenListeID, TranspozeNo;
	String SecilenSarkiAdi, SecilenSanatciAdi, SecilenSarkiIcerik, SecilenSarkiIcerikTemp, SecilenSarkiVideoURL;
	String HTMLBaslangic = "<html><head><style>html, body {top:0; bottom:0; left:0; right:0;}</style></head><body>";
	String HTMLBitis = "</body></html>";

	final static float STEP = 200;
	float mRatio = 1.0f;
	int mBaseDist;
	float mBaseRatio;
	Boolean AkorGizliMi = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_frg_sarki, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		veritabani = new Veritabani(activity);
		FragmentDataConn = (Int_DataConn_AnaEkran) activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, activity.MODE_PRIVATE);

		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		//CSVIcerikScroll = (CustomScrollView) activity.findViewById(R.id.CSVIcerikScroll);
			/*CSVIcerikScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
				@Override
				public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
					if (scrollY > firstVisibleItem) {
						firstVisibleItem = scrollY / 150;
					}

					if (firstVisibleItem > mPreviousVisibleItem) {
						FABMenu.hideMenuButton(true);
					} else if (firstVisibleItem < mPreviousVisibleItem) {
						FABMenu.showMenuButton(true);
					}
					mPreviousVisibleItem = firstVisibleItem;
				}
			});*/

		coordinatorLayout_FrgSarki = (CoordinatorLayout) activity.findViewById(R.id.coordinatorLayout_FrgSarki);

		LLSayfa = (LinearLayout) activity.findViewById(R.id.LLSayfa);

		FABMenu = (FloatingActionMenu) activity.findViewById(R.id.FABMenu);
		FABMenu.hideMenuButton(false);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				FABMenu.showMenuButton(true);
			}
		}, 300);

		FABAkorCetveli = (FloatingActionButton) activity.findViewById(R.id.FABAkorCetveli);
		FABAkorCetveli.setOnClickListener(this);

		FABAkorGizleGoster = (FloatingActionButton) activity.findViewById(R.id.FABAkorGizleGoster);
		FABAkorGizleGoster.setOnClickListener(this);

		FABTranspozeArti = (FloatingActionButton) activity.findViewById(R.id.FABTranspozeArti);
		FABTranspozeArti.setOnClickListener(this);

		FABTranspozeEksi = (FloatingActionButton) activity.findViewById(R.id.FABTranspozeEksi);
		FABTranspozeEksi.setOnClickListener(this);

		FABPaylas = (FloatingActionButton) activity.findViewById(R.id.FABPaylas);
		FABPaylas.setOnClickListener(this);
		registerForContextMenu(FABPaylas);

		FABVideoKlip = (FloatingActionButton) activity.findViewById(R.id.FABVideoKlip);
		FABVideoKlip.setOnClickListener(this);

		FABSarkiEkleCikar = (FloatingActionButton) activity.findViewById(R.id.FABSarkiEkleCikar);
		FABSarkiEkleCikar.setOnClickListener(this);

		FABSarkiDuzenle = (FloatingActionButton) activity.findViewById(R.id.FABSarkiDuzenle);
		FABSarkiDuzenle.setOnClickListener(this);

		FABSorunBildir = (FloatingActionButton) activity.findViewById(R.id.FABSorunBildir);
		FABSorunBildir.setOnClickListener(this);
		registerForContextMenu(FABSorunBildir);

		//lblsarki_icerik = (TextView) activity.findViewById(R.id.lblsarki_icerik);
			/*lblsarki_icerik.setOnTouchListener(new OnTouchListener() { //İKİ PARMAK ZOOM (CustomScroll ile birlikte çalışır..)
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getPointerCount() == 2) {
						CSVIcerikScroll.setEnableScrolling(false);
						int action = event.getAction();
						int pureaction = action & MotionEvent.ACTION_MASK;

						if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
							mBaseDist = getDistance(event);
							mBaseRatio = mRatio;
						} else {
							float delta = (getDistance(event) - mBaseDist) / STEP;
							float multi = (float)Math.pow(2, delta);
							mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
							lblsarki_icerik.setTextSize(mRatio+13);
						}
					} else {
						CSVIcerikScroll.setEnableScrolling(true);
					}

					return true;
				}
			});*/

		webview = (WebView) activity.findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setDisplayZoomControls(false);
		webview.addJavascriptInterface(new Object() {
			@JavascriptInterface
			public void performClick(String SecilenAkor) {
				mIntent = new Intent(activity, AkorCetveli.class);
				mIntent.putExtra("SecilenAkor", SecilenAkor);

				AkorDefterimSys.EkranGetir(mIntent, "Normal");
			}
		}, "AkorGosterici");

		webview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				int scrollX = webview.getScrollX(); //for horizontalScrollView
				int scrollY = webview.getScrollY(); //for verticalScrollView
				//DO SOMETHING WITH THE SCROLL COORDINATES

				if (scrollY > firstVisibleItem) {
					firstVisibleItem = scrollY / 150;
				}

				if (firstVisibleItem > mPreviousVisibleItem) {
					FABMenu.hideMenuButton(true);
				} else if (firstVisibleItem < mPreviousVisibleItem) {
					FABMenu.showMenuButton(true);
				}
				mPreviousVisibleItem = firstVisibleItem;
			}
		});

			/*webview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
				@Override
				public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
					if (scrollY > firstVisibleItem) {
						firstVisibleItem = scrollY / 150;
					}

					if (firstVisibleItem > mPreviousVisibleItem) {
						FABMenu.hideMenuButton(true);
					} else if (firstVisibleItem < mPreviousVisibleItem) {
						FABMenu.showMenuButton(true);
					}
					mPreviousVisibleItem = firstVisibleItem;
				}
			});*/

		Bundle bundle = this.getArguments();

		SecilenSarkiID = bundle.getInt("SecilenSarkiID", 0);
		SecilenListeID = bundle.getInt("SecilenListeID", 0);
		SecilenSanatciAdi = bundle.getString("SecilenSanatciAdi", "");
		SecilenSarkiAdi = bundle.getString("SecilenSarkiAdi", "");
		SecilenSarkiIcerik = bundle.getString("SecilenSarkiIcerik", "");
		SecilenSarkiVideoURL = bundle.getString("SecilenSarkiVideoURL", "");

		SecilenSarkiIcerikTemp = SecilenSarkiIcerik;
		webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);

		FABMenuGuncelle();

		AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarki_acildi", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"}]");
	}

	@Override
	public void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
		AkorDefterimSys.SharePrefAyarlarınıUygula();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if(v.getId() == R.id.FABPaylas) {
			String[] SarkiPaylasContextMenu = getResources().getStringArray(R.array.SarkiPaylasContextMenu);
			int i = 0;

			for (String aSarkiPaylasContextMenu : SarkiPaylasContextMenu) {
				menu.add(0, i, 0, aSarkiPaylasContextMenu);
				i++;
			}
		} else if(v.getId() == R.id.FABSorunBildir) {
			String[] SarkiPaylasContextMenu = getResources().getStringArray(R.array.SarkiSorunBildirContextMenu);
			int i = 0;

			for (String aSarkiPaylasContextMenu : SarkiPaylasContextMenu) {
				menu.add(1, i, 0, aSarkiPaylasContextMenu);
				i++;
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getGroupId()) {
			case 0:
				if (item.getItemId() == 0)
					AkorDefterimSys.SosyalMedyaSarkiPaylas("Google", SecilenSanatciAdi + " - " + SecilenSarkiAdi, getString(R.string.sarki_paylas_google_icerik, SecilenSanatciAdi, SecilenSarkiAdi, "https://www.cbcapp.net"), "https://www.cbcapp.net");
				else if (item.getItemId() == 1)
					AkorDefterimSys.SosyalMedyaSarkiPaylas("Facebook", SecilenSanatciAdi + " - " + SecilenSarkiAdi, getString(R.string.sarki_paylas_facebook_icerik, SecilenSanatciAdi, SecilenSarkiAdi), "https://www.cbcapp.net");

				break;
			case 1:
				// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
				Intent mIntent = new Intent(activity, Destek_GeriBildirim.class);

				if (item.getItemId() == 0) {
					mIntent.putExtra("BildirimTipi", "SarkiIcerik");
					mIntent.putExtra("GeriBildirimIcerik", getString(R.string.sarki_sorun_bildirim_icerik1, SecilenSanatciAdi, SecilenSarkiAdi));
				} else if (item.getItemId() == 1) {
					mIntent.putExtra("BildirimTipi", "SarkiIcerik");
					mIntent.putExtra("GeriBildirimIcerik", getString(R.string.sarki_sorun_bildirim_icerik2, SecilenSanatciAdi, SecilenSarkiAdi));
				} else if (item.getItemId() == 2) {
					mIntent.putExtra("BildirimTipi", "SarkiIcerik");
					mIntent.putExtra("GeriBildirimIcerik", getString(R.string.sarki_sorun_bildirim_icerik3, SecilenSanatciAdi, SecilenSarkiAdi));
				}

				AkorDefterimSys.EkranGetir(mIntent, "Slide");

				break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.FABSarkiEkleCikar:
				if(FABSarkiEkleCikar.getLabelText().equals(getString(R.string.listeme_ekle))) {
					Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
					mIntent.putExtra("Islem", "SarkiEkle");
					mIntent.putExtra("SecilenSanatciAdi", SecilenSanatciAdi);
					mIntent.putExtra("SecilenSarkiAdi", SecilenSarkiAdi);
					mIntent.putExtra("SecilenSarkiVideoURL", SecilenSarkiVideoURL);
					mIntent.putExtra("SecilenSarkiIcerik", SecilenSarkiIcerik);

					AkorDefterimSys.EkranGetir(mIntent, "Slide");

					AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarkiyi_repertuvarina_ekliyor", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"}]");
				} else if(FABSarkiEkleCikar.getLabelText().equals(getString(R.string.listemden_sil))) {
					if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
						ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
								getString(R.string.sil),
								getString(R.string.sarki_sil_soru, SecilenSanatciAdi, SecilenSarkiAdi),
								getString(R.string.evet),
								"Frg_Sarki_ADDialog_SarkiSil_Evet",
								getString(R.string.hayir),
								"Frg_Sarki_ADDialog_SarkiSil_Hayir");
						ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog.show();
					}

					AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarkiyi_repertuvarindan_siliyor", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"}]");
				}

				break;
			case R.id.FABSarkiDuzenle:
				Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
				mIntent.putExtra("Islem", "SarkiDuzenle");
				mIntent.putExtra("SecilenSarkiID", SecilenSarkiID);
				mIntent.putExtra("SecilenSanatciAdi", SecilenSanatciAdi);
				mIntent.putExtra("SecilenSarkiAdi", SecilenSarkiAdi);
				mIntent.putExtra("SecilenSarkiVideoURL", SecilenSarkiVideoURL);
				mIntent.putExtra("SecilenSarkiIcerik", veritabani.SarkiIcerikGetir(SecilenSarkiID));

				AkorDefterimSys.EkranGetir(mIntent, "Slide");

				AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarkiyi_repertuvarindan_duzenliyor", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"}]");
				break;
			case R.id.FABTranspozeArti:
				AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarki_transpoze_edildi", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"},{\"Param\":\"+1\"}]");
				SecilenSarkiIcerikTemp = AkorDefterimSys.Transpoze("+1", SecilenSarkiIcerikTemp).toString();
				webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				break;
			case R.id.FABTranspozeEksi:
				AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarki_transpoze_edildi", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"},{\"Param\":\"-1\"}]");
				SecilenSarkiIcerikTemp = AkorDefterimSys.Transpoze("-1", SecilenSarkiIcerikTemp).toString();
				webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				break;
			case R.id.FABAkorCetveli:
				mIntent = new Intent(activity, AkorCetveli.class);
				mIntent.putExtra("SecilenAkor", "A");

				AkorDefterimSys.EkranGetir(mIntent, "Normal");
				break;
			case R.id.FABPaylas:
				activity.openContextMenu(FABPaylas);
				break;
			case R.id.FABVideoKlip:
				try {
					URL VideoURL = new URL(SecilenSarkiVideoURL);

					if(VideoURL.getHost().equals("youtube.com") || VideoURL.getHost().equals("www.youtube.com") || VideoURL.getHost().equals("m.youtube.com")) {
						/**
						 * Calling youtube stand alone player
						 *
						 * You should read this parameter to change them
						 * Parameters
						 * activity*  The calling activity from which the standalone player will be started.
						 * developerKey*  A valid API key which is enabled to use the YouTube Data API v3 service. To generate a new key, visit the Google APIs Console.
						 * videoId*  The id of the video to be played.
						 * timeMillis*  The time, in milliseconds, where playback should start in the video.
						 * autoplay*  true to have the video start playback as soon as the standalone player loads, false to cue the video.
						 * lightboxMode*  true to have the video play in a dialog view above your current Activity, false to have the video play fullscreen.
						 */

						/*Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity, getString(R.string.google_api_key), SecilenSarkiVideoURL,0,true,true);
						startActivity(intent);*/

						try {
							List<String> URLQueryList = AkorDefterimSys.splitQuery(VideoURL).get("v");

							if(URLQueryList != null && URLQueryList.size() > 0) {
								mIntent = new Intent(activity, YoutubeVideoPlayer.class);
								mIntent.putExtra("VideoID", URLQueryList.get(0));

								AkorDefterimSys.EkranGetir(mIntent, "Normal");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SecilenSarkiVideoURL)));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

				break;
			case R.id.FABSorunBildir:
				activity.openContextMenu(FABSorunBildir);
				break;
			case R.id.FABAkorGizleGoster:
				if(AkorGizliMi) {
					SecilenSarkiIcerikTemp = SecilenSarkiIcerik;

					webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);

					AkorGizliMi = false;

					FABAkorGizleGoster.setLabelText(getString(R.string.akor_goster));
					FABAkorGizleGoster.setImageResource(R.drawable.ic_note_beyaz);
				} else { // Akorlar gizli değilse
					String Part1 = "", Part2 = "";

					while(true) {
						if(SecilenSarkiIcerikTemp.indexOf(AkorDefterimSys.HtmlTag1_1) != -1) {
							Part1 = SecilenSarkiIcerikTemp.substring(0, SecilenSarkiIcerikTemp.indexOf("<span"));
							Part2 = SecilenSarkiIcerikTemp.substring(SecilenSarkiIcerikTemp.indexOf("span>") + 5, SecilenSarkiIcerikTemp.length());
							SecilenSarkiIcerikTemp = Part1 + Part2;
						} else break;
					}

					webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);

					AkorGizliMi = true;

					FABAkorGizleGoster.setLabelText(getString(R.string.akor_gizle));
					FABAkorGizleGoster.setImageResource(R.drawable.ic_note_off_beyaz);
				}

				break;
		}
	}

	private void FABMenuGuncelle() {
		FABMenu.removeMenuButton(FABAkorCetveli);
		FABMenu.removeMenuButton(FABAkorGizleGoster);
		FABMenu.removeMenuButton(FABTranspozeArti);
		FABMenu.removeMenuButton(FABTranspozeEksi);
		FABMenu.removeMenuButton(FABPaylas);
		FABMenu.removeMenuButton(FABVideoKlip);
		FABMenu.removeMenuButton(FABSarkiEkleCikar);
		FABMenu.removeMenuButton(FABSarkiDuzenle);
		FABMenu.removeMenuButton(FABSorunBildir);

		FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
		FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli));
		FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
		FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
		FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

		FABMenu.addMenuButton(FABAkorCetveli);

		FABAkorGizleGoster.setButtonSize(FloatingActionButton.SIZE_MINI);

		if(AkorGizliMi) {
			FABAkorGizleGoster.setLabelText(getString(R.string.akor_gizle));
			FABAkorGizleGoster.setImageResource(R.drawable.ic_note_beyaz);
		} else {
			FABAkorGizleGoster.setLabelText(getString(R.string.akor_gizle));
			FABAkorGizleGoster.setImageResource(R.drawable.ic_note_off_beyaz);
		}

		FABAkorGizleGoster.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
		FABAkorGizleGoster.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

		FABMenu.addMenuButton(FABAkorGizleGoster);

		FABTranspozeArti.setButtonSize(FloatingActionButton.SIZE_MINI);
		FABTranspozeArti.setLabelText(getString(R.string.tizlestir));
		FABTranspozeArti.setImageResource(R.drawable.ic_transpoze_arti_beyaz);
		FABTranspozeArti.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
		FABTranspozeArti.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

		FABMenu.addMenuButton(FABTranspozeArti);

		FABTranspozeEksi.setButtonSize(FloatingActionButton.SIZE_MINI);
		FABTranspozeEksi.setLabelText(getString(R.string.pestlestir));
		FABTranspozeEksi.setImageResource(R.drawable.ic_transpoze_eksi_beyaz);
		FABTranspozeEksi.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
		FABTranspozeEksi.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

		FABMenu.addMenuButton(FABTranspozeEksi);

		if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi")) { // Çevrimdışı giriş yapıldıysa
			//FABMenu.hideMenuButton(true);
			FABSarkiDuzenle.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABSarkiDuzenle.setLabelText(getString(R.string.duzenle));
			FABSarkiDuzenle.setImageResource(R.drawable.ic_book_edit);
			FABSarkiDuzenle.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
			FABSarkiDuzenle.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABSarkiDuzenle);

			FABSarkiEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABSarkiEkleCikar.setLabelText(getString(R.string.listemden_sil));
			FABSarkiEkleCikar.setImageResource(R.drawable.book_minus_beyaz);
			FABSarkiEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
			FABSarkiEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABSarkiEkleCikar);
		} else {
			if(SecilenListeID == 0) { // Getirilen liste Akor Defterim listesi ise
				FABPaylas.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABPaylas.setLabelText(getString(R.string.paylas));
				FABPaylas.setImageResource(R.drawable.ic_share);
				FABPaylas.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABPaylas.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABPaylas);

                if(!SecilenSarkiVideoURL.equals("") || !SecilenSarkiVideoURL.equals("-")) { // Şarkı içeriğinde video klip var ise..
                    try {
                        URL VideoURL = new URL(SecilenSarkiVideoURL);

                        if(VideoURL.getHost().equals("youtube.com") || VideoURL.getHost().equals("www.youtube.com") || VideoURL.getHost().equals("m.youtube.com")) {
                            try {
                                List<String> URLQueryList = AkorDefterimSys.splitQuery(VideoURL).get("v");

                                if(URLQueryList != null && URLQueryList.size() > 0) {
                                    FABVideoKlip.setButtonSize(FloatingActionButton.SIZE_MINI);
                                    FABVideoKlip.setLabelText(getString(R.string.video_klip));
                                    FABVideoKlip.setImageResource(R.drawable.ic_youtube_beyaz);
                                    FABVideoKlip.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
                                    FABVideoKlip.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                                    FABMenu.addMenuButton(FABVideoKlip);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            FABVideoKlip.setButtonSize(FloatingActionButton.SIZE_MINI);
                            FABVideoKlip.setLabelText(getString(R.string.video_klip));
                            FABVideoKlip.setImageResource(R.drawable.ic_video_beyaz);
                            FABVideoKlip.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
                            FABVideoKlip.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                            FABMenu.addMenuButton(FABVideoKlip);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

				FABSarkiEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABSarkiEkleCikar.setLabelText(getString(R.string.listeme_ekle));
				FABSarkiEkleCikar.setImageResource(R.drawable.book_plus_beyaz);
				FABSarkiEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABSarkiEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABSarkiEkleCikar);

				FABSorunBildir.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABSorunBildir.setLabelText(getString(R.string.sorun_bildir));
				FABSorunBildir.setImageResource(R.drawable.ic_warning_file_beyaz);
				FABSorunBildir.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABSorunBildir.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABSorunBildir);
			} else { // Getirilen liste Cihaz veritabanındaki liste ise
                if(!SecilenSarkiVideoURL.equals("") || !SecilenSarkiVideoURL.equals("-")) { // Şarkı içeriğinde video klip var ise..
                    try {
                        URL VideoURL = new URL(SecilenSarkiVideoURL);

                        if(VideoURL.getHost().equals("youtube.com") || VideoURL.getHost().equals("www.youtube.com") || VideoURL.getHost().equals("m.youtube.com")) {
                            try {
                                List<String> URLQueryList = AkorDefterimSys.splitQuery(VideoURL).get("v");

                                if(URLQueryList != null && URLQueryList.size() > 0) {
                                    FABVideoKlip.setButtonSize(FloatingActionButton.SIZE_MINI);
                                    FABVideoKlip.setLabelText(getString(R.string.video_klip));
                                    FABVideoKlip.setImageResource(R.drawable.ic_youtube_beyaz);
                                    FABVideoKlip.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
                                    FABVideoKlip.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                                    FABMenu.addMenuButton(FABVideoKlip);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            FABVideoKlip.setButtonSize(FloatingActionButton.SIZE_MINI);
                            FABVideoKlip.setLabelText(getString(R.string.video_klip));
                            FABVideoKlip.setImageResource(R.drawable.ic_video_beyaz);
                            FABVideoKlip.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
                            FABVideoKlip.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                            FABMenu.addMenuButton(FABVideoKlip);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

				FABSarkiDuzenle.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABSarkiDuzenle.setLabelText(getString(R.string.duzenle));
				FABSarkiDuzenle.setImageResource(R.drawable.ic_book_edit);
				FABSarkiDuzenle.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABSarkiDuzenle.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABSarkiDuzenle);

				FABSarkiEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABSarkiEkleCikar.setLabelText(getString(R.string.listemden_sil));
				FABSarkiEkleCikar.setImageResource(R.drawable.book_minus_beyaz);
				FABSarkiEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABSarkiEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABSarkiEkleCikar);
			}
		}
	}

	int getDistance(MotionEvent event) {
		int dx = (int)(event.getX(0) - event.getX(1));
		int dy = (int)(event.getY(0) - event.getY(1));

		return (int)(Math.sqrt(dx * dx + dy * dy));
	}

	public boolean FABMenuDurum() {
		return FABMenu.isOpened();
	}

	public void FABKapatAc(Boolean Islem) {
		if(Islem) FABMenu.open(true);
		else if(!Islem) FABMenu.close(true);
	}

	// Bu method ile webview objesine dokunuyoruz..
	public void Dokun() {
		// Obtain MotionEvent object
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis() + 100;
		float x = 0.0f;
		float y = 0.0f;
		int metaState = 0;

		MotionEvent motionEvent = MotionEvent.obtain(
				downTime,
				eventTime,
				MotionEvent.ACTION_DOWN,
				x,
				y,
				metaState
		);

		// Dispatch touch event to view
		LLSayfa.dispatchTouchEvent(motionEvent);
	}

	public void StandartSnackBarMsj(String Mesaj) {
		AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout_FrgSarki, Mesaj);
	}

	public void SarkiSil() {
		if (veritabani.SarkiSil(SecilenSarkiID)) {
			FABMenuGuncelle();

			if(SecilenListeID != 0) {
				FragmentDataConn.FragmentSayfaGetir("anasayfa");
				FragmentDataConn.SarkiListesiGetir();
			}

			FragmentDataConn.StandartSnackBarMsj("Sağ", getString(R.string.sarki_silindi, SecilenSarkiAdi, SecilenSanatciAdi));
			AkorDefterimSys.SonYapilanIslemGuncelle("xxx_isimli_sarkiyi_repertuvarindan_sildi", "[{\"Param\":\"" + SecilenSanatciAdi + "\"},{\"Param\":\"" + SecilenSarkiAdi + "\"}]");
		} else StandartSnackBarMsj(getString(R.string.islem_yapilirken_bir_hata_olustu));
	}
}