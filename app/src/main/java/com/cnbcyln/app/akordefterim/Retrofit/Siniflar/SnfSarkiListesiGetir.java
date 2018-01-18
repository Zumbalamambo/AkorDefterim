package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfSarkiListesiGetir {
    @SerializedName("SarkiListesi")
    private String SarkiListesi;

    public String getSarkiListesi() {
        return SarkiListesi;
    }
}