package com.lzh.activity;

import java.util.List;

import com.lzh.app.MusicApplication;
import com.lzh.component.CircleImageView;
import com.lzh.constants.StateConstant;
import com.lzh.db.MusicDB;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.net.JSONUtils;
import com.lzh.service.MusicService;
import com.lzh.util.ImageCacher;
import com.lzh.util.MusicUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class HomeActivity extends Activity implements OnTouchListener{
     
	
	private RelativeLayout home_music;
	private RelativeLayout home_fav;
	private RelativeLayout home_list;
	private RelativeLayout home_settings;
	private CircleImageView userhead;
	private ImageView song_pic;
	private ImageView usergroup;
	private ImageSwitcher switcher;
	private Drawable[] bitmaps;
	private int pic_position;
	private List<String> pic_descriptions;
	private static HomeBroadReceiver receiver;
	private HomeHandler handler;
	private TextView music_name;
	private TextView artist_name;
	private TextView username;
	private static List<MediaFile> mediaFiles; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_layout);
		MusicApplication.getInstance().addActivity(this);
		bitmaps  = new Drawable[]{new BitmapDrawable(MusicUtils.decodePng(HomeActivity.this, R.drawable.one)),
				new BitmapDrawable(MusicUtils.decodePng(HomeActivity.this, R.drawable.two)),
				new BitmapDrawable(MusicUtils.decodePng(HomeActivity.this, R.drawable.three))};
		setUpViews();
		mediaFiles = MusicService.mediaFiles;
		receiver = new HomeBroadReceiver();
		handler = new HomeHandler(getMainLooper());
		Message msg = handler.obtainMessage(StateConstant.HOME_TOP_REFRESH);
		handler.removeMessages(StateConstant.HOME_TOP_REFRESH);
		handler.sendMessage(msg);
	}


	private void initUser() {
		SharedPreferences preferences = getSharedPreferences("usercache",0);
		String current_username = preferences.getString("uname", "");
		if(current_username.equals("")){
			userhead.setImageResource(R.drawable.user_not_login);
			username.setText("未登录");
		}else{
			MusicDB db = new MusicDB(this);
			MusicService.current_user =  db.getUserFromCacheByName(current_username);
			username.setText(MusicService.current_user.getNickname());
			MusicService.current_user.setLists(db.getUserPlaylists(MusicService.current_user.getId()));
			Message msg = handler.obtainMessage(StateConstant.LOAD_HEAD_CACHE);
			handler.removeMessages(StateConstant.LOAD_HEAD_CACHE);
			handler.sendMessage(msg);
		}
	}
	@Override
	protected void onPause(){
		super.onPause();
		unregisterReceiver(receiver);
	}


	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver();
		music_name.setText(MusicService.getMusicName());
		artist_name.setText(MusicService.getArtistName());
		if(!MusicService.getMusicName().equals("")){
			song_pic.setVisibility(View.VISIBLE);
		}
		initUser();
	}
	
	private void setUpViews(){
		userhead = (CircleImageView) findViewById(R.id.home_userhead);
		userhead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(MusicService.current_user == null){
				  Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
				  startActivity(intent);
				}else{
				  Intent intent = new Intent(HomeActivity.this,UserPageActivity.class);
				  startActivity(intent);
				}
			}
		});
		username = (TextView) findViewById(R.id.home_username);
		song_pic = (ImageView) findViewById(R.id.home_music_view);
		usergroup = (ImageView) findViewById(R.id.home_usergroup);
		home_music = (RelativeLayout) findViewById(R.id.home_to_music);
		home_music.setOnClickListener(getOnClickListener(home_music));
		home_fav = (RelativeLayout) findViewById(R.id.home_to_favorite);
		home_fav.setOnClickListener(getOnClickListener(home_fav));
		home_list = (RelativeLayout) findViewById(R.id.home_to_list);
		home_list.setOnClickListener(getOnClickListener(home_list));
		home_settings = (RelativeLayout) findViewById(R.id.home_to_settings);
		home_settings.setOnClickListener(getOnClickListener(home_settings));
		artist_name = (TextView) findViewById(R.id.home_music_status_aname);
		music_name = (TextView) findViewById(R.id.home_music_status_mname);
		switcher = (ImageSwitcher) findViewById(R.id.home_img_switcher);
		switcher.setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				final ImageView view = new ImageView(HomeActivity.this);
				view.setScaleType(ImageView.ScaleType.FIT_XY);
				view.setLayoutParams(new ImageSwitcher.LayoutParams
						(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
				return view;
			}
		});
		switcher.setBackgroundDrawable(bitmaps[1]); //默认第一张
		switcher.setOnTouchListener(this);
		pic_descriptions = JSONUtils.getRecommendDescriptions();
	}
     
	private float downX;   //手指放下时的X坐标
	private float upX;   //手指松开时的X坐标
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			downX = event.getX();
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			upX = event.getX();
			if(upX - downX > 100){
			    showPrePic();
			}else if (downX - upX > 100){
				showNextPic();
			}else{
				Intent intent = new Intent(HomeActivity.this,RecommendActivity.class);
				intent.putExtra("description", pic_descriptions.get(pic_position));
				intent.putExtra("pic_position", pic_position);
				startActivity(intent);
			}
			return true;
		}
		return false;
	}
	
	private OnClickListener getOnClickListener(View view){
		switch (view.getId()) {
		case R.id.home_to_music:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HomeActivity.this,DownloadManageActivity.class);
					startActivity(intent);
				}
			};
		case R.id.home_to_favorite:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
				    if(MusicService.current_user == null){
					   Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
					   startActivity(intent);
					}else{
					Intent intent = new Intent(HomeActivity.this,FavoriteActivity.class);
					startActivity(intent);
					}
				}
			}; 
		case R.id.home_to_list:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					 
					    int id =(int)mediaFiles.get(0).getMusic_id();
					    Intent intent = new Intent(StateConstant.send_POSITION_FROM_ARTIST);
					    intent.putExtra("song_id", id);
					    Log.i("music_id", String.valueOf(id)); 
					    sendBroadcast(intent);
					    Intent intent1 = new Intent(HomeActivity.this,MusicActivity.class);
					    Bundle bundle = new Bundle();
					    bundle.putInt("music_id", id);
					    bundle.putBoolean("fromlist", false);
					    intent1.putExtras(bundle);
					    startActivity(intent1);
					    finish();		
				}
			};
		case R.id.home_to_settings:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(MusicService.current_user == null){
					  Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
					  startActivity(intent);
					}else{
					  Intent intent = new Intent(HomeActivity.this,PlaylistActivity.class);
					  startActivity(intent);
					}
				}
			};
		}
		return null;
	}
    

		private void showNextPic(){
			if(pic_position+1 >= bitmaps.length){
				pic_position = 0;
			}else{
				pic_position++;
			}
			switcher.setBackgroundDrawable(bitmaps[pic_position]);
		}
		
		private void showPrePic(){
			if(pic_position -1 <0){
				pic_position = bitmaps.length-1;
			}else{
				pic_position --;
			}
			switcher.setBackgroundDrawable(bitmaps[pic_position]);
		}
		
		
		private  class HomeHandler extends Handler{
           
			public HomeHandler(Looper looper){
				super(looper);
			}
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case StateConstant.HOME_TOP_REFRESH:
					nextRefresh(refreshNow()); //顶部自动刷新
					break;
				case StateConstant.LOAD_HEAD_CACHE:
					Bitmap bitmap = ImageCacher.getUserCacheHead(MusicService.current_user.getUsername());
					userhead.setImageBitmap(bitmap);
//					usergroup.setVisibility(View.VISIBLE);
					if(MusicService.current_user.getGroup() == 0){
						usergroup.setImageResource(R.drawable.superuse_not);
					}else{
						usergroup.setImageResource(R.drawable.superuser);
					}
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
			  
		}
		
		private long refreshNow(){
			showNextPic();
			return 3000L;
		}
		
		private void nextRefresh(long interval){
			if(bitmaps.length != 0){
			Message msg = handler.obtainMessage(StateConstant.HOME_TOP_REFRESH);
			handler.removeMessages(StateConstant.HOME_TOP_REFRESH);
			handler.sendMessageDelayed(msg, interval);
			}
		}
		
		private class HomeBroadReceiver extends BroadcastReceiver{
			@Override
			public void onReceive(Context context, Intent intent) {
				     String mname = intent.getExtras().getString("music_name");
				     String aname = intent.getExtras().getString("artist_name");
				     music_name.setText(mname);
				     artist_name.setText(aname);
			}
		}
		private void registerReceiver(){
		    	IntentFilter filter = new IntentFilter(StateConstant.SEND_MUSIC_INFO_FROM_SERVICE); //从Service获取歌曲信息
		    	registerReceiver(receiver, filter);
		 }
}
