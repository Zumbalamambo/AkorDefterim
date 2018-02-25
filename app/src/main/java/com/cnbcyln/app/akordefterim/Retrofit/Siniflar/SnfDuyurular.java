package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfDuyurular extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Durum")
    private Boolean Durum;
    @SerializedName("Baslik")
    private String Baslik;
    @SerializedName("Icerik")
    private String Icerik;
    @SerializedName("Tarih")
    private String Tarih;
    @SerializedName("Tip")
    private String Tip;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public Boolean getDurum() {
        return Durum;
    }

    public String getBaslik() {
        return Baslik;
    }

    public String getIcerik() {
        return Icerik;
    }

    public String getTarih() {
        return Tarih;
    }

    public String getTip() {
        return Tip;
    }
}
