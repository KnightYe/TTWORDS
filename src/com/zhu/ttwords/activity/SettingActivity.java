package com.zhu.ttwords.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhu.ttwords.R;
import com.zhu.ttwords.bean.InformationBean;
import com.zhu.ttwords.bean.UserBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.value.SQLS;

public class SettingActivity extends Activity {
	/**
	 * 准备用fragment来做。模仿android原生设置。点击一个蹦出来一个。
	 */
	SharedPreferences sp;
	String username;
	LinearLayout layout;
	EditText text;
	EditText text2;
	UserBean bean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initData();
		init();
	}

	private void initData() {
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("USERNAME", null);
		try {
			bean = (UserBean) DataHelpUtil.getSingleBean(UserBean.class,
					SQLS.Main_All_Count, new String[] { username });
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void init() {

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
