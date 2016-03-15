package com.flylikewind.mobilesecurityguard.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

import com.flylikewind.mobilesecurityguard.bean.SmsInfo;

public class BackupSmsService extends Service {
	private SmsInfoService smsInfoService;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		smsInfoService = new SmsInfoService(this);
		new Thread() {
			@Override
			public void run() {
				try {
					List<SmsInfo> smsInfos = smsInfoService.getSmsInfos();
					File file = new File(Environment
							.getExternalStorageDirectory().getPath()
							+ "/smsbackup.xml");
					XmlSerializer serializer = Xml.newSerializer();
					FileOutputStream os = new FileOutputStream(file);
					serializer.setOutput(os, "utf-8");
					serializer.startDocument("utf-8", true);
					serializer.startTag(null, "smss");
					serializer.startTag(null, "count");
					serializer.text(smsInfos.size() + "");
					serializer.endTag(null, "count");
					String[] tags = new String[] { "id", "address", "date",
							"type", "body" };
					for (SmsInfo info : smsInfos) {
						String[] values = new String[] { info.getId(),
								info.getAddress(), info.getDate(),
								info.getType() + "", info.getBody() };

						serializer.startTag(null, "sms");
						for (int i = 0; i < values.length; i++) {
							serializer.startTag(null, tags[i]);
							serializer.text(values[i]);
							serializer.endTag(null, tags[i]);
						}
						serializer.endTag(null, "sms");
					}
					serializer.endTag(null, "smss");
					serializer.endDocument();

					// 把文件缓冲区的数据写出去
					os.flush();
					os.close();
					// 在子线程里面设置显示到主线程的Toast
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "备份完成！",
							Toast.LENGTH_SHORT).show();
					Looper.loop();
				} catch (Exception e) {
					e.printStackTrace();
					// 在子线程里面设置显示到主线程的Toast
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "备份失败！",
							Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
			}
		}.start();
	}
}
