package com.lzh.model;

public class OnlineMusic {
	
	
	private long music_id;
	private String music_name;
	private String artist_name;
	private String album_name;
	private String duration;
	private String path;
	private String img;
    private boolean isHQ;
    
    
	public OnlineMusic(long music_id, String music_name, String artist_name,
			String album_name, String duration, String path, String img,
			boolean isHQ) {
		super();
		this.music_id = music_id;
		this.music_name = music_name;
		this.artist_name = artist_name;
		this.album_name = album_name;
		this.duration = duration;
		this.path = path;
		this.img = img;
		this.isHQ = isHQ;
	}

	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public boolean isHQ() {
		return isHQ;
	}


	public void setHQ(boolean isHQ) {
		this.isHQ = isHQ;
	}


	public long getMusic_id() {
		return music_id;
	}

	public void setMusic_id(long music_id) {
		this.music_id = music_id;
	}

	public String getMusic_name() {
		return music_name;
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

	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
    
	
}
