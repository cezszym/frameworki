package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.entity.Post;
import org.example.entity.Reservation;
import org.example.entity.User;
import org.example.model.ReservationDTO;
import org.example.other.ReservationStatus;
import org.example.repository.PostRepository;
import org.example.repository.ReservationRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
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
    private final PostRepository postRepository;
    private final Identity identity;

    public ReservationController(final ReservationRepository reservationRepository, final UserRepository userRepository, final PostRepository postRepository, Identity identity){
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.identity = identity;
    }

    @Operation(summary = "Get all reservations")
    @GetMapping("/")
    public ResponseEntity<List<Reservation>> getAll(){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Reservation> reservations = new ArrayList<>(this.reservationRepository.getAllByUser(this.userRepository.getById(this.identity.getCurrent().getId())));

            if (reservations.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get reservation by id")
    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getById(@PathVariable("reservationId") UUID reservationId){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            Reservation reservation = this.reservationRepository.getByUserAndId(user, reservationId);
            if(reservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all reservations by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getAllByStatus(@PathVariable("status") ReservationStatus status){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try {
            ArrayList<Reservation> reservations = new ArrayList<>(this.reservationRepository.getAllByUserAndStatus(user, status));
            if(reservations.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create reservation")
    @PostMapping("/{postId}")
    public ResponseEntity<?> save(@PathVariable("postId") UUID postId, @RequestBody ReservationDTO reservationDTO){

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Post post = this.postRepository.getByUserAndId(user, postId);
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

    @Operation(summary = "Update reservation by id")
    @PutMapping("/{reservationId}")
    public ResponseEntity<?> update(@PathVariable("reservationId") UUID reservationId, @RequestBody ReservationDTO reservationDTO){

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Reservation currentReservation = this.reservationRepository.getByUserAndId(user, reservationId);
        if(currentReservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Post post = currentReservation.getPost();

        try{
            deleteById(reservationId);

            Reservation reservation = this.reservationRepository.save(Reservation.builder().
                    post(post).user(user).
                    start(reservationDTO.getStart()).
                    end(reservationDTO.getEnd()).
                    status(reservationDTO.getStatus()).
                    created(reservationDTO.getCreated()).build());

            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete reservation by id")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteById(@PathVariable("reservationId") UUID reservationId){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            Reservation reservation = this.reservationRepository.getByUserAndId(user, reservationId);
            if(reservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            this.reservationRepository.deleteById(reservationId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete reservation by status")
    @DeleteMapping("/status/{status}")
    public ResponseEntity<?> deleteByStatus(@PathVariable("status") ReservationStatus status){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            List<Reservation> reservations = this.reservationRepository.getAllByUserAndStatus(user, status);
            if(reservations.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            for(Reservation reservation : reservations)
                this.reservationRepository.delete(reservation);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete all reservations")
    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll(){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            List<Reservation> reservations = this.reservationRepository.getAllByUser(user);
            if(reservations.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            for(Reservation reservation : reservations)
                this.reservationRepository.delete(reservation);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


