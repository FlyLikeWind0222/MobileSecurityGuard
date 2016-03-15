package com.flylikewind.mobilesecurityguard.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.flylikewind.mobilesecurityguard.bean.ContactInfo;
import com.flylikewind.mobilesecurityguard.service.ContactInfoService;

public class ContactInfoServiceTest extends AndroidTestCase {

	public void testGetContacts() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo> contacts = service.getContacts();
		for(ContactInfo info:contacts){
			Log.i("i", info.toString());
		}
			 
	}
}
