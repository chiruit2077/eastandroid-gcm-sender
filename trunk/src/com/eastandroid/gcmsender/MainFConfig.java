package com.eastandroid.gcmsender;

import java.util.Observable;
import java.util.Observer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.GCMReceiver;
import android.util.ShareHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.MenuItem;
import com.eastandroid.gcmsender.common.GSFile;
import com.eastandroid.gcmsender.common.GSFragment;
import com.eastandroid.gcmsender.common.OH;
import com.eastandroid.gcmsender.common.P;
import com.eastandroid.gcmsender.data.Config;

public class MainFConfig extends GSFragment {
	static MainFConfig newInstance(int num) {
		return new MainFConfig();
	}

	private Config mConfig = new Config();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.config_view, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		OH.c().addObserver(observer);
		reload();

	}

	private void reload() {
		try {
			load(GSFile.getLastConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onDestroy() {
		OH.c().deleteObserver(observer);
		super.onDestroy();
	}

	private Observer observer = new Observer() {
		@Override
		public void update(Observable observable, Object data) {
			if (!(data instanceof OH.TYPE))
				return;

			if (OH.TYPE.CONFIG == data) {
				setData();
				mConfig.RegistrationId = P.c().getRegistrationId();
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						load();
					}
				});
			}

			if (OH.TYPE.PROJECT_NUMBER == data) {
				reload();
			}
		}
	};

	private void load() {
		setText(R.id.PROJECT_NUMBER, mConfig.PROJECT_NUMBER);
		setText(R.id.API_KEY, mConfig.API_KEY);
		setText(R.id.RegistrationId, mConfig.RegistrationId);
		save(GSFile.getLastConfig());
	}

	private void setData() {
		mConfig.PROJECT_NUMBER = text(R.id.PROJECT_NUMBER);
		mConfig.API_KEY = text(R.id.API_KEY);
		mConfig.RegistrationId = text(R.id.RegistrationId);
	}

	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		inflater.inflate(R.menu.reg, menu);
		inflater.inflate(R.menu.io, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}
	/**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.menu_load :
				getLoadFile(GSFile.EXT.config.name());
				break;
			case R.id.menu_save :
				getSaveFile(GSFile.EXT.config.name());
				break;
			case R.id.menu_reg :
				GCMReceiver.register(mContext, text(R.id.PROJECT_NUMBER));
				break;
			case R.id.menu_unreg :
				onUnreg();
				break;
			case R.id.menu_share_PROJECT_NUMBER :
				try {
					setData();
					String share_link = mConfig.getSharelinkPROJECT_NUMBER();
					Intent intent = ShareHelper.getIntentTextShare(share_link);
					intent.putExtra(Intent.EXTRA_SUBJECT, "Project number");
					ShareHelper.shareTo(mContext, intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.menu_share_registrationId :
				try {
					setData();
					String registrationId_link = mConfig.getSharelinkRegistrationId();
					Intent intent = ShareHelper.getIntentTextShare(registrationId_link);
					intent.putExtra(Intent.EXTRA_SUBJECT, "registrationId");
					ShareHelper.shareTo(mContext, intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	protected void load(String pathfile) {
		super.load(pathfile);

		Config config = null;
		if ((config = GSFile.load(pathfile, config)) == null) {
			toast("Unknow file format");
			return;
		}
		mConfig = config;
		load();
	}
	protected void save(String pathfile) {
		super.save(pathfile);
		setData();
		GSFile.save(pathfile, mConfig);
	}

	public void onUnreg() {

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GCMReceiver.unregister(((Dialog) dialog).getContext());
			}
		};
		showDialog("really?", "YES", positiveListener, "NO", null);
	}

}
