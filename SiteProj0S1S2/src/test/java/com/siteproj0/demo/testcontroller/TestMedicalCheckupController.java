package com.siteproj0.demo.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.siteproj0.demo.checkuptype.CheckupTypeResponseModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupDoctorResponseModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupPatientResponseModel;

class TestMedicalCheckupController extends TestController {

	@Test
	void showRegisterMedicalCheckupForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/registerMedicalCheckup"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("registerMedicalCheckup"));
	}
	
	@Test
	void registerMedicalCheckup_POST() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(medicalCheckupResM);
		
		when(bindingResult.hasErrors()).thenReturn(false);
		when(checkupTypeRepo.findById(anyInt())).thenReturn(Optional.of(checkupTypeDBM0));
		when(clinicRepo.findById(anyInt())).thenReturn(Optional.of(clinicDBM));
		when(roomRepo.findById(anyInt())).thenReturn(Optional.of(roomDBM));
		when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctorDBM));
		when(medicalCheckupRepo.findByDoctorIdAndDateAndTime(
			anyInt(),
			anyString(),
			anyString()))
		.thenReturn(new ArrayList<>());
		when(medicalCheckupRepo.save((MedicalCheckupDbModel)notNull())).thenReturn(medicalCheckupDBM);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/registerMedicalCheckup")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr)
	    	.header("token", UUID.randomUUID()))
		.andExpect(redirectedUrl("/clinicManager"));
	}
	
	@Test
	void getMcFromDoctor_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(medicalCheckupDoctorRMList,
	            new TypeToken<List<MedicalCheckupDoctorResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(medicalCheckupRepo.findByClinicIdAndDoctorIdAndFree(
			anyInt(),
			anyInt(),
			anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		when(checkupTypeRepo.findById(anyInt())).thenReturn(Optional.of(checkupTypeDBM0));
		when(roomRepo.findById(anyInt())).thenReturn(Optional.of(roomDBM));
		when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patientDBM));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getReservedMcFromDoctor")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void chooseAndBeginCheckupForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/chooseAndBeginCheckup"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("chooseAndBeginCheckup"));
	}
	
	@Test
	void beginMedicalCheckupForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/beginMedicalCheckup/1"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("beginMedicalCheckup"));
	}
	
	@Test
	void saveNotesAndSendMedicalCheckupRequest_POST() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(medicalCheckupRegReqM);
		
		when(bindingResult.hasErrors()).thenReturn(false);
		when(medicalCheckupRepo.findById(anyInt())).thenReturn(Optional.of(medicalCheckupDBM));
		when(medicalCheckupRepo.findByDoctorIdAndDateAndTime(
			anyInt(),
			anyString(),
			anyString()))
		.thenReturn(new ArrayList<>());
		when(medicalCheckupRepo.save((MedicalCheckupDbModel)notNull())).thenReturn(medicalCheckupDBM);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/saveNotesAndSendMedicalCheckupRequest")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr))
	    .andExpect(redirectedUrl("/chooseAndBeginCheckup"));
	}
	
	@Test
	void saveEndNotes_PUT() throws Exception {
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(medicalCheckupRepo.findById(anyInt())).thenReturn(Optional.of(medicalCheckupDBM));
		when(medicalCheckupRepo.save((MedicalCheckupDbModel)notNull()))
		.thenReturn(medicalCheckupDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/saveEndNotes")
			.header("token", UUID.randomUUID())
			.header("mcId", 1)
			.header("notes", "TestNotes"))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void sendMedicalCheckupRequestForm_POST() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(medicalCheckupResM);
	    
		when(bindingResult.hasErrors()).thenReturn(false);
		when(checkupTypeRepo.findById(anyInt())).thenReturn(Optional.of(checkupTypeDBM0));
		when(clinicRepo.findById(anyInt())).thenReturn(Optional.of(clinicDBM));
		when(roomRepo.findById(anyInt())).thenReturn(Optional.of(roomDBM));
		when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctorDBM));
		when(medicalCheckupRepo.save((MedicalCheckupDbModel)notNull())).thenReturn(medicalCheckupDBM);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/sendMedicalCheckupRequestForm")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr)
	    	.header("token", UUID.randomUUID()))
		.andExpect(redirectedUrl("/clinicManager"));
	}
	
	@Test
	void getMcFromDoctorAndPatient_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(medicalCheckupDoctorRMNotFinishedList,
	            new TypeToken<List<MedicalCheckupDoctorResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(medicalCheckupRepo.findByClinicIdAndDoctorIdAndPatientIdAndFreeAndFinished(
			anyInt(),
			anyInt(),
			anyInt(),
			anyBoolean(),
			anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		when(checkupTypeRepo.findById(anyInt())).thenReturn(Optional.of(checkupTypeDBM0));
		when(roomRepo.findById(anyInt())).thenReturn(Optional.of(roomDBM));
		when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patientDBM));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getReservedMcFromDoctorAndPatient/1")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void getMcFromPatient_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(medicalCheckupPatientRMList,
	            new TypeToken<List<MedicalCheckupPatientResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(doctorRepo.findBySecurityToken((UUID)notNull())).thenReturn(doctorDBM);
		when(medicalCheckupRepo.findByClinicIdAndPatientId(
			anyInt(), 
			anyInt()))
		.thenReturn(medicalCheckupDBMList);
		when(checkupTypeRepo.findById(anyInt())).thenReturn(Optional.of(checkupTypeDBM0));
		when(roomRepo.findById(anyInt())).thenReturn(Optional.of(roomDBM));
		when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctorDBM));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getMcFromPatient/1")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void checkupRequestManagerForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/checkupRequestManager"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("checkupRequestManager"));
	}
	
	@Test
	void getCheckupsWithNoRoom_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(medicalCheckupRoomRMList,
	            new TypeToken<List<MedicalCheckupDoctorResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByRoomIdIsNull()).thenReturn(medicalCheckupDBMWithNoRoomList);
		when(checkupTypeRepo.findById(anyInt())).thenReturn(Optional.of(checkupTypeDBM0));
		when(patientRepo.findById(anyInt())).thenReturn(Optional.of(patientDBM));
		when(doctorRepo.findById(anyInt())).thenReturn(Optional.of(doctorDBM));
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getCheckupsWithNoRoom")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
		
	}
	
	@Test
	void findRoomForCheckupForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/findRoomForCheckup/1"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("findRoomForCheckup"));
	}
	
	@Test
	void saveRoomAndDate_PUT() throws Exception {
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findById(anyInt())).thenReturn(Optional.of(medicalCheckupDBM));
		when(medicalCheckupRepo.findByRoomIdAndDateAndTime(
			anyInt(),
			anyString(),
			anyString()))
		.thenReturn(medicalCheckupDBMListWithOnlyOneElement);
		when(roomRepo.findById(anyInt())).thenReturn(Optional.of(roomDBM));
		when(medicalCheckupRepo.save((MedicalCheckupDbModel)notNull())).thenReturn(medicalCheckupDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/saveRoomAndDate")
			.header("token", UUID.randomUUID())
			.header("mcId", 1)
			.header("roomId", 1)
			.header("chosenDate", "2020-10-10"))
		.andExpect(MockMvcResultMatchers.status().isNoContent());
	}

}
