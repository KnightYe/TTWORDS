package com.zhu.ttwords.bean;

public class RepertoryBean extends AbstractCommonBean {
	String tname;
	int wid;
	String uid;
	int countw;
	int countall;
	int countn;
	int level;
	float ef;
	String create_date;
	String update_date;

	public int getCountw() {
		return countw;
	}

	public void setCountw(int countw) {
		this.countw = countw;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
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

	public int getCountall() {
		return countall;
	}

	public void setCountall(int countall) {
		this.countall = countall;
	}

	public int getCountn() {
		return countn;
	}

	public void setCountn(int countn) {
		this.countn = countn;
	}

	public float getEf() {
		return ef;
	}

	public void setEf(float ef) {
		this.ef = ef;
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
