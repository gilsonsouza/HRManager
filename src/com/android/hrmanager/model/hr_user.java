package com.android.hrmanager.model;

public class hr_user {
	private int codUser;
	private String document;
	private String name;
	private String function;
	private String sector;
	private hr_user supervisor;
	private String login;
	private String password;
	
	public int getCodUser() {
		return codUser;
	}
	public void setCodUser(int codUser) {
		this.codUser = codUser;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public hr_user getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(hr_user supervisor) {
		this.supervisor = supervisor;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
}
