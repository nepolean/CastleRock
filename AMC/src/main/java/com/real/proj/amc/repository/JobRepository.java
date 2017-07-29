package com.real.proj.amc.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.real.proj.amc.model.AbstractJob;

public interface JobRepository
    extends MongoRepository<AbstractJob, String>, PagingAndSortingRepository<AbstractJob, String> {

  public List<AbstractJob> findBySourceTypeAndSourceId(String source, String soureId);

}
