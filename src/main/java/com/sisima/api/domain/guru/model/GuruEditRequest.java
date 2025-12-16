package com.sisima.api.domain.guru.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuruEditRequest {


    @JsonProperty(value = "nama", required = true)
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Harus berupa huruf.")
    @Size(max = 100, message = "Nama maksimal 100 karakter.")
    private String nama;

    @JsonProperty(value = "gelar_depan", required = true)
    @Pattern(regexp = "^[A-Za-z\\s,.]*$", message = "Harus berupa huruf.")
    @Size(max = 20, message = "Maksimal 20 karakter.")
    private String gelarDepan;

    @JsonProperty(value = "gelar_belakang", required = true)
    @Pattern(regexp = "^[A-Za-z\\s,.]*$", message = "Harus berupa huruf.")
    @Size(max = 20, message = "Maksimal 20 karakter.")
    private String gelarBelakang;

    @JsonProperty(value = "jenis_kelamin", required = true)
    @NotNull(message = "Jenis kelamin wajib diisi.")
    private Boolean jenisKelamin;

    @JsonProperty(value = "tempat_lahir", required = true)
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Harus berupa huruf.")
    @Size(max = 50, message = "Maksimal 50 karakter.")
    private String tempatLahir;

    @JsonProperty(value = "tanggal_lahir", required = true)
    @NotNull(message = "Tanggal lahir wajib diisi.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tanggalLahir;

    @JsonProperty(value = "alamat", required = true)
    @Size(max = 255, message = "Maksimal 255 karakter.")
    private String alamat;
}
