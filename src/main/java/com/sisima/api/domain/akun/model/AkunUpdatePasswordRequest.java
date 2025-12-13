package com.sisima.api.domain.akun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AkunUpdatePasswordRequest {

    @NotNull
    @Size(min = 6, max = 20)
    @JsonProperty("password")
    private String newPassword;
    
}
