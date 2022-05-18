package org.example.controller;

import org.example.security.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/example")
public class ExampleController {

    public Identity identity;

    @Autowired
    public ExampleController(Identity identity){
        this.identity = identity;
    }

    @GetMapping
    public String getExampleData(){
        return identity.getCurrent().getId().toString();

    }
}
