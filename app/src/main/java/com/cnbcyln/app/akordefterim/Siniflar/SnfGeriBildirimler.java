package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfGeriBildirimler implements Serializable{
	private static final long serialVersionUID = 1L;
    private int Id;
    private int HesapID;
	private String Konu;
    private String OlusturulmaTarihi;
    private String SonMesajTarihi;
    private int Durum;

	public SnfGeriBildirimler() {
		super();
	}

    public SnfGeriBildirimler(int Id, int HesapID, String Konu, String OlusturulmaTarihi, int Durum) {
    	super();
        this.Id = Id;
        this.HesapID = HesapID;
        this.Konu = Konu;
        this.OlusturulmaTarihi = OlusturulmaTarihi;
        this.Durum = Durum;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
		this.Id = Id;
	}

    public int getHesapID() {
        return HesapID;
    }

    public void setHesapID(int HesapID) {
        this.HesapID = HesapID;
    }

    public String getKonu() {
        return Konu;
    }

    public void setKonu(String Konu) {
        this.Konu = Konu;
    }

    public String getOlusturulmaTarihi() {
        return OlusturulmaTarihi;
    }

    public void setOlusturulmaTarihi(String OlusturulmaTarihi) {
        this.OlusturulmaTarihi = OlusturulmaTarihi;
    }

    public String getSonMesajTarihi() {
        return SonMesajTarihi;
    }

    public void setSonMesajTarihi(String SonMesajTarihi) {
        this.SonMesajTarihi = SonMesajTarihi;
    }

    public int getDurum() {
        return Durum;
    }

    public void setDurum(int Durum) {
        this.Durum = Durum;
    }
}
