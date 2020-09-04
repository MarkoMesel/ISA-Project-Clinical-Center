package com.siteproj0.demo.medicalcheckup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
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

import java.text.DateFormatSymbols;

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
			return "registerMedicalCheckup";
		}
		mc.setDate(convertDateFormat(mc.getDate()));
		CheckupTypeDbModel ct = ctRepo.findById(mc.getCtId()).get();
		ClinicDbModel clinic = clinicRepo.findById(mc.getClinicId()).get();
		RoomDbModel room = roomRepo.findById(mc.getRoomId()).get();
		DoctorDbModel doctor = doctorRepo.findById(mc.getDoctorId()).get();

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
		mcdbm.setInProgress(false);

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
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndDoctorIdAndPatientIdNotNullAndFree(clinicId,
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
		mcdbm.setInProgress(false);
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
			mcdbm.setInProgress(false);
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
		mcdbm.setInProgress(false);
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
	
	/*
	 * U METODI "saveRoomAndDate" REALIZOVANA JE SLEDECA STAVKA:
	 * Prilikom odobravanja zahteva za operaciju/pregled, ne može jedna sala da bude
	 * rezervisana u isto vreme za različite operacije/preglede.
	 */
	@PutMapping(path = "/saveRoomAndDate")
	@ResponseBody
	public ResponseEntity saveRoomAndDate(@RequestHeader("token") UUID securityToken,
			@RequestHeader("mcId") int mcId,
			@RequestHeader("roomId") int roomId,
			@RequestHeader("chosenDate") String chosenDate) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		if(chosenDate.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
		}
		chosenDate = convertDateFormat(chosenDate);
		
		
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			
			MedicalCheckupDbModel mcdbm = medicalCheckupRepo.findById(mcId).get();
			
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByRoomIdAndDateAndTime(roomId,chosenDate,mcdbm.getTime());
			if(mcList.size() > 0) {
				return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			RoomDbModel rdbm = roomRepo.findById(roomId).get();
			
			mcdbm.setRoom(rdbm);
			mcdbm.setDate(chosenDate);
			try {
				medicalCheckupRepo.save(mcdbm);
				/*
				//Otkomentarisanjem ovoga se izazove ObjectOptimisticLockingFailureException
				MedicalCheckupDbModel mcdbmTest = new MedicalCheckupDbModel();
				mcdbmTest.setId(mcdbm.getId()); 
				mcdbmTest.setVersion(0);
				medicalCheckupRepo.save(mcdbmTest);
				 */			
			} catch(ObjectOptimisticLockingFailureException e0) {
				
				System.out.println("Save failed. Retrying...");
				Thread.sleep(1000);
				
				MedicalCheckupDbModel mcdbm0 = medicalCheckupRepo.findById(mcId).get();
				List<MedicalCheckupDbModel> mcList0 = medicalCheckupRepo.findByRoomIdAndDateAndTime(roomId,chosenDate,mcdbm0.getTime());
				if(mcList0.size() > 0) {
					return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				RoomDbModel rdbm0 = roomRepo.findById(roomId).get();
				mcdbm0.setRoom(rdbm0);
				mcdbm0.setDate(chosenDate);
				try {
					medicalCheckupRepo.save(mcdbm0);
				} catch(ObjectOptimisticLockingFailureException e1) {
					
					System.out.println("Save failed again. Retrying...");
					Thread.sleep(1000);
					
					MedicalCheckupDbModel mcdbm1 = medicalCheckupRepo.findById(mcId).get();
					List<MedicalCheckupDbModel> mcList1 = medicalCheckupRepo.findByRoomIdAndDateAndTime(roomId,chosenDate,mcdbm1.getTime());
					if(mcList1.size() > 0) {
						return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
					}
					RoomDbModel rdbm1 = roomRepo.findById(roomId).get();
					mcdbm1.setRoom(rdbm1);
					mcdbm1.setDate(chosenDate);
					try {
						medicalCheckupRepo.save(mcdbm1);
					} catch(ObjectOptimisticLockingFailureException e2) {
						
						System.out.println("Save failed. Try again later.");
						Thread.sleep(1000);
						return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
					}
				}
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	/*
	 * U METODI "saveRoomAndDateAndDoctor" REALIZOVANA JE SLEDECA STAVKA:
	 * Prilikom odobravanja zahteva za operaciju/pregled, ne može jedna sala da bude
	 * rezervisana u isto vreme za različite operacije/preglede.
	 */
	@PutMapping(path = "/saveRoomAndDateAndDoctor")
	@ResponseBody
	public ResponseEntity saveRoomAndDateAndDoctor(@RequestHeader("token") UUID securityToken,
			@RequestHeader("mcId") int mcId,
			@RequestHeader("roomId") int roomId,
			@RequestHeader("chosenDate") String chosenDate,
			@RequestHeader("doctorId") int doctorId) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		if(chosenDate.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
		}
		chosenDate = convertDateFormat(chosenDate);
		
		
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			
			MedicalCheckupDbModel mcdbm = medicalCheckupRepo.findById(mcId).get();
			
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByRoomIdAndDoctorIdAndDateAndTime(roomId,doctorId,chosenDate,mcdbm.getTime());
			if(mcList.size() > 0) {
				return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			RoomDbModel rdbm = roomRepo.findById(roomId).get();
			DoctorDbModel ddbm = doctorRepo.findById(doctorId).get();
			
			mcdbm.setRoom(rdbm);
			mcdbm.setDate(chosenDate);
			mcdbm.setDoctor(ddbm);
			
			try {
				medicalCheckupRepo.save(mcdbm);
				 
				//Otkomentarisanjem ovoga se izazove ObjectOptimisticLockingFailureException
				/*
				MedicalCheckupDbModel mcdbmTest = new MedicalCheckupDbModel();
				mcdbmTest.setId(mcdbm.getId()); mcdbmTest.setVersion(0);
				medicalCheckupRepo.save(mcdbmTest);
				*/
				 			
			} catch(ObjectOptimisticLockingFailureException e0) {
				
				System.out.println("Save failed. Retrying...");
				Thread.sleep(1000);
				
				MedicalCheckupDbModel mcdbm0 = medicalCheckupRepo.findById(mcId).get();
				List<MedicalCheckupDbModel> mcList0 = medicalCheckupRepo.findByRoomIdAndDoctorIdAndDateAndTime(roomId,doctorId,chosenDate,mcdbm0.getTime());
				if(mcList0.size() > 0) {
					return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				RoomDbModel rdbm0 = roomRepo.findById(roomId).get();
				DoctorDbModel ddbm0 = doctorRepo.findById(doctorId).get();
				mcdbm0.setRoom(rdbm0);
				mcdbm0.setDate(chosenDate);
				mcdbm.setDoctor(ddbm0);
				try {
					medicalCheckupRepo.save(mcdbm0);
				} catch(ObjectOptimisticLockingFailureException e1) {
					
					System.out.println("Save failed again. Retrying...");
					Thread.sleep(1000);
					
					MedicalCheckupDbModel mcdbm1 = medicalCheckupRepo.findById(mcId).get();
					List<MedicalCheckupDbModel> mcList1 = medicalCheckupRepo.findByRoomIdAndDoctorIdAndDateAndTime(roomId,doctorId,chosenDate,mcdbm1.getTime());
					if(mcList1.size() > 0) {
						return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
					}
					RoomDbModel rdbm1 = roomRepo.findById(roomId).get();
					DoctorDbModel ddbm1 = doctorRepo.findById(doctorId).get();
					mcdbm1.setRoom(rdbm1);
					mcdbm1.setDate(chosenDate);
					mcdbm.setDoctor(ddbm1);
					try {
						medicalCheckupRepo.save(mcdbm1);
					} catch(ObjectOptimisticLockingFailureException e2) {
						
						System.out.println("Save failed. Try again later.");
						Thread.sleep(1000);
						return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
					}
				}
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/getMcDaily")
	public ResponseEntity<Object> getMcDaily(@RequestHeader("token") UUID securityToken) {
		ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndFreeAndFinished(
				clinicId,
				false,
				true);
			
			List<MedicalCheckupDailyResponseModel> mcdrmList = new ArrayList<>();
			String currentMcDate = "";
			Integer occurences;
			
			HashMap<String, Integer> mcMap = new HashMap<>();
			for(MedicalCheckupDbModel mcdbm : mcList) {
				currentMcDate = mcdbm.getDate();
				occurences = mcMap.get(currentMcDate);
				mcMap.put(currentMcDate, (occurences == null) ? 1 : occurences + 1);
			}
			
			Iterator it = mcMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        MedicalCheckupDailyResponseModel mcdrm = new MedicalCheckupDailyResponseModel((String)pair.getKey(),(Integer)pair.getValue());
		        mcdrmList.add(mcdrm);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    List<MedicalCheckupDailyResponseModel> mcdrmListSorted = sortMcByDate(mcdrmList);
		    for(MedicalCheckupDailyResponseModel mcdrms : mcdrmListSorted) {
		    	mcdrms.setDate(renameDateToDaily(mcdrms.getDate()));
		    }
		    
			return new ResponseEntity<>(mcdrmListSorted, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/getMcWeekly")
	public ResponseEntity<Object> getMcWeekly(@RequestHeader("token") UUID securityToken) {
		ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndFreeAndFinished(
				clinicId,
				false,
				true);
			
			List<MedicalCheckupDailyResponseModel> mcdrmList = new ArrayList<>();
			String currentMcDate = "";
			Integer occurences;
			
			HashMap<String, Integer> mcMap = new HashMap<>();
			for(MedicalCheckupDbModel mcdbm : mcList) {
				currentMcDate = mcdbm.getDate();
				occurences = mcMap.get(currentMcDate);
				mcMap.put(currentMcDate, (occurences == null) ? 1 : occurences + 1);
			}
			
			Iterator it = mcMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        MedicalCheckupDailyResponseModel mcdrm = new MedicalCheckupDailyResponseModel((String)pair.getKey(),(Integer)pair.getValue());
		        mcdrmList.add(mcdrm);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    List<MedicalCheckupDailyResponseModel> mcdrmListSorted = sortMcByDate(mcdrmList);
		    for(MedicalCheckupDailyResponseModel mcdrms : mcdrmListSorted) {
		    	mcdrms.setDate(renameDateToWeekly(mcdrms.getDate()));
		    }
		    
		    List<MedicalCheckupDailyResponseModel> mcdrmListWeekly = new ArrayList<>();
		    boolean alreadyExists = false;
		    
		    for(MedicalCheckupDailyResponseModel current : mcdrmListSorted) {
		    	alreadyExists = false;
		    	for(MedicalCheckupDailyResponseModel existing : mcdrmListWeekly) {
			    	if(current.getDate().equals(existing.getDate())) {
			    		existing.setCount(existing.getCount()+current.getCount());
			    		alreadyExists = true;
			    		break;
			    	}
		    	}
		    	if(!alreadyExists) {
		    		mcdrmListWeekly.add(current);
		    	}
		    }
		    
			return new ResponseEntity<>(mcdrmListWeekly, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/getMcMonthly")
	public ResponseEntity<Object> getMcMonthly(@RequestHeader("token") UUID securityToken) {
		ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByClinicIdAndFreeAndFinished(
				clinicId,
				false,
				true);
			
			List<MedicalCheckupDailyResponseModel> mcdrmList = new ArrayList<>();
			String currentMcDate = "";
			Integer occurences;
			
			HashMap<String, Integer> mcMap = new HashMap<>();
			for(MedicalCheckupDbModel mcdbm : mcList) {
				currentMcDate = mcdbm.getDate();
				occurences = mcMap.get(currentMcDate);
				mcMap.put(currentMcDate, (occurences == null) ? 1 : occurences + 1);
			}
			
			Iterator it = mcMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        MedicalCheckupDailyResponseModel mcdrm = new MedicalCheckupDailyResponseModel((String)pair.getKey(),(Integer)pair.getValue());
		        mcdrmList.add(mcdrm);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    List<MedicalCheckupDailyResponseModel> mcdrmListSorted = sortMcByDate(mcdrmList);
		    for(MedicalCheckupDailyResponseModel mcdrms : mcdrmListSorted) {
		    	mcdrms.setDate(renameDateToMonthly(mcdrms.getDate()));
		    }
		    
		    List<MedicalCheckupDailyResponseModel> mcdrmListMonthly = new ArrayList<>();
		    boolean alreadyExists = false;
		    
		    for(MedicalCheckupDailyResponseModel current : mcdrmListSorted) {
		    	alreadyExists = false;
		    	for(MedicalCheckupDailyResponseModel existing : mcdrmListMonthly) {
			    	if(current.getDate().equals(existing.getDate())) {
			    		existing.setCount(existing.getCount()+current.getCount());
			    		alreadyExists = true;
			    		break;
			    	}
		    	}
		    	if(!alreadyExists) {
		    		mcdrmListMonthly.add(current);
		    	}
		    }
		    
			return new ResponseEntity<>(mcdrmListMonthly, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/registerMedicalCheckupDoctor")
	public String showRegisterMedicalCheckupDoctorForm(Model model) {
		return "registerMedicalCheckupDoctor";
	}
	
	/*
	 * U METODI "requestMedicalCheckup" REALIZOVANA JE SLEDECA STAVKA:
	 * Važno je da više istovremenih pacijenata, tj. korisnika aplikacije, ne može
	 * da zatraži upit za pregled u istom terminu kod istog lekara.
	 */
	@PutMapping(path = "/requestMedicalCheckup")
	@Transactional
	public String requestMedicalCheckup(
		@RequestHeader("token") UUID securityToken, 
		@RequestBody MedicalCheckupResponseModel mc, 
		BindingResult result) {
		try {
			UserDbModel user = userRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return "redirect:/requestMedicalCheckup";
			}
			
			if (result.hasErrors()) {
				return "redirect:/requestMedicalCheckup";
			}
			
			MedicalCheckupDbModel mcdbm = medicalCheckupRepo.findById(mc.getId()).get();
			mcdbm.setPatient(user);
			try {
				medicalCheckupRepo.save(mcdbm);
			} catch(ObjectOptimisticLockingFailureException e0) {
				System.out.println("Save failed. Retrying...");
				Thread.sleep(1000);
				MedicalCheckupDbModel mcdbm0 = medicalCheckupRepo.findById(mc.getId()).get();
				mcdbm0.setPatient(user);
				try {
					medicalCheckupRepo.save(mcdbm0);
				} catch(ObjectOptimisticLockingFailureException e1) {
					System.out.println("Save failed again. Retrying...");
					Thread.sleep(1000);
					MedicalCheckupDbModel mcdbm1 = medicalCheckupRepo.findById(mc.getId()).get();
					mcdbm1.setPatient(user);
					try {
						System.out.println("Save failed. Try again later.");
						medicalCheckupRepo.save(mcdbm1);
					} catch(ObjectOptimisticLockingFailureException e2) {
						return "redirect:/requestMedicalCheckup";
					}
				}
			}
			
			return "redirect:/home";
		} catch (Exception e) {
			return "redirect:/requestMedicalCheckup";
		}
	}
	
	@PutMapping(path = "/mcInProgressYes/{mcId}")
	public ResponseEntity markMcAsInProgress(@RequestHeader("token") UUID securityToken,
			@PathVariable int mcId) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			MedicalCheckupDbModel mcdbm = medicalCheckupRepo.findById(mcId).get();
			mcdbm.setInProgress(true);
	
			medicalCheckupRepo.save(mcdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/checkIfMcInProgress")
	public ResponseEntity<Object> checkIfMcInProgress(@RequestHeader("token") UUID securityToken) {
		try {
			DoctorDbModel user = doctorRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>("ne", HttpStatus.UNAUTHORIZED);
			}
			
			
			List<MedicalCheckupDbModel> mcdbmList = medicalCheckupRepo.findByDoctorIdAndInProgress(user.getId(), true);
			if(mcdbmList.size() > 0) {
				MedicalCheckupDbModel mcdbm = mcdbmList.get(0);
				return new ResponseEntity<>(mcdbm.getId(), HttpStatus.OK);
			}
			
			return new ResponseEntity<>("ne", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("ne", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private static List<MedicalCheckupDailyResponseModel> sortMcByDate(List<MedicalCheckupDailyResponseModel> mcdrmList) {
		Collections.sort(mcdrmList, new Comparator<MedicalCheckupDailyResponseModel>() {
		public int compare(MedicalCheckupDailyResponseModel o1, MedicalCheckupDailyResponseModel o2) {
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		      Date date1 = null;
		      Date date2 = null;
		      try {
		          date1=format.parse(o1.getDate());
		          date2=format.parse(o2.getDate());
		
		      } catch (ParseException e) {
		          e.printStackTrace();
		      }
		
		      return date1.compareTo(date2);
			}
		});
	
		return mcdrmList;
	}
	
	private String renameDateToDaily(String oldName) {
		String[] dateParts = oldName.split("/");
		int month = 0;
	    try {
	    	month = Integer.parseInt(dateParts[0]);
		} catch (NumberFormatException e) {
			return oldName;
		}
	    
	    String newMonth = new DateFormatSymbols().getMonths()[month-1].substring(0,3);
	    
	    return newMonth + " " + dateParts[1] + ", " + dateParts[2];
	}
	
	private String renameDateToWeekly(String oldName) {	
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		Date date = null;
		
		try {
	          date=dateFormat.parse(oldName);
	
	      } catch (ParseException e) {
	          e.printStackTrace();
	      }
		
		Calendar c = Calendar.getInstance();
		
	    c.setFirstDayOfWeek(Calendar.MONDAY); //Line2
	    c.setTime(date);
	    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
	    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
	    String weekStartDate = dateFormat.format(c.getTime());
	    c.add(Calendar.DATE, 6);
	    String weekEndDate = dateFormat.format(c.getTime());

	    return renameDateToDaily(weekStartDate) + " - " + renameDateToDaily(weekEndDate);
	}
	
	private String renameDateToMonthly(String oldName) {	
		String[] dateParts = oldName.split("/");
		int month = 0;
	    try {
	    	month = Integer.parseInt(dateParts[0]);
		} catch (NumberFormatException e) {
			return oldName;
		}
	    
	    String newMonth = new DateFormatSymbols().getMonths()[month-1].substring(0,3);
	    
	    return newMonth  + " " + dateParts[2];
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
	
	//KRAJ DANA
	@Scheduled(cron = "59 59 23 * * ?")
	public void autoSetMcDateAndTime() {
		System.out.println("Initiating automatic room and date finding procedure...");
		List<MedicalCheckupDbModel> mcList = medicalCheckupRepo.findByRoomIdIsNull();
		Iterable<RoomDbModel> roomIterable = roomRepo.findAll();
		String date = "";
		for(MedicalCheckupDbModel mcdbm : mcList) {
			System.out.println("Found new medical checkup.");
			date = getCurrentDate();
			do {
				date = getNextDate(date);
				Iterator<RoomDbModel> roomIterator = roomIterable.iterator();
				while(roomIterator.hasNext()) {
					RoomDbModel rdbm = roomIterator.next();
					List<MedicalCheckupDbModel> mcList0 = medicalCheckupRepo.findByRoomIdAndDateAndTime(rdbm.getId(),date,mcdbm.getTime());
					if(mcList0.size() <= 0) {
						mcdbm.setDate(date);
						mcdbm.setRoom(rdbm);
						medicalCheckupRepo.save(mcdbm);
						System.out.println("done");
						break;
					}
				}
			} while(mcdbm.getRoom() == null);
		}
		System.out.println("It is done.");
	}
	
	private String getNextDate(String oldDateStr) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		Date oldDate = null;
		try {
			oldDate = dateFormat.parse(oldDateStr);
	      } catch (ParseException e) {
	    	  e.printStackTrace();
	  	  }
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(oldDate);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		
		return dateFormat.format(cal.getTime());
		
	}
	
	private String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
}
