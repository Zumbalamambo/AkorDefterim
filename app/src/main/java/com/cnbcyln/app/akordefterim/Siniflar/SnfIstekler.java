package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfIstekler implements Serializable {
	private static final long serialVersionUID = 1L;

	private int Id;
	private int SarkiID;
	private String SanatciAdi;
	private String SarkiAdi;
	private String ClientID;
	private String ClientAdSoyad;
	private String ClientIP;
	private String ClientNot;
	private String IstekTarihi;


	public SnfIstekler() {
		super();
	}

	public SnfIstekler(int Id, int SarkiID, String SanatciAdi, String SarkiAdi, String ClientID, String ClientAdSoyad, String ClientIP, String ClientNot, String IstekTarihi) {
		super();
		this.Id = Id;
		this.SarkiID = SarkiID;
		this.SanatciAdi = SanatciAdi;
		this.SarkiAdi = SarkiAdi;
		this.ClientID = ClientID;
		this.ClientAdSoyad = ClientAdSoyad;
		this.ClientIP = ClientIP;
		this.ClientNot = ClientNot;
		this.IstekTarihi = IstekTarihi;
	}

	public int getId() {
		return Id;
	}

	public void setId(int Id) {
		this.Id = Id;
	}
	
	public int getSarkiID() {
		return SarkiID;
	}
	
	public void setSarkiID(int SarkiID) {
		this.SarkiID = SarkiID;
	}

	public String getSanatciAdi() {
		return SanatciAdi;
	}

	public void setSanatciAdi(String SanatciAdi) {
		this.SanatciAdi = SanatciAdi;
	}

	public String getSarkiAdi() {
		return SarkiAdi;
	}

	public void setSarkiAdi(String SarkiAdi) {
		this.SarkiAdi = SarkiAdi;
	}

	public String getClientID() {
		return ClientID;
	}

	public void setClientID(String ClientID) {
		this.ClientID = ClientID;
	}

	public String getClientAdSoyad() {
		return ClientAdSoyad;
	}

	public void setClientAdSoyad(String ClientAdSoyad) {
		this.ClientAdSoyad = ClientAdSoyad;
	}

	public String getClientIP() {
		return ClientIP;
	}
	
	public void setClientIP(String ClientIP) {
		this.ClientIP = ClientIP;
	}
	
	public String getClientNot() {
		return ClientNot;
	}
	
	public void setClientNot(String ClientNot) {
		this.ClientNot = ClientNot;
	}

	public String getIstekTarihi() {
		return IstekTarihi;
	}

	public void setIstekTarihi(String IstekTarihi) {
		this.IstekTarihi = IstekTarihi;
	}
}
