package com.eastandroid.gcmsender.common;

import android.common.BaseP;

/**
 * @author djrain
 * 
 */
public class P {

	private static P INSTANCE = new P();

	public static P c() {
		return INSTANCE;
	}

	public enum I {
		RegistrationId, Config, Message, Targets//
		, defalut_key, encode, includeme, noisall_message, noisall_target, silent_add_config, silent_add_message, silent_add_target//Properties
		;

		public String s() {
			return toString();
		}

		@SuppressWarnings("unchecked")
		public <TYTE> TYTE get(TYTE defvalue) {
			Object o = BaseP.c().get().getAll().get(name());
			if (o == null)
				return defvalue;
			return (TYTE) o;
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
