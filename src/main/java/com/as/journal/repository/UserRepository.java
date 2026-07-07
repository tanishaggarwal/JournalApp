package com.as.journal.repository;

import com.as.journal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<User, ObjectId> {

    public Optional<User> findByUserName(String userName);
    public void deleteByUserName(String userName);
}
