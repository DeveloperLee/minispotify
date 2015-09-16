package com.lzh.adapter;

import java.util.List;

import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayMusicListAdapter extends BaseAdapter {
	
	private static LayoutInflater inflater;
	private static Context mContext;
	private static List<MediaFile>music_data;
	private static int selected_position = -1;
	
	public PlayMusicListAdapter(Context context,List<MediaFile> data){
		 mContext = context;
		 music_data = data;
	}
	
	@Override
	public int getCount() {
		return music_data.size();
	}
	@Override
	public Object getItem(int position) {
		
		return music_data.get(position);
	}
	@Override
	public long getItemId(int position) {
		
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		inflater = LayoutInflater.from(mContext);
		ViewHolder holder  = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.play_list_item, null);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.play_list_item_layout);
			holder.music_name = (TextView) convertView.findViewById(R.id.play_music_name);
			holder.artist_name = (TextView) convertView.findViewById(R.id.play_artist_name);
			holder.current_song = (ImageView) convertView.findViewById(R.id.play_current_play);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		   holder.music_name.setText(music_data.get(position).getMusic_name());
		   holder.artist_name.setText(music_data.get(position).getArtist_name());
		   
		   if(position == selected_position){
			  holder.layout.setBackgroundResource(R.drawable.seekbar_bg);
			  holder.current_song.setBackgroundResource(R.drawable.current_song);
		   }else{
			   holder.layout.setBackgroundResource(0);
			   holder.current_song.setBackgroundResource(0);
		   }
		
		return convertView;
	}
	public  void setItemPosition(int position){
		selected_position = position;
	}
	
	private final class ViewHolder{
		RelativeLayout layout;
		TextView music_name;
		TextView artist_name;
		ImageView current_song;
	}

}
