package org.example.service;

import org.example.entity.Post;
import org.example.entity.Review;
import org.example.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService{

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAllReviewsByPostId(UUID id) {
        return reviewRepository.getAllReviewsByPost(id);
    }

    public List<Review> findAllReviewsByUserId(UUID id) {
        return reviewRepository.getAllReviewsByUser(id);
    }

    public List<Review> findAllReviewsByTitle(String title) {
        return reviewRepository.getAllReviewsByTitle(title);
    }

    public List<Review> findAllReviewsByOrderByLikesDesc(UUID postId) {
        return reviewRepository.findAllReviewsByPostOrderByLikesDesc(postId);
    }

    public List<Review> findAllReviewsByOrderByDislikesDesc(UUID postId) {
        return reviewRepository.findAllReviewsByPostOrderByDislikesDesc(postId);
    }
}
