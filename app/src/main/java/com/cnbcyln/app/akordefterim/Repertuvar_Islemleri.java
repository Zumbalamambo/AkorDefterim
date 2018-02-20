package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.AndroidMultiPartEntity;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("ALL")
public class Repertuvar_Islemleri extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private Veritabani veritabani;
	SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDDBYedekleniyor, PDDBGeriYukleniyor;
    File YerelDB;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	Button btnListeYonetimi, btnKategoriYonetimi, btnTarzYonetimi, btnSarkiYonetimi, btnVeritabaniSifirla;
	TextView lblBaslik, lblRepertuvarIslemleriAciklama, lblYedekle, lblSonYedeklemeTarihi, lblGeriYukle, lblSonGeriYukleTarihi;
	LinearLayout LLYedekleGeriYukle;
	RelativeLayout RLYedekle, RLGeriYukle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repertuvar_islemleri);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
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

        lblRepertuvarIslemleriAciklama = findViewById(R.id.lblRepertuvarIslemleriAciklama);
        lblRepertuvarIslemleriAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
        lblRepertuvarIslemleriAciklama.setText(getString(R.string.repertuvar_islemleri_aciklama, getString(R.string.uygulama_adi)));
        AkorDefterimSys.setTextViewHTML(lblRepertuvarIslemleriAciklama);

        btnListeYonetimi = findViewById(R.id.btnListeYonetimi);
        btnListeYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnListeYonetimi.setOnClickListener(this);

        btnKategoriYonetimi = findViewById(R.id.btnKategoriYonetimi);
        btnKategoriYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnKategoriYonetimi.setOnClickListener(this);

        btnTarzYonetimi = findViewById(R.id.btnTarzYonetimi);
        btnTarzYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnTarzYonetimi.setOnClickListener(this);

        btnSarkiYonetimi = findViewById(R.id.btnSarkiYonetimi);
        btnSarkiYonetimi.setTypeface(YaziFontu, Typeface.NORMAL);
        btnSarkiYonetimi.setOnClickListener(this);

        LLYedekleGeriYukle = findViewById(R.id.LLYedekleGeriYukle);

        RLYedekle = findViewById(R.id.RLYedekle);
        RLYedekle.setOnClickListener(this);

        lblYedekle = findViewById(R.id.lblYedekle);
        lblYedekle.setTypeface(YaziFontu, Typeface.NORMAL);

        lblSonYedeklemeTarihi = findViewById(R.id.lblSonYedeklemeTarihi);
        lblSonYedeklemeTarihi.setTypeface(YaziFontu, Typeface.NORMAL);
        lblSonYedeklemeTarihi.setText(getString(R.string.son_yedekleme, sharedPref.getString("prefSonYedeklenmeTarihi", "-")));
        AkorDefterimSys.setTextViewHTML(lblSonYedeklemeTarihi);

        RLGeriYukle = findViewById(R.id.RLGeriYukle);
        RLGeriYukle.setOnClickListener(this);

        lblGeriYukle = findViewById(R.id.lblGeriYukle);
        lblGeriYukle.setTypeface(YaziFontu, Typeface.NORMAL);

        lblSonGeriYukleTarihi = findViewById(R.id.lblSonGeriYukleTarihi);
        lblSonGeriYukleTarihi.setTypeface(YaziFontu, Typeface.NORMAL);
        lblSonGeriYukleTarihi.setText(getString(R.string.son_yedekleme, sharedPref.getString("prefSonGeriYuklemeTarihi", "-")));
        AkorDefterimSys.setTextViewHTML(lblSonGeriYukleTarihi);

        btnVeritabaniSifirla = findViewById(R.id.btnVeritabaniSifirla);
        btnVeritabaniSifirla.setTypeface(YaziFontu, Typeface.NORMAL);
        btnVeritabaniSifirla.setText(getString(R.string.veritabani_sifirla).toUpperCase());
        btnVeritabaniSifirla.setOnClickListener(this);

        if(!AkorDefterimSys.GirisYapildiMi()) {
            LLYedekleGeriYukle.setAlpha((float) 0.5);
            RLYedekle.setEnabled(false);
            RLGeriYukle.setEnabled(false);
        }
	}

    @Override
    protected void onStart() {
        super.onStart();
        AkorDefterimSys.activity = activity;
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				onBackPressed();
				break;
            case R.id.btnListeYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Liste_Yonetimi.class), "Slide");
                break;
            case R.id.btnKategoriYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Kategori_Yonetimi.class), "Slide");
                break;
            case R.id.btnTarzYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Tarz_Yonetimi.class), "Slide");
                break;
            case R.id.btnSarkiYonetimi:
                AkorDefterimSys.EkranGetir(new Intent(activity, Sarki_Yonetimi.class), "Slide");
                break;
            case R.id.RLYedekle:
                if(AkorDefterimSys.InternetErisimKontrolu()) {
                    YerelDB = new File(AkorDefterimSys.YerelDBDizinGetir() + File.separator + veritabani.DBAdi);

                    if(YerelDB.exists()) new DBYedekle().execute();
                    else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yerel_veritabani_dosyası_bulunamadi));
                } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

                break;
            case R.id.RLGeriYukle:
                if(AkorDefterimSys.InternetErisimKontrolu()) {
                    if(!AkorDefterimSys.ProgressDialogisShowing(PDDBGeriYukleniyor)) {
                        PDDBGeriYukleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.lutfen_bekleyiniz), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDDBGeriYukleniyor_Timeout");
                        PDDBGeriYukleniyor.show();
                    }

                    AkorDefterimSys.DosyaBilgisiGetir(activity, AkorDefterimSys.PHPYedekDBKlasoruDizini, sharedPref.getString("prefHesapID", "") + ".db", "DosyaBilgisiGetir_DBGeriYukle");
                }
                else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));

                break;
            case R.id.btnVeritabaniSifirla:
                if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                    ADDialog = AkorDefterimSys.H2ButtonCustomAlertDialog(activity,
                            getString(R.string.veritabani_sifirla),
                            getString(R.string.veritabani_sifirlama_mesaji),
                            getString(R.string.evet),
                            "ADDialog_Veritabani_Sifirla_Evet",
                            getString(R.string.hayir),
                            "ADDialog_Veritabani_Sifirla_Hayir");
                    ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    ADDialog.show();
                }
                break;
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        
        menu.add(0, 0, 0, getString(R.string.sorun_bildir));
        menu.add(0, 1, 0, getString(R.string.gorus_bildir));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Yeni açılacak olan intent'e gönderilecek bilgileri tanımlıyoruz
        Intent mIntent = new Intent(activity, Destek_GeriBildirim.class);

        switch (item.getItemId()) {
            case 0:
                mIntent.putExtra("BildirimTipi", "Sorun");
                break;
            case 1:
                mIntent.putExtra("BildirimTipi", "Görüş");
                break;
            default:
                return false;
        }

        AkorDefterimSys.EkranGetir(mIntent, "Slide");

        return true;
    }

    @Override
    public void AsyncTaskReturnValue(String sonuc) {
        try {
            JSONObject JSONSonuc = new JSONObject(sonuc);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat SDF1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat SDF2 = new SimpleDateFormat("dd MMMM yyyy - HH:mm:ss");

            switch (JSONSonuc.getString("Islem")) {
                case "TarihSaatOgren_Son_Yedeklenme_Tarihini_Guncelle":
                    AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);

                    if(JSONSonuc.getBoolean("Sonuc")) {
                        sharedPrefEditor = sharedPref.edit();
                        sharedPrefEditor.putString("prefSonYedeklenmeTarihi", SDF2.format(SDF1.parse(JSONSonuc.getString("TarihSaat"))));
                        sharedPrefEditor.apply();

                        lblSonYedeklemeTarihi.setText(getString(R.string.son_yedekleme, sharedPref.getString("prefSonYedeklenmeTarihi", "-")));
                        AkorDefterimSys.setTextViewHTML(lblSonYedeklemeTarihi);

                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.veritabani_yedeklendi));
                    } else {
                        Date TarihSaat = Calendar.getInstance().getTime();

                        sharedPrefEditor = sharedPref.edit();
                        sharedPrefEditor.putString("prefSonYedeklenmeTarihi", SDF2.format(TarihSaat));
                        sharedPrefEditor.apply();

                        lblSonYedeklemeTarihi.setText(getString(R.string.son_yedekleme, SDF2.format(TarihSaat)));
                        AkorDefterimSys.setTextViewHTML(lblSonYedeklemeTarihi);

                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.veritabani_yedeklendi));
                    }
                    break;
                case "DosyaBilgisiGetir_DBGeriYukle":
                    if(JSONSonuc.getString("Aciklama").equals("Dosya bulundu.")) {
                        new DBGeriYukle().execute(AkorDefterimSys.YedekDBKlasoruDizini + sharedPref.getString("prefHesapID", "") + ".db");
                    } else if(JSONSonuc.getString("Aciklama").equals("Dosya bulunamadı.")) {
                        AkorDefterimSys.DismissProgressDialog(PDDBGeriYukleniyor);
                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.yedek_veritabani_dosyası_bulunamadi));
                    }
                    break;
                case "TarihSaatOgren_Son_Geri_Yukleme_Tarihini_Guncelle":
                    AkorDefterimSys.DismissProgressDialog(PDDBGeriYukleniyor);

                    if(JSONSonuc.getBoolean("Sonuc")) {
                        sharedPrefEditor = sharedPref.edit();
                        sharedPrefEditor.putString("prefSonGeriYuklemeTarihi", SDF2.format(SDF1.parse(JSONSonuc.getString("TarihSaat"))));
                        sharedPrefEditor.apply();

                        lblSonGeriYukleTarihi.setText(getString(R.string.son_geri_yukleme, sharedPref.getString("prefSonGeriYuklemeTarihi", "-")));
                        AkorDefterimSys.setTextViewHTML(lblSonGeriYukleTarihi);

                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.veritabani_geri_yuklendi));
                    } else {
                        Date TarihSaat = Calendar.getInstance().getTime();

                        sharedPrefEditor = sharedPref.edit();
                        sharedPrefEditor.putString("prefSonGeriYuklemeTarihi", SDF2.format(TarihSaat));
                        sharedPrefEditor.apply();

                        lblSonGeriYukleTarihi.setText(getString(R.string.son_geri_yukleme, SDF2.format(TarihSaat)));
                        AkorDefterimSys.setTextViewHTML(lblSonGeriYukleTarihi);

                        AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.veritabani_geri_yuklendi));
                    }
                    break;
                case "PDDBYedekleniyor_Timeout":
                    AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);
                    onBackPressed();
                    break;
                case "PDDBGeriYukleniyor_Timeout":
                    AkorDefterimSys.DismissProgressDialog(PDDBGeriYukleniyor);
                    onBackPressed();
                    break;
                case "ADDialog_Veritabani_Sifirla_Evet":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    veritabani.VeritabaniSifirla();
                    AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.veritabani_sifirlandi));
                    break;
                case "ADDialog_Veritabani_Sifirla_Hayir":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    break;
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint({"InflateParams", "StaticFieldLeak"})
    private class DBYedekle extends AsyncTask<Void, Integer, String> {
        long totalSize = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(!AkorDefterimSys.ProgressDialogisShowing(PDDBYedekleniyor)) {
                PDDBYedekleniyor = AkorDefterimSys.CustomProgressDialog(getString(R.string.db_yedekleniyor, "0%"), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDDBYedekleniyor_Timeout");
                PDDBYedekleniyor.show();
            }
        }

        @Override
        protected String doInBackground(Void... parametre) {
            return UploadFile();
        }

        private String UploadFile() {
            String Sonuc;

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AkorDefterimSys.PHPDosyaYukle);

                // Adding file data to http body
                entity.addPart("Dosya", new FileBody(YerelDB));
                entity.addPart("Dizin", new StringBody(AkorDefterimSys.PHPYedekDBKlasoruDizini));
                entity.addPart("DosyaAdi", new StringBody(sharedPref.getString("prefHesapID", "") + ".db"));

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
                            AkorDefterimSys.TarihSaatGetir(activity, "TarihSaatOgren_Son_Yedeklenme_Tarihini_Guncelle");
                        }
                    });
                    break;
                case "FileNotFoundException":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.islem_yapilirken_bir_hata_olustu));
                        }
                    });
                    break;
                case "MalformedURLException":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.url_hatasi));
                        }
                    });
                    break;
                case "IOException":
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);
                            AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.dosya_yazma_okuma_hatasi));
                        }
                    });
                    break;
                default:
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);
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
                    PDDBYedekleniyor.setMessage(getString(R.string.db_yedekleniyor, String.valueOf(Deger[0]) + "%"));
                }
            });
        }

        @Override
        protected void onCancelled(String Sonuc) {
            super.onCancelled(Sonuc);
            AkorDefterimSys.DismissProgressDialog(PDDBYedekleniyor);
        }
    }

    private class DBGeriYukle extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PDDBGeriYukleniyor.setMessage(getString(R.string.db_geri_yukleniyor, String.valueOf(0) + "%"));
        }

        @Override
        protected String doInBackground(String... param) {
            int count;
            try {
                String YedekDBURL = param[0];

                URL url = new URL(YedekDBURL);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                YerelDB = new File(AkorDefterimSys.YerelDBDizinGetir() + File.separator + veritabani.DBAdi);
                OutputStream output = new FileOutputStream(YerelDB);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AkorDefterimSys.TarihSaatGetir(activity, "TarihSaatOgren_Son_Geri_Yukleme_Tarihini_Guncelle");
                }
            });
        }

        protected void onProgressUpdate(final String... Deger) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PDDBGeriYukleniyor.setMessage(getString(R.string.db_geri_yukleniyor, String.valueOf(Deger[0]) + "%"));
                }
            });
        }

        @Override
        protected void onCancelled(String Sonuc) {
            super.onCancelled(Sonuc);
            AkorDefterimSys.DismissProgressDialog(PDDBGeriYukleniyor);
        }
    }
}