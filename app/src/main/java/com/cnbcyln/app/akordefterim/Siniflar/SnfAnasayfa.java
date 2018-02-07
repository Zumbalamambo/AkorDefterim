package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfAnasayfa implements Serializable {
	private static final long serialVersionUID = 1L;

	private int SanatciID;
	private Boolean SanatciResimVarMi;
	private String SanatciAdi;
	private int SonEklenenSarkiID;
	private String SonEklenenSarkiAdi;
	private int ToplamSarki;

	public SnfAnasayfa() {
		super();
	}

	public SnfAnasayfa(int SanatciID, Boolean SanatciResimVarMi, String SanatciAdi, int SonEklenenSarkiID, String SonEklenenSarkiAdi, int ToplamSarki) {
		super();
		this.SanatciID = SanatciID;
		this.SanatciResimVarMi = SanatciResimVarMi;
		this.SanatciAdi = SanatciAdi;
		this.SonEklenenSarkiID = SonEklenenSarkiID;
		this.SonEklenenSarkiAdi = SonEklenenSarkiAdi;
		this.ToplamSarki = ToplamSarki;
	}

	public int getSanatciID() {
		return SanatciID;
	}

	public void setSanatciID(int SanatciID) {
		this.SanatciID = SanatciID;
	}

	public Boolean getSanatciResimVarMi() {
		return SanatciResimVarMi;
	}

	public void setSanatciResimVarMi(Boolean SanatciResimVarMi) {
		this.SanatciResimVarMi = SanatciResimVarMi;
	}

	public String getSanatciAdi() {
		return SanatciAdi;
	}

	public void setSanatciAdi(String SanatciAdi) {
		this.SanatciAdi = SanatciAdi;
	}

	public int getSonEklenenSarkiID() {
		return SonEklenenSarkiID;
	}

	public void setSonEklenenSarkiID(int SonEklenenSarkiID) {
		this.SonEklenenSarkiID = SonEklenenSarkiID;
	}

	public String getSonEklenenSarkiAdi() {
		return SonEklenenSarkiAdi;
	}

	public void setSonEklenenSarkiAdi(String SonEklenenSarkiAdi) {
		this.SonEklenenSarkiAdi = SonEklenenSarkiAdi;
	}

	public int getToplamSarki() {
		return ToplamSarki;
	}

	public void setToplamSarki(int ToplamSarki) {
		this.ToplamSarki = ToplamSarki;
	}
}
