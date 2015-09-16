package com.lzh.activity;


import com.lzh.app.ApplicationManager;
import com.lzh.app.MusicApplication;
import com.lzh.constants.StateConstant;
import com.lzh.db.MusicDB;
import com.lzh.lzhmusic.R;
import com.lzh.util.MusicIntent;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity implements OnCheckedChangeListener {
	//定义Tab选项卡标示符
	private static final String HOME_TAB = "home_tab";
	private static final String MENTION_TAB = "mention_tab";
	private static final String PERSON_TAB = "person_tab";
	private static final String MORE_TAB = "more_tab";
	private static final String PREF_NAME = "tab_name";
	private int quit_count = 0;
	private static MusicIntent musicIntent;   
	private static HomeBroadReceiver receiver;
    
	//定义Intent对象
	private Intent mHomeIntent,mMentionIntent,mPersonIntent,mMoreIntent;
	  

	//定义TabHost对象
	private TabHost mTabHost;
	
	private SharedPreferences sp;

	//定义单选按钮对象
	private RadioButton homeRb,mentionRb,personRb,moreRb;
	
    private MainActivityHandler handler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabhost);
		ApplicationManager.getInstance().addActivity(this);
		musicIntent = new MusicIntent(MainActivity.this);
		handler = new MainActivityHandler(Looper.myLooper());
		sp = getSharedPreferences("config", 0);
		initView();
		initData();
		receiver = new HomeBroadReceiver();
		initDB(); //初始化默认播放列表
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
		getPreference();
	}

	/**
	 * 初始化组件
	 */
	private void initView(){
		//得到TabHost
		mTabHost = getTabHost();
		
		//得到Intent对象
		mHomeIntent = new Intent(this, HomeActivity.class);
		mMentionIntent = new Intent(this, MusicListActivity.class);
		mPersonIntent = new Intent(this, NetworkLoadingActivity.class);
		mMoreIntent = new Intent(this, SettingsActivity.class);
		
		//得到单选按钮对象
		homeRb = ((RadioButton) findViewById(R.id.radio_home));
		mentionRb = ((RadioButton) findViewById(R.id.radio_mention));
		personRb = ((RadioButton) findViewById(R.id.radio_person_info));
		moreRb = ((RadioButton) findViewById(R.id.radio_more));
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		//给单选按钮设置监听
		homeRb.setOnCheckedChangeListener(this);
		mentionRb.setOnCheckedChangeListener(this);
		personRb.setOnCheckedChangeListener(this);
		moreRb.setOnCheckedChangeListener(this);
		
		
		//添加进Tab选项卡
		mTabHost.addTab(buildTabSpec(HOME_TAB, mHomeIntent));
		mTabHost.addTab(buildTabSpec(MENTION_TAB, mMentionIntent));
		mTabHost.addTab(buildTabSpec(PERSON_TAB, mPersonIntent));
		mTabHost.addTab(buildTabSpec(MORE_TAB, mMoreIntent));
		
		getPreference();	
	}
	
	private void initDB(){
		SharedPreferences preferences = getSharedPreferences("config",0);
		boolean initDB = preferences.getBoolean("initDB", true);
		if(initDB){
		  MusicDB helper = new MusicDB(this);
		  helper.initPlaylist(1);
		}
		SharedPreferences.Editor editor = preferences.edit();
	    editor.putBoolean("initDB", false);
	    editor.commit();
	}

	private void getPreference() {
		String tab_name = sp.getString(PREF_NAME, HOME_TAB);
		if(tab_name.equals(HOME_TAB)){
			homeRb.setChecked(true);
		}else if(tab_name.equals(MENTION_TAB)){
			mentionRb.setChecked(true);
		}else if(tab_name.equals(PERSON_TAB)){
			personRb.setChecked(true);
		}else{
			moreRb.setChecked(true);
		}
		mTabHost.setCurrentTabByTag(tab_name);
	}						

	private TabHost.TabSpec buildTabSpec(String tag, Intent intent) {
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setContent(intent).setIndicator("");
		return tabSpec;
	}

	/**
	 * Tab按钮选中监听事件
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			int id = buttonView.getId();
			Message msg = handler.obtainMessage(id);
			handler.removeMessages(id);
			handler.sendMessage(msg);
			Editor editor = sp.edit();
			switch (id) {
			case R.id.radio_home:
				mTabHost.setCurrentTabByTag(HOME_TAB);
				editor.putString(PREF_NAME, HOME_TAB);
				break;
			case R.id.radio_mention:
				mTabHost.setCurrentTabByTag(MENTION_TAB);
				editor.putString(PREF_NAME, MENTION_TAB);
				break;
			case R.id.radio_person_info:
				mTabHost.setCurrentTabByTag(PERSON_TAB);
				break;
			case R.id.radio_more:
				mTabHost.setCurrentTabByTag(MORE_TAB);
				editor.putString(PREF_NAME, MORE_TAB);
				break;
			default:
				break;
			}
			editor.commit();
		}
	}
	
	class MainActivityHandler extends Handler{
		
		public MainActivityHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case R.id.radio_home:
				homeRb.setButtonDrawable(R.drawable.home_pressed);
				homeRb.setTextColor(Color.WHITE);
				mentionRb.setButtonDrawable(R.drawable.local);
				mentionRb.setTextColor(Color.GRAY);
				personRb.setButtonDrawable(R.drawable.online);
				personRb.setTextColor(Color.GRAY);
				moreRb.setButtonDrawable(R.drawable.settings);
				moreRb.setTextColor(Color.GRAY);
				break;
			case R.id.radio_mention:
				homeRb.setButtonDrawable(R.drawable.home);
				mentionRb.setButtonDrawable(R.drawable.local_pressed);
				personRb.setButtonDrawable(R.drawable.online);
				moreRb.setButtonDrawable(R.drawable.settings);
				homeRb.setTextColor(Color.GRAY);
				mentionRb.setTextColor(Color.WHITE);
				personRb.setTextColor(Color.GRAY);
				moreRb.setTextColor(Color.GRAY);
				break;
			case R.id.radio_person_info:
				homeRb.setButtonDrawable(R.drawable.home);
				mentionRb.setButtonDrawable(R.drawable.local);
				personRb.setButtonDrawable(R.drawable.online_pressed);
				moreRb.setButtonDrawable(R.drawable.settings);
				homeRb.setTextColor(Color.GRAY);
				mentionRb.setTextColor(Color.GRAY);
				personRb.setTextColor(Color.WHITE);
				moreRb.setTextColor(Color.GRAY);
				break;
			case R.id.radio_more:
				homeRb.setButtonDrawable(R.drawable.home);
				mentionRb.setButtonDrawable(R.drawable.local);
				personRb.setButtonDrawable(R.drawable.online);
				moreRb.setButtonDrawable(R.drawable.settings_pressed);
				homeRb.setTextColor(Color.GRAY);
				mentionRb.setTextColor(Color.GRAY);
				personRb.setTextColor(Color.GRAY);
				moreRb.setTextColor(Color.WHITE);
				break;
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	//监听返回按钮事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(quit_count == 0){
				Toast.makeText(MainActivity.this, "再按一次返回键退出播放器", Toast.LENGTH_LONG).show();
				quit_count ++;
				return false;         //第一次按下返回键不做任何处理只提示用户
			}else if(quit_count == 1){
				musicIntent.setAction("stop_service");
				startService(musicIntent);
				MusicApplication.getInstance().exit();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK)) {
//			quitDialog();
		}
		return super.dispatchKeyEvent(event);
	}
	
	private class HomeBroadReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
		}
	}
	 private  void registerReceiver(){
	    	IntentFilter filter = new IntentFilter(StateConstant.SEND_MUSIC_INFO_FROM_SERVICE); //从Service获取歌曲信息
	    	registerReceiver(receiver, filter);
 }

	
}
