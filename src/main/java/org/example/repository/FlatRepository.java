package org.example.repository;

import org.example.entity.Flat;
import org.example.entity.Reservation;
import org.example.entity.User;
import org.example.other.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "flats", path = "flats")
public interface FlatRepository extends JpaRepository<Flat, UUID> {
    List<Flat> getAllFlatsByUser(User user);
    void deleteById(UUID reservationId);
}