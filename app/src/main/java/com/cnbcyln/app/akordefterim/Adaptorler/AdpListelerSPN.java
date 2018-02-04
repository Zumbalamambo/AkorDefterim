package com.cnbcyln.app.akordefterim.Adaptorler;

import java.util.ArrayList;
import java.util.List;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AdpListelerSPN extends BaseAdapter {
	private AkorDefterimSys AkorDefterimSys;
	private LayoutInflater inflater;
	private List<SnfListeler> SnfListeler;
	Typeface YaziFontu;

	public AdpListelerSPN(Activity activity, List<SnfListeler> Listeler) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		SnfListeler = Listeler;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return SnfListeler.size();
	}
	
	@Override
	public Object getItem(int position) {
		return SnfListeler.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		
		satirView = inflater.inflate(R.layout.spinner_satir, null); // create layout from
		
		TextView lblspinner_satir = satirView.findViewById(R.id.lblspinner_satir); // user name
		SnfListeler RepertuvarListeleri = SnfListeler.get(position);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(RepertuvarListeleri.getListeAdi());
		
		return satirView;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		
		satirView = inflater.inflate(R.layout.spinner_dropdown_satir, null); // create layout from
		
		TextView lblspinner_satir = satirView.findViewById(R.id.lblspinner_satir); // user name
		SnfListeler RepertuvarListeleri = SnfListeler.get(position);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(RepertuvarListeleri.getListeAdi());
		
		return satirView;
	}
}