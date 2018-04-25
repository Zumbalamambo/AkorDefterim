package com.cnbcyln.app.akordefterim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Veritabani;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.AsyncTask;

@SuppressWarnings("ALL")
public class PageHandler implements HttpRequestHandler {

	private AkorDefterimSys AkorDefterimSys;
	private Veritabani Veritabani;
	private Context context = null;
	private SharedPreferences sharedPref;
	Intent Intent_AnaEkran;
	HttpEntity entity;
	HttpRequest request;
	HttpResponse response;
	HttpContext httpContext;
	ProgressDialog PDDialog;
	Resources RES;
	String HTML = "", IPAdres = "", BaglantiIstegiSonuc = "";
	private JSONObject JSONWebGelenVeri;
	Boolean BaglantiBekleniyorMu = false;

	public PageHandler(Context context,  String IPAdres){
		this.context = context;
		this.IPAdres = IPAdres;

		AkorDefterimSys = new AkorDefterimSys(context);
		Veritabani = new Veritabani(context);

		context.registerReceiver(broadcastReceiver, new IntentFilter("com.cnbcyln.app.akordefterim.PageHandler"));
		Intent_AnaEkran = new Intent("com.cnbcyln.app.akordefterim.AnaEkran");

		sharedPref = context.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		RES = context.getResources();
	}

	@Override
	public void handle(final HttpRequest request, final HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		final String uriString = request.getRequestLine().getUri();

		entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");

				if(uriString.equals("/")) { //Giriş Sayfası (Anasayfa)
					Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"WebBaglantiIstegi\", \"IPAdres\":\"" + IPAdres + "\"}");
					context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

					//HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_baglanti_bekleme_sayfasi, false));

					//writer.write(HTML);
					//writer.flush();

					BaglantiBekleniyorMu = true;

					while (BaglantiBekleniyorMu) {
						try {
							if(!BaglantiBekleniyorMu) break;

							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					//HTML = "";
					//writer.write(HTML);
					//writer.flush();

					if(BaglantiIstegiSonuc.equals("KabulEdildi")) {
						if(AkorDefterimSys.InternetErisimKontrolu()) HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_masterpage, false));
						else HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_netyok_sayfasi, false));
					} else if(BaglantiIstegiSonuc.equals("KabulEdilmedi")) {
						HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_red_sayfasi, false));
					} else if(BaglantiIstegiSonuc.equals("ZamanAsimi")) {
						HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_timeout_sayfasi, false));
					}
				} else if(uriString.equals("/favicon.ico")) {

				} else if(uriString.equals("/img_favicon.ico")) {

				} else { //AJAX DATA TRANSFERİ
					try {
						String strJSON = uriString;
						strJSON = strJSON.replace("%60%60","``");
						strJSON = strJSON.replace("%C2%B4%C2%B4","´´");
						strJSON = strJSON.replace("%C2%BD","½");
						strJSON = strJSON.replace("%3E",">");
						strJSON = strJSON.replace("%3C","<");
						strJSON = strJSON.replace("%C2%A3","£");
						strJSON = strJSON.replace("%5E%5E","^^");
						strJSON = strJSON.replace("%20"," ");
						strJSON = strJSON.replace("%7B","{");
						strJSON = strJSON.replace("%7D","}");
						strJSON = strJSON.replace("%22","\"");
						strJSON = strJSON.replace("%E2%80%83","&nbsp;&nbsp;&nbsp;&nbsp;");
						strJSON = strJSON.replace("%7C","|");
						strJSON = strJSON.replace("%C4%B1","ı");
						strJSON = strJSON.replace("%C4%9F","ğ");
						strJSON = strJSON.replace("%C3%BC","ü");
						strJSON = strJSON.replace("%C5%9F","ş");
						strJSON = strJSON.replace("%C3%B6","ö");
						strJSON = strJSON.replace("%C3%A7","ç");
						strJSON = strJSON.replace("%C4%9E","Ğ");
						strJSON = strJSON.replace("%C3%9C","Ü");
						strJSON = strJSON.replace("%C5%9E","Ş");
						strJSON = strJSON.replace("%C4%B0","İ");
						strJSON = strJSON.replace("%C3%96","Ö");
						strJSON = strJSON.replace("%C3%87","Ç");
						strJSON = strJSON.replace("/\"","\\\"");
						strJSON = strJSON.substring(1);

						if(strJSON.indexOf("{") != -1 && strJSON.indexOf("}") != -1) {
							JSONWebGelenVeri = new JSONObject(strJSON);

							if (JSONWebGelenVeri.getString("Islem").equals("SayfaGetir")){
								switch (JSONWebGelenVeri.getString("Sayfa")) {
									case "anasayfa":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_anasayfa, true));
										break;
									case "istatistikler":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_istatistikler, true));
										break;
									case "liste_yonetimi":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_liste_yonetimi, true));
										break;
									case "kategori_yonetimi":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_kategori_yonetimi, true));
										break;
									case "tarz_yonetimi":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_tarz_yonetimi, true));
										break;
									case "sarki_yonetimi":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_sarki_yonetimi, true));
										break;
									case "hakkinda":
										HTML = HTMLEklentiSistemi(AkorDefterimSys.openHTMLString(context, R.raw.html_hakkinda, true));
										break;
									case "yardim":
										HTML = "<b>Bu sayfa yapım aşamasındadır..</b>";
										break;
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("IstatistikSayfaDataGetir")){
								int ToplamListeSayisi = Veritabani.ListeSayisiGetir();
								int ToplamKategoriSayisi = Veritabani.KategoriSayisiGetir();
								int ToplamTarzSayisi = Veritabani.TarzSayisiGetir();
								int ToplamSarkiSayisi = Veritabani.SarkiSayisiGetir();

								HTML = "{\"ToplamListeSayisi\":\"" + ToplamListeSayisi + "\", \"ToplamKategoriSayisi\":\"" + ToplamKategoriSayisi + "\", \"ToplamTarzSayisi\":\"" + ToplamTarzSayisi + "\", \"ToplamSarkiSayisi\":\"" + ToplamSarkiSayisi + "\"}";





							} else if (JSONWebGelenVeri.getString("Islem").equals("ToplamListeSayisiGetir")){
								HTML = "{\"ToplamListeSayisi\":\"" + Veritabani.ListeSayisiGetir() + "\"}";

							} else if (JSONWebGelenVeri.getString("Islem").equals("ListeListesiGetir")){
								HTML = Veritabani.lstJSONSTR_ListeGetir(JSONWebGelenVeri.getInt("BaslangicNo"), JSONWebGelenVeri.getInt("Limit"));

							} else if (JSONWebGelenVeri.getString("Islem").equals("ListeEkle")){
								if (!JSONWebGelenVeri.getString("ListeAdi").contains(",")) {
									if (!Veritabani.ListeVarmiKontrol(JSONWebGelenVeri.getString("ListeAdi"))) {
										if (Veritabani.ListeEkle(JSONWebGelenVeri.getString("ListeAdi"))) {
											Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
											context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

											HTML = "{\"Sonuc\":true}";
										} else HTML = "{\"Sonuc\":false, \"ListeDurum\":\"ListeYok\"}";
									} else HTML = "{\"Sonuc\":false, \"ListeDurum\":\"ListeVar\"}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("ListeAdiGetir")){
								HTML = "{\"ListeAdi\":\"" + Veritabani.ListeAdiGetir(JSONWebGelenVeri.getInt("ListeID")) + "\"}";
							} else if (JSONWebGelenVeri.getString("Islem").equals("ListeDuzenle")){
								if (!JSONWebGelenVeri.getString("ListeAdi").contains(",")) {
									if (!Veritabani.ListeVarmiKontrol(JSONWebGelenVeri.getString("ListeAdi"))) {
										if (Veritabani.ListeDuzenle(JSONWebGelenVeri.getInt("ListeID"), JSONWebGelenVeri.getString("ListeAdi"))) {
											Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
											context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

											HTML = "{\"Sonuc\":true}";
										} else HTML = "{\"Sonuc\":false, \"ListeDurum\":\"ListeYok\"}";
									} else HTML = "{\"Sonuc\":false, \"ListeDurum\":\"ListeVar\"}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("ListeyeAitSarkiVarmiKontrol")){
								if (Veritabani.ListeyeAitSarkiVarmiKontrol(JSONWebGelenVeri.getInt("ListeID"))) {
									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("ListeSil")){
								if (Veritabani.ListeSil(JSONWebGelenVeri.getInt("ListeID"))) {
									Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
									context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}





							} else if (JSONWebGelenVeri.getString("Islem").equals("ToplamKategoriSayisiGetir")){
								HTML = "{\"ToplamKategoriSayisi\":\"" + Veritabani.KategoriSayisiGetir() + "\"}";
							} else if (JSONWebGelenVeri.getString("Islem").equals("KategoriListesiGetir")){
								HTML = Veritabani.lstJSONSTR_KategoriGetir(JSONWebGelenVeri.getInt("BaslangicNo"), JSONWebGelenVeri.getInt("Limit"));
							} else if (JSONWebGelenVeri.getString("Islem").equals("KategoriEkle")){
								if (!JSONWebGelenVeri.getString("KategoriAdi").contains(",")) {
									if (!Veritabani.KategoriVarmiKontrol(JSONWebGelenVeri.getString("KategoriAdi"))) {
										if (Veritabani.KategoriEkle(JSONWebGelenVeri.getString("KategoriAdi"))) {
											Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
											context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

											HTML = "{\"Sonuc\":true}";
										} else HTML = "{\"Sonuc\":false, \"KategoriDurum\":\"KategoriYok\"}";
									} else HTML = "{\"Sonuc\":false, \"KategoriDurum\":\"KategoriVar\"}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("KategoriAdiGetir")){
								HTML = "{\"KategoriAdi\":\"" + Veritabani.KategoriAdiGetir(JSONWebGelenVeri.getInt("KategoriID")) + "\"}";
							} else if (JSONWebGelenVeri.getString("Islem").equals("KategoriDuzenle")){
								if (!JSONWebGelenVeri.getString("KategoriAdi").contains(",")) {
									if (!Veritabani.KategoriVarmiKontrol(JSONWebGelenVeri.getString("KategoriAdi"))) {
										if (Veritabani.KategoriDuzenle(JSONWebGelenVeri.getInt("KategoriID"), JSONWebGelenVeri.getString("KategoriAdi"))) {
											Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
											context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

											HTML = "{\"Sonuc\":true}";
										} else HTML = "{\"Sonuc\":false, \"KategoriDurum\":\"KategoriYok\"}";
									} else HTML = "{\"Sonuc\":false, \"KategoriDurum\":\"KategoriVar\"}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("KategoriyeAitSarkiVarmiKontrol")){
								if (Veritabani.KategoriyeAitSarkiVarmiKontrol(JSONWebGelenVeri.getInt("KategoriID"))) {
									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("KategoriSil")){
								if (Veritabani.KategoriSil(JSONWebGelenVeri.getInt("KategoriID"))) {
									Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
									context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}





							} else if (JSONWebGelenVeri.getString("Islem").equals("ToplamTarzSayisiGetir")){
								HTML = "{\"ToplamTarzSayisi\":\"" + Veritabani.TarzSayisiGetir() + "\"}";
							} else if (JSONWebGelenVeri.getString("Islem").equals("TarzListesiGetir")){
								HTML = Veritabani.lstJSONSTR_TarzGetir(JSONWebGelenVeri.getInt("BaslangicNo"), JSONWebGelenVeri.getInt("Limit"));
							} else if (JSONWebGelenVeri.getString("Islem").equals("TarzEkle")){
								if (!JSONWebGelenVeri.getString("TarzAdi").contains(",")) {
									if (!Veritabani.TarzVarmiKontrol(JSONWebGelenVeri.getString("TarzAdi"))) {
										if (Veritabani.TarzEkle(JSONWebGelenVeri.getString("TarzAdi"))) {
											Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
											context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

											HTML = "{\"Sonuc\":true}";
										} else HTML = "{\"Sonuc\":false, \"TarzDurum\":\"TarzYok\"}";
									} else HTML = "{\"Sonuc\":false, \"TarzDurum\":\"TarzVar\"}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("TarzAdiGetir")){
								HTML = "{\"TarzAdi\":\"" + Veritabani.TarzAdiGetir(JSONWebGelenVeri.getInt("TarzID")) + "\"}";
							} else if (JSONWebGelenVeri.getString("Islem").equals("TarzDuzenle")){
								if (!JSONWebGelenVeri.getString("TarzAdi").contains(",")) {
									if (!Veritabani.TarzVarmiKontrol(JSONWebGelenVeri.getString("TarzAdi"))) {
										if (Veritabani.TarzDuzenle(JSONWebGelenVeri.getInt("TarzID"), JSONWebGelenVeri.getString("TarzAdi"))) {
											Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
											context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

											HTML = "{\"Sonuc\":true}";
										} else HTML = "{\"Sonuc\":false, \"TarzDurum\":\"TarzYok\"}";
									} else HTML = "{\"Sonuc\":false, \"TarzDurum\":\"TarzVar\"}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("TarzaAitSarkiVarmiKontrol")){
								if (Veritabani.TarzaAitSarkiVarmiKontrol(JSONWebGelenVeri.getInt("TarzID"))) {
									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("TarzSil")){
								if (Veritabani.TarzSil(JSONWebGelenVeri.getInt("TarzID"))) {
									Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_TabRepKontrol_Guncelle\"}");
									context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}





							} else if (JSONWebGelenVeri.getString("Islem").equals("ToplamSarkiSayisiGetir")){
								HTML = "{\"ToplamSarkiSayisi\":\"" + Veritabani.SarkiSayisiGetir() + "\"}";
							} else if (JSONWebGelenVeri.getString("Islem").equals("SarkiListesiGetir")){
								HTML = Veritabani.lstJSONSTR_SarkiGetir(JSONWebGelenVeri.getInt("BaslangicNo"), JSONWebGelenVeri.getInt("Limit"), JSONWebGelenVeri.getString("SiralamaTipi"));
							} else if (JSONWebGelenVeri.getString("Islem").equals("FullSarkiListesiGetir")){
								HTML = Veritabani.lstJSONSTR_FullSarkiGetir();
							} else if (JSONWebGelenVeri.getString("Islem").equals("SarkiEkle")){
								if (!JSONWebGelenVeri.getString("SanatciAdi").contains(",") && !JSONWebGelenVeri.getString("SarkiAdi").contains(",")) {
								/*Calendar mcurrentTime = Calendar.getInstance();
								String Gun = "", Ay = "", Yil = "", Saat = "", Dakika = "", Saniye = "", Tarih = "";

								Gun = String.valueOf(mcurrentTime.get(Calendar.DAY_OF_MONTH)); // Güncel Günü alıyoruz
								Ay = String.valueOf(mcurrentTime.get(Calendar.MONTH) + 1); // Güncel Ayı alıyoruz
								Yil = String.valueOf(mcurrentTime.get(Calendar.YEAR)); // Güncel Yılı alıyoruz
								Saat = String.valueOf(mcurrentTime.get(Calendar.HOUR)); // Güncel Saati alıyoruz
								Dakika = String.valueOf(mcurrentTime.get(Calendar.MINUTE)); // Güncel Dakikayı alıyoruz
								Saniye = String.valueOf(mcurrentTime.get(Calendar.SECOND)); // Güncel Saniyeyi alıyoruz

								if (Gun.length() == 1) Gun = "0" + Gun;
								if (Ay.length() == 1) Ay = "0" + Ay;
								if (Yil.length() == 1) Yil = "0" + Yil;
								if (Saat.length() == 1) Saat = "0" + Saat;
								if (Dakika.length() == 1) Dakika = "0" + Dakika;
								if (Saniye.length() == 1) Saniye = "0" + Saniye;

								Tarih = Yil + "-" + Ay + "-" + Gun + " " + Saat + ":" + Dakika + ":" + Saniye;*/

									if (Veritabani.SarkiEkle(
											JSONWebGelenVeri.getInt("ListeID"),
											JSONWebGelenVeri.getInt("KategoriID"),
											JSONWebGelenVeri.getInt("TarzID"),
											JSONWebGelenVeri.getString("SarkiAdi"),
											JSONWebGelenVeri.getString("SanatciAdi"),
											JSONWebGelenVeri.getString("SarkiIcerik"), 0,
											JSONWebGelenVeri.getString("VideoURL"))) {

										Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_Istatistik_Guncelle\"}");
										context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

										if(JSONWebGelenVeri.getBoolean("AkorDefterimGonder")) new OnlineIcerikGonder().execute(JSONWebGelenVeri.getString("SanatciAdi"), JSONWebGelenVeri.getString("SarkiAdi"), JSONWebGelenVeri.getString("SarkiIcerik"), sharedPref.getString("prefHesapID", "0"));

										HTML = "{\"Sonuc\":true}";
									} else {
										HTML = "{\"Sonuc\":false}";
									}
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("SarkiGetir")){
								HTML = Veritabani.SarkiGetir(JSONWebGelenVeri.getInt("SarkiID"));
							} else if (JSONWebGelenVeri.getString("Islem").equals("SarkiDuzenle")){
								if (!JSONWebGelenVeri.getString("SanatciAdi").contains(",") && !JSONWebGelenVeri.getString("SarkiAdi").contains(",")) {
									if (Veritabani.SarkiDuzenle(
											JSONWebGelenVeri.getInt("SarkiID"),
											JSONWebGelenVeri.getInt("ListeID"),
											JSONWebGelenVeri.getInt("KategoriID"),
											JSONWebGelenVeri.getInt("TarzID"),
											JSONWebGelenVeri.getString("SanatciAdi"),
											JSONWebGelenVeri.getString("SarkiAdi"),
											JSONWebGelenVeri.getString("VideoURL"),
											JSONWebGelenVeri.getString("SarkiIcerik"))) {

										Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_Istatistik_Guncelle\"}");
										context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

										HTML = "{\"Sonuc\":true}";
									} else {
										HTML = "{\"Sonuc\":false}";
									}
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							} else if (JSONWebGelenVeri.getString("Islem").equals("SarkiSil")){
								if (Veritabani.SarkiSil(JSONWebGelenVeri.getInt("SarkiID"))) {
									Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"Frg_Istatistik_Guncelle\"}");
									context.getApplicationContext().sendBroadcast(Intent_AnaEkran);

									HTML = "{\"Sonuc\":true}";
								} else {
									HTML = "{\"Sonuc\":false}";
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				writer.write(HTML);
				writer.flush();
			}
		});

		response.setHeader("Content-Type", "text/html");
		response.setEntity(entity);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {
			String Islem = intent.getStringExtra("Islem");

			switch (Islem) {
				case "WebBaglantiIstegi":
					BaglantiIstegiSonuc = intent.getStringExtra("Sonuc");
					BaglantiBekleniyorMu = false;

					break;
				case "Mesaj":
					String Icerik = intent.getStringExtra("Icerik");
					HTML = "{\"Sonuc\":true,\"Icerik\":\"" + Icerik + "\"}";
					break;
			}
		}
	};

	private String HTMLEklentiSistemi(String HTML_Icerik){
		String EklentiBaslangic = "[Eklenti#";
		String EklentiBitis = "#Eklenti]";
		String StrBaslangic = "[Str#";
		String StrBitis = "#Str]";
		String DataBaslangic = "[Data#";
		String DataBitis = "#Data]";
		String BulunanString = "";
		String[] NesneBilgi = null;

		while (HTML_Icerik.contains(EklentiBaslangic) && HTML_Icerik.contains(EklentiBitis)){
			BulunanString = HTML_Icerik.substring(HTML_Icerik.indexOf(EklentiBaslangic) + EklentiBaslangic.length(), HTML_Icerik.indexOf(EklentiBitis));
			NesneBilgi = BulunanString.split("\\.");

			switch (NesneBilgi[1]) {
				case "jpg":
					HTML_Icerik = HTML_Icerik.replace(EklentiBaslangic + BulunanString + EklentiBitis, "data:image/jpg;base64," + AkorDefterimSys.ImageToString(context, context.getResources().getIdentifier(context.getPackageName() + ":raw/" + NesneBilgi[0],null,null), NesneBilgi[1]));
					break;
				case "png":
					HTML_Icerik = HTML_Icerik.replace(EklentiBaslangic + BulunanString + EklentiBitis, "data:image/png;base64," + AkorDefterimSys.ImageToString(context, context.getResources().getIdentifier(context.getPackageName() + ":raw/" + NesneBilgi[0],null,null), NesneBilgi[1]));
					break;
				case "ico":
					HTML_Icerik = HTML_Icerik.replace(EklentiBaslangic + BulunanString + EklentiBitis, "data:image/x-icon;base64," + AkorDefterimSys.ImageToString(context, context.getResources().getIdentifier(context.getPackageName() + ":raw/" + NesneBilgi[0],null,null), NesneBilgi[1]));
					break;
				case "js":
					HTML_Icerik = HTML_Icerik.replace(EklentiBaslangic + BulunanString + EklentiBitis, AkorDefterimSys.openHTMLString(context, context.getResources().getIdentifier(context.getPackageName() + ":raw/" + NesneBilgi[0],null,null), false));
					break;
				case "css":
					HTML_Icerik = HTML_Icerik.replace(EklentiBaslangic + BulunanString + EklentiBitis, AkorDefterimSys.openHTMLString(context, context.getResources().getIdentifier(context.getPackageName() + ":raw/" + NesneBilgi[0],null,null), false));
					break;
			}
		}

		while (HTML_Icerik.contains(StrBaslangic) && HTML_Icerik.contains(StrBitis)){
			BulunanString = HTML_Icerik.substring(HTML_Icerik.indexOf(StrBaslangic) + StrBaslangic.length(), HTML_Icerik.indexOf(StrBitis));

			HTML_Icerik = HTML_Icerik.replace(StrBaslangic + BulunanString + StrBitis, context.getResources().getString(context.getResources().getIdentifier(context.getPackageName() + ":string/" + BulunanString,null,null)));
		}

		while(HTML_Icerik.contains(DataBaslangic) && HTML_Icerik.contains(DataBitis)){
			BulunanString = HTML_Icerik.substring(HTML_Icerik.indexOf(DataBaslangic) + DataBaslangic.length(), HTML_Icerik.indexOf(DataBitis));

			switch (BulunanString) {
				case "data_surum":
					try {
						HTML_Icerik = HTML_Icerik.replace(DataBaslangic + BulunanString + DataBitis, context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.toString());
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					break;
			}
		}

		return HTML_Icerik;
	}

	@SuppressLint("InflateParams")
	public class OnlineIcerikGonder extends AsyncTask<String, String, String> {
		String SanatciAdi, SarkiAdi, Icerik, EkleyenID;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			//PDDialog = new ProgressDialog(context);
			//PDDialog.setMessage(RES.getString(R.string.islem_yapiliyor));
			//PDDialog.setCancelable(false);
			//PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			SanatciAdi = String.valueOf(parametre[0]);
			SarkiAdi = String.valueOf(parametre[1]);
			Icerik = String.valueOf(parametre[2]);
			EkleyenID = String.valueOf(parametre[3]);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("sanatciadi", SanatciAdi));
			nameValuePairs.add(new BasicNameValuePair("sarkiadi", SarkiAdi));
			nameValuePairs.add(new BasicNameValuePair("icerik", Icerik));
			nameValuePairs.add(new BasicNameValuePair("ekleyenid", EkleyenID));

			String sonuc = "";

			try{
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPRepertuvarIcerikGonder);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null)
				{
					sb.append(line).append("\n");
				}


				sonuc = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return sonuc;
		}

		@Override
		protected void onPostExecute(String Sonuc) {
			try {
				JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

				//PDDialog.dismiss();

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						if(JSONGelenVeri.getString("aciklama").equals("içerik gönderme işlemi başarılı")){
							Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"ToastMesaj\", \"Mesaj\":\"" + RES.getString(R.string.icerik_gonderildi) + "\"}");
							context.getApplicationContext().sendBroadcast(Intent_AnaEkran);
						}

						break;
					default:
						Intent_AnaEkran.putExtra("JSONData", "{\"Islem\":\"ToastMesaj\", \"Mesaj\":\"" + RES.getString(R.string.islem_yapilirken_bir_hata_olustu) + "\"}");
						context.getApplicationContext().sendBroadcast(Intent_AnaEkran);
						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onProgressUpdate(String... Deger) {
			super.onProgressUpdate(Deger);
		}

		@Override
		protected void onCancelled(String Sonuc) {
			super.onCancelled(Sonuc);
			//PDDialog.dismiss();
		}
	}
}