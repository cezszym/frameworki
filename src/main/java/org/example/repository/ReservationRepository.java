package org.example.repository;

import org.example.entity.Reservation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "reservation", path = "reservation")
public interface ReservationRepository extends PagingAndSortingRepository<Reservation, Long> {


}
