package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Post;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PostDTO {

    private UUID id;
    private FlatDTO flat;
    private UserDTO user;
    private String title;
    private String description;
    private Integer price;
    private boolean promoted;
    private long created;
    private long expired;

    public static PostDTO fromEntity(Post post) {
        return new PostDTO(post.getId(), FlatDTO.fromEntity(post.getFlat()), UserDTO.fromEntity(post.getUser()), post.getTitle(), post.getDescription(), post.getPrice(), post.isPromoted(), post.getCreated(), post.getExpired());
    }

}
