package cn.winfxk.android.help.abhor.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.winfxk.android.help.abhor.tool.Http;
import cn.winfxk.android.help.abhor.tool.MyMap;
import cn.winfxk.android.help.abhor.tool.Tool;

/**
 * @Createdate 2021/05/27 23:06:42
 * @author Winfxk
 */
public class MainThread extends Thread {
	private Main main;
	private int Key;
	private String ID;

	public MainThread(Main main, int Key) {
		this.main = main;
		this.Key = Key;
	}

	public MainThread(Main main, int Key, String ID) {
		this.main = main;
		this.Key = Key;
		this.ID = ID;
	}

	@Override
	public void run() {
		try {
			Map<String, Object> map;
			Object object;
			switch (Key) {
			case 2:
				try {
					map = Http.get(MyMap.make("Key", (Object) "add").add("Name", main.Name).add("Pass", main.Pass)
							.add("C", ID));
					if (map == null || map.size() <= 0) {
						main.handler.Message = null;
						main.handler.sendEmptyMessage(-2);
						return;
					}
					main.handler.Message = Tool.objToString(map.get("Text"));
					if (!Tool.ObjToBool(map.get("ret"))) {
						main.handler.sendEmptyMessage(-2);
						return;
					}
					main.handler.map = map;
					main.handler.sendEmptyMessage(3);
				} catch (Exception e) {
					e.printStackTrace();
					main.handler.Message = null;
					main.handler.sendEmptyMessage(-2);
				} finally {
					main.isAdd = false;
				}
				return;
			case 1:
				while (true) {
					main.handler.sendEmptyMessage(1);
					sleep(10);
				}
			case 0:
				try {
					map = Http.get(MyMap.make("Key", (Object) "get").add("Name", main.Name).add("Pass", main.Pass)
							.add("ID", ID));
					if (map == null || map.size() <= 0) {
						main.handler.Message = null;
						main.handler.sendEmptyMessage(-1);
						return;
					}
					main.handler.Message = Tool.objToString(map.get("Text"));
					if (!Tool.ObjToBool(map.get("ret"))) {
						main.handler.sendEmptyMessage(-1);
						return;
					}
					object = map.get("Item");
					main.adapter
							.setData((Map<String, Object>) (object == null || !(object instanceof Map) ? new HashMap<>()
									: object));
					object = map.get("List");
					ListAdapter.setList(
							(List<String>) (object == null || !(object instanceof List) ? new ArrayList<>() : object));
					main.handler.map = map;
					main.handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
					main.handler.Message = null;
					main.handler.sendEmptyMessage(-1);
				} finally {
					main.isHttp = false;
					main.handler.sendEmptyMessage(2);
				}
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
