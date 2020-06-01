package com.siteproj0.demo.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.DoctorDbModel;

public interface DoctorRepo extends CrudRepository<DoctorDbModel, Integer> {
	DoctorDbModel findByValidationToken(UUID token);
	DoctorDbModel findByEmailAndPassword(String email, String password);
	DoctorDbModel findBySecurityToken(UUID securityToken);
}
