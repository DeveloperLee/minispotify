package com.lzh.activity;

import com.lzh.animation.AlbumRotationAnim;
import com.lzh.lzhmusic.R;
import com.lzh.net.JSONUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class NetworkLoadingActivity extends Activity {
	
	private String[] loading_texts = {"好音乐马上就来","好音乐马上就来.","好音乐马上就来..","好音乐马上就来...","好音乐马上就来...."};
	private LoadingHandler handler;
	private TextView tw;
	private ImageView img;
	
	private static final int LOAD_FINISHED = 0;
	private static final int LOAD_REFREASH = 1;
	private static int text_index = 0;
	
	private static final String JSON = "http://172.16.185.157:8080/MusicPlayer/showRankMusic.action";
	private static final String SINGER_JSON = "http://172.16.185.157:8080/MusicPlayer/getPopularArtist.action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.network_loading_layout);
		handler = new LoadingHandler(Looper.myLooper());
		initPage();
		InitalThread initThread = new InitalThread();
		initThread.start();
	}
	
	private void initPage() {
		tw = (TextView) findViewById(R.id.loading_text);
		img = (ImageView) findViewById(R.id.loading_loading_pic);
		img.startAnimation(AlbumRotationAnim.getRotateAnim(1000L));
	} 

	private long refreshNow(){
		text_index = ++text_index %5;
		tw.setText(loading_texts[text_index]);
		return 500L;
	}
	
	private void nextRefresh(long interval){
		Message msg = handler.obtainMessage(LOAD_REFREASH);
		handler.removeMessages(LOAD_REFREASH);
		handler.sendMessageDelayed(msg, interval);
	}
	
	
	class InitalThread extends Thread{
		
		@Override
		public void run(){
				Message msg = handler.obtainMessage(LOAD_REFREASH);
			    handler.removeMessages(LOAD_REFREASH);
				handler.sendMessage(msg);
				String json_bottom = JSONUtils.getJson(JSON);
				String json_singer = JSONUtils.getJson(SINGER_JSON);
				String json = json_bottom+"-"+json_singer;
				cacheJSON(json);
				Message msg1 = handler.obtainMessage(LOAD_FINISHED,json);
				handler.removeMessages(LOAD_FINISHED);
				handler.sendMessage(msg1);
		}
	}
	
	class UIThread extends Thread{
		@Override
		public void run(){
			Message msg = handler.obtainMessage(LOAD_REFREASH);
			handler.removeMessages(LOAD_REFREASH);
			handler.sendMessage(msg);
		}
	}
	
	private class LoadingHandler extends Handler{
		public LoadingHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			  case LOAD_FINISHED: 
				 Intent intent = new Intent(NetworkLoadingActivity.this,OnlineMusicActivity.class);
				 intent.putExtra("json", (String)msg.obj);
				 NetworkLoadingActivity.this.startActivity(intent);
				 finish();
				 break;
			  case LOAD_REFREASH:
				 long interval = refreshNow();
				 nextRefresh(interval);
				 break;
			}
		}
	}
	
	private void cacheJSON(String json){
		SharedPreferences p = this.getSharedPreferences("jsonCache", 0);
		Editor e = p.edit();
		e.putString("online_json", json);
		e.commit();
	}
}
