package com.cnbcyln.app.akordefterim.Adaptorler;

import java.util.List;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfGenelMenu;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdpGenelMenu extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<SnfGenelMenu> GenelMenu;
	private Typeface YaziFontu;

	public AdpGenelMenu(Activity activity, List<SnfGenelMenu> GenelMenu) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		AkorDefterimSys AkorDefterimSys = new AkorDefterimSys(activity);
		this.activity = activity;
		this.GenelMenu = GenelMenu;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
	}
	
	@Override
	public int getCount() {
		return GenelMenu.size();
	}
	
	@Override
	public Object getItem(int position) {
		return GenelMenu.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder{
		ImageView ImgMenuIcon;
        TextView txtMenuAdi;
    }

	@SuppressLint({ "ViewHolder", "InflateParams", "DefaultLocale" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View satirView = convertView;
        final ViewHolder holder;
        
        if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstgenelmenu_satir, null);
			
			holder = new ViewHolder();
			holder.ImgMenuIcon = satirView.findViewById(R.id.ImgMenuIcon);
            holder.txtMenuAdi = satirView.findViewById(R.id.txtMenuAdi);
            holder.txtMenuAdi.setTypeface(YaziFontu, Typeface.NORMAL);
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
        ResimDosyaAdi = ResimDosyaAdi.replace("ç", "c");*/
        
        holder.ImgMenuIcon.setImageResource(activity.getResources().getIdentifier(activity.getPackageName() + ":drawable/ic_" + GenelMenu.get(position).getGenelMenuSembolAdi() + "_icon",null,null));
        
        holder.txtMenuAdi.setText(GenelMenu.get(position).getGenelMenuAdi());
		
		return satirView;
	}
}