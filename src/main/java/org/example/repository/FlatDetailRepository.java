package org.example.repository;

import org.example.entity.FlatDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlatDetailRepository extends JpaRepository<FlatDetail, UUID> {
    void deleteById(UUID reservationId);
}
