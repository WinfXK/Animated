package cn.winfxk.android.help.abhor;

import java.util.Map;

import cn.winfxk.android.help.abhor.tool.Http;
import cn.winfxk.android.help.abhor.tool.MyMap;
import cn.winfxk.android.help.abhor.tool.Tool;

/**
 * @Createdate 2021/05/27 17:26:50
 * @author Winfxk
 */
public class MainThread extends Thread {
	private MainActivity activity;
	private String name, pass;
	private int Key;

	public MainThread(MainActivity activity, int Key) {
		this.activity = activity;
		this.Key = Key;
	}

	public MainThread(MainActivity activity, String name, String pass) {
		this.activity = activity;
		this.name = name;
		this.pass = pass;
		Key = 0;
	}

	@Override
	public void run() {
		try {
			switch (Key) {
			case 2:
				sleep(1000);
				activity.handler.sendEmptyMessage(2);
				return;
			case 1:
				while (true) {
					if (activity.isPermissions())
						break;
					sleep(100);
				}
				activity.handler.sendEmptyMessage(1);
				return;
			case 0:
				try {
					Map<String, Object> map = Http
							.get(MyMap.make("Key", (Object) "login").add("Pass", pass).add("Name", name));
					if (map == null || map.size() <= 0) {
						activity.handler.Message = null;
						activity.handler.sendEmptyMessage(-1);
						return;
					}
					activity.handler.Message = Tool.objToString(map.get("Text"));
					if (Tool.ObjToBool(map.get("ret"))) {
						activity.handler.name = name;
						activity.handler.pass = pass;
						activity.handler.sendEmptyMessage(0);
						return;
					}
					activity.handler.sendEmptyMessage(-1);
					return;
				} catch (Exception e) {
					e.printStackTrace();
					activity.handler.Message = null;
					activity.handler.sendEmptyMessage(-1);
				} finally {
					activity.ToSign = false;
				}
			}
		} catch (InterruptedException e) {
		}
	}
}
