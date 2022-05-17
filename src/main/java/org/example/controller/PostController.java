package org.example.controller;

import org.example.entity.Post;
import org.example.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/myPosts")
    public ResponseEntity<List<Post>> getMyPosts() {
        return ResponseEntity.ok(postService.findMyPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findPostById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findPostsByUserId(id));
    }

    @PostMapping("/")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable UUID id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.updatePost(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable UUID id) {
        return postService.deletePost(id);
    }

//    @GetMapping
//    public ResponseEntity<List<Post>> getAllPosts() {
//
//    }
//    @GetMapping
//    public PageDTO<PostDTO> getAllPosts(@RequestParam(defaultValue = "0") int page) {
//        return new PageDTO<>(postService.findAllPosts(page), PostDTO::fromEntity);
//    }
//
//    @GetMapping("/byUser/{userId}")
//    public PageDTO<PostDTO> getAllPostsByUser(@PathVariable UUID userId, int page) {
//        return new PageDTO<>(postService.findAllPostsByUserId(userId, page), PostDTO::fromEntity);
//    }
//
//    @GetMapping("/byTitle")
//    public PageDTO<PostDTO> getAllPostsBTitle(String title, int page) {
//        return new PageDTO<>(postService.findAllPostByTitleLike(title, page), PostDTO::fromEntity);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deletePost(@PathVariable UUID id) {
//        postService.deletePost(id);
//    }
//
//    @PostMapping
//    public PostDTO createPost(@RequestBody PostDTO postDTO) {
//        return PostDTO.fromEntity(postService.createPost(postDTO.toEntity()));
//    }
//
//    @PutMapping("/{id}")
//    public PostDTO updatePost(@PathVariable UUID id, @RequestBody PostDTO postDTO) {
//        return PostDTO.fromEntity(postService.updatePost(id, postDTO.toEntity()));
//    }

}
