package com.eastandroid.gcmsender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import android.app.Activity;
import android.common.BaseP;
import android.content.Intent;
import android.os.Bundle;
import android.util.ShareHelper;
import android.util.SparseBooleanArray;
import android.util.UT;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.eastandroid.gcmsender.common.C;
import com.eastandroid.gcmsender.common.GSFile;
import com.eastandroid.gcmsender.common.GSFragment;
import com.eastandroid.gcmsender.common.OH;
import com.eastandroid.gcmsender.data.Message;

public class MainFMessage extends GSFragment implements OnItemLongClickListener {

	static MainFMessage newInstance(int num) {
		return new MainFMessage();
	}
	private ListView mList;
	private BaseAdapter mAdapter;
	private ArrayList<Message> mListItem = new ArrayList<Message>();
	private LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.itemlist_view, container, false);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		OH.c().addObserver(observer);
		mInflater = getActivity().getLayoutInflater();

		mList = (ListView) findViewById(R.id.list);
		mAdapter = new MessageAdapter();
		mList.setAdapter(mAdapter);

		reload();

		mList.setOnItemLongClickListener(this);

	}
	private void reload() {
		try {
			load(GSFile.getLastMessage());
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

			if (OH.TYPE.MESSAGE == data) {
				reload();
			}
		}
	};

	private void load() {
		if (mAdapter.isEmpty())
			mAdapter.notifyDataSetInvalidated();
		else
			mAdapter.notifyDataSetChanged();

		save(GSFile.getLastMessage());
	}
	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		inflater.inflate(R.menu.add, menu);
		inflater.inflate(R.menu.io, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.menu_load :
				getLoadFile(GSFile.EXT.message.s());
				break;
			case R.id.menu_save :
				getSaveFile(GSFile.EXT.message.name());
				break;
			case R.id.menu_add :
				startAdd();
				break;
			case R.id.menu_del :
				delete();
				break;
			case R.id.menu_share :
				try {
					SparseBooleanArray positions = mList.getCheckedItemPositions();
					ArrayList<Message> selected = new ArrayList<Message>();
					for (int i = 0; i < positions.size(); i++) {
						int position = positions.keyAt(i);
						if (positions.get(position)) {
							selected.add(mListItem.get(position));
						}
					}
					if (selected.size() <= 0) {
						selected = mListItem;
					}

					StringBuilder sb = new StringBuilder();
					for (Message message : selected) {
						sb.append(message.getShareLink());
						sb.append("\r\n");
						sb.append("\r\n");
					}

					Intent intent = ShareHelper.getIntentTextShare(sb.toString());
					intent.putExtra(Intent.EXTRA_SUBJECT, "Message");

					ShareHelper.shareTo(mContext, intent);

				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			default :
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startAdd() {
		startActivityForResult(new Intent(Intent.ACTION_INSERT, Message.getNewUri()), C.requestCode.message_add.o());
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Message msg = (Message) parent.getItemAtPosition(position);

		Intent intent = new Intent(Intent.ACTION_EDIT, msg.getEditUri());
		intent.putExtra(C.EXTRA.message_position.s(), position);

		startActivityForResult(intent, C.requestCode.message_edit.o());

		return true;
	}
	private void delete() {
		SparseBooleanArray positions = mList.getCheckedItemPositions();
		Set<Object> remove = new HashSet<Object>();
		for (int i = 0; i < positions.size(); i++) {
			int position = positions.keyAt(i);
			if (positions.get(position)) {
				remove.add(mListItem.get(position));
			}
		}
		mList.clearChoices();
		mListItem.removeAll(remove);
		load();
	}
	protected void load(String pathfile) {
		super.load(pathfile);
		ArrayList<Message> msg = null;
		if ((msg = GSFile.load(pathfile, msg)) != null) {
			mListItem = msg;
			load();
		}
	}
	protected void save(String pathfile) {
		super.save(pathfile);
		GSFile.save(pathfile, mListItem);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		String key = data.getStringExtra(C.EXTRA.key.s());
		String value = data.getStringExtra(C.EXTRA.value.s());
		switch (C.requestCode.values()[requestCode]) {
			case message_add :
				mListItem.add(new Message().set(key, value));
				load();
				break;
			case message_edit :
				int position = data.getIntExtra(C.EXTRA.message_position.s(), -1);
				if (position == -1)
					return;
				mListItem.get(position).set(key, value);
				load();
				break;
			default :
				break;
		}

	}

	public class MessageAdapter extends BaseAdapter {
		public class Holder {
			public TextView key;
			public TextView value;

			public View set(View v) {
				key = (TextView) v.findViewById(android.R.id.text1);
				value = (TextView) v.findViewById(android.R.id.text2);
				v.setTag(this);
				return v;
			}
		}

		@Override
		public int getCount() {
			return mListItem.size();
		}

		@Override
		public Object getItem(int position) {
			return mListItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				if (UT.isOverHoneycomb()) {
					convertView = new Holder().set(mInflater.inflate(android.R.layout.simple_list_item_activated_2, parent, false));
				} else {
					convertView = new Holder().set(mInflater.inflate(com.smart_c.R.layout.smc_simple_list_item_2_checked, parent, false));
				}
			}
			try {
				final Holder h = (Holder) convertView.getTag();
				final Message d = mListItem.get(position);
				h.key.setText(d.key);
				h.value.setText(d.value);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	public ArrayList<Message> getSelected() {
		ArrayList<Message> selected_messages = new ArrayList<Message>();
		SparseBooleanArray positions = mList.getCheckedItemPositions();
		for (int i = 0; i < positions.size(); i++) {
			int position = positions.keyAt(i);
			if (positions.get(position)) {
				selected_messages.add(mListItem.get(position));
			}
		}
		if (selected_messages.size() > 0)
			return selected_messages;

		if (BaseP.c().getBoolean("noisall_message"))
			return mListItem;
		else
			return selected_messages;
	}

}
