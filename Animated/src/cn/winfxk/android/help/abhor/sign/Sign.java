package cn.winfxk.android.help.abhor.sign;

import cn.pedant.SweetAlert.BaseSweet;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.winfxk.android.help.abhor.R;
import cn.winfxk.android.help.abhor.tool.Toast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * @Createdate 2021/05/27 17:23:34
 * @author Winfxk
 */
public class Sign extends Activity {
	private EditText name, pass, sign;
	protected SweetAlertDialog dialog;
	protected transient boolean isThread = false;
	protected SignHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		name = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText3);
		sign = (EditText) findViewById(R.id.editText2);
		handler = new SignHandler(this);
	}

	@SuppressWarnings("unused")
	public void onSign(View view) {
		if (isThread) {
			Toast.makeText(this, "请求中请稍后....").show();
			return;
		}
		String n = name.getText().toString();
		String p = pass.getText().toString();
		String s = sign.getText().toString();
		if (n == null || n.isEmpty() || p == null || p.isEmpty() || s == null || s.isEmpty()) {
			dialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
			dialog.setConfirmClickListener(BaseSweet.NoListener);
			dialog.setConfirmText("确定");
			dialog.setCancelable(false);
			dialog.setMessage("请输入" + ((n == null || n.isEmpty()) ? "账号" : ((p == null || p.isEmpty()) ? "密码" : "昵称")));
			dialog.show();
			return;
		}
		dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		dialog.setConfirmClickListener(BaseSweet.NoListener);
		dialog.setConfirmText("确定");
		dialog.setCancelable(false);
		dialog.setMessage("请求中...");
		dialog.show();
		isThread = true;
		new SignThread(this, n, s, p).start();
	}

	@Override
	public void onBackPressed() {
		toLogin(null);
	}

	@SuppressWarnings("unused")
	public void toLogin(View view) {
		this.setResult(RESULT_CANCELED);
		finish();
	}
}
