package com.sisima.api.domain.guru;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import com.github.f4b6a3.ulid.UlidCreator;
import com.sisima.api.domain.akun.Akun;
import com.sisima.api.domain.akun.AkunRepository;
import com.sisima.api.domain.guru.model.GuruAddRequest;
import com.sisima.api.domain.guru.model.GuruAddResponse;
import com.sisima.api.domain.guru.model.GuruEditRequest;
import com.sisima.api.domain.guru.model.GuruGetDetailResponse;
import com.sisima.api.domain.guru.model.GuruGetDetailResponse_Dokumen;
import com.sisima.api.domain.guru.model.GuruGetDetailResponse_Umum;
import com.sisima.api.domain.guru.model.GuruGetPaginatedResponse;
import com.sisima.api.enums.AkunRoleEnum;

import jakarta.validation.Valid;
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

    public GuruGetDetailResponse getDetailGuru(
        String section,
        Guru guru
    ) {
            
        if (section.equals("dokumen")) {
            return new GuruGetDetailResponse_Dokumen(
                guru.getNik(), 
                guru.getFileKtp() != null ? guru.getFileKtp().getPublicId() : null,
                guru.getFileNpwp()  != null ? guru.getFileNpwp().getPublicId() : null
            );
        }

        return new GuruGetDetailResponse_Umum(
            guru.getPublicId(),
            guru.getNip(),
            guru.getAkun().getEmail(),
            guru.getNama(),
            guru.getGelarDepan(),
            guru.getGelarBelakang(),
            guru.getJenisKelamin(),
            guru.getTempatLahir(),
            guru.getTanggalLahir(),
            guru.getAlamat(),
            // guru.getMasuk(),
            guru.getFoto() != null ? guru.getFoto().getPublicId() : null
        );
    }

    @Transactional
    public GuruAddResponse addGuru(
        GuruAddRequest request
    ) {

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
        guru.setMasuk(LocalDate.now().getYear());
        guru.setJenisKelamin(true);
        guruRepository.save(guru);


        return new GuruAddResponse("success", guru.getPublicId());
    }

    public void editGuru(String publicId, GuruEditRequest request, Guru guru) {

        boolean isSame = 
            Objects.equals(guru.getNama(), request.getNama()) &&
            Objects.equals(guru.getGelarDepan(), request.getGelarDepan()) &&
            Objects.equals(guru.getGelarBelakang(), request.getGelarBelakang()) &&
            Objects.equals(guru.getJenisKelamin(), request.getJenisKelamin()) &&
            Objects.equals(guru.getTempatLahir(), request.getTempatLahir()) &&
            Objects.equals(guru.getTanggalLahir(), request.getTanggalLahir()) &&
            Objects.equals(guru.getAlamat(), request.getAlamat());
        if (isSame) {
            throw new RuntimeException("same-request");
        }

        guru.setNama(request.getNama());
        guru.setGelarDepan(request.getGelarDepan());
        guru.setGelarBelakang(request.getGelarBelakang());
        guru.setJenisKelamin(request.getJenisKelamin());
        guru.setTempatLahir(request.getTempatLahir());
        guru.setTanggalLahir(request.getTanggalLahir());
        guru.setAlamat(request.getAlamat());

        guruRepository.save(guru);
    }
    

}
