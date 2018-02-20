package com.cnbcyln.app.akordefterim.Interface;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

public interface Int_DataConn_AnaEkran {
	void FragmentSayfaGetir(String SayfaAdi);
	void SlidingIslem(int islem);
	Fragment SlidingTabFragmentClassGetir(String FragmentAdi);
	void SarkiListesi_SecimPanelGuncelle(Boolean Durum);
	void SarkiListesi_SecimPanelBilgiGuncelle();
	void SarkiPaylas(String Platform, String Baslik, String Icerik, String URL);
	void AnaEkranProgressIslemDialogAc(String Mesaj);
	void AnaEkranProgressIslemDialogKapat();
	void SarkiListesiGetir();
	void StandartSnackBarMsj(String Mesaj);
	void StandartSnackBarMsj(CoordinatorLayout coordinatorLayout, String Mesaj);
	void StandartSnackBarMsj(String Menu, String Mesaj);
}