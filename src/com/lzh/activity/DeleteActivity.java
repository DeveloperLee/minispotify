package com.lzh.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.lzh.adapter.DownloadListAdapter;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.service.MusicService;
import com.lzh.util.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

public class DeleteActivity extends Activity {
	
	public static List<MediaFile> musics;
	private ListView delete_list;
	private DownloadListAdapter adapter;
	private CheckBox selectAll;
	private List<String> selected;
	private List<Integer> indexs;
	private TextView total_songs;
	private ImageButton confirm_delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		musics = MusicService.downloadedFiles;
		adapter = new DownloadListAdapter(getApplicationContext(),musics);
		adapter.notifyDataSetChanged();
		selected = new ArrayList<String>();
		indexs = new ArrayList<Integer>();
		initView();
	}

	private void initView() {
		selectAll = (CheckBox) findViewById(R.id.selectAll);
		delete_list = (ListView) findViewById(R.id.delete_music);
		delete_list.setAdapter(adapter);
		confirm_delete = (ImageButton) findViewById(R.id.delete_song);
		confirm_delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				selected.clear();
				indexs.clear();
				List<MediaFile> delete_files = new ArrayList<MediaFile>(); //待删除list
				Set<Integer> keys = adapter.checkedMap.keySet();
				Iterator<Integer> iter = keys.iterator();
				while(iter.hasNext()){
					int position = iter.next();
					if(adapter.checkedMap.get(position)){
						selected.add(musics.get(position).getPath());
						delete_files.add(musics.get(position));
					}
				}
				MusicService.allFiles.removeAll(delete_files);
				MusicService.downloadedFiles.removeAll(delete_files);
				FileUtils.deleteFile(selected);
				adapter.reset();
				adapter.notifyDataSetChanged();
				adapter.reset();
				total_songs.setText("已下载歌曲"+MusicService.downloadedFiles.size()+"首");
			}
		});
	
		total_songs = (TextView) findViewById(R.id.manage_total_songs);
		total_songs.setText("已下载歌曲"+musics.size()+"首");
		selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				Set<Integer> keys = adapter.checkedMap.keySet();
				Iterator<Integer> iter = keys.iterator();
				if(isChecked){
					while(iter.hasNext()){
						adapter.checkedMap.put(iter.next(), true);
					}
				}else{
					while(iter.hasNext()){
						adapter.checkedMap.put(iter.next(), false);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

}
