package com.eastandroid.gcmsender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.common.BaseP;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.miscellaneous.Log;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.GCMSend;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.view.MenuItem;
import com.eastandroid.gcmsender.common.C;
import com.eastandroid.gcmsender.common.GSActivity;
import com.eastandroid.gcmsender.common.GSFile;
import com.eastandroid.gcmsender.common.OH;
import com.eastandroid.gcmsender.common.P;
import com.eastandroid.gcmsender.data.Config;
import com.eastandroid.gcmsender.data.Message;
import com.eastandroid.gcmsender.data.Target;

public class MainTab extends GSActivity {

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintab);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabsAdapter = new TabsAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOnPageChangeListener(mTabsAdapter);

		parseIntent();

	}
	@Override
	protected void onNewIntent(Intent intent) {
//		Log.l(intent);
		setIntent(intent);
		super.onNewIntent(intent);
		parseIntent();
	}
	private void parseIntent() {
		Intent intent = getIntent();
		Log.l(intent);

		if (Intent.ACTION_SEND.equals(intent.getAction())) {
			onSendClickListener(null);
		}

		final Uri uri = intent.getData();
		if (uri != null) {
			uri.getScheme();
			uri.getAuthority();
			String path = uri.getLastPathSegment();

			ActionBar actionBar = getSupportActionBar();
			actionBar.selectTab(actionBar.getTabAt(CLASSES.valueOf(uri.getLastPathSegment()).o()));
			switch (CLASSES.valueOf(path)) {
				case MainFConfig :
					if (BaseP.c().getBoolean("silent_add_config")) {
						setConfig(uri);
					} else {
						OnClickListener positiveListener = new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								setConfig(uri);
							}
						};
						showDialog(R.string.insert_config, R.string.ok, positiveListener, R.string.cancel, null);
					}

					break;

				case MainFMessage :
					if (BaseP.c().getBoolean("silent_add_message")) {
						addMessage(uri);
					} else {
						OnClickListener positiveListener = new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								addMessage(uri);
							}
						};
						showDialog(R.string.insert_message, R.string.ok, positiveListener, R.string.cancel, null);
					}
					break;
				case MainFTargets :
					if (BaseP.c().getBoolean("silent_add_target")) {
						addTarget(uri);
					} else {
						OnClickListener positiveListener = new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								addTarget(uri);
							}
						};
						showDialog(R.string.insert_target, R.string.ok, positiveListener, R.string.cancel, null);
					}

					break;
			}
		} else {
			ActionBar actionBar = getSupportActionBar();
			actionBar.selectTab(actionBar.getTabAt(CLASSES.MainFMessage.o()));
		}
	}
	protected void addTarget(Uri uri) {
		String pathfile = GSFile.getLastTartet();
		ArrayList<Target> targets = null;
		targets = GSFile.load(pathfile, targets);
		if (targets == null) {
			targets = new ArrayList<Target>();
		}

		targets.add(new Target().set(uri));
		GSFile.save(pathfile, targets);
		OH.c().notifyObservers(OH.TYPE.TARGET);
	}
	protected void addMessage(Uri uri) {
		String pathfile = GSFile.getLastMessage();
		ArrayList<Message> messages = null;
		messages = GSFile.load(pathfile, messages);
		if (messages == null) {
			messages = new ArrayList<Message>();
		}

		messages.add(new Message().set(uri));
		GSFile.save(pathfile, messages);
		OH.c().notifyObservers(OH.TYPE.MESSAGE);

	}
	private void setConfig(Uri uri) {
		String pathfile = GSFile.getLastConfig();
		Config config = null;
		config = GSFile.load(pathfile, config);
		if (config == null) {
			config = new Config();
		}
		config.set(uri);
		GSFile.save(pathfile, config);
		OH.c().notifyObservers(OH.TYPE.CONFIG);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_config :
				startActivity(new Intent(mContext, Properties.class));
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onSendClickListener(String key, String msg) {
		ArrayList<String> regIds = new ArrayList<String>();
		String rid = P.c().getRegistrationId();
		if (rid != null)
			regIds.add(rid);
		ArrayList<Target> targets = null;
		targets = GSFile.load(GSFile.getLastTartet(), targets);
		for (Target target : targets) {
			regIds.add(target.registrationId);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(key, msg);

		GCMSend.sender(C.API_KEY, regIds, map);
	};

	public void onSendClickListener(View v) {
		if (v.getId() != R.id.send)
			throw new UnsupportedOperationException("resid must R.id.send");

		ArrayList<String> regIds = new ArrayList<String>();
		{
			if (BaseP.c().getBoolean("me")) {
				String rid = P.c().getRegistrationId();
				if (rid != null)
					regIds.add(rid);
			}

			MainFTargets fr = (MainFTargets) mTabsAdapter.getItem(CLASSES.MainFTargets.o());

//			MainFTargets fr = (MainFTargets) getSupportFragmentManager().getFragment(null, CLASSES.MainFTargets.title());
//			MainFTargets fr = (MainFTargets) getSupportFragmentManager().findFragmentById((int) mTabsAdapter.getItemId(CLASSES.MainFTargets.o()));
			ArrayList<Target> targets = fr.getSelected();
//			ArrayList<Target> targets = null;
//			targets = GSFile.load(GSFile.getLastTartet(), targets);
			for (Target target : targets) {
				regIds.add(target.registrationId);
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		{
			MainFMessage fr = (MainFMessage) mTabsAdapter.getItem(CLASSES.MainFMessage.o());
//			MainFMessage fr = (MainFMessage) getSupportFragmentManager().getFragment(null, CLASSES.MainFMessage.title());
//			MainFMessage fr = (MainFMessage) getSupportFragmentManager().findFragmentById((int) mTabsAdapter.getItemId(CLASSES.MainFMessage.o()));
			ArrayList<Message> messages = fr.getSelected();
//			ArrayList<Message> messages = null;
//			messages = GSFile.load(GSFile.getLastMessage(), messages);
			for (Message message : messages) {
				map.put(message.key, message.value);
			}
		}

		GCMSend.sender(C.API_KEY, regIds, map);
	};

	private enum CLASSES {
		MainFConfig, MainFMessage, MainFTargets;
		private String mClass[] = new String[]{MainFConfig.class.getName(), MainFMessage.class.getName(), MainFTargets.class.getName()};
		private String mTitle[] = new String[]{"Config", "Message", "Targets"};

		public String title() {
			return mTitle[o()];
		}
		public String classname() {
			return mClass[o()];
		}

		public int o() {
			return ordinal();
		}
	}

	public class TabsAdapter extends FragmentPagerAdapter implements TabListener, OnPageChangeListener {

		public TabsAdapter(FragmentManager fm) {
			super(fm);
			for (int i = 0; i < CLASSES.values().length; i++) {
				ActionBar.Tab tab = getSupportActionBar().newTab();
				tab.setText(CLASSES.values()[i].title());
				tab.setTabListener(this);//01
				getSupportActionBar().addTab(tab);//02
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return CLASSES.values().length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Object obj;
			Log.l(obj = super.instantiateItem(container, position));
			return obj;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.l(object);
			super.destroyItem(container, position, object);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fr = Fragment.instantiate(mContext//
					, CLASSES.values()[position].classname()//
					);

			Log.l(fr.getTag(), fr.getId(), fr);
			return fr;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			mViewPager.setCurrentItem(tab.getPosition());
		}
		@Override
		public void onPageSelected(int position) {
			final ActionBar actionBar = getSupportActionBar();
			actionBar.selectTab(actionBar.getTabAt(position));
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

	}

}