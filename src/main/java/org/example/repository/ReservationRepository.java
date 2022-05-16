package org.example.repository;

import org.example.entity.Reservation;
import org.example.entity.User;
import org.example.other.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> getAllReservationsByUser(UUID userId);
    Reservation getReservationByUserAndId(UUID userId, UUID reservationId);
    List<Reservation> getAllReservationsByUserAndStatus(UUID userId, ReservationStatus reservationStatus);
//    void updateReservation(UUID id, Reservation reservation);
    void deleteById(UUID id);



//
//
//    Optional<List<Reservation>> findByStatus(ReservationStatus reservationStatus);
//
//    Reservation save(Reservation reservation);
//
//    void deleteById(UUID id);
//
//    void updateById(UUID id, Reservation reservation);
}
