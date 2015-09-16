package com.lzh.activity;


import com.lzh.app.ApplicationManager;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.util.MusicIntent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;

public class InitActivity extends Activity {
	
	private MyHandler handler;
	private static final int IS_FIRST = 1;
	private static final int NOT_FIRST = 2;
	private static MusicIntent musicIntent;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.init_layout);
		ApplicationManager.getInstance().addActivity(this);
		handler = new MyHandler(Looper.myLooper());
		musicIntent = new MusicIntent(InitActivity.this);
		musicIntent.setAction(StateConstant.PREPARE);  //Service½øÈëÆô¶¯×´Ì¬
		startService(musicIntent);
		redirectPage();
		
	}
    
	private void redirectPage(){
		SharedPreferences preferences = getSharedPreferences("config",0);
		boolean isFirst = preferences.getBoolean("isFirst", true);
		if(isFirst){
			Message msg = handler.obtainMessage(IS_FIRST);
			handler.removeMessages(IS_FIRST);
			handler.sendMessageDelayed(msg, 3000L);
		}else{
			Message msg = handler.obtainMessage(NOT_FIRST);
			handler.removeMessages(NOT_FIRST);
			handler.sendMessageDelayed(msg, 3000L);
		}
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirst", false);
        editor.commit();
	}
	
	
	class MyHandler extends Handler{
		
		public MyHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			  case IS_FIRST:
				  Intent intent = new Intent(InitActivity.this,ViewPagerActivity.class);
				  startActivity(intent);
				  overridePendingTransition(R.anim.fade, R.anim.hold);
				  ApplicationManager.getInstance().finishActivity();
				  break;
			  case NOT_FIRST:
				  Intent intent1 = new Intent(InitActivity.this,MainActivity.class);
				  startActivity(intent1);
				  overridePendingTransition(R.anim.fade, R.anim.hold); 
				  ApplicationManager.getInstance().finishActivity();
			}
		}
	}
}
