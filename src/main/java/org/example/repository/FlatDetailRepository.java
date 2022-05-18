package org.example.repository;

import org.example.entity.Flat;
import org.example.entity.FlatDetail;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "flat_details", path = "flat_details")
public interface FlatDetailRepository extends JpaRepository<FlatDetail, UUID> {
    List<FlatDetail> getAllFlatDetailsByUser(User user);
    void deleteById(UUID reservationId);
}
