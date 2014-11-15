package com.zhu.ttwords.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhu.ttwords.R;
import com.zhu.ttwords.adapter.StudyAdapter;
import com.zhu.ttwords.adapter.TestAdapter;
import com.zhu.ttwords.bean.AbstractCommonBean;
import com.zhu.ttwords.bean.WordBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.util.DateUtil;
import com.zhu.ttwords.value.DefaultSetting;
import com.zhu.ttwords.value.SQLS;

public class StudyActivity extends AbstractCommonActivity {

	ImageView mao;
	ViewPager viewPager;
	StudyAdapter studyAdapter;
	TestAdapter testAdapter;
	TextView index_total;
	TextView showMode;
	Dialog testComplateDialog;
	Dialog exitDialog;
	Dialog studyComplateDialog;

	List<AbstractCommonBean> mData;
	SharedPreferences sp;
	OnClickListener listener;
	int index = 1;// viewpager当前显示页面序号。从1开始。
	boolean isStudyNotTest = true;
	boolean isReviewNotStudy = true;
	String username;
	String group;// 学习一组单词有多少个
	String params[] = new String[2];
	int test_right;
	int test_total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		initData();
		initUI();
	}

	private void initData() {
		// 初始化数据
		Intent intent = getIntent();
		String mission = intent.getStringExtra("MISSION");
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("USERNAME", null);
		if (!mission.equals("0")) {
			// 复习模式数据
			isReviewNotStudy = true;
			params[0] = username;
			params[1] = DateUtil.getCurrentDate();
			try {
				mData = DataHelpUtil.getDataBean(WordBean.class,
						SQLS.Study_ReviewWords, params);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			studyAdapter = new StudyAdapter(this, mData, this);
		} else {
			// 学习模式数据
			isReviewNotStudy = false;
			group = sp.getString("WORDS_COUNT_PER_GROUP",
					DefaultSetting.WORDS_COUNT_PER_GROUP);
			params[0] = username;
			params[1] = group;
			try {
				mData = DataHelpUtil.getDataBean(WordBean.class,
						SQLS.Study_StudyWords, params);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			studyAdapter = new StudyAdapter(this, mData, this);
		}
	}

	private void initUI() {
		// 初始化UI
		index_total = (TextView) this
				.findViewById(R.id.activity_study_index_total);
		index_total.setText(index + "/" + mData.size());
		showMode = (TextView) this.findViewById(R.id.activity_study_showmode);
		showMode.setText("学习模式");

		mao = (ImageView) this.findViewById(R.id.activity_study_mao);
		viewPager = (ViewPager) this
				.findViewById(R.id.activity_study_viewpager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(studyAdapter);
		viewPager.setOnPageChangeListener(studyAdapter);
		exitDialog = new AlertDialog.Builder(this).setTitle("退出本次学习")
				.setMessage("完成学习了吗？")
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StudyActivity.this.finish();
					}
				}).setNegativeButton("继续学习", null).create();

		studyComplateDialog = new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle("：完成学习？")
				.setItems(new String[] { "·开始测试", "·完成学习", "·再看一会" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									testMode();
									break;
								case 1:
									StudyActivity.this.finish();
									break;
								case 2:
									dialog.dismiss();
									break;
								}
							}
						}).create();
		listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isStudyNotTest) {
					studyComplateDialog.show();
				} else {
					test_right = testAdapter.testRight;
					test_total = mData.size();
					testComplateDialog = new AlertDialog.Builder(
							StudyActivity.this)
							.setIcon(R.drawable.ic_launcher)
							.setTitle("：完成测试？")
							.setMessage(
									"正确数："
											+ test_right
											+ "\n错误数："
											+ (test_total - test_right)
											+ "\n正确率："
											+ (test_right * 1.0 / test_total * 1.0)
											+ "%")
							.setPositiveButton("完成测试",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											StudyActivity.this.finish();
										}
									})
							.setNeutralButton("错词重测",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											initData();
											testMode();
										}
									}).setNegativeButton("让我改改", null).create();
					testComplateDialog.show();
				}
			}
		};
		mao.setOnClickListener(listener);
	}

	private void testMode() {
		testAdapter = new TestAdapter(StudyActivity.this, mData,
				StudyActivity.this);
		viewPager.setAdapter(testAdapter);
		viewPager.setOnPageChangeListener(testAdapter);
		index_total.setText(1 + "/" + mData.size());
		showMode.setText("测验模式");
		isStudyNotTest = false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			exitDialog.show();
			break;
		}
		return true;
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case RESULT_FIRST_USER:
			index = msg.arg1;
			index_total.setText(index + "/" + mData.size());
			break;
		case RESULT_OK:
			if (isStudyNotTest) {
				studyComplateDialog.show();
			} else {
				testComplateDialog.show();
			}
		}
		return false;
	}
}
