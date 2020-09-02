package com.siteproj0.demo.dal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name="Doctorrating")
public class DoctorRatingDbModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private UserDbModel user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOCTOR_ID")
	private DoctorDbModel doctor;
	
	private float rating;
	
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserDbModel getUser() {
		return user;
	}

	public void setUser(UserDbModel user) {
		this.user = user;
	}

	public DoctorDbModel getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorDbModel doctor) {
		this.doctor = doctor;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
