package com.zhu.ttwords.common;

import android.os.Message;

public abstract interface ReceiveMessageListener {
	public abstract void onReceivedMessage(Message msg);
}
