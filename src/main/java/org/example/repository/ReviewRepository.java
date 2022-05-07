package org.example.repository;

import org.example.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Review findReviewById(UUID id);

    List<Review> getAllReviewsByPost(UUID postId);

    List<Review> getAllReviewsByUser(UUID userId);

    List<Review> getAllReviewsByTitle(String title);

    List<Review> findAllReviewsByPostOrderByLikesDesc(UUID postId);

    List<Review> findAllReviewsByPostOrderByDislikesDesc(UUID postId);
}
