package com.siteproj0.demo.testcontroller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import com.siteproj0.demo.checkuptype.CheckupTypeRegisterModel;
import com.siteproj0.demo.checkuptype.CheckupTypeResponseModel;
import com.siteproj0.demo.clinic.ClinicResponseModel;
import com.siteproj0.demo.clinic.ClinicViewModel;
import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.ClinicRatingDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.DoctorRatingDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.dal.VacationDbModel;
import com.siteproj0.demo.doctor.DoctorRatingResponseModel;
import com.siteproj0.demo.doctor.DoctorRegisterModel;
import com.siteproj0.demo.doctor.DoctorResponseModel;
import com.siteproj0.demo.home.ChangePasswordRequestModel;
import com.siteproj0.demo.home.EditProfileBasicInfoRequestModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupDoctorResponseModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupPatientResponseModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupRegisterRequestModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupResponseModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupRoomResponseModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRatingRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRatingRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;
import com.siteproj0.demo.repo.RoomRepo;
import com.siteproj0.demo.repo.UserRepo;
import com.siteproj0.demo.repo.VacationRepo;
import com.siteproj0.demo.room.BusyDateResponseModel;
import com.siteproj0.demo.room.RoomRegisterModel;
import com.siteproj0.demo.room.RoomResponseModel;
import com.siteproj0.demo.user.EditProfileRequestModel;
import com.siteproj0.demo.user.LoginModel;
import com.siteproj0.demo.user.LoginResponseModel;
import com.siteproj0.demo.user.ProfileResponseModel;
import com.siteproj0.demo.user.UserResponseModel;
import com.siteproj0.demo.vacation.VacationRegisterModel;
import com.siteproj0.demo.vacation.VacationResponseModel;

@SpringBootTest
@AutoConfigureMockMvc
class TestController {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ClinicAdminRepo clinicAdminRepo;
	
	@MockBean
	CheckupTypeRepo checkupTypeRepo;
	
	@MockBean
	ClinicRepo clinicRepo;
	
	@MockBean
	MedicalCheckupRepo medicalCheckupRepo;
	
	@MockBean
	DoctorRepo doctorRepo;
	
	@MockBean
	RoomRepo roomRepo;
	
	@MockBean
	UserRepo patientRepo;
	
	@MockBean
	VacationRepo vacationRepo;
	
	@MockBean
	ClinicRatingRepo clinicRatingRepo;
	
	@MockBean
	DoctorRatingRepo doctorRatingRepo;
	
	static ClinicDbModel clinicDBM;
	static ClinicAdminDbModel clinicAdminDBM;
	static CheckupTypeDbModel checkupTypeDBM0;
	static CheckupTypeDbModel checkupTypeDBM1;
	static CheckupTypeDbModel checkupTypeDBM2;
	static List<CheckupTypeDbModel> checkupTypeDBMList;
	static CheckupTypeRegisterModel checkupTypeRegM;
	static CheckupTypeResponseModel checkupTypeResM0;
	static CheckupTypeResponseModel checkupTypeResM1;
	static CheckupTypeResponseModel checkupTypeResM2;
	static List<CheckupTypeResponseModel> checkupTypeResMList;
	static RoomDbModel roomDBM;
	static DoctorDbModel doctorDBM;
	static UserDbModel patientDBM0;
	static UserDbModel patientDBM1;
	static MedicalCheckupDbModel medicalCheckupDBM;
	static List<MedicalCheckupDbModel> medicalCheckupDBMList;
	static BindingResult bindingResult;
	
	static List<ClinicDbModel> clinicDBMList;
	static Iterable<ClinicDbModel> clinicDBMIterable;
	static ClinicViewModel clinicVM;
	static List<ClinicViewModel> clinicVMList;
	static ClinicResponseModel clinicRM;
	
	static LoginResponseModel loginRM;
	static LoginModel loginM;
	static ProfileResponseModel profileRM;
	static EditProfileBasicInfoRequestModel editProfileBasicInfoRM;
	static ChangePasswordRequestModel changePasswordRM;
	
	static DoctorResponseModel doctorResM;
	static List<DoctorResponseModel> doctorRMList;
	static List<DoctorDbModel> doctorDBMList;
	static DoctorRegisterModel doctorRegM;
	
	static MedicalCheckupResponseModel medicalCheckupResM;
	static MedicalCheckupDoctorResponseModel medicalCheckupDoctorRM;
	static List<MedicalCheckupDoctorResponseModel> medicalCheckupDoctorRMList;
	static MedicalCheckupRegisterRequestModel medicalCheckupRegReqM;
	static MedicalCheckupRoomResponseModel medicalCheckupRoomRM;
	static List<MedicalCheckupRoomResponseModel> medicalCheckupRoomRMList;
	static MedicalCheckupDbModel medicalCheckupDBMWithNoRoom;
	static List<MedicalCheckupDbModel> medicalCheckupDBMWithNoRoomList;
	static List<MedicalCheckupDbModel> medicalCheckupDBMListWithOnlyOneElement;
	static MedicalCheckupDoctorResponseModel medicalCheckupDoctorRMNotFinished;
	static List<MedicalCheckupDoctorResponseModel> medicalCheckupDoctorRMNotFinishedList;
	static MedicalCheckupPatientResponseModel medicalCheckupPatientRM;
	static List<MedicalCheckupPatientResponseModel> medicalCheckupPatientRMList;
	
	static List<RoomDbModel> roomDBMList;
	static RoomResponseModel roomResM;
	static List<RoomResponseModel> roomResMList;
	static RoomRegisterModel roomRegM;
	static BusyDateResponseModel busyDateRM;
	static List<BusyDateResponseModel> busyDateRMList;
	
	static EditProfileRequestModel editProfileRM;
	static List<UserDbModel> patientDBMList;
	static UserResponseModel patientResM;
	static List<UserResponseModel> patientResMList;
	
	static VacationDbModel vacationDBM;
	static List<VacationDbModel> vacationDBMList;
	static VacationRegisterModel vacationRegM;
	static VacationResponseModel vacationResM;
	static List<VacationResponseModel> vacationResMList;
	
	static ClinicRatingDbModel clinicRatingDBM0;
	static ClinicRatingDbModel clinicRatingDBM1;
	static List<ClinicRatingDbModel> clinicRatingDBMList;
	
	static DoctorRatingDbModel doctorRatingDBM0;
	static DoctorRatingDbModel doctorRatingDBM1;
	static List<DoctorRatingDbModel> doctorRatingDBMList;
	static DoctorRatingResponseModel doctorRatingResM;
	static List<DoctorRatingResponseModel> doctorRatingResMList;
	
	@BeforeAll
	static void init() {
		clinicDBM = new ClinicDbModel();
		clinicDBM.setId(1);
		clinicDBM.setAddress("TestAddress");
		clinicDBM.setDescription("TestDescription");
		clinicDBM.setName("TestName");
		clinicDBM.setRating(10.0f);
		clinicDBM.setPrice(1000.0f);
		
		clinicAdminDBM = new ClinicAdminDbModel();
		clinicAdminDBM.setId(1);
		clinicAdminDBM.setRole("CLINICADMIN");
		clinicAdminDBM.setCountry("TestCountry");
		clinicAdminDBM.setCity("TestCity");
		clinicAdminDBM.setStreet("TestStreet");
		clinicAdminDBM.setEmail("testmail@testmail.com");
		clinicAdminDBM.setFirstName("TestFirstName");
		clinicAdminDBM.setLastName("TestLastName");
		clinicAdminDBM.setJmbg("1111111111111");
		clinicAdminDBM.setPhone("0213334444");
		clinicAdminDBM.setVerified(true);
		clinicAdminDBM.setPassword("abcde123");
		clinicAdminDBM.setValidationToken(null);
		clinicAdminDBM.setSecurityToken(UUID.randomUUID());
		clinicAdminDBM.setClinic(clinicDBM);
		
		checkupTypeDBM0 = new CheckupTypeDbModel();
		checkupTypeDBM0.setId(1);
		checkupTypeDBM0.setName("ctName1");
		checkupTypeDBM0.setPrice(500);
		checkupTypeDBM0.setClinic(clinicDBM);
		checkupTypeDBM0.setEnabled(true);
		
		checkupTypeDBM1 = new CheckupTypeDbModel();
		checkupTypeDBM1.setId(2);
		checkupTypeDBM1.setName("ctName2");
		checkupTypeDBM1.setPrice(500);
		checkupTypeDBM1.setClinic(clinicDBM);
		checkupTypeDBM1.setEnabled(true);
		
		checkupTypeDBM2 = new CheckupTypeDbModel();
		checkupTypeDBM2.setId(3);
		checkupTypeDBM2.setName("ctName3");
		checkupTypeDBM2.setPrice(500);
		checkupTypeDBM2.setClinic(clinicDBM);
		checkupTypeDBM2.setEnabled(true);
		
		checkupTypeDBMList = new ArrayList<>();
		checkupTypeDBMList.add(checkupTypeDBM0);
		checkupTypeDBMList.add(checkupTypeDBM1);
		checkupTypeDBMList.add(checkupTypeDBM2);
		
		checkupTypeRegM = new CheckupTypeRegisterModel();
		checkupTypeRegM.setName("ctName1");
		checkupTypeRegM.setPrice(500);
		
		checkupTypeResM0 = new CheckupTypeResponseModel(1, "ctName1", 500, 1);
		checkupTypeResM1 = new CheckupTypeResponseModel(2, "ctName2", 500, 1);
		checkupTypeResM2 = new CheckupTypeResponseModel(3, "ctName3", 500, 1);
		
		checkupTypeResMList = new ArrayList<>();
		checkupTypeResMList.add(checkupTypeResM0);
		checkupTypeResMList.add(checkupTypeResM1);
		checkupTypeResMList.add(checkupTypeResM2);
		
		roomDBM = new RoomDbModel();
		roomDBM.setId(1);
		roomDBM.setName("A");
		roomDBM.setNumber("1");
		roomDBM.setClinic(clinicDBM);
		roomDBM.setEnabled(true);
		
		doctorDBM = new DoctorDbModel();
		doctorDBM.setId(1);
		doctorDBM.setRole("DOCTOR");
		doctorDBM.setCountry("TestCountry");
		doctorDBM.setCity("TestCity");
		doctorDBM.setStreet("TestStreet");
		doctorDBM.setEmail("testmail@testmail.com");
		doctorDBM.setFirstName("TestFirstName");
		doctorDBM.setLastName("TestLastName");
		doctorDBM.setJmbg("1111111111111");
		doctorDBM.setPhone("0213334444");
		doctorDBM.setVerified(true);
		doctorDBM.setPassword("abcde123");
		doctorDBM.setValidationToken(null);
		doctorDBM.setSecurityToken(UUID.randomUUID());
		doctorDBM.setClinic(clinicDBM);
		doctorDBM.setRating(10.0f);
		doctorDBM.setShiftStart("10:00");
		doctorDBM.setShiftEnd("18:00");
		doctorDBM.setEnabled(true);
		
		patientDBM0 = new UserDbModel();
		patientDBM0.setId(1);
		patientDBM0.setRole("USER");
		patientDBM0.setCountry("TestCountry");
		patientDBM0.setCity("TestCity");
		patientDBM0.setStreet("TestStreet");
		patientDBM0.setEmail("testmail@testmail.com");
		patientDBM0.setFirstName("TestFirstName");
		patientDBM0.setLastName("TestLastName");
		patientDBM0.setJmbg("1111111111111");
		patientDBM0.setPhone("0213334444");
		patientDBM0.setVerified(true);
		patientDBM0.setPassword("abcde123");
		patientDBM0.setValidationToken(null);
		patientDBM0.setSecurityToken(UUID.randomUUID());
		patientDBM0.setClinic(clinicDBM);
		patientDBM0.setEnabled(true);
		
		patientDBM1 = new UserDbModel();
		patientDBM1.setId(2);
		patientDBM1.setRole("USER");
		patientDBM1.setCountry("TestCountry");
		patientDBM1.setCity("TestCity");
		patientDBM1.setStreet("TestStreet");
		patientDBM1.setEmail("testmail@testmail.com");
		patientDBM1.setFirstName("TestFirstName");
		patientDBM1.setLastName("TestLastName");
		patientDBM1.setJmbg("1111111111111");
		patientDBM1.setPhone("0213334444");
		patientDBM1.setVerified(true);
		patientDBM1.setPassword("abcde123");
		patientDBM1.setValidationToken(null);
		patientDBM1.setSecurityToken(UUID.randomUUID());
		patientDBM1.setClinic(clinicDBM);
		patientDBM1.setEnabled(true);
		
		medicalCheckupDBM = new MedicalCheckupDbModel();
		medicalCheckupDBM.setId(1);
		medicalCheckupDBM.setDate("2020-10-10");
		medicalCheckupDBM.setTime("10:00");
		medicalCheckupDBM.setDuration("50");
		medicalCheckupDBM.setPrice(500);
		medicalCheckupDBM.setFree(false);
		medicalCheckupDBM.setCheckupType(checkupTypeDBM0);
		medicalCheckupDBM.setClinic(clinicDBM);
		medicalCheckupDBM.setRoom(roomDBM);
		medicalCheckupDBM.setDoctor(doctorDBM);
		medicalCheckupDBM.setPatient(patientDBM0);
		medicalCheckupDBM.setNotes("TestNotes");
		medicalCheckupDBM.setEndNotes("TestEndNotes");
		medicalCheckupDBM.setFinished(false);
		
		medicalCheckupDBMList = new ArrayList<>();
		medicalCheckupDBMList.add(medicalCheckupDBM);
		
		bindingResult = mock(BindingResult.class);
		
		clinicDBM = new ClinicDbModel();
		clinicDBM.setId(1);
		clinicDBM.setAddress("TestAddress");
		clinicDBM.setDescription("TestDescription");
		clinicDBM.setName("TestName");
		clinicDBM.setRating(10.0f);
		clinicDBM.setPrice(1000.0f);
		
		clinicDBMList = new ArrayList<>();
		clinicDBMList.add(clinicDBM);
		
		clinicDBMIterable = clinicDBMList;
		
		clinicVM = new ClinicViewModel(
			1,
			"TestAddress",
			"TestDescription",
			"TestName",
			10.0f,
			1000.0f);
		
		clinicVMList = new ArrayList<>();
		clinicVMList.add(clinicVM);
		
		clinicRM = new ClinicResponseModel(
			1,
			"TestName",
			"TestDescription",
			"TestAddress");
		
		loginRM = new LoginResponseModel(UUID.randomUUID());
		
		loginM = new LoginModel("testmail@testmail.com", "abcde123");
		
		profileRM = new ProfileResponseModel(
			1, 
			"TestFirstName", 
			"TestLastName",
			"TestCountry", 
			"TestCity", 
			"TestStreet", 
			"0213334444", 
			"1111111111111", 
			"testmail@testmail.com", 
			true, 
			"TEST");
		
		editProfileBasicInfoRM = new EditProfileBasicInfoRequestModel();
		editProfileBasicInfoRM.setFirstName("TestFirstName");
		editProfileBasicInfoRM.setLastName("TestLastName");
		editProfileBasicInfoRM.setCountry("TestCountry");
		editProfileBasicInfoRM.setCity("TestCity");
		editProfileBasicInfoRM.setStreet("TestStreet");
		editProfileBasicInfoRM.setPhone("0213334444");
		editProfileBasicInfoRM.setJmbg("1111111111111");
		editProfileBasicInfoRM.setEmail("testmail@testmail.com");
		
		changePasswordRM = new ChangePasswordRequestModel();
		changePasswordRM.setOldPassword("abcde123");
		changePasswordRM.setPassword("abcde123");
		
		doctorResM = new DoctorResponseModel
        		(1,
        		"TestFirstName",
        		"TestLastName",
        		"1111111111111",
        		"TestCountry",
        		"TestCity",
        		"TestStreet",
        		"testmail@testmail.com",
        		"0213334444",
        		10.0f,
        		"10:00",
        		"18:00",
        		1);
		
		doctorRMList = new ArrayList<>();
		doctorRMList.add(doctorResM);
		
		doctorDBMList = new ArrayList<>();
		doctorDBMList.add(doctorDBM);
		
		doctorRegM = new DoctorRegisterModel();
		doctorRegM.setId(21);
		doctorRegM.setCountry("TestCountry");
		doctorRegM.setCity("TestCity");
		doctorRegM.setStreet("TestStreet");
		doctorRegM.setEmail("testmail@testmail.com");
		doctorRegM.setFirstName("TestFirstName");
		doctorRegM.setLastName("TestLastName");
		doctorRegM.setJmbg("1111111111111");
		doctorRegM.setPhone("0213334444");
		doctorRegM.setPassword("abcde123");
		doctorRegM.setConfirmPassword("abcde123");
		doctorRegM.setClinicId(1);
		doctorRegM.setShiftStart("10:00");
		doctorRegM.setShiftEnd("18:00");
		
		medicalCheckupResM = new MedicalCheckupResponseModel(
			1, 
			"2020-10-10", 
			"10:00", 
			"50", 
			500, 
			1, 
			1, 
			1, 
			1);
		
		medicalCheckupDoctorRM = new MedicalCheckupDoctorResponseModel(
			1, 
			"2020-10-10", 
			"10:00", 
			"50", 
			500,
			"ctName1",
			"A - 1",
			"TestFirstName TestLastName",
			"TestNotes",
			"Yes");
		
		medicalCheckupDoctorRMList = new ArrayList<>();
		medicalCheckupDoctorRMList.add(medicalCheckupDoctorRM);
		
		medicalCheckupRegReqM = new MedicalCheckupRegisterRequestModel();
		medicalCheckupRegReqM.setId(1);
		medicalCheckupRegReqM.setDate("2020-10-10");
		medicalCheckupRegReqM.setTime("10:00");
		medicalCheckupRegReqM.setNotes("TestNotes");
		
		medicalCheckupDBMWithNoRoom = new MedicalCheckupDbModel();
		medicalCheckupDBMWithNoRoom.setId(1);
		medicalCheckupDBMWithNoRoom.setDate("2020-10-10");
		medicalCheckupDBMWithNoRoom.setTime("10:00");
		medicalCheckupDBMWithNoRoom.setDuration("50");
		medicalCheckupDBMWithNoRoom.setPrice(500);
		medicalCheckupDBMWithNoRoom.setFree(false);
		medicalCheckupDBMWithNoRoom.setCheckupType(checkupTypeDBM0);
		medicalCheckupDBMWithNoRoom.setClinic(clinicDBM);
		medicalCheckupDBMWithNoRoom.setRoom(null);
		medicalCheckupDBMWithNoRoom.setDoctor(doctorDBM);
		medicalCheckupDBMWithNoRoom.setPatient(patientDBM0);
		medicalCheckupDBMWithNoRoom.setNotes("TestNotes");
		medicalCheckupDBMWithNoRoom.setEndNotes("TestEndNotes");
		medicalCheckupDBMWithNoRoom.setFinished(false);
		
		medicalCheckupDBMWithNoRoomList = new ArrayList<>();
		medicalCheckupDBMWithNoRoomList.add(medicalCheckupDBMWithNoRoom);
		
		medicalCheckupRoomRM = new MedicalCheckupRoomResponseModel(
			1, 
			"2020-10-10", 
			"10:00", 
			"50", 
			500, 
			"ctName1", 
			"TestFirstName TestLastName", 
			"TestFirstName TestLastName", 
			"TestNotes");
		
		medicalCheckupRoomRMList = new ArrayList<>();
		medicalCheckupRoomRMList.add(medicalCheckupRoomRM);
				
		medicalCheckupDBMListWithOnlyOneElement = new ArrayList<>();
		medicalCheckupDBMListWithOnlyOneElement.add(medicalCheckupDBM);
		
		medicalCheckupDoctorRMNotFinished = new MedicalCheckupDoctorResponseModel(
			1, 
			"2020-10-10", 
			"10:00", 
			"50", 
			500,
			"ctName1",
			"A - 1",
			"TestFirstName TestLastName",
			"TestNotes",
			"No");
		
		medicalCheckupDoctorRMNotFinishedList = new ArrayList<>();
		medicalCheckupDoctorRMNotFinishedList.add(medicalCheckupDoctorRMNotFinished);
		
		medicalCheckupPatientRM = new MedicalCheckupPatientResponseModel(
			1, 
			"2020-10-10", 
			"10:00", 
			"50", 
			500,
			"ctName1",
			"A - 1",
			"TestFirstName TestLastName",
			"TestNotes",
			"Yes");
		
		medicalCheckupPatientRMList = new ArrayList<>();
		medicalCheckupPatientRMList.add(medicalCheckupPatientRM);
		
		roomDBMList = new ArrayList<>();
		roomDBMList.add(roomDBM);
		
		roomResM = new RoomResponseModel(
			1, 
			"A", 
			"1", 
			1);
		roomResMList = new ArrayList<>();
		roomResMList.add(roomResM);
		
		roomRegM = new RoomRegisterModel();
		roomRegM.setId(1);
		roomRegM.setName("A");
		roomRegM.setNumber("1");
		roomRegM.setClinicId(1);
		
		busyDateRM = new BusyDateResponseModel("2020-10-10");
		
		busyDateRMList = new ArrayList<>();
		busyDateRMList.add(busyDateRM);
		
		editProfileRM = new EditProfileRequestModel();
		editProfileRM.setFirstName("TestFirstName");
		editProfileRM.setLastName("TestLastName");
		editProfileRM.setPassword("abcde123");
		editProfileRM.setCountry("TestCountry");
		editProfileRM.setCity("TestCity");
		editProfileRM.setStreet("TestStreet");
		editProfileRM.setPhone("0213334444");
		
		patientDBMList = new ArrayList<>();
		patientDBMList.add(patientDBM0);
		
		patientResM = new UserResponseModel(
    		1,
    		"TestFirstName",
    		"TestLastName",
    		"1111111111111",
    		"TestCountry",
    		"TestCity",
    		"TestStreet",
    		"testmail@testmail.com",
    		"0213334444",
    		1);
		
		patientResMList = new ArrayList<>();
		patientResMList.add(patientResM);
		
		vacationDBM = new VacationDbModel();
		vacationDBM.setId(1);
		vacationDBM.setStartDate("2020-11-11");
		vacationDBM.setEndDate("2020-12-12");
		vacationDBM.setDoctor(doctorDBM);
		vacationDBM.setApproved(false);
		vacationDBM.setEnabled(true);
		
		vacationDBMList = new ArrayList<>();
		vacationDBMList.add(vacationDBM);
		
		vacationRegM = new VacationRegisterModel();
		vacationRegM.setId(1);
		vacationRegM.setStartDate("2020-11-11");
		vacationRegM.setEndDate("2020-12-12");
		vacationRegM.setDoctorId(1);
		
		vacationResM = new VacationResponseModel(
			1, 
			"TestFirstName TestLastName", 
			"2020-11-11", 
			"2020-12-12");
		
		vacationResMList = new ArrayList<>();
		vacationResMList.add(vacationResM);
		
		clinicRatingDBM0 = new ClinicRatingDbModel();
		clinicRatingDBM0.setId(1);
		clinicRatingDBM0.setUser(patientDBM0);
		clinicRatingDBM0.setClinic(clinicDBM);
		clinicRatingDBM0.setRating(5.3f);
		clinicRatingDBM0.setComment("This is a comment");
		
		clinicRatingDBM1 = new ClinicRatingDbModel();
		clinicRatingDBM1.setId(2);
		clinicRatingDBM1.setUser(patientDBM1);
		clinicRatingDBM1.setClinic(clinicDBM);
		clinicRatingDBM1.setRating(7.2f);
		clinicRatingDBM1.setComment("This is another comment");
		
		clinicRatingDBMList = new ArrayList<>();
		clinicRatingDBMList.add(clinicRatingDBM0);
		clinicRatingDBMList.add(clinicRatingDBM1);
		
		doctorRatingDBM0 = new DoctorRatingDbModel();
		doctorRatingDBM0.setId(1);
		doctorRatingDBM0.setUser(patientDBM0);
		doctorRatingDBM0.setDoctor(doctorDBM);
		doctorRatingDBM0.setRating(5.3f);
		doctorRatingDBM0.setComment("This is a comment");
		
		doctorRatingDBM1 = new DoctorRatingDbModel();
		doctorRatingDBM1.setId(2);
		doctorRatingDBM1.setUser(patientDBM0);
		doctorRatingDBM1.setDoctor(doctorDBM);
		doctorRatingDBM1.setRating(7.2f);
		doctorRatingDBM1.setComment("This is another comment");
		
		doctorRatingDBMList = new ArrayList<>();
		doctorRatingDBMList.add(doctorRatingDBM0);
		doctorRatingDBMList.add(doctorRatingDBM1);
		
		doctorRatingResM = new DoctorRatingResponseModel(
			1, 
			"TestFirstName", 
			"TestLastName", 
			6.25f, 
			1);
		
		doctorRatingResMList = new ArrayList<>();
		doctorRatingResMList.add(doctorRatingResM);
	}

}
