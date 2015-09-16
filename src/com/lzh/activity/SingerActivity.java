package com.lzh.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.lzh.adapter.SingerSongsAdapter;
import com.lzh.app.MusicApplication;
import com.lzh.component.CircleImageView;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.net.JSONUtils;
import com.lzh.service.MusicService;
import com.lzh.util.ImgBlur;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SingerActivity extends Activity {
	
	private ImageButton back;
	private TextView name;
	private ImageView singer_blur;
	private ImageView singer_head;
	private ImageView icon;
	private ListView songs;
	private CircleImageView[] views;
	
	private String singer_name;
	private UIHandler handler;
	
	private String[] names;
	private int[] resources;
	private int position;
	private int item_position;
	private SingerSongsAdapter adapter;
	private List<MediaFile> files;
	
	private static final int LOAD_IMG = 0;
	private static final int LOAD_SONG_START = 1;
	private static final int LOAD_SONG_FINISH = 2;
	private static final String JSON_SONGS = "http://172.16.185.157:8080/MusicPlayer/searchMusicByArtist.action?artistName=";
	private static final int DOWNLOAD_SONG = 3;
	private static final int START_ANIM = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_singer);
		initData();
		initViews();
		InitThread t = new InitThread();
		t.start();
	}
	
	private void initData() {
		singer_name = getIntent().getStringExtra("singer_name");
		position = getIntent().getIntExtra("position", 0);
		handler = new UIHandler(Looper.myLooper());
		names = new String[]{"Deng","Linjunjie","Zhoujielun","Wuyuetian","Haoshengyin"};
		resources = new int[]{R.drawable.abb,R.drawable.aaa,R.drawable.acc,R.drawable.add,R.drawable.aee};
	}

	private void initViews() {
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SingerActivity.this,OnlineMusicActivity.class);
				startActivity(intent);
			}
		});
		views = new CircleImageView[]{(CircleImageView) findViewById(R.id.music_1),
				(CircleImageView) findViewById(R.id.music_2),
				(CircleImageView) findViewById(R.id.music_3),
				(CircleImageView) findViewById(R.id.music_4),
				(CircleImageView) findViewById(R.id.music_5),
				(CircleImageView) findViewById(R.id.music_6)};
		Message msg2 = handler.obtainMessage(START_ANIM);
		handler.removeMessages(START_ANIM);
		handler.sendMessageDelayed(msg2, 500L);
		name = (TextView) findViewById(R.id.singer_name);
		name.setText(singer_name);
		icon = (ImageView) findViewById(R.id.singer_loading);
		singer_head = (ImageView) findViewById(R.id.singerinfo_userhead);
		singer_head.setImageResource(resources[position]);
		singer_blur = (ImageView) findViewById(R.id.singer_blur_pic);
		songs = (ListView) findViewById(R.id.singer_songs);
		songs.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				item_position = arg2;
				Message msg = handler.obtainMessage(DOWNLOAD_SONG);
				handler.removeMessages(DOWNLOAD_SONG);
				handler.sendMessage(msg);
			}
		});
		Message msg = handler.obtainMessage(LOAD_IMG);
		handler.removeMessages(LOAD_IMG);
		handler.sendMessageDelayed(msg, 500L);
	}
    
	private class UIHandler extends Handler{
		
		public UIHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			  case LOAD_IMG:
				  try {
					FileInputStream fis = new FileInputStream(MusicApplication.SINGER_CACHE+names[position]+".png");
					singer_blur.setBackgroundDrawable(ImgBlur.BoxBlurFilter(BitmapFactory.decodeStream(fis)));
					singer_blur.setVisibility(View.VISIBLE);
					AlphaAnimation anim = new AlphaAnimation(0f, 1.0f);
					anim.setFillAfter(true);
					anim.setFillBefore(true);
					anim.setDuration(1000L);
					singer_blur.startAnimation(anim);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				  break;
			  case LOAD_SONG_START:
				  break;
			  case LOAD_SONG_FINISH:
				  if(files == null){
					  icon.setImageResource(R.drawable.error_no_net);
				  }else if(files.size() == 0){
					  icon.setImageResource(R.drawable.empty_music_list);
				  }else{
					  icon.setVisibility(View.GONE);
					  adapter = new SingerSongsAdapter(SingerActivity.this,files);
					  songs.setAdapter(adapter);
					  songs.setVisibility(View.VISIBLE);
				  }
				  break;
			  case DOWNLOAD_SONG:
				   play(item_position);
				   break;
			  case START_ANIM:
				  for(int i=0;i<views.length;i++){
					  views[i].setVisibility(View.VISIBLE);
					  AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
					  anim.setFillAfter(true);
					  anim.setDuration(1000L);
					  anim.setStartOffset(1000 * i);
					  views[i].startAnimation(anim);
				  }
				  break;
			}
		}
	}
     class InitThread extends Thread{
		
		@Override
		public void run(){
			  Message msg = handler.obtainMessage(LOAD_SONG_START);
			  handler.removeMessages(LOAD_SONG_START);
			  handler.sendMessage(msg);
			  String json = JSONUtils.getSongByPost(JSON_SONGS+singer_name,singer_name);
			  files = JSONUtils.getSingerSong(json);
			  Message msg1 = handler.obtainMessage(LOAD_SONG_FINISH);
			  handler.removeMessages(LOAD_SONG_FINISH);
			  handler.sendMessage(msg1);
		}
	} 
 	  private void play(int position){
		 MusicService.downloadedFiles.add(files.get(position));
		 MusicService.mediaFiles = MusicService.downloadedFiles;
		 Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
		 to_service_intent.putExtra("position", MusicService.downloadedFiles.size()-1);
		 sendBroadcast(to_service_intent);
		 Intent intent = new Intent(SingerActivity.this,MusicActivity.class);
		 intent.putExtra("internet", true);
		 intent.putExtra("position", MusicService.downloadedFiles.size()-1);
		 intent.putExtra("path", files.get(position).getPath());
		 startActivity(intent); 
	}
}
