package com.siteproj0.demo.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.siteproj0.demo.dal.ScheduleDbModel;

public interface ScheduleRepo extends CrudRepository<ScheduleDbModel, Integer> {
	List<ScheduleDbModel> findByDay(int day);
}
