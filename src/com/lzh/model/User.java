package com.lzh.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	private int id;
	private String username;
	private String password;
	private String nickname;
	private String head_cache;
	private int uid;
	private int group;
	private int follow;
	private int friend;
	private List<Playlist> lists;
	
	public User(int id, String username, String password,String nickname,String head_cache,
			int uid, int group, int follow,int friend) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.head_cache = head_cache;
		this.uid = uid;
		this.group = group;
		this.follow = follow;
		this.friend = friend;
		lists = new ArrayList<Playlist>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
    
	public String getHead_cache() {
		return head_cache;
	}

	public void setHead_cache(String head_cache) {
		this.head_cache = head_cache;
	}

	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public int getFollow() {
		return follow;
	}
	public void setFollow(int follow) {
		this.follow = follow;
	}
	public int getFriend() {
		return friend;
	}
	public void setFriend(int friend) {
		this.friend = friend;
	}
	public List<Playlist> getLists() {
		return lists;
	}
	public void setLists(List<Playlist> lists) {
		this.lists = lists;
	}

	public void addList(Playlist list) {
		this.lists.add(list);
	}

}
