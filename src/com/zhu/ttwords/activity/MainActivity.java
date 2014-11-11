package com.zhu.ttwords.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhu.ttwords.R;
import com.zhu.ttwords.bean.InformationBean;
import com.zhu.ttwords.util.DataHelpUtil;

public class MainActivity extends Activity {

	SharedPreferences sp;
	String username;
	TextView learn;
	TextView review;
	String sql_count_relation;
	String text_learn;
	private long firstTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("USERNAME", null);
		learn = (TextView) this.findViewById(R.id.activity_main_learn);
		review = (TextView) this.findViewById(R.id.activity_main_review);

		sql_count_relation = "SELECT count(*) AS 'COUNT' FROM TT_REPERTORY_JP WHERE UID = ?;";
		InformationBean bean_info = null;
		try {
			bean_info = (InformationBean) DataHelpUtil.getSingleBean(
					InformationBean.class, sql_count_relation,
					new String[] { username });
			text_learn = bean_info.getCount();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		learn.setText(text_learn);
	}

	public void study(View v) {
		Intent intent = new Intent(this, StudyActivity.class);
		startActivity(intent);
	}

	public void setting(View v) {
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // ������ΰ���ʱ��������2�룬���˳�
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// ����firstTime
				return true;
			} else { // ���ΰ���С��2��ʱ���˳�Ӧ��
				System.exit(0);
			}
			break;
		}
		return super.onKeyUp(keyCode, event);
	}
}
