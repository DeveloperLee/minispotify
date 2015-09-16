package com.lzh.activity;

import java.util.List;

import com.lzh.adapter.ArtistListAdapter;
import com.lzh.exception.NoResourceException;
import com.lzh.lzhmusic.R;
import com.lzh.model.Artist;
import com.lzh.util.MusicUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class AllArtistFragment extends ListFragment{
   
	private static List<Artist> artists;
	private ArtistListAdapter adapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment2, container,false);
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			artists = MusicUtils.getAllArtists(getActivity().getContentResolver());
			adapter = new ArtistListAdapter(getActivity(), artists);
			setListAdapter(adapter);
		} catch (NoResourceException e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		    
		   Intent intent = new Intent(getActivity(),ArtistSongActivity.class);
		   intent.putExtra("artist_name", artists.get(position).getArtist_name());
		   startActivity(intent);
	}

	
}
