package com.lzh.net;

import android.os.Handler;
import android.os.Message;

public class UserValidatorThread extends Thread {
	
	private String name;
	private String password;
	private Handler handler;
	
	public UserValidatorThread(String name,String password,Handler handler){
		this.name = name;
		this.password = password;
		this.handler = handler;
	}
	
	@Override
	public void run(){
//		int result = UserValidator.register(name, password);
		int result = 1;
		Message msg = handler.obtainMessage(0);
		msg.arg1 = result;
		handler.removeMessages(0);
		handler.sendMessage(msg);
	}

}
