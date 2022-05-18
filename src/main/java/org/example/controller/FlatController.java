package org.example.controller;

import org.example.entity.Flat;
import org.example.entity.Reservation;
import org.example.entity.Review;
import org.example.other.ReservationStatus;
import org.example.repository.FlatRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/flats")
@RestController
public class FlatController {
    private final FlatRepository flatRepository;
    private final UserRepository userRepository;
    private final Identity identity;

    public FlatController(final FlatRepository flatRepository, final UserRepository userRepository, Identity identity){
        this.flatRepository = flatRepository;
        this.userRepository = userRepository;
        this.identity = identity;
    }

    @PostMapping("/create")
    public ResponseEntity<Flat> save(@RequestBody Flat flat){
        try{
            return ResponseEntity.ok(this.flatRepository.save(flat));
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
