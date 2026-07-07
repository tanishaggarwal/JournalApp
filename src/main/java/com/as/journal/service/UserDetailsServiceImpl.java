package com.as.journal.service;

import com.as.journal.entity.User;
import com.as.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);

        if(user.isPresent()) {
            return org.springframework.security.core.userdetails.User.withUsername(user.get().getUserName()).
                    password(user.get().getPassword()).
                    roles(user.get().getRoles().toArray(new String[0]))
                    .build();
        }
        else {
            throw new UsernameNotFoundException("user not found: "+username);
        }
    }
}
