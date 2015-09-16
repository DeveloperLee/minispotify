package com.lzh.app;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class ApplicationManager {
      
	private static Stack<Activity> activityStack;
	private static ApplicationManager instance;
	
	private ApplicationManager(){
		
	}
	
	public static ApplicationManager getInstance(){
		if(instance == null){
		   instance = new ApplicationManager();
		}
		return instance;
	}
	
	public void addActivity(Activity activity){
		if(activityStack == null){
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	
	public Activity currentActivity(){
		return activityStack.lastElement();
	}
	
	public static int getAcitivityStackSize(){
		return activityStack.size();
	}
	
	public void finishActivity(){
		Activity activity = activityStack.lastElement();
		if(activity!=null){
			activity.finish();
			activity = null;
		}
	}
	
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	public void finishActivity(Class<?> clz){
		for(Activity activity:activityStack){
			  if(activity.getClass().equals(clz)){
				  finishActivity(activity);
			  }
		}
	}
	
	public void finishAllActivities(){
		for(int i=0;i<activityStack.size();i++){
			if(activityStack.get(i)!=null){
				activityStack.get(i).finish();
			}
		}
	    activityStack.clear();
	}
	
	public void exit(Context context){
		finishAllActivities();
		ActivityManager m = (ActivityManager) context.getSystemService(
                 Context.ACTIVITY_SERVICE);
		m.restartPackage(context.getPackageName());
		System.exit(0);
	}
 }
