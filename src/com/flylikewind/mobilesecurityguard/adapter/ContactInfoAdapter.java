package com.flylikewind.mobilesecurityguard.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.bean.ContactInfo;

public class ContactInfoAdapter extends BaseAdapter {

	private List<ContactInfo> contacts;
	private LayoutInflater mInflater;

	public ContactInfoAdapter(Context context, List<ContactInfo> contacts) {
		this.contacts = contacts;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;
		ViewHolder viewHolder;

		/*
		 * convertView将之前加载好的布局进行缓存，所以此处判断convertView是否为空：不为空则已有缓存，直接使用缓存的内容；为空，
		 * 则重新创建对象，提高效率
		 */
		if (convertView == null) {
			view = mInflater.inflate(R.layout.contact_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_number = (TextView) view.findViewById(R.id.tv_number);
			// 将viewHolder存储在view中
			view.setTag(viewHolder);
		} else {
			view = convertView;
			// 重新获取viewHolder
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tv_name.setText(contacts.get(position).getName());
		viewHolder.tv_number.setText(contacts.get(position).getNumber());

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
		TextView tv_name;
		TextView tv_number;
	}
}
