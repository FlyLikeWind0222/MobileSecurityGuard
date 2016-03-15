package com.flylikewind.mobilesecurityguard;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.flylikewind.mobilesecurityguard.bean.TaskInfo;
import com.flylikewind.mobilesecurityguard.receiver.LockScreenReceiver;

public class MyApplication extends Application {
	public TaskInfo taskinfo;

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(1000);
		LockScreenReceiver recevier = new LockScreenReceiver();
		registerReceiver(recevier, filter);
	}

}
