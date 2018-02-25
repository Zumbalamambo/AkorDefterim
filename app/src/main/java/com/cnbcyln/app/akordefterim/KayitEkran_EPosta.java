package com.cnbcyln.app.akordefterim;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class KayitEkran_EPosta extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	ProgressDialog PDEPosta;
	AlertDialog ADDialog_EPosta;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	TextView lblBaslik, lblVazgec, lblEPostaAciklama;
	TextInputLayout txtILEPosta;
	EditText txtEPosta;

	String DogrulamaKodu = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kayit_ekran_eposta);

		activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.
		//AkorDefterimSys.EkranAnimasyon("Explode");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setTypeface(YaziFontu, Typeface.BOLD);
		lblVazgec.setOnClickListener(this);

		txtILEPosta = findViewById(R.id.txtILEPosta);
		txtILEPosta.setTypeface(YaziFontu);

		txtEPosta = findViewById(R.id.txtEPosta);
		txtEPosta.setTypeface(YaziFontu, Typeface.NORMAL);
		txtEPosta.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILEPosta.setError(null);
			}
		});
		txtEPosta.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					IleriIslem();
				}

				return false;
			}
		});

		lblEPostaAciklama = findViewById(R.id.lblEPostaAciklama);
		lblEPostaAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblEPostaAciklama);

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(this);
	}

    @Override
    protected void onStart() {
        super.onStart();

		AkorDefterimSys.activity = activity;

        if(AkorDefterimSys.prefAction.equals("Vazgec")) {
			AkorDefterimSys.prefAction = "";
            AkorDefterimSys.EkranKapat();
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
			case R.id.coordinatorLayout:
				txtILEPosta.setError(null);
				AkorDefterimSys.UnFocusEditText(txtEPosta);
				break;
			case R.id.btnGeri:
				AkorDefterimSys.EkranKapat();
				break;
			case R.id.lblVazgec:
				AkorDefterimSys.EkranKapat();
				break;
			case R.id.btnIleri:
				IleriIslem();
				break;
		}
	}

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
				case "HesapBilgiGetir":
					// Eğer hesap kontrol'de hesap bulunamadıysa sonuç false döner. Böylelikle kayıt işlemlerine devam edebiliriz.
					if(!JSONSonuc.getBoolean("Sonuc")) {
						// 6 haneli onay kodu oluşturuldu
						DogrulamaKodu = AkorDefterimSys.KodUret(6, true, false, false, false);

						// Onay kodu belirtilen eposta adresine gönderiliyor
						AkorDefterimSys.EPostaGonder(txtEPosta.getText().toString().trim(), "", getString(R.string.dogrulama_kodu), getString(R.string.eposta_dogrulama_kodu_icerik, DogrulamaKodu, getString(R.string.uygulama_adi)));
					} else {
						btnIleri.setEnabled(true);
						// PDEPosta Progress Dialog'u kapattık
						AkorDefterimSys.DismissProgressDialog(PDEPosta);

						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_EPosta)) { // Eğer ADDialog_EPosta açık değilse
							ADDialog_EPosta = AkorDefterimSys.CustomAlertDialog(activity,
									getString(R.string.hata),
									getString(R.string.hata_hesap_durum2, txtEPosta.getText().toString().trim()),
									activity.getString(R.string.tamam),
									"ADDialog_EPosta");
							ADDialog_EPosta.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_EPosta.show();
						}
					}

					break;
				case "ADDialog_EPosta":
					AkorDefterimSys.DismissAlertDialog(ADDialog_EPosta);
					break;
                case "EPostaGonder":
					btnIleri.setEnabled(true);
					// PDEPosta Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDEPosta);

                	// Eğer EPosta gönderildiyse sonuç true döner..
                    if(JSONSonuc.getBoolean("Sonuc")) {
						// Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
						Intent mIntent = new Intent(activity, Dogrulama_Kodu.class);
						mIntent.putExtra("Islem", "Kayit");
						mIntent.putExtra("EPosta", txtEPosta.getText().toString().trim());
						mIntent.putExtra("DogrulamaKodu", String.valueOf(DogrulamaKodu));

						AkorDefterimSys.EkranGetir(mIntent, "Slide");
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));

                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			txtEPosta.setText(txtEPosta.getText().toString().trim());
			String EPosta = txtEPosta.getText().toString();

			if(TextUtils.isEmpty(EPosta)) {
				txtILEPosta.setError(getString(R.string.hata_bos_alan));
				txtEPosta.requestFocus();
				txtEPosta.setSelection(txtEPosta.length());
				imm.showSoftInput(txtEPosta, 0);
			} else if(!AkorDefterimSys.isValid(EPosta, "EPosta")) {
				txtILEPosta.setError(getString(R.string.hata_format_eposta));
				txtEPosta.requestFocus();
				txtEPosta.setSelection(txtEPosta.length());
				imm.showSoftInput(txtEPosta, 0);
			} else if(AkorDefterimSys.isValid(EPosta, "FakeEPosta")) {
				txtILEPosta.setError(getString(R.string.hata_format_eposta));
				txtEPosta.requestFocus();
				txtEPosta.setSelection(txtEPosta.length());
				imm.showSoftInput(txtEPosta, 0);
			} else txtILEPosta.setError(null);

			if(txtILEPosta.getError() == null) {
				btnIleri.setEnabled(false);
				AkorDefterimSys.UnFocusEditText(txtEPosta);

				if(!AkorDefterimSys.ProgressDialogisShowing(PDEPosta)) { // Eğer progress dialog açık değilse
					PDEPosta = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "");
					PDEPosta.show();
				}

				AkorDefterimSys.HesapBilgiGetir(null, "", "", EPosta, "", "", "HesapBilgiGetir");
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}