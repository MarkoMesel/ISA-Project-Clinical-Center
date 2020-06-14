package com.siteproj0.demo.medicalcheckup;

public class MedicalCheckupRoomResponseModel {
	
	public MedicalCheckupRoomResponseModel(int id, String date, String time, String duration, float price,
			String checkupType, String doctor, String patient, String notes) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.price = price;
		this.checkupType = checkupType;
		this.doctor = doctor;
		this.patient = patient;
		this.notes = notes;
	}
	
	private int id;
	private String date;
	private String time;
	private String duration;
	private float price;
	private String checkupType;
	private String doctor;
	private String patient;
	private String notes;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getCheckupType() {
		return checkupType;
	}
	public void setCheckupType(String checkupType) {
		this.checkupType = checkupType;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getPatient() {
		return patient;
	}
	public void setPatient(String patient) {
		this.patient = patient;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
