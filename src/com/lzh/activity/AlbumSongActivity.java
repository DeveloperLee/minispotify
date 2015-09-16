package com.lzh.activity;

import java.util.List;

import com.lzh.adapter.MusicListAdapter;
import com.lzh.app.MusicApplication;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.service.MusicService;
import com.lzh.util.MusicUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumSongActivity extends Activity implements OnItemClickListener{
   
	private static String album_name;
	private TextView album;
	private ListView albumList;
	private MusicListAdapter adapter;
	private ImageButton back_btn;
	
	private static List<MediaFile> mediaFiles;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MusicApplication.getInstance().addActivity(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album_song);
		
		album_name = getIntent().getStringExtra("album_name");
		mediaFiles  = MusicUtils.getAlbumSong(album_name, getContentResolver());
		
		setUpViews();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
    
	private void setUpViews(){
		album = (TextView) findViewById(R.id.album);
		albumList = (ListView) findViewById(R.id.album_song_list);
		adapter = new MusicListAdapter(AlbumSongActivity.this, mediaFiles);
		albumList.setOnItemClickListener(this);
		albumList.setAdapter(adapter);
		album.setText(album_name);
		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AlbumSongActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    MusicService.mediaFiles = mediaFiles;
		    int id =(int)mediaFiles.get(arg2).getMusic_id();
		    Intent intent = new Intent(StateConstant.send_POSITION_FROM_ARTIST);
		    intent.putExtra("song_id", id);
		    sendBroadcast(intent);
		    Intent intent1 = new Intent(AlbumSongActivity.this,MusicActivity.class);
		    Bundle bundle = new Bundle();
		    bundle.putInt("music_id", id);
		    bundle.putBoolean("fromlist", false);
		    intent1.putExtras(bundle);
		    startActivity(intent1);
		    finish();		
	}
}
