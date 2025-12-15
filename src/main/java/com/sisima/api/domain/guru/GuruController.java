package com.sisima.api.domain.guru;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.guru.model.GuruAddRequest;
import com.sisima.api.domain.guru.model.GuruAddResponse;
import com.sisima.api.domain.guru.model.GuruGetPaginatedResponse;
import com.sisima.api.security.AccessControlService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/guru")
@AllArgsConstructor
public class GuruController {

    private final AccessControlService accessControlService;
    private final GuruService guruService;

    // used - root, admin
    @GetMapping
    public ResponseEntity<?> getAllGuruPaginated(
        Authentication auth,
        @RequestParam int page,
        @RequestParam int size
    ) {
        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"});
        if (!ra) {
            return ResponseEntity.status(403).body(null);
        }

        Page<GuruGetPaginatedResponse> guru = guruService.getAllGuruPaginated(page, size);

        return ResponseEntity.ok(guru);
    }

    // used - root, admin
    @PostMapping
    public ResponseEntity<?> addGuru(
        Authentication auth,
        GuruAddRequest request
    ) {
        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT", "ADMIN"});
        if (!ra) {
            return ResponseEntity.status(403).body(null);
        }

        GuruAddResponse response = guruService.addGuru(request);
        return ResponseEntity.status(201).body(response);
    }
    
}
