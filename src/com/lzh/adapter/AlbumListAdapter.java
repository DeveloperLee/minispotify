package com.lzh.adapter;

import java.util.List;

import com.lzh.lzhmusic.R;
import com.lzh.model.Album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumListAdapter extends BaseAdapter {
      
	private List<Album> albums;
	private LayoutInflater inflater;
	
	public AlbumListAdapter(Context context, List<Album> list){
		albums = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return albums.size();
	}

	@Override
	public Object getItem(int position) {
		return albums.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.album_item, null);
			holder.album_thumb = (ImageView) convertView.findViewById(R.id.album_pic1);
			holder.album_name = (TextView) convertView.findViewById(R.id.album_name);
			holder.album_total_song = (TextView) convertView.findViewById(R.id.total_song);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		  holder.album_name.setText(albums.get(position).getName());
		  holder.album_total_song.setText("Total");
		return convertView;
	}
	
	private final class ViewHolder{
		 @SuppressWarnings("unused")
		 ImageView album_thumb;
		 TextView album_name;
		 TextView album_total_song;
	}

}
