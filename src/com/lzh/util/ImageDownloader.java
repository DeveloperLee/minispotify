package com.lzh.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.lzh.app.MusicApplication;
import com.lzh.util.ImageCacher;

public class ImageDownloader extends Thread{
	
	
	public static Bitmap downloadImage(String url,String cacheName){
		File file = new File(MusicApplication.IMG_CACHE_FOLDER+cacheName+".png");
		if(!file.exists()){
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				ImageCacher.cacheImg(is,MusicApplication.IMG_CACHE_FOLDER,cacheName);
				final Bitmap bitmap = BitmapFactory.decodeStream(is);
				return bitmap;
			}else{
				return null;
			}
			
		}
		 catch (ClientProtocolException e) {
			e.printStackTrace();
		 }catch (IOException e) {
		 	e.printStackTrace();
		 }
		}else{
		  try {
			FileInputStream fis = new FileInputStream(file);
			final Bitmap bitmap = BitmapFactory.decodeStream(fis);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		}
		return null;
	}

}