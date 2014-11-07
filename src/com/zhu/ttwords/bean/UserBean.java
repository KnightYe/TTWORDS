package com.zhu.ttwords.bean;

import android.database.Cursor;

public class UserBean extends AbstractCommonBean {

	String UID;
	String name;
	String password;
	String email;
	String phone;
	String integral;
	String study_time;
	String create_time;

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getStudy_time() {
		return study_time;
	}

	public void setStudy_time(String study_time) {
		this.study_time = study_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	@Override
	public AbstractCommonBean buildFromCursor(Cursor cur) {
		this.setUID(cur.getString(cur.getColumnIndex("UID")));
		this.setName(cur.getString(cur.getColumnIndex("UNAME")));
		this.setPassword(cur.getString(cur.getColumnIndex("PASSWORD")));
		this.setEmail(cur.getString(cur.getColumnIndex("EMAIL")));
		this.setPhone(cur.getString(cur.getColumnIndex("TEL")));
		this.setIntegral(cur.getString(cur.getColumnIndex("INTEGRAL")));
		this.setStudy_time(cur.getString(cur.getColumnIndex("STUDY_TIME")));
		this.setCreate_time(cur.getString(cur.getColumnIndex("CREATE_TIME")));
		return this;
	}

}
