package com.eastandroid.gcmsender.common;

import android.common.BaseP;

/**
 * @author djrain
 * 
 */
public class P {

	public static P INSTANCE = new P();

	public static P c() {
		return INSTANCE;
	}

	private enum I {
		RegistrationId, Config, Message, Targets;

		public String s() {
			return toString();
		}
	};

	public String getRegistrationId() {
		return BaseP.c().get().getString(I.RegistrationId.s(), null);
	}
	public void removeRegistrationId() {
		BaseP.c().get().edit().remove(I.RegistrationId.s()).commit();
	}
	public void setRegistrationId(String registrationId) {
		BaseP.c().get().edit().putString(I.RegistrationId.s(), registrationId).commit();
	}

}
