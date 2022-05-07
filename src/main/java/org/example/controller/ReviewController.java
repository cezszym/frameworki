package org.example.controller;

import org.example.entity.Review;
import org.example.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{postId}")
    public List<?> getAllReviewsByPost(UUID postId) {
        return reviewService.findAllReviewsByPostId(postId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getAllReviewsByUser(@PathVariable UUID userId) {
        return reviewService.findAllReviewsByUserId(userId);
    }

    @GetMapping("/titles/{title}")
    public List<Review> getAllReviewsByTitle(@PathVariable String title) {
        return reviewService.findAllReviewsByTitle(title);
    }

    @GetMapping("/most-like")
    public List<Review> getAllReviewsMostLikes(UUID postId) {
        return reviewService.findAllReviewsByOrderByLikesDesc(postId);
    }

    @GetMapping("/most-dislike")
    public List<Review> getAllReviewsMostDislikes(UUID postId) {
        return reviewService.findAllReviewsByOrderByDislikesDesc(postId);
    }
}