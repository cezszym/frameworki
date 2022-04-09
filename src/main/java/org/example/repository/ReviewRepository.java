package org.example.repository;

import org.example.entity.Reservation;
import org.example.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "review", path = "review")
public interface ReviewRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path = "review", rel = "review")
    Review findReviewById(@Param("id") UUID id);

    @RestResource(path = "myreviews", rel = "myreviews")
    List<Review> findAllByUserId(@Param("id") UUID id);
}
