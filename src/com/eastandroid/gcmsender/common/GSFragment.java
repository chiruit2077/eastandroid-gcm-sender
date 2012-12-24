package com.eastandroid.gcmsender.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.actionbarsherlock.BaseFragment;
import com.eastandroid.gcmsender.NewFile;
import com.eastandroid.gcmsender.R;

public class GSFragment extends BaseFragment {

	protected void getLoadFile(final String ext) {
		ArrayList<File> files = new ArrayList<File>();
		files.addAll(Arrays.asList(GSFile.getfiles(ext)));

		showList(new ArrayAdapter<File>(mContext, R.layout.files_item, files), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AlertDialog dlg = (AlertDialog) dialog;
				File f = (File) dlg.getListView().getItemAtPosition(which);
//				Log.l(f);
				load(f.getAbsolutePath());
			}
		});
	}

	protected void getSaveFile(final String ext) {
		ArrayList<File> files = new ArrayList<File>();
		files.addAll(Arrays.asList(GSFile.getfiles(ext)));

		files.add(new NewFile(getString(R.string.new_more)));
		showList(new ArrayAdapter<File>(mContext, R.layout.files_item, files), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AlertDialog dlg = (AlertDialog) dialog;
				File f = (File) dlg.getListView().getItemAtPosition(which);

				if (f instanceof NewFile) {
					final EditText view = new EditText(mContext);
					DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							File f = GSFile.getVPathfile(view.getText().toString(), ext);
							String pathfile = f.getAbsolutePath();
							if (f.exists()) {
								pathfile = GSFile.getTempTime(ext + "_", "." + ext).getAbsolutePath();
							}
//							Log.l(pathfile);
							save(pathfile);
						}
					};
					showDialog(R.string.new_filename, null, null, view, R.string.ok, positiveListener, null, null, null, null);
				} else {
//					Log.l(f);
					save(f.getAbsolutePath());
				}
			}
		});

	}
	protected void load(String pathfile) {
//		Log.l(pathfile);
	}
	protected void save(String pathfile) {
//		Log.l(pathfile);
	}

}
