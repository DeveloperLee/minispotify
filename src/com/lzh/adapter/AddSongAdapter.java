package com.lzh.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lzh.app.MusicApplication;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AddSongAdapter extends BaseAdapter {
	
	private static LayoutInflater inflater;
	private static Context mContext;
	private List<MediaFile> music_data;
	private Handler handler;
	private int count;
	public Map<Integer,Boolean> checkedMap; //给Activity提供已选item
	
	public AddSongAdapter(Context context,List<MediaFile> data,Handler handler){
		 mContext = context;
		 music_data = data;
		 checkedMap = new HashMap<Integer,Boolean>();
		 this.handler = handler;
		 reset();
	}
	

	public void reset() {
		count = 0;
		for(int i=0;i<music_data.size();i++){
			 checkedMap.put(new Integer(i), false);
		 }
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		inflater = LayoutInflater.from(mContext);
		ViewHolder holder  = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.delete_list_item, null);
			holder.cb = (CheckBox) convertView.findViewById(R.id.delete_check);
			holder.music_pic = (ImageView) convertView.findViewById(R.id.album_pic);
			holder.music_name = (TextView) convertView.findViewById(R.id.music_name);
			holder.artist_name = (TextView) convertView.findViewById(R.id.artist_name);
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
				holder.music_pic.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(file)));
			   }catch (FileNotFoundException e){
				e.printStackTrace();
			   }
		   }
		   holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				 if(arg1){
					 checkedMap.put(position, true);
					 count++;
				 }else{
					 checkedMap.put(position, false);
					 count--;
				 }
				 if(count == 0){
					 Message msg = handler.obtainMessage(0);
					 handler.removeMessages(0);
					 handler.sendMessage(msg);
				 }else{
					 Message msg = handler.obtainMessage(1);
					 handler.removeMessages(1);
					 msg.arg1 = count;
					 handler.sendMessage(msg);
				 }
			}
		});
		   holder.cb.setChecked(checkedMap.get(position));
		   holder.music_name.setText(music_data.get(position).getMusic_name());
		   holder.artist_name.setText(music_data.get(position).getArtist_name());
		   if(music_data.get(position).isDownload()){
			   holder.isDownload.setImageResource(R.drawable.downloadmusic);
		   }else{
			   holder.isDownload.setImageResource(R.drawable.localmusic);
		   }
		
		return convertView;
	}
	
	private final class ViewHolder{
		CheckBox cb;
		ImageView music_pic;
		TextView music_name;
		TextView artist_name;
		ImageView isDownload;
	}
}