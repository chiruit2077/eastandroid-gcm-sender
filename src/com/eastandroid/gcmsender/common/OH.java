package com.eastandroid.gcmsender.common;

import java.util.Observable;

public class OH extends Observable {

	public enum TYPE {
		CONFIG, PROJECT_NUMBER, MESSAGE, TARGET;
	};

	private OH() {
	}

	private static OH INSTANCE = new OH();

	public static OH c() {
		return INSTANCE;
	}

	@Override
	public void notifyObservers(Object data) {
		if (data instanceof TYPE) {
			setChanged();
			super.notifyObservers(data);
		}
	}
}
