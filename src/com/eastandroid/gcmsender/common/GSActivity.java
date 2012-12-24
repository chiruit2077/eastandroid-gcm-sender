package com.eastandroid.gcmsender.common;

import android.os.Bundle;

import com.actionbarsherlock.BaseActivity;
import com.eastandroid.gcmsender.R;

public class GSActivity extends BaseActivity {

	public static int THEME = R.style.Sherlock___Theme_Light;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(THEME);
		super.onCreate(savedInstanceState);
	}

}
