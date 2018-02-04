package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpKategorilerLST;
import com.cnbcyln.app.akordefterim.FastScrollListview.FastScroller_Listview;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfKategoriler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Kategori_Yonetimi extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	SharedPreferences sharedPref;
	Typeface YaziFontu;
	AlertDialog ADDialog;
    View ViewDialogContent;
    LayoutInflater inflater;
    InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
    RelativeLayout RelativeLayout1;
    RelativeLayout RLKategoriYonetimi_AnaPanel, RLKategoriYonetimi_AramaPanel;
	ImageButton btnGeri_AnaPanel, btnAra_AnaPanel, btnGeri_AramaPanel;
	FastScroller_Listview lstKategoriYonetimi;
	TextView lblBaslik_AnaPanel, lblOrtaMesaj;
    EditText txtAra_AramaPanel;
    FloatingActionButton FABKategoriEkle;

    private List<SnfKategoriler> snfKategoriler;
    private List<SnfKategoriler> snfKategorilerTemp;

    int SecilenKategoriID = 0;
    String SecilenKategoriAdi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kategori_yonetimi);

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

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        RelativeLayout1 = findViewById(R.id.RelativeLayout1);
        RelativeLayout1.setOnClickListener(this);

        RLKategoriYonetimi_AnaPanel = findViewById(R.id.RLKategoriYonetimi_AnaPanel);
        RLKategoriYonetimi_AnaPanel.setVisibility(View.VISIBLE);

        btnGeri_AnaPanel = findViewById(R.id.btnGeri_AnaPanel);
        btnGeri_AnaPanel.setOnClickListener(this);

		lblBaslik_AnaPanel = findViewById(R.id.lblBaslik_AnaPanel);
		lblBaslik_AnaPanel.setTypeface(YaziFontu, Typeface.BOLD);

        btnAra_AnaPanel = findViewById(R.id.btnAra_AnaPanel);
        btnAra_AnaPanel.setOnClickListener(this);

        RLKategoriYonetimi_AramaPanel = findViewById(R.id.RLKategoriYonetimi_AramaPanel);
        RLKategoriYonetimi_AramaPanel.setVisibility(View.GONE);

        btnGeri_AramaPanel = findViewById(R.id.btnGeri_AramaPanel);
        btnGeri_AramaPanel.setOnClickListener(this);

        txtAra_AramaPanel = findViewById(R.id.txtAra_AramaPanel);
        txtAra_AramaPanel.setTypeface(YaziFontu, Typeface.NORMAL);
        txtAra_AramaPanel.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (snfKategoriler != null) {
                    snfKategorilerTemp = new ArrayList<>();
                    int textlength = txtAra_AramaPanel.getText().length();
                    snfKategorilerTemp.clear();

                    for (int i = 0; i < snfKategoriler.size(); i++) {
                        if (textlength <= snfKategoriler.get(i).getKategoriAdi().length()) {
                            if (snfKategoriler.get(i).getKategoriAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfKategoriler KategorilerTemp = new SnfKategoriler();
                                KategorilerTemp.setId(snfKategoriler.get(i).getId());
                                KategorilerTemp.setKategoriAdi(snfKategoriler.get(i).getKategoriAdi());
                                snfKategorilerTemp.add(KategorilerTemp);
                            }
                        }
                    }

                    if(snfKategorilerTemp.size() <= 0) {
                        lblOrtaMesaj.setVisibility(View.VISIBLE);
                        lblOrtaMesaj.setText(getString(R.string.liste_bos));
                        lstKategoriYonetimi.setVisibility(View.GONE);
                    } else {
                        lblOrtaMesaj.setVisibility(View.GONE);
                        lstKategoriYonetimi.setVisibility(View.VISIBLE);

                        // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                        AdpKategorilerLST adpKategorilerLST = new AdpKategorilerLST(activity, snfKategorilerTemp, snfKategorilerTemp.size() > 25);

                        lstKategoriYonetimi.setFastScrollEnabled(snfKategorilerTemp.size() > 25);
                        lstKategoriYonetimi.setAdapter(adpKategorilerLST);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lstKategoriYonetimi = findViewById(R.id.lstKategoriYonetimi);
        lstKategoriYonetimi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                // Eğer kategori arama alanına yazı GİRİLMEMİŞSE "snfKategoriler" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenKategoriID = snfKategoriler.get(position).getId();
                    SecilenKategoriAdi = snfKategoriler.get(position).getKategoriAdi();
                } else { // Eğer kategori arama alanına yazı GİRİLMİŞSE "snfKategorilerTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenKategoriID = snfKategorilerTemp.get(position).getId();
                    SecilenKategoriAdi = snfKategorilerTemp.get(position).getKategoriAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                AramaPanelKapat();
                openContextMenu(lstKategoriYonetimi);
            }
        });
        registerForContextMenu(lstKategoriYonetimi);

        lblOrtaMesaj = findViewById(R.id.lblOrtaMesaj);
        lblOrtaMesaj.setTypeface(YaziFontu, Typeface.BOLD);

        FABKategoriEkle = (FloatingActionButton) activity.findViewById(R.id.FABKategoriEkle);
        FABKategoriEkle.setImageResource(R.drawable.ic_plus_beyaz);
        FABKategoriEkle.setOnClickListener(this);
	}

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;

        KategoriGetir();
    }

    @Override
    public void onBackPressed() {
        if (RLKategoriYonetimi_AramaPanel.getVisibility() == View.VISIBLE) AramaPanelKapat();
        else {
            AkorDefterimSys.KlavyeKapat();
            AkorDefterimSys.DismissAlertDialog(ADDialog);

            super.onBackPressed();
        }
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
            case R.id.coordinatorLayout:
                AkorDefterimSys.UnFocusAll(null,RelativeLayout1,null,null,null);
                break;
            case R.id.ConstraintLayout1:
                AkorDefterimSys.UnFocusAll(null,RelativeLayout1,null,null,null);
                break;
			case R.id.btnGeri_AnaPanel:
				onBackPressed();
				break;
            case R.id.btnAra_AnaPanel:
                AramaPanelAc();
                break;
            case R.id.btnGeri_AramaPanel:
                AramaPanelKapat();
                break;
            case R.id.FABKategoriEkle:
                AramaPanelKapat();

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

                    EditText txtDialogInput = ViewDialogContent.findViewById(R.id.txtDialogInput);
                    txtDialogInput.setSelection(txtDialogInput.length());
                    imm.showSoftInput(txtDialogInput, 0);

                    //ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    ADDialog.show();
                }
                break;
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        switch (v.getId()) {
            case R.id.lstKategoriYonetimi:
                menu.setHeaderTitle(SecilenKategoriAdi);
                menu.add(0, 0, 0, getString(R.string.sil));
                menu.add(0, 1, 0, getString(R.string.duzenle));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getGroupId()) {
            case 0: // ListView
                switch (item.getItemId()) {
                    case 0:
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
                    case 1:
                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ViewDialogContent = inflater.inflate(R.layout.dialog_text_input, null);
                            ADDialog = AkorDefterimSys.H2Button_TextInput_CustomAlertDialog(activity,
                                    getString(R.string.duzenle),
                                    SecilenKategoriAdi,
                                    getString(R.string.kategori_adi),
                                    ViewDialogContent,
                                    getString(R.string.duzenle), "ADDialog_KategoriDuzenle",
                                    getString(R.string.iptal), "ADDialog_Kapat");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                        break;
                    default:
                        return false;
                }

                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
                case "ADDialog_KategoriEkle":
                    if(!veritabani.KategoriVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
                        if(veritabani.KategoriEkle(JSONSonuc.getString("InputIcerik"))) {
                            AramaPanelKapat();
                            KategoriGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_eklendi, JSONSonuc.getString("InputIcerik")));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else {
                        ADDialog.dismiss();
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_eklenemedi_hata1, JSONSonuc.getString("InputIcerik")));
                    }
                    break;
                case "ADDialog_KategoriDuzenle":
                    if(!veritabani.KategoriVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
                        if(veritabani.KategoriDuzenle(SecilenKategoriID, JSONSonuc.getString("InputIcerik"))) {
                            AramaPanelKapat();
                            KategoriGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_duzenlendi, JSONSonuc.getString("InputEskiIcerik"), JSONSonuc.getString("InputIcerik")));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else ADDialog.dismiss();
                    break;
                case "ADDialog_KategoriSil":
                    if(!veritabani.KategoriyeAitSarkiVarmiKontrol(SecilenKategoriID)) {
                        if(veritabani.KategoriSil(SecilenKategoriID)) {
                            AramaPanelKapat();
                            KategoriGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_silindi, SecilenKategoriAdi));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else {
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.kategori_silinemedi_hata1, SecilenKategoriAdi));
                        ADDialog.dismiss();
                    }
                    break;
                case "ADDialog_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    onBackPressed();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void KategoriGetir() {
        if(snfKategoriler != null) snfKategoriler.clear();
        else snfKategoriler = new ArrayList<>();

        snfKategoriler = veritabani.SnfKategoriGetir(false);

        if(snfKategoriler.get(0).getKategoriAdi().equals("")) snfKategoriler.clear();

        if(snfKategoriler.size() <= 0) {
            lblOrtaMesaj.setVisibility(View.VISIBLE);
            lblOrtaMesaj.setText(getString(R.string.liste_bos));
            lstKategoriYonetimi.setVisibility(View.GONE);
        } else {
            lblOrtaMesaj.setVisibility(View.GONE);
            lstKategoriYonetimi.setVisibility(View.VISIBLE);

            // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
            AdpKategorilerLST adpKategorilerLST = new AdpKategorilerLST(activity, snfKategoriler, snfKategoriler.size() > 25);

            lstKategoriYonetimi.setFastScrollEnabled(snfKategoriler.size() > 25);
            lstKategoriYonetimi.setAdapter(adpKategorilerLST);
        }
    }

    private void AramaPanelAc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AkorDefterimSys.circleReveal(R.id.RLKategoriYonetimi_AramaPanel,1, true, true);
        }

        txtAra_AramaPanel.setText("");
        txtAra_AramaPanel.requestFocus();
        imm.showSoftInput(txtAra_AramaPanel, 0);
    }

    private void AramaPanelKapat() {
        txtAra_AramaPanel.setText("");
        AkorDefterimSys.KlavyeKapat();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AkorDefterimSys.circleReveal(R.id.RLKategoriYonetimi_AramaPanel,1, true, false);
        } else {
            RLKategoriYonetimi_AnaPanel.setVisibility(View.VISIBLE);
            RLKategoriYonetimi_AramaPanel.setVisibility(View.GONE);
        }
    }
}