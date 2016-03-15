package com.flylikewind.mobilesecurityguard.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.flylikewind.mobilesecurityguard.db.dao.AddressDao;

public class NumberAddressService {

	/**
	 * 获取电话号码的归属地
	 * 
	 * @param number
	 *            要查询的电话号码
	 * @return 电话号码的归属地(若不存在返回该电话号码)
	 */
	public static String getAddress(String number) {
		String pattern = "^1[3458]\\d{9}$";
		String address = number;
		if (number.matches(pattern)) {
			/*
			 * 手机号码
			 */
			address = myExeQuery("select city from info where mobileprefix=?",
					new String[] { number.substring(0, 7) });
		} else {
			/*
			 * 固定电话
			 */
			int len = number.length();
			String sql = "SELECT city FROM info WHERE area=? limit 1";
			String[] params1 = new String[] { number.substring(0, 3) };
			String[] params2 = new String[] { number.substring(0, 4) };

			switch (len) {
			// 模拟器
			case 4:
				address = "模拟器";
				break;
			// 本地号码
			case 7:
				address = "本地号码";
				break;
			// 本地号码
			case 8:
				address = "本地号码";
				break;
			// 3位区号 + 7位号码
			case 10:
				address = myExeQuery(sql, params1);
				break;
			// 3位区号 + 8位号码或4位区号+7位号码
			case 11:
				address = myExeQuery(sql, params1);
				if ("".equals(address)) {
					address = myExeQuery(sql, params2);
				}
				break;
			// 4位区号+8位号码
			case 12:
				address = myExeQuery(sql, params2);
				break;
			}
		}
		return address;
	}

	/**
	 * 抽取数据库的查询操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	private static String myExeQuery(String sql, String[] params) {
		String address = "";
		SQLiteDatabase db = AddressDao.getAddressDB(Environment
				.getExternalStorageDirectory().getPath() + "/address.db");
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, params);
			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}
		return address;
	}
}
