package com.flylikewind.mobilesecurityguard.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.utils.ScreenUtils;

public class MainAdapte extends BaseAdapter {
	private LayoutInflater inflater;
	private SharedPreferences sp;
	private String[] textArray = new String[] { "手机防盗", "通讯卫士", "软件管理", "任务管理",
			"流量管理", "手机杀毒", "系统优化", "高级工具", "设置中心" };
	private int[] iconArray = new int[] { R.drawable.widget01,
			R.drawable.widget02, R.drawable.widget03, R.drawable.widget04,
			R.drawable.widget05, R.drawable.widget06, R.drawable.widget07,
			R.drawable.widget08, R.drawable.widget09 };

	public MainAdapte(Context context) {
		inflater = LayoutInflater.from(context);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return textArray.length;
	}

	@Override
	public Object getItem(int position) {
		return textArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * ①加载一个layout； ②用代码写一个view
		 */
		// LinearLayout view = new LinearLayout(context);
		// view.setOrientation(LinearLayout.VERTICAL);
		// view.setGravity(Gravity.CENTER);
		// view.setBackgroundResource(R.drawable.main_itembtn_bg);
		// view.setPadding(0, 10, 0, 10);
		// ImageView iv = new ImageView(context);
		// iv.setImageResource(iconArray[position]);
		// //设置控件的宽和高
		// iv.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
		// view.addView(iv);
		// TextView tv = new TextView(context);
		// tv.setLayoutParams(new
		// LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT));
		// tv.setText(textArray[position]);
		// tv.setTextColor(0xffC0C0C0);
		// tv.setTextSize(16);
		// view.addView(tv);
		// --------------------------------------------------------

		View view;
		ViewHolder viewHolder;

		/*
		 * convertView将之前加载好的布局进行缓存，所以此处判断convertView是否为空：不为空则已有缓存，直接使用缓存的内容；为空，
		 * 则重新创建对象，提高效率
		 */
		if (convertView == null) {
			view = inflater.inflate(R.layout.main_item, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_main = (ImageView) view.findViewById(R.id.iv_main);
			viewHolder.tv_main = (TextView) view.findViewById(R.id.tv_main);

			// 图片大小设置
			int size = ScreenUtils.getSmallerSize() / 4;
			LayoutParams mParams = new LayoutParams(size, size);
			viewHolder.iv_main.setLayoutParams(mParams);
			// 将viewHolder存储在view中
			view.setTag(viewHolder);
		} else {
			view = convertView;
			// 重新获取viewHolder
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.iv_main.setImageResource(iconArray[position]);

		if (position == 0) {
			String name = sp.getString("name", textArray[position]);
			viewHolder.tv_main.setText(name);
		} else {
			viewHolder.tv_main.setText(textArray[position]);
		}
		return view;
	}

	/**
	 * 内部类： 每次在getView()方法中还是会调用View的findViewById()方法来获取一次控件的实例，
	 * 此时借助ViewHolder来对这部分性能进行优化，用于对控件实例进行缓存。
	 * 
	 * @author FlyLikeWind
	 * 
	 */
	class ViewHolder {
		ImageView iv_main;
		TextView tv_main;
	}
}
