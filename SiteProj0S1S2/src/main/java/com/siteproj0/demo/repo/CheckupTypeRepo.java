package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.CheckupTypeDbModel;

public interface CheckupTypeRepo extends CrudRepository<CheckupTypeDbModel, Integer> {

	List<CheckupTypeDbModel> findByClinicIdAndEnabled(Integer clinicId, boolean enabled);
	
}
