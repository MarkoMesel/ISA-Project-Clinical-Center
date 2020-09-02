package com.siteproj0.demo.doctor;

public class DoctorRatingResponseModel {
	
	public DoctorRatingResponseModel(int id, String firstName, String lastName, float rating, int clinicId) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rating = rating;
		this.clinicId = clinicId;
	}
	
	private int id;
	private String firstName;
	private String lastName;
	private float rating;
	private int clinicId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public int getClinicId() {
		return clinicId;
	}
	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}
	
	
}
