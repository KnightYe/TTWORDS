package com.zhu.ttwords.activity;

import com.zhu.ttwords.common.IMessageFactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class AbstractCommonActivity extends Activity implements
		Handler.Callback, IMessageFactory {
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler(this);
	}

	public Message getMessage(int what) {
		return mHandler.obtainMessage(what);
	}
}
