package com.lzh.activity;

import java.util.ArrayList;
import java.util.List;

import com.lzh.adapter.MusicListAdapter;
import com.lzh.animation.MusicIconTransAnimation;
import com.lzh.constants.StateConstant;
import com.lzh.db.MusicDB;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.model.Playlist;
import com.lzh.service.MusicService;
import com.lzh.util.MusicIntent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlaylistInfoActivity extends Activity {
	
	private Playlist list;
	private String name;
	private List<MediaFile> songs;
	
	private TextView list_name;
	private ImageButton back;
	private ImageButton play;
	private ImageButton add_song_btn;
	private ListView list_song;
	private MusicListAdapter adapter;
	private MusicDB db;
	private int id;
	private int endLocation[];
	
	private ImageView music_icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.playlist_info);
		initData();
		initViews();
	}


	private void initData() {
		db = new MusicDB(getApplicationContext());
	    id = getIntent().getIntExtra("list_id", -1);
		list = db.getPlaylistById(id);
		name = list.getName();
		songs = list.getSongs();
		adapter = new MusicListAdapter(getApplicationContext(),songs);
		ArrayList<Integer> positions = getIntent().getIntegerArrayListExtra("positions");
		if(positions != null){
			List<MediaFile> files = new ArrayList<MediaFile>();
			for(int i = 0;i<positions.size();i++){
				MediaFile file = MusicService.allFiles.get(positions.get(i));
				files.add(file);
				songs.add(file);
			}
			adapter.notifyDataSetChanged();
			db.addSongsToList(id, files);
		}
		endLocation = new int[2];
	}
	
	private void initViews() {
		list_name = (TextView) findViewById(R.id.playlist_name);
		list_name.setText(name);
		music_icon = (ImageView) findViewById(R.id.music_icon);
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PlaylistInfoActivity.this,PlaylistActivity.class);
				startActivity(intent);
			}
		});
		add_song_btn = (ImageButton) findViewById(R.id.add_song_btn);
		add_song_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PlaylistInfoActivity.this,AddSongActivity.class);
				intent.putExtra("list_id", id);
				startActivity(intent);
				overridePendingTransition(R.anim.out_to_top,R.anim.in_from_bottom); 
			}
		});
		play = (ImageButton) findViewById(R.id.playlist_info_play);
		play.getLocationOnScreen(endLocation);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 play(0); 
			}
		});
		list_song = (ListView) findViewById(R.id.playlist_songs);
		list_song.setAdapter(adapter);
		list_song.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int[] view_location = new int[2];
				arg1.getLocationOnScreen(view_location);
				music_icon.startAnimation(MusicIconTransAnimation.getAnim(-endLocation[0], 0f, -view_location[1],0f));
				play(arg2);
			}
		});
	}
	
	private void play(int i) {
		 MusicService.mediaFiles = songs;
		 Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
		 to_service_intent.putExtra("position", i);
		 sendBroadcast(to_service_intent);
		 MusicService.setPath(songs.get(i).getPath());
		 MusicService.resetStatuse();
		 MusicIntent mIntent = new MusicIntent(getApplicationContext());
		 mIntent.setAction(StateConstant.PLAY);
		 mIntent.putExtra("mode", 1);
		 startService(mIntent);
	}

}
