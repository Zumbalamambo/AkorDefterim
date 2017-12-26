package com.cnbcyln.app.akordefterim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelemeTipi;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListeler;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListelemeTipi;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class Frg_TabRepKontrol extends Fragment implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani Veritabani;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	SharedPreferences sharedPref;
	private List<SnfListeler> SnfListeler;
	private List<SnfKategoriler> SnfKategoriler;
	private List<SnfTarzlar> SnfTarzlar;
	private List<SnfListelemeTipi> SnfListelemeTipi;
	int SecilenListeID;
	ProgressDialog PDDialog;
	AlertDialog ADDialog_InternetBaglantisi;

	TextView lblListeler, lblKategori, lblTarz, lblListelemeTipi;
	Spinner spnListeler, spnKategori, spnTarz, spnListelemeTipi;
	Button btnListele;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_repkontrol, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = new AkorDefterimSys(activity);
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Interface_FragmentDataConn) activity;

		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, activity.MODE_PRIVATE);

		lblListeler = (TextView) activity.findViewById(R.id.lblListeler);
		lblListeler.setTypeface(YaziFontu, Typeface.BOLD);

		lblKategori = (TextView) activity.findViewById(R.id.lblKategori);
		lblKategori.setTypeface(YaziFontu, Typeface.BOLD);

		lblTarz = (TextView) activity.findViewById(R.id.lblTarz);
		lblTarz.setTypeface(YaziFontu, Typeface.BOLD);

		lblListelemeTipi = (TextView) activity.findViewById(R.id.lblListelemeTipi);
		lblListelemeTipi.setTypeface(YaziFontu, Typeface.BOLD);

		spnListeler = (Spinner) activity.findViewById(R.id.spnListeler);
		spnListeler.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SecilenListeID = SnfListeler.get(spnListeler.getSelectedItemPosition()).getId();

				if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
					//new KategoriTarzGetir().execute(String.valueOf(SecilenListeID), "Evet", "Evet");
				} else {
					if(SecilenListeID == 0) {
						if(SnfListeler.size() > 1) {
							spnListeler.setSelection(1);
							SecilenListeID = SnfListeler.get(spnListeler.getSelectedItemPosition()).getId();
						}
					} else {
						//new KategoriTarzGetir().execute(String.valueOf(SecilenListeID), "Evet", "Evet");
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spnKategori = (Spinner) activity.findViewById(R.id.spnKategori);
		spnTarz = (Spinner) activity.findViewById(R.id.spnTarz);
		spnListelemeTipi = (Spinner) activity.findViewById(R.id.spnListelemeTipi);

		btnListele = (Button) activity.findViewById(R.id.btnListele);
		btnListele.setOnClickListener(this);
		btnListele.setTypeface(YaziFontu);

		spnListeGetir();
		spnListelemeTipiGetir();
	}

	public void spnListeGetir() {
		SnfListeler = Veritabani.lst_ListeGetir(sharedPref.getString("prefOturumTipi", "Cevrimdisi"));

		AdpListeler AdpListeler = new AdpListeler(activity, SnfListeler);
		spnListeler.setAdapter(AdpListeler);
	}

	@SuppressWarnings("deprecation")
	private class KategoriTarzGetir extends AsyncTask<String, String, String> {
		int SecilenListeID;
		String TumuSecenegiKategori = "", TumuSecenegiTarz = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.lutfen_bekleyiniz));
			PDDialog.setCancelable(false);
			//PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			SecilenListeID = Integer.parseInt(String.valueOf(parametre[0]));
			TumuSecenegiKategori = String.valueOf(parametre[1]);
			TumuSecenegiTarz = String.valueOf(parametre[2]);
			String sonuc = null;

			try{
				JSONArray JSONGelenVeriArr;
				JSONObject JSONGelenVeri;
				HttpClient httpClient;
				HttpPost httpPost;
				HttpResponse response;
				HttpEntity entity;
				InputStream is;
				BufferedReader reader;
				StringBuilder sb;
				String line;
				Boolean HerIkiListeVarMi = true;

				switch (SecilenListeID) {
					case 0:
						httpClient = new DefaultHttpClient();
						httpPost = new HttpPost(AkorDefterimSys.PHPKategoriListesiGetir);
						response = httpClient.execute(httpPost);
						entity = response.getEntity();
						is = entity.getContent();
						reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
						sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line).append("\n");
						}

						sonuc = sb.toString();

						JSONGelenVeriArr = new JSONArray(sonuc);
						JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(0));

						if(JSONGelenVeri.getInt("sonuc") == 1 && JSONGelenVeri.getString("aciklama").equals("kategori bulundu")) {
							SnfKategoriler = new ArrayList<>();

							if (TumuSecenegiKategori.equals("Evet")) {
								SnfKategoriler Kategori = new SnfKategoriler();
								Kategori.setId(0);
								Kategori.setKategoriAdi(getString(R.string.tumu));
								SnfKategoriler.add(Kategori);
							}

							for (int i = 0; i < JSONGelenVeriArr.length(); i++) {
								JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(i));

								SnfKategoriler Kategori = new SnfKategoriler();
								Kategori.setId(Integer.valueOf(JSONGelenVeri.getString("id")));
								Kategori.setKategoriAdi(JSONGelenVeri.getString("KategoriAdi"));
								SnfKategoriler.add(Kategori);
							}
						} else {
							SnfKategoriler Kategori = new SnfKategoriler();
							Kategori.setId(0);
							Kategori.setKategoriAdi(getString(R.string.labeltire));
							SnfKategoriler.add(Kategori);

							HerIkiListeVarMi = false;
						}

						httpClient = new DefaultHttpClient();
						httpPost = new HttpPost(AkorDefterimSys.PHPTarzListesiGetir);
						response = httpClient.execute(httpPost);
						entity = response.getEntity();
						is = entity.getContent();
						reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
						sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line).append("\n");
						}

						sonuc = sb.toString();

						JSONGelenVeriArr = new JSONArray(sonuc);
						JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(0));

						if(JSONGelenVeri.getInt("sonuc") == 1 && JSONGelenVeri.getString("aciklama").equals("tarz bulundu")) {
							SnfTarzlar = new ArrayList<>();

							if (TumuSecenegiTarz.equals("Evet")) {
								SnfTarzlar Tarz = new SnfTarzlar();
								Tarz.setId(0);
								Tarz.setTarzAdi(getString(R.string.tumu));
								SnfTarzlar.add(Tarz);
							}

							for (int i = 0; i < JSONGelenVeriArr.length(); i++) {
								JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(i));

								SnfTarzlar Tarz = new SnfTarzlar();
								Tarz.setId(Integer.valueOf(JSONGelenVeri.getString("id")));
								Tarz.setTarzAdi(JSONGelenVeri.getString("TarzAdi"));
								SnfTarzlar.add(Tarz);
							}
						} else {
							SnfTarzlar Tarz = new SnfTarzlar();
							Tarz.setId(0);
							Tarz.setTarzAdi(getString(R.string.labeltire));
							SnfTarzlar.add(Tarz);

							HerIkiListeVarMi = false;
						}

						sonuc = "[{\"sonuc\":1,\"aciklama\":\"islem tamam\"}]";
						break;
					default:
						SnfKategoriler = Veritabani.lst_KategoriGetir(true);
						SnfTarzlar = Veritabani.lst_TarzGetir(true);

						sonuc = "[{\"sonuc\":1,\"aciklama\":\"islem tamam\"}]";
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
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
						if(JSONGelenVeri.getString("aciklama").equals("islem tamam")) {
							AdpKategori AdpKategoriler = new AdpKategori(activity, SnfKategoriler);
							spnKategori.setAdapter(AdpKategoriler);

							AdpTarz AdpTarzlar = new AdpTarz(activity, SnfTarzlar);
							spnTarz.setAdapter(AdpTarzlar);
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

	public void spnListelemeTipiGetir() {
		SnfListelemeTipi = new ArrayList<SnfListelemeTipi>();

		for (String LT : getResources().getStringArray(R.array.ListelemeTipi)) {
			SnfListelemeTipi ListelemeTipi = new SnfListelemeTipi();
			ListelemeTipi.setListelemeTipi(LT);
			SnfListelemeTipi.add(ListelemeTipi);
		}

		AdpListelemeTipi AdpListelemeTipi = new AdpListelemeTipi(activity, SnfListelemeTipi);
		spnListelemeTipi.setAdapter(AdpListelemeTipi);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.btnListele:
				if(SnfListeler.get(spnListeler.getSelectedItemPosition()).getListeAdi().equals(getString(R.string.labeltire)))
					AkorDefterimSys.ToastMsj(activity, getString(R.string.liste_bulunamadi), Toast.LENGTH_SHORT);
				else if(SnfKategoriler.get(spnKategori.getSelectedItemPosition()).getKategoriAdi().equals(getString(R.string.labeltire)))
					AkorDefterimSys.ToastMsj(activity, getString(R.string.kategori_bulunamadi), Toast.LENGTH_SHORT);
				else if(SnfTarzlar.get(spnTarz.getSelectedItemPosition()).getTarzAdi().equals(getString(R.string.labeltire)))
					AkorDefterimSys.ToastMsj(activity, getString(R.string.tarz_bulunamadi), Toast.LENGTH_SHORT);
				else {
					int SecilenListeID = SnfListeler.get(spnListeler.getSelectedItemPosition()).getId();
					int SecilenListelemeTipi = spnListelemeTipi.getSelectedItemPosition();
					int SecilenKategoriID = SnfKategoriler.get(spnKategori.getSelectedItemPosition()).getId();
					int SecilenTarzID = SnfTarzlar.get(spnTarz.getSelectedItemPosition()).getId();

					if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
						FragmentDataConn.SarkiListesiDoldur(SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenListelemeTipi);
					} else {
						if(SecilenListeID == 0) {
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
							FragmentDataConn.SarkiListesiDoldur(SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenListelemeTipi);
						}
					}
				}

				break;
		}
	}
}