package com.lzh.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorManagerHelper implements SensorEventListener {
	
	private static final int SPEED_THRESHOLD = 5000;
	private static final int MINIMUM_INTERVAL = 50;
	private SensorManager manager;
	private Sensor sensor;
	private OnShakeListener shakeListener;
	private Context context;
	private float lastX = 0f;
	private float lastY = 0f;
	private float lastZ = 0f;
	private long lastEventTime;
	
	public SensorManagerHelper(Context context){
		this.context = context;
		initSensor();
	}
	
    /*
     * 初始化Sensor,先得到SensorManager，再通过Manager获得Sensor,然后注册Sensor
     */
	public void initSensor() {
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if(manager != null){
			sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		if(sensor != null){
			manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	public void stopSensor(){
		manager.unregisterListener(this);
	}
    
	public void setOnShakeListener(OnShakeListener listener){
		this.shakeListener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
    
	/*
	 * 这个方法当设备的Sensor数据改变的时候调用
	 * (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
       long currentEventTime = System.currentTimeMillis();
       long interval = currentEventTime - lastEventTime;
       if(interval < MINIMUM_INTERVAL){
    	   return ;
       }
       lastEventTime = currentEventTime;
       float deltaX = event.values[0] - lastX;
       float deltaY = event.values[1] - lastY;
       float deltaZ = event.values[2] - lastZ;
       lastX = event.values[0];
       lastY = event.values[1];
       lastZ = event.values[2];
       double speed = Math.sqrt(deltaX*deltaX + deltaY*deltaY + 
    		   deltaZ*deltaZ)/interval * 10000;
       if(speed >= SPEED_THRESHOLD){
    	   shakeListener.onShake();
       }
       
	}
	
	public interface OnShakeListener{
		public void onShake();
	}

}
