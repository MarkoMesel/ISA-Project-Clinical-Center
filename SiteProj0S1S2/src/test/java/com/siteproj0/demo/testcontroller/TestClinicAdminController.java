package com.siteproj0.demo.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;

class TestClinicAdminController extends TestController {

	@Test
	void clinicAdminLoginPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/clinicAdminLogin"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("clinicAdminlogin"));
	}
	
	@Test
	void login_POST() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(loginM);
	    
	    loginRM.setToken(clinicAdminDBM.getSecurityToken());
		String expected = new Gson().toJson(loginRM);
		
		when(clinicAdminRepo.findByEmailAndPassword(anyString(), anyString())).thenReturn(clinicAdminDBM);
	    
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/clinicAdminLogin")
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void getProfileInformation_GET() throws Exception {
		profileRM.setRole("CLINICADMIN");
		String expected = new Gson().toJson(profileRM);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getClinicAdminProfile")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void showEditClinicAdminProfileForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/editClinicAdminProfile"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("editClinicAdminProfile"));
	}
	
	@Test
	void editClinicAdminProfile_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(editProfileBasicInfoRM);
	    
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
	    when(clinicAdminRepo.save((ClinicAdminDbModel)notNull())).thenReturn(clinicAdminDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/editClinicAdminProfile")
			.header("token", UUID.randomUUID())
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void showChangeClinicAdminPasswordForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/changeClinicAdminPassword"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("changeClinicAdminPassword"));
	}
	
	@Test
	void changeClinicAdminPassword_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(changePasswordRM);
	    
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
	    when(clinicAdminRepo.save((ClinicAdminDbModel)notNull())).thenReturn(clinicAdminDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/changeClinicAdminPassword")
			.header("token", UUID.randomUUID())
    		.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

}
