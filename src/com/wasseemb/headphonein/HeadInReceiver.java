package com.wasseemb.headphonein;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadInReceiver extends BroadcastReceiver {
	private static final String TAG = "Debug";

	private static final String headUnPlugged = "com.wasseemb.headphonein.endactivity";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
			int state = intent.getIntExtra("state", -1);
			switch (state) {
			case 0:
				Log.d(TAG, "Headset is unplugged");
				NotificationManager notificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.cancel(99);
				Intent local = new Intent();
				local.setAction(headUnPlugged);
				context.sendBroadcast(local);
				
				break;
			case 1:
				Log.d(TAG, "Headset is plugged");
				Intent i = new Intent(context, HeadLaunchActivity.class);
		        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        context.startActivity(i);
				break;
			default:
				Log.d(TAG, "I have no idea what the headset state is");
			}
		}
	}

}
