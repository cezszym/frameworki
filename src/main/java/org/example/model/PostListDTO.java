package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.entity.Post;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PostListDTO {

    private List<PostDTO> posts;

    public static PostListDTO fromEntityList(List<Post> posts) {
        return new PostListDTO(posts.stream().map(PostDTO::fromEntity).collect(java.util.stream.Collectors.toList()));
    }
}
