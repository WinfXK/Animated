package cn.winfxk.android.help.abhor.tool;

import cn.winfxk.android.help.abhor.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @Createdate 2021/04/25 17:31:37
 * @author Winfxk
 */
public class CircleLayout extends RelativeLayout {
	private int color;
	private int[] colors;
	private int alpha;
	private Paint mPaint = new Paint();

	public CircleLayout(Context context) {
		super(context);
	}

	public CircleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		setWillNotDraw(false);
	}

	private void init(Context context, AttributeSet attrs) {
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleRelativeLayoutLayout);
		color = array.getColor(R.styleable.CircleRelativeLayoutLayout_background_color, 0X0000000);
		alpha = array.getInteger(R.styleable.CircleRelativeLayoutLayout_background_alpha, 100);
		setColors();
		array.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = getMeasuredWidth();
		mPaint.clearShadowLayer();
		mPaint.setARGB(alpha, colors[0], colors[1], colors[2]);
		mPaint.setAntiAlias(true);
		float cirX = width / 2;
		float cirY = width / 2;
		float radius = width / 2;
		canvas.drawCircle(cirX, cirY, radius, mPaint);
		super.onDraw(canvas);
	}

	public void setColor(int color) {
		this.color = color;
		setColors();
		invalidate();
	}

	public void setAlhpa(int alhpa) {
		this.alpha = alhpa;
		invalidate();
	}

	public void setColors() {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		this.colors = new int[] { red, green, blue };
	}

}
