package com.siteproj0.demo.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping(path = {"","/","/home"})
	public String showHome() {
		return "home";
	}
	
	@GetMapping(path = {"/doctorHome"})
	public String showDoctorHome() {
		return "doctorHome";
	}
	
	@GetMapping(path = {"/clinicAdminHome"})
	public String showClinicAdminHome() {
		return "clinicAdminHome";
	}
	
	@GetMapping(path = "/whatAreYou")
	public String showWhatAreYouPage() {
		return "whatAreYou";
	}
	
	@GetMapping(path = "/notVerified")
	public String showNotVerifiedPage() {
		return "notVerified";
	}
	
	@GetMapping(path = "/notAuthorized")
	public String showNotAuthorizedPage() {
		return "notAuthorized";
	}
	
}
