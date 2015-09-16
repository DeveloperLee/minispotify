package com.lzh.util;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GsonUtil {
	
	
	public static String getJson(String url){
		 HttpClient client =  new DefaultHttpClient();
		 HttpPost request;
		 try {
			request = new HttpPost(new URI(url));
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

}
