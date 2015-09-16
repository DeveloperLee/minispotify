package com.lzh.activity;

import java.util.List;
import java.util.Map;

import com.lzh.net.BitmapDownloadTask;
import com.lzh.net.DownLoadThread;
import com.lzh.net.JSONUtils;
import com.lzh.app.MusicApplication;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.service.MusicService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OnlineMusicActivity extends Activity {
    
	private String json[];
	private List<String> singer_names;
	private Map<String,List<MediaFile>> music_map;
	private ImageView[] new_music_img;
	private ImageView[] rank_top_img;
	private ImageButton back;
	private ImageView downloading;
	private TextView[] new_music_name ;
	private TextView[] new_music_artist;
	private TextView[] rank_music_name;
	private TextView[] rank_music_artist;
	private TextView[] hot_singer_names;
	private MyHandler handler;
	
	private int position;
	
	
	private static final String DOWNLOAD = "http://172.16.185.157:8080/MusicPlayer/downloadMusic.action?id=";
	private static final String IMG = "http://172.16.185.157:8080/MusicPlayer/showMusicPic.action?id=";
	
	private static final int LOAD_NEW = 0;
	private static final int LOAD_RANK = 1;
	private static final int LOAD_SINGER = 2;
	private static final int DOWNLOAD_SONG = 3;
	private static final int DOWNLOAD_START = 4;
	private static final int DOWNLOAD_FINISHED = 5;
	
	private List<MediaFile> newMusics;
	private List<MediaFile> rankMusics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.online_layout);
		initData();
		initPage();
	}

	private void initData() {
		handler = new MyHandler(Looper.myLooper());
		loadResponseCache();
		music_map = JSONUtils.decodeJSON(json[0]);
		singer_names = JSONUtils.decodeSinger(json[1]);
	}
	
	private void initPage(){
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OnlineMusicActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		downloading = (ImageView) findViewById(R.id.online_downloading);
		loadNewMusicInfo();
		loadRankMusicInfo();
		loadSingerInfo();
	}

	/**
	 * 读取新歌推荐信息,下载并缓存图片
	 */
	private void loadNewMusicInfo(){
		new_music_img = new ImageView[]{(ImageView) findViewById(R.id.net_newest_a),
				(ImageView) findViewById(R.id.net_newest_b),
				(ImageView) findViewById(R.id.net_newest_c),
				(ImageView) findViewById(R.id.net_newest_d)};
		position = Integer.parseInt(new_music_img[0].getContentDescription().toString());
		new_music_img[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Message msg = handler.obtainMessage(DOWNLOAD_SONG);
				msg.arg1 = position;
				handler.removeMessages(DOWNLOAD_SONG);
				handler.sendMessage(msg);
			}
		});
		new_music_name = new TextView[]{(TextView) findViewById(R.id.net_newest_a_album),
				(TextView) findViewById(R.id.net_newest_b_album),
				(TextView) findViewById(R.id.net_newest_c_album),
				(TextView) findViewById(R.id.net_newest_d_album)};
		new_music_artist = new TextView[]{ (TextView) findViewById(R.id.net_newest_a_artist),
				(TextView) findViewById(R.id.net_newest_b_artist),
				(TextView) findViewById(R.id.net_newest_c_artist),
				(TextView) findViewById(R.id.net_newest_d_artist)};
		Message msg = handler.obtainMessage(LOAD_NEW);
		handler.removeMessages(LOAD_NEW);
		handler.sendMessage(msg);
	}
	
	/**
	 * 读取排行榜信息
	 */
	private void loadRankMusicInfo(){
		rank_top_img = new ImageView[]{(ImageView) findViewById(R.id.rank_first_album_pic),
				(ImageView) findViewById(R.id.rank_second_album_pic),
				(ImageView) findViewById(R.id.rank_third_album_pic)};
		rank_music_name = new TextView[]{(TextView) findViewById(R.id.rank_first_album_name),
				(TextView) findViewById(R.id.rank_second_album_name),
				(TextView) findViewById(R.id.rank_third_album_name)};
		rank_music_artist = new TextView[]{ (TextView) findViewById(R.id.rank_first_artist_name),
				(TextView) findViewById(R.id.rank_second_artist_name),
				(TextView) findViewById(R.id.rank_third_artist_name)};
		newMusics = music_map.get("new");
		Message msg = handler.obtainMessage(LOAD_RANK);
		handler.removeMessages(LOAD_RANK);
		handler.sendMessage(msg);
	}
	
	/**
	 * 读取热门歌手
	 */
	private void loadSingerInfo() {
		hot_singer_names = new TextView[]{(TextView) findViewById(R.id.singer1_name),
				(TextView) findViewById(R.id.singer2_name),
				(TextView) findViewById(R.id.singer3_name),
				(TextView) findViewById(R.id.singer4_name),
				(TextView) findViewById(R.id.singer5_name)};
		hot_singer_names = new TextView[]{(TextView) findViewById(R.id.singer1_name),
				(TextView) findViewById(R.id.singer2_name),
				(TextView) findViewById(R.id.singer3_name),
				(TextView) findViewById(R.id.singer4_name),
				(TextView) findViewById(R.id.singer5_name)};
		rankMusics = music_map.get("rank");
		final RelativeLayout l = (RelativeLayout) findViewById(R.id.singer1);
		final RelativeLayout l2 = (RelativeLayout) findViewById(R.id.singer2);
		final RelativeLayout l3 = (RelativeLayout) findViewById(R.id.singer3);
		l.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OnlineMusicActivity.this,SingerActivity.class);
				String name = ((TextView) l.getChildAt(1)).getText().toString();
				int position = Integer.parseInt(((ImageView) l.getChildAt(0)).getContentDescription().toString());
				intent.putExtra("singer_name", name);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		l2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OnlineMusicActivity.this,SingerActivity.class);
				String name = ((TextView) l2.getChildAt(1)).getText().toString();
				int position = Integer.parseInt(((ImageView) l2.getChildAt(0)).getContentDescription().toString());
				intent.putExtra("singer_name", name);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		l3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OnlineMusicActivity.this,SingerActivity.class);
				String name = ((TextView) l3.getChildAt(1)).getText().toString();
				int position = Integer.parseInt(((ImageView) l3.getChildAt(0)).getContentDescription().toString());
				intent.putExtra("singer_name", name);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		Message msg = handler.obtainMessage(LOAD_SINGER);
		handler.removeMessages(LOAD_SINGER);
		handler.sendMessage(msg);
	}
	
	class MyHandler extends Handler{
		public MyHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			
			switch(msg.what){
			   case LOAD_NEW:
					for(int i=0;i<4;i++){
						MediaFile temp = newMusics.get(i);
						BitmapDownloadTask task = new BitmapDownloadTask(new_music_img[i]);
						task.execute(IMG+temp.getMusic_id(),newMusics.get(i).getMusic_name());
						new_music_name[i].setText(temp.getMusic_name());
						new_music_artist[i].setText(temp.getArtist_name());
					}
				   break;
			   case LOAD_RANK:
					for(int i=0;i<3;i++){
						BitmapDownloadTask task = new BitmapDownloadTask(rank_top_img[i]);
						task.execute(IMG+rankMusics.get(i).getMusic_id(),rankMusics.get(i).getMusic_name());
						MediaFile temp = rankMusics.get(i);
						rank_music_name[i].setText(temp.getMusic_name());
						rank_music_artist[i].setText(temp.getArtist_name());
					}
				   break;
			   case LOAD_SINGER:
				   for(int i=0;i<hot_singer_names.length;i++){
					   hot_singer_names[i].setText(singer_names.get(i));
				   }
				   break;
			   case DOWNLOAD_SONG:
				   DownLoadThread t = new DownLoadThread(handler,DOWNLOAD+newMusics.get(msg.arg1).getMusic_id(),
						   MusicApplication.MUSIC_PATH+newMusics.get(msg.arg1).getMusic_name()+".mp3");
				   t.start();
				   break;
			   case DOWNLOAD_START:
				   play(position);
				   break;
			   case DOWNLOAD_FINISHED:
			       break;
			}
		}
	}
	
	private void play(int position){
		 MusicService.downloadedFiles.add(newMusics.get(position));
		 MusicService.mediaFiles = MusicService.downloadedFiles;
		 Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
		 to_service_intent.putExtra("position", MusicService.downloadedFiles.size()-1);
		 sendBroadcast(to_service_intent);
		 Intent intent = new Intent(OnlineMusicActivity.this,MusicActivity.class);
		 intent.putExtra("internet", true);
		 intent.putExtra("position", MusicService.downloadedFiles.size()-1);
		 intent.putExtra("path", newMusics.get(position).getPath());
		 startActivity(intent); 
	}
	
	private void loadResponseCache(){
		SharedPreferences p = this.getSharedPreferences("jsonCache", 0);
		String cache = p.getString("online_json", "");
		if(!cache.equals("")){
		  json = cache.split("-");
		}else{
		  json = getIntent().getStringExtra("json").split("-");
		}
	}
	
}
