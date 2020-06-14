package com.siteproj0.demo.user;

public class UserResponseModel {

	public UserResponseModel(int id, String firstName, String lastName, String jmbg, String country, String city, String street,
			String email, String phone, int clinicId) {
		super();
		this.id = id; 
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.city = city;
		this.street = street;
		this.phone = phone;
		this.jmbg = jmbg;
		this.email = email;
		this.clinicId = clinicId;
	}
	
	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private String country; 
	
	private String city;
	
	private String street;
	
	private String phone;	

	private String jmbg;	

	private String email;
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone= phone;
	}
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getClinicId() {
		return clinicId;
	}
	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}
}
