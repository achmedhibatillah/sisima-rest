package com.sisima.api.domain.akun;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.akun.model.AkunAddResponse;
import com.sisima.api.domain.akun.model.AkunGetResponse;
import com.sisima.api.domain.akun.model.AkunUpdatePasswordRequest;
import com.sisima.api.security.AccessControlService;
import com.sisima.api.domain.akun.model.AkunAddRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@RestController
@RequestMapping("/akun")
public class AkunController {

    private final AkunService akunService;
    private final AccessControlService accessControlService;

    @GetMapping
    public ResponseEntity<?> getAllAkun(Authentication auth) {
        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "GURU"});
        if (!ra) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<AkunGetResponse> response = akunService.getAllAkun();
        if (response.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<?> getDetailAkun(@PathVariable String publicId, Authentication auth) {
        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT"});
        boolean oa = accessControlService.ownerCanAccess(auth, publicId);
        if (!ra && !oa) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            AkunGetResponse response = akunService.getDetailAkunByPublicId(publicId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("404")) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addAkun(@RequestBody @Valid AkunAddRequest request) {
        AkunAddResponse response = akunService.createAkun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // used - root, admin, owner
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(
        Authentication auth,
        @RequestBody @Valid AkunUpdatePasswordRequest request
    ) {
        boolean canAccess = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"}) 
            || accessControlService.ownerCanAccess(auth, request.getPublicId());
        if (!canAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            return ResponseEntity.status(400).body("Password lama dan baru tidak boleh sama.");
        }

        try {
            akunService.updatePassword(request);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            if (e.getMessage().equals("404")) {
                return ResponseEntity.status(404).body("Akun tidak ditemukan.");
            } else if (e.getMessage().equals("400")) {
                return ResponseEntity.status(400).body("Password lama salah.");
            }
            return ResponseEntity.status(500).build();
        }
    }
    
    @DeleteMapping("/{publicId}")
    public ResponseEntity<?> deleteAkun(@PathVariable String publicId) {

        boolean status = akunService.deleteAkunByPublicId(publicId);
        try {
            if (status) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    
}
