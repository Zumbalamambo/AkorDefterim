package com.cnbcyln.app.akordefterim.Adaptorler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.StringMatcher;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import java.util.List;

public class AdpSarkiListesiLST2 extends BaseAdapter implements SectionIndexer {
	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	Typeface YaziFontu;
	private LayoutInflater inflater;
	private StringBuilder mSections = new StringBuilder();
	private List<SnfSarkilar> snfSarkilar;
	private int ListelemeTipi;
	//private Int_DataConn_SarkiYonetimi int_DataConn_SarkiYonetimi;
	//private boolean Selectable = false;

	public AdpSarkiListesiLST2(Activity activity, List<SnfSarkilar> snfSarkilar, Boolean SiraliHarf, int ListelemeTipi) {
		this.activity = activity;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		veritabani = new Veritabani(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
		//int_DataConn_SarkiYonetimi = (Int_DataConn_SarkiYonetimi) activity;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.snfSarkilar = snfSarkilar;
		this.ListelemeTipi = ListelemeTipi;

		if (SiraliHarf) {
			mSections.append("#ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ");
		} else {
			mSections.append("#");

			switch (ListelemeTipi) {
				case 0:
					for (int i = 0; i < snfSarkilar.size(); i++) {
						String item = snfSarkilar.get(i).getSanatciAdi();
						String index = item.substring(0, 1).toUpperCase();

						if (mSections.indexOf(index) == -1)
							mSections.append(index);
					}
					break;
				case 1:
					for (int i = 0; i < snfSarkilar.size(); i++) {
						String item = snfSarkilar.get(i).getSarkiAdi();
						String index = item.substring(0, 1).toUpperCase();

						if (mSections.indexOf(index) == -1)
							mSections.append(index);
					}
					break;
				default:
					for (int i = 0; i < snfSarkilar.size(); i++) {
						String item = snfSarkilar.get(i).getSanatciAdi();
						String index = item.substring(0, 1).toUpperCase();

						if (mSections.indexOf(index) == -1)
							mSections.append(index);
					}
					break;
			}
		}
	}

	@Override
	public int getCount() {
		return snfSarkilar.size();
	}

	@Override
	public Object getItem(int position) {
		return snfSarkilar.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private String getItemChar(int position) {
		String Sonuc = "";

		if (ListelemeTipi == 0)
			Sonuc = String.valueOf(snfSarkilar.get(position).getSanatciAdi().charAt(0));
		else if (ListelemeTipi == 1)
			Sonuc = String.valueOf(snfSarkilar.get(position).getSarkiAdi().charAt(0));

		return Sonuc;
	}

	private class ViewHolder {
		CheckBox ChkSarkiSec;
		TextView lblSanatciSarkiAdi, lblKategoriAdi1, lblKategoriAdi2, lblTarzAdi1, lblTarzAdi2;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View satirView = convertView;
		ViewHolder holder;

		if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstreperttuvar_listesi_item2, null);

			holder = new ViewHolder();
			holder.ChkSarkiSec = satirView.findViewById(R.id.ChkSarkiSec);
			/*holder.ChkSarkiSec.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setSelectable(position);
				}
			});*/
			//holder.ChkSarkiSec.setTag(snfSarkilar.get(position));
			holder.lblSanatciSarkiAdi = satirView.findViewById(R.id.lblSanatciSarkiAdi);
			//holder.lblListeAdi1 = satirView.findViewById(R.id.lblListeAdi1);
			//holder.lblListeAdi2 = satirView.findViewById(R.id.lblListeAdi2);
			holder.lblKategoriAdi1 = satirView.findViewById(R.id.lblKategoriAdi1);
			holder.lblKategoriAdi2 = satirView.findViewById(R.id.lblKategoriAdi2);
			holder.lblTarzAdi1 = satirView.findViewById(R.id.lblTarzAdi1);
			holder.lblTarzAdi2 = satirView.findViewById(R.id.lblTarzAdi2);

			satirView.setTag(holder);
		} else {
			holder = (ViewHolder) satirView.getTag();
		}

		//holder.ChkSarkiSec.setVisibility(Selectable ? View.VISIBLE : View.GONE);

		/*if(snfSarkilar.get(position).getSecim() == null)
			holder.ChkSarkiSec.setChecked(false);
		else
			holder.ChkSarkiSec.setChecked(snfSarkilar.get(position).getSecim());*/

		switch (ListelemeTipi) {
			case 0:
				holder.lblSanatciSarkiAdi.setText(String.format("%s%s%s", snfSarkilar.get(position).getSanatciAdi(), " - ", snfSarkilar.get(position).getSarkiAdi()));
				break;
			case 1:
				holder.lblSanatciSarkiAdi.setText(String.format("%s%s%s", snfSarkilar.get(position).getSarkiAdi(), " - ", snfSarkilar.get(position).getSanatciAdi()));
				break;
			default:
				holder.lblSanatciSarkiAdi.setText(String.format("%s%s%s", snfSarkilar.get(position).getSanatciAdi(), " - ", snfSarkilar.get(position).getSarkiAdi()));
				break;
		}

		holder.lblSanatciSarkiAdi.setTypeface(YaziFontu, Typeface.NORMAL);

		//String SecilenListeAdi = veritabani.ListeAdiGetir(snfSarkilar.get(position).getListeID());
		String SecilenKategoriAdi = veritabani.KategoriAdiGetir(snfSarkilar.get(position).getKategoriID());
		String SecilenTarzAdi = veritabani.TarzAdiGetir(snfSarkilar.get(position).getTarzID());

		//holder.lblListeAdi1.setText(String.format("%s: ", activity.getString(R.string.liste)));
		//holder.lblListeAdi1.setTypeface(YaziFontu, Typeface.BOLD);

		//holder.lblListeAdi2.setText(SecilenListeAdi);
		//holder.lblListeAdi2.setTypeface(YaziFontu, Typeface.NORMAL);

		holder.lblKategoriAdi1.setText(String.format("%s: ", activity.getString(R.string.kategori)));
		holder.lblKategoriAdi1.setTypeface(YaziFontu, Typeface.BOLD);

		holder.lblKategoriAdi2.setText(SecilenKategoriAdi);
		holder.lblKategoriAdi2.setTypeface(YaziFontu, Typeface.NORMAL);

		holder.lblTarzAdi1.setText(String.format("%s: ", activity.getString(R.string.tarz)));
		holder.lblTarzAdi1.setTypeface(YaziFontu, Typeface.BOLD);

		holder.lblTarzAdi2.setText(SecilenTarzAdi);
		holder.lblTarzAdi2.setTypeface(YaziFontu, Typeface.NORMAL);

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

	/*private void setSelectable(int pos) {
		snfSarkilar.get(pos).SecimDegistir(); //Long Click yapıldığında şarkının secim özelliği mutlaka değişecek..
		//FragmentDataConn.SarkiSecimDegistir(pos);

		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelGuncelle(false);
		} else if(ToplamSecilenSarkiSayisi == 1) {
			if(!this.Selectable) this.Selectable = true;
			int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelGuncelle(true);
		}

		int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public void setFullSelectable() {
		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == snfSarkilar.size()) {
			for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
				if(snfSarkilar.get(i).getSecim()) snfSarkilar.get(i).SecimDegistir();
			}
		} else {
			for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
				if(!snfSarkilar.get(i).getSecim()) snfSarkilar.get(i).SecimDegistir();
			}
		}

		ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelGuncelle(false);
		}

		int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public void setFullNotSelectable() {
		for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
			if(snfSarkilar.get(i).getSecim()) snfSarkilar.get(i).SecimDegistir();
		}

		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelGuncelle(false);
		}

		int_DataConn_SarkiYonetimi.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public boolean isSelectable() {
		return Selectable;
	}

	private int ToplamSecilenSarkiSayisiGetir() {
		int ToplamSecilenSarki = 0;

		for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
			if(snfSarkilar.get(i).getSecim()) ToplamSecilenSarki++;
		}

		return ToplamSecilenSarki;
	}*/
}