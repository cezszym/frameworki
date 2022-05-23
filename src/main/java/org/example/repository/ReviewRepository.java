package org.example.repository;

import org.example.entity.Post;
import org.example.entity.Review;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    void deleteById(UUID reservationId);
    void deleteAllReviewsByUser(User user);
    List<Review> getAllReviewsByUser(User user);
    Review getReviewByUserAndId(User user, UUID reviewId);
    List<Review> findAllReviewsByPostOrderByLikesDesc(UUID postId);
    List<Review> findAllReviewsByPostOrderByDislikesDesc(UUID postId);
}
