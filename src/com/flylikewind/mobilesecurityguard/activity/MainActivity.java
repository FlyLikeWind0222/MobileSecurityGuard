package com.flylikewind.mobilesecurityguard.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.adapter.MainAdapte;
import com.flylikewind.mobilesecurityguard.utils.ScreenUtils;

public class MainActivity extends BaseActivity {

	private GridView gv_main_gv;
	private MainAdapte adapte;
	// 用来持久化一些配置信息
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 初始化ScreenUtils工具类
		ScreenUtils.init(this);

		adapte = new MainAdapte(this);
		gv_main_gv = (GridView) findViewById(R.id.gv_main_gv);
		gv_main_gv.setAdapter(adapte);
		gv_main_gv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent,
					final View view, int position, long id) {
				if (position == 0) {
					AlertDialog.Builder buider = new Builder(MainActivity.this);
					buider.setTitle("设置");
					buider.setMessage("请输入要更改的名称");
					final EditText et = new EditText(MainActivity.this);
					et.setHint("请输入文本");
					buider.setView(et);
					buider.setPositiveButton("确定", new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							String name = et.getText().toString().trim();
							if ("".equals(name)) {
								Toast.makeText(getApplicationContext(),
										"内容不能为空", Toast.LENGTH_SHORT).show();
								return;
							} else {
								Editor editor = sp.edit();
								editor.putString("lost_name", name);
								// 完成数据的提交
								editor.commit();
								TextView tv = (TextView) view
										.findViewById(R.id.tv_main_name);
								tv.setText(name);
							}

						}
					});
					buider.setNegativeButton("取消", null);
					buider.create().show();
				}
				return false;
			}
		});
		gv_main_gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				switch (position) {
				case 0:// 手机防盗
					intent = new Intent(getApplicationContext(),
							LostProtectedActivity.class);
					startActivity(intent);
					break;
				case 1:// 通讯卫士
					intent = new Intent(getApplicationContext(),
							CallSmsActivity.class);
					startActivity(intent);
					break;
				case 2:// 程序管理
					intent = new Intent(getApplicationContext(),
							AppManagerActivity.class);
					startActivity(intent);
					break;
				case 3:// 任务管理
					intent = new Intent(MainActivity.this,
							TaskManagerActivity.class);
					startActivity(intent);
					break;
				case 4:// 流量管理
					Intent trafficmanagerIntent = new Intent(MainActivity.this,
							TrafficManagerActivity.class);
					startActivity(trafficmanagerIntent);
					break;
				case 5:// 手机杀毒
					intent = new Intent(getApplicationContext(),
							AntivirusActivity.class);
					startActivity(intent);
					break;
				case 6:// 系统优化
					intent = new Intent(getApplicationContext(),
							OptimizeActivity.class);
					startActivity(intent);
					break;
				case 7:// 高级工具
					intent = new Intent(getApplicationContext(),
							HighToolsActivity.class);
					startActivity(intent);
					break;
				case 8:// 设置中心
					intent = new Intent(getApplicationContext(),
							SettingCenterActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 让listview自动刷新，其实就是让getView()再次调用
		adapte.notifyDataSetChanged();
	}
}
