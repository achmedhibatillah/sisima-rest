package com.sisima.api.domain.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.github.f4b6a3.ulid.UlidCreator;
import com.sisima.api.domain.storage.model.StorageAddResponse;
import com.sisima.api.enums.FileAccess;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StorageService {

    private final StorageRepository fileRepository;
    private static final String UPLOAD_DIR = "./uploads";

    public ByteArrayResource getFileContent(String publicId, String requesterId, String role) {
        Optional<Storage> fileOpt = fileRepository.findFileByPublicId(publicId);
        if (fileOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        Storage file = fileOpt.get();

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

    public StorageAddResponse saveFile(MultipartFile multipartFile, String ownerId, Integer[] access) throws IOException {

        if (!Files.exists(Paths.get(UPLOAD_DIR))) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileNameWithoutExtension = UlidCreator.getUlid().toString();
        String fileName = fileNameWithoutExtension + extension;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        Files.write(filePath, multipartFile.getBytes());

        Storage storage = new Storage();

        String publicId = UlidCreator.getUlid().toString();
        storage.setPublicId(publicId);
        storage.setPath(fileName);
        storage.setOwnerId(ownerId);
        storage.setAccess(access);

        fileRepository.save(storage);

        return new StorageAddResponse(
            storage.getPublicId(),
            storage.getAccess()
        );
    }

    public void deleteFile(String publicId, String requesterId, String role) {
        Storage file = fileRepository.findFileByPublicId(publicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        Path filePath = Paths.get(UPLOAD_DIR, file.getPath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file");
        }

        fileRepository.delete(file);
    }

    private boolean hasAccess(Storage file, String requesterId, String role) {
        if (hasAccess(file, FileAccess.PUBLIC)) return true;

        if (requesterId != null && file.getOwnerId() != null && file.getOwnerId().equals(requesterId))
            return true;

        if (role != null) {
            switch (role) {
                case "ROLE_ROOT": return hasAccess(file, FileAccess.ROOT);
                case "ROLE_ADMIN": return hasAccess(file, FileAccess.ADMIN);
                case "ROLE_GURU": return hasAccess(file, FileAccess.GURU);
                case "ROLE_WALIMURID": return hasAccess(file, FileAccess.WALIMURID);
            }
        }

        return false;
    }

    private boolean hasAccess(Storage file, int accessCode) {
        if (file.getAccess() == null) return false;
        return Arrays.stream(file.getAccess())
                     .anyMatch(a -> a != null && a == accessCode);
    }
}
