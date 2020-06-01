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

import java.util.UUID;

import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.repo.UserRepo;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Controller
public class UserController {

	@Autowired
	UserRepo repo;

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

			ProfileResponseModel result = new ProfileResponseModel(user.getFirstName(), user.getLastName(),
					user.getCountry(), user.getCity(), user.getStreet(), user.getPhone(), user.getJmbg(), user.getEmail(), user.getRole());
			
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
		userDbModel.setIsVerified(false);
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
		user.setIsVerified(true);
		user.setValidationToken(null);
		repo.save(user);
		return "redirect:/home?success";
	}
}
