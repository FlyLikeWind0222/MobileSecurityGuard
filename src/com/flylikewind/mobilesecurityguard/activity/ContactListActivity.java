package com.flylikewind.mobilesecurityguard.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.flylikewind.mobilesecurityguard.R;
import com.flylikewind.mobilesecurityguard.adapter.ContactInfoAdapter;
import com.flylikewind.mobilesecurityguard.bean.ContactInfo;
import com.flylikewind.mobilesecurityguard.service.ContactInfoService;

public class ContactListActivity extends BaseActivity {

	private ListView lv_contact;
	private ContactInfoService service;
	private ContactInfoAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);

		lv_contact = (ListView) findViewById(R.id.lv_contact);
		service = new ContactInfoService(this);
		List<ContactInfo> contacts = service.getContacts();
		mAdapter = new ContactInfoAdapter(this, contacts);
		lv_contact.setAdapter(mAdapter);
		lv_contact.setOnItemClickListener(new MyOnItemClickListener());
	}

	private final class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ContactInfo info = (ContactInfo) mAdapter.getItem(position);
			String number = info.getNumber();
			Intent data = new Intent();
			data.putExtra("number", number);
			// 向上一个Activity返回数据
			setResult(200, data);
			finish();
		}
	}
}
