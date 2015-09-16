package com.lzh.model;


public class MediaFile {
	
	private long music_id;
	private String music_name;
	private String artist_name;
	private String album;
	private String path;
	private int duration;    
	private String img;
	private boolean isDownload;
	private boolean isHQ;
	
	public boolean isDownload() {
		return isDownload;
	}


	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}


	public boolean isHQ() {
		return isHQ;
	}


	public MediaFile(long music_id, String music_name, String artist_name,
			String album, String path, int duration, String img,
			boolean isDownload, boolean isHQ) {
		super();
		this.music_id = music_id;
		this.music_name = music_name;
		this.artist_name = artist_name;
		this.album = album;
		this.path = path;
		this.duration = duration;
		this.img = img;
		this.isDownload = isDownload;
		this.isHQ = isHQ;
	}


	public void setHQ(boolean isHQ) {
		this.isHQ = isHQ;
	}


	public MediaFile(){
		
	}
	

	public MediaFile(String music_name,String path){
          this.music_name = music_name;
          this.path = path;
	}
	public MediaFile(int sid) {
		this.music_id = sid;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public long getMusic_id() {
		return music_id;
	}

	public void setMusic_id(long music_id) {
		this.music_id = music_id;
	}

	public String getMusic_name() {
		return this.music_name;
	}

	public void setMusic_name(String music_name) {
		this.music_name = music_name;
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = (int) duration;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof MediaFile)){
			return false;
		}
		MediaFile o = (MediaFile) other;
		if(this.music_id == o.getMusic_id()){
			return true;
		}else{
			return false;
		}
	}

}
