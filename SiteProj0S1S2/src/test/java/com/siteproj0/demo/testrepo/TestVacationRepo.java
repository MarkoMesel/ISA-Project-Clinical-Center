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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.siteproj0.demo.dal.VacationDbModel;
import com.siteproj0.demo.repo.VacationRepo;

class TestVacationRepo extends TestRepo {

	@Autowired
	VacationRepo vacationRepo;
	
	@Test
	void findByApprovedAndEnabled_ReturnList() {
		List<VacationDbModel> result = vacationRepo.findByApprovedAndEnabled(false,true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("approved", is(false)),
				hasProperty("enabled", is(true))
			)
		));
	}
	
	@Test
	void findById_ReturnObj() {
		VacationDbModel result = vacationRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));
	}

}
