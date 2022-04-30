package org.example.service;

import org.example.entity.Post;
import org.example.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(UUID id, Post post) {
        if (post.getId() == null) {
            throw new IllegalArgumentException("Post id cannot be null");
        }
        return postRepository.save(post);
    }

    @Override
    public void deletePost(UUID id) {
        postRepository.delete(findPostById(id));
    }

    @Override
    public Post findPostById(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Override
    public Page<Post> findAllPostByTitleLike(String title, int page) {
        return postRepository.findAllByTitleContainingIgnoreCase(Pageable.ofSize(10).withPage(Math.max(page, 0)), title);
    }

    @Override
    public Page<Post> findAllPosts(int page) {
        return postRepository.findAll(Pageable.ofSize(10).withPage(Math.max(page, 0)));
    }

    @Override
    public Page<Post> findAllPostsByUserId(UUID id, int page) {
        return postRepository.findAllByUserId(Pageable.ofSize(10).withPage(Math.max(page, 0)), id);
    }
}
