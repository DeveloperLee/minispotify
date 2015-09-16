package com.lzh.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lzh.exception.NoResourceException;
import com.lzh.exception.SDCardEmptyException;
import com.lzh.model.MediaFile;

import android.os.Environment;

public class FileUtils {
	   
	   public static List<File> filter() throws NoResourceException, SDCardEmptyException{
		    ArrayList<File> audio  = new ArrayList<File>();
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		    	File file = Environment.getExternalStorageDirectory();
		    	File files[] = file.listFiles();
		    	if(files!=null){
		    		for(File f : files){
		    			  if(f.toString().contains(".mp3")){
		    				  audio.add(f);
		    			  }
		    		}
		    		if(audio.isEmpty()){
		    			throw new NoResourceException("您的手机里没有音乐文件,请检查您的SD卡内是否有MP3文件");
		    		}
		    	}else{
		    		throw new SDCardEmptyException("您的SD卡内没有文件");
		    	}
	         }
			return  audio;
	   }
	   
	   public static void deleteFile(List<String> paths){
		   for(int i=0;i<paths.size();i++){
			   File file = new File(paths.get(i));
			   file.delete();
		   }
	   }
	   
	      public static int getPosition(long id,List<MediaFile> files){
		    	for(int i = 0;i<files.size();i++){
		    		if(files.get(i).getMusic_id() == id){
		    			return i;
		    		}
		    	}
				return -1;
		      }
}
