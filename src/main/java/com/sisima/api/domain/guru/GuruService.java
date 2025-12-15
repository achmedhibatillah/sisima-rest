package com.sisima.api.domain.guru;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.github.f4b6a3.ulid.UlidCreator;
import com.sisima.api.domain.akun.Akun;
import com.sisima.api.domain.akun.AkunRepository;
import com.sisima.api.domain.guru.model.GuruAddRequest;
import com.sisima.api.domain.guru.model.GuruAddResponse;
import com.sisima.api.domain.guru.model.GuruGetPaginatedResponse;
import com.sisima.api.enums.AkunRoleEnum;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuruService {

    private GuruRepository guruRepository;
    private AkunRepository akunRepository;
    private PasswordEncoder passwordEncoder;

    public Page<GuruGetPaginatedResponse> getAllGuruPaginated(
        int page,
        int size
    ) {
        Pageable pageable = PageRequest.of(
            page, 
            size,
            Sort.by(Sort.Direction.ASC, "nama")
        );

        return guruRepository.findAll(pageable).map(this::toResponse);
    }
    
    private GuruGetPaginatedResponse toResponse(Guru guru) {
        return new GuruGetPaginatedResponse(
            guru.getPublicId(),
            guru.getNik(),
            guru.getNip(),
            guru.getNama(),
            guru.getGelarDepan(),
            guru.getGelarBelakang(),
            guru.getJenisKelamin(),
            guru.getFoto() != null ? guru.getFoto().getPublicId() : null,
            guru.getAkun().getPublicId()
        );
    }

    @Transactional
    public GuruAddResponse addGuru(GuruAddRequest request) {

        if (akunRepository.findDetailByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already registered"
            );
        }

        Akun akun = new Akun();
        String akunPublicId = UlidCreator.getUlid().toString();
        akun.setPublicId(akunPublicId);
        akun.setEmail(request.getEmail());
        akun.setPassword(passwordEncoder.encode(request.getPassword()));
        akun.setRole(AkunRoleEnum.GURU); 
        akunRepository.save(akun);

        Guru guru = new Guru();
        String guruPublicId = UlidCreator.getUlid().toString();
        guru.setPublicId(guruPublicId);
        guru.setAkun(akun);
        guruRepository.save(guru);


        return new GuruAddResponse("success", guru.getPublicId());
    }

    

}
