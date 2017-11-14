package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfUlkeKodlari implements Serializable {
    private static final long serialVersionUID = 1L;
    private String UlkeAdi;
    private String UlkeKodu;

    public SnfUlkeKodlari() {
        super();
    }

    public SnfUlkeKodlari(String UlkeAdi, String UlkeKodu) {
        super();
        this.UlkeAdi = UlkeAdi;
        this.UlkeKodu = UlkeKodu;
    }

    public String getUlkeAdi() {
        return UlkeAdi;
    }

    public void setUlkeAdi(String UlkeAdi) {
        this.UlkeAdi = UlkeAdi;
    }

    public String getUlkeKodu() {
        return UlkeKodu;
    }

    public void setUlkeKodu(String UlkeKodu) {
        this.UlkeKodu = UlkeKodu;
    }
}
