package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.flylikewind.mobilesecurityguard.R;

public class Setup4ConfigActivity extends Activity implements OnClickListener {

	private SharedPreferences sp;
	private CheckBox cb_setup4_start_protect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_config4);

		cb_setup4_start_protect = (CheckBox) findViewById(R.id.cb_setup4_start_protect);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		cb_setup4_start_protect.setOnClickListener(this);

		boolean isProtected = sp.getBoolean("isprotected", false);
		if (isProtected) {
			cb_setup4_start_protect.setChecked(true);
			cb_setup4_start_protect.setText("防盗保护已经开启");
		} else {
			cb_setup4_start_protect.setChecked(false);
			cb_setup4_start_protect.setText("防盗保护未开启");
		}
	}

	public void pre(View v) {
		Intent intent = new Intent(this, Setup3ConfigActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}

	public void next(View v) {
		// 判断防盗保护是否已经开启
		boolean isProtected = sp.getBoolean("isprotected", false);
		if (isProtected) {
			Editor editor = sp.edit();
			// 设置向导完成
			editor.putBoolean("issetup", true);
			editor.commit();

			// 跳转到防盗界面
			Intent intent = new Intent(this, LostProtectedSettingActivity.class);
			startActivity(intent);
			finish();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("强烈建议");
			builder.setMessage("手机防盗极大的保护了手机的安全，请开启防盗保护！");
			builder.setPositiveButton("开启",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							cb_setup4_start_protect.setChecked(true);
							cb_setup4_start_protect.setText("防盗保护已经开启");
							Editor editor = sp.edit();
							editor.putBoolean("isprotected", true);
							editor.putBoolean("issetup", true);
							// 设置向导完成
							editor.commit();
							
							// 跳转到防盗界面
							Intent intent = new Intent(Setup4ConfigActivity.this, LostProtectedSettingActivity.class);
							startActivity(intent);
							finish();
						}
					});
			builder.setNegativeButton("放弃",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Editor editor = sp.edit();
							editor.putBoolean("issetup", true);
							// 设置向导完成
							editor.commit();
							
							// 跳转到防盗界面
							Intent intent = new Intent(Setup4ConfigActivity.this, LostProtectedSettingActivity.class);
							startActivity(intent);
							finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.cb_setup4_start_protect:
			boolean isProtected = sp.getBoolean("isprotected", false);
			if (isProtected) {
				cb_setup4_start_protect.setChecked(false);
				cb_setup4_start_protect.setText("防盗保护未开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", false);
				editor.commit();
			} else {
				cb_setup4_start_protect.setChecked(true);
				cb_setup4_start_protect.setText("防盗保护已经开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", true);
				editor.commit();
			}
			break;
		default:
			break;
		}
	}
}
