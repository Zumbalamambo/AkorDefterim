package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfSarkilar implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int ListeID;
	private int KategoriID;
	private int TarzID;
	private String SanatciAdi;
	private String SarkiAdi;
	private String Icerik;
	private String VideoURL;
	private String EklenmeTarihi;
	private String DuzenlenmeTarihi;
	private int Siralama;
	private Boolean Secim = false;
	
	public SnfSarkilar() {
		super();
	}
	
	public SnfSarkilar(int id, String DepolamaAlani, int ListeID, int KategoriID, int TarzID, String SanatciAdi, String SarkiAdi, String Icerik, String VideoURL, String EklenmeTarihi, String DuzenlenmeTarihi, int Siralama, Boolean Secim) {
		super();
		this.id = id;
		this.ListeID = ListeID;
		this.KategoriID = KategoriID;
		this.TarzID = TarzID;
		this.SanatciAdi = SanatciAdi;
		this.SarkiAdi = SarkiAdi;
		this.Icerik = Icerik;
		this.VideoURL = VideoURL;
		this.EklenmeTarihi = EklenmeTarihi;
		this.DuzenlenmeTarihi = DuzenlenmeTarihi;
		this.Siralama = Siralama;
		this.Secim = Secim;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getListeID() {
		return ListeID;
	}

	public void setListeID(int ListeID) {
		this.ListeID = ListeID;
	}

	public int getKategoriID() {
		return KategoriID;
	}

	public void setKategoriID(int KategoriID) {
		this.KategoriID = KategoriID;
	}

	public int getTarzID() {
		return TarzID;
	}

	public void setTarzID(int TarzID) {
		this.TarzID = TarzID;
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

	public String getIcerik() {
		return Icerik;
	}

	public void setIcerik(String Icerik) {
		this.Icerik = Icerik;
	}

	public String getVideoURL() {
		return VideoURL;
	}

	public void setVideoURL(String VideoURL) {
		this.VideoURL = VideoURL;
	}

	public String getEklenmeTarihi() {
		return EklenmeTarihi;
	}

	public void setEklenmeTarihi(String EklenmeTarihi) {
		this.EklenmeTarihi = EklenmeTarihi;
	}

	public String getDuzenlenmeTarihi() {
		return DuzenlenmeTarihi;
	}

	public void setDuzenlenmeTarihi(String DuzenlenmeTarihi) {
		this.DuzenlenmeTarihi = DuzenlenmeTarihi;
	}

	public int getSiralama() {
		return Siralama;
	}

	public void setSiralama(int Siralama) {
		this.Siralama = Siralama;
	}

	public Boolean getSecim() {
		return Secim;
	}

	public void setSecim(Boolean Secim) {
		this.Secim = Secim;
	}

	public void SecimDegistir() {
		 setSecim(!Secim);
	}
}
