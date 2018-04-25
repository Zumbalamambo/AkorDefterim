package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfSarkiGetir extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("Icerik")
    private String Icerik;
    @SerializedName("VideoURL")
    private String VideoURL;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public String getIcerik() {
        return Icerik;
    }

    public String getVideoURL() {
        return VideoURL;
    }
}