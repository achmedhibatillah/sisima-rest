package com.sisima.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sisima.api.entity.Akun;

@Repository
public interface AkunRepository extends JpaRepository<Akun, Long> {

    Optional<Akun> findDetailByPublicId(String publicId);
    
}
