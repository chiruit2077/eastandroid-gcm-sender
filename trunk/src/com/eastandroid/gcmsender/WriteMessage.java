package com.eastandroid.gcmsender;

import android.content.Intent;
import android.miscellaneous.Log;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.eastandroid.gcmsender.common.C.EXTRA;
import com.eastandroid.gcmsender.common.GSActivity;

public class WriteMessage extends GSActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_message);
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
			String key = uri.getQueryParameter(EXTRA.key.s());
			String value = uri.getQueryParameter(EXTRA.value.s());
			setText(R.id.key, key);
			setText(R.id.value, value);
		}
	}

	private OnClickListener onSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final Intent data = getIntent();
			data.putExtra(EXTRA.key.s(), text(R.id.key));
			data.putExtra(EXTRA.value.s(), text(R.id.value));
			setResult(RESULT_OK, data);
			finish();
		}
	};

}
