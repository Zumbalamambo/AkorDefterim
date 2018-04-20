package com.cnbcyln.app.akordefterim.Retrofit.Siniflar;

import com.google.gson.annotations.SerializedName;

public class SnfHesapGirisYap extends SnfHata {
    @SerializedName("Sonuc")
    private Boolean Sonuc;
    @SerializedName("Aciklama")
    private String Aciklama;
    @SerializedName("HesapID")
    private String HesapID;
    @SerializedName("HesapFirebaseToken")
    private String HesapFirebaseToken;
    @SerializedName("HesapEPosta")
    private String HesapEPosta;
    @SerializedName("HesapParolaSHA1")
    private String HesapParolaSHA1;
    @SerializedName("HesapDurum")
    private String HesapDurum;
    @SerializedName("HesapDurumBilgi")
    private String HesapDurumBilgi;
    @SerializedName("HesapDurumTarihSaat")
    private String HesapDurumTarihSaat;

    public Boolean getSonuc() {
        return Sonuc;
    }

    public String getAciklama() {
        return Aciklama;
    }

    public String getHesapID() {
        return HesapID;
    }

    public String getHesapFirebaseToken() {
        return HesapFirebaseToken;
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

    public String getHesapDurumTarihSaat() {
        return HesapDurumTarihSaat;
    }
}