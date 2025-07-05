package com.example.duantotnghiep.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gio_hang")
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_nguoi_dung")
    private NguoiDung nguoiDung;

    @Column(name = "id_san_pham_chi_tiet")
    private Integer idSanPhamChiTiet;

    @Column(name = "ngay_them")
    private LocalDateTime ngayThem;
}

