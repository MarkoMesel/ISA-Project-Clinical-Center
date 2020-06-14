package com.siteproj0.demo.medicalcheckup;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MedicalCheckupRegisterModel {
	@Id
	@GeneratedValue
	private int id;
	
	@NotEmpty
	private String date;
	
	@NotEmpty
	private String time;
	
	@NotEmpty
	private String duration;
	
	@NotNull
	private float price;
	
	@NotNull
	private int checkupTypeId;
	
	@NotNull
	private int clinicId;
	
	@NotNull
	private int roomId;
	
	@NotNull
	private int doctorId;
	
	@NotEmpty
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

	public int getCheckupTypeId() {
		return checkupTypeId;
	}

	public void setCheckupTypeId(int checkupTypeId) {
		this.checkupTypeId = checkupTypeId;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
