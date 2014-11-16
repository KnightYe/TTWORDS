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
	Runnable readDB;// 读取数据库线程
	Animation rotateAnimation;
	ImageView image;// 载入图片
	TextView loading_text;// 状态说明文字
	EditText user_tel;
	EditText password;
	Button submit;
	Button regeist;
	OnClickListener regeistListener;
	OnClickListener submitListener;
	Long startTime = 0l;// 程序开始时间
	Long endTime = 0l;// 载入完毕时间
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
	 * 判断数据库是否存在？存在的话:不存在的话写入数据库
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
	 * 检查数据库路径、检查数据库文件、打开数据库、检查数据库版本**** 初始化默认设置
	 */
	private void initDB() {
		boolean hasDir = DataHelpUtil.initDatabaseDir(this);
		boolean hasDB = DataHelpUtil.initDatabaseFile(this);
		boolean isNewVer = DataHelpUtil.initDatabaseVersion(this);
		// 如果没有DB，没有DB目录，则认为是新安装。
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
		// 登录
		text_username = sp.getString("USERNAME", "");
		// 获取记录的用户名密码，如果有，则认为是老用户，如果没有，就认为第一次登录
		if (!text_username.equals("")) {
			text_password = sp.getString("PASSWORD", "");
			login();
		}
		// 没存储用户名，可能为第一次登录
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
			// 用户名密码错误
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
