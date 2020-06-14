package com.siteproj0.demo.dal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Entity
@Table(name="Medicalcheckup")
public class MedicalCheckupDbModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty
	private String date;
	
	@NotEmpty
	private String time;
	
	@NotEmpty
	private String duration;
	
	@NotNull
	private float price;
	
	private boolean free;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CHECKUPTYPE_ID")
	private CheckupTypeDbModel checkupType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CLINIC_ID")
	private ClinicDbModel clinic;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ROOM_ID")
	private RoomDbModel room;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOCTOR_ID")
	private DoctorDbModel doctor;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PATIENT_ID")
	private UserDbModel patient;
	
	private String notes;
	
	private String endNotes;
	
	private boolean finished;

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

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public CheckupTypeDbModel getCheckupType() {
		return checkupType;
	}

	public void setCheckupType(CheckupTypeDbModel checkupType) {
		this.checkupType = checkupType;
	}
	
	public ClinicDbModel getClinic() {
		return clinic;
	}

	public void setClinic(ClinicDbModel clinic) {
		this.clinic = clinic;
	}

	public RoomDbModel getRoom() {
		return room;
	}

	public void setRoom(RoomDbModel room) {
		this.room = room;
	}

	public DoctorDbModel getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorDbModel doctor) {
		this.doctor = doctor;
	}

	public UserDbModel getPatient() {
		return patient;
	}

	public void setPatient(UserDbModel patient) {
		this.patient = patient;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getEndNotes() {
		return endNotes;
	}

	public void setEndNotes(String endNotes) {
		this.endNotes = endNotes;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
}
