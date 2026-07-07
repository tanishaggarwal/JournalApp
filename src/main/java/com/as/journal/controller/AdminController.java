package com.as.journal.controller;

import com.as.journal.entity.User;
import com.as.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/all-users")
    public Iterable<User> fetchAllUsers()
    {
        return userService.fetchAll();
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createNewUser(@RequestBody User user)
    {
        try {
            userService.insert(user);
            return new ResponseEntity<>("User Created", HttpStatus.OK);
        }
        catch(Exception exception)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
