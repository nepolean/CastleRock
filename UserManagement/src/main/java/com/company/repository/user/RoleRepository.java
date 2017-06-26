package com.company.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.company.model.user.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    public Role findByName(String name);
    
}