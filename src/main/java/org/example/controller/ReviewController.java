package org.example.controller;

import org.example.entity.Post;
import org.example.entity.Review;
import org.example.entity.User;
import org.example.model.ReviewDTO;
import org.example.repository.PostRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final Identity identity;

    public ReviewController(final ReviewRepository reviewRepository, final UserRepository userRepository, PostRepository postRepository, Identity identity){
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.identity = identity;
    }

    @GetMapping("/")
    public ResponseEntity<List<Review>> getAllByUser(){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.getAllReviewsByUser(user));

            if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/post/likes/{postId}")
    public ResponseEntity<List<Review>> findAllReviewsByPostOrderByLikesDesc(@PathVariable("postId") UUID postId){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.findAllReviewsByPostOrderByLikesDesc(postId));
            if (reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/post/dislikes/{postId}")
    public ResponseEntity<List<Review>> findAllReviewsByPostOrderByDislikesDesc(@PathVariable("postId") UUID postId){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.findAllReviewsByPostOrderByDislikesDesc(postId));
            if (reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getById(@PathVariable("reviewId") UUID reviewId){
        try{
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Review review = this.reviewRepository.getReviewByUserAndId(user, reviewId);
            if(review == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> save(@PathVariable("postId") UUID id, @RequestBody ReviewDTO reviewDTO){

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Post post = this.postRepository.getByUserAndId(user, id);
        if(post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            Review review = this.reviewRepository.save(Review.builder().
                    post(post).user(user).
                    title(reviewDTO.getTitle()).
                    contents(reviewDTO.getContents()).
                    exposureDate(reviewDTO.getExposureDate()).
                    likes(reviewDTO.getLikes()).
                    dislikes(reviewDTO.getDislikes()).build());

            return new ResponseEntity<>(review, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> update(@PathVariable("reviewId") UUID reviewId, @RequestBody ReviewDTO reviewDTO){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Review currentReview = this.reviewRepository.getReviewByUserAndId(user, reviewId);
        if(currentReview == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Post post = currentReview.getPost();

        try{
            // Delete existing review
            deleteById(reviewId);

            // Add new ones
            Review review = this.reviewRepository.save(Review.builder().
                    post(post).user(user).
                    title(reviewDTO.getTitle()).
                    contents(reviewDTO.getContents()).
                    exposureDate(reviewDTO.getExposureDate()).
                    likes(reviewDTO.getLikes()).
                    dislikes(reviewDTO.getDislikes()).build());

            return new ResponseEntity<>(review,HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteById(@PathVariable("reviewId") UUID reviewId){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            Review review = this.reviewRepository.getReviewByUserAndId(user, reviewId);
            if(review == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            this.reviewRepository.deleteById(reviewId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll(){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            this.reviewRepository.deleteAllReviewsByUser(this.userRepository.getById(this.identity.getCurrent().getId()));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}