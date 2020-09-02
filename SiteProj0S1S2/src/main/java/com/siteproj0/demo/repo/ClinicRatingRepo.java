package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.ClinicRatingDbModel;

public interface ClinicRatingRepo extends CrudRepository<ClinicRatingDbModel, Integer> {

	List<ClinicRatingDbModel> findByClinicId(int id);
	
}
