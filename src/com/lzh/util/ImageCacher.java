package com.lzh.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lzh.app.MusicApplication;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ImageCacher {
	
	public static void cacheImg(InputStream is,String folder,String name){
		File file = new File(folder);
		if(!file.exists()){
			file.mkdirs();
		}
		try {
			byte cache[] = new byte[1024];
			int ch = -1;
			File cacheFile = new File(folder+name+".png");
			if(!cacheFile.exists()){
			  cacheFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(cacheFile);
			while((ch = is.read(cache)) != -1){
				fos.write(cache, 0, ch);
			}
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void cacheUserhead(Bitmap bitmap,String username){
		File folder = new File(MusicApplication.USER_HEAD_CACHE_FOLDER);
		if(!folder.exists()){
			folder.mkdirs();
		}
		String save_path = MusicApplication.USER_HEAD_CACHE_FOLDER+username+".png";
		File f = new File(save_path);
		if(f.exists()){
		   try {
			f.createNewFile();
		   } catch (IOException e) {
			e.printStackTrace();
		   }
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, bos);
		byte[] stream = bos.toByteArray();
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(stream);
			fos.flush();
			fos.close();
			bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Bitmap getUserCacheHead(String username){
		String save_path = MusicApplication.USER_HEAD_CACHE_FOLDER+username+".png";
		File file = new File(save_path);
		try {
			FileInputStream is = new FileInputStream(file);
			BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
			sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
			is.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
