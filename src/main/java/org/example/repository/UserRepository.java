package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);
    Optional<User> findByNickOrEmail(String username, String email);
    Optional<User> findByNick(String nick);
    Boolean existsByNick(String username);
    Boolean existsByEmail(String email);
}