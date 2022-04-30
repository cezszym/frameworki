package org.example.service;

import org.example.entity.Review;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ReviewService {

    Review createReview(Review review);

    Review updateReview(UUID id, Review review);

    void deleteReview(UUID id);

    Review findReviewById(UUID id);

    Page<Review> findAllReviewsByUserId(UUID id, int page);

    Page<Review> findAllReviewByTitleLike(String title, int page);

    Page<Review> findAllReviews(int page);
}
