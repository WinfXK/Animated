package cn.pedant.SweetAlert;

/**
 * @Createdate 2021/04/24 10:48:35
 * @author Winfxk
 */
public class BaseSweet implements OnSweetClickListener {
	/**
	 * 点击后无响应
	 */
	public static final BaseSweet Listener = new BaseSweet();
	/**
	 * 点击后关闭
	 */
	public static final BaseSweet NoListener = new BaseSweet(true);
	private boolean isClose = true;

	public BaseSweet(boolean isClose) {
		this.isClose = isClose;
	}

	public BaseSweet() {
	}

	@Override
	public void onClick(SweetAlertDialog sweetAlertDialog) {
		if (isClose)
			sweetAlertDialog.dismiss();
	}
}
