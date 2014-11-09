package com.zhu.ttwords.bean;


public class RepertoryBean extends AbstractCommonBean{
	String table;
	int wid;
	String uid;
	int count_right;
	int count_wrong;
	String status;
	String create_date;
	String update_date;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getWid() {
		return wid;
	}

	public void setWid(int wid) {
		this.wid = wid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getCount_right() {
		return count_right;
	}

	public void setCount_right(int count_right) {
		this.count_right = count_right;
	}

	public int getCount_wrong() {
		return count_wrong;
	}

	public void setCount_wrong(int count_wrong) {
		this.count_wrong = count_wrong;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

}
