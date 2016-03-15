package com.flylikewind.mobilesecurityguard.test;

import android.test.AndroidTestCase;

import com.flylikewind.mobilesecurityguard.service.GPSInfoService;

public class GPSInfoServiceTest extends AndroidTestCase {

	
	public void testRegisterLocationChangeListener() throws Exception{
		GPSInfoService service = GPSInfoService.getInstance(getContext());
		service.registenerLocationChangeListener();
	}
}
