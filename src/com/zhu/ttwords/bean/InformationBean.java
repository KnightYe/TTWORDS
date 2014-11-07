package com.zhu.ttwords.bean;

import android.database.Cursor;

public class InformationBean extends AbstractCommonBean {

	String count;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public AbstractCommonBean buildFromCursor(Cursor cur) {
		this.setCount(cur.getString(cur.getColumnIndex("COUNT")));
		return this;
	}
}
