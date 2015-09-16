package com.lzh.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageGetter {
	
	
	public static Drawable getSeekbarBg(Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int y = (int) (height *0.6);
		int x = (int) (width * 0.3);
		int get_y = (int)(height * 0.1);
		int get_x = (int)(width * 0.4);
		return new BitmapDrawable(Bitmap.createBitmap(bitmap, x, y, get_x, get_y));
	}
	
	public static Drawable getControllerBg(Bitmap bitmap){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int y = (int) (height*0.7);
		int x = (int) (width*0.3);
		int get_y = (int) (height * 0.2);
		int get_x = (int) (width * 0.4);
		return new BitmapDrawable(Bitmap.createBitmap(bitmap, x, y, get_x, get_y));
	}

}
