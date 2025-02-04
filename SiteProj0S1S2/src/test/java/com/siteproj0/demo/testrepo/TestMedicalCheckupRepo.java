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
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
			"03/03/2020",
			"18:00");
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("doctor", hasProperty("id", is(2))),
				hasProperty("date", is("03/03/2020")),
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
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdAndDateAndTime(3,"03/03/2020","18:00");
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", hasProperty("id", is(3))),
				hasProperty("date", is("03/03/2020")),
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
		List<MedicalCheckupDbModel> result = mcRepo.findByRoomIdAndDate(3, "03/03/2020");
		assertThat(result, not(empty()));
		assertThat(result, hasSize(greaterThan(0)));
		assertThat(result, everyItem(
			allOf(
				hasProperty("room", hasProperty("id", is(3))),
				hasProperty("date", is("03/03/2020"))
			)
		));
	}
	
	/*
	 * Test vezan za tacku 4.4
	 * Inkrementiranje verzije prilikom cuvanja
	 */
	@Test
	void save_OptimisticLockingCheck_CheckVersionIncrement() {
		MedicalCheckupDbModel mcdbm0 = mcRepo.findById(1).get();
		/*
		 * Promenom vrednosti unutar mcdbm ce se registrovati promena u objektu prilikom
		 * cuvanja. Kada dodje do neke promene, version se inkrementira.
		 */
		Integer version0 = mcdbm0.getVersion();
		mcdbm0.setDuration("1000");
		mcRepo.save(mcdbm0);
		
		MedicalCheckupDbModel mcdbm1 = mcRepo.findById(1).get();
		Integer version1 = mcdbm1.getVersion();
		mcdbm1.setDuration("2000");
		mcRepo.save(mcdbm1);
		
		Integer version0Incremented = version0.intValue() + 1;
		
		assertThat(version1, greaterThan(version0));
		assertEquals(version1, version0Incremented);
	}
	
	/*
	 * Test vezan za tacku 4.4
	 * Throw-ovanje exception-a prilikom pokusaja cuvanja
	 * stare verzije objekta
	 */
	@Test
	void save_OptimisticLockingCheck_ThrowsException() {
		final MedicalCheckupDbModel mcdbm = mcRepo.findById(2).get();
		/*
		 * Promenom vrednosti unutar mcdbm ce se registrovati promena u objektu prilikom
		 * cuvanja. Kada dodje do neke promene, version se inkrementira.
		 */
		mcdbm.setDuration("1000");
		mcRepo.save(mcdbm);
		/*
		 * mcdbm nakon prvog save-a je jos uvek na staroj verziji, dok je verzija u
		 * bazi inkrementirana. Zbog toga, ako se jos jednom pokrene save, detektovace
		 * se mismatch u verzijama i bice throw-ovan exception
		 */		
		Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
			mcRepo.save(mcdbm);
		});
	}
	
	@Test
	void save_OptimisticLockingCheck_CatchException() {
		int i = 0;
		final MedicalCheckupDbModel mcdbm = mcRepo.findById(3).get();
		mcdbm.setDuration("1000");
		mcRepo.save(mcdbm);
		try {
			mcRepo.save(mcdbm);
		} catch(ObjectOptimisticLockingFailureException e) {
			i++;
		}
		
		assertEquals(i,1);
	}
	
	@Test
	void save_OptimisticLockingCheck_TestSLeep() throws InterruptedException {
		int i = 0;
		final MedicalCheckupDbModel mcdbm = mcRepo.findById(4).get();
		mcdbm.setDuration("1000");
		mcRepo.save(mcdbm);
		try {
			mcRepo.save(mcdbm);
		} catch(ObjectOptimisticLockingFailureException e0) {
			System.out.println("Sleep 1 start...");
			Thread.sleep(1000);
			System.out.println("Sleep 1 end.");
			try {
				mcRepo.save(mcdbm);
			} catch(ObjectOptimisticLockingFailureException e1) {
				System.out.println("Sleep 2 start...");
				Thread.sleep(1000);
				System.out.println("Sleep 2 end.");
				try {
					mcRepo.save(mcdbm);
				} catch(ObjectOptimisticLockingFailureException e2) {
					i++;
				}
			}
		}
		assertEquals(i,1);
	}
	
	
}
