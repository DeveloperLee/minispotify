package com.lzh.db;

import java.util.ArrayList;
import java.util.List;

import com.lzh.app.MusicApplication;
import com.lzh.model.MediaFile;
import com.lzh.model.OnlineMusic;
import com.lzh.model.Playlist;
import com.lzh.model.User;
import com.lzh.service.MusicService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MusicDB {
	
	private MusicDBHelper helper;
	private static final String DB_NAME = "download.db";
	private static final String TABLE_NAME = "download_musics";
	private static final String USER_TABLE_NAME ="users";
	private static final String LIST_TABLE_NAME = "play_lists";
	private static final String LIST_SONG_TABLE_NAME = "list_song";
	private static final int VERSION = 1;
	
	public MusicDB(Context context){
		helper = new MusicDBHelper(context,DB_NAME,null,VERSION);
	}
	
	public void insert(OnlineMusic music){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", music.getMusic_id()); 
		values.put("music_name", music.getMusic_name());
		values.put("artist_name", music.getArtist_name());
		values.put("album_name", music.getAlbum_name());
		values.put("path", music.getPath());
		values.put("img", music.getImg());
		values.put("duration", music.getDuration());
		values.put("isHQ", true);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	public List<MediaFile> getAllDownloadMusics(){
		SQLiteDatabase	db = helper.getReadableDatabase();
		List<MediaFile> musics = new ArrayList<MediaFile>();
		Cursor cursor = null;
		cursor = db.query(TABLE_NAME,null, null, null, null, null, null);
		for(cursor.moveToFirst();!cursor.isLast();cursor.moveToNext()){
			MediaFile f = new MediaFile();
			f.setMusic_id(cursor.getLong(cursor.getColumnIndex("id")));
			f.setMusic_name(cursor.getString(cursor.getColumnIndex("music_name")));
			f.setArtist_name(cursor.getString(cursor.getColumnIndexOrThrow("artist_name")));
			f.setAlbum(cursor.getString(cursor.getColumnIndex("album_name")));
			f.setPath(cursor.getString(cursor.getColumnIndex("path")));
			f.setImg(cursor.getString(cursor.getColumnIndex("img")));
			f.setDuration(318694);
			f.setDownload(true);
			f.setHQ(true); //待改
			musics.add(f);
		}
		cursor.close();
		db.close();
		return musics;
	}
	
	public boolean exist(String path){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[]{"path"}, 
				"path=?", new String[]{path}, null, null, null);
		if(cursor.getCount() == 0){
			cursor.close();
			db.close();
			return false;
		}else{
			cursor.close();
			db.close();
			return true;
	    }
	}
	
	public void initPlaylist(int id){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("list_name", "我的收藏");
		values.put("img", "");
		values.put("uid", id);
		db.insert(LIST_TABLE_NAME, null, values);
		for(int i=0;i<MusicService.mediaFiles.size();i++){
			values.clear();
			values.put("sid", MusicService.mediaFiles.get(i).getMusic_id());
			values.put("lid", 1);
			db.insert(LIST_SONG_TABLE_NAME, null, values);
		}
		values.clear();
		values.put("list_name", "最近播放");
		values.put("img", "");
		values.put("uid", id);
		db.insert(LIST_TABLE_NAME, null, values);
		for(int i=0;i<MusicService.allFiles.size();i++){
			values.clear();
			values.put("sid", MusicService.allFiles.get(i).getMusic_id());
			values.put("lid", 2);
			db.insert(LIST_SONG_TABLE_NAME, null, values);
		}
		db.close();
	}
	
	public void addPlaylist(String name,int uid){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("list_name", name);
		values.put("img", "");
		values.put("uid", uid);
		db.insert(LIST_TABLE_NAME, null, values);
		db.close();
	}
	
	public void addSongsToList(int id,List<MediaFile> files){
		SQLiteDatabase db = helper.getWritableDatabase();
		for(int i = 0;i<files.size();i++){
			ContentValues values = new ContentValues();
			values.put("sid", files.get(i).getMusic_id());
			values.put("lid", id);
			db.insert(LIST_SONG_TABLE_NAME,null, values);
		}
		db.close();
	}
	
	public List<Playlist> getAllPlaylists(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Playlist> lists = new ArrayList<Playlist>();
		Cursor cursor = db.query(LIST_TABLE_NAME, new String[]{"id"},null, null, null, null, null);
		Log.i("list_size",String.valueOf(cursor.getCount()));
		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			Playlist list = getPlaylistById(id);
			lists.add(list);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return lists;
	}
	
	public int getPlaylistIdByName(String name){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(LIST_TABLE_NAME, new String[]{"id"}, "list_name=?",
			    new String[]{name}, null, null, null);
		cursor.moveToFirst();
		int id = cursor.getInt(cursor.getColumnIndex("id"));
		cursor.close();
		db.close();
		return id;
	}
	
	public Playlist getPlaylistById(int id){
		Playlist list = new Playlist();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(LIST_TABLE_NAME, new String[]{"id,list_name,img"}, 
				"id=?", new String[]{String.valueOf(id)}, null, null, null);
		cursor.moveToFirst();
		String name = cursor.getString(cursor.getColumnIndex("list_name"));
		String img = cursor.getString(cursor.getColumnIndex("img"));
		list.setId(id);
		list.setName(name);
		list.setImg(img);
		Cursor cursor1 = db.query(LIST_SONG_TABLE_NAME,new String[]{"sid"},"lid=?",
				new String[]{String.valueOf(id)}, null,null,null);
		if(cursor1.getCount()!=0){
		cursor1.moveToFirst();
		for(int i=0;i<cursor1.getCount();i++){
			int sid = cursor1.getInt(cursor1.getColumnIndex("sid"));
			int index = MusicService.allFiles.indexOf(new MediaFile(sid));
			MediaFile file =  MusicService.allFiles.get(index);
			list.addSong(file);
			cursor1.moveToNext();
		  }
		  cursor.close();
		  cursor1.close();
		  db.close();
		  return list;
		}
		cursor.close();
		cursor1.close();
		db.close();
	    return list;
	}
	
	public List<Integer> getUserPlaylistId(int uid){
		List<Integer> list_id = new ArrayList<Integer>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(LIST_TABLE_NAME, new String[]{"id"}, 
				"uid=?", new String[]{String.valueOf(uid)}, null, null, null);
		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++){
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			cursor.moveToNext();
			list_id.add(id);
		}
		cursor.close();
		db.close();
		return list_id;
	}
	
	public List<Playlist> getUserPlaylists(int uid){
		List<Playlist> lists = new ArrayList<Playlist>();
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Integer> lids = getUserPlaylistId(uid);
		for(int i=0;i<lids.size();i++){
			lists.add(getPlaylistById(lids.get(i)));
		}
		db.close();
		return lists;
	}

	public void addCacheUser(String uname, String pwd) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("uid", (int) uname.toCharArray()[2]);
		values.put("uname", uname);
		values.put("password", pwd);
		values.put("nickname", uname);
		values.put("head_cache", "");
		values.put("ugroup", 0);
		values.put("follow", 0);
		values.put("friend", 0);
		db.insert(USER_TABLE_NAME, null, values);
		db.close();
	}
	
	public User getUserFromCacheByName(String uname){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(USER_TABLE_NAME, new String[]{"id","uid","uname","password","nickname",
				"ugroup","head_cache",
				"follow","friend"},
				"uname=?",new String[]{uname}, null, null, null);
		cursor.moveToFirst();
		if(cursor.getCount() == 0){
			cursor.close();
			db.close();
			return null;
		}else{
		    int id = cursor.getInt(cursor.getColumnIndex("id"));
		    int uid = cursor.getInt(cursor.getColumnIndex("uid"));
		    String password = cursor.getString(cursor.getColumnIndex("password"));
		    String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
		    String head_cache = cursor.getString(cursor.getColumnIndex("head_cache"));
		    int ugroup = cursor.getInt(cursor.getColumnIndex("ugroup"));
		    int follow = cursor.getInt(cursor.getColumnIndex("follow"));
		    int friend = cursor.getInt(cursor.getColumnIndex("friend"));
		    User u = new User(id, uname, password, nickname,head_cache, uid, ugroup, follow,friend);
		    cursor.close();
		    db.close();
		    return u;
	    }
	}
	
	public void saveUserCacheHeadPath(String uname){
		SQLiteDatabase db = helper.getWritableDatabase();
		String cache_path = MusicApplication.USER_HEAD_CACHE_FOLDER+uname+".png";
		ContentValues values = new ContentValues();
		values.put("head_cache",cache_path);
		db.update(USER_TABLE_NAME, values, "uname=?", new String[]{uname});
		db.close();
	}
}
