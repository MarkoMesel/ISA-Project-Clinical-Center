package com.siteproj0.demo.dal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name="Appointment")
public class AppointmentDbModel {

	public AppointmentDbModel() {
		super();
	}
	
	public AppointmentDbModel(Date date, ScheduleDbModel schedule, UserDbModel patient, Float price,
			Float discount, boolean confirmed) {
		super();
		this.date = date;
		this.schedule = schedule;
		this.patient = patient;
		this.price = price;
		this.discount = discount;
		this.confirmed = confirmed;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SCHEDULE_ID")
	private ScheduleDbModel schedule;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID")
	private UserDbModel patient;
	
	private Float price;
	
	private Float discount;
	
	private boolean confirmed;
	
	public int getId() {
		return id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
	
	public Float getPrice() {
		return price;
	}
	
	public void setPrice(Float price) {
		this.price = price;
	}
	
	public Float getDiscount() {
		return discount;
	}
	
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	public ScheduleDbModel getSchedule() {
		return schedule;
	}
	public UserDbModel getPatient() {
		return patient;
	}

	public void setPatient(UserDbModel patient) {
		this.patient = patient;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
}

	

