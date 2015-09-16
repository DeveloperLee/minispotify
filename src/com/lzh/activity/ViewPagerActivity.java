package com.lzh.activity;

import java.util.ArrayList;
import java.util.List;

import com.lzh.lzhmusic.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ViewPagerActivity extends Activity {
	
	private ViewPager vp;
	private List<View> views;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_page);
		initPage();
		initViewPager();
	}

    private void initPage(){
    	
    }
    
    private void initViewPager(){
    	vp = (ViewPager) findViewById(R.id.vp);
    	views = new ArrayList<View>();
    	LayoutInflater inflater = this.getLayoutInflater();
        views.add(inflater.inflate(R.layout.welcome_1, null));
        views.add(inflater.inflate(R.layout.welcome_2, null));
        views.add(inflater.inflate(R.layout.welcome_3, null));
        View redirect = inflater.inflate(R.layout.welcome_4, null);
        Button btn = (Button) redirect.findViewById(R.id.begin_app);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(arg0.getContext(),MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);  
			}
		});
        views.add(redirect);
        vp.setAdapter(new MyPagerAdapter(views));
        vp.setCurrentItem(0);
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    
    
    class MyOnClickListener implements View.OnClickListener{
    	
    	private int index = 0;
        public MyOnClickListener(int index){
        	this.index = index;
        }
		@Override
		public void onClick(View view) {
			vp.setCurrentItem(index);
		}
    }
    
    class MyPagerAdapter extends PagerAdapter{
    	
    	private List<View> views;
        
    	public MyPagerAdapter(List<View> views){
    		this.views = views;
    	}
		@Override
		public int getCount() {
			return views.size();
		}
		
		/**
		 * 从ViewPager中卸载某一个View,position是View在集合中的位置
		 */
		@Override
		public void destroyItem(View view,int position,Object obj){
			((ViewPager) view).removeView(views.get(position));
		}
		
		/**
		 * 初始化View并添加到ViewPager中，View中的事件注册可以在这里写
		 * 将新添加的View作为返回值
		 */
		@Override
		public Object instantiateItem(View view,int position){
		    ((ViewPager) view).addView(views.get(position));
			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
    }
    
    class MyOnPageChangeListener implements OnPageChangeListener{
        
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}


		@Override
		public void onPageSelected(int arg0) {
		}
    	
    }
}
