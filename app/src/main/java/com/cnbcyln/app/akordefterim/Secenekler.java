package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

@SuppressWarnings("ALL")
public class Secenekler extends AppCompatActivity implements OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	Typeface YaziFontu;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnEkranIsigi, btnRepertuvarIslemleri, btnEgitim, btnYardimMerkezi, btnSorunBildir, btnOyVer, btnGizlilikIlkesi, btnHizmetKosullari, btnAcikKaynakKutuphaneleri;
	TextView lblBaslik, lblAyarlar, lblDestek, lblHakkinda;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secenekler);

        activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        btnGeri = findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

        lblAyarlar = findViewById(R.id.lblAyarlar);
        lblAyarlar.setTypeface(YaziFontu, Typeface.BOLD);

        btnEkranIsigi = findViewById(R.id.btnEkranIsigi);
        btnEkranIsigi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnEkranIsigi.setOnClickListener(this);

        btnRepertuvarIslemleri = findViewById(R.id.btnRepertuvarIslemleri);
        btnRepertuvarIslemleri.setTypeface(YaziFontu, Typeface.NORMAL);
        btnRepertuvarIslemleri.setOnClickListener(this);

        lblDestek = findViewById(R.id.lblDestek);
        lblDestek.setTypeface(YaziFontu, Typeface.BOLD);

        btnEgitim = findViewById(R.id.btnEgitim);
        btnEgitim.setTypeface(YaziFontu, Typeface.NORMAL);
        btnEgitim.setOnClickListener(this);

        btnYardimMerkezi = findViewById(R.id.btnYardimMerkezi);
        btnYardimMerkezi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnYardimMerkezi.setText(getString(R.string.s_yardim_merkezi, getString(R.string.uygulama_adi)));
        btnYardimMerkezi.setOnClickListener(this);

        btnSorunBildir = findViewById(R.id.btnSorunBildir);
        btnSorunBildir.setTypeface(YaziFontu, Typeface.NORMAL);
        btnSorunBildir.setOnClickListener(this);
        registerForContextMenu(btnSorunBildir);

        lblHakkinda = findViewById(R.id.lblHakkinda);
        lblHakkinda.setTypeface(YaziFontu, Typeface.BOLD);

        btnOyVer = findViewById(R.id.btnOyVer);
        btnOyVer.setTypeface(YaziFontu, Typeface.NORMAL);
        btnOyVer.setOnClickListener(this);

        btnGizlilikIlkesi = findViewById(R.id.btnGizlilikIlkesi);
        btnGizlilikIlkesi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnGizlilikIlkesi.setOnClickListener(this);

        btnHizmetKosullari = findViewById(R.id.btnHizmetKosullari);
        btnHizmetKosullari.setTypeface(YaziFontu, Typeface.NORMAL);
        btnHizmetKosullari.setOnClickListener(this);

        btnAcikKaynakKutuphaneleri = findViewById(R.id.btnAcikKaynakKutuphaneleri);
        btnAcikKaynakKutuphaneleri.setTypeface(YaziFontu, Typeface.NORMAL);
        btnAcikKaynakKutuphaneleri.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				onBackPressed();
				break;
            case R.id.btnEkranIsigi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Ayarlar_Ekran_Isigi.class), "Slide");
                break;
            case R.id.btnRepertuvarIslemleri:
                AkorDefterimSys.EkranGetir(new Intent(activity, Repertuvar_Islemleri.class), "Slide");
                break;
            case R.id.btnEgitim:
                AkorDefterimSys.EkranGetir(new Intent(activity, Egitim.class), "Slide");
                break;
            case R.id.btnYardimMerkezi:

                break;
            case R.id.btnSorunBildir:
                if(AkorDefterimSys.GirisYapildiMi()) {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        openContextMenu(btnSorunBildir);
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.devam_etmek_icin_giris_yapmalisin));

                break;
            case R.id.btnOyVer:
                if(AkorDefterimSys.InternetErisimKontrolu()) {
                    String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                break;
            case R.id.btnGizlilikIlkesi:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cbcapp.net/akordefterim/gizlilikpolitikasi.html")));
                break;
            case R.id.btnHizmetKosullari:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cbcapp.net/akordefterim/hizmetkosullari.html")));
                break;
            case R.id.btnAcikKaynakKutuphaneleri:
                AkorDefterimSys.EkranGetir(new Intent(activity, Hakkinda_Open_Source_Lib.class), "Slide");
                break;
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        menu.add(0, 0, 0, getString(R.string.sorun_bildir));
        menu.add(0, 1, 0, getString(R.string.gorus_bildir));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
        Intent mIntent = new Intent(activity, Destek_GeriBildirim.class);

        switch (item.getItemId()) {
            case 0:
                mIntent.putExtra("BildirimTipi", "Sorun");
                break;
            case 1:
                mIntent.putExtra("BildirimTipi", "Görüş");
                break;
            default:
                return false;
        }

        AkorDefterimSys.EkranGetir(mIntent, "Slide");

        return true;
    }
}