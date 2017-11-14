package com.cnbcyln.app.akordefterim.Siniflar;

import java.io.Serializable;

public class SnfListelemeTipi implements Serializable{
	private static final long serialVersionUID = 1L;
	private String ListelemeTipi;
    
	public SnfListelemeTipi() {
		super();
	}
	
    public SnfListelemeTipi(String ListelemeTip) {
    	super();
    	ListelemeTipi = ListelemeTip;
    }
    
    public String getListelemeTipi()
    {
        return ListelemeTipi;
    }
    
    public void setListelemeTipi(String ListelemeTipi) {
		this.ListelemeTipi = ListelemeTipi;
	}

    public String toString()
    {
        return ListelemeTipi;
    }
}
