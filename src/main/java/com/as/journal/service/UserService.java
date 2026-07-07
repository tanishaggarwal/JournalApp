package com.as.journal.service;

import com.as.journal.entity.User;
import com.as.journal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public void insert(User user) throws Exception {
        try {
            if (user.getUserName()!=null) {
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                userRepository.save(user);
                log.info("User Created Successfully");
            }
            else {
                throw new Exception("USERNAME NOT GIVEN!!");
            }
        }
        catch (Exception exception)
        {
         log.error("Error Occurred while creating the user in db : {}", exception.getMessage(), exception);
         //throw new Exception("Error occurred while creating new user in DB: " + exception.getMessage());
        }

    }

    public Iterable<User> fetchAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> fetchById(ObjectId id )
    {
        return userRepository.findById(id);
    }

    public void deleleById(ObjectId id)
    {
        userRepository.deleteById(id);
    }

    public void update(User entry, String userName) throws Exception {
        // updates only username and password and not the journal entry references of the user
        Optional<User> oldUser = userRepository.findByUserName(userName);
        try {
            User user = oldUser.get();
            user.setUserName(entry.getUserName());
            user.setPassword(encoder.encode(entry.getPassword()));
            user.setRoles(entry.getRoles());
            userRepository.save(user);
        }
        catch(NoSuchElementException e) {
            throw new Exception("No such user exists!!");
        }
        catch(Exception exception)
        {
            throw new Exception(exception.getMessage());
        }
    }
}
