package com.sisima.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sisima.api.entity.Akun;
import com.sisima.api.model.AkunResponse;
import com.sisima.api.repository.AkunRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AkunService {

    private final AkunRepository akunRepository;

    public List<AkunResponse> getAllAkun() {
        List<Akun> akunList = akunRepository.findAll();

        return akunList.stream()
            .map(
                a -> new AkunResponse(
                    a.getPublicId(),
                    a.getEmail(),
                    a.getRole(),
                    a.getCreatedAt()
                )
            ).collect(Collectors.toList());
    }

    public AkunResponse getDetailAkunByPublicId(String publicId) {
        Akun akun = akunRepository.findDetailByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("404"));
        
            return new AkunResponse(
                akun.getPublicId(),
                akun.getEmail(),
                akun.getRole(),
                akun.getCreatedAt()
            );
    }
    
}
