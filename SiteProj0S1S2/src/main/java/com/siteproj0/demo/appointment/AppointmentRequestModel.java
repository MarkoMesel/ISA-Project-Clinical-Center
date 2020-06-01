package com.siteproj0.demo.appointment;

import java.util.Date;

public class AppointmentRequestModel {
	private int scheduleId;
	private Date date;
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
