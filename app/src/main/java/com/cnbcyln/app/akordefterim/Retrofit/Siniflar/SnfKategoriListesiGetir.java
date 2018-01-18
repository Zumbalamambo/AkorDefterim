package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfKategoriListesiGetir {
    @SerializedName("KategoriListesi")
    private String KategoriListesi;

    public String getKategoriListesi() {
        return KategoriListesi;
    }
}