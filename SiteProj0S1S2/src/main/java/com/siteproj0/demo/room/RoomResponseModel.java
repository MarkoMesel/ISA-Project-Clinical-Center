package com.siteproj0.demo.room;

public class RoomResponseModel {
	public RoomResponseModel(int id, String name, String number, int clinicId) {
		super();
		this.id = id;
		this.name = name;
		this.number = number;
		this.clinicId = clinicId;
	}
	private int id;
	private String name;
	private String number;
	private int clinicId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public int getClinicId() {
		return clinicId;
	}
	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}
	
	
}
