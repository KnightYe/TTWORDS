package com.zhu.ttwords.bean;

import android.database.Cursor;

public class RepertoryBean extends AbstractCommonBean {
	String table;
	int wId;
	String uId;
	int right;
	int wrong;
	String status;
	String create_time;
	String update_time;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getwId() {
		return wId;
	}

	public void setwId(int wId) {
		this.wId = wId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getWrong() {
		return wrong;
	}

	public void setWrong(int wrong) {
		this.wrong = wrong;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	@Override
	public AbstractCommonBean buildFromCursor(Cursor cur) {
		this.setTable(cur.getString(cur.getColumnIndex("TABLE_NAME")));
		this.setwId(cur.getInt(cur.getColumnIndex("WID")));
		this.setuId(cur.getString(cur.getColumnIndex("UID")));
		this.setRight(cur.getInt(cur.getColumnIndex("COUNT_RIGHT")));
		this.setWrong(cur.getInt(cur.getColumnIndex("COUNT_WRONG")));
		this.setStatus(cur.getString(cur.getColumnIndex("STATUS")));
		this.setCreate_time(cur.getString(cur.getColumnIndex("CREATE_DATE")));
		this.setUpdate_time(cur.getString(cur.getColumnIndex("IPDATE_DATE")));
		return this;
	}
}
