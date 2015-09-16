package com.lzh.adapter;

import java.io.UnsupportedEncodingException;

import com.lzh.lzhmusic.R;
import com.lzh.util.MusicUtils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends SimpleCursorAdapter {
   
	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view =  super.newView(context, cursor, parent);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) view.findViewById(R.id.music_name);
		holder.artist  = (TextView) view.findViewById(R.id.artist_name);
		holder.duration = (TextView) view.findViewById(R.id.duration);
		holder.view = (ImageView) view.findViewById(R.id.album_pic);
		view.setTag(holder);
		return view;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		String artistname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
		String songname = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
		try {
			artistname = new String(artistname.getBytes("ISO-8859-1"),"GBK");
			songname = new String(songname.getBytes("ISO-8859-1"),"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(artistname.equals("<unknown>")){
			holder.artist.setText("未知艺术家");
		}else{
		holder.artist.setText(artistname);
		}
		holder.name.setText(songname);
		holder.duration.setText(MusicUtils.convertToTime((int)cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
		super.bindView(view, context, cursor);
	}

	static final class ViewHolder{
	  TextView name;
	  TextView artist;
	  TextView duration;
	  ImageView view;
	}
}
