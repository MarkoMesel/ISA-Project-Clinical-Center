package com.siteproj0.demo.testcontroller;

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

public class TestCheckupTypeController extends TestController {
	
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
		when(checkupTypeRepo.save((CheckupTypeDbModel)notNull())).thenReturn(checkupTypeDBM0);
		
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
	void isCtReserved_GET() throws Exception {
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
