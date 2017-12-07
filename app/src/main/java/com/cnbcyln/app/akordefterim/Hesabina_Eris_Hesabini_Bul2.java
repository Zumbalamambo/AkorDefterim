package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class Hesabina_Eris_Hesabini_Bul2 extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
	SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    AlertDialog ADDialog_EPosta_Gonder, ADDialog_SMS_Gonder;
	Timer TSMSGondermeSayac, TEPostaGondermeSayac;
	TimerTask TTSMSGondermeSayac, TTEPostaGondermeSayac;
	Handler SMSGondermeHandler = new Handler();
	Handler EPostaGondermeHandler = new Handler();

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	TextView lblBaslik, lblAdSoyad, lblDahaFazlaYardim;
	ImageView ImgBuyukProfilResim;
	SpinKitView SKVLoader;
	CircleImageView CImgKucukProfilResim, CImgSosyalAgFacebookIcon, CImgSosyalAgGoogleIcon;
	Button btnEPostaGonder, btnSmsGonder;

    String HesapID, FacebookID, GoogleID, AdSoyad, ResimURL, EPosta, Parola, TelKodu, CepTelefon, CepTelefonOnay;
    int EPostaGondermeKalanSure = 0;
	int SMSGondermeKalanSure = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hesabini_bul2);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		HesapID = mBundle.getString("HesapID");
		FacebookID = mBundle.getString("FacebookID");
		GoogleID = mBundle.getString("GoogleID");
		AdSoyad = mBundle.getString("AdSoyad");
		ResimURL = mBundle.getString("ResimURL");
		EPosta = mBundle.getString("EPosta");
		Parola = mBundle.getString("Parola");
		TelKodu = mBundle.getString("TelKodu");
		CepTelefon = mBundle.getString("CepTelefon");
		CepTelefonOnay = mBundle.getString("CepTelefonOnay");

		TEPostaGondermeSayac = new Timer();
		TEPostaGondermeSayac_Ayarla();

		if(sharedPref.getInt("prefEPostaGondermeKalanSure", 0) > 0) {
			EPostaGondermeKalanSure = sharedPref.getInt("prefEPostaGondermeKalanSure", 0);
			TEPostaGondermeSayac.schedule(TTEPostaGondermeSayac, 10, 1000);
		} else
			EPostaGondermeKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

		TSMSGondermeSayac = new Timer();
		TSMSGondermeSayac_Ayarla();

		if(sharedPref.getInt("prefSMSGondermeKalanSure", 0) > 0) {
			SMSGondermeKalanSure = sharedPref.getInt("prefSMSGondermeKalanSure", 0);
			TSMSGondermeSayac.schedule(TTSMSGondermeSayac, 10, 1000);
		} else
			SMSGondermeKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		SKVLoader = findViewById(R.id.SKVLoader);
		SKVLoader.setVisibility(View.GONE);

		ImgBuyukProfilResim = findViewById(R.id.ImgBuyukProfilResim);

		if(ResimURL.equals("")) ImgBuyukProfilResim.setImageResource(R.drawable.bos_profil);
		else new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + ResimURL);

		CImgKucukProfilResim = findViewById(R.id.CImgKucukProfilResim);

		if(ResimURL.equals("")) CImgKucukProfilResim.setImageResource(R.drawable.bos_profil);
		else new AkorDefterimSys.NettenResimYukle(CImgKucukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + ResimURL);

		CImgSosyalAgFacebookIcon = findViewById(R.id.CImgSosyalAgFacebookIcon);
		if(!FacebookID.equals("")) CImgSosyalAgFacebookIcon.setVisibility(View.VISIBLE);
		else CImgSosyalAgFacebookIcon.setVisibility(View.GONE);

		CImgSosyalAgGoogleIcon = findViewById(R.id.CImgSosyalAgGoogleIcon);
		if(!GoogleID.equals("")) CImgSosyalAgGoogleIcon.setVisibility(View.VISIBLE);
		else CImgSosyalAgGoogleIcon.setVisibility(View.GONE);

		lblAdSoyad = findViewById(R.id.lblAdSoyad);
		lblAdSoyad.setTypeface(YaziFontu, Typeface.BOLD);
		lblAdSoyad.setText(AdSoyad.toUpperCase());

		btnEPostaGonder = findViewById(R.id.btnEPostaGonder);
		btnEPostaGonder.setTypeface(YaziFontu, Typeface.BOLD);
		btnEPostaGonder.setOnClickListener(this);

		btnSmsGonder = findViewById(R.id.btnSmsGonder);
		btnSmsGonder.setTypeface(YaziFontu, Typeface.BOLD);
		btnSmsGonder.setOnClickListener(this);

		lblDahaFazlaYardim = findViewById(R.id.lblDahaFazlaYardim);
		lblDahaFazlaYardim.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewOnClick(lblDahaFazlaYardim, "Daha_Fazla_Yardim");
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
			case R.id.btnEPostaGonder:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					btnEPostaGonder.setEnabled(false);

					if(EPostaGondermeKalanSure > 0 && EPostaGondermeKalanSure < AkorDefterimSys.EPostaGondermeToplamSure) {
						btnEPostaGonder.setEnabled(true);
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_eposta_telebinde_bulunamazsiniz));
					} else {
						SKVLoader.setVisibility(View.VISIBLE);
						AkorDefterimSys.EPostaGonder(EPosta, AdSoyad, getString(R.string.uygulama_adi) + " - Parola Sıfırlama", getString(R.string.eposta_parola_gonderildi_icerik, AdSoyad, Parola, getString(R.string.uygulama_adi)));
					}
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case R.id.btnSmsGonder:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					btnSmsGonder.setEnabled(false);

					if (CepTelefonOnay.equals("1")) {
						if(SMSGondermeKalanSure > 0 && SMSGondermeKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
							btnSmsGonder.setEnabled(true);
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_sms_telebinde_bulunamazsiniz));
						} else {
							SKVLoader.setVisibility(View.VISIBLE);
							AkorDefterimSys.SMSGonder(TelKodu, CepTelefon, getString(R.string.sms_parola_gonderildi_icerik, AdSoyad, Parola, getString(R.string.uygulama_adi)));
						}
					} else {
						ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.hata),
								getString(R.string.hesabini_bul_ceptelefon_onay_hata),
								activity.getString(R.string.tamam),
								"ADDialog_SMS_Gonder_Kapat");
						ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SMS_Gonder.show();
					}
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
		}
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
				case "EPostaGonder":
					btnEPostaGonder.setEnabled(true);
					SKVLoader.setVisibility(View.GONE);

					if(JSONSonuc.getBoolean("Sonuc")) {
						ADDialog_EPosta_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.eposta_gonderildi),
								getString(R.string.eposta_parola_gonderildi_mesaj, AkorDefterimSys.EPostaSifrele(EPosta)),
								activity.getString(R.string.tamam),
								"ADDialog_EPosta_Gonder_Kapat");
						ADDialog_EPosta_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_EPosta_Gonder.show();

						sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
						sharedPrefEditor.putInt("prefEPostaGondermeKalanSure", EPostaGondermeKalanSure);
						sharedPrefEditor.apply();

						TEPostaGondermeSayac.schedule(TTEPostaGondermeSayac, 10, 1000);
					} else {
						ADDialog_EPosta_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.eposta_gonderilemedi),
								getString(R.string.eposta_gonderilemedi_mesaj, AkorDefterimSys.EPostaSifrele(EPosta)),
								activity.getString(R.string.tamam),
								"ADDialog_EPosta_Gonder_Kapat");
						ADDialog_EPosta_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_EPosta_Gonder.show();
					}

					break;
				case "ADDialog_EPosta_Gonder_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog_EPosta_Gonder);

					break;
				case "SMSGonder":
					btnSmsGonder.setEnabled(true);
					SKVLoader.setVisibility(View.GONE);

					if(JSONSonuc.getBoolean("Sonuc")) {
						ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.sms_gonderildi),
								getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
								activity.getString(R.string.tamam),
								"ADDialog_SMS_Gonder_Kapat");
						ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SMS_Gonder.show();

						sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
						sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
						sharedPrefEditor.apply();

						TSMSGondermeSayac.schedule(TTSMSGondermeSayac, 10, 1000);
					} else {
						ADDialog_SMS_Gonder = AkorDefterimSys.CustomAlertDialog(activity,
								getString(R.string.sms_gonderilemedi),
								getString(R.string.sms_gonderilemedi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
								activity.getString(R.string.tamam),
								"ADDialog_SMS_Gonder_Kapat");
						ADDialog_SMS_Gonder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SMS_Gonder.show();
					}

					break;
				case "ADDialog_SMS_Gonder_Kapat":
					btnSmsGonder.setEnabled(true);
					AkorDefterimSys.DismissAlertDialog(ADDialog_SMS_Gonder);

					break;
				case "Daha_Fazla_Yardim":
					AkorDefterimSys.ToastMsj(activity, getString(R.string.cok_yakinda), Toast.LENGTH_SHORT);
					break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public void TEPostaGondermeSayac_Ayarla() {
		TTEPostaGondermeSayac = new TimerTask() {
			@Override
			public void run() {
				EPostaGondermeHandler.post(new Runnable() {
					public void run() {
						if (EPostaGondermeKalanSure == 0) {
							if (TEPostaGondermeSayac != null) {
								TEPostaGondermeSayac.cancel();
								TEPostaGondermeSayac = null;
							}

							sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
							sharedPrefEditor.remove("prefEPostaGondermeKalanSure");
							sharedPrefEditor.apply();

							btnEPostaGonder.setText(getString(R.string.eposta_gonder));
						} else {
							sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
							sharedPrefEditor.putInt("prefEPostaGondermeKalanSure", EPostaGondermeKalanSure);
							sharedPrefEditor.apply();

							btnEPostaGonder.setText(getString(R.string.eposta_gonder2, AkorDefterimSys.ZamanFormatMMSS(EPostaGondermeKalanSure)));
							EPostaGondermeKalanSure--;
						}
					}
				});
			}
		};
	}

	public void TSMSGondermeSayac_Ayarla() {
		TTSMSGondermeSayac = new TimerTask() {
			@Override
			public void run() {
				SMSGondermeHandler.post(new Runnable() {
					public void run() {
						if (SMSGondermeKalanSure == 0) {
							if (TSMSGondermeSayac != null) {
								TSMSGondermeSayac.cancel();
								TSMSGondermeSayac = null;
							}

							sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
							sharedPrefEditor.remove("prefSMSGondermeKalanSure");
							sharedPrefEditor.apply();

							btnSmsGonder.setText(getString(R.string.sms_gonder));
						} else {
							sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
							sharedPrefEditor.putInt("prefSMSGondermeKalanSure", SMSGondermeKalanSure);
							sharedPrefEditor.apply();

							btnSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSGondermeKalanSure)));
							SMSGondermeKalanSure--;
						}
					}
				});
			}
		};
	}
}