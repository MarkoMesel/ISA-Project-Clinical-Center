package com.siteproj0.demo.repo;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.ClinicDbModel;

public interface ClinicRepo extends CrudRepository<ClinicDbModel, Integer> {
	
}
