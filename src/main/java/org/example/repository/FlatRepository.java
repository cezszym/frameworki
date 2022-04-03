package org.example.repository;

import org.example.entity.Flat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "flats", path = "flats")
public interface FlatRepository extends PagingAndSortingRepository<Flat, Long> {
}