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

    List<Reservation> getAllReservationsByUser(User user);
    Reservation getReservationByUserAndId(User user, UUID reservationId);
    List<Reservation> getAllReservationsByUserAndStatus(User user, ReservationStatus reservationStatus);
    void deleteById(UUID reservationId);
    void deleteAllReservationsByUser(User user);
}
