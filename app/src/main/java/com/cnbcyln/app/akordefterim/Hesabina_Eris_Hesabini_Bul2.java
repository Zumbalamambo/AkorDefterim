package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
    AlertDialog ADDialog;
    ProgressDialog PDIslem;
	Timer TSMSDogrulamaKoduSayac, TEPostaDogrulamaKoduSayac;
	TimerTask TTSMSDogrulamaKoduSayac, TTEPostaDogrulamaKoduSayac;
	Handler SMSDogrulamaKoduHandler = new Handler();
	Handler EPostaDogrulamaKoduHandler = new Handler();

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	TextView lblBaslik, lblAdSoyad, lblDahaFazlaYardim;
	ImageView ImgBuyukProfilResim;
	SpinKitView SKVLoader;
	CircleImageView CImgKucukProfilResim, CImgSosyalAgFacebookIcon, CImgSosyalAgGoogleIcon;
	Button btnEPostaGonder, btnSmsGonder;

    String HesapID = "", FacebookID = "", GoogleID = "", AdSoyad = "", ResimURL = "", EPosta = "", Parola = "", TelKodu = "", CepTelefon = "";
    int EPostaKalanSure = 0;
	int SMSKalanSure = 0;

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

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		SKVLoader = findViewById(R.id.SKVLoader);
		SKVLoader.setVisibility(View.GONE);

		ImgBuyukProfilResim = findViewById(R.id.ImgBuyukProfilResim);

		CImgKucukProfilResim = findViewById(R.id.CImgKucukProfilResim);

		if(ResimURL.equals("-")) {
			ImgBuyukProfilResim.setImageResource(R.drawable.bos_profil);
			CImgKucukProfilResim.setImageResource(R.drawable.bos_profil);
		} else {
			if (ResimURL.startsWith("http://") || ResimURL.startsWith("https://")) {
				new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(ResimURL);
				new AkorDefterimSys.NettenResimYukle(CImgKucukProfilResim).execute(ResimURL);
			} else {
				new AkorDefterimSys.NettenResimYukle(ImgBuyukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + ResimURL);
				new AkorDefterimSys.NettenResimYukle(CImgKucukProfilResim).execute(AkorDefterimSys.CBCAPP_HttpsAdres + ResimURL);
			}
		}

		CImgSosyalAgFacebookIcon = findViewById(R.id.CImgSosyalAgFacebookIcon);
		if(FacebookID.equals("-")) CImgSosyalAgFacebookIcon.setVisibility(View.GONE);
		else CImgSosyalAgFacebookIcon.setVisibility(View.VISIBLE);

		CImgSosyalAgGoogleIcon = findViewById(R.id.CImgSosyalAgGoogleIcon);
		if(GoogleID.equals("-")) CImgSosyalAgGoogleIcon.setVisibility(View.GONE);
		else CImgSosyalAgGoogleIcon.setVisibility(View.VISIBLE);

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
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			// prefEPostaGonderiTarihi veya prefSMSGonderiTarihi isimli prefkey var mı?
			if(sharedPref.contains("prefEPostaGonderiTarihi") || sharedPref.contains("prefSMSGonderiTarihi")) { // Varsa
				if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
					PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
					PDIslem.show();
				}

				AkorDefterimSys.TarihSaatGetir("TarihSaatOgren_GeriSayimaBasla");
			}
		} else {
			if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
				ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
						getString(R.string.hata),
						getString(R.string.internet_baglantisi_saglanamadi),
						activity.getString(R.string.tamam),
						"ADDialog_Kapat_GeriGit");
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog.show();
			}
		}
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
					if(EPostaKalanSure > 0 && EPostaKalanSure < AkorDefterimSys.EPostaGondermeToplamSure)
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_eposta_telebinde_bulunamazsiniz));
					else {
						btnEPostaGonder.setEnabled(false);
						SKVLoader.setVisibility(View.VISIBLE);

						if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
							PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
							PDIslem.show();
						}

						AkorDefterimSys.EPostaGonder(EPosta, AdSoyad, getString(R.string.uygulama_adi) + " - Parola Sıfırlama", getString(R.string.eposta_parola_gonderildi_icerik, AdSoyad, Parola, getString(R.string.uygulama_adi)));
					}
				} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

				break;
			case R.id.btnSmsGonder:
				if(AkorDefterimSys.InternetErisimKontrolu()) {
					if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure)
						AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.sure_bitmeden_yeni_sms_telebinde_bulunamazsiniz));
					else {
						btnSmsGonder.setEnabled(false);
						SKVLoader.setVisibility(View.VISIBLE);

						if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) { // Eğer progress dialog açık değilse
							PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
							PDIslem.show();
						}

						AkorDefterimSys.SMSGonder(TelKodu, CepTelefon, getString(R.string.sms_parola_gonderildi_icerik, AdSoyad, Parola, getString(R.string.uygulama_adi)));
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
				case "TarihSaatOgren_GeriSayimaBasla":
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						if(sharedPref.contains("prefEPostaGonderiTarihi")) {
							EPostaKalanSure = AkorDefterimSys.EPostaGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefEPostaGonderiTarihi", ""),"Saniye");

							if(EPostaKalanSure > 0 && EPostaKalanSure < AkorDefterimSys.EPostaGondermeToplamSure) {
								if (TEPostaDogrulamaKoduSayac != null) {
									TEPostaDogrulamaKoduSayac.cancel();
									TEPostaDogrulamaKoduSayac = null;
									TTEPostaDogrulamaKoduSayac.cancel();
									TTEPostaDogrulamaKoduSayac = null;
								}

								TEPostaDogrulamaKoduSayac = new Timer();
								EPostaDogrulamaKoduSayac_Ayarla();

								btnEPostaGonder.setText(getString(R.string.eposta_gonder2, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));

								TEPostaDogrulamaKoduSayac.schedule(TTEPostaDogrulamaKoduSayac, 10, 1000);
							} else {
								EPostaKalanSure = 0;

								sharedPrefEditor = sharedPref.edit();
								sharedPrefEditor.remove("prefEPostaGonderiTarihi");
								sharedPrefEditor.apply();

								btnEPostaGonder.setText(getString(R.string.eposta_gonder));
							}
						}

						if(sharedPref.contains("prefSMSGonderiTarihi")) {
							SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure - (int) AkorDefterimSys.IkiTarihArasiFark(JSONSonuc.getString("TarihSaat"), sharedPref.getString("prefSMSGonderiTarihi", ""),"Saniye");

							if(SMSKalanSure > 0 && SMSKalanSure < AkorDefterimSys.SMSGondermeToplamSure) {
								if (TSMSDogrulamaKoduSayac != null) {
									TSMSDogrulamaKoduSayac.cancel();
									TSMSDogrulamaKoduSayac = null;
									TTSMSDogrulamaKoduSayac.cancel();
									TTSMSDogrulamaKoduSayac = null;
								}

								TSMSDogrulamaKoduSayac = new Timer();
								SMSDogrulamaKoduSayac_Ayarla();

								btnSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));

								TSMSDogrulamaKoduSayac.schedule(TTSMSDogrulamaKoduSayac, 10, 1000);
							} else {
								SMSKalanSure = 0;

								sharedPrefEditor = sharedPref.edit();
								sharedPrefEditor.remove("prefSMSGonderiTarihi");
								sharedPrefEditor.apply();

								btnSmsGonder.setText(getString(R.string.sms_gonder));
							}
						}
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.islem_yapilirken_bir_hata_olustu),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat_GeriGit");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "EPostaGonder":
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_DevamEt_EPosta"); // Öncelikle sistem saatini öğreniyoruz. Daha sonra E-posta gönderdikten sonra sistem tarihini kullanıcıya kaydediyoruz..
					else {
						btnEPostaGonder.setEnabled(true);
						SKVLoader.setVisibility(View.GONE);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.eposta_gonderilemedi),
									getString(R.string.eposta_gonderilemedi_mesaj, AkorDefterimSys.EPostaSifrele(EPosta)),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "TarihSaatGetir_DevamEt_EPosta":
					btnEPostaGonder.setEnabled(true);
					SKVLoader.setVisibility(View.GONE);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefEPostaGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						if (TEPostaDogrulamaKoduSayac != null) {
							TEPostaDogrulamaKoduSayac.cancel();
							TEPostaDogrulamaKoduSayac = null;
							TTEPostaDogrulamaKoduSayac.cancel();
							TTEPostaDogrulamaKoduSayac = null;
						}

						TEPostaDogrulamaKoduSayac = new Timer();
						EPostaDogrulamaKoduSayac_Ayarla();
						EPostaKalanSure = AkorDefterimSys.EPostaGondermeToplamSure;

						btnEPostaGonder.setText(getString(R.string.eposta_gonder2, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));

						TEPostaDogrulamaKoduSayac.schedule(TTEPostaDogrulamaKoduSayac, 10, 1000);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.eposta_gonderildi),
									getString(R.string.eposta_parola_gonderildi_mesaj, AkorDefterimSys.EPostaSifrele(EPosta)),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					break;
				case "SMSGonder":
					if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.TarihSaatGetir("TarihSaatGetir_DevamEt_SMS"); // Öncelikle sistem saatini öğreniyoruz. Daha sonra SMS gönderdikten sonra sistem tarihini kullanıcıya kaydediyoruz..
					else {
						btnSmsGonder.setEnabled(true);
						SKVLoader.setVisibility(View.GONE);
						AkorDefterimSys.DismissProgressDialog(PDIslem);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.sms_gonderilemedi),
									getString(R.string.sms_gonderilemedi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}

					break;
				case "TarihSaatGetir_DevamEt_SMS":
					btnSmsGonder.setEnabled(true);
					SKVLoader.setVisibility(View.GONE);
					AkorDefterimSys.DismissProgressDialog(PDIslem);

					// Tarih bilgisi alındıysa sonuç true döner..
					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefSMSGonderiTarihi", JSONSonuc.getString("TarihSaat"));
						sharedPrefEditor.apply();

						if (TSMSDogrulamaKoduSayac != null) {
							TSMSDogrulamaKoduSayac.cancel();
							TSMSDogrulamaKoduSayac = null;
							TTSMSDogrulamaKoduSayac.cancel();
							TTSMSDogrulamaKoduSayac = null;
						}

						TSMSDogrulamaKoduSayac = new Timer();
						SMSDogrulamaKoduSayac_Ayarla();
						SMSKalanSure = AkorDefterimSys.SMSGondermeToplamSure;

						btnSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));

						TSMSDogrulamaKoduSayac.schedule(TTSMSDogrulamaKoduSayac, 10, 1000);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.sms_gonderildi),
									getString(R.string.sms_parola_gonderildi_mesaj, AkorDefterimSys.CepTelefonSifrele(TelKodu, CepTelefon)),
									activity.getString(R.string.tamam),
									"ADDialog_Kapat");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					break;
				case "ADDialog_Kapat_GeriGit":
					AkorDefterimSys.DismissAlertDialog(ADDialog);

					finish();
					break;
				case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);
					break;
				case "Daha_Fazla_Yardim":
					AkorDefterimSys.ToastMsj(activity, getString(R.string.cok_yakinda), Toast.LENGTH_SHORT);
					break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	public void EPostaDogrulamaKoduSayac_Ayarla() {
		TTEPostaDogrulamaKoduSayac = new TimerTask() {
			@Override
			public void run() {
				EPostaDogrulamaKoduHandler.post(new Runnable() {
					public void run() {
						if (EPostaKalanSure == 0) {
							if (TTEPostaDogrulamaKoduSayac != null) {
								TTEPostaDogrulamaKoduSayac.cancel();
								TTEPostaDogrulamaKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefEPostaGonderiTarihi");
							sharedPrefEditor.apply();

							btnEPostaGonder.setText(getString(R.string.eposta_gonder));
						} else {
							btnEPostaGonder.setText(getString(R.string.eposta_gonder2, AkorDefterimSys.ZamanFormatMMSS(EPostaKalanSure)));
							EPostaKalanSure--;
						}
					}
				});
			}
		};
	}

	public void SMSDogrulamaKoduSayac_Ayarla() {
		TTSMSDogrulamaKoduSayac = new TimerTask() {
			@Override
			public void run() {
				SMSDogrulamaKoduHandler.post(new Runnable() {
					public void run() {
						if (SMSKalanSure == 0) {
							if (TTSMSDogrulamaKoduSayac != null) {
								TTSMSDogrulamaKoduSayac.cancel();
								TTSMSDogrulamaKoduSayac = null;
							}

							sharedPrefEditor = sharedPref.edit();
							sharedPrefEditor.remove("prefSMSGonderiTarihi");
							sharedPrefEditor.apply();

							btnSmsGonder.setText(getString(R.string.sms_gonder));
						} else {
							btnSmsGonder.setText(getString(R.string.sms_gonder2, AkorDefterimSys.ZamanFormatMMSS(SMSKalanSure)));
							SMSKalanSure--;
						}
					}
				});
			}
		};
	}
}