package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpSarkiListesiLST;
import com.cnbcyln.app.akordefterim.FastScrollListview.FastScroller_Listview;
import com.cnbcyln.app.akordefterim.Interface.Int_DataConn_AnaEkran;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.ResideMenu.ResideMenu;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.ClearableEditText;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnaEkran extends AppCompatActivity implements Int_DataConn_AnaEkran, Interface_AsyncResponse, View.OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
    FragmentTransaction FT;
    FragmentManager FM;
    Fragment Fragment_Sayfa;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
    Typeface YaziFontu;
    ProgressDialog PDIslem;
    AlertDialog ADDialog, ADDialog_Duyuru;
    InputMethodManager imm;
    LayoutInflater layoutInflater;
    private List<SnfSarkilar> snfSarkilar;
    private List<SnfSarkilar> snfSarkilarTemp;

    // AnaEkran Değişken Tanımlamaları
    ImageButton btnSolMenu, btnSagMenu;
    LinearLayout LLSayfa;
    TextView lblSayfaBaslik;
    CoordinatorLayout AnaEkran_CoordinatorLayout;
    ResideMenu resideMenu;

    // Sliding Menü Sağ Değişkenler Tanımlamaları
    CoordinatorLayout coordinatorLayoutSag;
    RelativeLayout RLSarkiListesi_AnaPanel, RLSarkiListesi_AramaPanel;
    TextView lblMenuSag_OrtaMesaj;
    ImageView ImgMenuSag_OrtaMesaj_SolOk;
    ImageButton btnAra_AnaPanel, btnGeri_AramaPanel;
    ClearableEditText txtAra_AramaPanel;
    FastScroller_Listview lstSarkiListesi;

    // Sliding Menü Sol Değişkenler Tanımlamaları
    CoordinatorLayout coordinatorLayoutSol;
    ViewPager VPSolMenuPager;
    PagerAdapter PagerAdapter;
    PagerSlidingTabStrip PSTTabs;

	String UygulamaVersiyon = "", Fragment_SayfaAdi = "";
    boolean CikisIcinCiftTiklandiMi = false;
    int SecilenListeID = 0, SecilenKategoriID = 0, SecilenTarzID = 0, SecilenListelemeTipi = 0;

	@SuppressWarnings("ConstantConditions")
    @SuppressLint({"InflateParams", "CommitTransaction", "ClickableViewAccessibility", "ResourceAsColor"})
    @Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ana);

        /* **************************************
           ***                                ***
           ***        Genel Değişkenler       ***
           ***                                ***
           **************************************/

        activity = this;
        AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
        veritabani = new Veritabani(activity);
        YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
        sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

        FM = getFragmentManager();

        sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putBoolean("prefAppRunning", true);
        sharedPrefEditor.apply();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

        AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
        //AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
        //AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        try {
            UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        FT = getFragmentManager().beginTransaction(); // Fragment methodunu kullanmak için sabit nesne

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        resideMenu = new ResideMenu(activity, R.layout.menu_sol, R.layout.menu_sag);
        resideMenu.setBackground(R.drawable.bg);
        resideMenu.attachToActivity(activity);
        resideMenu.setScaleValue(0.6f);
        resideMenu.setUse3D(true);

        //resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        //resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        /* **************************************
           ***                                ***
           ***         Sliding Menü Sag       ***
           ***                                ***
           **************************************/

        View ViewSlidingSagMenuContainer = resideMenu.getRightMenuView();

        coordinatorLayoutSag = ViewSlidingSagMenuContainer.findViewById(R.id.coordinatorLayout);

        RLSarkiListesi_AnaPanel = ViewSlidingSagMenuContainer.findViewById(R.id.RLSarkiListesi_AnaPanel);
        RLSarkiListesi_AnaPanel.setVisibility(View.VISIBLE);

        btnAra_AnaPanel = ViewSlidingSagMenuContainer.findViewById(R.id.btnAra_AnaPanel);
        btnAra_AnaPanel.setEnabled(false);
        btnAra_AnaPanel.setOnClickListener(this);

        RLSarkiListesi_AramaPanel = ViewSlidingSagMenuContainer.findViewById(R.id.RLSarkiListesi_AramaPanel);
        RLSarkiListesi_AramaPanel.setVisibility(View.GONE);

        btnGeri_AramaPanel = ViewSlidingSagMenuContainer.findViewById(R.id.btnGeri_AramaPanel);
        btnGeri_AramaPanel.setOnClickListener(this);

        txtAra_AramaPanel = ViewSlidingSagMenuContainer.findViewById(R.id.txtAra_AramaPanel);
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
                                SarkilarTemp.setTarzID(snfSarkilar.get(i).getTarzID());
                                SarkilarTemp.setKategoriID(snfSarkilar.get(i).getKategoriID());
                                SarkilarTemp.setSanatciAdi(snfSarkilar.get(i).getSanatciAdi());
                                SarkilarTemp.setSarkiAdi(snfSarkilar.get(i).getSarkiAdi());
                                SarkilarTemp.setVideoURL(snfSarkilar.get(i).getVideoURL());
                                snfSarkilarTemp.add(SarkilarTemp);
                            } else if (snfSarkilar.get(i).getSarkiAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfSarkilar SarkilarTemp = new SnfSarkilar();
                                SarkilarTemp.setId(snfSarkilar.get(i).getId());
                                SarkilarTemp.setListeID(snfSarkilar.get(i).getListeID());
                                SarkilarTemp.setTarzID(snfSarkilar.get(i).getTarzID());
                                SarkilarTemp.setKategoriID(snfSarkilar.get(i).getKategoriID());
                                SarkilarTemp.setSanatciAdi(snfSarkilar.get(i).getSanatciAdi());
                                SarkilarTemp.setSarkiAdi(snfSarkilar.get(i).getSarkiAdi());
                                SarkilarTemp.setVideoURL(snfSarkilar.get(i).getVideoURL());
                                snfSarkilarTemp.add(SarkilarTemp);
                            }
                        }
                    }

                    if(snfSarkilarTemp.size() <= 0) {
                        btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
                        btnAra_AnaPanel.setEnabled(false);
                        lblMenuSag_OrtaMesaj.setVisibility(View.VISIBLE);
                        lblMenuSag_OrtaMesaj.setText(getString(R.string.liste_bos));
                        ImgMenuSag_OrtaMesaj_SolOk.setVisibility(View.VISIBLE);
                        lstSarkiListesi.setVisibility(View.GONE);
                    } else {
                        btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
                        btnAra_AnaPanel.setEnabled(true);
                        lblMenuSag_OrtaMesaj.setVisibility(View.GONE);
                        ImgMenuSag_OrtaMesaj_SolOk.setVisibility(View.GONE);
                        lstSarkiListesi.setVisibility(View.VISIBLE);

                        // Elde ettiğimiz snfSarkilar sıfındaki tüm kayıtları AdpSarkiListesiLST ile lstSarkiListesi isimli Listview'a set ediyoruz..
                        // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                        AdpSarkiListesiLST adpSarkiListesiLST2 = new AdpSarkiListesiLST(activity, snfSarkilarTemp, true, SecilenListelemeTipi);

                        lstSarkiListesi.setFastScrollEnabled(snfSarkilar.size() > 25);
                        lstSarkiListesi.setAdapter(adpSarkiListesiLST2);
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

        lblMenuSag_OrtaMesaj = ViewSlidingSagMenuContainer.findViewById(R.id.lblMenuSag_OrtaMesaj);
        lblMenuSag_OrtaMesaj.setTypeface(YaziFontu, Typeface.NORMAL);

        ImgMenuSag_OrtaMesaj_SolOk = ViewSlidingSagMenuContainer.findViewById(R.id.ImgMenuSag_OrtaMesaj_SolOk);

        lstSarkiListesi = ViewSlidingSagMenuContainer.findViewById(R.id.lstSarkiListesi);
        lstSarkiListesi.setFastScrollEnabled(true);
        lstSarkiListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
            // Eğer listeden şarkı arama alanına yazı GİRİLMEMİŞSE "snfSarkilar" sıfından kayıt baz alarak içerik getirtiyoruz..
            if (txtAra_AramaPanel.getText().length() == 0) {
                if(SecilenListeID == 0) {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                        AkorDefterimSys.SarkiGetir(veritabani, SecilenListeID, snfSarkilar.get(position).getId(), snfSarkilar.get(position).getSanatciAdi(), snfSarkilar.get(position).getSarkiAdi(), snfSarkilar.get(position).getVideoURL());
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayoutSag, getString(R.string.internet_baglantisi_saglanamadi));
                } else {
                    AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                    AkorDefterimSys.SarkiGetir(veritabani, SecilenListeID, snfSarkilar.get(position).getId(), snfSarkilar.get(position).getSanatciAdi(), snfSarkilar.get(position).getSarkiAdi(), snfSarkilar.get(position).getVideoURL());
                }
            } else { // Eğer listeden şarkı arama alanına yazı GİRİLMİŞSE "snfSarkilarTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                if(SecilenListeID == 0) {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                        AkorDefterimSys.SarkiGetir(veritabani, SecilenListeID, snfSarkilarTemp.get(position).getId(), snfSarkilarTemp.get(position).getSanatciAdi(), snfSarkilarTemp.get(position).getSarkiAdi(), snfSarkilarTemp.get(position).getVideoURL());
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayoutSag, getString(R.string.internet_baglantisi_saglanamadi));
                } else {
                    AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                    AkorDefterimSys.SarkiGetir(veritabani, SecilenListeID, snfSarkilarTemp.get(position).getId(), snfSarkilarTemp.get(position).getSanatciAdi(), snfSarkilarTemp.get(position).getSarkiAdi(), snfSarkilarTemp.get(position).getVideoURL());
                }
            }

            AkorDefterimSys.KlavyeKapat();
            }
        });
        lstSarkiListesi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    /*if (RLSarkiListesi_SarkiAramaPanel.getVisibility() != View.VISIBLE && !AdpSarkiListesiLST.isSelectable()) { //Şarkı listesinde şarkılar seçilebilir durumda mı? Yani checkbox görünüyor mu? => Görünmüyorsa..
                        AdpSarkiListesiLST.setSelectable(position);
                    }*/

                return true;
            }
        });
        lstSarkiListesi.setVisibility(View.GONE);

        /* **************************************
           ***                                ***
           ***         Sliding Menü Sol       ***
           ***                                ***
           **************************************/

        View ViewSlidingSolMenuContainer = resideMenu.getLeftMenuView();

        coordinatorLayoutSol = ViewSlidingSolMenuContainer.findViewById(R.id.coordinatorLayout);

        TextView lblVersiyonNo = ViewSlidingSolMenuContainer.findViewById(R.id.lblVersiyonNo);
        lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
        lblVersiyonNo.setText(String.valueOf("v").concat(UygulamaVersiyon));

        VPSolMenuPager = ViewSlidingSolMenuContainer.findViewById(R.id.VPSolMenuPager);

        PagerAdapter = new PagerAdapter(activity, getSupportFragmentManager());

        VPSolMenuPager.setAdapter(PagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        VPSolMenuPager.setPageMargin(pageMargin);

        PSTTabs = ViewSlidingSolMenuContainer.findViewById(R.id.PSTTabs);
        PSTTabs.setViewPager(VPSolMenuPager);
        PSTTabs.setTextColor(R.color.KoyuYazi);
        PSTTabs.setTypeface(YaziFontu, Typeface.BOLD);

        /* **************************************
           ***                                ***
           ***       Genel Tanımlamalar       ***
           ***                                ***
           **************************************/

        AnaEkran_CoordinatorLayout = activity.findViewById(R.id.AnaEkran_CoordinatorLayout);

        btnSolMenu = findViewById(R.id.btnSolMenu);
        btnSolMenu.setOnClickListener(this);

        btnSagMenu = findViewById(R.id.btnSagMenu);
        btnSagMenu.setOnClickListener(this);

        lblSayfaBaslik = findViewById(R.id.lblSayfaBaslik);
        lblSayfaBaslik.setTypeface(YaziFontu, Typeface.NORMAL);

        LLSayfa = findViewById(R.id.LLSayfa);

        /*int x = LLMenu.getRight();
        int y = LLMenu.getBottom();

        int startRadius = 0;
        int endRadius = (int) Math.hypot(CLMenu.getWidth(), CLMenu.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(LLMenu, x, y, startRadius, endRadius);
            mSlidingRootNavBuilder.setAnim(anim);
        }*/

        Bundle extras = getIntent().getExtras();

        switch (extras.getString("Islem")) {
            case "SarkiGetir":
                AkorDefterimSys.SarkiGetir(veritabani, 0, extras.getInt("SarkiID"), extras.getString("SanatciAdi"), extras.getString("SarkiAdi"), extras.getString("VideoURL"));
                break;
            default:
                FragmentSayfaGetir("anasayfa");
                break;
        }
	}

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        switch (Fragment_SayfaAdi) {
            case "Frg_Anasayfa":
                savedInstanceState.putString("SayfaAdi", "anasayfa");
                break;
            case "Frg_Sarki":
                savedInstanceState.putString("SayfaAdi", "sarki");
                break;
            default:
                savedInstanceState.putString("SayfaAdi", "anasayfa");
                break;
        }

        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);

        super.onSaveInstanceState(savedInstanceState);
    }*/

    /*public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Görünüm hiyerarşisini tekrar yüklemek için superclass olarak çağrılır.
        super.onRestoreInstanceState(savedInstanceState);
        // Bilgileri geri yükle
        //FragmentSayfaGetir(savedInstanceState.getString("SayfaAdi"));

        AkorDefterimSys.ToastMsj(activity, savedInstanceState.getString("SayfaAdi"), Toast.LENGTH_SHORT);
    }*/

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;

        AkorDefterimSys.SharePrefAyarlarınıUygula();

        if(AkorDefterimSys.prefAction.equals("Şarkı eklendi")) {
            if(Fragment_SayfaAdi.equals("Frg_Sarki")) {
                Frg_Sarki Frg_Sarki = (Frg_Sarki) FM.findFragmentById(R.id.LLSayfa);
                Frg_Sarki.StandartSnackBarMsj(getString(R.string.sarki_eklendi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi));
            }

            AkorDefterimSys.prefAction = "";
            AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = "";
            AkorDefterimSys.prefEklenenDuzenlenenVideoURL = "";
            AkorDefterimSys.prefEklenenDuzenlenenSarkiID = 0;
        } else if(AkorDefterimSys.prefAction.equals("Şarkı düzenlendi")) {
            SarkiListesiGetir();
            AkorDefterimSys.SarkiGetir(veritabani, SecilenListeID, AkorDefterimSys.prefEklenenDuzenlenenSarkiID, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi, AkorDefterimSys.prefEklenenDuzenlenenVideoURL);

            // Şarkı düzenlendiyse düzenlenen şarkıyı yeniden çağırıyoruz. Çağırma işleminde olduğu için SnackBar Mesaj gösteremiyorduk.
            // Bu sebeple şarkıyı çağırdıktan 1 saniye sonra SnackBar Mesajı göstermesini istedik.
            AkorDefterimSys.ZamanlayiciBaslat(activity, "Handler", 0, 1000, "Sarki_Duzenlendi_SnackBarMsj_Goster");
        }

        Frg_TabRepKontrol Frg_TabRepKontrol_1 = (Frg_TabRepKontrol) SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
        if(Frg_TabRepKontrol_1 != null) Frg_TabRepKontrol_1.spnDoldur();

        AkorDefterimSys.YeniSurumYeniliklerDialog();

        if(AkorDefterimSys.InternetErisimKontrolu()) AkorDefterimSys.DuyuruGetir();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (RLSarkiListesi_AramaPanel.getVisibility() == View.VISIBLE) { // Arama panel açıksa kapatıyoruz.
            AkorDefterimSys.AramaPanelKapat(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);
        } else if(resideMenu.isOpened()) { // Menü açıksa kapatıyoruz.
            SlidingIslem(0);
        } else if(!Fragment_SayfaAdi.equals("Frg_Anasayfa")) { // Fragment anasayfa değilse anasayfayı çağırıyoruz.
            FragmentSayfaGetir("anasayfa");
        } else {
            // Çift tıklandığında uygulamadan çıkılması için gerekli fonksiyonu yazdık.
            if (CikisIcinCiftTiklandiMi) {
                Process.killProcess(Process.myPid());
            }

            CikisIcinCiftTiklandiMi = true;
            AkorDefterimSys.ToastMsj(activity, getString(R.string.uygulama_cikis_mesaji), Toast.LENGTH_SHORT);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CikisIcinCiftTiklandiMi = false;
                }
            }, 2000);
        }

        //super.onBackPressed();
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

    @SuppressLint({"CommitTransaction", "Assert"})
    @Override
    public void FragmentSayfaGetir(String SayfaAdi) {
        switch (SayfaAdi) {
            case "anasayfa": // Anasayfa

                if(Fragment_SayfaAdi.isEmpty() && Fragment_Sayfa == null) {
                    Fragment_Sayfa = new Frg_Anasayfa();// Fragment sayfa belirle
                    Fragment_SayfaAdi = "Frg_Anasayfa";

                    if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimici"))
                        lblSayfaBaslik.setText(String.format("%s%s%s (%s)", getString(R.string.anasayfa), " - ", getString(R.string.son_eklenen_sarkilar), getString(R.string.cevrimici)));
                    else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi"))
                        lblSayfaBaslik.setText(String.format("%s%s%s (%s)", getString(R.string.anasayfa), " - ", getString(R.string.son_eklenen_sarkilar), getString(R.string.cevrimdisi)));

                    FT = FM.beginTransaction(); // Fragment methodunu kullanmak için sabit nesne
                    FT.add(R.id.LLSayfa, Fragment_Sayfa);
                    FT.commit();// Çağırma işlemini yaptırıyoruz..
                } else if(Fragment_SayfaAdi != "Frg_Anasayfa") {
                    Fragment_Sayfa = new Frg_Anasayfa();// Fragment sayfa belirle
                    Fragment_SayfaAdi = "Frg_Anasayfa";

                    if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimici"))
                        lblSayfaBaslik.setText(String.format("%s%s%s (%s)", getString(R.string.anasayfa), " - ", getString(R.string.son_eklenen_sarkilar), getString(R.string.cevrimici)));
                    else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Cevrimdisi"))
                        lblSayfaBaslik.setText(String.format("%s%s%s (%s)", getString(R.string.anasayfa), " - ", getString(R.string.son_eklenen_sarkilar), getString(R.string.cevrimdisi)));

                    FT = FM.beginTransaction(); // Fragment methodunu kullanmak için sabit nesne

                    FT.replace(R.id.LLSayfa, Fragment_Sayfa);
                    FT.addToBackStack(null);
                    FT.commit();
                }

                break;
            case "hesabim": // Hesabım
                if(AkorDefterimSys.GirisYapildiMi()) {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        AkorDefterimSys.EkranGetir(new Intent(activity, Hesabim.class), "Slide");
                    } else AkorDefterimSys.StandartSnackBarMsj(AnaEkran_CoordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                } else AkorDefterimSys.StandartSnackBarMsj(AnaEkran_CoordinatorLayout, getString(R.string.devam_etmek_icin_giris_yapmalisin));
                break;
            case "girisyap": // Giriş Yap
                AkorDefterimSys.CikisYap();

                break;
            case "bossayfa": // Boş Sayfa
                Fragment_Sayfa = new Frg_BosSayfa();// Fragment sayfa belirle
                Fragment_SayfaAdi = "Frg_BosSayfa";

                FT = FM.beginTransaction(); // Fragment methodunu kullanmak için sabit nesne

                FT.replace(R.id.LLSayfa, Fragment_Sayfa);
                FT.addToBackStack(null);
                FT.commit();

                break;
            /*case "istekyap": // İstek Yap
                if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
                    FT.remove(Fragment_Sayfa); // Geçerli fragment'i sil

                    Fragment_Sayfa = new Frg_IstekYap();// Fragment sayfa belirle
                    Fragment_SayfaTag = "Frg_" + SayfaAdi;
                    lblSayfaBaslik.setText(getString(R.string.istek_yap));
                    btnTranspozeArti.setVisibility(View.GONE);
                    btnTranspozeEksi.setVisibility(View.GONE);
                    btnStar.setVisibility(View.GONE);

                    FT = FM.beginTransaction(); // Fragment methodunu kullanmak için sabit nesne
                    FT.replace(R.id.LLSayfa, Fragment_Sayfa, Fragment_SayfaTag); // FT.add(<Hangi layout içinde çağırılacaksa id'si>, <Çağırılan Fragment>, <Çağırılan Fragment Takma Adı>);
                    FT.commit();// Çağırma işlemini yaptırıyoruz..
                } else
                    AkorDefterimSys.ToastMsj(activity, getString(R.string.internet_baglantisi_saglanamadi), Toast.LENGTH_SHORT);

                break;*/
        }
    }

    @Override
    public void SlidingIslem(int islem) {
        switch (islem) {
            case 0: //Sliding kapatır
                if(resideMenu.isOpened()) resideMenu.closeMenu();

                break;
            case 1: //Sol sliding açar
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);

                break;
            case 2: //Sağ sliding açar
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);

                break;
        }
    }

    @Override
    public android.support.v4.app.Fragment SlidingTabFragmentClassGetir(String FragmentAdi) {
        android.support.v4.app.Fragment fragment = null;

        for (int i = 0; i < PagerAdapter.getCount(); i++) {
            if (PagerAdapter.getPageTitle(i).equals(FragmentAdi)) {
                fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.VPSolMenuPager + ":" + i);
                break;
            }
        }

        return fragment;
    }

    @Override
    public void SarkiListesi_SecimPanelGuncelle(Boolean Durum) {

    }

    @Override
    public void SarkiListesi_SecimPanelBilgiGuncelle() {

    }

    @Override
    public void SarkiPaylas(String Platform, String Baslik, String Icerik, String URL) {

    }

    @Override
    public void AnaEkranProgressIslemDialogAc(String Mesaj) {
        if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
            PDIslem = AkorDefterimSys.CustomProgressDialog(Mesaj, false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
            PDIslem.show();
        }
    }

    @Override
    public void AnaEkranProgressIslemDialogKapat() {
	    AkorDefterimSys.DismissProgressDialog(PDIslem);
    }

    @Override
    public void SarkiListesiGetir() {
        // AnaEkran üzerinden Progress Dialog'u açıyoruz ve "Liste indiriliyor. Lütfen bekleyiniz.." mesajını gösteriyoruz..
        AnaEkranProgressIslemDialogAc(getString(R.string.liste_indiriliyor_lutfen_bekleyiniz));
        AkorDefterimSys.SarkiListesiGetir(veritabani, SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenListelemeTipi, "");
    }

    @Override
    public void StandartSnackBarMsj(String Mesaj) {
        AkorDefterimSys.StandartSnackBarMsj(AnaEkran_CoordinatorLayout, Mesaj);
    }

    @Override
    public void StandartSnackBarMsj(CoordinatorLayout coordinatorLayout, String Mesaj) {
        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, Mesaj);
    }

    @Override
    public void StandartSnackBarMsj(String AcikOlanMenu, String Mesaj) {
        switch (AcikOlanMenu) {
            case "Sağ":
                AkorDefterimSys.StandartSnackBarMsj(coordinatorLayoutSag, Mesaj);
                break;
            case "Sol":
                AkorDefterimSys.StandartSnackBarMsj(coordinatorLayoutSol, Mesaj);
                break;
        }
    }

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            final JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
                case "ADDialog_Egitim_Evet":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    AkorDefterimSys.EkranGetir(new Intent(activity, Egitim.class), "Slide");

                    sharedPrefEditor = sharedPref.edit();
                    sharedPrefEditor.putBoolean("prefEgitimTamamlandiMi", true);
                    sharedPrefEditor.apply();
                    break;
                case "ADDialog_Egitim_Hayir":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);

                    sharedPrefEditor = sharedPref.edit();
                    sharedPrefEditor.putBoolean("prefEgitimTamamlandiMi", true);
                    sharedPrefEditor.apply();

                    AkorDefterimSys.YeniSurumYeniliklerDialog();
                    break;
                case "PDIslem_Timeout":
                    AnaEkranProgressIslemDialogKapat();
                    break;
                case "AnasayfaGetir":
                    if(Fragment_SayfaAdi.equals("Frg_Anasayfa")) {
                        Frg_Anasayfa Frg_Anasayfa = (Frg_Anasayfa) FM.findFragmentById(R.id.LLSayfa);

                        if(Frg_Anasayfa != null) {
                            if(JSONSonuc.getBoolean("Sonuc")) Frg_Anasayfa.AnasayfaDoldur(JSONSonuc.getString("SarkiListesi"));
                            else Frg_Anasayfa.AnasayfaDoldur("");
                        }
                    }
                    break;
                case "KategoriListesiGetir":
                    Frg_TabRepKontrol Frg_TabRepKontrol_1 = (Frg_TabRepKontrol) SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
                    if(Frg_TabRepKontrol_1 != null) Frg_TabRepKontrol_1.KategoriListesiGetir(JSONSonuc.getString("KategoriListesi"));
                    break;
                case "TarzListesiGetir":
                    Frg_TabRepKontrol Frg_TabRepKontrol_2 = (Frg_TabRepKontrol) SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
                    if(Frg_TabRepKontrol_2 != null) Frg_TabRepKontrol_2.TarzListesiGetir(JSONSonuc.getString("TarzListesi"));
                    break;
                case "SarkiListesiGetir":
                    AnaEkranProgressIslemDialogKapat();

                    SecilenListeID = JSONSonuc.getInt("ListeID");
                    SecilenKategoriID = JSONSonuc.getInt("KategoriID");
                    SecilenTarzID = JSONSonuc.getInt("TarzID");

                    // Secilen Listeleme Tipini integer olarak düzenliyoruz. Çünkü Harf sıralaması fonksiyonu bizden integer istiyor..
                    SecilenListelemeTipi = JSONSonuc.getInt("ListelemeTipi");


                    try {
                        if(snfSarkilar != null) snfSarkilar.clear(); // snfSarkilar sınıfı null değilse (Yani tanımlı sınıf varsa) içindeki tüm kayıtları temizliyoruz.
                        else snfSarkilar = new ArrayList<>(); // snfSarkilar sınıfı null ise ArrayList olarak yeni bir sınıf oluşturuyoruz..

                        // JSON Formatında gelen SarkiListesi listesini JSON Array değişkenine atıyoruz.
                        JSONArray JSONGelenVeriArr = new JSONArray(JSONSonuc.getString("SarkiListesi"));
                        JSONObject JSONGelenVeri;

                        // Döngü ile JSON Array değişkeni içindeki bilgileri tek tek snfSarkilar sıfına kayıt ediyoruz..
                        for (int i = 0; i < JSONGelenVeriArr.length(); i++) {
                            JSONGelenVeri = new JSONObject(JSONGelenVeriArr.getString(i));

                            SnfSarkilar Sarki = new SnfSarkilar();
                            Sarki.setId(JSONGelenVeri.getInt("id"));
                            Sarki.setListeID(SecilenListeID);
                            Sarki.setKategoriID(JSONGelenVeri.getInt("KategoriID"));
                            Sarki.setTarzID(JSONGelenVeri.getInt("TarzID"));
                            Sarki.setSanatciAdi(JSONGelenVeri.getString("SanatciAdi"));
                            Sarki.setSarkiAdi(JSONGelenVeri.getString("SarkiAdi"));
                            Sarki.setVideoURL(JSONGelenVeri.getString("VideoURL"));
                            snfSarkilar.add(Sarki);
                        }

                        // Yalnızca şarkıcı adına göre ya da şarkı adına göre listelendiğinde alfabetik sıraya diz..
                        if (SecilenListelemeTipi == 0 || SecilenListelemeTipi == 1) Collections.sort(snfSarkilar, new SnfRepertuvarComparatorRepertuvar());

                        if(snfSarkilar.size() <= 0) {
                            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
                            btnAra_AnaPanel.setEnabled(false);
                            lblMenuSag_OrtaMesaj.setVisibility(View.VISIBLE);
                            lblMenuSag_OrtaMesaj.setText(getString(R.string.liste_bos));
                            ImgMenuSag_OrtaMesaj_SolOk.setVisibility(View.VISIBLE);
                            lstSarkiListesi.setVisibility(View.GONE);
                        } else {
                            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
                            btnAra_AnaPanel.setEnabled(true);
                            lblMenuSag_OrtaMesaj.setVisibility(View.GONE);
                            ImgMenuSag_OrtaMesaj_SolOk.setVisibility(View.GONE);
                            lstSarkiListesi.setVisibility(View.VISIBLE);

                            // Elde ettiğimiz snfSarkilar sıfındaki tüm kayıtları AdpSarkiListesiLST ile lstSarkiListesi isimli Listview'a set ediyoruz..
                            // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                            AdpSarkiListesiLST adpSarkiListesiLST = new AdpSarkiListesiLST(activity, snfSarkilar, snfSarkilar.size() > 25, SecilenListelemeTipi);

                            //lstSarkiListesi.setFastScrollEnabled(snfSarkilar.size() > 25);
                            lstSarkiListesi.setAdapter(adpSarkiListesiLST);
                        }

                        SlidingIslem(0); // Sliding kapat
                        SlidingIslem(2); // Sağ sliding aç

                        if(!JSONSonuc.getString("SecilenSanatciAdi").equals("")) { // Eğer anasayfadaki sanatçıya ait listeyi getir dediysek Liste çağrıldığı zaman sanatçı adı olarak arama yapılıyor ve sadece onun şarkı listesi geliyor..
                            if(RLSarkiListesi_AramaPanel.getVisibility() == View.GONE) // Arama panel kapalıysa açıyoruz.
                                AkorDefterimSys.AramaPanelAc(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                            txtAra_AramaPanel.setText(JSONSonuc.getString("SecilenSanatciAdi"));
                            txtAra_AramaPanel.requestFocus();
                            txtAra_AramaPanel.setSelection(txtAra_AramaPanel.length());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "SarkiGetir":
                    AkorDefterimSys.AramaPanelKapat(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                    SlidingIslem(0); // Sliding kapat
                    AnaEkranProgressIslemDialogKapat();

                    if(JSONSonuc.getBoolean("Sonuc")) {
                        lblSayfaBaslik.setText(String.format("%s%s%s", JSONSonuc.getString("SanatciAdi"), " - ", JSONSonuc.getString("SarkiAdi")));

                        // Burada önce boş fragment ve 300ms sonra ardından sarkı fragment'i çağırdık.
                        // Çünkü aynı fragment tekrar açılırken anlamsız bir hata oluyor. Boş fragment geliyor. Grafikler bozuluyor vs.
                        // Bu yöntemle sorun düzeldi..
                        FragmentSayfaGetir("bossayfa");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Fragment_Sayfa = new Frg_Sarki();// Fragment sayfa belirle
                                    Fragment_SayfaAdi = "Frg_Sarki";

                                    Bundle bundle = new Bundle();
                                    bundle.putInt("SecilenSarkiID", JSONSonuc.getInt("SarkiID"));
                                    bundle.putInt("SecilenListeID", JSONSonuc.getInt("ListeID"));
                                    bundle.putString("SecilenSanatciAdi", JSONSonuc.getString("SanatciAdi"));
                                    bundle.putString("SecilenSarkiAdi", JSONSonuc.getString("SarkiAdi"));
                                    bundle.putString("SecilenSarkiIcerik", JSONSonuc.getString("Icerik"));
                                    bundle.putString("SecilenSarkiVideoURL", JSONSonuc.getString("VideoURL"));

                                    Fragment_Sayfa.setArguments(bundle);

                                    FT = FM.beginTransaction();
                                    FT.replace(R.id.LLSayfa, Fragment_Sayfa);
                                    FT.addToBackStack(null);
                                    FT.commit();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 300);
                    } else AkorDefterimSys.StandartSnackBarMsj(AnaEkran_CoordinatorLayout, getString(R.string.secilen_sarki_bulunamadi));
                    break;
                case "Sarki_Duzenlendi_SnackBarMsj_Goster":
                    if(Fragment_SayfaAdi.equals("Frg_Sarki")) {
                        Frg_Sarki Frg_Sarki = (Frg_Sarki) FM.findFragmentById(R.id.LLSayfa);
                        Frg_Sarki.StandartSnackBarMsj(getString(R.string.sarki_duzenlendi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi, AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi));
                    }

                    AkorDefterimSys.prefAction = "";
                    AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = "";
                    AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = "";
                    AkorDefterimSys.prefEklenenDuzenlenenSarkiID = 0;
                    break;
                case "Frg_Sarki_ADDialog_SarkiSil_Evet":
                    if(Fragment_SayfaAdi.equals("Frg_Sarki")) {
                        Frg_Sarki Frg_Sarki = (Frg_Sarki) FM.findFragmentById(R.id.LLSayfa);
                        Frg_Sarki.SarkiSil();
                        AkorDefterimSys.DismissAlertDialog(Frg_Sarki.ADDialog);
                    }

                    break;
                case "Frg_Sarki_ADDialog_SarkiSil_Hayir":
                    if(Fragment_SayfaAdi.equals("Frg_Sarki")) {
                        Frg_Sarki Frg_Sarki = (Frg_Sarki) FM.findFragmentById(R.id.LLSayfa);
                        AkorDefterimSys.DismissAlertDialog(Frg_Sarki.ADDialog);
                    }

                    break;
                case "DuyuruGetir":
                    if(JSONSonuc.getBoolean("Sonuc")) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date DuyuruTarih1 = format.parse(sharedPref.getString("prefDuyuruTarih", "1990-01-01 00:00:00"));
                        Date DuyuruTarih2 = format.parse(JSONSonuc.getString("Tarih"));

                        if (DuyuruTarih2.getTime() > DuyuruTarih1.getTime()) {
                            if(JSONSonuc.getString("Tip").equals("Yazi")) {
                                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Duyuru)) {
                                    LayoutInflater inflater = activity.getLayoutInflater();
                                    View ViewDialogCustom = inflater.inflate(R.layout.dialog_duyurular, null);

                                    TextView Dialog_lblVersiyonNo = ViewDialogCustom.findViewById(R.id.Dialog_lblVersiyonNo);
                                    Dialog_lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
                                    Dialog_lblVersiyonNo.setText(UygulamaVersiyon);

                                    TextView Dialog_lblDuyuru_Icerik = ViewDialogCustom.findViewById(R.id.Dialog_lblDuyuru_Icerik);
                                    Dialog_lblDuyuru_Icerik.setTypeface(YaziFontu);
                                    Dialog_lblDuyuru_Icerik.setText(JSONSonuc.getString("Icerik"));
                                    AkorDefterimSys.setTextViewHTML(Dialog_lblDuyuru_Icerik);
                                    Dialog_lblDuyuru_Icerik.setMovementMethod(ScrollingMovementMethod.getInstance());

                                    final CheckBox Dialog_chkTekrarGosterme = ViewDialogCustom.findViewById(R.id.Dialog_chkTekrarGosterme);

                                    Button btnDialogButton = ViewDialogCustom.findViewById(R.id.btnDialogButton);
                                    btnDialogButton.setTypeface(YaziFontu, Typeface.BOLD);
                                    btnDialogButton.setText(activity.getString(R.string.tamam));
                                    btnDialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                AkorDefterimSys.DismissAlertDialog(ADDialog_Duyuru);

                                                if (Dialog_chkTekrarGosterme.isChecked()) {
                                                    sharedPrefEditor = sharedPref.edit();
                                                    sharedPrefEditor.putString("prefDuyuruTarih", JSONSonuc.getString("Tarih"));
                                                    sharedPrefEditor.apply();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    ADDialog_Duyuru = new AlertDialog.Builder(activity)
                                            .setView(ViewDialogCustom)
                                            .setCancelable(false)
                                            .create();

                                    ADDialog_Duyuru.setOnKeyListener(new Dialog.OnKeyListener() {
                                        @Override
                                        public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                AkorDefterimSys.DismissAlertDialog(ADDialog_Duyuru);
                                            }
                                            return true;
                                        }
                                    });

                                    ADDialog_Duyuru.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    ADDialog_Duyuru.show();
                                }
                            } else if(JSONSonuc.getString("Tip").equals("Resim")) {

                            }
                        }
                    }
                    break;
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSolMenu:
                SlidingIslem(1);
                break;
            case R.id.btnSagMenu:
                SlidingIslem(2);
                break;
            case R.id.btnAra_AnaPanel:
                AkorDefterimSys.AramaPanelAc(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                break;
            case R.id.btnGeri_AramaPanel:
                AkorDefterimSys.AramaPanelKapat(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                break;
        }
    }
}