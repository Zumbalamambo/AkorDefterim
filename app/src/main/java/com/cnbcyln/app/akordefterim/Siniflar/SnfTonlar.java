package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfTonlar implements Serializable {
	private static final long serialVersionUID = 1L;

	private String TonAdi;
	private Boolean Secim;

	public SnfTonlar() {
		super();
	}

	public SnfTonlar(String TonAdi, Boolean Secim) {
		super();
		this.TonAdi = TonAdi;
		this.Secim = Secim;
	}

	public String getTonAdi() {
		return TonAdi;
	}

	public void setTonAdi(String TonAdi) {
		this.TonAdi = TonAdi;
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
