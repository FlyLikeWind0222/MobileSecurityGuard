package com.flylikewind.mobilesecurityguard.test;

import android.test.AndroidTestCase;

import com.flylikewind.mobilesecurityguard.provider.AppInfoProvider;

public class TestGetAppInfo extends AndroidTestCase {
	public void getApps() throws Exception{
		AppInfoProvider provider = new AppInfoProvider(getContext());
		provider.getAllApps();
	}
}
