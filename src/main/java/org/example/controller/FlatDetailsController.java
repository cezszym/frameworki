package org.example.controller;

import org.example.entity.FlatDetail;
import org.example.repository.FlatDetailRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RequestMapping("/api/flat_details")
@RestController
public class FlatDetailsController {
    private final FlatDetailRepository flatDetailRepository;
    private final UserRepository userRepository;
    private final Identity identity;

    public FlatDetailsController(final FlatDetailRepository flatDetailRepository, final UserRepository userRepository, Identity identity){
        this.flatDetailRepository = flatDetailRepository;
        this.userRepository = userRepository;
        this.identity = identity;
    }


}
