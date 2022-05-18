package org.example.controller;

import org.example.entity.Flat;
import org.example.entity.FlatDetail;
import org.example.entity.Reservation;
import org.example.entity.Review;
import org.example.other.ReservationStatus;
import org.example.repository.FlatDetailRepository;
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
public class FlatDetailsController {
    private final FlatDetailRepository flatDetailRepository;
    private final UserRepository userRepository;
    private final Identity identity;

    public FlatDetailsController(final FlatDetailRepository flatDetailRepository, final UserRepository userRepository, Identity identity){
        this.flatDetailRepository = flatDetailRepository;
        this.userRepository = userRepository;
        this.identity = identity;
    }

    @DeleteMapping("/delete/{flatDetailsId}")
    public ResponseEntity<?> deleteById(@PathVariable("flatDetailsId") UUID flatDetailsId){
        try{
            FlatDetail flatDetailToDelete = this.flatDetailRepository.getById(flatDetailsId);
            if(flatDetailToDelete == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            if (!this.userRepository.getById(this.identity.getCurrent().getId()).equals(flatDetailToDelete.getUser().getId())) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            this.flatDetailRepository.deleteById(flatDetailsId);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{flatDetailsId}")
    public ResponseEntity<?> editById(@PathVariable("flatDetailsId") UUID flatDetailsId, @RequestBody FlatDetail flatDetail){
        try{
            FlatDetail flatDetailsToEdit = this.flatDetailRepository.getById(flatDetailsId);
            if(flatDetailsToEdit == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            if (!this.userRepository.getById(this.identity.getCurrent().getId()).equals(flatDetailsToEdit.getUser().getId())) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            this.flatDetailRepository.deleteById(flatDetailsId);
            this.flatDetailRepository.save(flatDetail);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current_user")
    public ResponseEntity<FlatDetail> getFlatDetailsByUser(){
        try {
            ArrayList<FlatDetail> flatDetails = new ArrayList<>(this.flatDetailRepository.getAllFlatDetailsByUser(this.userRepository.getById(this.identity.getCurrent().getId())));
            if (flatDetails.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity(flatDetails, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
