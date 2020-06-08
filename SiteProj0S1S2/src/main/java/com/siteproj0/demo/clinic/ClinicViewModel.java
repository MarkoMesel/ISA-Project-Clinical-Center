package com.siteproj0.demo.clinic;

import com.siteproj0.demo.dal.ClinicDbModel;

public class ClinicViewModel {
	
	public ClinicViewModel(int id, String address, String description, String name, float rating, float price) {
		super();
		this.id = id;
		this.address = address;
		this.description = description;
		this.name = name;
		this.rating = rating;
		this.price = price;
	}
	
	public ClinicViewModel(ClinicDbModel dbModel) {
		super();
		this.id = dbModel.getId();
		this.name = dbModel.getName();
		this.rating = dbModel.getRating();
		this.price = dbModel.getPrice();
		this.address = dbModel.getAddress();
		this.description = dbModel.getDescription();
	}
	
	public ClinicViewModel() {
		super();
	}
	
	private int id;
	private String address;
	private String description;
	private String name;	
	private float rating;
	private float price;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
