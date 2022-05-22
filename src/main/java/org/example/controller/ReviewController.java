package org.example.controller;

import org.example.entity.Post;
import org.example.entity.Review;
import org.example.entity.User;
import org.example.model.ReviewDTO;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.example.service.PostService;
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
    private final PostService postService;
    private final Identity identity;

    public ReviewController(final ReviewRepository reviewRepository, final UserRepository userRepository, PostService postService, Identity identity){
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.postService = postService;
        this.identity = identity;
    }

    @GetMapping("/")
    public ResponseEntity<List<Review>> getAllByUser(){

        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.getAllReviewsByUser(this.userRepository.getById(this.identity.getCurrent().getId())));

            if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/post/{title}")
    public ResponseEntity<List<Review>> getAllByPostTitle(@PathVariable("title") String title){

        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Review> reviews = new ArrayList<>();
            List<Post> tempList= this.postService.findPostsByUserId(this.identity.getCurrent().getId());
            for (Post element : tempList) {
                if(element.getTitle().equals(title)){
                    reviews = new ArrayList<>(this.reviewRepository.getAllReviewsByPost(element));
                    if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    return new ResponseEntity<>(reviews, HttpStatus.OK);
                }
            }

            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review/{title}")
    public ResponseEntity<List<Review>> getAllReviewsByTitle(@PathVariable("title") String title){

        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.getAllReviewsByTitle(title));

            if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/post/likes/{postId}")
    public ResponseEntity<List<Review>> findAllReviewsByPostOrderByLikesDesc(@PathVariable("postId") UUID postId){

        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.findAllReviewsByPostOrderByLikesDesc(postId));

            if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/post/dislikes/{postId}")
    public ResponseEntity<List<Review>> findAllReviewsByPostOrderByDislikesDesc(@PathVariable("postId") UUID postId){

        try {
            // first, take current user's ID, then get user from database and put into getAllReservationsByUser
            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.findAllReviewsByPostOrderByDislikesDesc(postId));

            if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{reservationId}")
    public ResponseEntity<Review> getById(@PathVariable("reservationId") UUID reviewId){
        try{
            Review review = this.reviewRepository.getReviewByUserAndId(this.userRepository.getById(this.identity.getCurrent().getId()), reviewId);
            if(review == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(review, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody ReviewDTO reviewDTO){

        User user = this.userRepository.getById(this.identity.getCurrent().getId());
        Post post = this.postService.findPostById(reviewDTO.getPost().getId());

        if(post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            Review review = this.reviewRepository.save(Review.builder().
                    post(post).user(user).
                    title(reviewDTO.getTitle()).
                    contents(reviewDTO.getContents()).
                    exposureDate(reviewDTO.getExposureDate()).
                    likes(reviewDTO.getLikes()).
                    dislikes(reviewDTO.getDislikes()).build());
            return new ResponseEntity<>(review,HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateById(@PathVariable("reviewId") UUID reviewId, @RequestBody ReviewDTO reviewDTO){
        // Check if there is a reservation with given id that belongs to current user
        User user = this.userRepository.getById(this.identity.getCurrent().getId());
        Review oldReview = this.reviewRepository.getReviewByUserAndId(user, reviewId);
        if(oldReview == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            this.reviewRepository.deleteById(reviewId);
            return save(reviewDTO);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll(){
        try{
            this.reviewRepository.deleteAllReviewsByUser(this.userRepository.getById(this.identity.getCurrent().getId()));
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteById(@PathVariable("reviewId") UUID reviewId){
        try{
            Review currentReview = this.reviewRepository.getReviewByUserAndId(this.userRepository.getById(this.identity.getCurrent().getId()), reviewId);
            if(currentReview == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            this.reviewRepository.deleteById(reviewId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}