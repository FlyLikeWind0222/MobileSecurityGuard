package com.flylikewind.mobilesecurityguard.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.flylikewind.mobilesecurityguard.bean.ContactInfo;

public class ContactInfoService {

	private Context context;

	public ContactInfoService(Context context) {
		this.context = context;
	}

	/**
	 * 获取手机里所有的联系人
	 * 
	 * @return
	 */
	public List<ContactInfo> getContacts() {
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();

		// 查询raw_contacts表得到联系人的_id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Cursor cursor = resolver.query(uri, new String[] { "_id",
				"display_name" }, null, null, null);
		while (cursor.moveToNext()) {
			ContactInfo info = new ContactInfo();
			String _id = cursor.getString(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor
					.getColumnIndex("display_name"));
			info.setName(name);

			// 查询data表
			uri = Uri.parse("content://com.android.contacts/raw_contacts/"
					+ _id + "/data");
			Cursor cursor2 = resolver.query(uri, new String[] { "data1",
					"mimetype" }, null, null, null);
			while (cursor2.moveToNext()) {
				String data1 = cursor2.getString(cursor2
						.getColumnIndex("data1"));
				String mimetype = cursor2.getString(cursor2
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					info.setNumber(data1);
					contactInfos.add(info);
				}
			}
			cursor2.close();
		}
		cursor.close();

		return contactInfos;
	}
}
