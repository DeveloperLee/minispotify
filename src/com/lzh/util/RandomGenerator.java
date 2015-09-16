package com.lzh.util;

import java.util.Random;


public class RandomGenerator { 
	     
	    private static int next_pos;
         
	    //产生不重复的随机数
	     public static int generateNextShuffelPos(int current,int size){
	    	  Random random = new Random();
	    	  do{
	    		  next_pos = Math.abs(random.nextInt() % size);           //随机数可能产生负数，取绝对值
	    	  }while(next_pos == current);
	    	  
	    	  return next_pos;
	     }

}
