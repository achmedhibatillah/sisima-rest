package com.sisima.api.domain.storage;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

    Optional<Storage> findFileByPublicId(String publicId);
    
}
