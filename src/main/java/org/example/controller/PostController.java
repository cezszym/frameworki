package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.entity.Flat;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.model.PostDTO;
import org.example.repository.FlatRepository;
import org.example.repository.PostRepository;
import org.example.security.Identity;
import org.example.service.BrowserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/api/post")
public class PostController {

    private final PostRepository postRepository;
    private final FlatRepository flatRepository;
    private final BrowserService browserService;
    private final Identity identity;

    public PostController(PostRepository postRepository, FlatRepository flatRepository, BrowserService browserService, Identity identity) {
        this.postRepository = postRepository;
        this.flatRepository = flatRepository;
        this.browserService = browserService;
        this.identity = identity;
    }

    @Operation(summary = "Get all posts")
    @GetMapping("/")
    public ResponseEntity<CollectionModel<Post>> getAll() {

        Link link = linkTo(PostController.class).withSelfRel();

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try {
            List<Post> posts = new ArrayList<>(this.postRepository.findAll());
            if(posts.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            posts.forEach(post -> post.add(linkTo(PostController.class).slash(post.getId()).withSelfRel()));
            return ResponseEntity.ok(CollectionModel.of(posts,link));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get post by id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Post>> getById(
            @Parameter(
                    description = "unique id of post",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("id") UUID id) {

        Link link = linkTo(PostController.class).slash(id).withSelfRel();

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try {
            Post post = this.postRepository.getByUserAndId(user, id);
            if(post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            return ResponseEntity.ok(EntityModel.of(post,link));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get posts for authenticated user")
    @GetMapping("/user/{id}")
    public ResponseEntity<CollectionModel<Post>> getUserPosts(
            @PathVariable("id") UUID id) {

        Link link = linkTo(PostController.class).slash("user").slash(id).withSelfRel();

        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        try {
            List<Post> posts = new ArrayList<>(this.postRepository.getAllByUser(user));
            posts.forEach(post -> post.add(linkTo(PostController.class).slash(post.getId()).withSelfRel()));

            if(posts.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            return ResponseEntity.ok(CollectionModel.of(posts,link));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create post by flat id")
    @PostMapping("/{flatId}")
    public ResponseEntity<EntityModel<Post>> save(
            @Parameter(
                    description = "unique id of flat",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("flatId") UUID id, @RequestBody PostDTO postDTO) {
        // Authorize user
        User user = this.identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Flat flat = this.flatRepository.getByUserAndId(user, id);
        if(flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            Post post = postRepository.save(Post.builder().flat(flat)
                    .user(user)
                    .title(postDTO.getTitle())
                    .description(postDTO.getDescription())
                    .price(postDTO.getPrice())
                    .promoted(postDTO.isPromoted())
                    .created(postDTO.getCreated())
                    .expired(postDTO.getExpired())
                    .build());

            // index post
            browserService.createPost(post);

            Link link = linkTo(PostRepository.class).slash(post.getId()).withSelfRel();
            return ResponseEntity.ok(EntityModel.of(post, link));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update post by id")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<?>> updatePost(
            @Parameter(
                    description = "unique id of post",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("id") UUID id, @RequestBody PostDTO postDTO) {
        Link link = linkTo(ReservationController.class).slash(id).withSelfRel();

        // Authorize user
        User user = this.identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Flat flat = this.postRepository.getByUserAndId(user, id).getFlat();
        if(flat == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Post post = this.postRepository.getByUserAndId(user, id);
        if (post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            post.setFlat(flat);
            post.setUser(user);
            post.setTitle(postDTO.getTitle());
            post.setDescription(postDTO.getDescription());
            post.setPrice(postDTO.getPrice());
            post.setPromoted(postDTO.isPromoted());
            post.setCreated(postDTO.getCreated());
            post.setExpired(postDTO.getExpired());

            this.postRepository.save(post);

            // update post index
            browserService.updatePost(post);

            return ResponseEntity.ok(EntityModel.of(post, link));
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete post by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(
                    description = "unique id of post",
                    example = "b8d02d81-6329-ef96-8a4d-55b376d8b25a")
            @PathVariable("id") UUID id) {
        // Authorize user
        User user = identity.getCurrent();
        if (user == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // Check if user has permissions to delete this flat
        Post post = this.postRepository.getByUserAndId(user, id);
        if (post == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        try{
            this.postRepository.delete(post);

            // delete post index
            browserService.deletePost(post);

            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
