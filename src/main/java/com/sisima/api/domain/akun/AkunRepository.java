package com.sisima.api.domain.akun;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AkunRepository extends JpaRepository<Akun, Long> {

    Optional<Akun> findDetailByPublicId(String publicId);
    
}
