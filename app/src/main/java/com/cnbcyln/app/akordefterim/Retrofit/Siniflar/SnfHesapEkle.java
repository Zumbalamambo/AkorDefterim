package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfHesapEkle extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("EklenenHesapID")
    private int EklenenHesapID;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public int getEklenenHesapID() {
        return EklenenHesapID;
    }
}