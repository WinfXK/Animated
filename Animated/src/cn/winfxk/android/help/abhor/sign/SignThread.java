package cn.winfxk.android.help.abhor.sign;

import java.util.Map;

import cn.winfxk.android.help.abhor.tool.Http;
import cn.winfxk.android.help.abhor.tool.MyMap;
import cn.winfxk.android.help.abhor.tool.Tool;

/**
 * @Createdate 2021/05/27 22:11:59
 * @author Winfxk
 */
public class SignThread extends Thread {
	private Sign activity;
	private String name, sign, pass;
	private int Key;

	public SignThread(Sign main, int Key) {
		this.Key = Key;
		this.activity = main;
	}

	public SignThread(Sign main, String name, String sign, String pass) {
		this.activity = main;
		this.name = name;
		this.sign = sign;
		this.pass = pass;
		Key = 0;
	}

	@Override
	public void run() {
		switch (Key) {
		case 1:
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			activity.handler.sendEmptyMessage(1);
			return;
		case 0:
			try {
				Map<String, Object> map = Http
						.get(MyMap.make("Key", (Object) "sign").add("Pass", pass).add("Name", name).add("Sign", sign));
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
				activity.isThread = false;
			}
			return;
		}
	}
}
