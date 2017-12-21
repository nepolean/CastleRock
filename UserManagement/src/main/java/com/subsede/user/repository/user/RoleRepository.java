package com.subsede.user.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.subsede.user.model.user.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

    public Role findByName(String name);
    
}