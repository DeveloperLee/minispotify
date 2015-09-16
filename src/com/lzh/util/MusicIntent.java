package com.lzh.util;

import com.lzh.service.MusicService;

import android.content.Context;
import android.content.Intent;

public class MusicIntent extends Intent {
	
	      public MusicIntent(Context context){
	    	    setClass(context, MusicService.class);
	      }
	      
	      public Intent setMusicAction(String action){
	    	  return setAction(action);
	      }

}
