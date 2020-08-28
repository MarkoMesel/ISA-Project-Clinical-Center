package com.siteproj0.demo;

//import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.everyItem;
//import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

@DataJpaTest
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
})
class TestCheckupTypeRepo {

	@Autowired
	CheckupTypeRepo ctRepo;
	
	@Test
	@Sql("data.sql")
	void findByClinicIdAndEnabled_ReturnList() {	
		List<CheckupTypeDbModel> result = ctRepo.findByClinicIdAndEnabled(1, true);
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
	@Sql("data.sql")
	void findByIdAndClinicIdAndEnabled_ReturnObj() {
		CheckupTypeDbModel result = ctRepo.findByIdAndClinicIdAndEnabled(1, 1, true);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("id", is(1)),
			hasProperty("clinic", hasProperty("id", is(1))),
			hasProperty("enabled", is(true))
		));
	}
	
	@Test
	@Sql("data.sql")
	void findByNameAndPriceAndClinicIdAndEnabled_ReturnObj() {
		CheckupTypeDbModel result = ctRepo.findByNameAndPriceAndClinicIdAndEnabled(
			"type1",
			1000,
			1,
			true
		);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("name", is("type1")),
			hasProperty("price", is(1000)),
			hasProperty("clinic", hasProperty("id", is(1))),
			hasProperty("enabled", is(true))
		));
	}
	
	@Test
	@Sql("data.sql")
	void findById_ReturnObj() {
		CheckupTypeDbModel result = ctRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));
	}
	
}
