package com.eastandroid.gcmsender.common;

import android.content.Intent;
import android.miscellaneous.Log;
import android.os.Handler;
import android.util.GCMIntentService;
import android.view.Gravity;
import android.widget.Toast;

public class GSIntentService extends GCMIntentService {
	private Handler h = new Handler();

	@Override
	protected void onRegistrationId(String registrationId) {
		super.onRegistrationId(registrationId);
		P.c().setRegistrationId(registrationId);
		OH.c().notifyObservers(OH.TYPE.CONFIG);
	}
	@Override
	protected void onUnregistration() {
		super.onUnregistration();
		P.c().removeRegistrationId();
	}

	@Override
	protected void onMessage(Intent intent) {
		super.onMessage(intent);

		final String intent_s = Log._DUMP(intent);
		h.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(GSIntentService.this, intent_s, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, (int) (System.currentTimeMillis() % 1000 / 100 * 10));
				toast.show();
			}
		});
	}

}
