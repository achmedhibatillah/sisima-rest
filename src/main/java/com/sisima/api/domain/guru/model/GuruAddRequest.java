package com.sisima.api.domain.guru.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuruAddRequest {

    @NotBlank(message = "Tidak boleh kosong.")
    @Email(message = "Email tidak valid.")
    private String email;

    @NotBlank(message = "Tidak boleh kosong.")
    @Size(min = 6, max = 20, message = "Panjang harus 6-20 karakter.")
    private String password;
    
}
