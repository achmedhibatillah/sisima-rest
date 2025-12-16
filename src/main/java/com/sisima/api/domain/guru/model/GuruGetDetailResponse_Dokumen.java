package com.sisima.api.domain.guru.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuruGetDetailResponse_Dokumen implements GuruGetDetailResponse {

    private Long nik;

    @JsonProperty(value = "file_ktp")
    private String fileKtp;

    @JsonProperty(value = "file_npwp")
    private String fileNpwp;

}
