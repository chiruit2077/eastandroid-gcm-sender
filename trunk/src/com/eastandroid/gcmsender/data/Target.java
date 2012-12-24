package com.eastandroid.gcmsender.data;

import android.net.Uri;

import com.eastandroid.gcmsender.MainFTargets;
import com.eastandroid.gcmsender.WriteTarget;
import com.eastandroid.gcmsender.common.C;

public class Target extends BaseSave {
	private static final long serialVersionUID = -7792145143005897444L;
	public String name = "";
	public String registrationId = "";

	public Target set(String name, String registrationId) {
		this.name = name;
		this.registrationId = registrationId;
		return this;
	}

	@Override
	public String toString() {
		return "[" + name + "," + registrationId + "]";
	}
	public String getShareLink() {
		return new Uri.Builder()//
				.scheme(C.SCHEME)//
				.authority(C.AUTHORITY)//
				.appendPath(MainFTargets.class.getSimpleName())//
				.appendQueryParameter(C.EXTRA.name.s(), name)//
				.appendQueryParameter(C.EXTRA.registrationId.s(), registrationId)//
				.build().toString();
	}

	public Uri getEditUri() {
		return new Uri.Builder()//
				.scheme(C.SCHEME_GCMSENDER)//
				.authority(C.AUTHORITY)//
				.appendPath(WriteTarget.class.getSimpleName())//
				.appendQueryParameter(C.EXTRA.name.s(), name)//
				.appendQueryParameter(C.EXTRA.registrationId.s(), registrationId)//
				.build();
	}

	public static Uri getNewUri() {
		return new Uri.Builder()//
				.scheme(C.SCHEME_GCMSENDER)//
				.authority(C.AUTHORITY)//
				.appendPath(WriteTarget.class.getSimpleName())//
				.build();
	}

	public Target set(Uri uri) {
		name = uri.getQueryParameter(C.EXTRA.name.s());
		registrationId = uri.getQueryParameter(C.EXTRA.registrationId.s());
		return this;
	}

}
