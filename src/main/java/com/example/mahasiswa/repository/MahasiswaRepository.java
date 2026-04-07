package com.example.mahasiswa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.mahasiswa.model.Mahasiswa;

public interface MahasiswaRepository extends MongoRepository<Mahasiswa, Long> {
}