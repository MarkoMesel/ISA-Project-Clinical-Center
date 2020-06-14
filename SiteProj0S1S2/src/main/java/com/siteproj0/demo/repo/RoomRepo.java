package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.RoomDbModel;

public interface RoomRepo extends CrudRepository<RoomDbModel, Integer>{

	List<RoomDbModel> findByClinicIdAndEnabled(Integer clinicId, boolean enabled);

	RoomDbModel findByIdAndClinicId(Integer roomId, Integer clinicId);

	RoomDbModel findByIdAndClinicIdAndEnabled(Integer roomId, Integer clinicId, boolean enabled);

	RoomDbModel findByNameAndNumberAndClinicIdAndEnabled(String name,
																String number,
																Integer clinicId,
																boolean enabled);

	List<RoomDbModel> findByNameAndClinicIdAndEnabled(String searchByThis, Integer clinicId, boolean enabled);
	List<RoomDbModel> findByNumberAndClinicIdAndEnabled(String searchByThis, Integer clinicId, boolean enabled);


}
