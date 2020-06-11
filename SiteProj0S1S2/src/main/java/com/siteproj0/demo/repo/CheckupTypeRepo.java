package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.CheckupTypeDbModel;

public interface CheckupTypeRepo extends CrudRepository<CheckupTypeDbModel, Integer> {

	List<CheckupTypeDbModel> findByClinicIdAndEnabled(Integer clinicId, boolean enabled);

	CheckupTypeDbModel findByIdAndClinicId(Integer ctId, Integer clinicId);

	CheckupTypeDbModel findByNameAndPriceAndClinicIdAndEnabled(String name, Integer price, Integer clinicId, boolean enabled);

	CheckupTypeDbModel findByIdAndClinicIdAndEnabled(Integer ctId, Integer clinicId, boolean b);
	
}
