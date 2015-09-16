package com.lzh.activity;

import java.util.List;

import com.lzh.adapter.MusicListAdapter;
import com.lzh.constants.StateConstant;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.service.MusicService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ManageActivity extends Activity {
	
	private List<MediaFile> musics;
	private ListView manage_list;
	private MusicListAdapter adapter;
	private TextView total_songs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		musics = MusicService.downloadedFiles;
		adapter = new MusicListAdapter(getApplicationContext(),musics);
		manage_list = (ListView) findViewById(R.id.download_musics);
		manage_list.setAdapter(adapter);
		manage_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				  MusicService.mediaFiles = musics;
				  Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
				  to_service_intent.putExtra("position", arg2);
				  ManageActivity.this.sendBroadcast(to_service_intent);
				  Intent intent = new Intent(ManageActivity.this,MusicActivity.class);
				  Bundle b = new Bundle();
				  b.putString("path", musics.get(arg2).getPath());
				  b.putInt("position", arg2);   
				  b.putBoolean("fromlist", true);
				  intent.putExtras(b);
				  startActivity(intent); 
			}
			
		});
		total_songs = (TextView) findViewById(R.id.manage_total_songs);
		total_songs.setText("已下载歌曲"+MusicService.downloadedFiles.size()+"首");
	}
	@Override
	protected void onResume() {
		super.onResume();
		musics = MusicService.downloadedFiles;
		adapter.notifyDataSetChanged();
		total_songs.setText("已下载歌曲"+MusicService.downloadedFiles.size()+"首");
	}
	
}
