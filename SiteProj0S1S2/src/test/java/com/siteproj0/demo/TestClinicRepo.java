package com.siteproj0.demo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.repo.ClinicRepo;

@DataJpaTest
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
})
class TestClinicRepo {
	
	@Autowired
	ClinicRepo clinicRepo;
	
	@Test
	@Sql("data.sql")
	void findById_ReturnObj() {
		ClinicDbModel result = clinicRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));
	}
	
	@Test
	@Sql("data.sql")
	void findAll_ReturnIterable() {
		Iterable<ClinicDbModel> result = clinicRepo.findAll();
		assertThat(result, not(emptyIterable()));
	}

}
