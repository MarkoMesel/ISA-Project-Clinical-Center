package com.siteproj0.demo.testrepo;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;

class TestClinicAdminRepo extends TestRepo {

	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@Test
	void findBySecurityToken_ReturnObj() {
		UUID securityToken = UUID.fromString("2D1EBC5B-7D27-4197-9CF0-E84451C5BBB4");
		ClinicAdminDbModel result = clinicAdminRepo.findBySecurityToken(securityToken);
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("securityToken", is(securityToken)));
	}
	
	@Test
	void findByEmailAndPassword_ReturnObj() {
		ClinicAdminDbModel result = clinicAdminRepo.findByEmailAndPassword("abab@gmail.com", "abcde123");
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("email", is("abab@gmail.com")),
			hasProperty("password", is("abcde123"))
		));
	}

}
