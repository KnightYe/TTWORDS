package com.zhu.ttwords.activity;

import android.app.Activity;
import android.os.Message;

import com.zhu.ttwords.common.IOnReceiveMessageListener;
import com.zhu.ttwords.common.MessageHandler;

public abstract class AbstractCommonActivity extends Activity implements
		IOnReceiveMessageListener {

	private MessageHandler handler;

	public MessageHandler getHandler() {
		return handler;
	}

	public void setHandler(MessageHandler handler) {
		this.handler = handler;
	}

	/**
	 * ���ʹ��handler��Ҫ���Ǵ˷���
	 */
	@Override
	public int getMessageWHAT() {
		return 0;
	}

	/**
	 * ���ʹ��handler��Ҫ���Ǵ˷���
	 */
	@Override
	public void onReceivedMessage(Message msg) {

	}

}
