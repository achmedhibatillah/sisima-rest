package com.sisima.api.domain.akun.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AkunGetResponse {

    @JsonProperty("id")
    private String publicId; 

    private String email;

    private String role;

    private LocalDateTime createdAt;

    public Map<String, Object> getTimestamp() {
        Map<String, Object> createdAtMap = new HashMap<>();
        createdAtMap.put("day", createdAt.getDayOfWeek().toString());
        createdAtMap.put("date", createdAt.toLocalDate().toString());
        createdAtMap.put("time", createdAt.toLocalTime().toString());

        Map<String, Object> timestamp = new HashMap<>();
        timestamp.put("created_at", createdAtMap);
        return timestamp;
    }
    
}
