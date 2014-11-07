package com.zhu.ttwords.bean;

import android.database.Cursor;

public class WordBean extends AbstractCommonBean {
	String wid;
	String content;
	String pos;
	String pronounce;
	String explain;

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPronounce() {
		return pronounce;
	}

	public void setPronounce(String pronounce) {
		this.pronounce = pronounce;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	@Override
	public AbstractCommonBean buildFromCursor(Cursor cur) {
		this.setWid(cur.getString(cur.getColumnIndex("WID")));
		this.setContent(cur.getString(cur.getColumnIndex("CONTENT")));
		this.setExplain(cur.getString(cur.getColumnIndex("EXPLAIN")));
		this.setPos(cur.getString(cur.getColumnIndex("POS")));
		this.setPronounce(cur.getString(cur.getColumnIndex("PRONOUNCE")));
		return this;
	}
}
