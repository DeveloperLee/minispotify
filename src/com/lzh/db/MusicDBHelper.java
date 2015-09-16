package com.lzh.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDBHelper extends SQLiteOpenHelper{
	
	private static final String CREATE_MUSIC_TABLE = "create table if not exists download_musics("
                                        +"id integer primary key not null,"
                                        +"music_name char not null,"
                                        +"artist_name char not null,"
                                        +"album_name char not null,"
                                        +"path char not null,"
                                        +"duration char not null,"
                                        +"img char not null,"
                                        +"isHQ integer not null"
                                        +")";
	private static final String CREATE_LIST_TABLE = "create table if not exists play_lists("
                                        +"id integer primary key AUTOINCREMENT,"
                                        +"list_name char not null,"
                                        +"img char not null,"
                                        +"uid integer not null"
                                        +")";
	private static final String CREATE_LISTSONG_TABLE = "create table if not exists list_song("
			+"id integer primary key AUTOINCREMENT,"
            +"sid integer,"
            +"lid integer" 
            +")";
	private static final String CREATE_USER_TABLE = "create table if not exists users("
			+"id integer primary key AUTOINCREMENT,"
			+"uid integer not null,"
			+"uname char not null,"
			+"nickname char not null,"
			+"password char not null,"
			+"head_cache char not null,"
			+"ugroup integer not null,"
			+"follow integer,"
			+"friend integer"
			+")";
	

	public MusicDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL(CREATE_MUSIC_TABLE);
		arg0.execSQL(CREATE_LIST_TABLE);
		arg0.execSQL(CREATE_LISTSONG_TABLE);
		arg0.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
		arg0.execSQL("DROP TABLE IF EXISTS download_musics");   
        arg0.execSQL("DROP TABLE IF EXISTS play_lists");  
        arg0.execSQL("DROP TABLE IF EXISTS list_song");
        arg0.execSQL("DROP TABLE IF EXISTS users");
        onCreate(arg0);
	}

}
