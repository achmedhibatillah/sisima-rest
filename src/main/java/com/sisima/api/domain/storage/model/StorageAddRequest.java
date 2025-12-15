package com.sisima.api.domain.storage.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageAddRequest {

    private MultipartFile file;
    private Integer[] access;
    
}

