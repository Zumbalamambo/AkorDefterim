package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfTarzListesiGetir {
    @SerializedName("TarzListesi")
    private String TarzListesi;

    public String getTarzListesi() {
        return TarzListesi;
    }
}