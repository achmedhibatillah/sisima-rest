package com.sisima.api.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sisima.api.enums.AkunRoleEnum;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AkunResponse {

    @JsonProperty("id")
    private String publicId;

    private String email;

    private AkunRoleEnum role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd MMMM yyyy")
    private LocalDateTime createdAt;
    
}
