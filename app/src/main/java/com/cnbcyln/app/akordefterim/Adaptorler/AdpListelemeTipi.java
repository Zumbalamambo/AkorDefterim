package com.cnbcyln.app.akordefterim.Adaptorler;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListelemeTipi;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

public class AdpListelemeTipi extends BaseAdapter {
	private AkorDefterimSys AkorDefterimSys;
	private LayoutInflater inflater;
	private List<SnfListelemeTipi> ListelemeTipi;
	Typeface YaziFontu;

	public AdpListelemeTipi(Activity activity, List<SnfListelemeTipi> ListelemeTip) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ListelemeTipi = ListelemeTip;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return ListelemeTipi.size();
	}
	
	@Override
	public Object getItem(int position) {
		return ListelemeTipi.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		
		satirView = inflater.inflate(R.layout.spinner_satir, null); // create layout from
		
		TextView lblspinner_satir = (TextView) satirView.findViewById(R.id.lblspinner_satir); // user name
		SnfListelemeTipi Listeleme = ListelemeTipi.get(position);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(Listeleme.getListelemeTipi());
		
		return satirView;
	}
	
	@SuppressLint("InflateParams") @Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		
		satirView = inflater.inflate(R.layout.spinner_dropdown_satir, null); // create layout from
		
		TextView lblspinner_satir = (TextView) satirView.findViewById(R.id.lblspinner_satir); // user name
		SnfListelemeTipi Listeleme = ListelemeTipi.get(position);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(Listeleme.getListelemeTipi());
		
		return satirView;
	}
}