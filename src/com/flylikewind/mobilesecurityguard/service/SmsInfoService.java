package com.flylikewind.mobilesecurityguard.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import com.flylikewind.mobilesecurityguard.bean.SmsInfo;

public class SmsInfoService {

	private Context context;

	public SmsInfoService(Context context) {
		this.context = context;
	}

	/**
	 * 获取所有短信信息
	 * 
	 * @return
	 */
	public List<SmsInfo> getSmsInfos() {
		SmsInfo smsInfo;
		List<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = contentResolver.query(uri, new String[] { "_id",
				"address", "date", "body", "type" }, null, null, "date desc");
		while (cursor.moveToNext()) {
			smsInfo = new SmsInfo(cursor.getString(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3), cursor.getInt(4));
			smsInfos.add(smsInfo);
			smsInfo = null;
		}

		return smsInfos;
	}

	/**
	 * 还原短信信息
	 * 
	 * @param path
	 *            短信备份文件对应的路径
	 * @param pd
	 */
	public void restoreSms(String path, ProgressDialog pd) throws Exception {
		File file = new File(path);
		ContentValues values = null;
		FileInputStream fis = new FileInputStream(file);
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(fis, "utf-8");
		int type = parser.getEventType();
		int currentCount = 0;
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("count".equals(parser.getName())) {
					String count = parser.nextText();
					pd.setMax(Integer.parseInt(count));
				}

				if ("sms".equals(parser.getName())) {
					values = new ContentValues();
				} else if ("address".equals(parser.getName())) {
					values.put("address", parser.nextText());
				} else if ("date".equals(parser.getName())) {
					values.put("date", parser.nextText());
				} else if ("type".equals(parser.getName())) {
					values.put("type", parser.nextText());
				} else if ("body".equals(parser.getName())) {
					values.put("body", parser.nextText());
				}
				break;

			case XmlPullParser.END_TAG:
				if ("sms".equals(parser.getName())) {
					ContentResolver contentResolver = context
							.getContentResolver();
					contentResolver.insert(Uri.parse("content://sms/"), values);
					values = null;
					currentCount++;
					pd.setProgress(currentCount);
				}
				break;
			}
			type = parser.next();
		}
	}
}
