package com.example.mahasiswa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.mahasiswa.model.Mahasiswa;
import com.example.mahasiswa.service.MahasiswaService;

@RestController
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService service;

    @PostMapping
    public Mahasiswa create(@RequestBody Mahasiswa mhs) {
        return service.create(mhs);
    }

    @GetMapping
    public List<Mahasiswa> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mahasiswa getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Mahasiswa update(@PathVariable Long id, @RequestBody Mahasiswa mhs) {
        return service.update(id, mhs);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return service.delete(id);
    }
}