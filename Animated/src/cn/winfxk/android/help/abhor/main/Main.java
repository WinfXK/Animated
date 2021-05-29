package cn.winfxk.android.help.abhor.main;

import cn.pedant.SweetAlert.BaseSweet;
import cn.pedant.SweetAlert.OnSweetClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.winfxk.android.help.abhor.MainActivity;
import cn.winfxk.android.help.abhor.R;
import cn.winfxk.android.help.abhor.tool.MyBuilder;
import cn.winfxk.android.help.abhor.tool.Toast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Createdate 2021/05/27 20:53:59
 * @author Winfxk
 */
public class Main extends Activity implements OnItemClickListener, OnClickListener, android.view.View.OnClickListener,
		TextWatcher, OnLongClickListener, OnSweetClickListener {
	protected ImageButton add, admin, reload;
	protected ListView listView, listView2;
	protected MainHandler handler;
	protected transient boolean isAdmin = false, isNew = false, isHttp = false, isAdd = false;
	protected String Pass, Name, ToID;
	protected TextView textView;
	protected MainAdapter adapter;
	private EditText editText;
	protected transient SweetAlertDialog dialog;
	protected ListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		add = (ImageButton) findViewById(R.id.imageButton1);
		admin = (ImageButton) findViewById(R.id.imageButton2);
		reload = (ImageButton) findViewById(R.id.imageButton3);
		listView = (ListView) findViewById(R.id.listView1);
		textView = (TextView) findViewById(R.id.textView1);
		handler = new MainHandler(this);
		listView.setAdapter(adapter = new MainAdapter(this));
		listView.setOnItemClickListener(this);
		Pass = MainActivity.config.getString("Pass");
		ToID = Name = MainActivity.config.getString("Name");
		Toast.makeText(this, "请求数据中.....").show();
		isHttp = true;
		new MainThread(this, 0, ToID).start();
		new MainThread(this, 1).start();
		reload.setOnLongClickListener(this);
	}

	public void onAdd(View view) {
		view = View.inflate(this, R.layout.temperature, null);
		editText = (EditText) view.findViewById(R.id.editText1);
		Builder builder = new Builder(this);
		builder.setView(view);
		builder.setTitle("请输入您的体温~");
		builder.setCancelable(false);
		builder.setNegativeButton("确定", this);
		builder.setPositiveButton("取消", adapter);
		builder.show();
	}

	public void onAdmin(View view) {
		view = View.inflate(this, R.layout.listview, null);
		editText = (EditText) view.findViewById(R.id.editText1);
		listView2 = (ListView) view.findViewById(R.id.listView1);
		listView2.setAdapter(listAdapter = new ListAdapter(this));
		Builder builder = new Builder(this);
		editText.addTextChangedListener(this);
		builder.setView(view);
		builder.setTitle("请选择想要查看的对象");
		builder.setCancelable(false);
		builder.setPositiveButton("取消", adapter);
		final AlertDialog dis = builder.show();
		listView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Toast.makeText(Main.this, "请求数据中.....").show();
				isHttp = true;
				new MainThread(Main.this, 0, ToID = (((MyData) arg1.getTag()).textView1.getText().toString())).start();
				dis.dismiss();
			}
		});
	}

	@SuppressWarnings("unused")
	public void onReload(View view) {
		if (isHttp)
			return;
		reload.setVisibility(View.INVISIBLE);
		isHttp = true;
		new MainThread(this, 0, ToID).start();
	}

	@SuppressWarnings("unused")
	public void onExit(View view) {
		SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
		dialog.setMessage("确定要注销吗？");
		dialog.setCancelable(false);
		dialog.setConfirmClickListener(this);
		dialog.setConfirmText("OK");
		dialog.setCancelClickListener(BaseSweet.NoListener);
		dialog.setCancelText("No");
		dialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MyBuilder builder = new MyBuilder(this);
		MyData data = (MyData) arg1.getTag();
		builder.setMsg(
				textView.getText() + "\n打卡体温：" + data.textView1.getText() + "\n打卡时间：" + data.textView2.getText());
		builder.setCancelable(false);
		builder.setNegativeButton("确定", this);
		builder.show();
	}

	@Override
	public void onClick(DialogInterface d, int which) {
		String string = editText.getText().toString();
		if (string == null || string.isEmpty()) {
			Toast.makeText(this, "请输入您的体温！").show();
			return;
		}
		dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		isAdd = true;
		dialog.setCancelable(false);
		dialog.setConfirmClickListener(BaseSweet.Listener);
		dialog.setConfirmText("OK");
		dialog.setMessage("提交中....");
		dialog.show();
		new MainThread(this, 2, string).start();
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		listAdapter.reload(s.toString()).notifyDataSetChanged();
	}

	@Override
	public boolean onLongClick(View v) {
		ToID = Name;
		Toast.makeText(this, "查看自己的数据").show();
		isHttp = true;
		new MainThread(this, 0, ToID).start();
		return true;
	}

	@Override
	public void onClick(SweetAlertDialog sweetAlertDialog) {
		MainActivity.config.remove("Name");
		MainActivity.config.remove("Pass");
		MainActivity.config.save();
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
}
