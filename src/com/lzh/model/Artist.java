package com.lzh.model;

public class Artist {
	    
	   private String artist_name;
	   private int total_song_count;
	   
	   public Artist(String name){
		   artist_name = name;
	   }

	public Artist() {
	}

	public String getArtist_name() {
		return artist_name;
	}

	public void setArtist_name(String artist_name) {
		this.artist_name = artist_name;
	}

	public int getTotal_song_count() {
		return total_song_count;
	}

	public void setTotal_song_count(int total_song_count) {
		this.total_song_count = total_song_count;
	}

}
