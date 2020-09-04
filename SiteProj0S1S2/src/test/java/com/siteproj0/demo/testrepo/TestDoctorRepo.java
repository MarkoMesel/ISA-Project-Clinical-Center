package com.siteproj0.demo.testrepo;

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

import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.repo.DoctorRepo;

@DataJpaTest
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
})
@Sql("data.sql")
class TestDoctorRepo {
	
	@Autowired
	DoctorRepo doctorRepo;

	@Test
	void findBySecurityToken_ReturnObj() {
		//2D1EBC5B7D2741979CF0E84451C5BBB2
		UUID securityToken = UUID.fromString("2D1EBC5B-7D27-4197-9CF0-E84451C5BBB2");
		DoctorDbModel result = doctorRepo.findBySecurityToken(securityToken);
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("securityToken", is(securityToken)));
	}
	
	@Test
	void findByClinicIdAndEnabled_ReturnList() {
		List<DoctorDbModel> result = doctorRepo.findByClinicIdAndEnabled(1, true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("enabled", is(true))
			)
		));
	}
	
	@Test
	void findByFirstNameAndLastNameAndRatingAndClinicIdAndEnabled_ReturnObj() {
		DoctorDbModel result = doctorRepo.findByFirstNameAndLastNameAndRatingAndClinicIdAndEnabled(
			"Aleksandar",
			"Aleksandrovic",
			10,
			1,
			true);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
				hasProperty("firstName", is("Aleksandar")),
				hasProperty("lastName", is("Aleksandrovic")),
				hasProperty("rating", is(10.0f)),
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("enabled", is(true))
			));
	}
	
	@Test
	void findByIdAndClinicIdAndEnabled_ReturnObj() {
		DoctorDbModel result = doctorRepo.findByIdAndClinicIdAndEnabled(1, 1, true);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
				hasProperty("id", is(1)),
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("enabled", is(true))
			));
	}
	
	@Test
	void findById_ReturnObj() {
		DoctorDbModel result = doctorRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));
	}
}
