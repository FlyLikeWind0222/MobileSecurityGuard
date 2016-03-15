package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.service.NumberAddressService;

public class QueryNumberActivity extends Activity {

	private TextView tv_query_number_address;
	private EditText et_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_number);
		tv_query_number_address = (TextView) findViewById(R.id.tv_query_number_address);
		et_number = (EditText) findViewById(R.id.et_query_number);
	}

	public void query(View view) {
		// 判断号码是否为空
		String number = et_number.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
		} else {
			// 打开数据库，查询号码归属地
			String address = NumberAddressService.getAddress(number);
			tv_query_number_address.setText("归属地信息：" + address);
		}
	}
}
