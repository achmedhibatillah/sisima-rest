package com.sisima.api.domain.guru;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuruRepository extends JpaRepository<Guru, Long> {
    Page<Guru> findAllPaginatedGuruByPublicId(
            String publicId,
            Pageable pageable
    );
}
