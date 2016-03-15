package com.flylikewind.mobilesecurityguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "重启完毕");
		// 判断手机是否处于保护状态
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isProtected = sp.getBoolean("isprotected", false);
		if (isProtected) {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = manager.getSimSerialNumber();
			String realSim = sp.getString("sim_serial", "");
			// sim卡串号不同
			if (!currentSim.equals(realSim)) {
				// 发送报警短信
				Log.i(TAG, "发送报警短信");
				SmsManager smsManager = SmsManager.getDefault();
				String destinationAddress = sp.getString("safenumber", "");
				smsManager.sendTextMessage(destinationAddress, null,
						"SIM卡发生了改变，手机可能被盗！", null, null);
			}
		}
	}

}
