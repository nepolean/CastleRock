package com.subsede.amc.service.job;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.subsede.amc.model.job.AbstractJob;
import com.subsede.amc.repository.JobRepository;

@Component
public class JobScheduler {
  
  private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
  
  private static final int SCHEDULE_DELAY_FOR_IMMEDIATE = 2;

  
  JobRepository jRepository;
  
  @Autowired
  public void setRepository(JobRepository jRepository) {
    this.jRepository = jRepository;
  }
  

  public void scheduleImmediate(Set<AbstractJob> jobs) {
    logger.info("Scheduling jobs" );
    //let's give 2/3 days time frame
    Date targetDate = this.getDateAfter();
    if (logger.isDebugEnabled())
      logger.debug("Scheduling on {}", targetDate);
    for(AbstractJob job : jobs)
      job.schedule(targetDate);
    this.jRepository.save(jobs);
  }
  
  
  
  private Date getDateAfter() {
    Instant now = Instant.now();
    return Date.from(now.plus(SCHEDULE_DELAY_FOR_IMMEDIATE, ChronoUnit.DAYS));
  }

}
