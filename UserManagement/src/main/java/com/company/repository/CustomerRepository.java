package com.company.repository;

import com.company.model.Customer;
import com.company.repository.user.UserRepository;

public interface CustomerRepository extends UserRepository<Customer> {

    public Customer findByAddress(String address);

}