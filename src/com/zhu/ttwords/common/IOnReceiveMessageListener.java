package com.zhu.ttwords.common;

import android.os.Message;

public abstract interface IOnReceiveMessageListener {

	public abstract int getMessageWHAT();

	public abstract void onReceivedMessage(Message msg);
}
