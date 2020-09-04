package com.siteproj0.demo.vacation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.dal.VacationDbModel;
import com.siteproj0.demo.medicalcheckup.MedicalCheckupDoctorResponseModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.VacationRepo;
import com.siteproj0.demo.room.RoomRegisterModel;
import com.siteproj0.demo.user.UserRegisterModel;

@Controller
public class VacationController {
	
	/*
	 * U METODAMA "approveVacationRequest" i "rejectVacationRequest" REALIZOVANA JE 
	 * SLEDECA STAVKA:
	 * Smatra se da student nije uspešno ispunio ovaj zahtev ukoliko pored navedenih
	 * ograničenja ne pronađe i adekvatno ne reši bar još jednu konfliktnu situaciju
	 * za svoj deo funkcionalnosti propisanih specifikacijom.
	 */
	 
	@Autowired
	VacationRepo repo;
	
	@Autowired
	DoctorRepo doctorRepo;
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@ModelAttribute("vacation")
	public VacationRegisterModel vacation() {
		return new VacationRegisterModel();
	}
	
	@PostMapping(path = "/sendVacationRequest")
	public String sendVacationRequest(@ModelAttribute("vacation") @Valid VacationRegisterModel vacation, BindingResult result) {
		vacation.setStartDate(convertDateFormat(vacation.getStartDate()));
		vacation.setEndDate(convertDateFormat(vacation.getEndDate()));
		if (result.hasErrors()) {
			return "sendVacationRequest";
		}
		
		DoctorDbModel dr = doctorRepo.findById(vacation.getDoctorId()).get();
		
		VacationDbModel vdbm = new VacationDbModel();
		
		vdbm.setId(21);
		vdbm.setStartDate(vacation.getStartDate());
		vdbm.setEndDate(vacation.getEndDate());
		vdbm.setDoctor(dr);
		vdbm.setApproved(false);
		vdbm.setEnabled(true);
		vdbm.setType(vacation.getType());
		vdbm.setVersion(0);
		repo.save(vdbm);
		//return "redirect:/doctorHome?success";
		return "redirect:/doctorHome";
	}
	
	@GetMapping(path = "/sendVacationRequest")
	public String sendVacationRequestPage() {
		return "sendVacationRequest";
	}
	
	@GetMapping(path = "/vacationRequestsPendingForApproval")
	public String vacationRequestsPendingForApprovalPage() {
		return "vacationRequestsPendingForApproval";
	}
	
	@GetMapping(path = "/getPendingVacationRequests")
	public ResponseEntity<Object> getVcFromClinicAdmin(@RequestHeader("token") UUID securityToken) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			List<VacationDbModel> vList = repo.findByApprovedAndEnabled(false,true);
			
			List<VacationResponseModel> vrmList = new ArrayList<VacationResponseModel>();
			
			for(VacationDbModel v : vList) {
				//PROVERA DA LI SU DOKTOR I CLINICADMIN IZ ISTE KLINIKE
				DoctorDbModel dr = doctorRepo.findByIdAndClinicIdAndEnabled(v.getDoctor().getId(), user.getClinic().getId(), true);
				if(dr != null) {
					VacationResponseModel vrm = new VacationResponseModel(
							v.getId(),
							dr.getFirstName() + " " + dr.getLastName(),
							v.getType(),
							v.getStartDate(),
							v.getEndDate());
					vrmList.add(vrm);
				}
			}
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(vrmList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* Nakon sto je odobren odmor/odsustvo, ne moze se ponovo odobriti/odbiti. */
	@PutMapping(path = "/approveVacationRequest")
	@ResponseBody
	public ResponseEntity approveVacationRequest(@RequestHeader("token") UUID securityToken,
		@RequestHeader("vrId") int vrId) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			VacationDbModel vdbm = repo.findById(vrId).get();
				if(!vdbm.isApproved() && vdbm.isEnabled()) {
					vdbm.setApproved(true);
				}
			try {
				repo.save(vdbm);
				//Otkomentarisanjem ovoga se izazove ObjectOptimisticLockingFailureException
				/*
				VacationDbModel vdbmTest = new VacationDbModel();
				vdbmTest.setId(vdbm.getId()); 
				vdbmTest.setApproved(false);
				vdbmTest.setVersion(0); 
				repo.save(vdbmTest);
				 */
			} catch(ObjectOptimisticLockingFailureException e0) {
				System.out.println("Approval failed. It's already approved/rejected.");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	/* Nakon sto je odbijen odmor/odsustvo, ne moze se ponovo odobriti/odbiti. */
	@PutMapping(path = "/rejectVacationRequest")
	@ResponseBody
	public ResponseEntity rejectVacationRequest(@RequestHeader("token") UUID securityToken,
		@RequestHeader("vrId") int vrId, @RequestHeader("notes") String notes) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			VacationDbModel vdbm = repo.findById(vrId).get();
			if(!vdbm.isApproved() && vdbm.isEnabled()) {
				vdbm.setApproved(false);
				vdbm.setEnabled(false);
			}
			try {
				repo.save(vdbm);
				//Otkomentarisanjem ovoga se izazove ObjectOptimisticLockingFailureException
				/*
				VacationDbModel vdbmTest = new VacationDbModel();
				vdbmTest.setId(vdbm.getId()); 
				vdbmTest.setApproved(true);
				vdbmTest.setVersion(0); 
				repo.save(vdbmTest);
				 */
			} catch(ObjectOptimisticLockingFailureException e0) {
				System.out.println("Rejection failed. It's already approved/rejected.");
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			
			
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	private String convertDateFormat(String currentDateStr) {
		DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		DateFormat neededFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		Date currentDate = null;
		
		try {
			currentDate = originalFormat.parse(currentDateStr);
	      } catch (ParseException e) {
	    	  try {
	  			currentDate = neededFormat.parse(currentDateStr);
	  	      } catch (ParseException e0) {
	  	          e.printStackTrace();
	  	      }
	      }
		
		String newFormat = neededFormat.format(currentDate);
		
		return newFormat;
	}
}
