package cn.winfxk.android.help.abhor.main;

import java.util.ArrayList;
import java.util.List;

import cn.winfxk.android.help.abhor.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @Createdate 2021/05/28 22:55:54
 * @author Winfxk
 */
public class ListAdapter extends BaseAdapter {
	private Main main;
	private transient static List<String> list;
	protected transient List<String> data;

	public ListAdapter(Main main) {
		this.main = main;
		data = new ArrayList<>(list);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected ListAdapter reload(String string) {
		if (string == null || string.isEmpty()) {
			data = new ArrayList<>(list);
			return this;
		}
		data.clear();
		for (String s : list) {
			if (s.equals(string) || s.contains(string))
				data.add(s);
		}
		return this;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		MyData data;
		if (view == null) {
			view = View.inflate(main, R.layout.item, null);
			data = new MyData(view.findViewById(R.id.textView1), view.findViewById(R.id.textView2));
			view.setTag(data);
		} else
			data = (MyData) view.getTag();
		data.textView1.setText(getItem(position));
		data.textView2.setText(getItem(position));
		return view;
	}

	public synchronized static void setList(List<String> list) {
		ListAdapter.list = list;
	}
}
