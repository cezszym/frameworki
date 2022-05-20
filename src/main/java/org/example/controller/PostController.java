package org.example.controller;

import org.example.entity.Post;
import org.example.model.PostDTO;
import org.example.model.PostListDTO;
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
    public PostDTO getPost(@PathVariable UUID id) {
        return PostDTO.fromEntity(postService.findPostById(id));
    }

    @GetMapping("/user/{id}")
    public PostListDTO getUserPosts(@PathVariable UUID id) {
        return PostListDTO.fromEntityList(postService.findPostsByUserId(id));
    }

    @PostMapping("/")
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        return PostDTO.fromEntity(postService.createPost(postDTO));
    }

    @PutMapping("/{id}")
    public PostDTO updatePost(@PathVariable UUID id, @RequestBody PostDTO postDTO) {
        return PostDTO.fromEntity(postService.updatePost(id, postDTO));
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
