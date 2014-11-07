package com.zhu.ttwords.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.zhu.ttwords.R;
import com.zhu.ttwords.adapter.ViewPagerAdapter;
import com.zhu.ttwords.adapter.ViewPagerAdapter.ViewHolder;
import com.zhu.ttwords.bean.AbstractCommonBean;
import com.zhu.ttwords.bean.WordBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.value.DefaultSetting;

public class StudyActivity extends Activity {

	public static final int LEARN = 0;
	public static final int TEST = 1;
	public static final int RIGHT = 2;
	public static final int WRONG = 3;
	public static final int ATTENTION = 4;

	SharedPreferences sp;
	ViewPager viewPager;
	ViewPagerAdapter adapter;
	List<AbstractCommonBean> mData;
	int currentPosition;
	int currentStatus;
	ViewHolder currentHolder;
	Cursor cursor;
	String group;// 学习一组单词有多少个
	String totle;// 已经学习单词
	String sql = "select * from TT_RESOURCE_JP LIMIT ?,?";
	String params[] = new String[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		init();
	}

	private void init() {
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		group = sp.getString("WORDS_COUNT_PER_GROUP",
				DefaultSetting.WORDS_COUNT_PER_GROUP);
		totle = sp.getString("TOTLE", "0");
		params[0] = totle;
		params[1] = group;
		try {
			mData = DataHelpUtil.getDataBean(WordBean.class, sql, params);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		adapter = new ViewPagerAdapter(this, mData);
		viewPager = (ViewPager) this
				.findViewById(R.id.activity_study_viewpager);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(adapter);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
			adapter.checkWord();
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

}
