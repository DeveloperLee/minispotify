package com.lzh.adapter;


import com.lzh.lzhmusic.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsAdapter extends BaseAdapter{
    
	
	private String[] content;
	private int[] resources;
	private LayoutInflater inflater;
	
	public SettingsAdapter(Context context,String[] content,int[] resources){
		this.content = content;
		this.resources = resources;
		inflater = LayoutInflater.from(context);
	}
	
	@Override 
	public int getCount() {
		return content.length;
	}

	@Override
	public Object getItem(int arg0) {
		return content[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if(arg1 == null){
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.item_settings, null);
			holder.pic = (ImageView) arg1.findViewById(R.id.settings_item_pic);
			holder.content = (TextView) arg1.findViewById(R.id.settings_item_text);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		holder.pic.setImageResource(resources[arg0]); 
		holder.content.setText(content[arg0]);
		return arg1;
	}
	
	private final class ViewHolder{
		ImageView pic;
		TextView content;
	}

}
