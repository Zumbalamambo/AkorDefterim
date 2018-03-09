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
import com.cnbcyln.app.akordefterim.Siniflar.SnfTonlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import java.util.List;

public class AdpTonlar extends BaseAdapter {
	private Activity activity;
	private List<SnfTonlar> snfTonlar;
	private LayoutInflater inflater;
	private Typeface YaziFontu;

	public AdpTonlar(Activity activity, List<SnfTonlar> snfTonlar) {
		this.activity = activity;
		this.snfTonlar = snfTonlar;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return snfTonlar.size();
	}
	
	@Override
	public Object getItem(int position) {
		return snfTonlar.get(position);
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
			holder.LLTonAdiAkorAdi = satirView.findViewById(R.id.LLTonAdiAkorAdi);

            holder.txtTonAdiAkorAdi = satirView.findViewById(R.id.txtTonAdiAkorAdi);
            holder.txtTonAdiAkorAdi.setTypeface(YaziFontu, Typeface.NORMAL);
            satirView.setTag(holder);
	    } else holder = (ViewHolder) satirView.getTag();

		if(snfTonlar.get(position).getSecim()) {
			holder.LLTonAdiAkorAdi.setBackgroundColor(activity.getResources().getColor(R.color.KoyuMavi2));
			holder.txtTonAdiAkorAdi.setTextColor(activity.getResources().getColor(R.color.Beyaz));
		}
		else {
			holder.LLTonAdiAkorAdi.setBackgroundColor(activity.getResources().getColor(R.color.Beyaz));
			holder.txtTonAdiAkorAdi.setTextColor(activity.getResources().getColor(R.color.KoyuYazi));
		}
        
        holder.txtTonAdiAkorAdi.setText(snfTonlar.get(position).getTonAdi());
		
		return satirView;
	}

	public void setSelectable(int pos) {
		for(int i = 0; i <= snfTonlar.size() - 1; i++) {
			snfTonlar.get(i).setSecim(false);
		}

		snfTonlar.get(pos).setSecim(true);

		notifyDataSetChanged();
	}
}