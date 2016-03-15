package com.flylikewind.mobilesecurityguard.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.flylikewind.mobilesecurityguard.bean.SmsInfo;
import com.flylikewind.mobilesecurityguard.service.SmsInfoService;

public class TestSmsInfoService extends AndroidTestCase {

	
	public void testGetSmsInfos() throws Exception{
		SmsInfoService service  = new SmsInfoService(getContext());
		List<SmsInfo>  smsinfos = service.getSmsInfos();
		assertEquals(5, smsinfos.size());
	}
}
