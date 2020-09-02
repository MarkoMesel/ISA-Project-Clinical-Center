package com.siteproj0.demo.testrepo;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.siteproj0.demo.dal.DoctorRatingDbModel;
import com.siteproj0.demo.repo.DoctorRatingRepo;

class TestDoctorRatingRepo extends TestRepo {

	@Autowired
	DoctorRatingRepo doctorRatingRepo;
	
	@Test
	void findByDoctorId_ReturnList() {
		List<DoctorRatingDbModel> result = doctorRatingRepo.findByDoctorId(1);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			hasProperty("doctor", hasProperty("id", is(1)))
		));
	}

}
