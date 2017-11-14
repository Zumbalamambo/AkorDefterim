package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfAnasayfa implements Serializable {
	private static final long serialVersionUID = 1L;

	private int SanatciID;
	private String SanatciAdi;
	private Boolean SanatciResimVarMi;
	private int ToplamSarki;

	public SnfAnasayfa() {
		super();
	}

	public SnfAnasayfa(int SanatciID, String SanatciAdi, Boolean SanatciResimVarMi, int ToplamSarki) {
		super();
		this.SanatciID = SanatciID;
		this.SanatciAdi = SanatciAdi;
		this.SanatciResimVarMi = SanatciResimVarMi;
		this.ToplamSarki = ToplamSarki;
	}

	public int getSanatciID() {
		return SanatciID;
	}

	public void setSanatciID(int SanatciID) {
		this.SanatciID = SanatciID;
	}

	public String getSanatciAdi() {
		return SanatciAdi;
	}

	public void setSanatciAdi(String SanatciAdi) {
		this.SanatciAdi = SanatciAdi;
	}

	public Boolean getSanatciResimVarMi() {
		return SanatciResimVarMi;
	}

	public void setSanatciResimVarMi(Boolean SanatciResimVarMi) {
		this.SanatciResimVarMi = SanatciResimVarMi;
	}

	public int getToplamSarki() {
		return ToplamSarki;
	}

	public void setToplamSarki(int ToplamSarki) {
		this.ToplamSarki = ToplamSarki;
	}
}
