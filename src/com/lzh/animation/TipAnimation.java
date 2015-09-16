package com.lzh.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class TipAnimation {
	
	  public static void startAnimation(final View view){
		   AlphaAnimation animation  = new AlphaAnimation(1.0F, 0.0F);
		   animation.setDuration(8000L);
		   animation.setFillAfter(true);
		   animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				  view.setVisibility(View.GONE);
			}
		});
		   view.startAnimation(animation);
	  }

}
