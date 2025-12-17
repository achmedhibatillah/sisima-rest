-- V1__initial_schema.sql

-- 1. TABLE: AKUN
CREATE TABLE akun (
    id SERIAL PRIMARY KEY,
    public_id CHAR(26) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role SMALLINT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP
);

INSERT INTO akun (public_id, email, password, role) VALUES
(
    '01KCNNCJF2MVTMHM7VB25F5Q0J',
    'root@sisima.com',
    '$2a$10$6jehAX3Wdgd7QA1G/xF9xuLCj4EX5CRmQeA0OFZOSuIwdFRt6eviW',
    0
);

-- 2. TABLE: REFRESH_TOKEN
CREATE TABLE refresh_token (
    id SERIAL PRIMARY KEY,
    owner_public_id CHAR(26) NOT NULL,
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (owner_public_id) REFERENCES akun(public_id)
);

-- 3. TABLE: STORAGE
CREATE TABLE storage (
    id SERIAL PRIMARY KEY,
    public_id CHAR(26) NOT NULL UNIQUE,
    title VARCHAR(50),
    type INT,
    access INT[],
    path VARCHAR(255),
    owner_id CHAR(26),
    FOREIGN KEY (owner_id) REFERENCES akun(public_id) ON DELETE CASCADE
);

-- 4. TABLE: GURU
CREATE TABLE guru (
    id SERIAL PRIMARY KEY,
    public_id CHAR(26) NOT NULL UNIQUE,
    nik BIGINT UNIQUE,
    nip BIGINT UNIQUE,
    npwp BIGINT UNIQUE,
    nama VARCHAR(255),
    gelar_depan VARCHAR(20),
    gelar_belakang VARCHAR(20),
    jenis_kelamin BOOLEAN,
    tempat_lahir VARCHAR(30),
    tanggal_lahir DATE,
    alamat VARCHAR(255),
    masuk SMALLINT,
    foto INT,
    file_ktp INT,
    file_npwp INT,
    akun_id INT NOT NULL,
    FOREIGN KEY (foto) REFERENCES storage(id) ON DELETE SET NULL,
    FOREIGN KEY (file_ktp) REFERENCES storage(id) ON DELETE SET NULL,
    FOREIGN KEY (file_npwp) REFERENCES storage(id) ON DELETE SET NULL,
    FOREIGN KEY (akun_id) REFERENCES akun(id) ON DELETE CASCADE
);