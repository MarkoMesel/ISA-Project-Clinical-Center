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
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.doctor.DoctorResponseModel;
import com.siteproj0.demo.user.UserResponseModel;

class TestUserController extends TestController {

	@Test
	void loginPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/login"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("login"));
	}
	
	@Test
	void editProfilePage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/editProfile"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("editprofile"));
	}
	
	@Test
	void getProfileInformation_GET() throws Exception {
		profileRM.setRole("USER");
		String expected = new Gson().toJson(profileRM);
		
		when(patientRepo.findBySecurityToken((UUID)notNull())).thenReturn(patientDBM);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getProfile")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void login_POST() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(loginM);
	    
	    loginRM.setToken(patientDBM.getSecurityToken());
		String expected = new Gson().toJson(loginRM);
		
		when(patientRepo.findByEmailAndPassword(anyString(), anyString())).thenReturn(patientDBM);
	    
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void showRegistrationForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("registration"));
	}
	
	@Test
	void editProfile_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(editProfileRM);
	    
		when(patientRepo.findBySecurityToken((UUID)notNull())).thenReturn(patientDBM);
	    when(patientRepo.save((UserDbModel)notNull())).thenReturn(patientDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/editProfile")
			.header("token", UUID.randomUUID())
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void verify_GET() throws Exception {
		when(patientRepo.findByValidationToken((UUID)notNull())).thenReturn(patientDBM);
	    when(patientRepo.save((UserDbModel)notNull())).thenReturn(patientDBM);
	    
		mockMvc.perform(MockMvcRequestBuilders.get("/verify/{token}", UUID.randomUUID()))
		.andExpect(redirectedUrl("/home?success"));
	}
	
	@Test
	void getPatientsFromDoctor_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(patientResMList,
	            new TypeToken<List<UserResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(patientRepo.findByIsVerifiedAndClinicIdAndEnabled(
			anyBoolean(), 
			anyInt(), 
			anyBoolean()))
		.thenReturn(patientDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getPatientsFromDoctor")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void findPatientsByFirstName_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(patientResMList,
	            new TypeToken<List<UserResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(patientRepo.findByFirstNameAndClinicIdAndIsVerifiedAndEnabled(anyString(),
			anyInt(),
			anyBoolean(),
			anyBoolean()))
		.thenReturn(patientDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findPatientsByFirstName")
	    	.header("token", UUID.randomUUID())
	    	.header("findByThis", "TestFirstName"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void findPatientsByLastName_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(patientResMList,
	            new TypeToken<List<UserResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(patientRepo.findByLastNameAndClinicIdAndIsVerifiedAndEnabled(anyString(),
			anyInt(),
			anyBoolean(),
			anyBoolean()))
		.thenReturn(patientDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findPatientsByLastName")
	    	.header("token", UUID.randomUUID())
	    	.header("findByThis", "TestFirstName"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void findPatientsByJmbg_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(patientResMList,
	            new TypeToken<List<UserResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(patientRepo.findByJmbgAndClinicIdAndIsVerifiedAndEnabled(anyString(),
			anyInt(),
			anyBoolean(),
			anyBoolean()))
		.thenReturn(patientDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findPatientsByJmbg")
	    	.header("token", UUID.randomUUID())
	    	.header("findByThis", "TestFirstName"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void showUserProfileForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/userProfile/1"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("userProfile"));
	}
	
	@Test
	void getUserInfo_GET() throws Exception {
		String expected = new Gson().toJson(patientResM);
		
		when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patientDBM));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getUserInfo/1")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void showMedicalRecordForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/medicalRecord/1"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("medicalRecord"));
	}
}
