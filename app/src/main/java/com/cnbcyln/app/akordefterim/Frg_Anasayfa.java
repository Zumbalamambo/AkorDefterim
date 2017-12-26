package com.cnbcyln.app.akordefterim;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpAnasayfa;
import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.Siniflar.SnfAnasayfa;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.GridSpacingItemDecoration;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class Frg_Anasayfa extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
	
	Activity activity;
	AkorDefterimSys AkorDefterimSys;
	Veritabani Veritabani;
	SharedPreferences sharedPref;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	CoordinatorLayout coordinatorLayout;
	SwipeRefreshLayout SRLAnasayfa;
    RecyclerView RVAnasayfa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frg_anasayfa, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		activity = getActivity();
		AkorDefterimSys = new AkorDefterimSys(activity);
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Interface_FragmentDataConn) activity;

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);
		
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

        SRLAnasayfa = activity.findViewById(R.id.SRLAnasayfa);
        SRLAnasayfa.setOnRefreshListener(this);
        SRLAnasayfa.post(new Runnable() {
			@Override
			public void run() {
                SRLAnasayfa_ListeGuncelle();
			}
		});

		RecyclerView.LayoutManager mGridLayoutManager = new GridLayoutManager(activity, 2);

		RVAnasayfa = activity.findViewById(R.id.RVAnasayfa);
		RVAnasayfa.setLayoutManager(mGridLayoutManager);
		RVAnasayfa.addItemDecoration(new GridSpacingItemDecoration(2, AkorDefterimSys.dpToPx(10), true));
	}

	@Override
	public void onRefresh() {
        SRLAnasayfa_ListeGuncelle();
	}

	public void SRLAnasayfa_ListeGuncelle() {
		if(AkorDefterimSys.InternetErisimKontrolu()) { // İnternet bağlantısı var ise
			if(sharedPref.getString("prefHesapID", "").equals("")) { // Çevrimdışı ise
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.bu_sayfayi_cevrimdisi_kullanamazsiniz));
				SRLAnasayfa.setRefreshing(false);
			} else // Çevrimiçi ise
				new Anasayfa_ListeGuncelle().execute();
		} else { // İnternet bağlantısı yok ise
			SRLAnasayfa.setRefreshing(false);

			Snackbar snackbar = AkorDefterimSys.SnackBar(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi),
					R.color.Beyaz,
					R.color.TuruncuYazi,
					R.integer.SnackBar_GurunumSuresi_10);

			snackbar.setAction(getString(R.string.yeniden_dene), new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					SRLAnasayfa.setRefreshing(true);
					SRLAnasayfa_ListeGuncelle();
				}
			});

			snackbar.show();
		}
	}

	@SuppressLint({"InflateParams", "StaticFieldLeak"})
	private class Anasayfa_ListeGuncelle extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			SRLAnasayfa.setRefreshing(true);
		}

		@Override
		protected String doInBackground(String... parametre) {
			String sonuc = "";

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.AnasayfaURL);

				//httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null)
					sb.append(line).append("\n");

				sonuc = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
				SRLAnasayfa.setRefreshing(false);
			}

			return sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				JSONArray JSONGelenVeriArr = new JSONArray(Sonuc);
				JSONObject JSONGelenVeri;

				SRLAnasayfa.setRefreshing(false);

				List<SnfAnasayfa> snfAnasayfa = new ArrayList<>();

				for (int i = 0; i < JSONGelenVeriArr.length(); i++) {
					JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(i));

					SnfAnasayfa mSnfAnasayfa = new SnfAnasayfa();
					mSnfAnasayfa.setSanatciID(JSONGelenVeri.getInt("SanatciID"));
					mSnfAnasayfa.setSanatciResimVarMi(JSONGelenVeri.getBoolean("SanatciResimVarMi"));
					mSnfAnasayfa.setSanatciAdi(JSONGelenVeri.getString("SanatciAdi"));
					mSnfAnasayfa.setToplamSarki(JSONGelenVeri.getInt("ToplamSarki"));
					snfAnasayfa.add(mSnfAnasayfa);
				}

                /*AdpAnasayfa adpAnasayfa = new AdpAnasayfa(activity, snfAnasayfa, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        AkorDefterimSys.ToastMsj(activity, "Pozisyon: " + position + " / Sanatçı adı: " + snfAnasayfa.get(position).getSanatciAdi() + " / Toplam Şarkı: " + snfAnasayfa.get(position).getToplamSarki(), Toast.LENGTH_SHORT);
                    }
                });*/

				AdpAnasayfa adpAnasayfa = new AdpAnasayfa(activity, snfAnasayfa);

                RVAnasayfa.setHasFixedSize(true);
                RVAnasayfa.setAdapter(adpAnasayfa);
                RVAnasayfa.setItemAnimator(new DefaultItemAnimator());
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
            SRLAnasayfa.setRefreshing(false);
		}
	}

	public void AsyncTaskReturnValue(JSONObject JSONSonuc) {

	}

	/*public void SonEklenenSarkilar_ListeGuncelle() {
		SRLSonEklenenSarkilar.setRefreshing(true);

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(AkorDefterimSys.SonEklenenSarkilarURL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(AkorDefterimSys.SonEklenenSarkilarURL).build();
		RetrofitInterface RIC_SonEklenenSarkilar = restAdapter.create(RetrofitInterface.class);

		RIC_SonEklenenSarkilar.getJsonValues(new Callback<MdlSonEklenenSarkilar[]>() { // json array döneceği için modelimizi array tipinde belirledik
			@Override
			public void success(MdlSonEklenenSarkilar[] model, Response response) {
				snfAnasayfa = new ArrayList<>();

				for (MdlSonEklenenSarkilar modelValues : model) {
					SnfAnasayfa SonEklenenSarkilar = new SnfAnasayfa();
					SonEklenenSarkilar.setId(modelValues.Id);
					SonEklenenSarkilar.setSanatciAdi(modelValues.SanatciAdi);
					SonEklenenSarkilar.setSarkiAdi(modelValues.SarkiAdi);
					SonEklenenSarkilar.setKategoriAdi(modelValues.KategoriAdi);
					SonEklenenSarkilar.setTarzAdi(modelValues.TarzAdi);
					SonEklenenSarkilar.setEklenmeTarihi(modelValues.EklenmeTarihi);
					SonEklenenSarkilar.setEkleyenAdSoyad(modelValues.EkleyenAdSoyad);
					SonEklenenSarkilar.setGunFarki(modelValues.GunFarki);
					snfAnasayfa.add(SonEklenenSarkilar);
				}

				AdpAnasayfa = new AdpAnasayfa(activity, snfAnasayfa);
				lstSonEklenenSarkilar.setAdapter(AdpAnasayfa);

				SRLSonEklenenSarkilar.setRefreshing(false);
			}

			@Override
			public void failure(RetrofitError error) {
				SRLSonEklenenSarkilar.setRefreshing(false);

				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, error.getMessage());
			}
		});
	}*/
}