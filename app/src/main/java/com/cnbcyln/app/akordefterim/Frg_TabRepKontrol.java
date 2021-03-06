package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelemeTipi;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelerSPN;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListelemeTipi;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Frg_TabRepKontrol extends Fragment implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	Int_DataConn_AnaEkran FragmentDataConn;
	Typeface YaziFontu;
	SharedPreferences sharedPref;
	private List<SnfListeler> snfListeler;
	private List<SnfKategoriler> snfKategoriler;
	private List<SnfTarzlar> snfTarzlar;
	private List<SnfListelemeTipi> snfListelemeTipi;

	CoordinatorLayout coordinatorLayout;
	TextView lblListeler, lblKategoriler, lblTarzlar, lblListelemeTipi;
	Spinner spnListeler, spnKategoriler, spnTarzlar, spnListelemeTipi;
	Button btnListele;
	SpinKitView SKVKategoriler, SKVTarzlar;

	int SecilenListeID = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_repkontrol, container, false);
	}

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

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

		lblListeler = (TextView) activity.findViewById(R.id.lblListeler);
		lblListeler.setTypeface(YaziFontu, Typeface.BOLD);

		spnListeler = (Spinner) activity.findViewById(R.id.spnListeler);
		spnListeler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SecilenListeID = snfListeler.get(position).getId();

				spnListelemeTipiGetir();

				if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi")) {
					snfKategoriler = veritabani.SnfKategoriGetir(true);
					AdpKategori AdpKategoriler = new AdpKategori(activity, snfKategoriler);
					spnKategoriler.setAdapter(AdpKategoriler);

					snfTarzlar = veritabani.SnfTarzGetir(true);
					AdpTarz AdpTarzlar = new AdpTarz(activity, snfTarzlar);
					spnTarzlar.setAdapter(AdpTarzlar);

					btnListele.setEnabled(true);
				} else {
					if(SecilenListeID == 0) {
						if(AkorDefterimSys.InternetErisimKontrolu()) {
							SKVKategoriler.setVisibility(View.VISIBLE);
							SKVTarzlar.setVisibility(View.VISIBLE);
							btnListele.setEnabled(false);

							if(snfKategoriler != null) snfKategoriler.clear();
							else snfKategoriler = new ArrayList<>();

							if(snfTarzlar != null) snfTarzlar.clear();
							else snfTarzlar = new ArrayList<>();

							// Burada gelen bilgilerde geçikme olursa loader çalıştırıyoruz ve bilgiler gelene kadar loader'ı aktif ediyoruz. Bilgiler geldiği an loaderları görünmez yapıyoruz..
							new Thread(new Runnable() {
								public void run() {
									while(true){
										if(snfKategoriler.size() > 0 && snfTarzlar.size() > 0) {
											activity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													SKVKategoriler.setVisibility(View.GONE);
													SKVTarzlar.setVisibility(View.GONE);
													btnListele.setEnabled(true);
												}
											});

											break;
										}
									}
								}
							}).start();

							AkorDefterimSys.KategoriListesiGetir(getString(R.string.tumu));
							AkorDefterimSys.TarzListesiGetir(getString(R.string.tumu));
						} else {
							if(snfKategoriler != null) snfKategoriler.clear();
							else snfKategoriler = new ArrayList<>();

							if(snfTarzlar != null) snfTarzlar.clear();
							else snfTarzlar = new ArrayList<>();

							KategoriListesiGetir("[{\"id\":0,\"KategoriAdi\":\"\"}]");
							TarzListesiGetir("[{\"id\":0,\"TarzAdi\":\"\"}]");
							btnListele.setEnabled(false);
							FragmentDataConn.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
						}
					} else {
						snfKategoriler = veritabani.SnfKategoriGetir(true);
						AdpKategori AdpKategoriler = new AdpKategori(activity, snfKategoriler);
						spnKategoriler.setAdapter(AdpKategoriler);

						snfTarzlar = veritabani.SnfTarzGetir(true);
						AdpTarz AdpTarzlar = new AdpTarz(activity, snfTarzlar);
						spnTarzlar.setAdapter(AdpTarzlar);

						btnListele.setEnabled(true);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		lblKategoriler = (TextView) activity.findViewById(R.id.lblKategoriler);
		lblKategoriler.setTypeface(YaziFontu, Typeface.BOLD);

		SKVKategoriler = (SpinKitView) activity.findViewById(R.id.SKVKategoriler);
		SKVKategoriler.setVisibility(View.GONE);

		spnKategoriler = (Spinner) activity.findViewById(R.id.spnKategoriler);

		lblTarzlar = (TextView) activity.findViewById(R.id.lblTarzlar);
		lblTarzlar.setTypeface(YaziFontu, Typeface.BOLD);

		SKVTarzlar = (SpinKitView) activity.findViewById(R.id.SKVTarzlar);
		SKVTarzlar.setVisibility(View.GONE);

		spnTarzlar = (Spinner) activity.findViewById(R.id.spnTarzlar);

		lblListelemeTipi = (TextView) activity.findViewById(R.id.lblListelemeTipi);
		lblListelemeTipi.setTypeface(YaziFontu, Typeface.BOLD);

		spnListelemeTipi = (Spinner) activity.findViewById(R.id.spnListelemeTipi);

		btnListele = (Button) activity.findViewById(R.id.btnListele);
		btnListele.setTypeface(YaziFontu);
		btnListele.setOnClickListener(this);

		spnDoldur();
	}

	@Override
	public void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
	}

	public void spnListeGetir() {
		snfListeler = veritabani.SnfListeGetir(sharedPref.getString("prefOturumTipi", "Cevrimdisi"), false);
		AdpListelerSPN AdpListelerSPN = new AdpListelerSPN(activity, snfListeler);
		spnListeler.setAdapter(AdpListelerSPN);
	}

	public void KategoriListesiGetir(String JSONKategoriListesi) {
		try {
			JSONArray JSONGelenVeriArr = new JSONArray(JSONKategoriListesi);
			JSONObject JSONGelenVeri;

			for (int i = 0; i < JSONGelenVeriArr.length(); i++) {
				JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(i));

				SnfKategoriler Kategori = new SnfKategoriler();
				Kategori.setId(Integer.valueOf(JSONGelenVeri.getString("id")));
				Kategori.setKategoriAdi(JSONGelenVeri.getString("KategoriAdi"));
				snfKategoriler.add(Kategori);
			}

			AdpKategori AdpKategoriler = new AdpKategori(activity, snfKategoriler);
			spnKategoriler.setAdapter(AdpKategoriler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void TarzListesiGetir(String JSONTarzListesi) {
		try {
			JSONArray JSONGelenVeriArr = new JSONArray(JSONTarzListesi);
			JSONObject JSONGelenVeri;

			for (int i = 0; i < JSONGelenVeriArr.length(); i++) {
				JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(i));

				SnfTarzlar Tarz = new SnfTarzlar();
				Tarz.setId(Integer.valueOf(JSONGelenVeri.getString("id")));
				Tarz.setTarzAdi(JSONGelenVeri.getString("TarzAdi"));
				snfTarzlar.add(Tarz);
			}

			AdpTarz AdpTarzlar = new AdpTarz(activity, snfTarzlar);
			spnTarzlar.setAdapter(AdpTarzlar);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void spnListelemeTipiGetir() {
		snfListelemeTipi = new ArrayList<SnfListelemeTipi>();

		for (String LT : getResources().getStringArray(R.array.ListelemeTipi)) {
			SnfListelemeTipi ListelemeTipi = new SnfListelemeTipi();
			ListelemeTipi.setListelemeTipi(LT);
			snfListelemeTipi.add(ListelemeTipi);
		}

		if(SecilenListeID == 0) snfListelemeTipi.remove(snfListelemeTipi.size() - 1);

		AdpListelemeTipi AdpListelemeTipi = new AdpListelemeTipi(activity, snfListelemeTipi);
		spnListelemeTipi.setAdapter(AdpListelemeTipi);
	}

	public void spnDoldur() {
		spnListeGetir();
		spnListelemeTipiGetir();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnListele:
				if(snfListeler.get(spnListeler.getSelectedItemPosition()).getListeAdi().equals(getString(R.string.labeltire)))
					FragmentDataConn.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_bulunamadi));
				else if(snfKategoriler.get(spnKategoriler.getSelectedItemPosition()).getKategoriAdi().equals(getString(R.string.labeltire)))
					FragmentDataConn.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_bulunamadi));
				else if(snfTarzlar.get(spnTarzlar.getSelectedItemPosition()).getTarzAdi().equals(getString(R.string.labeltire)))
					FragmentDataConn.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_bulunamadi));
				else {
					int ListeID = snfListeler.get(spnListeler.getSelectedItemPosition()).getId();
					int KategoriID = snfKategoriler.get(spnKategoriler.getSelectedItemPosition()).getId();
					int TarzID = snfTarzlar.get(spnTarzlar.getSelectedItemPosition()).getId();
					int ListelemeTipi = spnListelemeTipi.getSelectedItemPosition();

					if(ListeID == 0) {
						if(AkorDefterimSys.InternetErisimKontrolu()) {
							// AnaEkran üzerinden Progress Dialog'u açıyoruz ve "Liste indiriliyor. Lütfen bekleyiniz.." mesajını gösteriyoruz..
							FragmentDataConn.AnaEkranProgressIslemDialogAc(getString(R.string.liste_indiriliyor_lutfen_bekleyiniz));
							AkorDefterimSys.SarkiListesiGetir(veritabani, ListeID, KategoriID, TarzID, ListelemeTipi, "");
							AkorDefterimSys.SonYapilanIslemGuncelle("online_repertuvar_listesi_listeleniyor", "[]");
						} else FragmentDataConn.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
					} else {
						// AnaEkran üzerinden Progress Dialog'u açıyoruz ve "Liste indiriliyor. Lütfen bekleyiniz.." mesajını gösteriyoruz..
						FragmentDataConn.AnaEkranProgressIslemDialogAc(getString(R.string.liste_indiriliyor_lutfen_bekleyiniz));
						AkorDefterimSys.SarkiListesiGetir(veritabani, ListeID, KategoriID, TarzID, ListelemeTipi, "");
						AkorDefterimSys.SonYapilanIslemGuncelle("yerel_repertuvar_listesi_listeleniyor", "[]");
					}
				}

				break;
		}
	}
}