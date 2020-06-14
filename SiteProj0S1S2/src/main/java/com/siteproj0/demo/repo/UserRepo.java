package com.siteproj0.demo.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.UserDbModel;

public interface UserRepo extends CrudRepository<UserDbModel, Integer> {
	UserDbModel findByValidationToken(UUID token);
	UserDbModel findByEmailAndPassword(String email, String password);
	UserDbModel findBySecurityToken(UUID securityToken);
	List<UserDbModel> findByIsVerifiedAndClinicIdAndEnabled(boolean isVerified, Integer clinicId, boolean enabled);
	
	List<UserDbModel> findByFirstNameAndLastNameAndJmbgAndClinicIdAndEnabled(String firstName, String lastName,
			String jmbg, Integer clinicId, boolean enabled);
	
	List<UserDbModel> findByFirstNameAndClinicIdAndIsVerifiedAndEnabled(String findByThis, Integer clinicId, boolean isVerified,
			boolean isEnabled);
	List<UserDbModel> findByLastNameAndClinicIdAndIsVerifiedAndEnabled(String findByThis, Integer clinicId, boolean isVerified,
			boolean isEnabled);
	List<UserDbModel> findByJmbgAndClinicIdAndIsVerifiedAndEnabled(String findByThis, Integer clinicId, boolean isVerified,
			boolean isEnabled);
}
