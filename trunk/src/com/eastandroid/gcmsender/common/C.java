package com.eastandroid.gcmsender.common;

import android.common.BaseC;

public interface C extends BaseC {
	String PROJECT_NUMBER = "316429311504";
	String API_KEY = "AIzaSyBtgAww81TGotvtf79rXGETGSsyN3JAar0";

	String SCHEME = "http";
	String SCHEME_GCMSENDER = "gcmsender";
	String AUTHORITY = "gcmsender.eastandroid.com";

	enum requestCode {
		message_add, target_add, target_edit, message_edit;
		public int o() {
			return ordinal();
		}
	};

	enum EXTRA {
		key, value, message_position//message_add
		, name, registrationId, target_position//target_add
		;

		public String s() {
			return name();
		}
	}

}
