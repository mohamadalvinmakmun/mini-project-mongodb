package com.example.mahasiswa.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "mahasiswa")
public class Mahasiswa {
    @Id
    private Long id;
    private String name;
    private String age;
    private String major;

    @NotNull(message = "Gender tidak boleh kosong")
    @Min(value = 1, message = "Gender harus 1 (Laki-laki) atau 2 (Perempuan)")
    @Max(value = 2, message = "Gender harus 1 (Laki-laki) atau 2 (Perempuan)")
    private Integer gender;
}