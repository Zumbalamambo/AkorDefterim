package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

public class Egitim extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private AkorDefterimSys AkorDefterimSys;
    InputMethodManager imm;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    Typeface YaziFontu;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] sayfalar;
    private Button btnSonraki, btnGec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egitim);

        activity = this;
        AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
        YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

        sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

        AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
        //AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
        //AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        viewPager = findViewById(R.id.view_pager);

        dotsLayout = findViewById(R.id.layoutDots);

        btnSonraki = findViewById(R.id.btnSonraki);
        btnSonraki.setTypeface(YaziFontu, Typeface.BOLD);
        btnSonraki.setOnClickListener(this);

        btnGec = findViewById(R.id.btnGec);
        btnGec.setTypeface(YaziFontu, Typeface.BOLD);
        btnGec.setOnClickListener(this);

        sayfalar = new int[]{
                R.layout.activity_egitim_sayfa1,
                R.layout.activity_egitim_sayfa2,
                R.layout.activity_egitim_sayfa3,
                R.layout.activity_egitim_sayfa4,
                R.layout.activity_egitim_sayfa5,
                R.layout.activity_egitim_sayfa6,
                R.layout.activity_egitim_sayfa7,
                R.layout.activity_egitim_sayfa8,
                R.layout.activity_egitim_sayfa9,
                R.layout.activity_egitim_sayfa10,
                R.layout.activity_egitim_sayfa11};

        // adding bottom dots
        addBottomDots(0);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AkorDefterimSys.activity = activity;
        AkorDefterimSys.SharePrefAyarlarınıUygula();
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[sayfalar.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == sayfalar.length - 1) {
                // last page. make button text to GOT IT
                btnSonraki.setText(getString(R.string.basla));
                btnGec.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnSonraki.setText(getString(R.string.ileri));
                btnGec.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSonraki:
                int ToplamSayfaSayisi = sayfalar.length;
                int SayfaNo = getItem(+1); // Geçerli sayfa no

                // Son sayfada olup olmadığına bakıyoruz
                if (SayfaNo < ToplamSayfaSayisi) {
                    // Sonraki sayfa
                    viewPager.setCurrentItem(SayfaNo);
                } else {
                    AkorDefterimSys.EkranKapat();
                }

                break;
            case R.id.btnGec:
                AkorDefterimSys.EkranKapat();

                break;
        }
    }

    private class MyViewPagerAdapter extends android.support.v4.view.PagerAdapter {
        private LayoutInflater layoutInflater;
        private Typeface YaziFontu;

        private MyViewPagerAdapter() {
            YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(sayfalar[position], container, false);

            switch (position) {
                case 0:
                    TextView lblegitim_1_baslik = view.findViewById(R.id.lblegitim_1_baslik);
                    lblegitim_1_baslik.setTypeface(YaziFontu, Typeface.NORMAL);

                    TextView lblegitim_1_icerik = view.findViewById(R.id.lblegitim_1_icerik);
                    lblegitim_1_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    lblegitim_1_icerik.setText(getString(R.string.egitim_1_icerik, getString(R.string.uygulama_adi)));
                    AkorDefterimSys.setTextViewHTML(lblegitim_1_icerik);
                    break;
                case 1:
                    TextView lblegitim_2_baslik = view.findViewById(R.id.lblegitim_2_baslik);
                    lblegitim_2_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_2_icerik = view.findViewById(R.id.lblegitim_2_icerik);
                    lblegitim_2_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    lblegitim_2_icerik.setText(getString(R.string.egitim_2_icerik, getString(R.string.uygulama_adi)));
                    AkorDefterimSys.setTextViewHTML(lblegitim_2_icerik);
                    break;
                case 2:
                    TextView lblegitim_3_baslik = view.findViewById(R.id.lblegitim_3_baslik);
                    lblegitim_3_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_3_icerik = view.findViewById(R.id.lblegitim_3_icerik);
                    lblegitim_3_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_3_icerik);
                    break;
                case 3:
                    TextView lblegitim_4_baslik = view.findViewById(R.id.lblegitim_4_baslik);
                    lblegitim_4_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_4_icerik = view.findViewById(R.id.lblegitim_4_icerik);
                    lblegitim_4_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_4_icerik);
                    break;
                case 4:
                    TextView lblegitim_5_baslik = view.findViewById(R.id.lblegitim_5_baslik);
                    lblegitim_5_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_5_icerik = view.findViewById(R.id.lblegitim_5_icerik);
                    lblegitim_5_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_5_icerik);
                    break;
                case 5:
                    TextView lblegitim_6_baslik = view.findViewById(R.id.lblegitim_6_baslik);
                    lblegitim_6_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_6_icerik = view.findViewById(R.id.lblegitim_6_icerik);
                    lblegitim_6_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_6_icerik);
                    break;
                case 6:
                    TextView lblegitim_7_baslik = view.findViewById(R.id.lblegitim_7_baslik);
                    lblegitim_7_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_7_icerik = view.findViewById(R.id.lblegitim_7_icerik);
                    lblegitim_7_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_7_icerik);
                    break;
                case 7:
                    TextView lblegitim_8_baslik = view.findViewById(R.id.lblegitim_8_baslik);
                    lblegitim_8_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_8_icerik = view.findViewById(R.id.lblegitim_8_icerik);
                    lblegitim_8_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_8_icerik);
                    break;
                case 8:
                    TextView lblegitim_9_baslik = view.findViewById(R.id.lblegitim_9_baslik);
                    lblegitim_9_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_9_icerik = view.findViewById(R.id.lblegitim_9_icerik);
                    lblegitim_9_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_9_icerik);
                    break;
                case 9:
                    TextView lblegitim_10_baslik = view.findViewById(R.id.lblegitim_10_baslik);
                    lblegitim_10_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_10_icerik = view.findViewById(R.id.lblegitim_10_icerik);
                    lblegitim_10_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_10_icerik);
                    break;
                case 10:
                    TextView lblegitim_11_baslik = view.findViewById(R.id.lblegitim_11_baslik);
                    lblegitim_11_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_11_icerik = view.findViewById(R.id.lblegitim_11_icerik);
                    lblegitim_11_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    AkorDefterimSys.setTextViewHTML(lblegitim_11_icerik);
                    break;
            }


            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return sayfalar.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}