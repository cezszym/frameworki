package org.example.controller;

import org.example.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RepositoryRestController
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewRepository reviewRepository;

    public ReviewController(final ReviewRepository reservationRepository) {
        this.reviewRepository = reservationRepository;
    }

    @GetMapping("/reviews")
    ResponseEntity<?> readAllReviews(){
        logger.warn("Exposing all reviews");
        return ResponseEntity.ok(reviewRepository.findAll());
    }
}