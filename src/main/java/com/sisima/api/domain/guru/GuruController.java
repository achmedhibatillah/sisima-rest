package com.sisima.api.domain.guru;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.akun.AkunRepository;
import com.sisima.api.domain.auth.model.AuthErrorResponse;
import com.sisima.api.domain.guru.model.GuruAddRequest;
import com.sisima.api.domain.guru.model.GuruAddResponse;
import com.sisima.api.domain.guru.model.GuruEditRequest;
import com.sisima.api.domain.guru.model.GuruGetDetailResponse;
import com.sisima.api.domain.guru.model.GuruGetPaginatedResponse;
import com.sisima.api.security.AccessControlService;
import com.sisima.api.security.AuthDecision;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/guru")
@AllArgsConstructor
public class GuruController {

    private final AccessControlService accessControlService;
    private final GuruService guruService;
    private final GuruRepository guruRepository;
    private final AkunRepository akunRepository;

    // used - root, admin
    @GetMapping
    public ResponseEntity<?> getAllGuruPaginated(
        Authentication auth,
        @RequestParam int page,
        @RequestParam int size
    ) {
        // boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"});
        // if (!ra) {
        //     return ResponseEntity.status(401).build();
        // }

        AuthDecision authDecision =
            accessControlService.rolesCanAccess_v2(
                SecurityContextHolder.getContext().getAuthentication(),
                new String[]{"ROOT", "ADMIN"}
            );

        if (!authDecision.allowed()) {
            return ResponseEntity
                .status(401)
                .body(AuthErrorResponse.of(authDecision.reason()));
        }

        Page<GuruGetPaginatedResponse> guru = guruService.getAllGuruPaginated(page, size);

        return ResponseEntity.ok(guru);
    }

    // used - root, admin, guru, walimurid
    @GetMapping("/{publicId}")
    public ResponseEntity<?> getDetailGuru(
        Authentication auth,
        @PathVariable String publicId,
        @RequestParam(name = "section", required = true) String section
    ) {
        Guru guru = guruRepository.findDetailByPublicId(publicId).orElse(null);
        if (guru == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Data tidak tersedia."));
        }

        // boolean canAccess =
        //     accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"})
        //     || accessControlService.ownerCanAccess(auth, guru.getAkun().getPublicId());
        // if (!canAccess) {
        //     return ResponseEntity.status(401).build();
        // }

        AuthDecision authDecision =
            accessControlService.roleOrOwnerCanAccess_v2(
                SecurityContextHolder.getContext().getAuthentication(),
                new String[]{"ROOT", "ADMIN"},
                guru.getAkun().getPublicId()
            );

        if (!authDecision.allowed()) {
            return ResponseEntity
                .status(401)
                .body(AuthErrorResponse.of(authDecision.reason()));
        }

        GuruGetDetailResponse response = guruService.getDetailGuru(section, guru);

        return ResponseEntity.status(200).body(response);
    }


    // used - root, admin
    @PostMapping
    public ResponseEntity<?> addGuru(
        Authentication auth,
        @RequestBody @Valid GuruAddRequest request
    ) {
        if (akunRepository.findDetailByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "Akun telah terdaftar."));
        }

        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"});
        if (!ra) {
            return ResponseEntity.status(401).build();
        }

        GuruAddResponse response = guruService.addGuru(request);
        return ResponseEntity.status(201).body(response);
    }

    // used - root, admin, owner
    @PatchMapping("/{publicId}")
    public ResponseEntity<?> editGuru(
        Authentication auth,
        @PathVariable String publicId,
        @RequestBody @Valid GuruEditRequest request
    ) {
        Guru guru = guruRepository.findDetailByPublicId(publicId).orElse(null);
        if (guru == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Data tidak tersedia."));
        }
            
        boolean canAccess = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"}) 
            || accessControlService.ownerCanAccess(auth, guru.getAkun().getPublicId());
        if (!canAccess) {
            return ResponseEntity.status(401).build();
        }

        try {
            guruService.editGuru(publicId, request, guru);
            return ResponseEntity.status(204).build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("same-request")) {
                return ResponseEntity.status(400).body(Map.of("message", "Data harus diubah."));
            }
            return ResponseEntity.status(500).build();
        }
    }
    
}
