package org.example.service;

import org.example.entity.Post;
import org.example.model.PostDTO;
import org.example.model.PostListDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post createPost(PostDTO postDTO);

    Post updatePost(UUID id, PostDTO postDTO);

    ResponseEntity deletePost(UUID id);

    Post findPostById(UUID id);

    List<Post> findPostsByUserId(UUID id);

    List<Post> findMyPosts();
}
