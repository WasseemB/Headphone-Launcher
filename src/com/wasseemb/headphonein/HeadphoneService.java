package com.wasseemb.headphonein;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class HeadphoneService extends Service {
	HeadInReceiver mReceiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mReceiver = new HeadInReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(mReceiver, filter);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);

	}
}
