package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfHata {
    @SerializedName("Hata")
    private Boolean Hata;
    @SerializedName("HataMesaj")
    private String HataMesaj;

    public Boolean getHata() {
        return Hata;
    }

    public String getHataMesaj() {
        return HataMesaj;
    }
}
