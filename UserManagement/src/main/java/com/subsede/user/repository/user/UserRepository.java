package com.subsede.user.repository.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.subsede.user.model.user.User;

@Repository
public interface UserRepository<U extends User> extends MongoRepository<U, String> {

  public U findByUsername(String username);

  public U findByEmail(String email);

  public U findByUsernameAndEmail(String username, String email);

  public List<U> findByLastName(String lastName);

  public Page<U> findAllByUsername(Pageable pageable, String username);

  public Page<U> findAllByUserType(Pageable pageable, String userType);

}