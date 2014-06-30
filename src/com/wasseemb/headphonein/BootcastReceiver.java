package com.wasseemb.headphonein;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		boolean autoStart = prefs.getBoolean("enable_boot", false);
		Log.d("Debug", autoStart+"");
		if (autoStart) {
			Intent startServiceIntent = new Intent(context,
					HeadphoneService.class);
			context.startService(startServiceIntent);
		}
	}
}
