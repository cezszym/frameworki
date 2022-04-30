package org.example.service;

import org.example.entity.Review;
import org.example.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(UUID id, Review Review) {
        if (Review.getId() == null) {
            throw new IllegalArgumentException("Review id cannot be null");
        }
        return reviewRepository.save(Review);
    }

    @Override
    public void deleteReview(UUID id) {
        reviewRepository.delete(findReviewById(id));
    }

    @Override
    public Review findReviewById(UUID id) {
        return reviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }

    @Override
    public Page<Review> findAllReviewByTitleLike(String title, int page) {
        return reviewRepository.findAllByTitleContainingIgnoreCase(Pageable.ofSize(10).withPage(Math.max(page, 0)), title);
    }

    @Override
    public Page<Review> findAllReviews(int page) {
        return reviewRepository.findAll(Pageable.ofSize(10).withPage(Math.max(page, 0)));
    }

    @Override
    public Page<Review> findAllReviewsByUserId(UUID id, int page) {
        return reviewRepository.findAllByUserId(Pageable.ofSize(10).withPage(Math.max(page, 0)), id);
    }
}
