package cn.winfxk.android.help.abhor.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.winfxk.android.help.abhor.R;
import cn.winfxk.android.help.abhor.tool.Tool;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @Createdate 2021/05/28 17:24:26
 * @author Winfxk
 */
public class MainAdapter extends BaseAdapter implements OnClickListener {
	private Main main;
	private transient Map<String, Object> map;
	protected  transient List<String> list;

	public MainAdapter(Main main) {
		this.main = main;
		setData(new HashMap<String, Object>());
	}

	@Override
	public int getCount() {
		return map.size();
	}

	@Override
	public Object getItem(int position) {
		return map.get(list.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int id, View view, ViewGroup parent) {
		MyData data;
		if (view == null) {
			view = View.inflate(main, R.layout.item, null);
			data = new MyData(view.findViewById(R.id.textView1), view.findViewById(R.id.textView2));
			view.setTag(data);
		} else
			data = (MyData) view.getTag();
		Object object = this.map.get(list.get(id));
		Map<String, Object> map = (Map<String, Object>) ((object != null && object instanceof Map) ? object
				: new HashMap<>());
		data.textView1.setText(Tool.objToString(map.get("Heat"), "未知"));
		data.textView2.setText(Tool.objToString(map.get("Date"), "未知"));
		return view;
	}

	public synchronized MainAdapter setData(Map<String, Object> map) {
		this.map = map;
		list = new ArrayList<>(map.keySet());
		return this;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	}
}
