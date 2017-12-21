package com.subsede.amc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.subsede.amc.model.job.AbstractJob;

public interface JobRepository
    extends MongoRepository<AbstractJob, String>, PagingAndSortingRepository<AbstractJob, String> {

  public List<AbstractJob> findBySourceTypeAndSourceId(String source, String soureId);

  public List<AbstractJob> findByScheduledDateLessThan(Date dt);

  public List<AbstractJob> findByScheduledDateGreaterThan(Date dt);

}
