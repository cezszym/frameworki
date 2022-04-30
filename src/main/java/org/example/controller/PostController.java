package org.example.controller;

import org.example.dto.PageDTO;
import org.example.dto.PostDTO;
import org.example.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public PageDTO<PostDTO> getAllPosts(@RequestParam(defaultValue = "0") int page) {
        return new PageDTO<>(postService.findAllPosts(page), PostDTO::fromEntity);
    }

    @GetMapping("/byUser/{userId}")
    public PageDTO<PostDTO> getAllPostsByUser(@PathVariable UUID userId, int page) {
        return new PageDTO<>(postService.findAllPostsByUserId(userId, page), PostDTO::fromEntity);
    }

    @GetMapping("/byTitle")
    public PageDTO<PostDTO> getAllPostsBTitle(String title, int page) {
        return new PageDTO<>(postService.findAllPostByTitleLike(title, page), PostDTO::fromEntity);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
    }

    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        return PostDTO.fromEntity(postService.createPost(postDTO.toEntity()));
    }

    @PutMapping("/{id}")
    public PostDTO updatePost(@PathVariable UUID id, @RequestBody PostDTO postDTO) {
        return PostDTO.fromEntity(postService.updatePost(id, postDTO.toEntity()));
    }

}
