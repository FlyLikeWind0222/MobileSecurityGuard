package com.flylikewind.mobilesecurityguard.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.flylikewind.mobilesecurityguard.db.dao.BlackNumberDao;

public class TestBlacknumberDao extends AndroidTestCase {

	public void testAdd() throws Exception{
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		long number =13512345678l;
		for(int i=0;i<100;i++){
		   dao.add(number+i+"");
		}
	}
	
	public void testfindall() throws Exception{
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		List<String> numbers = dao.getAllNumbers();
		System.out.println(numbers.size());
		assertEquals(100, numbers.size());
	}
	
	public void testDelete() throws Exception{
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		dao.delete("13512345777");
	}
	
	public void testupdate() throws Exception{
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		dao.update("13512345776", "13512345779");
	}
}
