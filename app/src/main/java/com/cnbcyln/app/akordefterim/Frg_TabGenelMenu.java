package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpGenelMenu;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.Siniflar.SnfGenelMenu;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
@SuppressWarnings("unused")
public class Frg_TabGenelMenu extends Fragment implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	ListView lstGenelMenu;
	private List<com.cnbcyln.app.akordefterim.Siniflar.SnfGenelMenu> SnfGenelMenu;
	String Fragment_SayfaTag = "Frg_Anasayfa";
	Int_DataConn_AnaEkran FragmentDataConn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_genelmenu, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		FragmentDataConn = (Int_DataConn_AnaEkran) activity;

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);
		//YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		lstGenelMenu = activity.findViewById(R.id.lstGenelMenu);

		String[] StrArrayGenelMenuListe = null;

		switch (sharedPref.getString("prefOturumTipi", "Cevrimdisi")){
			case "Cevrimici":
				StrArrayGenelMenuListe = getResources().getStringArray(R.array.GenelMenu_Cevrimici);
				break;
			case "Cevrimdisi":
				StrArrayGenelMenuListe = getResources().getStringArray(R.array.GenelMenu_Cevrimdisi);
				break;
		}

		SnfGenelMenu = new ArrayList<>();

		assert StrArrayGenelMenuListe != null;
		for (String GML : StrArrayGenelMenuListe) {
			SnfGenelMenu GenelMenu = new SnfGenelMenu();
			GenelMenu.setGenelMenuSembolAdi(GML.substring(0, GML.indexOf("_")));
			GenelMenu.setGenelMenuAdi(GML.substring(GML.indexOf("_") + 1, GML.length()));
			SnfGenelMenu.add(GenelMenu);
		}

		AdpGenelMenu AdpGenelMenu = new AdpGenelMenu(activity, SnfGenelMenu);
		lstGenelMenu.setAdapter(AdpGenelMenu);
		lstGenelMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
				String SayfaAdi = SnfGenelMenu.get(position).getGenelMenuSembolAdi();

				switch (SayfaAdi) {
					case "hesabim": // Hesabım
						FragmentDataConn.FragmentSayfaGetir(SayfaAdi);
						break;
					case "secenekler": // Seçenekler
						AkorDefterimSys.EkranGetir(new Intent(activity, Secenekler.class), "Slide");
						break;
					case "girisyap":
						FragmentDataConn.FragmentSayfaGetir(SayfaAdi);
						break;
					default:
						Fragment_SayfaTag = "Frg_" + SayfaAdi;
						FragmentDataConn.FragmentSayfaGetir(SayfaAdi);
						FragmentDataConn.SlidingIslem(0); //Sliding kapat
						break;
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "":

					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}