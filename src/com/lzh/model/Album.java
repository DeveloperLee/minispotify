package com.lzh.model;

public class Album {   
	   
	    private String name;
	    private int total_song;
	    
	    public Album(String name){
	    	this.name = name;
	    }
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getTotal_song() {
			return total_song;
		}
		public void setTotal_song(int total_song) {
			this.total_song = total_song;
		}
	           

}
