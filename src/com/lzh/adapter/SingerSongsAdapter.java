package com.lzh.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.net.BitmapDownloadTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SingerSongsAdapter extends BaseAdapter {
	
	private List<MediaFile> musics;
	private LayoutInflater inflater;
	public Map<Integer,Integer> mState;
	
	public static final Integer NORMAL = 0;
	public static final Integer DOWNLOADING = 1;
	public static final Integer DOWNLOADED = 2;
	private static final String IMG = "http://172.16.185.157:8080/MusicPlayer/showMusicPic.action?id=";
	
	public SingerSongsAdapter(Context context,List<MediaFile> musics){
		this.musics = musics;
		inflater = LayoutInflater.from(context);
		initMap();
	}

	private void initMap() {
		mState = new HashMap<Integer,Integer>();
		for(int i=0;i<musics.size();i++){
			mState.put(i, NORMAL);
		}
	}

	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Object getItem(int arg0) {
		return musics.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView,ViewGroup parent) {
		ViewHolder holder  = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_singer_song, null);
			holder.music_pic = (ImageView) convertView.findViewById(R.id.album_pic);
			holder.music_name = (TextView) convertView.findViewById(R.id.music_name);
			holder.artist_name = (TextView) convertView.findViewById(R.id.artist_name);
			holder.download = (ImageView) convertView.findViewById(R.id.download);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		} 
		   BitmapDownloadTask task = new BitmapDownloadTask(holder.music_pic);
		   task.execute(IMG+musics.get(position).getMusic_id(),musics.get(position).getMusic_name()); 
		   holder.music_pic.setImageResource(R.drawable.default_music_bg);
		   holder.music_name.setText(musics.get(position).getMusic_name());
		   holder.artist_name.setText(musics.get(position).getArtist_name());
		   holder.download.setImageResource(R.drawable.go_right);
		return convertView;
	}
	
	 public final class ViewHolder{
		ImageView music_pic;
		TextView music_name;
		TextView artist_name;
		ImageView download;
	}

}
