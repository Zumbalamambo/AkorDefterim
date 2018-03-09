package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

@SuppressLint({ "HandlerLeak", "DefaultLocale" })
public class Frg_Istatistik extends Fragment implements OnChartValueSelectedListener {
	
	Activity activity;
	AkorDefterimSys AkorDefterimSys;
	Veritabani Veritabani;
	SharedPreferences sharedPref;
	Int_DataConn_AnaEkran FragmentDataConn;
	Typeface YaziFontu;

	TextView lblGenelOnlineIstatistik_Yazi, lblIstatistiklerBaslik, lblToplamKategori, lblToplamTarz, lblToplamListe, lblToplamSarki;
	PieChart mChartEnSonGoruntulenenSarkilar;

	Typeface mTfRegular;
	Typeface mTfLight;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_frg_istatistik, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		activity = getActivity();
		AkorDefterimSys = com.cnbcyln.app.akordefterim.util.AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		Veritabani = new Veritabani(activity);
		FragmentDataConn = (Int_DataConn_AnaEkran) activity;

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);
		
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
		mTfRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/anivers_regular.otf");
		mTfLight = Typeface.createFromAsset(activity.getAssets(), "fonts/anivers_regular.otf");

		lblGenelOnlineIstatistik_Yazi = activity.findViewById(R.id.lblGenelOnlineIstatistik_Yazi);
		lblGenelOnlineIstatistik_Yazi.setTypeface(YaziFontu);

		lblIstatistiklerBaslik = activity.findViewById(R.id.lblIstatistiklerBaslik);
		lblIstatistiklerBaslik.setTypeface(YaziFontu);

		lblToplamListe = activity.findViewById(R.id.lblToplamListe);
		lblToplamListe.setTypeface(YaziFontu);
		lblToplamListe.setText(getString(R.string.toplam_liste) + ": " + Integer.toString(Veritabani.ListeSayisiGetir()));

		lblToplamKategori = activity.findViewById(R.id.lblToplamKategori);
		lblToplamKategori.setTypeface(YaziFontu);
		lblToplamKategori.setText(getString(R.string.toplam_kategori) + ": " + Integer.toString(Veritabani.KategoriSayisiGetir()));
		
		lblToplamTarz = activity.findViewById(R.id.lblToplamTarz);
		lblToplamTarz.setTypeface(YaziFontu);
		lblToplamTarz.setText(getString(R.string.toplam_tarz) + ": " + Integer.toString(Veritabani.TarzSayisiGetir()));

		lblToplamSarki = activity.findViewById(R.id.lblToplamSarki);
		lblToplamSarki.setTypeface(YaziFontu);
		lblToplamSarki.setText(getString(R.string.toplam_sarki) + ": " + Integer.toString(Veritabani.SarkiSayisiGetir()));

		mChartEnSonGoruntulenenSarkilar = activity.findViewById(R.id.ChartEnSonGoruntulenenSarkilar);
		mChartEnSonGoruntulenenSarkilar.setUsePercentValues(true);
		mChartEnSonGoruntulenenSarkilar.getDescription().setEnabled(false);
		mChartEnSonGoruntulenenSarkilar.setExtraOffsets(5, 10, 5, 5);

		mChartEnSonGoruntulenenSarkilar.setDragDecelerationFrictionCoef(0.95f);

		mChartEnSonGoruntulenenSarkilar.setCenterTextTypeface(mTfLight);
		mChartEnSonGoruntulenenSarkilar.setCenterText(SpannableChartOrtaYaziBaslik("En Son Görütülenen Şarkılar"));

		mChartEnSonGoruntulenenSarkilar.setDrawHoleEnabled(true);
		mChartEnSonGoruntulenenSarkilar.setHoleColor(Color.WHITE);

		mChartEnSonGoruntulenenSarkilar.setTransparentCircleColor(Color.WHITE);
		mChartEnSonGoruntulenenSarkilar.setTransparentCircleAlpha(110);

		mChartEnSonGoruntulenenSarkilar.setHoleRadius(58f);
		mChartEnSonGoruntulenenSarkilar.setTransparentCircleRadius(61f);

		mChartEnSonGoruntulenenSarkilar.setDrawCenterText(true);

		mChartEnSonGoruntulenenSarkilar.setRotationAngle(0);
		// enable rotation of the chart by touch
		mChartEnSonGoruntulenenSarkilar.setRotationEnabled(true);
		mChartEnSonGoruntulenenSarkilar.setHighlightPerTapEnabled(true);

		// mChart.setUnit(" €");
		// mChart.setDrawUnitsInChart(true);

		// add a selection listener
		mChartEnSonGoruntulenenSarkilar.setOnChartValueSelectedListener(this);

		setData(4, 100, "EnSonGoruntulenenSarkilar");

		mChartEnSonGoruntulenenSarkilar.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mChart.spin(2000, 0, 360);

		Legend l = mChartEnSonGoruntulenenSarkilar.getLegend();
		l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
		l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
		l.setOrientation(Legend.LegendOrientation.VERTICAL);
		l.setDrawInside(false);
		l.setXEntrySpace(7f);
		l.setYEntrySpace(0f);
		l.setYOffset(0f);

		// entry label styling
		mChartEnSonGoruntulenenSarkilar.setEntryLabelColor(Color.WHITE);
		mChartEnSonGoruntulenenSarkilar.setEntryLabelTypeface(mTfRegular);
		mChartEnSonGoruntulenenSarkilar.setEntryLabelTextSize(12f);
	}

	@Override
	public void onStart() {
		super.onStart();
		AkorDefterimSys.activity = activity;
		AkorDefterimSys.SharePrefAyarlarınıUygula();
	}

	public void IstatistikGuncelle() {
		lblToplamListe.setText(getString(R.string.toplam_liste) + ": " + Integer.toString(Veritabani.ListeSayisiGetir()));
		lblToplamKategori.setText(getString(R.string.toplam_kategori) + ": " + Integer.toString(Veritabani.KategoriSayisiGetir()));
		lblToplamTarz.setText(getString(R.string.toplam_tarz) + ": " + Integer.toString(Veritabani.TarzSayisiGetir()));
		lblToplamSarki.setText(getString(R.string.toplam_sarki) + ": " + Integer.toString(Veritabani.SarkiSayisiGetir()));
	}

	private SpannableString SpannableChartOrtaYaziBaslik(String ChartBaslik) {
		SpannableString s = new SpannableString(ChartBaslik);
		s.setSpan(new RelativeSizeSpan(1.5f), 0, ChartBaslik.length(), 0);
		//s.setSpan(new StyleSpan(Typeface.NORMAL), UstYazi.length(), s.length(), 0);
		//s.setSpan(new ForegroundColorSpan(Color.GRAY), UstYazi.length(), s.length(), 0);
		//s.setSpan(new RelativeSizeSpan(.8f), UstYazi.length(), s.length(), 0);
		//s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
		//s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
		return s;
	}

	@SuppressWarnings("deprecation")
	private void setData(int count, float range, String ChartAdı) {

		String[] mParties = new String[0];

		switch (ChartAdı) {
			case "EnSonGoruntulenenSarkilar":
				mParties = new String[] {
						"Barış Manço - Dönence", "Murat Boz - Özledim", "Volkan Konak - Aleni", "Kıraç - Razısan Gel", "Sıla - Yan Benimle"
				};

				break;
		}

		ArrayList<PieEntry> entries = new ArrayList<>();

		// NOTE: The order of the entries when being added to the entries array determines their position around the center of
		// the chart.
		for (int i = 0; i < count ; i++) {
			entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
					mParties[i % mParties.length],
					getResources().getDrawable(R.drawable.ic_oyla_icon)));
		}

		PieDataSet dataSet = new PieDataSet(entries, "Election Results");

		dataSet.setDrawIcons(false);

		dataSet.setSliceSpace(3f);
		dataSet.setIconsOffset(new MPPointF(0, 40));
		dataSet.setSelectionShift(5f);

		// add a lot of colors

		ArrayList<Integer> colors = new ArrayList<>();

		for (int c : ColorTemplate.VORDIPLOM_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.JOYFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.COLORFUL_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.LIBERTY_COLORS)
			colors.add(c);

		for (int c : ColorTemplate.PASTEL_COLORS)
			colors.add(c);

		colors.add(ColorTemplate.getHoloBlue());

		dataSet.setColors(colors);
		//dataSet.setSelectionShift(0f);

		PieData data = new PieData(dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.WHITE);
		data.setValueTypeface(mTfLight);
		mChartEnSonGoruntulenenSarkilar.setData(data);

		// undo all highlights
		mChartEnSonGoruntulenenSarkilar.highlightValues(null);

		mChartEnSonGoruntulenenSarkilar.invalidate();
	}

	@Override
	public void onValueSelected(Entry e, Highlight h) {
		if (e == null)
			return;
		Log.i("VAL SELECTED",
				"Value: " + e.getY() + ", index: " + h.getX()
						+ ", DataSet index: " + h.getDataSetIndex());
	}

	@Override
	public void onNothingSelected() {
		Log.i("PieChart", "nothing selected");
	}
}