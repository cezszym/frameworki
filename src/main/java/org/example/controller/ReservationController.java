package org.example.controller;

import org.example.entity.Reservation;
import org.example.other.ReservationStatus;
import org.example.repository.ReservationRepository;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/api/reservations")
@RestController
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final UUID id;

    public ReservationController(final ReservationRepository reservationRepository, Identity identity){
        this.reservationRepository = reservationRepository;
        this.id = identity.getCurrent().getId();
    }

    @GetMapping("/")
    public ResponseEntity<List<Reservation>> getAll(){

        try {
            List<Reservation> reservations = new ArrayList<>(this.reservationRepository.getAllReservationsByUser(this.id));

            if (reservations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getById(@PathVariable("reservationId") UUID reservationId){
        try{
            Reservation reservation = this.reservationRepository.getReservationByUserAndId(this.id, reservationId);
            if(reservation == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getAllByStatus(@PathVariable("status") String status){
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            ArrayList<Reservation> reservations = new ArrayList<>(this.reservationRepository.getAllReservationsByUserAndStatus(this.id, reservationStatus));
            if(reservations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody Reservation reservation){
        try{
            this.reservationRepository.save(reservation);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<?> updateById(@PathVariable("reservationId") UUID reservationId, @RequestBody Reservation reservation){
        try{
            Reservation currentReservation = this.reservationRepository.getReservationByUserAndId(this.id, reservationId);
            if(currentReservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            reservation.setId(reservationId);
//            this.reservationRepository.deleteById(reservationId);
//            this.reservationRepository.save(reservation);
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteById(@PathVariable("reservationId") UUID reservationId){
        try{
            Reservation currentReservation = this.reservationRepository.getReservationByUserAndId(this.id, reservationId);
            if(currentReservation == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            this.reservationRepository.deleteById(reservationId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


