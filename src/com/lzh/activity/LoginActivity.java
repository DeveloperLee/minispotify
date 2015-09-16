package com.lzh.activity;

import com.lzh.lzhmusic.R;
import com.lzh.net.UserValidator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private ImageButton back;
	private Button login;
	private Button register;
	private EditText username;
	private EditText password;
	
	private String uname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initData();
		initView();
	}

	private void initData() {
		uname = getIntent().getStringExtra("uname");
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		username = (EditText) findViewById(R.id.login_username);
		if(uname != null){
			username.setText(uname);
		}
		password = (EditText) findViewById(R.id.login_password);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String name = username.getText().toString();
				String pwd = password.getText().toString();
				if(name.equals("") || pwd.equals("")){
					username.setText("");
					password.setText("");
					Toast.makeText(getApplicationContext(), R.string.name_pwd_cant_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				int result = UserValidator.login(name, pwd, getApplicationContext());
				switch(result){
				    case 0 :
				    	username.setText("");
						password.setText("");
						Toast.makeText(getApplicationContext(), R.string.user_not_exist, Toast.LENGTH_SHORT).show();
				    	break;
				    case 1:
				    	username.setText("");
						password.setText("");
						Toast.makeText(getApplicationContext(), R.string.wrong_user, Toast.LENGTH_SHORT).show();
				    	break;
				    case 2:
						Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
						saveCacheUser(name);
						Intent intent = new Intent(LoginActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
				    	break;
				}
			}
		});
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.out_to_top,R.anim.in_from_bottom);
			}
		});
		
	}
	
	private void saveCacheUser(String uname) {
		SharedPreferences p = this.getSharedPreferences("usercache", 0);
		Editor e = p.edit();
		e.putString("uname", uname);
		e.commit();
	}

}
