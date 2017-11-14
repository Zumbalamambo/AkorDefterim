package com.cnbcyln.app.akordefterim.Adaptorler;

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
import com.cnbcyln.app.akordefterim.Siniflar.SnfUlkeKodlari;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import java.util.List;

public class AdpUlkeKodlari extends BaseAdapter {
	private AkorDefterimSys AkorDefterimSys;
	private LayoutInflater inflater;
	private List<SnfUlkeKodlari> snfUlkeKodlari;
	Typeface YaziFontu;

	public AdpUlkeKodlari(Activity activity, List<SnfUlkeKodlari> snfUlkeKodlari) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.snfUlkeKodlari = snfUlkeKodlari;
		AkorDefterimSys = new AkorDefterimSys(activity);
		
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return snfUlkeKodlari.size();
	}
	
	@Override
	public Object getItem(int position) {
		return snfUlkeKodlari.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;

		SnfUlkeKodlari snfUlkeKodlari = this.snfUlkeKodlari.get(position);
		
		satirView = inflater.inflate(R.layout.spinner_satir, null);
		
		TextView lblspinner_satir = (TextView) satirView.findViewById(R.id.lblspinner_satir);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText("+" + snfUlkeKodlari.getUlkeKodu());
		
		return satirView;
	}

	@SuppressLint("InflateParams") @Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		SnfUlkeKodlari snfUlkeKodlari = this.snfUlkeKodlari.get(position);
		
		satirView = inflater.inflate(R.layout.spinner_dropdown_satir, null);
		
		TextView lblspinner_satir = (TextView) satirView.findViewById(R.id.lblspinner_satir);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(snfUlkeKodlari.getUlkeAdi() + " (+" + snfUlkeKodlari.getUlkeKodu() + ")");
		
		return satirView;
	}
}