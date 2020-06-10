package com.siteproj0.demo.dal;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="Clinic")
public class ClinicDbModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	
	@OneToMany(mappedBy = "clinic")
	private List<DoctorDbModel> doctors;
	
	@OneToMany(mappedBy = "clinic")
	private List<ClinicAdminDbModel> clinicAdmins;
	
	@OneToMany(mappedBy = "clinic")
	private List<RoomDbModel> rooms;
	
	private String name;	
	
	private String address;
	
	private String description;
	
	private float rating;
	
	private float price;
	
	private String room;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public List<DoctorDbModel> getDoctors() {
		return doctors;
	}
	public void setDoctors(List<DoctorDbModel> doctors) {
		this.doctors = doctors;
	}
	public List<ClinicAdminDbModel> getClinicAdmins() {
		return clinicAdmins;
	}
	public void setClinicAdmins(List<ClinicAdminDbModel> clinicAdmins) {
		this.clinicAdmins = clinicAdmins;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<RoomDbModel> getRooms() {
		return rooms;
	}
	public void setRooms(List<RoomDbModel> rooms) {
		this.rooms = rooms;
	}
	
}
	

