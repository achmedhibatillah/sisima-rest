package com.sisima.api.domain.akun;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sisima.api.enums.AkunRoleEnum;

@Repository
public interface AkunRepository extends JpaRepository<Akun, Long> {

    Optional<Akun> findDetailByPublicId(String publicId);

    Optional<Akun> findDetailByEmail(String email);

    List<Akun> findAllByRole(AkunRoleEnum role, Pageable pageable);
    
}
