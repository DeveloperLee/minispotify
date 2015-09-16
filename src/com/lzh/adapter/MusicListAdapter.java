package com.lzh.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.lzh.app.MusicApplication;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.util.MusicUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter {
   
	private static LayoutInflater inflater;
	private static Context mContext;
	private  List<MediaFile>music_data;
	
	public MusicListAdapter(Context context,List<MediaFile> data){
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
			convertView = inflater.inflate(R.layout.list_item, null);
			holder.music_pic = (ImageView) convertView.findViewById(R.id.album_pic);
			holder.music_name = (TextView) convertView.findViewById(R.id.music_name);
			holder.artist_name = (TextView) convertView.findViewById(R.id.artist_name);
			holder.duration = (TextView) convertView.findViewById(R.id.duration);
			holder.isDownload = (ImageView) convertView.findViewById(R.id.isDownload);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		   File file = new File(MusicApplication.IMG_CACHE_FOLDER+music_data.get(position).getMusic_name()+".png");
		   if(!file.exists()){
			   holder.music_pic.setImageResource(R.drawable.default_music_bg);
		   }else{
			   try{
			    BitmapFactory.Options opt = new BitmapFactory.Options();  
			    opt.inPreferredConfig =Bitmap.Config.RGB_565;
			    opt.inPurgeable = true;  
			    opt.inInputShareable = true;  
				holder.music_pic.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(file),null,opt));
			   }catch (FileNotFoundException e){
				e.printStackTrace();
			   }
		   }
		   holder.music_name.setText(music_data.get(position).getMusic_name());
		   holder.artist_name.setText(music_data.get(position).getArtist_name());
		   holder.duration.setText(MusicUtils.convertToTime(music_data.get(position).getDuration()));
		   if(music_data.get(position).isDownload()){
			   holder.isDownload.setImageResource(R.drawable.downloadmusic);
		   }else{
			   holder.isDownload.setImageResource(R.drawable.localmusic);
		   }
		
		return convertView;
	}
	
	private final class ViewHolder{
		ImageView music_pic;
		TextView music_name;
		TextView artist_name;
		TextView duration;
		ImageView isDownload;
	}

	public void setResource(List<MediaFile> mediaFiles) {
		this.music_data = mediaFiles;
	}

}
