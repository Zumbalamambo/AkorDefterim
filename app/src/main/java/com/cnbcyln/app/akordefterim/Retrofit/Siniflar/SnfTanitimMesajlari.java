package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfTanitimMesajlari {
    @SerializedName("MesajListesi")
    private String MesajListesi;

    public String getMesajListesi() {
        return MesajListesi;
    }
}
