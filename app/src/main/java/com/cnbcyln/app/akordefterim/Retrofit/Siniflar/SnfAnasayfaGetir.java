package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfAnasayfaGetir {
    @SerializedName("SarkiListesi")
    private String SarkiListesi;

    public String getSarkiListesi() {
        return SarkiListesi;
    }
}