package com.siteproj0.demo.repo;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.siteproj0.demo.dal.AppointmentDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;

public interface DoctorRepo extends CrudRepository<DoctorDbModel, Integer> {
	DoctorDbModel findByValidationToken(UUID token);
	DoctorDbModel findByEmailAndPassword(String email, String password);
	DoctorDbModel findBySecurityToken(UUID securityToken);
	List<DoctorDbModel> findByClinicIdAndEnabled(Integer clinicId, boolean enabled);
	List<DoctorDbModel> findByFirstNameAndLastNameAndRatingAndClinicIdAndEnabled(String firstName,
																		String lastName,
																		float rating,
																		Integer clinicId,
																		boolean enabled);
	List<DoctorDbModel> findByIdAndClinicIdAndEnabled(Integer id, Integer clinicId, boolean b);
}
