package com.flylikewind.mobilesecurityguard.receiver;

import com.flylikewind.mobilesecurityguard.activity.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("PhoneReceiver", "已经拦截到了外拨电话");

		/*
		 * 1.清除数据；2.激活一个Activity。
		 */
		String number = getResultData();
		if ("20152015".equals(number)) {
			// 终止掉这个电话
			// 不能通过 abortBroadcast();
			setResultData(null);

			// receiver是不存在于任务栈里面，在里面启动Activity必须要指定Flag：FLAG_ACTIVITY_NEW_TASK
			Intent i = new Intent(context, LostProtectedActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

}
