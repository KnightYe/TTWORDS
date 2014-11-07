package com.zhu.ttwords;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

import com.zhu.ttwords.common.ReceiveMessageListener;
import com.zhu.ttwords.value.WHAT;

public class TTWORDS extends Application {

	private static Handler handler;
	public static SparseArray<ReceiveMessageListener> mReceiveMessageListenerMap = new SparseArray<ReceiveMessageListener>();

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new TTWORDSHandler();
	}

	public static Handler getHandler() {
		return handler;
	}

	public static void setHandler(Handler handler) {
		TTWORDS.handler = handler;
	}

	private static class TTWORDSHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT.LOADINGACTIVITY:
				mReceiveMessageListenerMap.get(msg.what).onReceivedMessage(msg);
				break;
			}
		}
	}
}
