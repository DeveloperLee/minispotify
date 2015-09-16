package com.lzh.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class MusicApplication {
	 
	   private static MusicApplication instance = null;
	   private static List<Activity> activities;
	   public static final String IMG_CACHE_FOLDER = "/sdcard/LzhMusic/Picture/";
	   public static final String USER_HEAD_CACHE_FOLDER = "/sdcard/LzhMusic/Heads/";
	   public static final String MUSIC_PATH = "/sdcard/LzhMusic/Music/";
	   public static final String SINGER_CACHE = "/sdcard/LzhMusic/Singers/";
	   public static final String MUSIC_DB_NAME = "music.db3";
	   
	   private MusicApplication(){
		     activities = new ArrayList<Activity>();
	   }
	   
	   public static MusicApplication getInstance(){
		    if(instance == null){
		    	instance = new MusicApplication();
		    	return instance;
		    }else{
		    	return instance;
		    }
	   }
	   
	   public void addActivity(Activity activity){
		    activities.add(activity);
	   }
	   
	   public void exit(){
		   for(Activity activity:activities){
			   activity.finish();
		   }
		   
		   System.exit(0);
	   }

}
