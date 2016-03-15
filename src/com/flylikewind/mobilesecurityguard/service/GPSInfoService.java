package com.flylikewind.mobilesecurityguard.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 单例模式：保证一个类仅有一个实例，并提供一个访问它的全局访问点。
 * 
 * @author FlyLikeWind
 * 
 */
public class GPSInfoService {

	private static GPSInfoService mInstance;
	// 定位服务对象
	private LocationManager locationManager;
	private SharedPreferences sp;
	private MyLocationListener listener;

	private GPSInfoService(Context context) {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	/**
	 * 双重锁定(Double-Check Locking)的单例模式
	 * 
	 * @param context
	 * @return
	 */
	public static GPSInfoService getInstance(Context context) {
		if (mInstance == null) {
			synchronized (GPSInfoService.class) {
				if (mInstance == null) {
					mInstance = new GPSInfoService(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 注册定位监听
	 */
	public void registenerLocationChangeListener() {
		// 查询条件
		Criteria criteria = new Criteria();
		// 定位的精准度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 是否关注海拔信息
		criteria.setAltitudeRequired(false);
		// 是否关心周围的事情
		criteria.setBearingRequired(false);
		// 是否支持收费的查询
		criteria.setCostAllowed(true);
		// 是否耗电
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 对速度是否关注
		criteria.setSpeedRequired(false);
		// 得到最好的定位方式
		String provider = locationManager.getBestProvider(criteria, true);
		// 注册监听
		locationManager.requestLocationUpdates(provider, 60000, 0,
				getListener());
	}

	/**
	 * 取消定位监听
	 */
	public void unRegistenerLocationChangeListener() {
		locationManager.removeUpdates(getListener());
	}

	/**
	 * 得到定位的监听器
	 * 
	 * @return
	 */
	private MyLocationListener getListener() {
		if (listener == null) {
			listener = new MyLocationListener();
		}

		return listener;
	}

	/**
	 * 得到上一个地理位置
	 * 
	 * @return
	 */
	public String getLastLocation() {
		return sp.getString("last_location", "");
	}

	private final class MyLocationListener implements LocationListener {

		// 位置改变
		@Override
		public void onLocationChanged(Location location) {
			// 纬度
			double latitude = location.getLatitude();
			// 经度
			double longitude = location.getLongitude();

			// 保存上一次地理位置到SharedPreferences
			String last_location = "经度：" + longitude + "，纬度：" + latitude;
			Editor editor = sp.edit();
			editor.putString("last_location", last_location);
			editor.commit();
		}

		// GPS卫星有一个没有找到
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// 某个位置被打开
		@Override
		public void onProviderEnabled(String provider) {
		}

		// 某个位置被关闭
		@Override
		public void onProviderDisabled(String provider) {
		}
	}
}
