package com.flylikewind.mobilesecurityguard.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.flylikewind.mobilesecurityguard.db.dao.AppLockDao;

public class AppLockProvider extends ContentProvider {

	private static final int INSERT = 10;
	private static final int DELETE = 11;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static Uri changeuri = Uri
			.parse("content://cn.itcast.applockprovider");
	private AppLockDao dao;
	static {
		matcher.addURI("cn.itcast.applockprovider", "insert", INSERT);
		matcher.addURI("cn.itcast.applockprovider", "delete", DELETE);
	}

	@Override
	public boolean onCreate() {
		dao = new AppLockDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int result = matcher.match(uri);
		if (result == INSERT) {
			String packname = (String) values.get("packname");

			dao.add(packname);
			getContext().getContentResolver().notifyChange(changeuri, null);
		} else {
			throw new IllegalArgumentException("uri��ַ����ȷ");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = matcher.match(uri);
		if (result == DELETE) {
			String packname = selectionArgs[0];
			dao.delete(packname);
			getContext().getContentResolver().notifyChange(changeuri, null);
		} else {
			throw new IllegalArgumentException("uri��ַ����ȷ");
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
