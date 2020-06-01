package com.siteproj0.demo.user;

import java.util.UUID;

public class LoginResponseModel {
	private UUID token;

	public LoginResponseModel(UUID token) {
		super();
		this.token = token;
	}
	
	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}
}
