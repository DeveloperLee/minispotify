package com.lzh.activity;

import java.util.List;

import com.lzh.adapter.MusicListAdapter;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.net.JSONUtils;
import com.lzh.service.MusicService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommendActivity extends Activity {
	
	private ImageButton back_btn;
	private ImageButton play;
	private RelativeLayout pic;
	private TextView description;
	private ListView list;
	
	private int pic_position;
	private String pic_description;
	private List<MediaFile> songs;
	private MusicListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recommend);
		initData();
		initView();
	}

	private void initData() {
		pic_position = getIntent().getIntExtra("pic_position",0);
		pic_description = getIntent().getStringExtra("description");
		songs = JSONUtils.getRecommendSongsById(pic_position);
		adapter = new MusicListAdapter(this,songs);
	}

	private void initView() {
		back_btn = (ImageButton) findViewById(R.id.back);
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RecommendActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		description = (TextView) findViewById(R.id.recommend_name);
		description.setText(pic_description);
		play = (ImageButton) findViewById(R.id.recommend_play);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play(0);
			}
		});
		pic = (RelativeLayout) findViewById(R.id.recommend_pic);
		switch(pic_position){
		   case 0:
			   pic.setBackgroundResource(R.drawable.one);
			   break;
		   case 1:
			   pic.setBackgroundResource(R.drawable.two);
			   break;
		   case 2:
			   pic.setBackgroundResource(R.drawable.three);
			   break;
		}
		list = (ListView) findViewById(R.id.recommend_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				play(arg2);
			}
		});
	}
	
	private void play(int position) {
		MusicService.mediaFiles = songs;
		Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
		to_service_intent.putExtra("position", position);
	    sendBroadcast(to_service_intent);
		Intent intent = new Intent(RecommendActivity.this,MusicActivity.class);
		Bundle b = new Bundle();
		b.putString("path", songs.get(position).getPath());
		b.putInt("position", position);   
		b.putBoolean("fromlist", true);
		intent.putExtras(b);
		startActivity(intent);
		finish();
	}

}
