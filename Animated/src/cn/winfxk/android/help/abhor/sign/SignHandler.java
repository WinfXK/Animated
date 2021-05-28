package cn.winfxk.android.help.abhor.sign;

import cn.pedant.SweetAlert.BaseSweet;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.winfxk.android.help.abhor.tool.Toast;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * @Createdate 2021/05/27 22:12:51
 * @author Winfxk
 */
public class SignHandler extends Handler {
	private Sign activity;
	protected transient String Message;
	protected transient String name, pass;

	public SignHandler(Sign sign) {
		activity = sign;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			Intent intent = new Intent();
			intent.putExtra("name", name);
			intent.putExtra("pass", pass);
			activity.setResult(Activity.RESULT_OK, intent);
			activity.finish();
			return;
		case 0:
			activity.dialog.setType(SweetAlertDialog.SUCCESS_TYPE);
			activity.dialog.setMessage("注册成功！");
			activity.dialog.setConfirmClickListener(BaseSweet.Listener);
			activity.dialog.setConfirmText("确定");
			Toast.makeText(activity, Message == null || Message.isEmpty() ? "注册成功！" : Message).show();
			new SignThread(activity, 1).start();
			return;
		case -1:
			activity.dialog.setType(SweetAlertDialog.ERROR_TYPE);
			activity.dialog.setMessage("请求失败！");
			activity.dialog.setConfirmClickListener(BaseSweet.NoListener);
			activity.dialog.setConfirmText("确定");
			Toast.makeText(activity, Message == null || Message.isEmpty() ? "无法连接至服务器！" : Message).show();
			return;
		}
	}
}
