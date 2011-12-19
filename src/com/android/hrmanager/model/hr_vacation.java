package com.android.hrmanager.model;

public class hr_vacation {
	private int codhol;
	private String date;
	private String period;
	private hr_user user;
	
	public hr_user getUser() {
		return user;
	}
	
	public void setUser(hr_user user) {
		this.user = user;
	}
	
	public int getCodhol() {
		return codhol;
	}
	public void setCodhol(int codhol) {
		this.codhol = codhol;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
}
