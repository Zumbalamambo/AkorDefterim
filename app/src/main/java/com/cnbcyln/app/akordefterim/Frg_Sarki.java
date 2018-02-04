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
import android.widget.Toast;

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
	AlertDialog ADDialog;
	SharedPreferences sharedPref;

	List<SnfListeler> SnfListeler;
	List<SnfKategoriler> SnfKategoriler;
	List<SnfTarzlar> SnfTarzlar;

	AdpListelerSPN AdpListelerSPN;
	AdpKategori AdpKategoriler;
	AdpTarz AdpTarzlar;

	CoordinatorLayout coordinatorLayout_FrgSarki;
	LinearLayout LLSayfa;
	FloatingActionMenu FABMenu;
	FloatingActionButton FABListemeEkleCikar, FABTranspozeArti, FABTranspozeEksi, FABPaylas, FABAkorCetveli;
	WebView webview;

	int firstVisibleItem, mPreviousVisibleItem, SecilenSarkiID, SecilenListeID, TranspozeNo;
	String SecilenSarkiAdi, SecilenSanatciAdi, SecilenSarkiIcerik, SecilenSarkiIcerikTemp;
	String HTMLBaslangic = "<html><head><style>html, body {top:0; bottom:0; left:0; right:0;}</style></head><body>";
	String HTMLBitis = "</body></html>";

	final static float STEP = 200;
	float mRatio = 1.0f;
	int mBaseDist;
	float mBaseRatio;

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

		FABPaylas = (FloatingActionButton) activity.findViewById(R.id.FABPaylas);
		FABPaylas.setOnClickListener(this);
		registerForContextMenu(FABPaylas);

		FABTranspozeArti = (FloatingActionButton) activity.findViewById(R.id.FABTranspozeArti);
		FABTranspozeArti.setOnClickListener(this);

		FABTranspozeEksi = (FloatingActionButton) activity.findViewById(R.id.FABTranspozeEksi);
		FABTranspozeEksi.setOnClickListener(this);

		FABListemeEkleCikar = (FloatingActionButton) activity.findViewById(R.id.FABListemeEkleCikar);
		FABListemeEkleCikar.setOnClickListener(this);

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
			public void performClick(String akor) {
				AkorDefterimSys.AkorCetveli(activity, akor);
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

		SecilenSarkiIcerikTemp = SecilenSarkiIcerik;
		webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);

		FABMenuGuncelle();
	}

	@Override
	public void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		String[] SarkiPaylasContextMenu = getResources().getStringArray(R.array.SarkiPaylasContextMenu);

		for (String aSarkiPaylasContextMenu : SarkiPaylasContextMenu) {
			menu.add(0, v.getId(), 0, aSarkiPaylasContextMenu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getTitle().toString()) {
			case "Google":
				AkorDefterimSys.SosyalMedyaSarkiPaylas("Google", SecilenSanatciAdi + " - " + SecilenSarkiAdi, getString(R.string.sarki_paylas_google_icerik, SecilenSanatciAdi, SecilenSarkiAdi, "https://www.cbcapp.net"), "https://www.cbcapp.net");

				break;
			case "Facebook":
				AkorDefterimSys.SosyalMedyaSarkiPaylas("Facebook", SecilenSanatciAdi + " - " + SecilenSarkiAdi, getString(R.string.sarki_paylas_facebook_icerik, SecilenSanatciAdi, SecilenSarkiAdi), "https://www.cbcapp.net");

				break;
			default:
				return false;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.FABListemeEkleCikar:
				if(FABListemeEkleCikar.getLabelText().equals(getString(R.string.listeme_ekle))) {
					Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
					mIntent.putExtra("Islem", "SarkiEkle");
					mIntent.putExtra("SecilenSanatciAdi", SecilenSanatciAdi);
					mIntent.putExtra("SecilenSarkiAdi", SecilenSarkiAdi);
					mIntent.putExtra("SecilenSarkiIcerik", SecilenSarkiIcerik);

					AkorDefterimSys.EkranGetir(mIntent, "Slide");

					/*SarkiIcerikTemp = SarkiIcerik; // IcerikTemp'i orjinal içerik yapıyoruz..

					final WebView Dialog_webview = (WebView) ViewDialogContent.findViewById(R.id.Dialog_webview);
					Dialog_webview.loadDataWithBaseURL(null, "<html><body>" + SarkiIcerikTemp + "</body></html>", "text/html", "utf-8", null);

					Button Dialog_btnTranspozeArti = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnTranspozeArti);
					Dialog_btnTranspozeArti.setTypeface(YaziFontu);
					Dialog_btnTranspozeArti.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							SarkiIcerikTemp = AkorDefterimSys.Transpoze("+1", SarkiIcerikTemp).toString();

							Dialog_webview.loadDataWithBaseURL(null, "<html><body>" + SarkiIcerikTemp + "</body></html>", "text/html", "utf-8", null);
							TranspozeNo++;

							if(TranspozeNo < 0) Dialog_lblTranspozeSeviye.setText(String.valueOf(TranspozeNo));
							else if(TranspozeNo == 0) Dialog_lblTranspozeSeviye.setText(getString(R.string.orjinal_ton));
							else if(TranspozeNo > 0) Dialog_lblTranspozeSeviye.setText("+" + String.valueOf(TranspozeNo));
						}
					});

					Button Dialog_btnTranspozeEksi = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnTranspozeEksi);
					Dialog_btnTranspozeEksi.setTypeface(YaziFontu);
					Dialog_btnTranspozeEksi.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							SarkiIcerikTemp = AkorDefterimSys.Transpoze("-1", SarkiIcerikTemp).toString();

							Dialog_webview.loadDataWithBaseURL(null, "<html><body>" + SarkiIcerikTemp + "</body></html>", "text/html", "utf-8", null);
							TranspozeNo--;

							if(TranspozeNo < 0) Dialog_lblTranspozeSeviye.setText(String.valueOf(TranspozeNo));
							else if(TranspozeNo == 0) Dialog_lblTranspozeSeviye.setText(getString(R.string.orjinal_ton));
							else if(TranspozeNo > 0) Dialog_lblTranspozeSeviye.setText("+" + String.valueOf(TranspozeNo));
						}
					});*/
				} else if(FABListemeEkleCikar.getLabelText().equals(getString(R.string.listemden_sil))) {
					if (veritabani.SarkiSil(SecilenSarkiID)) {
						FABMenuGuncelle();

						if(SecilenListeID != 0) {
							FragmentDataConn.FragmentSayfaGetir("anasayfa");
							//FragmentDataConn.SarkiListesiDoldur();
							FragmentDataConn.SlidingIslem(2); //Sağ sliding aç
						}

						AkorDefterimSys.ToastMsj(activity, getString(R.string.yerel_repertuvar_guncellendi), Toast.LENGTH_SHORT);
					} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
				}

				break;
			case R.id.FABTranspozeArti:
				SecilenSarkiIcerikTemp = AkorDefterimSys.Transpoze("+1", SecilenSarkiIcerikTemp).toString();
				webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				break;
			case R.id.FABTranspozeEksi:
				SecilenSarkiIcerikTemp = AkorDefterimSys.Transpoze("-1", SecilenSarkiIcerikTemp).toString();
				webview.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				break;
			case R.id.FABAkorCetveli:
				AkorDefterimSys.AkorCetveli(activity, "A");
				break;
			case R.id.FABPaylas:
				activity.openContextMenu(FABPaylas);
				break;
		}
	}

	private void FABMenuGuncelle() {
		FABMenu.removeMenuButton(FABAkorCetveli);
		FABMenu.removeMenuButton(FABPaylas);
		FABMenu.removeMenuButton(FABTranspozeArti);
		FABMenu.removeMenuButton(FABTranspozeEksi);
		FABMenu.removeMenuButton(FABListemeEkleCikar);

		if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi")) { // Çevrimdışı giriş yapıldıysa
			//FABMenu.hideMenuButton(true);
			FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli));
			FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
			FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
			FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABAkorCetveli);

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

			FABListemeEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABListemeEkleCikar.setLabelText(getString(R.string.listemden_sil));
			FABListemeEkleCikar.setImageResource(R.drawable.book_minus_beyaz);
			FABListemeEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
			FABListemeEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABListemeEkleCikar);
		} else {
			if(SecilenListeID == 0) { // Getirilen liste Akor Defterim listesi ise
				FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli));
				FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
				FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABAkorCetveli);

				FABPaylas.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABPaylas.setLabelText(getString(R.string.paylas));
				FABPaylas.setImageResource(R.drawable.ic_share);
				FABPaylas.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABPaylas.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABPaylas);

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

				FABListemeEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABListemeEkleCikar.setLabelText(getString(R.string.listeme_ekle));
				FABListemeEkleCikar.setImageResource(R.drawable.book_plus_beyaz);
				FABListemeEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABListemeEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABListemeEkleCikar);
			} else { // Getirilen liste Cihaz veritabanındaki liste ise
				FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli));
				FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
				FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABAkorCetveli);

				FABPaylas.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABPaylas.setLabelText(getString(R.string.paylas));
				FABPaylas.setImageResource(R.drawable.ic_share);
				FABPaylas.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABPaylas.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABPaylas);

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

				FABListemeEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABListemeEkleCikar.setLabelText(getString(R.string.listemden_sil));
				FABListemeEkleCikar.setImageResource(R.drawable.book_minus_beyaz);
				FABListemeEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.KoyuMavi2));
				FABListemeEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABListemeEkleCikar);
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
}