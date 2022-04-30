package org.example.repository;

import org.example.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "reviews")
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @RestResource(path = "review", rel = "review")
    Review findReviewById(@Param("id") UUID id);

    @RestResource(path = "myreviews", rel = "myreviews")
    List<Review> findAllByUserId(@Param("id") UUID id);

    Page<Review> findAllByTitleContainingIgnoreCase(Pageable pageable, String title);

    Page<Review> findAllByUserId(Pageable pageable, UUID userId);
}
