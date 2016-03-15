package com.flylikewind.mobilesecurityguard.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flylikewind.mobilesecurityguard.db.BlackNumberDBHelper;

public class BlackNumberDao {

	private BlackNumberDBHelper dbHelper;
	private static BlackNumberDao blackNumberDao;

	/**
	 * 单例-双重锁定
	 * 
	 * @param context
	 * @return
	 */
	public static BlackNumberDao getInstance(Context context) {
		if (blackNumberDao == null) {
			synchronized (BlackNumberDao.class) {
				if (blackNumberDao == null) {
					blackNumberDao = new BlackNumberDao(context);
				}
			}
		}
		return blackNumberDao;
	}

	private BlackNumberDao(Context context) {
		dbHelper = new BlackNumberDBHelper(context);
	}

	/**
	 * 查询
	 * 
	 * @param number
	 * @return
	 */
	public boolean find(String number) {
		boolean result = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"SELECT number FROM blacknumber WHERE number=?",
					new String[] { number });
			if (cursor.moveToNext()) {
				result = true;
			}
			cursor.close();
			db.close();
		}

		return result;
	}

	/**
	 * 添加-若已存在则不再添加
	 * 
	 * @param number
	 */
	public void add(String number) {
		if (find(number)) {
			return;
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("INSERT INTO blacknumber (number) VALUES (?)",
					new Object[] { number });
			db.close();
		}
	}

	/**
	 * 删除
	 * 
	 * @param number
	 */
	public void delete(String number) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("DELETE FROM blacknumber WHERE number=?",
					new Object[] { number });
			db.close();
		}
	}

	/**
	 * 更新操作
	 * 
	 * @param oldnumber
	 *            旧的号码
	 * @param newNumber
	 *            新的号码
	 */
	public void update(String oldNumber, String newNumber) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("UPDATE blacknumber SET number=? WHERE number=?",
					new Object[] { oldNumber, newNumber });
			db.close();
		}
	}

	/**
	 * 查找全部号码
	 */
	public List<String> getAllNumbers() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<String> numbers = new ArrayList<String>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("SELECT number FROM blacknumber", null);
			while (cursor.moveToNext()) {
				String number = cursor.getString(0);
				numbers.add(number);
			}
			cursor.close();
			db.close();
		}

		return numbers;
	}
}
