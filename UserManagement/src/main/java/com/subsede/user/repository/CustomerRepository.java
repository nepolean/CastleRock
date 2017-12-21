package com.subsede.user.repository;

import com.subsede.user.model.Customer;
import com.subsede.user.repository.user.UserRepository;

public interface CustomerRepository extends UserRepository<Customer> {

    public Customer findByAddress(String address);

}