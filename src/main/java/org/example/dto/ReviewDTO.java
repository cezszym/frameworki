package org.example.dto;

import lombok.Data;
import org.example.entity.Review;

import java.util.Date;
import java.util.UUID;

@Data
public class ReviewDTO {

    private UUID id;
    private UUID post;
    private UUID user;

    private String contents;
    private Date exposureDate;
    private Integer likes;
    private Integer dislikes;

    public static ReviewDTO fromEntity(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setPost(review.getPost());
        dto.setUser(review.getUser());
        dto.setContents(review.getContents());
        dto.setExposureDate(review.getExposureDate());
        dto.setLikes(review.getLikes());
        dto.setDislikes(review.getDislikes());
        return dto;
    }

    public Review toEntity() {
        Review review = new Review();
        review.setId(this.getId());
        review.setPost(this.getPost());
        review.setUser(this.getUser());
        review.setContents(this.getContents());
        review.setExposureDate(this.getExposureDate());
        review.setLikes(this.getLikes());
        review.setDislikes(this.getDislikes());
        return review;
    }
}
