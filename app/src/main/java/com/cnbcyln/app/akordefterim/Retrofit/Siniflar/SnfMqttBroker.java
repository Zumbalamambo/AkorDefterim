package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfMqttBroker extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("Adres")
    private String Adres;
    @SerializedName("KullaniciAdi")
    private String KullaniciAdi;
    @SerializedName("Sifre")
    private String Sifre;
    @SerializedName("Qos")
    private int Qos;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public String getAdres() {
        return Adres;
    }

    public String getKullaniciAdi() {
        return KullaniciAdi;
    }

    public String getSifre() {
        return Sifre;
    }

    public int getQos() {
        return Qos;
    }
}
