package com.lzh.animation;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class AlbumRotationAnim {
    
	     public static RotateAnimation getRotateAnim(){
	    	 RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	    	 anim.setDuration(1000L);
	    	 anim.setFillAfter(false);
	    	 anim.setRepeatCount(-1);
	    	 anim.setRepeatMode(Animation.RESTART);
			return anim;
	     }
	     
	     public static RotateAnimation getRotateAnim(long duration){
	    	 RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
	    	 anim.setDuration(duration);
	    	 anim.setFillAfter(false);
	    	 anim.setRepeatCount(-1);
	    	 anim.setRepeatMode(Animation.RESTART);
			return anim;
	     }
}
