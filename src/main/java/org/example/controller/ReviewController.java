package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.entity.Post;
import org.example.entity.Review;
import org.example.entity.User;
import org.example.model.ReviewDTO;
import org.example.repository.PostRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


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

    @Operation(summary = "Get all reviews for authenticated user")
    @GetMapping("/")
    public ResponseEntity<CollectionModel<Review>> getAllByUser(){

        Link link = linkTo(ReviewController.class).withSelfRel();

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.getAllByUser(user));

            reviews.forEach(review -> review.add(linkTo(ReviewController.class).slash(review.getId()).withSelfRel()));

            if (reviews.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return ResponseEntity.ok(CollectionModel.of(reviews,link));
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all reviews by post id in descending order of likes")
    @GetMapping("/post/likes/{postId}")
    public ResponseEntity<EntityModel<Review>> findAllReviewsByPostOrderByLikesDesc(@PathVariable("postId") UUID postId){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Post post = this.postRepository.getByUserAndId(user, postId);
            if(post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.findAllReviewsByPostOrderByLikesDesc(post));
            if (reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            Link link = linkTo(ReviewController.class).slash("post/likes").slash(reviews.get(0).getId()).withSelfRel();

            return ResponseEntity.ok(EntityModel.of(reviews.get(0),link));
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all reviews by post id in descending order of dislikes")
    @GetMapping("/post/dislikes/{postId}")
    public ResponseEntity<EntityModel<Review>> findAllReviewsByPostOrderByDislikesDesc(@PathVariable("postId") UUID postId){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Post post = this.postRepository.getByUserAndId(user, postId);
            if(post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            ArrayList<Review> reviews = new ArrayList<>(this.reviewRepository.findAllReviewsByPostOrderByDislikesDesc(post));
            if (reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            Link link = linkTo(ReviewController.class).slash("post").slash("dislikes").slash(reviews.get(0).getId()).withSelfRel();

            return ResponseEntity.ok(EntityModel.of(reviews.get(0),link));
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/like/{reviewId}")
    public ResponseEntity<?> like(@PathVariable("reviewId") UUID id){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Review review = this.reviewRepository.getByUserAndId(user, id);
            if(review == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            review.setLikes(review.getLikes()+1);

            return update(id, ReviewDTO.fromEntity(review));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dislike/{reviewId}")
    public ResponseEntity<?> dislike(@PathVariable("reviewId") UUID id){

        try {
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Review review = this.reviewRepository.getByUserAndId(user, id);
            if(review == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            review.setDislikes(review.getDislikes()+1);

            return update(id, ReviewDTO.fromEntity(review));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get review by id")
    @GetMapping("/{reviewId}")
    public ResponseEntity<EntityModel<Review>> getById(@PathVariable("reviewId") UUID reviewId){

        Link link = linkTo(ReviewController.class).slash(reviewId).withSelfRel();

        try{
            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Review review = this.reviewRepository.getByUserAndId(user, reviewId);
            if(review == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            review.add(link);
            return ResponseEntity.ok(EntityModel.of(review, link));
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create review")
    @PostMapping("/{postId}")
    public ResponseEntity<EntityModel<?>> save(@PathVariable("postId") UUID id, @RequestBody ReviewDTO reviewDTO){

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

            Link link = linkTo(ReviewController.class).slash(review.getId()).withSelfRel();
            return ResponseEntity.ok(EntityModel.of(review, link));
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update review by id")
    @PutMapping("/{reviewId}")
    public ResponseEntity<EntityModel<?>> update(@PathVariable("reviewId") UUID reviewId, @RequestBody ReviewDTO reviewDTO){
        try {
            Link link = linkTo(ReviewController.class).slash(reviewId).withSelfRel();

            // Authorize user
            User user = identity.getCurrent();
            if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

            Review currentReview = this.reviewRepository.getByUserAndId(user, reviewId);
            if (currentReview == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            currentReview.setPost(currentReview.getPost());
            currentReview.setUser(user);

            currentReview.setTitle(reviewDTO.getTitle());
            currentReview.setContents(reviewDTO.getContents());
            currentReview.setExposureDate(reviewDTO.getExposureDate());

            currentReview.setLikes(reviewDTO.getLikes());
            currentReview.setDislikes(reviewDTO.getDislikes());

            this.reviewRepository.save(currentReview);

            return ResponseEntity.ok(EntityModel.of(currentReview, link));
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete review by id")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteById(@PathVariable("reviewId") UUID reviewId){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            Review review = this.reviewRepository.getByUserAndId(user, reviewId);
            if(review == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            this.reviewRepository.deleteById(reviewId);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete all reviews")
    @DeleteMapping("/")
    public ResponseEntity<?> deleteAll(){
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try{
            List<Review> reviews = this.reviewRepository.getAllByUser(user);
            if(reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            for(Review review : reviews)
                this.reviewRepository.delete(review);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}