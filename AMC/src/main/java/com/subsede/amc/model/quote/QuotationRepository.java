package com.subsede.amc.model.quote;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends MongoRepository<Quotation, String> {

}
