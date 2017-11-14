package com.cnbcyln.app.akordefterim.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebServerService extends Service {

	private WebServer server = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		server = new WebServer(this);
		server.startThread();
	}

	@Override
	public void onDestroy() {
		server.stopThread();
		server = null;
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
