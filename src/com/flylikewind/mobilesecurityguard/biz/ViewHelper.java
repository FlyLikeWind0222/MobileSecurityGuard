package com.flylikewind.mobilesecurityguard.biz;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ViewHelper {

	/**
	 * 获取版本号
	 * @return
	 */
	public static String getVersion(Context context) {
		PackageManager pm = context.getPackageManager();//拿到包的管理器
		try {
			//封装了所有的功能清单中的数据
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
