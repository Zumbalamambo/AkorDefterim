package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AnaEkran extends AppCompatActivity implements Int_DataConn_AnaEkran, Interface_AsyncResponse, View.OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
    FragmentTransaction FT;
    Fragment Fragment_Sayfa;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
    Typeface YaziFontu;
    ProgressDialog PDIslem;
    AlertDialog ADDialog_InternetBaglantisi, ADDialog_CikisYap, ADDialog_Egitim;
    InputMethodManager imm;
    LayoutInflater layoutInflater;
    private List<SnfSarkilar> snfSarkilar;
    private List<SnfSarkilar> snfSarkilarTemp;

    // AnaEkran Değişken Tanımlamaları
    ImageButton btnSolMenu, btnSagMenu;
    LinearLayout LLSayfa;
    TextView lblSayfaBaslik, lblMenuSag_OrtaMesaj;
    CoordinatorLayout coordinatorLayout;
    ResideMenu resideMenu;

    // Sliding Menü Sağ Değişkenler Tanımlamaları
    CoordinatorLayout coordinatorLayoutSag;
    RelativeLayout RLSarkiListesi_AnaPanel, RLSarkiListesi_AramaPanel;
    ImageButton btnSarkiEkle_AnaPanel, btnAra_AnaPanel, btnGeri_AramaPanel;
    EditText txtAra_AramaPanel;
    FastScroller_Listview lstSarkiListesi;

    // Sliding Menü Sol Değişkenler Tanımlamaları
    CoordinatorLayout coordinatorLayoutSol;
    ViewPager VPSolMenuPager;
    PagerAdapter PagerAdapter;
    PagerSlidingTabStrip PSTTabs;

	String UygulamaVersiyon, Fragment_SayfaAdi;
    boolean CikisIcinCiftTiklandiMi = false;
    int SecilenListeID = 0, SecilenListelemeTipi = 0;

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

        btnSarkiEkle_AnaPanel = ViewSlidingSagMenuContainer.findViewById(R.id.btnSarkiEkle_AnaPanel);
        btnSarkiEkle_AnaPanel.setOnClickListener(this);

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
                                snfSarkilarTemp.add(SarkilarTemp);
                            } else if (snfSarkilar.get(i).getSarkiAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfSarkilar SarkilarTemp = new SnfSarkilar();
                                SarkilarTemp.setId(snfSarkilar.get(i).getId());
                                SarkilarTemp.setListeID(snfSarkilar.get(i).getListeID());
                                SarkilarTemp.setTarzID(snfSarkilar.get(i).getTarzID());
                                SarkilarTemp.setKategoriID(snfSarkilar.get(i).getKategoriID());
                                SarkilarTemp.setSanatciAdi(snfSarkilar.get(i).getSanatciAdi());
                                SarkilarTemp.setSarkiAdi(snfSarkilar.get(i).getSarkiAdi());
                                snfSarkilarTemp.add(SarkilarTemp);
                            }
                        }
                    }

                    if(snfSarkilarTemp.size() <= 0) {
                        btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
                        btnAra_AnaPanel.setEnabled(false);
                        lblMenuSag_OrtaMesaj.setVisibility(View.VISIBLE);
                        lblMenuSag_OrtaMesaj.setText(getString(R.string.liste_bos));
                        lstSarkiListesi.setVisibility(View.GONE);
                    } else {
                        btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
                        btnAra_AnaPanel.setEnabled(true);
                        lblMenuSag_OrtaMesaj.setVisibility(View.GONE);
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
                            AkorDefterimSys.SarkiGetir(veritabani, String.valueOf(SecilenListeID), String.valueOf(snfSarkilar.get(position).getId()), snfSarkilar.get(position).getSanatciAdi(), snfSarkilar.get(position).getSarkiAdi());
                        } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayoutSag, getString(R.string.internet_baglantisi_saglanamadi));
                    } else {
                        AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                        AkorDefterimSys.SarkiGetir(veritabani, String.valueOf(SecilenListeID), String.valueOf(snfSarkilar.get(position).getId()), snfSarkilar.get(position).getSanatciAdi(), snfSarkilar.get(position).getSarkiAdi());
                    }
                } else { // Eğer listeden şarkı arama alanına yazı GİRİLMİŞSE "snfSarkilarTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    if(SecilenListeID == 0) {
                        if(AkorDefterimSys.InternetErisimKontrolu()) {
                            AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                            AkorDefterimSys.SarkiGetir(veritabani, String.valueOf(SecilenListeID), String.valueOf(snfSarkilarTemp.get(position).getId()), snfSarkilarTemp.get(position).getSanatciAdi(), snfSarkilarTemp.get(position).getSarkiAdi());
                        } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayoutSag, getString(R.string.internet_baglantisi_saglanamadi));
                    } else {
                        AnaEkranProgressIslemDialogAc(getString(R.string.icerik_indiriliyor_lutfen_bekleyiniz));
                        AkorDefterimSys.SarkiGetir(veritabani, String.valueOf(SecilenListeID), String.valueOf(snfSarkilarTemp.get(position).getId()), snfSarkilarTemp.get(position).getSanatciAdi(), snfSarkilarTemp.get(position).getSarkiAdi());
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

        coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

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
                AkorDefterimSys.SarkiGetir(veritabani, "0", String.valueOf(extras.getInt("SarkiID")), extras.getString("SanatciAdi"), extras.getString("SarkiAdi"));
                break;
            default:
                FragmentSayfaGetir("anasayfa");
                break;
        }
	}

    @Override
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
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Görünüm hiyerarşisini tekrar yüklemek için superclass olarak çağrılır.
        super.onRestoreInstanceState(savedInstanceState);
        // Bilgileri geri yükle
        FragmentSayfaGetir(savedInstanceState.getString("SayfaAdi"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;

        if(AkorDefterimSys.prefAction.equals("Şarkı eklendi")) {
            if(Fragment_SayfaAdi.equals("Frg_Sarki")) {
                Frg_Sarki Frg_Sarki = (Frg_Sarki) activity.getFragmentManager().findFragmentByTag(Fragment_SayfaAdi);
                Frg_Sarki.StandartSnackBarMsj(getString(R.string.sarki_eklendi, AkorDefterimSys.prefEklenenDuzenlenenSanatciAdiSarkiAdi));
            }

            AkorDefterimSys.prefAction = "";
            AkorDefterimSys.prefEklenenDuzenlenenSanatciAdiSarkiAdi = "";
        } else if(AkorDefterimSys.prefAction.equals("Şarkı düzenlendi")) {
            if(Fragment_SayfaAdi.equals("Frg_Sarki")) {
                Frg_Sarki Frg_Sarki = (Frg_Sarki) activity.getFragmentManager().findFragmentByTag(Fragment_SayfaAdi);
                Frg_Sarki.StandartSnackBarMsj(getString(R.string.sarki_duzenlendi));
            }
        }

        Frg_TabRepKontrol Frg_TabRepKontrol_1 = (Frg_TabRepKontrol) SlidingTabFragmentClassGetir(getString(R.string.tabsayfa_repertuvar_kontrol));
        if(Frg_TabRepKontrol_1 != null) Frg_TabRepKontrol_1.spnDoldur();

        if(sharedPref.getBoolean("prefEgitimTamamlandiMi", false)) AkorDefterimSys.YeniSurumYeniliklerDialog();
        else {
            if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_Egitim)) {
                ADDialog_Egitim = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
                        getString(R.string.egitim),
                        getString(R.string.egitime_katilmak_istermisin),
                        getString(R.string.hayir),
                        "ADDialog_Egitim_Hayir",
                        getString(R.string.evet),
                        "ADDialog_Egitim_Evet");
                ADDialog_Egitim.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                ADDialog_Egitim.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (RLSarkiListesi_AramaPanel.getVisibility() == View.VISIBLE) {
            AkorDefterimSys.AramaPanelKapat(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);
        } else if(resideMenu.isOpened()) {
            SlidingIslem(0);
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
                //noinspection ObjectEqualsNull
                if(Fragment_SayfaAdi != "Frg_Anasayfa") {
                    Fragment_Sayfa = new Frg_Anasayfa();// Fragment sayfa belirle
                    Fragment_SayfaAdi = "Frg_Anasayfa";
                    lblSayfaBaslik.setText(String.format("%s%s%s", getString(R.string.anasayfa), " - ", getString(R.string.son_eklenen_sarkilar)));

                    if(FT == null && Fragment_Sayfa == null)
                        FT.add(R.id.LLSayfa, Fragment_Sayfa, Fragment_SayfaAdi); // FT.add(<Hangi layout içinde çağırılacaksa id'si>, <Çağırılan Fragment>, <Çağırılan Fragment Takma Adı>);
                    else {
                        assert FT != null;
                        FT.remove(Fragment_Sayfa); // Geçerli fragment'i sil
                        FT = activity.getFragmentManager().beginTransaction(); // Fragment methodunu kullanmak için sabit nesne
                        FT.replace(R.id.LLSayfa, Fragment_Sayfa, Fragment_SayfaAdi); // FT.add(<Hangi layout içinde çağırılacaksa id'si>, <Çağırılan Fragment>, <Çağırılan Fragment Takma Adı>);
                    }

                    FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    FT.commit();// Çağırma işlemini yaptırıyoruz..
                }

                break;
            case "hesabim": // Hesabım
                if(AkorDefterimSys.GirisYapildiMi()) {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        AkorDefterimSys.EkranGetir(new Intent(activity, Hesabim.class), "Slide");
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.devam_etmek_icin_giris_yapmalisin));
                break;
            case "girisyap": // Giriş Yap
                AkorDefterimSys.CikisYap();

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

                    FT = activity.getFragmentManager().beginTransaction(); // Fragment methodunu kullanmak için sabit nesne
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
                resideMenu.closeMenu();

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
    public void StandartSnackBarMsj(String Mesaj) {
        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, Mesaj);
    }

    @Override
    public void StandartSnackBarMsj(CoordinatorLayout mCoordinatorLayout, String Mesaj) {
        AkorDefterimSys.StandartSnackBarMsj(mCoordinatorLayout, Mesaj);
    }

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
                case "ADDialog_Egitim_Evet":
                    AkorDefterimSys.DismissAlertDialog(ADDialog_Egitim);
                    AkorDefterimSys.EkranGetir(new Intent(activity, Egitim.class), "Slide");

                    sharedPrefEditor = sharedPref.edit();
                    sharedPrefEditor.putBoolean("prefEgitimTamamlandiMi", true);
                    sharedPrefEditor.apply();
                    break;
                case "ADDialog_Egitim_Hayir":
                    AkorDefterimSys.DismissAlertDialog(ADDialog_Egitim);

                    sharedPrefEditor = sharedPref.edit();
                    sharedPrefEditor.putBoolean("prefEgitimTamamlandiMi", true);
                    sharedPrefEditor.apply();

                    AkorDefterimSys.YeniSurumYeniliklerDialog();
                    break;
                case "ADDialog_InternetBaglantisi_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog_InternetBaglantisi);
                    break;
                case "ADDialog_CikisYap_Iptal":
                    AkorDefterimSys.DismissAlertDialog(ADDialog_CikisYap);
                    break;
                case "ADDialog_CikisYap_CikisYap":
                    AkorDefterimSys.DismissAlertDialog(ADDialog_CikisYap);

                    AkorDefterimSys.CikisYap();
                    break;
                case "PDIslem_Timeout":
                    AnaEkranProgressIslemDialogKapat();
                    break;
                case "AnasayfaGetir":
                    if(Fragment_SayfaAdi.equals("Frg_Anasayfa")) {
                        Frg_Anasayfa Frg_Anasayfa = (Frg_Anasayfa) activity.getFragmentManager().findFragmentByTag(Fragment_SayfaAdi);

                        if(JSONSonuc.getBoolean("Sonuc")) Frg_Anasayfa.AnasayfaDoldur(JSONSonuc.getString("SarkiListesi"));
                        else Frg_Anasayfa.AnasayfaDoldur("");
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

                    // Secilen Listeleme Tipini integer olarak düzenliyoruz. Çünkü Harf sıralaması fonksiyonu bizden integer istiyor..
                    SecilenListelemeTipi = JSONSonuc.getInt("ListelemeTipi");
                    SecilenListeID = JSONSonuc.getInt("ListeID");

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
                            snfSarkilar.add(Sarki);
                        }

                        // Yalnızca şarkıcı adına göre ya da şarkı adına göre listelendiğinde alfabetik sıraya diz..
                        if (SecilenListelemeTipi == 0 || SecilenListelemeTipi == 1) Collections.sort(snfSarkilar, new SnfRepertuvarComparatorRepertuvar());

                        if(snfSarkilar.size() <= 0) {
                            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
                            btnAra_AnaPanel.setEnabled(false);
                            lblMenuSag_OrtaMesaj.setVisibility(View.VISIBLE);
                            lblMenuSag_OrtaMesaj.setText(getString(R.string.liste_bos));
                            lstSarkiListesi.setVisibility(View.GONE);
                        } else {
                            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
                            btnAra_AnaPanel.setEnabled(true);
                            lblMenuSag_OrtaMesaj.setVisibility(View.GONE);
                            lstSarkiListesi.setVisibility(View.VISIBLE);

                            // Elde ettiğimiz snfSarkilar sıfındaki tüm kayıtları AdpSarkiListesiLST ile lstSarkiListesi isimli Listview'a set ediyoruz..
                            // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                            AdpSarkiListesiLST adpSarkiListesiLST = new AdpSarkiListesiLST(activity, snfSarkilar, snfSarkilar.size() > 25, SecilenListelemeTipi);

                            //lstSarkiListesi.setFastScrollEnabled(snfSarkilar.size() > 25);
                            lstSarkiListesi.setAdapter(adpSarkiListesiLST);
                        }

                        SlidingIslem(0); // Sliding kapat
                        SlidingIslem(2); // Sağ sliding aç
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "SarkiGetir":
                    AkorDefterimSys.AramaPanelKapat(RLSarkiListesi_AnaPanel.getId(),RLSarkiListesi_AramaPanel.getId(),txtAra_AramaPanel, imm);

                    SlidingIslem(0); // Sliding kapat
                    AnaEkranProgressIslemDialogKapat();

                    if(JSONSonuc.getBoolean("Sonuc")) {
                        if(FT == null && Fragment_Sayfa == null) {
                            // Fragment oluşturuyoruz ve ilk hangi fragment çağırılacaksa onu çağırıyoruz..
                            FT = getFragmentManager().beginTransaction(); // Fragment methodunu kullanmak için sabit nesne

                            Fragment_Sayfa = new Frg_Sarki();// Fragment sayfa belirle
                            Fragment_SayfaAdi = "Frg_Sarki";
                            lblSayfaBaslik.setText(String.format("%s%s%s", JSONSonuc.getString("SanatciAdi"), " - ", JSONSonuc.getString("SarkiAdi")));

                            Bundle bundle = new Bundle();
                            bundle.putInt("SecilenSarkiID", JSONSonuc.getInt("SarkiID"));
                            bundle.putInt("SecilenListeID", JSONSonuc.getInt("ListeID"));
                            bundle.putString("SecilenSanatciAdi", JSONSonuc.getString("SanatciAdi"));
                            bundle.putString("SecilenSarkiAdi", JSONSonuc.getString("SarkiAdi"));
                            bundle.putString("SecilenSarkiIcerik", JSONSonuc.getString("Icerik"));

                            Fragment_Sayfa.setArguments(bundle);

                            FT.add(R.id.LLSayfa, Fragment_Sayfa, Fragment_SayfaAdi); // FT.add(<Hangi layout içinde çağırılacaksa id'si>, <Çağırılan Fragment>, <Çağırılan Fragment Takma Adı>);
                            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            FT.commit();// Çağırma işlemini yaptırıyoruz..
                        } else {
                            assert FT != null;
                            FT.remove(Fragment_Sayfa); // Geçerli fragment'i sil

                            Fragment_Sayfa = new Frg_Sarki();// Fragment sayfa belirle
                            Fragment_SayfaAdi = "Frg_Sarki";
                            lblSayfaBaslik.setText(String.format("%s%s%s", JSONSonuc.getString("SanatciAdi"), " - ", JSONSonuc.getString("SarkiAdi")));

                            Bundle bundle = new Bundle();
                            bundle.putInt("SecilenSarkiID", JSONSonuc.getInt("SarkiID"));
                            bundle.putInt("SecilenListeID", JSONSonuc.getInt("ListeID"));
                            bundle.putString("SecilenSanatciAdi", JSONSonuc.getString("SanatciAdi"));
                            bundle.putString("SecilenSarkiAdi", JSONSonuc.getString("SarkiAdi"));
                            bundle.putString("SecilenSarkiIcerik", JSONSonuc.getString("Icerik"));

                            Fragment_Sayfa.setArguments(bundle);

                            FT = activity.getFragmentManager().beginTransaction(); // Fragment methodunu kullanmak için sabit nesne
                            FT.replace(R.id.LLSayfa, Fragment_Sayfa, Fragment_SayfaAdi); // FT.add(<Hangi layout içinde çağırılacaksa id'si>, <Çağırılan Fragment>, <Çağırılan Fragment Takma Adı>);
                            FT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            FT.commit();// Çağırma işlemini yaptırıyoruz..
                        }
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.secilen_sarki_bulunamadi));
                    break;
            }
        } catch (JSONException e) {
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
            case R.id.btnSarkiEkle_AnaPanel:
                //veritabani.VeritabaniSifirla();
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