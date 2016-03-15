package com.flylikewind.mobilesecurityguard.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.flylikewind.mobilesecurityguard.bean.AppInfo;

public class AppInfoProvider {

	private PackageManager packManager;

	public AppInfoProvider(Context context) {
		this.packManager = context.getPackageManager();
	}

	/**
	 * 返回当前手机里面安装的所有的应用程序信息的集合
	 * 
	 * @return
	 */
	public List<AppInfo> getAllApps() {
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		List<PackageInfo> packInfos = packManager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packInfos) {
			AppInfo myApp = new AppInfo();
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			String appName = appInfo.loadLabel(packManager).toString();

			myApp.setPackName(packageInfo.packageName);
			myApp.setIcon(appInfo.loadIcon(packManager));
			myApp.setAppName(appName);

			if (filterApp(appInfo)) {
				myApp.setSystemApp(false);
			} else {
				myApp.setSystemApp(true);
			}
			appInfos.add(myApp);
		}
		return appInfos;
	}

	/**
	 * 判断某个应用是不是第三方的应用
	 * 
	 * @param appName
	 * @return
	 */
	private boolean filterApp(ApplicationInfo appInfo) {
		if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
				|| (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
