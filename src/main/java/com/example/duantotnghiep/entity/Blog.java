package com.example.duantotnghiep.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tieuDe;

    @Column(columnDefinition = "TEXT")
    private String noiDung;

    private String hinhAnh;

    private LocalDateTime ngayDang;
}