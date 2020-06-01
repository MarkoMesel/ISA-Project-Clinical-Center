package com.siteproj0.demo.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.ClinicAdminDbModel;

public interface ClinicAdminRepo extends CrudRepository<ClinicAdminDbModel, Integer> {
	ClinicAdminDbModel findByValidationToken(UUID token);
	ClinicAdminDbModel findByEmailAndPassword(String email, String password);
	ClinicAdminDbModel findBySecurityToken(UUID securityToken);
}
