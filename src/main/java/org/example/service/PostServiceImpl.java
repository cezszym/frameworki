//package org.example.service;
//
//import org.example.entity.Flat;
//import org.example.entity.Post;
//import org.example.entity.User;
//import org.example.model.PostDTO;
//import org.example.model.PostListDTO;
//import org.example.repository.FlatRepository;
//import org.example.repository.PostRepository;
//import org.example.repository.UserRepository;
//import org.example.security.Identity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class PostServiceImpl implements PostService {
//
//
//    @Override
//    public Post createPost(PostDTO postDTO) {
//
//    }
//
//    @Override
//    public Post updatePost(UUID id, PostDTO postDTO) {
//        Post post = postRepository.findById(id).orElse(null);
//        if (post == null) {
//            return null;
//        }
//        if (postDTO.getFlat() != null) {
//            post.setFlat(flatRepository.findById(postDTO.getFlat().getId()).orElse(null));
//        }
//        if (postDTO.getTitle() != null) {
//            post.setTitle(postDTO.getTitle());
//        }
//        if (postDTO.getDescription() != null) {
//            post.setDescription(postDTO.getDescription());
//        }
//        if (postDTO.getPrice() != null) {
//            post.setPrice(postDTO.getPrice());
//        }
//        return postRepository.save(post);
//    }
//
//    @Override
//    public ResponseEntity<?> deletePost(UUID id) {
//        try {
//            postRepository.deleteById(id);
//            return new ResponseEntity<>(null, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Override
//    public Post findPostById(UUID id) {
//        if (postRepository.findById(id).isEmpty()) {
//            return null;
//        }
//        return postRepository.findById(id).get();
//    }
//
//    @Override
//    public List<Post> findPostsByUserId(UUID id) {
//        return postRepository.findAllByUserId(id);
//    }
//
//    @Override
//    public List<Post> findMyPosts() {
//        return postRepository.findAllByUserId(this.identity.getCurrent().getId());
//    }
//
//}
