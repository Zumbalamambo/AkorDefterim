package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfAkorlar implements Serializable {
	private static final long serialVersionUID = 1L;

	private String AkorAdi;
	private String Dizi;
	private Boolean SecimBG;
	private Boolean SecimYazi;

	public SnfAkorlar() {
		super();
	}

	public SnfAkorlar(String AkorAdi, String Dizi, Boolean SecimBG, Boolean SecimYazi) {
		super();
		this.AkorAdi = AkorAdi;
		this.Dizi = Dizi;
		this.SecimBG = SecimBG;
		this.SecimYazi = SecimYazi;
	}

	public String getAkorAdi() {
		return AkorAdi;
	}

	public void setAkorAdi(String AkorAdi) {
		this.AkorAdi = AkorAdi;
	}

	public String getDizi() {
		return Dizi;
	}

	public void setDizi(String Dizi) {
		this.Dizi = Dizi;
	}

	public Boolean getSecimBG() {
		return SecimBG;
	}

	public void setSecimBG(Boolean SecimBG) {
		this.SecimBG = SecimBG;
	}

	public void SecimBGDegistir() {
		setSecimBG(!SecimBG);
	}

	public Boolean getSecimYazi() {
		return SecimYazi;
	}

	public void setSecimYazi(Boolean SecimYazi) {
		this.SecimYazi = SecimYazi;
	}

	public void SecimYaziDegistir() {
		setSecimYazi(!SecimYazi);
	}
}
