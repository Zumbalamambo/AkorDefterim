package com.cnbcyln.app.akordefterim.Adaptorler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfAkorlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import java.util.List;

public class AdpAkorlar extends BaseAdapter {
	private Activity activity;
	private List<SnfAkorlar> snfAkorlar;
	private LayoutInflater inflater;
	private Typeface YaziFontu;

	public AdpAkorlar(Activity activity, List<SnfAkorlar> snfAkorlar) {
		this.activity = activity;
		this.snfAkorlar = snfAkorlar;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");


		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return snfAkorlar.size();
	}
	
	@Override
	public Object getItem(int position) {
		return snfAkorlar.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder{
		LinearLayout LLTonAdiAkorAdi;
		TextView txtTonAdiAkorAdi;
    }

	@SuppressLint({ "ViewHolder", "InflateParams", "DefaultLocale" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
        final ViewHolder holder;
        
        if (satirView == null) {
			satirView = inflater.inflate(R.layout.lsttonlarakorlar_satir, null);
			
			holder = new ViewHolder();
			holder.LLTonAdiAkorAdi = (LinearLayout) satirView.findViewById(R.id.LLTonAdiAkorAdi);

            holder.txtTonAdiAkorAdi = (TextView) satirView.findViewById(R.id.txtTonAdiAkorAdi);
            holder.txtTonAdiAkorAdi.setTypeface(YaziFontu, Typeface.NORMAL);
            satirView.setTag(holder);
	    } else holder = (ViewHolder) satirView.getTag();

		if(snfAkorlar.get(position).getSecimBG()) {
			holder.LLTonAdiAkorAdi.setBackgroundColor(activity.getResources().getColor(R.color.KoyuMavi2));

			if(snfAkorlar.get(position).getSecimYazi()) {
				holder.txtTonAdiAkorAdi.setTextColor(activity.getResources().getColor(R.color.TuruncuYazi));
				holder.txtTonAdiAkorAdi.setTypeface(YaziFontu, Typeface.BOLD);
			} else {
				holder.txtTonAdiAkorAdi.setTextColor(activity.getResources().getColor(R.color.Beyaz));
				holder.txtTonAdiAkorAdi.setTypeface(YaziFontu, Typeface.NORMAL);
			}
		} else {
			holder.LLTonAdiAkorAdi.setBackgroundColor(activity.getResources().getColor(R.color.Gri));
			holder.txtTonAdiAkorAdi.setTextColor(activity.getResources().getColor(R.color.KoyuYazi));
			holder.txtTonAdiAkorAdi.setTypeface(YaziFontu, Typeface.NORMAL);
		}

		holder.txtTonAdiAkorAdi.setText(snfAkorlar.get(position).getAkorAdi());
		
		return satirView;
	}

	public void setSelectableYazi(int pos) {
		for(int i = 0; i <= snfAkorlar.size() - 1; i++) {
			snfAkorlar.get(i).setSecimYazi(false);
		}

		snfAkorlar.get(pos).setSecimYazi(true);

		notifyDataSetChanged();
	}
}