package com.flylikewind.mobilesecurityguard.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flylikewind.mobilesecurityguard.R;

public class OptimizeActivity extends Activity {
	private TextView tv;
	private ProgressBar pb;
	private SQLiteDatabase db;
	@SuppressLint("SdCardPath")
	private String dbPath = "/data/data/com.flylikewind.mobilesecurityguard/files/clearpath.db";
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String text = (String) msg.obj;
			tv.setText(text);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optimization);
		tv = (TextView) this.findViewById(R.id.tv);
		pb = (ProgressBar) this.findViewById(R.id.progressBar1);

		// 判断手机内存里面是否有数据库存在
		File file = new File(dbPath);
		if (!file.exists()) {
			copyfile();
		}
		// 文件写到哪里了?
		// data/data/com.flylikewind.mobilesecurityguard/files/name

	}

	public void start(View view) {
		db = SQLiteDatabase.openDatabase(dbPath, null,
				SQLiteDatabase.OPEN_READONLY);
		new Thread(new Runnable(){
			@Override
			public void run() {
				List<PackageInfo> packinfos = getPackageManager()
						.getInstalledPackages(0);
				pb.setMax(packinfos.size());// 设置进度条的最大条目个数
				int total = 0;
				for (PackageInfo info : packinfos) {
					String packname = info.packageName;
					Cursor curosr = db.rawQuery(
							"select filepath from softdetail where apkname=?",
							new String[] { packname });
					if (curosr.moveToFirst()) {
						String path = curosr.getString(0);
						System.out.println("清除" + packname + "sd卡缓存" + path);
						File file = new File(
								Environment.getExternalStorageDirectory(), path);
						deleteDir(file);
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					total++;
					pb.setProgress(total);
					curosr.close();
					Message msg = Message.obtain();
					msg.obj = "清除" + packname;
					handler.sendMessage(msg);
				}
				
				Message msg = Message.obtain();
				msg.obj = "清除完毕";
				handler.sendMessage(msg);
				db.close();
			}
		}).start();
	}

	private void copyfile() {

		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					"clearpath.db");
			OutputStream fos = this
					.openFileOutput("clearpath.db", MODE_PRIVATE);
			byte[] buffer = new byte[1024];
			int len = 0;

			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteDir(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDir(file);
			}
		} else {
			file.delete();
		}

	}
	
	public void clearCache(View view) {
		Intent intent = new Intent(this,
				ClearCacheActivity.class);
		startActivity(intent);
	}
	
}