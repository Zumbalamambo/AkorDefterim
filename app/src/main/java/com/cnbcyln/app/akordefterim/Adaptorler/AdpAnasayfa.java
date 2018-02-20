package com.cnbcyln.app.akordefterim.Adaptorler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfAnasayfa;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AdpAnasayfa extends RecyclerView.Adapter<AdpAnasayfa.ViewHolder> {
	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private List<SnfAnasayfa> snfAnasayfa;
	private Int_DataConn_AnaEkran FragmentDataConn;
	private Typeface YaziFontu;
	//private CustomItemClickListener listener;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public AdpAnasayfa(Activity activity, List<SnfAnasayfa> snfAnasayfa) {
		this.activity = activity;
		this.AkorDefterimSys = com.cnbcyln.app.akordefterim.util.AkorDefterimSys.getInstance();
		this.AkorDefterimSys.activity = activity;
		this.snfAnasayfa = snfAnasayfa;
		this.FragmentDataConn = (Int_DataConn_AnaEkran) activity;
		YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
	}

	/*public AdpAnasayfa(Activity activity, List<SnfAnasayfa> snfAnasayfa, CustomItemClickListener listener) {
		this.activity = activity;
		this.AkorDefterimSys = com.cnbcyln.app.akordefterim.util.AkorDefterimSys.getInstance();
        this.AkorDefterimSys.activity = activity;
		this.snfAnasayfa = snfAnasayfa;
		this.listener = listener;
	}*/

	public static class ViewHolder extends RecyclerView.ViewHolder {
		ImageView ImgSanatciResim;
		TextView lblSanatciAdi;
		TextView lblSonEklenenSarkiAdi;
		TextView lblToplamSarki;
		ImageView ImgMenu;

		public ViewHolder(View view) {
			super(view);

			ImgSanatciResim = view.findViewById(R.id.ImgSanatciResim);
			lblSanatciAdi = view.findViewById(R.id.lblSanatciAdi);
			lblSonEklenenSarkiAdi = view.findViewById(R.id.lblSonEklenenSarkiAdi);
			lblToplamSarki = view.findViewById(R.id.lblToplamSarki);
			ImgMenu = view.findViewById(R.id.ImgMenu);
		}
	}

	@Override
	public AdpAnasayfa.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvanasayfa_satir, parent, false);
		final ViewHolder view_holder = new ViewHolder(v);

		view_holder.ImgMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		return view_holder;
	}

	@Override
	public void onBindViewHolder(AdpAnasayfa.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
		if(snfAnasayfa.get(position).getSanatciResimVarMi()) {
			//AkorDefterimSys.NettenResimYukle mNettenResimYukle = new AkorDefterimSys.NettenResimYukle(holder.ImgSanatciResim);
			//mNettenResimYukle.execute(AkorDefterimSys.SanatciResimleriKlasoruURL + SanatciListesi.get(position).getSanatciID() + ".jpg");

			ImageLoader.getInstance().displayImage(AkorDefterimSys.SanatciResimleriKlasoruDizini + snfAnasayfa.get(position).getSanatciID() + ".jpg", holder.ImgSanatciResim, animateFirstListener);
		} else
			holder.ImgSanatciResim.setImageResource(activity.getResources().getIdentifier(activity.getPackageName() + ":drawable/bos_profil", null, null));

		holder.lblSanatciAdi.setText(snfAnasayfa.get(position).getSanatciAdi());
		holder.lblSanatciAdi.setTypeface(YaziFontu, Typeface.BOLD);

		holder.lblSonEklenenSarkiAdi.setText(snfAnasayfa.get(position).getSonEklenenSarkiAdi());
		holder.lblSonEklenenSarkiAdi.setTypeface(YaziFontu, Typeface.NORMAL);

		holder.lblToplamSarki.setText(String.format("%s %s %s", activity.getString(R.string.toplam), String.valueOf(snfAnasayfa.get(position).getToplamSarki()), activity.getString(R.string.sarki)));
		holder.lblToplamSarki.setTypeface(YaziFontu, Typeface.NORMAL);

		holder.ImgMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupMenu(v, snfAnasayfa.get(position).getSanatciID(), snfAnasayfa.get(position).getSanatciAdi(), snfAnasayfa.get(position).getSonEklenenSarkiID(), snfAnasayfa.get(position).getSonEklenenSarkiAdi());
			}
		});
	}

	@Override
	public int getItemCount() {
		return snfAnasayfa.size();
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private void showPopupMenu(View view, int SanatciID, String SanatciAdi, int SonEklenenSarkiID, String SonEklenenSarkiAdi) {
		// inflate menu
		PopupMenu popup = new PopupMenu(activity, view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.anasayfa_sanatci, popup.getMenu());
		popup.setOnMenuItemClickListener(new MyMenuItemClickListener(SanatciID, SanatciAdi, SonEklenenSarkiID, SonEklenenSarkiAdi));
		popup.show();
	}

	class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
		int SanatciID;
		String SanatciAdi;
		int SonEklenenSarkiID;
		String SonEklenenSarkiAdi;

		MyMenuItemClickListener(int SanatciID, String SanatciAdi, int SonEklenenSarkiID, String SonEklenenSarkiAdi) {
			this.SanatciID = SanatciID;
			this.SanatciAdi = SanatciAdi;
			this.SonEklenenSarkiID = SonEklenenSarkiID;
			this.SonEklenenSarkiAdi = SonEklenenSarkiAdi;
		}

		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.action_son_eklenen_sarkiyi_ac:
					if(AkorDefterimSys.InternetErisimKontrolu()) {
						FragmentDataConn.AnaEkranProgressIslemDialogAc(activity.getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
						AkorDefterimSys.SarkiGetir(null, 0, SonEklenenSarkiID, SanatciAdi, SonEklenenSarkiAdi);
					} else FragmentDataConn.StandartSnackBarMsj(activity.getString(R.string.internet_baglantisi_saglanamadi));
					return true;
				case R.id.action_sanatciya_ait_sarki_listesini_getir:
					if(AkorDefterimSys.InternetErisimKontrolu()) {
						FragmentDataConn.AnaEkranProgressIslemDialogAc(activity.getString(R.string.liste_indiriliyor_lutfen_bekleyiniz));
						AkorDefterimSys.SarkiListesiGetir(null, 0, 0, 0, 0, SanatciAdi);
					} else FragmentDataConn.StandartSnackBarMsj(activity.getString(R.string.internet_baglantisi_saglanamadi));
					return true;
				default:
			}
			return false;
		}
	}
}