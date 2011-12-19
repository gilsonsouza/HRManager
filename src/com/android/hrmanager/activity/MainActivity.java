package com.android.hrmanager.activity;

import com.android.hrmanager.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity{
	
	public static final String TAG = "hrmanager";
	public static final String SAP_PREFS = "sapPrefs";
	public static final String AUTHENTICATED = "authenticated";
	public static final String LOGIN_REQUIRED = "loginRequired";
	public static final String USER_ID = "userId";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getAuth() && !getLoginRequired()) {
			actionNewsScreen();
		} else {
			actionLoginScreen();
		}
	}
	
	public boolean getAuth() {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(SAP_PREFS, Context.MODE_PRIVATE);
		return sapPreferences.getBoolean(AUTHENTICATED, false);
	}
	
	public boolean getLoginRequired() {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(SAP_PREFS, Context.MODE_PRIVATE);
		return sapPreferences.getBoolean(LOGIN_REQUIRED, false);
	}

	private void actionLoginScreen() {
		Intent it = new Intent(this, LoginActivity.class);
		startActivity(it);
		finish();
	}
	
	private void actionNewsScreen() {
		Intent it = new Intent(this, NewsActivity.class);
		startActivity(it);
		finish();
	}

}
