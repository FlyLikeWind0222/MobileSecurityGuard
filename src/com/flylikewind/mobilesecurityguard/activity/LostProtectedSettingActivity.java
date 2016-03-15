package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flylikewind.mobilesecurityguard.R;

public class LostProtectedSettingActivity extends Activity implements
		OnClickListener {

	private static final int MENU_CHANGE_NAME_ID = 0;
	private TextView tv_lost_protected_setting_safe_number,
			tv_lost_protected_setting_reset;
	private CheckBox cb_lost_protected_setting_protecting;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.lost_protected_setting);

		String safe_number = sp.getString("safe_number", "");
		boolean isProtected = sp.getBoolean("isprotected", false);
		tv_lost_protected_setting_safe_number = (TextView) findViewById(R.id.tv_lost_protected_setting_safe_number);
		cb_lost_protected_setting_protecting = (CheckBox) findViewById(R.id.cb_lost_protected_setting_protecting);
		tv_lost_protected_setting_reset = (TextView) findViewById(R.id.tv_lost_protected_setting_reset);
		tv_lost_protected_setting_safe_number.setText(safe_number);
		tv_lost_protected_setting_reset.setOnClickListener(this);
		if (isProtected) {
			cb_lost_protected_setting_protecting.setChecked(true);
			cb_lost_protected_setting_protecting.setText("防盗保护已经开启");
		} else {
			cb_lost_protected_setting_protecting.setChecked(false);
			cb_lost_protected_setting_protecting.setText("防盗保护未开启");
		}
		cb_lost_protected_setting_protecting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_lost_protected_setting_reset:
			Intent intent = new Intent(this, Setup1ConfigActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.cb_lost_protected_setting_protecting:
			boolean isProtected = sp.getBoolean("isprotected", false);
			if (isProtected) {
				cb_lost_protected_setting_protecting.setChecked(false);
				cb_lost_protected_setting_protecting.setText("防盗保护未开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", false);
				editor.commit();
			} else {
				cb_lost_protected_setting_protecting.setChecked(true);
				cb_lost_protected_setting_protecting.setText("防盗保护已经开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", true);
				editor.commit();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CHANGE_NAME_ID, 0, "修改名称");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case MENU_CHANGE_NAME_ID:
			View view = View.inflate(this, R.layout.change_name, null);
			final EditText et_change_name = (EditText) view
					.findViewById(R.id.et_change_name);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("修改手机防盗的名称");
			builder.setView(view);
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String name = et_change_name.getText().toString();
							if ("".equals(name)) {
								Toast.makeText(getApplicationContext(),
										"名称不能为空！", Toast.LENGTH_SHORT).show();
							} else {
								Editor editor = sp.edit();
								editor.putString("name", name);
								editor.commit();
							}
						}
					});
			builder.setNegativeButton("取消", null);
			AlertDialog dialog = builder.create();
			dialog.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
