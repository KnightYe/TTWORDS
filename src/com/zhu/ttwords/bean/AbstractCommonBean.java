package com.zhu.ttwords.bean;

import android.database.Cursor;

public abstract class AbstractCommonBean {
	public abstract AbstractCommonBean buildFromCursor(Cursor cur);
}
