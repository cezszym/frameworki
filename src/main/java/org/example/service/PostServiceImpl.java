package org.example.service;

import org.example.entity.Post;
import org.example.entity.Review;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.security.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Identity identity;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, UserRepository userRepository1, Identity identity) {
        this.postRepository = postRepository;
        this.userRepository = userRepository1;
        this.identity = identity;
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
    public ResponseEntity<?> deletePost(UUID id) {
        try {
            postRepository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Post findPostById(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Override
    public List<Post> findPostsByUserId(UUID id) {
        return postRepository.findAllByUserId(id);
    }

    @Override
    public List<Post> findMyPosts() {
        return postRepository.findAllByUserId(this.identity.getCurrent().getId());
    }

}
