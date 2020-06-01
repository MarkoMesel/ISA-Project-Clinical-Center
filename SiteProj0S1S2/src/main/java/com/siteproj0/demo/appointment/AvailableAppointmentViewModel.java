package com.siteproj0.demo.appointment;

import java.util.Date;

public class AvailableAppointmentViewModel {

	public AvailableAppointmentViewModel(Date date, String doctorName, int scheduleId, String clinic, float price) {
		super();
		this.date = date;
		this.doctorName = doctorName;
		this.scheduleId = scheduleId;
		this.clinic = clinic;
		this.price = price;
	}

	private Date date;	
	private String doctorName;
	private int scheduleId;
	private String clinic;
	private float price;

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public int getscheduleId() {
		return scheduleId;
	}

	public void setscheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
}
