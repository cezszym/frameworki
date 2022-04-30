package org.example.repository;

import org.example.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "posts", path = "posts")
public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findAllByTitleContainingIgnoreCase(Pageable pageable, String title);

    Page<Post> findAllByUserId(Pageable pageable, UUID userId);
}
