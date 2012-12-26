package com.eastandroid.gcmsender.common;

import android.common.BaseApplication;
import android.miscellaneous.E;

public class GSApplication extends BaseApplication {

	@Override
	public void onCreate() {
		log();
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	private void log() {
		E.LOG = true;
		E.NETLOG = true;
		E.SCREEN = true;
	}

}
