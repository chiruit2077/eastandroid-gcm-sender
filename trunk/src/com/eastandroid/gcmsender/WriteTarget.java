package com.eastandroid.gcmsender;

import android.content.Intent;
import android.miscellaneous.Log;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.eastandroid.gcmsender.common.C.EXTRA;
import com.eastandroid.gcmsender.common.GSActivity;

public class WriteTarget extends GSActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_registration_id);
		parseExtra();

		setOnClickListener(R.id.save, onSaveClickListener);
	}

	private void parseExtra() {
		final Intent intent = getIntent();
		Log.l(intent);

		final String action = intent.getAction();
		if (action.equals(Intent.ACTION_INSERT)) {
			return;
		}

		if (action.equals(Intent.ACTION_EDIT)) {
			final Uri uri = intent.getData();
			String name = uri.getQueryParameter(EXTRA.name.s());
			String registrationId = uri.getQueryParameter(EXTRA.registrationId.s());
			setText(R.id.name, name);
			setText(R.id.registrationId, registrationId);
		}

	}
	private OnClickListener onSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final Intent data = getIntent();
			data.putExtra(EXTRA.name.s(), text(R.id.name));
			data.putExtra(EXTRA.registrationId.s(), text(R.id.registrationId));
			setResult(RESULT_OK, data);
			finish();
		}
	};
}
