package com.lzh.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.lzh.adapter.AddSongAdapter;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;
import com.lzh.service.MusicService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AddSongActivity extends Activity {
	
	private ImageButton back;
	private ImageButton confirm;
	private ListView add_list;
	private TextView total_songs;
	private CheckBox selectAll;
	private int id;
	private List<MediaFile> songs;
	private AddSongAdapter adapter;
	private MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addsong_layout);
		initData();
		initViews();
	}

	private void initData() {
		id = getIntent().getIntExtra("list_id", -1);
		songs = MusicService.allFiles;
		handler = new MyHandler(Looper.myLooper());
		adapter = new AddSongAdapter(this,songs,handler);
	}

	private void initViews() {
		add_list = (ListView) findViewById(R.id.add_list);
		add_list.setAdapter(adapter);
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AddSongActivity.this,PlaylistInfoActivity.class);
				intent.putExtra("list_id", id);
				startActivity(intent);
			}
		});
		confirm = (ImageButton) findViewById(R.id.confirm);
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ArrayList<Integer> positions = new ArrayList<Integer>();
				Set<Integer> keys = adapter.checkedMap.keySet();
				Iterator<Integer> iter = keys.iterator();
				while(iter.hasNext()){
					int position = iter.next();
					if(adapter.checkedMap.get(position)){
						positions.add(position);
					}
				}
				Intent intent = new Intent(AddSongActivity.this,PlaylistInfoActivity.class);
				intent.putExtra("list_id", id);
				intent.putIntegerArrayListExtra("positions", positions);
				startActivity(intent);
			}
		});
		total_songs = (TextView) findViewById(R.id.total_selected);
		selectAll = (CheckBox) findViewById(R.id.selectAll);
		selectAll.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				Set<Integer> keys = adapter.checkedMap.keySet();
				Iterator<Integer> iter = keys.iterator();
				if(isChecked){
					while(iter.hasNext()){
						adapter.checkedMap.put(iter.next(), true);
			    }
				}else{
					while(iter.hasNext()){
						adapter.checkedMap.put(iter.next(), false);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	class MyHandler extends Handler{
		
		public MyHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			   case 0:
				   confirm.setVisibility(View.GONE);
				   total_songs.setText("");
				   break;
			   case 1:
				   confirm.setVisibility(View.VISIBLE);
				   total_songs.setText("Ñ¡ÔñÁË"+msg.arg1+"Ê×¸è");
				   break;
			}
		}
	}

}
