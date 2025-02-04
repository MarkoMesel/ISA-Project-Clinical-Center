package com.siteproj0.demo.dal;

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
import validations.ValidPassword;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="User")
public class UserDbModel {
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
	
	@OneToMany(mappedBy = "patient")
	private List<AppointmentDbModel> appointments;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CLINIC_ID")
	private ClinicDbModel clinic;
	
	@OneToMany(mappedBy = "patient")
	private List<MedicalCheckupDbModel> medicalCheckups;
	
	private boolean enabled;
	
	private boolean firstLogin;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setVerified(Boolean isVerified) {
		this.isVerified = isVerified;
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
		this.phone= phone;
	}
	public boolean isVerified() {
		return isVerified;
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

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<AppointmentDbModel> getAppointments() {
		return appointments;
	}
	public void setAppointments(List<AppointmentDbModel> appointments) {
		this.appointments = appointments;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	public ClinicDbModel getClinic() {
		return clinic;
	}
	public void setClinic(ClinicDbModel clinic) {
		this.clinic = clinic;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public List<MedicalCheckupDbModel> getMedicalCheckups() {
		return medicalCheckups;
	}
	public void setMedicalCheckups(List<MedicalCheckupDbModel> medicalCheckups) {
		this.medicalCheckups = medicalCheckups;
	}
	public boolean isFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}
	
}
