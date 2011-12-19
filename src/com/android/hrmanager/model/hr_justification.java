package com.android.hrmanager.model;

public class hr_justification {
	private int codJust;
	private String date;
	private String reason;
	private String description;
	private hr_user user;
	
	public hr_user getUser() {
		return user;
	}
	public void setUser(hr_user user) {
		this.user = user;
	}
	
	public int getCodJust() {
		return codJust;
	}
	public void setCodJust(int codJust) {
		this.codJust = codJust;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}		
}
