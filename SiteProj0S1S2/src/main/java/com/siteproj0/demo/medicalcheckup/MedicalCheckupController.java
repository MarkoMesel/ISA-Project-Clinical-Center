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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.siteproj0.demo.repo.ClinicAdminRepo;
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
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
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
		
		/*
		 * Važno je da više istovremenih pacijenata, 
		 * tj. korisnika aplikacije, 
		 * ne može da zatraži upit za pregled 
		 * u istom terminu kod istog lekara.
		 */
		/*
		 * Jedan lekar ne može 
		 * istovremeno da bude 
		 * prisutan na više 
		 * različitih operacija.
		 */
		List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByDoctorIdAndDateAndTime(doctor.getId(),
																							mc.getDate(),
																							mc.getTime());
		if(mcList.size() > 0) {
			return "redirect:/registerMedicalCheckup";
		}
		
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
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndDoctorIdAndFree(clinicId,
																												doctorId,
																												false);
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
			return "redirect:/doctorHome";
		}
		
		MedicalCheckupDbModel mcOld = medicalCheckupRepo.findById(mc.getId()).get();
		
		/*
		 * Jedan lekar ne može 
		 * istovremeno da bude 
		 * prisutan na više 
		 * različitih operacija.
		 */
		List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByDoctorIdAndDateAndTime(mcOld.getDoctor().getId(),
																							mc.getDate(),
																							mc.getTime());
		if(mcList.size() > 0) {
			return "redirect:/doctorHome";
		}
		
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
		
		return "redirect:/chooseAndBeginCheckup";
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
	
	@GetMapping(path = "/getReservedMcFromDoctorAndPatient/{uId}")
	public ResponseEntity<Object> getMcFromDoctorAndPatient(@RequestHeader("token") UUID securityToken,
															@PathVariable int uId) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			Integer doctorId = user.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndDoctorIdAndPatientIdAndFreeAndFinished(clinicId,
																												doctorId,
																												uId,
																												false,
																												false);
			//CheckupTypeDbModel ctdbm = ctRepo.findById(mcs.)
			
			List<MedicalCheckupDoctorResponseModel> mcdrmList = new ArrayList<MedicalCheckupDoctorResponseModel>();
			for (MedicalCheckupDbModel mc : mcList) {
				if(mc.getRoom() != null) {
					CheckupTypeDbModel ctdbm = ctRepo.findById(mc.getCheckupType().getId()).get();
					RoomDbModel rdbm = roomRepo.findById(mc.getRoom().getId()).get();
					UserDbModel udbm = userRepo.findById(mc.getPatient().getId()).get();
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
							"No");
							mcdrmList.add(mcdrm);
				}
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(mcdrmList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/getMcFromPatient/{uId}")
	public ResponseEntity<Object> getMcFromPatient(@RequestHeader("token") UUID securityToken,
															@PathVariable int uId) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			Integer doctorId = user.getId();
			//System.out.println("ID OD OVE KLINIKE JE: " + clinicId);
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndPatientId(clinicId, uId);
			//CheckupTypeDbModel ctdbm = ctRepo.findById(mcs.)
			
			List<MedicalCheckupPatientResponseModel> mcdrmList = new ArrayList<MedicalCheckupPatientResponseModel>();
			for (MedicalCheckupDbModel mc : mcList) {
				if(mc.getRoom() != null) {
					CheckupTypeDbModel ctdbm = ctRepo.findById(mc.getCheckupType().getId()).get();
					RoomDbModel rdbm = roomRepo.findById(mc.getRoom().getId()).get();
					DoctorDbModel udbm = doctorRepo.findById(mc.getDoctor().getId()).get();
					//System.out.println(udbm.getId())
					String finished = "No";
					if(mc.isFinished()) {
						finished = "Yes";
					}
					MedicalCheckupPatientResponseModel mcdrm = new MedicalCheckupPatientResponseModel(
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
	
	@GetMapping(path = "/checkupRequestManager")
	public String checkupRequestManagerForm(Model model) {
		return "checkupRequestManager";
	}
	
	@GetMapping(path = "/getCheckupsWithNoRoom")
	public ResponseEntity<Object> getCheckupsWithNoRoom(@RequestHeader("token") UUID securityToken) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByRoomIdIsNull();
			//CheckupTypeDbModel ctdbm = ctRepo.findById(mcs.)
			
			List<MedicalCheckupRoomResponseModel> mcdrmList = new ArrayList<MedicalCheckupRoomResponseModel>();
			for (MedicalCheckupDbModel mc : mcList) {
				CheckupTypeDbModel ctdbm = ctRepo.findById(mc.getCheckupType().getId()).get();
				//RoomDbModel rdbm = roomRepo.findById(mc.getRoom().getId()).get();
				UserDbModel udbm = userRepo.findById(mc.getPatient().getId()).get();
				DoctorDbModel ddbm = doctorRepo.findById(mc.getDoctor().getId()).get();
				MedicalCheckupRoomResponseModel mcdrm = new MedicalCheckupRoomResponseModel(
						mc.getId(),
						mc.getDate(),
						mc.getTime(),
						mc.getDuration(),
						mc.getPrice(),
						ctdbm.getName(),
						ddbm.getFirstName() + " " + ddbm.getLastName(),
						udbm.getFirstName() + " " + udbm.getLastName(),
						mc.getNotes());
						mcdrmList.add(mcdrm);
	        }
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(mcdrmList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findRoomForCheckup/{mcid}")
	public String findRoomForCheckupForm(Model model) {
		return "findRoomForCheckup";
	}
	
	@PutMapping(path = "/saveRoomAndDate")
	@ResponseBody
	public ResponseEntity saveRoomAndDate(@RequestHeader("token") UUID securityToken,
			@RequestHeader("mcId") int mcId,
			@RequestHeader("roomId") int roomId,
			@RequestHeader("chosenDate") String chosenDate) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			MedicalCheckupDbModel mcdbm = medicalCheckupRepo.findById(mcId).get();
			
			/*
			 * Prilikom odobravanja zahteva za 
			 * operaciju/pregled, ne može jedna sala 
			 * da bude rezervisana u isto vreme 
			 * za različite operacije/preglede
			 */
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByRoomIdAndDateAndTime(roomId,chosenDate,mcdbm.getTime());
			if(mcList.size() > 1) {
				return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			RoomDbModel rdbm = roomRepo.findById(roomId).get();
			mcdbm.setRoom(rdbm);
			mcdbm.setDate(chosenDate);
			medicalCheckupRepo.save(mcdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
}
