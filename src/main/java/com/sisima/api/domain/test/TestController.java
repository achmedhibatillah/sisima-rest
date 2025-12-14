package com.sisima.api.domain.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sisima.api.security.JwtUserPrincipal;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    @GetMapping
    public ResponseEntity<?> indexTest(Authentication auth) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");

        JwtUserPrincipal user = (JwtUserPrincipal) auth.getPrincipal();
        Map<String, String> userMap = new HashMap<>();
        userMap.put("id", user.getPublicId());
        userMap.put("email", user.getUsername());
        userMap.put("role", user.getPassword());
        response.put("user", userMap);

        return ResponseEntity.ok(null);
    }
    
}
