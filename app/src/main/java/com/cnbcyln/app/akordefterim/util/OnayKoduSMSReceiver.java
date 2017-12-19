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
		Intent Intent_Dogrulama_Kodu = new Intent("com.cnbcyln.app.akordefterim.Dogrulama_Kodu");
		
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
				case "Dogrulama_Kodu":
					Intent_Dogrulama_Kodu.putExtra("Kimden", Kimden);
					Intent_Dogrulama_Kodu.putExtra("OnayKodu", Icerik.substring(Icerik.indexOf(":") + 2, Icerik.indexOf(":") + 8));
					context.sendBroadcast(Intent_Dogrulama_Kodu);
					
					break;
				}
			}
		}
	}
}