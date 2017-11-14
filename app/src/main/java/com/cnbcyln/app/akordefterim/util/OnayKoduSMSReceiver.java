package com.cnbcyln.app.akordefterim.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.content.SharedPreferences;

public class OnayKoduSMSReceiver extends BroadcastReceiver {
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		AkorDefterimSys AkorDefterimSys = new AkorDefterimSys(context);
		Intent Intent_FrgHesabim = new Intent("com.cnbcyln.app.akordefterim.Frg_Hesabim");
		Intent Intent_SifremiUnuttum = new Intent("com.cnbcyln.app.akordefterim.SifremiUnuttum");
		
		Bundle bundle = intent.getExtras();
		
		SmsMessage[] smsm;
		String Kimden = "", Icerik = "";
		
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			assert pdus != null;
			smsm = new SmsMessage[pdus.length];
			
			for (int i=0; i < smsm.length; i++) {
				smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				Kimden = smsm[i].getOriginatingAddress();
				Icerik = smsm[i].getMessageBody();
		    }
			
			SharedPreferences sharedPref = context.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);
			
			if(AkorDefterimSys.SMSGondericiAdi.contains(Kimden)) {
				switch (sharedPref.getString("prefSMSOnayKoduSayfaAdi", "")) {
				case "Frg_Hesabim":
					Intent_FrgHesabim.putExtra("Kimden", Kimden);
					Intent_FrgHesabim.putExtra("OnayKodu", Icerik.substring(Icerik.length() - 6, Icerik.length()));
					context.sendBroadcast(Intent_FrgHesabim);
					
					break;
				case "SifremiUnuttum":
					Intent_SifremiUnuttum.putExtra("Kimden", Kimden);
					Intent_SifremiUnuttum.putExtra("OnayKodu", Icerik.substring(Icerik.length() - 6, Icerik.length()));
					context.sendBroadcast(Intent_SifremiUnuttum);
					
					break;
				}
			}
		}
	}
}