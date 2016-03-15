package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.utils.ScreenUtils;

public class DragViewActivity extends Activity implements OnTouchListener {

	private static final String TAG = "DragViewActivity";
	private SharedPreferences sp;
	private LinearLayout ll_drag_view;
	private TextView tv_drag_view;
	// 记录下来第一次手指触摸屏幕的位置
	private int startX;
	private int startY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drag_view);
		ll_drag_view = (LinearLayout) findViewById(R.id.ll_drag_view);
		tv_drag_view = (TextView) findViewById(R.id.tv_drag_view);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		ll_drag_view.setOnTouchListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 重新加载上次imageview在窗体中的位置
		int x = sp.getInt("lastx", 0);
		int y = sp.getInt("lasty", 0);
		Log.i(TAG, "x=" + x);
		Log.i(TAG, "y=" + y);
		// 通过布局更改iv_drag_view在窗体中的位置
		LayoutParams params = (LayoutParams) ll_drag_view.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		ll_drag_view.setLayoutParams(params);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		// 如果把手指放在imageview上面拖动
		case R.id.ll_drag_view:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 获取手指第一次接触屏幕在X方向的坐标
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			// 手指没有离开屏幕，在屏幕上移动
			case MotionEvent.ACTION_MOVE:
				int x = (int) event.getRawX();
				int y = (int) event.getRawY();
				// 获取tv_drag_view宽度
				int height = tv_drag_view.getBottom() - tv_drag_view.getTop();
				if (y < ScreenUtils.screenHeight / 2) {
					// tv_drag_view设置在窗体的下面
					tv_drag_view.layout(tv_drag_view.getLeft(),
							ScreenUtils.screenHeight - height,
							tv_drag_view.getRight(), ScreenUtils.screenHeight);
				} else {
					tv_drag_view.layout(tv_drag_view.getLeft(), 0,
							tv_drag_view.getRight(), height);
				}

				// 获取手指移动的距离
				int dx = x - startX;
				int dy = y - startY;
				int l = ll_drag_view.getLeft();
				int t = ll_drag_view.getTop();
				int r = ll_drag_view.getRight();
				int b = ll_drag_view.getBottom();
				ll_drag_view.layout(l + dx, t + dy, r + dx, b + dy);

				// 获取到移动后的位置
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			// 手指离开屏幕的事件
			case MotionEvent.ACTION_UP:
				Log.i(TAG, "手指离开屏幕!!!");
				// 记录下来imageview最后在窗体中的位置
				int lastX = ll_drag_view.getLeft();
				int lastY = ll_drag_view.getTop();
				Editor editor = sp.edit();
				editor.putInt("lastx", lastX);
				editor.putInt("lasty", lastY);
				editor.commit();
				break;
			}
			break;
		}
		// 不会中断触摸事件的返回
		return true;
	}
}
