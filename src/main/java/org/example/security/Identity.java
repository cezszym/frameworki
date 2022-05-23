package org.example.security;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Identity {

    private final UserRepository userRepository;
    public Identity(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getCurrent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByNick(authentication.getName());
        return userOptional.orElse(null);
    }
}
