package com.eastandroid.gcmsender;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

@SuppressWarnings("deprecation")
public class Properties extends SherlockPreferenceActivity {
	public static int THEME = R.style.Sherlock___Theme_Light;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(THEME);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

//		Log.l(BaseP.c().get().getAll());
	}
}
