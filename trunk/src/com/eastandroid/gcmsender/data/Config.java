package com.eastandroid.gcmsender.data;

import android.net.Uri;
import android.os.Build;

import com.eastandroid.gcmsender.MainFConfig;
import com.eastandroid.gcmsender.MainFTargets;
import com.eastandroid.gcmsender.common.C;

public class Config extends BaseSave {
	private static final long serialVersionUID = 3884672113359964651L;
	public String PROJECT_NUMBER = C.PROJECT_NUMBER;
	public String API_KEY = C.API_KEY;
	public String RegistrationId = "";

	public void set(String API_KEY, String PROJECT_NUMBER, String RegistrationId) {
		this.API_KEY = API_KEY;
		this.PROJECT_NUMBER = PROJECT_NUMBER;
		this.RegistrationId = RegistrationId;
	}

	@Override
	public String toString() {
		return PROJECT_NUMBER + "," + API_KEY + "," + RegistrationId;
	}

	public String getSharelinkPROJECT_NUMBER() {
		return new Uri.Builder()//
				.scheme(C.SCHEME)//
				.authority(C.AUTHORITY)//
				.appendPath(MainFConfig.class.getSimpleName())//
				.appendQueryParameter("PROJECT_NUMBER", PROJECT_NUMBER)//
				.appendQueryParameter("API_KEY", API_KEY)//
				.build().toString();

	}

	public String getSharelinkRegistrationId() {
		return new Uri.Builder()//
				.scheme(C.SCHEME)//
				.authority(C.AUTHORITY)//
				.appendPath(MainFTargets.class.getSimpleName())//
				.appendQueryParameter(C.EXTRA.name.s(), Build.MODEL)//
				.appendQueryParameter(C.EXTRA.registrationId.s(), RegistrationId)//
				.build().toString();
	}

	public void set(Uri uri) {
		API_KEY = uri.getQueryParameter("API_KEY");
		PROJECT_NUMBER = uri.getQueryParameter("PROJECT_NUMBER");
	}
}
