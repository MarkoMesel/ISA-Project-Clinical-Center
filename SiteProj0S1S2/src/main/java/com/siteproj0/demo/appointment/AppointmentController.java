package com.siteproj0.demo.appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siteproj0.demo.clinic.ClinicViewModel;
import com.siteproj0.demo.dal.AppointmentDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.ScheduleDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.repo.AppointmentRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.ScheduleRepo;
import com.siteproj0.demo.repo.UserRepo;

@Controller
public class AppointmentController {

	@Autowired
	AppointmentRepo appointmentRepo;

	@Autowired
	ClinicRepo clinicRepo;

	@Autowired
	DoctorRepo doctorRepo;

	@Autowired
	ScheduleRepo scheduleRepo;

	@Autowired
	UserRepo userRepo;
	
	@GetMapping(path = "/myAppointments")
	public String myAppointmentsPage() {
		return "myAppointments";
	}
	
	@GetMapping(path = "/getMyAppointments")
	@ResponseBody
	public ResponseEntity<List<AppointmentViewModel>> getMyAppointments(@RequestHeader("token") UUID securityToken) {
		UserDbModel user = userRepo.findBySecurityToken(securityToken);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		List<AppointmentViewModel> result = new ArrayList<AppointmentViewModel>();
		
		List<AppointmentDbModel> dbResult = appointmentRepo.findByPatientId(user.getId()).stream().filter(n->n.isConfirmed()).collect(Collectors.toList());
		dbResult.forEach((n) -> result.add(new AppointmentViewModel(n.getId(), n.getDate(),
				n.getSchedule().getDoctor().getFirstName() + " " + n.getSchedule().getDoctor().getLastName(),
				n.getSchedule().getDoctor().getClinic().getName(), n.getPrice(), n.getDiscount())));
		return new ResponseEntity<>(result,HttpStatus.OK);
	}

	@GetMapping(path = "/clinicsAtDate/{date}")
	public ResponseEntity<List<ClinicViewModel>> apc1Request(@PathVariable("date") String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date date;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<ClinicViewModel> result = new ArrayList<ClinicViewModel>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		List<ScheduleDbModel> schedules = scheduleRepo.findByDay(dayOfWeek);

		schedules.forEach((n) -> {

			List<AppointmentDbModel> appointments = n.getAppointments().stream()
					.filter(c -> sameTime(c.getDate(), date, n.getTime())).collect(Collectors.toList());
			if (appointments.isEmpty()) {
				if (result.stream().filter(c -> c.getId() == n.getDoctor().getClinic().getId())
						.collect(Collectors.toList()).isEmpty()) {
					result.add(new ClinicViewModel(n.getDoctor().getClinic()));
				}
			}
		});

		return new ResponseEntity<List<ClinicViewModel>>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/availableAppointmentsByDateAndClinic/{date}/{clinicId}")
	public ResponseEntity<List<AvailableAppointmentViewModel>> apc2Request(@PathVariable("date") String dateString,
			@PathVariable("clinicId") String clinicIdString) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Date date;
		int clinicId;
		try {
			date = formatter.parse(dateString);
			clinicId = Integer.parseInt(clinicIdString);
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<AvailableAppointmentViewModel> result = new ArrayList<AvailableAppointmentViewModel>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		List<ScheduleDbModel> schedules = scheduleRepo.findByDay(dayOfWeek).stream()
				.filter(n -> n.getDoctor().getClinic().getId() == clinicId).collect(Collectors.toList());

		schedules.forEach((n) -> {

			List<AppointmentDbModel> appointments = n.getAppointments().stream()
					.filter(c -> sameTime(c.getDate(), date, n.getTime())).collect(Collectors.toList());
			if (appointments.isEmpty()) {
				Calendar appointmentCalendar = Calendar.getInstance();
				appointmentCalendar.setTime(date);
				appointmentCalendar.set(Calendar.HOUR_OF_DAY, n.getTime());
				appointmentCalendar.set(Calendar.MINUTE, 0);
				appointmentCalendar.set(Calendar.MILLISECOND, 0);
				appointmentCalendar.set(Calendar.ZONE_OFFSET, 0);

				String doctorName = n.getDoctor().getFirstName() + " " + n.getDoctor().getLastName();

				result.add(new AvailableAppointmentViewModel(appointmentCalendar.getTime(), doctorName, n.getId(),
						n.getDoctor().getClinic().getName(), n.getDoctor().getClinic().getPrice()));
			}
		});

		return new ResponseEntity<List<AvailableAppointmentViewModel>>(result, HttpStatus.OK);
	}

	private boolean sameTime(Date appointmentDate, Date requestedDate, int scheduleTime) {

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(appointmentDate);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(requestedDate);

		boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean sameHour = cal1.get(Calendar.HOUR_OF_DAY) == scheduleTime;
		return sameDay && sameHour;
	}

	@GetMapping(path = "/unreservedPredefinedAppointments/{clinicId}")
	@ResponseBody
	public List<AppointmentViewModel> getUnreservedPredefinedAppointments(@PathVariable int clinicId) {
		List<AppointmentViewModel> result = new ArrayList<AppointmentViewModel>();
		List<AppointmentDbModel> dbResult = appointmentRepo.findByPatientId(null).stream()
				.filter(n -> n.getSchedule().getDoctor().getClinic().getId() == clinicId).collect(Collectors.toList());
		dbResult.forEach((n) -> result.add(new AppointmentViewModel(n.getId(), n.getDate(),
				n.getSchedule().getDoctor().getFirstName() + " " + n.getSchedule().getDoctor().getLastName(),
				n.getSchedule().getDoctor().getClinic().getName(), n.getPrice(), n.getDiscount())));
		return result;
	}

	@PutMapping(path = "/reservePredefinedAppointment/{appointmentId}")
	@ResponseBody
	public ResponseEntity reservePredefinedAppointments(@RequestHeader("token") UUID securityToken,
			@PathVariable("appointmentId") String appointmentIdString) {
		
		int appointmentId;
		try {
			appointmentId = Integer.parseInt(appointmentIdString);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		UserDbModel user = userRepo.findBySecurityToken(securityToken);
		if (user == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		Optional<AppointmentDbModel> appointment = appointmentRepo.findById(appointmentId);
		if (!appointment.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		if (appointment.get().getPatient() != null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		appointment.get().setPatient(user);
		appointment.get().setConfirmed(true);
		appointmentRepo.save(appointment.get());
		
		// send email
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
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));

			// Set Subject: header field
			message.setSubject("Isa - Appointment confirmed");

			// Now set the actual message
			message.setText("Appointment confirmed: "+appointment.get().getDate()+", "+appointment.get().getSchedule().getDoctor().getClinic().getName());

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@PostMapping(path = "/requestAppointment")
	@ResponseBody
	public ResponseEntity requestAppointment(@RequestHeader("token") UUID securityToken,
			@RequestBody AppointmentRequestModel appointmentRequestModel) {
		UserDbModel user = userRepo.findBySecurityToken(securityToken);
		if (user == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		Optional<ScheduleDbModel> schedule = scheduleRepo.findById(appointmentRequestModel.getScheduleId());
		if (!schedule.isPresent()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Calendar appointmentCalendar = Calendar.getInstance();
		appointmentCalendar.setTime(appointmentRequestModel.getDate());
		appointmentCalendar.set(Calendar.HOUR_OF_DAY, schedule.get().getTime());
		appointmentCalendar.set(Calendar.MINUTE, 0);
		appointmentCalendar.set(Calendar.MILLISECOND, 0);

		AppointmentDbModel appointment = new AppointmentDbModel(appointmentCalendar.getTime(), schedule.get(), user,
				schedule.get().getDoctor().getClinic().getPrice(), null, false);
		appointmentRepo.save(appointment);

		// send email
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
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));

			// Set Subject: header field
			message.setSubject("Isa - Confirm appointment");

			// Now set the actual message
			message.setText("Confirm: " + "http://localhost:8080/confirm/" + appointment.getId() + "\n" + "Cancel: "
					+ "http://localhost:8080/cancel/" + appointment.getId(), "utf-8");

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@GetMapping(path = "/confirm/{appointmentId}")
	public String confirm(@PathVariable("appointmentId") String appointmentIdString) {
		int appointmentId;
		try {
			appointmentId = Integer.parseInt(appointmentIdString);
		} catch (Exception e) {
			return "redirect:/myAppointments";
		}
		
		Optional<AppointmentDbModel> appointment = appointmentRepo.findById(appointmentId);
		if (appointment.isPresent()) {
			appointment.get().setConfirmed(true);
			appointmentRepo.save(appointment.get());
		}
		return "redirect:/myAppointments";
	}

	@GetMapping(path = "/cancel/{appointmentId}")
	public String cancel(@PathVariable("appointmentId") String appointmentIdString) {
		int appointmentId;
		try {
			appointmentId = Integer.parseInt(appointmentIdString);
		} catch (Exception e) {
			return "redirect:/myAppointments";
		}
		Optional<AppointmentDbModel> appointment = appointmentRepo.findById(appointmentId);
		if (appointment.isPresent()) {
			appointmentRepo.delete(appointment.get());
		}
		return "redirect:/myAppointments";
	}
	
	@DeleteMapping(path = "/cancel/{appointmentId}")
	public ResponseEntity delete(@PathVariable("appointmentId") String appointmentIdString) {
		int appointmentId;
		try {
			appointmentId = Integer.parseInt(appointmentIdString);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		Optional<AppointmentDbModel> appointment = appointmentRepo.findById(appointmentId);
		if (appointment.isPresent()) {
			appointmentRepo.delete(appointment.get());
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
