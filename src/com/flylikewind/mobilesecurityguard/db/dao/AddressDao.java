package com.flylikewind.mobilesecurityguard.db.dao;

import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	/**
	 * 获取操作数据库的对象（只读）
	 * 
	 * @param path
	 *            数据库的路径
	 * @return 数据库的对象
	 */
	public static SQLiteDatabase getAddressDB(String path) {
		SQLiteDatabase sQLiteDatabase = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		return sQLiteDatabase;
	}
}
