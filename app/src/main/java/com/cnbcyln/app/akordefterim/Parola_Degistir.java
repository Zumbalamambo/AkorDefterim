package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.PasswordView;
import com.cnbcyln.app.akordefterim.util.Strings;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class Parola_Degistir extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDBilgiIslem;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnKapat, btnKaydet;
	TextInputLayout txtILMevcutParola, txtILYeniParola, txtILYeniParolaTekrar;
	TextView lblBaslik, lblParolaAciklama;
	PasswordView txtMevcutParola, txtYeniParola, txtYeniParolaTekrar;
	LinearLayout LLParolaGuvenligi1, LLParolaGuvenligi2, LLParolaGuvenligi3;

	String Islem = "", EPosta = "";
	int ParolaKarakterSayisiMIN = 0, ParolaKarakterSayisiMAX = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parola_degistir);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		ParolaKarakterSayisiMIN = getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN);
		ParolaKarakterSayisiMAX = getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX);

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        btnKapat = findViewById(R.id.btnKapat);
        btnKapat.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

        btnKaydet = findViewById(R.id.btnKaydet);
        btnKaydet.setOnClickListener(this);

		lblParolaAciklama = findViewById(R.id.lblParolaAciklama);
		lblParolaAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblParolaAciklama.setText(getString(R.string.parola_ekran_aciklama, String.valueOf(ParolaKarakterSayisiMIN), String.valueOf(ParolaKarakterSayisiMAX)));
		AkorDefterimSys.setTextViewHTML(lblParolaAciklama);

        txtILMevcutParola = findViewById(R.id.txtILMevcutParola);
        txtILMevcutParola.setTypeface(YaziFontu);

        txtMevcutParola = findViewById(R.id.txtMevcutParola);
        txtMevcutParola.setTypeface(YaziFontu, Typeface.NORMAL);

        txtILYeniParola = findViewById(R.id.txtILYeniParola);
        txtILYeniParola.setTypeface(YaziFontu);

        txtYeniParola = findViewById(R.id.txtYeniParola);
        txtYeniParola.setTypeface(YaziFontu, Typeface.NORMAL);
        txtYeniParola.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
                txtILYeniParola.setError(null);

				String YeniParola = txtYeniParola.getText().toString().trim();
				int ParolaGuvenligiSkor = AkorDefterimSys.ParolaGuvenligi(YeniParola);

				if(ParolaGuvenligiSkor < 30) {
					LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
					LLParolaGuvenligi2.setBackgroundColor(0);
					LLParolaGuvenligi3.setBackgroundColor(0);
				} else if(ParolaGuvenligiSkor >= 30 && ParolaGuvenligiSkor < 90) {
					LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.TuruncuYazi));
					LLParolaGuvenligi2.setBackgroundColor(getResources().getColor(R.color.TuruncuYazi));
					LLParolaGuvenligi3.setBackgroundColor(0);
				} else if(ParolaGuvenligiSkor >= 90) {
					LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Yesil2));
					LLParolaGuvenligi2.setBackgroundColor(getResources().getColor(R.color.Yesil2));
					LLParolaGuvenligi3.setBackgroundColor(getResources().getColor(R.color.Yesil2));
				}
			}
		});

        txtILYeniParolaTekrar = findViewById(R.id.txtILYeniParolaTekrar);
        txtILYeniParolaTekrar.setTypeface(YaziFontu);

        txtYeniParolaTekrar = findViewById(R.id.txtYeniParolaTekrar  );
        txtYeniParolaTekrar.setTypeface(YaziFontu, Typeface.NORMAL);
        txtYeniParolaTekrar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					Kaydet();
				}

				return false;
			}
		});

		LLParolaGuvenligi1 = findViewById(R.id.LLParolaGuvenligi1);
		LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
		LLParolaGuvenligi2 = findViewById(R.id.LLParolaGuvenligi2);
		LLParolaGuvenligi2.setBackgroundColor(0);
		LLParolaGuvenligi3 = findViewById(R.id.LLParolaGuvenligi3);
		LLParolaGuvenligi3.setBackgroundColor(0);
	}

    @Override
    protected void onStart() {
        super.onStart();

        if(!AkorDefterimSys.GirisYapildiMi()) AkorDefterimSys.CikisYap();
    }

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.coordinatorLayout:
				txtILMevcutParola.setError(null);
				AkorDefterimSys.UnFocusEditText(txtMevcutParola);
				txtILYeniParola.setError(null);
				AkorDefterimSys.UnFocusEditText(txtYeniParola);
                txtILYeniParolaTekrar.setError(null);
                AkorDefterimSys.UnFocusEditText(txtYeniParolaTekrar);
				break;
			case R.id.btnKapat:
				onBackPressed();
				break;
			case R.id.btnKaydet:
				Kaydet();
				break;
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

            btnKapat.setEnabled(true);
            btnKaydet.setEnabled(true);
            AkorDefterimSys.DismissProgressDialog(PDBilgiIslem);

			switch (JSONSonuc.getString("Islem")) {
                case "HesapBilgiGetir":
                    if(JSONSonuc.getBoolean("Sonuc")) {
                        if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
                            if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                        getString(R.string.hesap_durumu),
                                        getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
                                        activity.getString(R.string.tamam),
                                        "ADDialog_Kapat_CikisYap");
                                ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                ADDialog.show();
                            }
                        } else {
                            if(!JSONSonuc.getString("ParolaSHA1").equals(Strings.getSHA1(txtMevcutParola.getText().toString())))
                                AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.txtmevcutparola_hata1));
                            else {
                                String FirebaseToken = FirebaseInstanceId.getInstance().getToken();
                                @SuppressLint("HardwareIds")
                                String OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                String OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
                                String UygulamaVersiyon = "";

                                try {
                                    UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }

                                String Parola = txtYeniParola.getText().toString().trim();
                                String ParolaSHA1 = Strings.getSHA1(Parola);

                                AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "", FirebaseToken, OSID, OSVersiyon, "", "", "","", Parola, ParolaSHA1, "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");
                            }
                        }
                    } else {
                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.hesap_durumu),
                                    getString(R.string.hesap_bilgileri_bulunamadi),
                                    activity.getString(R.string.tamam),
                                    "ADDialog_Kapat_CikisYap");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                    }

                    break;
                case "HesapBilgiGuncelle":
                    if(JSONSonuc.getBoolean("Sonuc")) {
                        sharedPrefEditor = sharedPref.edit();
                        sharedPrefEditor.putString("prefParolaSHA1", Strings.getSHA1(txtMevcutParola.getText().toString()));
                        sharedPrefEditor.apply();

                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.parolani_degistir),
                                    getString(R.string.parolan_guncellendi),
                                    activity.getString(R.string.tamam),
                                    "ADDialog_Kapat_GeriGit");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                    } else {
                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.hata),
                                    getString(R.string.parolan_guncellenemedi),
                                    activity.getString(R.string.tamam),
                                    "ADDialog_Kapat_GeriGit");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                    }
                    break;
                case "ADDialog_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    onBackPressed();
                    break;
                case "ADDialog_Kapat_CikisYap":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    AkorDefterimSys.CikisYap();
                    break;
                case "PDBilgiIslem_Timeout":
                    onBackPressed();
                    break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void Kaydet() {
		AkorDefterimSys.KlavyeKapat();

        if(AkorDefterimSys.InternetErisimKontrolu()) {
            txtMevcutParola.setText(txtMevcutParola.getText().toString().trim());
            String MevcutParola = txtMevcutParola.getText().toString().trim();

            txtYeniParola.setText(txtYeniParola.getText().toString().trim());
            String YeniParola = txtYeniParola.getText().toString().trim();

            txtYeniParolaTekrar.setText(txtYeniParolaTekrar.getText().toString().trim());

            if(TextUtils.isEmpty(MevcutParola)) { // Parola alanı boş ise
                txtILMevcutParola.setError(getString(R.string.txtmevcutparola_hata2));
                txtMevcutParola.requestFocus();
                txtMevcutParola.setSelection(txtMevcutParola.length());
                imm.showSoftInput(txtMevcutParola, 0);
            } else if(AkorDefterimSys.EditTextKarakterKontrolMIN(MevcutParola, getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN))) {
                txtILMevcutParola.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN))));
                txtMevcutParola.requestFocus();
                txtMevcutParola.setSelection(txtMevcutParola.length());
                imm.showSoftInput(txtMevcutParola, 0);
            } else if(AkorDefterimSys.EditTextKarakterKontrolMAX(MevcutParola, getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
                txtILMevcutParola.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX))));
                txtMevcutParola.requestFocus();
                txtMevcutParola.setSelection(txtMevcutParola.length());
                imm.showSoftInput(txtMevcutParola, 0);
            } else txtILMevcutParola.setError(null);

            if(TextUtils.isEmpty(YeniParola)) { // Parola alanı boş ise
                txtILYeniParola.setError(getString(R.string.txtsifre_hata1));
                txtYeniParola.requestFocus();
                txtYeniParola.setSelection(txtYeniParola.length());
                imm.showSoftInput(txtYeniParola, 0);
                LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
                LLParolaGuvenligi2.setBackgroundColor(0);
                LLParolaGuvenligi3.setBackgroundColor(0);
            } else if(AkorDefterimSys.EditTextKarakterKontrolMIN(YeniParola, getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN))) {
                txtILYeniParola.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN))));
                txtYeniParola.requestFocus();
                txtYeniParola.setSelection(txtYeniParola.length());
                imm.showSoftInput(txtYeniParola, 0);
                LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
                LLParolaGuvenligi2.setBackgroundColor(0);
                LLParolaGuvenligi3.setBackgroundColor(0);
            } else if(AkorDefterimSys.EditTextKarakterKontrolMAX(YeniParola, getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
                txtILYeniParola.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX))));
                txtYeniParola.requestFocus();
                txtYeniParola.setSelection(txtYeniParola.length());
                imm.showSoftInput(txtYeniParola, 0);
            } else txtILYeniParola.setError(null);

            if(!AkorDefterimSys.EditTextisMatching(txtYeniParola, txtYeniParolaTekrar)) { // Parolalar aynı değil ise
                txtILYeniParolaTekrar.setError(getString(R.string.txtyeniparolatekrar_hata1));
                txtYeniParolaTekrar.requestFocus();
                txtYeniParolaTekrar.setSelection(txtYeniParolaTekrar.length());
                imm.showSoftInput(txtYeniParolaTekrar, 0);
            } else txtILYeniParolaTekrar.setError(null);

            if(txtILMevcutParola.getError() == null && txtILYeniParola.getError() == null && txtILYeniParolaTekrar.getError() == null) {
                btnKapat.setEnabled(false);
                btnKaydet.setEnabled(false);
                AkorDefterimSys.UnFocusEditText(txtMevcutParola);
                AkorDefterimSys.UnFocusEditText(txtYeniParola);
                AkorDefterimSys.UnFocusEditText(txtYeniParolaTekrar);

                if(!AkorDefterimSys.ProgressDialogisShowing(PDBilgiIslem)) {
                    PDBilgiIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_yukleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDBilgiIslem_Timeout");
                    PDBilgiIslem.show();
                }

                AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID",""), "", "", "HesapBilgiGetir");
            }
        } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}