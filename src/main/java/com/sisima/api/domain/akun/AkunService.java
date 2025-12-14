package com.sisima.api.domain.akun;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.sisima.api.domain.akun.model.AkunAddResponse;
import com.sisima.api.domain.akun.model.AkunGetResponse; 
import com.sisima.api.domain.akun.model.AkunAddRequest;
import com.sisima.api.enums.AkunRoleEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AkunService {

    private final AkunRepository akunRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AkunGetResponse> getAllAkun() {
        List<Akun> akunList = akunRepository.findAll();

        return akunList.stream()
            .map(
                a -> new AkunGetResponse(
                    a.getPublicId(),
                    a.getEmail(),
                    a.getRole().name().toLowerCase(),
                    a.getCreatedAt(),
                    a.getUpdatedAt()
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
                akun.getCreatedAt(),
                akun.getUpdatedAt()
            );
    }

    public AkunAddResponse createAkun(AkunAddRequest request) {
        Akun akun = new Akun();

        String publicId = UlidCreator.getUlid().toString();
        akun.setPublicId(publicId);
        akun.setEmail(request.getEmail());
        akun.setPassword(passwordEncoder.encode(request.getPassword()));
        akun.setRole(parseRole(request.getRole()));

        akunRepository.save(akun);

        return new AkunAddResponse(
            "success"
        );
    }

    public boolean updatePassword(String publicId, String password) {
        Optional<Akun> akunOpt = akunRepository.findDetailByPublicId(publicId);
        if (akunOpt.isPresent()) {
            Akun akun = akunOpt.get();
            akun.setPassword(passwordEncoder.encode(password));
            akunRepository.save(akun);
            return true;
        }
        return false;
    }

    public boolean deleteAkunByPublicId(String publicId) {
        Optional<Akun> akunOpt = akunRepository.findDetailByPublicId(publicId);
        if (akunOpt.isPresent()) {
            akunRepository.delete(akunOpt.get());
            return true;
        }
        return false;
    }

    private AkunRoleEnum parseRole(String role) {
        return AkunRoleEnum.valueOf(role.toUpperCase());
    }
    
}
