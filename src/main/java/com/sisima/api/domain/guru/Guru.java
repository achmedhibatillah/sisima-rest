package com.sisima.api.domain.guru;

import java.time.LocalDate;

import com.sisima.api.domain.akun.Akun;
import com.sisima.api.domain.storage.Storage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "guru")
@Getter
@Setter
@NoArgsConstructor
public class Guru {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", length = 26, nullable = false, unique = true)
    private String publicId;

    @Column(name = "nik", unique = true)
    private Long nik;

    @Column(name = "nip", unique = true)
    private Long nip;

    @Column(name = "npwp", unique = true)
    private Long npwp;

    @Column(name = "nama")
    private String nama;

    @Column(name = "gelar_depan", length = 20)
    private String gelarDepan;

    @Column(name = "gelar_belakang", length = 20)
    private String gelarBelakang;

    @Column(name = "jenis_kelamin")
    private Boolean jenisKelamin;

    @Column(name = "tempat_lahir", length = 30)
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "masuk")
    private int masuk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foto", referencedColumnName = "id")
    private Storage foto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_ktp", referencedColumnName = "id")
    private Storage fileKtp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_npwp", referencedColumnName = "id")
    private Storage fileNpwp;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "akun_id", nullable = false)
    private Akun akun; 
}
