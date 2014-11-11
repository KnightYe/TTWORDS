package com.zhu.ttwords.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.zhu.ttwords.R;
import com.zhu.ttwords.adapter.ViewPagerAdapter;
import com.zhu.ttwords.bean.AbstractCommonBean;
import com.zhu.ttwords.bean.WordBean;
import com.zhu.ttwords.util.DataHelpUtil;
import com.zhu.ttwords.value.DefaultSetting;

public class StudyActivity extends Activity {

	ViewPager viewPager;
	ViewPagerAdapter adapter;
	List<AbstractCommonBean> mData;
	SharedPreferences sp;
	String username;
	String group;// 学习一组单词有多少个
	String sql = "select * from TT_RESOURCE_JP  WHERE WID NOT IN (SELECT WID FROM TT_REPERTORY_JP WHERE UID= ?) LIMIT ?,?";

	String params[] = new String[3];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		init();
	}

	private void init() {
		Intent intent = getIntent();
		sp = getSharedPreferences("setting", MODE_PRIVATE);
		username = sp.getString("USERNAME", null);
		group = sp.getString("WORDS_COUNT_PER_GROUP",
				DefaultSetting.WORDS_COUNT_PER_GROUP);
		params[0] = username;
		params[1] = "0";
		params[2] = group;
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
}
