package org.example.service;

import org.example.entity.Flat;
import org.example.entity.Post;

import java.io.IOException;
import java.util.List;

public interface BrowserService {
    void createIndex();

    List<Flat> searchFlat(String query);

    void createFlat(Flat flat) throws IOException;

    void updateFlat(Flat flat) throws IOException;

    void deleteFlat(Flat flat) throws IOException;

    List<Post> searchPost(String query);

    void createPost(Post post) throws IOException;

    void updatePost(Post post) throws IOException;

    void deletePost(Post post) throws IOException;
}
