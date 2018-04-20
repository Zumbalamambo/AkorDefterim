package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpAkorlar;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTonlar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfAkorlar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTonlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class AkorCetveli extends AppCompatActivity implements OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	TextView lblBaslik, lblTon, lblAkor;
	ListView lstTonlar, lstAkorlar;
    WebView WVGitarAkor;

	String SecilenAkor = "", SecilenTon = "", SecilenAkorAdi = "", SecilenAkorDizisi = "";
	int HtmlAkorSemaBoyutu = 0;
    Boolean AkorBulunduMu = false;
    List<SnfAkorlar> snfAkorlar = new ArrayList<>();
    AdpTonlar AdpTonlar = null;
    AdpAkorlar AdpAkorlar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_akor_cetveli);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        HtmlAkorSemaBoyutu = getResources().getInteger(R.integer.HtmlAkorSemaBoyutu);
        AkorDefterimSys.SonYapilanIslemGuncelle("akorcetveli_ekranina_giris_yapildi", "[]");

		coordinatorLayout = findViewById(R.id.coordinatorLayout);

        btnGeri = findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

        lblTon = findViewById(R.id.lblTon);
        lblTon.setTypeface(YaziFontu, Typeface.BOLD);

        lstTonlar = findViewById(R.id.lstTonlar);

        lblAkor = findViewById(R.id.lblAkor);
        lblAkor.setTypeface(YaziFontu, Typeface.BOLD);

        lstAkorlar = findViewById(R.id.lstAkorlar);

        WVGitarAkor = findViewById(R.id.WVGitarAkor);
        WVGitarAkor.getSettings().setJavaScriptEnabled(true);
        WVGitarAkor.getSettings().setBuiltInZoomControls(true);
        WVGitarAkor.getSettings().setDisplayZoomControls(false);

        //ImgGitarKlavye = findViewById(R.id.ImgGitarKlavye);

        Bundle mBundle = getIntent().getExtras();
        SecilenAkor = mBundle.getString("SecilenAkor", "A");

        lstTonlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
                AdpTonlar.setSelectable(position);

                try {
                    snfAkorlar.clear();
                    SnfAkorlar Akorlar;
                    Boolean IlkAkorSecildiMi = false;

                    JSONArray JSONArrAkorlar = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(AkorDefterimSys.JSONTonAkorGetir(position)).getString("AkorCetveli")).getString("Tonlar")).getString(0)).getString("Akorlari"));

                    for(int i = 0; i < JSONArrAkorlar.length(); i++) {
                        Akorlar = new SnfAkorlar();
                        Akorlar.setAkorAdi(new JSONObject(JSONArrAkorlar.getString(i)).getString("AkorAdi"));
                        Akorlar.setDizi(new JSONObject(JSONArrAkorlar.getString(i)).getString("Dizi"));
                        Akorlar.setSecimBG(true);

                        if(!IlkAkorSecildiMi) {
                            Akorlar.setSecimYazi(true);
                            IlkAkorSecildiMi = true;

                            SecilenAkorAdi = Akorlar.getAkorAdi();
                            SecilenAkorDizisi = Akorlar.getDizi().replace("[","").replace("]","");
                        } else Akorlar.setSecimYazi(false);

                        snfAkorlar.add(Akorlar);
                    }

                    AdpAkorlar = new AdpAkorlar(activity, snfAkorlar);
                    lstAkorlar.setAdapter(AdpAkorlar);

                    SecilenAkorAdi = SecilenAkorAdi.replace("#", "Diyez");

                    // akoradi = Üstteki akor adını yazdırır
                    // dizi = Akora ait dizi
                    // parmaklar = Şema altında parmak pozisyonunu yazdırır
                    // boyut = Şema boyutunu belirler
                    WVGitarAkor.loadUrl(AkorDefterimSys.AkorHtmlURLAssets + "?akoradi=" + SecilenAkorAdi + "&dizi=" + SecilenAkorDizisi + "&boyut=" + HtmlAkorSemaBoyutu + "&aciklamalarbaslik=" + getString(R.string.aciklamalar) + "&fraciklama=" + getString(R.string.fred));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        lstAkorlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
                AdpAkorlar.setSelectableYazi(position);

                SecilenAkorAdi = snfAkorlar.get(position).getAkorAdi();
                SecilenAkorDizisi = snfAkorlar.get(position).getDizi().replace("[","").replace("]","");

                SecilenAkorAdi = SecilenAkorAdi.replace("#", "Diyez");

                // akoradi = Üstteki akor adını yazdırır
                // dizi = Akora ait dizi
                // parmaklar = Şema altında parmak pozisyonunu yazdırır
                // boyut = Şema boyutunu belirler
                WVGitarAkor.loadUrl(AkorDefterimSys.AkorHtmlURLAssets + "?akoradi=" + SecilenAkorAdi + "&dizi=" + SecilenAkorDizisi + "&boyut=" + HtmlAkorSemaBoyutu + "&aciklamalarbaslik=" + getString(R.string.aciklamalar) + "&fraciklama=" + getString(R.string.fred));
            }
        });
	}

    @Override
    protected void onStart() {
        super.onStart();
        AkorDefterimSys.activity = activity;

        AkorDefterimSys.SharePrefAyarlarınıUygula();

        if(!SecilenAkor.equals("")) {
            if(SecilenAkor.length() < 2) SecilenTon = SecilenAkor;
            else SecilenTon = (SecilenAkor.substring(0,2).contains("#") || SecilenAkor.substring(0,2).contains("b")) ? SecilenAkor.substring(0,2) : SecilenAkor.substring(0,1);
        } else SecilenTon = "";

        try {
            JSONArray JSONArrTonlar;
            List<SnfTonlar> snfTonlar = new ArrayList<>();
            SnfTonlar Tonlar;

            for(int x = 0; x < 17; x++) {
                JSONArrTonlar = new JSONArray(new JSONObject(new JSONObject(AkorDefterimSys.JSONTonAkorGetir(x)).getString("AkorCetveli")).getString("Tonlar"));

                for(int i = 0; i < JSONArrTonlar.length(); i++) {
                    Tonlar = new SnfTonlar();
                    Tonlar.setTonAdi(new JSONObject(JSONArrTonlar.getString(i)).getString("TonAdi"));

                    if(SecilenTon.equals(new JSONObject(JSONArrTonlar.getString(i)).getString("TonAdi"))) Tonlar.setSecim(true);
                    else Tonlar.setSecim(false);

                    snfTonlar.add(Tonlar);
                }
            }

            AdpTonlar = new AdpTonlar(activity, snfTonlar);
            lstTonlar.setAdapter(AdpTonlar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            snfAkorlar.clear();
            SnfAkorlar Akorlar;
            Boolean IlkAkorSecildiMi = false;

            JSONArray JSONArrTonlar;
            JSONArray JSONArrAkorlar = null;

            for(int x = 0; x < 17; x++) {
                JSONArrTonlar = new JSONArray(new JSONObject(new JSONObject(AkorDefterimSys.JSONTonAkorGetir(x)).getString("AkorCetveli")).getString("Tonlar"));

                for(int i = 0; i < JSONArrTonlar.length(); i++) {
                    if(new JSONObject(JSONArrTonlar.getString(i)).getString("TonAdi").equals(SecilenTon)) {
                        JSONArrAkorlar = new JSONArray(new JSONObject(JSONArrTonlar.getString(i)).getString("Akorlari"));
                        AkorBulunduMu = true;
                    }
                }
            }

            for(int i = 0; i < JSONArrAkorlar.length(); i++) {
                Akorlar = new SnfAkorlar();
                Akorlar.setAkorAdi(new JSONObject(JSONArrAkorlar.getString(i)).getString("AkorAdi"));
                Akorlar.setDizi(new JSONObject(JSONArrAkorlar.getString(i)).getString("Dizi"));

                if(!SecilenAkor.equals("") && AkorBulunduMu) Akorlar.setSecimBG(true);
                else Akorlar.setSecimBG(false);

                if(SecilenAkor.equals(new JSONObject(JSONArrAkorlar.getString(i)).getString("AkorAdi"))) {
                    if(!IlkAkorSecildiMi) {
                        Akorlar.setSecimYazi(true);
                        IlkAkorSecildiMi = true;

                        SecilenAkorAdi = Akorlar.getAkorAdi();
                        SecilenAkorDizisi = Akorlar.getDizi().replace("[","").replace("]","");
                    } else Akorlar.setSecimYazi(false);
                } else Akorlar.setSecimYazi(false);

                snfAkorlar.add(Akorlar);
            }

            AdpAkorlar = new AdpAkorlar(activity, snfAkorlar);
            lstAkorlar.setAdapter(AdpAkorlar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SecilenAkorAdi = SecilenAkorAdi.replace("#", "Diyez");

        // akoradi = Üstteki akor adını yazdırır
        // dizi = Akora ait dizi
        // parmaklar = Şema altında parmak pozisyonunu yazdırır
        // boyut = Şema boyutunu belirler
        WVGitarAkor.loadUrl(AkorDefterimSys.AkorHtmlURLAssets + "?akoradi=" + SecilenAkorAdi + "&dizi=" + SecilenAkorDizisi + "&boyut=" + HtmlAkorSemaBoyutu + "&aciklamalarbaslik=" + getString(R.string.aciklamalar) + "&fraciklama=" + getString(R.string.fred));

        for(int i = 0;i < snfAkorlar.size();i ++) {
            if(snfAkorlar.get(i).getSecimYazi()) {
                lstAkorlar.setSelection(i);
            }
        }
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				AkorDefterimSys.EkranKapat();
				break;
		}
	}
}