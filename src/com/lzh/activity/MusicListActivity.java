package com.lzh.activity;

import java.util.HashMap;
import java.util.List;

import com.lzh.adapter.MusicListAdapter;
import com.lzh.adapter.MyCursorAdapter;
import com.lzh.animation.AlbumRotationAnim;
import com.lzh.app.MusicApplication;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.service.MusicService;
import com.lzh.util.MusicIntent;
import com.lzh.util.MusicUtils;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class MusicListActivity extends FragmentActivity{
    
	
	private TextView bottom_musicname;     //底部Bar 音乐名
	private TextView bottom_artistname;     //底部Bar 歌手名
	private TextView total_song_num;         //统计
	private ImageView bottom_album_pic;
	private static Intent play_intent;    

	private static List<MediaFile> mediaFiles;   
	private ImageView song,arist,album;
	
	private static int current_positon;
	private static int fragment_position = 0;
	
	private MusicListBroadcastReceiver receiver;
	private BottomBarHandler handler;
	
	private AllSongFragment all_song_fragment;
	private AllArtistFragment all_artist_fragment;
	private AllAlbumFragment all_album_fragment;
	private RelativeLayout all_song,all_artist,all_album;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MusicApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		
		bottom_artistname = (TextView) findViewById(R.id.music_list_bottom_artistname);
		bottom_musicname = (TextView) findViewById(R.id.music_list_bottom_musicname);
		bottom_album_pic = (ImageView) findViewById(R.id.music_list_bottom_pic);
		
		mediaFiles = MusicService.mediaFiles;
		if(mediaFiles == null){
			Toast.makeText(getApplicationContext(), "SD卡内没有音乐哦，快去下载吧！", Toast.LENGTH_SHORT).show();
		}
		handler = new BottomBarHandler(Looper.myLooper());
		
		current_positon = MusicService.getPosition();
		setSideBar();
		initFragments();
		setUpFirstFragment();
	}

   
	private void registerReceiver(){
		  receiver = new MusicListBroadcastReceiver();
		  IntentFilter filter = new IntentFilter(StateConstant.SEND_POSITION_FROM_SERVICE);
		  registerReceiver(receiver, filter);
	}
	
	//播放列表内的监听器，用于监听音乐播放状态并更新底部状态栏
	private class MusicListBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			    if(intent.getAction().equals(StateConstant.SEND_POSITION_FROM_SERVICE)){
			      current_positon = intent.getIntExtra("position", 0);
			      setSideBar();
			    }
		}
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
		mediaFiles = MusicService.allFiles;
		current_positon = MusicService.getPosition();
		setSideBar();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	private void setSideBar(){
		   if(MusicService.isPrepared()){
		   Message msg = handler.obtainMessage(StateConstant.BOTTOM_REFRESH);
		   handler.removeMessages(StateConstant.BOTTOM_REFRESH);
		   handler.sendMessage(msg);
		   }
	}
	
	private class BottomBarHandler extends Handler{
		
		public BottomBarHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg){
			   
		switch (msg.what) {
			case StateConstant.BOTTOM_REFRESH:
				   if(mediaFiles != null){
				   bottom_musicname.setText("正在播放 : "+mediaFiles.get(current_positon).getMusic_name());
				   bottom_artistname.setText(mediaFiles.get(current_positon).getArtist_name());
				   }else{
					   bottom_musicname.setText("欢迎使用Swift Music V2.0");
				   }
				break;
			default:
				break;
			}
		}
		
	}
	
	private void initFragments(){
		all_song_fragment = new AllSongFragment();
		all_artist_fragment = new AllArtistFragment();
		all_album_fragment = new AllAlbumFragment();
		all_song  = (RelativeLayout) findViewById(R.id.top_songs);
		all_artist = (RelativeLayout) findViewById(R.id.top_artists);
		all_album = (RelativeLayout) findViewById(R.id.top_albums);
		all_song.setOnClickListener(new MyOnClickListener(0));
		all_artist.setOnClickListener(new MyOnClickListener(1));
		all_album.setOnClickListener(new MyOnClickListener(2));
	}
	
	class MyOnClickListener implements OnClickListener {
		int index;  //保存点击时传入的index
		public MyOnClickListener(int index) {
		this.index = index;
		}
		@Override
		public void onClick(View v) {
		fragmentTransaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.top_songs:
	    all_song.setBackgroundResource(R.drawable.top_selector_bg);
	    all_artist.setBackgroundResource(0);
	    all_album.setBackgroundResource(0);
		fragmentTransaction.replace(R.id.content, all_song_fragment);
		break;
		case R.id.top_artists:
		all_song.setBackgroundResource(0);
		all_artist.setBackgroundResource(R.drawable.top_selector_bg);
		all_album.setBackgroundResource(0);
		fragmentTransaction.replace(R.id.content, all_artist_fragment);
		break;
		case R.id.top_albums:
		all_song.setBackgroundResource(0);
		all_artist.setBackgroundResource(0);
		all_album.setBackgroundResource(R.drawable.top_selector_bg);
		fragmentTransaction.replace(R.id.content, all_album_fragment);
		break;
		default:
		break;
		}
//		if(index != fragment_position){
		fragmentTransaction.commit();
//		}
		fragment_position = index;   //保存当前index
		}
	}
	
	//填充第一个Fragment
	private void setUpFirstFragment(){
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content, all_song_fragment);
		fragmentTransaction.commit();
	}
	
}
