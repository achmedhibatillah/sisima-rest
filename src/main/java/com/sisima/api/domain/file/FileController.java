package com.sisima.api.domain.file;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{publicId}")
    public ResponseEntity<ByteArrayResource> getFile(Authentication auth, @PathVariable String publicId) {
        String requesterId = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        ByteArrayResource resource = fileService.getFileContent(publicId, requesterId, role);

        return 
            ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + publicId + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
