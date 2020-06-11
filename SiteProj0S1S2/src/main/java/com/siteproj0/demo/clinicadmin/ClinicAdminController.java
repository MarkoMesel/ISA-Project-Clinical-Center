package com.siteproj0.demo.clinicadmin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.home.ChangePasswordRequestModel;
import com.siteproj0.demo.home.EditProfileBasicInfoRequestModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupRegisterModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.room.RoomResponseModel;
import com.siteproj0.demo.user.EditProfileRequestModel;
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
	
	@ModelAttribute("clinicAdminChangePassword")
	public ChangePasswordRequestModel mc() {
		return new ChangePasswordRequestModel();
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
					user.getCountry(), user.getCity(), user.getStreet(), user.getPhone(), user.getJmbg(), user.getEmail(), user.isVerified(), user.getRole());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = {"/editClinicAdminProfile"})
	public String showEditClinicAdminProfileForm() {
		return "editClinicAdminProfile";
	}
	
	@PutMapping(path = "/editClinicAdminProfile")
	@ResponseBody
	public ResponseEntity editClinicAdminProfile(@RequestHeader("token") UUID securityToken,
			@RequestBody EditProfileBasicInfoRequestModel editProfileModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = repo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}

			user.setFirstName(editProfileModel.getFirstName());
			user.setLastName(editProfileModel.getLastName());
			user.setCity(editProfileModel.getCity());
			user.setStreet(editProfileModel.getStreet());
			user.setCountry(editProfileModel.getCountry());
			user.setPhone(editProfileModel.getPhone());
			user.setEmail(editProfileModel.getJmbg());
			user.setEmail(editProfileModel.getEmail());
			/*
			if (editProfileModel.getPassword()!=null && !editProfileModel.getPassword().isEmpty()) {
				user.setPassword(editProfileModel.getPassword());
			}
			*/
			repo.save(user);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = {"/changeClinicAdminPassword"})
	public String showChangeClinicAdminPasswordForm() {
		return "changeClinicAdminPassword";
	}
	
	@PutMapping(path = "/changeClinicAdminPassword")
	@ResponseBody
	public ResponseEntity changeClinicAdminPassword(@RequestHeader("token") UUID securityToken,
			@RequestBody ChangePasswordRequestModel clinicAdminModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = repo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
			String oldPassword = clinicAdminModel.getOldPassword();
			String passwordFromDb = user.getPassword();
			
			if(oldPassword.equals(passwordFromDb)) {
				user.setPassword(clinicAdminModel.getPassword());
				repo.save(user);
			} else {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
}
