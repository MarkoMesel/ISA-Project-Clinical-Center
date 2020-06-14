package com.siteproj0.demo.medicalcheckup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.dal.UserDbModel;
import com.siteproj0.demo.doctor.DoctorRegisterModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;
import com.siteproj0.demo.repo.RoomRepo;
import com.siteproj0.demo.repo.UserRepo;
import com.siteproj0.demo.room.RoomResponseModel;
import com.siteproj0.demo.user.UserRegisterModel;
import com.siteproj0.demo.user.UserResponseModel;

@Controller
public class MedicalCheckupController {
	@Autowired
	MedicalCheckupRepo medicalCheckupRepo;
	
	@Autowired
	CheckupTypeRepo ctRepo;
	
	@Autowired
	ClinicRepo clinicRepo;
	
	@Autowired
	RoomRepo roomRepo;
	
	@Autowired
	DoctorRepo doctorRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@ModelAttribute("medicalCheckup")
	public MedicalCheckupRegisterModel mc() {
		return new MedicalCheckupRegisterModel();
	}
	
	@ModelAttribute("mc")
	public MedicalCheckupRegisterRequestModel mc2() {
		return new MedicalCheckupRegisterRequestModel();
	}
	
	@GetMapping(path = "/registerMedicalCheckup")
	public String showRegisterMedicalCheckupForm(Model model) {
		return "registerMedicalCheckup";
	}
	
	@PostMapping(path = "/registerMedicalCheckup")
	public String registerMedicalCheckup(@RequestBody MedicalCheckupResponseModel mc, BindingResult result) {
		if (result.hasErrors()) {
			return "registerDoctor";
		}

		CheckupTypeDbModel ct = ctRepo.findById(mc.getCtId()).get();
		ClinicDbModel clinic = clinicRepo.findById(mc.getClinicId()).get();
		RoomDbModel room = roomRepo.findById(mc.getRoomId()).get();
		DoctorDbModel doctor = doctorRepo.findById(mc.getDoctorId()).get();
		
		MedicalCheckupDbModel mcdbm = new MedicalCheckupDbModel();
		mcdbm.setId(21);
		mcdbm.setDate(mc.getDate());
		mcdbm.setTime(mc.getTime());
		mcdbm.setDuration(mc.getDuration());
		mcdbm.setPrice(mc.getPrice());
		mcdbm.setCheckupType(ct);
		mcdbm.setClinic(clinic);
		mcdbm.setRoom(room);
		mcdbm.setDoctor(doctor);
		mcdbm.setFree(true);
		mcdbm.setPatient(null);
		mcdbm.setNotes(mc.getNotes());
		mcdbm.setEndNotes("");
		mcdbm.setFinished(false);

		medicalCheckupRepo.save(mcdbm);
		
		return "redirect:/clinicManager";
	}
	
	@GetMapping(path = "/getReservedMcFromDoctor")
	public ResponseEntity<Object> getMcFromDoctor(@RequestHeader("token") UUID securityToken) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			Integer doctorId = user.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndDoctorIdAndFree(clinicId, doctorId, false);
			//CheckupTypeDbModel ctdbm = ctRepo.findById(mcs.)
			
			List<MedicalCheckupDoctorResponseModel> mcdrmList = new ArrayList<MedicalCheckupDoctorResponseModel>();
			for (MedicalCheckupDbModel mc : mcList) {
				if(mc.getRoom() != null) {
					CheckupTypeDbModel ctdbm = ctRepo.findById(mc.getCheckupType().getId()).get();
					RoomDbModel rdbm = roomRepo.findById(mc.getRoom().getId()).get();
					UserDbModel udbm = userRepo.findById(mc.getPatient().getId()).get();
					//System.out.println(udbm.getId());
					String finished = "No";
					if(mc.isFinished()) {
						finished = "Yes";
					}
					MedicalCheckupDoctorResponseModel mcdrm = new MedicalCheckupDoctorResponseModel(
							mc.getId(),
							mc.getDate(),
							mc.getTime(),
							mc.getDuration(),
							mc.getPrice(),
							ctdbm.getName(),
							rdbm.getName() + " - " + rdbm.getNumber(),
							udbm.getFirstName() + " " + udbm.getLastName(),
							mc.getNotes(),
							finished);
							mcdrmList.add(mcdrm);
				}
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(mcdrmList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/chooseAndBeginCheckup")
	public String chooseAndBeginCheckupForm(Model model) {
		return "chooseAndBeginCheckup";
	}
	
	@GetMapping(path = "/beginMedicalCheckup/{mcid}")
	public String beginMedicalCheckupForm(Model model) {
		return "beginMedicalCheckup";
	}
	
	@PostMapping(path = "/saveNotesAndSendMedicalCheckupRequest")
	public String saveNotesAndSendMedicalCheckupRequest(@RequestBody MedicalCheckupRegisterRequestModel mc, BindingResult result) {
		if (result.hasErrors()) {
			return "registerDoctor";
		}
		
		MedicalCheckupDbModel mcOld = medicalCheckupRepo.findById(mc.getId()).get();
		
		MedicalCheckupDbModel mcdbm = new MedicalCheckupDbModel();
		mcdbm.setId(21);
		mcdbm.setDate(mc.getDate());
		mcdbm.setTime(mc.getTime());
		mcdbm.setDuration(mcOld.getDuration());
		mcdbm.setPrice(mcOld.getPrice());
		mcdbm.setCheckupType(mcOld.getCheckupType());
		mcdbm.setClinic(mcOld.getClinic());
		mcdbm.setRoom(null);
		mcdbm.setDoctor(mcOld.getDoctor());
		mcdbm.setFree(false);
		mcdbm.setPatient(mcOld.getPatient());
		mcdbm.setNotes("Please find a room for this next checkup.");
		mcdbm.setEndNotes("");
		mcdbm.setFinished(false);
		medicalCheckupRepo.save(mcdbm);
		
		return "redirect:/doctorHome";
	}
	
	@PutMapping(path = "/saveEndNotes")
	@ResponseBody
	public ResponseEntity saveEndNotes(@RequestHeader("token") UUID securityToken,
			@RequestHeader("mcId") int mcId,
			@RequestHeader("notes") String notes) {
		//System.out.println("I AM HERE!!!");
		//System.out.println("UBACENI ID JE: " + roomId );
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			MedicalCheckupDbModel mcdbm = medicalCheckupRepo.findById(mcId).get();
			
			mcdbm.setEndNotes(notes);
			mcdbm.setFinished(true);
			medicalCheckupRepo.save(mcdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(path = "/sendMedicalCheckupRequestForm")
	public String sendMedicalCheckupRequestForm(@RequestBody MedicalCheckupResponseModel mc, BindingResult result) {
		if (result.hasErrors()) {
			return "doctorHome";
		}

		CheckupTypeDbModel ct = ctRepo.findById(mc.getCtId()).get();
		ClinicDbModel clinic = clinicRepo.findById(mc.getClinicId()).get();
		RoomDbModel room = roomRepo.findById(mc.getRoomId()).get();
		DoctorDbModel doctor = doctorRepo.findById(mc.getDoctorId()).get();
		
		MedicalCheckupDbModel mcdbm = new MedicalCheckupDbModel();
		mcdbm.setId(21);
		mcdbm.setDate(mc.getDate());
		mcdbm.setTime(mc.getTime());
		mcdbm.setDuration(mc.getDuration());
		mcdbm.setPrice(mc.getPrice());
		mcdbm.setCheckupType(ct);
		mcdbm.setClinic(clinic);
		mcdbm.setRoom(room);
		mcdbm.setDoctor(doctor);
		mcdbm.setFree(true);
		mcdbm.setPatient(null);
		mcdbm.setNotes(mc.getNotes());
		mcdbm.setEndNotes("");
		mcdbm.setFinished(false);

		medicalCheckupRepo.save(mcdbm);
		
		return "redirect:/clinicManager";
	}
	
}
