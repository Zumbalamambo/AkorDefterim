package com.cnbcyln.app.akordefterim.Adaptorler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.StringMatcher;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import java.util.List;

public class AdpKategorilerLST extends BaseAdapter implements SectionIndexer {
	private Activity activity;
	private LayoutInflater inflater;
	private StringBuilder mSections = new StringBuilder();
	private List<SnfKategoriler> snfKategoriler;
	private Typeface YaziFontu;
	private Veritabani veritabani;

	public AdpKategorilerLST(Activity activity, List<SnfKategoriler> snfKategoriler, Boolean SiraliHarf) {
		this.activity = activity;
		this.snfKategoriler = snfKategoriler;

		if (SiraliHarf) mSections.append("#ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ");
		else {
			mSections.append("#");

			for (int i = 0; i < snfKategoriler.size(); i++) {
				String item = snfKategoriler.get(i).getKategoriAdi();
				String index = item.substring(0, 1);

				if (mSections.indexOf(index) == -1)
					mSections.append(index);
			}
		}

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
		veritabani = new Veritabani(activity);
	}

	@Override
	public int getCount() {
		return snfKategoriler.size();
	}

	@Override
	public Object getItem(int position) {
		return snfKategoriler.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private String getItemChar(int position) {
		return String.valueOf(snfKategoriler.get(position).getKategoriAdi().charAt(0));
	}

	private class ViewHolder {
		TextView lblKategoriAdi, lblToplamSarkiSayisi;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View satirView = convertView;
		ViewHolder holder;

		if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstkategoriyonetimi_item, null);

			holder = new ViewHolder();
			holder.lblKategoriAdi = satirView.findViewById(R.id.lblKategoriAdi);
			holder.lblKategoriAdi.setTypeface(YaziFontu, Typeface.NORMAL);

			holder.lblToplamSarkiSayisi = satirView.findViewById(R.id.lblToplamSarkiSayisi);
			holder.lblToplamSarkiSayisi.setTypeface(YaziFontu, Typeface.NORMAL);

			satirView.setTag(holder);
		} else {
			holder = (ViewHolder) satirView.getTag();
		}

		holder.lblKategoriAdi.setText(snfKategoriler.get(position).getKategoriAdi());
		holder.lblToplamSarkiSayisi.setText(String.format("%s %s", String.valueOf(veritabani.KategoriyeAitSarkiSayisiGetir(snfKategoriler.get(position).getId())), activity.getString(R.string.sarki)));

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
}