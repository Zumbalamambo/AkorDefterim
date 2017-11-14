package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfListeler implements Serializable{
	private static final long serialVersionUID = 1L;
    private int Id;
	private String ListeAdi;
    
	public SnfListeler() {
		super();
	}
	
    public SnfListeler(String ListeAdi) {
    	super();
        this.ListeAdi = ListeAdi;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
		this.Id = Id;
	}

    public String getListeAdi() {
        return ListeAdi;
    }

    public void setListeAdi(String ListeAdi) {
        this.ListeAdi = ListeAdi;
    }
}
