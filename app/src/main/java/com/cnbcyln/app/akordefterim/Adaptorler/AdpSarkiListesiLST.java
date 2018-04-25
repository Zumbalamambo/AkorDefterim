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

import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.util.StringMatcher;
import com.mikepenz.iconics.view.IconicsTextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdpSarkiListesiLST extends BaseAdapter implements SectionIndexer {
	private com.cnbcyln.app.akordefterim.util.AkorDefterimSys AkorDefterimSys;
	private Typeface YaziFontu;
	private LayoutInflater inflater;
	private StringBuilder mSections = new StringBuilder();
	private List<SnfSarkilar> SnfSarkilar;
	private int ListelemeTipi;
	private Int_DataConn_AnaEkran int_DataConn_AnaEkran;
	private boolean Selectable = false;

	public AdpSarkiListesiLST(Activity activity, List<SnfSarkilar> SnfSarkilar, Boolean SiraliHarf, int ListelemeTipi) {
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
		int_DataConn_AnaEkran = (Int_DataConn_AnaEkran) activity;

		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.SnfSarkilar = SnfSarkilar;
		this.ListelemeTipi = ListelemeTipi;

		if (SiraliHarf) {
			mSections.append("#ABCÇDEFGĞHIİJKLMNOÖPQRSŞTUÜVWXYZ");
		} else {
			mSections.append("#");

			switch (ListelemeTipi) {
				case 0:
					for (int i = 0; i < SnfSarkilar.size(); i++) {
						String item = SnfSarkilar.get(i).getSanatciAdi();
						String index = item.substring(0, 1);

						if (mSections.indexOf(index) == -1)
							mSections.append(index);
					}
					break;
				case 1:
					for (int i = 0; i < SnfSarkilar.size(); i++) {
						String item = SnfSarkilar.get(i).getSarkiAdi();
						String index = item.substring(0, 1);

						if (mSections.indexOf(index) == -1)
							mSections.append(index);
					}
					break;
				default:
					for (int i = 0; i < SnfSarkilar.size(); i++) {
						String item = SnfSarkilar.get(i).getSanatciAdi();
						String index = item.substring(0, 1);

						if (mSections.indexOf(index) == -1)
							mSections.append(index);
					}
					break;
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
		IconicsTextView lblSanatciSarkiadi;
		CheckBox ChkSarkiSec;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View satirView = convertView;
		ViewHolder holder;

		if (satirView == null) {
			satirView = inflater.inflate(R.layout.lstreperttuvar_listesi_item, null);

			holder = new ViewHolder();
			holder.lblSanatciSarkiadi = satirView.findViewById(R.id.lblSanatciSarkiadi);
			holder.ChkSarkiSec = satirView.findViewById(R.id.ChkSarkiSec);
			/*holder.ChkSarkiSec.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setSelectable(position);
				}
			});*/

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

		String SecilenSarkiVideoURL = SnfSarkilar.get(position).getVideoURL();

		switch (ListelemeTipi) {
			case 0:
				if(SecilenSarkiVideoURL == null || SecilenSarkiVideoURL.equals("") || SecilenSarkiVideoURL.equals("-"))
					holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
				else {
					try {
						URL VideoURL = new URL(SecilenSarkiVideoURL);

						if(VideoURL.getHost().equals("youtube.com") || VideoURL.getHost().equals("www.youtube.com") || VideoURL.getHost().equals("m.youtube.com")) {
							try {
								List<String> URLQueryList = AkorDefterimSys.splitQuery(VideoURL).get("v");

								if(URLQueryList != null && URLQueryList.size() > 0)
									holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), " {gmi-youtube-play}"));
								else
									holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
							} catch (Exception e) {
								e.printStackTrace();
								holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
							}
						} else holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), " {gmi-videocam}"));
					} catch (MalformedURLException e) {
						e.printStackTrace();
						holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
					}
				}

				break;
			case 1:
				if(SecilenSarkiVideoURL.equals("") || SecilenSarkiVideoURL.equals("-"))
					holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSarkiAdi(), " - ", SnfSarkilar.get(position).getSanatciAdi(), ""));
				else {
					try {
						URL VideoURL = new URL(SecilenSarkiVideoURL);

						if(VideoURL.getHost().equals("youtube.com") || VideoURL.getHost().equals("www.youtube.com") || VideoURL.getHost().equals("m.youtube.com")) {
							try {
								List<String> URLQueryList = AkorDefterimSys.splitQuery(VideoURL).get("v");

								if(URLQueryList != null && URLQueryList.size() > 0)
									holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSarkiAdi(), " - ", SnfSarkilar.get(position).getSanatciAdi(), " {gmi-youtube-play}"));
								else
									holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSarkiAdi(), " - ", SnfSarkilar.get(position).getSanatciAdi(), ""));
							} catch (Exception e) {
								e.printStackTrace();
								holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSarkiAdi(), " - ", SnfSarkilar.get(position).getSanatciAdi(), ""));
							}
						} else holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSarkiAdi(), " - ", SnfSarkilar.get(position).getSanatciAdi(), " {gmi-videocam}"));
					} catch (MalformedURLException e) {
						e.printStackTrace();
						holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSarkiAdi(), " - ", SnfSarkilar.get(position).getSanatciAdi(), ""));
					}
				}

				break;
			default:
				if(SecilenSarkiVideoURL.equals("") || SecilenSarkiVideoURL.equals("-"))
					holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
				else {
					try {
						URL VideoURL = new URL(SecilenSarkiVideoURL);

						if(VideoURL.getHost().equals("youtube.com") || VideoURL.getHost().equals("www.youtube.com") || VideoURL.getHost().equals("m.youtube.com")) {
							try {
								List<String> URLQueryList = AkorDefterimSys.splitQuery(VideoURL).get("v");

								if(URLQueryList != null && URLQueryList.size() > 0)
									holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), " {gmi-youtube-play}"));
								else
									holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
							} catch (Exception e) {
								e.printStackTrace();
								holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
							}
						} else holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), " {gmi-videocam}"));
					} catch (MalformedURLException e) {
						e.printStackTrace();
						holder.lblSanatciSarkiadi.setText(String.format("%s%s%s%s", SnfSarkilar.get(position).getSanatciAdi(), " - ", SnfSarkilar.get(position).getSarkiAdi(), ""));
					}
				}

				break;
		}

		holder.lblSanatciSarkiadi.setTypeface(YaziFontu, Typeface.NORMAL);

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

	private void setSelectable(int pos) {
		SnfSarkilar.get(pos).SecimDegistir(); //Long Click yapıldığında şarkının secim özelliği mutlaka değişecek..
		//FragmentDataConn.SarkiSecimDegistir(pos);

		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			int_DataConn_AnaEkran.SarkiListesi_SecimPanelGuncelle(false);
		} else if(ToplamSecilenSarkiSayisi == 1) {
			if(!this.Selectable) this.Selectable = true;
			int_DataConn_AnaEkran.SarkiListesi_SecimPanelGuncelle(true);
		}

		int_DataConn_AnaEkran.SarkiListesi_SecimPanelBilgiGuncelle();

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
			int_DataConn_AnaEkran.SarkiListesi_SecimPanelGuncelle(false);
		}

		int_DataConn_AnaEkran.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public void setFullNotSelectable() {
		for(int i = 0; i <= SnfSarkilar.size() - 1; i++) {
			if(SnfSarkilar.get(i).getSecim()) SnfSarkilar.get(i).SecimDegistir();
		}

		int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

		if(ToplamSecilenSarkiSayisi == 0) {
			if(this.Selectable) this.Selectable = false;
			int_DataConn_AnaEkran.SarkiListesi_SecimPanelGuncelle(false);
		}

		int_DataConn_AnaEkran.SarkiListesi_SecimPanelBilgiGuncelle();

		notifyDataSetChanged();
	}

	public boolean isSelectable() {
		return Selectable;
	}

	private int ToplamSecilenSarkiSayisiGetir() {
		int ToplamSecilenSarki = 0;

		for(int i = 0; i <= SnfSarkilar.size() - 1; i++) {
			if(SnfSarkilar.get(i).getSecim()) ToplamSecilenSarki++;
		}

		return ToplamSecilenSarki;
	}
}