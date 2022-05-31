package org.example.repository;

import org.example.entity.Picture;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "pictures", path = "pictures")
public interface PictureRepository extends PagingAndSortingRepository<Picture, UUID> {

}