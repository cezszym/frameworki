package org.example.controller;

import org.example.entity.Flat;
import org.example.entity.FlatDetail;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.model.FlatDTO;
import org.example.model.FlatDetailDTO;
import org.example.model.PostDTO;
import org.example.request.wrappers.FlatWrapper;
import org.example.service.BrowserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api/search")
@RestController
public class SearchController {
    private final BrowserService browserService;

    public SearchController(BrowserService browserService) {
        this.browserService = browserService;
    }

    @GetMapping("/flats")
    public ResponseEntity<List<FlatDTO>> searchFlats(String query){
        if (query == null || query.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            List<Flat> flats = browserService.searchFlat(query);
            return new ResponseEntity<>(flats.stream().map(FlatDTO::fromEntity).collect(Collectors.toList()), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> searchPosts(String query){
        if (query == null || query.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            List<Post> posts = browserService.searchPost(query);
            return new ResponseEntity<>(posts.stream().map(PostDTO::fromEntity).collect(Collectors.toList()), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
