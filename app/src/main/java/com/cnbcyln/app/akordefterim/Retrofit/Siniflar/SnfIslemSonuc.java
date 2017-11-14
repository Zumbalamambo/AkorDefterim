package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfIslemSonuc extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }
}