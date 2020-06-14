package com.siteproj0.demo.home;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
	
	/*	
	Radi, samo treba za account da se na linku:
	https://myaccount.google.com/lesssecureapps?pli=1
	ukljuci "Allow less secure apps" na ON
*/
	@GetMapping(path = "/sendMailTest")
	public String registerUserAccount() {
		// Sender's email ID needs to be mentioned
		String from = ""; //IME ACCOUNT-A

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

				return new PasswordAuthentication(from, ""); //PASSWORD`

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
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("mmarkom0001@gmail.com"));

			// Set Subject: header field
			message.setSubject("Isa");

			// Now set the actual message
			message.setText("Activation link: " + "http://localhost:8080/verify/" + "NE_ZNAM_VISE");

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return "redirect:/registration?success";
	}
	
}
