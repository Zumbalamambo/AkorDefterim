package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ConstantConditions")
public class SplashEkran extends Activity implements Interface_AsyncResponse {

	private Activity activity;
	private AkorDefterimSys AkorDefterimSys;

	SharedPreferences.Editor sharedPrefEditor;
	Typeface YaziFontu;
	Animation anim;
	ProgressDialog PDSistemKontrol, PDGuncellemeKontrol, PDGirisYap;
	AlertDialog ADDialog_PlayGoogleServisi, ADDialog_SistemDurum, ADDialog_Guncelleme, ADDialog_InternetErisimSorunu, ADDialog_HesapDurumu;

	//private GoogleApiClient mGoogleLoginApiClient;

	//CallbackManager mFacebookCallbackManager;

	RelativeLayout RLBG;
	TextView lblVersiyonNo;

	String FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon;

	@SuppressLint("HardwareIds")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//FacebookSdk.sdkInitialize(getApplicationContext());
		//mFacebookCallbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.activity_splash_ekran);

		activity = this;
		AkorDefterimSys = new AkorDefterimSys(activity);
		YaziFontu = AkorDefterimSys.FontGetir(activity, "anivers_regular");

		AkorDefterimSys.GenelAyarlar(); // Uygulama için genel ayarları uyguladık.
		AkorDefterimSys.TransparanNotifyBar(); // Notification Bar'ı transparan yapıyoruz.
		AkorDefterimSys.NotifyIkonParlakligi(); // Notification Bar'daki simgelerin parlaklığını aldık.

		FirebaseToken = FirebaseInstanceId.getInstance().getToken();
		OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);

		try {
			UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		lblVersiyonNo = (TextView) findViewById(R.id.lblVersiyonNo);
		lblVersiyonNo.setTypeface(YaziFontu, Typeface.BOLD);
		lblVersiyonNo.setText(String.valueOf("v").concat(UygulamaVersiyon));

		anim = AnimationUtils.loadAnimation(this, R.anim.anim_giris_bg_alpha);
		anim.reset();

		RLBG = (RelativeLayout) findViewById(R.id.RLBG);
		RLBG.clearAnimation();
		RLBG.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
		anim.reset();

        //FacebookLoginInit();
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (AkorDefterimSys.checkPlayServices(activity)) {
			//GoogleAPIInit();

			if (AkorDefterimSys.InternetErisimKontrolu()) { // İnternet bağlantısı var ise
				PDSistemKontrol = AkorDefterimSys.CustomProgressDialog(getString(R.string.lutfen_bekleyiniz), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
				PDSistemKontrol.show();

				AkorDefterimSys.SistemDurumKontrol();
			} else { // İnternet bağlantısı yok ise
				ADDialog_InternetErisimSorunu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.internet_baglantisi),
						getString(R.string.internet_baglantisi_saglanamadi),
						activity.getString(R.string.cevrimdisi_devam_et),
						activity.getString(R.string.uygulamadan_cik));
				ADDialog_InternetErisimSorunu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog_InternetErisimSorunu.show();

				ADDialog_InternetErisimSorunu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog_InternetErisimSorunu.dismiss();

						AkorDefterimSys.HesapPrefSifirla();

						Intent mIntent = new Intent(activity, Ana.class);
						mIntent.putExtra("Islem", "");
						startActivity(mIntent);

						finish();
					}
				});

				ADDialog_InternetErisimSorunu.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog_InternetErisimSorunu.dismiss();
						Process.killProcess(Process.myPid());
					}
				});
			}
		} else {
			if(!AkorDefterimSys.AlertDialogisShowing(ADDialog_PlayGoogleServisi)) {
				ADDialog_PlayGoogleServisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
						getString(R.string.google_play_servis_baslik),
						getString(R.string.google_play_servis_hata1),
						activity.getString(R.string.tamam));
				ADDialog_PlayGoogleServisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				ADDialog_PlayGoogleServisi.show();

				ADDialog_PlayGoogleServisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ADDialog_PlayGoogleServisi.dismiss();
						Process.killProcess(Process.myPid());
					}
				});
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		AkorDefterimSys.DismissProgressDialog(PDSistemKontrol);
		AkorDefterimSys.DismissProgressDialog(PDGuncellemeKontrol);
		AkorDefterimSys.DismissProgressDialog(PDGirisYap);
		AkorDefterimSys.DismissAlertDialog(ADDialog_PlayGoogleServisi);
		AkorDefterimSys.DismissAlertDialog(ADDialog_SistemDurum);
		AkorDefterimSys.DismissAlertDialog(ADDialog_Guncelleme);
		AkorDefterimSys.DismissAlertDialog(ADDialog_InternetErisimSorunu);
		AkorDefterimSys.DismissAlertDialog(ADDialog_HesapDurumu);

		//if (mGoogleLoginApiClient.isConnected())
		//mGoogleLoginApiClient.disconnect();
	}

	/**@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
			case Activity.RESULT_OK: //Second activity normal olarak sonlanirsa RESULT_OK degeri döner.
				if (requestCode == AkorDefterimSys.RC_GOOGLE_LOGIN) {
					GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

					if (result.isSuccess()) {
						GoogleSignInAccount acct = result.getSignInAccount();

						//String ResimURL = (acct.getPhotoUrl() == null ? "":acct.getPhotoUrl().toString());

						AkorDefterimSys.HesapGirisYap("Google", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, acct.getEmail(), "", acct.getId(), acct.getDisplayName());
					}
				}

				break;
			case Activity.RESULT_CANCELED: //Second activity beklendmedik sekilde kapanirsa(Mesela cihazdaki back buttonuna tikalnirsa) RESULT_CANCELED degeri doner.
				//Toast.makeText(activity, "Beklenmedik sekilde second activity sonlandi", Toast.LENGTH_SHORT).show();

				break;
			default:

				break;
		}
	}*/

	@Override
	public void AsyncTaskReturnValue(String Sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(Sonuc);

			switch (JSONSonuc.getString("Islem")) {
				case "SistemDurumKontrol":
					// PDSistemKontrol Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDSistemKontrol);

					if(JSONSonuc.getBoolean("Durum")) { // Sistem çalışıyor ise
						PDGuncellemeKontrol = AkorDefterimSys.CustomProgressDialog(getString(R.string.guncellemeler_denetleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
						PDGuncellemeKontrol.show();

						AkorDefterimSys.YeniGuncellemeKontrol(); // Yeni güncelleme kontrolü yap
					} else { // Sistem kapalı ise
						ADDialog_SistemDurum = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								JSONSonuc.getString("Baslik"),
								JSONSonuc.getString("Icerik"),
								activity.getString(R.string.tamam));
						ADDialog_SistemDurum.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_SistemDurum.show();

						ADDialog_SistemDurum.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_SistemDurum.dismiss();
								Process.killProcess(Process.myPid());
							}
						});
					}

					break;
				case "Guncelleme":
					// PDGuncellemeKontrol Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDGuncellemeKontrol);

					// Durum => false (Eğer güncelleme yok ise false dönüyor)
					if(JSONSonuc.getBoolean("Durum")) { // Güncelleme var ise
						ADDialog_Guncelleme = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
								activity.getString(R.string.yeni_guncelleme),
								activity.getString(R.string.yeni_guncelleme_icerik, JSONSonuc.getString("GecerliVersiyonAdi"), JSONSonuc.getString("YeniVersiyonAdi")),
								activity.getString(R.string.cevrimdisi_devam_et),
								activity.getString(R.string.guncelle),
								activity.getString(R.string.uygulamadan_cik));
						ADDialog_Guncelleme.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
						ADDialog_Guncelleme.show();

						// Çevrimdışı devam et
						ADDialog_Guncelleme.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_Guncelleme.dismiss();

								AkorDefterimSys.HesapPrefSifirla();

								Intent myIntent = new Intent(activity, Ana.class);
								myIntent.putExtra("Islem", "");
								startActivity(myIntent);
								finish();
							}
						});

						// Güncelle
						ADDialog_Guncelleme.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_Guncelleme.dismiss();
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
								Process.killProcess(Process.myPid());
							}
						});

						// Uygulamadan çık
						ADDialog_Guncelleme.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ADDialog_Guncelleme.dismiss();
								Process.killProcess(Process.myPid());
							}
						});
					} else { // Güncelleme yok ise
						if (AkorDefterimSys.PrefAyarlar().getString("prefHesapID", "").equals("")) {
							AkorDefterimSys.HesapPrefSifirla();

							AkorDefterimSys.EkranGetir(new Intent(activity, GirisEkran.class), "Normal");
							finish();
						} else {
							PDGirisYap = AkorDefterimSys.CustomProgressDialog(getString(R.string.giris_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
							PDGirisYap.show();

							AkorDefterimSys.HesapGirisYap("Normal", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, AkorDefterimSys.PrefAyarlar().getString("prefEPosta", ""), AkorDefterimSys.PrefAyarlar().getString("prefParolaSHA1", ""),"", "");
						}
					}

					break;
				case "HesapGirisYap":
					// PDGirisYap Progress Dialog'u kapattık
					AkorDefterimSys.DismissProgressDialog(PDGirisYap);

					if(JSONSonuc.getBoolean("Sonuc")) {
						sharedPrefEditor = AkorDefterimSys.PrefAyarlar().edit();
						sharedPrefEditor.putString("prefHesapID", JSONSonuc.getString("HesapID"));
						sharedPrefEditor.putString("prefEPosta", JSONSonuc.getString("HesapEPosta"));
						sharedPrefEditor.putString("prefParolaSHA1", JSONSonuc.getString("HesapParolaSHA1"));
                        sharedPrefEditor.apply();

						Intent mIntent = new Intent(activity, Ana.class);
						mIntent.putExtra("Islem", "");

						AkorDefterimSys.EkranGetir(mIntent, "Normal");

						finish();
					} else {
						AkorDefterimSys.HesapPrefSifirla();

						switch (JSONSonuc.getString("HesapDurum")) {
							case "Ban":
                                ADDialog_HesapDurumu = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
                                        getString(R.string.hesap_durumu),
                                        getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
                                        activity.getString(R.string.tamam));
                                ADDialog_HesapDurumu.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                ADDialog_HesapDurumu.show();

                                ADDialog_HesapDurumu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ADDialog_HesapDurumu.dismiss();
										AkorDefterimSys.EkranGetir(new Intent(activity, GirisEkran.class), "Normal");
										finish();
                                    }
                                });

								break;
							default:
								AkorDefterimSys.EkranGetir(new Intent(activity, GirisEkran.class), "Normal");
								finish();
								break;
						}
					}

					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*private void GoogleAPIInit() {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleLoginApiClient = new GoogleApiClient.Builder(this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
	}

    @SuppressLint("HardwareIds")
    private void FacebookLoginInit() {
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject JSONFacebookGelenVeri, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            try {
								PDGirisYap = AkorDefterimSys.CustomProgressDialog(getString(R.string.giris_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi);
								PDGirisYap.show();

								String HesapID = JSONFacebookGelenVeri.getString("id");
								String AdSoyad = JSONFacebookGelenVeri.getString("name");
								//final String ResimURL = new URL("https://graph.facebook.com/" + HesapID + "/picture?width=300&height=300").toString();
								String EPosta = JSONFacebookGelenVeri.getString("email");

								AkorDefterimSys.HesapGirisYap("Facebook", FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon, EPosta, "", HesapID, AdSoyad);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
				AkorDefterimSys.HesapPrefSifirla();
				AkorDefterimSys.EkranGetir(new Intent(activity, GirisEkran.class), "Normal");
				finish();
            }

            @Override
            public void onError(FacebookException error) {
				AkorDefterimSys.HesapPrefSifirla();
				AkorDefterimSys.EkranGetir(new Intent(activity, GirisEkran.class), "Normal");
				finish();
            }
        });
    }*/
}