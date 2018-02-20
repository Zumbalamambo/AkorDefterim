package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelemeTipi;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelerSPN;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpSarkiListesiLST2;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.FastScrollListview.FastScroller_Listview;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_SarkiYonetimi;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListelemeTipi;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.clans.fab.FloatingActionButton;

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
    RelativeLayout RelativeLayout1, RLSarkiYonetimi_AnaPanel, RLSarkiYonetimi_AramaPanel, RLContent;
    LinearLayout LLFiltreMenu;
	ImageButton btnGeri_AnaPanel, btnFiltre_AnaPanel, btnAra_AnaPanel, btnGeri_AramaPanel;
	Button btnListele;
	FastScroller_Listview lstSarkiYonetimi;
	TextView lblBaslik_AnaPanel, lblOrtaMesaj, lblFiltre, lblListeler, lblKategoriler, lblTarzlar, lblListelemeTipi;
	Spinner spnListeler, spnKategoriler, spnTarzlar, spnListelemeTipi;
    EditText txtAra_AramaPanel;
    FloatingActionButton FABSarkiEkle;

    private List<SnfSarkilar> snfSarkilar;
    private List<SnfSarkilar> snfSarkilarTemp;
    private List<SnfListeler> snfListeler;
    private List<SnfKategoriler> snfKategoriler;
    private List<SnfTarzlar> snfTarzlar;
    private List<SnfListelemeTipi> snfListelemeTipi;

    int SecilenListeID = 0, SecilenKategoriID = 0, SecilenTarzID = 0, SecilenListelemeTipi = 0, SecilenSarkiID = 0;
    String SecilenSanatciAdi = "", SecilenSarkiAdi = "";
    Boolean FiltreMenuAcikMi = false;

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

        RelativeLayout1 = findViewById(R.id.RelativeLayout1);
        RelativeLayout1.setOnClickListener(this);

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
                                SarkilarTemp.setEklenmeTarihi(snfSarkilar.get(i).getEklenmeTarihi());
                                SarkilarTemp.setDuzenlenmeTarihi(snfSarkilar.get(i).getDuzenlenmeTarihi());
                                snfSarkilarTemp.add(SarkilarTemp);
                            } else if (snfSarkilar.get(i).getSarkiAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfSarkilar SarkilarTemp = new SnfSarkilar();
                                SarkilarTemp.setId(snfSarkilar.get(i).getId());
                                SarkilarTemp.setListeID(snfSarkilar.get(i).getListeID());
                                SarkilarTemp.setTarzID(snfSarkilar.get(i).getTarzID());
                                SarkilarTemp.setSanatciAdi(snfSarkilar.get(i).getSanatciAdi());
                                SarkilarTemp.setSarkiAdi(snfSarkilar.get(i).getSarkiAdi());
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

                        // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                        AdpSarkiListesiLST2 adpSarkiListesiLST2 = new AdpSarkiListesiLST2(activity, snfSarkilarTemp, snfSarkilarTemp.size() > 25, SecilenListelemeTipi);

                        lstSarkiYonetimi.setFastScrollEnabled(snfSarkilarTemp.size() > 10);
                        lstSarkiYonetimi.setAdapter(adpSarkiListesiLST2);
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

        RLContent = findViewById(R.id.RLContent);
        RLContent.setOnClickListener(this);

        lstSarkiYonetimi = findViewById(R.id.lstSarkiYonetimi);
        lstSarkiYonetimi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                // Eğer sarki arama alanına yazı GİRİLMEMİŞSE "snfSarkilar" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenSarkiID = snfSarkilar.get(position).getId();
                    SecilenSanatciAdi = snfSarkilar.get(position).getSanatciAdi();
                    SecilenSarkiAdi = snfSarkilar.get(position).getSarkiAdi();
                } else { // Eğer sarki arama alanına yazı GİRİLMİŞSE "snfSarkilarTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenSarkiID = snfSarkilarTemp.get(position).getId();
                    SecilenSanatciAdi = snfSarkilarTemp.get(position).getSanatciAdi();
                    SecilenSarkiAdi = snfSarkilarTemp.get(position).getSarkiAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                FiltreMenuKapat();
                openContextMenu(lstSarkiYonetimi);
            }
        });
        lstSarkiYonetimi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Eğer sarki arama alanına yazı GİRİLMEMİŞSE "snfSarkilar" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenSarkiID = snfSarkilar.get(position).getId();
                    SecilenSanatciAdi = snfSarkilar.get(position).getSanatciAdi();
                    SecilenSarkiAdi = snfSarkilar.get(position).getSarkiAdi();
                } else { // Eğer sarki arama alanına yazı GİRİLMİŞSE "snfSarkilarTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenSarkiID = snfSarkilarTemp.get(position).getId();
                    SecilenSanatciAdi = snfSarkilarTemp.get(position).getSanatciAdi();
                    SecilenSarkiAdi = snfSarkilarTemp.get(position).getSarkiAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                FiltreMenuKapat();
                openContextMenu(lstSarkiYonetimi);

                return true;
            }
        });
        registerForContextMenu(lstSarkiYonetimi);

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

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;

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
        if (RLSarkiYonetimi_AramaPanel.getVisibility() == View.VISIBLE) AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
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
            case R.id.RelativeLayout1:
                FiltreMenuKapat();
                break;
            case R.id.RLContent:
                FiltreMenuKapat();
                break;
			case R.id.btnGeri_AnaPanel:
				onBackPressed();
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
            case R.id.btnGeri_AramaPanel:
                FiltreMenuKapat();
                AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                break;
            case R.id.FABSarkiEkle:
                FiltreMenuKapat();
                AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
                mIntent.putExtra("Islem", "YeniSarkiEkle");
                mIntent.putExtra("SecilenSarkiID", 0);
                mIntent.putExtra("SecilenSanatciAdi", "");
                mIntent.putExtra("SecilenSarkiAdi", "");
                mIntent.putExtra("SecilenSarkiIcerik", "");

                AkorDefterimSys.EkranGetir(mIntent, "Slide");
                break;
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        switch (v.getId()) {
            case R.id.lstSarkiYonetimi:
                menu.setHeaderTitle(String.format("%s - %s", SecilenSanatciAdi, SecilenSarkiAdi));
                menu.add(0, 0, 0, getString(R.string.sil));
                menu.add(0, 1, 0, getString(R.string.duzenle));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getGroupId()) {
            case 0: // ListView
                switch (item.getItemId()) {
                    case 0:
                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
                                    getString(R.string.sil),
                                    getString(R.string.sarki_sil_soru, SecilenSanatciAdi, SecilenSarkiAdi),
                                    getString(R.string.evet),"ADDialog_SarkiSil",
                                    getString(R.string.hayir),"ADDialog_Kapat");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                        break;
                    case 1:
                        AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                        Intent mIntent = new Intent(activity, Sarki_EkleDuzenle.class);
                        mIntent.putExtra("Islem", "SarkiDuzenle");
                        mIntent.putExtra("SecilenSarkiID", SecilenSarkiID);
                        mIntent.putExtra("SecilenSanatciAdi", SecilenSanatciAdi);
                        mIntent.putExtra("SecilenSarkiAdi", SecilenSarkiAdi);
                        mIntent.putExtra("SecilenSarkiIcerik", veritabani.SarkiIcerikGetir(SecilenSarkiID));

                        AkorDefterimSys.EkranGetir(mIntent, "Slide");
                        break;
                    default:
                        return false;
                }

                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
                case "ADDialog_SarkiSil":
                    AkorDefterimSys.AramaPanelKapat(RLSarkiYonetimi_AnaPanel.getId(),RLSarkiYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                    if(veritabani.SarkiVarMiKontrol(SecilenSarkiID)) {
                        if(veritabani.SarkiSil(SecilenSarkiID))
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sarki_silindi, SecilenSanatciAdi, SecilenSarkiAdi));
                        else
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                    } else
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sarki_bulunamadi, SecilenSanatciAdi, SecilenSarkiAdi));

                    ADDialog.dismiss();

                    SarkiListesiGetir();
                    break;
                case "ADDialog_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    onBackPressed();
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
        snfListeler = veritabani.SnfListeGetir("Cevrimdisi", true);
        AdpListelerSPN AdpListelerSPN = new AdpListelerSPN(activity, snfListeler);
        spnListeler.setAdapter(AdpListelerSPN);
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
        if(snfSarkilar != null) snfSarkilar.clear();
        else snfSarkilar = new ArrayList<>();

        snfSarkilar = veritabani.SnfSarkiGetir(SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenListelemeTipi);

        if(snfSarkilar.size() <= 0) {
            //btnFiltre_AnaPanel.setImageResource(R.drawable.ic_filter);
            //btnFiltre_AnaPanel.setEnabled(false);
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
            btnAra_AnaPanel.setEnabled(false);
            lblOrtaMesaj.setVisibility(View.VISIBLE);
            lblOrtaMesaj.setText(getString(R.string.liste_bos));
            lstSarkiYonetimi.setVisibility(View.GONE);
        } else {
            //btnFiltre_AnaPanel.setImageResource(R.drawable.ic_filter_siyah);
            //btnFiltre_AnaPanel.setEnabled(true);
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
            btnAra_AnaPanel.setEnabled(true);
            lblOrtaMesaj.setVisibility(View.GONE);
            lstSarkiYonetimi.setVisibility(View.VISIBLE);

            Collections.sort(snfSarkilar, new SnfRepertuvarComparatorRepertuvar());

            // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
            AdpSarkiListesiLST2 adpSarkiListesiLST2 = new AdpSarkiListesiLST2(activity, snfSarkilar, snfSarkilar.size() > 25, SecilenListelemeTipi);

            lstSarkiYonetimi.setFastScrollEnabled(snfSarkilar.size() > 10);
            lstSarkiYonetimi.setAdapter(adpSarkiListesiLST2);
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
}