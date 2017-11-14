package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfSistemDurum extends SnfHata {
    @SerializedName("Durum")
    private Boolean Durum;
    @SerializedName("Baslik")
    private String Baslik;
    @SerializedName("Icerik")
    private String Icerik;

    public Boolean getDurum() {
        return Durum;
    }

    public String getBaslik() {
        return Baslik;
    }

    public String getIcerik() {
        return Icerik;
    }
}
