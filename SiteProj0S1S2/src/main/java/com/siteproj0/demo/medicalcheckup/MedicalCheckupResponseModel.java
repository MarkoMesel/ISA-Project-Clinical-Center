package com.siteproj0.demo.medicalcheckup;

public class MedicalCheckupResponseModel {
	
	public MedicalCheckupResponseModel(int id, String date, String time, String duration, int price, int ctId,
			int clinicId, int roomId, int doctorId) {
		super();
		this.id = id;
		this.date = date;
		this.time = time;
		this.duration = duration;
		this.price = price;
		this.ctId = ctId;
		this.clinicId = clinicId;
		this.roomId = roomId;
		this.doctorId = doctorId;
	}
	
	private int id;
	private String date;
	private String time;
	private String duration;
	private float price;
	private int ctId;
	private int clinicId;
	private int roomId;
	private int doctorId;
	
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
	public int getCtId() {
		return ctId;
	}
	public void setCtId(int ctId) {
		this.ctId = ctId;
	}
	public int getClinicId() {
		return clinicId;
	}
	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	
	
}
