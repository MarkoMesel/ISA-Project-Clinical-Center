package com.siteproj0.demo.clinic;

public class ClinicResponseModel {
	public ClinicResponseModel(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}
	
	private String name;
	private String address;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
