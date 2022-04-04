package org.example.repository;

import org.example.entity.Flat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "flats", path = "flats")
public interface FlatRepository extends PagingAndSortingRepository<Flat, UUID> {
}