package com.sisima.api.domain.guru;

import jakarta.persistence.*;

public class Guru {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
}
