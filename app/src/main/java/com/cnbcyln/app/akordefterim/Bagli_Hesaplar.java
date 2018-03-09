package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cnbcyln.app.akordefterim.Interface.Interface_AsyncResponse;
import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class Bagli_Hesaplar extends AppCompatActivity implements Interface_AsyncResponse, OnClickListener {

    private Activity activity;
	private AkorDefterimSys AkorDefterimSys;
	SharedPreferences sharedPref;
	Typeface YaziFontu;
	AlertDialog ADDialog;
	ProgressDialog PDIslem;
    CallbackManager mFacebookCallbackManager;
    GoogleSignInClient mGoogleSignInClient;

	CoordinatorLayout coordinatorLayout;
	ImageButton btnGeri;
	TextView lblBaslik, lblBagliHesaplarAciklama, lblFacebook, lblGoogle;
	Switch SFacebook, SGoogle;

	String FirebaseToken = "", OSID = "", OSVersiyon = "", UygulamaVersiyon = "", BulunanGoogleID = "", BulunanFacebookID = "";
	Boolean SFacebookIlkDefamiDegisiyor = true, SGoogleIlkDefamiDegisiyor = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.activity_bagli_hesaplar);

        activity = this;
		AkorDefterimSys = AkorDefterimSys.getInstance();
        AkorDefterimSys.activity = activity;
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

        lblBagliHesaplarAciklama = findViewById(R.id.lblBagliHesaplarAciklama);
        lblBagliHesaplarAciklama.setTypeface(YaziFontu, Typeface.NORMAL);
        lblBagliHesaplarAciklama.setText(getString(R.string.bagli_hesaplar_aciklama, getString(R.string.uygulama_adi)));
		AkorDefterimSys.setTextViewHTML(lblBagliHesaplarAciklama);

        lblFacebook = findViewById(R.id.lblFacebook);
        lblFacebook.setTypeface(YaziFontu, Typeface.NORMAL);

        SFacebook = findViewById(R.id.SFacebook);
        SFacebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(SFacebookIlkDefamiDegisiyor) SFacebookIlkDefamiDegisiyor = false;
                else {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        btnGeri.setEnabled(false);
                        SFacebook.setEnabled(false);
                        SGoogle.setEnabled(false);

                        if(isChecked)
                            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
                        else {
                            LoginManager.getInstance().logOut();

                            if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
                                PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
                                PDIslem.show();
                            }

                            AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "-", "", FirebaseToken, OSID, OSVersiyon, "", "", "","", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");
                        }
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                }
            }
        });

        lblGoogle = findViewById(R.id.lblGoogle);
        lblGoogle.setTypeface(YaziFontu, Typeface.NORMAL);

        SGoogle = findViewById(R.id.SGoogle);
        SGoogle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(SGoogleIlkDefamiDegisiyor) SGoogleIlkDefamiDegisiyor = false;
                else {
                    if(AkorDefterimSys.InternetErisimKontrolu()) {
                        btnGeri.setEnabled(false);
                        SFacebook.setEnabled(false);
                        SGoogle.setEnabled(false);

                        if(isChecked) {
                            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                            startActivityForResult(signInIntent, AkorDefterimSys.RC_GOOGLE_LOGIN);
                        } else {
                            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
                                        PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
                                        PDIslem.show();
                                    }

                                    AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", "-", FirebaseToken, OSID, OSVersiyon, "", "", "","", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");
                                }
                            });
                        }
                    } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
                }
            }
        });
	}

    @Override
    protected void onStart() {
        super.onStart();

        AkorDefterimSys.activity = activity;

        AkorDefterimSys.SharePrefAyarlarınıUygula();

        FirebaseToken = FirebaseInstanceId.getInstance().getToken();
        OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);

        try {
            UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        FacebookLoginInit();
        GoogleAPIInit(false);

        if(!AkorDefterimSys.GirisYapildiMi()) AkorDefterimSys.CikisYap();
        else {
            if(AkorDefterimSys.InternetErisimKontrolu()) {
                btnGeri.setEnabled(false);

                if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
                    PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.bilgileriniz_yukleniyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
                    PDIslem.show();
                }

                AkorDefterimSys.HesapBilgiGetir(null, sharedPref.getString("prefHesapID",""), "", "", "", "", "HesapBilgiGetir");
            } else AkorDefterimSys.StandartSnackBarMsj(coordinatorLayout, getString(R.string.internet_baglantisi_saglanamadi));
        }
    }

	@Override
	public void onBackPressed() {
		AkorDefterimSys.KlavyeKapat();
		AkorDefterimSys.DismissAlertDialog(ADDialog);

		super.onBackPressed();
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case Activity.RESULT_OK: //Second activity normal olarak sonlanirsa RESULT_OK degeri döner.
                if (requestCode == AkorDefterimSys.RC_GOOGLE_LOGIN) {
                    try {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount acct = task.getResult(ApiException.class);

                        if (acct != null) {
                            //String personName = acct.getDisplayName();
                            //String personGivenName = acct.getGivenName();
                            //String personFamilyName = acct.getFamilyName();
                            //String personEmail = acct.getEmail();
                            BulunanGoogleID = acct.getId();
                            //Uri personPhoto = acct.getPhotoUrl();

                            if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
                                PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
                                PDIslem.show();
                            }

                            AkorDefterimSys.HesapBilgiGetir(null, "", "", "", "", BulunanGoogleID, "HesapBilgiGetir_GoogleID");
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case Activity.RESULT_CANCELED: //Second activity beklendmedik sekilde kapanirsa(Mesela cihazdaki back buttonuna tikalnirsa) RESULT_CANCELED degeri doner.
                //Toast.makeText(activity, "Beklenmedik sekilde second activity sonlandi", Toast.LENGTH_SHORT).show();

                break;
            default:

                break;
        }
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnGeri:
				AkorDefterimSys.EkranKapat();
				break;
		}
	}

	@Override
	public void AsyncTaskReturnValue(String sonuc) {
		try {
			JSONObject JSONSonuc = new JSONObject(sonuc);

			switch (JSONSonuc.getString("Islem")) {
                case "HesapBilgiGetir":
                    btnGeri.setEnabled(true);
                    SFacebook.setEnabled(true);
                    SGoogle.setEnabled(true);
                    AkorDefterimSys.DismissProgressDialog(PDIslem);

                    if(JSONSonuc.getBoolean("Sonuc")) {
                        if(JSONSonuc.getString("HesapDurum").equals("Ban")) { // Eğer hesap banlanmışsa
                            if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                                ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                        getString(R.string.hesap_durumu),
                                        getString(R.string.hesap_banlandi, JSONSonuc.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
                                        getString(R.string.tamam),
                                        "ADDialog_Kapat_CikisYap");
                                ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                                ADDialog.show();
                            }
                        } else {
                            if(JSONSonuc.getString("FacebookID").equals("-")) {
                                SFacebook.setChecked(false);
                                SFacebookIlkDefamiDegisiyor = false;
                            } else SFacebook.setChecked(true);

                            if(JSONSonuc.getString("GoogleID").equals("-")) {
                                SGoogle.setChecked(false);
                                SGoogleIlkDefamiDegisiyor = false;
                            } else SGoogle.setChecked(true);
                        }
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
                case "HesapBilgiGetir_GoogleID":
                    if(JSONSonuc.getBoolean("Sonuc")) {
                        SGoogle.setChecked(false);

                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.hesap_durumu),
                                    getString(R.string.google_hesabi_bagli_durumda),
                                    getString(R.string.tamam),
                                    "ADDialog_Kapat");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                    } else AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), "", BulunanGoogleID, FirebaseToken, OSID, OSVersiyon, "", "", "","", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");

                    break;
                case "HesapBilgiGetir_FacebookID":
                    if(JSONSonuc.getBoolean("Sonuc")) {
                        SFacebook.setChecked(false);

                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.hesap_durumu),
                                    getString(R.string.facebook_hesabi_bagli_durumda),
                                    getString(R.string.tamam),
                                    "ADDialog_Kapat");
                            ADDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog.show();
                        }
                    } else AkorDefterimSys.HesapBilgiGuncelle(sharedPref.getString("prefHesapID",""), BulunanFacebookID, "", FirebaseToken, OSID, OSVersiyon, "", "", "","", "", "", "", "", "", UygulamaVersiyon, "HesapBilgiGuncelle");

                    break;
                case "HesapBilgiGuncelle":
                    btnGeri.setEnabled(true);
                    SFacebook.setEnabled(true);
                    SGoogle.setEnabled(true);
                    AkorDefterimSys.DismissProgressDialog(PDIslem);

                    if(JSONSonuc.getBoolean("Sonuc")) {

                    } else {
                        if(!AkorDefterimSys.AlertDialogisShowing(ADDialog)) {
                            ADDialog = AkorDefterimSys.CustomAlertDialog(activity,
                                    getString(R.string.hata),
                                    getString(R.string.islem_yapilirken_bir_hata_olustu),
                                    getString(R.string.tamam),
                                    "ADDialog_Kapat_GeriGit");
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
                    AkorDefterimSys.EkranKapat();
                    break;
                case "ADDialog_Kapat_CikisYap":
                    AkorDefterimSys.DismissAlertDialog(ADDialog);
                    AkorDefterimSys.CikisYap();
                    break;
                case "PDIslem_Timeout":
                    AkorDefterimSys.EkranKapat();
                    break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    private void GoogleAPIInit(Boolean GirisYapisinMi) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //mGoogleLoginApiClient = new GoogleApiClient.Builder(activity)
                //.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                //.build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
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
                            Log.d("Hata", "Facebook girişi yapılırken hata oluştu..");
                        } else {
                            try {
                                BulunanFacebookID = JSONFacebookGelenVeri.getString("id");

                                if(!AkorDefterimSys.ProgressDialogisShowing(PDIslem)) {
                                    PDIslem = AkorDefterimSys.CustomProgressDialog(getString(R.string.islem_yapiliyor), false, AkorDefterimSys.ProgressBarTimeoutSuresi, "PDIslem_Timeout");
                                    PDIslem.show();
                                }

                                AkorDefterimSys.HesapBilgiGetir(null, "", "", "", BulunanFacebookID, "", "HesapBilgiGetir_FacebookID");
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
                btnGeri.setEnabled(true);
                SFacebook.setEnabled(true);
                SGoogle.setEnabled(true);

                SFacebookIlkDefamiDegisiyor = true;
                SFacebook.setChecked(false);
            }

            @Override
            public void onError(FacebookException error) {
                btnGeri.setEnabled(true);
                SFacebook.setEnabled(true);
                SGoogle.setEnabled(true);

                SFacebookIlkDefamiDegisiyor = true;
                SFacebook.setChecked(false);
            }
        });
    }
}