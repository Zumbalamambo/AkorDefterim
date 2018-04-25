package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
public class Frg_Anasayfa extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
	
	Activity activity;
	AkorDefterimSys AkorDefterimSys;
	Veritabani Veritabani;
	SharedPreferences sharedPref;
	Int_DataConn_AnaEkran FragmentDataConn;
	Typeface YaziFontu;

	CoordinatorLayout coordinatorLayout_Anasayfa;
	ConstraintLayout CLYenidenDene;
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

		AkorDefterimSys.SonYapilanIslemGuncelle("anasayfa_ekranina_giris_yapildi", "[]");

		coordinatorLayout_Anasayfa = activity.findViewById(R.id.coordinatorLayout_Anasayfa);

		CLYenidenDene = activity.findViewById(R.id.CLYenidenDene);
		CLYenidenDene.setVisibility(View.GONE);
		CLYenidenDene.setOnClickListener(this);

        SRLAnasayfa = activity.findViewById(R.id.SRLAnasayfa);
        SRLAnasayfa.setOnRefreshListener(this);
        SRLAnasayfa.post(new Runnable() {
			@Override
			public void run() {
                SRLAnasayfa_ListeGuncelle();
			}
		});

        // Alttaki fonksiyonla ekran boyutunu alıyoruz. Dönen değer direk 1-2-3 şeklinde geldiği için işimize yaradı.
		// Direk spanCount değerine atadık..
		int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

		RecyclerView.LayoutManager mGridLayoutManager = new GridLayoutManager(activity, screenSize);

		RVAnasayfa = activity.findViewById(R.id.RVAnasayfa);
		RVAnasayfa.setLayoutManager(mGridLayoutManager);
		RVAnasayfa.addItemDecoration(new GridSpacingItemDecoration(screenSize, AkorDefterimSys.dpToPx(10), true));
	}

	@Override
	public void onStart() {
		super.onStart();
		AkorDefterimSys.activity = activity;
		AkorDefterimSys.SharePrefAyarlarınıUygula();
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
				CLYenidenDene.setVisibility(View.GONE);
				SRLAnasayfa.setVisibility(View.VISIBLE);
				SRLAnasayfa.setRefreshing(true);
				AkorDefterimSys.AnasayfaGetir();
			} else {
				CLYenidenDene.setVisibility(View.VISIBLE);
				SRLAnasayfa.setVisibility(View.GONE);
				SRLAnasayfa.setRefreshing(false);
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout_Anasayfa, getString(R.string.internet_baglantisi_saglanamadi));
			}
		} else {
			CLYenidenDene.setVisibility(View.GONE);
			SRLAnasayfa.setVisibility(View.GONE);
			SRLAnasayfa.setRefreshing(false);
		}
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
					mSnfAnasayfa.setSonEklenenSarkiVideoURL(JSONGelenVeri.getString("SonEklenenSarkiVideoURL"));
					mSnfAnasayfa.setSonEklenenSarkiID(JSONGelenVeri.getInt("SonEklenenSarkiID"));
					mSnfAnasayfa.setSonEklenenSarkiAdi(JSONGelenVeri.getString("SonEklenenSarkiAdi"));
					mSnfAnasayfa.setToplamSarki(JSONGelenVeri.getInt("ToplamSarki"));
					snfAnasayfa.add(mSnfAnasayfa);
				}

				AdpAnasayfa adpAnasayfa = new AdpAnasayfa(activity, snfAnasayfa);

				RVAnasayfa.setHasFixedSize(true);
				RVAnasayfa.setAdapter(adpAnasayfa);
				RVAnasayfa.setItemAnimator(new DefaultItemAnimator());

				CLYenidenDene.setVisibility(View.GONE);
				SRLAnasayfa.setVisibility(View.VISIBLE);

				AkorDefterimSys.SonYapilanIslemGuncelle("sayfa_yenilendi", "[]");
			} else {
				CLYenidenDene.setVisibility(View.VISIBLE);
				SRLAnasayfa.setVisibility(View.GONE);
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout_Anasayfa, getString(R.string.icerik_bulunamadi));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.CLYenidenDene:
				SRLAnasayfa_ListeGuncelle();
				break;
		}
	}
}