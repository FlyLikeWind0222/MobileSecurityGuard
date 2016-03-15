package com.flylikewind.mobilesecurityguard.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.utils.MD5;

public class LostProtectedActivity extends BaseActivity {
	private SharedPreferences sp;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String pwd = sp.getString("pwd", "");

		// 判断之前有木有设置密码
		// 如果密码能取到说明已经设置密码了
		if ("".equals(pwd)) {
			// 密码为空,第一次进入手机防盗界面
			showFirstEntryDialog();
		} else {
			// 已经设置密码了，正常进入手机防盗界面
			showNormalEntryDialog();
		}

	}

	/**
	 * 输入已经设置的密码，正常进入手机防盗界面
	 */
	private void showNormalEntryDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = inflater.inflate(R.layout.nomal_dialog, null);
		builder.setTitle("请输入密码");
		builder.setView(view);
		final Dialog d = builder.create();
		final EditText et_first_one = (EditText) view
				.findViewById(R.id.et_first_one);

		// 处理确定按钮的事件
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pwd = et_first_one.getText().toString();
				String md5_pwd = MD5.getData(pwd);
				String old_pwd = sp.getString("pwd", "");

				if ("".equals(pwd)) {
					Toast.makeText(getApplicationContext(), "密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (old_pwd.equals(md5_pwd)) {
					// 密码一致, 进入手机防盗向导界面
					loadGuideUI();
					d.dismiss();
					finish();
				} else {
					// 密码不一致
					Toast.makeText(getApplicationContext(), "您的密码输入有误,请重新输入！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// 取消事件
		view.findViewById(R.id.quit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
				finish();
			}
		});

		// 显示对话框
		d.show();
	}

	/**
	 * 进入手机向导界面
	 */
	protected void loadGuideUI() {
		// 是否完成设置向导
		boolean isSetup = sp.getBoolean("issetup", false);
		if (isSetup) {
			// 直接进入手机防盗设置界面
			Intent intent = new Intent(this, LostProtectedSettingActivity.class);
			startActivity(intent);
		} else {
			//进入防盗向导界面
			Intent intent=new Intent(this,Setup1ConfigActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 设置密码，第一次进入手机防盗界面
	 */
	private void showFirstEntryDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = inflater.inflate(R.layout.first_dialog, null);
		builder.setCancelable(false);
		builder.setTitle("请输入密码");
		builder.setView(view);
		final Dialog d = builder.create();
		final EditText et_first_one = (EditText) view
				.findViewById(R.id.et_first_one);
		final EditText et_next_one = (EditText) view
				.findViewById(R.id.et_next_one);

		// 处理确定按钮的事件
		view.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String first = et_first_one.getText().toString();
				String next = et_next_one.getText().toString();
				if ("".equals(first) || "".equals(next)) {
					Toast.makeText(getApplicationContext(), "密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else if (first.equals(next)) {
					// 对密码进行加密
					String md5_pwd = MD5.getData(first);
					// 密码相同保存密码
					Editor editor = sp.edit();
					editor.putString("pwd", md5_pwd);
					editor.commit();

					d.dismiss();
					finish();
				} else {
					// 密码不一致
					Toast.makeText(getApplicationContext(),
							"您两次输入的密码不同，请重新输入密码！", Toast.LENGTH_SHORT).show();
				}
				
				//进入手机设置向导界面
				loadGuideUI();
			}
		});
		// 取消事件
		view.findViewById(R.id.quit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
				finish();
			}
		});

		d.show();
	}
}
