package com.lzh.adapter;

import java.util.List;

import com.lzh.lzhmusic.R;
import com.lzh.model.Artist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArtistListAdapter extends BaseAdapter {
     
	private LayoutInflater inflater;
	private List<Artist> artists;
	
	public ArtistListAdapter(Context context,List<Artist> artists){
		inflater = LayoutInflater.from(context);
		this.artists = artists;
	}
	
	@Override
	public int getCount() {
		return artists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return artists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		 ViewHolder holder = null;
		if(view == null){
			view = inflater.inflate(R.layout.artist_item, null);
			holder = new ViewHolder();
			holder.artist_thumb = (ImageView) view.findViewById(R.id.artist_pic);
			holder.artist_name = (TextView) view.findViewById(R.id.artist_name);
			holder.total_song_count = (TextView) view.findViewById(R.id.total_music_count);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
		}
           holder.artist_name.setText(artists.get(position).getArtist_name());
           holder.total_song_count.setText("total");
		return view;
	}
	
	private final class ViewHolder{
		@SuppressWarnings("unused")
		ImageView artist_thumb;
		TextView artist_name;
		TextView total_song_count;
	}

}
