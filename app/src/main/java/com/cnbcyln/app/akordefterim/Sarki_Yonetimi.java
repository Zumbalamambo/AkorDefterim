package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelemeTipi;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelerSPN;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpSarkiListesiDragDropLST;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_SarkiYonetimi;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListelemeTipi;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.ClearableEditText;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.clans.fab.FloatingActionButton;
import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ALL")
public class Sarki_Yonetimi extends AppCompatActivity implements Interface_AsyncResponse, Int_DataConn_SarkiYonetimi, OnClickListener {
    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
    View ViewDialogContent;
    LayoutInflater inflater;
    InputMethodManager imm;

    CoordinatorLayout coordinatorLayout;
    ConstraintLayout CLContainer;
    RelativeLayout RLSarkiYonetimi_AnaPanel, RLSarkiYonetimi_AramaPanel, RLSarkiYonetimi_DuzenlePanel, RLContent;
    LinearLayout LLFiltreMenu;
	ImageButton btnGeri_AnaPanel, btnFiltre_AnaPanel, btnAra_AnaPanel, btnMenu_AnaPanel, btnGeri_AramaPanel, btnGeri_DuzenlePanel, btnMenu_DuzenlePanel;
	Button btnListele;
    DragListView lstSarkiYonetimi;
	TextView lblBaslik_AnaPanel, lblchkTumunuSec_DuzenlePanel, lblListeAdi, lblOrtaMesaj, lblFiltre, lblListeler, lblKategoriler, lblTarzlar, lblListelemeTipi;
	Spinner spnListeler, spnKategoriler, spnTarzlar, spnListelemeTipi;
    ClearableEditText txtAra_AramaPanel;
    FloatingActionButton FABSarkiEkle;

    private List<SnfSarkilar> snfSarkilar;
    private List<SnfSarkilar> snfSarkilarTemp;
    private List<SnfListeler> snfListeler;
    private List<SnfKategoriler> snfKategoriler;
    private List<SnfTarzlar> snfTarzlar;
    private List<SnfListelemeTipi> snfListelemeTipi;
    AdpSarkiListesiDragDropLST adpSarkiListesiDragDropLST;

    int SecilenListeID = 0, SecilenKategoriID = 0, SecilenTarzID = 0, SecilenListelemeTipi = 0;
    Boolean FiltreMenuAcikMi = false, DuzenlenebilirMi = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sarki_yonetimi);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
		veritabani = new Veritabani(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
        inflater = activity.getLayoutInflater();

        sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

        CLContainer = findViewById(R.id.CLContainer);
        CLContainer.setOnClickListener(this);

        RLSarkiYonetimi_AnaPanel = findViewById(R.id.RLSarkiYonetimi_AnaPanel);
        RLSarkiYonetimi_AnaPanel.setVisibility(View.VISIBLE);

        btnGeri_AnaPanel = findViewById(R.id.btnGeri_AnaPanel);
        btnGeri_AnaPanel.setOnClickListener(this);

		lblBaslik_AnaPanel = findViewById(R.id.lblBaslik_AnaPanel);
		lblBaslik_AnaPanel.setTypeface(YaziFontu, Typeface.BOLD);

        btnFiltre_AnaPanel = findViewById(R.id.btnFiltre_AnaPanel);
        btnFiltre_AnaPanel.setOnClickListener(this);

        btnAra_AnaPanel = findViewById(R.id.btnAra_AnaPanel);
        btnAra_AnaPanel.setOnClickListener(this);

        btnMenu_AnaPanel = findViewById(R.id.btnMenu_AnaPanel);
        btnMenu_AnaPanel.setOnClickListener(this);

        RLSarkiYonetimi_AramaPanel = findViewById(R.id.RLSarkiYonetimi_AramaPanel);
        RLSarkiYonetimi_AramaPanel.setVisibility(View.GONE);

        btnGeri_AramaPanel = findViewById(R.id.btnGeri_AramaPanel);
        btnGeri_AramaPanel.setOnClickListener(this);

        txtAra_AramaPanel = findViewById(R.id.txtAra_AramaPanel);
        txtAra_AramaPanel.setTypeface(YaziFontu, Typeface.NORMAL);
        txtAra_AramaPanel.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (snfSarkilar != null) {
                    snfSarkilarTemp = new ArrayList<>();
                    int textlength = txtAra_AramaPanel.getText().length();
                    snfSarkilarTemp.clear();

                    for (int i = 0; i < snfSarkilar.size(); i++) {
                        if (textlength <= snfSarkilar.get(i).getSanatciAdi().length() || textlength <= snfSarkilar.get(i).getSarkiAdi().length()) {
                            if (snfSarkilar.get(i).getSanatciAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfSarkilar SarkilarTemp = new SnfSarkilar();
                                SarkilarTemp.setId(snfSarkilar.get(i).getId());
                                SarkilarTemp.setListeID(snfSarkilar.get(i).getListeID());
                                SarkilarTemp.setKategoriID(snfSarkilar.get(i).getKategoriID());
                                SarkilarTemp.setTarzID(snfSarkilar.get(i).getTarzID());
                                SarkilarTemp.setSanatciAdi(snfSarkilar.get(i).getSanatciAdi());
                                SarkilarTemp.setSarkiAdi(snfSarkilar.get(i).getSarkiAdi());
                                SarkilarTemp.setVideoURL(snfSarkilar.get(i).getVideoURL());
                                SarkilarTemp.setEklenmeTarihi(snfSarkilar.get(i).getEklenmeTarihi());
                                SarkilarTemp.setDuzenlenmeTarihi(snfSarkilar.get(i).getDuzenlenmeTarihi());
                                snfSarkilarTemp.add(SarkilarTemp);
                            } else if (snfSarkilar.get(i).getSarkiAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfSarkilar SarkilarTemp = new SnfSarkilar();
                                SarkilarTemp.setId(snfSarkilar.get(i).getId());
                                SarkilarTemp.setListeID(snfSarkilar.get(i).getListeID());
                                SarkilarTemp.setKategoriID(snfSarkilar.get(i).getKategoriID());
                                SarkilarTemp.setTarzID(snfSarkilar.get(i).getTarzID());
                                SarkilarTemp.setSanatciAdi(snfSarkilar.get(i).getSanatciAdi());
                                SarkilarTemp.setSarkiAdi(snfSarkilar.get(i).getSarkiAdi());
                                SarkilarTemp.setVideoURL(snfSarkilar.get(i).getVideoURL());
                                SarkilarTemp.setEklenmeTarihi(snfSarkilar.get(i).getEklenmeTarihi());
                                SarkilarTemp.setDuzenlenmeTarihi(snfSarkilar.get(i).getDuzenlenmeTarihi());
                                snfSarkilarTemp.add(SarkilarTemp);
                            }
                        }
                    }

                    if(snfSarkilarTemp.size() <= 0) {
                        lblOrtaMesaj.setVisibility(View.VISIBLE);
                        lblOrtaMesaj.setText(getString(R.string.liste_bos));
                        lstSarkiYonetimi.setVisibility(View.GONE);
                    } else {
                        lblOrtaMesaj.setVisibility(View.GONE);
                        lstSarkiYonetimi.setVisibility(View.VISIBLE);

                        Collections.sort(snfSarkilarTemp, new SnfRepertuvarComparatorRepertuvar());

                        ArrayList<Pair<Long, String>> SarkiListesi = new ArrayList<>();

                        for (int i = 0; i <= snfSarkilarTemp.size() - 1; i++) {
                            SarkiListesi.add(new Pair<>((long) i, "{\"SarkiID\":" + snfSarkilarTemp.get(i).getId() + ",\"SanatciAdi\":\"" + snfSarkilarTemp.get(i).getSanatciAdi() + "\",\"SarkiAdi\":\"" + snfSarkilarTemp.get(i).getSarkiAdi() + "\"}"));
                        }

                        adpSarkiListesiDragDropLST = new AdpSarkiListesiDragDropLST(activity, snfSarkilarTemp, SarkiListesi, SecilenListelemeTipi, R.layout.explstsarkilistesi_item, R.id.ImgDragDropIcon, false);
                        adpSarkiListesiDragDropLST.setDuzenlenebilirMi(DuzenlenebilirMi, RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
                        lstSarkiYonetimi.setAdapter(adpSarkiListesiDragDropLST, true);

                        // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                        //AdpSarkiListesiLST2 adpSarkiListesiLST2 = new AdpSarkiListesiLST2(activity, snfSarkilarTemp, snfSarkilarTemp.size() > 25, SecilenListelemeTipi);

                        //lstSarkiYonetimi.setFastScrollEnabled(snfSarkilarTemp.size() > 10);
                        //lstSarkiYonetimi.setAdapter(adpSarkiListesiLST2);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RLSarkiYonetimi_DuzenlePanel = findViewById(R.id.RLSarkiYonetimi_DuzenlePanel);
        RLSarkiYonetimi_DuzenlePanel.setVisibility(View.GONE);

        btnGeri_DuzenlePanel = findViewById(R.id.btnGeri_DuzenlePanel);
        btnGeri_DuzenlePanel.setOnClickListener(this);

        lblchkTumunuSec_DuzenlePanel = findViewById(R.id.lblchkTumunuSec_DuzenlePanel);
        lblchkTumunuSec_DuzenlePanel.setTypeface(YaziFontu, Typeface.NORMAL);
        lblchkTumunuSec_DuzenlePanel.setOnClickListener(this);

        btnMenu_DuzenlePanel = findViewById(R.id.btnMenu_DuzenlePanel);
        btnMenu_DuzenlePanel.setOnClickListener(this);

        lblListeAdi = findViewById(R.id.lblListeAdi);
        lblListeAdi.setTypeface(YaziFontu, Typeface.BOLD);

        RLContent = findViewById(R.id.RLContent);
        RLContent.setOnClickListener(this);

        lstSarkiYonetimi = findViewById(R.id.lstSarkiYonetimi);
        lstSarkiYonetimi.getRecyclerView().setVerticalScrollBarEnabled(true);
        lstSarkiYonetimi.setLayoutManager(new LinearLayoutManager(activity));
        lstSarkiYonetimi.setCanDragHorizontally(false);
        lstSarkiYonetimi.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int position) {
                //AkorDefterimSys.ToastMsj(activity, "Start - position: " + position, Toast.LENGTH_SHORT);
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) { // Tutulan kayıt sürüklenmiş ve aynı yerde değilse
                    for(int i = 0; i <= lstSarkiYonetimi.getAdapter().getItemList().size() - 1; i++) {
                        try {
                            JSONObject JSONSonuc = new JSONObject(((Pair)lstSarkiYonetimi.getAdapter().getItemList().get(i)).second.toString());

                            veritabani.SarkiSiraNoDegistir(JSONSonuc.getInt("SarkiID"), i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    SarkiListesiGetir();
                }
            }
        });

        lblOrtaMesaj = findViewById(R.id.lblOrtaMesaj);
        lblOrtaMesaj.setTypeface(YaziFontu, Typeface.NORMAL);

        FABSarkiEkle = (FloatingActionButton) activity.findViewById(R.id.FABSarkiEkle);
        FABSarkiEkle.setImageResource(R.drawable.ic_plus_beyaz);
        FABSarkiEkle.setOnClickListener(this);

        LLFiltreMenu = findViewById(R.id.LLFiltreMenu);
        LLFiltreMenu.setVisibility(View.GONE);

        lblFiltre = findViewById(R.id.lblFiltre);
        lblFiltre.setTypeface(YaziFontu, Typeface.BOLD);

        lblListeler = findViewById(R.id.lblListeler);
        lblListeler.setTypeface(YaziFontu, Typeface.BOLD);

        spnListeler = findViewById(R.id.spnListeler);
        spnListeler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SecilenListeID = snfListeler.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lblKategoriler = findViewById(R.id.lblKategoriler);
        lblKategoriler.setTypeface(YaziFontu, Typeface.BOLD);

        spnKategoriler = findViewById(R.id.spnKategoriler);
        spnKategoriler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SecilenKategoriID = snfKategoriler.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lblTarzlar = findViewById(R.id.lblTarzlar);
        lblTarzlar.setTypeface(YaziFontu, Typeface.BOLD);

        spnTarzlar = findViewById(R.id.spnTarzlar);
        spnTarzlar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SecilenTarzID = snfTarzlar.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lblListelemeTipi = findViewById(R.id.lblListelemeTipi);
        lblListelemeTipi.setTypeface(YaziFontu, Typeface.BOLD);

        spnListelemeTipi = findViewById(R.id.spnListelemeTipi);
        spnListelemeTipi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SecilenListelemeTipi = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnListele = findViewById(R.id.btnListele);
        btnListele.setTypeface(YaziFontu, Typeface.NORMAL);
        btnListele.setOnClickListener(this);
	}

    private static class MyDragItem extends DragItem {
        MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence SanatciAdiSarkiAdi = ((TextView) clickedView.findViewById(R.id.lblSanatciSarkiAdi)).getText();
            CharSequence Kategori1 = ((TextView) clickedView.findViewById(R.id.lblKategoriAdi1)).getText();
            CharSequence Kategori2 = ((TextView) clickedView.findViewById(R.id.lblKategoriAdi2)).getText();
            CharSequence Tarz1 = ((TextView) clickedView.findViewById(R.id.lblTarzAdi1)).getText();
            CharSequence Tarz2 = ((TextView) clickedView.findViewById(R.id.lblTarzAdi2)).getText();

            ((TextView) dragView.findViewById(R.id.lblSanatciSarkiAdi)).setText(SanatciAdiSarkiAdi);
            ((TextView) dragView.findViewById(R.id.lblKategoriAdi1)).setText(Kategori1);
            ((TextView) dragView.findViewById(R.id.lblKategoriAdi2)).setText(Kategori2);
            ((TextView) dragView.findViewById(R.id.lblTarzAdi1)).setText(Tarz1);
            ((TextView) dragView.findViewById(R.id.lblTarzAdi2)).setText(Tarz2);

            dragView.findViewById(R.id.item_layout).setBackgroundColor(dragView.getResources().getColor(R.color.Siyah));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;
        AkorDefterimSys.SharePrefAyarlarınıUygula();

        if(AkorDefterimSys.prefAction.equals("Şarkı eklendi")) {
            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sarki_eklendi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi));
            AkorDefterimSys.prefAction = "";
            AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiID = 0;
        } else if(AkorDefterimSys.prefAction.equals("Şarkı düzenlendi")) {
            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sarki_duzenlendi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi));
            AkorDefterimSys.prefAction = "";
            AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiID = 0;
        } else if(AkorDefterimSys.prefAction.equals("Şarkı eklendi ve gönderildi")) {
            if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                        getString(R.string.sarki_yonetimi),
                        getString(R.string.sarki_eklendi_gonderildi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi, getString(R.string.uygulama_adi)),
                        getString(R.string.tamam),
                        "ADDialog_Kapat");
                ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                ADDialog.show();
            }

            AkorDefterimSys.prefAction = "";
            AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiID = 0;
        } else if(AkorDefterimSys.prefAction.equals("Şarkı eklendi ama gönderilemedi")) {
            if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                        getString(R.string.sarki_yonetimi),
                        getString(R.string.sarki_eklendi_gonderilemedi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi),
                        getString(R.string.tamam),
                        "ADDialog_Kapat");
                ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                ADDialog.show();
            }

            AkorDefterimSys.prefAction = "";
            AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiID = 0;
        }

        spnListeGetir();
        spnKategoriGetir();
        spnTarzGetir();
        spnListelemeTipiGetir();
        SarkiListesiGetir();
    }

    @Override
    public void onBackPressed() {
        if (RLSarkiYonetimi_DuzenlePanel.getVisibility() == View.VISIBLE) DuzenlemeyiKapat(RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
        else if (RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE) AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
        else if(FiltreMenuAcikMi) FiltreMenuKapat();
        else {
            AkorDefterimSys.KlavyeKapat();
            AkorDefterimSys.DismissAlertDialog(ADDialog);

            super.onBackPressed();
        }
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.CLContainer:
                FiltreMenuKapat();
                break;
            case R.id.RLContent:
                FiltreMenuKapat();
                break;
			case R.id.btnGeri_AnaPanel:
				AkorDefterimSys.EkranKapat();
				break;
            case R.id.btnFiltre_AnaPanel:
                if (!FiltreMenuAcikMi) { // Filtre menu kapalıysa
                    AkorDefterimSys.CircularReveal(activity, R.id.RLContent, R.id.LLFiltreMenu, "Sol", "Alt", "Ac");
                    FiltreMenuAcikMi = true;
                } else { // Filtre menu açıksa
                    AkorDefterimSys.CircularReveal(activity, R.id.RLContent, R.id.LLFiltreMenu, "Sol", "Alt", "Kapat");
                    FiltreMenuAcikMi = false;
                }
                break;
            case R.id.btnListele:
                FiltreMenuKapat();
                SarkiListesiGetir();
                break;
            case R.id.btnAra_AnaPanel:
                FiltreMenuKapat();
                AkorDefterimSys.AramaPanelAc(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                break;
            case R.id.btnMenu_AnaPanel:
                AkorDefterimSys.showPopupMenu(activity, v, R.menu.sarki_yonetimi_menu);
                break;
            case R.id.btnGeri_AramaPanel:
                FiltreMenuKapat();
                AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                break;
            case R.id.btnGeri_DuzenlePanel:
                DuzenlemeyiKapat(RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
                break;
            case R.id.lblchkTumunuSec_DuzenlePanel:
                adpSarkiListesiDragDropLST.TumunuSecYadaSecme();
                break;
            case R.id.btnMenu_DuzenlePanel:
                AkorDefterimSys.showPopupMenu(activity, v, R.menu.sarki_yonetimi_duzenle_menu);
                break;
            case R.id.FABSarkiEkle:
                FiltreMenuKapat();
                AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
                mIntent.putExtra("Islem", "YeniSarkiEkle");
                mIntent.putExtra("SecilenSarkiID", 0);
                mIntent.putExtra("SecilenSanatciAdi", "");
                mIntent.putExtra("SecilenSarkiAdi", "");
                mIntent.putExtra("SecilenSarkiVideoURL", "");
                mIntent.putExtra("SecilenSarkiIcerik", "");

                AkorDefterimSys.EkranGetir(mIntent, "Slide");
                break;
		}
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
                case "PopupMenu":
                    switch (JSONSonuc.getInt("ItemId")) {
                        case R.id.btnDuzenle:
                            DuzenlemeyiAc(RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
                            break;
                        case R.id.btnSecilenleriSil:
                            int ToplamSecilenSarkiSayisi = adpSarkiListesiDragDropLST.ToplamSecilenSarkiSayisiGetir();

                            if(ToplamSecilenSarkiSayisi > 1) {
                                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                    ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
                                            getString(R.string.sil),
                                            getString(R.string.sarki_sil_soru2, String.valueOf(ToplamSecilenSarkiSayisi)),
                                            getString(R.string.evet),"ADDialog_SarkiSil",
                                            getString(R.string.hayir),"ADDialog_Kapat");
                                    ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    ADDialog.show();
                                }
                            } else if(ToplamSecilenSarkiSayisi == 1) {
                                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                    ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
                                            getString(R.string.sil),
                                            getString(R.string.sarki_sil_soru, adpSarkiListesiDragDropLST.SecilenSanatciAdiGetir(), adpSarkiListesiDragDropLST.SecilenSarkiAdiGetir()),
                                            getString(R.string.evet),"ADDialog_SarkiSil",
                                            getString(R.string.hayir),"ADDialog_Kapat");
                                    ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    ADDialog.show();
                                }
                            } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.silmek_istediginiz_sarkilari_secin));

                            break;
                    }
                    break;
                case "ADDialog_SarkiSil":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    adpSarkiListesiDragDropLST.SecilenKayitlariSil();
                    break;
                case "SecilenKayitlarSilindi":
                    DuzenlemeyiKapat(RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
                    AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                    AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sarki_silindi2));
                    SarkiListesiGetir();
                    break;
                case "TumunuSecGuncelle":
                    switch (JSONSonuc.getString("Durum")) {
                        case "Dolu":
                            lblchkTumunuSec_DuzenlePanel.setText(getString(R.string.tumunu_sec_circle_checkbox_dolu));
                            break;
                        case "YariDolu":
                            lblchkTumunuSec_DuzenlePanel.setText(getString(R.string.tumunu_sec_circle_checkbox_yaridolu));
                            break;
                        case "Boş":
                            lblchkTumunuSec_DuzenlePanel.setText(getString(R.string.tumunu_sec_circle_checkbox_bos));
                            break;
                    }
                    break;
                case "DuzenlemeyiAc":
                    DuzenlemeyiAc(RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
                    break;
                case "SarkiDuzenleEkranGetir":
                    AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                    AkorDefterimSys.KlavyeKapat();
                    FiltreMenuKapat();

                    Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
                    mIntent.putExtra("Islem", "SarkiDuzenle");
                    mIntent.putExtra("SecilenSarkiID", JSONSonuc.getInt("SecilenSarkiID"));
                    mIntent.putExtra("SecilenSanatciAdi", JSONSonuc.getString("SecilenSanatciAdi"));
                    mIntent.putExtra("SecilenSarkiAdi", JSONSonuc.getString("SecilenSarkiAdi"));
                    mIntent.putExtra("SecilenSarkiVideoURL", JSONSonuc.getString("SecilenSarkiVideoURL"));
                    mIntent.putExtra("SecilenSarkiIcerik", veritabani.SarkiIcerikGetir(JSONSonuc.getInt("SecilenSarkiID")));

                    AkorDefterimSys.EkranGetir(mIntent, "Slide");
                    break;
                case "ADDialog_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    AkorDefterimSys.EkranKapat();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SnfRepertuvarComparatorRepertuvar implements Comparator<SnfSarkilar> {
        @Override
        public int compare(SnfSarkilar Sarkilar1, SnfSarkilar Sarkilar2) {
            Collator collator = Collator.getInstance(Locale.getDefault()); // veya Collator trCollator = Collator.getInstance(new Locale("tr", "TR"));

            switch (SecilenListelemeTipi) {
                case 0:
                    return collator.compare(Sarkilar1.getSanatciAdi(), Sarkilar2.getSanatciAdi());
                case 1:
                    return collator.compare(Sarkilar1.getSarkiAdi(), Sarkilar2.getSarkiAdi());
            }

            return 0;
        }
    }

    private void spnListeGetir() {
        snfListeler = veritabani.SnfListeGetir("Cevrimdisi", false);
        AdpListelerSPN AdpListelerSPN = new AdpListelerSPN(activity, snfListeler);
        spnListeler.setAdapter(AdpListelerSPN);

        if(snfListeler.size() > 0) SecilenListeID = snfListeler.get(0).getId();
    }

    private void spnKategoriGetir() {
        snfKategoriler = veritabani.SnfKategoriGetir(true);
        AdpKategori AdpKategoriler = new AdpKategori(activity, snfKategoriler);
        spnKategoriler.setAdapter(AdpKategoriler);
    }

    private void spnTarzGetir() {
        snfTarzlar = veritabani.SnfTarzGetir(true);
        AdpTarz AdpTarzlar = new AdpTarz(activity, snfTarzlar);
        spnTarzlar.setAdapter(AdpTarzlar);
    }

    public void spnListelemeTipiGetir() {
        snfListelemeTipi = new ArrayList<SnfListelemeTipi>();

        for (String LT : getResources().getStringArray(R.array.ListelemeTipi)) {
            SnfListelemeTipi ListelemeTipi = new SnfListelemeTipi();
            ListelemeTipi.setListelemeTipi(LT);
            snfListelemeTipi.add(ListelemeTipi);
        }

        AdpListelemeTipi AdpListelemeTipi = new AdpListelemeTipi(activity, snfListelemeTipi);
        spnListelemeTipi.setAdapter(AdpListelemeTipi);
    }

    private void SarkiListesiGetir() {
        lblListeAdi.setText(veritabani.ListeAdiGetir(SecilenListeID));

        if(snfSarkilar != null) snfSarkilar.clear();
        else snfSarkilar = new ArrayList<>();

        snfSarkilar = veritabani.SnfSarkiGetir(SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenListelemeTipi);

        if(snfSarkilar.size() <= 0) {
            //btnFiltre_AnaPanel.setImageResource(R.drawable.ic_filter);
            //btnFiltre_AnaPanel.setEnabled(false);
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
            btnAra_AnaPanel.setEnabled(false);
            btnMenu_AnaPanel.setImageResource(R.drawable.dots_vertical);
            btnMenu_AnaPanel.setEnabled(false);
            lblOrtaMesaj.setVisibility(View.VISIBLE);
            lblOrtaMesaj.setText(getString(R.string.liste_bos));
            lstSarkiYonetimi.setVisibility(View.GONE);
        } else {
            //btnFiltre_AnaPanel.setImageResource(R.drawable.ic_filter_siyah);
            //btnFiltre_AnaPanel.setEnabled(true);
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
            btnAra_AnaPanel.setEnabled(true);
            btnMenu_AnaPanel.setImageResource(R.drawable.dots_vertical_siyah);
            btnMenu_AnaPanel.setEnabled(true);
            lblOrtaMesaj.setVisibility(View.GONE);
            lstSarkiYonetimi.setVisibility(View.VISIBLE);

            Collections.sort(snfSarkilar, new SnfRepertuvarComparatorRepertuvar());

            ArrayList<Pair<Long, String>> SarkiListesi = new ArrayList<>();

            for (int i = 0; i <= snfSarkilar.size() - 1; i++) {
                SarkiListesi.add(new Pair<>((long) i, "{\"SarkiID\":" + snfSarkilar.get(i).getId() + ",\"SanatciAdi\":\"" + snfSarkilar.get(i).getSanatciAdi() + "\",\"SarkiAdi\":\"" + snfSarkilar.get(i).getSarkiAdi() + "\",\"VideoURL\":\"" + snfSarkilar.get(i).getVideoURL() + "\"}"));
            }

            adpSarkiListesiDragDropLST = new AdpSarkiListesiDragDropLST(activity, snfSarkilar, SarkiListesi, SecilenListelemeTipi, R.layout.explstsarkilistesi_item, R.id.ImgDragDropIcon, false);
            adpSarkiListesiDragDropLST.setDuzenlenebilirMi(DuzenlenebilirMi, RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE);
            lstSarkiYonetimi.setAdapter(adpSarkiListesiDragDropLST, true);
        }
    }

    private void FiltreMenuAc() {
        if (!FiltreMenuAcikMi) {
            AkorDefterimSys.CircularReveal(activity, R.id.RLContent, R.id.LLFiltreMenu, "Sol", "Alt", "Ac");
            FiltreMenuAcikMi = true;
        }
    }

    private void FiltreMenuKapat() {
        if(FiltreMenuAcikMi) {
            AkorDefterimSys.CircularReveal(activity, R.id.RLContent, R.id.LLFiltreMenu, "Sol", "Alt", "Kapat");
            FiltreMenuAcikMi = false;
        }
    }

    private void DuzenlemeyiAc(boolean AramaPanelAcikMi) {
        FABSarkiEkle.hide(true);
        FiltreMenuKapat();
        View[] GorunmeyecekOlanViewlar = {RLSarkiYonetimi_AnaPanel};
        AkorDefterimSys.AnimasyonFadeIn(activity, RLSarkiYonetimi_DuzenlePanel, GorunmeyecekOlanViewlar);
        DuzenlenebilirMi = true;
        adpSarkiListesiDragDropLST.setDuzenlenebilirMi(DuzenlenebilirMi, AramaPanelAcikMi);
    }

    private void DuzenlemeyiKapat(boolean AramaPanelAcikMi) {
        FABSarkiEkle.show(true);
        View[] GorunecekOlanViewlar = {RLSarkiYonetimi_AnaPanel};
        AkorDefterimSys.AnimasyonFadeOut(activity, RLSarkiYonetimi_DuzenlePanel, GorunecekOlanViewlar);
        DuzenlenebilirMi = false;
        adpSarkiListesiDragDropLST.setDuzenlenebilirMi(DuzenlenebilirMi, AramaPanelAcikMi);
        adpSarkiListesiDragDropLST.TumSecimleriKaldir();
    }
}