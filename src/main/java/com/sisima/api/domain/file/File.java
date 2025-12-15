package com.sisima.api.domain.file;

import com.sisima.api.enums.FileTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, length = 26)
    private String publicId;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private FileTypeEnum type;

    @Column(name = "access", columnDefinition = "integer[]")
    private Integer[] access;

    @Column(name = "path", length = 255)
    private String path;

    @Column(name = "owner_id", length = 26)
    private String ownerId;
    
}
