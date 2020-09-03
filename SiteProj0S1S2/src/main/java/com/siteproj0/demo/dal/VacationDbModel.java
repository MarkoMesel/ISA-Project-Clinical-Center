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

@SuppressWarnings("unused")
@Entity
@Table(name="Vacation")
public class VacationDbModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty
	private String startDate;
	
	@NotEmpty
	private String endDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOCTOR_ID")
	private DoctorDbModel doctor;
	
	private boolean approved;
	
	private boolean enabled;

	private String type;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public DoctorDbModel getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorDbModel doctor) {
		this.doctor = doctor;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
