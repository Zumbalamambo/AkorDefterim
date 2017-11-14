package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.PasswordView;
import com.cnbcyln.app.akordefterim.util.Strings;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class ParolaBelirle extends AppCompatActivity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	Typeface YaziFontu;
    SharedPreferences.Editor sharedPrefEditor;
	AlertDialog ADDialog_ParolaBelirle;
	ProgressDialog PDParolaBelirle;

	CoordinatorLayout coordinatorLayout;
	Button btnGeri, btnIleri;
	TextInputLayout txtILParola, txtILParolaTekrar;
	TextView lblVazgec, lblBaslik, lblParolaAciklama, lblParolaGuvenligi;
	PasswordView txtParola, txtParolaTekrar;
	LinearLayout LLParolaGuvenligi1, LLParolaGuvenligi2, LLParolaGuvenligi3;

	String Islem, EPosta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parolabelirle);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		EPosta = mBundle.getString("EPosta");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txtILParola.setError(null);
                AkorDefterimSys.UnFocusEditText(txtParola);
				txtILParolaTekrar.setError(null);
				AkorDefterimSys.UnFocusEditText(txtParolaTekrar);
			}
		});

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
                sharedPrefEditor.putString("GelinenEkran", "ParolaBelirle");
                sharedPrefEditor.apply();

				onBackPressed();
			}
		});

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
				sharedPrefEditor.putString("Action", "Vazgec");
				sharedPrefEditor.apply();

				onBackPressed();
			}
		});

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblParolaAciklama = findViewById(R.id.lblParolaAciklama);
		lblParolaAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblParolaAciklama);

		txtILParola = findViewById(R.id.txtILParola);
		txtILParola.setTypeface(YaziFontu);

		txtParola = findViewById(R.id.txtParola);
		txtParola.setTypeface(YaziFontu, Typeface.NORMAL);
		txtParola.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				txtILParola.setError(null);

				String Parola = txtParola.getText().toString().trim();
				int ParolaGuvenligiSkor = AkorDefterimSys.ParolaGuvenligi(Parola);

				if(ParolaGuvenligiSkor < 30) {
					LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
					LLParolaGuvenligi2.setBackgroundColor(0);
					LLParolaGuvenligi3.setBackgroundColor(0);
					AkorDefterimSys.setTextViewHTML(lblParolaGuvenligi);
				} else if(ParolaGuvenligiSkor >= 30 && ParolaGuvenligiSkor < 90) {
					LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.TuruncuYazi));
					LLParolaGuvenligi2.setBackgroundColor(getResources().getColor(R.color.TuruncuYazi));
					LLParolaGuvenligi3.setBackgroundColor(0);
					AkorDefterimSys.setTextViewHTML(lblParolaGuvenligi);
				} else if(ParolaGuvenligiSkor >= 90) {
					LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Yesil2));
					LLParolaGuvenligi2.setBackgroundColor(getResources().getColor(R.color.Yesil2));
					LLParolaGuvenligi3.setBackgroundColor(getResources().getColor(R.color.Yesil2));
					AkorDefterimSys.setTextViewHTML(lblParolaGuvenligi);
				}
			}
		});

		txtILParolaTekrar = findViewById(R.id.txtILParolaTekrar);
		txtILParolaTekrar.setTypeface(YaziFontu);

		txtParolaTekrar = findViewById(R.id.txtParolaTekrar);
		txtParolaTekrar.setTypeface(YaziFontu, Typeface.NORMAL);
		txtParolaTekrar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
					IleriIslem();
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

		lblParolaGuvenligi = findViewById(R.id.lblParolaGuvenligi);
		lblParolaGuvenligi.setTypeface(YaziFontu, Typeface.NORMAL);
		AkorDefterimSys.setTextViewHTML(lblParolaGuvenligi);

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		if(Islem.equals("ParolamiUnuttum")) {
			btnIleri.setText(getString(R.string.tamamla));
			btnIleri.setBackground(getResources().getDrawable(R.drawable.custom_button_yesil));
			btnIleri.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_beyaz), null);
		}
		btnIleri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IleriIslem();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(AkorDefterimSys.PrefAyarlar().getString("Action", "").equals("Vazgec")) onBackPressed();
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();

		super.onBackPressed();
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "HesapParolaDegistir":
					// PDParolaBelirle Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDParolaBelirle);

					if(JSONSonuc.getBoolean("Sonuc")) {
						ADDialog_ParolaBelirle = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								getString(R.string.hesap_bilgileri),
								getString(R.string.parola_degistirildi),
								activity.getString(R.string.tamam));
						ADDialog_ParolaBelirle.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_ParolaBelirle.show();

						ADDialog_ParolaBelirle.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_ParolaBelirle.dismiss();

								sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
								sharedPrefEditor.putString("Action", "Vazgec");
								sharedPrefEditor.apply();

								onBackPressed();
							}
						});
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		String Parola = txtParola.getText().toString().trim();

		if(TextUtils.isEmpty(Parola)) { // Parola alanı boş ise
			txtILParola.setError(getString(R.string.txtsifre_hata1));
			LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
			LLParolaGuvenligi2.setBackgroundColor(0);
			LLParolaGuvenligi3.setBackgroundColor(0);
			AkorDefterimSys.setTextViewHTML(lblParolaGuvenligi);
		} else if(AkorDefterimSys.EditTextKarakterKontrolMIN(Parola, getResources().getInteger(R.integer.SifreKarakterSayisi_MIN))) {
			txtILParola.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.SifreKarakterSayisi_MIN))));
			LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
			LLParolaGuvenligi2.setBackgroundColor(0);
			LLParolaGuvenligi3.setBackgroundColor(0);
			AkorDefterimSys.setTextViewHTML(lblParolaGuvenligi);
		} else if(AkorDefterimSys.EditTextKarakterKontrolMAX(Parola, getResources().getInteger(R.integer.SifreKarakterSayisi_MAX)))
			txtILParola.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(getResources().getInteger(R.integer.SifreKarakterSayisi_MAX))));
		else
			txtILParola.setError(null);

		if(!AkorDefterimSys.EditTextisMatching(txtParola, txtParolaTekrar)) // Parolalar aynı değil ise
			txtILParolaTekrar.setError(getString(R.string.txtparolatekrar_hata1));
		else
			txtILParolaTekrar.setError(null);

		if(txtILParola.getError() == null && txtILParolaTekrar.getError() == null) {
			if(AkorDefterimSys.InternetErisimKontrolu()) {
				if(Islem.equals("Kayit")) SonrakiEkran();
				else if(Islem.equals("ParolamiUnuttum")) {
					PDParolaBelirle = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
					PDParolaBelirle.show();

					AkorDefterimSys.HesapParolaDegistir(EPosta, Parola, Strings.getSHA1(Parola));
				}
			} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
		}
	}

	private void SonrakiEkran() {
		Intent mIntent = new Intent(activity, KayitEkran_AdSoyad_DTarih_Resim.class);
		mIntent.putExtra("EPosta", EPosta);
		mIntent.putExtra("Parola", txtParola.getText().toString().trim());

		AkorDefterimSys.EkranGetir(mIntent, "Slide");
	}
}