package com.sisima.api.domain.storage;

import java.io.IOError;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.sisima.api.domain.storage.model.StorageAddRequest;
import com.sisima.api.domain.storage.model.StorageAddResponse;
import com.sisima.api.security.AccessControlService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/storage")
@AllArgsConstructor
public class StorageController {

    private final AccessControlService accessControlService;
    private final StorageService storageService;

    @GetMapping("/public/{publicId}")
    public ResponseEntity<ByteArrayResource> getPublicFile(@PathVariable String publicId) {
        return ResponseEntity.ok(storageService.getFileContent(publicId, null, null));
    }

    @GetMapping("/private/{publicId}")
    public ResponseEntity<ByteArrayResource> getFile(Authentication auth, @PathVariable String publicId) {
        String requesterId = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        ByteArrayResource resource = storageService.getFileContent(publicId, requesterId, role);

        return 
            ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + publicId + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            Authentication auth,
            @ModelAttribute StorageAddRequest request
    ) {
        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT"});
        if (!ra) {
            return ResponseEntity.status(500).body(null);
        }

        String ownerId = auth.getName();

        try {
            StorageAddResponse saved = storageService.saveFile(request.getFile(), ownerId, request.getAccess());
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to save file", e
            );
        }
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<?> deleteFile(Authentication auth, @PathVariable String publicId) {
        boolean ra = accessControlService.rolesCanAccess(auth, new String[]{"ROOT"});
        if (!ra) {
            return ResponseEntity.status(500).body(null);
        }

        String requesterId = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        try {
            storageService.deleteFile(publicId, requesterId, role);
            return ResponseEntity.status(204).body(null);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

}
