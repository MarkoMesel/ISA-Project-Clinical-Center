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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.repo.RoomRepo;

class TestRoomRepo extends TestRepo {

	@Autowired
	RoomRepo roomRepo;
	
	@Test
	void findById_ReturnObj() {
		RoomDbModel result = roomRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));	
	}
	
	@Test
	void findByClinicIdAndEnabled_ReturnList() {
		List<RoomDbModel> result = roomRepo.findByClinicIdAndEnabled(1, true);
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
	void findByIdAndClinicId_ReturnObj() {
		RoomDbModel result = roomRepo.findByIdAndClinicId(1, 1);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("id", is(1)),
			hasProperty("clinic", hasProperty("id", is(1)))
		));
	}
	
	@Test
	void findByIdAndClinicIdAndEnabled_ReturnObj() {
		RoomDbModel result = roomRepo.findByIdAndClinicIdAndEnabled(1, 1, true);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("id", is(1)),
			hasProperty("clinic", hasProperty("id", is(1))),
			hasProperty("enabled", is(true))
		));
	}
	
	@Test
	void findByNameAndNumberAndClinicIdAndEnabled_ReturnObj() {
		RoomDbModel result = roomRepo.findByNameAndNumberAndClinicIdAndEnabled(
			"A",
			"1",
			1,
			true);
		assertThat(result, notNullValue());
		assertThat(result, allOf(
			hasProperty("name", is("A")),
			hasProperty("number", is("1")),
			hasProperty("clinic", hasProperty("id", is(1))),
			hasProperty("enabled", is(true))
		));
	}
	
	@Test
	void findByNameAndClinicIdAndEnabled_ReturnList() {
		List<RoomDbModel> result = roomRepo.findByNameAndClinicIdAndEnabled(
			"A",
			1,
			true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
					hasProperty("name", is("A")),
					hasProperty("clinic", hasProperty("id", is(1))),
					hasProperty("enabled", is(true))
			)
		));
	}
	
	@Test
	void findByNumberAndClinicIdAndEnabled_ReturnList() {
		List<RoomDbModel> result = roomRepo.findByNumberAndClinicIdAndEnabled(
			"1",
			1,
			true);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
					hasProperty("number", is("1")),
					hasProperty("clinic", hasProperty("id", is(1))),
					hasProperty("enabled", is(true))
			)
		));
	}

}
