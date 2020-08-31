package com.siteproj0.demo.testcontroller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
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
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.room.RoomResponseModel;

class TestRoomController extends TestController {

	@Test
	void showRoomManagerPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/roomManager"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("roomManager"));
	}

	@Test
	void getRoomsFromClinicAdmin_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(roomResMList,
	            new TypeToken<List<RoomResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByClinicIdAndEnabled(anyInt(), anyBoolean())).thenReturn(roomDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getRoomsFromClinicAdmin")
			.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void showAddRoomForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/addRoom"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("addRoom"));
	}
	
	@Test
	void addNewRoom_POST() throws Exception {
		when(clinicRepo.findById(anyInt())).thenReturn(Optional.of(clinicDBM));
		when(roomRepo.save((RoomDbModel)notNull())).thenReturn(roomDBM);
		when(bindingResult.hasErrors()).thenReturn(false);
		
	    mockMvc.perform(MockMvcRequestBuilders.post("/addRoom")
	    	.flashAttr("room", roomRegM))
		.andExpect(redirectedUrl("/roomManager"));
	}
	
	@Test
	void showEditRoomForm_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/editRoom/1"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("editRoom"));
	}
	
	@Test
	void getRoomInfo_GET() throws Exception {
		String expected = new Gson().toJson(roomResM);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByIdAndClinicId(anyInt(), anyInt())).thenReturn(roomDBM);
		
	    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getRoomInfo/1")
	    	.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isOk())
		.andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void editRoomInfo_PUT() throws Exception {
		Gson gson = new Gson();
	    String jsonStr = gson.toJson(roomResM);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByIdAndClinicId(anyInt(), anyInt())).thenReturn(roomDBM);
	    when(roomRepo.save((RoomDbModel)notNull())).thenReturn(roomDBM);
	    
	    mockMvc.perform(MockMvcRequestBuilders.put("/editRoomInfo")
	    	.contentType(MediaType.APPLICATION_JSON)
	    	.content(jsonStr)
	    	.header("token", UUID.randomUUID()))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void logicalDeleteRoom_PUT() throws Exception {
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByIdAndClinicIdAndEnabled(anyInt(), anyInt(), anyBoolean())).thenReturn(roomDBM);
	    when(roomRepo.save((RoomDbModel)notNull())).thenReturn(roomDBM);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/logicalDeleteRoom")
	    	.header("token", UUID.randomUUID())
	    	.header("roomId", 1))
	    .andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	void findRoomNameNumber_GET() throws Exception {
		String expected = new Gson().toJson(roomResM);
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByNameAndNumberAndClinicIdAndEnabled(
			anyString(),
			anyString(),
			anyInt(),
			anyBoolean()))
		.thenReturn(roomDBM);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findRoomNameNumber")
			.header("token", UUID.randomUUID())
			.header("name", "A")
			.header("number", "1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		    
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void isRoomInUse_GET() throws Exception {
		String expected = "true";

		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByRoomIdAndFinished(
				anyInt(),
				anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/isRoomInUse")
			.header("token", UUID.randomUUID())
	    	.header("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void isRoomReserved_GET() throws Exception {		
		String expected = "true";
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByRoomIdAndFreeAndFinished(
				anyInt(),
				anyBoolean(),
				anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/isRoomReserved")
			.header("token", UUID.randomUUID())
	    	.header("roomId", 1))
		.andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
		
		String result = mvcResult.getResponse().getContentAsString();

		assertEquals(expected,result);
	}
	
	@Test
	void findRoomByName_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(roomResMList,
	            new TypeToken<List<RoomResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByNameAndClinicIdAndEnabled(
			anyString(),
			anyInt(),
			anyBoolean()))
		.thenReturn(roomDBMList);
		when(medicalCheckupRepo.findByRoomIdAndDate(
			anyInt(), 
			anyString()))
		.thenReturn(new ArrayList<>());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findRoomByName")
			.header("token", UUID.randomUUID())
			.header("searchByThis", "A")
			.header("rDate", "2020-11-10"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void findRoomByNumber_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(roomResMList,
	            new TypeToken<List<RoomResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(roomRepo.findByNumberAndClinicIdAndEnabled(
			anyString(),
			anyInt(),
			anyBoolean()))
		.thenReturn(roomDBMList);
		when(medicalCheckupRepo.findByRoomIdAndDate(
			anyInt(), 
			anyString()))
		.thenReturn(new ArrayList<>());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/findRoomByNumber")
			.header("token", UUID.randomUUID())
			.header("searchByThis", "1")
			.header("rDate", "2020-11-10"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
	
	@Test
	void getBusyDates_GET() throws Exception {
		JsonArray expectedJsonArray = (JsonArray) new Gson().toJsonTree(busyDateRMList,
	            new TypeToken<List<RoomResponseModel>>() {
	            }.getType());
		
		String expected = expectedJsonArray.toString();
		
		when(clinicAdminRepo.findBySecurityToken((UUID)notNull())).thenReturn(clinicAdminDBM);
		when(medicalCheckupRepo.findByRoomIdAndFinished(
			anyInt(),
			anyBoolean()))
		.thenReturn(medicalCheckupDBMList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getBusyDates/1")
			.header("token", UUID.randomUUID())
			.header("searchByThis", "1")
			.header("rDate", "2020-11-10"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andReturn();
	    
		String result = mvcResult.getResponse().getContentAsString();
		
		assertEquals(expected,result);
	}
}
