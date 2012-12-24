package com.eastandroid.gcmsender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShareReceiver extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.l(getIntent());
		startActivity(getIntent()//
				.setClass(this, MainTab.class)//
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP)//
		);

		finish();
	}
}
