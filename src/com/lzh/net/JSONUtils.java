package com.lzh.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.lzh.db.MusicDB;
import com.lzh.model.MediaFile;
import com.lzh.model.User;
import com.lzh.service.MusicService;
import com.lzh.util.MusicUtils;

public class JSONUtils {
	
	private static final String PATH = Environment.getExternalStorageDirectory()+"/LzhMusic/Music/";
	private static final String IMG_PATH =  Environment.getExternalStorageDirectory()+"/LzhMusic/Picture/";

	
	public static Map<String,List<MediaFile>> decodeJSON(String json){
		Map<String,List<MediaFile>> map = new HashMap<String,List<MediaFile>>();
		try {
			List<MediaFile> rank = new ArrayList<MediaFile>();
			JSONObject object1 = new JSONObject(json);
			JSONArray array1 = object1.getJSONArray("rank");
			for(int i=0;i<array1.length();i++){
				JSONObject obj = (JSONObject) array1.get(i);
				MediaFile music = new MediaFile(obj.getLong("id"), obj.getString("name"), 
						obj.getString("artist"), obj.getString("album"), 
						PATH+obj.getString("name")+".mp3",MusicUtils.convertToLong(obj.getString("duration")),
						IMG_PATH+obj.getString("name")+".png",true,true);
				rank.add(music);
			}
			map.put("rank", rank);
			List<MediaFile> news = new ArrayList<MediaFile>();
			JSONObject object2 = new JSONObject(json);
			JSONArray array2 = object2.getJSONArray("new");
			for(int i=0;i<array2.length();i++){
				JSONObject obj = (JSONObject) array2.get(i);
				MediaFile music = new MediaFile(obj.getLong("id"), obj.getString("name"), 
						obj.getString("artist"), obj.getString("album"), 
						PATH+obj.getString("name")+".mp3",MusicUtils.convertToLong(obj.getString("duration")),
						IMG_PATH+obj.getString("name")+".png",true,true);
				news.add(music);
			}
			map.put("new", news);
			return map;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getSongByPost(String url,String name){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
	    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
	    pairs.add(new BasicNameValuePair("artistName",name));
	    try {
			post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");
				return result;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static List<String> decodeSinger(String json){
		List<String> singers = new ArrayList<String>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray singer_names = obj.getJSONArray("popularArtist");
			for(int i=0;i<singer_names.length();i++){
				JSONObject o = (JSONObject) singer_names.get(i);
				singers.add(o.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return singers;
	}
	
	public static List<MediaFile> getSingerSong(String json){
		List<MediaFile> songs = new ArrayList<MediaFile>();
		try {
			JSONObject object1 = new JSONObject(json);
			JSONArray array1 = object1.getJSONArray("search");
			for(int i=0;i<array1.length();i++){
				JSONObject obj = (JSONObject) array1.get(i);
				MediaFile music = new MediaFile(obj.getLong("id"), obj.getString("name"), 
						obj.getString("artist"), obj.getString("album"), 
						PATH+obj.getString("name")+".mp3",MusicUtils.convertToLong(obj.getString("duration")),
						IMG_PATH+obj.getString("name")+".png",true,true);
				songs.add(music);
		    }
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	  return songs;	
	}
	
	
	public static String getJson(String url){
		 HttpClient client =  new DefaultHttpClient();
		 HttpGet request;
		 try {
			request = new HttpGet(new URI(url));
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				if(entity != null){
					String json = EntityUtils.toString(entity, "UTF-8");
					return json;
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return "a";
		} catch (ParseException e) {
			e.printStackTrace();
			return "b";
		} catch (IOException e) {
			e.printStackTrace();
			return "c";
		}
		return null;
	}
	
	public static List<String> getRecommendDescriptions(){
		List<String> des = new ArrayList<String>();
		des.add("大爷大妈,健康长寿");
		des.add("因为爱,我只爱你");
		des.add("章子怡都听什么");
		return des;
	}

    //Method stub
	public static List<MediaFile> getRecommendSongsById(int pic_position) {
		List<MediaFile> files = new ArrayList<MediaFile>();
		files.add(MusicService.allFiles.get(pic_position));
		files.add(MusicService.allFiles.get(pic_position+1));
		files.add(MusicService.allFiles.get(2*pic_position+2));
		files.add(MusicService.allFiles.get(3*pic_position+3));
		return files;
	}
	
	//Method stub
	public static User getUserByUsername(String uname,Context context){
		MusicDB db = new MusicDB(context);
		return db.getUserFromCacheByName(uname);
	}
}
