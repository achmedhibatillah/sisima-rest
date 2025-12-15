package com.sisima.api.domain.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileGetResponse { 

    private String title;

    private String path;

    private String ownerId;
    
}
