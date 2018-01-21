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
import com.cnbcyln.app.akordefterim.Siniflar.SnfIstekler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import java.util.List;

public class AdpIstekler extends BaseAdapter {
	private LayoutInflater inflater;
	private List<SnfIstekler> snfIstekler;
	private Typeface YaziFontu;

	public AdpIstekler(Activity activity, List<SnfIstekler> snfIstekler) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.snfIstekler = snfIstekler;
		YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return snfIstekler.size();
	}
	
	@Override
	public Object getItem(int position) {
		return snfIstekler.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder{
		//public ImageView ImgMenuIcon;
		TextView txtSanatciAdiSarkiAdi;
    }

	@SuppressLint({ "ViewHolder", "InflateParams", "DefaultLocale" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
        final ViewHolder holder;
        
        if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstistekler_satir, null);
			
			holder = new ViewHolder();
			//holder.ImgMenuIcon = (ImageView) satirView.findViewById(R.id.ImgMenuIcon);
            holder.txtSanatciAdiSarkiAdi = satirView.findViewById(R.id.txtSanatciAdiSarkiAdi);
            holder.txtSanatciAdiSarkiAdi.setTypeface(YaziFontu, Typeface.NORMAL);
            satirView.setTag(holder);
	    } else {
	    	holder = (ViewHolder) satirView.getTag();
	    }
        
        /*String ResimDosyaAdi = GenelMenu.get(position).getGenelMenuAdi().toLowerCase();
        ResimDosyaAdi = ResimDosyaAdi.replace(" ", "");
        ResimDosyaAdi = ResimDosyaAdi.replace("ı", "i");
        ResimDosyaAdi = ResimDosyaAdi.replace("ğ", "g");
        ResimDosyaAdi = ResimDosyaAdi.replace("ü", "u");
        ResimDosyaAdi = ResimDosyaAdi.replace("ş", "s");
        ResimDosyaAdi = ResimDosyaAdi.replace("ö", "o");
        ResimDosyaAdi = ResimDosyaAdi.replace("ç", "c");
        
        holder.ImgMenuIcon.setImageResource(activity.getResources().getIdentifier(activity.getPackageName() + ":drawable/ic_" + ResimDosyaAdi + "_icon",null,null));*/
        
        holder.txtSanatciAdiSarkiAdi.setText(snfIstekler.get(position).getSanatciAdi() + " - " + snfIstekler.get(position).getSarkiAdi());
		
		return satirView;
	}
}