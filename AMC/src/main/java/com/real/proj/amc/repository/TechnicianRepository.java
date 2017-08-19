package com.real.proj.amc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.job.Technician;

public interface TechnicianRepository
    extends MongoRepository<Technician, String>, PagingAndSortingRepository<Technician, String> {

  public Page<Technician> findBySkills(List<String> skills, Pageable page);

  public Page<Technician> findByAgencyId(String agencyId, Pageable page);

}
