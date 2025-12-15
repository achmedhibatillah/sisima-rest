package com.sisima.api.domain.guru.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuruGetPaginatedResponse {

    @JsonProperty("id")
    private String publicId;

    private Long nik;

    private Long nip;

    private String nama;

    @JsonProperty("gelar_depan")
    private String gelarDepan;

    @JsonProperty("gelar_belakang")
    private String gelarBelakang;

    @JsonProperty("jenis_kelamin")
    private Boolean jenisKelamin;

    private String foto;

    @JsonProperty("akun_id")
    private String akunId; 
    
}
