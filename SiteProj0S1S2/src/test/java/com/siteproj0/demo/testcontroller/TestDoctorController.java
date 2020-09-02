package com.siteproj0.demo.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.siteproj0.demo.checkuptype.CheckupTypeResponseModel;
import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.doctor.DoctorRatingResponseModel;
import com.siteproj0.demo.doctor.DoctorResponseModel;

class TestDoctorController extends TestController {

	@Test
	void doctorLoginPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/doctorLogin"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("doctorlogin"));
	}
	
	@Test
	void login_POST() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(loginM);
	    
	    loginRM.setToken(doctorDBM.getSecurityToken());
		String expected = new Gson().toJson(loginRM);
		
		when(doctorRepo.findByEmailAndPassword(anyString(), anyString())).thenReturn(doctorDBM);
	    
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/doctorLogin")
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void getProfileInformation_GET() throws Exception {
		profileRM.setRole("DOCTOR");
		String expected = new Gson().toJson(profileRM);
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getDoctorProfile")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void showDoctorManagerPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/doctorManager"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("doctorManager"));
	}
	
	@Test
	void getDoctorsFromClinicAdmin_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(doctorRMList,
	            new TypeToken<List<DoctorResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(doctorRepo.findByClinicIdAndEnabled(anyInt(), anyBoolean())).thenReturn(doctorDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getDoctorsFromClinicAdmin")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void showDoctorRegistrationForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/registerDoctor"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("registerDoctor"));
	}
	
	@Test
	void registerDoctorAccount_POST() throws Exception {
		when(bindingResult.hasErrors()).thenReturn(false);
		when(clinicRepo.findById(anyInt())).thenReturn(Optional.of(clinicDBM));
		when(doctorRepo.save((DoctorDbModel)notNull())).thenReturn(doctorDBM);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/registerDoctor")
	    	.flashAttr("doctor", doctorRegM))
		.andExpect(redirectedUrl("/doctorManager"));
	}
	
	@Test
	void findDoctorFirstNameLastNameRating_GET() throws Exception {
		String expected = new Gson().toJson(doctorResM);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(doctorRepo.findByFirstNameAndLastNameAndRatingAndClinicIdAndEnabled(
			anyString(),
			anyString(),
			anyFloat(),
			anyInt(),
			anyBoolean()))
		.thenReturn(doctorDBM);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findDoctorFirstNameLastNameRating")
	    	.header("token", UUID.randomUUID())
	    	.header("firstName", "TestFirstName")
	    	.header("lastName", "TestLastName")
	    	.header("rating", 10.0f))
	    .andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void logicalDeleteDoctor_PUT() throws Exception {
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(doctorRepo.findByIdAndClinicIdAndEnabled(anyInt(), anyInt(), anyBoolean())).thenReturn(doctorDBM);
		when(doctorRepo.save((DoctorDbModel)notNull())).thenReturn(doctorDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/logicalDeleteDoctor")
	    	.header("token", UUID.randomUUID())
	    	.header("doctorId", 1))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void showEditDoctorProfileForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/editDoctorProfile"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("editDoctorProfile"));
	}
	
	@Test
	void editDoctorProfile_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(editProfileBasicInfoRM);
	    
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
	    when(doctorRepo.save((DoctorDbModel)notNull())).thenReturn(doctorDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/editDoctorProfile")
			.header("token", UUID.randomUUID())
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void changeDoctorPassword_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(changePasswordRM);
	    
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
	    when(doctorRepo.save((DoctorDbModel)notNull())).thenReturn(doctorDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/changeDoctorPassword")
			.header("token", UUID.randomUUID())
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void listOfPatientsPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/listOfPatients"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("listOfPatients"));
	}
	
	@Test
	void doctorWorkingCalendarPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/doctorWorkingCalendar"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("doctorWorkingCalendar"));
	}
	
	@Test
	void isRoomInUse_GET() throws Exception {
		String expected = "true";
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByCheckupTypeIdAndFreeAndFinished(
			anyInt(), 
			anyBoolean(), 
			anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/isDoctorInUse")
			.header("token", UUID.randomUUID())
	    	.header("doctorId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void checkIfDoctorCanViewMedicalRecord_GET() throws Exception {
		String expected = "true";

		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(medicalCheckupRepo.findByDoctorIdAndPatientId(
			anyInt(), 
			anyInt()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/checkIfDoctorCanViewMedicalRecord/1")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void getDoctorRatings_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(doctorRatingResMList,
	            new TypeToken<List<DoctorRatingResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findByClinicIdAndEnabled(anyInt(), anyBoolean())).thenReturn(doctorDBMList);
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(doctorRatingRepo.findByDoctorId(anyInt())).thenReturn(doctorRatingDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getDoctorAverageRatings")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}

}
