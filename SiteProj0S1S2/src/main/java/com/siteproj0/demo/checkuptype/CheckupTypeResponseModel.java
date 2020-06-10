package com.siteproj0.demo.checkuptype;

public class CheckupTypeResponseModel {
	public CheckupTypeResponseModel(int id, String name, int price, int clinicId) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.clinicId = clinicId;
	}
	
	private int id;
	private String name;
	private int price;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getClinicId() {
		return clinicId;
	}
	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}
	
	
}
