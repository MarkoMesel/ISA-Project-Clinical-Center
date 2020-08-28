package com.siteproj0.demo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import org.springframework.validation.BindingResult;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.siteproj0.demo.checkuptype.CheckupTypeRegisterModel;
import com.siteproj0.demo.checkuptype.CheckupTypeResponseModel;
import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;

@SpringBootTest
@AutoConfigureMockMvc
class TestCheckupTypeController {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	ClinicAdminRepo clinicAdminRepo;
	
	@MockBean
	CheckupTypeRepo checkupTypeRepo;
	
	@MockBean
	ClinicRepo clinicRepo;
	
	@MockBean
	MedicalCheckupRepo medicalCheckupRepo;
	
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
	static UserDbModel patientDBM;
	static MedicalCheckupDbModel medicalCheckupDBM;
	static List<MedicalCheckupDbModel> medicalCheckupDBMList;
	static BindingResult bindingResult;
	
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
		
		patientDBM = new UserDbModel();
		patientDBM.setId(1);
		patientDBM.setRole("USER");
		patientDBM.setCountry("TestCountry");
		patientDBM.setCity("TestCity");
		patientDBM.setStreet("TestStreet");
		patientDBM.setEmail("testmail@testmail.com");
		patientDBM.setFirstName("TestFirstName");
		patientDBM.setLastName("TestLastName");
		patientDBM.setJmbg("1111111111111");
		patientDBM.setPhone("0213334444");
		patientDBM.setVerified(true);
		patientDBM.setPassword("abcde123");
		patientDBM.setValidationToken(null);
		patientDBM.setSecurityToken(UUID.randomUUID());
		patientDBM.setClinic(clinicDBM);
		patientDBM.setEnabled(true);
		
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
		medicalCheckupDBM.setNotes("TestNotes");
		medicalCheckupDBM.setEndNotes("TestEndNotes");
		medicalCheckupDBM.setFinished(false);
		
		medicalCheckupDBMList = new ArrayList<>();
		medicalCheckupDBMList.add(medicalCheckupDBM);
		
		bindingResult = mock(BindingResult.class);
	}
	
	@Test
	void showCtManagerPage_GET() throws Exception {	
		this.mockMvc.perform(MockMvcRequestBuilders.get("/checkupTypeManager"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("checkupTypeManager"));
	}
	
	@Test
	void getCtFromClinicAdmin_GET() throws Exception {		
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(checkupTypeResMList,
	            new TypeToken<List<CheckupTypeResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(checkupTypeRepo.findByClinicIdAndEnabled(
			anyInt(), 
			anyBoolean()))
		.thenReturn(checkupTypeDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getCtFromClinicAdmin")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void showAddCtForm_GET() throws Exception {	
		this.mockMvc.perform(MockMvcRequestBuilders.get("/addCt"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("addCheckupType"));
	}
	
	@Test
	void addNewCt_POST() throws Exception {
	    when(bindingResult.hasErrors()).thenReturn(false);
	    when(clinicRepo.findById(anyInt())).thenReturn(Optional.of(clinicDBM));
	    when(checkupTypeRepo.save((CheckupTypeDbModel)notNull())).thenReturn(checkupTypeDBM0);
	    
	    mockMvc.perform(MockMvcRequestBuilders.post("/addCt")
	    	.flashAttr("ct", checkupTypeRegM))
		.andExpect(redirectedUrl("/checkupTypeManager"));
	}
	
	@Test
	void showEditCtForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/editCheckupType/1"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("editCheckupType"));
	}
	
	@Test
	void getCtInfo_GET() throws Exception {
		String expected = new Gson().toJson(checkupTypeResM0);

		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(checkupTypeRepo.findByIdAndClinicIdAndEnabled(
			anyInt(), 
			anyInt(), 
			anyBoolean()))
		.thenReturn(checkupTypeDBM0);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getCtInfo/1")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void editCtInfo_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(checkupTypeResM0);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(checkupTypeRepo.findByIdAndClinicIdAndEnabled(anyInt(), anyInt(), anyBoolean())).thenReturn(checkupTypeDBM0);
	    when(checkupTypeRepo.save((CheckupTypeDbModel)notNull())).thenReturn(checkupTypeDBM0);
	    
	    mockMvc.perform(MockMvcRequestBuilders.put("/editCtInfo")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr)
	    	.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void findCtNamePrice_GET() throws Exception {
		String expected = new Gson().toJson(checkupTypeResM0);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(checkupTypeRepo.findByNameAndPriceAndClinicIdAndEnabled(anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(checkupTypeDBM0);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findCtNamePrice")
			.header("token", UUID.randomUUID())
			.header("name", "ctName1")
			.header("price", 500))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}

	@Test
	void logicalDeleteCt_PUT() throws Exception {
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(checkupTypeRepo.findByIdAndClinicIdAndEnabled(anyInt(), anyInt(), anyBoolean())).thenReturn(checkupTypeDBM0);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/logicalDeleteCt")
	    	.header("token", UUID.randomUUID())
	    	.header("ctid", 1))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void isCtInUse_GET() throws Exception {
		String expected = "true";

		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByCheckupTypeIdAndFinished(
			anyInt(), 
			anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/isCheckupTypeInUse")
			.header("token", UUID.randomUUID())
	    	.header("ctid", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void isCtReserved() throws Exception {
		String expected = "true";
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByCheckupTypeIdAndFreeAndFinished(
			anyInt(), 
			anyBoolean(), 
			anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/isCheckupTypeReserved")
			.header("token", UUID.randomUUID())
	    	.header("ctid", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
}
