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
	Handler handler;// 接受消息，发送消息用
	Runnable readDB;// 读取数据库线程
	Animation rotateAnimation;
	ImageView image;// 载入图片
	TextView text;// 状态说明文字
	Long startTime = 0l;// 程序开始时间
	Long endTime = 0l;// 载入完毕时间
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
	 * 判断数据库是否存在？存在的话:不存在的话写入数据库
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
	 * 检查数据库路径、检查数据库文件、打开数据库、检查数据库版本**** 初始化默认设置
	 */
	private void initDB() {
		boolean hasDir = DataHelpUtil.initDatabaseDir(this);
		boolean hasDB = DataHelpUtil.initDatabaseFile(this);
		boolean isNewVer = DataHelpUtil.initDatabaseVersion(this);
		// 如果没有DB，没有DB目录，则认为是新安装。
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
		// 登录
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
		// 登录成功则读取user数据库信息，存入配置文件
		// 已经学过的单词数。
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
