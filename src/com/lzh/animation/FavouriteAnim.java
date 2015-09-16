package com.lzh.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class FavouriteAnim { 
	
	  public static void addFavorAnim(View view){
		  ScaleAnimation animation = new ScaleAnimation(1F, 2F, 1F, 2F, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
		  animation.setDuration(100L);
		  animation.setFillAfter(false);
		  view.startAnimation(animation);
	  }

}
