package com.flylikewind.mobilesecurityguard.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.db.dao.BlackNumberDao;

public class CallSmsActivity extends Activity {
	private ListView lv_call_sms_safe;
	private Button bt_add_black_number;
	private BlackNumberDao dao;
	private List<String> numbers;
	private CallSmsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_sms_safe);
		dao = BlackNumberDao.getInstance(this);
		lv_call_sms_safe = (ListView) findViewById(R.id.lv_call_sms_safe);
		// 给listview注册上下文菜单
		registerForContextMenu(lv_call_sms_safe);
		bt_add_black_number = (Button) findViewById(R.id.btn_add_black_number);
		bt_add_black_number.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createDialog("添加黑名单号码", "添加", "");
			}
		});
		numbers = dao.getAllNumbers();
		adapter = new CallSmsAdapter();
		lv_call_sms_safe.setAdapter(adapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent intent=getIntent();
		String number=intent.getStringExtra("number");
		if(null!=number){
			createDialog("添加黑名单号码", "添加", "");
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int id = (int) info.id;
		String number = numbers.get(id);
		// 当前条目的id
		switch (item.getItemId()) {
		case R.id.update_number:
			updateNumber(number);
			break;
		case R.id.delete_number:
			dao.delete(number);
			// 重新获取黑名单号码
			numbers = dao.getAllNumbers();
			// 通知listview更新界面
			adapter.notifyDataSetChanged();
			break;
		}
		// 为false时，每次点击都会响应
		return false;
	}

	/**
	 * 更新黑名单号码
	 * 
	 * @param number
	 */
	private void updateNumber(final String oldNumber) {
		createDialog("更改黑名单号码", "更改", oldNumber);
	}

	/**
	 * 对话框创建
	 */
	private void createDialog(String title, final String positiveButtonName,
			final String oldNumber) {
		AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
		builder.setTitle(title);
		final EditText et = new EditText(CallSmsActivity.this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		builder.setView(et);
		builder.setPositiveButton(positiveButtonName,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String number = et.getText().toString().trim();
						if (TextUtils.isEmpty(number)) {
							Toast.makeText(getApplicationContext(),
									"黑名单号码不能为空！", Toast.LENGTH_SHORT).show();
							return;
						} else {
							if ("更改".equals(positiveButtonName)) {
								dao.update(oldNumber, number);
							} else if ("添加".equals(positiveButtonName)) {
								dao.add(number);
							}
							// todo: 通知listview更新数据
							// 缺点: 重新刷新整个listview
							// numbers = dao.getAllNumbers();
							// lv_call_sms_safe.setAdapter(new
							// ArrayAdapter<String>(CallSmsActivity.this,
							// R.layout.blacknumber_item,
							// R.id.tv_blacknumber_item, numbers));
							numbers = dao.getAllNumbers();
							// 让数据适配器通知listview更新数据
							adapter.notifyDataSetChanged();
						}
					}
				});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	/**
	 * 适配器
	 * 
	 * @author FlyLikeWind
	 * 
	 */
	private class CallSmsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return numbers.size();
		}

		@Override
		public Object getItem(int position) {
			return numbers.get(position);
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
			 * convertView将之前加载好的布局进行缓存，所以此处判断convertView是否为空：不为空则已有缓存，直接使用缓存的内容；
			 * 为空， 则重新创建对象，提高效率
			 */
			if (convertView == null) {
				view = View.inflate(CallSmsActivity.this,
						R.layout.blacknumber_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tv = (TextView) view
						.findViewById(R.id.tv_blacknumber_item);
				// 将viewHolder存储在view中
				view.setTag(viewHolder);
			} else {
				view = convertView;
				// 重新获取viewHolder
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.tv.setText(numbers.get(position));
			return view;
		}
	}

	/**
	 * 内部类： 每次在getView()方法中还是会调用View的findViewById()方法来获取一次控件的实例，
	 * 此时借助ViewHolder来对这部分性能进行优化，用于对控件实例进行缓存。
	 * 
	 * @author FlyLikeWind
	 * 
	 */
	class ViewHolder {
		TextView tv;
	}
}
