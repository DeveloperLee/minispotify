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
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AllSongFragment extends ListFragment{
    
	IMusicItem mSelect;
	private MusicListAdapter adpter;
	private static List<MediaFile> mediaFiles;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.all_song_fragment, container,false);
		return view;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mediaFiles = MusicService.allFiles;
		adpter = new MusicListAdapter(getActivity(), mediaFiles);
		setListAdapter(adpter);
		adpter.notifyDataSetChanged();
	}
   
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		  MusicService.mediaFiles = mediaFiles;
		  Intent to_service_intent = new Intent(StateConstant.SEND_LIST_POSITION);
		  to_service_intent.putExtra("position", position);
		  getActivity().sendBroadcast(to_service_intent);
		  Intent intent = new Intent(getActivity(),MusicActivity.class);
		  Bundle b = new Bundle();
		  b.putString("path", mediaFiles.get(position).getPath());
		  b.putInt("position", position);   
		  b.putBoolean("fromlist", true);
		  intent.putExtras(b);
		  startActivity(intent); 
	}

	@Override
	public void onResume() {
		super.onResume();
		mediaFiles = MusicService.allFiles;
		adpter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		super.onStart();
	}
  
	//宿主Activity必须实现的接口
	public interface IMusicItem{
		public void musicItemSelected(int position);
	}
	
}
