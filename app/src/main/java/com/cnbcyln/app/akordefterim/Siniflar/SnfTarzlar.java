package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfTarzlar implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String TarzAdi;
	
	public SnfTarzlar() {
		super();
	}
	
	public SnfTarzlar(String TarzAdi) {
		super();
		this.TarzAdi = TarzAdi;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTarzAdi() {
		return TarzAdi;
	}
	
	public void setTarzAdi(String TarzAdi) {
		this.TarzAdi = TarzAdi;
	}
}