package com.lzh.activity;



import java.io.File;
import java.util.List;

import com.lzh.adapter.PlayMusicListAdapter;
import com.lzh.animation.TipAnimation;
import com.lzh.app.MusicApplication;
import com.lzh.component.LrcView;
import com.lzh.constants.PlayStyleConstant;
import com.lzh.constants.StateConstant;
import com.lzh.db.DBHelper;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.net.DownLoadThread;
import com.lzh.service.MusicService;
import com.lzh.service.SensorManagerHelper;
import com.lzh.service.SensorManagerHelper.OnShakeListener;
import com.lzh.util.ImageGetter;
import com.lzh.util.ImgBlur;
import com.lzh.util.MusicIntent;
import com.lzh.util.MusicUtils;
import com.lzh.util.RandomGenerator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MusicActivity extends Activity {
	
	private static LinearLayout player_bar;
	private static RelativeLayout music_status;
   	private static RelativeLayout fl;
   	private static RelativeLayout main;
   	private static RelativeLayout seek_bar_area;
   	private static RelativeLayout control_area;
	
	private ImageButton play;
	private ImageButton next_song_btn;
	private ImageButton prevoius_song_btn;
	private ImageButton play_style;
	private ImageButton back_to_list;
	private ImageButton share;
	private ImageButton add_favourite;
	private ImageButton show_lyric;
	private ImageView seekBg;
	private ImageView controlBg;
	private ImageView tips;
	private ImageView buffering;
	
	private TextView music_name;
	private TextView artist_name;
	private TextView music_name_bar;
	private TextView total_duration;
	private TextView current_time;
	private SeekBar seekBar;
	private ListView music_list;
	private com.lzh.component.FlingGalleryView gallery;
	
	private SeekbarHandler seekbarHandler;
	private UIHandler handler;
	private static MusicIntent mIntent;
	private static int current_position = -1;
	
	private static boolean isFavourite = false;
	private boolean internet;
	
	private static int current_play_mode = PlayStyleConstant.NORMAL_SEQUENCE;
	private static String curruntPathString = "";
	private static final String FAV_DB_NAME = "favourite.db3";
	
	private static MusicBroadcastReceiver receiver;
	private static List<MediaFile> mediaFiles;
	private PlayMusicListAdapter adapter;
	
	public static LrcView lrcView;
	private static DBHelper helper;
	
	private SensorManagerHelper smh;
	
	private static final String DOWNLOAD = "http://172.16.185.157:8080/MusicPlayer/downloadMusic.action?id=";
	private static final int DOWNLOAD_SONG = 3;
	private static final int DOWNLOAD_FINISHED = 5;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicApplication.getInstance().addActivity(this);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.music_layout);
        
        fl = (RelativeLayout) findViewById(R.id.bg);
        main = (RelativeLayout) findViewById(R.id.main);
        seek_bar_area = (RelativeLayout) findViewById(R.id.seekbar_area);
        control_area = (RelativeLayout) findViewById(R.id.controller_panel);
        music_status = (RelativeLayout) findViewById(R.id.music_info_area);
        
        helper = new DBHelper(MusicActivity.this,FAV_DB_NAME , null, 1);
        setUpView();
        
        seekbarHandler = new SeekbarHandler(Looper.myLooper());
        handler = new UIHandler(Looper.myLooper());
        mIntent = new MusicIntent(getApplicationContext());
        
        receiver = new MusicBroadcastReceiver();
        isTipShown();
        smh = new SensorManagerHelper(this);
        smh.setOnShakeListener(new OnShakeListener(){
			@Override
			public void onShake() {
				nextSong();
			}
        });
    }

    private void isTipShown() {
    	 SharedPreferences p = getSharedPreferences("config",0);
    	 boolean showTip = p.getBoolean("showTip", true);
         if(showTip){
        	 TipAnimation.startAnimation(tips);
         }else{
        	 tips.setVisibility(View.GONE);
         }
         SharedPreferences.Editor editor = p.edit();
         editor.putBoolean("showTip", false);
         editor.commit();
	}
    
    @Override
    protected void onPause(){
    	super.onPause();
    	unregisterReceiver(receiver);
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_layout, menu);
        return true;
    }
    
	private void setUpView(){
    	
       mediaFiles = MusicService.mediaFiles;
	   player_bar = (LinearLayout) findViewById(R.id.play_bar);
       player_bar.setBackgroundDrawable(new BitmapDrawable(MusicUtils.decodePng(getApplicationContext(), R.drawable.bg)));
       
       gallery = (com.lzh.component.FlingGalleryView) findViewById(R.id.fgv_player_main);
       gallery.setDefaultScreen(1);
       
       lrcView = (LrcView) findViewById(R.id.lrcView);
       tips = (ImageView) findViewById(R.id.tip);
       buffering = (ImageView) findViewById(R.id.buffering_btn);
    	
    	play = (ImageButton) findViewById(R.id.play_btn);
    	play.setOnClickListener(getOnClickListener(play));
    	next_song_btn = (ImageButton) findViewById(R.id.next_song_btn);
    	next_song_btn.setOnClickListener(getOnClickListener(next_song_btn));
    	prevoius_song_btn = (ImageButton) findViewById(R.id.previous_song_btn);
    	prevoius_song_btn.setOnClickListener(getOnClickListener(prevoius_song_btn));
    	play_style = (ImageButton) findViewById(R.id.play_style);
    	play_style.setOnClickListener(getOnClickListener(play_style));
    	back_to_list = (ImageButton) findViewById(R.id.back_to_list_btn);
    	back_to_list.setOnClickListener(getOnClickListener(back_to_list));
    	share = (ImageButton) findViewById(R.id.share);
    	share.setOnClickListener(getOnClickListener(share));
    	add_favourite = (ImageButton) findViewById(R.id.add_favourite);
    	add_favourite.setOnClickListener(getOnClickListener(add_favourite));
    	seekBg = (ImageView) findViewById(R.id.seekBg);
    	controlBg = (ImageView) findViewById(R.id.controlBg);
    	
    	show_lyric = (ImageButton) findViewById(R.id.lyric);
    	show_lyric.setOnClickListener(getOnClickListener(show_lyric));
    	
    	
    	music_list = (ListView) findViewById(R.id.play_activity_musiclist);
    	adapter = new PlayMusicListAdapter(this, mediaFiles);
    	music_list.setAdapter(adapter);
    	music_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				       invalidateList(position);
				       current_position = position;
				       setUpNextSong(position);
			}
		});
    	
    	
    	music_name = (TextView) findViewById(R.id.music_name);
    	artist_name = (TextView) findViewById(R.id.artist_name);
    	music_name_bar = (TextView) findViewById(R.id.music_name_bar);
    	
    	total_duration  = (TextView) findViewById(R.id.total_time); 
    	current_time = (TextView)  findViewById(R.id.current_time);
    	
    	seekBar = (SeekBar) findViewById(R.id.seekbar);
    	seekBar.setMax(1000);
    	seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(fromUser){
					MusicService.setCurrentProgress(progress);
				}
			}
		});
    }
    
    private OnClickListener getOnClickListener(View view){
    	
    	switch (view.getId()) {
		case R.id.play_btn:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					play();
				}
			};
		
		case R.id.next_song_btn:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					nextSong();
				}
			};
			
		case R.id.previous_song_btn:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					  previousSong();
				}
			};
		case R.id.share:
			return new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//分享操作
				}
			};
		case R.id.play_style:
			return new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String mode_name =  "";
					switch (current_play_mode) {
					case PlayStyleConstant.NORMAL_SEQUENCE:               //顺序播放
						current_play_mode = PlayStyleConstant.SHUFFELL_ALL;
						play_style.setBackgroundResource(R.drawable.shuffle_song);
						mode_name = "随机播放";
						break;
					case PlayStyleConstant.SHUFFELL_ALL:                          //随机播放
						current_play_mode = PlayStyleConstant.SINGLE_SONG;
						play_style.setBackgroundResource(R.drawable.single_song);
						mode_name = "单曲循环";
						break;
					case PlayStyleConstant.SINGLE_SONG:                          //单曲循环
						current_play_mode = PlayStyleConstant.NORMAL_SEQUENCE;
						play_style.setBackgroundResource(R.drawable.play_all_song);
						mode_name = "全曲播放";
						break;
					default:
						break;
					}
					Intent intent = new Intent(StateConstant.SEND_MODE_FROM_ACTIVITY);
					intent.putExtra("play_mode", current_play_mode);                     //每次点击的时将position和播放模式发送给Service
					sendBroadcast(intent);
					Toast.makeText(MusicActivity.this, mode_name, Toast.LENGTH_LONG).show();
				}
			};
		case R.id.back_to_list_btn:
		       return new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MusicActivity.this,MainActivity.class);
				    startActivity(intent);
				}
			};
		case R.id.add_favourite:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(MusicService.current_user == null){
						  Intent intent = new Intent(MusicActivity.this,LoginActivity.class);
						  startActivity(intent);
						  overridePendingTransition(R.anim.fade_out,R.anim.fade_in); 
				    }else{
					      handlerFavourite();
				    }
				}
			};
		case R.id.lyric:
			return new OnClickListener() {
				@Override
				public void onClick(View v) {
					 gallery.setToScreen(2, true);   //点击歌词按钮则跳转到歌词界面
					 
				}
			};
		}
		return null;
    }
    
    private void initMusicPlay(){
    	     internet = getIntent().getBooleanExtra("internet", false);
    	     if(internet){
    	    	 current_position = getIntent().getIntExtra("position", mediaFiles.size()-1);
    	    	 curruntPathString = getIntent().getStringExtra("path");
    	    	 play.setVisibility(View.INVISIBLE);
    	    	 Message msg = handler.obtainMessage(DOWNLOAD_SONG);
 				 handler.removeMessages(DOWNLOAD_SONG);
 				 handler.sendMessageDelayed(msg,1000L);
    	     }else{
    		   if(getIntent().getExtras().getBoolean("fromlist") && mediaFiles != null){
    		     current_position = getIntent().getExtras().getInt("position");
    		     curruntPathString = mediaFiles.get(current_position).getPath();
           	     if(curruntPathString.equals(MusicService.getCurrentPath())){//通过路径来判断是否同一歌曲在播放，如果是则继续，不是则播放新歌
           		  if(MusicService.isPaused()){
                   setCurrSongPath(curruntPathString);
           		   play();
           		   }else{
                   //再次回到这个Activity 且音乐正在播放的时候要继续更新UI，从后台Service取得当前保留的进度并继续更新
           			Message msg  = seekbarHandler.obtainMessage(StateConstant.REFRESH);
       			    seekbarHandler.removeMessages(StateConstant.REFRESH);
       	    	    seekbarHandler.sendMessage(msg);
       	    	    play.setBackgroundResource(R.drawable.playing_pause_normal);
           		 }
           	    }else{
           		    //如果不是同一首歌，那么重置Service并填充新的路径到Service中
           		    changeSong(curruntPathString);
           	       }
           }else{
        	     String path = getIntent().getExtras().getString("path");
        	     if(!(path == null)){
        	       current_position = getIntent().getExtras().getInt("position");
        	       changeSong(path);
        	     }else{
        	    	playFromArtistOrAlbum();
        	     }
                    
           }
    	   }
    	   initPlayModeButton();
    	   music_name.setText(mediaFiles.get(current_position).getMusic_name());
    	   artist_name.setText(mediaFiles.get(current_position).getArtist_name());
    	   music_name_bar.setText(mediaFiles.get(current_position).getMusic_name());
    	   isAlbumPicAvailable();
    }

	private void isAlbumPicAvailable() {
		File file = new File(MusicApplication.IMG_CACHE_FOLDER+mediaFiles.get(current_position)
    			   .getMusic_name()+".png");
    	   if(file.exists()){
    		   Message msg = handler.obtainMessage(0,file);
    		   handler.removeMessages(0);
    		   handler.sendMessageDelayed(msg, 500L);
    	   }else{
    		   Message msg = handler.obtainMessage(1);
    		   handler.removeMessages(1);
    		   handler.sendMessage(msg);
    	   }
	}
    
    //点击播放按钮触发的事件
     private void play(){
    	 try {
			 if(MusicService.isPaused()){
			     mIntent.setAction(StateConstant.PLAY);
			     startService(mIntent);
			 }else{
				 mIntent.setAction(StateConstant.PAUSE);
				 startService(mIntent);
			 }
			 resetButton();
			 Message msg  = seekbarHandler.obtainMessage(StateConstant.REFRESH);
			 seekbarHandler.removeMessages(StateConstant.REFRESH);
	    	 seekbarHandler.sendMessage(msg);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
     }
    /**
     * 刷新Seekbar组件并且返回下次刷新时间,默认1秒
     * @return
     */
    private long refreshNow(){
   	    total_duration.setText(MusicUtils.convertToTime(mediaFiles.get(current_position).getDuration()));
    	int current =  MusicService.getCurrentPosition();
    	int seekTo = (int) (current*1.0 /  MusicService.getCurrSongDuration() * 1000); 
        seekBar.setProgress(seekTo);
        current_time.setText(MusicUtils.convertToTime(current));
    	return 1000L;
    }
    
    private void nextRefresh(long interval){
    	if(MusicService.isPlaying()){
    		Message msg = seekbarHandler.obtainMessage(StateConstant.REFRESH);
    		seekbarHandler.removeMessages(StateConstant.REFRESH);
    		seekbarHandler.sendMessageDelayed(msg, interval);
    	}
    }
    
    private void setCurrSongPath(String path){
    	   MusicService.setPath(path);
    }
    
    private void resetButton(){
    	   play.setVisibility(View.VISIBLE);
    	   buffering.setVisibility(View.GONE);
    	if(MusicService.isPaused()){
			play.setBackgroundResource(R.drawable.pause_btn);
			}else{
			play.setBackgroundResource(R.drawable.play_btn);
			}
    }
    
    private class SeekbarHandler extends Handler{
    	public SeekbarHandler(Looper looper){
    		  super(looper);
    	}
    	
    	@Override
    	public void handleMessage(Message msg){
    		 switch (msg.what) {
			case StateConstant.REFRESH:
				long interval = refreshNow();    //循环刷新（通过循环发送message 来实现）
				nextRefresh(interval);
				break;
			case StateConstant.STOP_REFRESH:
				seekBar.setProgress(0);
				break;
			case StateConstant.SHOW_SETTINGS_ANIM :
				break;
			default:
				break;
			}
    	}
    }
    
    //当更改当前要播放的曲目时调用此方法，如前一首，后一首，或者从播放列表播放不同歌曲时
    private void changeSong(String path){
    	setCurrSongPath(path);
    	MusicService.resetStatuse();
		play();
    }
    
    
    /**
     * 根据播放模式填充下一首歌曲
     */
    private void nextSong(){
    	if(current_play_mode == PlayStyleConstant.NORMAL_SEQUENCE){
    	    current_position++;
    	    current_position = current_position % mediaFiles.size();
    	}else if (current_play_mode == PlayStyleConstant.SHUFFELL_ALL){
    		current_position = RandomGenerator.generateNextShuffelPos(current_position, mediaFiles.size());
    	}
    	invalidateList(current_position);
    	initFavourite();
    	setUpNextSong(current_position);
    	isAlbumPicAvailable();
    }
    
    /**
     * 根据播放模式填充上一首歌曲
     */
    private void previousSong(){
    	if(current_play_mode == PlayStyleConstant.NORMAL_SEQUENCE){
    	current_position--;
    	if(current_position<0){
    		current_position = mediaFiles.size()-1;
    	}
    	}else if(current_play_mode == PlayStyleConstant.SHUFFELL_ALL){
    		current_position = RandomGenerator.generateNextShuffelPos(current_position, mediaFiles.size());
    	}
    	invalidateList(current_position);
    	music_name.setText(mediaFiles.get(current_position).getMusic_name());
    	artist_name.setText(mediaFiles.get(current_position).getArtist_name());
    	music_name_bar.setText(mediaFiles.get(current_position).getMusic_name());
    	total_duration.setText(MusicUtils.convertToTime(mediaFiles.get(current_position).getDuration()));
    	Intent intent = new Intent(StateConstant.SEND_POSTION_FROM_ACTIVITY);
    	intent.putExtra("position", current_position);
    	sendBroadcast(intent);
    	initFavourite();
		changeSong(mediaFiles.get(current_position).getPath());
		isAlbumPicAvailable();
    }
    
    private void setUpNextSong(int position){
    	music_name.setText(mediaFiles.get(position).getMusic_name());
    	artist_name.setText(mediaFiles.get(position).getArtist_name());
    	music_name_bar.setText(mediaFiles.get(current_position).getMusic_name());
    	total_duration.setText(MusicUtils.convertToTime(mediaFiles.get(position).getDuration()));
    	Intent intent = new Intent(StateConstant.SEND_POSTION_FROM_ACTIVITY);
    	intent.putExtra("position", position);               
    	sendBroadcast(intent);
    	changeSong(mediaFiles.get(position).getPath());
    	isAlbumPicAvailable();
    }
    
    //内部广播接收器类，用于接受来自Service的广播（如歌曲播放完成时发送的广播）
    private class MusicBroadcastReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			  if(intent.getAction().equals(StateConstant.AUTO_NEXT_SONG) || intent.getAction().equals(StateConstant.SEND_LIST_POSITION))
			  current_position = intent.getIntExtra("position", 0);
			  invalidateList(current_position);
			  setUpNextSong(current_position);        
			  Log.i("123", String.valueOf(current_position));
		}
    }
    
    //注册广播接收器
    private  void registerReceiver(){
    	IntentFilter filter = new IntentFilter(StateConstant.AUTO_NEXT_SONG); //从Service获取播放完成通知
   	    filter.addAction(StateConstant.SEND_LIST_POSITION);  //从播放列表获取position
    	registerReceiver(receiver, filter);
    }
    
    //重置播放模式按钮
    private void initPlayModeButton(){
    	current_play_mode = MusicService.getPlayMode();
    	switch (current_play_mode) {
		case PlayStyleConstant.NORMAL_SEQUENCE:               //顺序播放
			play_style.setBackgroundResource(R.drawable.play_all_song);
			break;
		case PlayStyleConstant.SHUFFELL_ALL:                          //随机播放
			play_style.setBackgroundResource(R.drawable.shuffle_song);
			break;
		case PlayStyleConstant.SINGLE_SONG:                          //单曲循环
			play_style.setBackgroundResource(R.drawable.single_song);
			break;
     }
    }
    
    /**
     * 处理收藏事件
     */
    private void handlerFavourite(){
    	if(!isFavourite){
    		 add_favourite.setBackgroundResource(R.drawable.is_favourite);
    		 isFavourite = true;
    		 if( addFavourite()){
    			 Toast.makeText(MusicActivity.this, "添加收藏成功", Toast.LENGTH_SHORT).show();
    		 }
    	}else{
    		add_favourite.setBackgroundResource(R.drawable.not_favourite);
    		isFavourite = false;
    		if(deleteFavourite()){
    			Toast.makeText(MusicActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
    		}
    	}
    }

	@Override
	protected void onResume() {
	   super.onResume();
	   mediaFiles = MusicService.mediaFiles;
       registerReceiver();
       initMusicPlay();
	   initFavourite();
       if(current_position !=-1){
    		 invalidateList(current_position);
    	}
	}
	
	
	private void playFromArtistOrAlbum(){
	     int id = getIntent().getExtras().getInt("music_id");
   	     for(int i = 0;i<mediaFiles.size();i++){
   	    	 int a = (int) mediaFiles.get(i).getMusic_id();
   	    	   if(id == a){
   	    		   current_position = i;
   	    		   break;
   	    	   }
   	     }
   	    curruntPathString = mediaFiles.get(current_position).getPath();
     	if(curruntPathString.equals(MusicService.getCurrentPath()) && mediaFiles != null){//通过路径来判断是否同一歌曲在播放，如果是则继续，不是则播放新歌
     		 if(MusicService.isPaused()){
                 setCurrSongPath(curruntPathString);
     		 play();
     		 }else{
             //再次回到这个Activity 且音乐正在播放的时候要继续更新UI，从后台Service取得当前保留的进度并继续更新
     			Message msg  = seekbarHandler.obtainMessage(StateConstant.REFRESH);
 			    seekbarHandler.removeMessages(StateConstant.REFRESH);
 	    	    seekbarHandler.sendMessage(msg);
 	    	    play.setBackgroundResource(R.drawable.playing_pause_normal);
     		 }
     	}else{
     		    //如果不是同一首歌，那么重置Service并填充新的路径到Service中
     		    changeSong(curruntPathString);
     	}
     	if(mediaFiles != null){
     	 music_name.setText(mediaFiles.get(current_position).getMusic_name());
  	     artist_name.setText(mediaFiles.get(current_position).getArtist_name());
  	     music_name_bar.setText(mediaFiles.get(current_position).getMusic_name());
  	     invalidateList(current_position);
     	}
	}
	
	/**
	 *  在自定义adapter中加入了设置点击哪一个位置的方法，为了重绘adapter
	 * @param position
	 */
	private void invalidateList(int position){
	       adapter.notifyDataSetInvalidated();
	       adapter.setItemPosition(position);    //在自定义adapter中加入了设置点击哪一个位置的方法，为了重绘adapter
	       music_list.setSelection(current_position); //为了使ListView实现自动滚动
	}
	
	private ContentValues values = new ContentValues();
	private boolean addFavourite(){
		values.clear();
		SQLiteDatabase db = helper.getWritableDatabase();
		values.put("id", (int)mediaFiles.get(current_position).getMusic_id());
		values.put("music_name", mediaFiles.get(current_position).getMusic_name());
		values.put("artist_name", mediaFiles.get(current_position).getArtist_name());
		values.put("path", mediaFiles.get(current_position).getPath());
		values.put("duration", mediaFiles.get(current_position).getDuration());
		if(db.insert("favourite", null, values) != -1){
			db.close();
			return true;
		}else{
			db.close();
			return false;
		}
		
	}
	private boolean deleteFavourite(){
		SQLiteDatabase db = helper.getWritableDatabase();
		if(db.delete("favourite", "id = ?", new String[]{String.valueOf(mediaFiles.get(current_position).getMusic_id())}) >=0 ){
			db.close();
			return true;
		}else{
			db.close();
			return false;
		}
	}
	/**
	 * 进入该Activity时判断这首歌是否已被收藏
	 * @return
	 */
	private void initFavourite(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query("favourite", new String[]{"id"}, "id = ?",
				new String[]{String.valueOf(mediaFiles.get(current_position).getMusic_id())}, null, null, null);
		if(c.getCount() == 0){
			add_favourite.setBackgroundResource(R.drawable.not_favourite);
			isFavourite = false;
		}else{
			add_favourite.setBackgroundResource(R.drawable.is_favourite);
			isFavourite = true;
		}
		c.close();
		db.close();
	}
	class UIHandler extends Handler{
		
		public UIHandler(Looper looper){
			super(looper);
		}

		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			  case 0:
				   File file = (File) msg.obj;
				   Drawable d = BitmapDrawable.createFromPath(file.getPath());
	    		   fl.setBackgroundDrawable(d);
	    		   BitmapDrawable music_pic = (BitmapDrawable) ImgBlur.BoxBlurFilter(((BitmapDrawable)d).getBitmap());
	    		   Bitmap img = music_pic.getBitmap();
	    		   Drawable d1 = ImageGetter.getSeekbarBg(img);
	    		   Drawable d2 = ImageGetter.getControllerBg(img);
	    		   seek_bar_area.setBackgroundDrawable(d1);
	    		   music_status.setBackgroundDrawable(d1);
	    		   control_area.setBackgroundDrawable(d2);
	    		   seekBg.setBackgroundResource(R.drawable.info_area);
	    		   controlBg.setBackgroundResource(R.drawable.info_area);
				  break;
			  case 1:
				   fl.setBackgroundResource(R.drawable.default_album_playing);
	    		   main.setBackgroundDrawable(null);
	    		   seek_bar_area.setBackgroundColor(Color.BLACK);
	    		   control_area.setBackgroundColor(Color.BLACK);
	    		   music_status.setBackgroundColor(Color.BLACK);
	    		   seekBg.setBackgroundDrawable(null);
	    		   controlBg.setBackgroundDrawable(null);
				  break;
			  case DOWNLOAD_SONG:
				  DownLoadThread t = new DownLoadThread(handler,DOWNLOAD+mediaFiles.get(current_position).getMusic_id(),
						   MusicApplication.MUSIC_PATH+mediaFiles.get(current_position).getMusic_name()+".mp3");
				   t.start();
				   buffering.startAnimation(AnimationUtils.loadAnimation(MusicActivity.this, R.anim.rotate_anim));
				   Toast.makeText(MusicActivity.this, "正在下载:"+mediaFiles.get(current_position).getMusic_name(), Toast.LENGTH_SHORT).show();
				  break;
			  case DOWNLOAD_FINISHED:
				  buffering.clearAnimation();
				  buffering.setVisibility(View.INVISIBLE);
				  Toast.makeText(MusicActivity.this, mediaFiles.get(current_position).getMusic_name()+"下载完成", Toast.LENGTH_SHORT).show();
	    	      MusicService.setPath(curruntPathString);
	    		  MusicService.resetStatuse();
	    		  MusicIntent mIntent = new MusicIntent(getApplicationContext());
	    		  mIntent.setAction(StateConstant.PLAY);
	    		  mIntent.putExtra("mode", 1);
	    		  startService(mIntent);
	    		  resetButton();
	 			  Message msg1  = seekbarHandler.obtainMessage(StateConstant.REFRESH);
	 			  seekbarHandler.removeMessages(StateConstant.REFRESH);
	 	    	  seekbarHandler.sendMessage(msg1);
				  break;
			}
		}
	}
    
}
