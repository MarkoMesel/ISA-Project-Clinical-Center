package com.siteproj0.demo.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.CheckupTypeDbModel;
import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;

public interface MedicalCheckupRepo extends CrudRepository<MedicalCheckupDbModel, Integer> {

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorId(Integer clinicId, Integer doctorId);

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorIdAndFree(Integer clinicId, Integer doctorId, boolean free);

	List<MedicalCheckupDbModel> findByRoomIdAndFreeAndFinished(Integer roomId, boolean free, boolean finished);

	List<MedicalCheckupDbModel> findByCheckupTypeIdAndFreeAndFinished(Integer ctId, boolean free, boolean finished);

	List<MedicalCheckupDbModel> findByRoomIdAndFinished(Integer roomId, boolean finished);

	List<MedicalCheckupDbModel> findByCheckupTypeIdAndFinished(Integer ctId, boolean finished);

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorIdAndPatientIdAndFreeAndFinished(Integer clinicId, Integer doctorId,
			Integer patientId, boolean free, boolean finished);

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorIdAndFreeAndFinished(Integer clinicId, Integer doctorId,
			boolean free, boolean finished);

	List<MedicalCheckupDbModel> findByClinicIdAndPatientId(Integer clinicId, Integer patientId);

	List<MedicalCheckupDbModel> findByDoctorIdAndPatientId(Integer doctorId, Integer patientId);

	List<MedicalCheckupDbModel> findByRoomIdIsNull();

	List<MedicalCheckupDbModel> findByRoomIdAndDate(Integer roomId, String date);

	List<MedicalCheckupDbModel> findByRoomId(int roomId);

	List<MedicalCheckupDbModel> findByRoomIdAndDateAndTime(int roomId, String date, String time);

	List<MedicalCheckupDbModel> findByDoctorIdAndDateAndTime(int doctorId, String date, String time);

	List<MedicalCheckupDbModel> findByClinicIdAndFreeAndFinished(Integer clinicId, boolean b, boolean c);

	List<MedicalCheckupDbModel> findByClinicIdAndDoctorIdAndPatientIdNotNullAndFree(Integer clinicId, Integer doctorId,
			boolean b);

	List<MedicalCheckupDbModel> findByDoctorIdAndInProgress(Integer doctorId, boolean b);

}
