package com.siteproj0.demo.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.siteproj0.demo.checkuptype.CheckupTypeResponseModel;
import com.siteproj0.demo.clinic.ClinicResponseModel;
import com.siteproj0.demo.clinic.ClinicViewModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;

import junit.framework.TestCase;

public class TestClinicController extends TestController {
	
	@Test
	void getClinics_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(clinicVMList,
	            new TypeToken<List<CheckupTypeResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicRepo.findAll()).thenReturn(clinicDBMIterable);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getClinics"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void getClinicInfo_GET() throws Exception {
		String expected = new Gson().toJson(clinicRM);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getClinicInfo")
			.header("token", UUID.randomUUID()))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void showClinicBasicInfo_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/clinicBasicInfo"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("clinicBasicInfo"));
	}
	
	@Test
	void editClinicInfo_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(clinicRM);
	    
	    when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
	    when(clinicRepo.save((ClinicDbModel)notNull())).thenReturn(clinicDBM);
	    
	    mockMvc.perform(MockMvcRequestBuilders.put("/editClinicInfo")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr)
	    	.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}
