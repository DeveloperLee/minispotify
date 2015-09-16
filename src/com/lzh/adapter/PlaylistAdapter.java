package com.lzh.adapter;

import java.util.List;

import com.lzh.lzhmusic.R;
import com.lzh.model.Playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaylistAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<Playlist> lists;
	
	public PlaylistAdapter(Context context,List<Playlist> lists){
		this.inflater = LayoutInflater.from(context);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if(arg1 == null){
		    holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.playlist_item, null);
			holder.pic = (ImageView) arg1.findViewById(R.id.playlist_pic);
			holder.name = (TextView) arg1.findViewById(R.id.playlist_name);
			holder.description = (TextView) arg1.findViewById(R.id.description);
			holder.isAll = (ImageView) arg1.findViewById(R.id.isDownloadAll);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		if(arg0 == 0){
			holder.pic.setImageResource(R.drawable.fav_playlist);
			
		}else if(arg0 == 1){
			holder.pic.setImageResource(R.drawable.recent_played);
		}else{
			holder.pic.setImageResource(R.drawable.default_playlist_bg);
		}
		holder.name.setText(lists.get(arg0).getName());
		holder.description.setText(lists.get(arg0).getDescription());
		if(lists.get(arg0).isDowanloadAll()){
			holder.isAll.setImageResource(R.drawable.all_download);
		}else{
			holder.isAll.setImageResource(R.drawable.half_download);
		}
		return arg1;
	}
	
	final class ViewHolder{
		ImageView pic;
		TextView name;
		TextView description;
		ImageView isAll;
	}

}
