package com.wasseemb.headphonein;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

public class RemovablePreference extends Preference implements View.OnClickListener {
	private View mRemoveButton;
	private String mPackageName;
	private boolean mManualOverride = false;

	public RemovablePreference(Context context) {
		super(context);
		init(context, null);
	}

	public RemovablePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RemovablePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setWidgetLayoutResource(R.layout.removable_preference);
	}

	@Override
	protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
		super.onAttachedToHierarchy(preferenceManager);
		refresh();
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		mRemoveButton = view.findViewById(R.id.remove_button);
		mRemoveButton.setOnClickListener(this);

		if (!mManualOverride)
			refresh();

		if ((!mManualOverride && !isPreferenceSet()) || (mManualOverride && mPackageName == null))
			mRemoveButton.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (mManualOverride) {
			mPackageName = null;
			refresh();
		} else {
			getPreferenceManager().getSharedPreferences().edit().remove(getKey()).commit();
			refresh();
		}
	}

	private boolean isPreferenceSet() {
		return getPreferenceManager().getSharedPreferences().contains(getKey());
	}

	public String getPackageName() {
		return mPackageName == null ? "" : mPackageName;
	}

	public void setManualMode(boolean mm) {
		mManualOverride = mm;
	}

	public void setPackageName(String packageName) {
		mPackageName = packageName;
		if (mPackageName != null) {
			if (mRemoveButton != null)
				mRemoveButton.setVisibility(View.VISIBLE);
		}
		mManualOverride = true;
		refresh();
	}

	public void refresh() {
		if (mManualOverride) {
			if (mPackageName == null) {
				if (mRemoveButton != null)
					mRemoveButton.setVisibility(View.GONE);
				String count = getKey().substring(getKey().length()-1);
				setTitle(String.format(getString(R.string.pref_shortcut_at, count)));
				setSummary(R.string.pref_description_shortcut);
			} else {
				if (mRemoveButton != null)
					mRemoveButton.setVisibility(View.VISIBLE);
				PackageManager pm = getContext().getPackageManager();
				ApplicationInfo info;
				setSummary(mPackageName);
				try {
					info = pm.getApplicationInfo(mPackageName, 0);
					CharSequence label = pm.getApplicationLabel(info);
					setTitle(label);

				} catch (NameNotFoundException e) {
					setTitle(R.string.app_uninstalled);
					e.printStackTrace();
				}
			}
		} else {
			if (isPreferenceSet()) {
				mPackageName = getPreferenceManager().getSharedPreferences().getString(getKey(), null);
				PackageManager pm = getContext().getPackageManager();
				ApplicationInfo info;
				setSummary(mPackageName);
				try {
					info = pm.getApplicationInfo(mPackageName, 0);
					CharSequence label = pm.getApplicationLabel(info);
					setTitle(label);

				} catch (NameNotFoundException e) {
					setTitle(R.string.app_uninstalled);
					e.printStackTrace();
				}
				if (mRemoveButton != null)
					mRemoveButton.setVisibility(View.VISIBLE);
			} else {
				if (mRemoveButton != null)
					mRemoveButton.setVisibility(View.GONE);
				String count = getKey().substring(getKey().length()-1);
				setTitle(String.format(getString(R.string.pref_shortcut_at, count)));
				setSummary(R.string.pref_description_shortcut);
				mPackageName = null;
			}
		}
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		refresh();
		super.onSetInitialValue(restorePersistedValue, defaultValue);
		refresh();
	}

	private String getString(int resId, Object... args) {
		return getContext().getResources().getString(resId, args);
	}
}