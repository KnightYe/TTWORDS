package com.zhu.ttwords.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhu.ttwords.R;
import com.zhu.ttwords.bean.InformationBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.util.DateUtil;
import com.zhu.ttwords.value.SQLS;

public class MainActivity extends Activity {

	SharedPreferences sp;
	String username;
	TextView learn;
	TextView review;
	TextView tomrow;
	String text_learn;
	String text_mission;
	String text_tomrow;
	private long firstTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("USERNAME", null);
		learn = (TextView) this.findViewById(R.id.activity_main_learn);
		review = (TextView) this.findViewById(R.id.activity_main_review);
		tomrow = (TextView) this.findViewById(R.id.activity_main_tomrow);

		init();
	}

	private void init() {
		InformationBean bean_all_count = null;
		InformationBean bean_mission_count = null;
		InformationBean bean_tomrow_count = null;
		try {
			bean_all_count = (InformationBean) DataHelpUtil.getSingleBean(
					InformationBean.class, SQLS.Main_All_Count,
					new String[] { username });

			bean_mission_count = (InformationBean) DataHelpUtil.getSingleBean(
					InformationBean.class, SQLS.Main_Mission_Count,
					new String[] { username, DateUtil.getCurrentDate() });
			bean_tomrow_count = (InformationBean) DataHelpUtil.getSingleBean(
					InformationBean.class, SQLS.Main_Mission_Count,
					new String[] { username, DateUtil.getTomrowDate() });
			text_learn = bean_all_count.getCount();
			text_mission = bean_mission_count.getCount();
			text_tomrow = bean_tomrow_count.getCount();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		learn.setText(text_learn);
		review.setText(text_mission);
		tomrow.setText(text_tomrow);
	}

	public void study(View v) {
		Intent intent = new Intent(this, StudyActivity.class);
		intent.putExtra("MISSION", text_mission);
		startActivity(intent);
	}

	public void setting(View v) {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataHelpUtil.close();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else { // 两次按键小于2秒时，退出应用
				System.exit(0);
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
