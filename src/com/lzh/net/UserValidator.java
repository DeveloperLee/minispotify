package com.lzh.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.lzh.db.MusicDB;
import com.lzh.model.User;
import com.lzh.service.MusicService;


public class UserValidator {
	
  	   private static final String REGISTER_URL = "http://172.16.185.157:8080/MusicPlayer/userRegister.action"; 
	
	    //Method stub
		public static int register(String username,String password){
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(REGISTER_URL);
		    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		    pairs.add(new BasicNameValuePair("username",username));
		    pairs.add(new BasicNameValuePair("password",password));
		    try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
				HttpResponse response = client.execute(post);
				if(response.getStatusLine().getStatusCode() == 200){
					HttpEntity entity = response.getEntity();
					int result = Integer.parseInt(EntityUtils.toString(entity, "UTF-8"));
					return result;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}
		
		//Method stub
		public static int login(String username,String password,Context context){
			MusicDB db = new MusicDB(context);
			User u = db.getUserFromCacheByName(username);
			if(u == null){
				return 0;
			}else if(!u.getPassword().equals(password)){
				return 1;
			}else{
				u.setLists(db.getUserPlaylists(u.getId()));
				MusicService.current_user = u;
				return 2;
			}
		}

}
