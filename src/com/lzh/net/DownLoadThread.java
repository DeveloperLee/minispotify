package com.lzh.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.os.Handler;
import android.os.Message;

public class DownLoadThread extends Thread{
	
	private Handler handler;
	private String url;
	private String path;
	
	public DownLoadThread(Handler handler,String url,String path){
		this.handler = handler;
		this.url = url;
		this.path = path;
	}
	
	@Override
	public void run(){
		this.download(handler, url, path);
	}
	
	public void download(final Handler handler,String url,final String path){
		
		final File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		URL location;
		try {
			location = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) location.openConnection();
			InputStream is = conn.getInputStream();
			byte buf[] = new byte[1024];
			int ch = -1;
			FileOutputStream fos = new FileOutputStream(file);
			Message msg = handler.obtainMessage(4);
			handler.removeMessages(4);
			handler.sendMessage(msg);
			while((ch = is.read(buf))!=-1){
				fos.write(buf, 0, ch);
			}
			fos.flush();
			fos.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		   handler.sendMessage(handler.obtainMessage(5));
		}
	}

}
