package com.cnbcyln.app.akordefterim;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpIstekler;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.Siniflar.SnfIstekler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Frg_TabIstekler extends Fragment {

	Activity activity;
	Veritabani Veritabani;
	AkorDefterimSys AkorDefterimSys;
	Int_DataConn_AnaEkran FragmentDataConn;
	SharedPreferences sharedPref;
	Typeface YaziFontu;
	AdpIstekler AdpIstekler;

	TextView lblIstekListesiOrtaMesaj;
	ListView lstIstekListesi;

	private List<SnfIstekler> snfIstekler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_istekler, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Int_DataConn_AnaEkran) activity;

		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		lblIstekListesiOrtaMesaj = (TextView) activity.findViewById(R.id.lblIstekListesiOrtaMesaj);
		lblIstekListesiOrtaMesaj.setTypeface(YaziFontu);
		//lblIstekListesiOrtaMesaj.setText(getString(R.string.cok_yakinda));

		lstIstekListesi = (ListView) activity.findViewById(R.id.lstIstekListesi);
		lstIstekListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
				final AlertDialog ADDialogIstekDetay = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.istek_detay),
						getString(R.string.istek_detay_icerik, snfIstekler.get(position).getSanatciAdi(), snfIstekler.get(position).getSarkiAdi(), snfIstekler.get(position).getIstekTarihi(), snfIstekler.get(position).getClientAdSoyad(), snfIstekler.get(position).getClientNot()),
						getString(R.string.sil),
						getString(R.string.kapat));
				ADDialogIstekDetay.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialogIstekDetay.show();

				ADDialogIstekDetay.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialogIstekDetay.cancel();

						Veritabani.IstekSil(snfIstekler.get(position).getId());
						IstekListesiGetir();
					}
				});

				ADDialogIstekDetay.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialogIstekDetay.cancel();
					}
				});
			}
		});
		lstIstekListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


				return true;
			}
		});

		IstekListesiGetir();
	}

	@Override
	public void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;
	}

	public void IstekListesiGetir() {
		snfIstekler = new ArrayList<>();
		snfIstekler = Veritabani.lst_IstekGetir();

		if(snfIstekler.size() > 0) {
			AdpIstekler = new AdpIstekler(activity, snfIstekler);
			lstIstekListesi.setAdapter(AdpIstekler);

			lstIstekListesi.setVisibility(View.VISIBLE);
			lblIstekListesiOrtaMesaj.setVisibility(View.GONE);
		} else {
			lstIstekListesi.setVisibility(View.GONE);
			lblIstekListesiOrtaMesaj.setVisibility(View.VISIBLE);
			lblIstekListesiOrtaMesaj.setText(getString(R.string.liste_bos));
		}
	}
}