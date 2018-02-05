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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Adaptorler.AdpListelerLST;
import com.cnbcyln.app.akordefterim.FastScrollListview.FastScroller_Listview;
import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.Siniflar.SnfListeler;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Liste_Yonetimi extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

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
    RelativeLayout RLListeYonetimi_AnaPanel, RLListeYonetimi_AramaPanel;
	ImageButton btnGeri_AnaPanel, btnAra_AnaPanel, btnGeri_AramaPanel;
	FastScroller_Listview lstListeYonetimi;
	TextView lblBaslik_AnaPanel, lblOrtaMesaj;
    EditText txtAra_AramaPanel;
    FloatingActionButton FABListeEkle;

    private List<SnfListeler> snfListeler;
    private List<SnfListeler> snfListelerTemp;

    int SecilenListeID = 0;
    String SecilenListeAdi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_yonetimi);

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

        RLListeYonetimi_AnaPanel = findViewById(R.id.RLListeYonetimi_AnaPanel);
        RLListeYonetimi_AnaPanel.setVisibility(View.VISIBLE);

        btnGeri_AnaPanel = findViewById(R.id.btnGeri_AnaPanel);
        btnGeri_AnaPanel.setOnClickListener(this);

		lblBaslik_AnaPanel = findViewById(R.id.lblBaslik_AnaPanel);
		lblBaslik_AnaPanel.setTypeface(YaziFontu, Typeface.BOLD);

        btnAra_AnaPanel = findViewById(R.id.btnAra_AnaPanel);
        btnAra_AnaPanel.setOnClickListener(this);

        RLListeYonetimi_AramaPanel = findViewById(R.id.RLListeYonetimi_AramaPanel);
        RLListeYonetimi_AramaPanel.setVisibility(View.GONE);

        btnGeri_AramaPanel = findViewById(R.id.btnGeri_AramaPanel);
        btnGeri_AramaPanel.setOnClickListener(this);

        txtAra_AramaPanel = findViewById(R.id.txtAra_AramaPanel);
        txtAra_AramaPanel.setTypeface(YaziFontu, Typeface.NORMAL);
        txtAra_AramaPanel.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (snfListeler != null) {
                    snfListelerTemp = new ArrayList<>();
                    int textlength = txtAra_AramaPanel.getText().length();
                    snfListelerTemp.clear();

                    for (int i = 0; i < snfListeler.size(); i++) {
                        if (textlength <= snfListeler.get(i).getListeAdi().length()) {
                            if (snfListeler.get(i).getListeAdi().toLowerCase().contains(txtAra_AramaPanel.getText().toString().toLowerCase())) {
                                SnfListeler ListelerTemp = new SnfListeler();
                                ListelerTemp.setId(snfListeler.get(i).getId());
                                ListelerTemp.setListeAdi(snfListeler.get(i).getListeAdi());
                                snfListelerTemp.add(ListelerTemp);
                            }
                        }
                    }

                    if(snfListelerTemp.size() <= 0) {
                        lblOrtaMesaj.setVisibility(View.VISIBLE);
                        lblOrtaMesaj.setText(getString(R.string.liste_bos));
                        lstListeYonetimi.setVisibility(View.GONE);
                    } else {
                        lblOrtaMesaj.setVisibility(View.GONE);
                        lstListeYonetimi.setVisibility(View.VISIBLE);

                        // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
                        AdpListelerLST adpListelerLST = new AdpListelerLST(activity, snfListelerTemp, snfListelerTemp.size() > 25);

                        lstListeYonetimi.setFastScrollEnabled(snfListelerTemp.size() > 25);
                        lstListeYonetimi.setAdapter(adpListelerLST);
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

        lstListeYonetimi = findViewById(R.id.lstListeYonetimi);
        lstListeYonetimi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                // Eğer liste arama alanına yazı GİRİLMEMİŞSE "snfListeler" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenListeID = snfListeler.get(position).getId();
                    SecilenListeAdi = snfListeler.get(position).getListeAdi();
                } else { // Eğer liste arama alanına yazı GİRİLMİŞSE "snfListelerTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenListeID = snfListelerTemp.get(position).getId();
                    SecilenListeAdi = snfListelerTemp.get(position).getListeAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                AramaPanelKapat();
                openContextMenu(lstListeYonetimi);
            }
        });
        lstListeYonetimi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Eğer liste arama alanına yazı GİRİLMEMİŞSE "snfListeler" sıfından kayıt baz alarak içerik getirtiyoruz..
                if (txtAra_AramaPanel.getText().length() == 0) {
                    SecilenListeID = snfListeler.get(position).getId();
                    SecilenListeAdi = snfListeler.get(position).getListeAdi();
                } else { // Eğer liste arama alanına yazı GİRİLMİŞSE "snfListelerTemp" sıfından kayıt baz alarak içerik getirtiyoruz..
                    SecilenListeID = snfListelerTemp.get(position).getId();
                    SecilenListeAdi = snfListelerTemp.get(position).getListeAdi();
                }

                AkorDefterimSys.KlavyeKapat();
                AramaPanelKapat();
                openContextMenu(lstListeYonetimi);

                return true;
            }
        });
        registerForContextMenu(lstListeYonetimi);

        lblOrtaMesaj = findViewById(R.id.lblOrtaMesaj);
        lblOrtaMesaj.setTypeface(YaziFontu, Typeface.NORMAL);

        FABListeEkle = (FloatingActionButton) activity.findViewById(R.id.FABListeEkle);
        FABListeEkle.setImageResource(R.drawable.ic_plus_beyaz);
        FABListeEkle.setOnClickListener(this);
	}

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;

        ListeGetir();
    }

    @Override
    public void onBackPressed() {
        if (RLListeYonetimi_AramaPanel.getVisibility() == View.VISIBLE) AramaPanelKapat();
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
            case R.id.FABListeEkle:
                AramaPanelKapat();

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
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        switch (v.getId()) {
            case R.id.lstListeYonetimi:
                menu.setHeaderTitle(SecilenListeAdi);
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
                                    getString(R.string.liste_sil_soru, SecilenListeAdi),
                                    getString(R.string.evet),"ADDialog_ListeSil",
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
                                    SecilenListeAdi,
                                    getString(R.string.liste_adi),
                                    ViewDialogContent,
                                    getString(R.string.duzenle), "ADDialog_ListeDuzenle",
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
                case "ADDialog_ListeEkle":
                    if(!veritabani.ListeVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
                        if(veritabani.ListeEkle(JSONSonuc.getString("InputIcerik"))) {
                            AramaPanelKapat();
                            ListeGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_eklendi, JSONSonuc.getString("InputIcerik")));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else {
                        ADDialog.dismiss();
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_eklenemedi_hata1, JSONSonuc.getString("InputIcerik")));
                    }
                    break;
                case "ADDialog_ListeDuzenle":
                    if(!veritabani.ListeVarmiKontrol(JSONSonuc.getString("InputIcerik"))) {
                        if(veritabani.ListeDuzenle(SecilenListeID, JSONSonuc.getString("InputIcerik"))) {
                            AramaPanelKapat();
                            ListeGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_duzenlendi, JSONSonuc.getString("InputEskiIcerik"), JSONSonuc.getString("InputIcerik")));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else ADDialog.dismiss();
                    break;
                case "ADDialog_ListeSil":
                    if(!veritabani.ListeyeAitSarkiVarmiKontrol(SecilenListeID)) {
                        if(veritabani.ListeSil(SecilenListeID)) {
                            AramaPanelKapat();
                            ListeGetir();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_silindi, SecilenListeAdi));
                            ADDialog.dismiss();
                        } else {
                            ADDialog.dismiss();
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    } else {
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.liste_silinemedi_hata1, SecilenListeAdi));
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

    private void ListeGetir() {
        if(snfListeler != null) snfListeler.clear();
        else snfListeler = new ArrayList<>();

        snfListeler = veritabani.SnfListeGetir("Cevrimdisi", false);

        if(snfListeler.get(0).getListeAdi().equals("")) snfListeler.clear();

        if(snfListeler.size() <= 0) {
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara);
            btnAra_AnaPanel.setEnabled(false);
            lblOrtaMesaj.setVisibility(View.VISIBLE);
            lblOrtaMesaj.setText(getString(R.string.liste_bos));
            lstListeYonetimi.setVisibility(View.GONE);
        } else {
            btnAra_AnaPanel.setImageResource(R.drawable.ic_ara_siyah);
            btnAra_AnaPanel.setEnabled(true);
            lblOrtaMesaj.setVisibility(View.GONE);
            lstListeYonetimi.setVisibility(View.VISIBLE);

            // Eğer kayıt sayısı 25'ten fazla ise FastScroll'u aktif et
            AdpListelerLST adpListelerLST = new AdpListelerLST(activity, snfListeler, snfListeler.size() > 25);

            lstListeYonetimi.setFastScrollEnabled(snfListeler.size() > 25);
            lstListeYonetimi.setAdapter(adpListelerLST);
        }
    }

    private void AramaPanelAc() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AkorDefterimSys.circleReveal(R.id.RLListeYonetimi_AramaPanel,1, true, true);
        }

        txtAra_AramaPanel.setText("");
        txtAra_AramaPanel.requestFocus();
        imm.showSoftInput(txtAra_AramaPanel, 0);
    }

    private void AramaPanelKapat() {
        txtAra_AramaPanel.setText("");
        AkorDefterimSys.KlavyeKapat();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AkorDefterimSys.circleReveal(R.id.RLListeYonetimi_AramaPanel,1, true, false);
        } else {
            RLListeYonetimi_AnaPanel.setVisibility(View.VISIBLE);
            RLListeYonetimi_AramaPanel.setVisibility(View.GONE);
        }
    }
}