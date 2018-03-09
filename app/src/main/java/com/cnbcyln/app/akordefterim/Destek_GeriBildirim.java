package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.NetInfo;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("ALL")
public class Destek_GeriBildirim extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	Typeface YaziFontu;
	File SecilenEkranGoruntusuFile1, SecilenEkranGoruntusuFile2, SecilenEkranGoruntusuFile3, YuklenecekEkranGoruntusu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
    InputMethodManager imm;

	CoordinatorLayout coordinatorLayout;
    ConstraintLayout constraintLayout;
	ImageButton btnKapat, btnGonder, btnEkranGoruntusu1, btnEkranGoruntusu2, btnEkranGoruntusu3;
	TextView lblBaslik, lblGeriBildirimAciklama, lblEkranGoruntusu;
    MaterialEditText txtIcerik;
	LinearLayout LLEkranGoruntusu;
	View view2;

	String BildirimTipi;
    private static final int DOSYAOKUMAYAZMA_IZIN = 1;
    private static final int RESIMSEC = 2;
    private static final int RESIMKES_EKRANGORUNTUSU_1 = 3;
    private static final int RESIMKES_EKRANGORUNTUSU_2 = 4;
    private static final int RESIMKES_EKRANGORUNTUSU_3 = 5;
    int SecilenEkranGoruntusuNo = 0, GeriBildirimKarakterSayisi_MIN = 0, GeriBildirimKarakterSayisi_MAX = 0, YenidenGeriBildirimGondermeSuresi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destek_geribildirim);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular"); // Genel yazı fontunu belirttik

		sharedPref = getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		//AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		//AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

        GeriBildirimKarakterSayisi_MIN = getResources().getInteger(R.integer.GeriBildirimKarakterSayisi_MIN);
        GeriBildirimKarakterSayisi_MAX = getResources().getInteger(R.integer.GeriBildirimKarakterSayisi_MAX);
        YenidenGeriBildirimGondermeSuresi = getResources().getInteger(R.integer.YenidenGeriBildirimGondermeSuresi);

        Bundle mBundle = getIntent().getExtras();
        BildirimTipi = mBundle.getString("BildirimTipi");

		coordinatorLayout = findViewById(R.id.coordinatorLayout);
		coordinatorLayout.setOnClickListener(this);

        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(this);

        btnKapat = findViewById(R.id.btnKapat);
        btnKapat.setOnClickListener(this);

		lblBaslik = findViewById(R.id.lblBaslik);
		lblBaslik.setTypeface(YaziFontu, Typeface.BOLD);

        btnGonder = findViewById(R.id.btnGonder);
        btnGonder.setOnClickListener(this);

        lblGeriBildirimAciklama = findViewById(R.id.lblGeriBildirimAciklama);
        lblGeriBildirimAciklama.setTypeface(YaziFontu, Typeface.NORMAL);

        txtIcerik = findViewById(R.id.txtIcerik);
        txtIcerik.setTypeface(YaziFontu, Typeface.NORMAL);
        txtIcerik.setMinCharacters(GeriBildirimKarakterSayisi_MIN);
        txtIcerik.setMaxCharacters(GeriBildirimKarakterSayisi_MAX);
        txtIcerik.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Gonder();
                }

                return false;
            }
        });

        lblEkranGoruntusu = findViewById(R.id.lblEkranGoruntusu);
        lblEkranGoruntusu.setTypeface(YaziFontu, Typeface.BOLD);

        LLEkranGoruntusu = findViewById(R.id.LLEkranGoruntusu);

        view2 = findViewById(R.id.view2);

        btnEkranGoruntusu1 = findViewById(R.id.btnEkranGoruntusu1);
        btnEkranGoruntusu1.setOnClickListener(this);
        registerForContextMenu(btnEkranGoruntusu1);

        btnEkranGoruntusu2 = findViewById(R.id.btnEkranGoruntusu2);
        btnEkranGoruntusu2.setOnClickListener(this);
        registerForContextMenu(btnEkranGoruntusu2);

        btnEkranGoruntusu3 = findViewById(R.id.btnEkranGoruntusu3);
        btnEkranGoruntusu3.setOnClickListener(this);
        registerForContextMenu(btnEkranGoruntusu3);

        switch (BildirimTipi) {
            case "Sorun":
                lblBaslik.setText(getString(R.string.sorun_bildir));

                lblGeriBildirimAciklama.setText(getString(R.string.sorun_bildir_aciklama, getString(R.string.uygulama_adi), getString(R.string.uygulama_adi), YenidenGeriBildirimGondermeSuresi));
                AkorDefterimSys.setTextViewHTML(lblGeriBildirimAciklama);

                txtIcerik.setHint(getString(R.string.ne_oldugunu_kisaca_acikla));
                lblEkranGoruntusu.setVisibility(View.VISIBLE);
                LLEkranGoruntusu.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                btnEkranGoruntusu1.setVisibility(View.VISIBLE);
                btnEkranGoruntusu2.setVisibility(View.VISIBLE);
                btnEkranGoruntusu3.setVisibility(View.VISIBLE);
                break;
            case "Görüş":
                lblBaslik.setText(getString(R.string.gorus_bildir));

                lblGeriBildirimAciklama.setText(getString(R.string.gorus_bildir_aciklama, getString(R.string.uygulama_adi), YenidenGeriBildirimGondermeSuresi));
                AkorDefterimSys.setTextViewHTML(lblGeriBildirimAciklama);

                txtIcerik.setHint(getString(R.string.gorusleriniz_bizim_icin_onemli));
                lblEkranGoruntusu.setVisibility(View.GONE);
                LLEkranGoruntusu.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                btnEkranGoruntusu1.setVisibility(View.GONE);
                btnEkranGoruntusu2.setVisibility(View.GONE);
                btnEkranGoruntusu3.setVisibility(View.GONE);
                break;
        }
	}

    @Override
    protected void onStart() {
        super.onStart();
        AkorDefterimSys.activity = activity;
        AkorDefterimSys.SharePrefAyarlarınıUygula();
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
                AkorDefterimSys.UnFocusEditText(txtIcerik);
                break;
            case R.id.constraintLayout:
                AkorDefterimSys.UnFocusEditText(txtIcerik);
                break;
			case R.id.btnKapat:
				AkorDefterimSys.EkranKapat();
				break;
            case R.id.btnGonder:
                Gonder();
                break;
            case R.id.btnEkranGoruntusu1:
                if(AkorDefterimSys.InternetErisimKontrolu()) {
                    AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
                    SecilenEkranGoruntusuNo = 0;
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                break;
            case R.id.btnEkranGoruntusu2:
                if(AkorDefterimSys.InternetErisimKontrolu()) {
                    AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
                    SecilenEkranGoruntusuNo = 1;
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                break;
            case R.id.btnEkranGoruntusu3:
                if(AkorDefterimSys.InternetErisimKontrolu()) {
                    AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
                    SecilenEkranGoruntusuNo = 2;
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                break;
		}
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case DOSYAOKUMAYAZMA_IZIN:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    AkorDefterimSys.IntentGetir(new String[]{"GaleridenResimGetir", String.valueOf(DOSYAOKUMAYAZMA_IZIN), String.valueOf(RESIMSEC)});
                else {
                    if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                        ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                getString(R.string.uygulama_izinleri),
                                getString(R.string.uygulama_izni_dosya_yazma_hata),
                                getString(R.string.tamam), "ADDialog_Kapat");
                        ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        ADDialog.show();
                    }
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RESIMSEC:
                    switch (SecilenEkranGoruntusuNo) {
                        case 0:
                            AkorDefterimSys.IntentGetir(new String[]{"ResimKirp2", data.getData().toString(), String.valueOf(RESIMKES_EKRANGORUNTUSU_1)});
                            break;
                        case 1:
                            AkorDefterimSys.IntentGetir(new String[]{"ResimKirp2", data.getData().toString(), String.valueOf(RESIMKES_EKRANGORUNTUSU_2)});
                            break;
                        case 2:
                            AkorDefterimSys.IntentGetir(new String[]{"ResimKirp2", data.getData().toString(), String.valueOf(RESIMKES_EKRANGORUNTUSU_3)});
                            break;
                    }
                    break;
                case RESIMKES_EKRANGORUNTUSU_1:
                    SecilenEkranGoruntusuFile1 = new File(CropImage.getActivityResult(data).getUri().toString().substring(7));
                    btnEkranGoruntusu1.setImageBitmap(BitmapFactory.decodeFile(SecilenEkranGoruntusuFile1.getPath()));
                    break;
                case RESIMKES_EKRANGORUNTUSU_2:
                    SecilenEkranGoruntusuFile2 = new File(CropImage.getActivityResult(data).getUri().toString().substring(7));
                    btnEkranGoruntusu2.setImageBitmap(BitmapFactory.decodeFile(SecilenEkranGoruntusuFile2.getPath()));
                    break;
                case RESIMKES_EKRANGORUNTUSU_3:
                    SecilenEkranGoruntusuFile3 = new File(CropImage.getActivityResult(data).getUri().toString().substring(7));
                    btnEkranGoruntusu3.setImageBitmap(BitmapFactory.decodeFile(SecilenEkranGoruntusuFile3.getPath()));
                    break;
            }
        }
    }

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            switch (JSONSonuc.getString("Islem")) {
                case "IPAdresGetir":
                    txtIcerik.setText(txtIcerik.getText().toString().trim());
                    String Icerik = txtIcerik.getText().toString();

                    AkorDefterimSys.GeriBildirimEkle(String.valueOf(YenidenGeriBildirimGondermeSuresi), sharedPref.getString("prefHesapID",""), BildirimTipi, Icerik, JSONSonuc.getString("IPAdres"), "GeriBildirimEkle");
                    break;
                case "GeriBildirimEkle":
                    if(JSONSonuc.getBoolean("Sonuc")) {
                        if(SecilenEkranGoruntusuFile1 != null) {
                            YuklenecekEkranGoruntusu = SecilenEkranGoruntusuFile1;
                            SecilenEkranGoruntusuFile1 = null;
                            new ResimYukle().execute();
                        } else if(SecilenEkranGoruntusuFile2 != null) {
                            YuklenecekEkranGoruntusu = SecilenEkranGoruntusuFile2;
                            SecilenEkranGoruntusuFile2 = null;
                            new ResimYukle().execute();
                        } else if(SecilenEkranGoruntusuFile3 != null) {
                            YuklenecekEkranGoruntusu = SecilenEkranGoruntusuFile3;
                            SecilenEkranGoruntusuFile3 = null;
                            new ResimYukle().execute();
                        } else {
                            btnKapat.setEnabled(true);
                            btnGonder.setEnabled(true);
                            btnEkranGoruntusu1.setEnabled(true);
                            btnEkranGoruntusu2.setEnabled(true);
                            btnEkranGoruntusu3.setEnabled(true);
                            AkorDefterimSys.DismissProgressDialog(PDIslem);

                            if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                        getString(R.string.geri_bildirim),
                                        getString(R.string.geri_bildirim_gonderildi),
                                        activity.getString(R.string.tamam),
                                        "ADDialog_Kapat_GeriGit");
                                ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                ADDialog.show();
                            }
                        }
                    } else {
                        btnKapat.setEnabled(true);
                        btnGonder.setEnabled(true);
                        btnEkranGoruntusu1.setEnabled(true);
                        btnEkranGoruntusu2.setEnabled(true);
                        btnEkranGoruntusu3.setEnabled(true);
                        AkorDefterimSys.DismissProgressDialog(PDIslem);

                        switch (JSONSonuc.getString("Aciklama")) {
                            case "Bildirim eklenemedi! Sure dolmamış.":
                                AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.geri_bildirim_s_dk_aralikla_gonderebilirsiniz, YenidenGeriBildirimGondermeSuresi));
                                break;
                            default:
                                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                    ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                            getString(R.string.hata),
                                            getString(R.string.geri_bildirim_gonderilemedi),
                                            activity.getString(R.string.tamam),
                                            "ADDialog_Kapat");
                                    ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    ADDialog.show();
                                }
                                break;
                        }
                    }
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint({"InflateParams", "StaticFieldLeak"})
    private class ResimYukle extends AsyncTask<Void, Integer, String> {
        long totalSize = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PDIslem.setMessage(getString(R.string.geri_bildirim_ekran_goruntuleri_yukleniyor, "0%"));
        }

        @Override
        protected String doInBackground(Void... parametre) {
            return UploadFile();
        }

        private String UploadFile() {
            String Sonuc;

            try {
                Bitmap SecilenResimBitmap = BitmapFactory.decodeFile(YuklenecekEkranGoruntusu.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                SecilenResimBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AkorDefterimSys.PHPResimYukle);

                // Adding file data to http body
                entity.addPart("Dosya", new FileBody(YuklenecekEkranGoruntusu));
                entity.addPart("Dizin", new StringBody(AkorDefterimSys.PHPGeriBildirimEkranGoruntuleriDizini + sharedPref.getString("prefHesapID", "") + File.separator));
                entity.addPart("DosyaAdi", new StringBody("Img_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + "_" + AkorDefterimSys.KodUret(6, true, false, false, false)));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                //HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    Sonuc = "OK";
                } else {
                    Sonuc = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Sonuc = "FileNotFoundException";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Sonuc = "MalformedURLException";
            } catch (IOException e) {
                e.printStackTrace();
                Sonuc = "IOException";
            }

            return Sonuc;
        }

        @Override
        protected void onPostExecute(String Sonuc) {
            switch (Sonuc) {
                case "OK":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(SecilenEkranGoruntusuFile1 != null) {
                                YuklenecekEkranGoruntusu = SecilenEkranGoruntusuFile1;
                                SecilenEkranGoruntusuFile1 = null;
                                new ResimYukle().execute();
                            } else if(SecilenEkranGoruntusuFile2 != null) {
                                YuklenecekEkranGoruntusu = SecilenEkranGoruntusuFile2;
                                SecilenEkranGoruntusuFile2 = null;
                                new ResimYukle().execute();
                            } else if(SecilenEkranGoruntusuFile3 != null) {
                                YuklenecekEkranGoruntusu = SecilenEkranGoruntusuFile3;
                                SecilenEkranGoruntusuFile3 = null;
                                new ResimYukle().execute();
                            } else {
                                btnKapat.setEnabled(true);
                                btnGonder.setEnabled(true);
                                btnEkranGoruntusu1.setEnabled(true);
                                btnEkranGoruntusu2.setEnabled(true);
                                btnEkranGoruntusu3.setEnabled(true);
                                AkorDefterimSys.DismissProgressDialog(PDIslem);

                                if(SecilenEkranGoruntusuFile1 != null && SecilenEkranGoruntusuFile1.exists()) SecilenEkranGoruntusuFile1.delete();
                                if(SecilenEkranGoruntusuFile2 != null && SecilenEkranGoruntusuFile2.exists()) SecilenEkranGoruntusuFile2.delete();
                                if(SecilenEkranGoruntusuFile3 != null && SecilenEkranGoruntusuFile3.exists()) SecilenEkranGoruntusuFile3.delete();
                                if(YuklenecekEkranGoruntusu != null && YuklenecekEkranGoruntusu.exists()) YuklenecekEkranGoruntusu.delete();

                                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                    ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                            getString(R.string.geri_bildirim),
                                            getString(R.string.geri_bildirim_gonderildi),
                                            activity.getString(R.string.tamam),
                                            "ADDialog_Kapat_GeriGit");
                                    ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                    ADDialog.show();
                                }
                            }
                        }
                    });
                    break;
                case "FileNotFoundException":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    });
                    break;
                case "MalformedURLException":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.url_hatasi));
                        }
                    });
                    break;
                case "IOException":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.dosya_yazma_okuma_hatasi));
                        }
                    });
                    break;
                default:
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.beklenmedik_hata_meydana_geldi));
                        }
                    });
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(final Integer... Deger) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PDIslem.setMessage(getString(R.string.geri_bildirim_ekran_goruntuleri_yukleniyor, String.valueOf(Deger[0]) + "%"));
                }
            });
        }

        @Override
        protected void onCancelled(String Sonuc) {
            super.onCancelled(Sonuc);
            // PDIslem Progress Dialog'u kapattık
            AkorDefterimSys.DismissProgressDialog(PDIslem);
        }

        public void execute(String s) {
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void Gonder() {
        AkorDefterimSys.KlavyeKapat();

        if(AkorDefterimSys.InternetErisimKontrolu()) {
            txtIcerik.setText(txtIcerik.getText().toString().trim());
            String Icerik = txtIcerik.getText().toString();

            if (TextUtils.isEmpty(Icerik)) {
                txtIcerik.setError(getString(R.string.hata_bos_alan));
                txtIcerik.requestFocus();
                txtIcerik.setSelection(txtIcerik.length());
                imm.showSoftInput(txtIcerik, 0);
            } else if (Icerik.length() > GeriBildirimKarakterSayisi_MAX) {
                txtIcerik.setError(getString(R.string.hata_en_fazla_karakter, String.valueOf(GeriBildirimKarakterSayisi_MAX)));
                txtIcerik.requestFocus();
                txtIcerik.setSelection(txtIcerik.length());
                imm.showSoftInput(txtIcerik, 0);
            } else if(Icerik.length() < getResources().getInteger(R.integer.GeriBildirimKarakterSayisi_MIN)) {
                txtIcerik.setError(getString(R.string.hata_en_az_karakter, String.valueOf(GeriBildirimKarakterSayisi_MIN)));
                txtIcerik.requestFocus();
                txtIcerik.setSelection(txtIcerik.length());
                imm.showSoftInput(txtIcerik, 0);
            } else txtIcerik.setError(null);

            if(txtIcerik.getError() == null) {
                btnKapat.setEnabled(false);
                btnGonder.setEnabled(false);
                btnEkranGoruntusu1.setEnabled(false);
                btnEkranGoruntusu2.setEnabled(false);
                btnEkranGoruntusu3.setEnabled(false);
                AkorDefterimSys.UnFocusEditText(txtIcerik);

                if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
                    PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
                    PDIslem.show();
                }

                AkorDefterimSys.IPAdresGetir(activity);
            }
        } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
    }
}