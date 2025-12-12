package com.sisima.api.domain.akun;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.sisima.api.domain.akun.model.AkunGetResponse;
import com.sisima.api.domain.akun.model.AkunPostRequest;
import com.sisima.api.enums.AkunRoleEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AkunService {

    private final AkunRepository akunRepository;

    public List<AkunGetResponse> getAllAkun() {
        List<Akun> akunList = akunRepository.findAll();

        return akunList.stream()
            .map(
                a -> new AkunGetResponse(
                    a.getPublicId(),
                    a.getEmail(),
                    a.getRole().name().toLowerCase(),
                    a.getCreatedAt()
                )
            ).collect(Collectors.toList());
    }

    public AkunGetResponse getDetailAkunByPublicId(String publicId) {
        Akun akun = akunRepository.findDetailByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("404"));
        
            return new AkunGetResponse(
                akun.getPublicId(),
                akun.getEmail(),
                akun.getRole().name().toLowerCase(),
                akun.getCreatedAt()
            );
    }

    public AkunGetResponse createAkun(AkunPostRequest request) {
        Akun akun = new Akun();

        String publicId = UlidCreator.getUlid().toString();
        akun.setPublicId(publicId);
        akun.setEmail(request.getEmail());
        akun.setPassword(request.getPassword());
        akun.setRole(parseRole(request.getRole()));

        Akun saved = akunRepository.save(akun);

        return new AkunGetResponse(
            saved.getPublicId(),
            saved.getEmail(),
            saved.getRole().name().toLowerCase(),
            saved.getCreatedAt()
        );
    }

    private AkunRoleEnum parseRole(String role) {
        return AkunRoleEnum.valueOf(role.toUpperCase());
    }
    
}
