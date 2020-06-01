package com.siteproj0.demo.appointment;

import java.util.Date;

public class AppointmentViewModel {
	
	public AppointmentViewModel(int id, Date date, String doctor, String clinic, Float price, Float discount) {
		super();
		this.id = id;
		this.date = date;
		this.doctor = doctor;
		this.clinic = clinic;
		this.price = price;
		this.discount = discount;
	}

	private int id;
	private Date date;	
	private String doctor;
	private String clinic;
	private Float price;
	private Float discount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}	
	public String getClinic() {
		return clinic;
	}
	public void setClinic(String clinic) {
		this.clinic = clinic;
	}		
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public Float getDiscount() {
		return discount;
	}
	
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
}
