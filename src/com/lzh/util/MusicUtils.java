package com.lzh.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.lzh.app.MusicApplication;
import com.lzh.exception.NoResourceException;
import com.lzh.model.Album;
import com.lzh.model.Artist;
import com.lzh.model.MediaFile;


import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;

public class MusicUtils {
	
	      private static final int args[] = new int[2];
	      private static String convertedTime;

	      
	      public static String convertToTime(int time){
	    	   convertedTime = "";
	    	   int seconds = time/1000;
	    	   final int timeArgs[] = args;
	    	   timeArgs [0] = (int) (seconds / 60);
	    	   timeArgs[1] = (int) (seconds % 60);
	    	   convertedTime = fillTime(timeArgs[0])+":"+fillTime(timeArgs[1]);
	    	  return convertedTime;
	      }
	      
	      private static String fillTime(int toFormat){
	    	  if(toFormat>=0 && toFormat<10){
	    		  return "0"+String.valueOf(toFormat);
	    	  }else if(toFormat>=10){
	    		  return String.valueOf(toFormat);
	    	  }else{
	    		  return "--";
	    	  }
	      }
	      
	      public static int convertToLong(String time){
	    	  String[] times = time.split(":");
	    	  int minute = Integer.parseInt(times[0]);
	    	  int second = Integer.parseInt(times[1]);
	    	  int duration = (minute * 60 + second) * 1000;
	    	  return duration;
	      }
	      //判断给定的Service是否正在运行
	      public static boolean isServiceRunning(Context mContext,String className) {

	           boolean isRunning = false;
	           ActivityManager activityManager = (ActivityManager)
	           mContext.getSystemService(Context.ACTIVITY_SERVICE); 
	           List<ActivityManager.RunningServiceInfo> serviceList 
	                      = activityManager.getRunningServices(30);
	           if (!(serviceList.size()>0)) {
	               return false;
	           }
	           for (int i=0; i<serviceList.size(); i++) {
	               if (serviceList.get(i).service.getClassName().equals(className) == true) {
	            	   Log.i("Service",serviceList.get(i).service.getClassName().toString());
	                   isRunning = true;
	                   break;
	               }
	           }
	           return isRunning;
	       }
	      
	      //获得歌曲的集合的Cursor
	      public static Cursor queryAllMusic(ContentResolver resolver){
	    	  return resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					   null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	      }
	      
	      //将Cursor内的信息封装到MediaFile类中并返回List集合
	      public static List<MediaFile> getMediaFileList(Cursor cursor){
	    	   
	    	  List<MediaFile> mediaFiles = new ArrayList<MediaFile>();
	    	  
	    	  for(int count=0;count<cursor.getCount();count++){
	    		  cursor.moveToPosition(count);
	    		  if(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) !=0){
	    			  MediaFile mediaFile = new MediaFile();
	    			  mediaFile.setMusic_id(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
	    			  Log.i("id", String.valueOf(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))));
	    			  mediaFile.setMusic_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
	    			  mediaFile.setArtist_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
	    			  mediaFile.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
	    			  mediaFile.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
	    			  mediaFile.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
	    			  if(mediaFile.getPath().startsWith("/mnt"+MusicApplication.MUSIC_PATH)){
	    				  mediaFile.setDownload(true);
	    			  }else{
	    				  mediaFile.setDownload(false);
	    			  }
	    			  Log.i("PATH", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
	    			  mediaFile.setHQ(true);
	    			  mediaFiles.add(mediaFile);
	    		  }
	    	  }
	    	  return mediaFiles;
	      }
	      
	      public static List<HashMap<String, String>> getMediaFileMap(List<MediaFile> mediaFiles){
	    	  
	    	List<HashMap<String, String>> meHashMaps = new ArrayList<HashMap<String,String>>();
	    	Iterator<MediaFile> media_iter = mediaFiles.iterator();
	    	for(;media_iter.hasNext();){
	    		MediaFile file = media_iter.next();
	    		HashMap<String, String> map = new HashMap<String, String>();
	    		map.put("music_name", file.getMusic_name());
	    		map.put("artist_name", file.getArtist_name());
	    		map.put("album", file.getAlbum());
	    		map.put("path", file.getPath());
	    		map.put("duration", convertToTime(file.getDuration()));
	    		map.put("isDownload", String.valueOf(file.isDownload()));
	    		map.put("isHQ", String.valueOf(file.isHQ()));
	    		meHashMaps.add(map);
	    	}
			return meHashMaps;
	    	  
	      }
	      
	      public static int getPosition(long id,ContentResolver resolver){
	    	List<MediaFile> mediaFiles = getMediaFileList(queryAllMusic(resolver));
	    	for(int i = 0;i<mediaFiles.size();i++){
	    		if(mediaFiles.get(i).getMusic_id() == id){
	    			return i;
	    		}
	    	}
			return -1;
	      }
	      
	      public static Bitmap decodePng(Context context,int resId){
	    	 BitmapFactory.Options opt = new BitmapFactory.Options();  
	         opt.inPreferredConfig =Bitmap.Config.RGB_565;
	         opt.inPurgeable = true;  
	         opt.inInputShareable = true;  
	           //获取资源图片  
	         InputStream is = context.getResources().openRawResource(resId);  
	         return BitmapFactory.decodeStream(is,null,opt);  
	      }
	      
	      
	      public static List<Artist> getAllArtists(ContentResolver resolver) throws NoResourceException{
	    	    List<Artist> artists = new  ArrayList<Artist>();
	    	    HashSet<String> names = new HashSet<String>();
	    	    Cursor c = queryAllMusic(resolver);
	    	    c.moveToFirst();
	    	    for(int i = 0;i<c.getCount();i++){
	    	    	String name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
	    	    	if(name.equals("<unknown>")){
	    	    		name = "未知艺术家";
	    	    	}
	    	    	if(names.add(name)){
	    	    		artists.add(new Artist(name));
	    	    	}
	    	    	c.moveToNext();
	    	    }
	    	    if(artists.size() == 0){
	    	    	throw new NoResourceException("SD卡内没有音乐文件哦，快去下载吧！");
	    	    }
	    	    return artists;
	      }
	      
	      public static List<Album> getAllAlbums(ContentResolver resolver) throws NoResourceException{
	    	    List<Album> albums = new ArrayList<Album>();
	    	    HashSet<String> names = new HashSet<String>();
	    	    Cursor c = queryAllMusic(resolver);
	    	    c.moveToFirst();
	    	    for(int i = 0;i<c.getCount();i++){
	    	    	String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
	    	    	if(album.equals("<unknown>")){
	    	    		 album = "未知专辑";
	    	    	}
	    	    	if(names.add(album)){
	    	    		albums.add(new Album(album));
	    	    	}
	    	    	c.moveToNext();
	    	    }
	    	    if(albums.size() == 0){
	    	    	throw new NoResourceException("SD卡内没有音乐文件哦，快去下载吧！");
	    	    }
	    	    return albums;
	      }
	      
	      public static List<MediaFile> getArtistSong(String name,ContentResolver resolver){
	    	  List<MediaFile> mediaFiles = new ArrayList<MediaFile>();
	    	  
	    	  Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, 
	    			       MediaStore.Audio.Media.ARTIST + "='" + name+"'", null, null);
	    	  for(int count=0;count<cursor.getCount();count++){
	    		  cursor.moveToPosition(count);
	    		  if(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) !=0){
	    			  MediaFile mediaFile = new MediaFile();
	    			  mediaFile.setMusic_id(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
	    			  mediaFile.setMusic_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
	    			  mediaFile.setArtist_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
	    			  mediaFile.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
	    			  mediaFile.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
	    			  mediaFile.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
	    			  Log.i("Path", cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
	    			  if(mediaFile.getPath().startsWith(MusicApplication.MUSIC_PATH)){
	    				  mediaFile.setDownload(true);
	    			  }else{
	    				  mediaFile.setDownload(false);
	    			  }
	    			  mediaFile.setHQ(true);
	    			  mediaFiles.add(mediaFile);
	    		  }
	    	  }
	    	  
	    	  return mediaFiles;
	      }
	      
	      public static List<MediaFile> getAlbumSong(String name,ContentResolver resolver){
	    	  List<MediaFile> mediaFiles = new ArrayList<MediaFile>();
	    	  Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, 
	    			  MediaStore.Audio.Media.ALBUM+"='"+name+"'", null, null);
	    	  for(int count=0;count<cursor.getCount();count++){
	    		  cursor.moveToPosition(count);
	    		  if(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) !=0){
	    			  MediaFile mediaFile = new MediaFile();
	    			  mediaFile.setMusic_id(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
	    			  mediaFile.setMusic_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
	    			  mediaFile.setArtist_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
	    			  mediaFile.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
	    			  mediaFile.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
	    			  mediaFile.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
	    			  if(mediaFile.getPath().startsWith(MusicApplication.MUSIC_PATH)){
	    				  mediaFile.setDownload(true);
	    			  }else{
	    				  mediaFile.setDownload(false);
	    			  }
	    			  mediaFile.setHQ(true);
	    			  mediaFiles.add(mediaFile);
	    		  }
	    	  }
	    	  
	    	  return mediaFiles;
	      }
	      
	      public static List<MediaFile> getAllDownloadMusics(Cursor cursor){
              List<MediaFile> mediaFiles = new ArrayList<MediaFile>();
	    	  for(int count=0;count<cursor.getCount();count++){
	    		  cursor.moveToPosition(count);
	    		  if(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)) !=0
	    				  && cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)).startsWith(
	    						  "/mnt"+MusicApplication.MUSIC_PATH)){
	    			  MediaFile mediaFile = new MediaFile();
	    			  mediaFile.setMusic_id(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
	    			  mediaFile.setMusic_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
	    			  mediaFile.setArtist_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
	    			  mediaFile.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
	    			  mediaFile.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
	    			  mediaFile.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
	    		      mediaFile.setDownload(true);
	    			  mediaFile.setHQ(true);
	    			  mediaFiles.add(mediaFile);
	    		  }
	    	  }
	    	  return mediaFiles;
	      }
}
