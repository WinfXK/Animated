package cn.winfxk.android.help.abhor.main;

import android.view.View;
import android.widget.TextView;

/**
 * @Createdate 2021/05/28 17:29:22
 * @author Winfxk
 */
public class MyData {
	public TextView textView1, textView2;
	public MyData(View textView1, View textView2) {
		this.textView1 = (TextView) textView1;
		this.textView2 = (TextView) textView2;
	}
}
