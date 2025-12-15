package com.sisima.api.domain.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sisima.api.enums.FileAccess;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private static final String UPLOAD_DIR = "./uploads";

    public ByteArrayResource getFileContent(String publicId, String requesterId, String role) {
        Optional<File> fileOpt = fileRepository.findFileByPublicId(publicId);
        if (fileOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        File file = fileOpt.get();

        if (!hasAccess(file, requesterId, role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        Path filePath = Paths.get(UPLOAD_DIR, file.getPath());
        if (!Files.exists(filePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File content not found");
        }

        try {
            byte[] data = Files.readAllBytes(filePath);
            return new ByteArrayResource(data);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file");
        }
    }

    private boolean hasAccess(File file, String requesterId, String role) {
        if (hasAccess(file, FileAccess.PUBLIC)) return true;
        if (file.getOwnerId() != null && file.getOwnerId().equals(requesterId)) return true;

        switch (role) {
            case "ROLE_ROOT": return hasAccess(file, FileAccess.ROOT);
            case "ROLE_ADMIN": return hasAccess(file, FileAccess.ADMIN);
            case "ROLE_GURU": return hasAccess(file, FileAccess.GURU);
            case "ROLE_WALIMURID": return hasAccess(file, FileAccess.WALIMURID);
            default: return false;
        }
    }

    private boolean hasAccess(File file, int accessCode) {
        if (file.getAccess() == null) return false;
        return Arrays.stream(file.getAccess())
                     .anyMatch(a -> a != null && a == accessCode);
    }
}
