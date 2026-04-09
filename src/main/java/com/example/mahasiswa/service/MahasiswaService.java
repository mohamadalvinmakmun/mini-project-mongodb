package com.example.mahasiswa.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.mahasiswa.model.Mahasiswa;
import com.example.mahasiswa.repository.MahasiswaRepository;

@Service
public class MahasiswaService {

    @Autowired
    private MahasiswaRepository repo;

    @Autowired
    private CounterService counterService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String ALL_KEY = "mahasiswa::all";

    public Mahasiswa create(Mahasiswa mhs) {
        validateGender(mhs.getGender());

        mhs.setId(counterService.getNextSequence("mahasiswa_seq"));
        Mahasiswa saved = repo.save(mhs);

        redisTemplate.delete(ALL_KEY);
        redisTemplate.delete("mahasiswa::" + saved.getId());

        return saved;
    }

    public List<Mahasiswa> getAll() {

        Object cache = redisTemplate.opsForValue().get(ALL_KEY);

        if (cache != null && cache instanceof List) {
            System.out.println("GET ALL dari REDIS");
            return (List<Mahasiswa>) cache;
        }

        List<Mahasiswa> data = repo.findAll();

        redisTemplate.opsForValue().set(ALL_KEY, data, 10, TimeUnit.MINUTES);

        System.out.println("GET ALL dari MONGODB");
        return data;
    }

    public Mahasiswa getById(Long id) {

        String key = "mahasiswa::" + id;

        Object cache = redisTemplate.opsForValue().get(key);

        if (cache != null && cache instanceof Mahasiswa) {
            System.out.println("GET BY ID dari REDIS");
            return (Mahasiswa) cache;
        }

        Mahasiswa data = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        redisTemplate.opsForValue().set(key, data, 10, TimeUnit.MINUTES);

        System.out.println("GET BY ID dari MONGODB");
        return data;
    }

    public Mahasiswa update(Long id, Mahasiswa mhs) {
        validateGender(mhs.getGender());

        Mahasiswa existing = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Data tidak ditemukan"));

        existing.setName(mhs.getName());
        existing.setAge(mhs.getAge());
        existing.setMajor(mhs.getMajor());
        existing.setGender(mhs.getGender());

        Mahasiswa updated = repo.save(existing);

        redisTemplate.delete(ALL_KEY);
        redisTemplate.delete("mahasiswa::" + id);

        return updated;
    }

    public String delete(Long id) {

        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Data tidak ditemukan");
        }

        repo.deleteById(id);

        redisTemplate.delete(ALL_KEY);
        redisTemplate.delete("mahasiswa::" + id);

        return "Deleted";
    }

    // Method validasi gender
    private void validateGender(Integer gender) {
        if (gender == null || (gender != 1 && gender != 2)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Gender harus 1 (Laki-laki) atau 2 (Perempuan)"
            );
        }
    }
}