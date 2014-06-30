package com.wasseemb.headphonein;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends PreferenceActivity implements
		OnPreferenceClickListener {
	HeadInReceiver mReceiver;
	SharedPreferences prefs;
	Preference pEnable;
	Preference pDisable;
	CheckBoxPreference pBoot;

	Context context;
	private static final String URL_MY_MODULES = "http://repo.xposed.info/module-overview?combine=WasseemB&sort_by=title";
	private static final String URL_MY_APPS = "market://search?q=pub:WasseemB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_main);
		context = getApplicationContext();
		pEnable = (Preference) findPreference("service_start");
		pDisable = (Preference) findPreference("service_stop");
		pBoot = (CheckBoxPreference) findPreference("enable_boot");

		initShortcuts();

		Preference copyrightPreference = findPreference("copyright_key");
		final String[] array = { "Play Store", "Xposed Modules" };
		copyrightPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								MainActivity.this);
						builder.setTitle("").setItems(array,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Uri uri = null;
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										switch (which) {
										case 0:
											uri = Uri.parse(URL_MY_APPS);
											intent.setPackage("com.android.vending");
											break;
										case 1:
											uri = Uri.parse(URL_MY_MODULES);
											break;
										}
										startActivity(intent.setData(uri));
									}
								});
						builder.create().show();
						return false;
					}
				});

		pEnable.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				// TODO Auto-generated method stub
				startService(new Intent(getApplicationContext(),
						HeadphoneService.class));
				return false;
			}
		});

		pDisable.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				stopService(new Intent(getApplicationContext(),
						HeadphoneService.class));

				return false;
			}
		});

		// mReceiver = new HeadInReceiver();

		IntentFilter filter = new IntentFilter();

		filter.addAction("com.wasseemb,headphonein.endBack");
		registerReceiver(receiver, filter);

	}

	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	};

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			finish();

		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initShortcuts() {
		for (int i = 1; i <= 9; i++) {
			String key = "shortcut_" + String.valueOf(i);
			findPreference(key).setOnPreferenceClickListener(this);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		int count = requestCode;
		String key = "shortcut_" + String.valueOf(count);

		getPreferenceManager().getSharedPreferences().edit()
				.putString(key, data.getStringExtra("package_name")).commit();
		((RemovablePreference) findPreference(key)).refresh();
		// emitAppListChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		Intent intent = new Intent(getApplicationContext(),
				ApplicationPickerActivity.class);
		int count = Integer.parseInt(preference.getKey().substring(
				preference.getKey().length() - 1));
		startActivityForResult(intent, count);
		return false;
	}

}
