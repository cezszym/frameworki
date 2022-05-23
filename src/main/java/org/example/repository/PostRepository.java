package org.example.repository;

import org.example.entity.Post;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> getAll();
    List<Post> getAllByUser(User user);
    Post getByUserAndId(User user, UUID id);
    void deleteById(UUID id);
}
