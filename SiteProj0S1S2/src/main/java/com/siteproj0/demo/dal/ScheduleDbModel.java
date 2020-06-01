package com.siteproj0.demo.dal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="Schedule")
public class ScheduleDbModel {

	public ScheduleDbModel() {
		super();
	}

	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int day;
	private int time;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOCTOR_ID")
	private DoctorDbModel doctor;
	
	@OneToMany(mappedBy = "schedule")
	private List<AppointmentDbModel> appointments;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<AppointmentDbModel> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentDbModel> appointments) {
		this.appointments = appointments;
	}

	public DoctorDbModel getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorDbModel doctor) {
		this.doctor = doctor;
	}
}

	

