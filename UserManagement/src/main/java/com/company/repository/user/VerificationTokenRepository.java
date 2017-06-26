package com.company.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.company.model.user.User;
import com.company.model.user.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

    public VerificationToken findByUser(User user);
    public VerificationToken findByToken(String token);
    
}