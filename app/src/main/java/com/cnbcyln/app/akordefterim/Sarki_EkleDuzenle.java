package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategori;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelerSPN;
import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarz;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.Siniflar.SnfSarkilar;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressWarnings("ALL")
public class Sarki_EkleDuzenle extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {
    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
	View ViewDialogContent;
	LayoutInflater inflater;
	InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
	ConstraintLayout ConstraintLayout2;
	TextInputLayout txtILSanatciAdi, txtILSarkiAdi;
	EditText txtSanatciAdi, txtSarkiAdi;
	ImageButton btnGeri, btnListeEkle, btnListeDuzenle, btnListeSil, btnKategoriEkle, btnKategoriDuzenle, btnKategoriSil, btnTarzEkle, btnTarzDuzenle, btnTarzSil;
	Button btnIleri;
	TextView lblBaslik, lblListeler, lblKategoriler, lblTarzlar;
	Spinner spnListeler, spnKategoriler, spnTarzlar;
	CheckBox ChkIcerikGonder;

	int SecilenSarkiID = 0, SecilenListeID = 0, SecilenKategoriID = 0, SecilenTarzID = 0, AdSoyadKarakterSayisi_MIN = 0, AdSoyadKarakterSayisi_MAX = 0, SarkiAdiKarakterSayisi_MIN = 0, SarkiAdiKarakterSayisi_MAX = 0;
	String Islem = "", SecilenSanatciAdi = "", SecilenSarkiAdi = "", SecilenListeAdi = "", SecilenKategoriAdi = "", SecilenTarzAdi = "", SecilenSarkiIcerik = "";

	private SnfSarkilar snfSarkilar;
	private List<SnfListeler> snfListeler;
	private List<SnfKategoriler> snfKategoriler;
	private List<SnfTarzlar> snfTarzlar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sarki_ekleduzenle);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		veritabani = new Veritabani(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik
		inflater = activity.getLayoutInflater();

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		AdSoyadKarakterSayisi_MIN = getResources().getInteger(R.integer.AdSoyadKarakterSayisi_MIN);
		AdSoyadKarakterSayisi_MAX = getResources().getInteger(R.integer.AdSoyadKarakterSayisi_MAX);
		SarkiAdiKarakterSayisi_MIN = getResources().getInteger(R.integer.SarkiAdiKarakterSayisi_MIN);
		SarkiAdiKarakterSayisi_MAX = getResources().getInteger(R.integer.SarkiAdiKarakterSayisi_MAX);

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        btnGeri = findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

		ConstraintLayout2 = findViewById(R.id.ConstraintLayout2);
		ConstraintLayout2.setOnClickListener(this);

		txtILSanatciAdi = findViewById(R.id.txtILSanatciAdi);
		txtILSanatciAdi.setTypeface(YaziFontu);

		txtSanatciAdi = findViewById(R.id.txtSanatciAdi);
		txtSanatciAdi.setTypeface(YaziFontu, Typeface.NORMAL);

		txtILSarkiAdi = findViewById(R.id.txtILSarkiAdi);
		txtILSarkiAdi.setTypeface(YaziFontu);

		txtSarkiAdi = findViewById(R.id.txtSarkiAdi);
		txtSarkiAdi.setTypeface(YaziFontu, Typeface.NORMAL);

		lblListeler = findViewById(R.id.lblListeler);
		lblListeler.setTypeface(YaziFontu, Typeface.BOLD);

		spnListeler = findViewById(R.id.spnListeler);
		spnListeler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SecilenListeID = snfListeler.get(position).getId();
				SecilenListeAdi = snfListeler.get(position).getListeAdi();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		btnListeEkle = findViewById(R.id.btnListeEkle);
		btnListeEkle.setOnClickListener(this);

		btnListeDuzenle = findViewById(R.id.btnListeDuzenle);
		btnListeDuzenle.setOnClickListener(this);

		btnListeSil = findViewById(R.id.btnListeSil);
		btnListeSil.setOnClickListener(this);

		lblKategoriler = findViewById(R.id.lblKategoriler);
		lblKategoriler.setTypeface(YaziFontu, Typeface.BOLD);

		spnKategoriler = findViewById(R.id.spnKategoriler);
		spnKategoriler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SecilenKategoriID = snfKategoriler.get(position).getId();
				SecilenKategoriAdi = snfKategoriler.get(position).getKategoriAdi();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		btnKategoriEkle = findViewById(R.id.btnKategoriEkle);
		btnKategoriEkle.setOnClickListener(this);

		btnKategoriDuzenle = findViewById(R.id.btnKategoriDuzenle);
		btnKategoriDuzenle.setOnClickListener(this);

		btnKategoriSil = findViewById(R.id.btnKategoriSil);
		btnKategoriSil.setOnClickListener(this);

		lblTarzlar = findViewById(R.id.lblTarzlar);
		lblTarzlar.setTypeface(YaziFontu, Typeface.BOLD);

		spnTarzlar = findViewById(R.id.spnTarzlar);
		spnTarzlar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SecilenTarzID = snfTarzlar.get(position).getId();
				SecilenTarzAdi = snfTarzlar.get(position).getTarzAdi();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		btnTarzEkle = findViewById(R.id.btnTarzEkle);
		btnTarzEkle.setOnClickListener(this);

		btnTarzDuzenle = findViewById(R.id.btnTarzDuzenle);
		btnTarzDuzenle.setOnClickListener(this);

		btnTarzSil = findViewById(R.id.btnTarzSil);
		btnTarzSil.setOnClickListener(this);

		ChkIcerikGonder = findViewById(R.id.ChkIcerikGonder);
		ChkIcerikGonder.setTypeface(YaziFontu);
		ChkIcerikGonder.setVisibility(View.GONE);
		AkorDefterimSys.setTextViewHTML(ChkIcerikGonder);

		btnIleri = findViewById(R.id.btnIleri);
		btnIleri.setTypeface(YaziFontu, Typeface.NORMAL);
		btnIleri.setOnClickListener(this);

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		SecilenSarkiID = mBundle.getInt("SecilenSarkiID");
		SecilenSanatciAdi = mBundle.getString("SecilenSanatciAdi");
		SecilenSarkiAdi = mBundle.getString("SecilenSarkiAdi");
		SecilenSarkiIcerik = mBundle.getString("SecilenSarkiIcerik");

		if(Islem.equals("YeniSarkiEkle") && AkorDefterimSys.GirisYapildiMi()) ChkIcerikGonder.setVisibility(View.VISIBLE);

		txtSanatciAdi.setText(SecilenSanatciAdi);
		txtSarkiAdi.setText(SecilenSarkiAdi);

		snfSarkilar = veritabani.SnfSarkiGetir(SecilenSarkiID);

		spnListeGetir();
		spnKategoriGetir();
		spnTarzGetir();
		SarkiyaAitBilgilerleSpinnerDoldur();
	}

	@Override
	protected void onStart() {
		super.onStart();

		AkorDefterimSys.activity = activity;

		if(AkorDefterimSys.prefAction.equals("Şarkı eklendi") ||
				AkorDefterimSys.prefAction.equals("Şarkı düzenlendi") ||
				AkorDefterimSys.prefAction.equals("Şarkı eklendi ve gönderildi") ||
				AkorDefterimSys.prefAction.equals("Şarkı eklendi ama gönderilemedi")) AkorDefterimSys.EkranKapat();
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
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);
				break;
			case R.id.ConstraintLayout2:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);
				break;
			case R.id.btnGeri:
				AkorDefterimSys.EkranKapat();
				break;
			case R.id.btnIleri:
				Ileri();
				break;
			case R.id.btnListeEkle:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
					ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
							getString(R.string.ekle),
							"",
							getString(R.string.liste_adi),
							ViewDialogContent,
							getString(R.string.ekle), "ADDialog_ListeEkle",
							getString(R.string.iptal), "ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
				break;
			case R.id.btnListeSil:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				SecilenListeID = snfListeler.get(spnListeler.getSelectedItemPosition()).getId();
				SecilenListeAdi = snfListeler.get(spnListeler.getSelectedItemPosition()).getListeAdi();

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
							getString(R.string.sil),
							getString(R.string.liste_sil_soru, SecilenListeAdi),
							getString(R.string.evet),"ADDialog_ListeSil",
							getString(R.string.hayir),"ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();
				}
				break;
			case R.id.btnListeDuzenle:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				SecilenListeID = snfListeler.get(spnListeler.getSelectedItemPosition()).getId();
				SecilenListeAdi = snfListeler.get(spnListeler.getSelectedItemPosition()).getListeAdi();

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
					ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
							getString(R.string.duzenle),
							SecilenListeAdi,
							getString(R.string.liste_adi),
							ViewDialogContent,
							getString(R.string.duzenle), "ADDialog_ListeDuzenle",
							getString(R.string.iptal), "ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
				break;
			case R.id.btnKategoriEkle:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
					ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
							getString(R.string.ekle),
							"",
							getString(R.string.kategori_adi),
							ViewDialogContent,
							getString(R.string.ekle), "ADDialog_KategoriEkle",
							getString(R.string.iptal), "ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
				break;
			case R.id.btnKategoriSil:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				SecilenKategoriID = snfKategoriler.get(spnKategoriler.getSelectedItemPosition()).getId();
				SecilenKategoriAdi = snfKategoriler.get(spnKategoriler.getSelectedItemPosition()).getKategoriAdi();

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
							getString(R.string.sil),
							getString(R.string.kategori_sil_soru, SecilenKategoriAdi),
							getString(R.string.evet),"ADDialog_KategoriSil",
							getString(R.string.hayir),"ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();
				}
				break;
			case R.id.btnKategoriDuzenle:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				SecilenKategoriID = snfKategoriler.get(spnKategoriler.getSelectedItemPosition()).getId();
				SecilenKategoriAdi = snfKategoriler.get(spnKategoriler.getSelectedItemPosition()).getKategoriAdi();

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
					ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
							getString(R.string.duzenle),
							SecilenKategoriAdi,
							getString(R.string.kategori_adi),
							ViewDialogContent,
							getString(R.string.duzenle),"ADDialog_KategoriDuzenle",
							getString(R.string.iptal),"ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
				break;
			case R.id.btnTarzEkle:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
					ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
							getString(R.string.ekle),
							"",
							getString(R.string.tarz_adi),
							ViewDialogContent,
							getString(R.string.ekle), "ADDialog_TarzEkle",
							getString(R.string.iptal), "ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
				break;
			case R.id.btnTarzSil:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				SecilenTarzID = snfTarzlar.get(spnTarzlar.getSelectedItemPosition()).getId();
				SecilenTarzAdi = snfTarzlar.get(spnTarzlar.getSelectedItemPosition()).getTarzAdi();

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
							getString(R.string.sil),
							getString(R.string.tarz_sil_soru, SecilenTarzAdi),
							getString(R.string.evet),"ADDialog_TarzSil",
							getString(R.string.hayir),"ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();
				}
				break;
			case R.id.btnTarzDuzenle:
				AkorDefterimSys.UnFocusAll(null,null,null,null,ConstraintLayout2);

				SecilenTarzID = snfTarzlar.get(spnTarzlar.getSelectedItemPosition()).getId();
				SecilenTarzAdi = snfTarzlar.get(spnTarzlar.getSelectedItemPosition()).getTarzAdi();

				if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
					ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
					ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
							getString(R.string.duzenle),
							SecilenTarzAdi,
							getString(R.string.tarz_adi),
							ViewDialogContent,
							getString(R.string.duzenle),"ADDialog_TarzDuzenle",
							getString(R.string.iptal),"ADDialog_Kapat");
					ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
					ADDialog.show();

					ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				}
				break;
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "ADDialog_ListeEkle":
					if(!veritabani.ListeVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
						if(veritabani.ListeEkle(JSONSonuc.getString("InputIcerik"))) {
							spnListeGetir();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_eklendi, JSONSonuc.getString("InputIcerik")));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_eklenemedi_hata1, JSONSonuc.getString("InputIcerik")));

					ADDialog.dismiss();
					break;
				case "ADDialog_ListeDuzenle":
					if(!veritabani.ListeVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
						if(veritabani.ListeDuzenle(SecilenListeID, JSONSonuc.getString("InputIcerik"))) {
							spnListeGetir();
							SarkiyaAitBilgilerleSpinnerDoldur();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_duzenlendi, JSONSonuc.getString("InputEskiIcerik"), JSONSonuc.getString("InputIcerik")));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					}

					ADDialog.dismiss();
					break;
				case "ADDialog_ListeSil":
					if(!veritabani.ListeyeAitSarkiVarmiKontrol(SecilenListeID)) {
						if(veritabani.ListeSil(SecilenListeID)) {
							spnListeGetir();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_silindi, SecilenListeAdi));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_silinemedi_hata1, SecilenListeAdi));

					ADDialog.dismiss();
					break;
				case "ADDialog_KategoriEkle":
					if(!veritabani.KategoriVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
						if(veritabani.KategoriEkle(JSONSonuc.getString("InputIcerik"))) {
							spnKategoriGetir();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_eklendi, JSONSonuc.getString("InputIcerik")));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_eklenemedi_hata1, JSONSonuc.getString("InputIcerik")));

					ADDialog.dismiss();
					break;
				case "ADDialog_KategoriDuzenle":
					if(!veritabani.KategoriVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
						if(veritabani.KategoriDuzenle(SecilenKategoriID, JSONSonuc.getString("InputIcerik"))) {
							spnKategoriGetir();
							SarkiyaAitBilgilerleSpinnerDoldur();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_duzenlendi, JSONSonuc.getString("InputEskiIcerik"), JSONSonuc.getString("InputIcerik")));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					}

					ADDialog.dismiss();
					break;
				case "ADDialog_KategoriSil":
					if(!veritabani.KategoriyeAitSarkiVarmiKontrol(SecilenKategoriID)) {
						if(veritabani.KategoriSil(SecilenKategoriID)) {
							spnKategoriGetir();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_silindi, SecilenKategoriAdi));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_silinemedi_hata1, SecilenKategoriAdi));

					ADDialog.dismiss();
					break;
				case "ADDialog_TarzEkle":
					if(!veritabani.TarzVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
						if(veritabani.TarzEkle(JSONSonuc.getString("InputIcerik"))) {
							spnTarzGetir();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_eklendi, JSONSonuc.getString("InputIcerik")));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_eklenemedi_hata1, JSONSonuc.getString("InputIcerik")));

					ADDialog.dismiss();
					break;
				case "ADDialog_TarzDuzenle":
					if(!veritabani.TarzVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
						if(veritabani.TarzDuzenle(SecilenTarzID, JSONSonuc.getString("InputIcerik"))) {
							spnTarzGetir();
							SarkiyaAitBilgilerleSpinnerDoldur();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_duzenlendi, JSONSonuc.getString("InputEskiIcerik"), JSONSonuc.getString("InputIcerik")));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					}

					ADDialog.dismiss();
					break;
				case "ADDialog_TarzSil":
					if(!veritabani.TarzaAitSarkiVarmiKontrol(SecilenTarzID)) {
						if(veritabani.TarzSil(SecilenTarzID)) {
							spnTarzGetir();
							AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_silindi, SecilenTarzAdi));
						} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
					} else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_silinemedi_hata1, SecilenTarzAdi));

					ADDialog.dismiss();
					break;
				case "ADDialog_Kapat":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    AkorDefterimSys.EkranKapat();
                    break;
                case "PDIslem_Timeout":
					AkorDefterimSys.DismissProgressDialog(PDIslem);
                    AkorDefterimSys.EkranKapat();
                    break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void spnListeGetir() {
		snfListeler = veritabani.SnfListeGetir("Cevrimdisi", false);
		AdpListelerSPN AdpListelerSPN = new AdpListelerSPN(activity, snfListeler);
		spnListeler.setAdapter(AdpListelerSPN);

		if(snfListeler.get(0).getListeAdi().equals("")) {
			btnListeSil.setEnabled(false);
			btnListeSil.setImageResource(R.drawable.ic_negative);
			btnListeDuzenle.setEnabled(false);
			btnListeDuzenle.setImageResource(R.drawable.ic_pencil);
		} else {
			btnListeSil.setEnabled(true);
			btnListeSil.setImageResource(R.drawable.ic_negative_siyah);
			btnListeDuzenle.setEnabled(true);
			btnListeDuzenle.setImageResource(R.drawable.ic_pencil_siyah);
		}
	}

	private void spnKategoriGetir() {
		snfKategoriler = veritabani.SnfKategoriGetir(false);
		AdpKategori AdpKategoriler = new AdpKategori(activity, snfKategoriler);
		spnKategoriler.setAdapter(AdpKategoriler);

		if(snfKategoriler.get(0).getKategoriAdi().equals("")) {
			btnKategoriSil.setEnabled(false);
			btnKategoriSil.setImageResource(R.drawable.ic_negative);
			btnKategoriDuzenle.setEnabled(false);
			btnKategoriDuzenle.setImageResource(R.drawable.ic_pencil);
		} else {
			btnKategoriSil.setEnabled(true);
			btnKategoriSil.setImageResource(R.drawable.ic_negative_siyah);
			btnKategoriDuzenle.setEnabled(true);
			btnKategoriDuzenle.setImageResource(R.drawable.ic_pencil_siyah);
		}
	}

	private void spnTarzGetir() {
		snfTarzlar = veritabani.SnfTarzGetir(false);
		AdpTarz AdpTarzlar = new AdpTarz(activity, snfTarzlar);
		spnTarzlar.setAdapter(AdpTarzlar);

		if(snfTarzlar.get(0).getTarzAdi().equals("")) {
			btnTarzSil.setEnabled(false);
			btnTarzSil.setImageResource(R.drawable.ic_negative);
			btnTarzDuzenle.setEnabled(false);
			btnTarzDuzenle.setImageResource(R.drawable.ic_pencil);
		} else {
			btnTarzSil.setEnabled(true);
			btnTarzSil.setImageResource(R.drawable.ic_negative_siyah);
			btnTarzDuzenle.setEnabled(true);
			btnTarzDuzenle.setImageResource(R.drawable.ic_pencil_siyah);
		}
	}

	private void SarkiyaAitBilgilerleSpinnerDoldur() {
		if(Islem.equals("SarkiDuzenle")) {
			for(int i = 0; 0 < snfListeler.size(); i++) {
				if(snfListeler.get(i).getId() == snfSarkilar.getListeID()) {
					spnListeler.setSelection(i);
					break;
				}
			}

			for(int i = 0; 0 < snfKategoriler.size(); i++) {
				if(snfKategoriler.get(i).getId() == snfSarkilar.getKategoriID()) {
					spnKategoriler.setSelection(i);
					break;
				}
			}

			for(int i = 0; 0 < snfTarzlar.size(); i++) {
				if(snfTarzlar.get(i).getId() == snfSarkilar.getTarzID()) {
					spnTarzlar.setSelection(i);
					break;
				}
			}
		}
	}

	private void Ileri() {
		AkorDefterimSys.KlavyeKapat();

		if(ChkIcerikGonder.isChecked()) {
			if(!AkorDefterimSys.InternetErisimKontrolu()) {
				AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi_sarki_ekle_katki));
				return;
			}
		}

		txtSanatciAdi.setText(txtSanatciAdi.getText().toString().trim());
		SecilenSanatciAdi = txtSanatciAdi.getText().toString();

		txtSarkiAdi.setText(txtSarkiAdi.getText().toString().trim());
		SecilenSarkiAdi = txtSarkiAdi.getText().toString().trim();

		if (TextUtils.isEmpty(SecilenSanatciAdi))
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSanatciAdi, txtSanatciAdi, getString(R.string.hata_bos_alan));
		else if (!AkorDefterimSys.isValid(SecilenSanatciAdi, "SadeceSayiKucukHarfBuyukHarfOzelKarakterBosluklu"))
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSanatciAdi, txtSanatciAdi, getString(R.string.hata_format_sadece_sayi_kucukharf_buyukharf_ozel_karakter_bosluklu));
		else if(SecilenSanatciAdi.length() < AdSoyadKarakterSayisi_MIN)
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSanatciAdi, txtSanatciAdi, getString(R.string.hata_en_az_karakter, String.valueOf(AdSoyadKarakterSayisi_MIN)));
		else if(SecilenSanatciAdi.length() > AdSoyadKarakterSayisi_MAX)
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSanatciAdi, txtSanatciAdi, getString(R.string.hata_en_fazla_karakter, String.valueOf(AdSoyadKarakterSayisi_MAX)));
		else txtILSanatciAdi.setError(null);

		if (TextUtils.isEmpty(SecilenSarkiAdi))
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSarkiAdi, txtSarkiAdi, getString(R.string.hata_bos_alan));
		else if (!AkorDefterimSys.isValid(SecilenSarkiAdi, "SadeceSayiKucukHarfBuyukHarfOzelKarakterBosluklu"))
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSarkiAdi, txtSarkiAdi, getString(R.string.hata_format_sadece_sayi_kucukharf_buyukharf_ozel_karakter_bosluklu));
		else if(SecilenSarkiAdi.length() < SarkiAdiKarakterSayisi_MIN)
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSarkiAdi, txtSarkiAdi, getString(R.string.hata_en_az_karakter, String.valueOf(SarkiAdiKarakterSayisi_MIN)));
		else if(SecilenSarkiAdi.length() > SarkiAdiKarakterSayisi_MAX)
			AkorDefterimSys.txtInputHataMesajiGoster(imm, txtILSarkiAdi, txtSarkiAdi, getString(R.string.hata_en_fazla_karakter, String.valueOf(SarkiAdiKarakterSayisi_MAX)));
		else txtILSarkiAdi.setError(null);

		if(txtILSanatciAdi.getError() == null && txtILSarkiAdi.getError() == null) {
			if(SecilenListeAdi.equals("")) AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_bulunamadi));
			else if(SecilenKategoriAdi.equals("")) AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_bulunamadi));
			else if(SecilenTarzAdi.equals("")) AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_bulunamadi));
			else {
				AkorDefterimSys.UnFocusAll(null, null, null, null, ConstraintLayout2);

				Intent mIntent = new Intent(activity, Sarki_EkleDuzenle_Icerik.class);
				mIntent.putExtra("Islem", Islem);
				mIntent.putExtra("SecilenSarkiID", SecilenSarkiID);
				mIntent.putExtra("SecilenSanatciAdi", SecilenSanatciAdi);
				mIntent.putExtra("SecilenSarkiAdi", SecilenSarkiAdi);
				mIntent.putExtra("SecilenListeID", SecilenListeID);
				mIntent.putExtra("SecilenKategoriID", SecilenKategoriID);
				mIntent.putExtra("SecilenTarzID", SecilenTarzID);
				mIntent.putExtra("KatkidaBulunuluyorMu", ChkIcerikGonder.isChecked());
				mIntent.putExtra("SecilenSarkiIcerik", SecilenSarkiIcerik);

				AkorDefterimSys.EkranGetir(mIntent, "Slide");
			}
		}
	}
}