package com.lzh.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.OvershootInterpolator;

public class SettingsBarAnim {
	
	    
	    public static void comeInScreen(View view){
	    	  AlphaAnimation animation = new AlphaAnimation(0F, 1F);
	    	  animation.setDuration(500L);
	    	  animation.setFillAfter(true);
	    	  animation.setInterpolator(new OvershootInterpolator(2F));
	    	  view.startAnimation(animation);
	    }
	    
	    public static void quitScreen(final View view){
	    	AlphaAnimation animation = new AlphaAnimation(1F, 0F);
	    	animation.setDuration(500L);
	    	animation.setFillAfter(true);
	    	view.startAnimation(animation);
	    }
}
