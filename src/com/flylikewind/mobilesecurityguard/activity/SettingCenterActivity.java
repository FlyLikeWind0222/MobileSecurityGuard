package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.service.WatchDogService;

public class SettingCenterActivity extends Activity implements OnClickListener {

	private TextView tv_setting_applock;
	private Intent watchdogintent;
	private CheckBox cb_setting_center_auto_update, cb_setting_applock;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_center);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		cb_setting_center_auto_update = (CheckBox) findViewById(R.id.cb__setting_center_auto_update);
		cb_setting_applock = (CheckBox) findViewById(R.id.cb_setting_applock);
		tv_setting_applock = (TextView) findViewById(R.id.tv_setting_applock);
		boolean isLockServiceOpen = sp.getBoolean("islockserviceopen", false);
		if (isLockServiceOpen) {
			tv_setting_applock.setText("程序锁服务已经开启");
			tv_setting_applock.setTextColor(getResources().getColor(
					R.color.text_on_textView));
			cb_setting_applock.setChecked(true);
		} else {
			tv_setting_applock.setTextColor(Color.RED);
			tv_setting_applock.setText("程序锁服务已经停止");
		}
		watchdogintent = new Intent(this, WatchDogService.class);
		boolean isAutoUpdate = sp.getBoolean("isautoupdate", true);
		if (isAutoUpdate) {
			cb_setting_center_auto_update.setChecked(true);
			cb_setting_center_auto_update.setText("自动更新已开启");
		} else {
			cb_setting_center_auto_update.setChecked(false);
			cb_setting_center_auto_update.setText("自动更新已关闭");
		}
		cb_setting_center_auto_update.setOnClickListener(this);
		cb_setting_applock
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							startService(watchdogintent);
							tv_setting_applock.setText("程序锁服务已经开启");
							tv_setting_applock.setTextColor(getResources()
									.getColor(R.color.text_on_textView));
							Editor editor = sp.edit();
							editor.putBoolean("islockserviceopen", true);
							editor.commit();
						} else {
							stopService(watchdogintent); // ->ondestroy->flag
															// false->停止子线程
							tv_setting_applock.setText("程序锁服务已经停止");
							tv_setting_applock.setTextColor(Color.RED);
							Editor editor = sp.edit();
							editor.putBoolean("islockserviceopen", false);
							editor.commit();
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.cb__setting_center_auto_update:
			boolean isAutoUpdate = sp.getBoolean("isautoupdate", true);
			Editor editor = sp.edit();
			if (isAutoUpdate) {
				cb_setting_center_auto_update.setChecked(false);
				cb_setting_center_auto_update.setText("自动更新已关闭");
				editor.putBoolean("isautoupdate", false);
			} else {
				cb_setting_center_auto_update.setChecked(true);
				cb_setting_center_auto_update.setText("自动更新已开启");
				editor.putBoolean("isautoupdate", true);
			}
			editor.commit();
			break;
		default:
			break;
		}
	}
}
