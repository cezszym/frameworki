package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Review;

import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ReviewDTO {

    private UUID id;
    private PostDTO post;
    private UserDTO user;
    private String title;
    private String contents;
    private Date exposureDate;
    private Integer likes;
    private Integer dislikes;

    public static ReviewDTO fromEntity(Review review) {
        return new ReviewDTO(review.getId(), PostDTO.fromEntity(review.getPost()), UserDTO.fromEntity(review.getUser()), review.getTitle(), review.getContents(), review.getExposureDate(), review.getLikes(), review.getDislikes());
    }

}
