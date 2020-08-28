package com.siteproj0.demo;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.repo.UserRepo;

@DataJpaTest
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
})
class TestUserRepo {

	@Autowired
	UserRepo userRepo;
	
	@Test
	@Sql("data.sql")
	void findById_ReturnObj() {
		UserDbModel result = userRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));	
	}
	
	@Test
	@Sql("data.sql")
	void findBySecurityToken_ReturnObj() {
		UUID securityToken = UUID.fromString("2D1EBC5B-7D27-4197-9CF0-E84451C5BBB1");
		UserDbModel result = userRepo.findBySecurityToken(securityToken);
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("securityToken", is(securityToken)));
	}
	
	@Test
	@Sql("data.sql")
	void findByEmailAndPassword_ReturnObj() {
		UserDbModel result = userRepo.findByEmailAndPassword("neznam@gmail.com", "neznamneznam");
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("email", is("neznam@gmail.com")),
			hasProperty("password", is("neznamneznam"))
		));
	}
	
	@Test
	@Sql("data.sql")
	void findByIsVerifiedAndClinicIdAndEnabled_ReturnList() {
		List<UserDbModel> result = userRepo.findByIsVerifiedAndClinicIdAndEnabled(true, 1, true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("verified", is(true)),
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("enabled", is(true))
			)
		));
	}
	
	@Test
	@Sql("data.sql")
	void findByFirstNameAndClinicIdAndIsVerifiedAndEnabled_ReturnList() {
		List<UserDbModel> result = userRepo.findByFirstNameAndClinicIdAndIsVerifiedAndEnabled(
			"Isa",
			1,
			true,
			true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("firstName", is("Isa")),
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("verified", is(true)),
				hasProperty("enabled", is(true))
			)
		));
	}
	
	void findByJmbgAndClinicIdAndIsVerifiedAndEnabled_ReturnList() {
		List<UserDbModel> result = userRepo.findByJmbgAndClinicIdAndIsVerifiedAndEnabled(
			"5454554545454",
			1,
			true,
			true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("jmbg", is("5454554545454")),
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("IsVerified", is(true)),
				hasProperty("enabled", is(true))
			)
		));
	}

}
