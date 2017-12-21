package com.real.proj.amc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.job.AbstractJob;
import com.real.proj.amc.model.job.JobConstants;
import com.real.proj.amc.model.job.JobEvents;
import com.real.proj.amc.model.job.JobStateHandler;
import com.real.proj.amc.model.job.JobStates;
import com.real.proj.amc.model.job.Technician;
import com.real.proj.amc.repository.JobRepository;
import com.real.proj.amc.repository.TechnicianRepository;

@RestController
@RequestMapping("/api/v1/admin")
@Secured("ROLE_ADMIN")
public class ServiceJobAdminController {

  Logger logger = LoggerFactory.getLogger(ServiceJobAdminController.class);

  JobRepository jobRepository;
  TechnicianRepository technicianRepository;

  JobStateHandler jobHandler;

  @Autowired
  public void setJobRepository(
      JobRepository jobRepository,
      TechnicianRepository technicianRepository,
      JobStateHandler jobHandler) {
    this.jobRepository = jobRepository;
    this.technicianRepository = technicianRepository;
    this.jobHandler = jobHandler;
  }

  @RequestMapping(path = "/job/{id}/assign", method = RequestMethod.POST)
  public void assignTechnician(@PathVariable String id, @RequestBody String technicianId) throws Exception {
    if (logger.isInfoEnabled())
      logger.info("Assign technician for the job {}", id);
    AbstractJob job = getJob(id);
    Technician technician = findUser(technicianId);
    Map<String, Object> data = new HashMap<>();
    data.put(JobConstants.JOB, job);
    data.put(JobConstants.TECHNICIAN, technician);
    fireEvent(JobStates.CREATED, JobEvents.ASSIGN, data);
  }

  protected void fireEvent(JobStates source, JobEvents event, Object data) throws Exception {
    Message<JobEvents> msg = MessageBuilder.withPayload(event)
        .setHeader("DATA_KEY", data)
        .build();
    this.jobHandler.handlEvent(msg, source);
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
