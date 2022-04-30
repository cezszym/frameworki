package org.example.service;

import org.example.entity.Post;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PostService {

    Post createPost(Post post);

    Post updatePost(UUID id, Post post);

    void deletePost(UUID id);

    Post findPostById(UUID id);

    Page<Post> findAllPostsByUserId(UUID id, int page);

    Page<Post> findAllPostByTitleLike(String title, int page);

    Page<Post> findAllPosts(int page);
}
