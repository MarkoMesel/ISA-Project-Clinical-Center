package com.siteproj0.demo.clinic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteproj0.demo.appointment.AppointmentViewModel;
import com.siteproj0.demo.dal.AppointmentDbModel;
import com.siteproj0.demo.dal.ClinicDbModel;
import com.siteproj0.demo.repo.ClinicRepo;

@Controller
public class ClinicController {
	@Autowired
	ClinicRepo clinicRepo;
	
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
	
	@GetMapping(path = "/getClinics")
	@ResponseBody
	public List<ClinicViewModel> getClinics()
	{
		List<ClinicViewModel> result = new ArrayList<ClinicViewModel>();
		Iterable<ClinicDbModel> clinics= clinicRepo.findAll();
		
		clinics.forEach((n)->result.add(new ClinicViewModel(n.getId(), n.getName(), n.getRating(), n.getPrice())));
		return result;
	}
}
