package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfGenelMenu implements Serializable{
	private static final long serialVersionUID = 1L;
    private String GenelMenuSembolAdi;
	private String GenelMenuAdi;
    
	public SnfGenelMenu() {
		super();
	}
	
    public SnfGenelMenu(String GenelMenuSembolAdi, String GenelMenuAdi) {
    	super();
    	this.GenelMenuSembolAdi = GenelMenuSembolAdi;
        this.GenelMenuAdi = GenelMenuAdi;
    }
    
    public String getGenelMenuSembolAdi() {
        return GenelMenuSembolAdi;
    }
    
    public void setGenelMenuSembolAdi(String GenelMenuSembolAdi) {
		this.GenelMenuSembolAdi = GenelMenuSembolAdi;
	}

    public String getGenelMenuAdi() {
        return GenelMenuAdi;
    }

    public void setGenelMenuAdi(String GenelMenuAdi) {
        this.GenelMenuAdi = GenelMenuAdi;
    }

    public String toString() {
        return GenelMenuAdi;
    }
}
