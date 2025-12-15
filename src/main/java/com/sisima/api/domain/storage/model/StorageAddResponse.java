package com.sisima.api.domain.storage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StorageAddResponse {

    @JsonProperty("id")
    private String publicId;

    private Integer[] access;
    
}
