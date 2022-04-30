package org.example.dto;

import lombok.Data;
import org.example.entity.Post;

import java.util.UUID;

@Data
public class PostDTO {

    private UUID id;
    private String title;
    private String description;
    private int price;
    private long created;
    private long expired;
    private boolean promoted;

    public static PostDTO fromEntity(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setPrice(post.getPrice());
        dto.setCreated(post.getCreated());
        dto.setExpired(post.getExpired());
        dto.setPromoted(post.isPromoted());
        return dto;
    }

    public Post toEntity() {
        Post post = new Post();
        post.setId(this.getId());
        post.setTitle(this.getTitle());
        post.setDescription(this.getDescription());
        post.setPrice(this.getPrice());
        post.setCreated(this.getCreated());
        post.setExpired(this.getExpired());
        post.setPromoted(this.isPromoted());
        return post;
    }
}
