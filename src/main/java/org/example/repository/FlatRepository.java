package org.example.repository;

import org.example.entity.Flat;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlatRepository extends JpaRepository<Flat, UUID> {
    List<Flat> getAllByUser(User user);
    Flat getByUserAndId(User user, UUID id);
    Flat findByUserAndId(User user, UUID id);
}