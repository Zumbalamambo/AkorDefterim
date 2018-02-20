package com.cnbcyln.app.akordefterim.Retrofit.Interface;

import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfAnasayfaGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfDosyaBilgisiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapBilgiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapEkle;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfHesapGirisYap;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfIslemSonuc;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfKategoriListesiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfSarkiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfSarkiListesiGetir;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfSistemDurum;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfTarihSaat;
import com.cnbcyln.app.akordefterim.Retrofit.Siniflar.SnfTarzListesiGetir;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    /* **************************************
       ***                                ***
       ***         GENEL İŞLEMLER         ***
       ***                                ***
       **************************************/

    @GET("phpscriptleri/genel/tarihsaat.php")
    Call<SnfTarihSaat> TarihSaatGetir();

    @GET("phpscriptleri/genel/sistemdurum.php")
    Call<SnfSistemDurum> SistemDurumKontrol();

    @GET("phpscriptleri/mail/index.php")
    Call<SnfIslemSonuc> EPostaGonder(@Query("EPosta") String mEPosta,
                                     @Query("AdSoyad") String mAdSoyad,
                                     @Query("Baslik") String mBaslik,
                                     @Query("Icerik") String mIcerik);

    @GET("phpscriptleri/sms/index.php")
    Call<SnfIslemSonuc> SMSGonder(@Query("TelKodu") String mTelKodu,
                                  @Query("CepTelefon") String mCepTelefon,
                                  @Query("Mesaj") String mMesaj);

    @GET("phpscriptleri/genel/anasayfa.php")
    Call<SnfAnasayfaGetir> AnasayfaGetir();

    @GET("phpscriptleri/genel/dosya_bilgisi_getir.php")
    Call<SnfDosyaBilgisiGetir> DosyaBilgisiGetir(@Query("DosyaDizin") String mDosyaDizin,
                                                 @Query("DosyaAdi") String mDosyaAdi);

    @GET("phpscriptleri/genel/firebase_mesaj_yolla.php")
    Call<SnfIslemSonuc> FirebaseMesajGonder(@Query("FirebaseToken") String mFirebaseToken,
                                            @Query("Icerik") String mIcerik);

    /* **************************************
       ***                                ***
       ***         HESAP İŞLEMLERİ        ***
       ***                                ***
       **************************************/

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
    Call<SnfHesapBilgiGetir> HesapBilgiGetir(@Query("HesapID") String mHesapID, @Query("TelKodu") String mTelKodu, @Query("EPostaKullaniciAdiTelefon") String mEPostaKullaniciAdiTelefon);

    @GET("phpscriptleri/hesapislemleri/hesap_bilgi_guncelle.php")
    Call<SnfIslemSonuc> HesapBilgiGuncelle(@Query("HesapID") String mHesapID,
                                           @Query("FacebookID") String mFacebookID,
                                           @Query("GoogleID") String mGoogleID,
                                           @Query("FirebaseToken") String mFirebaseToken,
                                           @Query("OSID") String mOSID,
                                           @Query("OSVersiyon") String mOSVersiyon,
                                           @Query("AdSoyad") String mAdSoyad,
                                           @Query("DogumTarih") String mDogumTarih,
                                           @Query("ResimURL") String mResimURL,
                                           @Query("EPosta") String mEPosta,
                                           @Query("Parola") String mParola,
                                           @Query("ParolaSHA1") String mParolaSHA1,
                                           @Query("KullaniciAdi") String mKullaniciAdi,
                                           @Query("TelKodu") String mTelKodu,
                                           @Query("CepTelefon") String mCepTelefon,
                                           @Query("UygulamaVersiyon") String mUygulamaVersiyon);

    /* **************************************
       ***                                ***
       ***     GERİ BİLDİRİM İŞLEMLERİ    ***
       ***                                ***
       **************************************/

    @GET("phpscriptleri/genel/geribildirim_ekle.php")
    Call<SnfIslemSonuc> GeriBildirimEkle(@Query("YenidenGeriBildirimGondermeSuresi") String mYenidenGeriBildirimGondermeSuresi,
                                         @Query("HesapID") String mHesapID,
                                         @Query("BildirimTipi") String mBildirimTipi,
                                         @Query("Icerik") String mIcerik,
                                         @Query("IPAdres") String mIPAdres);

    /* **************************************
       ***                                ***
       ***       REPERTUVAR İŞLEMLERİ     ***
       ***                                ***
       **************************************/

    @GET("phpscriptleri/repertuvarislemleri/kategori_listesi_getir.php")
    Call<SnfKategoriListesiGetir> KategoriListesiGetir(@Query("StrTumu") String mStrTumu);

    @GET("phpscriptleri/repertuvarislemleri/tarz_listesi_getir.php")
    Call<SnfTarzListesiGetir> TarzListesiGetir(@Query("StrTumu") String mStrTumu);

    @GET("phpscriptleri/repertuvarislemleri/sarki_listesi_getir.php")
    Call<SnfSarkiListesiGetir> SarkiListesiGetir(@Query("KategoriID") String mKategoriID,
                                                 @Query("TarzID") String mTarzID,
                                                 @Query("ListelemeTipi") String mListelemeTipi);

    @GET("phpscriptleri/repertuvarislemleri/sarki_getir.php")
    Call<SnfSarkiGetir> SarkiGetir(@Query("SarkiID") String mSarkiID);

    @GET("phpscriptleri/repertuvarislemleri/sarki_gonder.php")
    Call<SnfIslemSonuc> SarkiGonder(@Query("SanatciAdi") String mSanatciAdi,
                                    @Query("SarkiAdi") String mSarkiAdi,
                                    @Query("Icerik") String mIcerik,
                                    @Query("EkleyenID") String mEkleyenID);
}