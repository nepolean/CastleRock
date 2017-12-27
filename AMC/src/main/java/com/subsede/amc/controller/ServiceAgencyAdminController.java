package com.subsede.amc.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.subsede.amc.model.job.Agency;
import com.subsede.amc.model.job.Technician;
import com.subsede.amc.repository.AgencyRepository;
import com.subsede.amc.repository.TechnicianRepository;

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

  @GetMapping(path = "/agency/{id}")
  public ResponseEntity<Agency> getAgencyFromID(
      @PathVariable String agencyId) {
    Agency agency = getAgency(agencyId);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }
  
  @GetMapping(path="/agency/valid")
  public ResponseEntity<List<Agency>> getValidAgencies() {
    return ResponseEntity.ok(this.agencyRepository.findByIsActive(Boolean.TRUE));
  }
  
  @GetMapping(path="/agency/all")
  public ResponseEntity<Page<Agency>> getValidAgencies(Pageable page) {
    return ResponseEntity.ok(this.agencyRepository.findAll(page));
  }

  @PostMapping(path = "/agency/onboard")
  public ResponseEntity<Agency> createAgency(@RequestBody @Valid Agency agency) {
    logger.info("Create new agency {}", agency);
    agency = this.agencyRepository.save(agency);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @PostMapping(path = "/agency/{id}/block")
  public ResponseEntity<Agency> blockAgency(
      @PathVariable String agencyId) {
    Agency agency = getAgency(agencyId);
    agency.setActive(false);
    agency = this.agencyRepository.save(agency);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @PostMapping(path = "/agency/{id}/unblock")
  public ResponseEntity<Agency> unblockAgency(
      @PathVariable String agencyId) {
    Agency agency = getAgency(agencyId);
    agency.setActive(true);
    agency = this.agencyRepository.save(agency);
    return new ResponseEntity<>(agency, HttpStatus.OK);
  }

  @PostMapping(path = "/agency/{id}/technician")
  public ResponseEntity<Technician> createTechnician(
      @PathVariable String agencyId,
      @RequestBody @Valid Technician technician) {
    Agency agency = getAgency(agencyId);
    technician.setAgency(agency);
    this.technicianRepository.save(technician);
    return new ResponseEntity<>(technician, HttpStatus.OK);
  }

  @GetMapping(path = "/agency/{id}/technicians")
  public ResponseEntity<Page<Technician>> fetchTechnicians(
      @PathVariable String agencyId,
      Pageable page) {
    Page<Technician> technicians = this.technicianRepository.findByAgencyId(agencyId, page);
    return new ResponseEntity<>(technicians, HttpStatus.OK);
  }

  @GetMapping(path = "/technician/skills")
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
