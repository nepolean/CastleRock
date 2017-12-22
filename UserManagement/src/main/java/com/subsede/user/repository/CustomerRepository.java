package com.subsede.user.repository;

import org.springframework.stereotype.Repository;

import com.subsede.user.model.Customer;
import com.subsede.user.repository.user.UserRepository;

@Repository
public interface CustomerRepository extends UserRepository<Customer> {

  public Customer findByAddress(String address);

}