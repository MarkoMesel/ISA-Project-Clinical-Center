package com.siteproj0.demo.dal;

import java.io.Console;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import validations.FieldMatch;
import validations.ValidPassword;
import javax.validation.constraints.NotEmpty;

@SuppressWarnings("unused")
@Entity
@Table(name="Doctor")
public class DoctorDbModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty
	private String role;
	
	@NotEmpty
	private String jmbg;
	
	@NotEmpty
	private String firstName;
	
	@NotEmpty
	private String lastName;
	
	@NotEmpty
	@ValidPassword
	private String password;

	private boolean isVerified;
	
	private UUID validationToken;
	
	private UUID securityToken;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	private String country; 
	
	@NotEmpty
	private String city;
	
	@NotEmpty
	private String street;
	
	@NotEmpty
	private String phone;
	
	private float rating;
	
	private String shiftStart;
	
	private String shiftEnd;
	
	private boolean firstLogin;
	
	@OneToMany(mappedBy = "doctor")
	private List<ScheduleDbModel> schedules;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CLINIC_ID")
	private ClinicDbModel clinic;
	
	private boolean enabled;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public UUID getValidationToken() {
		return validationToken;
	}

	public void setValidationToken(UUID validationToken) {
		this.validationToken = validationToken;
	}

	public UUID getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(UUID securityToken) {
		this.securityToken = securityToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		this.phone = phone;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public List<ScheduleDbModel> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<ScheduleDbModel> schedules) {
		this.schedules = schedules;
	}

	public ClinicDbModel getClinic() {
		return clinic;
	}

	public void setClinic(ClinicDbModel clinic) {
		this.clinic = clinic;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}
	
}
	

