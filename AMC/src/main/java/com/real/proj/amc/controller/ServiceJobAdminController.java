package com.real.proj.amc.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.job.AbstractJob;
import com.real.proj.amc.model.job.Technician;
import com.real.proj.amc.repository.JobRepository;
import com.real.proj.amc.repository.TechnicianRepository;

@RestController
@RequestMapping("/api/v1/admin")
public class ServiceJobAdminController {

  Logger logger = LoggerFactory.getLogger(ServiceJobAdminController.class);
  JobRepository jobRepository;
  TechnicianRepository technicianRepository;

  @Autowired
  public void setJobRepository(
      JobRepository jobRepository,
      TechnicianRepository technicianRepository) {
    this.jobRepository = jobRepository;
    this.technicianRepository = technicianRepository;
  }

  @RequestMapping(path = "/job/assign", method = RequestMethod.POST)
  public void assignTechnician(@PathVariable String jobId, @RequestBody String technicianId) {
    if (logger.isInfoEnabled())
      logger.info("Assign technician for the job {}", jobId);
    AbstractJob job = getJob(jobId);
    Technician technician = findUser(technicianId);

    job.assign(technician);
  }

  private Technician findUser(String technicianId) {
    Technician technician = this.technicianRepository.findOne(technicianId);
    technician = Objects.requireNonNull(technician, "Technician with id " + technicianId + " not found.");
    return technician;
  }

  private AbstractJob getJob(String jobId) {
    AbstractJob job = this.jobRepository.findOne(jobId);
    job = Objects.requireNonNull(job, "Job with id " + jobId + " not found.");
    return job;
  }

}
