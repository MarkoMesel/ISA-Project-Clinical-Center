package com.siteproj0.demo.testrepo;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.repo.MedicalCheckupRepo;

class TestMedicalCheckupRepo  extends TestRepo {

	@Autowired
	MedicalCheckupRepo mcRepo;
	
	@Test
	void findByCheckupTypeIdAndFinished_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByCheckupTypeIdAndFinished(1, false);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("checkupType", hasProperty("id", is(1))),
				hasProperty("finished", is(false))
			)
		));
	}
	
	@Test
	void findByCheckupTypeIdAndFreeAndFinished_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByCheckupTypeIdAndFreeAndFinished(1,false,false);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("checkupType", hasProperty("id", is(1))),
				hasProperty("free", is(false)),
				hasProperty("finished", is(false))
			)
		));
	}
	
	@Test
	void findByDoctorIdAndPatientId_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByDoctorIdAndPatientId(2, 1);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("doctor", hasProperty("id", is(2))),
				hasProperty("patient", hasProperty("id", is(1)))
			)
		));
	}
	
	@Test
	void findByDoctorIdAndDateAndTime_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByDoctorIdAndDateAndTime(
			2,
			"2020-03-03",
			"18:00");
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("doctor", hasProperty("id", is(2))),
				hasProperty("date", is("2020-03-03")),
				hasProperty("time", is("18:00"))
			)
		));
	}
	
	@Test
	void findByClinicIdAndDoctorIdAndFree_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByClinicIdAndDoctorIdAndFree(
			1,
			2,
			false);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("doctor", hasProperty("id", is(2))),
				hasProperty("free", is(false))
			)
		));
	}
	
	@Test
	void findById_ReturnObj() {
		MedicalCheckupDbModel result = mcRepo.findById(1).get();
		assertThat(result, notNullValue());
		assertThat(result, hasProperty("id", is(1)));
	}
	
	@Test
	void findByClinicIdAndDoctorIdAndPatientIdAndFreeAndFinished_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByClinicIdAndDoctorIdAndPatientIdAndFreeAndFinished(
			1,
			2,
			1,
			false,
			false);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("doctor", hasProperty("id", is(2))),
				hasProperty("patient", hasProperty("id", is(1))),
				hasProperty("free", is(false)),
				hasProperty("finished", is(false))
			)
		));
	}
	
	@Test
	void findByClinicIdAndPatientId_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByClinicIdAndPatientId(1, 1);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("clinic", hasProperty("id", is(1))),
				hasProperty("patient", hasProperty("id", is(1)))
			)
		));
	}
	
	@Test
	void findByRoomIdIsNull_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdIsNull();
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", is(nullValue()))
			)
		));
	}
	
	@Test
	void medicalCheckupRepo_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdAndDateAndTime(3,"2020-03-03","18:00");
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", hasProperty("id", is(3))),
				hasProperty("date", is("2020-03-03")),
				hasProperty("time", is("18:00"))
			)
		));

	}
	
	@Test
	void findByRoomIdAndFinished_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdAndFinished(3,false);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", hasProperty("id", is(3))),
				hasProperty("finished", is(false))
			)
		));
	}

	@Test
	void findByRoomIdAndFreeAndFinished_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdAndFreeAndFinished(3,false,false);
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", hasProperty("id", is(3))),
				hasProperty("free", is(false)),
				hasProperty("finished", is(false))
			)
		));
	}
	
	@Test
	void findByRoomIdAndDate_ReturnList() {
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdAndDate(3, "2020-03-03");
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", hasProperty("id", is(3))),
				hasProperty("date", is("2020-03-03"))
			)
		));
	}
}
