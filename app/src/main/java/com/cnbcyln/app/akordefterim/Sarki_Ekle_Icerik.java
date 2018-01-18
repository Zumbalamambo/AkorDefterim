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
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.mthli.knife.KnifeText;

@SuppressWarnings("ALL")
public class Sarki_Ekle_Icerik extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri, btnKaydet, btnEditor_AKOR, btnEditor_TranspozeArti, btnEditor_TranspozeEksi;
	TextView lblBaslik;
	KnifeText KTEditor;
	WebView WVSarkiIcerik;

	int SecilenListeID, SecilenKategoriID, SecilenTarzID;
	String Islem, SecilenSanatciAdi, SecilenSarkiAdi, SecilenSarkiIcerik, SecilenSarkiIcerikTemp, IcerikHTML, Icerik;
	String HTMLBaslangic = "<html><head><style>html, body {top:0; bottom:0; left:0; right:0;}</style></head><body>";
	String HTMLBitis = "</body></html>";
	Boolean KatkidaBulunuluyorMu = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sarki_ekle_icerik);

        activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		veritabani = new Veritabani(activity);
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

        btnKaydet = findViewById(R.id.btnKaydet);
        btnKaydet.setOnClickListener(this);

		KTEditor = findViewById(R.id.KTEditor);

		WVSarkiIcerik = findViewById(R.id.WVSarkiIcerik);
		WVSarkiIcerik.getSettings().setJavaScriptEnabled(true);
		WVSarkiIcerik.getSettings().setBuiltInZoomControls(true);
		WVSarkiIcerik.getSettings().setDisplayZoomControls(false);
		WVSarkiIcerik.addJavascriptInterface(new Object() {
			@JavascriptInterface
			public void performClick(String akor) {
				AkorDefterimSys.AkorCetveli(activity, akor);
			}
		}, "AkorGosterici");

		btnEditor_AKOR = findViewById(R.id.btnEditor_AKOR);
		btnEditor_AKOR.setOnClickListener(this);

		btnEditor_TranspozeArti = findViewById(R.id.btnEditor_TranspozeArti);
		btnEditor_TranspozeArti.setOnClickListener(this);

		btnEditor_TranspozeEksi = findViewById(R.id.btnEditor_TranspozeEksi);
		btnEditor_TranspozeEksi.setOnClickListener(this);

		Bundle mBundle = getIntent().getExtras();
		Islem = mBundle.getString("Islem");
		SecilenSanatciAdi = mBundle.getString("SecilenSanatciAdi");
		SecilenSarkiAdi = mBundle.getString("SecilenSarkiAdi");
		SecilenListeID = mBundle.getInt("SecilenListeID");
		SecilenKategoriID = mBundle.getInt("SecilenKategoriID");
		SecilenTarzID = mBundle.getInt("SecilenTarzID");
		KatkidaBulunuluyorMu = mBundle.getBoolean("KatkidaBulunuluyorMu");
		SecilenSarkiIcerik = mBundle.getString("SecilenSarkiIcerik");
		SecilenSarkiIcerikTemp = SecilenSarkiIcerik;

		switch (Islem) {
			case "SarkiEkle":
				KTEditor.setVisibility(View.GONE);
				WVSarkiIcerik.setVisibility(View.VISIBLE);
				btnEditor_AKOR.setVisibility(View.GONE);
				btnEditor_TranspozeArti.setVisibility(View.VISIBLE);
				btnEditor_TranspozeEksi.setVisibility(View.VISIBLE);

				WVSarkiIcerik.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				break;
			case "YeniSarkiEkle":
				KTEditor.setVisibility(View.VISIBLE);
				WVSarkiIcerik.setVisibility(View.GONE);
				btnEditor_AKOR.setVisibility(View.VISIBLE);
				btnEditor_TranspozeArti.setVisibility(View.GONE);
				btnEditor_TranspozeEksi.setVisibility(View.GONE);
				break;
		}
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
			case R.id.btnGeri:
				onBackPressed();
				break;
            case R.id.btnKaydet:
				/*IcerikHTML = KTEditor.toHtml();
				IcerikHTML = IcerikHTML.replace(" ", "&nbsp;");
				IcerikHTML = IcerikHTML.replace("<b>", "").replace("</b>", "");
				IcerikHTML = IcerikHTML.replace("<i>", "").replace("</i>", "");
				IcerikHTML = IcerikHTML.replace("<u>", "").replace("</u>", "");
				IcerikHTML = IcerikHTML.replace("&#160;", "&nbsp;");
				IcerikHTML = IcerikHTML.replace("&#305;", "ı");
				IcerikHTML = IcerikHTML.replace("&#287;", "ğ");
				IcerikHTML = IcerikHTML.replace("&#252;", "ü");
				IcerikHTML = IcerikHTML.replace("&#351;", "ş");
				IcerikHTML = IcerikHTML.replace("&#246;", "ö");
				IcerikHTML = IcerikHTML.replace("&#231;", "ç");
				IcerikHTML = IcerikHTML.replace("&#304;", "İ");
				IcerikHTML = IcerikHTML.replace("&#286;", "Ğ");
				IcerikHTML = IcerikHTML.replace("&#220;", "Ü");
				IcerikHTML = IcerikHTML.replace("&#350;", "Ş");
				IcerikHTML = IcerikHTML.replace("&#214;", "Ö");
				IcerikHTML = IcerikHTML.replace("&#199;", "Ç");

            	AkorDefterimSys.ToastMsj(activity, IcerikHTML, Toast.LENGTH_SHORT);*/

				if (veritabani.SarkiEkle(SecilenListeID, SecilenKategoriID, SecilenTarzID, (Islem.equals("SarkiEkle") ? 1:0), SecilenSarkiAdi, SecilenSanatciAdi, SecilenSarkiIcerikTemp)) {
					sharedPrefEditor = sharedPref.edit();
					sharedPrefEditor.putString("prefAction", "Yeni şarkı eklendi");
					sharedPrefEditor.putString("prefEklenenSanatciAdiSarkiAdi", SecilenSanatciAdi + " - " + SecilenSarkiAdi);
					sharedPrefEditor.apply();

					onBackPressed();
				} else AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);

                break;
			case R.id.btnEditor_AKOR:
				//AkorDefterimSys.ToastMsj(activity, KTEditor.getSelectionStart() + "/" + KTEditor.getSelectionEnd(), Toast.LENGTH_SHORT);

				if(KTEditor.getSelectionStart() != KTEditor.getSelectionEnd()) {
					IcerikHTML = KTEditor.toHtml();
					IcerikHTML = IcerikHTML.replace(" ", "&nbsp;");
					IcerikHTML = IcerikHTML.replace("&#160;", "&nbsp;");

					Icerik = IcerikHTML.replace("<b>", "").replace("</b>", "");
					Icerik = Icerik.replace("<i>", "").replace("</i>", "");
					Icerik = Icerik.replace("<u>", "").replace("</u>", "");
					Icerik = Icerik.replace("<br>", " ");
					Icerik = Icerik.replace("&nbsp;", " ");
					Icerik = Icerik.replace("&#160;", " ");
					Icerik = Icerik.replace("&amp;", "&");
					Icerik = Icerik.replace("&lt;", "<");
					Icerik = Icerik.replace("&gt;", ">");
					Icerik = Icerik.replace("&gt;", ">");
					Icerik = Icerik.replace("&quot;", "\"");
					Icerik = Icerik.replace("&#39;", "'");
					Icerik = Icerik.replace("&#305;", "ı");
					Icerik = Icerik.replace("&#287;", "ğ");
					Icerik = Icerik.replace("&#252;", "ü");
					Icerik = Icerik.replace("&#351;", "ş");
					Icerik = Icerik.replace("&#246;", "ö");
					Icerik = Icerik.replace("&#231;", "ç");
					Icerik = Icerik.replace("&#304;", "İ");
					Icerik = Icerik.replace("&#286;", "Ğ");
					Icerik = Icerik.replace("&#220;", "Ü");
					Icerik = Icerik.replace("&#350;", "Ş");
					Icerik = Icerik.replace("&#214;", "Ö");
					Icerik = Icerik.replace("&#199;", "Ç");

					String SecilenString = Icerik.substring(KTEditor.getSelectionStart(), KTEditor.getSelectionEnd());
					int HtmlSelectionStart = 0;
					int HtmlSelectionEnd = 0;

					if(AkorDefterimSys.SecilenStringAkorMu(SecilenString)) {
						for(int i = 0; i < KTEditor.getSelectionStart(); i++) {
							switch (Icerik.substring(0, 1)) {
								case " ":
									if(IcerikHTML.startsWith("&nbsp;")) {
										IcerikHTML = IcerikHTML.substring(6, IcerikHTML.length());
										HtmlSelectionStart += 6;
									} else if(IcerikHTML.startsWith("<br>")) {
										IcerikHTML = IcerikHTML.substring(4, IcerikHTML.length());
										HtmlSelectionStart += 4;
									}

									Icerik = Icerik.substring(1, Icerik.length());

									break;
								default:
									IcerikHTML = IcerikHTML.substring(1, IcerikHTML.length());
									Icerik = Icerik.substring(1, Icerik.length());
									HtmlSelectionStart += 1;
									break;
							}
						}

						HtmlSelectionEnd = HtmlSelectionStart + (KTEditor.getSelectionEnd() - KTEditor.getSelectionStart());

						IcerikHTML = KTEditor.toHtml();
						IcerikHTML = IcerikHTML.replace(" ", "&nbsp;");
						IcerikHTML = IcerikHTML.replace("&#160;", "&nbsp;");

						if(HtmlSelectionStart == 0 && !IcerikHTML.substring(HtmlSelectionEnd, HtmlSelectionEnd + 1).equals("]")) { // Seçilen Örn: Em ise taglar siliniyor..
							KTEditor.fromHtml("[" + IcerikHTML.substring(HtmlSelectionStart, HtmlSelectionEnd) + "]" + IcerikHTML.substring(HtmlSelectionEnd, IcerikHTML.length()));
						} else if(HtmlSelectionStart != 0 && !IcerikHTML.substring(HtmlSelectionEnd, HtmlSelectionEnd + 1).equals("]")) { // Seçilen Örn: Em ise taglar ekleniyor..
							KTEditor.fromHtml(IcerikHTML.substring(0, HtmlSelectionStart) + "[" + IcerikHTML.substring(HtmlSelectionStart, HtmlSelectionEnd) + "]" + IcerikHTML.substring(HtmlSelectionEnd, IcerikHTML.length()));
						} else if(IcerikHTML.substring(HtmlSelectionStart - 1, HtmlSelectionStart).equals("[") && IcerikHTML.substring(HtmlSelectionEnd, HtmlSelectionEnd + 1).equals("]")) { // Seçilen Örn: Em ise taglar siliniyor..
							KTEditor.fromHtml(IcerikHTML.substring(0, HtmlSelectionStart - 1) + IcerikHTML.substring(HtmlSelectionStart, HtmlSelectionEnd) + IcerikHTML.substring(HtmlSelectionEnd + 1, IcerikHTML.length()));
						}

						/* else if(IcerikHTML.substring(HtmlSelectionStart, HtmlSelectionStart + 1).equals("[") && IcerikHTML.substring(HtmlSelectionEnd, HtmlSelectionEnd - 1).equals("]")) { // Seçilen Örn: [Em]
							KTEditor.fromHtml(IcerikHTML.substring(0, HtmlSelectionStart) + );
						}*/

						IcerikHTML = KTEditor.toHtml();
						IcerikHTML = IcerikHTML.replace(" ", "&nbsp;");
						IcerikHTML = IcerikHTML.replace("&#160;", "&nbsp;");

						AkorDefterimSys.ToastMsj(activity, IcerikHTML, Toast.LENGTH_SHORT);
					} else AkorDefterimSys.ToastMsj(activity, "Akor seçmelisin!", Toast.LENGTH_SHORT);
				} else {

				}

				break;
			case R.id.btnEditor_TranspozeArti:
				if(Islem.equals("SarkiEkle")) {
					SecilenSarkiIcerikTemp = AkorDefterimSys.Transpoze("+1", SecilenSarkiIcerikTemp).toString();

                    WVSarkiIcerik.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				}

				break;
			case R.id.btnEditor_TranspozeEksi:
				if(Islem.equals("SarkiEkle")) {
					SecilenSarkiIcerikTemp = AkorDefterimSys.Transpoze("-1", SecilenSarkiIcerikTemp).toString();

					WVSarkiIcerik.loadDataWithBaseURL(null, HTMLBaslangic + SecilenSarkiIcerikTemp + HTMLBitis, "text/html", "utf-8", null);
				}

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
                case "HesapBilgiGetir":
                    if(JSONSonuc.getBoolean("Sonuc")) {

                    } else {
                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.hesap_durumu),
                                    getString(R.string.hesap_bilgileri_bulunamadi),
                                    getString(R.string.tamam),
                                    "ADDialog_Kapat_CikisYap");
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
                case "PDIslem_Timeout":
                    onBackPressed();
                    break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}