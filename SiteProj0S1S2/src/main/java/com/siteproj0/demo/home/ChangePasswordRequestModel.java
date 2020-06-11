package com.siteproj0.demo.home;

import javax.validation.constraints.NotEmpty;

import validations.ValidPassword;

public class ChangePasswordRequestModel {
	@NotEmpty
	@ValidPassword
	private String oldPassword;
	
	@NotEmpty
	@ValidPassword
	private String password;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
