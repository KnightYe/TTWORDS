package com.zhu.ttwords.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zhu.ttwords.R;

public class LoginActivity extends Activity {

	SharedPreferences sp;
	EditText userName;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		sp = getSharedPreferences("setting", MODE_PRIVATE);

		userName = (EditText) this.findViewById(R.id.activity_logon_username);
		password = (EditText) this.findViewById(R.id.activity_logon_password);
	}

	public void regist(View v) {
		Editor editor = sp.edit();
		editor.putString("password", password.getText().toString());
		editor.commit();
	}
}
