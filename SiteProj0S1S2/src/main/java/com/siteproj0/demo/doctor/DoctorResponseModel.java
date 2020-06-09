package com.siteproj0.demo.doctor;

public class DoctorResponseModel {
	
	public DoctorResponseModel(int id, String firstName, String lastName, String jmbg, String country, String city,
			String street, String email, String phone, float rating, String shiftStart, String shiftEnd, int clinicId) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jmbg = jmbg;
		this.country = country;
		this.city = city;
		this.street = street;
		this.email = email;
		this.phone = phone;
		this.rating = rating;
		this.shiftStart = shiftStart;
		this.shiftEnd = shiftEnd;
		this.clinicId = clinicId;
	}
	
	private int id;
	private String firstName;
	private String lastName;
	private String jmbg;
	private String country;
	private String city;
	private String street;
	private String email;
	private String phone;
	private float rating;
	private String shiftStart;
	private String shiftEnd;
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
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getShiftStart() {
		return shiftStart;
	}
	public void setShiftStart(String shiftStart) {
		this.shiftStart = shiftStart;
	}
	public String getShiftEnd() {
		return shiftEnd;
	}
	public void setShiftEnd(String shiftEnd) {
		this.shiftEnd = shiftEnd;
	}
	public int getClinicId() {
		return clinicId;
	}
	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}
	
	
}
