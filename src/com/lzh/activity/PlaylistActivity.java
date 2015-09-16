package com.lzh.activity;


import java.util.List;

import com.lzh.adapter.PlaylistAdapter;
import com.lzh.app.ApplicationManager;
import com.lzh.db.MusicDB;
import com.lzh.lzhmusic.R;
import com.lzh.model.Playlist;
import com.lzh.service.MusicService;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class PlaylistActivity extends Activity {
	
	private ImageButton back_btn;
	private ImageButton add_list;
	private ListView list;
	private PlaylistAdapter adapter;
	private MusicDB db;
	private List<Playlist> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationManager.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_playlist);
		initData();
		initView();
	}

	private void initData() {
		db = new MusicDB(getApplicationContext());
		lists = MusicService.current_user.getLists();
		adapter = new PlaylistAdapter(getApplicationContext(),lists);
	}

	private void initView() {
		back_btn = (ImageButton) findViewById(R.id.back);
		back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PlaylistActivity.this,MainActivity.class);
				startActivity(intent);
				ApplicationManager.getInstance().finishActivity();
			}
		});
		add_list = (ImageButton) findViewById(R.id.playlist_manage_btn);
		add_list.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog();
			}
		});
		list = (ListView) findViewById(R.id.playlist_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(PlaylistActivity.this,PlaylistInfoActivity.class);
				intent.putExtra("list_id", db.getPlaylistIdByName(lists.get(arg2).getName()));
				startActivity(intent);
			}
		});
	}
	
	private void showDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogView = inflater.inflate(R.layout.add_playlist_dialog,null);
		Button confirm = (Button) dialogView.findViewById(R.id.confirm_add);
		final EditText name = (EditText) dialogView.findViewById(R.id.edit_playlist_name);
		final Dialog dialog = new Dialog(PlaylistActivity.this,R.style.selectorDialog);
		dialog.setContentView(dialogView);
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			    String add_name = name.getText().toString();
			    db.addPlaylist(add_name,MusicService.current_user.getId());
			    Playlist list = new Playlist(add_name,"");
			    MusicService.current_user.addList(list);
			    adapter.notifyDataSetChanged();
			    dialog.dismiss();
			}
		});
		dialog.show();
	}
}

