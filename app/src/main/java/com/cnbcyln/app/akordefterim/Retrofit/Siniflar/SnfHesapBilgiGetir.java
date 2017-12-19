package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfHesapBilgiGetir extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("HesapID")
    private String HesapID;
    @SerializedName("FacebookID")
    private String FacebookID;
    @SerializedName("GoogleID")
    private String GoogleID;
    @SerializedName("AdSoyad")
    private String AdSoyad;
    @SerializedName("DogumTarih")
    private String DogumTarih;
    @SerializedName("ResimURL")
    private String ResimURL;
    @SerializedName("EPosta")
    private String EPosta;
    @SerializedName("Parola")
    private String Parola;
    @SerializedName("ParolaSHA1")
    private String ParolaSHA1;
    @SerializedName("KullaniciAdi")
    private String KullaniciAdi;
    @SerializedName("TelKodu")
    private String TelKodu;
    @SerializedName("CepTelefon")
    private String CepTelefon;
    @SerializedName("KayitTarih")
    private String KayitTarih;
    @SerializedName("SonOturumTarih")
    private String SonOturumTarih;
    @SerializedName("HesapDurum")
    private String HesapDurum;
    @SerializedName("HesapDurumBilgi")
    private String HesapDurumBilgi;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public String getHesapID() {
        return HesapID;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public String getGoogleID() {
        return GoogleID;
    }

    public String getAdSoyad() {
        return AdSoyad;
    }

    public String getDogumTarih() {
        return DogumTarih;
    }

    public String getResimURL() {
        return ResimURL;
    }

    public String getEPosta() {
        return EPosta;
    }

    public String getParola() {
        return Parola;
    }

    public String getParolaSHA1() {
        return ParolaSHA1;
    }

    public String getKullaniciAdi() {
        return KullaniciAdi;
    }

    public String getTelKodu() {
        return TelKodu;
    }

    public String getCepTelefon() {
        return CepTelefon;
    }

    public String getKayitTarih() {
        return KayitTarih;
    }

    public String getSonOturumTarih() {
        return SonOturumTarih;
    }

    public String getHesapDurum() {
        return HesapDurum;
    }

    public String getHesapDurumBilgi() {
        return HesapDurumBilgi;
    }
}