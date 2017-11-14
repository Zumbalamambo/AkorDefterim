package com.cnbcyln.app.akordefterim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListeler;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.*;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

@SuppressWarnings("ALL")
public class Frg_Sarki extends Fragment implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani Veritabani;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	InputMethodManager imm;
	ProgressDialog PDOnlineSarkiGetirDialog;
	SharedPreferences sharedPref;
	View ViewDialogContent;
	LayoutInflater inflater;

	// *** Dialog Değişkenleri *** //
	AlertDialog ADDialogSarkiEkle, ADDialogListeKategoriTarzEkle, ADDialogListeKategoriTarzSil;
	Spinner Dialog_spnListeler, Dialog_spnKategori, Dialog_spnTarz;
	Button Dialog_btnListeEkle, Dialog_btnListeSil, Dialog_btnKategoriEkle, Dialog_btnKategoriSil, Dialog_btnTarzEkle, Dialog_btnTarzSil;

	List<SnfListeler> SnfListeler;
	List<SnfKategoriler> SnfKategoriler;
	List<SnfTarzlar> SnfTarzlar;

	AdpListeler AdpListeler;
	AdpKategori AdpKategoriler;
	AdpTarz AdpTarzlar;

	FloatingActionMenu FABMenu;
	FloatingActionButton FABListemeEkleCikar, FABTranspozeArti, FABTranspozeEksi, FABPaylas, FABAkorCetveli;
	WebView webview;
	String SarkiAdi, SanatciAdi, DepolamaAlani, SarkiIcerik, SarkiIcerikTemp;
	int firstVisibleItem, mPreviousVisibleItem, SarkiID, TranspozeNo;

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
		AkorDefterimSys = new AkorDefterimSys(activity);
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Interface_FragmentDataConn) activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
		inflater = activity.getLayoutInflater();

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

		ViewDialogContent = inflater.inflate(R.layout.dialog_listeye_sarki_ekle, null);
		ADDialogSarkiEkle = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.listeye_sarki_ekle_baslik), ViewDialogContent, getString(R.string.ekle), getString(R.string.iptal), false);

		Bundle bundle = this.getArguments();

		SarkiID = bundle.getInt("SarkiID", 0);
		SarkiAdi = bundle.getString("SarkiAdi", "");
		SanatciAdi = bundle.getString("SanatciAdi", "");
		DepolamaAlani = bundle.getString("DepolamaAlani", "Cihaz");

		if(DepolamaAlani.equals("AkorDefterim") && !AkorDefterimSys.ProgressDialogisShowing(PDOnlineSarkiGetirDialog)) new OnlineSarkiGetir().execute(String.valueOf(SarkiID), "0");
		else SarkiDoldur(Veritabani.SarkiIcerikGetir(SarkiID), "0");

		FABMenuGuncelle();
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
				FragmentDataConn.SarkiPaylas("Google", SanatciAdi + " - " + SarkiAdi, getString(R.string.sarki_paylas_google_icerik, sharedPref.getString("prefHesapAdSoyad", ""), SanatciAdi, SarkiAdi, "https://www.cbcapp.net"), "https://www.cbcapp.net");

				break;
			case "Facebook":
				FragmentDataConn.SarkiPaylas("Facebook", SanatciAdi + " - " + SarkiAdi, getString(R.string.sarki_paylas_facebook_icerik, sharedPref.getString("prefHesapAdSoyad", ""), SanatciAdi, SarkiAdi), "https://www.cbcapp.net");

				break;
			default:
				return false;
		}

		return true;
	}

	private void SarkiDoldur(String Icerik, String Transpoze) {
		SarkiIcerik = Icerik;
		SarkiIcerikTemp = SarkiIcerik;
		if(!Transpoze.equals("0")) SarkiIcerikTemp = AkorDefterimSys.Transpoze(Transpoze, SarkiIcerikTemp).toString();
		webview.loadDataWithBaseURL(null, "<html><body>" + SarkiIcerikTemp + "</body></html>", "text/html", "utf-8", null);
	}

	private void FABMenuGuncelle() {
		FABMenu.removeMenuButton(FABAkorCetveli);
		FABMenu.removeMenuButton(FABPaylas);
		FABMenu.removeMenuButton(FABTranspozeArti);
		FABMenu.removeMenuButton(FABTranspozeEksi);
		FABMenu.removeMenuButton(FABListemeEkleCikar);

		if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi")) { //Yalnızca Normal Oturum olan hesaplarda repertuvara ekleme tuşu gösterilir..
			//FABMenu.hideMenuButton(true);
			FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli, ""));
			FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
			FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
			FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABAkorCetveli);

			FABPaylas.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABPaylas.setLabelText(getString(R.string.paylas));
			FABPaylas.setImageResource(R.drawable.paylas);
			FABPaylas.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
			FABPaylas.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABPaylas);

			FABTranspozeArti.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABTranspozeArti.setLabelText(getString(R.string.tizlestir));
			FABTranspozeArti.setImageResource(R.drawable.transpoze_arti2);
			FABTranspozeArti.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
			FABTranspozeArti.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABTranspozeArti);

			FABTranspozeEksi.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABTranspozeEksi.setLabelText(getString(R.string.pestlestir));
			FABTranspozeEksi.setImageResource(R.drawable.transpoze_eksi2);
			FABTranspozeEksi.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
			FABTranspozeEksi.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABTranspozeEksi);

			FABListemeEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
			FABListemeEkleCikar.setLabelText(getString(R.string.listemden_sil));
			FABListemeEkleCikar.setImageResource(R.drawable.book_minus_beyaz);
			FABListemeEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
			FABListemeEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

			FABMenu.addMenuButton(FABListemeEkleCikar);
		} else {
			if(DepolamaAlani.equals("AkorDefterim")) { //Getirilen liste Akor Defterim listesi ise
				FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli, ""));
				FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
				FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABAkorCetveli);

				FABPaylas.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABPaylas.setLabelText(getString(R.string.paylas));
				FABPaylas.setImageResource(R.drawable.paylas);
				FABPaylas.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABPaylas.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABPaylas);

				FABTranspozeArti.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABTranspozeArti.setLabelText(getString(R.string.tizlestir));
				FABTranspozeArti.setImageResource(R.drawable.transpoze_arti2);
				FABTranspozeArti.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABTranspozeArti.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABTranspozeArti);

				FABTranspozeEksi.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABTranspozeEksi.setLabelText(getString(R.string.pestlestir));
				FABTranspozeEksi.setImageResource(R.drawable.transpoze_eksi2);
				FABTranspozeEksi.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABTranspozeEksi.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABTranspozeEksi);

				FABListemeEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABListemeEkleCikar.setLabelText(getString(R.string.listeme_ekle));
				FABListemeEkleCikar.setImageResource(R.drawable.book_plus_beyaz);
				FABListemeEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABListemeEkleCikar.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABListemeEkleCikar);
			} else { // Getirilen liste Cihaz veritabanındaki liste ise
				FABAkorCetveli.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABAkorCetveli.setLabelText(getString(R.string.akor_cetveli, ""));
				FABAkorCetveli.setImageResource(R.drawable.akor_cetveli);
				FABAkorCetveli.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABAkorCetveli.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABAkorCetveli);

				FABPaylas.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABPaylas.setLabelText(getString(R.string.paylas));
				FABPaylas.setImageResource(R.drawable.paylas);
				FABPaylas.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABPaylas.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABPaylas);

				FABTranspozeArti.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABTranspozeArti.setLabelText(getString(R.string.tizlestir));
				FABTranspozeArti.setImageResource(R.drawable.transpoze_arti2);
				FABTranspozeArti.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABTranspozeArti.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABTranspozeArti);

				FABTranspozeEksi.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABTranspozeEksi.setLabelText(getString(R.string.pestlestir));
				FABTranspozeEksi.setImageResource(R.drawable.transpoze_eksi2);
				FABTranspozeEksi.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
				FABTranspozeEksi.setColorPressed(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

				FABMenu.addMenuButton(FABTranspozeEksi);

				FABListemeEkleCikar.setButtonSize(FloatingActionButton.SIZE_MINI);
				FABListemeEkleCikar.setLabelText(getString(R.string.listemden_sil));
				FABListemeEkleCikar.setImageResource(R.drawable.book_minus_beyaz);
				FABListemeEkleCikar.setColorNormal(ContextCompat.getColor(getActivity(), R.color.AcikMavi));
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.FABListemeEkleCikar:
				if(FABListemeEkleCikar.getLabelText().equals(getString(R.string.listeme_ekle))) {
					if(!ADDialogSarkiEkle.isShowing()) {
						SarkiIcerikTemp = SarkiIcerik; // IcerikTemp'i orjinal içerik yapıyoruz..

						ViewDialogContent = inflater.inflate(R.layout.dialog_listeye_sarki_ekle, null);
						ADDialogSarkiEkle = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.listeye_sarki_ekle_baslik), ViewDialogContent, getString(R.string.ekle), getString(R.string.iptal), false);

						ADDialogSarkiEkle.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

						TextView Dialog_lblTranspozeBaslik = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblTranspozeBaslik);
						Dialog_lblTranspozeBaslik.setTypeface(YaziFontu, Typeface.BOLD);

						final TextView Dialog_lblTranspozeSeviye = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblTranspozeSeviye);
						Dialog_lblTranspozeSeviye.setTypeface(YaziFontu);
						Dialog_lblTranspozeSeviye.setText(getString(R.string.orjinal_ton));

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
						});




						// ##### Listeler #####
						TextView Dialog_lblListeler = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblListeler);
						Dialog_lblListeler.setTypeface(YaziFontu, Typeface.BOLD);

						// Burayı "Cevrimdisi" olarak belirledik. Çünkü Şarkı eklerken şarkı ekleme dialogundaki "Listeler" bölümünde "Akor Defterim" seçeneği çıkıyordu. Böylelikle bunu pasif yaptık..
						SnfListeler = Veritabani.lst_ListeGetir("Cevrimdisi");
						AdpListeler = new AdpListeler(activity, SnfListeler);

						Dialog_spnListeler = (Spinner) ViewDialogContent.findViewById(R.id.Dialog_spnListeler);
						Dialog_spnListeler.setAdapter(AdpListeler);

						Dialog_btnListeEkle = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnListeEkle);
						Dialog_btnListeEkle.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
								ADDialogListeKategoriTarzEkle = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_liste_ekle), ViewDialogContent, getString(R.string.ekle), getString(R.string.iptal), false);
								ADDialogListeKategoriTarzEkle.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

								final EditText Dialog_EtxtListeKategoriTarzAdi = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtListeKategoriTarzAdi);
								Dialog_EtxtListeKategoriTarzAdi.setTypeface(YaziFontu);
								Dialog_EtxtListeKategoriTarzAdi.setHint(getString(R.string.web_liste_adi));
								Dialog_EtxtListeKategoriTarzAdi.requestFocus();
								imm.showSoftInput(Dialog_EtxtListeKategoriTarzAdi, 0);

								ADDialogListeKategoriTarzEkle.show();

								ADDialogListeKategoriTarzEkle.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										String ListeAdi = Dialog_EtxtListeKategoriTarzAdi.getText().toString().trim();

										if(!Veritabani.ListeVarmiKontrol(ListeAdi)) {
											if(Veritabani.ListeEkle(ListeAdi)) {
												SnfListeler = Veritabani.lst_ListeGetir("Cevrimdisi");
												AdpListeler = new AdpListeler(activity, SnfListeler);

												Dialog_spnListeler.setAdapter(AdpListeler);

												Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
												Frg_TabRepKontrol.spnListeGetir();

												AkorDefterimSys.ToastMsj(activity, getString(R.string.web_liste_eklendi2), Toast.LENGTH_SHORT);
											} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_liste_eklenemedi2), Toast.LENGTH_SHORT);
										} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_liste_eklenemedi3, ListeAdi), Toast.LENGTH_SHORT);

										imm.hideSoftInputFromWindow(Dialog_EtxtListeKategoriTarzAdi.getWindowToken(), 0);
										ADDialogListeKategoriTarzEkle.dismiss();
									}
								});

								ADDialogListeKategoriTarzEkle.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										imm.hideSoftInputFromWindow(Dialog_EtxtListeKategoriTarzAdi.getWindowToken(), 0);
										ADDialogListeKategoriTarzEkle.dismiss();
									}
								});
							}
						});

						Dialog_btnListeSil = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnListeSil);
						Dialog_btnListeSil.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								final int ListeID = SnfListeler.get(Dialog_spnListeler.getSelectedItemPosition()).getId();
								final String ListeAdi = SnfListeler.get(Dialog_spnListeler.getSelectedItemPosition()).getListeAdi();

								ADDialogListeKategoriTarzSil = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_liste_sil), getString(R.string.web_liste_sil_soru, ListeAdi), getString(R.string.web_sil), getString(R.string.iptal));
								ADDialogListeKategoriTarzSil.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialogListeKategoriTarzSil.show();

								ADDialogListeKategoriTarzSil.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(!Veritabani.ListeyeAitSarkiVarmiKontrol(ListeID)) {
											if(Veritabani.ListeSil(ListeID)) {
												SnfListeler = Veritabani.lst_ListeGetir("Cevrimdisi");
												AdpListeler = new AdpListeler(activity, SnfListeler);

												Dialog_spnListeler.setAdapter(AdpListeler);

												Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
												Frg_TabRepKontrol.spnListeGetir();

												AkorDefterimSys.ToastMsj(activity, getString(R.string.web_liste_silindi2, ListeAdi), Toast.LENGTH_SHORT);
											} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_liste_silinemedi2), Toast.LENGTH_SHORT);
										} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_liste_silinemedi3), Toast.LENGTH_SHORT);

										ADDialogListeKategoriTarzSil.dismiss();
									}
								});

								ADDialogListeKategoriTarzSil.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										ADDialogListeKategoriTarzSil.dismiss();
									}
								});
							}
						});




						// ##### Kategoriler #####
						TextView Dialog_lblKategori = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblKategori);
						Dialog_lblKategori.setTypeface(YaziFontu, Typeface.BOLD);

						SnfKategoriler = Veritabani.lst_KategoriGetir(false);
						AdpKategoriler = new AdpKategori(activity, SnfKategoriler);

						Dialog_spnKategori = (Spinner) ViewDialogContent.findViewById(R.id.Dialog_spnKategori);
						Dialog_spnKategori.setAdapter(AdpKategoriler);

						Dialog_btnKategoriEkle = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnKategoriEkle);
						Dialog_btnKategoriEkle.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
								ADDialogListeKategoriTarzEkle = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_kategori_ekle), ViewDialogContent, getString(R.string.ekle), getString(R.string.iptal), false);
								ADDialogListeKategoriTarzEkle.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

								final EditText Dialog_EtxtListeKategoriTarzAdi = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtListeKategoriTarzAdi);
								Dialog_EtxtListeKategoriTarzAdi.setTypeface(YaziFontu);
								Dialog_EtxtListeKategoriTarzAdi.setHint(getString(R.string.web_kategori_adi));
								Dialog_EtxtListeKategoriTarzAdi.requestFocus();
								imm.showSoftInput(Dialog_EtxtListeKategoriTarzAdi, 0);

								ADDialogListeKategoriTarzEkle.show();

								ADDialogListeKategoriTarzEkle.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										String KategoriAdi = Dialog_EtxtListeKategoriTarzAdi.getText().toString().trim();

										if(!Veritabani.KategoriVarmiKontrol(KategoriAdi)) {
											if(Veritabani.KategoriEkle(KategoriAdi)) {
												SnfKategoriler = Veritabani.lst_KategoriGetir(false);
												AdpKategoriler = new AdpKategori(activity, SnfKategoriler);

												Dialog_spnKategori.setAdapter(AdpKategoriler);

												Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
												Frg_TabRepKontrol.spnListeGetir();

												AkorDefterimSys.ToastMsj(activity, getString(R.string.web_kategori_eklendi2), Toast.LENGTH_SHORT);
											} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_kategori_eklenemedi2), Toast.LENGTH_SHORT);
										} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_kategori_eklenemedi3, KategoriAdi), Toast.LENGTH_SHORT);

										imm.hideSoftInputFromWindow(Dialog_EtxtListeKategoriTarzAdi.getWindowToken(), 0);
										ADDialogListeKategoriTarzEkle.dismiss();
									}
								});

								ADDialogListeKategoriTarzEkle.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										imm.hideSoftInputFromWindow(Dialog_EtxtListeKategoriTarzAdi.getWindowToken(), 0);
										ADDialogListeKategoriTarzEkle.dismiss();
									}
								});
							}
						});

						Dialog_btnKategoriSil = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnKategoriSil);
						Dialog_btnKategoriSil.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								final int KategoriID = SnfKategoriler.get(Dialog_spnKategori.getSelectedItemPosition()).getId();
								final String KategoriAdi = SnfKategoriler.get(Dialog_spnKategori.getSelectedItemPosition()).getKategoriAdi();

								ADDialogListeKategoriTarzSil = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_kategori_sil), getString(R.string.web_kategori_sil_soru, KategoriAdi), getString(R.string.web_sil), getString(R.string.iptal));
								ADDialogListeKategoriTarzSil.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialogListeKategoriTarzSil.show();

								ADDialogListeKategoriTarzSil.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(!Veritabani.KategoriyeAitSarkiVarmiKontrol(KategoriID)) {
											if(Veritabani.KategoriSil(KategoriID)) {
												SnfKategoriler = Veritabani.lst_KategoriGetir(false);
												AdpKategoriler = new AdpKategori(activity, SnfKategoriler);

												Dialog_spnKategori.setAdapter(AdpKategoriler);

												Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
												Frg_TabRepKontrol.spnListeGetir();

												AkorDefterimSys.ToastMsj(activity, getString(R.string.web_kategori_silindi2, KategoriAdi), Toast.LENGTH_SHORT);
											} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_kategori_silinemedi2), Toast.LENGTH_SHORT);
										} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_kategori_silinemedi3), Toast.LENGTH_SHORT);

										ADDialogListeKategoriTarzSil.dismiss();
									}
								});

								ADDialogListeKategoriTarzSil.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										ADDialogListeKategoriTarzSil.dismiss();
									}
								});
							}
						});



						// ##### Tarzlar #####

						TextView Dialog_lblTarz = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblTarz);
						Dialog_lblTarz.setTypeface(YaziFontu, Typeface.BOLD);

						SnfTarzlar = Veritabani.lst_TarzGetir(false);
						AdpTarzlar = new AdpTarz(activity, SnfTarzlar);

						Dialog_spnTarz = (Spinner) ViewDialogContent.findViewById(R.id.Dialog_spnTarz);
						Dialog_spnTarz.setAdapter(AdpTarzlar);

						Dialog_btnTarzEkle = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnTarzEkle);
						Dialog_btnTarzEkle.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
								ADDialogListeKategoriTarzEkle = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_tarz_ekle), ViewDialogContent, getString(R.string.ekle), getString(R.string.iptal), false);
								ADDialogListeKategoriTarzEkle.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

								final EditText Dialog_EtxtListeKategoriTarzAdi = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtListeKategoriTarzAdi);
								Dialog_EtxtListeKategoriTarzAdi.setTypeface(YaziFontu);
								Dialog_EtxtListeKategoriTarzAdi.setHint(getString(R.string.web_tarz_adi));
								Dialog_EtxtListeKategoriTarzAdi.requestFocus();
								imm.showSoftInput(Dialog_EtxtListeKategoriTarzAdi, 0);

								ADDialogListeKategoriTarzEkle.show();

								ADDialogListeKategoriTarzEkle.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										String TarzAdi = Dialog_EtxtListeKategoriTarzAdi.getText().toString().trim();

										if(!Veritabani.TarzVarmiKontrol(TarzAdi)) {
											if(Veritabani.TarzEkle(TarzAdi)) {
												SnfTarzlar = Veritabani.lst_TarzGetir(false);
												AdpTarzlar = new AdpTarz(activity, SnfTarzlar);

												Dialog_spnTarz.setAdapter(AdpTarzlar);

												Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
												Frg_TabRepKontrol.spnListeGetir();

												AkorDefterimSys.ToastMsj(activity, getString(R.string.web_tarz_eklendi2), Toast.LENGTH_SHORT);
											} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_tarz_eklenemedi2), Toast.LENGTH_SHORT);
										} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_tarz_eklenemedi3, TarzAdi), Toast.LENGTH_SHORT);

										imm.hideSoftInputFromWindow(Dialog_EtxtListeKategoriTarzAdi.getWindowToken(), 0);
										ADDialogListeKategoriTarzEkle.dismiss();
									}
								});

								ADDialogListeKategoriTarzEkle.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										imm.hideSoftInputFromWindow(Dialog_EtxtListeKategoriTarzAdi.getWindowToken(), 0);
										ADDialogListeKategoriTarzEkle.dismiss();
									}
								});
							}
						});

						Dialog_btnTarzSil = (Button) ViewDialogContent.findViewById(R.id.Dialog_btnTarzSil);
						Dialog_btnTarzSil.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								final int TarzID = SnfTarzlar.get(Dialog_spnTarz.getSelectedItemPosition()).getId();
								final String TarzAdi = SnfTarzlar.get(Dialog_spnTarz.getSelectedItemPosition()).getTarzAdi();

								ADDialogListeKategoriTarzSil = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.web_tarz_sil), getString(R.string.web_tarz_sil_soru, TarzAdi), getString(R.string.web_sil), getString(R.string.iptal));
								ADDialogListeKategoriTarzSil.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialogListeKategoriTarzSil.show();

								ADDialogListeKategoriTarzSil.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										if(!Veritabani.TarzaAitSarkiVarmiKontrol(TarzID)) {
											if(Veritabani.TarzSil(TarzID)) {
												SnfTarzlar = Veritabani.lst_TarzGetir(false);
												AdpTarzlar = new AdpTarz(activity, SnfTarzlar);

												Dialog_spnTarz.setAdapter(AdpTarzlar);

												Frg_TabRepKontrol Frg_TabRepKontrol = (Frg_TabRepKontrol) FragmentDataConn.SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
												Frg_TabRepKontrol.spnListeGetir();

												AkorDefterimSys.ToastMsj(activity, getString(R.string.web_tarz_silindi2, TarzAdi), Toast.LENGTH_SHORT);
											} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_tarz_silinemedi2), Toast.LENGTH_SHORT);
										} else AkorDefterimSys.ToastMsj(activity, getString(R.string.web_tarz_silinemedi3), Toast.LENGTH_SHORT);

										ADDialogListeKategoriTarzSil.dismiss();
									}
								});

								ADDialogListeKategoriTarzSil.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										ADDialogListeKategoriTarzSil.dismiss();
									}
								});
							}
						});








						ADDialogSarkiEkle.show();

						ADDialogSarkiEkle.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(SnfListeler.get(Dialog_spnListeler.getSelectedItemPosition()).getListeAdi().equals(getString(R.string.labeltire)))
									AkorDefterimSys.ToastMsj(activity, getString(R.string.liste_bulunamadi), Toast.LENGTH_SHORT);
								else if(SnfKategoriler.get(Dialog_spnKategori.getSelectedItemPosition()).getKategoriAdi().equals(getString(R.string.labeltire)))
									AkorDefterimSys.ToastMsj(activity, getString(R.string.kategori_bulunamadi), Toast.LENGTH_SHORT);
								else if(SnfTarzlar.get(Dialog_spnTarz.getSelectedItemPosition()).getTarzAdi().equals(getString(R.string.labeltire)))
									AkorDefterimSys.ToastMsj(activity, getString(R.string.tarz_bulunamadi), Toast.LENGTH_SHORT);
								else {
									/*Calendar mcurrentTime = Calendar.getInstance();
									String Gun, Ay, Yil, Saat, Dakika, Saniye, Tarih;

									Gun = String.valueOf(mcurrentTime.get(Calendar.DAY_OF_MONTH)); // Güncel Günü alıyoruz
									Ay = String.valueOf(mcurrentTime.get(Calendar.MONTH) + 1); // Güncel Ayı alıyoruz
									Yil = String.valueOf(mcurrentTime.get(Calendar.YEAR)); // Güncel Yılı alıyoruz
									Saat = String.valueOf(mcurrentTime.get(Calendar.HOUR_OF_DAY)); // Güncel Saati alıyoruz
									Dakika = String.valueOf(mcurrentTime.get(Calendar.MINUTE)); // Güncel Dakikayı alıyoruz
									Saniye = String.valueOf(mcurrentTime.get(Calendar.SECOND)); // Güncel Saniyeyi alıyoruz

									if (Gun.length() == 1) Gun = "0" + Gun;
									if (Ay.length() == 1) Ay = "0" + Ay;
									if (Yil.length() == 1) Yil = "0" + Yil;
									if (Saat.length() == 1) Saat = "0" + Saat;
									if (Dakika.length() == 1) Dakika = "0" + Dakika;
									if (Saniye.length() == 1) Saniye = "0" + Saniye;*/

									/*String Tarih;

									Tarih = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());*/

                            		/*if (TranspozeNo == 0) Transpoze = "0";
                            		else if (TranspozeNo > 0) Transpoze = "+" + TranspozeNo;
                            		else if (TranspozeNo < 0) Transpoze = String.valueOf(TranspozeNo);*/

									TranspozeNo = 0;

									if (Veritabani.SarkiEkle(SnfListeler.get(Dialog_spnListeler.getSelectedItemPosition()).getId(), SnfKategoriler.get(Dialog_spnKategori.getSelectedItemPosition()).getId(), SnfTarzlar.get(Dialog_spnTarz.getSelectedItemPosition()).getId(), SarkiAdi, SanatciAdi, SarkiIcerikTemp)) {
										//FABMenuGuncelle();

										AkorDefterimSys.ToastMsj(activity, getString(R.string.yerel_repertuvar_guncellendi), Toast.LENGTH_SHORT);
									} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

									ADDialogSarkiEkle.dismiss();
								}
							}
						});

						ADDialogSarkiEkle.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								TranspozeNo = 0;
								ADDialogSarkiEkle.dismiss();
							}
						});
					}
				} else if(FABListemeEkleCikar.getLabelText().equals(getString(R.string.listemden_sil))) {
					if (Veritabani.SarkiSil(SarkiID)) {
						FABMenuGuncelle();

						if(DepolamaAlani.equals("Cihaz")) {
							FragmentDataConn.FragmentSayfaGetir("anasayfa");
							FragmentDataConn.SarkiListesiDoldur();
							FragmentDataConn.SlidingIslem(2); //Sağ sliding aç
						}

						AkorDefterimSys.ToastMsj(activity, getString(R.string.yerel_repertuvar_guncellendi), Toast.LENGTH_SHORT);
					} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
				}

				break;
			case R.id.FABTranspozeArti:
				SarkiIcerikTemp = AkorDefterimSys.Transpoze("+1", SarkiIcerikTemp).toString();
				webview.loadDataWithBaseURL(null, "<html><body>" + SarkiIcerikTemp + "</body></html>", "text/html", "utf-8", null);
				break;
			case R.id.FABTranspozeEksi:
				SarkiIcerikTemp = AkorDefterimSys.Transpoze("-1", SarkiIcerikTemp).toString();
				webview.loadDataWithBaseURL(null, "<html><body>" + SarkiIcerikTemp + "</body></html>", "text/html", "utf-8", null);
				break;
			case R.id.FABAkorCetveli:
				AkorDefterimSys.AkorCetveli(activity, "A");
				break;
			case R.id.FABPaylas:
				activity.openContextMenu(FABPaylas);
				break;
		}
	}

	@SuppressLint("InflateParams")
	public class OnlineSarkiGetir extends AsyncTask<String, String, String> {
		String SarkiID, Transpoze;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDOnlineSarkiGetirDialog = new ProgressDialog(activity);
			PDOnlineSarkiGetirDialog.setMessage(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
			PDOnlineSarkiGetirDialog.setCancelable(false);
			PDOnlineSarkiGetirDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			SarkiID = parametre[0];
			Transpoze = parametre[1];

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("id", SarkiID));
			String sonuc = null;

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPSarkiGetir);
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
				if(Sonuc != null && !Sonuc.isEmpty()) {
					JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

					switch (JSONGelenVeri.getInt("sonuc")) {
						case 1:
							if(JSONGelenVeri.getString("aciklama").equals("şarkı bulundu")) {
								SarkiDoldur(JSONGelenVeri.getString("Icerik"), "0");
							}

							break;
						case 0:
							if(JSONGelenVeri.getString("aciklama").equals("sarkiadi alanı boş"))
								AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

							else if(JSONGelenVeri.getString("aciklama").equals("sanatciadi alanı boş"))
								AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

							else if(JSONGelenVeri.getString("aciklama").equals("şarkı bulunamadı"))
								AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

							else if(JSONGelenVeri.getString("aciklama").equals("hatalı işlem"))
								AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

							break;
					}
				} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

				if(AkorDefterimSys.ProgressDialogisShowing(PDOnlineSarkiGetirDialog))
					PDOnlineSarkiGetirDialog.dismiss();
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
			if(AkorDefterimSys.ProgressDialogisShowing(PDOnlineSarkiGetirDialog))
				PDOnlineSarkiGetirDialog.dismiss();
		}
	}

	public void SarkiEklemeDialogGuncelle() {
		if(ADDialogSarkiEkle.isShowing()) {
			// Burayı "Cevrimdisi" olarak belirledik. Çünkü Şarkı eklerken şarkı ekleme dialogundaki "Listeler" bölümünde "Akor Defterim" seçeneği çıkıyordu. Böylelikle bunu pasif yaptık..
			SnfListeler = Veritabani.lst_ListeGetir("Cevrimdisi");

			AdpListeler = new AdpListeler(activity, SnfListeler);
			Dialog_spnListeler.setAdapter(AdpListeler);

			SnfKategoriler = Veritabani.lst_KategoriGetir(false);

			AdpKategoriler = new AdpKategori(activity, SnfKategoriler);
			Dialog_spnKategori.setAdapter(AdpKategoriler);

			SnfTarzlar = Veritabani.lst_TarzGetir(false);

			AdpTarzlar = new AdpTarz(activity, SnfTarzlar);
			Dialog_spnTarz.setAdapter(AdpTarzlar);
		}
	}

	public boolean FABMenuDurum() {
		return FABMenu.isOpened();
	}

	public void FABKapatAc(Boolean Islem) {
		if(Islem) FABMenu.open(true);
		else if(!Islem) FABMenu.close(true);
	}
}