package com.cnbcyln.app.akordefterim;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

class PagerAdapter extends FragmentPagerAdapter {
	private Activity activity;
	private int PAGES;
	private SharedPreferences sharedPref;
	
    PagerAdapter(Activity activity, FragmentManager fm) {
    	super(fm);
    	this.activity = activity;
		this.sharedPref = activity.getSharedPreferences(new AkorDefterimSys(activity).PrefAdi, Context.MODE_PRIVATE);

		if(sharedPref.getString("prefHesapID", "").equals("")) // Çevrimdışı ise
			this.PAGES = 2;
		else // Çevrimiçi ise
			this.PAGES = 3;
    }

    @Override
    public Fragment getItem(int i) {
		if (sharedPref.getString("prefHesapID", "").equals("")) { // Çevrimdışı ise
			switch (i) {
				case 0:
					return new Frg_TabGenelMenu();
				case 1:
					return new Frg_TabRepKontrol();
			}
		} else { // Çevrimiçi ise
			switch (i) {
				case 0:
					return new Frg_TabGenelMenu();
				case 1:
					return new Frg_TabRepKontrol();
				case 2:
					return new Frg_TabIstekler();
			}
		}
    	
    	return null;
    }
    
    public CharSequence getPageTitle(int position) {
		if (sharedPref.getString("prefHesapID", "").equals("")) { // Çevrimdışı ise
			switch (position) {
				case 0:
					return activity.getString(R.string.tabsayfa_genel_menu);
				case 1:
					return activity.getString(R.string.tabsayfa_repertuvar_kontrol);
			}
		} else { // Çevrimiçi ise
			switch (position) {
				case 0:
					return activity.getString(R.string.tabsayfa_genel_menu);
				case 1:
					return activity.getString(R.string.tabsayfa_repertuvar_kontrol);
				case 2:
					return activity.getString(R.string.tabsayfa_istekler);
			}
		}

        return null; 
    }

	@Override
    public int getCount() {
        return PAGES;
    }
}