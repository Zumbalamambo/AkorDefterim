package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpAnasayfa;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.Siniflar.SnfAnasayfa;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.GridSpacingItemDecoration;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class Frg_Anasayfa extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
	
	Activity activity;
	AkorDefterimSys AkorDefterimSys;
	Veritabani Veritabani;
	SharedPreferences sharedPref;
	Int_DataConn_AnaEkran FragmentDataConn;
	Typeface YaziFontu;
	CoordinatorLayout coordinatorLayout_Anasayfa;
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
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Int_DataConn_AnaEkran) activity;

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);
		
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		coordinatorLayout_Anasayfa = activity.findViewById(R.id.coordinatorLayout_Anasayfa);

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
	public void onStart() {
		super.onStart();
		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRefresh() {
        SRLAnasayfa_ListeGuncelle();
	}

	public void SRLAnasayfa_ListeGuncelle() {
		if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimici")) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				SRLAnasayfa.setRefreshing(true);
				AkorDefterimSys.AnasayfaGetir();
			} else {
				SRLAnasayfa.setRefreshing(false);
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout_Anasayfa, getString(R.string.internet_baglantisi_saglanamadi));
			}
		} else SRLAnasayfa.setRefreshing(false);
	}

	public void AnasayfaDoldur(String Icerik) {
		try {
			SRLAnasayfa.setRefreshing(false);

			if(!Icerik.equals("")) {
				JSONArray JSONGelenVeriArr = new JSONArray(Icerik);
				JSONObject JSONGelenVeri;

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
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout_Anasayfa, getString(R.string.icerik_bulunamadi));
		} catch (JSONException e) {
			e.printStackTrace();
		}
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