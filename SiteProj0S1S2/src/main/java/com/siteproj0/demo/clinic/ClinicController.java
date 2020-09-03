package com.siteproj0.demo.clinic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteproj0.demo.dal.AppointmentDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.ClinicRatingDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRatingRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;
import com.siteproj0.demo.user.EditProfileRequestModel;
import com.siteproj0.demo.user.ProfileResponseModel;

@Controller
public class ClinicController {
	@Autowired
	ClinicRepo clinicRepo;
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@Autowired
	ClinicRatingRepo clinicRatingRepo;
	
	@Autowired
	MedicalCheckupRepo medicalCheckupRepo;
	
	@Autowired
	DoctorRepo doctorRepo;
	
	@GetMapping(path = "/clinic/{clinicId}")
	public String showHome() {
		return "clinic";
	}
	
	@GetMapping(path = "/apc0")
	public String appointmentCreationStep0() {
		return "appointmentCreationStep0";
	}
	
	@GetMapping(path = "/apc1/{date}")
	public String appointmentCreationStep1() {
		return "appointmentCreationStep1";
	}
	
	@GetMapping(path = "/apc2/{date}/{clinicId}")
	public String appointmentCreationStep2() {
		return "appointmentCreationStep2";
	}
	
	@GetMapping(path = "/health")
	public String healthRecord() {
		return "healthRecord";
	}
	
	@GetMapping(path = "/clinicManager")
	public String showClinicManagerPage() {
		return "clinicManager";
	}
	
	@GetMapping(path = "/getClinics")
	@ResponseBody
	public List<ClinicViewModel> getClinics()
	{
		List<ClinicViewModel> result = new ArrayList<ClinicViewModel>();
		Iterable<ClinicDbModel> clinics= clinicRepo.findAll();
		
		clinics.forEach((n)->result.add(new ClinicViewModel(n.getId(), n.getAddress(), n.getDescription(), n.getName(), n.getRating(), n.getPrice())));
		return result;
	}
	
	@GetMapping(path = "/getClinicInfo")
	public ResponseEntity<ClinicResponseModel> getClinicInfo(@RequestHeader("token") UUID securityToken) {
		try {
			ClinicDbModel clinic = null;
			
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			
			if (user == null) {
				DoctorDbModel doctor = doctorRepo.findBySecurityToken(securityToken);
				if(doctor == null) {
					return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
				}
				clinic = doctor.getClinic();
			} else {
				clinic = user.getClinic();
			}
			
			ClinicResponseModel result = new ClinicResponseModel(clinic.getId(), clinic.getName(), clinic.getDescription(), clinic.getAddress());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/clinicBasicInfo")
	public String showClinicBasicInfo() {
		return "clinicBasicInfo";
	}
	
	@PutMapping(path = "/editClinicInfo")
	@ResponseBody
	public ResponseEntity editClinicInfo(@RequestHeader("token") UUID securityToken,
			@RequestBody ClinicResponseModel clinicModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
			
			ClinicDbModel clinic = user.getClinic();
			clinic.setName(clinicModel.getName());
			clinic.setDescription(clinicModel.getDescription());
			clinic.setAddress(clinicModel.getAddress());

			clinicRepo.save(clinic);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/getClinicAverageRating")
	public ResponseEntity<Object> getClinicAverageRating(@RequestHeader("token") UUID securityToken) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			
			List<ClinicRatingDbModel> clinicRatingDBMList = clinicRatingRepo.findByClinicId(clinic.getId());
			
			float clinicRatingAverage = (float) clinicRatingDBMList.stream()
                .mapToDouble(crdbm -> crdbm.getRating())
                .average()
                .orElse(0);
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getId(), clinic.getName(), clinic.getDescription(), clinic.getAddress());
			
			return new ResponseEntity<>(clinicRatingAverage, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path="/calculateClinicIncome")
	public ResponseEntity<Object> calculateClinicIncome(
		@RequestHeader("token") UUID securityToken,
		@RequestHeader("dateFrom") String dateFrom,
		@RequestHeader("dateTo") String dateTo) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndFreeAndFinished(
				clinic.getId(),
				false,
				true);
			
			float sum = 0;
			
			for(MedicalCheckupDbModel mcdbm : mcList) {
				if(checkupWasFinishedInPeriod(mcdbm,dateFrom,dateTo))
					sum+= mcdbm.getPrice();
			}
			
			return new ResponseEntity<>(sum, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public boolean checkupWasFinishedInPeriod(MedicalCheckupDbModel mcdbm, String dateFromStr, String dateToStr) {
		Date checkupDate = parseDate(mcdbm.getDate());
		Date dateFrom = parseDate(dateFromStr);
		Date dateTo = parseDate(dateToStr);
		
		if(checkupDate.before(dateFrom) || checkupDate.after(dateTo)) {
			return false;
		}
		
		return true;
	}
	
	public Date parseDate(String dateStr) {
		DateFormat dateFormat0 = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
		Date result = null;
		
		try {
			result = dateFormat0.parse(dateStr);
	      } catch (ParseException e) {
	    	  try {
	  			result = dateFormat1.parse(dateStr);
	  	      } catch (ParseException e0) {
	  	          e.printStackTrace();
	  	      }
	      }
		
		return result;
	}
	
}
