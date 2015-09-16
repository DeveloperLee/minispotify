package com.lzh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String img;
	private List<MediaFile> songs;
	
	public Playlist(String name,String img){
		this.name = name;
		this.img = img;
		songs = new ArrayList<MediaFile>();
	}
	
	
	public Playlist() {
		songs = new ArrayList<MediaFile>();
	}


	public Playlist(int id) {
		this.id = id;
	}


	public void addSong(MediaFile file){
		this.songs.add(file);
	}
	
	public void removeSong(MediaFile file){
		this.songs.remove(file);
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCount(){
		return this.songs.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<MediaFile> getSongs() {
		return songs;
	}

	public void setSongs(List<MediaFile> songs) {
		this.songs = songs;
	}
	
	public String getDescription(){
		return "È«²¿¸èÇú"+getCount()+"Ê×";
	}

	public boolean isDowanloadAll() {
		if(this.songs == null){
			return false;
		}
		for(int i=0;i<songs.size();i++){
			if(!songs.get(i).isDownload()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof MediaFile)){
			return false;
		}
		Playlist o = (Playlist) other;
		if(this.id == o.getId()){
			return true;
		}else{
			return false;
		}
	}

}
