package com.eastandroid.gcmsender.data;

import android.net.Uri;

import com.eastandroid.gcmsender.MainFMessage;
import com.eastandroid.gcmsender.WriteMessage;
import com.eastandroid.gcmsender.common.C;

public class Message extends BaseSave {
	public String key = "";
	public String value = "";
	private static final long serialVersionUID = 6765966246190988252L;

	public Message set(String key, String value) {
		this.key = key;
		this.value = value;
		return this;
	}

	@Override
	public String toString() {
		return "[" + key + "," + value + "]";
	}

	public String getShareLink() {
		return new Uri.Builder()//
				.scheme(C.SCHEME)//
				.authority(C.AUTHORITY)//
				.appendPath(MainFMessage.class.getSimpleName())//
				.appendQueryParameter(C.EXTRA.key.s(), key)//
				.appendQueryParameter(C.EXTRA.value.s(), value)//
				.build().toString();
	}
	public Uri getEditUri() {
		return new Uri.Builder()//
				.scheme(C.SCHEME_GCMSENDER)//
				.authority(C.AUTHORITY)//
				.appendPath(WriteMessage.class.getSimpleName())//
				.appendQueryParameter(C.EXTRA.key.s(), key)//
				.appendQueryParameter(C.EXTRA.value.s(), value)//
				.build();
	}
	public static Uri getNewUri() {
		return new Uri.Builder()//
				.scheme(C.SCHEME_GCMSENDER)//
				.authority(C.AUTHORITY)//
				.appendPath(WriteMessage.class.getSimpleName())//
				.build();
	}

	public Message set(Uri uri) {
		key = uri.getQueryParameter(C.EXTRA.key.s());
		value = uri.getQueryParameter(C.EXTRA.value.s());
		return this;
	}
}
