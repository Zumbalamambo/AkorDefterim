package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfKategoriler implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String KategoriAdi;
	
	public SnfKategoriler() {
		super();
	}
	
	public SnfKategoriler(int id, String KategoriAdi) {
		super();
		this.id = id;
		this.KategoriAdi = KategoriAdi;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getKategoriAdi() {
		return KategoriAdi;
	}
	
	public void setKategoriAdi(String KategoriAdi) {
		this.KategoriAdi = KategoriAdi;
	}
}