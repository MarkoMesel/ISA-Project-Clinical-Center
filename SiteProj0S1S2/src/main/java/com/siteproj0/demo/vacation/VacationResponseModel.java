package com.siteproj0.demo.vacation;

public class VacationResponseModel {
	public VacationResponseModel(int id, String doctor, String type, String startDate, String endDate) {
		super();
		this.id = id;
		this.doctor = doctor;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	private int id;
	private String doctor;
	private String type;
	private String startDate;
	private String endDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDoctor() {
		return doctor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
