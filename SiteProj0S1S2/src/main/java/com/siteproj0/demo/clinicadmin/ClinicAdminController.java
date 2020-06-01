package com.siteproj0.demo.clinicadmin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.user.LoginModel;
import com.siteproj0.demo.user.LoginResponseModel;
import com.siteproj0.demo.user.ProfileResponseModel;

@Controller
public class ClinicAdminController {
	
	@Autowired
	ClinicAdminRepo repo;
	
	/*
	 * Prikazi login page
	 */
	@GetMapping(path = "/clinicAdminLogin")
	public String clinicAdminLoginPage() {
		return "clinicAdminlogin";
	}
	
	@ModelAttribute("loginUser")
	public LoginModel loginUser() {
		return new LoginModel();
	}
	
	/*
	 * Uloguj usera
	 */
	@PostMapping(path = "/clinicAdminLogin")
	@ResponseBody
	public ResponseEntity<LoginResponseModel> login(@RequestBody LoginModel loginModel) {
		ClinicAdminDbModel user = repo.findByEmailAndPassword(loginModel.getEmail(), loginModel.getPassword());
		return new ResponseEntity<>(new LoginResponseModel(user.getSecurityToken()), HttpStatus.OK);
	}
	
	/*
	 * Nabavi profil usera
	 */
	@GetMapping(path = "/getClinicAdminProfile")
	public ResponseEntity<ProfileResponseModel> getProfileInformation(@RequestHeader("token") UUID securityToken) {
		try {
			ClinicAdminDbModel user = repo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			ProfileResponseModel result = new ProfileResponseModel(user.getFirstName(), user.getLastName(),
					user.getCountry(), user.getCity(), user.getStreet(), user.getPhone(), user.getJmbg(), user.getEmail(), user.getRole());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
