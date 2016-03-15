package com.flylikewind.mobilesecurityguard.service;

import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;
import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.activity.CallSmsActivity;
import com.flylikewind.mobilesecurityguard.db.dao.BlackNumberDao;

public class AddressService extends Service {

	private static final String TAG = "AddressService";
	private TelephonyManager telephonyManager;
	private MyPhoneListener listener;
	private WindowManager windowManager;
	private SharedPreferences sp;
	private View view;
	private BlackNumberDao dao;
	private long firstRingTime;
	private long endRingTime;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = BlackNumberDao.getInstance(this);
		listener = new MyPhoneListener();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		// 注册系统电话管理服务的监听器
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

	/**
	 * 在窗体上显示位置信息
	 * 
	 * @param address
	 */
	private void showLocation(String address) {
		LayoutParams params = new LayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.flags = LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_NOT_TOUCHABLE
				| LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = LayoutParams.TYPE_TOAST;
		params.setTitle("Address");
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.x = sp.getInt("lastx", 0);
		params.y = sp.getInt("lasty", 0);

		view = View.inflate(getApplicationContext(), R.layout.show_location,
				null);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_location);

		int backgroundId = sp.getInt("background", 0);
		if (backgroundId == 0) {
			ll.setBackgroundResource(R.drawable.call_locate_white);
		} else if (backgroundId == 1) {
			ll.setBackgroundResource(R.drawable.call_locate_gray);
		} else if (backgroundId == 2) {
			ll.setBackgroundResource(R.drawable.call_locate_blue);
		} else if (backgroundId == 3) {
			ll.setBackgroundResource(R.drawable.call_locate_green);
		} else if (backgroundId == 4) {
			ll.setBackgroundResource(R.drawable.call_locate_orange);
		}

		TextView tv = (TextView) view.findViewById(R.id.tv_location);
		tv.setTextSize(24);
		tv.setText(address);
		windowManager.addView(view, params);
	}

	private class MyPhoneListener extends PhoneStateListener {
		/**
		 * 电话状态发生改变的时候调用的方法
		 */
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			// 处于静止状态，即没有呼叫
			case TelephonyManager.CALL_STATE_IDLE:
				endRingTime = System.currentTimeMillis();
				long callTime = endRingTime - firstRingTime;
				Log.i(TAG, "callTime=" + callTime);
				if (firstRingTime < endRingTime && callTime < 5000
						&& callTime > 0) {
					Log.i(TAG, "响一声电话！");
					endRingTime = 0;
					firstRingTime = 0;
					// 弹出来notification通知用户这是一个骚扰电话
					showNotification(incomingNumber);
				}

				if (view != null) {
					windowManager.removeView(view);
					view = null;
				}
				break;
			// 响铃状态
			case TelephonyManager.CALL_STATE_RINGING:
				firstRingTime = System.currentTimeMillis();
				Log.i(TAG, "来电号码为：" + incomingNumber);
				if (dao.find(incomingNumber)) {
					// 挂断电话
					endCall();

					// 注册一个内容观察者，观察call_log的uri的信息
					getContentResolver().registerContentObserver(
							CallLog.Calls.CONTENT_URI, true,
							new MyObserver(new Handler(), incomingNumber));
				}

				String address = NumberAddressService
						.getAddress(incomingNumber);
				Log.i(TAG, "归属地为：" + address);
				showLocation(address);
				break;
			// 接通电话状态
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (view != null) {
					windowManager.removeView(view);
					view = null;
				}
				break;
			}
		}
	}

	/**
	 * 弹出notification通知用户添加黑名单号码
	 * 
	 * @param incomingNumber
	 */
	@SuppressWarnings("deprecation")
	private void showNotification(String incomingNumber) {
		// 1.获取notification的管理服务
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 2.把一个要想显示的Notification对象创建出来
		Notification notification = new Notification(R.drawable.notification,
				"发现响一声电话！", System.currentTimeMillis());
		// 3.配置Notification的一些参数
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		Intent notificationIntent = new Intent(this, CallSmsActivity.class);
		// 把响一声的号码设置到intent对象里面
		notificationIntent.putExtra("number", incomingNumber);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(getApplicationContext(), "响一声号码",
				incomingNumber, contentIntent);
		// 4.通过manager把notification激活
		manager.notify(0, notification);
	}

	/**
	 * 根据电话号码删除呼叫记录
	 * 
	 * @param incomingNumber
	 */
	private void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, null,
				"number=?", new String[] { incomingNumber }, null);
		// 查询到了呼叫记录
		if (cursor.moveToFirst()) {
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			resolver.delete(CallLog.Calls.CONTENT_URI, "_id=?",
					new String[] { id });
		}
	}

	private void endCall() {
		try {
			Method method = Class.forName("android.os.ServiceManager")
					.getMethod("getService", String.class);
			IBinder binder = (IBinder) method.invoke(null,
					new Object[] { TELEPHONY_SERVICE });
			ITelephony telephoy = ITelephony.Stub.asInterface(binder);
			telephoy.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyObserver extends ContentObserver {
		private String incomingNumber;

		public MyObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			deleteCallLog(incomingNumber);

			// 当删除了呼叫记录后，反注册内容观察者
			getContentResolver().unregisterContentObserver(this);
		}
	}
}
