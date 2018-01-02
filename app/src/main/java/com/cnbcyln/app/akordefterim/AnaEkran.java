package com.cnbcyln.app.akordefterim;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.SlideNav.SlidingRootNav;
import com.cnbcyln.app.akordefterim.util.SlideNav.SlidingRootNavLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class AnaEkran extends AppCompatActivity implements Interface_FragmentDataConn, Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

    FragmentTransaction FT;
    Fragment Fragment_Sayfa;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
    SlidingRootNav mSlidingRootNavSol, mSlidingRootNavSag;
    Typeface YaziFontu;
    AlertDialog ADDialog_InternetBaglantisi, ADDialog_CikisYap;

    ImageButton btnSolMenu, btnSagMenu;
    LinearLayout LLSayfa;
    TextView lblSayfaBaslik;
    ViewPager VPSolMenuPager;
    PagerAdapter PagerAdapter;
    PagerSlidingTabStrip PSTTabs;
    CoordinatorLayout coordinatorLayout;

	String UygulamaVersiyon, Fragment_SayfaAdi;
    boolean CikisIcinCiftTiklandiMi = false;

	@SuppressWarnings("ConstantConditions")
    @SuppressLint({"InflateParams", "CommitTransaction", "ClickableViewAccessibility"})
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
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");
		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        try {
            UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        FT = getFragmentManager().beginTransaction(); // Fragment methodunu kullanmak için sabit nesne

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /* **************************************
           ***                                ***
           ***         Sliding Menü Sag       ***
           ***                                ***
           **************************************/

        View ViewSlidingSagMenuContainer = layoutInflater.inflate(R.layout.menu_sag, null, false);
        ViewSlidingSagMenuContainer.setTag("R.layout.menu_sag");

        Button button2 = ViewSlidingSagMenuContainer.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                AkorDefterimSys.ToastMsj(activity, "naber", Toast.LENGTH_SHORT);
            }
        });

        mSlidingRootNavSag = AkorDefterimSys.SetupSlidingMenu(savedInstanceState, ViewSlidingSagMenuContainer, true, true);
        ((SlidingRootNavLayout) mSlidingRootNavSag).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!mSlidingRootNavSol.isMenuHidden()) mSlidingRootNavSag.closeMenu();
                return false;
            }
        });

        /* **************************************
           ***                                ***
           ***         Sliding Menü Sol       ***
           ***                                ***
           **************************************/

        View ViewSlidingSolMenuContainer = layoutInflater.inflate(R.layout.menu_sol, null, false);
        ViewSlidingSolMenuContainer.setTag("R.layout.menu_sol");

        TextView lblVersiyonNo = ViewSlidingSolMenuContainer.findViewById(R.id.lblVersiyonNo);
        lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
        lblVersiyonNo.setText(String.valueOf("v").concat(UygulamaVersiyon));

        mSlidingRootNavSol = AkorDefterimSys.SetupSlidingMenu(savedInstanceState, ViewSlidingSolMenuContainer, true, true);
        ((SlidingRootNavLayout) mSlidingRootNavSol).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!mSlidingRootNavSag.isMenuHidden()) mSlidingRootNavSol.closeMenu();
                return false;
            }
        });

        /* **************************************
           ***                                ***
           ***       Genel Tanımlamalar       ***
           ***                                ***
           **************************************/

        coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

        btnSolMenu = findViewById(R.id.btnSolMenu);
        btnSolMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                if(mSlidingRootNavSol.isMenuHidden()) mSlidingRootNavSol.openMenu(true);
                else mSlidingRootNavSol.closeMenu(true);
            }
        });

        btnSagMenu = findViewById(R.id.btnSagMenu);
        btnSagMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                if(mSlidingRootNavSag.isMenuHidden()) mSlidingRootNavSag.openMenu(true);
                else mSlidingRootNavSag.closeMenu(true);
            }
        });

        lblSayfaBaslik = findViewById(R.id.lblSayfaBaslik);
        lblSayfaBaslik.setTypeface(YaziFontu, Typeface.NORMAL);

        LLSayfa = findViewById(R.id.LLSayfa);

        VPSolMenuPager = findViewById(R.id.VPSolMenuPager);

        PagerAdapter = new PagerAdapter(activity, getSupportFragmentManager());

        VPSolMenuPager.setAdapter(PagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        VPSolMenuPager.setPageMargin(pageMargin);

        PSTTabs = findViewById(R.id.PSTTabs);
        PSTTabs.setViewPager(VPSolMenuPager);
        PSTTabs.setTextColor(R.color.KoyuYazi);
        PSTTabs.setTypeface(YaziFontu, Typeface.BOLD);

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
                FragmentSarkiSayfaGetir(extras.getInt("SarkiID"), extras.getString("SarkiAdi"), extras.getString("SanatciAdi"), "AkorDefterim");
                break;
            default:
                FragmentSayfaGetir("anasayfa");
                break;
        }
	}

    @Override
    public void onBackPressed() {
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

        //super.onBackPressed();
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
                    lblSayfaBaslik.setText(getString(R.string.anasayfa).concat(" - " + getString(R.string.son_eklenen_sarkilar)));

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
        }
    }

    @Override
    public void FragmentSarkiSayfaGetir(int SarkiID, String SarkiAdi, String SanatciAdi, String DepolamaAlani) {

    }

    @Override
    public void SarkiListesiDoldur() {

    }

    @Override
    public void SarkiListesiDoldur(int SecilenListeID, int SecilenKategoriID, int SecilenTarzID, int SecilenListelemeTipi) {

    }

    @Override
    public void HesapCikisYap(JSONObject CikisData) {

    }

    @Override
    public void SecilenSarkiIcerikAl(String Icerik) {

    }

    @Override
    public void WebYonetimServisiDurumGuncelle(boolean Durum) {

    }

    @Override
    public void WebYonetimServisiYenidenBaslat() {

    }

    @Override
    public void SlidingIslem(int islem) {
        switch (islem) {
            case 0: //Sliding kapatır
                if(!mSlidingRootNavSol.isMenuHidden()) mSlidingRootNavSol.closeMenu(true);
                else if(!mSlidingRootNavSag.isMenuHidden()) mSlidingRootNavSag.closeMenu(true);

                break;
            case 1: //Sol sliding açar
                if (!mSlidingRootNavSag.isMenuHidden()) mSlidingRootNavSag.closeMenu(true);
                mSlidingRootNavSol.openMenu(true);

                break;
            case 2: //Sağ sliding açar
                if(!mSlidingRootNavSol.isMenuHidden()) mSlidingRootNavSol.closeMenu(true);
                mSlidingRootNavSag.openMenu(true);

                break;
        }
    }

    @Override
    public android.support.v4.app.Fragment SlidingTabFragmentClassGetir(String FragmentAdi) {
        return null;
    }

    @Override
    public void SarkiListesi_SecimPanelGuncelle(Boolean Durum) {

    }

    @Override
    public void SarkiListesi_SecimPanelBilgiGuncelle() {

    }

    @Override
    public void YayinAraci_KonumBilgileriThread(Boolean Durum) {

    }

    @Override
    public void YayinAraci_CanliIstek(Boolean Durum) {

    }

    @Override
    public void YayinAraciDurumBilgisiAl() {

    }

    @Override
    public void GoogleSatinAlma() {

    }

    @Override
    public void SarkiPaylas(String Platform, String Baslik, String Icerik, String URL) {

    }

    void enterReveal(LinearLayout LLMenu) {
		// get the center for the clipping circle
		int cx = LLMenu.getMeasuredWidth() / 2;
		int cy = LLMenu.getMeasuredHeight() / 2;

		// get the final radius for the clipping circle
		int finalRadius = Math.max(LLMenu.getWidth(), LLMenu.getHeight()) / 2;

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			// create the animator for this view (the start radius is zero)
			Animator anim = ViewAnimationUtils.createCircularReveal(LLMenu, cx, cy, 0, finalRadius);
			anim.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {

				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			});

			anim.addPauseListener(new Animator.AnimatorPauseListener() {
				@Override
				public void onAnimationPause(Animator animation) {

				}

				@Override
				public void onAnimationResume(Animator animation) {

				}
			});

			anim.setDuration(1000);

			// make the view visible and start the animation
			LLMenu.setVisibility(View.VISIBLE);
			anim.start();
		}
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}