package com.cnbcyln.app.akordefterim.Adaptorler;

import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.util.StringMatcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

public class AdpSarkiListesi extends BaseAdapter implements SectionIndexer {
	private LayoutInflater inflater;
	private StringBuilder mSections = new StringBuilder();
	private List<SnfSarkilar> SnfSarkilar;
	private int ListelemeTipi;
	private Interface_FragmentDataConn FragmentDataConn;
	private boolean Selectable = false;

	public AdpSarkiListesi(Activity activity, List<SnfSarkilar> SnfSarkilar, Boolean SiraliHarf, int ListelemeTipi) {
		FragmentDataConn = (Interface_FragmentDataConn) activity;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.SnfSarkilar = SnfSarkilar;
		this.ListelemeTipi = ListelemeTipi;

		if (SiraliHarf) {
			mSections.append("#ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ");
		} else {
			mSections.append("#");

			for (int i = 0; i < SnfSarkilar.size(); i++) {
				String item = SnfSarkilar.get(i).getSanatciAdi();
				String index = item.substring(0, 1);

				if (mSections.indexOf(index) == -1)
					mSections.append(index);
			}
		}
	}

	@Override
	public int getCount() {
		return SnfSarkilar.size();
	}

	@Override
	public Object getItem(int position) {
		return SnfSarkilar.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private String getItemChar(int position) {
		String Sonuc = "";

		if (ListelemeTipi == 0)
			Sonuc = String.valueOf(SnfSarkilar.get(position).getSanatciAdi().charAt(0));
		else if (ListelemeTipi == 1)
			Sonuc = String.valueOf(SnfSarkilar.get(position).getSarkiAdi().charAt(0));

		return Sonuc;
	}

	private class ViewHolder {
		TextView lblsanatcisarkiadi;
		CheckBox ChkSarkiSec;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View satirView = convertView;
		ViewHolder holder;

		if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstreperttuvar_listesi_item, null);

			holder = new ViewHolder();
			holder.lblsanatcisarkiadi = (TextView) satirView.findViewById(R.id.lblsanatcisarkiadi);
			holder.ChkSarkiSec = (CheckBox) satirView.findViewById(R.id.ChkSarkiSec);
			holder.ChkSarkiSec.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setSelectable(position);
				}
			});

			satirView.setTag(holder);
			holder.ChkSarkiSec.setTag(SnfSarkilar.get(position));
		} else {
			holder = (ViewHolder) satirView.getTag();
		}

		holder.ChkSarkiSec.setVisibility(Selectable ? View.VISIBLE : View.GONE);

		if(SnfSarkilar.get(position).getSecim() == null)
			holder.ChkSarkiSec.setChecked(false);
		else
			holder.ChkSarkiSec.setChecked(SnfSarkilar.get(position).getSecim());

		switch (ListelemeTipi) {
		case 0:
			holder.lblsanatcisarkiadi.setText(SnfSarkilar.get(position).getSanatciAdi() + " - " + SnfSarkilar.get(position).getSarkiAdi());
			break;
		case 1:
			holder.lblsanatcisarkiadi.setText(SnfSarkilar.get(position).getSarkiAdi() + " - " + SnfSarkilar.get(position).getSanatciAdi());
			break;
		default:
			holder.lblsanatcisarkiadi.setText(SnfSarkilar.get(position).getSanatciAdi() + " - " + SnfSarkilar.get(position).getSarkiAdi());
			break;
		}

		return satirView;
	}

	@Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be selected
		
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(getItemChar(j), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(getItemChar(j), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}

		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

	public void setSelectable(int pos) {
		SnfSarkilar.get(pos).SecimDegistir(); //Long Click yapıldığında şarkının secim özelliği mutlaka değişecek..
		//FragmentDataConn.SarkiSecimDegistir(pos);

		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			FragmentDataConn.SarkiListesi_SecimPanelGuncelle(false);
		} else if(ToplamSecilenSarkiSayisi == 1) {
			if(!this.Selectable) this.Selectable = true;
			FragmentDataConn.SarkiListesi_SecimPanelGuncelle(true);
		}

		FragmentDataConn.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public void setFullSelectable() {
		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == SnfSarkilar.size()) {
			for(int i = 0; i <= SnfSarkilar.size() - 1; i++) {
				if(SnfSarkilar.get(i).getSecim()) SnfSarkilar.get(i).SecimDegistir();
			}
		} else {
			for(int i = 0; i <= SnfSarkilar.size() - 1; i++) {
				if(!SnfSarkilar.get(i).getSecim()) SnfSarkilar.get(i).SecimDegistir();
			}
		}

		ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			FragmentDataConn.SarkiListesi_SecimPanelGuncelle(false);
		}

		FragmentDataConn.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public void setFullNotSelectable() {
		for(int i = 0; i <= SnfSarkilar.size() - 1; i++) {
			if(SnfSarkilar.get(i).getSecim()) SnfSarkilar.get(i).SecimDegistir();
		}

		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			FragmentDataConn.SarkiListesi_SecimPanelGuncelle(false);
		}

		FragmentDataConn.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public boolean isSelectable() {
		return Selectable;
	}

	public int ToplamSecilenSarkiSayisiGetir() {
		int ToplamSecilenSarki = 0;

		for(int i = 0; i <= SnfSarkilar.size() - 1; i++) {
			if(SnfSarkilar.get(i).getSecim()) ToplamSecilenSarki++;
		}

		return ToplamSecilenSarki;
	}
}