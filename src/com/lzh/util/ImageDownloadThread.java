package com.lzh.util;

public class ImageDownloadThread extends Thread{
	
	private String url;
	private String cacheName;
   
	public ImageDownloadThread(String url,String cacheName){
		this.url = url;
		this.cacheName = cacheName;
	}
	
	@Override
	public void run(){
		ImageDownloader.downloadImage(url, cacheName);
	}
}
