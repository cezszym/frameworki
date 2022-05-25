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
    List<Review> getAllByUser(User user);
    Review getByUserAndId(User user, UUID reviewId);
    List<Review> findAllReviewsByPostOrderByLikesDesc(Post post);
    List<Review> findAllReviewsByPostOrderByDislikesDesc(Post post);
}
