package com.lzh.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.lzh.app.MusicApplication;
import com.lzh.component.CircleImageView;
import com.lzh.constants.StateConstant;
import com.lzh.db.MusicDB;
import com.lzh.lzhmusic.R;
import com.lzh.model.Playlist;
import com.lzh.model.User;
import com.lzh.service.MusicService;
import com.lzh.util.ImageCacher;
import com.lzh.util.ImgBlur;
import com.lzh.util.MusicIntent;
import com.lzh.util.MusicUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserPageActivity extends Activity {
	
	private ImageButton back;
	private CircleImageView userhead;
	private TextView name;
	private TextView follow;
	private TextView friend;
	private TextView list;
	private ImageView vip_level;
	private ImageView singer_blur;
	private ImageView[] view;
	private TextView[] text;
	private Drawable[] drawables;
	private Button logoff;
	
	private CircleImageView music1;
	private CircleImageView music2;
	private CircleImageView music3;
	private CircleImageView music4;
	private CircleImageView music5;
	private CircleImageView music6;
	
	private RelativeLayout to_list;
	
	private User user;
	private MusicDB db;
	
	private static final int LOAD_IMG = 0;
	private UIHandler handler;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_page);
		initData();
		initViews();
	}


	private void initData() {
	    db = new MusicDB(this);
		user = MusicService.current_user;
		drawables = new Drawable[]{new BitmapDrawable(MusicUtils.decodePng(UserPageActivity.this, R.drawable.fav_playlist)),
				new BitmapDrawable(MusicUtils.decodePng(UserPageActivity.this, R.drawable.recent_played)),
				new BitmapDrawable(MusicUtils.decodePng(UserPageActivity.this, R.drawable.default_playlist_bg))};
	}
	
	private void initViews() {
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserPageActivity.this,MainActivity.class);
				startActivity(intent);
			}
		}); 
		userhead = (CircleImageView) findViewById(R.id.userinfo_userhead);
		if(user.getHead_cache()!=null){
			userhead.setImageBitmap(ImageCacher.getUserCacheHead(user.getUsername()));
		}
		userhead.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				loadDCIM();
			}
		});
		singer_blur = (ImageView) findViewById(R.id.singer_blur_pic);
		handler = new UIHandler(Looper.myLooper());
		Message msg = handler.obtainMessage(LOAD_IMG);
		handler.removeMessages(LOAD_IMG);
		handler.sendMessageDelayed(msg, 500L);
		name = (TextView) findViewById(R.id.userinfo_username);
		name.setText(user.getNickname());
		follow = (TextView) findViewById(R.id.userinfo_follow_number);
		follow.setText(String.valueOf(user.getFollow()));
		friend = (TextView) findViewById(R.id.userinfo_friend_number);
		friend.setText(String.valueOf(MusicService.downloadedFiles.size()));
		list = (TextView) findViewById(R.id.userinfo_list_number);
		list.setText(String.valueOf(MusicService.current_user.getLists().size()));
		vip_level = (ImageView) findViewById(R.id.vip_icon);
		vip_level.setImageResource(R.drawable.superuse_not);
		view = new ImageView[]{(ImageView) findViewById(R.id.userinfo_1_pic),
				(ImageView) findViewById(R.id.userinfo_2_pic),
		(ImageView) findViewById(R.id.userinfo_3_pic)};
		text = new TextView[]{ (TextView) findViewById(R.id.userinfo_1_text),
				 (TextView) findViewById(R.id.userinfo_2_text),
				 (TextView) findViewById(R.id.userinfo_3_text)};
		logoff = (Button) findViewById(R.id.logoff);
		logoff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				logoff();
			}
		});
		to_list = (RelativeLayout) findViewById(R.id.userinfo_lists);
		to_list.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserPageActivity.this,PlaylistActivity.class);
				startActivity(intent);
			}
		});
		loadFavs();
		music1 = (CircleImageView) findViewById(R.id.music_1);
		music1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gene_anim));
		music1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play(0);
			}
		});
		music2 = (CircleImageView) findViewById(R.id.music_2);
		music2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gene_anim2));
		music2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 play(1);
			}
		});
		music3 = (CircleImageView) findViewById(R.id.music_3);
		music3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gene_anim3));
		music3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play(2);
			}
		});
		music4 = (CircleImageView) findViewById(R.id.music_4);
		music4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gene_anim4));
		music4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play(3);
			}
		});
		music5 = (CircleImageView) findViewById(R.id.music_5);
		music5.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gene_anim5));
		music1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			   play(4);
			}
		});
		music6 = (CircleImageView) findViewById(R.id.music_6);
		music6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.gene_anim6));
		music1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				play(5);
			}
		});
		
	}
	
	 private void loadFavs() {
		  List<Playlist> lists = MusicService.current_user.getLists();
		  int size = lists.size();
		  if(size<3){
			  for(int i=0;i<size;i++){
				  view[i].setBackgroundDrawable(drawables[i]);
				  text[i].setText(lists.get(i).getName());
			  }
		  }else{
			  for(int i=0;i<3;i++){
				  view[i].setBackgroundDrawable(drawables[i]);
				  text[i].setText(lists.get(i).getName());
			  }
		  }
		  registerListener(lists);
	}


	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
	   super.onActivityResult(requestCode, resultCode, data);
	   userhead.setImageBitmap(cameraBitmap);
	   ImageCacher.cacheUserhead(cameraBitmap, user.getUsername());
	   db.saveUserCacheHeadPath(user.getUsername());
	   Message msg = handler.obtainMessage(LOAD_IMG);
	   handler.removeMessages(LOAD_IMG);
	   handler.sendMessageDelayed(msg, 500L);
	 }
     
	 //读取相机图片
	 private void loadDCIM() {
			Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
		    intent.addCategory(Intent.CATEGORY_OPENABLE);
		    intent.setType("image/*");
		    intent.putExtra("crop", "true");
		    intent.putExtra("aspectX", 1);
		    intent.putExtra("aspectY", 1);
		    intent.putExtra("outputX", 80);
		    intent.putExtra("outputY", 80);
		    intent.putExtra("return-data", true);
		    startActivityForResult(intent, 0);
	}
	 

	private void registerListener(final List<Playlist> lists) {
	   view[0].setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(UserPageActivity.this,PlaylistInfoActivity.class);
			intent.putExtra("list_id", db.getPlaylistIdByName(lists.get(0).getName()));
			UserPageActivity.this.startActivity(intent);
		}
	   });
	   if(lists.size()>=2){
	   view[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserPageActivity.this,PlaylistInfoActivity.class);
				intent.putExtra("list_id", db.getPlaylistIdByName(lists.get(1).getName()));
				UserPageActivity.this.startActivity(intent);
			}
		  });
	   }
	   if(lists.size() == 3){
	   view[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserPageActivity.this,PlaylistInfoActivity.class);
				intent.putExtra("list_id", db.getPlaylistIdByName(lists.get(2).getName()));
				UserPageActivity.this.startActivity(intent);
			}
	   });
	   }
	}
	
	//Session内user
	//SP内user
	//
	private void logoff(){
		MusicService.current_user = null;
		SharedPreferences sp = getSharedPreferences("usercache",0);
		Editor editor = sp.edit();
		editor.remove("uname");
		editor.commit();
		Intent intent = new Intent(UserPageActivity.this,MainActivity.class);
		startActivity(intent);
	}

	private void play(int i) {
		  Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
		  to_service_intent.putExtra("position", i);
		  sendBroadcast(to_service_intent);
		  MusicService.setPath(MusicService.mediaFiles.get(i).getPath());
		  MusicService.resetStatuse();
		  MusicIntent mIntent = new MusicIntent(getApplicationContext());
		  mIntent.setAction(StateConstant.PLAY);
		  mIntent.putExtra("mode", 1);
		  startService(mIntent);
	}
     private class UIHandler extends Handler{
		
		public UIHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case LOAD_IMG:
				File file = new File(MusicApplication.USER_HEAD_CACHE_FOLDER+user.getUsername()+".png");
				if(file.exists()){
				try {
					FileInputStream fis = new FileInputStream(file);
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
				}
				break;
			}
		}
		
      }
	
}
