package com.flylikewind.mobilesecurityguard.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.db.dao.BlackNumberDao;
import com.flylikewind.mobilesecurityguard.service.GPSInfoService;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";
	private SharedPreferences sp;
	private BlackNumberDao dao;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "已经拦截到了短信");
		dao = BlackNumberDao.getInstance(context);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 判断保护是否开启
		boolean isProtected = sp.getBoolean("isprotected", false);
		if (isProtected) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				String body = smsMessage.getDisplayMessageBody();
				String safe_number = sp.getString("safe_number", "");

				if ("#*location*#".equals(body)) {
					// 中断广播
					abortBroadcast();
					// 获取手机的位置
					// 把位置发送给安全号码
					GPSInfoService service = GPSInfoService
							.getInstance(context);
					service.registenerLocationChangeListener();
					String lastLocation = service.getLastLocation();
					SmsManager smsManager = SmsManager.getDefault();
					if (!"".equals(lastLocation)) {
						smsManager.sendTextMessage(safe_number, null, "手机的位置："
								+ lastLocation, null, null);
					}
				} else if ("#*lockscreen*#".equals(body)) {
					// 中断广播
					abortBroadcast();
					DevicePolicyManager policyManager = (DevicePolicyManager) context
							.getSystemService(Context.DEVICE_POLICY_SERVICE);
					policyManager.resetPassword("19940222", 0);
					policyManager.lockNow();
				} else if ("#*wipedata*#".equals(body)) {
					// 中断广播
					abortBroadcast();
					DevicePolicyManager policyManager = (DevicePolicyManager) context
							.getSystemService(Context.DEVICE_POLICY_SERVICE);
					policyManager.wipeData(0);
				} else if ("#*alarm*#".equals(body)) {
					// 中断广播
					abortBroadcast();
					MediaPlayer mediaPlayer = MediaPlayer.create(context,
							R.raw.ylzs);
					mediaPlayer.setVolume(1.0f, 1.0f);
					mediaPlayer.start();
				}

				if (dao.find(smsMessage.getOriginatingAddress())) {
					// 黑名单的短信
					// 中断广播
					abortBroadcast();
					// TODO 把短信内容存放到自己的数据库里面
				}

				// 建立短信内容的匹配库 (关键字: 发票,卖房,哥,学生....办证...)
				if (body.contains("发票")) {
					Log.i(TAG, "垃圾短信，已被拦截！");
					// 中断广播
					abortBroadcast();
				}
			}
		}
	}

}
