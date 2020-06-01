package com.siteproj0.demo.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.AppointmentDbModel;

public interface AppointmentRepo extends CrudRepository<AppointmentDbModel, Integer> {
	List<AppointmentDbModel> findByPatientId(Integer patientId);
}
