package com.zhu.ttwords.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhu.ttwords.R;
import com.zhu.ttwords.TTWORDS;
import com.zhu.ttwords.bean.InformationBean;
import com.zhu.ttwords.bean.UserBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.value.DefaultSetting;
import com.zhu.ttwords.value.WHAT;

public class LoadingActivity extends AbstractCommonActivity {

	public static final Long loadTime = 1500l;
	SharedPreferences sp;
	Editor editor;
	Handler handler;// ������Ϣ��������Ϣ��
	Runnable readDB;// ��ȡ���ݿ��߳�
	Animation rotateAnimation;
	ImageView image;// ����ͼƬ
	TextView text;// ״̬˵������
	Long startTime = 0l;// ����ʼʱ��
	Long endTime = 0l;// �������ʱ��
	TelephonyManager phoneMgr;
	String sql_user;
	String sql_relation;
	String sql_count_relation;
	String username;
	String password;
	String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		init();
	}

	/**
	 * �ж����ݿ��Ƿ���ڣ����ڵĻ�:�����ڵĻ�д�����ݿ�
	 */
	private void init() {
		this.sp = getSharedPreferences("setting", MODE_PRIVATE);
		this.phoneMgr = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		this.tel = phoneMgr.getLine1Number();
		this.startTime = System.currentTimeMillis();
		this.text = (TextView) LoadingActivity.this
				.findViewById(R.id.activity_loading_text);
		this.image = (ImageView) LoadingActivity.this
				.findViewById(R.id.activity_loading);
		this.rotateAnimation = AnimationUtils.loadAnimation(
				LoadingActivity.this, R.anim.loading_animation);
		this.handler = TTWORDS.getHandler();
		this.registerHandler(WHAT.LOADINGACTIVITY, this);
		this.readDB = new Runnable() {
			@Override
			public void run() {
				initDB();
				initInfo();

			}

		};
		new Thread(readDB).start();
		/*****/
		image.startAnimation(rotateAnimation);

	}

	/**
	 * ������ݿ�·����������ݿ��ļ��������ݿ⡢������ݿ�汾**** ��ʼ��Ĭ������
	 */
	private void initDB() {
		boolean hasDir = DataHelpUtil.initDatabaseDir(this);
		boolean hasDB = DataHelpUtil.initDatabaseFile(this);
		boolean isNewVer = DataHelpUtil.initDatabaseVersion(this);
		// ���û��DB��û��DBĿ¼������Ϊ���°�װ��
		if (!hasDir && !hasDB) {
			editor = sp.edit();
			editor.putString("TEL", "");
			editor.putString("PASSWORD", "");
			editor.putString("WORDS_COUNT_PER_GROUP",
					DefaultSetting.WORDS_COUNT_PER_GROUP);
			editor.putString("TOTLE", "0");

			editor.commit();
		}
	}

	private void initInfo() {
		sql_user = "SELECT* FROM TT_USER WHERE TEL = ? AND PASSWORD = ?;";
		sql_relation = "SELECT * FROM TT_REPERTORY_JP WHERE  TABLE_NAME = 'TT_RESOURCE_JP' AND UID = ?; ";
		sql_count_relation = "SELECT count(*) AS 'COUNT' FROM TT_REPERTORY_JP;";
		// ��¼
		username = sp.getString("TEL", "");
		password = sp.getString("PASSWORD", "");
		if (!username.equals("") && !password.equals("")) {
			UserBean bean = null;
			try {
				bean = (UserBean) DataHelpUtil.getDataBean(UserBean.class,
						sql_user, new String[] { username, password });
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (bean == null) {
				Log.d("DEBUG", "login wrong");
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
			Log.d("DEBUG", "login success");
		}
		// ��¼�ɹ����ȡuser���ݿ���Ϣ�����������ļ�
		// �Ѿ�ѧ���ĵ�������
		InformationBean bean_info = null;
		try {
			bean_info = (InformationBean) DataHelpUtil.getSingleBean(
					InformationBean.class, sql_count_relation);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		editor = sp.edit();
		editor.putString("TOTLE", bean_info.getCount());
		editor.commit();
		sendCompleteMessage();
	}

	private void sendCompleteMessage() {

		endTime = System.currentTimeMillis();
		if (endTime - startTime < loadTime) {
			try {
				Thread.sleep(loadTime - (endTime - startTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Message msg = handler.obtainMessage(WHAT.LOADINGACTIVITY, RESULT_OK, 0);
		handler.sendMessage(msg);
	}

	@Override
	public void onReceivedMessage(Message msg) {
		switch (msg.arg1) {
		case RESULT_OK:
			Intent intent = new Intent();
			intent.setClass(LoadingActivity.this, MainActivity.class);
			LoadingActivity.this.startActivity(intent);
			LoadingActivity.this.finish();
		}
	}

}
