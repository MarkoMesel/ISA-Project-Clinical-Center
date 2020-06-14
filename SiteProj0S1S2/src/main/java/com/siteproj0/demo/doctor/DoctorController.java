package com.siteproj0.demo.doctor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteproj0.demo.clinic.ClinicResponseModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.home.ChangePasswordRequestModel;
import com.siteproj0.demo.home.EditProfileBasicInfoRequestModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;
import com.siteproj0.demo.user.LoginModel;
import com.siteproj0.demo.user.LoginResponseModel;
import com.siteproj0.demo.user.ProfileResponseModel;
import com.siteproj0.demo.user.UserRegisterModel;

@Controller
public class DoctorController {

	@Autowired
	DoctorRepo doctorRepo;
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@Autowired
	ClinicRepo clinicRepo;
	
	@Autowired
	MedicalCheckupRepo mcRepo;
	
	@ModelAttribute("doctor")
	public DoctorRegisterModel doctor() {
		return new DoctorRegisterModel();
	}
	
	@ModelAttribute("changeDoctorPassword")
	public ChangePasswordRequestModel mc() {
		return new ChangePasswordRequestModel();
	}
	
	/*
	 * Prikazi login page
	 */
	@GetMapping(path = "/doctorLogin")
	public String doctorLoginPage() {
		return "doctorlogin";
	}
	
	@ModelAttribute("loginUser")
	public LoginModel loginUser() {
		return new LoginModel();
	}
	
	/*
	 * Uloguj usera
	 */
	@PostMapping(path = "/doctorLogin")
	@ResponseBody
	public ResponseEntity<LoginResponseModel> login(@RequestBody LoginModel loginModel) {
		DoctorDbModel user = doctorRepo.findByEmailAndPassword(loginModel.getEmail(), loginModel.getPassword());
		return new ResponseEntity<>(new LoginResponseModel(user.getSecurityToken()), HttpStatus.OK);
	}
	
	/*
	 * Nabavi profil usera
	 */
	@GetMapping(path = "/getDoctorProfile")
	public ResponseEntity<ProfileResponseModel> getProfileInformation(@RequestHeader("token") UUID securityToken) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			ProfileResponseModel result = new ProfileResponseModel(user.getId(), user.getFirstName(), user.getLastName(),
					user.getCountry(), user.getCity(), user.getStreet(), user.getPhone(), user.getJmbg(), user.getEmail(), user.isVerified(), user.getRole());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/doctorManager")
	public String showDoctorManagerPage() {
		return "doctorManager";
	}
	
	@GetMapping(path = "/getDoctorsFromClinicAdmin")
	public ResponseEntity<Object> getDoctorsFromClinicAdmin(@RequestHeader("token") UUID securityToken) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			
			List<DoctorDbModel> doctors = doctorRepo.findByClinicIdAndEnabled(clinicId, true);
			//System.out.println("NASAO JE OVOLIKO ELEMENATA: " + doctors.size());
			
			List<DoctorResponseModel> doctorResponseList = new ArrayList<DoctorResponseModel>();
			for (DoctorDbModel d : doctors) {
	            DoctorResponseModel drm = new DoctorResponseModel
	            		(d.getId(),
	            		d.getFirstName(),
	            		d.getLastName(),
	            		d.getJmbg(),
	            		d.getCountry(),
	            		d.getCity(),
	            		d.getStreet(),
	            		d.getEmail(),
	            		d.getPhone(),
	            		d.getRating(),
	            		d.getShiftStart(),
	            		d.getShiftEnd(),
	            		clinicId);
	            doctorResponseList.add(drm);
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(doctorResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/registerDoctor")
	public String showDoctorRegistrationForm(Model model) {
		return "registerDoctor";
	}
	
	@PostMapping(path = "/registerDoctor")
	public String registerDoctorAccount(@ModelAttribute("doctor") @Valid DoctorRegisterModel doctor, BindingResult result) {
		if (result.hasErrors()) {
			return "registerDoctor";
		}

		ClinicDbModel clinic = clinicRepo.findById(doctor.getClinicId()).get();
		
		DoctorDbModel doctorDbModel = new DoctorDbModel();
		
		doctorDbModel.setId(21);
		doctorDbModel.setJmbg(doctor.getJmbg());
		doctorDbModel.setCity(doctor.getCity());
		doctorDbModel.setCountry(doctor.getCountry());
		doctorDbModel.setEmail(doctor.getEmail());
		doctorDbModel.setFirstName(doctor.getFirstName());
		doctorDbModel.setLastName(doctor.getLastName());
		doctorDbModel.setPassword(doctor.getPassword());
		doctorDbModel.setPhone(doctor.getPhone());
		doctorDbModel.setStreet(doctor.getStreet());
		doctorDbModel.setShiftStart(doctor.getShiftStart());
		doctorDbModel.setShiftEnd(doctor.getShiftEnd());
		doctorDbModel.setVerified(true);
		doctorDbModel.setValidationToken(null);
		doctorDbModel.setSecurityToken(UUID.randomUUID());
		doctorDbModel.setRole("DOCTOR");
		doctorDbModel.setClinic(clinic);
		doctorDbModel.setEnabled(true);

		doctorRepo.save(doctorDbModel);
		
		return "redirect:/doctorManager";
	}
	
	@GetMapping(path = "/findDoctorFirstNameLastNameRating")
	public ResponseEntity<Object> findDoctorFirstNameLastNameRating(@RequestHeader("token") UUID securityToken,
																	@RequestHeader("firstName") String firstName,
																	@RequestHeader("lastName") String lastName,
																	@RequestHeader("rating") float rating) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			
			List<DoctorDbModel> doctors = doctorRepo.findByFirstNameAndLastNameAndRatingAndClinicIdAndEnabled(firstName,
																											lastName,
																											rating,
																											clinicId,
																											true);
			
			DoctorDbModel d= doctors.get(0);
            DoctorResponseModel drm = new DoctorResponseModel
            		(d.getId(),
            		d.getFirstName(),
            		d.getLastName(),
            		d.getJmbg(),
            		d.getCountry(),
            		d.getCity(),
            		d.getStreet(),
            		d.getEmail(),
            		d.getPhone(),
            		d.getRating(),
            		d.getShiftStart(),
            		d.getShiftEnd(),
            		clinicId);
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(drm, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/logicalDeleteDoctor")
	@ResponseBody
	public ResponseEntity logicalDeleteDoctor(@RequestHeader("token") UUID securityToken,
			@RequestHeader("doctorId") int doctorId) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			List<DoctorDbModel> doctors = doctorRepo.findByIdAndClinicIdAndEnabled(doctorId, clinicId, true);
			DoctorDbModel d= doctors.get(0);
			d.setEnabled(false);
			doctorRepo.save(d);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = {"/editDoctorProfile"})
	public String showEditDoctorProfileForm() {
		return "editDoctorProfile";
	}
	
	@PutMapping(path = "/editDoctorProfile")
	@ResponseBody
	public ResponseEntity editDoctorProfile(@RequestHeader("token") UUID securityToken,
			@RequestBody EditProfileBasicInfoRequestModel editProfileModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
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
			doctorRepo.save(user);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = {"/changeDoctorPassword"})
	public String showChangeDoctorPasswordForm() {
		return "changeDoctorPassword";
	}
	
	@PutMapping(path = "/changeDoctorPassword")
	@ResponseBody
	public ResponseEntity changeDoctorPassword(@RequestHeader("token") UUID securityToken,
			@RequestBody ChangePasswordRequestModel doctorModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
			String oldPassword = doctorModel.getOldPassword();
			String passwordFromDb = user.getPassword();
			
			if(oldPassword.equals(passwordFromDb)) {
				user.setPassword(doctorModel.getPassword());
				doctorRepo.save(user);
			} else {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/listOfPatients")
	public String listOfPatientsPage() {
		return "listOfPatients";
	}
	
	@GetMapping(path = "/doctorWorkingCalendar")
	public String doctorWorkingCalendarPage() {
		return "doctorWorkingCalendar";
	}
	
	@GetMapping(path = "/isDoctorInUse")
	public ResponseEntity<Object> isRoomInUse(@RequestHeader("token") UUID securityToken,
								@RequestHeader("doctorId") int doctorId) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByCheckupTypeIdAndFreeAndFinished(doctorId,false,false);
			if(mcList.size() > 0)
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
