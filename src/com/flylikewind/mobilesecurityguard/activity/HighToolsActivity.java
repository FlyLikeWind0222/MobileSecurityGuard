package com.flylikewind.mobilesecurityguard.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.service.AddressService;
import com.flylikewind.mobilesecurityguard.service.BackupSmsService;
import com.flylikewind.mobilesecurityguard.service.SmsInfoService;
import com.flylikewind.mobilesecurityguard.utils.DownloadManager;

@SuppressLint("HandlerLeak")
public class HighToolsActivity extends Activity implements OnClickListener {

	protected static final int ERROR = 10, SUCCESS = 11;
	private TextView tv_query, tv_hightools_address, tv_hightools_select_bg,
			tv_hightools_change_location, tv_hightools_sms_backup,
			tv_hightools_sms_restore, tv_hightools_app_lock;
	private ProgressDialog pd;
	private Intent serviceIntent;
	private CheckBox cb_hightools_address;
	private SharedPreferences sp;
	private SmsInfoService smsInfoService;
	private boolean isShowAddress;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case ERROR:
				Toast.makeText(getApplicationContext(), "下载数据库失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS:
				Toast.makeText(getApplicationContext(), "下载数据库成功！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hightool);

		serviceIntent = new Intent(this, AddressService.class);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		isShowAddress = sp.getBoolean("isshowaddress", false);
		tv_hightools_address = (TextView) findViewById(R.id.tv_atools_address);
		tv_hightools_change_location = (TextView) findViewById(R.id.tv_atools_change_location);
		tv_hightools_change_location.setOnClickListener(this);
		tv_hightools_select_bg = (TextView) findViewById(R.id.tv_atools_select_bg);
		tv_hightools_select_bg.setOnClickListener(this);
		tv_query = (TextView) findViewById(R.id.tv_atools_query);
		tv_query.setOnClickListener(this);
		tv_hightools_sms_backup = (TextView) findViewById(R.id.tv_atools_sms_backup);
		tv_hightools_sms_backup.setOnClickListener(this);
		tv_hightools_sms_restore = (TextView) findViewById(R.id.tv_atools_sms_restore);
		tv_hightools_sms_restore.setOnClickListener(this);
		tv_hightools_app_lock = (TextView) findViewById(R.id.tv_atools_app_lock);
		tv_hightools_app_lock.setOnClickListener(this);
		cb_hightools_address = (CheckBox) findViewById(R.id.cb_atools_address);
		cb_hightools_address.setChecked(isShowAddress);
		cb_hightools_address
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						if (isChecked) {
							startService(serviceIntent);
							tv_hightools_address.setTextColor(getResources()
									.getColor(R.color.text_on_textView));
							tv_hightools_address.setText("号码归属地服务已经开启");
							editor.putBoolean("isshowaddress", true);
						} else {
							stopService(serviceIntent);
							tv_hightools_address.setTextColor(Color.RED);
							tv_hightools_address.setText("号码归属地服务未开启");
							editor.putBoolean("isshowaddress", false);
						}
						editor.commit();
					}
				});
		if (isShowAddress) {
			tv_hightools_address.setTextColor(getResources().getColor(
					R.color.text_on_textView));
			tv_hightools_address.setText("号码归属地服务已经开启");
		} else {
			tv_hightools_address.setTextColor(Color.RED);
			tv_hightools_address.setText("号码归属地服务未开启");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_atools_query:
			// 判断来电归属地数据库是否存在
			if (isDBExist()) {
				Intent intent = new Intent(this, QueryNumberActivity.class);
				startActivity(intent);
			} else {
				// 提示用户下载数据库
				pd = new ProgressDialog(this);
				pd.setMessage("正在下载数据库！");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.show();
				// 下载数据库
				new Thread() {
					@Override
					public void run() {
						Message msg = new Message();
						try {
							String path = getResources().getString(
									R.string.addressdburl);
							DownloadManager.download(path, pd);
							msg.what = SUCCESS;
						} catch (Exception e) {
							e.printStackTrace();
							msg.what = ERROR;
						} finally {
							pd.dismiss();
							handler.sendMessage(msg);
						}
					}
				}.start();
			}
			break;
		case R.id.tv_atools_select_bg:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("归属地提示显示风格");
			String[] items = new String[] { "半透明", "金属灰", "卫士蓝", "苹果绿", "活力橙" };
			builder.setSingleChoiceItems(items, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Editor editor = sp.edit();
							editor.putInt("background", which);
							editor.commit();
						}
					});
			builder.setPositiveButton("确定", null);
			builder.create().show();
			break;
		case R.id.tv_atools_change_location:
			// 更改来电归属地的显示位置
			Intent intent = new Intent(this, DragViewActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_atools_sms_backup:
			// 备份短信
			Intent service = new Intent(this, BackupSmsService.class);
			startService(service);
			break;
		case R.id.tv_atools_sms_restore:
			// 还原短信
			/*
			 * 1.读取备份的xml文件 ；2.解析文件里面的信息； 3.插入到信息应用里面
			 */
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("正在还原短信，请稍候。。。");
			pd.show();
			smsInfoService = new SmsInfoService(this);
			new Thread() {
				@Override
				public void run() {
					try {
						smsInfoService.restoreSms(Environment
								.getExternalStorageDirectory().getPath()
								+ "/smsbackup.xml", pd);
						pd.dismiss();
						// 在主线程显示Toast
						Looper.prepare();
						Toast.makeText(getApplicationContext(), "短信还原成功！",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					} catch (Exception e) {
						e.printStackTrace();
						pd.dismiss();
						// 在主线程显示Toast
						Looper.prepare();
						Toast.makeText(getApplicationContext(), "短信还原失败！",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				}
			}.start();
			break;
		case R.id.tv_atools_app_lock:
			Intent appLockIntent = new Intent(this, AppLockActivity.class);
			startActivity(appLockIntent);
			break;
		}
	}

	/**
	 * 判断数据库是否存在
	 * 
	 * @return
	 */
	public boolean isDBExist() {
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/address.db");
		return file.exists();
	}
}
