package org.example.service;

import org.example.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post createPost(Post post);

    Post updatePost(UUID id, Post post);

    ResponseEntity deletePost(UUID id);

    Post findPostById(UUID id);

    List<Post> findPostsByUserId(UUID id);

    List<Post> findMyPosts();
}
