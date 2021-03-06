package cn.winfxk.android.help.abhor.tool;
import cn.winfxk.android.help.abhor.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Author: liuqiang Time: 2018-01-02 13:28 Description:
 */
@SuppressWarnings("deprecation")
@SuppressLint({ "InflateParams", "NewApi" })
public class MyBuilder {
	private Dialog dialog;
	private LinearLayout container;
	private TextView titleTv;
	private TextView msgTv;
	private Button negBtn;
	private Button posBtn;
	private ImageView img_line;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;

	public static MyBuilder makeBuilder(Activity activity, String Message) {
		return makeBuilder(activity, "提示", Message, new BuilderData("确定", (OnClickListener) null), null, false);
	}

	public static MyBuilder makeBuilder(Activity activity, String Title, String Message, String Button1,
			OnClickListener listener) {
		return makeBuilder(activity, Title, Message, new BuilderData(Button1, listener), null, false);
	}

	public static MyBuilder makeBuilder(Activity activity, String Title, String Message, BuilderData Button1) {
		return makeBuilder(activity, Title, Message, Button1, null, false);
	}

	public static MyBuilder makeBuilder(Activity activity, String Title, String Message, BuilderData Button1,
			BuilderData Button2, boolean isCancelable) {
		MyBuilder builder = new MyBuilder(activity);
		if (Title != null)
			builder.setTitle(Title);
		if (Message != null)
			builder.setMsg(Message);
		if (Button1 != null)
			builder.setPositiveButton(Button1.Button1, Button1.listener1);
		if (Button2 != null)
			builder.setNegativeButton(Button2.Button2, Button2.listener2);
		builder.setCancelable(isCancelable);
		return builder;
	}

	public static class BuilderData {
		public String Button1, Button2;
		public OnClickListener listener1, listener2;

		public BuilderData(String Button1, OnClickListener listener1, String Button2, OnClickListener listener2) {
			this.Button1 = Button1;
			this.Button2 = Button2;
			this.listener1 = listener1;
			this.listener2 = listener2;
		}

		public BuilderData(String Button1, OnClickListener listener) {
			this.Button1 = Button1;
			this.listener1 = listener;
		}

		public BuilderData(String Button1, String Button2) {
			this.Button1 = Button1;
			this.Button2 = Button2;
		}
	}

	public MyBuilder(Activity context) {
		View view = View.inflate(context, R.layout.view_alertdialog, null);
		container = (LinearLayout) view.findViewById(R.id.container);
		titleTv = (TextView) view.findViewById(R.id.txt_title);
		titleTv.setVisibility(View.GONE);
		titleTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		msgTv = (TextView) view.findViewById(R.id.txt_msg);
		msgTv.setVisibility(View.GONE);
		negBtn = (Button) view.findViewById(R.id.btn_neg);
		negBtn.setVisibility(View.GONE);
		posBtn = (Button) view.findViewById(R.id.btn_pos);
		posBtn.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		container.setLayoutParams(
				new FrameLayout.LayoutParams((int) (dm.widthPixels * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	public MyBuilder setTitle(String title) {
		showTitle = true;
		if (title != null && "".equals(title))
			title = "标题";
		if (Build.VERSION.SDK_INT >= 24)
			titleTv.setText(Html.fromHtml("<b>" + title + "</b>", 1));
		else
			titleTv.setText(Html.fromHtml("<b>" + title + "</b>"));
		return this;
	}

	public MyBuilder setMsg(String msg) {
		showMsg = true;
		if (msg != null && "".equals(msg)) {
			msgTv.setText("内容");
		} else
			msgTv.setText(msg);
		return this;
	}

	public MyBuilder setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public MyBuilder setPositiveButton(String text) {
		return setPositiveButton(text, null);
	}

	public MyBuilder setPositiveButton(String text, final View.OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			posBtn.setText("确定");
		} else
			posBtn.setText(text);
		posBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public MyBuilder setNegativeButton(String text) {
		return setNegativeButton(text, null);
	}

	public MyBuilder setNegativeButton(String text, final View.OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			negBtn.setText("取消");
		} else
			negBtn.setText(text);
		negBtn.setTextColor(0xffee0000);
		negBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			titleTv.setText("提示");
			titleTv.setVisibility(View.VISIBLE);
		}
		if (showTitle)
			titleTv.setVisibility(View.VISIBLE);
		if (showMsg)
			msgTv.setVisibility(View.VISIBLE);
		if (!showPosBtn && !showNegBtn) {
			posBtn.setText("确定");
			posBtn.setVisibility(View.VISIBLE);
			posBtn.setBackgroundResource(R.drawable.alertdialog_single_selector);
			posBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		if (showPosBtn && showNegBtn) {
			posBtn.setVisibility(View.VISIBLE);
			posBtn.setBackgroundResource(R.drawable.alertdialog_right_selector);
			negBtn.setVisibility(View.VISIBLE);
			negBtn.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}
		if (showPosBtn && !showNegBtn) {
			posBtn.setVisibility(View.VISIBLE);
			posBtn.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
		if (!showPosBtn && showNegBtn) {
			negBtn.setVisibility(View.VISIBLE);
			negBtn.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}
