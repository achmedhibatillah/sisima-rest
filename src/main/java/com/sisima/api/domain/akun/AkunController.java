package com.sisima.api.domain.akun;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.domain.akun.model.AkunGetResponse;
import com.sisima.api.domain.akun.model.AkunPostRequest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@RestController
@RequestMapping("/akun")
public class AkunController {

    private final AkunService akunService;

    @GetMapping
    public ResponseEntity<?> getAllAkun() {
        List<AkunGetResponse> response = akunService.getAllAkun();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{publicId}")
    public ResponseEntity<?> getDetailAkun(@PathVariable String publicId) {
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
    public ResponseEntity<?> addAkun(@RequestBody @Valid AkunPostRequest request) {
        AkunGetResponse createdAkun = akunService.createAkun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAkun);
    }
    
    
}
