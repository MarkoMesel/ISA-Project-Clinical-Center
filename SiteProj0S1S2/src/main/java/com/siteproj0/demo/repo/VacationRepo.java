package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.RoomDbModel;
import com.siteproj0.demo.dal.VacationDbModel;

public interface VacationRepo extends CrudRepository<VacationDbModel, Integer>{

	List<VacationDbModel> findByApprovedAndEnabled(boolean approved, boolean enabled);

	List<VacationDbModel> findByEnabled(boolean b);

}
