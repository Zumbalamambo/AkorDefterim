package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfHesapGirisYap extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("HesapID")
    private String HesapID;
    @SerializedName("HesapEPosta")
    private String HesapEPosta;
    @SerializedName("HesapParolaSHA1")
    private String HesapParolaSHA1;
    @SerializedName("HesapDurum")
    private String HesapDurum;
    @SerializedName("HesapDurumBilgi")
    private String HesapDurumBilgi;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public String getHesapID() {
        return HesapID;
    }

    public String getHesapEPosta() {
        return HesapEPosta;
    }

    public String getHesapParolaSHA1() {
        return HesapParolaSHA1;
    }

    public String getHesapDurum() {
        return HesapDurum;
    }

    public String getHesapDurumBilgi() {
        return HesapDurumBilgi;
    }
}