package com.real.order.repository;

import com.real.order.model.PaymentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentRequest, String> {

  @Override
  PaymentRequest findOne(String s);

  List<PaymentRequest> findBy(Query query);

  PaymentRequest findOneBy(Query query);

  Page<PaymentRequest> findByPaymentStatusIn(String[] status, Pageable page);

}
