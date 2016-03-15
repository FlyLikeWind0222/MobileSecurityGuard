package com.flylikewind.mobilesecurityguard.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.flylikewind.mobilesecurityguard.bean.ContactInfo;
import com.flylikewind.mobilesecurityguard.service.ContactInfoService;

public class TestGetContactInfo extends AndroidTestCase {

	public void testGetContacts() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo> infos =  service.getContacts();
		for(ContactInfo info : infos ){
			System.out.println(info.getName());
			System.out.println(info.getNumber());
			
		}
	}
}
