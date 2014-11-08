package com.zhu.ttwords.common;

import android.os.Handler;
import android.os.Message;

public class MessageHandler extends Handler {
	IOnReceiveMessageListener iOnReceiveMessageListener;
 
	public IOnReceiveMessageListener getiOnReceiveMessageListener() {
		return iOnReceiveMessageListener;
	}

	public void setiOnReceiveMessageListener(
			IOnReceiveMessageListener iOnReceiveMessageListener) {
		this.iOnReceiveMessageListener = iOnReceiveMessageListener;
	}

	@Override
	public void handleMessage(Message msg) {
		if (msg.what == iOnReceiveMessageListener.getMessageWHAT()) {
			iOnReceiveMessageListener.onReceivedMessage(msg);
		}
	}

}
