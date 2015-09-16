package com.lzh.animation;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class MusicIconTransAnimation {
      
	public static TranslateAnimation getAnim(float fromX,float toX,float fromY,float toY){
		TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE,fromX, Animation.ABSOLUTE,toX, 
				Animation.ABSOLUTE,
				fromY,Animation.ABSOLUTE,toY);
		anim.setDuration(2000L);
		anim.setInterpolator(new AccelerateInterpolator());
		anim.setFillAfter(false);
		return anim;
	}
}
