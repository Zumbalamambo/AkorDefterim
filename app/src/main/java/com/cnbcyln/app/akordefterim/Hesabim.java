package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabim extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog_HesapDurumu;
	ProgressDialog PDBilgilerAliniyor;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	TextView lblBaslik, lblAdSoyad;
	ImageView ImgBuyukProfilResim;
	CircleImageView CImgKucukProfilResim, CImgSosyalAgFacebookIcon, CImgSosyalAgGoogleIcon;
	Button btnProfiliDuzenle, btnBagliHesaplar, btnParolayiDegistir;

	String EPosta = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabim);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		EPosta = mBundle.getString("EPosta");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		ImgBuyukProfilResim = findViewById(R.id.ImgBuyukProfilResim);

		CImgKucukProfilResim = findViewById(R.id.CImgKucukProfilResim);

		CImgSosyalAgFacebookIcon = findViewById(R.id.CImgSosyalAgFacebookIcon);
		CImgSosyalAgFacebookIcon.setVisibility(View.GONE);

		CImgSosyalAgGoogleIcon = findViewById(R.id.CImgSosyalAgGoogleIcon);
		CImgSosyalAgGoogleIcon.setVisibility(View.GONE);

		lblAdSoyad = findViewById(R.id.lblAdSoyad);
		lblAdSoyad.setTypeface(YaziFontu, Typeface.BOLD);
		lblAdSoyad.setText("");

		btnProfiliDuzenle = findViewById(R.id.btnProfiliDuzenle);
		btnProfiliDuzenle.setTypeface(YaziFontu, Typeface.BOLD);
		btnProfiliDuzenle.setOnClickListener(this);

		btnBagliHesaplar = findViewById(R.id.btnBagliHesaplar);
		btnBagliHesaplar.setTypeface(YaziFontu, Typeface.BOLD);
		btnBagliHesaplar.setOnClickListener(this);

		btnParolayiDegistir = findViewById(R.id.btnParolayiDegistir);
		btnParolayiDegistir.setTypeface(YaziFontu, Typeface.BOLD);
		btnParolayiDegistir.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.GirisYapildiMi()) {
			if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgilerAliniyor)) {
				PDBilgilerAliniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_yukleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgilerAliniyor_Timeout");
				PDBilgilerAliniyor.show();
			}

			AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID",""), "", "", "HesapBilgiGetir");
		} else AkorDefterimSys.CikisYap();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				onBackPressed();

				break;
			case R.id.btnProfiliDuzenle:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.EkranGetir(new Intent(activity, Profil_Duzenle.class), "Slide");
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case R.id.btnBagliHesaplar:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.ToastMsj(activity, "Bağlı Hesaplar", Toast.LENGTH_SHORT);
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case R.id.btnParolayiDegistir:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					AkorDefterimSys.ToastMsj(activity, "Parolayı Değiştir", Toast.LENGTH_SHORT);
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			AkorDefterimSys.DismissProgressDialog(PDBilgilerAliniyor);

			switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					if(JSONSonuc.getBoolean("Sonuc")) {
						if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
							if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
								ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
										getString(R.string.hesap_durumu),
										getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
										activity.getString(R.string.tamam),
										"ADDialog_HesapDurumu_Kapat_CikisYap");
								ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
								ADDialog_HesapDurumu.show();
							}
						} else {
							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.putString("prefEPosta", JSONSonuc.getString("EPosta"));
							sharedPrefEditor.putString("prefParolaSHA1", JSONSonuc.getString("ParolaSHA1"));
							sharedPrefEditor.apply();

							if(JSONSonuc.getString("ResimURL").equals("-")) {
								ImgBuyukProfilResim.setImageResource(R.drawable.bos_profil);
								CImgKucukProfilResim.setImageResource(R.drawable.bos_profil);
							} else {
								if (JSONSonuc.getString("ResimURL").startsWith("http://") || JSONSonuc.getString("ResimURL").startsWith("https://")) {
									new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(JSONSonuc.getString("ResimURL"));
									new AkorDefterimSys.NettenResimYukle(CImgKucukProfilResim).execute(JSONSonuc.getString("ResimURL"));
								} else {
									new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + JSONSonuc.getString("ResimURL"));
									new AkorDefterimSys.NettenResimYukle(CImgKucukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + JSONSonuc.getString("ResimURL"));
								}
							}

							if(!JSONSonuc.getString("FacebookID").equals("")) CImgSosyalAgFacebookIcon.setVisibility(View.VISIBLE);
							else CImgSosyalAgFacebookIcon.setVisibility(View.GONE);

							if(!JSONSonuc.getString("GoogleID").equals("")) CImgSosyalAgGoogleIcon.setVisibility(View.VISIBLE);
							else CImgSosyalAgGoogleIcon.setVisibility(View.GONE);

							lblAdSoyad.setText(JSONSonuc.getString("AdSoyad").toUpperCase());
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_HesapDurumu)) {
							ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hesap_durumu),
									getString(R.string.hesap_bilgileri_bulunamadi),
									activity.getString(R.string.tamam),
									"ADDialog_HesapDurumu_Kapat_CikisYap");
							ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_HesapDurumu.show();
						}
					}

					break;
				case "ADDialog_HesapDurumu_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

					AkorDefterimSys.CikisYap();
					break;
				case "ADDialog_HesapDurumu_Kapat_CikisYap":
					AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

					AkorDefterimSys.CikisYap();
					break;
				case "PDBilgilerAliniyor_Timeout":
					onBackPressed();
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}