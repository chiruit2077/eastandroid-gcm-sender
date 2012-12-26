package com.eastandroid.gcmsender;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.common.BaseP;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.util.SparseArray;
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
			send(P.I.defalut_key.get("gcmsender"), intent.getStringExtra(Intent.EXTRA_TEXT));
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
					if (P.I.silent_add_config.get(false)) {
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
					if (P.I.silent_add_message.get(false)) {
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
					if (P.I.silent_add_target.get(false)) {
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

	public void send(String key, String msg) {
		ArrayList<String> regIds = new ArrayList<String>();
		{
			if (BaseP.c().getBoolean(P.I.includeme.s())) {
				String rid = P.c().getRegistrationId();
				if (rid != null)
					regIds.add(rid);
			}

			MainFTargets fr = (MainFTargets) mTabsAdapter.getInstantiateItem(CLASSES.MainFTargets.o());
			ArrayList<Target> targets = fr.getSelected();
			for (Target target : targets) {
				regIds.add(target.registrationId);
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(key, msg);

		String api_key = "";
		{
			MainFConfig fr = (MainFConfig) mTabsAdapter.getInstantiateItem(CLASSES.MainFConfig.o());
			api_key = fr.getApiKey();
		}
		GCMSend.sender(api_key, regIds, map);
	};

	public void onSendClickListener(View v) {
		if (v.getId() != R.id.send)
			throw new UnsupportedOperationException("resid must R.id.send");

		ArrayList<String> regIds = new ArrayList<String>();
		{
			if (P.I.includeme.get(false)) {
				String rid = P.c().getRegistrationId();
				if (rid != null)
					regIds.add(rid);
			}

			MainFTargets fr = (MainFTargets) mTabsAdapter.getInstantiateItem(CLASSES.MainFTargets.o());
			ArrayList<Target> targets = fr.getSelected();
			for (Target target : targets) {
				regIds.add(target.registrationId);
			}
		}

		Map<String, String> map = new HashMap<String, String>();
		{
			MainFMessage fr = (MainFMessage) mTabsAdapter.getInstantiateItem(CLASSES.MainFMessage.o());
			ArrayList<Message> messages = fr.getSelected();
			for (Message message : messages) {
				map.put(message.key, message.value);
				String key = message.key;
				String value = message.value;
				if (P.I.encode.get(false)) {
					try {
						key = URLEncoder.encode(key, C.CHAR_UTF8);
						value = URLEncoder.encode(value, C.CHAR_UTF8);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				map.put(key, value);
			}
		}

		String api_key = "";
		{
			MainFConfig fr = (MainFConfig) mTabsAdapter.getInstantiateItem(CLASSES.MainFConfig.o());
			api_key = fr.getApiKey();
		}
		GCMSend.sender(api_key, regIds, map);

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
		private SparseArray<Object> mTags = new SparseArray<Object>();

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

		Object getInstantiateItem(int position) {
			return mTags.get(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Object obj = super.instantiateItem(container, position);
			mTags.put(position, obj);
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
