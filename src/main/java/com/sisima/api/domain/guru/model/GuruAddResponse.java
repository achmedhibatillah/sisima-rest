package com.sisima.api.domain.guru.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuruAddResponse {

    private String status;
    
    @JsonProperty("id")
    private String publicId;
    
}
