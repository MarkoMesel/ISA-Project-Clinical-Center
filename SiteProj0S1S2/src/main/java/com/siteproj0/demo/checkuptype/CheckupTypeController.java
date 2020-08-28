package com.siteproj0.demo.checkuptype;

import java.util.ArrayList;
import java.util.List;
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
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;

@Controller
public class CheckupTypeController {
	@Autowired
	CheckupTypeRepo ctRepo;
	
	@Autowired
	ClinicRepo clinicRepo;
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@Autowired
	MedicalCheckupRepo mcRepo;
	
	@ModelAttribute("ct")
	public CheckupTypeRegisterModel ct() {
		return new CheckupTypeRegisterModel();
	}
	
	@GetMapping(path = "/checkupTypeManager")
	public String showCtManagerPage() {
		return "checkupTypeManager";
	}

	@GetMapping(path = "/getCtFromClinicAdmin")
	public ResponseEntity<Object> getCtFromClinicAdmin(@RequestHeader("token") UUID securityToken) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			//System.out.println("I HAVE FOUND A CLINIC! ITS ID IS " + clinicId);
			
			List<CheckupTypeDbModel> cts = ctRepo.findByClinicIdAndEnabled(clinicId, true);
			
			//System.out.println("I HAVE FOUND SOME ROOMS! NUMBER OF ROOMS I'VE FOUND: "  + rooms.size());
			
			//System.out.println("NASAO JE OVOLIKO ELEMENATA: " + doctors.size());
			
			List<CheckupTypeResponseModel> ctResponseList = new ArrayList<CheckupTypeResponseModel>();
			
			for(CheckupTypeDbModel ct : cts) {
				CheckupTypeResponseModel ctrm = new CheckupTypeResponseModel
						(ct.getId(),
						ct.getName(),
						ct.getPrice(),
						clinicId);
				ctResponseList.add(ctrm);
			}	
			
			//System.out.println("I HAVE CONVERTED THE ROOMS!");
			
			return new ResponseEntity<>(ctResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/addCt")
	public String showAddCtForm(Model model) {
		return "addCheckupType";
	}
	
	@PostMapping(path = "/addCt")
	public String addNewCt(@ModelAttribute("ct") @Valid CheckupTypeRegisterModel ct, BindingResult result) {
		if (result.hasErrors()) {
			return "addCheckupType";
		}

		ClinicDbModel clinic = clinicRepo.findById(ct.getClinicId()).get();
		
		CheckupTypeDbModel ctDbModel = new CheckupTypeDbModel();
		
		ctDbModel.setId(21);
		ctDbModel.setName(ct.getName());
		ctDbModel.setPrice(ct.getPrice());
		ctDbModel.setClinic(clinic);
		ctDbModel.setEnabled(true);
		
		ctRepo.save(ctDbModel);
		
		return "redirect:/checkupTypeManager";
	}
	
	@GetMapping(path = "/editCheckupType/{ctId}")
	public String showEditCtForm() {
		return "editCheckupType";
	}
	
	@GetMapping(path = "/getCtInfo/{ctId}")
	public ResponseEntity<CheckupTypeResponseModel> getCtInfo(@RequestHeader("token") UUID securityToken, @PathVariable int ctId) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			
			CheckupTypeDbModel ctbm = ctRepo.findByIdAndClinicIdAndEnabled(ctId, clinicId, true);
			
			CheckupTypeResponseModel result = new CheckupTypeResponseModel(ctbm.getId(), ctbm.getName(), ctbm.getPrice(), clinicId);
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getId(), clinic.getName(), clinic.getDescription(), clinic.getAddress());
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/editCtInfo")
	@ResponseBody
	public ResponseEntity editCtInfo(@RequestHeader("token") UUID securityToken,
			@RequestBody CheckupTypeResponseModel ctModel) {
		if (securityToken == null) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			}
			
			Integer ctId = ctModel.getId();
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();
			
			CheckupTypeDbModel ctdbm = ctRepo.findByIdAndClinicIdAndEnabled(ctId, clinicId, true);
			
			ctdbm.setName(ctModel.getName());
			ctdbm.setPrice(ctModel.getPrice());
			ctRepo.save(ctdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/findCtNamePrice")
	public ResponseEntity<Object> findCtNamePrice(@RequestHeader("token") UUID securityToken,
													@RequestHeader("name") String name,
													@RequestHeader("price") int price) {
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			ClinicDbModel clinic = user.getClinic();
			Integer clinicId = clinic.getId();	
			
			CheckupTypeDbModel ctdbm = ctRepo.findByNameAndPriceAndClinicIdAndEnabled(name,
																				price,
																				clinicId,
																				true);
			
            CheckupTypeResponseModel ctrm = new CheckupTypeResponseModel(
            		ctdbm.getId(),
            		ctdbm.getName(),
            		ctdbm.getPrice(),
            		clinicId);
			
			//ClinicResponseModel result = new ClinicResponseModel(clinic.getName(), clinic.getDescription(), clinic.getAddress());
			return new ResponseEntity<>(ctrm, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/logicalDeleteCt")
	@ResponseBody
	public ResponseEntity logicalDeleteCt(@RequestHeader("token") UUID securityToken,
			@RequestHeader("ctId") int ctId) {
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
			
			CheckupTypeDbModel ctdbm = ctRepo.findByIdAndClinicIdAndEnabled(ctId, clinicId, true);
			//System.out.println("ID OD PRONADJENOG JE: " + rdbm.getId() );
			ctdbm.setEnabled(false);
			ctRepo.save(ctdbm);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/isCheckupTypeInUse")
	public ResponseEntity<Object> isCtInUse(@RequestHeader("token") UUID securityToken,
								@RequestHeader("ctId") int ctId) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByCheckupTypeIdAndFinished(ctId, false);
			if(mcList.size() > 0)
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/isCheckupTypeReserved")
	public ResponseEntity<Object> isCtReserved(@RequestHeader("token") UUID securityToken,
								@RequestHeader("ctId") int ctId) {
		//System.out.println("I AM HERE!");
		try {
			ClinicAdminDbModel user = clinicAdminRepo.findBySecurityToken(securityToken);
			if (user == null) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}
			
			List<MedicalCheckupDbModel> mcList = mcRepo.findByCheckupTypeIdAndFreeAndFinished(ctId,false,false);
			if(mcList.size() > 0)
				return new ResponseEntity<>(true, HttpStatus.OK);
			return new ResponseEntity<>(false, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
