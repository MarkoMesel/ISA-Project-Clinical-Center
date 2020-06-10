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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicAdminDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;
import com.siteproj0.demo.repo.ClinicAdminRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.RoomRepo;
import com.siteproj0.demo.room.RoomRegisterModel;
import com.siteproj0.demo.room.RoomResponseModel;

@Controller
public class CheckupTypeController {
	@Autowired
	CheckupTypeRepo ctRepo;
	
	@Autowired
	ClinicRepo clinicRepo;
	
	@Autowired
	ClinicAdminRepo clinicAdminRepo;
	
	@ModelAttribute("ct")
	public CheckupTypeRegisterModel ct() {
		return new CheckupTypeRegisterModel();
	}
	
	@GetMapping(path = "/checkupTypeManager")
	public String showctManagerPage() {
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
}
