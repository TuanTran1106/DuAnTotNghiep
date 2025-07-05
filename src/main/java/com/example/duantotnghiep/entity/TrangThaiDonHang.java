package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trang_thai_don_hang")
public class TrangThaiDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_trang_thai")
    private String tenTrangThai;
}
