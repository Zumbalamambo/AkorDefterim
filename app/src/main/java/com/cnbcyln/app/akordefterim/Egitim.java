package com.cnbcyln.app.akordefterim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.*;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Egitim extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private AkorDefterimSys AkorDefterimSys;
    InputMethodManager imm;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] sayfalar;
    private Button btnSonraki, btnGec;
    ProgressDialog PDDialog_Giris;
    AlertDialog ADDialog_PlayGoogleServisi, ADDialog_Giris;

    private GoogleApiClient mGoogleLoginApiClient;

    CallbackManager callbackManager;

    Bundle Ekstra = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_egitim);

        activity = this;
        AkorDefterimSys = new AkorDefterimSys(activity);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // İstenildiği zaman klavyeyi gizlemeye yarayan kod tanımlayıcısı
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // Klavyeyi gizleyen method
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Ekranın yalnızca portrait (Dikey) olarak çalışmasını ayarlıyoruz

        // Notification Bar'ı transparan yapıyoruz
        AkorDefterimSys.TransparanNotifyBar();

        sharedPref = activity.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

        // Ekran ışığını eğer prefEkranIsigiAydinligi değeri ayarlanmamışsa en parlak olan 255'e ayarlıyoruz. Aksi halde ayar ne ise o ayarlanıyor..
        WindowManager.LayoutParams layoutpars = getWindow().getAttributes();
        //Set the brightness of this window
        layoutpars.screenBrightness = sharedPref.getInt("prefEkranIsigiAydinligi", 255) / (float) 255;
        //Apply attribute changes to this window
        getWindow().setAttributes(layoutpars);

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);

        btnSonraki = (Button) findViewById(R.id.btnSonraki);
        btnSonraki.setOnClickListener(this);

        btnGec = (Button) findViewById(R.id.btnGec);
        btnGec.setOnClickListener(this);

        sayfalar = new int[]{
                R.layout.activity_egitim_sayfa1,
                R.layout.activity_egitim_sayfa2,
                R.layout.activity_egitim_sayfa3,
                R.layout.activity_egitim_sayfa4,
                R.layout.activity_egitim_sayfa5,
                R.layout.activity_egitim_sayfa6,
                R.layout.activity_egitim_sayfa7,
                R.layout.activity_egitim_sayfa8,
                R.layout.activity_egitim_sayfa9,
                R.layout.activity_egitim_sayfa10,
                R.layout.activity_egitim_sayfa11};

        // adding bottom dots
        addBottomDots(0);

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        Ekstra = getIntent().getExtras();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (AkorDefterimSys.checkPlayServices(activity)) GoogleAPIInit();
        else {
            ADDialog_PlayGoogleServisi = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
                    getString(R.string.google_play_servis_baslik),
                    getString(R.string.google_play_servis_hata1),
                    activity.getString(R.string.tamam));
            ADDialog_PlayGoogleServisi.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            ADDialog_PlayGoogleServisi.show();

            ADDialog_PlayGoogleServisi.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ADDialog_PlayGoogleServisi.cancel();
                    Process.killProcess(Process.myPid());
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AkorDefterimSys.DismissProgressDialog(PDDialog_Giris);
        AkorDefterimSys.DismissAlertDialog(ADDialog_Giris);
    }

    private void GoogleAPIInit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleLoginApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[sayfalar.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    @SuppressWarnings("ConstantConditions")
    private void EgitimKapat() {
        if(Ekstra != null && Ekstra.getString("GelinenEkran").equals("AnaEkran")) {
            finish();
        } else {
            if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Normal")) {
                if (AkorDefterimSys.InternetErisimKontrolu()) //İnternet kontrolü yap
                    if (sharedPref.getString("prefHesapEmailKullaniciAdi", "").equals("") && sharedPref.getString("prefHesapSifreSha1", "").equals("")) {
                        AkorDefterimSys.HesapPrefSifirla();

                        startActivity(new Intent(activity, Giris.class));
                        finish();
                    } else new GirisYap().execute("Normal", "", "", "", sharedPref.getString("prefHesapEmailKullaniciAdi", ""), sharedPref.getString("prefHesapSifreSha1", ""));
                else {
                    AkorDefterimSys.HesapPrefSifirla();

                    startActivity(new Intent(activity, Giris.class));
                    finish();
                }
            } else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Google")) {
                if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
                    if (mGoogleLoginApiClient != null)
                        mGoogleLoginApiClient.connect();

                    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleLoginApiClient);
                    if (opr.isDone()) {
                        // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                        // and the GoogleSignInResult will be available instantly.
                        GoogleSignInResult result = opr.get();

                        if (result.isSuccess()) {
                            // Signed in successfully, show authenticated UI.
                            GoogleSignInAccount acct = result.getSignInAccount();
                            //AkorDefterimSys.ToastMsj(activity, acct.getDisplayName(), Toast.LENGTH_SHORT);

                            assert acct != null;
                            new GirisYap().execute("Google", acct.getId(), (acct.getPhotoUrl() == null ? "":acct.getPhotoUrl().toString()), acct.getDisplayName(), acct.getEmail(), "");
                        } else {
                            AkorDefterimSys.HesapPrefSifirla();

                            startActivity(new Intent(activity, Giris.class));
                            finish();
                        }
                    } else {
                        // If the user has not previously signed in on this device or the sign-in has expired,
                        // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                        // single sign-on will occur in this branch.
                        //showProgressDialog();
                        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                            @Override
                            public void onResult(GoogleSignInResult result) {
                                //hideProgressDialog();

                                if (result.isSuccess()) {
                                    // Signed in successfully, show authenticated UI.
                                    GoogleSignInAccount acct = result.getSignInAccount();
                                    //AkorDefterimSys.ToastMsj(activity, acct.getDisplayName(), Toast.LENGTH_SHORT);

                                    assert acct != null;
                                    new GirisYap().execute("Google", acct.getId(), (acct.getPhotoUrl() == null ? "":acct.getPhotoUrl().toString()), acct.getDisplayName(), acct.getEmail(), "");
                                } else {
                                    AkorDefterimSys.HesapPrefSifirla();

                                    startActivity(new Intent(activity, Giris.class));
                                    finish();
                                }
                            }
                        });
                    }
                } else {
                    AkorDefterimSys.HesapPrefSifirla();

                    startActivity(new Intent(activity, Giris.class));
                    finish();
                }
            } else if(sharedPref.getString("prefOturumTipi", "Cevrimdisi").equals("Facebook")) {
                if (AkorDefterimSys.InternetErisimKontrolu()) { //İnternet kontrolü yap
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                    LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject JSONFacebookGelenVeri, GraphResponse response) {
                                    if (response.getError() != null) {
                                        AkorDefterimSys.HesapPrefSifirla();

                                        startActivity(new Intent(activity, Giris.class));
                                        finish();
                                    } else {
                                        //Bundle bFacebookData = getFacebookData(object);

                                        try {
                                            //AkorDefterimSys.ToastMsj(activity, "ID: " + JSONFacebookGelenVeri.getString("id") + " Ad Soyad: " + JSONFacebookGelenVeri.getString("name") + " Email: " + JSONFacebookGelenVeri.getString("email"), Toast.LENGTH_SHORT);

                                            String FacebookID = JSONFacebookGelenVeri.getString("id");
                                            String FacebookAdSoyad = JSONFacebookGelenVeri.getString("name");
                                            String FacebookEmail = JSONFacebookGelenVeri.getString("email");

                                            URL profile_pic = new URL("https://graph.facebook.com/" + FacebookID + "/picture?width=300&height=300");
                                            String FacebookResimUrl = profile_pic.toString();

                                            new GirisYap().execute("Facebook", FacebookID, FacebookResimUrl, FacebookAdSoyad, FacebookEmail, "");
                                        } catch (MalformedURLException | JSONException e) {
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
                            // App code
                        }

                        @Override
                        public void onError(FacebookException error) {
                            // App code
                        }
                    });
                } else {
                    AkorDefterimSys.HesapPrefSifirla();

                    startActivity(new Intent(activity, Giris.class));
                    finish();
                }
            } else {
                startActivity(new Intent(activity, Giris.class));
                finish();
            }
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == sayfalar.length - 1) {
                // last page. make button text to GOT IT
                btnSonraki.setText(getString(R.string.basla));
                btnGec.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnSonraki.setText(getString(R.string.ileri));
                btnGec.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSonraki:
                int ToplamSayfaSayisi = sayfalar.length;
                int SayfaNo = getItem(+1); // Geçerli sayfa no

                // Son sayfada olup olmadığına bakıyoruz
                if (SayfaNo < ToplamSayfaSayisi) {
                    // Sonraki sayfa
                    viewPager.setCurrentItem(SayfaNo);
                } else {
                    EgitimKapat();
                }

                break;
            case R.id.btnGec:
                EgitimKapat();

                break;
        }
    }

    @SuppressLint("InflateParams")
    private class GirisYap extends AsyncTask<String, String, String> {
        String SosyalAg, ID, ResimUrl, AdSoyad, EmailKullaniciAdi, SifreSHA1, FirebaseToken, OSID, OSVersiyon, UygulamaVersiyon;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            PDDialog_Giris = new ProgressDialog(activity);
            PDDialog_Giris.setMessage(getString(R.string.islem_yapiliyor));
            PDDialog_Giris.setCancelable(false);

            if(!AkorDefterimSys.ProgressDialogisShowing(PDDialog_Giris))
                PDDialog_Giris.show();
        }

        @SuppressLint("HardwareIds")
        @Override
        protected String doInBackground(String... parametre) {
            SosyalAg = String.valueOf(parametre[0]);
            ID = String.valueOf(parametre[1]);
            ResimUrl = String.valueOf(parametre[2]);
            AdSoyad = String.valueOf(parametre[3]);
            EmailKullaniciAdi = String.valueOf(parametre[4]);
            SifreSHA1 = String.valueOf(parametre[5]);
            FirebaseToken = FirebaseInstanceId.getInstance().getToken();
            OSID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            OSVersiyon = AkorDefterimSys.AndroidSurumBilgisi(Build.VERSION.SDK_INT);
            String sonuc = null;

            try {
                UygulamaVersiyon = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("firebasetoken", FirebaseToken));
                nameValuePairs.add(new BasicNameValuePair("osid", OSID));
                nameValuePairs.add(new BasicNameValuePair("osversiyon", OSVersiyon));
                nameValuePairs.add(new BasicNameValuePair("uygulamaversiyon", UygulamaVersiyon));

                if(SosyalAg.equals("Google") || SosyalAg.equals("Facebook")) {
                    nameValuePairs.add(new BasicNameValuePair("id", ID));
                    nameValuePairs.add(new BasicNameValuePair("resimurl", ResimUrl));
                    nameValuePairs.add(new BasicNameValuePair("adsoyad", AdSoyad));
                    nameValuePairs.add(new BasicNameValuePair("email", EmailKullaniciAdi));
                } else if(SosyalAg.equals("Normal")) {
                    nameValuePairs.add(new BasicNameValuePair("emailkullaniciadi", EmailKullaniciAdi));
                    nameValuePairs.add(new BasicNameValuePair("sifresha1", SifreSHA1));
                }


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = null;

                switch (SosyalAg) {
                    case "Google":
                        httpPost = new HttpPost(AkorDefterimSys.PHPGoogleHesapEkle_GirisYap);
                        break;
                    case "Facebook":
                        httpPost = new HttpPost(AkorDefterimSys.PHPFacebookHesapEkle_GirisYap);
                        break;
                    case "Normal":
                        httpPost = new HttpPost(AkorDefterimSys.PHPHesapGirisYap);
                        break;
                }

                assert httpPost != null;
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null)
                    sb.append(line).append("\n");

                sonuc = sb.toString();
            } catch (PackageManager.NameNotFoundException | IOException e) {
                e.printStackTrace();
            }

            return sonuc;
        }

        @Override
        protected void onPostExecute(String Sonuc) {
            try {
                JSONObject JSONGelenVeri = new JSONObject(new JSONArray(Sonuc).getString(0));

                AkorDefterimSys.DismissProgressDialog(PDDialog_Giris);

                switch (JSONGelenVeri.getInt("sonuc")) {
                    case 1:
                        if (JSONGelenVeri.getString("HesapDurum").equals("Ban")) {
                            ADDialog_Giris = AkorDefterimSys.CustomAlertDialog(activity, R.mipmap.ic_launcher,
                                    getString(R.string.hesap_durumu),
                                    getString(R.string.hesap_banlandi, JSONGelenVeri.getString("HesapDurumBilgi"), getString(R.string.uygulama_yapimci_site)),
                                    activity.getString(R.string.tamam));
                            ADDialog_Giris.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            ADDialog_Giris.show();

                            ADDialog_Giris.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ADDialog_Giris.cancel();

                                    if(SosyalAg.equals("Google")) {
                                        Auth.GoogleSignInApi.signOut(mGoogleLoginApiClient).setResultCallback(new ResultCallback<com.google.android.gms.common.api.Status>() {
                                            @Override
                                            public void onResult(@NonNull com.google.android.gms.common.api.Status status) {

                                            }
                                        });
                                    } else if(SosyalAg.equals("Facebook")) LoginManager.getInstance().logOut();

                                    AkorDefterimSys.HesapPrefSifirla();

                                    startActivity(new Intent(activity, Giris.class));
                                    finish();
                                }
                            });
                        } else if (JSONGelenVeri.getString("HesapDurum").equals("Aktif")) {
                            AkorDefterimSys.HesapPrefSifirla();

                            sharedPrefEditor = sharedPref.edit();

                            switch (SosyalAg) {
                                case "Google":
                                    sharedPrefEditor.putString("prefFirebaseToken", FirebaseToken);
                                    sharedPrefEditor.putString("prefHesapID", ID);
                                    sharedPrefEditor.putString("prefHesapProfilResimUrl", ResimUrl);
                                    sharedPrefEditor.putString("prefHesapAdSoyad", AdSoyad);
                                    sharedPrefEditor.putString("prefHesapEmail", EmailKullaniciAdi);
                                    sharedPrefEditor.putString("prefOturumTipi", "Cevrimici" + SosyalAg + "Oturum");
                                    break;
                                case "Facebook":
                                    sharedPrefEditor.putString("prefFirebaseToken", FirebaseToken);
                                    sharedPrefEditor.putString("prefHesapID", ID);
                                    sharedPrefEditor.putString("prefHesapProfilResimUrl", ResimUrl);
                                    sharedPrefEditor.putString("prefHesapAdSoyad", AdSoyad);
                                    sharedPrefEditor.putString("prefHesapEmail", EmailKullaniciAdi);
                                    sharedPrefEditor.putString("prefOturumTipi", "Cevrimici" + SosyalAg + "Oturum");
                                    break;
                                case "Normal":
                                    sharedPrefEditor.putString("prefFirebaseToken", FirebaseToken);
                                    sharedPrefEditor.putString("prefHesapID", String.valueOf(JSONGelenVeri.getInt("id")));
                                    sharedPrefEditor.putString("prefHesapProfilResimUrl", AkorDefterimSys.CBCAPP_HttpsAdres + "/akordefterim/profil_img/" + String.valueOf(JSONGelenVeri.getInt("id")) + ".jpg");
                                    sharedPrefEditor.putString("prefHesapAdSoyad", JSONGelenVeri.getString("AdSoyad"));
                                    sharedPrefEditor.putString("prefHesapEmailKullaniciAdi", EmailKullaniciAdi);
                                    sharedPrefEditor.putString("prefHesapSifreSha1", SifreSHA1);
                                    sharedPrefEditor.putString("prefOturumTipi", "Normal");
                                    break;
                            }

                            sharedPrefEditor.apply();

                            if(!JSONGelenVeri.getString("FirebaseToken").equals(FirebaseToken))
                                AkorDefterimSys.FirebaseMesajGonder(JSONGelenVeri.getString("FirebaseToken"), "{\"Islem\":\"PushHesapCikis\", \"FirebaseToken\":\"" + JSONGelenVeri.getString("FirebaseToken") + "\"}");

                            Intent myIntent = new Intent(activity, AnaEkran.class);
                            myIntent.putExtra("Islem", "");
                            startActivity(myIntent);
                            finish();
                        }

                        break;
                    case 0:
                        if (JSONGelenVeri.getString("aciklama").equals("hesap bulunamadı")) {
                            AkorDefterimSys.HesapPrefSifirla();

                            startActivity(new Intent(activity, Giris.class));
                            finish();
                        } else if (JSONGelenVeri.getString("aciklama").equals("hatalı işlem")) {
                            AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
                            AkorDefterimSys.HesapPrefSifirla();

                            startActivity(new Intent(activity, Giris.class));
                            finish();
                        }

                        break;
                    default:
                        AkorDefterimSys.ToastMsj(activity, getString(R.string.islem_yapilirken_bir_hata_olustu), Toast.LENGTH_SHORT);
                        AkorDefterimSys.HesapPrefSifirla();

                        startActivity(new Intent(activity, Giris.class));
                        finish();
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
            AkorDefterimSys.DismissProgressDialog(PDDialog_Giris);
        }
    }

    private class MyViewPagerAdapter extends android.support.v4.view.PagerAdapter {
        private LayoutInflater layoutInflater;
        private Typeface YaziFontu;

        private MyViewPagerAdapter() {
            YaziFontu = new AkorDefterimSys(activity).FontGetir(activity, "anivers_regular");
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(sayfalar[position], container, false);

            switch (position) {
                case 0:
                    TextView lblegitim_1_baslik = (TextView) view.findViewById(R.id.lblegitim_1_baslik);
                    lblegitim_1_baslik.setTypeface(YaziFontu, Typeface.NORMAL);

                    TextView lblegitim_1_icerik = (TextView) view.findViewById(R.id.lblegitim_1_icerik);
                    lblegitim_1_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    lblegitim_1_icerik.setText(getString(R.string.egitim_1_icerik, getString(R.string.uygulama_adi)));
                    break;
                case 1:
                    TextView lblegitim_2_baslik = (TextView) view.findViewById(R.id.lblegitim_2_baslik);
                    lblegitim_2_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_2_icerik = (TextView) view.findViewById(R.id.lblegitim_2_icerik);
                    lblegitim_2_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 2:
                    TextView lblegitim_3_baslik = (TextView) view.findViewById(R.id.lblegitim_3_baslik);
                    lblegitim_3_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_3_icerik = (TextView) view.findViewById(R.id.lblegitim_3_icerik);
                    lblegitim_3_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 3:
                    TextView lblegitim_4_baslik = (TextView) view.findViewById(R.id.lblegitim_4_baslik);
                    lblegitim_4_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_4_icerik = (TextView) view.findViewById(R.id.lblegitim_4_icerik);
                    lblegitim_4_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 4:
                    TextView lblegitim_5_baslik = (TextView) view.findViewById(R.id.lblegitim_5_baslik);
                    lblegitim_5_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_5_icerik = (TextView) view.findViewById(R.id.lblegitim_5_icerik);
                    lblegitim_5_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 5:
                    TextView lblegitim_6_baslik = (TextView) view.findViewById(R.id.lblegitim_6_baslik);
                    lblegitim_6_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_6_icerik = (TextView) view.findViewById(R.id.lblegitim_6_icerik);
                    lblegitim_6_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 6:
                    TextView lblegitim_7_baslik = (TextView) view.findViewById(R.id.lblegitim_7_baslik);
                    lblegitim_7_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_7_icerik = (TextView) view.findViewById(R.id.lblegitim_7_icerik);
                    lblegitim_7_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 7:
                    TextView lblegitim_8_baslik = (TextView) view.findViewById(R.id.lblegitim_8_baslik);
                    lblegitim_8_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_8_icerik = (TextView) view.findViewById(R.id.lblegitim_8_icerik);
                    lblegitim_8_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 8:
                    TextView lblegitim_9_baslik = (TextView) view.findViewById(R.id.lblegitim_9_baslik);
                    lblegitim_9_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_9_icerik = (TextView) view.findViewById(R.id.lblegitim_9_icerik);
                    lblegitim_9_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 9:
                    TextView lblegitim_10_baslik = (TextView) view.findViewById(R.id.lblegitim_10_baslik);
                    lblegitim_10_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_10_icerik = (TextView) view.findViewById(R.id.lblegitim_10_icerik);
                    lblegitim_10_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
                case 10:
                    TextView lblegitim_11_baslik = (TextView) view.findViewById(R.id.lblegitim_11_baslik);
                    lblegitim_11_baslik.setTypeface(YaziFontu, Typeface.BOLD);

                    TextView lblegitim_11_icerik = (TextView) view.findViewById(R.id.lblegitim_11_icerik);
                    lblegitim_11_icerik.setTypeface(YaziFontu, Typeface.NORMAL);
                    break;
            }


            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return sayfalar.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}