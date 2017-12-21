package com.subsede.user.repository.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.subsede.user.model.user.User;
import com.subsede.user.model.user.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {

    public VerificationToken findByUser(User user);
    public VerificationToken findByToken(String token);
    
}