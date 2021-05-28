package cn.winfxk.android.help.abhor;

import java.io.File;

import cn.pedant.SweetAlert.BaseSweet;
import cn.pedant.SweetAlert.OnSweetClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.winfxk.android.help.abhor.sign.Sign;
import cn.winfxk.android.help.abhor.tool.Config;
import cn.winfxk.android.help.abhor.tool.MyBuilder;
import cn.winfxk.android.help.abhor.tool.Toast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements OnSweetClickListener {
	private EditText name, pass;
	protected MainHandler handler;
	protected transient boolean ToSign = false;
	protected SweetAlertDialog dialog;
	public static Config config;
	public static final String[] Permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (config == null)
			config = new Config(new File(getFilesDir(), "Config.yml"));
		setContentView(R.layout.activity_main);
		name = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText2);
		handler = new MainHandler(this);
		getPermissions();
		String Name = config.getString("Name");
		String Pass = config.getString("Pass");
		if (Name != null && !Name.isEmpty() && Pass != null && !Pass.isEmpty()) {
			name.setText(Name);
			pass.setText(Pass);
			onSign(null);
		}
	}

	@SuppressLint("NewApi")
	protected boolean isPermissions() {
		for (String str : MainActivity.Permissions)
			if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED)
				return false;
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(SweetAlertDialog sweetAlertDialog) {
		if (dialog != null)
			dialog.setMessage("等待授权中....");
		this.requestPermissions(Permissions, 1);
		new MainThread(this, 2).start();
	}

	private void getPermissions() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (!isPermissions()) {
				dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
				dialog.setMessage("我们需要一些权限...");
				dialog.setConfirmText("确定");
				dialog.setCancelable(false);
				dialog.show();
				dialog.setConfirmClickListener(this);
				return;
			} else {
				config.set("OldStart", true);
				config.save();
				return;
			}
		} else if (!config.getBoolean("OldStart")) {
			config.set("OldStart", true);
			config.save();
			MyBuilder builder = new MyBuilder(this);
			builder.setTitle("提示");
			builder.setCancelable(false);
			builder.setMsg("我们需要一些权限才能正常运行！\n这对你的手机不会造成什么损害也不会泄露你的隐私，请放心授予！");
			builder.setNegativeButton("确定");
			builder.show();
			return;
		}
	}

	@SuppressWarnings("unused")
	public void onSign(View view) {
		if (ToSign) {
			Toast.makeText(this, "请求中！请稍后......").show();
			return;
		}
		String n = name.getText().toString();
		String p = pass.getText().toString();
		if (n == null || n.isEmpty() || p == null || p.isEmpty()) {
			dialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
			dialog.setCancelable(false);
			dialog.setMessage("请输入" + ((n == null || n.isEmpty()) ? "账号" : "密码"));
			dialog.setConfirmClickListener(BaseSweet.NoListener);
			dialog.setConfirmText("确定");
			dialog.show();
			return;
		}
		ToSign = true;
		dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		dialog.setCancelable(false);
		dialog.setMessage("请求中.....");
		dialog.setConfirmClickListener(BaseSweet.Listener);
		dialog.show();
		new MainThread(this, n, p).start();
	}

	@SuppressWarnings("unused")
	public void toSign(View view) {
		if (ToSign)
			return;
		startActivityForResult(new Intent(this, Sign.class), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				name.setText(data.getStringExtra("name"));
				pass.setText(data.getStringExtra("pass"));
				onSign(null);
			}
			break;

		default:
			break;
		}
	}
}
