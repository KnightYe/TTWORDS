package com.zhu.ttwords.activity;

import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhu.ttwords.R;
import com.zhu.ttwords.TTWORDS;
import com.zhu.ttwords.bean.InformationBean;
import com.zhu.ttwords.bean.UserBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.util.DateUtil;
import com.zhu.ttwords.value.DefaultSetting;
import com.zhu.ttwords.value.SQLS;
import com.zhu.ttwords.value.WHAT;

public class LoadingActivity extends AbstractCommonActivity {

	public static final Long loadTime = 1500l;
	SharedPreferences sp;
	Editor editor;
	Runnable readDB;// ��ȡ���ݿ��߳�
	Animation rotateAnimation;
	ImageView image;// ����ͼƬ
	TextView loading_text;// ״̬˵������
	EditText user_tel;
	EditText password;
	Button submit;
	Button regeist;
	OnClickListener regeistListener;
	OnClickListener submitListener;
	Long startTime = 0l;// ����ʼʱ��
	Long endTime = 0l;// �������ʱ��
	TelephonyManager phoneMgr;
	String text_username;
	String text_password;
	String text_tel;

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
		this.text_tel = phoneMgr.getLine1Number();
		this.startTime = System.currentTimeMillis();
		this.loading_text = (TextView) LoadingActivity.this
				.findViewById(R.id.activity_loading_text);
		this.image = (ImageView) LoadingActivity.this
				.findViewById(R.id.activity_loading);
		this.user_tel = (EditText) this
				.findViewById(R.id.activity_loading_user);
		this.password = (EditText) this
				.findViewById(R.id.activity_loading_password);
		this.submit = (Button) this.findViewById(R.id.activity_loading_submit);
		this.regeist = (Button) this
				.findViewById(R.id.activity_loading_regeist);
		this.rotateAnimation = AnimationUtils.loadAnimation(
				LoadingActivity.this, R.anim.loading_animation);
		this.submitListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				text_username = user_tel.getText().toString();
				text_password = password.getText().toString();
				login();
			}
		};
		this.regeistListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				text_username = UUID.randomUUID().toString();
				text_password = text_username.substring(0, 6);
				UserBean bean = new UserBean();
				bean.setCreate_date(DateUtil.getCurrentDate());
				bean.setTel(text_tel);
				bean.setIntegral(0);
				bean.setStudy_time(0f);
				bean.setEmail(null);
				bean.setUsername(text_username);
				bean.setShowname(text_username);
				bean.setPassword(text_password);
				DataHelpUtil.saveBeanData("TT_USER", bean);
				login();

			}
		};
		this.readDB = new Runnable() {
			@Override
			public void run() {
				initDB();
				initInfo();

			}

		};
		new Thread(readDB).start();
		/*****/
		this.submit.setOnClickListener(submitListener);
		this.regeist.setOnClickListener(regeistListener);
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
			editor.putString("USERNAME", null);
			editor.putString("PASSWORD", null);
			editor.putString("TEL", null);
			editor.putString("WORDS_COUNT_PER_GROUP",
					DefaultSetting.WORDS_COUNT_PER_GROUP);
			editor.commit();
		}
	}

	private void initInfo() {
		// ��¼
		text_username = sp.getString("USERNAME", "");
		// ��ȡ��¼���û������룬����У�����Ϊ�����û������û�У�����Ϊ��һ�ε�¼
		if (!text_username.equals("")) {
			text_password = sp.getString("PASSWORD", "");
			login();
		}
		// û�洢�û���������Ϊ��һ�ε�¼
		checkDelayTime();

		Message msg = getMessage(RESULT_FIRST_USER);
		msg.sendToTarget();
	}

	private void login() {
		UserBean bean = null;
		try {
			bean = (UserBean) DataHelpUtil.getSingleBean(UserBean.class,
					SQLS.Loading_UserInfo, new String[] { text_username,
							text_password });
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (bean != null) {
			Log.d("DEBUG", "login success");

			editor = sp.edit();
			editor.putString("USERNAME", bean.getUsername());
			editor.putString("PASSWORD", bean.getPassword());
			editor.putString("SHOWNAME", bean.getShowname());
			editor.putString("EMAIL", bean.getEmail());
			editor.putString("TEL", bean.getTel());
			editor.putFloat("STUDY_TIME", bean.getStudy_time());
			editor.putString("CREATE_DATE", bean.getCreate_date());
			editor.commit();
			checkDelayTime();
			loginSuccess();
			return;
		} else {
			loginFail();
		}
	}

	private void loginFail() {
		Message msg = getMessage(RESULT_CANCELED);
		msg.sendToTarget();
	}

	private void loginSuccess() {
		Message msg = getMessage(RESULT_OK);
		msg.sendToTarget();
	}

	private void checkDelayTime() {
		endTime = System.currentTimeMillis();
		if (endTime - startTime < loadTime) {
			try {
				Thread.sleep(loadTime - (endTime - startTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case RESULT_OK:
			Intent intent = new Intent();
			intent.setClass(LoadingActivity.this, MainActivity.class);
			LoadingActivity.this.startActivity(intent);
			LoadingActivity.this.finish();
			break;
		case RESULT_CANCELED:
			// �û����������
			Toast.makeText(this, R.string.activity_loading_waring,
					Toast.LENGTH_SHORT).show();
			password.getText().clear();
			password.requestFocus();
			break;
		case RESULT_FIRST_USER:
			rotateAnimation.cancel();
			image.setVisibility(View.INVISIBLE);
			loading_text.setVisibility(View.INVISIBLE);
			break;
		}
		return true;
	}

}
