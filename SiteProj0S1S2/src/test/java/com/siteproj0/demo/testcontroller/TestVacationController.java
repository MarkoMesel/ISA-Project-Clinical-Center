package com.siteproj0.demo.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.siteproj0.demo.dal.VacationDbModel;
import com.siteproj0.demo.doctor.DoctorResponseModel;
import com.siteproj0.demo.vacation.VacationResponseModel;

class TestVacationController extends TestController {

	@Test
	void sendVacationRequest_POST() throws Exception {
		when(bindingResult.hasErrors()).thenReturn(false);
		when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctorDBM));
		when(vacationRepo.save((VacationDbModel)notNull())).thenReturn(vacationDBM);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/sendVacationRequest")
	    	.flashAttr("vacation", vacationRegM))
		.andExpect(redirectedUrl("/doctorHome"));
	}
	
	@Test
	void sendVacationRequestPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/sendVacationRequest"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("sendVacationRequest"));
	}
	
	@Test
	void vacationRequestsPendingForApprovalPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vacationRequestsPendingForApproval"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("vacationRequestsPendingForApproval"));
	}
	
	@Test
	void getVcFromClinicAdmin_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(vacationResMList,
	            new TypeToken<List<VacationResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(vacationRepo.findByApprovedAndEnabled(
			anyBoolean(),
			anyBoolean()))
		.thenReturn(vacationDBMList);
		when(doctorRepo.findByIdAndClinicIdAndEnabled(
			anyInt(), 
			anyInt(), 
			anyBoolean()))
		.thenReturn(doctorDBM);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getPendingVacationRequests")
	    	.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void approveVacationRequest_PUT() throws Exception {
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(vacationRepo.findById(anyInt())).thenReturn(Optional.of(vacationDBM));
		when(vacationRepo.save((VacationDbModel)notNull())).thenReturn(vacationDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/approveVacationRequest")
	    	.header("token", UUID.randomUUID())
	    	.header("vrId", 1))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void rejectVacationRequest_PUT() throws Exception {
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(vacationRepo.findById(anyInt())).thenReturn(Optional.of(vacationDBM));
		when(vacationRepo.save((VacationDbModel)notNull())).thenReturn(vacationDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/rejectVacationRequest")
	    	.header("token", UUID.randomUUID())
	    	.header("vrId", 1)
	    	.header("notes", "TestNotes"))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}

}
