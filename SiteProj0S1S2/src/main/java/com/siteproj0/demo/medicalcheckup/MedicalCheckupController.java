package com.siteproj0.demo.medicalcheckup;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.doctor.DoctorRegisterModel;
import com.siteproj0.demo.repo.CheckupTypeRepo;
import com.siteproj0.demo.repo.ClinicRepo;
import com.siteproj0.demo.repo.DoctorRepo;
import com.siteproj0.demo.repo.MedicalCheckupRepo;
import com.siteproj0.demo.repo.RoomRepo;
import com.siteproj0.demo.room.RoomResponseModel;

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
	
	
	@ModelAttribute("medicalCheckup")
	public MedicalCheckupRegisterModel mc() {
		return new MedicalCheckupRegisterModel();
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

		medicalCheckupRepo.save(mcdbm);
		
		return "redirect:/clinicManager";
	}
}
