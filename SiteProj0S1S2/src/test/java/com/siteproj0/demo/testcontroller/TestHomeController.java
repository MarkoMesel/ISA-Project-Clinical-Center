package com.siteproj0.demo.testcontroller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class TestHomeController extends TestController {

	@Test
	void showHome_EmptyUrl_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get(""))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("home"));
	}
	
	@Test
	void showHome_ForwardSlashUrl_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("home"));
	}
	
	@Test
	void showHome_homeUrl_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/home"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("home"));
	}
	
	@Test
	void showDoctorHome_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/doctorHome"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("doctorHome"));
	}
	
	@Test
	void showClinicAdminHome_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/clinicAdminHome"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("clinicAdminHome"));
	}
	
	@Test
	void showWhatAreYouPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/whatAreYou"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("whatAreYou"));
	}
	
	@Test
	void showNotVerifiedPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/notVerified"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("notVerified"));
	}
	
	@Test
	void showNotAuthorizedPage_GET() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/notAuthorized"))
	    .andExpect(MockMvcResultMatchers.status().isOk())
	    .andExpect(view().name("notAuthorized"));
	}
}
