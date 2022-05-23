package org.example.controller;

import org.example.entity.Post;
import org.example.entity.Reservation;
import org.example.entity.User;
import org.example.model.ReservationDTO;
import org.example.other.ReservationStatus;
import org.example.repository.ReservationRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.example.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/reservations")
@RestController
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final Identity identity;

    public ReservationController(final ReservationRepository reservationRepository, final UserRepository userRepository, final PostService postService, Identity identity){
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.postService = postService;
        this.identity = identity;
    }

    @GetMapping("/")
    public ResponseEntity<List<Reservation>> getAll(){
        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Reservation> reservations = new ArrayList<>(this.reservationRepository.getAllReservationsByUser(this.userRepository.getById(this.identity.getCurrent().getId())));

            if (reservations.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getById(@PathVariable("reservationId") UUID reservationId){
        try{
            Reservation reservation = this.reservationRepository.getReservationByUserAndId(this.userRepository.getById(this.identity.getCurrent().getId()), reservationId);
            if(reservation == null) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getAllByStatus(@PathVariable("status") ReservationStatus status){
        try {
            ArrayList<Reservation> reservations = new ArrayList<>(this.reservationRepository.getAllReservationsByUserAndStatus(this.userRepository.getById(this.identity.getCurrent().getId()), status));
            if(reservations.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody ReservationDTO reservationDTO){

        User user = this.userRepository.getById(this.identity.getCurrent().getId());
        Post post = this.postService.findPostById(reservationDTO.getPostDTO().getId());

        if(post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            Reservation reservation = this.reservationRepository.save(Reservation.builder().
                    post(post).user(user).
                    start(reservationDTO.getStart()).
                    end(reservationDTO.getEnd()).
                    status(reservationDTO.getStatus()).
                    created(reservationDTO.getCreated()).build());
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<?> updateById(@PathVariable("reservationId") UUID reservationId, @RequestBody ReservationDTO reservationDTO){

        // Check if there is a reservation with given id that belongs to current user
        User user = this.userRepository.getById(this.identity.getCurrent().getId());
        Reservation oldReservation = this.reservationRepository.getReservationByUserAndId(user, reservationId);
        if(oldReservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            this.reservationRepository.deleteById(reservationId);
            return save(reservationDTO);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteById(@PathVariable("reservationId") UUID reservationId){
        try{
            Reservation currentReservation = this.reservationRepository.getReservationByUserAndId(this.userRepository.getById(this.identity.getCurrent().getId()), reservationId);
            if(currentReservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            this.reservationRepository.deleteById(reservationId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll(){
        try{
            this.reservationRepository.deleteAllReservationsByUser(this.userRepository.getById(this.identity.getCurrent().getId()));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


