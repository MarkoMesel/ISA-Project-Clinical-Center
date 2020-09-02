package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.ClinicRatingDbModel;
import com.siteproj0.demo.dal.DoctorRatingDbModel;

public interface DoctorRatingRepo extends CrudRepository<DoctorRatingDbModel, Integer> {

	List<DoctorRatingDbModel> findByDoctorId(int id);
	
}
