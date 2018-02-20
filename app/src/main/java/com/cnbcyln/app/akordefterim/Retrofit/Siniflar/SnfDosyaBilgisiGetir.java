package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfDosyaBilgisiGetir extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("LastModified")
    private String LastModified;
    @SerializedName("DosyaBoyutu")
    private String DosyaBoyutu;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public String getLastModified() {
        return LastModified;
    }

    public String getDosyaBoyutu() {
        return DosyaBoyutu;
    }
}