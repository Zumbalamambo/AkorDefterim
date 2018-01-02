package com.cnbcyln.app.akordefterim.Interface;

import android.support.v4.app.Fragment;

import org.json.JSONObject;

public interface Interface_FragmentDataConn {
	void FragmentSayfaGetir(String SayfaAdi);
	void FragmentSarkiSayfaGetir(int SarkiID, String SarkiAdi, String SanatciAdi, String DepolamaAlani);
	void SarkiListesiDoldur();
	void SarkiListesiDoldur(int SecilenListeID, int SecilenKategoriID, int SecilenTarzID, int SecilenListelemeTipi);
	void HesapCikisYap(JSONObject CikisData);
	void SecilenSarkiIcerikAl(String Icerik);
	void WebYonetimServisiDurumGuncelle(boolean Durum);
	void WebYonetimServisiYenidenBaslat();
	void SlidingIslem(int islem);
	//public void PopupMenuIslemleri(MenuItem item);
	Fragment SlidingTabFragmentClassGetir(String FragmentAdi);
	void SarkiListesi_SecimPanelGuncelle(Boolean Durum);
	void SarkiListesi_SecimPanelBilgiGuncelle();
	void YayinAraci_KonumBilgileriThread(Boolean Durum);
	void YayinAraci_CanliIstek(Boolean Durum);
	void YayinAraciDurumBilgisiAl();
	void GoogleSatinAlma();
	void SarkiPaylas(String Platform, String Baslik, String Icerik, String URL);

	//GPS İşlemleri
	//public void IstekAlSwitch(boolean isChecked);
	//public void IstekYapSwitch();
	//public boolean IstekAlmaDurumu();
	//public void GPSSifirla();
}