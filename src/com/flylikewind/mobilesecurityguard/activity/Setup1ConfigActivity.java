package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.flylikewind.mobilesecurityguard.R;

public class Setup1ConfigActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_config1);
	}

	public void next(View v) {
		Intent intent = new Intent(this, Setup2ConfigActivity.class);
		startActivity(intent);
		// 从当前任务里面移除
		finish();
		// 指定界面切换的动画
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}
}
