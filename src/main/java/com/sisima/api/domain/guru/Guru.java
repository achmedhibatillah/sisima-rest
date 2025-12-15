package com.sisima.api.domain.guru;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "guru")
public class Guru {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "public_id")
    private String publicId;
     
}
