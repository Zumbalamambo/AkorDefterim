package com.cnbcyln.app.akordefterim.Adaptorler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.CustomItemClickListener;
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
	Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private List<SnfAnasayfa> SanatciListesi;
	private CustomItemClickListener listener;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public static class ViewHolder extends RecyclerView.ViewHolder {
		ImageView ImgSanatciResim;
		TextView lblSanatciAdi;
		TextView lblToplamSarki;
		ImageView ImgMenu;

		public ViewHolder(View view) {
			super(view);

			ImgSanatciResim = view.findViewById(R.id.ImgSanatciResim);
			lblSanatciAdi = view.findViewById(R.id.lblSanatciAdi);
			lblToplamSarki = view.findViewById(R.id.lblToplamSarki);
			ImgMenu = view.findViewById(R.id.ImgMenu);
		}
	}

	public AdpAnasayfa(Activity activity, List<SnfAnasayfa> SanatciListesi) {
		this.activity = activity;
		this.AkorDefterimSys = com.cnbcyln.app.akordefterim.util.AkorDefterimSys.getInstance();
        this.AkorDefterimSys.activity = activity;
		this.SanatciListesi = SanatciListesi;
	}

	public AdpAnasayfa(Activity activity, List<SnfAnasayfa> SanatciListesi, CustomItemClickListener listener) {
		this.activity = activity;
		this.AkorDefterimSys = com.cnbcyln.app.akordefterim.util.AkorDefterimSys.getInstance();
        this.AkorDefterimSys.activity = activity;
		this.SanatciListesi = SanatciListesi;
		this.listener = listener;
	}

	@Override
	public AdpAnasayfa.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvanasayfa_satir, parent, false);
		final ViewHolder view_holder = new ViewHolder(v);

		/*v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onItemClick(v, view_holder.getPosition());
			}
		});*/

		view_holder.ImgMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		return view_holder;
	}

	@Override
	public void onBindViewHolder(AdpAnasayfa.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
		if(SanatciListesi.get(position).getSanatciResimVarMi()) {
			//AkorDefterimSys.NettenResimYukle mNettenResimYukle = new AkorDefterimSys.NettenResimYukle(holder.ImgSanatciResim);
			//mNettenResimYukle.execute(AkorDefterimSys.SanatciResimleriKlasoruURL + SanatciListesi.get(position).getSanatciID() + ".jpg");

			ImageLoader.getInstance().displayImage(AkorDefterimSys.CBCAPP_HttpsAdres + AkorDefterimSys.SanatciResimleriKlasoruDizini + SanatciListesi.get(position).getSanatciID() + ".jpg", holder.ImgSanatciResim, animateFirstListener);
		} else
			holder.ImgSanatciResim.setImageResource(activity.getResources().getIdentifier(activity.getPackageName() + ":drawable/bos_profil", null, null));

		holder.lblSanatciAdi.setText(SanatciListesi.get(position).getSanatciAdi());
		holder.lblToplamSarki.setText(String.valueOf(SanatciListesi.get(position).getToplamSarki()).concat(" şarkı"));

		holder.ImgMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupMenu(v, SanatciListesi.get(position).getSanatciID());
			}
		});
	}

	@Override
	public int getItemCount() {
		return SanatciListesi.size();
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

	private void showPopupMenu(View view, int SanatciID) {
		// inflate menu
		PopupMenu popup = new PopupMenu(activity, view);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.anasayfa_sanatci, popup.getMenu());
		popup.setOnMenuItemClickListener(new MyMenuItemClickListener(SanatciID));
		popup.show();
	}

	class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
		int SanatciID;

		MyMenuItemClickListener(int SanatciID) {
			this.SanatciID = SanatciID;
		}

		@Override
		public boolean onMenuItemClick(MenuItem menuItem) {
			switch (menuItem.getItemId()) {
				case R.id.action_son_eklenen_sarkiyi_ac:
					AkorDefterimSys.ToastMsj(activity, "Şarkı açıldı. Sanatçı ID:" + SanatciID, Toast.LENGTH_SHORT);
					return true;
				case R.id.action_sanatciya_ait_sarki_listesini_getir:

					AkorDefterimSys.ToastMsj(activity, "Liste getirildi. Sanatçı ID:" + SanatciID, Toast.LENGTH_SHORT);
					return true;
				default:
			}
			return false;
		}
	}
}