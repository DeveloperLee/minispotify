package com.lzh.activity;

import java.util.List;

import com.lzh.adapter.AlbumListAdapter;
import com.lzh.exception.NoResourceException;
import com.lzh.lzhmusic.R;
import com.lzh.model.Album;
import com.lzh.util.MusicUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class AllAlbumFragment extends ListFragment{
	
	 private static List<Album> albums;
	 private AlbumListAdapter adapter;
	 
 	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			albums  =  MusicUtils.getAllAlbums(getActivity().getContentResolver());
			adapter = new AlbumListAdapter(getActivity(), albums);
			setListAdapter(adapter);
		} catch (NoResourceException e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment3, container,false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		    
		   Intent intent = new Intent(getActivity(),AlbumSongActivity.class);
		   intent.putExtra("album_name", albums.get(position).getName());
		   startActivity(intent);
	}

}
