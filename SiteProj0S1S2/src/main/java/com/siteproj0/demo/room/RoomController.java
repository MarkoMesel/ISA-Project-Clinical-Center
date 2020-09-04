package com.siteproj0.demo.room;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

import com.siteproj0.demo.clinic.ClinicResponseModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.doctor.DoctorRegisterModel;
import com.siteproj0.demo.doctor.DoctorResponseModel;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;
import com.siteproj0.demo.repo.RoomRepo;
import com.siteproj0.demo.room.RoomRegisterModel;

@Controller
public class RoomController {
	@Autowired
	RoomRepo roomRepo;
	
	@Autowired
	ClinicRepo clinicRepo;
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@Autowired
	MedicalCheckupRepo mcRepo;
	
	@ModelAttribute("room")
	public RoomRegisterModel room() {
		return new RoomRegisterModel();
	}
	
	@GetMapping(path = "/roomManager")
	public String showRoomManagerPage() {
		return "roomManager";
	}
	
	@GetMapping(path = "/getRoomsFromClinicAdmin")
	public ResponseEntity<Object> getRoomsFromClinicAdmin(@RequestHeader("token") UUID securityToken) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("I HAVE FOUND A CLINIC! ITS ID IS " + clinicId);
			
			
			
			List<RoomDbModel> rooms = roomRepo.findByClinicIdAndEnabled(clinicId, true);
			
			//System.out.println("I HAVE FOUND SOME ROOMS! NUMBER OF ROOMS I'VE FOUND: "  + rooms.size());
			
			//System.out.println("NASAO JE OVOLIKO ELEMENATA: " + doctors.size());
			
			List<RoomResponseModel> roomResponseList = new ArrayList<RoomResponseModel>();
			
			for(RoomDbModel r : rooms) {
				RoomResponseModel rrm = new RoomResponseModel
						(r.getId(),
						r.getName(),
						r.getNumber(),
						clinicId);
				roomResponseList.add(rrm);
			}	
			
			//System.out.println("I HAVE CONVERTED THE ROOMS!");
			
			return new ResponseEntity<>(roomResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/addRoom")
	public String showAddRoomForm(Model model) {
		return "addRoom";
	}
	
	@PostMapping(path = "/addRoom")
	public String addNewRoom(@ModelAttribute("room") @Valid RoomRegisterModel room, BindingResult result) {
		if (result.hasErrors()) {
			return "addRoom";
		}

		ClinicDbModel clinic = clinicRepo.findById(room.getClinicId()).get();
		
		RoomDbModel roomDbModel = new RoomDbModel();
		
		roomDbModel.setId(21);
		roomDbModel.setName(room.getName());
		roomDbModel.setNumber(room.getNumber());
		roomDbModel.setClinic(clinic);
		roomDbModel.setEnabled(true);
		
		roomRepo.save(roomDbModel);
		
		return "redirect:/roomManager";
	}
	
	@GetMapping(path = "/editRoom/{roomId}")
	public String showEditRoomForm() {
		return "editRoom";
	}
	
	@GetMapping(path = "/getRoomInfo/{roomId}")
	public ResponseEntity<RoomResponseModel> getRoomInfo(@RequestHeader("token") UUID securityToken, @PathVariable int roomId) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			
			RoomDbModel rdbm = roomRepo.findByIdAndClinicId(roomId, clinicId);
			
			RoomResponseModel result = new RoomResponseModel(rdbm.getId(), rdbm.getName(), rdbm.getNumber(), clinicId);
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getId(), clinic.getName(), clinic.getDescription(), clinic.getAddress());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/editRoomInfo")
	@ResponseBody
	public ResponseEntity editRoomInfo(@RequestHeader("token") UUID securityToken,
			@RequestBody RoomResponseModel roomModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
			
			Integer roomId = roomModel.getId();
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			RoomDbModel rdbm = roomRepo.findByIdAndClinicId(roomId, clinicId);
			
			rdbm.setName(roomModel.getName());
			rdbm.setNumber(roomModel.getNumber());
			roomRepo.save(rdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping(path = "/logicalDeleteRoom")
	@ResponseBody
	public ResponseEntity logicalDeleteRoom(@RequestHeader("token") UUID securityToken,
			@RequestHeader("roomId") int roomId) {
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
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			RoomDbModel rdbm = roomRepo.findByIdAndClinicIdAndEnabled(roomId, clinicId, true);
			//System.out.println("ID OD PRONADJENOG JE: " + rdbm.getId() );
			rdbm.setEnabled(false);
			roomRepo.save(rdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/findRoomNameNumber")
	public ResponseEntity<Object> findRoomNameNumber(@RequestHeader("token") UUID securityToken,
													@RequestHeader("name") String name,
													@RequestHeader("number") String number) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();	
			
			RoomDbModel rdbm = roomRepo.findByNameAndNumberAndClinicIdAndEnabled(name,
																				number,
																				clinicId,
																				true);
			
            RoomResponseModel rrm = new RoomResponseModel(
            		rdbm.getId(),
            		rdbm.getName(),
            		rdbm.getNumber(),
            		clinicId);
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(rrm, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/isRoomInUse")
	public ResponseEntity<Object> isRoomInUse(@RequestHeader("token") UUID securityToken,
								@RequestHeader("roomId") int roomId) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByRoomIdAndFinished(roomId,false);
			if(mcList.size() > 0)
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/isRoomReserved")
	public ResponseEntity<Object> isRoomReserved(@RequestHeader("token") UUID securityToken,
								@RequestHeader("roomId") int roomId) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByRoomIdAndFreeAndFinished(roomId,false,false);
			if(mcList.size() > 0)
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findRoomByName")
	public ResponseEntity<Object> findRoomByName(@RequestHeader("token") UUID securityToken,
													@RequestHeader("searchByThis") String searchByThis,
													@RequestHeader("rDate") String rDate) {
		if(searchByThis.isEmpty() || rDate.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();	
			
			rDate = convertDateFormat(rDate);
			
			List<RoomDbModel> rooms = roomRepo.findByNameAndClinicIdAndEnabled(searchByThis,
																				clinicId,
																				true);
			
				List<RoomResponseModel> roomResponseList = new ArrayList<RoomResponseModel>();
					
					for(RoomDbModel r : rooms) {
						List<MedicalCheckupDbModel> mcList = mcRepo.findByRoomIdAndDate(r.getId(), rDate);
						if(mcList.size() <= 0) {
							RoomResponseModel rrm = new RoomResponseModel
									(r.getId(),
									r.getName(),
									r.getNumber(),
									clinicId);
							roomResponseList.add(rrm);
						}
					}
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(roomResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/findRoomByNumber")
	public ResponseEntity<Object> findRoomByNumber(@RequestHeader("token") UUID securityToken,
													@RequestHeader("searchByThis") String searchByThis,
													@RequestHeader("rDate") String rDate) {
		
		if(searchByThis.isEmpty() || rDate.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			rDate = convertDateFormat(rDate);
			
			List<RoomDbModel> rooms = roomRepo.findByNumberAndClinicIdAndEnabled(searchByThis,
																				clinicId,
																				true);
			
				List<RoomResponseModel> roomResponseList = new ArrayList<RoomResponseModel>();
					
					for(RoomDbModel r : rooms) {
						List<MedicalCheckupDbModel> mcList = mcRepo.findByRoomIdAndDate(r.getId(), rDate);
						if(mcList.size() <= 0) {
							RoomResponseModel rrm = new RoomResponseModel
									(r.getId(),
									r.getName(),
									r.getNumber(),
									clinicId);
							roomResponseList.add(rrm);
						}
					}
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(roomResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/getBusyDates/{pId}")
	public ResponseEntity<Object> getBusyDates(@RequestHeader("token") UUID securityToken, @PathVariable int pId) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("I HAVE FOUND A CLINIC! ITS ID IS " + clinicId);
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByRoomIdAndFinished(pId,false);
			
			//System.out.println("I HAVE FOUND SOME ROOMS! NUMBER OF ROOMS I'VE FOUND: "  + rooms.size());
			
			//System.out.println("NASAO JE OVOLIKO ELEMENATA: " + doctors.size());
			
			List<FoundDateResponseModel> busyDates = new ArrayList<FoundDateResponseModel>();
			
			//List<RoomResponseModel> roomResponseList = new ArrayList<RoomResponseModel>();
			
			for(MedicalCheckupDbModel mc : mcList) {
				FoundDateResponseModel bd = new FoundDateResponseModel(mc.getDate());
				busyDates.add(bd);
			}	
			
			//System.out.println("I HAVE CONVERTED THE ROOMS!");
			
			return new ResponseEntity<>(busyDates, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/getfirstFreeDateForRoom/{pId}")
	public ResponseEntity<Object> getfirstFreeDateForRoom(@RequestHeader("token") UUID securityToken, @PathVariable int pId) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			FoundDateResponseModel foundDate = null;
			
			String foundDateStr = getCurrentDate();
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByRoomIdAndDateAndFreeAndFinished(pId, foundDateStr, false, false);
			if(mcList.size() <= 0) {
				foundDate = new FoundDateResponseModel(foundDateStr);
				return new ResponseEntity<>(foundDate, HttpStatus.OK);
			}
			
			do {
				foundDateStr = getNextDate(foundDateStr);
				mcList = mcRepo.findByRoomIdAndDateAndFreeAndFinished(pId, foundDateStr, false, false);
			} while(mcList.size() > 0);
			
			foundDate = new FoundDateResponseModel(foundDateStr);
			return new ResponseEntity<>(foundDate, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	
	private String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
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
	
}
