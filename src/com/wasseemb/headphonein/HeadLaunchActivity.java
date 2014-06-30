package com.wasseemb.headphonein;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

public class HeadLaunchActivity extends Activity {
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setUpWindow();
		setContentView(R.layout.app_activity);
		createNotification();

		pref = PreferenceManager.getDefaultSharedPreferences(this);
		initQuick();
		IntentFilter filter = new IntentFilter();

		filter.addAction("com.wasseemb.headphonein.endactivity");
		registerReceiver(receiver, filter);
		Intent local = new Intent();
		local.setAction("com.wasseemb,headphonein.endBack");
		sendBroadcast(local);

	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			clearNotification(arg0, 99);
			finish();

		}
	};

	public void createNotification() {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this, HeadLaunchActivity.class);
		PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);
		Notification notification;

		notification = new Notification.Builder(this)
				.setContentTitle("Headphone Launcher").setContentText("Start")
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pending)
				.setWhen(when).setAutoCancel(true).setOngoing(true).build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		nm.notify(99, notification);
	}

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	};

	public void initQuick() {

		final ArrayList<String> shortcutArray = new ArrayList<String>();
		for (int i = 1; i <= 9; i++) {
			shortcutArray.add(i - 1, pref.getString("shortcut_" + i, "empty"));
		}
		int id[] = { R.id.imageview1, R.id.imageview2, R.id.imageview3,
				R.id.imageview4, R.id.imageview5, R.id.imageview6,
				R.id.imageview7, R.id.imageview8, R.id.imageview9 };

		for (int i = 0; i <= 8; i++) {
			if (!shortcutArray.get(i).equals("empty")) {
				final int x = i;

				CircleButton i1 = (CircleButton) findViewById(id[i]);
				i1.setImageDrawable(getIcon(shortcutArray.get(i)));
				i1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent LaunchIntent = getPackageManager()
								.getLaunchIntentForPackage(shortcutArray.get(x));
						startActivity(LaunchIntent);

					}
				});
			}

		}

	}

	public Drawable getIcon(String image) {

		Drawable icon = null;
		try {
			icon = getPackageManager().getApplicationIcon(image);
			return icon;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return icon;

	}

	public void clearNotification(Context mContext, int NOTIFICATION_ID) {
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

}
