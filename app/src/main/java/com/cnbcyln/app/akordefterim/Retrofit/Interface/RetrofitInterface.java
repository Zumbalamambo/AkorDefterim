package com.cnbcyln.app.akordefterim.Retrofit.Interface;

import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapBilgiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapEkle;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapGirisYap;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfIslemSonuc;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfSistemDurum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("phpscriptleri/sistemdurum.php")
    Call<SnfSistemDurum> SistemDurumKontrol();

    /*@POST("phpscriptleri/mail/index.php")
    Call<SnfIslemSonuc> EPostaGonder (@Body EPosta mEPosta);*/

    @GET("phpscriptleri/mail/index.php")
    Call<SnfIslemSonuc> EPostaGonder(@Query("EPosta") String mEPosta, @Query("AdSoyad") String mAdSoyad, @Query("Baslik") String mBaslik, @Query("Icerik") String mIcerik);

    //@GET("phpscriptleri/hesapislemleri/hesap_kontrol.php")
    //Call<SnfHesapKontrol> HesapKontrol(@Query("EPosta") String mEPosta);

    //@GET("phpscriptleri/hesapislemleri/kullaniciadi_kontrol.php")
    //Call<SnfIslemSonuc> KullaniciAdiKontrol(@Query("KullaniciAdi") String mKullaniciAdi);

    @GET("phpscriptleri/hesapislemleri/hesap_ekle.php")
    Call<SnfHesapEkle> HesapEkle(@Query("FirebaseToken") String mFirebaseToken,
                                 @Query("OSID") String mOSID,
                                 @Query("OSVersiyon") String mOSVersiyon,
                                 @Query("AdSoyad") String mAdSoyad,
                                 @Query("DogumTarih") String mDogumTarih,
                                 @Query("ProfilResmiVarmi") Boolean mProfilResmiVarmi,
                                 @Query("EPosta") String mEPosta,
                                 @Query("Parola") String mParola,
                                 @Query("ParolaSHA1") String mParolaSHA1,
                                 @Query("KullaniciAdi") String mKullaniciAdi,
                                 @Query("UygulamaVersiyon") String mUygulamaVersiyon);

    @GET("phpscriptleri/hesapislemleri/hesap_giris_yap.php")
    Call<SnfHesapGirisYap> HesapGirisYap(@Query("GirisTipi") String mGirisTipi,
                                         @Query("FirebaseToken") String mFirebaseToken,
                                         @Query("OSID") String mOSID,
                                         @Query("OSVersiyon") String mOSVersiyon,
                                         @Query("UygulamaVersiyon") String mUygulamaVersiyon,
                                         @Query("EPostaVeyaKullaniciAdi") String mEPostaVeyaKullaniciAdi,
                                         @Query("ParolaSHA1") String mParolaSHA1,
                                         @Query("SosyalHesapID") String mSosyalHesapID,
                                         @Query("AdSoyad") String mAdSoyad);

    @GET("phpscriptleri/hesapislemleri/hesap_bilgi_getir.php")
    Call<SnfHesapBilgiGetir> HesapBilgiGetir(@Query("EPostaKullaniciAdi") String mEPostaKullaniciAdi);

    @GET("phpscriptleri/hesapislemleri/hesap_bilgi_guncelle.php")
    Call<SnfIslemSonuc> HesapBilgiGuncelle(@Query("FirebaseToken") String mFirebaseToken,
                                           @Query("OSID") String mOSID,
                                           @Query("OSVersiyon") String mOSVersiyon,
                                           @Query("AdSoyad") String mAdSoyad,
                                           @Query("DogumTarih") String mDogumTarih,
                                           @Query("ProfilResmiVarmi") String mResimURL,
                                           @Query("EPosta") String mEPosta,
                                           @Query("EPostaOnay") String mEPostaOnay,
                                           @Query("Parola") String mParola,
                                           @Query("ParolaSHA1") String mParolaSHA1,
                                           @Query("KullaniciAdi") String mKullaniciAdi,
                                           @Query("UygulamaVersiyon") String mUygulamaVersiyon);

    @GET("phpscriptleri/hesapislemleri/hesap_parola_degistir.php")
    Call<SnfIslemSonuc> HesapParolaDegistir(@Query("EPosta") String mEPosta,
                                            @Query("Parola") String mParola,
                                            @Query("ParolaSHA1") String mParolaSHA1);
}