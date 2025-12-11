package com.sisima.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sisima.api.model.AkunResponse;
import com.sisima.api.service.AkunService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@RestController
@RequestMapping("/akun")
public class AkunController {

    private final AkunService akunService;

    @GetMapping
    public List<AkunResponse> getAllAkun() {
        return akunService.getAllAkun();
    }
    
    @GetMapping("/{publicId}")
    public ResponseEntity<?> getDetailAkun(@PathVariable String publicId) {
        try {
            AkunResponse response = akunService.getDetailAkunByPublicId(publicId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("404")) {
                return ResponseEntity.status(404).body(null);
            }
            return ResponseEntity.status(500).body(null);
        }
    }
    
}
