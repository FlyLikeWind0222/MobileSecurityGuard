package com.flylikewind.mobilesecurityguard.biz;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 对SharePreference操作
 * 存
 * 取
 */
public class SafePreference {
	/**
	 * 往SharePreference中存数据
	 * @param contex
	 * @param key
	 * @param value
	 * 不仅可以保持字符串还可以保持boolean
	 */
	public static void save(Context context, String key, Object value){
		//拿到SharePreference
		SharedPreferences sp = context.getSharedPreferences(Const.PFNAME, 0);
		//判断要存的数据是什么数据
		if(value instanceof String){
			sp.edit().putString(key, (String)value).commit();
		}else if(value instanceof Boolean){
			sp.edit().putBoolean(key,(Boolean)value).commit();
		}
	}
	
	/**
	 * 从SharePreference中取数据
	 * @param contex
	 * @param key
	 * @param value
	 */
	public static String getStr(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(Const.PFNAME, 0);
		return sp.getString(key, "");
	}
	
	public static Boolean getBoolean(Context context, String key){
		SharedPreferences sp = context.getSharedPreferences(Const.PFNAME, 0);
		return sp.getBoolean(key, false);
	}
	
}
