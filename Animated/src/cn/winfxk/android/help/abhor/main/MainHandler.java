package cn.winfxk.android.help.abhor.main;

import java.util.Map;

import cn.pedant.SweetAlert.BaseSweet;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.winfxk.android.help.abhor.tool.Toast;
import cn.winfxk.android.help.abhor.tool.Tool;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * @Createdate 2021/05/27 23:07:37
 * @author Winfxk
 */
public class MainHandler extends Handler {
	private Main main;
	protected transient String Message;
	protected transient Map<String, Object> map;
	private int xy = 0;

	public MainHandler(Main main) {
		this.main = main;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case -2:
			main.dialog.setMessage("提交失败！");
			main.dialog.setConfirmClickListener(BaseSweet.NoListener);
			main.dialog.setType(SweetAlertDialog.ERROR_TYPE);
			Toast.makeText(main, (Message != null && Message.isEmpty()) ? Message : "无法连接至服务器！").show();
			return;
		case 3:
			main.dialog.setMessage("提交成功！");
			main.dialog.setConfirmClickListener(BaseSweet.NoListener);
			main.dialog.setType(SweetAlertDialog.SUCCESS_TYPE);
			Toast.makeText(main, (Message != null && Message.isEmpty()) ? Message : "提交成功！").show();
			main.onReload(null);
			return;
		case 2:
			main.reload.setVisibility(View.VISIBLE);
			return;
		case 1:
			main.reload.setRotation(xy = xy-- <= 0 ? 360 : xy);
			return;
		case 0:
			main.textView.setText(Tool.objToString(map.get("Sign"), "") + "的体温数据");
			main.admin.setVisibility(Tool.ObjToBool(map.get("Admin")) ? View.VISIBLE : View.INVISIBLE);
			main.add.setVisibility(main.adapter.list.contains(Tool.getDate()) ? View.INVISIBLE : View.VISIBLE);
			main.adapter.notifyDataSetChanged();
			if (main.adapter.getCount() <= 0)
				Toast.makeText(main, "当前你还没有体温数据！赶紧去添加一个吧~").show();
			return;
		case -1:
			Toast.makeText(main, (Message != null && Message.isEmpty()) ? Message : "无法连接至服务器！").show();
			return;
		}
	}
}
