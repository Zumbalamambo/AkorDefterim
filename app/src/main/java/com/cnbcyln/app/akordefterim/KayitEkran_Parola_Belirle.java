package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.PasswordView;

@SuppressWarnings({"deprecation", "ResultOfMethodCallIgnored", "ConstantConditions"})
public class KayitEkran_Parola_Belirle extends AppCompatActivity implements OnClickListener {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnIleri;
	TextInputLayout txtILParola, txtILParolaTekrar;
	TextView lblVazgec, lblBaslik, lblParolaAciklama;
	PasswordView txtParola, txtParolaTekrar;
	LinearLayout LLParolaGuvenligi1, LLParolaGuvenligi2, LLParolaGuvenligi3;

	String Islem = "", EPosta = "";
	int ParolaKarakterSayisiMIN = 0, ParolaKarakterSayisiMAX = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kayit_ekran_parolabelirle);

		activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		ParolaKarakterSayisiMIN = getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN);
		ParolaKarakterSayisiMAX = getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX);

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		EPosta = mBundle.getString("EPosta");

		coordinatorLayout = activity.findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

		btnGeri = findViewById(R.id.btnGeri);
		btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		lblVazgec = findViewById(R.id.lblVazgec);
		lblVazgec.setTypeface(YaziFontu, Typeface.BOLD);
		lblVazgec.setOnClickListener(this);

		lblParolaAciklama = findViewById(R.id.lblParolaAciklama);
		lblParolaAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
		lblParolaAciklama.setText(getString(R.string.parola_ekran_aciklama, String.valueOf(ParolaKarakterSayisiMIN), String.valueOf(ParolaKarakterSayisiMAX)));
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

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		/*if(Islem.equals("Giris_Yardimi")) {
			btnIleri.setText(getString(R.string.tamamla));
			btnIleri.setBackground(getResources().getDrawable(R.drawable.custom_button_yesil));
			btnIleri.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_check_beyaz), null);
		}*/
		btnIleri.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;

		if(AkorDefterimSys.prefAction.equals("Vazgec")) onBackPressed();
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
				txtILParola.setError(null);
				AkorDefterimSys.UnFocusEditText(txtParola);
				txtILParolaTekrar.setError(null);
				AkorDefterimSys.UnFocusEditText(txtParolaTekrar);
				break;
			case R.id.btnGeri:
				onBackPressed();
				break;
			case R.id.lblVazgec:
				AkorDefterimSys.prefAction = "Vazgec";
				onBackPressed();
				break;
			case R.id.btnIleri:
				IleriIslem();
				break;
		}
	}

	private void IleriIslem() {
		AkorDefterimSys.KlavyeKapat();

		if(AkorDefterimSys.InternetErisimKontrolu()) {
			txtParola.setText(txtParola.getText().toString().trim());
			String Parola = txtParola.getText().toString().trim();

			txtParolaTekrar.setText(txtParolaTekrar.getText().toString().trim());

			if(TextUtils.isEmpty(Parola)) { // Parola alanı boş ise
				txtILParola.setError(getString(R.string.txtsifre_hata1));
				txtParola.requestFocus();
				txtParola.setSelection(txtParola.length());
				imm.showSoftInput(txtParola, 0);
				LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
				LLParolaGuvenligi2.setBackgroundColor(0);
				LLParolaGuvenligi3.setBackgroundColor(0);
			} else if(AkorDefterimSys.EditTextKarakterKontrolMIN(Parola, getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN))) {
				txtILParola.setError(getString(R.string.hata_en_az_karakter, String.valueOf(getResources().getInteger(R.integer.ParolaKarakterSayisi_MIN))));
				txtParola.requestFocus();
				txtParola.setSelection(txtParola.length());
				imm.showSoftInput(txtParola, 0);
				LLParolaGuvenligi1.setBackgroundColor(getResources().getColor(R.color.Kirmizi));
				LLParolaGuvenligi2.setBackgroundColor(0);
				LLParolaGuvenligi3.setBackgroundColor(0);
			} else if(AkorDefterimSys.EditTextKarakterKontrolMAX(Parola, getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
				txtILParola.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(getResources().getInteger(R.integer.ParolaKarakterSayisi_MAX))));
				txtParola.requestFocus();
				txtParola.setSelection(txtParola.length());
				imm.showSoftInput(txtParola, 0);
			} else txtILParola.setError(null);

			if(!AkorDefterimSys.EditTextisMatching(txtParola, txtParolaTekrar)) { // Parolalar aynı değil ise
				txtILParolaTekrar.setError(getString(R.string.txtparolatekrar_hata1));
				txtParolaTekrar.requestFocus();
				txtParolaTekrar.setSelection(txtParolaTekrar.length());
				imm.showSoftInput(txtParolaTekrar, 0);
			} else txtILParolaTekrar.setError(null);

			if(txtILParola.getError() == null && txtILParolaTekrar.getError() == null) {
				btnIleri.setEnabled(false);
				AkorDefterimSys.UnFocusEditText(txtParola);
				AkorDefterimSys.UnFocusEditText(txtParolaTekrar);

				Intent mIntent = new Intent(activity, KayitEkran_AdSoyad_DTarih_Resim.class);
				mIntent.putExtra("EPosta", EPosta);
				mIntent.putExtra("Parola", txtParola.getText().toString().trim());

				AkorDefterimSys.EkranGetir(mIntent, "Slide");

				btnIleri.setEnabled(true);
			}
		} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
	}
}