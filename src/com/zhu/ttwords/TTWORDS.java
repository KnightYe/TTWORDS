package com.zhu.ttwords;

import android.app.Application;

import com.zhu.ttwords.common.MessageHandler;

public class TTWORDS extends Application {

	private static MessageHandler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new MessageHandler();
	}

	public static MessageHandler getHandler() {
		return handler;
	}

}
