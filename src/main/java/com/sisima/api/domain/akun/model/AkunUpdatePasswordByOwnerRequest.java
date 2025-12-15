package com.sisima.api.domain.akun.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AkunUpdatePasswordByOwnerRequest {

    @JsonProperty("id")
    @NotBlank
    private String publicId;
    
    @JsonProperty("old_password")
    @NotBlank
    private String oldPassword;

    @JsonProperty("new_password")
    @NotBlank
    @Size(min = 6, max = 20)
    private String newPassword;

}
