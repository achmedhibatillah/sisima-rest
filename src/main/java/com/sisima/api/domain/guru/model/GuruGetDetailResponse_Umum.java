package com.sisima.api.domain.guru.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuruGetDetailResponse_Umum implements GuruGetDetailResponse {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @JsonProperty(value = "id")
    private String publicId;

    private Long nip;

    private String email;

    private String nama;
    
    @JsonProperty(value = "gelar_depan")
    private String gelarDepan;

    @JsonProperty(value = "gelar_belakang")
    private String gelarBelakang;

    @JsonProperty(value = "jenis_kelamin")
    private boolean jenisKelamin;

    @JsonProperty(value = "tempat_lahir")
    private String tempatLahir;

    @JsonProperty(value = "tanggal_lahir")
    private LocalDate tanggalLahir;
    public String getTanggalLahir() {
        if (this.tanggalLahir == null) {
            return null;
        }
        return this.tanggalLahir.format(DATE_FORMAT);
    }

    private String alamat;

    // private int masuk;

    private String foto;
    
}
