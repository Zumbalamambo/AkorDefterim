package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
public class Repertuvar_Islemleri extends AppCompatActivity implements OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	Typeface YaziFontu;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnListeYonetimi, btnKategoriYonetimi, btnTarzYonetimi, btnSarkiYonetimi, btnYedeklemeIslemleri;
	TextView lblBaslik, lblRepertuvarIslemleriAciklama;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repertuvar_islemleri);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
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

        lblRepertuvarIslemleriAciklama = findViewById(R.id.lblRepertuvarIslemleriAciklama);
        lblRepertuvarIslemleriAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
        lblRepertuvarIslemleriAciklama.setText(getString(R.string.repertuvar_islemleri_aciklama, getString(R.string.uygulama_adi)));
        AkorDefterimSys.setTextViewHTML(lblRepertuvarIslemleriAciklama);

        btnListeYonetimi = findViewById(R.id.btnListeYonetimi);
        btnListeYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnListeYonetimi.setOnClickListener(this);

        btnKategoriYonetimi = findViewById(R.id.btnKategoriYonetimi);
        btnKategoriYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnKategoriYonetimi.setOnClickListener(this);

        btnTarzYonetimi = findViewById(R.id.btnTarzYonetimi);
        btnTarzYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnTarzYonetimi.setOnClickListener(this);

        btnSarkiYonetimi = findViewById(R.id.btnSarkiYonetimi);
        btnSarkiYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnSarkiYonetimi.setOnClickListener(this);

        btnYedeklemeIslemleri = findViewById(R.id.btnYedeklemeIslemleri);
        btnYedeklemeIslemleri.setTypeface(YaziFontu, Typeface.NORMAL);
        btnYedeklemeIslemleri.setOnClickListener(this);
	}

    @Override
    protected void onStart() {
        super.onStart();
        AkorDefterimSys.activity = activity;
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				onBackPressed();
				break;
            case R.id.btnListeYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Liste_Yonetimi.class), "Slide");
                break;
            case R.id.btnKategoriYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Kategori_Yonetimi.class), "Slide");
                break;
            case R.id.btnTarzYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Tarz_Yonetimi.class), "Slide");
                break;
            case R.id.btnSarkiYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Sarki_Yonetimi.class), "Slide");
                break;
            case R.id.btnYedeklemeIslemleri:

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