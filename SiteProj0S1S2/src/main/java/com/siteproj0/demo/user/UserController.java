package com.siteproj0.demo.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siteproj0.demo.checkuptype.CheckupTypeResponseModel;
import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.doctor.DoctorResponseModel;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.UserRepo;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Controller
public class UserController {

	@Autowired
	UserRepo repo;
	
	@Autowired
	DoctorRepo doctorRepo;

	@ModelAttribute("user")
	public UserRegisterModel user() {
		return new UserRegisterModel();
	}

	@ModelAttribute("loginUser")
	public LoginModel loginUser() {
		return new LoginModel();
	}
	
	/*
	 * Prikazi login page
	 */
	@GetMapping(path = "/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping(path = "/editProfile")
	public String editProfilePage() {
		return "editprofile";
	}

	/*
	 * Nabavi profil usera
	 */
	@GetMapping(path = "/getProfile")
	public ResponseEntity<ProfileResponseModel> getProfileInformation(@RequestHeader("token") UUID securityToken) {
		try {
			UserDbModel user = repo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			ProfileResponseModel result = new ProfileResponseModel(user.getId(), user.getFirstName(), user.getLastName(),
					user.getCountry(), user.getCity(), user.getStreet(), user.getPhone(), user.getJmbg(), user.getEmail(), user.isVerified(), user.getRole(), user.isFirstLogin());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * Uloguj usera
	 */
	@PostMapping(path = "/login")
	@ResponseBody
	public ResponseEntity<LoginResponseModel> login(@RequestBody LoginModel loginModel) {
		UserDbModel user = repo.findByEmailAndPassword(loginModel.getEmail(), loginModel.getPassword());
		return new ResponseEntity<>(new LoginResponseModel(user.getSecurityToken()), HttpStatus.OK);
	}

	@GetMapping(path = "/registration")
	public String showRegistrationForm(Model model) {
		return "registration";
	}

	@PostMapping(path = "/registration")
	public String registerUserAccount(@ModelAttribute("user") @Valid UserRegisterModel user, BindingResult result) {

		if (result.hasErrors()) {
			return "registration";
		}

		UserDbModel userDbModel = new UserDbModel();
		userDbModel.setId(21);
		userDbModel.setJmbg(user.getJmbg());
		userDbModel.setCity(user.getCity());
		userDbModel.setCountry(user.getCountry());
		userDbModel.setEmail(user.getEmail());
		userDbModel.setFirstName(user.getFirstName());
		userDbModel.setLastName(user.getLastName());
		userDbModel.setPassword(user.getPassword());
		userDbModel.setPhone(user.getPhone());
		userDbModel.setStreet(user.getStreet());
		userDbModel.setVerified(false);
		userDbModel.setValidationToken(UUID.randomUUID());
		userDbModel.setSecurityToken(UUID.randomUUID());
		userDbModel.setRole("USER");

		repo.save(userDbModel);

		// Sender's email ID needs to be mentioned
		String from = "isaklinike@gmail.com";

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, "isaKlinike20");

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(userDbModel.getEmail()));

			// Set Subject: header field
			message.setSubject("Isa");

			// Now set the actual message
			message.setText("Activation link: " + "http://localhost:8080/verify/" + userDbModel.getValidationToken());

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return "redirect:/registration?success";
	}

	
	@PutMapping(path = "/editProfile")
	@ResponseBody
	public ResponseEntity editProfile(@RequestHeader("token") UUID securityToken,
			@RequestBody EditProfileRequestModel editProfileModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			UserDbModel user = repo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}

			user.setFirstName(editProfileModel.getFirstName());
			user.setLastName(editProfileModel.getLastName());
			user.setCity(editProfileModel.getCity());
			user.setStreet(editProfileModel.getStreet());
			user.setCountry(editProfileModel.getCountry());
			user.setPhone(editProfileModel.getPhone());

			if (editProfileModel.getPassword()!=null && !editProfileModel.getPassword().isEmpty()) {
				user.setPassword(editProfileModel.getPassword());
			}
			repo.save(user);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping(path = "/verify/{token}")
	public String verify(@PathVariable UUID token) {

		UserDbModel user = repo.findByValidationToken(token);
		user.setVerified(true);
		user.setValidationToken(null);
		repo.save(user);
		return "redirect:/home?success";
	}
	
	@GetMapping(path = "/getPatientsFromDoctor")
	public ResponseEntity<Object> getPatientsFromDoctor(@RequestHeader("token") UUID securityToken) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			
			List<UserDbModel> users = repo.findByIsVerifiedAndClinicIdAndEnabled(true, clinicId, true);
			//System.out.println("NASAO JE OVOLIKO ELEMENATA: " + doctors.size());
			
			List<UserResponseModel> userResponseList = new ArrayList<UserResponseModel>();
			for (UserDbModel u : users) {
	            UserResponseModel urm = new UserResponseModel
	            		(u.getId(),
	            		u.getFirstName(),
	            		u.getLastName(),
	            		u.getJmbg(),
	            		u.getCountry(),
	            		u.getCity(),
	            		u.getStreet(),
	            		u.getEmail(),
	            		u.getPhone(),
	            		clinicId);
	            userResponseList.add(urm);
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(userResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findPatientsByFirstName")
	public ResponseEntity<Object> findPatientsByFirstName(@RequestHeader("token") UUID securityToken,
																	@RequestHeader("findByThis") String findByThis) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			List<UserDbModel> users = repo.findByFirstNameAndClinicIdAndIsVerifiedAndEnabled(findByThis,
																								clinicId,
																								true,
																								true);
			
			List<UserResponseModel> userResponseList = new ArrayList<UserResponseModel>();
			for (UserDbModel u : users) {
	            UserResponseModel urm = new UserResponseModel
	            		(u.getId(),
	            		u.getFirstName(),
	            		u.getLastName(),
	            		u.getJmbg(),
	            		u.getCountry(),
	            		u.getCity(),
	            		u.getStreet(),
	            		u.getEmail(),
	            		u.getPhone(),
	            		clinicId);
	            userResponseList.add(urm);
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(userResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findPatientsByLastName")
	public ResponseEntity<Object> findPatientsByLastName(@RequestHeader("token") UUID securityToken,
																	@RequestHeader("findByThis") String findByThis) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			List<UserDbModel> users = repo.findByLastNameAndClinicIdAndIsVerifiedAndEnabled(findByThis,
																								clinicId,
																								true,
																								true);
			
			List<UserResponseModel> userResponseList = new ArrayList<UserResponseModel>();
			for (UserDbModel u : users) {
	            UserResponseModel urm = new UserResponseModel
	            		(u.getId(),
	            		u.getFirstName(),
	            		u.getLastName(),
	            		u.getJmbg(),
	            		u.getCountry(),
	            		u.getCity(),
	            		u.getStreet(),
	            		u.getEmail(),
	            		u.getPhone(),
	            		clinicId);
	            userResponseList.add(urm);
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(userResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findPatientsByJmbg")
	public ResponseEntity<Object> findPatientsByJmbg(@RequestHeader("token") UUID securityToken,
																	@RequestHeader("findByThis") String findByThis) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			List<UserDbModel> users = repo.findByJmbgAndClinicIdAndIsVerifiedAndEnabled(findByThis,
																								clinicId,
																								true,
																								true);
			
			List<UserResponseModel> userResponseList = new ArrayList<UserResponseModel>();
			for (UserDbModel u : users) {
	            UserResponseModel urm = new UserResponseModel
	            		(u.getId(),
	            		u.getFirstName(),
	            		u.getLastName(),
	            		u.getJmbg(),
	            		u.getCountry(),
	            		u.getCity(),
	            		u.getStreet(),
	            		u.getEmail(),
	            		u.getPhone(),
	            		clinicId);
	            userResponseList.add(urm);
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(userResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/userProfile/{pId}")
	public String showUserProfileForm() {
		return "userProfile";
	}
	
	@GetMapping(path = "/getUserInfo/{uId}")
	public ResponseEntity<UserResponseModel> getUserInfo(@RequestHeader("token") UUID securityToken, @PathVariable int uId) {
		try {
			/*
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			*/
			/*
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			*/
			
			UserDbModel udbm = repo.findById(uId).get();
			UserResponseModel result = new UserResponseModel(
					udbm.getId(),
					udbm.getFirstName(),
					udbm.getLastName(),
					udbm.getJmbg(),
					udbm.getCountry(),
					udbm.getCity(),
					udbm.getStreet(),
					udbm.getEmail(),
					udbm.getPhone(),
					udbm.getClinic().getId());

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/medicalRecord/{uId}")
	public String showMedicalRecordForm() {
		return "medicalRecord";
	}
}
