package com.cnbcyln.app.akordefterim.Adaptorler;

import java.util.List;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdpTarz extends BaseAdapter {
	private AkorDefterimSys AkorDefterimSys;
	private LayoutInflater inflater;
	private List<SnfTarzlar> SnfTarzlar;
	Typeface YaziFontu;

	public AdpTarz(Activity activity, List<SnfTarzlar> Tarzlar) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.SnfTarzlar = Tarzlar;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;

		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
	}

	@Override
	public int getCount() {
		return SnfTarzlar.size();
	}

	@Override
	public Object getItem(int position) {
		return SnfTarzlar.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		SnfTarzlar Tarz = SnfTarzlar.get(position);

		satirView = inflater.inflate(R.layout.spinner_satir, null);

		TextView lblspinner_satir = (TextView) satirView.findViewById(R.id.lblspinner_satir);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(Tarz.getTarzAdi());

		return satirView;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
		SnfTarzlar Tarz = SnfTarzlar.get(position);

		satirView = inflater.inflate(R.layout.spinner_dropdown_satir, null);

		TextView lblspinner_satir = (TextView) satirView.findViewById(R.id.lblspinner_satir);
		lblspinner_satir.setTypeface(YaziFontu);
		lblspinner_satir.setText(Tarz.getTarzAdi());

		return satirView;
	}
}