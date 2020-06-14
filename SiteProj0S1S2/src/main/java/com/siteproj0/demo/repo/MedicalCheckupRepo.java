package com.siteproj0.demo.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;

public interface MedicalCheckupRepo extends CrudRepository<MedicalCheckupDbModel, Integer> {

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorId(Integer clinicId, Integer doctorId);

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorIdAndFree(Integer clinicId, Integer doctorId, boolean free);

	List<MedicalCheckupDbModel> findByRoomIdAndFreeAndFinished(Integer roomId, boolean free, boolean finished);

	List<MedicalCheckupDbModel> findByCheckupTypeIdAndFreeAndFinished(Integer ctId, boolean free, boolean finished);

	List<MedicalCheckupDbModel> findByRoomIdAndFinished(Integer roomId, boolean finished);

	List<MedicalCheckupDbModel> findByCheckupTypeIdAndFinished(Integer ctId, boolean finished);

}
