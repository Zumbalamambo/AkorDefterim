package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpTarzlarLST;
import com.cnbcyln.app.akordefterim.FastScrollListview.FastScroller_Listview;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfTarzlar;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Tarz_Yonetimi extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

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
    RelativeLayout RLTarzYonetimi_AnaPanel, RLTarzYonetimi_AramaPanel;
	ImageButton btnGeri_AnaPanel, btnAra_AnaPanel, btnGeri_AramaPanel;
	FastScroller_Listview lstTarzYonetimi;
	TextView lblBaslik_AnaPanel, lblOrtaMesaj;
    EditText txtAra_AramaPanel;
    FloatingActionButton FABTarzEkle;

    private List<SnfTarzlar> snfTarzlar;
    private List<SnfTarzlar> snfTarzlarTemp;

    int SecilenTarzID = 0;
    String SecilenTarzAdi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tarz_yonetimi);

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

        RLTarzYonetimi_AnaPanel = findViewById(R.id.RLTarzYonetimi_AnaPanel);
        RLTarzYonetimi_AnaPanel.setVisibility(View.VISIBLE);

        btnGeri_AnaPanel = findViewById(R.id.btnGeri_AnaPanel);
        btnGeri_AnaPanel.setOnClickListener(this);

		lblBaslik_AnaPanel = findViewById(R.id.lblBaslik_AnaPanel);
		lblBaslik_AnaPanel.setTypeface(YaziFontu, Typeface.BOLD);

        btnAra_AnaPanel = findViewById(R.id.btnAra_AnaPanel);
        btnAra_AnaPanel.setOnClickListener(this);

        RLTarzYonetimi_AramaPanel = findViewById(R.id.RLTarzYonetimi_AramaPanel);
        RLTarzYonetimi_AramaPanel.setVisibility(View.GONE);

        btnGeri_AramaPanel = findViewById(R.id.btnGeri_AramaPanel);
        btnGeri_AramaPanel.setOnClickListener(this);

        txtAra_AramaPanel = findViewById(R.id.txtAra_AramaPanel);
        txtAra_AramaPanel.setTypeface(YaziFontu, Typeface.NORMAL);
        txtAra_AramaPanel.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (snfTarzlar != null) {
                    snfTarzlarTemp = new ArrayList<>();
                    int textlength = txtAra_AramaPanel.getText().length();
                    snfTarzlarTemp.clear();

                    for (int i = 0; i < snfTarzlar.size(); i++) {
                        if (textlength <= snfTarzlar.get(i).getTarzAdi().length()) {
                            if (snfTarzlar.get(i).getTarzAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfTarzlar TarzlarTemp = new SnfTarzlar();
                                TarzlarTemp.setId(snfTarzlar.get(i).getId());
                                TarzlarTemp.setTarzAdi(snfTarzlar.get(i).getTarzAdi());
                                snfTarzlarTemp.add(TarzlarTemp);
                            }
                        }
                    }

                    if(snfTarzlarTemp.size() <= 0) {
                        lblOrtaMesaj.setVisibility(View.VISIBLE);
                        lblOrtaMesaj.setText(getString(R.string.liste_bos));
                        lstTarzYonetimi.setVisibility(View.GONE);
                    } else {
                        lblOrtaMesaj.setVisibility(View.GONE);
                        lstTarzYonetimi.setVisibility(View.VISIBLE);

                        // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                        AdpTarzlarLST adpTarzlarLST = new AdpTarzlarLST(activity, snfTarzlarTemp, snfTarzlarTemp.size() > 25);

                        lstTarzYonetimi.setFastScrollEnabled(snfTarzlarTemp.size() > 25);
                        lstTarzYonetimi.setAdapter(adpTarzlarLST);
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

        lstTarzYonetimi = findViewById(R.id.lstTarzYonetimi);
        lstTarzYonetimi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                // Eğer tarz arama alanına yazı GİRİLMEMİŞSE "snfTarzlar" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenTarzID = snfTarzlar.get(position).getId();
                    SecilenTarzAdi = snfTarzlar.get(position).getTarzAdi();
                } else { // Eğer tarz arama alanına yazı GİRİLMİŞSE "snfTarzlarTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenTarzID = snfTarzlarTemp.get(position).getId();
                    SecilenTarzAdi = snfTarzlarTemp.get(position).getTarzAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                openContextMenu(lstTarzYonetimi);
            }
        });
        lstTarzYonetimi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Eğer tarz arama alanına yazı GİRİLMEMİŞSE "snfTarzlar" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenTarzID = snfTarzlar.get(position).getId();
                    SecilenTarzAdi = snfTarzlar.get(position).getTarzAdi();
                } else { // Eğer tarz arama alanına yazı GİRİLMİŞSE "snfTarzlarTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenTarzID = snfTarzlarTemp.get(position).getId();
                    SecilenTarzAdi = snfTarzlarTemp.get(position).getTarzAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                openContextMenu(lstTarzYonetimi);

                return true;
            }
        });
        registerForContextMenu(lstTarzYonetimi);

        lblOrtaMesaj = findViewById(R.id.lblOrtaMesaj);
        lblOrtaMesaj.setTypeface(YaziFontu, Typeface.NORMAL);

        FABTarzEkle = (FloatingActionButton) activity.findViewById(R.id.FABTarzEkle);
        FABTarzEkle.setImageResource(R.drawable.ic_plus_beyaz);
        FABTarzEkle.setOnClickListener(this);
	}

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;
        AkorDefterimSys.SharePrefAyarlarınıUygula();

        TarzGetir();
    }

    @Override
    public void onBackPressed() {
        if (RLTarzYonetimi_AramaPanel.getVisibility() == View.VISIBLE) AkorDefterimSys.AramaPanelKapat(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
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
				AkorDefterimSys.EkranKapat();
				break;
            case R.id.btnAra_AnaPanel:
                AkorDefterimSys.AramaPanelAc(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                break;
            case R.id.btnGeri_AramaPanel:
                AkorDefterimSys.AramaPanelKapat(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                break;
            case R.id.FABTarzEkle:
                AkorDefterimSys.AramaPanelKapat(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);

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
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        switch (v.getId()) {
            case R.id.lstTarzYonetimi:
                menu.setHeaderTitle(SecilenTarzAdi);
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
                                    getString(R.string.tarz_sil_soru, SecilenTarzAdi),
                                    getString(R.string.evet),"ADDialog_TarzSil",
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
                                    SecilenTarzAdi,
                                    getString(R.string.tarz_adi),
                                    ViewDialogContent,
                                    getString(R.string.duzenle), "ADDialog_TarzDuzenle",
                                    getString(R.string.iptal), "ADDialog_Kapat");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();

                            ADDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
                case "ADDialog_TarzEkle":
                    if(!veritabani.TarzVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
                        if(veritabani.TarzEkle(JSONSonuc.getString("InputIcerik"))) {
                            AkorDefterimSys.AramaPanelKapat(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                            TarzGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_eklendi, JSONSonuc.getString("InputIcerik")));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else {
                        ADDialog.dismiss();
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_eklenemedi_hata1, JSONSonuc.getString("InputIcerik")));
                    }
                    break;
                case "ADDialog_TarzDuzenle":
                    if(!veritabani.TarzVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
                        if(veritabani.TarzDuzenle(SecilenTarzID, JSONSonuc.getString("InputIcerik"))) {
                            AkorDefterimSys.AramaPanelKapat(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                            TarzGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_duzenlendi, JSONSonuc.getString("InputEskiIcerik"), JSONSonuc.getString("InputIcerik")));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else ADDialog.dismiss();
                    break;
                case "ADDialog_TarzSil":
                    if(!veritabani.TarzaAitSarkiVarmiKontrol(SecilenTarzID)) {
                        if(veritabani.TarzSil(SecilenTarzID)) {
                            AkorDefterimSys.AramaPanelKapat(RLTarzYonetimi_AnaPanel.getId(),RLTarzYonetimi_AramaPanel.getId(),txtAra_AramaPanel, imm);
                            TarzGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_silindi, SecilenTarzAdi));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else {
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.tarz_silinemedi_hata1, SecilenTarzAdi));
                        ADDialog.dismiss();
                    }
                    break;
                case "ADDialog_Kapat":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
                case "ADDialog_Kapat_GeriGit":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    AkorDefterimSys.EkranKapat();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void TarzGetir() {
        if(snfTarzlar != null) snfTarzlar.clear();
        else snfTarzlar = new ArrayList<>();

        snfTarzlar = veritabani.SnfTarzGetir(false);

        if(snfTarzlar.get(0).getTarzAdi().equals("")) snfTarzlar.clear();

        if(snfTarzlar.size() <= 0) {
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
            btnAra_AnaPanel.setEnabled(false);
            lblOrtaMesaj.setVisibility(View.VISIBLE);
            lblOrtaMesaj.setText(getString(R.string.liste_bos));
            lstTarzYonetimi.setVisibility(View.GONE);
        } else {
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
            btnAra_AnaPanel.setEnabled(true);
            lblOrtaMesaj.setVisibility(View.GONE);
            lstTarzYonetimi.setVisibility(View.VISIBLE);

            // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
            AdpTarzlarLST adpTarzlarLST = new AdpTarzlarLST(activity, snfTarzlar, snfTarzlar.size() > 25);

            lstTarzYonetimi.setFastScrollEnabled(snfTarzlar.size() > 25);
            lstTarzYonetimi.setAdapter(adpTarzlarLST);
        }
    }
}