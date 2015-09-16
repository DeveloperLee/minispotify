package com.lzh.activity;

import java.util.ArrayList;
import java.util.List;

import com.lzh.adapter.MusicListAdapter;
import com.lzh.app.MusicApplication;
import com.lzh.constants.StateConstant;
import com.lzh.db.DBHelper;
import com.lzh.lzhmusic.R;
import com.lzh.model.MediaFile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteActivity extends Activity implements OnItemClickListener{
    
	 private TextView total;
	 private TextView info;
	 private ListView favs;
	 private MusicListAdapter adapter;
	 private List<MediaFile> favourites;
	 private DBHelper helper;
	 private Cursor cursor;
	 private ImageButton back_btn;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourite_layout);
		MusicApplication.getInstance().addActivity(this);
		loadFavourites();
		setUpViews();
	}
    
	private static final String FAV_DB_NAME = "favourite.db3";
	private void loadFavourites() {
		favourites = new ArrayList<MediaFile>();
		helper = new DBHelper(getApplicationContext(), FAV_DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		cursor = db.query("favourite", null, null, null, null, null, null);
		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++){
			final MediaFile file = new MediaFile();
			file.setMusic_id(cursor.getInt(cursor.getColumnIndex("id")));
			file.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
			file.setArtist_name(cursor.getString(cursor.getColumnIndex("artist_name")));
			file.setPath(cursor.getString(cursor.getColumnIndex("path")));
			file.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
			favourites.add(file);
			cursor.moveToNext();
		}
		db.close();
	}

	private void setUpViews() {
        total = (TextView) findViewById(R.id.fav_total);
        total.setText("¹²"+cursor.getCount()+"Ê×ÊÕ²Ø");
        info = (TextView) findViewById(R.id.fav_info);
        if(cursor.getCount()!=0){
        	info.setVisibility(View.GONE);
        }
        favs = (ListView) findViewById(R.id.fav_song_list);
        adapter = new MusicListAdapter(FavoriteActivity.this, favourites);
        favs.setAdapter(adapter);
        favs.setOnItemClickListener(this);
        back_btn = (ImageButton) findViewById(R.id.back);
        back_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FavoriteActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		helper.close(); 
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int id =(int)favourites.get(arg2).getMusic_id();
	    Intent intent = new Intent(StateConstant.send_POSITION_FROM_ARTIST);
	    intent.putExtra("song_id", id);
	    sendBroadcast(intent);
	    Intent intent1 = new Intent(FavoriteActivity.this,MusicActivity.class);
	    Bundle bundle = new Bundle();
	    bundle.putInt("music_id", id);
	    bundle.putBoolean("fromlist", false);
	    intent1.putExtras(bundle);
	    startActivity(intent1);
	    finish();        		
	}
}
