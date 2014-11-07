package com.zhu.ttwords.activity;

import com.zhu.ttwords.TTWORDS;
import com.zhu.ttwords.common.ReceiveMessageListener;
import com.zhu.ttwords.value.WHAT;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;

public abstract class AbstractCommonActivity extends Activity implements
		ReceiveMessageListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void registerHandler(int key, ReceiveMessageListener listener) {
		TTWORDS.mReceiveMessageListenerMap.put(WHAT.LOADINGACTIVITY, this);
	}

	@Override
	public void onReceivedMessage(Message msg) {

	}

}
