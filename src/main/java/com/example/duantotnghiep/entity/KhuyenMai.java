package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_khuyen_mai")
    private String maKhuyenMai;

    @Column(name = "ten_chuong_trinh")
    private String tenChuongTrinh;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "kieu_giam")
    private Boolean kieuGiam;

    @Column(name = "muc_giam_gia")
    private BigDecimal mucGiamGia;

    @Column(name = "trang_thai")
    private Integer trangThai;
}

