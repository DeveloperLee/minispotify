package com.lzh.activity;

import com.lzh.adapter.SettingsAdapter;
import com.lzh.app.MusicApplication;
import com.lzh.lzhmusic.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
public class SettingsActivity extends Activity {
	
	private ListView basic;
	private ListView advance;
	private ListView about;
	
	private SettingsAdapter basic_adapter;
	private SettingsAdapter advance_adapter;
	private SettingsAdapter about_adapter;
	
	private String[] basic_content;
	private int[] basic_resource;
	private String[] advance_content;
	private int[] advance_resource;
	private String[] about_content;
	private int[] about_resource;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		MusicApplication.getInstance().addActivity(this);
		initData();
		initViews();
	}

	private void initData() {
		basic_content = new String[]{"缓存管理","音效设置","定时关闭"};
		basic_resource = new int[]{R.drawable.cache_manage,R.drawable.sound_effect,R.drawable.time_close};
		advance_content = new String[]{"同步"};
		advance_resource = new int[]{R.drawable.sync};
		about_content = new String[]{"关于SwiftMusic"};
		about_resource = new int[]{R.drawable.about};
	}

	private void initViews() {
		basic = (ListView) findViewById(R.id.settings_basic);
		basic_adapter = new SettingsAdapter(this,basic_content,basic_resource);
		basic.setAdapter(basic_adapter);
		advance = (ListView) findViewById(R.id.settings_advance);
		advance_adapter = new SettingsAdapter(this,advance_content,advance_resource);
		advance.setAdapter(advance_adapter);
		about = (ListView) findViewById(R.id.settings_about);
		about_adapter = new SettingsAdapter(this,about_content,about_resource);
		about.setAdapter(about_adapter);
	}

}
