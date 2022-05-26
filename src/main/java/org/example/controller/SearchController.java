package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
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

    @Operation(summary = "Search for flats by specific query")
    @GetMapping("/flats")
    public ResponseEntity<List<FlatDTO>> searchFlats(
            @Parameter(
                    description = "query for flats",
                    example = "city:lublin")
            String query){
        if (query == null || query.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            List<Flat> flats = browserService.searchFlat(query);
            return new ResponseEntity<>(flats.stream().map(FlatDTO::fromEntity).collect(Collectors.toList()), HttpStatus.OK);
        } catch(QueryNodeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Search for posts by specific query")
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> searchPosts(
            @Parameter(
                    description = "query for posts",
                    example = "price:[100 TO 300]")
            String query){
        if (query == null || query.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            List<Post> posts = browserService.searchPost(query);
            return new ResponseEntity<>(posts.stream().map(PostDTO::fromEntity).collect(Collectors.toList()), HttpStatus.OK);
        } catch(QueryNodeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
