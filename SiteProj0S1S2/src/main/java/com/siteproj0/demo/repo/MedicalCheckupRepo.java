package com.siteproj0.demo.repo;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.DoctorDbModel;
import com.siteproj0.demo.dal.MedicalCheckupDbModel;

public interface MedicalCheckupRepo extends CrudRepository<MedicalCheckupDbModel, Integer> {

}
