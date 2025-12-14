package com.sisima.api.domain.akun.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AkunGetResponse {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @JsonProperty("id")
    private String publicId; 

    private String email;

    private String role;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonProperty("timestamp")
    public Map<String, Object> getTimestamp() {
        Map<String, Object> createdAtMap = new LinkedHashMap<>();
        createdAtMap.put("date", createdAt.format(DATE_FORMAT));
        createdAtMap.put("time", createdAt.format(TIME_FORMAT));

        Map<String, Object> updatedAtMap = new LinkedHashMap<>();
        updatedAtMap.put("date", updatedAt.format(DATE_FORMAT));
        updatedAtMap.put("time", updatedAt.format(TIME_FORMAT));

        Map<String, Object> timestamp = new LinkedHashMap<>();
        timestamp.put("created_at", createdAtMap);
        timestamp.put("updated_at", updatedAtMap);
        return timestamp;
    }
    
}
