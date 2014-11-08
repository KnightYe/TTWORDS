package com.zhu.ttwords.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhu.ttwords.R;

public class SettingActivity extends Activity {

	LinearLayout layout;
	EditText text;
	EditText text2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		layout = (LinearLayout) this.findViewById(R.id.layout);
		text = (EditText) this.findViewById(R.id.editText1);
		text2 = (EditText) this.findViewById(R.id.editText2);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
			if (text.hasFocus()) {
				text2.requestFocus();
			} else {
				text.requestFocus();
			}
			break;

		default:
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
