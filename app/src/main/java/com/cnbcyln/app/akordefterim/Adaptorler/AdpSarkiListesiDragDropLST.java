package com.cnbcyln.app.akordefterim.Adaptorler;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.R;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdpSarkiListesiDragDropLST extends DragItemAdapter<Pair<Long, String>, AdpSarkiListesiDragDropLST.ViewHolder> {
    private Activity activity;
    private AkorDefterimSys AkorDefterimSys;
    private Veritabani veritabani;
    private Typeface YaziFontu;

    private List<SnfSarkilar> snfSarkilar;

    private int SecilenListelemeTipi;
    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private boolean DuzenlenebilirMi = false;
    private boolean AramaPanelAcikMi = false;

    public AdpSarkiListesiDragDropLST(Activity activity, List<SnfSarkilar> snfSarkilar, ArrayList<Pair<Long, String>> SarkiListesi, int SecilenListelemeTipi, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        this.activity = activity;
        this.AkorDefterimSys = AkorDefterimSys.getInstance();
        this.AkorDefterimSys.activity = activity;
        veritabani = new Veritabani(activity);

        this.snfSarkilar = snfSarkilar;

        this.SecilenListelemeTipi = SecilenListelemeTipi;
        this.mLayoutId = layoutId;
        this.mGrabHandleId = grabHandleId;
        this.mDragOnLongPress = dragOnLongPress;

        YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
        setItemList(SarkiListesi);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

        int KategoriID = snfSarkilar.get(position).getKategoriID();
        int TarzID = snfSarkilar.get(position).getTarzID();
        final int SarkiID = snfSarkilar.get(position).getId();
        final String SanatciAdi = snfSarkilar.get(position).getSanatciAdi();
        final String SarkiAdi = snfSarkilar.get(position).getSarkiAdi();
        final String VideoURL = snfSarkilar.get(position).getVideoURL();

        if(isDuzenlenebilirMi())
            AkorDefterimSys.AnimasyonFadeIn(activity, holder.LLSecim, null);
        else
            AkorDefterimSys.AnimasyonFadeOut(activity, holder.LLSecim, null);

        if(snfSarkilar.get(position).getSecim())
            holder.ImgChkSecimIcon.setImageDrawable(new IconicsDrawable(activity).icon(CommunityMaterial.Icon.cmd_check_circle_outline));
        else
            holder.ImgChkSecimIcon.setImageDrawable(new IconicsDrawable(activity).icon(CommunityMaterial.Icon.cmd_checkbox_blank_circle_outline));

        holder.ImgChkSecimIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDuzenlenebilirMi()) {
                    snfSarkilar.get(position).SecimDegistir();

                    int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

                    if(ToplamSecilenSarkiSayisi == snfSarkilar.size())
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Dolu\"}");
                    else if(ToplamSecilenSarkiSayisi < snfSarkilar.size() && ToplamSecilenSarkiSayisi > 0)
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"YariDolu\"}");
                    else
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Boş\"}");

                    notifyDataSetChanged();
                }
            }
        });

        holder.LLIcerik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDuzenlenebilirMi()) {
                    snfSarkilar.get(position).SecimDegistir();

                    int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

                    if(ToplamSecilenSarkiSayisi == snfSarkilar.size())
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Dolu\"}");
                    else if(ToplamSecilenSarkiSayisi < snfSarkilar.size() && ToplamSecilenSarkiSayisi > 0)
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"YariDolu\"}");
                    else
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Boş\"}");

                    notifyDataSetChanged();
                } else AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SarkiDuzenleEkranGetir\", \"SecilenSarkiID\":" + SarkiID + ", \"SecilenSanatciAdi\":\"" + SanatciAdi + "\", \"SecilenSarkiAdi\":\"" + SarkiAdi + "\", \"SecilenSarkiVideoURL\":\"" + VideoURL + "\"}");
            }
        });

        holder.LLIcerik.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!isDuzenlenebilirMi()) {
                    snfSarkilar.get(position).SecimDegistir();

                    int ToplamSecilenSarkiSayisi = ToplamSecilenSarkiSayisiGetir();

                    if(ToplamSecilenSarkiSayisi == snfSarkilar.size())
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Dolu\"}");
                    else if(ToplamSecilenSarkiSayisi < snfSarkilar.size() && ToplamSecilenSarkiSayisi > 0)
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"YariDolu\"}");
                    else
                        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Boş\"}");

                    AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"DuzenlemeyiAc\"}");

                    notifyDataSetChanged();
                }

                return true;
            }
        });

        //String SanatciSarkiAdi = mItemList.get(position).second;
        holder.lblSanatciSarkiAdi.setText(String.format("%s - %s", SanatciAdi, SarkiAdi));
        holder.lblKategoriAdi1.setText(String.format("%s: ", activity.getString(R.string.kategori)));
        holder.lblKategoriAdi2.setText(veritabani.KategoriAdiGetir(KategoriID));
        holder.lblTarzAdi1.setText(String.format("%s: ", activity.getString(R.string.tarz)));
        holder.lblTarzAdi2.setText(veritabani.TarzAdiGetir(TarzID));

        if(!AramaPanelAcikMi) {
            if(isDuzenlenebilirMi() && SecilenListelemeTipi == 3)
                AkorDefterimSys.AnimasyonFadeIn(activity, holder.LLDragDrop, null);
            else
                AkorDefterimSys.AnimasyonFadeOut(activity, holder.LLDragDrop, null);
        } else holder.LLDragDrop.setVisibility(View.GONE);

        holder.itemView.setTag(mItemList.get(position));
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    public int ToplamSecilenSarkiSayisiGetir() {
        int ToplamSecilenSarki = 0;

        for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
            if(snfSarkilar.get(i).getSecim()) ToplamSecilenSarki++;
        }

        return ToplamSecilenSarki;
    }

    public void TumunuSecYadaSecme() {
        final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

        if(ToplamSecilenSarkiSayisiGetir() == snfSarkilar.size()) {
            for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
                snfSarkilar.get(i).SecimDegistir();
            }
        } else if(ToplamSecilenSarkiSayisiGetir() < snfSarkilar.size() && ToplamSecilenSarkiSayisiGetir() > 0) {
            for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
                if(!snfSarkilar.get(i).getSecim()) snfSarkilar.get(i).SecimDegistir();
            }
        } else {
            for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
                snfSarkilar.get(i).SecimDegistir();
            }
        }

        if(ToplamSecilenSarkiSayisiGetir() == snfSarkilar.size())
            AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Dolu\"}");
        else if(ToplamSecilenSarkiSayisiGetir() == 0)
            AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Boş\"}");

        notifyDataSetChanged();
    }

    public void TumSecimleriKaldir() {
        final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

        for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
            snfSarkilar.get(i).setSecim(false);
        }

        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"TumunuSecGuncelle\", \"Durum\":\"Boş\"}");

        notifyDataSetChanged();
    }

    public void SecilenKayitlariSil() {
        final Interface_AsyncResponse AsyncResponse = (Interface_AsyncResponse) activity;

        // Seçili olan şarkılar veritabanından siliniyor..
        for(int i = 0; i <= snfSarkilar.size() -  1; i++) {
            if(snfSarkilar.get(i).getSecim()) veritabani.SarkiSil(snfSarkilar.get(i).getId());
        }

        // Seçili olan şarkılar snfSarkilar sınıfından siliniyor..
        int index = 0;
        for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
            if(snfSarkilar.get(index).getSecim()) {
                snfSarkilar.remove(index);
                index--;
            }

            index++;
        }

        // Geri kalan tüm şarkılar snfSarkilar sınıfında yeniden sıralanıyor..
        for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
            snfSarkilar.get(i).setSiralama(i);
        }

        // Geri kalan tüm şarkılar veritabanında yeniden sıralanıyor..
        for(int i = 0; i <= snfSarkilar.size() -  1; i++) {
            veritabani.SarkiSiraNoDegistir(snfSarkilar.get(i).getId(), i);
        }

        AsyncResponse.AsyncTaskReturnValue("{\"Islem\":\"SecilenKayitlarSilindi\"}");
    }

    public String SecilenSanatciAdiGetir() {
        String SecilenSanatciAdi = "";

        for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
            if(snfSarkilar.get(i).getSecim()) {
                SecilenSanatciAdi = snfSarkilar.get(i).getSanatciAdi();
                break;
            }
        }

        return SecilenSanatciAdi;
    }

    public String SecilenSarkiAdiGetir() {
        String SecilenSarkiAdi = "";

        for(int i = 0; i <= snfSarkilar.size() - 1; i++) {
            if(snfSarkilar.get(i).getSecim()) {
                SecilenSarkiAdi = snfSarkilar.get(i).getSarkiAdi();
                break;
            }
        }

        return SecilenSarkiAdi;
    }

    public void SarkilariOtomatikSirala() {
        for(int i = 0; i <= snfSarkilar.size() -  1; i++) {
            veritabani.SarkiSiraNoDegistir(snfSarkilar.get(i).getId(), i);
        }

        notifyDataSetChanged();
    }

    public boolean isDuzenlenebilirMi() {
        return DuzenlenebilirMi;
    }

    public void setDuzenlenebilirMi(boolean DuzenlenebilirMi, boolean AramaPanelAcikMi) {
        this.DuzenlenebilirMi = DuzenlenebilirMi;
        this.AramaPanelAcikMi = AramaPanelAcikMi;
        notifyDataSetChanged();
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        LinearLayout LLSecim, LLIcerik, LLDragDrop;
        ImageView ImgChkSecimIcon;
        TextView lblSanatciSarkiAdi, lblKategoriAdi1, lblKategoriAdi2, lblTarzAdi1, lblTarzAdi2;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            LLSecim = itemView.findViewById(R.id.LLSecim);
            //LLSecim.setAlpha(0);

            ImgChkSecimIcon = itemView.findViewById(R.id.ImgChkSecimIcon);

            LLIcerik = itemView.findViewById(R.id.LLIcerik);

            lblSanatciSarkiAdi = itemView.findViewById(R.id.lblSanatciSarkiAdi);
            lblSanatciSarkiAdi.setTypeface(YaziFontu, Typeface.NORMAL);

            lblKategoriAdi1 = itemView.findViewById(R.id.lblKategoriAdi1);
            lblKategoriAdi1.setTypeface(YaziFontu, Typeface.BOLD);

            lblKategoriAdi2 = itemView.findViewById(R.id.lblKategoriAdi2);
            lblKategoriAdi2.setTypeface(YaziFontu, Typeface.NORMAL);

            lblTarzAdi1 = itemView.findViewById(R.id.lblTarzAdi1);
            lblTarzAdi1.setTypeface(YaziFontu, Typeface.BOLD);

            lblTarzAdi2 = itemView.findViewById(R.id.lblTarzAdi2);
            lblTarzAdi2.setTypeface(YaziFontu, Typeface.NORMAL);

            LLDragDrop = itemView.findViewById(R.id.LLDragDrop);
            //LLDragDrop.setAlpha(0);
        }

        @Override
        public void onItemClicked(View view) {
            //Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            //Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}