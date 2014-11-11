package com.zhu.ttwords.activity;

import android.app.Activity;
import android.os.Message;

import com.zhu.ttwords.common.IOnReceiveMessageListener;
import com.zhu.ttwords.common.MessageHandler;

public abstract class AbstractCommonActivity extends Activity implements
		IOnReceiveMessageListener {

	private MessageHandler handler;

	MessageHandler getHandler() {
		return handler;
	}

	void setHandler(MessageHandler handler) {
		this.handler = handler;
	}

	/**
	 * 如果使用handler需要覆盖此方法
	 */
	@Override
	public int getMessageWHAT() {
		return 0;
	}

	/**
	 * 如果使用handler需要覆盖此方法
	 */
	@Override
	public void onReceivedMessage(Message msg) {

	}

}
