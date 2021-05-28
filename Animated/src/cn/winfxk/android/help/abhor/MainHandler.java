package cn.winfxk.android.help.abhor;

import cn.pedant.SweetAlert.BaseSweet;
import cn.pedant.SweetAlert.OnSweetClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.winfxk.android.help.abhor.main.Main;
import cn.winfxk.android.help.abhor.tool.Toast;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

/**
 * @Createdate 2021/05/27 17:27:25
 * @author Winfxk
 */
public class MainHandler extends Handler implements OnSweetClickListener {
	private MainActivity activity;
	protected transient String Message;
	protected transient String name, pass;

	public MainHandler(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 2:
			activity.startActivity(new Intent(activity, Main.class));
			activity.finish();
			return;
		case 1:
			activity.dialog.setMessage("授权成功！");
			activity.dialog.setType(SweetAlertDialog.SUCCESS_TYPE);
			activity.dialog.setConfirmClickListener(this);
			return;
		case 0:
			MainActivity.config.set("Name", name);
			MainActivity.config.set("Pass", pass);
			MainActivity.config.save();
			activity.dialog.setMessage("验证成功！");
			Toast.makeText(activity, (Message != null && Message.isEmpty()) ? Message : "登陆成功！").show();
			activity.dialog.setType(SweetAlertDialog.SUCCESS_TYPE);
			activity.dialog.setCancelClickListener(BaseSweet.Listener);
			activity.dialog.setConfirmText("确定");
			new MainThread(activity, 2).start();
			return;
		case -1:
			activity.dialog.setType(SweetAlertDialog.ERROR_TYPE);
			activity.dialog.setConfirmClickListener(BaseSweet.NoListener);
			activity.dialog.setConfirmText("确定");
			activity.dialog.setMessage("验证失败！");
			Toast.makeText(activity, (Message != null && Message.isEmpty()) ? Message : "无法连接至服务器！").show();
			return;
		}
	}

	@Override
	public void onClick(SweetAlertDialog sweetAlertDialog) {
		MainActivity.config.set("OldStart", true);
		MainActivity.config.save();
		activity.dialog.dismiss();
	}
}
