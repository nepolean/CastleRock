package com.real.proj.amc.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.real.proj.amc.model.job.Agency;
import com.real.proj.amc.model.job.Technician;
import com.real.proj.amc.repository.AgencyRepository;
import com.real.proj.amc.repository.TechnicianRepository;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class ServiceAgencyAdminController {

  private static Logger logger = LoggerFactory.getLogger(ServiceAgencyAdminController.class);

  private AgencyRepository agencyRepository;
  private TechnicianRepository technicianRepository;

  @Autowired
  public void setAgencyRepository(
      AgencyRepository agencyRepository,
      TechnicianRepository technicianRepository) {
    this.agencyRepository = agencyRepository;
    this.technicianRepository = technicianRepository;
  }

  @RequestMapping(path = "/agency/{id}", method = { RequestMethod.POST })
  public ResponseEntity<Agency> getAgencyFromID(
      @PathVariable String agencyId) {
    Agency agency = getAgency(agencyId);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @RequestMapping(path = "/", method = { RequestMethod.POST, RequestMethod.PUT })
  public ResponseEntity<Agency> createAgency(@RequestBody @Valid Agency agency) {
    logger.info("Create new agency {}", agency);
    agency = this.agencyRepository.save(agency);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @RequestMapping(path = "/agency/{id}/block", method = { RequestMethod.POST })
  public ResponseEntity<Agency> blockAgency(
      @PathVariable String agencyId) {
    Agency agency = getAgency(agencyId);
    agency.setActive(false);
    agency = this.agencyRepository.save(agency);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @RequestMapping(path = "/agency/{id}/unblock", method = { RequestMethod.POST })
  public ResponseEntity<Agency> unblockAgency(
      @PathVariable String agencyId) {
    Agency agency = getAgency(agencyId);
    agency.setActive(true);
    agency = this.agencyRepository.save(agency);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @RequestMapping(path = "/agency/{id}/technician", method = { RequestMethod.POST })
  public ResponseEntity<Technician> createTechnician(
      @PathVariable String agencyId,
      @RequestBody @Valid Technician technician) {
    Agency agency = getAgency(agencyId);
    technician.setAgency(agency);
    this.technicianRepository.save(technician);
    return new ResponseEntity<>(technician, HttpStatus.OK);
  }

  @RequestMapping(path = "/agency/{id}/technicians", method = { RequestMethod.GET })
  public ResponseEntity<Page<Technician>> fetchTechnicians(
      @PathVariable String agencyId,
      Pageable page) {
    Page<Technician> technicians = this.technicianRepository.findByAgencyId(agencyId, page);
    return new ResponseEntity<>(technicians, HttpStatus.OK);
  }

  @RequestMapping(path = "/technician/skills", method = RequestMethod.GET)
  public ResponseEntity<Page<Technician>> getTechniciansWithMatchingSkills(
      @RequestBody @Valid List<String> skills,
      Pageable page) {
    logger.info("Find technicians by skills {}", skills);
    Page<Technician> technicians = this.technicianRepository.findBySkills(skills, page);
    return new ResponseEntity<Page<Technician>>(technicians, HttpStatus.OK);
  }

  private Agency getAgency(String agencyId) {
    Agency agency = this.agencyRepository.findOne(agencyId);
    agency = Objects.requireNonNull(agency, "Agency with id " + agencyId + " cannot be found.");
    return agency;
  }

}
