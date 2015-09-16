package com.lzh.activity;

import com.lzh.db.MusicDB;
import com.lzh.lzhmusic.R;
import com.lzh.net.UserValidatorThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private ImageButton back;
	private EditText username;
	private EditText password;
	private EditText confirm_password;
	private Button register;
	
	private String uname;
	private String pwd;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		handler = new UserHandler(Looper.myLooper());
		initView();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		username = (EditText) findViewById(R.id.register_username);
		password = (EditText) findViewById(R.id.register_password);
		confirm_password = (EditText) findViewById(R.id.register_confirm_password);
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
			    	uname = username.getText().toString();
					pwd = password.getText().toString();
					String confirm_pwd = confirm_password.getText().toString();
					if(uname.equals("") || pwd.equals("")){
						password = (EditText) findViewById(R.id.register_password);
						confirm_password = (EditText) findViewById(R.id.register_confirm_password);
						Toast.makeText(RegisterActivity.this,R.string.name_pwd_cant_empty,Toast.LENGTH_SHORT).show();
						return ;
					}
					if(!pwd.equals(confirm_pwd)){
						password.setText("");
						confirm_password.setText("");
						Toast.makeText(RegisterActivity.this,R.string.pwd_confirm_failed,Toast.LENGTH_SHORT).show();
						return ;
					}
					new UserValidatorThread(uname,pwd,handler).start();
			}
		});
		
	}
	
	class UserHandler extends Handler{
		public UserHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0 :
			if(msg.arg1 == 0){
				username.setText("");
				password.setText("");
				confirm_password.setText("");
				Toast.makeText(RegisterActivity.this,R.string.uname_duplicate,Toast.LENGTH_SHORT).show();
			}else{
				MusicDB db = new MusicDB(getApplicationContext());
				db.addCacheUser(uname,pwd);
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				intent.putExtra("uname", uname);
				startActivity(intent);
				Toast.makeText(getApplicationContext(),R.string.register_success,Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
			case 1:
		    break;
        }
	 }
	}
}
