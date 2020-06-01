package com.siteproj0.demo.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.UserDbModel;

public interface UserRepo extends CrudRepository<UserDbModel, Integer> {
	UserDbModel findByValidationToken(UUID token);
	UserDbModel findByEmailAndPassword(String email, String password);
	UserDbModel findBySecurityToken(UUID securityToken);
}
