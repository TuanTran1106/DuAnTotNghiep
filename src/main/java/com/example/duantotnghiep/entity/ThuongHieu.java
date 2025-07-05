package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "thuong_hieu")
public class ThuongHieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_thuong_hieu")
    private String tenThuongHieu;

    @Column(name = "quoc_gia")
    private String quocGia;
}
