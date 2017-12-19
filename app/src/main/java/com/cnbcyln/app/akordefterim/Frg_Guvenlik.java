package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.Interface.Interface_FragmentDataConn;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.cnbcyln.app.akordefterim.util.Strings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Frg_Guvenlik extends Fragment implements OnClickListener {
	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	private SharedPreferences sharedPref;
	SharedPreferences.Editor sharedPrefEditor;
	InputMethodManager imm;
	Interface_FragmentDataConn FragmentDataConn;
	Typeface YaziFontu;
	AlertDialog ADDialog_SifreDegistirme, ADDialog_InternetBaglantisi;
	LayoutInflater inflater;
	Resources RES;
	ProgressDialog PDDialog;

	Button btnSifreDegistir;
	TextView lblGuvenlik_Yazi, lblIstatistiklerBaslik;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_frg_guvenlik, container, false);
	}

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		AkorDefterimSys = new AkorDefterimSys(activity);
		RES = activity.getResources();
		FragmentDataConn = (Interface_FragmentDataConn) activity;

		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı

		inflater = activity.getLayoutInflater();

		sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		lblGuvenlik_Yazi = (TextView) activity.findViewById(R.id.lblGuvenlik_Yazi);
		lblGuvenlik_Yazi.setTypeface(YaziFontu);

		lblIstatistiklerBaslik = (TextView) activity.findViewById(R.id.lblIstatistiklerBaslik);
		lblIstatistiklerBaslik.setTypeface(YaziFontu);

		btnSifreDegistir = (Button) activity.findViewById(R.id.btnSifreDegistir);
		btnSifreDegistir.setTypeface(YaziFontu);
		btnSifreDegistir.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnSifreDegistir:
				View ViewDialogContent = inflater.inflate(R.layout.dialog_sifre_degistir, null);
				ADDialog_SifreDegistirme = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher, getString(R.string.parolani_degistir), ViewDialogContent, getString(R.string.degistir), getString(R.string.web_iptal), false);
				ADDialog_SifreDegistirme.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

				final TextView Dialog_lblSifreDegistirmeBilgi = (TextView) ViewDialogContent.findViewById(R.id.Dialog_lblSifreDegistirmeBilgi);
				Dialog_lblSifreDegistirmeBilgi.setTypeface(YaziFontu);

				final EditText Dialog_EtxtGuncelSifre = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtGuncelSifre);
				Dialog_EtxtGuncelSifre.setTypeface(YaziFontu);

				final EditText Dialog_EtxtYeniSifre = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtYeniSifre);
				Dialog_EtxtYeniSifre.setTypeface(YaziFontu);

				final EditText Dialog_EtxtYeniSifreTekrar = (EditText) ViewDialogContent.findViewById(R.id.Dialog_EtxtYeniSifreTekrar);
				Dialog_EtxtYeniSifreTekrar.setTypeface(YaziFontu);

				ADDialog_SifreDegistirme.show();

				ADDialog_SifreDegistirme.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
					@SuppressWarnings("ConstantConditions")
					@Override
					public void onClick(View v) {
						Dialog_EtxtGuncelSifre.setText(Dialog_EtxtGuncelSifre.getText().toString().trim());
						Dialog_EtxtYeniSifre.setText(Dialog_EtxtYeniSifre.getText().toString().trim());
						Dialog_EtxtYeniSifreTekrar.setText(Dialog_EtxtYeniSifreTekrar.getText().toString().trim());

						String GuncelSifre = Dialog_EtxtGuncelSifre.getText().toString();
						String YeniSifre = Dialog_EtxtYeniSifre.getText().toString();
						String YeniSifreTekrar = Dialog_EtxtYeniSifreTekrar.getText().toString();

						if(AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
							if (TextUtils.isEmpty(GuncelSifre)){
								Dialog_EtxtGuncelSifre.requestFocus();
								Dialog_EtxtGuncelSifre.setSelection(GuncelSifre.length());
								imm.showSoftInput(Dialog_EtxtGuncelSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtguncelsifre_hata1), Toast.LENGTH_SHORT);

							} else if(AkorDefterimSys.EditTextKarakterKontrol(GuncelSifre, RES.getInteger(R.integer.ParolaKarakterSayisi_MIN), RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
								Dialog_EtxtGuncelSifre.requestFocus();
								Dialog_EtxtGuncelSifre.setSelection(GuncelSifre.length());
								imm.showSoftInput(Dialog_EtxtGuncelSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtguncelsifre_hata2, String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

							} else if(!AkorDefterimSys.isValid(GuncelSifre, "SadeceSayiKucukHarfBuyukHarf")) {
								Dialog_EtxtGuncelSifre.requestFocus();
								Dialog_EtxtGuncelSifre.setSelection(GuncelSifre.length());
								imm.showSoftInput(Dialog_EtxtGuncelSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtguncelsifre_hata3), Toast.LENGTH_SHORT);

							} else if(!AkorDefterimSys.isValid(GuncelSifre, "Mutlaka_EnAzBir_Sayi_KucukHarf_Icermeli")) {
								Dialog_EtxtGuncelSifre.requestFocus();
								Dialog_EtxtGuncelSifre.setSelection(GuncelSifre.length());
								imm.showSoftInput(Dialog_EtxtGuncelSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtguncelsifre_hata4), Toast.LENGTH_SHORT);

							} else if (!Strings.getSHA1(GuncelSifre).equals(sharedPref.getString("prefHesapSifreSha1", ""))){
								Dialog_EtxtGuncelSifre.requestFocus();
								Dialog_EtxtGuncelSifre.setSelection(Dialog_EtxtGuncelSifre.getText().length());
								imm.showSoftInput(Dialog_EtxtGuncelSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtguncelsifre_hata5), Toast.LENGTH_SHORT);

							} else if (TextUtils.isEmpty(YeniSifre)){
								Dialog_EtxtYeniSifre.requestFocus();
								Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
								imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata1), Toast.LENGTH_SHORT);

							} else if(AkorDefterimSys.EditTextKarakterKontrol(YeniSifre, RES.getInteger(R.integer.ParolaKarakterSayisi_MIN), RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))) {
								Dialog_EtxtYeniSifre.requestFocus();
								Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
								imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata2, String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MIN)), String.valueOf(RES.getInteger(R.integer.ParolaKarakterSayisi_MAX))), Toast.LENGTH_SHORT);

							} else if(!AkorDefterimSys.isValid(YeniSifre, "SadeceSayiKucukHarfBuyukHarf")) {
								Dialog_EtxtYeniSifre.requestFocus();
								Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
								imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata3), Toast.LENGTH_SHORT);

							} else if(!AkorDefterimSys.isValid(YeniSifre, "Mutlaka_EnAzBir_Sayi_KucukHarf_Icermeli")) {
								Dialog_EtxtYeniSifre.requestFocus();
								Dialog_EtxtYeniSifre.setSelection(YeniSifre.length());
								imm.showSoftInput(Dialog_EtxtYeniSifre, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.txtyenisifre_hata4), Toast.LENGTH_SHORT);

							} else if(!AkorDefterimSys.EditTextisMatching(Dialog_EtxtYeniSifre, Dialog_EtxtYeniSifreTekrar)) {
								Dialog_EtxtYeniSifreTekrar.requestFocus();
								Dialog_EtxtYeniSifreTekrar.setSelection(YeniSifreTekrar.length());
								imm.showSoftInput(Dialog_EtxtYeniSifreTekrar, 0);
								AkorDefterimSys.ToastMsj(activity, getString(R.string.yenisifre_yenisifretekrar_uyusmuyor), Toast.LENGTH_SHORT);

							} else {
								imm.hideSoftInputFromWindow(Dialog_EtxtGuncelSifre.getWindowToken(), 0);
								imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifre.getWindowToken(), 0);
								imm.hideSoftInputFromWindow(Dialog_EtxtYeniSifreTekrar.getWindowToken(), 0);

								new SifreDegistir().execute(sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""), YeniSifre);
							}
						} else {
							ADDialog_InternetBaglantisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
									getString(R.string.internet_baglantisi),
									getString(R.string.internet_baglantisi_saglanamadi),
									activity.getString(R.string.tamam));
							ADDialog_InternetBaglantisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
							ADDialog_InternetBaglantisi.show();

							ADDialog_InternetBaglantisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									ADDialog_InternetBaglantisi.cancel();
								}
							});
						}
					}
				});

				ADDialog_SifreDegistirme.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog_SifreDegistirme.dismiss();
					}
				});

				break;
		}
	}

	private class SifreDegistir extends AsyncTask<String, String, String> {
		String EmailKullaniciAdi, SifreSHA1, YeniSifre, YeniSifreSHA1;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			PDDialog = new ProgressDialog(activity);
			PDDialog.setMessage(getString(R.string.islem_yapiliyor));
			PDDialog.setCancelable(false);
			PDDialog.show();
		}

		@Override
		protected String doInBackground(String... parametre) {
			EmailKullaniciAdi = String.valueOf(parametre[0]);
			SifreSHA1 = String.valueOf(parametre[1]);
			YeniSifre = String.valueOf(parametre[2]);
			YeniSifreSHA1 = Strings.getSHA1(YeniSifre);

			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
			nameValuePairs.add(new BasicNameValuePair("sifresha1", SifreSHA1));
			nameValuePairs.add(new BasicNameValuePair("yenisifre", YeniSifre));
			nameValuePairs.add(new BasicNameValuePair("yenisifresha1", YeniSifreSHA1));
			String sonuc = null;

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(AkorDefterimSys.PHPHesapYeniSifre);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line;

				while ((line = reader.readLine()) != null) {
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

				PDDialog.dismiss();

				switch (JSONGelenVeri.getInt("sonuc")) {
					case 1:
						ADDialog_SifreDegistirme.dismiss();

						sharedPrefEditor = sharedPref.edit();
						sharedPrefEditor.putString("prefHesapSifreSha1", YeniSifreSHA1);
						sharedPrefEditor.apply();

						AkorDefterimSys.ToastMsj(activity, getString(R.string.parola_degistirildi), Toast.LENGTH_SHORT);

						break;
					case 0:
						if (JSONGelenVeri.getString("aciklama").equals("yenisifre alanı boş")) {
							activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Klavyeyi gizleyen method
							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						} else if (JSONGelenVeri.getString("aciklama").equals("yenisifresha1 alanı boş")) {
							activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Klavyeyi gizleyen method
							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						} else if (JSONGelenVeri.getString("aciklama").equals("güncelleme işlemi başarısız")) {
							activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Klavyeyi gizleyen method
							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						} else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
							activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Klavyeyi gizleyen method
							AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
						}

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
			PDDialog.dismiss();
		}
	}
}