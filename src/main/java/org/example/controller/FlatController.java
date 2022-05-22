package org.example.controller;

import org.example.entity.*;
import org.example.model.FlatDTO;
import org.example.repository.FlatDetailRepository;
import org.example.repository.FlatRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RequestMapping("/api/flats")
@RestController
public class FlatController {
    private final FlatRepository flatRepository;
    private final FlatDetailRepository flatDetailRepository;
    private final UserRepository userRepository;
    private final Identity identity;

    public FlatController(final FlatRepository flatRepository, FlatDetailRepository flatDetailRepository, final UserRepository userRepository, Identity identity){
        this.flatRepository = flatRepository;
        this.flatDetailRepository = flatDetailRepository;
        this.userRepository = userRepository;
        this.identity = identity;
    }

    @PostMapping("/create")
    public ResponseEntity<Flat> save(@RequestBody FlatDTO flatDTO){
        User user = this.userRepository.getById(this.identity.getCurrent().getId());
        Flat tempFlat = this.flatRepository.getById(flatDTO.getId());
        FlatDetail flatDetail = this.flatDetailRepository.getFlatDetailByFlat(tempFlat);

        if(flatDetail == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            Flat flat = this.flatRepository.save(Flat.builder().
                    user(user).flatDetail(flatDetail).
                    adress(flatDTO.getAdress()).
                    postCode(flatDTO.getPostCode()).
                    city(flatDTO.getCity()).
                    country(flatDTO.getCountry()).
                    metrage(flatDTO.getMetrage()).
                    numOfRooms(flatDTO.getNumOfRooms()).build());
            return new ResponseEntity<>(flat,HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{flatId}")
    public ResponseEntity<?> deleteById(@PathVariable("flatId") UUID flatId){
        try{
            Flat flatToDelete = this.flatRepository.getById(flatId);
            if(flatToDelete == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            if (!this.userRepository.getById(this.identity.getCurrent().getId()).equals(flatToDelete.getUser().getId())) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            return ResponseEntity.ok("Deleted");
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> count(){
        try{
            return ResponseEntity.ok(this.flatRepository.count());
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current_user")
    public ResponseEntity<Flat> getFlatsByUser(){
        try {
            ArrayList<Flat> flats = new ArrayList<>(this.flatRepository.getAllFlatsByUser(this.userRepository.getById(this.identity.getCurrent().getId())));
            if (flats.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity(flats, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
