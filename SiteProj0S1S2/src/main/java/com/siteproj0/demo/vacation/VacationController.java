package com.siteproj0.demo.vacation;

import java.util.ArrayList;
import java.util.List;
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
	public String registerUserAccount(@ModelAttribute("vacation") @Valid VacationRegisterModel vacation, BindingResult result) {

		if (result.hasErrors()) {
			return "sendVacationRequest";
		}
		System.out.println(vacation.getDoctorId());
		System.out.println(vacation.getStartDate());
		System.out.println(vacation.getEndDate());
		
		DoctorDbModel dr = doctorRepo.findById(vacation.getDoctorId()).get();
		
		VacationDbModel vdbm = new VacationDbModel();
		
		vdbm.setId(21);
		vdbm.setStartDate(vacation.getStartDate());
		vdbm.setEndDate(vacation.getEndDate());
		vdbm.setDoctor(dr);
		vdbm.setApproved(false);
		vdbm.setEnabled(true);
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
	
	@PutMapping(path = "/approveVacationRequest")
	@ResponseBody
	public ResponseEntity approveVacationRequest(@RequestHeader("token") UUID securityToken,
			@RequestHeader("vrId") int vrId) {
		//System.out.println("I AM HERE!!!");
		//System.out.println("UBACENI ID JE: " + roomId );
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			VacationDbModel vdbm = repo.findById(vrId).get();
			vdbm.setApproved(true);
			repo.save(vdbm);
			
			
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping(path = "/rejectVacationRequest")
	@ResponseBody
	public ResponseEntity rejectVacationRequest(@RequestHeader("token") UUID securityToken,
			@RequestHeader("vrId") int vrId, @RequestHeader("notes") String notes) {
		//System.out.println("I AM HERE!!!");
		//System.out.println("UBACENI ID JE: " + roomId );
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			VacationDbModel vdbm = repo.findById(vrId).get();
			vdbm.setApproved(false);
			vdbm.setEnabled(false);
			repo.save(vdbm);
			
			
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
}
