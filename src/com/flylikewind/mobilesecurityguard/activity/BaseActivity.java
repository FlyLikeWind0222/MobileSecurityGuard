package com.flylikewind.mobilesecurityguard.activity;

import android.app.Activity;
import android.view.View;

public class BaseActivity extends Activity{
	
	@SuppressWarnings("unchecked")
	protected <A extends View> A getView(int id){
		return (A)findViewById(id);
	}
}
