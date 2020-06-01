package com.siteproj0.demo.user;

import javax.validation.constraints.NotEmpty;

public class LoginModel {
	public LoginModel(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	public LoginModel() {
		super();
	}
	
	private String email;
	
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
