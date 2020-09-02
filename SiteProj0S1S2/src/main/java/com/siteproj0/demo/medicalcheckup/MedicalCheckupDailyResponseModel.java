package com.siteproj0.demo.medicalcheckup;

public class MedicalCheckupDailyResponseModel {
	
	public MedicalCheckupDailyResponseModel(String date, Integer count) {
		super();
		this.date = date;
		this.count = count;
	}
	
	private String date;
	private Integer count;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
}
