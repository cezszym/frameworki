package org.example.controller;

import org.example.dto.PageDTO;
import org.example.dto.ReviewDTO;
import org.example.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public PageDTO<ReviewDTO> getAllReviews(@RequestParam(defaultValue = "0") int page) {
        return new PageDTO<>(reviewService.findAllReviews(page), ReviewDTO::fromEntity);
    }

    @GetMapping("/byUser/{userId}")
    public PageDTO<ReviewDTO> getAllPostsByUser(@PathVariable UUID userId, int page) {
        return new PageDTO<>(reviewService.findAllReviewsByUserId(userId, page), ReviewDTO::fromEntity);
    }

    @GetMapping("/byTitle")
    public PageDTO<ReviewDTO> getAllPostsBTitle(String title, int page) {
        return new PageDTO<>(reviewService.findAllReviewByTitleLike(title, page), ReviewDTO::fromEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
    }

    @PostMapping
    public ReviewDTO createReview(@RequestBody ReviewDTO reviewDTO) {
        return ReviewDTO.fromEntity(reviewService.createReview(reviewDTO.toEntity()));
    }

    @PutMapping("/{id}")
    public ReviewDTO updateReview(@PathVariable UUID id, @RequestBody ReviewDTO reviewDTO) {
        return ReviewDTO.fromEntity(reviewService.updateReview(id, reviewDTO.toEntity()));
    }
}