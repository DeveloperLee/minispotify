package com.lzh.net;

import java.lang.ref.WeakReference;

import com.lzh.lzhmusic.R;
import com.lzh.util.ImageDownloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
	
	private final WeakReference<ImageView> img;
	
	public BitmapDownloadTask(ImageView img){
		this.img = new WeakReference<ImageView>(img);
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		return ImageDownloader.downloadImage(arg0[0],arg0[1]);
	}
	
	@Override
	protected void onPostExecute(Bitmap bitmap){
		if(this.isCancelled()){
			bitmap = null;
			ImageView view = img.get();
			view.setImageResource(R.drawable.icon);
		}
		
		if(img!=null){
			ImageView view1 = img.get();
			if(bitmap!=null){
				view1.setImageBitmap(bitmap);
			}
		}
	}

}
