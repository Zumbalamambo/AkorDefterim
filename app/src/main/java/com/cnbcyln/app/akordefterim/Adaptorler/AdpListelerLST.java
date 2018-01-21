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
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.StringMatcher;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import java.util.List;

public class AdpListelerLST extends BaseAdapter implements SectionIndexer {
	private Activity activity;
	private LayoutInflater inflater;
	private StringBuilder mSections = new StringBuilder();
	private List<SnfListeler> snfListeler;
	private Typeface YaziFontu;
	private Veritabani veritabani;

	public AdpListelerLST(Activity activity, List<SnfListeler> snfListeler, Boolean SiraliHarf) {
		this.activity = activity;
		this.snfListeler = snfListeler;

		if (SiraliHarf) mSections.append("#ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ");
		else {
			mSections.append("#");

			for (int i = 0; i < snfListeler.size(); i++) {
				String item = snfListeler.get(i).getListeAdi();
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
		return snfListeler.size();
	}

	@Override
	public Object getItem(int position) {
		return snfListeler.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private String getItemChar(int position) {
		return String.valueOf(snfListeler.get(position).getListeAdi().charAt(0));
	}

	private class ViewHolder {
		TextView lblListeAdi, lblToplamSarkiSayisi;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View satirView = convertView;
		ViewHolder holder;

		if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstlisteyonetimi_item, null);

			holder = new ViewHolder();
			holder.lblListeAdi = satirView.findViewById(R.id.lblListeAdi);
			holder.lblListeAdi.setTypeface(YaziFontu, Typeface.NORMAL);

			holder.lblToplamSarkiSayisi = satirView.findViewById(R.id.lblToplamSarkiSayisi);
			holder.lblToplamSarkiSayisi.setTypeface(YaziFontu, Typeface.NORMAL);

			satirView.setTag(holder);
		} else {
			holder = (ViewHolder) satirView.getTag();
		}

		holder.lblListeAdi.setText(snfListeler.get(position).getListeAdi());
		holder.lblToplamSarkiSayisi.setText(String.format("%s %s", String.valueOf(veritabani.ListeyeAitSarkiSayisiGetir(snfListeler.get(position).getId())), activity.getString(R.string.sarki)));

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