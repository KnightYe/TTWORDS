package com.zhu.ttwords.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.zhu.ttwords.R;

public class MainActivity extends Activity {

	private long firstTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
