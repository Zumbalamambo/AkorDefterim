package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import ren.qinc.edit.PerformEdit;

@SuppressWarnings("ALL")
public class Sarki_EkleDuzenle_Icerik extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {
    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
	private PerformEdit mPerformEdit;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnKaydet, btnEditor_AKOR, btnEditor_TranspozeArti, btnEditor_TranspozeEksi, btnEditor_KoseliParantez1, btnEditor_KoseliParantez2, btnEditor_Geri, btnEditor_Ileri;
	TextView lblBaslik;
	MaterialEditText txtSarkiIcerik;
	//KnifeText KTEditor;

	//Aztec aztec;
	//AztecText editor;
	//SourceViewEditText sourceeditor;
	//AztecToolbar toolbar;
	//PopupMenu mediaMenu;

	//RTManager rtManager;
	//RTEditText RTeditor;

	int SecilenSarkiID, SecilenListeID, SecilenKategoriID, SecilenTarzID, EditTextCursorNoktasi = 0, StringUzunluk, Start, End;
	String Islem, SecilenSanatciAdi, SecilenSarkiAdi, SecilenSarkiIcerik, IcerikHTML;
	String HTMLBaslangic = "<html><head><style>html, body {top:0; bottom:0; left:0; right:0;}</style></head><body>";
	String HTMLBitis = "</body></html>";
	Boolean KatkidaBulunuluyorMu = false, IcerikDegistirildiMi = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sarki_ekleduzenle_icerik);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
		AkorDefterimSys.activity = activity;
		veritabani = new Veritabani(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		SecilenSarkiID = mBundle.getInt("SecilenSarkiID");
		SecilenSanatciAdi = mBundle.getString("SecilenSanatciAdi");
		SecilenSarkiAdi = mBundle.getString("SecilenSarkiAdi");
		SecilenListeID = mBundle.getInt("SecilenListeID");
		SecilenKategoriID = mBundle.getInt("SecilenKategoriID");
		SecilenTarzID = mBundle.getInt("SecilenTarzID");
		KatkidaBulunuluyorMu = mBundle.getBoolean("KatkidaBulunuluyorMu");
		SecilenSarkiIcerik = mBundle.getString("SecilenSarkiIcerik");

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        btnGeri = findViewById(R.id.btnGeri);
        btnGeri.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

        btnKaydet = findViewById(R.id.btnKaydet);
        btnKaydet.setOnClickListener(this);

		txtSarkiIcerik = findViewById(R.id.txtSarkiIcerik);
		txtSarkiIcerik.setText(Html.fromHtml(AkorDefterimSys.AkorHtmlToTag(SecilenSarkiIcerik)));
		txtSarkiIcerik.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(btnEditor_Geri != null) {
					if(mPerformEdit.history.size() > 0) {
						btnEditor_Geri.setImageDrawable(getResources().getDrawable(R.drawable.ic_undo_siyah));
						btnEditor_Geri.setEnabled(true);
					} else {
						btnEditor_Geri.setImageDrawable(getResources().getDrawable(R.drawable.ic_undo_beyaz));
						btnEditor_Geri.setEnabled(false);
					}
				}

				if(btnEditor_Ileri != null) {
					if(mPerformEdit.historyBack.size() > 0) {
						btnEditor_Ileri.setImageDrawable(getResources().getDrawable(R.drawable.ic_redo_siyah));
						btnEditor_Ileri.setEnabled(true);
					} else {
						btnEditor_Ileri.setImageDrawable(getResources().getDrawable(R.drawable.ic_redo_beyaz));
						btnEditor_Ileri.setEnabled(false);
					}
				}
			}
		});

		mPerformEdit = new PerformEdit(txtSarkiIcerik);
		mPerformEdit.setDefaultText(Html.fromHtml(AkorDefterimSys.AkorHtmlToTag(SecilenSarkiIcerik)));

		btnEditor_AKOR = findViewById(R.id.btnEditor_AKOR);
		btnEditor_AKOR.setOnClickListener(this);

		btnEditor_TranspozeArti = findViewById(R.id.btnEditor_TranspozeArti);
		btnEditor_TranspozeArti.setOnClickListener(this);

		btnEditor_TranspozeEksi = findViewById(R.id.btnEditor_TranspozeEksi);
		btnEditor_TranspozeEksi.setOnClickListener(this);

		btnEditor_KoseliParantez1 = findViewById(R.id.btnEditor_KoseliParantez1);
		btnEditor_KoseliParantez1.setOnClickListener(this);

		btnEditor_KoseliParantez2 = findViewById(R.id.btnEditor_KoseliParantez2);
		btnEditor_KoseliParantez2.setOnClickListener(this);

		btnEditor_Geri = findViewById(R.id.btnEditor_Geri);
		btnEditor_Geri.setOnClickListener(this);

		btnEditor_Ileri = findViewById(R.id.btnEditor_Ileri);
		btnEditor_Ileri.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		AkorDefterimSys.activity = activity;
	}

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		if(mPerformEdit.history.size() != 0 || mPerformEdit.historyBack.size() != 0) {
			if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
				ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
						getString(R.string.sarki_yonetimi),
						getString(R.string.sarki_ekleduzenle_geri_soru),
						getString(R.string.evet),
						"ADDialog_Kapat_GeriGit",
						getString(R.string.hayir),
						"ADDialog_Kapat");
				ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog.show();
			}
		} else super.onBackPressed();
	}
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				onBackPressed();
				break;
            case R.id.btnKaydet:
				if(Islem.equals("SarkiEkle") || Islem.equals("YeniSarkiEkle")) {
					if (veritabani.SarkiEkle(SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenSarkiAdi, SecilenSanatciAdi, AkorDefterimSys.AkorTagToHtml(AkorDefterimSys.KotuIcerikDuzenleme(txtSarkiIcerik)))) {
						if(Islem.equals("YeniSarkiEkle") && KatkidaBulunuluyorMu) {
							if(AkorDefterimSys.InternetErisimKontrolu()) {
								AkorDefterimSys.SarkiGonder(SecilenSanatciAdi, SecilenSarkiAdi, AkorDefterimSys.AkorTagToHtml(AkorDefterimSys.KotuIcerikDuzenleme(txtSarkiIcerik)), sharedPref.getString("prefHesapID", ""));
							} else {
								if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
									ADDialog = AkorDefterimSys.VButtonCustomAlertDialog(activity,
											getString(R.string.sarki_yonetimi),
											getString(R.string.sarki_gonder_internet_hata),
											getString(R.string.yeniden_dene),
											"ADDialog_YenidenDene",
											getString(R.string.sarki_gondermeden_devam_et),
											"ADDialog_Gondermeden_Devam_Et");
									ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
									ADDialog.show();
								}
							}
						} else {
							AkorDefterimSys.prefAction = "Şarkı eklendi";
							AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = SecilenSanatciAdi;
							AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = SecilenSarkiAdi;
							AkorDefterimSys.prefEklenenDuzenlenenSarkiID = SecilenSarkiID;
							AkorDefterimSys.KlavyeKapat();
							finish();
						}
					} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
				} else if(Islem.equals("SarkiDuzenle")) {
					if (veritabani.SarkiDuzenle(SecilenSarkiID, SecilenListeID, SecilenKategoriID, SecilenTarzID, SecilenSanatciAdi, SecilenSarkiAdi, AkorDefterimSys.AkorTagToHtml(AkorDefterimSys.KotuIcerikDuzenleme(txtSarkiIcerik)))) {
						AkorDefterimSys.prefAction = "Şarkı düzenlendi";
						AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = SecilenSanatciAdi;
						AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = SecilenSarkiAdi;
						AkorDefterimSys.prefEklenenDuzenlenenSarkiID = SecilenSarkiID;
						AkorDefterimSys.KlavyeKapat();
						finish();
					} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
				}

                break;
			case R.id.btnEditor_AKOR:
				int SecilenStringBaslangic = txtSarkiIcerik.getSelectionStart();
				int SecilenStringBitis = txtSarkiIcerik.getSelectionEnd();

				if(SecilenStringBaslangic != SecilenStringBitis) {
					String Icerik = txtSarkiIcerik.getText().toString();
					CharSequence SecilenString = txtSarkiIcerik.getText().subSequence(SecilenStringBaslangic, SecilenStringBitis);

					if(AkorDefterimSys.SecilenStringAkorMu(SecilenString.toString())) {
						if(!Icerik.substring(SecilenStringBaslangic - 1, SecilenStringBaslangic).equals(AkorDefterimSys.NormalTag1) && !Icerik.substring(SecilenStringBitis, SecilenStringBitis + 1).equals(AkorDefterimSys.NormalTag2)) {
							txtSarkiIcerik.setText(Icerik.substring(0, SecilenStringBaslangic) + AkorDefterimSys.NormalTag1 + SecilenString + AkorDefterimSys.NormalTag2 + Icerik.substring(SecilenStringBitis, Icerik.length()));
							txtSarkiIcerik.requestFocus();
							txtSarkiIcerik.setSelection(SecilenStringBaslangic, SecilenStringBitis + 2);
						} else AkorDefterimSys.ToastMsj(activity, getString(R.string.secilen_metin_akor_tag_icinde), Toast.LENGTH_SHORT);
					} else AkorDefterimSys.ToastMsj(activity, getString(R.string.secilen_metin_akor_degil), Toast.LENGTH_SHORT);
				}

				AkorDefterimSys.KlavyeKapat();

				break;
			case R.id.btnEditor_TranspozeArti:
				txtSarkiIcerik.setText(Html.fromHtml(AkorDefterimSys.AkorHtmlToTag(AkorDefterimSys.Transpoze("+1", AkorDefterimSys.AkorTagToHtml(AkorDefterimSys.KotuIcerikDuzenleme(txtSarkiIcerik))).toString())));
				txtSarkiIcerik.setSelection(0);
				AkorDefterimSys.KlavyeKapat();

				break;
			case R.id.btnEditor_TranspozeEksi:
				txtSarkiIcerik.setText(Html.fromHtml(AkorDefterimSys.AkorHtmlToTag(AkorDefterimSys.Transpoze("-1", AkorDefterimSys.AkorTagToHtml(AkorDefterimSys.KotuIcerikDuzenleme(txtSarkiIcerik))).toString())));
				txtSarkiIcerik.setSelection(0);
				AkorDefterimSys.KlavyeKapat();

				break;
			case R.id.btnEditor_KoseliParantez1:
				StringUzunluk = txtSarkiIcerik.length();
				Start = txtSarkiIcerik.getSelectionStart();
				End = txtSarkiIcerik.getSelectionEnd();

				txtSarkiIcerik.setText(txtSarkiIcerik.getText().subSequence(0, Start) + AkorDefterimSys.NormalTag1 + txtSarkiIcerik.getText().subSequence(End, StringUzunluk));
				txtSarkiIcerik.setSelection(Start + 1);
				break;
			case R.id.btnEditor_KoseliParantez2:
				StringUzunluk = txtSarkiIcerik.length();
				Start = txtSarkiIcerik.getSelectionStart();
				End = txtSarkiIcerik.getSelectionEnd();

				txtSarkiIcerik.setText(txtSarkiIcerik.getText().subSequence(0, Start) + AkorDefterimSys.NormalTag2 + txtSarkiIcerik.getText().subSequence(End, StringUzunluk));
				txtSarkiIcerik.setSelection(Start + 1);
				break;
			case R.id.btnEditor_Geri:
				mPerformEdit.undo();
				txtSarkiIcerik.setSelection(txtSarkiIcerik.length());
				AkorDefterimSys.KlavyeKapat();
				break;
			case R.id.btnEditor_Ileri:
				mPerformEdit.redo();
				txtSarkiIcerik.setSelection(txtSarkiIcerik.length());
				AkorDefterimSys.KlavyeKapat();
				break;
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

            btnGeri.setEnabled(true);
            btnKaydet.setEnabled(true);
            AkorDefterimSys.DismissProgressDialog(PDIslem);

			switch (JSONSonuc.getString("Islem")) {
                case "SarkiGonder":
                    if(JSONSonuc.getBoolean("Sonuc")) AkorDefterimSys.prefAction = "Şarkı eklendi ve gönderildi";
                    else AkorDefterimSys.prefAction = "Şarkı eklendi ama gönderilemedi";

					AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = SecilenSanatciAdi;
					AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = SecilenSarkiAdi;
					AkorDefterimSys.prefEklenenDuzenlenenSarkiID = SecilenSarkiID;
					AkorDefterimSys.KlavyeKapat();
					finish();

                    break;
				case "ADDialog_YenidenDene":
					AkorDefterimSys.DismissAlertDialog(ADDialog);

					if(AkorDefterimSys.InternetErisimKontrolu()) {
						AkorDefterimSys.SarkiGonder(SecilenSanatciAdi, SecilenSarkiAdi, AkorDefterimSys.AkorTagToHtml(AkorDefterimSys.KotuIcerikDuzenleme(txtSarkiIcerik)), sharedPref.getString("prefHesapID", ""));
					} else {
						if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
							ADDialog = AkorDefterimSys.VButtonCustomAlertDialog(activity,
									getString(R.string.sarki_yonetimi),
									getString(R.string.sarki_gonder_internet_hata),
									getString(R.string.yeniden_dene),
									"ADDialog_YenidenDene",
									getString(R.string.sarki_gondermeden_devam_et),
									"ADDialog_Gondermeden_Devam_Et");
							ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog.show();
						}
					}
					break;
				case "ADDialog_Gondermeden_Devam_Et":
					AkorDefterimSys.DismissAlertDialog(ADDialog);
					AkorDefterimSys.prefAction = "Şarkı eklendi";
					AkorDefterimSys.prefEklenenDuzenlenenSanatciAdi = SecilenSanatciAdi;
					AkorDefterimSys.prefEklenenDuzenlenenSarkiAdi = SecilenSarkiAdi;
					AkorDefterimSys.prefEklenenDuzenlenenSarkiID = SecilenSarkiID;
					AkorDefterimSys.KlavyeKapat();
					finish();
					break;
                case "ADDialog_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    finish();
                    break;
                case "PDIslem_Timeout":
                    onBackPressed();
                    break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}